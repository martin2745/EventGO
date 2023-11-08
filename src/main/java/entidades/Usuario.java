package entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "USUARIO")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    private String nombre;

    private String email;

    private String rol;

    private String dni;

    private String fechaNacimiento;

    private String pais;

    private String imagenUsuario;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT '0'")
    private String borradoLogico;

    public Usuario() {

    }

    public Usuario(String login, String password, String nombre, String email, String rol, String dni, String fechaNacimiento, String pais, String imagenUsuario, String borradoLogico) {
        super();
        this.login = login;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
        this.rol=rol;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.pais=pais;
        this.imagenUsuario=imagenUsuario;
        this.borradoLogico=borradoLogico;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(String imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    public String getBorradoLogico() {
        return borradoLogico;
    }

    public void setBorradoLogico(String borradoLogico) {
        this.borradoLogico = borradoLogico;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", dni='" + dni + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", pais='" + pais + '\'' +
                ", imagenUsuario='" + imagenUsuario + '\'' +
                ", borradoLogico='" + borradoLogico + '\'' +
                '}';
    }
}
