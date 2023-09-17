package controllers;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entidades.Usuario;
import excepciones.AccionException;
import excepciones.AtributoException;
import servicios.AutenticationService;
import validaciones.CodigosRespuesta;
import validaciones.ValidacionesAtributos;
import dtos.DatosLogin;
import dtos.MensajeRespuesta;
import dtos.RespuestaJWT;
import dtos.RecuperarPassword;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/auth")
public class AutenticacionController {

    @Autowired
    AutenticationService autenticationService;

    private final ValidacionesAtributos validacionesAtributos = new ValidacionesAtributos();

    @PostMapping(path = "login")
    public ResponseEntity<?> login(@RequestBody @Valid DatosLogin datosLogin) {
        try {
            validacionesAtributos.comprobarLogin(datosLogin);
            RespuestaJWT respuestaJWT = autenticationService.login(datosLogin);
            return ResponseEntity.ok(respuestaJWT);
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PostMapping(path = "registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid Usuario usuario) {
        try {
            validacionesAtributos.comprobarInsercionUsuario(usuario);
            autenticationService.registrarUsuario(usuario);
            return ResponseEntity.ok(new MensajeRespuesta(CodigosRespuesta.USUARIO_REGISTRO_OK.getCode(), CodigosRespuesta.USUARIO_REGISTRO_OK.getCode()));
        }catch(final AtributoException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final AccionException e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(e.getCode(), e.getMessage()));
        }catch(final Exception e) {
            return ResponseEntity.badRequest().body(new MensajeRespuesta(CodigosRespuesta.ERROR_INESPERADO.getCode(), CodigosRespuesta.ERROR_INESPERADO.getMsg()));
        }
    }

    @PostMapping(value = "recuperarPassword")
    public ResponseEntity<?> RecuperarPassword(@RequestBody @Valid RecuperarPassword recuperarPassword) {
        try {
            validacionesAtributos.comprobarRecuperarPassword(recuperarPassword);
            autenticationService.recuperarPasswdUsuario(recuperarPassword.getLogin(), recuperarPassword.getEmail(), recuperarPassword.getIdioma());
            return ResponseEntity.ok(new MensajeRespuesta(CodigosRespuesta.RECUPERAR_PASS_OK.getCode(), CodigosRespuesta.RECUPERAR_PASS_OK.getCode()));

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
}
