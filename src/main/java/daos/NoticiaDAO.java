package daos;

import entidades.Categoria;
import entidades.Noticia;
import entidades.Suscripcion;
import entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface NoticiaDAO extends JpaRepository<Noticia, Long> {
    List<Suscripcion> findByUsuario(Usuario usuario);

    @Query("SELECT n FROM Noticia n WHERE " +
            "(:titulo IS NULL OR n.titulo LIKE %:titulo%) AND " +
            "(:descripcion IS NULL OR n.descripcion LIKE %:descripcion%) AND " +
            "(:fechaNoticia IS NULL OR n.fechaNoticia LIKE %:fechaNoticia%) AND " +
            "(:idUsuario IS NULL OR n.usuario.id =:idUsuario) ")
    public List<Noticia> buscarTodos(String titulo, String descripcion, String fechaNoticia, Long idUsuario);

}