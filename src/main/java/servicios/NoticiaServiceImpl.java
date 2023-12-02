package servicios;

import autenticacion.Mail;
import daos.NoticiaDAO;
import daos.AmistadDAO;
import entidades.Amistad;
import entidades.Evento;
import entidades.Noticia;
import entidades.Usuario;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import validaciones.CodigosRespuesta;
import validaciones.Constantes;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoticiaServiceImpl implements NoticiaService{

    @Autowired
    private NoticiaDAO noticiaDAO;
    @Autowired
    private AmistadDAO amistadDAO;
    @Autowired
    MailService mailService;

    public List<Noticia> buscarTodos(String titulo, String descripcion, String fechaNoticia, String idUsuario){
        List<Noticia> noticias = new ArrayList<Noticia>();
        noticias = noticiaDAO.buscarTodos(titulo, descripcion, fechaNoticia, idUsuario != null ? Long.parseLong(idUsuario) : null);
        return noticias;
    }
    public Noticia crear(Noticia noticia, String idioma) throws AccionException, MessagingException {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
        noticia.setFechaNoticia(fechaFormateada);

        /*
         * Hay que comprobar que el usuario para el que se crea la noticia es un ROLE_GERENTE
         * */

        List<Amistad> listaAmistades = amistadDAO.findByGerente(noticia.getUsuario());

        final String fechaEmail = mailService.fechaCorreo(idioma);
        final String contenidoEmail = mailService.contenidoCorreo(fechaEmail, noticia.getDescripcion(), idioma);

        for (Amistad a : listaAmistades) {
            final Mail email = new Mail(Constantes.EMISOR_EMAIL, a.getSeguidor().getEmail(), noticia.getTitulo(),
                    contenidoEmail, Constantes.TIPO_CONTENIDO, null);
            mailService.enviarCorreo(email);
        }

        return noticiaDAO.save(noticia);
    }
    public void eliminar(Long id) throws AccionException{
        Optional<Noticia> noticia = noticiaDAO.findById(id);
        if (noticia.isPresent()) {
            noticiaDAO.delete(noticia.get());
        } else {
            throw new AccionException(CodigosRespuesta.NOTICIA_NO_EXISTE.getCode(), CodigosRespuesta.NOTICIA_NO_EXISTE.getMsg());
        }
    }
}
