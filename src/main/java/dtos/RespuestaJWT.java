package dtos;

import java.util.List;

public class RespuestaJWT {
    private String token;
    private Long id;
    private String login;
    private String rol;

    public RespuestaJWT() {
        super();
    }

    public RespuestaJWT(String token, Long id, String login, String rol) {
        super();
        this.token = token;
        this.id = id;
        this.login = login;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
