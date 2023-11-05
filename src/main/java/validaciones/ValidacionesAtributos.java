package validaciones;

import dtos.DatosLogin;
import dtos.RecuperarPassword;
import entidades.Evento;
import entidades.Usuario;
import entidades.Categoria;
import excepciones.AtributoException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import antlr.StringUtils;

@Component
public class ValidacionesAtributos {

    //AUTENTICACIÓN
    public void comprobarLogin(final DatosLogin datosLogin) throws AtributoException{
        if (datosLogin == null || isBlank(datosLogin.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_VACIO.getCode(), CodigosRespuesta.LOGIN_VACIO.getMsg());
        }else if(isBlank(datosLogin.getPassword())) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_VACIO.getCode(), CodigosRespuesta.PASSWORD_VACIO.getMsg());
        }else if(!esAlfanumerico(datosLogin.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_ALFANUMERICO.getCode(), CodigosRespuesta.LOGIN_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(datosLogin.getLogin(), 3)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(datosLogin.getLogin(), 15)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getMsg());
        }else if(!esAlfanumerico(datosLogin.getPassword())) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_FORMATO.getCode(), CodigosRespuesta.PASSWORD_FORMATO.getMsg());
        }else if(!tamanhoMinimo(datosLogin.getPassword(), 3)) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_TAMANHO_MINIMO.getCode(), CodigosRespuesta.PASSWORD_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(datosLogin.getPassword(), 16)) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.PASSWORD_TAMANHO_MAXIMO.getMsg());
        }
    }

    public void comprobarInsercionUsuario(final Usuario usuario) throws AtributoException{
        if (usuario == null || isBlank(usuario.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_VACIO.getCode(), CodigosRespuesta.LOGIN_VACIO.getMsg());
        }else if(isBlank(usuario.getPassword())) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_VACIO.getCode(), CodigosRespuesta.PASSWORD_VACIO.getMsg());
        }else if(isBlank(usuario.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_VACIO.getCode(), CodigosRespuesta.NOMBRE_VACIO.getMsg());
        }else if(isBlank(usuario.getEmail())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_VACIO.getCode(), CodigosRespuesta.EMAIL_VACIO.getMsg());
        }else if(isBlank(usuario.getRol())) {
            throw new AtributoException(CodigosRespuesta.ROL_VACIO.getCode(), CodigosRespuesta.ROL_VACIO.getMsg());
        }else if(isBlank(usuario.getDni())) {
            throw new AtributoException(CodigosRespuesta.DNI_VACIO.getCode(), CodigosRespuesta.DNI_VACIO.getMsg());
        }else if(usuario.getFechaNacimiento() == null) {
            throw new AtributoException(CodigosRespuesta.FECHA_VACIO.getCode(), CodigosRespuesta.FECHA_VACIO.getMsg());
        }else if(isBlank(usuario.getPais())) {
            throw new AtributoException(CodigosRespuesta.PAIS_VACIO.getCode(), CodigosRespuesta.PAIS_VACIO.getMsg());
        }else if(!esAlfanumerico(usuario.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_ALFANUMERICO.getCode(), CodigosRespuesta.LOGIN_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getLogin(), 3)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getLogin(), 15)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getMsg());
        }else if(!esAlfanumerico(usuario.getPassword())) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_FORMATO.getCode(), CodigosRespuesta.PASSWORD_FORMATO.getMsg());
        }else if(!tamanhoMinimo(usuario.getPassword(), 3)) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_TAMANHO_MINIMO.getCode(), CodigosRespuesta.PASSWORD_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getPassword(), 16)) {
            throw new AtributoException(CodigosRespuesta.PASSWORD_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.PASSWORD_TAMANHO_MAXIMO.getMsg());
        }else if(!esAlfabeticoEspacio(usuario.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_ALFABETICO.getCode(), CodigosRespuesta.NOMBRE_ALFABETICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getNombre(), 3)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_TAMANHO_MINIMO.getCode(), CodigosRespuesta.NOMBRE_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getNombre(), 40)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.NOMBRE_TAMANHO_MAXIMO.getMsg());
        }else if(!esCorreoElectronicoValido(usuario.getEmail())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
        }else if(!tamanhoMinimo(usuario.getEmail(), 3)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getEmail(), 40)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getMsg());
        }else if(!esRol(usuario.getRol())) {
            throw new AtributoException(CodigosRespuesta.ROL_ALFABETICO.getCode(), CodigosRespuesta.ROL_ALFABETICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getRol(), 3)) {
            throw new AtributoException(CodigosRespuesta.ROL_TAMANHO_MINIMO.getCode(), CodigosRespuesta.ROL_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getRol(), 20)) {
            throw new AtributoException(CodigosRespuesta.ROL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.ROL_TAMANHO_MAXIMO.getMsg());
        }else if(!esDni(usuario.getDni())) {
            throw new AtributoException(CodigosRespuesta.DNI_FORMATO.getCode(), CodigosRespuesta.DNI_FORMATO.getMsg());
        }else if(!esFecha(usuario.getFechaNacimiento())) {
            throw new AtributoException(CodigosRespuesta.FECHA_FORMATO.getCode(), CodigosRespuesta.FECHA_FORMATO.getMsg());
        }else if(!esAlfabetico(usuario.getPais())) {
            throw new AtributoException(CodigosRespuesta.PAIS_ALFABETICO.getCode(), CodigosRespuesta.PAIS_ALFABETICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getPais(), 3)) {
            throw new AtributoException(CodigosRespuesta.PAIS_TAMANHO_MINIMO.getCode(), CodigosRespuesta.PAIS_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getPais(), 15)) {
            throw new AtributoException(CodigosRespuesta.PAIS_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.PAIS_TAMANHO_MAXIMO.getMsg());
        }
    }

    public void comprobarRecuperarPassword(final RecuperarPassword recuperarPassword) throws AtributoException{
        if (recuperarPassword == null || isBlank(recuperarPassword.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_VACIO.getCode(), CodigosRespuesta.LOGIN_VACIO.getMsg());
        }else if(!esAlfanumerico(recuperarPassword.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_ALFANUMERICO.getCode(), CodigosRespuesta.LOGIN_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(recuperarPassword.getLogin(), 3)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(recuperarPassword.getLogin(), 15)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getMsg());
        }else if(isBlank(recuperarPassword.getEmail())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_VACIO.getCode(), CodigosRespuesta.EMAIL_VACIO.getMsg());
        }else if(!esCorreoElectronicoValido(recuperarPassword.getEmail())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
        }else if(!tamanhoMinimo(recuperarPassword.getEmail(), 3)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(recuperarPassword.getEmail(), 40)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getMsg());
        }
    }

    //USUARIO
    public void usuarioBuscarTodos(String login, String nombre, String email, String rol, String dni, String fechaNacimiento, String pais) throws AtributoException{
        if (!isBlank(login)) {
            if(!esAlfanumerico(login)) {
                throw new AtributoException(CodigosRespuesta.LOGIN_ALFANUMERICO.getCode(), CodigosRespuesta.LOGIN_ALFANUMERICO.getMsg());
            }else if(!tamanhoMaximo(login, 15)) {
                throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getMsg());
            }
        }else if(!isBlank(nombre)) {
            if(!esAlfabeticoEspacio(nombre)) {
                throw new AtributoException(CodigosRespuesta.NOMBRE_ALFABETICO.getCode(), CodigosRespuesta.NOMBRE_ALFABETICO.getMsg());
            }else if(!tamanhoMaximo(nombre, 40)) {
                throw new AtributoException(CodigosRespuesta.NOMBRE_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.NOMBRE_TAMANHO_MAXIMO.getMsg());
            }
        }else if(!isBlank(email)) {
            if(!esCorreoElectronicoValidoBuscar(email)) {
                throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
            }else if(!tamanhoMaximo(email, 40)) {
                throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getMsg());
            }
        }else if(!isBlank(rol)) {
            if(!esRol(rol)) {
                throw new AtributoException(CodigosRespuesta.ROL_ALFABETICO.getCode(), CodigosRespuesta.ROL_ALFABETICO.getMsg());
            }else if(!tamanhoMaximo(rol, 20)) {
                throw new AtributoException(CodigosRespuesta.ROL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.ROL_TAMANHO_MAXIMO.getMsg());
            }
        }else if(!isBlank(dni)) {
            if(!esAlfanumerico(dni)) {
                throw new AtributoException(CodigosRespuesta.DNI_ALFANUMERICO.getCode(), CodigosRespuesta.DNI_ALFANUMERICO.getMsg());
            }
        }else if(!isBlank(pais)) {
            if(!esAlfabetico(pais)) {
                throw new AtributoException(CodigosRespuesta.PAIS_ALFABETICO.getCode(), CodigosRespuesta.PAIS_ALFABETICO.getMsg());
            }else if(!tamanhoMaximo(pais, 15)) {
                throw new AtributoException(CodigosRespuesta.PAIS_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.PAIS_TAMANHO_MAXIMO.getMsg());
            }
        }
    }

    public void comprobarModificarUsuario(final Usuario usuario) throws AtributoException{
        if (usuario == null || isBlank(usuario.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_VACIO.getCode(), CodigosRespuesta.LOGIN_VACIO.getMsg());
        }else if(isBlank(usuario.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_VACIO.getCode(), CodigosRespuesta.NOMBRE_VACIO.getMsg());
        }else if(isBlank(usuario.getEmail())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_VACIO.getCode(), CodigosRespuesta.EMAIL_VACIO.getMsg());
        }else if(isBlank(usuario.getRol())) {
            throw new AtributoException(CodigosRespuesta.ROL_VACIO.getCode(), CodigosRespuesta.ROL_VACIO.getMsg());
        }else if(isBlank(usuario.getDni())) {
            throw new AtributoException(CodigosRespuesta.DNI_VACIO.getCode(), CodigosRespuesta.DNI_VACIO.getMsg());
        }else if(usuario.getFechaNacimiento() == null) {
            throw new AtributoException(CodigosRespuesta.FECHA_VACIO.getCode(), CodigosRespuesta.FECHA_VACIO.getMsg());
        }else if(isBlank(usuario.getPais())) {
            throw new AtributoException(CodigosRespuesta.PAIS_VACIO.getCode(), CodigosRespuesta.PAIS_VACIO.getMsg());
        }else if(!esAlfanumerico(usuario.getLogin())) {
            throw new AtributoException(CodigosRespuesta.LOGIN_ALFANUMERICO.getCode(), CodigosRespuesta.LOGIN_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getLogin(), 3)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getLogin(), 15)) {
            throw new AtributoException(CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.LOGIN_TAMANHO_MAXIMO.getMsg());
        }else if(!esAlfabeticoEspacio(usuario.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_ALFABETICO.getCode(), CodigosRespuesta.NOMBRE_ALFABETICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getNombre(), 3)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_TAMANHO_MINIMO.getCode(), CodigosRespuesta.NOMBRE_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getNombre(), 40)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.NOMBRE_TAMANHO_MAXIMO.getMsg());
        }else if(!esCorreoElectronicoValido(usuario.getEmail())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
        }else if(!tamanhoMinimo(usuario.getEmail(), 3)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getEmail(), 40)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getMsg());
        }else if(!esRol(usuario.getRol())) {
            throw new AtributoException(CodigosRespuesta.ROL_ALFABETICO.getCode(), CodigosRespuesta.ROL_ALFABETICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getRol(), 3)) {
            throw new AtributoException(CodigosRespuesta.ROL_TAMANHO_MINIMO.getCode(), CodigosRespuesta.ROL_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getRol(), 20)) {
            throw new AtributoException(CodigosRespuesta.ROL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.ROL_TAMANHO_MAXIMO.getMsg());
        }else if(!esDni(usuario.getDni())) {
            throw new AtributoException(CodigosRespuesta.DNI_FORMATO.getCode(), CodigosRespuesta.DNI_FORMATO.getMsg());
        }else if(!esFecha(usuario.getFechaNacimiento())) {
            throw new AtributoException(CodigosRespuesta.FECHA_FORMATO.getCode(), CodigosRespuesta.FECHA_FORMATO.getMsg());
        }else if(!esAlfabetico(usuario.getPais())) {
            throw new AtributoException(CodigosRespuesta.PAIS_ALFABETICO.getCode(), CodigosRespuesta.PAIS_ALFABETICO.getMsg());
        }else if(!tamanhoMinimo(usuario.getPais(), 3)) {
            throw new AtributoException(CodigosRespuesta.PAIS_TAMANHO_MINIMO.getCode(), CodigosRespuesta.PAIS_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(usuario.getPais(), 15)) {
            throw new AtributoException(CodigosRespuesta.PAIS_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.PAIS_TAMANHO_MAXIMO.getMsg());
        }
    }

    //CATEGORÍA
    public void comprobarInsertarModificarCategoria(final Categoria categoria) throws AtributoException{
        if (categoria == null || isBlank(categoria.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_VACIO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_VACIO.getMsg());
        }else if(isBlank(categoria.getDescripcion())) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_VACIO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_VACIO.getMsg());
        }else if(!esAlfanumericoEspacio(categoria.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_ALFANUMERICO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(categoria.getNombre(), 3)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MINIMO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(categoria.getNombre(), 15)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MAXIMO.getMsg());
        }else if(!esDescripcion(categoria.getDescripcion())) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_ALFANUMERICO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(categoria.getDescripcion(), 3)) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MINIMO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(categoria.getDescripcion(), 255)) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO.getMsg());
        }
    }

    public void categoriaBuscarTodos(String nombre, String descripcion) throws AtributoException {
        if (!isBlank(nombre)) {
            if(!esAlfanumericoEspacio(nombre)) {
                throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_ALFANUMERICO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_ALFANUMERICO.getMsg());
            }else if(!tamanhoMaximo(nombre, 15)) {
                throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MAXIMO.getMsg());
            }
        }else if(!isBlank(descripcion)) {
            if(!esDescripcion(descripcion)) {
                throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_ALFANUMERICO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_ALFANUMERICO.getMsg());
            }else if(!tamanhoMaximo(descripcion, 255)) {
                throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO.getMsg());
            }
        }
    }

    //Evento
    public void comprobarInsertarModificarEvento(final Evento evento) throws AtributoException{
        if (evento == null || isBlank(evento.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_EVENTO_VACIO.getCode(), CodigosRespuesta.NOMBRE_EVENTO_VACIO.getMsg());
        }else if(isBlank(evento.getDescripcion())) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_EVENTO_VACIO.getCode(), CodigosRespuesta.DESCRIPCION_EVENTO_VACIO.getMsg());
        }else if(isBlank(evento.getTipoAsistencia().toString())) {
            throw new AtributoException(CodigosRespuesta.TIPO_ASISTENCIA_EVENTO_VACIO.getCode(), CodigosRespuesta.TIPO_ASISTENCIA_EVENTO_VACIO.getMsg());
        }else if(isBlank(String.valueOf(evento.getNumAsistentes()))) {
            throw new AtributoException(CodigosRespuesta.NUM_ASISTENTES_EVENTO_VACIO.getCode(), CodigosRespuesta.NUM_ASISTENTES_EVENTO_VACIO.getMsg());
        }else if(isBlank(evento.getFechaEvento())) {
            throw new AtributoException(CodigosRespuesta.FECHA_VACIO.getCode(), CodigosRespuesta.FECHA_VACIO.getMsg());
        }else if(isBlank(evento.getDireccion())) {
            throw new AtributoException(CodigosRespuesta.DIRECCION_VACIO.getCode(), CodigosRespuesta.DIRECCION_VACIO.getMsg());
        }else if(isBlank(evento.getEmailContacto())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_VACIO.getCode(), CodigosRespuesta.EMAIL_VACIO.getMsg());
        }else if(isBlank(evento.getTelefonoContacto())) {
            throw new AtributoException(CodigosRespuesta.TELEFONO_VACIO.getCode(), CodigosRespuesta.TELEFONO_VACIO.getMsg());
        }else if(isBlank(evento.getCategoria().getId().toString())) {
            throw new AtributoException(CodigosRespuesta.CATEGORIA_EVENTO_VACIO.getCode(), CodigosRespuesta.CATEGORIA_EVENTO_VACIO.getMsg());
        }else if(!esAlfanumericoEspacioPunto(evento.getNombre())) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_EVENTO_ALFANUMERICO.getCode(), CodigosRespuesta.NOMBRE_EVENTO_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(evento.getNombre(), 3)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_EVENTO_TAMANHO_MINIMO.getCode(), CodigosRespuesta.NOMBRE_EVENTO_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(evento.getNombre(), 30)) {
            throw new AtributoException(CodigosRespuesta.NOMBRE_EVENTO_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.NOMBRE_EVENTO_TAMANHO_MAXIMO.getMsg());
        }else if(!esDescripcion(evento.getDescripcion())) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_EVENTO_ALFANUMERICO.getCode(), CodigosRespuesta.DESCRIPCION_EVENTO_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(evento.getDescripcion(), 3)) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_EVENTO_TAMANHO_MINIMO.getCode(), CodigosRespuesta.DESCRIPCION_EVENTO_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(evento.getDescripcion(), 255)) {
            throw new AtributoException(CodigosRespuesta.DESCRIPCION_EVENTO_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.DESCRIPCION_EVENTO_TAMANHO_MAXIMO.getMsg());
        }else if(!tipoAsistencia(evento.getTipoAsistencia().toString())) {
            throw new AtributoException(CodigosRespuesta.TIPO_ASISTENCIA_EVENTO_INVALIDO.getCode(), CodigosRespuesta.TIPO_ASISTENCIA_EVENTO_INVALIDO.getMsg());
        }else if(!esNumeroEntero(String.valueOf(evento.getNumAsistentes()))) {
            throw new AtributoException(CodigosRespuesta.NUM_ASISTENTES_EVENTO_NUMERICO.getCode(), CodigosRespuesta.NUM_ASISTENTES_EVENTO_NUMERICO.getMsg());
        }else if(!esFecha(evento.getFechaEvento())) {
            throw new AtributoException(CodigosRespuesta.FECHA_FORMATO.getCode(), CodigosRespuesta.FECHA_FORMATO.getMsg());
        }else if(!esDireccion(evento.getDireccion())) {
            throw new AtributoException(CodigosRespuesta.DIRECCION_EVENTO_ALFANUMERICO.getCode(), CodigosRespuesta.DIRECCION_EVENTO_ALFANUMERICO.getMsg());
        }else if(!tamanhoMinimo(evento.getDireccion(), 5)) {
            throw new AtributoException(CodigosRespuesta.DIRECCION_EVENTO_TAMANHO_MINIMO.getCode(), CodigosRespuesta.DIRECCION_EVENTO_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(evento.getDireccion(), 50)) {
            throw new AtributoException(CodigosRespuesta.DIRECCION_EVENTO_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.DIRECCION_EVENTO_TAMANHO_MAXIMO.getMsg());
        }else if(!esCorreoElectronicoValido(evento.getEmailContacto())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
        }else if(!tamanhoMinimo(evento.getEmailContacto(), 3)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MINIMO.getMsg());
        }else if(!tamanhoMaximo(evento.getEmailContacto(), 40)) {
            throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getMsg());
        }else if(!esNumeroEntero(evento.getTelefonoContacto())) {
            throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
        }else if(!tamanhoMinimo(evento.getTelefonoContacto(), 9)) {
            throw new AtributoException(CodigosRespuesta.TELEFONO_FORMATO.getCode(), CodigosRespuesta.TELEFONO_FORMATO.getMsg());
        }else if(!tamanhoMaximo(evento.getTelefonoContacto(), 9)) {
            throw new AtributoException(CodigosRespuesta.TELEFONO_FORMATO.getCode(), CodigosRespuesta.TELEFONO_FORMATO.getMsg());
        }else if(!formatoURL(evento.getUrl())) {
            throw new AtributoException(CodigosRespuesta.URL_FORMATO.getCode(), CodigosRespuesta.URL_FORMATO.getMsg());
        }
    }

    /*public void eventoBuscarTodos(String nombre, String descripcion, String tipoAsistencia, String numAsistentes, String fechaEvento, String direccion, String emailContacto, String telefonoContacto, String idCategoria) throws AtributoException {
        if (!isBlank(nombre)) {
            if(!esAlfanumericoEspacio(nombre)) {
                throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_ALFANUMERICO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_ALFANUMERICO.getMsg());
            }else if(!tamanhoMaximo(nombre, 15)) {
                throw new AtributoException(CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.NOMBRE_CATEGORIA_TAMANHO_MAXIMO.getMsg());
            }
        }else if(!isBlank(descripcion)) {
            if(!esAlfanumericoEspacio(descripcion)) {
                throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_ALFANUMERICO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_ALFANUMERICO.getMsg());
            }else if(!tamanhoMaximo(descripcion, 255)) {
                throw new AtributoException(CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO.getMsg());
            }
        }
        else if(!isBlank(tipoAsistencia)) {
            if(!tipoAsistencia(tipoAsistencia)) {
                throw new AtributoException(CodigosRespuesta.TIPO_ASISTENCIA_EVENTO_INVALIDO.getCode(), CodigosRespuesta.TIPO_ASISTENCIA_EVENTO_INVALIDO.getMsg());
            }
        }
        else if(!isBlank(numAsistentes)) {
            if(!esNumeroEntero(numAsistentes)) {
                throw new AtributoException(CodigosRespuesta.NUM_ASISTENTES_EVENTO_NUMERICO.getCode(), CodigosRespuesta.NUM_ASISTENTES_EVENTO_NUMERICO.getMsg());
            }
        }
        else if(!isBlank(fechaEvento)) {
            if(!esFecha(fechaEvento)) {
                throw new AtributoException(CodigosRespuesta.FECHA_FORMATO.getCode(), CodigosRespuesta.FECHA_FORMATO.getMsg());
            }
        }
        else if(!isBlank(direccion)) {
            if(!esDireccion(direccion)) {
                throw new AtributoException(CodigosRespuesta.DIRECCION_EVENTO_ALFANUMERICO.getCode(), CodigosRespuesta.DIRECCION_EVENTO_ALFANUMERICO.getMsg());
            }else if(!tamanhoMaximo(direccion, 50)) {
                throw new AtributoException(CodigosRespuesta.DIRECCION_EVENTO_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.DIRECCION_EVENTO_TAMANHO_MAXIMO.getMsg());
            }
        }
        else if(!isBlank(emailContacto)) {
            if(!esCorreoElectronicoValido(emailContacto)) {
                throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
            }else if(!tamanhoMaximo(emailContacto, 40)) {
                throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getMsg());
            }
        }
        else if(!isBlank(telefonoContacto)) {
            if(!esCorreoElectronicoValido(telefonoContacto)) {
                throw new AtributoException(CodigosRespuesta.TELEFONO_FORMATO.getCode(), CodigosRespuesta.TELEFONO_FORMATO.getMsg());
            }
        }
        else if(!isBlank(idCategoria)) {
            if(!esNumeroEntero(String.valueOf(idCategoria))) {
                throw new AtributoException(CodigosRespuesta.ID_FORMATO.getCode(), CodigosRespuesta.ID_FORMATO.getMsg());
            }
        }
    }*/


    //FUNCIONES VARIAS
    //Verifica si una cadena es nula o está compuesta solo de espacios en blanco.
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Verifica si el valor es un número entero.
    private boolean esNumeroEntero(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Verifica si el valor es un número entero.
    private boolean esNumeroFlotante(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Verifica si una cadena es alfanumérica, aceptando acentos y la letra "ñ".
    private boolean esAlfanumerico(String str) {
        return str != null && str.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ]+");
    }

    // Verifica si una cadena es alfabética, aceptando acentos y la letra "ñ".
    private boolean esAlfabetico(String str) {
        return str != null && str.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+");
    }

    // Verifica si una cadena es alfanumérica, aceptando acentos y la letra "ñ".
    private boolean esAlfanumericoEspacio(String str) {
        return str != null && str.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ ]+");
    }

    // Verifica si una cadena es alfanumérica, aceptando acentos y la letra "ñ".
    private boolean esAlfanumericoEspacioPunto(String str) {
        return str != null && str.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ. ]+");
    }

    // Verifica si una cadena es alfanumérica, aceptando acentos y la letra "ñ".,
    private boolean esDescripcion(String str) {
        return str != null && str.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ:., ]+");
    }

    // Verifica si una cadena es alfanumérica, aceptando acentos y la letra "ñ".
    private boolean esDireccion(String str) {
        return str != null && str.matches("[a-zA-Z0-9ñÑáéíóúÁÉÍÓÚüÜ/ºª,.;: ]+");
    }

    // Verifica si una cadena es alfabética, aceptando acentos y la letra "ñ".
    private boolean esAlfabeticoEspacio(String str) {
        return str != null && str.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ ]+");
    }

    // Verifica si el formato del rol es correcto (alfabético_alfabético), aceptando acentos y la letra "ñ".
    private boolean esRol(String str) {
        return str != null && str.matches("[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+_[a-zA-ZñÑáéíóúÁÉÍÓÚüÜ]+");
    }

    // Verifica si una cadena es un correo electrónico válido.
    private boolean esCorreoElectronicoValido(String str) {
        return str != null && str.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    //Verifica si está formada la cadena por números, letras, @ o .
    private boolean esCorreoElectronicoValidoBuscar(String str){
        return str != null && str.matches("[a-zA-Z0-9@.]+");
    }

    //Verifica si una cadena no es nula y su longitud es mayor o igual al tamaño mínimo especificado.
    private boolean tamanhoMinimo(String str, int minLength) {
        return str != null && str.length() >= minLength;
    }

    //Verifica si una cadena no es nula y su longitud es menor o igual al tamaño máximo especificado.
    private boolean tamanhoMaximo(String str, int maxLength) {
        return str != null && str.length() <= maxLength;
    }

    // Verificar si la contraseña cumple con los requisitos de formato
    //     ^: Indica el inicio de la cadena.
    // 	   [a-zA-Z0-9\\p{Punct}]: Define un conjunto de caracteres permitidos.
    //	   [a-zA-Z0-9]: Representa cualquier letra (mayúscula o minúscula) o dígito.
    //	   \\p{Punct}: Representa cualquier carácter especial de puntuación.
    //	   +: Indica que el conjunto de caracteres permitidos puede aparecer una o más veces.
    //	   $: Indica el final de la cadena.
    public boolean validarFormatoContrasena(String password) {
        return password.matches("^[a-zA-Z0-9\\\\p{Punct}]+$");
    }

    public static boolean esDni(String dni) {
        // El DNI debe tener una longitud de 9 caracteres (8 números + 1 letra)
        if (dni.length() != 9) {
            return false;
        }

        // Comprueba si los primeros 8 caracteres son números
        for (int i = 0; i < 8; i++) {
            if (!Character.isDigit(dni.charAt(i))) {
                return false;
            }
        }

        // Comprueba si el último carácter es una letra (A-Z)
        char letra = dni.charAt(8);
        if (!Character.isLetter(letra) || !Character.isUpperCase(letra)) {
            return false;
        }

        // Comprueba si la letra es la correcta según el algoritmo de cálculo
        int numeros = Integer.parseInt(dni.substring(0, 8));
        char letraCalculada = calcularLetraDni(numeros);
        return letra == letraCalculada;
    }

    public static char calcularLetraDni(int numeros) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int indice = numeros % 23;
        return letras.charAt(indice);
    }

    public static boolean esFecha(String fecha) {
        String regex = "^(?:19|20)\\d\\d-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fecha);

        return matcher.matches();
    }

    public static boolean tipoAsistencia(String tipoAsistencia) {
        if ("PUBLICO".equals(tipoAsistencia) || "PRIVADO".equals(tipoAsistencia)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean formatoURL(String url) {
        String regex = "^(https?|http)://[A-Za-z0-9.-]+(:[0-9]+)?(/[A-Za-z0-9-._~:/?#\\[\\]@!$&'()*+,;=]*)?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }
}
