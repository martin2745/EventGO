package dtos;
public class DatosLogin {
    private String login;
    private String password;

    public DatosLogin() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DatosLogin [login=" + login + ", password=" + password + "]";
    }

}
