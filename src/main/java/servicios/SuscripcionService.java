package servicios;

import entidades.Suscripcion;
import entidades.Usuario;
import entidades.Evento;
import excepciones.AccionException;

import java.util.List;
import java.util.Optional;

public interface SuscripcionService {
    public List<Suscripcion> buscarTodos(String idUsuario, String idEvento, String fechaSuscripcion);
    public Optional<Suscripcion> buscarPorId(Long id);
    public Suscripcion crear(Suscripcion suscripcion) throws AccionException;
    public boolean eventoPublico(Suscripcion suscripcion);
    public void eliminar(Long id) throws AccionException;
}
