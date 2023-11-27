package controllers;

import dtos.MensajeRespuesta;
import entidades.Comentario;
import entidades.Solicitud;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import servicios.ComentarioService;
import validaciones.CodigosRespuesta;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/comentario")
public class ComentarioController {
    @Autowired
    ComentarioService comentarioService;

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "{id}")
    public ResponseEntity<?> buscarTodos(@PathVariable("id") Long id){
        try {
            List<Comentario> resultado = comentarioService.buscarTodos(id);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<?>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOComentario(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "/numEstrellasEvento/{id}")
    public ResponseEntity<?> numEstrellasEvento(@PathVariable("id") Long id){
        try {
            List<Integer> resultado = comentarioService.valoraciones(id);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Comentario comentario, @RequestHeader("login") String loginHeader) {
        try {
            Comentario nuevoComentario = comentarioService.crear(comentario);
            EntityModel<Comentario> dto = crearDTOComentario(nuevoComentario);
            URI uri = crearURIComentario(nuevoComentario);
            return ResponseEntity.created(uri).body(dto);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    private EntityModel<Comentario> crearDTOComentario(Comentario comentario) {
        Long id = comentario.getId();
        EntityModel<Comentario> dto = EntityModel.of(comentario);
        return dto;
    }

    private URI crearURIComentario(Comentario comentario) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comentario.getId()).toUri();
    }
}
