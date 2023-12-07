package daos;

import entidades.Evento;
import entidades.Comentario;
import entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComentarioDAO extends JpaRepository<Comentario, Long> {
    List<Comentario> findByUsuarioId(Long id);
    List<Comentario> findByUsuario(Usuario usuario);

    List<Comentario> findByEvento(Evento evento);

    List<Comentario> findByUsuarioAndEvento(Usuario usuario, Evento evento);

    @Query("SELECT c FROM Comentario c WHERE " +
            "(:idEvento IS NULL OR c.evento.id =:idEvento) " +
            "ORDER BY c.puntuacion DESC")
    List<Comentario> findTodos(Long idEvento);
}