package servicios;

import daos.CategoriaDAO;
import daos.EventoDAO;
import daos.UsuarioDAO;
import entidades.Categoria;
import entidades.Evento;
import entidades.Usuario;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PostConstruct;

@Service
public class StorageServiceImpl implements StorageService{

    @Value("${media.location}")
    private String mediaLocation;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private EventoDAO eventoDAO;

    private Path rootLocation;

    @Override
    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(mediaLocation);
        Files.createDirectories(rootLocation);
    }

    @Override
    public String store(MultipartFile file, Long id, String caso) throws AccionException{
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loginUsuarioSistema = authentication.getName();

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
                    if(!usuarioOptional.get().getLogin().equals(loginUsuarioSistema) && !"admin".equals(loginUsuarioSistema)){
                        throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
                    }else {
                        usuarioOptional.get().setImagenUsuario("http://localhost:8080/api/media/" + randomFilename);
                        usuarioDAO.saveAndFlush(usuarioOptional.get());
                    }
                    break;
                case "imagenCategoria":
                    Optional<Categoria> categoriaOptional = categoriaDAO.findById(id);
                    categoriaOptional.get().setImagenCategoria("http://localhost:8080/api/media/" + randomFilename);
                    categoriaDAO.saveAndFlush(categoriaOptional.get());
                    break;
                case "imagenEvento":
                    List<Usuario> usuarioEvento = usuarioDAO.findUsuarioByLoginContaining(loginUsuarioSistema);
                    Optional<Evento> eventoOptional = eventoDAO.findById(id);
                    if(!usuarioEvento.get(0).getLogin().equals(eventoOptional.get().getUsuario().getLogin()) && !"admin".equals(loginUsuarioSistema)) {
                        throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
                    }else{
                        eventoOptional.get().setImagenEvento("http://localhost:8080/api/media/" + randomFilename);
                        eventoDAO.saveAndFlush(eventoOptional.get());
                    }
                    break;
                case "documentoEvento":
                    List<Usuario> usuarioEventoDocumento = usuarioDAO.findUsuarioByLoginContaining(loginUsuarioSistema);
                    Optional<Evento> eventoOptionalDocumento = eventoDAO.findById(id);
                    if(!usuarioEventoDocumento.get(0).getLogin().equals(eventoOptionalDocumento.get().getUsuario().getLogin()) && !"admin".equals(loginUsuarioSistema)) {
                        throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
                    }else{
                        eventoOptionalDocumento.get().setDocumentoEvento("http://localhost:8080/api/media/" + randomFilename);
                        eventoDAO.saveAndFlush(eventoOptionalDocumento.get());
                    }
                    break;
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
