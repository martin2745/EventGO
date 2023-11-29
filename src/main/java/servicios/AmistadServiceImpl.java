package servicios;

import daos.AmistadDAO;
import daos.UsuarioDAO;
import entidades.Amistad;
import entidades.Categoria;
import entidades.Usuario;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import validaciones.CodigosRespuesta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AmistadServiceImpl implements AmistadService{
    @Autowired
    private AmistadDAO amistadDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;

    public List<Amistad> buscarTodos(String idGerente, String idSeguidor, String nombreGerente){
        List<Amistad> amistades = new ArrayList<Amistad>();
        amistades = amistadDAO.buscarTodos(idGerente != null ? Long.parseLong(idGerente) : null, idSeguidor != null ? Long.parseLong(idSeguidor) : null, nombreGerente);
        return amistades;
    }
    public Amistad crear(Amistad amistad) throws AccionException{
        /*Solo crea amistades el usuario seguidor*/
        /*El usuario seguidor tiene que existir en el sistema*/
        /*El usuario gerente tiene que existir en el sistema*/
        /*Ya existe esta amistad en el sistema*/
        return amistadDAO.save(amistad);
    }

    public void eliminar(Long id) throws AccionException{
        Optional<Amistad> amistad = amistadDAO.findById(id);
        /*Solo elimina la amistad el usuario seguidor*/
        if (amistad.isPresent()) {
            amistadDAO.delete(amistad.get());
        } else {
            throw new AccionException(CodigosRespuesta.AMISTAD_NO_EXISTE.getCode(), CodigosRespuesta.AMISTAD_NO_EXISTE.getMsg());
        }
    }
}
