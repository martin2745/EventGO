package servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import entidades.Usuario;
import excepciones.AccionException;

import javax.mail.MessagingException;

public interface UsuarioService {
    public List<Usuario> buscarTodos(String login, String nombre, String email, String rol, String dni, String fechaNacimiento, String pais, String imagenUsuario, String borradoLogico);
    public Usuario crear(Usuario usuario) throws AccionException;
    public Usuario modificar(Long id, Usuario usuario, String loginHeader, String idioma) throws AccionException, MessagingException;
    public void eliminar(Long id) throws AccionException;
    boolean loginYaExiste(Long id, List <Usuario> usuarioLogin);
    boolean emailYaExiste(Long id, List <Usuario> usuarioLogin);
    public Optional<Usuario> buscarPorId(Long id);
    public Optional<Usuario> buscarPorLogin(String login);
    public List<Usuario> buscarGerentes(Long id);

}

