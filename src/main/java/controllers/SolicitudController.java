package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import entidades.Solicitud;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import servicios.SolicitudService;
import validaciones.CodigosRespuesta;
import excepciones.AccionException;
import dtos.MensajeRespuesta;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/solicitud")
public class SolicitudController {
    @Autowired
    SolicitudService solicitudService;

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping()
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "idUsuario", required = false) String idUsuario,
            @RequestParam(name = "idEvento", required = false) String idEvento,
            @RequestParam(name = "fechaSolicitud", required = false) String fechaSolicitud){
        try {
            List<Solicitud> resultado = solicitudService.buscarTodos(idUsuario, idEvento, fechaSolicitud);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Solicitud>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOSolicitud(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        Optional<Solicitud> solicitud = solicitudService.buscarPorId(id);
        if (solicitud.isPresent()) {
            EntityModel<Solicitud> dto = crearDTOSolicitud(solicitud.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Solicitud solicitud) {
        try {
                Solicitud nuevoSolicitud = solicitudService.crear(solicitud);
                EntityModel<Solicitud> dto = crearDTOSolicitud(nuevoSolicitud);
                URI uri = crearURISolicitud(nuevoSolicitud);
                return ResponseEntity.created(uri).body(dto);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(path = "/aceptarSolicitud/{id}")
    public ResponseEntity<?> aceptarSolicitud(@Valid @RequestBody Solicitud solicitud, @PathVariable("id") Long id, @RequestHeader("idioma") String idioma) {
        try {
            solicitudService.aceptarSolicitud(solicitud, id, idioma);
            return ResponseEntity.ok(new MensajeRespuesta(CodigosRespuesta.SOLICITUD_ACEPTADA.getCode(), CodigosRespuesta.SOLICITUD_ACEPTADA.getCode()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch (MessagingException messagingException) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ENVIO_EMAIL_EXCEPTION.getCode(), CodigosRespuesta.ENVIO_EMAIL_EXCEPTION.getMsg()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(path = "/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            solicitudService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    private EntityModel<Solicitud> crearDTOSolicitud(Solicitud solicitud) {
        Long id = solicitud.getId();
        EntityModel<Solicitud> dto = EntityModel.of(solicitud);
        return dto;
    }

    private URI crearURISolicitud(Solicitud solicitud) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(solicitud.getId()).toUri();
    }
}
