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
    JavaMailSender javaMailSender;

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
        else if (usuarioDAO.existsByDni(usuario.getDni())) {
            throw new AccionException(CodigosRespuesta.DNI_YA_EXISTE.getCode(), CodigosRespuesta.DNI_YA_EXISTE.getMsg());
        }

        Usuario nuevoUsuario = new Usuario(usuario.getLogin(), passwordEncoder.encode(usuario.getPassword()),usuario.getNombre(), usuario.getEmail(), usuario.getRol(), usuario.getDni(), usuario.getFechaNacimiento(), usuario.getPais(), usuario.getImagenUsuario());
        usuarioDAO.save(nuevoUsuario);
    }

    @Override
    public void recuperarPasswdUsuario(final String login, final String emailUsuario, final String idioma) throws AccionException, MessagingException {
        String passwdTemp = "";

        Optional<Usuario> user = usuarioDAO.findFirstByLogin(login);

        if (user.isPresent()) {
            if (user.get().getEmail().equals(emailUsuario)) {

                passwdTemp = generarPasswdAleatoria();

                final String fechaEmail = fechaCorreo(idioma);
                final String asuntoEmail = asuntoCorreo(idioma);
                final String mensajeEmail = mensajeCorreo(idioma, passwdTemp);
                final String contenidoEmail = contenidoCorreo(fechaEmail, mensajeEmail, idioma);

                final Mail email = new Mail(Constantes.EMISOR_EMAIL, emailUsuario, asuntoEmail,
                        contenidoEmail, Constantes.TIPO_CONTENIDO, null);

                final String result = enviarCorreo(email);

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

        //return resultado;
    }

    private String enviarCorreo(final Mail mail) throws MessagingException {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(mail.getReceptorEmail());
        mimeMessageHelper.setFrom(mail.getEmisorEmail());
        mimeMessageHelper.setSubject(mail.getAsuntoEmail());
        mimeMessageHelper.setText(mail.getContenidoEmail());
        javaMailSender.send(mimeMessageHelper.getMimeMessage());

        return Constantes.OK;

    }

    private String generarPasswdAleatoria() {
        final Random rand = new Random();
        String passwdTemporal = "";
        final char[] caract = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'e', 'h', 'i', 'j', 'l', 'k', 'm', 'n', 'o', 'p',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9' };

        for (int i = 0; i < 10; i++) {
            passwdTemporal += caract[rand.nextInt(caract.length)];
        }

        return passwdTemporal;
    }

    private String contenidoCorreo(final String fechaEmail, final String mensajeEmail, final String idioma) {
        String mensaje = "";

        switch (idioma) {
            case "es":
                mensaje = Constantes.TABULACION_FECHA + fechaEmail + Constantes.SALTO_LINEA + Constantes.SALUDO_ES
                        + Constantes.SALTO_LINEA + mensajeEmail + Constantes.SALTO_LINEA + Constantes.SALTO_LINEA
                        + Constantes.TABULACION_DESPEDIDA + Constantes.DESPEDIDA_ES + Constantes.SALTO_LINEA
                        + Constantes.TABULACION_FIRMA + Constantes.FIRMA_ES;
                break;
            case "ga":
                mensaje = Constantes.TABULACION_FECHA + fechaEmail + Constantes.SALTO_LINEA + Constantes.SALUDO_GA
                        + Constantes.SALTO_LINEA + mensajeEmail + Constantes.SALTO_LINEA + Constantes.SALTO_LINEA
                        + Constantes.TABULACION_DESPEDIDA + Constantes.DESPEDIDA_GA + Constantes.SALTO_LINEA
                        + Constantes.TABULACION_FIRMA + Constantes.FIRMA_GA;
                break;
            case "en":
                mensaje = Constantes.TABULACION_FECHA + fechaEmail + Constantes.SALTO_LINEA + Constantes.SALUDO_EN
                        + Constantes.SALTO_LINEA + mensajeEmail + Constantes.SALTO_LINEA + Constantes.SALTO_LINEA
                        + Constantes.TABULACION_DESPEDIDA + Constantes.DESPEDIDA_EN + Constantes.SALTO_LINEA
                        + Constantes.TABULACION_FIRMA + Constantes.FIRMA_EN;
                break;
            default:
                break;
        }

        return mensaje;
    }

    private String fechaCorreo(final String idioma) {
        final String dia = Integer.toString(LocalDate.now().getDayOfMonth());
        final Month mes = LocalDate.now().getMonth();
        final String annio = Integer.toString(LocalDate.now().getYear());
        String mesNombre = "";
        String fechaEmail = "";

        switch (idioma) {
            case "es":
                mesNombre = mes.getDisplayName(TextStyle.FULL, new Locale(idioma, "ES"));
                fechaEmail = String.format(Constantes.FECHA_ES, dia, mesNombre, annio);
                break;
            case "ga":
                mesNombre = mes.getDisplayName(TextStyle.FULL, new Locale("gl", "ES"));
                fechaEmail = String.format(Constantes.FECHA_GA, dia, mesNombre, annio);
                break;
            case "en":
                mesNombre = mes.getDisplayName(TextStyle.FULL, new Locale(idioma, "GB"));
                fechaEmail = String.format(Constantes.FECHA_EN, dia, mesNombre, annio);
                break;
            default:
                break;
        }

        return fechaEmail;
    }

    private String asuntoCorreo(final String idioma) {
        String asuntoEmail = "";

        switch (idioma) {
            case "es":
                asuntoEmail = Constantes.ASUNTO_ES;
                break;
            case "ga":
                asuntoEmail = Constantes.ASUNTO_GA;
                break;
            case "en":
                asuntoEmail = Constantes.ASUNTO_EN;
                break;
            default:
                break;
        }

        return asuntoEmail;
    }

    private String mensajeCorreo(final String idioma, final String passwdTemp) {
        String mensajeEmail = "";

        switch (idioma) {
            case "es":
                mensajeEmail = String.format(Constantes.CUERPO_ES, passwdTemp);
                break;
            case "ga":
                mensajeEmail = String.format(Constantes.CUERPO_GA, passwdTemp);
                break;
            case "en":
                mensajeEmail = String.format(Constantes.CUERPO_EN, passwdTemp);
                break;
            default:
                break;
        }

        return mensajeEmail;
    }
}