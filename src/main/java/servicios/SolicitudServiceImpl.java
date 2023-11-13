package servicios;

import daos.EventoDAO;
import daos.SolicitudDAO;
import daos.SuscripcionDAO;
import daos.UsuarioDAO;
import entidades.Evento;
import entidades.Solicitud;
import entidades.Suscripcion;
import entidades.Usuario;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import validaciones.CodigosRespuesta;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudServiceImpl implements SolicitudService{

    @Autowired
    private SuscripcionDAO suscripcionDAO;
    @Autowired
    private SolicitudDAO solicitudDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private EventoDAO eventoDAO;

    public List<Solicitud> buscarTodos(String idUsuario, String idEvento, String fechaSolicitud){
        List<Solicitud> solicitud = new ArrayList<Solicitud>();
        solicitud = solicitudDAO.buscarTodos(idUsuario != null ? Long.parseLong(idUsuario) : null, idEvento != null ? Long.parseLong(idEvento) : null, fechaSolicitud);
        return solicitud;
    }

    public Solicitud crear(Solicitud solicitud) throws AccionException{
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
        solicitud.setFechaSolicitud(fechaFormateada);

        Usuario usuarioSus = usuarioDAO.getById(solicitud.getUsuario().getId());
        Evento eventoSus = eventoDAO.getById(solicitud.getEvento().getId());
        List<Suscripcion> suscripcionesExistentes = suscripcionDAO.findByUsuarioAndEvento(usuarioSus, eventoSus);
        List<Solicitud> solicitudesExistentes = solicitudDAO.findByUsuarioAndEvento(usuarioSus, eventoSus);

        if (!suscripcionesExistentes.isEmpty() || !solicitudesExistentes.isEmpty()) {
            throw new AccionException(CodigosRespuesta.EXISTE_SUSCRIPCION.getCode(), CodigosRespuesta.EXISTE_SUSCRIPCION.getMsg());
        }
        if (eventoSus.getEstado().equals("CERRADO")) {
            throw new AccionException(CodigosRespuesta.EVENTO_CERRADO.getCode(), CodigosRespuesta.EVENTO_CERRADO.getMsg());
        }
        if (eventoSus.getNumAsistentes() == eventoSus.getNumInscritos()) {
            throw new AccionException(CodigosRespuesta.PLAZAS_CUBIERTAS.getCode(), CodigosRespuesta.PLAZAS_CUBIERTAS.getMsg());
        }
        return solicitudDAO.save(solicitud);
    }

    public void aceptarSolicitud(Solicitud solicitud, Long id) throws AccionException{
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);

        Suscripcion suscripcionNueva = new Suscripcion(solicitud.getEvento(), solicitud.getUsuario(), fechaFormateada);
        Evento eventoSus = eventoDAO.getById(suscripcionNueva.getEvento().getId());

        if (eventoSus.getNumAsistentes() == eventoSus.getNumInscritos()) {
            throw new AccionException(CodigosRespuesta.PLAZAS_CUBIERTAS.getCode(), CodigosRespuesta.PLAZAS_CUBIERTAS.getMsg());
        }

        eventoSus.setNumInscritos(eventoSus.getNumInscritos() + 1);
        suscripcionDAO.save(suscripcionNueva);

        Optional<Solicitud> solicitudBorrar = solicitudDAO.findById(id);
        solicitudDAO.delete(solicitudBorrar.get());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Solicitud> buscarPorId(Long id) {
        return solicitudDAO.findById(id);
    }

    public void eliminar(Long id) throws AccionException{
        Optional<Solicitud> solicitud = solicitudDAO.findById(id);
        solicitudDAO.delete(solicitud.get());
    }
}
