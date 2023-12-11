package controllers;

import dtos.MensajeRespuesta;
import entidades.Categoria;
import entidades.Evento;
import entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import servicios.CategoriaService;
import servicios.UsuarioService;
import servicios.EventoService;
import validaciones.CodigosRespuesta;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/abierto")
public class AbiertoController {

    @Autowired
    CategoriaService categoriaService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    EventoService eventoService;
    @GetMapping(path = "categoria")
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

    @GetMapping(path = "usuario")
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
            List<Usuario> resultado = usuarioService.buscarTodos(login, nombre, email, rol, dni, fechaNacimiento, pais, imagenUsuario, borradoLogico);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Usuario>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOUsuario(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @GetMapping(path = "evento")
    public ResponseEntity<?> buscarTodos(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "tipoAsistencia", required = false) String tipoAsistencia,
            @RequestParam(name = "numAsistentes", required = false) String numAsistentes,
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "fechaEvento", required = false) String fechaEvento,
            @RequestParam(name = "direccion", required = false) String direccion,
            @RequestParam(name = "emailContacto", required = false) String emailContacto,
            @RequestParam(name = "telefonoContacto", required = false) String telefonoContacto,
            @RequestParam(name = "idCategoria", required = false) String idCategoria,
            @RequestParam(name = "idUsuario", required = false) String idUsuario,
            @RequestParam(name = "borradoLogico", required = false) String borradoLogico){
        try {
            List<Evento> resultado = eventoService.buscarTodos(nombre, descripcion, tipoAsistencia, numAsistentes, estado, fechaEvento, direccion, emailContacto, telefonoContacto, idCategoria, idUsuario, borradoLogico);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Evento>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOEvento(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @GetMapping(path = "evento/eventosCategoria/{idCategoria}")
    public ResponseEntity<?> eventosCategoria(@PathVariable("idCategoria") Long idCategoria) {
        try {
            List<Evento> resultado = eventoService.eventosCategoria(idCategoria);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Evento>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOEvento(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);

        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @GetMapping(path = "evento/eventosCategoriaValidos/{idCategoria}")
    public ResponseEntity<?> eventosCategoriaValidos(@PathVariable("idCategoria") Long idCategoria) {
        try {
            List<Evento> resultado = eventoService.eventosCategoriaValidos(idCategoria);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Evento>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOEvento(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);

        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path="/evento/eventosCategoriaValidosBuscar")
    public ResponseEntity<?> eventosCategoriaValidosBuscar(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "descripcion", required = false) String descripcion,
            @RequestParam(name = "tipoAsistencia", required = false) String tipoAsistencia,
            @RequestParam(name = "numAsistentes", required = false) String numAsistentes,
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "fechaEvento", required = false) String fechaEvento,
            @RequestParam(name = "direccion", required = false) String direccion,
            @RequestParam(name = "emailContacto", required = false) String emailContacto,
            @RequestParam(name = "telefonoContacto", required = false) String telefonoContacto,
            @RequestParam(name = "idCategoria", required = false) String idCategoria,
            @RequestParam(name = "idUsuario", required = false) String idUsuario,
            @RequestParam(name = "borradoLogico", required = false) String borradoLogico){
        try {
            List<Evento> resultado = eventoService.eventosCategoriaValidosBuscar(nombre, descripcion, tipoAsistencia, numAsistentes, estado, fechaEvento, direccion, emailContacto, telefonoContacto, idCategoria, idUsuario, borradoLogico);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Evento>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOEvento(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    private EntityModel<Categoria> crearDTOCategoria(Categoria categoria) {
        Long id = categoria.getId();
        EntityModel<Categoria> dto = EntityModel.of(categoria);
        return dto;
    }

    private EntityModel<Usuario> crearDTOUsuario(Usuario usuario) {
        Long id = usuario.getId();
        EntityModel<Usuario> dto = EntityModel.of(usuario);
        return dto;
    }

    private EntityModel<Evento> crearDTOEvento(Evento evento) {
        Long id = evento.getId();
        EntityModel<Evento> dto = EntityModel.of(evento);
        return dto;
    }


}
