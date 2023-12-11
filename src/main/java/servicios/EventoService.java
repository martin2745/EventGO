package servicios;

import entidades.Evento;
import excepciones.AccionException;

import java.util.List;
import java.util.Optional;

public interface EventoService {
    public List<Evento> buscarTodos(String nombre, String descripcion, String tipoAsistencia, String numAsistentes, String estado, String fechaEvento, String direccion, String emailContacto, String telefonoContacto, String idCategoria, String idUsuario, String borradoLogico);
    public Evento crear(Evento evento) throws AccionException;
    public Evento modificar(Long id, Evento evento) throws AccionException;
    public void eliminar(Long id) throws AccionException;
    public boolean nombreEventoYaExiste(Long id, Evento eventoEditar, List <Evento> eventos);
    public Optional<Evento> buscarPorId(Long id);
    public List<Evento> eventosCategoria(Long idCategoria);
    public List<Evento> eventosCategoriaValidos(Long idCategoria);
    public List<Evento> eventosCategoriaValidosBuscar(String nombre, String descripcion, String tipoAsistencia, String numAsistentes, String estado, String fechaEvento, String direccion, String emailContacto, String telefonoContacto, String idCategoria, String idUsuario, String borradoLogico);
    public Optional<Evento> buscarPorNombreEvento(String nombre);
}
