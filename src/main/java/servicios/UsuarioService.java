package servicios;

import java.util.List;
import java.util.Optional;

import entidades.Usuario;
import excepciones.AccionException;

public interface UsuarioService {
    public List<Usuario> buscarTodos(String login, String nombre, String email);
    public Usuario crear(Usuario usuario) throws AccionException;
    public Usuario modificar(Long id, Usuario usuario) throws AccionException;
    public void eliminar(Long id) throws AccionException;
    public Optional<Usuario> buscarPorId(Long id);
    public Optional<Usuario> buscarPorLogin(String login);
}

