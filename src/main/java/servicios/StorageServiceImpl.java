package servicios;

import daos.UsuarioDAO;
import entidades.Usuario;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import validaciones.CodigosRespuesta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

@Service
public class StorageServiceImpl implements StorageService{

    @Value("${media.location}")
    private String mediaLocation;

    @Autowired
    private UsuarioDAO usuarioDAO;

    private Path rootLocation;

    @Override
    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(mediaLocation);
        Files.createDirectories(rootLocation);
    }

    @Override
    public String store(MultipartFile file, Long id, String caso, String loginHeader) throws AccionException{
        try {
            if (file.isEmpty()) {
                throw new AccionException(CodigosRespuesta.ARCHIVO_VACIO.getCode(), CodigosRespuesta.ARCHIVO_VACIO.getMsg());
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String randomFilename = generateRandomFilename(originalFilename, extension);

            Path destinationFile = rootLocation.resolve(Paths.get(randomFilename))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            switch(caso){
                case "imagenUsuario":
                    Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);
                    if(!usuarioOptional.get().getLogin().equals(loginHeader) && !"admin".equals(loginHeader)){
                        throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
                    }else {
                        usuarioOptional.get().setImagenUsuario("http://localhost:8080/api/media/" + randomFilename);
                        usuarioDAO.saveAndFlush(usuarioOptional.get());
                    }
            }

            return randomFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    private String generateRandomFilename(String originalFilename, String extension) {
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String randomString = Integer.toString(randomNum);
        return originalFilename + "_" + randomString + extension;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource((file.toUri()));

            if(resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename);
        }
    }
}
