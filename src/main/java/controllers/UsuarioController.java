package controllers;

import java.net.URI;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import entidades.Usuario;
import servicios.AutenticationService;
import servicios.StorageService;
import servicios.UsuarioService;
import validaciones.CodigosRespuesta;
import validaciones.ValidacionesAtributos;
import dtos.DatosLogin;
import dtos.MensajeRespuesta;
import dtos.RespuestaJWT;
import dtos.RespuestaJWT;
import validaciones.ValidacionesAtributos;
import excepciones.AccionException;
import excepciones.AtributoException;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/usuario")
@PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    StorageService storageService;
    private HttpServletRequest request;

    private final ValidacionesAtributos validacionesAtributos = new ValidacionesAtributos();

    @GetMapping()
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "login", required = false) String login,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "email", required = false) String email){
        try {
            validacionesAtributos.usuarioBuscarTodos(login, nombre, email);
            List<Usuario> resultado = usuarioService.buscarTodos(login, nombre, email);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Usuario>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOUsuario(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);

        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            EntityModel<Usuario> dto = crearDTOUsuario(usuario.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario) {
        try {
            validacionesAtributos.comprobarInsercionUsuario(usuario);
            Usuario nuevoUsuario = usuarioService.crear(usuario);
            EntityModel<Usuario> dto = crearDTOUsuario(nuevoUsuario);
            URI uri = crearURIUsuario(nuevoUsuario);
            return ResponseEntity.created(uri).body(dto);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@PathVariable("id") Long id, @Valid @RequestBody Usuario usuario) {
        try {
            validacionesAtributos.comprobarModificarUsuario(usuario);
            EntityModel<Usuario> dto = crearDTOUsuario(usuarioService.modificar(id, usuario));
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            usuarioService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PostMapping(value ="uploadImagenUsuario", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public Map<String, String> uploadImagenUsuario(@RequestParam("file") MultipartFile multipartFile, @RequestParam("id") Long id) {
        String path = storageService.store(multipartFile, id, "imagenUsuario");
        String host = "http://localhost:8080/";
        String url = ServletUriComponentsBuilder
                .fromHttpUrl(host)
                .path("api/media/")
                .path(path)
                .toUriString();

        return Collections.singletonMap("url", url);
    }

    private EntityModel<Usuario> crearDTOUsuario(Usuario usuario) {
        Long id = usuario.getId();
        EntityModel<Usuario> dto = EntityModel.of(usuario);
        return dto;
    }

    private URI crearURIUsuario(Usuario usuario) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId()).toUri();
    }
}