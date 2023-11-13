package servicios;

import entidades.Solicitud;
import entidades.Suscripcion;
import excepciones.AccionException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudService{
    public List<Solicitud> buscarTodos(String idUsuario, String idEvento, String fechaSolicitud);
    public Optional<Solicitud> buscarPorId(Long id);
    public Solicitud crear(Solicitud solicitud) throws AccionException;
    public void aceptarSolicitud(Solicitud solicitud, Long id) throws AccionException;
    public void eliminar(Long id) throws AccionException;
}
