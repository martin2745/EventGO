package servicios;

import daos.EventoDAO;
import daos.UsuarioDAO;
import daos.ComentarioDAO;
import entidades.*;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import validaciones.CodigosRespuesta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ComentarioServiceImpl implements ComentarioService{
    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private EventoDAO eventoDAO;
    @Autowired
    private ComentarioDAO comentarioDAO;
    public List<Comentario> buscarTodos(Long idEvento){
        List<Comentario> comentarios = comentarioDAO.findTodos(idEvento);
        Collections.sort(comentarios, (c1, c2) -> Integer.compare(c1.getPuntuacion(), c2.getPuntuacion()));
        return comentarios;
    }

    public List<Integer> valoraciones(Long idEvento){
        List<Comentario> comentarios = comentarioDAO.findTodos(idEvento);
        List<Integer> contadorPuntuaciones = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
        for (Comentario comentario : comentarios) {
            int puntuacion = comentario.getPuntuacion();
            if (puntuacion >= 1 && puntuacion <= 5) {
                contadorPuntuaciones.set(puntuacion - 1, contadorPuntuaciones.get(puntuacion - 1) + 1);
            }
        }
        return contadorPuntuaciones;
    }

    public Comentario crear(Comentario comentario) throws AccionException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        Optional<Usuario> usuario = usuarioDAO.findById(comentario.getUsuario().getId());
        Optional<Evento> evento = eventoDAO.findById(comentario.getEvento().getId());
        List<Comentario> comentarioExistentes = comentarioDAO.findByUsuarioAndEvento(usuario.get(), evento.get());

        if (usuario == null) {
            throw new AccionException(CodigosRespuesta.USUARIO_NO_EXISTE.getCode(), CodigosRespuesta.USUARIO_NO_EXISTE.getMsg());
        }
        else if (evento == null) {
            throw new AccionException(CodigosRespuesta.EVENTO_NO_EXISTE.getCode(), CodigosRespuesta.EVENTO_NO_EXISTE.getMsg());
        }
        else if (!comentarioExistentes.isEmpty()) {
            throw new AccionException(CodigosRespuesta.COMENTARIO_YA_EXISTE.getCode(), CodigosRespuesta.COMENTARIO_YA_EXISTE.getMsg());
        }
        else if(!usuario.get().getLogin().equals(loginUsuarioSistema) && !("admin".equals(loginUsuarioSistema))){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }
        return comentarioDAO.save(comentario);
    }

    public void eliminar(Long id) throws AccionException{
        Optional<Comentario> comentario = comentarioDAO.findById(id);
        if (comentario.isPresent()) {
            comentarioDAO.delete(comentario.get());
        } else {
            throw new AccionException(CodigosRespuesta.COMENTARIO_NO_EXISTE.getCode(), CodigosRespuesta.COMENTARIO_NO_EXISTE.getMsg());
        }
    }
}
