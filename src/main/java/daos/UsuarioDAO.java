package daos;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import entidades.Usuario;


public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    public List<Usuario> findUsuarioByNombreContaining(String nombre);
    public List<Usuario> findUsuarioByLoginContaining(String nombre);
    public List<Usuario> findUsuarioByEmailContaining(String email);
    public List<Usuario> findUsuarioByDniContaining(String dni);
    public Optional<Usuario> findFirstByLogin(String login);
    public Optional<Usuario> findByRol(String rol);
    public Optional<Usuario> findByLogin(String login);
    public Boolean existsByLogin(String login);
    public Boolean existsByEmail(String email);
    public Boolean existsByDni(String dni);

    @Query("SELECT u FROM Usuario u WHERE " +
            "(:login IS NULL OR u.login LIKE %:login%) AND " +
            "(:nombre IS NULL OR u.nombre LIKE %:nombre%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%)")
    public List<Usuario> buscarTodos(String login, String nombre, String email);
}
