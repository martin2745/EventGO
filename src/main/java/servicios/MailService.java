package servicios;

import autenticacion.Mail;

import javax.mail.MessagingException;

public interface MailService {
    public String enviarCorreo(final Mail mail) throws MessagingException;
    public String generarPasswdAleatoria();
    public String contenidoCorreo(final String fechaEmail, final String mensajeEmail, final String idioma);
    public String fechaCorreo(final String idioma);
    public String asuntoCorreo(final String idioma);
    public String mensajeCorreoCambioPassword(final String idioma, final String passwdTemp);
}
