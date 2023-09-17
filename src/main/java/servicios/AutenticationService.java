package servicios;

import javax.mail.MessagingException;

import dtos.DatosLogin;
import dtos.RespuestaJWT;
import entidades.Usuario;
import excepciones.AccionException;

public interface AutenticationService {
    public RespuestaJWT login(DatosLogin datosLogin) throws AccionException;
    public void registrarUsuario(Usuario usuario) throws AccionException;
    public void recuperarPasswdUsuario(final String usuario, final String emailUsuario, final String idioma) throws AccionException, MessagingException;
}
