package servicios;

import entidades.Amistad;
import entidades.Categoria;
import excepciones.AccionException;

import java.util.List;

public interface AmistadService {
    public List<Amistad> buscarTodos(String idGerente, String idSeguidor, String nombreGerente);
    public Amistad crear(Amistad amistad) throws AccionException;
    public void eliminar(Long id) throws AccionException;
}
