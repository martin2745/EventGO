package servicios;

import daos.UsuarioDAO;
import daos.EventoDAO;
import daos.SuscripcionDAO;
import entidades.Evento;
import entidades.Suscripcion;
import entidades.Usuario;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import validaciones.CodigosRespuesta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuscripcionServiceImpl implements SuscripcionService{

    @Autowired
    private SuscripcionDAO suscripcionDAO;
    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private EventoDAO eventoDAO;
    public List<Suscripcion> buscarTodos(String idUsuario, String idEvento, String fechaSuscripcion){
        List<Suscripcion> suscripcion = new ArrayList<Suscripcion>();
        suscripcion = suscripcionDAO.buscarTodos(idUsuario != null ? Long.parseLong(idUsuario) : null, idEvento != null ? Long.parseLong(idEvento) : null, fechaSuscripcion);
        return suscripcion;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Suscripcion> buscarPorId(Long id) {
        return suscripcionDAO.findById(id);
    }

    public Suscripcion crear(Suscripcion suscripcion) throws AccionException{
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaFormateada = fechaActual.format(formato);
        suscripcion.setFechaSuscripcion(fechaFormateada);

        Usuario usuarioSus = usuarioDAO.getById(suscripcion.getUsuario().getId());
        Evento eventoSus = eventoDAO.getById(suscripcion.getEvento().getId());
        List<Suscripcion> suscripcionesExistentes = suscripcionDAO.findByUsuarioAndEvento(usuarioSus, eventoSus);

        if (!suscripcionesExistentes.isEmpty()) {
            throw new AccionException(CodigosRespuesta.EXISTE_SUSCRIPCION.getCode(), CodigosRespuesta.EXISTE_SUSCRIPCION.getMsg());
        }
        if (eventoSus.getEstado().equals("CERRADO")) {
            throw new AccionException(CodigosRespuesta.EVENTO_CERRADO.getCode(), CodigosRespuesta.EVENTO_CERRADO.getMsg());
        }
        if (eventoSus.getNumAsistentes() == eventoSus.getNumInscritos()) {
            throw new AccionException(CodigosRespuesta.PLAZAS_CUBIERTAS.getCode(), CodigosRespuesta.PLAZAS_CUBIERTAS.getMsg());
        }
        eventoSus.setNumInscritos(eventoSus.getNumInscritos() + 1);
        return suscripcionDAO.save(suscripcion);
    }

    public boolean eventoPublico(Suscripcion suscripcion){
        Evento eventoSus = eventoDAO.getById(suscripcion.getEvento().getId());
        if(eventoSus.getTipoAsistencia().equals("PUBLICO")){
            return true;
        }else{
            return false;
        }
    }

    public void eliminar(Long id) throws AccionException{
        Optional<Suscripcion> suscripcion = suscripcionDAO.findById(id);
        //Reatamos el numInscritos en 1
        suscripcion.get().getEvento().setNumInscritos(suscripcion.get().getEvento().getNumInscritos() - 1);
        suscripcionDAO.delete(suscripcion.get());
    }
}