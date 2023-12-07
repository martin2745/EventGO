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
import daos.EventoDAO;
import daos.UsuarioDAO;
import daos.AmistadDAO;
import daos.ComentarioDAO;
import daos.NoticiaDAO;
import daos.SuscripcionDAO;
import daos.SolicitudDAO;
import entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import excepciones.AccionException;
import validaciones.CodigosRespuesta;
import validaciones.Constantes;

import javax.mail.MessagingException;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private EventoDAO eventoDAO;

    @Autowired
    private AmistadDAO amistadDAO;

    @Autowired
    private ComentarioDAO comentarioDAO;

    @Autowired
    private NoticiaDAO noticiaDAO;

    @Autowired
    private SolicitudDAO solicitudDAO;

    @Autowired
    private SuscripcionDAO suscripcionDAO;

    @Autowired
    MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Usuario> buscarTodos(String login, String nombre, String email, String rol, String dni, String fechaNacimiento, String pais, String imagenUsuario, String borradoLogico) {
        List<Usuario> resultado = new ArrayList<Usuario>();

        if (fechaNacimiento == null || fechaNacimiento.isEmpty()) {
            resultado = usuarioDAO.buscarTodos(login, nombre, email, rol, dni, null, pais, imagenUsuario, borradoLogico);
        } else {
            resultado = usuarioDAO.buscarTodos(login, nombre, email, rol, dni, fechaNacimiento, pais, imagenUsuario, borradoLogico);
        }

        return resultado;
    }

    public List<Usuario> buscarGerentes(Long id){
        /*Devolver gerentes que no sigo en este momento*/
        Optional<Usuario> usuario = usuarioDAO.findById(id);
        List<Usuario> toret = new ArrayList<Usuario>();
        List<Usuario> gerentes = new ArrayList<Usuario>();
        List<Amistad> amistades = new ArrayList<>();
        gerentes = usuarioDAO.findUsuarioByRolContaining("ROLE_GERENTE");
        amistades = amistadDAO.findBySeguidor(usuario.get());

        for (Usuario gerente : gerentes) {
            if(!gerente.getId().equals(id)){
                boolean seguir = true;
                for (Amistad amistad : amistades) {
                    if (gerente.getId().equals(amistad.getGerente().getId())) {
                        seguir = false;
                        break;
                    }
                }
                if (seguir) {
                    toret.add(gerente);
                }
            }
        }
        return toret;
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
        else if (usuario.getRol().equals("ROLE_ADMINISTRADOR")) {
            throw new AccionException(CodigosRespuesta.NO_CREAR_ADMINISTRADOR.getCode(), CodigosRespuesta.NO_CREAR_ADMINISTRADOR.getMsg());
        }
        usuario.setBorradoLogico("0");
        return usuarioDAO.save(usuario);

    }

    @Override
    @Transactional
    public Usuario modificar(Long id, Usuario usuario, final String idioma) throws AccionException, MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsuarioSistema = authentication.getName();

        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);
        List<Usuario> usuarioLogin = usuarioDAO.findUsuarioByLoginContaining(usuario.getLogin());
        List<Usuario> usuarioEmail = usuarioDAO.findUsuarioByEmailContaining(usuario.getEmail());

        if (this.loginYaExiste(id, usuarioLogin)) {
            throw new AccionException(CodigosRespuesta.LOGIN_YA_EXISTE.getCode(), CodigosRespuesta.LOGIN_YA_EXISTE.getMsg());
        }
        else if (this.emailYaExiste(id, usuarioEmail)) {
            throw new AccionException(CodigosRespuesta.EMAIL_YA_EXISTE.getCode(), CodigosRespuesta.EMAIL_YA_EXISTE.getMsg());
        }
        else if (!usuarioOptional.get().getRol().equals(usuario.getRol())) {
            throw new AccionException(CodigosRespuesta.NO_EDIRTAR_ROL.getCode(), CodigosRespuesta.NO_EDIRTAR_ROL.getMsg());
        }
        else if(!(usuarioOptional.get().getLogin().equals(loginUsuarioSistema)) && !("admin".equals(loginUsuarioSistema))){
            throw new AccionException(CodigosRespuesta.PERMISO_DENEGADO.getCode(), CodigosRespuesta.PERMISO_DENEGADO.getMsg());
        }

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
                List<Evento> eventos = eventoDAO.findByUsuarioId(id);
                List<Amistad> amistadesGerente = amistadDAO.findByGerenteId(id);
                List<Amistad> amistadesSeguidor = amistadDAO.findBySeguidorId(id);
                List<Comentario> comentarios = comentarioDAO.findByUsuarioId(id);
                List<Noticia> noticias = noticiaDAO.findByUsuarioId(id);
                List<Solicitud> solicitudes = solicitudDAO.findByUsuarioId(id);
                List<Suscripcion> suscripcion = suscripcionDAO.findByUsuarioId(id);
                if (!eventos.isEmpty()) {
                    //Si hay eventos creados el usuario se borra de forma lógicamente y el estado de los eventos se pone en CERRADO --> Nadie más se inscribe en ellos
                    for (Evento evento: eventos){
                        evento.setEstado("CERRADO");
                    }
                    usuario.get().setBorradoLogico("1");
                    usuarioDAO.save(usuario.get());
                }
                else if(!amistadesGerente.isEmpty() || !amistadesSeguidor.isEmpty() || !comentarios.isEmpty() ||
                !noticias.isEmpty() || !solicitudes.isEmpty() || !suscripcion.isEmpty()){
                    //Si existe alguna relación de las anteriores con usuario se borra de forma lógica.
                    usuario.get().setBorradoLogico("1");
                    usuarioDAO.save(usuario.get());
                }else {
                    usuarioDAO.delete(usuario.get());
                }
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