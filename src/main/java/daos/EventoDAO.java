package daos;

import entidades.Categoria;
import entidades.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventoDAO extends JpaRepository<Evento, Long> {
    public List<Evento> findEventoByNombreContaining(String nombre);
    public Optional<Evento> findFirstByNombre(String nombre);
    public List<Evento> findByCategoriaId (Long idCategoria);
    public List<Evento> findByUsuarioId (Long idUsuario);
    public List<Evento> findByFechaEvento (String fechaEvento);
    public Boolean existsByNombre(String nombre);

    @Query("SELECT e FROM Evento e WHERE " +
            "(:nombre IS NULL OR e.nombre LIKE %:nombre%) AND " +
            "(:descripcion IS NULL OR e.descripcion LIKE %:descripcion%) AND " +
            "(:tipoAsistencia IS NULL OR e.tipoAsistencia =:tipoAsistencia) AND " +
            "(:numAsistentes IS NULL OR e.numAsistentes =:numAsistentes) AND " +
            "(:estado IS NULL OR e.estado LIKE %:estado%) AND " +
            "(:fechaEvento IS NULL OR e.fechaEvento LIKE %:fechaEvento%) AND " +
            "(:direccion IS NULL OR e.direccion LIKE %:direccion%) AND " +
            "(:emailContacto IS NULL OR e.emailContacto LIKE %:emailContacto%) AND " +
            "(:telefonoContacto IS NULL OR e.telefonoContacto LIKE %:telefonoContacto%) AND " +
            "(:idCategoria IS NULL OR e.categoria.id =:idCategoria)  AND " +
            "(:idUsuario IS NULL OR e.usuario.id =:idUsuario) AND " +
            "(:borradoLogico IS NULL OR e.borradoLogico LIKE %:borradoLogico%)")
    public List<Evento> buscarTodos(String nombre, String descripcion,
                                    String tipoAsistencia, Integer numAsistentes, String estado,
                                    String fechaEvento, String direccion,
                                    String emailContacto, String telefonoContacto,
                                    Long idCategoria, Long idUsuario, String borradoLogico);
}
