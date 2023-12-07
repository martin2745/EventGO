package controllers;

import dtos.MensajeRespuesta;
import entidades.Amistad;
import entidades.Categoria;
import excepciones.AccionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import servicios.AmistadService;
import validaciones.CodigosRespuesta;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/amistad")
public class AmistadController {
    @Autowired
    AmistadService amistadService;

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping()
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "idGerente", required = false) String idGerente,
            @RequestParam(name = "idSeguidor", required = false) String idSeguidor,
            @RequestParam(name = "nombreGerente", required = false) String nombreGerente){
        try {
            List<Amistad> resultado = amistadService.buscarTodos(idGerente, idSeguidor, nombreGerente);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Amistad>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOAmistad(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Amistad amistad, @RequestHeader("idioma") String idioma) {
        try {
            Amistad nuevoAmistad = amistadService.crear(amistad);
            EntityModel<Amistad> dto = crearDTOAmistad(nuevoAmistad);
            URI uri = crearURIAmistad(nuevoAmistad);
            return ResponseEntity.created(uri).body(dto);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(path = "/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            amistadService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    private EntityModel<Amistad> crearDTOAmistad(Amistad amistad) {
        Long id = amistad.getId();
        EntityModel<Amistad> dto = EntityModel.of(amistad);
        return dto;
    }

    private URI crearURIAmistad(Amistad amistad) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(amistad.getId()).toUri();
    }
}
