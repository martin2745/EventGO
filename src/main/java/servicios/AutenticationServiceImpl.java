package servicios;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dtos.DatosLogin;
import dtos.MensajeRespuesta;
import dtos.RespuestaJWT;
import daos.UsuarioDAO;
import entidades.Usuario;
import excepciones.AccionException;
import excepciones.AtributoException;
import autenticacion.Mail;
import jwt.UtilidadesJWT;
import validaciones.CodigosRespuesta;
import validaciones.Constantes;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class AutenticationServiceImpl implements AutenticationService{

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UtilidadesJWT utilidadesJWT;

    public RespuestaJWT login(DatosLogin datosLogin) throws AccionException{
        String login = datosLogin.getLogin();
        String password = datosLogin.getPassword();
        Optional<Usuario> usuarioOptional = usuarioDAO.findFirstByLogin(login);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                throw new AccionException(CodigosRespuesta.LOGIN_PASSWORD_NO_COINCIDEN.getCode(), CodigosRespuesta.LOGIN_PASSWORD_NO_COINCIDEN.getMsg());
            }
            else if(usuario.getBorradoLogico().equals("1")){
                throw new AccionException(CodigosRespuesta.USUARIO_BORRADO_LOGICO.getCode(), CodigosRespuesta.USUARIO_BORRADO_LOGICO.getMsg());
            }
            else{
                String token = utilidadesJWT.crearTokenJWT(login);
                return new RespuestaJWT(token, usuario.getId(), usuario.getLogin(), usuario.getRol());
            }
        }
        else {
            throw new AccionException(CodigosRespuesta.LOGIN_NO_EXISTE.getCode(), CodigosRespuesta.LOGIN_NO_EXISTE.getMsg());
        }
    }

    public void registrarUsuario(Usuario usuario) throws AccionException{
        if (usuarioDAO.existsByLogin(usuario.getLogin())) {
            throw new AccionException(CodigosRespuesta.LOGIN_YA_EXISTE.getCode(), CodigosRespuesta.LOGIN_YA_EXISTE.getMsg());
        }
        else if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            throw new AccionException(CodigosRespuesta.EMAIL_YA_EXISTE.getCode(), CodigosRespuesta.EMAIL_YA_EXISTE.getMsg());
        }
        else if (usuario.getRol().equals("ROLE_ADMINISTRADOR")) {
            throw new AccionException(CodigosRespuesta.NO_CREAR_ADMINISTRADOR.getCode(), CodigosRespuesta.NO_CREAR_ADMINISTRADOR.getMsg());
        }
        Usuario nuevoUsuario = new Usuario(usuario.getLogin(), passwordEncoder.encode(usuario.getPassword()),usuario.getNombre(), usuario.getEmail(), usuario.getRol(), usuario.getDni(), usuario.getFechaNacimiento(), usuario.getPais(), usuario.getImagenUsuario(), "0");
        usuarioDAO.save(nuevoUsuario);
    }

    @Override
    public void recuperarPasswdUsuario(final String login, final String emailUsuario, final String idioma) throws AccionException, MessagingException {
        String passwdTemp = "";

        Optional<Usuario> user = usuarioDAO.findFirstByLogin(login);

        if (user.isPresent()) {
            if (user.get().getEmail().equals(emailUsuario)) {

                passwdTemp = mailService.generarPasswdAleatoria();

                final String fechaEmail = mailService.fechaCorreo(idioma);
                final String asuntoEmail = mailService.asuntoCorreo(idioma);
                final String mensajeEmail = mailService.mensajeCorreoCambioPassword(idioma, passwdTemp);
                final String contenidoEmail = mailService.contenidoCorreo(fechaEmail, mensajeEmail, idioma);

                final Mail email = new Mail(Constantes.EMISOR_EMAIL, emailUsuario, asuntoEmail,
                        contenidoEmail, Constantes.TIPO_CONTENIDO, null);

                final String result = mailService.enviarCorreo(email);

                if (result.equals("")) {
                    throw new AccionException(CodigosRespuesta.MAIL_NO_ENVIADO.getCode(), CodigosRespuesta.MAIL_NO_ENVIADO.getMsg());
                } else {
                    final String passEncrypt = passwordEncoder.encode(passwdTemp);
                    user.get().setPassword(passEncrypt);
                    usuarioDAO.save(user.get());
                }
            } else {
                throw new AccionException(CodigosRespuesta.EMAIL_NO_ENCONTRADO.getCode(), CodigosRespuesta.EMAIL_NO_ENCONTRADO.getMsg());
            }
        }  else {
            throw new AccionException(CodigosRespuesta.USUARIO_NO_ENCONTRADO.getCode(), CodigosRespuesta.USUARIO_NO_ENCONTRADO.getMsg());
        }
    }
}