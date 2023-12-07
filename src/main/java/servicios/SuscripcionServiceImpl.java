package servicios;

import autenticacion.Mail;
import daos.UsuarioDAO;
import daos.EventoDAO;
import daos.SuscripcionDAO;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuscripcionServiceImpl implements SuscripcionService{

    @Autowired
    private SuscripcionDAO suscripcionDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private EventoDAO eventoDAO;
    @Autowired
    MailService mailService;

    public List<Suscripcion> buscarTodos(String idUsuario, String idEvento, String fechaSuscripcion){
        List<Suscripcion> suscripcion = new ArrayList<Suscripcion>();
        suscripcion = suscripcionDAO.buscarTodos(idUsuario != null ? Long.parseLong(idUsuario) : null, idEvento != null ? Long.parseLong(idEvento) : null, fechaSuscripcion);
        return suscripcion;
    }

    public List<Evento> eventosSuscritos(String idUsuario){
        Long id = idUsuario != null ? Long.parseLong(idUsuario) : null;
        Usuario usuarioSus = usuarioDAO.findById(id).get();
        List<Suscripcion> suscripciones = suscripcionDAO.findByUsuario(usuarioSus);
        List<Evento> eventos = new ArrayList<Evento>();
        for (Suscripcion suscripcion : suscripciones) {
            Long idEvento = suscripcion.getEvento().getId();
            Evento evento = eventoDAO.getById(idEvento);
            eventos.add(evento);
        }
        return eventos;
    }

    public List<Usuario> usuariosSuscritos(String idEvento){
        Long id = idEvento != null ? Long.parseLong(idEvento) : null;
        Evento eventoSus = eventoDAO.findById(id).get();
        List<Suscripcion> suscripciones = suscripcionDAO.findByEvento(eventoSus);
        List<Usuario> usuarios = new ArrayList<Usuario>();
        for (Suscripcion suscripcion : suscripciones) {
            Long idUsuario = suscripcion.getUsuario().getId();
            Usuario usuario = usuarioDAO.getById(idUsuario);
            usuarios.add(usuario);
        }
        return usuarios;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Suscripcion> buscarPorId(Long id) {
        return suscripcionDAO.findById(id);
    }

    public Suscripcion crear(Suscripcion suscripcion, final String idioma) throws AccionException, MessagingException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
        suscripcion.setFechaSuscripcion(fechaFormateada);

        Optional<Usuario> usuarioSus = usuarioDAO.findById(suscripcion.getUsuario().getId());
        Optional<Evento> eventoSus = eventoDAO.findById(suscripcion.getEvento().getId());
        List<Suscripcion> suscripcionesExistentes = suscripcionDAO.findByUsuarioAndEvento(usuarioSus.get(), eventoSus.get());

        if (!suscripcionesExistentes.isEmpty()) {
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

        String emailDestino = usuarioSus.get().getEmail();
        String nombreEvento = eventoSus.get().getNombre();
        //Envio del correo electrónico
        final String fechaEmail = mailService.fechaCorreo(idioma);
        final String asuntoEmail = mailService.asuntoSuscripcion(idioma);
        final String mensajeEmail = mailService.mensajeSuscripcionEvento(idioma, nombreEvento);
        final String contenidoEmail = mailService.contenidoCorreo(fechaEmail, mensajeEmail, idioma);

        final Mail email = new Mail(Constantes.EMISOR_EMAIL, emailDestino, asuntoEmail,
                contenidoEmail, Constantes.TIPO_CONTENIDO, null);

        final String result = mailService.enviarCorreo(email);
        eventoSus.get().setNumInscritos(eventoSus.get().getNumInscritos() + 1);
        return suscripcionDAO.save(suscripcion);
    }

    @Override
    public boolean eventoPublico(Suscripcion suscripcion){
        Evento eventoSus = eventoDAO.getById(suscripcion.getEvento().getId());
        if(eventoSus.getTipoAsistencia().equals("PUBLICO")){
            return true;
        }else{
            return false;
        }
    }

    public void eliminar(Long id) throws AccionException{
        Optional<Suscripcion> suscripcion = suscripcionDAO.findById(id);
        if (suscripcion.isPresent()) {
            //Reatamos el numInscritos en 1
            suscripcion.get().getEvento().setNumInscritos(suscripcion.get().getEvento().getNumInscritos() - 1);
            suscripcionDAO.delete(suscripcion.get());
        } else {
            throw new AccionException(CodigosRespuesta.SUSCRIPCION_NO_EXISTE.getCode(), CodigosRespuesta.SUSCRIPCION_NO_EXISTE.getMsg());
        }
    }
}
