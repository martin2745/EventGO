package daos;

import java.util.List;
import entidades.Evento;
import entidades.Solicitud;
import entidades.Suscripcion;
import entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SuscripcionDAO extends JpaRepository<Suscripcion, Long> {
    List<Suscripcion> findByUsuario(Usuario usuario);
    List<Suscripcion> findByUsuarioId(Long idUsuario);

    List<Suscripcion> findByEvento(Evento evento);

    List<Suscripcion> findByUsuarioAndEvento(Usuario usuario, Evento evento);

    @Query("SELECT s FROM Suscripcion s WHERE " +
            "(:idUsuario IS NULL OR s.usuario.id = :idUsuario) AND " +
            "(:idEvento IS NULL OR s.evento.id = :idEvento) AND " +
            "(:fechaSuscripcion IS NULL OR s.fechaSuscripcion LIKE %:fechaSuscripcion%)")
    public List<Suscripcion> buscarTodos(Long idUsuario, Long idEvento, String fechaSuscripcion);

}