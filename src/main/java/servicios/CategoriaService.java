package servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import entidades.Categoria;
import entidades.Usuario;
import excepciones.AccionException;

import javax.mail.MessagingException;

public interface CategoriaService {
    public List<Categoria> buscarTodos(String nombre, String descripcion, String borradoLogico);
    public Categoria crear(Categoria categoria) throws AccionException;
    public Categoria modificar(Long id, Categoria categoria) throws AccionException;
    public void eliminar(Long id) throws AccionException;
    public boolean nombreCategoriaYaExiste(Long id, List <Categoria> categoriaLogin);
    public Optional<Categoria> buscarPorId(Long id);
    public Optional<Categoria> buscarPorNombreCategoria(String nombre);
}
