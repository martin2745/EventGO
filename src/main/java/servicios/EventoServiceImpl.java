package servicios;

import daos.*;
import entidades.*;
import excepciones.AccionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import validaciones.CodigosRespuesta;
import servicios.EventoService;
import java.util.stream.Collectors;

@Service
public class EventoServiceImpl implements EventoService{

    @Autowired
    private EventoDAO eventoDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private ComentarioDAO comentarioDAO;

    @Autowired
    private SolicitudDAO solicitudDAO;

    @Autowired
    private SuscripcionDAO suscripcionDAO;

    public List<Evento> buscarTodos(String nombre, String descripcion, String tipoAsistencia, String numAsistentes, String estado, String fechaEvento, String direccion, String emailContacto, String telefonoContacto, String idCategoria, String idUsuario, String borradoLogico){
        List<Evento> resultado = new ArrayList<Evento>();
        resultado = eventoDAO.buscarTodos(nombre, descripcion, tipoAsistencia, numAsistentes != null ? Integer.parseInt(numAsistentes) : null, estado, fechaEvento, direccion, emailContacto, telefonoContacto, idCategoria != null ? Long.parseLong(idCategoria) : null, idUsuario != null ? Long.parseLong(idUsuario) : null, borradoLogico);
        return resultado;
    }
    public Evento crear(Evento evento) throws AccionException{
        evento.setNumInscritos(0);
        evento.setEstado("ABIERTO");
        List<Evento> eventosFecha = eventoDAO.findByFechaEvento(evento.getFechaEvento());
        for (Evento eventoExistente : eventosFecha) {
            //Ya existe un evento en esa fecha, en la misma categoria con ese mismo nombre en el sistema
            if (eventoExistente.getNombre().equals(evento.getNombre()) && eventoExistente.getCategoria().getId().equals(evento.getCategoria().getId())) {
                throw new AccionException(CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getCode(),
                        CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getMsg());
            }
        }
        /*Enviar correo a los seguidores de que existe un nuevo evento en el sistema*/
        return eventoDAO.save(evento);
    }
    public Evento modificar(Long id, Evento evento) throws AccionException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        List<Usuario> usuarioEvento = usuarioDAO.findUsuarioByLoginContaining(loginUsuarioSistema);
        Optional<Evento> eventoOptional = eventoDAO.findById(id);
        List<Evento> eventoNombre = eventoDAO.findEventoByNombreContaining(evento.getNombre());

        if(!usuarioEvento.get(0).getLogin().equals(loginUsuarioSistema) && !"admin".equals(loginUsuarioSistema)){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }
        else if (this.nombreEventoYaExiste(id, evento, eventoNombre)) {
            throw new AccionException(CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getCode(), CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getMsg());
        }

        if (eventoOptional.isPresent()) {
            return eventoDAO.save(evento);
        } else {
            throw new AccionException(CodigosRespuesta.EVENTO_NO_EXISTE.getCode(), CodigosRespuesta.EVENTO_NO_EXISTE.getMsg());
        }
    }
    public void eliminar(Long id) throws AccionException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        List<Usuario> usuarioEvento = usuarioDAO.findUsuarioByLoginContaining(loginUsuarioSistema);
        Optional<Evento> evento = eventoDAO.findById(id);
        if (evento.isPresent()) {
            if(!usuarioEvento.get(0).getLogin().equals(loginUsuarioSistema) && !"admin".equals(loginUsuarioSistema)){
                throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
            }

            List<Comentario> comentarios = comentarioDAO.findByUsuarioId(id);
            List<Solicitud> solicitudes = solicitudDAO.findByUsuarioId(id);
            List<Suscripcion> suscripcion = suscripcionDAO.findByUsuarioId(id);

            if(!comentarios.isEmpty() || !solicitudes.isEmpty() || !suscripcion.isEmpty()){
                evento.get().setEstado("CERRADO");
                evento.get().setBorradoLogico("1");
                eventoDAO.save(evento.get());
            }else {
                eventoDAO.delete(evento.get());
            }
        } else {
            throw new AccionException(CodigosRespuesta.EVENTO_NO_EXISTE.getCode(), CodigosRespuesta.EVENTO_NO_EXISTE.getMsg());
        }
    }
    public boolean nombreEventoYaExiste(Long id, Evento eventoEditar, List <Evento> eventoNombre){
        boolean toret = false;

        if(!eventoNombre.isEmpty()){
            for(Evento evento : eventoNombre){
                if (id != evento.getId() && eventoEditar.getFechaEvento().equals(evento.getFechaEvento())){
                    toret = true;
                }
            }
        }

        return toret;
    }
    public Optional<Evento> buscarPorId(Long id){
        return eventoDAO.findById(id);
    }

    public List<Evento> eventosCategoria(Long idCategoria){
        List<Evento> resultado = new ArrayList<Evento>();
        resultado = eventoDAO.findByCategoriaId (idCategoria);
        return resultado;
    }

    public List<Evento> eventosCategoriaValidos(Long idCategoria){
        List<Evento> resultado = new ArrayList<Evento>();
        List<Evento> toret = new ArrayList<Evento>();
        resultado = eventoDAO.findByCategoriaId (idCategoria);
        LocalDate fechaActual = LocalDate.now();
        for(Evento e: resultado){
            LocalDate fechaEvento = LocalDate.parse(e.getFechaEvento());
            if(fechaEvento.isAfter(fechaActual)){
                if(e.getEstado().equals("ABIERTO") && e.getNumAsistentes() > e.getNumInscritos()){
                    toret.add(e);
                }
            }
        }
        return toret;
    }
    public Optional<Evento> buscarPorNombreEvento(String nombre){
        return eventoDAO.findFirstByNombre(nombre);
    }
}
