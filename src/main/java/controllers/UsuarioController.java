package controllers;

import java.net.URI;
import java.util.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import entidades.Usuario;
import servicios.StorageService;
import servicios.UsuarioService;
import validaciones.CodigosRespuesta;
import validaciones.ValidacionesAtributos;
import dtos.MensajeRespuesta;
import excepciones.AccionException;
import excepciones.AtributoException;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    StorageService storageService;
    private HttpServletRequest request;

    private final ValidacionesAtributos validacionesAtributos = new ValidacionesAtributos();

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping()
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "login", required = false) String login,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "rol", required = false) String rol,
            @RequestParam(name = "dni", required = false) String dni,
            @RequestParam(name = "fechaNacimiento", required = false) String fechaNacimiento,
            @RequestParam(name = "pais", required = false) String pais,
            @RequestParam(name = "imagenUsuario", required = false) String imagenUsuario,
            @RequestParam(name = "borradoLogico", required = false) String borradoLogico){
        try {
            //validacionesAtributos.usuarioBuscarTodos(login, nombre, email, rol, dni, fechaNacimiento, pais);
            List<Usuario> resultado = usuarioService.buscarTodos(login, nombre, email, rol, dni, fechaNacimiento, pais, imagenUsuario, borradoLogico);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Usuario>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOUsuario(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);

        /*}catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }*/}catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
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

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, @RequestHeader("login") String loginHeader) {
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

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(path = "/modificar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@PathVariable("id") Long id, @Valid @RequestBody Usuario usuario, @RequestHeader("login") String loginHeader, @RequestHeader("idioma") String idioma) {
        try {
            validacionesAtributos.comprobarModificarUsuario(usuario);
            EntityModel<Usuario> dto = crearDTOUsuario(usuarioService.modificar(id, usuario, loginHeader, idioma));
            return new ResponseEntity<>(dto, HttpStatus.OK);
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

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @PostMapping(path = "/eliminar/{id}")
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


    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @PostMapping(value ="uploadImagenUsuario", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<?> uploadImagenUsuario(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("id") Long id, @RequestHeader("login") String loginHeader) {
        try {
            String path = storageService.store(multipartFile, id, "imagenUsuario", loginHeader);
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

    private EntityModel<Usuario> crearDTOUsuario(Usuario usuario) {
        Long id = usuario.getId();
        EntityModel<Usuario> dto = EntityModel.of(usuario);
        return dto;
    }

    private URI crearURIUsuario(Usuario usuario) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuario.getId()).toUri();
    }
}