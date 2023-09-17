package validaciones;

import dtos.DatosLogin;
import dtos.RecuperarPassword;
import entidades.Usuario;
import excepciones.AtributoException;

import java.util.Date;

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
    public void usuarioBuscarTodos(String login, String nombre, String email) throws AtributoException{
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
            if(!esCorreoElectronicoValido(email)) {
                throw new AtributoException(CodigosRespuesta.EMAIL_FORMATO.getCode(), CodigosRespuesta.EMAIL_FORMATO.getMsg());
            }else if(!tamanhoMaximo(email, 40)) {
                throw new AtributoException(CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getCode(), CodigosRespuesta.EMAIL_TAMANHO_MAXIMO.getMsg());
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

    //FUNCIONES VARIAS
    //Verifica si una cadena es nula o está compuesta solo de espacios en blanco.
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
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

    public static boolean esFecha(Object valor) {
        if (valor instanceof Date) {
            return true;
        } else {
            return false;
        }
    }
}
