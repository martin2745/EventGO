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
    USUARIO_BORRADO_LOGICO("USUARIO_BORRADO_LOGICO", "El usuario está eliminado en el sistema, por favor, si es un error póngase en contacto con el administrador en admin@admin.com"),
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
    NO_CREAR_ADMINISTRADOR("NO_CREAR_ADMINISTRADOR", "No se puede crear un administrador en el sistema"),
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
    NO_EDIRTAR_ROL("NO_EDIRTAR_ROL", "No se puede editar el rol"),
    NO_ELIMINAR_ADMINISTRADOR("NO_ELIMINAR_ADMINISTRADOR", "No puedes eliminar el administrador del sistema"),
    PERMISO_DENEGADO("PERMISO_DENEGADO", "Usted no puede realizar esta acción"),
    ARCHIVO_SUBIDO_OK("ARCHIVO_SUBIDO_OK", "Archivo subido al sistema"),
    ARCHIVO_VACIO("ARCHIVO_VACIO", "No se puede subir un archivo vacío"),

    //CategoriaController Atributos
    NOMBRE_CATEGORIA_VACIO("NOMBRE_CATEGORIA_VACIO", "El nombre de la categoría no puede ser vacío"),
    NOMBRE_CATEGORIA_ALFANUMERICO_PUNTO_COMA("NOMBRE_CATEGORIA_ALFANUMERICO_PUNTO_COMA", "El nombre de la categoría solo puede contener caracteres alfanuméricos.,"),
    NOMBRE_CATEGORIA_TAMANHO_MINIMO("NOMBRE_CATEGORIA_TAMANHO_MINIMO", "El  nombre de la categoría no puede tener menos de 3 caracteres"),
    NOMBRE_CATEGORIA_TAMANHO_MAXIMO("NOMBRE_CATEGORIA_TAMANHO_MAXIMO", "El  nombre de la categoría no puede superar los 15 caracteres"),
    DESCRIPCION_CATEGORIA_VACIO("DESCRIPCION_CATEGORIA_VACIO", "La descripción de la categoría no puede ser vacío"),
    DESCRIPCION_CATEGORIA_ALFANUMERICO_PUNTO_COMA("DESCRIPCION_CATEGORIA_ALFANUMERICO", "La descripción de la categoría solo puede contener caracteres alfanuméricos.,"),
    DESCRIPCION_CATEGORIA_TAMANHO_MINIMO("DESCRIPCION_CATEGORIA_TAMANHO_MINIMO", "La descripción de la categoría no puede tener menos de 3 caracteres"),
    DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO("DESCRIPCION_CATEGORIA_TAMANHO_MAXIMO", " La descripción no puede superar los 255 caracteres"),
    //CategoriaController Acciones
    NOMBRE_CATEGORIA_YA_EXISTE("NOMBRE_CATEGORIA_YA_EXISTE", " Ya existe una categoría con este nombre en el sistema"),
    CATEGORIA_NO_EXISTE("CATEGORIA_NO_EXISTE", "No existe la categoría en el sistema"),

    //EventoController Atributos
    NOMBRE_EVENTO_VACIO("NOMBRE_EVENTO_VACIO", "El nombre del evento no puede ser vacío"),
    NOMBRE_EVENTO_ALFANUMERICO_PUNTO_COMA("NOMBRE_EVENTO_ALFANUMERICO_PUNTO_COMA", "El nombre del evento solo puede contener caracteres alfanuméricos.,"),
    NOMBRE_EVENTO_TAMANHO_MINIMO("NOMBRE_EVENTO_TAMANHO_MINIMO", "El nombre del evento no puede tener menos de 3 caracteres"),
    NOMBRE_EVENTO_TAMANHO_MAXIMO("NOMBRE_EVENTO_TAMANHO_MAXIMO", "El nombre del evento no puede superar los 30 caracteres"),
    DESCRIPCION_EVENTO_VACIO("DESCRIPCION_EVENTO_VACIO", "La descripción del evento no puede ser vacía"),
    DESCRIPCION_EVENTO_ALFANUMERICO_PUNTO_COMA("DESCRIPCION_EVENTO_ALFANUMERICO", "La descripción del evento solo puede contener caracteres alfanuméricos.,"),
    DESCRIPCION_EVENTO_TAMANHO_MINIMO("DESCRIPCION_EVENTO_TAMANHO_MINIMO", "La descripción del evento no puede tener menos de 3 caracteres"),
    DESCRIPCION_EVENTO_TAMANHO_MAXIMO("DESCRIPCION_EVENTO_TAMANHO_MAXIMO", "La descripción no puede superar los 255 caracteres"),
    TIPO_ASISTENCIA_EVENTO_VACIO("TIPO_ASISTENCIA_EVENTO_VACIO", "El tipo de asistencia del evento no puede ser vacío"),
    TIPO_ASISTENCIA_EVENTO_INVALIDO("TIPO_ASISTENCIA_EVENTO_INVALIDO", "El tipo de asistencia de un evento ha de ser PUBLICO o PRIVADO"),
    NUM_ASISTENTES_EVENTO_VACIO("NUM_ASISTENTES_EVENTO_VACIO", "El número de asistentes del evento no puede ser vacío"),
    NUM_ASISTENTES_EVENTO_NUMERICO("NUM_ASISTENTES_EVENTO_NUMERICO", "El número de asistentes ha de ser numérico"),
    TELEFONO_VACIO("TELEFONO_VACIO", "El teléfono no puede ser vacío"),
    TELEFONO_FORMATO("TELEFONO_FORMATO", "El teléfono tiene que estar formado por 9 números"),
    CATEGORIA_EVENTO_VACIO("CATEGORIA_EVENTO_VACIO", "La categoría del evento no puede ser vacía"),
    DIRECCION_VACIO("DIRECCION_VACIO", "La dirección del evento no puede ser vacío"),
    DIRECCION_EVENTO_ALFANUMERICO("DIRECCION_EVENTO_ALFANUMERICO", "La dirección del evento solo puede contener caracteres alfanuméricos"),
    DIRECCION_EVENTO_TAMANHO_MINIMO("DIRECCION_EVENTO_TAMANHO_MINIMO", "La dirección del evento no puede tener menos de 5 caracteres"),
    DIRECCION_EVENTO_TAMANHO_MAXIMO("DIRECCION_EVENTO_TAMANHO_MAXIMO", "La dirección del evento no puede tener más de 50 caracteres"),
    URL_FORMATO("URL_FORMATO", "La URL tiene un formato incorrecto."),
    //EventoController Acciones
    NOMBRE_EVENTO_YA_EXISTE("NOMBRE_EVENTO_YA_EXISTE", " Ya existe un evento con este nombre en el sistema"),
    EVENTO_NO_EXISTE("EVENTO_NO_EXISTE", "No existe este evento en el sistema"),
    //SuscripcionController y SolicitudController Atributos

    //SuscripcionController y SolicitudController Acciones
    SOLICITUD_ACEPTADA("SOLICITUD_ACEPTADA", "Se ha aceptado la solicitud y se ha creado una suscripción al evento"),
    EXISTE_SUSCRIPCION("EXISTE_SUSCRIPCION", "Ya existe una suscripción para este evento"),
    EVENTO_CERRADO("EVENTO_CERRADO", "No te puedes inscribir en un evento cerrado por el responsable del evento"),
    PLAZAS_CUBIERTAS("PLAZAS_CUBIERTAS", "No te puedes inscribir a un evento con todas las plazas ya cubiertas"),
    SUSCRIPCION_NO_EXISTE("SUSCRIPCION_NO_EXISTE","No existe la suscripción en el sistema"),
    //ComentarioController Atributos
    COMENTARIO_VACIO("COMENTARIO_VACIO", "El comentario del evento no puede ser vacío"),
    COMENTARIO_ALFANUMERICO("COMENTARIO_ALFANUMERICO", "El comentario del evento solo puede contener caracteres alfanuméricos"),
    COMENTARIO_TAMANHO_MINIMO("COMENTARIO_TAMANHO_MINIMO", "El comentario del evento no puede tener menos de 3 caracteres"),
    COMENTARIO_TAMANHO_MAXIMO("COMENTARIO_TAMANHO_MAXIMO", "El comentario del evento no puede superar los 255 caracteres"),

    //ComentarioController Acciones
    COMENTARIO_NO_EXISTE("COMENTARIO_NO_EXISTE","No existe un comentario del usuario para este evento"),
    COMENTARIO_YA_EXISTE("COMENTARIO_YA_EXISTE","Ya existe un comentario del usuario para este evento"),
    //AmistadController Atributos

    //AmistadController Acciones
    GERENTE_NO_EXISTE("GERENTE_NO_EXISTE","No existe el gerente de eventos"),
    SEGUIDOR_NO_EXISTE("SEGUIDOR_NO_EXISTE","No existe el seguidor en el sistema"),
    AMISTAD_YA_EXISTE("AMISTAD_YA_EXISTE","Ya existe la amistad entre el gerente de eventos y el seguidor en el sistema"),
    AMISTAD_NO_EXISTE("AMISTAD_NO_EXISTE","No existe la amistad entre el gerente de eventos y el seguidor en el sistema"),
    //NoticiaController Atributos
    TITULO_NOTICIA_VACIO("TITULO_NOTICIA_VACIO", "El título de la noticia no puede ser vacío"),
    TITULO_NOTICIA_ALFANUMERICO("TITULO_NOTICIA_ALFANUMERICO", "El título de la noticia solo puede contener caracteres alfanuméricos"),
    TITULO_NOTICIA_TAMANHO_MINIMO("TITULO_NOTICIA_TAMANHO_MINIMO", "El título de la noticia no puede tener menos de 3 caracteres"),
    TITULO_NOTICIA_TAMANHO_MAXIMO("TITULO_NOTICIA_TAMANHO_MAXIMO", "El título de la noticia no puede superar los 30 caracteres"),
    DESCRIPCION_NOTICIA_VACIO("DESCRIPCION_NOTICIA_VACIO", "La descripción de la noticia no puede ser vacía"),
    DESCRIPCION_TITULO_ALFANUMERICO("DESCRIPCION_EVENTO_ALFANUMERICO", "La descripción de la noticia solo puede contener caracteres alfanuméricos"),
    DESCRIPCION_TITULO_TAMANHO_MINIMO("DESCRIPCION_EVENTO_TAMANHO_MINIMO", "La descripción de la noticia no puede tener menos de 3 caracteres"),
    DESCRIPCION_TITULO_TAMANHO_MAXIMO("DESCRIPCION_EVENTO_TAMANHO_MAXIMO", "La descripción de la noticia no puede superar los 255 caracteres"),
    //NoticiaController Acciones
    NOTICIA_NO_EXISTE("NOTICIA_NO_EXISTE","No existe la noticia en el sistema"),

    ID_FORMATO("ID_FORMATO", "El id no es numérico");


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
