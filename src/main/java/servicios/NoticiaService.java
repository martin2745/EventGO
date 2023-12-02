package servicios;

import entidades.Noticia;
import excepciones.AccionException;

import javax.mail.MessagingException;
import java.util.List;

public interface NoticiaService {
    public List<Noticia> buscarTodos(String titulo, String descripcion, String fechaNoticia, String idUsuario);
    public Noticia crear(Noticia noticia, String idioma) throws AccionException, MessagingException;
    public void eliminar(Long id) throws AccionException;

}