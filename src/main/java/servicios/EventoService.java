package servicios;

import entidades.Evento;
import excepciones.AccionException;

import java.util.List;
import java.util.Optional;

public interface EventoService {
    public List<Evento> buscarTodos(String nombre, String descripcion, String tipoAsistencia, String numAsistentes, String estado, String fechaEvento, String direccion, String emailContacto, String telefonoContacto, String idCategoria, String idUsuario);
    public Evento crear(Evento evento) throws AccionException;
    public Evento modificar(Long id, Evento evento, String loginHeader) throws AccionException;
    public void eliminar(Long id, String loginHeader) throws AccionException;
    public boolean nombreEventoYaExiste(Long id, List <Evento> eventos);
    public Optional<Evento> buscarPorId(Long id);
    public List<Evento> eventosCategoria(Long idCategoria);
    public Optional<Evento> buscarPorNombreEvento(String nombre);
}
