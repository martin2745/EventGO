package validaciones;


public enum CodigosRespuesta {
    ERROR_INESPERADO("ERROR_INESPERADO", "Error inesperado"),

    //AutenticacionController Login Atributos
    LOGIN_VACIO("LOGIN_VACIO", "El login de usuario no puede ser vacío"),
    PASSWORD_VACIO("PASSWORD_VACIO", "La password de usuario no puede ser vacía"),
    LOGIN_ALFANUMERICO("LOGIN_ALFANUMERICO", "El login solo puede contener caracteres alfanuméricos"),
    LOGIN_TAMANHO_MINIMO("LOGIN_TAMANHO_MAXIMO", "El login no puede tener menos de 3 caracteres"),
    LOGIN_TAMANHO_MAXIMO("LOGIN_TAMANHO_MAXIMO", "El login no puede tener más de 15 caracteres"),
    PASSWORD_FORMATO("PASSWORD_ALFANUMERICO", "La contraseña solo puede contener caracteres alfanuméricos y especiales"),
    PASSWORD_TAMANHO_MINIMO("PASSWORD_TAMANHO_MAXIMO", "La contraseña no puede tener menos de 3 caracteres"),
    PASSWORD_TAMANHO_MAXIMO("PASSWORD_TAMANHO_MAXIMO", "La contraseña no puede tener más de 16 caracteres"),
    //AutenticacionController Login Acciones
    LOGIN_NO_EXISTE("LOGIN_NO_EXISTE", "No existe un usuario con ese login en el sistema"),
    LOGIN_PASSWORD_NO_COINCIDEN("LOGIN_PASSWORD_NO_COINCIDEN", "El login y la password no coinciden"),
    //AutenticacionController Login Registro
    NOMBRE_VACIO("NOMBRE_VACIO", "El nombre de usuario no puede ser vacío"),
    EMAIL_VACIO("EMAIL_VACIO", "El email de usuario no puede ser vacío"),
    ROL_VACIO("ROL_VACIO", "El rol de usuario no puede ser vacío"),
    DNI_VACIO("DNI_VACIO", "El dni de usuario no puede ser vacío"),
    FECHA_VACIO("FECHA_VACIO", "La fecha no puede ser vacía"),
    PAIS_VACIO("PAIS_VACIO", "El pais no puede ser vacío"),
    NOMBRE_ALFABETICO("NOMBRE_ALFABETICO", "El nombre solo puede contener caracteres alfabéticos"),
    NOMBRE_TAMANHO_MINIMO("NOMBRE_TAMANHO_MINIMO", "El nombre no puede tener menos de 3 caracteres"),
    NOMBRE_TAMANHO_MAXIMO("NOMBRE_TAMANHO_MAXIMO", "El nombre no puede tener más de 40 caracteres"),
    EMAIL_FORMATO("EMAIL_FORMATO", "El correo electrónico válido debe tener un nombre de usuario con letras, dígitos y ciertos caracteres especiales, seguido de un símbolo \"@\" y un dominio que puede contener letras, dígitos y caracteres especiales, con un punto (.) antes de la extensión que debe tener al menos dos letras"),
    EMAIL_TAMANHO_MINIMO("EMAIL_TAMANHO_MINIMO", "El email no puede tener menos de 3 caracteres"),
    EMAIL_TAMANHO_MAXIMO("EMAIL_TAMANHO_MAXIMO", "El email no puede tener más de 40 caracteres"),
    ROL_ALFABETICO("ROL_ALFABETICO", "El rol solo puede contener caracteres alfabéticos"),
    ROL_TAMANHO_MINIMO("ROL_TAMANHO_MINIMO", "El rol no puede tener menos de 3 caracteres"),
    ROL_TAMANHO_MAXIMO("ROL_TAMANHO_MAXIMO", "El rol no puede tener más de 20 caracteres"),
    DNI_FORMATO("DNI_FORMATO", "El formato del DNI no es correcto"),
    DNI_ALFANUMERICO("DNI_ALFANUMERICO", "El DNI solo contiene letras y números"),
    FECHA_FORMATO("FECHA_FORMATO", "La fecha tiene un formato incorrecto (yyyy-MM-dd)"),
    PAIS_ALFABETICO("PAIS_ALFABETICO", "El país solo puede contener caracteres alfabéticos"),
    PAIS_TAMANHO_MINIMO("PAIS_TAMANHO_MINIMO", "El país no puede tener menos de 3 caracteres"),
    PAIS_TAMANHO_MAXIMO("PAIS_TAMANHO_MAXIMO", "El país no puede tener más de 15 caracteres"),
    //AutenticacionController Registro Acciones
    LOGIN_YA_EXISTE("LOGIN_YA_EXISTE", "Ya existe un usuario con ese login en el sistema"),
    EMAIL_YA_EXISTE("EMAIL_YA_EXISTE", "Ya existe un usuario con ese email en el sistema"),
    DNI_YA_EXISTE("DNI_YA_EXISTE", "Ya existe un usuario con ese DNI en el sistema"),
    USUARIO_REGISTRO_OK("USUARIO_REGISTRO_OK", "Usuario registrado con éxito"),
    RECUPERAR_PASS_OK("RECUPERAR_PASS_OK", "Se ha enviado una nueva contraseña a su email"),
    //AuthenticacionController RecuperarPassword Acciones
    USUARIO_NO_ENCONTRADO("USUARIO_NO_ENCONTRADO", "No se ha encontrado el usuario en el sistema"),
    EMAIL_NO_ENCONTRADO("EMAIL_NO_ENCONTRADO", "No se ha encontrado el email en el sistema"),
    MAIL_NO_ENVIADO("MAIL_NO_ENVIADO", "Se ha producido un error al enviar el email"),
    ENVIO_EMAIL_EXCEPTION("ENVIO_EMAIL_EXCEPTION", "Ha ocurrido un errror al enviar el email"),

