package controllers;

import dtos.MensajeRespuesta;
import entidades.Categoria;
import entidades.Evento;
import excepciones.AccionException;
import excepciones.AtributoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import servicios.StorageService;
import servicios.CategoriaService;
import validaciones.CodigosRespuesta;
import validaciones.ValidacionesAtributos;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/categoria")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;

    @Autowired
    StorageService storageService;

    private HttpServletRequest request;

    private final ValidacionesAtributos validacionesAtributos = new ValidacionesAtributos();

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping()
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "borradoLogico", required = false) String borradoLogico){
        try {
            List<Categoria> resultado = categoriaService.buscarTodos(nombre, descripcion, borradoLogico);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Categoria>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOCategoria(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        if (categoria.isPresent()) {
            EntityModel<Categoria> dto = crearDTOCategoria(categoria.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Categoria categoria) {
        try {
            validacionesAtributos.comprobarInsertarModificarCategoria(categoria);
            Categoria nuevoCategoria = categoriaService.crear(categoria);
            EntityModel<Categoria> dto = crearDTOCategoria(nuevoCategoria);
            URI uri = crearURICategoria(nuevoCategoria);
            return ResponseEntity.created(uri).body(dto);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping(path = "/modificar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@PathVariable("id") Long id, @Valid @RequestBody Categoria categoria) {
        try {
            validacionesAtributos.comprobarInsertarModificarCategoria(categoria);
            EntityModel<Categoria> dto = crearDTOCategoria(categoriaService.modificar(id, categoria));
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping(path = "/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            categoriaService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping(value ="uploadImagenCategoria", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<?> uploadImagenCategoria(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("id") Long id) {
        try {
            String path = storageService.store(multipartFile, id, "imagenCategoria");
            String host = "http://localhost:8080/";
            String url = ServletUriComponentsBuilder
                    .fromHttpUrl(host)
                    .path("api/media/")
                    .path(path)
                    .toUriString();
            return ResponseEntity.ok(new MensajeRespuesta(CodigosRespuesta.ARCHIVO_SUBIDO_OK.getCode(), url));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    private EntityModel<Categoria> crearDTOCategoria(Categoria categoria) {
        Long id = categoria.getId();
        EntityModel<Categoria> dto = EntityModel.of(categoria);
        return dto;
    }

    private URI crearURICategoria(Categoria categoria) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId()).toUri();
    }
}
