package dtos;

public class RecuperarPassword {
    private String login;
    private String email;
    private String idioma;

    public RecuperarPassword() {

    }

    public RecuperarPassword(String login, String email, String idioma) {
        super();
        this.login = login;
        this.email = email;
        this.idioma = idioma;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(final String idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return "RecuperarPass [login=" + login + ", email=" + email + ", idioma=" + idioma + "]";
    }

}
