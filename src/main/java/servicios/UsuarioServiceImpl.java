package servicios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import autenticacion.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import daos.UsuarioDAO;
import entidades.Usuario;
import excepciones.AccionException;
import servicios.UsuarioService;
import validaciones.CodigosRespuesta;
import validaciones.Constantes;

import javax.mail.MessagingException;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Usuario> buscarTodos(String login, String nombre, String email, String rol, String dni, String fechaNacimiento, String pais, String imagenUsuario) {
        List<Usuario> resultado = new ArrayList<Usuario>();

        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            resultado = usuarioDAO.buscarTodos(login, nombre, email, rol, dni, null, pais, imagenUsuario);
        } else {
            resultado = usuarioDAO.buscarTodos(login, nombre, email, rol, dni, fechaNacimiento, pais, imagenUsuario);
        }

        return resultado;
    }

    @Override
    @Transactional
    public Usuario crear(Usuario usuario) throws AccionException{
        if (usuarioDAO.existsByLogin(usuario.getLogin())) {
            throw new AccionException(CodigosRespuesta.LOGIN_YA_EXISTE.getCode(), CodigosRespuesta.LOGIN_YA_EXISTE.getMsg());
        }
        else if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            throw new AccionException(CodigosRespuesta.EMAIL_YA_EXISTE.getCode(), CodigosRespuesta.EMAIL_YA_EXISTE.getMsg());
        }
        /*else if (usuarioDAO.existsByDni(usuario.getDni())) {
            throw new AccionException(CodigosRespuesta.DNI_YA_EXISTE.getCode(), CodigosRespuesta.DNI_YA_EXISTE.getMsg());
        }*/
        return usuarioDAO.save(usuario);

    }

    @Override
    @Transactional
    public Usuario modificar(Long id, Usuario usuario, String loginHeader, final String idioma) throws AccionException, MessagingException {

        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);
        Optional<Usuario> usuarioAdmin = usuarioDAO.findByRol("ROLE_ADMINISTRADOR");
        List<Usuario> usuarioLogin = usuarioDAO.findUsuarioByLoginContaining(usuario.getLogin());
        List<Usuario> usuarioEmail = usuarioDAO.findUsuarioByEmailContaining(usuario.getEmail());
        List<Usuario> usuarioDni = usuarioDAO.findUsuarioByDniContaining(usuario.getDni());

        if (this.loginYaExiste(id, usuarioLogin)) {
            throw new AccionException(CodigosRespuesta.LOGIN_YA_EXISTE.getCode(), CodigosRespuesta.LOGIN_YA_EXISTE.getMsg());
        }
        else if (this.emailYaExiste(id, usuarioEmail)) {
            throw new AccionException(CodigosRespuesta.EMAIL_YA_EXISTE.getCode(), CodigosRespuesta.EMAIL_YA_EXISTE.getMsg());
        }
        /*else if (!usuarioDni.isEmpty() && usuarioDni.stream().noneMatch(u -> u.getId() == id)) {
            throw new AccionException(CodigosRespuesta.DNI_YA_EXISTE.getCode(), CodigosRespuesta.DNI_YA_EXISTE.getMsg());
        }*/
        else if (("ROLE_ADMINISTRADOR".equals(usuario.getRol()) && usuarioOptional.get().getId() != usuarioAdmin.get().getId()) ||
                (!("ROLE_ADMINISTRADOR".equals(usuario.getRol())) && id == usuarioAdmin.get().getId())) {
            throw new AccionException(CodigosRespuesta.NO_EDIRTAR_ROL_ADMINISTRADOR.getCode(), CodigosRespuesta.NO_EDIRTAR_ROL_ADMINISTRADOR.getMsg());
        }
        else if(!(usuarioOptional.get().getLogin().equals(loginHeader)) && !("admin".equals(loginHeader))){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }
        //Excepción futura, no se puede cambiar de Gerente a Usuario si tienes eventos activos asignados

        if (usuarioOptional.isPresent()) {
            if(!usuarioOptional.get().getPassword().equals(usuario.getPassword())){

                final String fechaEmail = mailService.fechaCorreo(idioma);
                final String asuntoEmail = mailService.asuntoCorreo(idioma);
                final String mensajeEmail = mailService.mensajeCorreoCambioPassword(idioma, usuario.getPassword());
                final String contenidoEmail = mailService.contenidoCorreo(fechaEmail, mensajeEmail, idioma);

                final Mail email = new Mail(Constantes.EMISOR_EMAIL, usuarioOptional.get().getEmail(), asuntoEmail,
                        contenidoEmail, Constantes.TIPO_CONTENIDO, null);

                final String result = mailService.enviarCorreo(email);

                if (result.equals("")) {
                    throw new AccionException(CodigosRespuesta.MAIL_NO_ENVIADO.getCode(), CodigosRespuesta.MAIL_NO_ENVIADO.getMsg());
                } else {
                    final String passEncrypt = passwordEncoder.encode(usuario.getPassword());
                    usuario.setPassword(passEncrypt);
                    return usuarioDAO.save(usuario);
                }
            }else{
                return usuarioDAO.save(usuario);
            }
        } else {
            throw new AccionException(CodigosRespuesta.USUARIO_NO_EXISTE.getCode(), CodigosRespuesta.USUARIO_NO_EXISTE.getMsg());
        }
    }

    public boolean loginYaExiste(Long id, List <Usuario> usuarioLogin){
        boolean toret = false;

        if(!usuarioLogin.isEmpty()){
            for(Usuario usuario : usuarioLogin){
                if(id != usuario.getId()){
                    toret = true;
                }
            }
        }

        return toret;
    }

    public boolean emailYaExiste(Long id, List <Usuario> usuarioEmail){
        boolean toret = false;

        if(!usuarioEmail.isEmpty()){
            for(Usuario usuario : usuarioEmail){
                if(id != usuario.getId()){
                    toret = true;
                }
            }
        }

        return toret;
    }

    @Override
    @Transactional
    public void eliminar(Long id) throws AccionException{
        Optional<Usuario> usuario = usuarioDAO.findById(id);
        Optional<Usuario> usuarioAdmin = usuarioDAO.findByRol("ROLE_ADMINISTRADOR");
        if (usuario.isPresent()) {
            if(usuarioAdmin.get().getId() == id) {
                throw new AccionException(CodigosRespuesta.NO_ELIMINAR_ADMINISTRADOR.getCode(), CodigosRespuesta.NO_ELIMINAR_ADMINISTRADOR.getMsg());
            }else {
                usuarioDAO.delete(usuario.get());
            }
        } else {
            throw new AccionException(CodigosRespuesta.USUARIO_NO_EXISTE.getCode(), CodigosRespuesta.USUARIO_NO_EXISTE.getMsg());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioDAO.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        // TODO Auto-generated method stub
        return usuarioDAO.findFirstByLogin(login);
    }
}