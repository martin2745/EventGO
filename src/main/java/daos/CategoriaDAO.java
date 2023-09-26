package daos;

import entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaDAO extends JpaRepository<Categoria, Long> {
    public List<Categoria> findCategoriaByNombreContaining(String nombre);
    public Optional<Categoria> findFirstByNombre(String nombre);
    public Boolean existsByNombre(String nombre);

    @Query("SELECT c FROM Categoria c WHERE " +
            "(:nombre IS NULL OR c.nombre LIKE %:nombre%) AND " +
            "(:descripcion IS NULL OR c.descripcion LIKE %:descripcion%)")
    public List<Categoria> buscarTodos(String nombre, String descripcion);
}
