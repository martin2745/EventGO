package daos;

import entidades.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AmistadDAO extends JpaRepository<Amistad, Long> {
    List<Amistad> findBySeguidor(Usuario seguidor);
    List<Amistad> findByGerente(Usuario gerente);
    List<Amistad> findByGerenteId(Long idGerente);
    List<Amistad> findBySeguidorId(Long idSeguidor);
    List<Amistad> findByGerenteAndSeguidor(Usuario gerente, Usuario seguidor);
    @Query("SELECT a FROM Amistad a WHERE " +
            "(:idGerente IS NULL OR a.gerente.id =:idGerente) AND " +
            "(:idSeguidor IS NULL OR a.seguidor.id =:idSeguidor) AND " +
            "(:nombreGerente IS NULL OR a.gerente.nombre LIKE %:nombreGerente%)")
    public List<Amistad> buscarTodos(Long idGerente, Long idSeguidor, String nombreGerente);
}
