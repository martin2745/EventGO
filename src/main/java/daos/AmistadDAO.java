package daos;

import entidades.Amistad;
import entidades.Categoria;
import entidades.Comentario;
import entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AmistadDAO extends JpaRepository<Amistad, Long> {
    List<Amistad> findBySeguidor(Usuario seguidor);
    List<Amistad> findByGerente(Usuario gerente);
    @Query("SELECT a FROM Amistad a WHERE " +
            "(:idGerente IS NULL OR a.gerente.id =:idGerente) AND " +
            "(:idSeguidor IS NULL OR a.seguidor.id =:idSeguidor) AND " +
            "(:nombreGerente IS NULL OR a.gerente.nombre LIKE %:nombreGerente%)")
    public List<Amistad> buscarTodos(Long idGerente, Long idSeguidor, String nombreGerente);
}
