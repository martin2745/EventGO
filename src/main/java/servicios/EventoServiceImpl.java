package servicios;

import daos.UsuarioDAO;
import entidades.Evento;
import entidades.Usuario;
import excepciones.AccionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import daos.EventoDAO;
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

    public List<Evento> buscarTodos(String nombre, String descripcion, String tipoAsistencia, String numAsistentes, String estado, String fechaEvento, String direccion, String emailContacto, String telefonoContacto, String idCategoria, String idUsuario){
        List<Evento> resultado = new ArrayList<Evento>();
        resultado = eventoDAO.buscarTodos(nombre, descripcion, tipoAsistencia, numAsistentes != null ? Integer.parseInt(numAsistentes) : null, estado, fechaEvento, direccion, emailContacto, telefonoContacto, idCategoria != null ? Long.parseLong(idCategoria) : null, idUsuario != null ? Long.parseLong(idUsuario) : null);
        return resultado;
    }
    public Evento crear(Evento evento) throws AccionException{
        evento.setNumInscritos(0);
        evento.setEstado("ABIERTO");
        List<Evento> eventosFecha = eventoDAO.findByFechaEvento(evento.getFechaEvento());
        for (Evento eventoExistente : eventosFecha) {
            if (eventoExistente.getNombre().equals(evento.getNombre())) {
                throw new AccionException(CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getCode(),
                        CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getMsg());
            }
        }

        return eventoDAO.save(evento);
    }
    public Evento modificar(Long id, Evento evento, String loginHeader) throws AccionException{
        List<Usuario> usuarioEvento = usuarioDAO.findUsuarioByLoginContaining(loginHeader);
        Optional<Evento> eventoOptional = eventoDAO.findById(id);
        List<Evento> eventoNombre = eventoDAO.findEventoByNombreContaining(evento.getNombre());

        if(!usuarioEvento.get(0).getLogin().equals(loginHeader) && !"admin".equals(loginHeader)){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }
        if (this.nombreEventoYaExiste(id, evento, eventoNombre)) {
            throw new AccionException(CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getCode(), CodigosRespuesta.NOMBRE_EVENTO_YA_EXISTE.getMsg());
        }
        if (eventoOptional.isPresent()) {
            return eventoDAO.save(evento);
        } else {
            throw new AccionException(CodigosRespuesta.EVENTO_NO_EXISTE.getCode(), CodigosRespuesta.EVENTO_NO_EXISTE.getMsg());
        }
    }
    public void eliminar(Long id, String loginHeader) throws AccionException{
        List<Usuario> usuarioEvento = usuarioDAO.findUsuarioByLoginContaining(loginHeader);
        Optional<Evento> evento = eventoDAO.findById(id);
        if (evento.isPresent()) {
            /*
             * Añadir excepciones de existe evento en la categoría.
             * */
            if(!usuarioEvento.get(0).getLogin().equals(loginHeader) && !"admin".equals(loginHeader)){
                throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
            }
            eventoDAO.delete(evento.get());
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
