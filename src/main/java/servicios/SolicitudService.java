package servicios;

import entidades.Evento;
import entidades.Solicitud;
import entidades.Suscripcion;
import entidades.Usuario;
import excepciones.AccionException;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface SolicitudService{
    public List<Solicitud> buscarTodos(String idUsuario, String idEvento, String fechaSolicitud);
    public Optional<Solicitud> buscarPorId(Long id);
    public Solicitud crear(Solicitud solicitud) throws AccionException;
    public List<Evento> eventosSolicitados(String idUsuario);
    public List<Usuario> usuariosSolicitados(String idEvento);
    public void aceptarSolicitud(Solicitud solicitud, Long id, final String idioma) throws AccionException, MessagingException;
    public void eliminar(Long id) throws AccionException;
}
