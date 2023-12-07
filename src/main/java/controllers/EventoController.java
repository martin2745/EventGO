package controllers;

import dtos.MensajeRespuesta;
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
import servicios.EventoService;
import servicios.StorageService;
import servicios.SuscripcionService;
import servicios.SolicitudService;
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
@RequestMapping(path = "api/evento")
public class EventoController {
    @Autowired
    EventoService eventoService;
    @Autowired
    SuscripcionService suscripcionService;
    @Autowired
    SolicitudService solicitudService;
    @Autowired
    StorageService storageService;

    private final ValidacionesAtributos validacionesAtributos = new ValidacionesAtributos();

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping()
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

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        Optional<Evento> evento = eventoService.buscarPorId(id);
        if (evento.isPresent()) {
            EntityModel<Evento> dto = crearDTOEvento(evento.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping("/eventosSuscritos")
    public ResponseEntity<?> eventosSuscritos(
            @RequestParam(name = "idUsuario", required = false) String idUsuario){
        try {
            List<Evento> resultado = suscripcionService.eventosSuscritos(idUsuario);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Evento>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOEvento(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping("/eventosSolicitud")
    public ResponseEntity<?> eventosSolicitud(
            @RequestParam(name = "idUsuario", required = false) String idUsuario){
        try {
            List<Evento> resultado = solicitudService.eventosSolicitados(idUsuario);
            if (resultado.isEmpty()) { return new ResponseEntity<>(HttpStatus.NO_CONTENT); }
            List<EntityModel<Evento>> resultadoDTO = new ArrayList<>();
            resultado.forEach(i -> resultadoDTO.add(crearDTOEvento(i)));
            return new ResponseEntity<>(resultadoDTO, HttpStatus.OK);
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "eventosCategoria/{idCategoria}")
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

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE') or hasRole('ROLE_USUARIO')")
    @GetMapping(path = "eventosCategoriaValidos/{idCategoria}")
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


    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> crear(@Valid @RequestBody Evento evento) {
        try {
            validacionesAtributos.comprobarInsertarModificarEvento(evento);
            Evento nuevoEvento = eventoService.crear(evento);
            EntityModel<Evento> dto = crearDTOEvento(nuevoEvento);
            URI uri = crearURIEvento(nuevoEvento);
            return ResponseEntity.created(uri).body(dto);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE')")
    @PostMapping(path = "/modificar/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modificar(@PathVariable("id") Long id, @Valid @RequestBody Evento evento) {
        try {
            validacionesAtributos.comprobarInsertarModificarEvento(evento);
            EntityModel<Evento> dto = crearDTOEvento(eventoService.modificar(id, evento));
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE')")
    @PostMapping(path = "/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable("id") Long id) {
        try {
            eventoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE')")
    @PostMapping(value ="uploadImagenEvento", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<?> uploadImagenEvento(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("id") Long id) {
        try {
            String path = storageService.store(multipartFile, id, "imagenEvento");
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

    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENTE')")
    @PostMapping(value ="uploadDocumentoEvento", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<?> uploadDocumentoEvento(@RequestParam("file") MultipartFile multipartFile, @RequestHeader("id") Long id) {
        try {
            String path = storageService.store(multipartFile, id, "documentoEvento");
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

    private EntityModel<Evento> crearDTOEvento(Evento evento) {
        Long id = evento.getId();
        EntityModel<Evento> dto = EntityModel.of(evento);
        return dto;
    }

    private URI crearURIEvento(Evento evento) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(evento.getId()).toUri();
    }
}
