package servicios;

import entidades.Suscripcion;
import entidades.Usuario;
import entidades.Evento;
import excepciones.AccionException;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface SuscripcionService {
    public List<Suscripcion> buscarTodos(String idUsuario, String idEvento, String fechaSuscripcion);
    public Optional<Suscripcion> buscarPorId(Long id);
    public List<Evento> eventosSuscritos(String idUsuario);
    public List<Usuario> usuariosSuscritos(String idEvento);
    public Suscripcion crear(Suscripcion suscripcion, final String idioma) throws AccionException, MessagingException;
    public boolean eventoPublico(Suscripcion suscripcion);
    public void eliminar(Long id) throws AccionException;
}
