package servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import daos.UsuarioDAO;
import entidades.Usuario;
import excepciones.AccionException;
import servicios.UsuarioService;
import validaciones.CodigosRespuesta;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioDAO usuarioDAO;

    public List<Usuario> buscarTodos(String login, String nombre, String email) {
        List<Usuario> resultado = new ArrayList<Usuario>();
        resultado = usuarioDAO.buscarTodos(login, nombre, email);
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
        else if (usuarioDAO.existsByDni(usuario.getDni())) {
            throw new AccionException(CodigosRespuesta.DNI_YA_EXISTE.getCode(), CodigosRespuesta.DNI_YA_EXISTE.getMsg());
        }
        return usuarioDAO.save(usuario);

    }

    @Override
    @Transactional
    public Usuario modificar(Long id, Usuario usuario) throws AccionException{

        Optional<Usuario> usuarioOptional = usuarioDAO.findById(id);
        Optional<Usuario> usuarioAdmin = usuarioDAO.findByRol("ROLE_ADMINISTRADOR");
        List<Usuario> usuarioLogin = usuarioDAO.findUsuarioByLoginContaining(usuario.getLogin());
        List<Usuario> usuarioEmail = usuarioDAO.findUsuarioByEmailContaining(usuario.getEmail());
        List<Usuario> usuarioDni = usuarioDAO.findUsuarioByDniContaining(usuario.getDni());

        if (!usuarioLogin.isEmpty() && usuarioLogin.stream().noneMatch(u -> u.getId() == id)) {
            throw new AccionException(CodigosRespuesta.LOGIN_YA_EXISTE.getCode(), CodigosRespuesta.LOGIN_YA_EXISTE.getMsg());
        }
        else if (!usuarioEmail.isEmpty() && usuarioEmail.stream().noneMatch(u -> u.getId() == id)) {
            throw new AccionException(CodigosRespuesta.EMAIL_YA_EXISTE.getCode(), CodigosRespuesta.EMAIL_YA_EXISTE.getMsg());
        }
        else if (!usuarioDni.isEmpty() && usuarioDni.stream().noneMatch(u -> u.getId() == id)) {
            throw new AccionException(CodigosRespuesta.DNI_YA_EXISTE.getCode(), CodigosRespuesta.DNI_YA_EXISTE.getMsg());
        }
        else if (("ROLE_ADMINISTRADOR".equals(usuario.getRol()) && usuarioOptional.get().getId() != usuarioAdmin.get().getId()) ||
                (!("ROLE_ADMINISTRADOR".equals(usuario.getRol())) && id == usuarioAdmin.get().getId())) {
            throw new AccionException(CodigosRespuesta.NO_EDIRTAR_ROL_ADMINISTRADOR.getCode(), CodigosRespuesta.NO_EDIRTAR_ROL_ADMINISTRADOR.getMsg());
        }
        //Excepción futura, no se puede cambiar de Gerente a Usuario si tienes eventos activos asignados

        if (usuarioOptional.isPresent()) {
            return usuarioDAO.save(usuario);
        } else {
            throw new AccionException(CodigosRespuesta.USUARIO_NO_EXISTE.getCode(), CodigosRespuesta.USUARIO_NO_EXISTE.getMsg());
        }
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