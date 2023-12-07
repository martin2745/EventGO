package controllers;

import entidades.Evento;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.*;
import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import entidades.Suscripcion;
import servicios.SuscripcionService;
import validaciones.CodigosRespuesta;
import dtos.MensajeRespuesta;
import excepciones.AccionException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/suscripcion")
public class SuscripcionController {

    @Autowired
    SuscripcionService suscripcionService;

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping()
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "idUsuario", required = false) String idUsuario,
            @RequestParam(name = "idEvento", required = false) String idEvento,
            @RequestParam(name = "fechaSuscripcion", required = false) String fechaSuscripcion){
        try {
            List<Suscripcion> resultado = suscripcionService.buscarTodos(idUsuario, idEvento, fechaSuscripcion);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Suscripcion>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOSuscripcion(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        Optional<Suscripcion> suscripcion = suscripcionService.buscarPorId(id);
        if (suscripcion.isPresent()) {
            EntityModel<Suscripcion> dto = crearDTOSuscripcion(suscripcion.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Suscripcion suscripcion, @RequestHeader("idioma") String idioma) {
        try {
            Suscripcion nuevoSuscripcion = suscripcionService.crear(suscripcion, idioma);
            EntityModel<Suscripcion> dto = crearDTOSuscripcion(nuevoSuscripcion);
            URI uri = crearURISuscripcion(nuevoSuscripcion);
            return ResponseEntity.created(uri).body(dto);
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
            suscripcionService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    private EntityModel<Suscripcion> crearDTOSuscripcion(Suscripcion suscripcion) {
        Long id = suscripcion.getId();
        EntityModel<Suscripcion> dto = EntityModel.of(suscripcion);
        return dto;
    }


    private URI crearURISuscripcion(Suscripcion suscripcion) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(suscripcion.getId()).toUri();
    }
}
