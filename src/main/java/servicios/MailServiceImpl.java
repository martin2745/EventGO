package servicios;

import autenticacion.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import validaciones.Constantes;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService{

    @Autowired
    JavaMailSender javaMailSender;

    public String enviarCorreo(final Mail mail) throws MessagingException {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(mail.getReceptorEmail());
        mimeMessageHelper.setFrom(mail.getEmisorEmail());
        mimeMessageHelper.setSubject(mail.getAsuntoEmail());
        mimeMessageHelper.setText(mail.getContenidoEmail());
        javaMailSender.send(mimeMessageHelper.getMimeMessage());

        return Constantes.OK;

    }

    public String generarPasswdAleatoria() {
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

    public String contenidoCorreo(final String fechaEmail, final String mensajeEmail, final String idioma) {
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

    public String fechaCorreo(final String idioma) {
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

    public String asuntoCorreo(final String idioma) {
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

    public String mensajeCorreoCambioPassword(final String idioma, final String passwdTemp) {
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
