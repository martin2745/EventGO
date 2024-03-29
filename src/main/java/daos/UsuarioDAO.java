package daos;

import java.util.Date;
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
    @Query("SELECT u FROM Usuario u WHERE u.rol LIKE %:rol% AND u.borradoLogico = :borradoLogico")
    public List<Usuario> findUsuarioByRolAndBorradoLogico(String rol, String borradoLogico);
    public Optional<Usuario> findFirstByLogin(String login);
    public Optional<Usuario> findByRol(String rol);
    public Optional<Usuario> findByLogin(String login);
    public Boolean existsByLogin(String login);
    public Boolean existsByEmail(String email);
    public Boolean existsByDni(String dni);

    @Query("SELECT u FROM Usuario u WHERE " +
            "(:login IS NULL OR u.login LIKE %:login%) AND " +
            "(:nombre IS NULL OR u.nombre LIKE %:nombre%) AND " +
            "(:email IS NULL OR u.email LIKE %:email%) AND " +
            "(:rol IS NULL OR u.rol LIKE %:rol%) AND " +
            "(:dni IS NULL OR u.dni LIKE %:dni%) AND " +
            "(:fechaNacimiento IS NULL OR u.fechaNacimiento LIKE %:fechaNacimiento%) AND " +
            "(:pais IS NULL OR u.pais LIKE %:pais%) AND " +
            "(:imagenUsuario IS NULL OR u.imagenUsuario LIKE %:imagenUsuario%) AND " +
            "(:borradoLogico IS NULL OR u.borradoLogico LIKE %:borradoLogico%)")
    public List<Usuario> buscarTodos(String login, String nombre, String email, String rol, String dni, String fechaNacimiento, String pais, String imagenUsuario, String borradoLogico);
}