    //UsuarioController Acciones
    USUARIO_NO_EXISTE("USUARIO_NO_EXISTE", "El usuario no existe en el sistema"),
    NO_EDIRTAR_ROL_ADMINISTRADOR("NO_EDIRTAR_ROL_ADMINISTRADOR", "No puedes editar el rol a administrador"),
    NO_ELIMINAR_ADMINISTRADOR("NO_ELIMINAR_ADMINISTRADOR", "No puedes eliminar el administrador del sistema"),
    PERMISO_DENEGADO("PERMISO_DENEGADO", "Usted no puede realizar esta acción"),
    ARCHIVO_SUBIDO_OK("ARCHIVO_SUBIDO_OK", "Archivo subido al sistema"),
    ARCHIVO_VACIO("ARCHIVO_VACIO", "No se puede subir un archivo vacío"),

    //CategoriaController Atributos
    NOMBRE_CATEGORIA_VACIO("NOMBRE_CATEGORIA_VACIO", "El nombre de la categoría no puede ser vacío"),
    NOMBRE_CATEGORIA_ALFANUMERICO("NOMBRE_CATEGORIA_ALFANUMERICO", "El nombre de la categoría solo puede contener caracteres alfanuméricos"),
    NOMBRE_CATEGORIA_TAMANHO_MINIMO("NOMBRE_CATEGORIA_TAMANHO_MINIMO", "El  nombre de la categoría no puede tener menos de 3 caracteres"),
    NOMBRE_CATEGORIA_TAMANHO_MAXIMO("NOMBRE_CATEGORIA_TAMANHO_MAXIMO", "El  nombre de la categoría no puede superar los 15 caracteres"),
    DESCRIPCION_CATEGORIA_VACIO("DESCRIPCION_CATEGORIA_VACIO", "La descripción de la categoría no puede ser vacío"),
    DESCRIPCION_CATEGORIA_ALFANUMERICO("DESCRIPCION_CATEGORIA_ALFANUMERICO", "La descripción de la categoría solo puede contener caracteres alfanuméricos"),
    DESCRIPCION_CATEGORIA_TAMANHO_MINIMO("DESCRIPCION_CATEGORIA_TAMANHO_MINIMO", "La descripción de la categoría no puede tener menos de 3 caracteres"),
    DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO("DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO", " La descripción no puede superar los 255 caracteres"),
    //CategoriaController Acciones
    NOMBRE_CATEGORIA_YA_EXISTE("NOMBRE_CATEGORIA_YA_EXISTE", " Ya existe una categoría con este nombre en el sistema"),
    CATEGORIA_NO_EXISTE("CATEGORIA_NO_EXISTE", "No existe la categoría en el sistema");

    private String code;
    private String msg;

    CodigosRespuesta(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
