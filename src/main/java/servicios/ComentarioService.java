package servicios;

import entidades.Comentario;
import excepciones.AccionException;

import java.util.List;
import java.util.Optional;

public interface ComentarioService {
    public List<Comentario> buscarTodos(Long idEvento);
    public List<Integer> valoraciones(Long idEvento);
    public Comentario crear(Comentario comentario) throws AccionException;
    public void eliminar(Long id) throws AccionException;
}
