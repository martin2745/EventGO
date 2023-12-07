package servicios;

import daos.AmistadDAO;
import daos.UsuarioDAO;
import entidades.*;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import validaciones.CodigosRespuesta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AmistadServiceImpl implements AmistadService{
    @Autowired
    private AmistadDAO amistadDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;

    public List<Amistad> buscarTodos(String idGerente, String idSeguidor, String nombreGerente){
        List<Amistad> amistades = new ArrayList<Amistad>();
        amistades = amistadDAO.buscarTodos(idGerente != null ? Long.parseLong(idGerente) : null, idSeguidor != null ? Long.parseLong(idSeguidor) : null, nombreGerente);
        return amistades;
    }
    public Amistad crear(Amistad amistad) throws AccionException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        Optional<Usuario> gerente = usuarioDAO.findById(amistad.getGerente().getId());
        Optional<Usuario> seguidor = usuarioDAO.findById(amistad.getSeguidor().getId());
        List<Amistad> amistadExistentes = amistadDAO.findByGerenteAndSeguidor(gerente.get(), seguidor.get());

        if (gerente == null) {
            throw new AccionException(CodigosRespuesta.GERENTE_NO_EXISTE.getCode(), CodigosRespuesta.GERENTE_NO_EXISTE.getMsg());
        }
        else if (seguidor == null) {
            throw new AccionException(CodigosRespuesta.SEGUIDOR_NO_EXISTE.getCode(), CodigosRespuesta.SEGUIDOR_NO_EXISTE.getMsg());
        }
        else if (!amistadExistentes.isEmpty()) {
            throw new AccionException(CodigosRespuesta.AMISTAD_YA_EXISTE.getCode(), CodigosRespuesta.AMISTAD_YA_EXISTE.getMsg());
        }
        else if(!(gerente.get().getLogin().equals(loginUsuarioSistema)) && !(seguidor.get().getLogin().equals(loginUsuarioSistema)) && !("admin".equals(loginUsuarioSistema))){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }

        return amistadDAO.save(amistad);
    }

    public void eliminar(Long id) throws AccionException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        Optional<Amistad> amistad = amistadDAO.findById(id);

        if(!(amistad.get().getSeguidor().getLogin().equals(loginUsuarioSistema)) && !("admin".equals(loginUsuarioSistema))){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }

        if (amistad.isPresent()) {
            amistadDAO.delete(amistad.get());
        } else {
            throw new AccionException(CodigosRespuesta.AMISTAD_NO_EXISTE.getCode(), CodigosRespuesta.AMISTAD_NO_EXISTE.getMsg());
        }
    }
}
