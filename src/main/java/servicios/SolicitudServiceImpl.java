package servicios;

import autenticacion.Mail;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import validaciones.CodigosRespuesta;
import validaciones.Constantes;

import javax.mail.MessagingException;
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
    @Autowired
    MailService mailService;

    public List<Solicitud> buscarTodos(String idUsuario, String idEvento, String fechaSolicitud){
        List<Solicitud> solicitud = new ArrayList<Solicitud>();
        solicitud = solicitudDAO.buscarTodos(idUsuario != null ? Long.parseLong(idUsuario) : null, idEvento != null ? Long.parseLong(idEvento) : null, fechaSolicitud);
        return solicitud;
    }

    public Solicitud crear(Solicitud solicitud) throws AccionException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
        solicitud.setFechaSolicitud(fechaFormateada);

        Optional<Usuario> usuarioSus = usuarioDAO.findById(solicitud.getUsuario().getId());
        Optional<Evento> eventoSus = eventoDAO.findById(solicitud.getEvento().getId());
        List<Suscripcion> suscripcionesExistentes = suscripcionDAO.findByUsuarioAndEvento(usuarioSus.get(), eventoSus.get());
        List<Solicitud> solicitudesExistentes = solicitudDAO.findByUsuarioAndEvento(usuarioSus.get(), eventoSus.get());

        if (!suscripcionesExistentes.isEmpty() || !solicitudesExistentes.isEmpty()) {
            throw new AccionException(CodigosRespuesta.EXISTE_SUSCRIPCION.getCode(), CodigosRespuesta.EXISTE_SUSCRIPCION.getMsg());
        }
        else if (eventoSus.get().getEstado().equals("CERRADO")) {
            throw new AccionException(CodigosRespuesta.EVENTO_CERRADO.getCode(), CodigosRespuesta.EVENTO_CERRADO.getMsg());
        }
        else if (eventoSus.get().getNumAsistentes() == eventoSus.get().getNumInscritos()) {
            throw new AccionException(CodigosRespuesta.PLAZAS_CUBIERTAS.getCode(), CodigosRespuesta.PLAZAS_CUBIERTAS.getMsg());
        }
        else if(!usuarioSus.get().getLogin().equals(loginUsuarioSistema) && !("admin".equals(loginUsuarioSistema))){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }
        return solicitudDAO.save(solicitud);
    }

    public List<Evento> eventosSolicitados(String idUsuario){
        Long id = idUsuario != null ? Long.parseLong(idUsuario) : null;
        Usuario usuarioSus = usuarioDAO.findById(id).get();
        List<Solicitud> solicitudes = solicitudDAO.findByUsuario(usuarioSus);
        List<Evento> eventos = new ArrayList<Evento>();
        for (Solicitud solicitud : solicitudes) {
            Long idEvento = solicitud.getEvento().getId();
            Evento evento = eventoDAO.getById(idEvento);
            eventos.add(evento);
        }
        return eventos;
    }

    public List<Usuario> usuariosSolicitados(String idEvento){
        Long id = idEvento != null ? Long.parseLong(idEvento) : null;
        Evento eventoSus = eventoDAO.findById(id).get();
        List<Solicitud> solicitudes = solicitudDAO.findByEvento(eventoSus);
        List<Usuario> usuarios = new ArrayList<Usuario>();
        for (Solicitud solicitud : solicitudes) {
            Long idUsuario = solicitud.getUsuario().getId();
            Usuario usuario = usuarioDAO.getById(idUsuario);
            usuarios.add(usuario);
        }
        return usuarios;
    }

    public void aceptarSolicitud(Solicitud solicitud, Long id, final String idioma) throws AccionException, MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);

        Suscripcion suscripcionNueva = new Suscripcion(solicitud.getEvento(), solicitud.getUsuario(), fechaFormateada);
        Usuario usuarioSus = usuarioDAO.getById(suscripcionNueva.getUsuario().getId());
        Evento eventoSus = eventoDAO.getById(suscripcionNueva.getEvento().getId());

        if (eventoSus.getNumAsistentes() == eventoSus.getNumInscritos()) {
            throw new AccionException(CodigosRespuesta.PLAZAS_CUBIERTAS.getCode(), CodigosRespuesta.PLAZAS_CUBIERTAS.getMsg());
        }
        else if(!solicitud.getEvento().getUsuario().getLogin().equals(loginUsuarioSistema) && !("admin".equals(loginUsuarioSistema))){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }

        String emailDestino = usuarioSus.getEmail();
        String nombreEvento = eventoSus.getNombre();
        //Envio del correo electrónico
        final String fechaEmail = mailService.fechaCorreo(idioma);
        final String asuntoEmail = mailService.asuntoSuscripcion(idioma);
        final String mensajeEmail = mailService.mensajeSuscripcionEvento(idioma, nombreEvento);
        final String contenidoEmail = mailService.contenidoCorreo(fechaEmail, mensajeEmail, idioma);

        final Mail email = new Mail(Constantes.EMISOR_EMAIL, emailDestino, asuntoEmail,
                contenidoEmail, Constantes.TIPO_CONTENIDO, null);

        final String result = mailService.enviarCorreo(email);

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
        if (solicitud.isPresent()) {
            solicitudDAO.delete(solicitud.get());
        } else {
            throw new AccionException(CodigosRespuesta.COMENTARIO_NO_EXISTE.getCode(), CodigosRespuesta.COMENTARIO_NO_EXISTE.getMsg());
        }
    }
}
