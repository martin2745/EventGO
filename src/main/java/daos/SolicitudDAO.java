package daos;

import java.util.List;
import entidades.Evento;
import entidades.Solicitud;
import entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SolicitudDAO extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findByUsuario(Usuario usuario);

    List<Solicitud> findByEvento(Evento evento);

    List<Solicitud> findByUsuarioAndEvento(Usuario usuario, Evento evento);

    @Query("SELECT s FROM Solicitud s WHERE " +
            "(:idUsuario IS NULL OR s.usuario.id = :idUsuario) AND " +
            "(:idEvento IS NULL OR s.evento.id = :idEvento) AND " +
            "(:fechaSolicitud IS NULL OR s.fechaSolicitud LIKE %:fechaSolicitud%)")
    public List<Solicitud> buscarTodos(Long idUsuario, Long idEvento, String fechaSolicitud);
}