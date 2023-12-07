package controllers;

import dtos.MensajeRespuesta;
import entidades.Noticia;
import excepciones.AccionException;
import excepciones.AtributoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import servicios.NoticiaService;
import validaciones.CodigosRespuesta;
import validaciones.ValidacionesAtributos;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/noticia")
public class NoticiaController {
    @Autowired
    NoticiaService noticiaService;
    private final ValidacionesAtributos validacionesAtributos = new ValidacionesAtributos();

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping()
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "titulo", required = false) String titulo,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "fechaNoticia", required = false) String fechaNoticia,
            @RequestParam(name = "idUsuario", required = false) String idUsuario){
        try {
            List<Noticia> resultado = noticiaService.buscarTodos(titulo, descripcion, fechaNoticia, idUsuario);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Noticia>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTONoticia(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Noticia noticia, @RequestHeader("idioma") String idioma) {
        try {
            validacionesAtributos.comprobarInsertarNoticia(noticia);
            Noticia nuevoNoticia = noticiaService.crear(noticia, idioma);
            EntityModel<Noticia> dto = crearDTONoticia(nuevoNoticia);
            URI uri = crearURINoticia(nuevoNoticia);
            return ResponseEntity.created(uri).body(dto);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
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
            noticiaService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    private EntityModel<Noticia> crearDTONoticia(Noticia noticia) {
        Long id = noticia.getId();
        EntityModel<Noticia> dto = EntityModel.of(noticia);
        return dto;
    }

    private URI crearURINoticia(Noticia noticia) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(noticia.getId()).toUri();
    }
}
