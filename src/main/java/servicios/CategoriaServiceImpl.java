package servicios;


import entidades.Evento;
import entidades.Usuario;
import excepciones.AccionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import autenticacion.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import daos.CategoriaDAO;
import daos.EventoDAO;
import entidades.Categoria;
import excepciones.AccionException;
import servicios.CategoriaService;
import validaciones.CodigosRespuesta;
import validaciones.Constantes;

import javax.mail.MessagingException;

@Service
public class CategoriaServiceImpl implements CategoriaService{

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private EventoDAO eventoDAO;

    public List<Categoria> buscarTodos(String nombre, String descripcion, String borradoLogico){
        List<Categoria> resultado = new ArrayList<Categoria>();
        resultado = categoriaDAO.buscarTodos(nombre, descripcion, borradoLogico);
        return resultado;
    }
    public Categoria crear(Categoria categoria) throws AccionException{
        if (categoriaDAO.existsByNombre(categoria.getNombre())) {
            throw new AccionException(CodigosRespuesta.NOMBRE_CATEGORIA_YA_EXISTE.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_YA_EXISTE.getMsg());
        }
        return categoriaDAO.save(categoria);
    }
    public Categoria modificar(Long id, Categoria categoria) throws AccionException{
        Optional<Categoria> categoriaOptional = categoriaDAO.findById(id);
        List<Categoria> categoriaNombre = categoriaDAO.findCategoriaByNombreContaining(categoria.getNombre());

        if (this.nombreCategoriaYaExiste(id, categoriaNombre)) {
            throw new AccionException(CodigosRespuesta.NOMBRE_CATEGORIA_YA_EXISTE.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_YA_EXISTE.getMsg());
        }

        if (categoriaOptional.isPresent()) {
            return categoriaDAO.save(categoria);
        } else {
            throw new AccionException(CodigosRespuesta.CATEGORIA_NO_EXISTE.getCode(), CodigosRespuesta.CATEGORIA_NO_EXISTE.getMsg());
        }
    }
    public void eliminar(Long id) throws AccionException{
        Optional<Categoria> categoria = categoriaDAO.findById(id);
        if (categoria.isPresent()) {
            List<Evento> eventosCategoria = eventoDAO.findByCategoriaId(categoria.get().getId());
            if(!eventosCategoria.isEmpty()){
                categoria.get().setBorradoLogico("1");
                categoriaDAO.save(categoria.get());
            }else{
                categoriaDAO.delete(categoria.get());
            }
        } else {
            throw new AccionException(CodigosRespuesta.CATEGORIA_NO_EXISTE.getCode(), CodigosRespuesta.CATEGORIA_NO_EXISTE.getMsg());
        }
    }
    public boolean nombreCategoriaYaExiste(Long id, List <Categoria> categoriaNombre){
        boolean toret = false;

        if(!categoriaNombre.isEmpty()){
            for(Categoria categoria : categoriaNombre){
                if(id != categoria.getId()){
                    toret = true;
                }
            }
        }

        return toret;
    }
    public Optional<Categoria> buscarPorId(Long id){
        return categoriaDAO.findById(id);
    }

    public Optional<Categoria> buscarPorNombreCategoria(String nombre){
        return categoriaDAO.findFirstByNombre(nombre);
    }
}
