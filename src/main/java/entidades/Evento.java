package entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "EVENTO")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private String tipoAsistencia;
    private int numAsistentes;
    private int numInscritos;
    private String estado;
    private String fechaEvento;
    private String direccion;
    private String emailContacto;
    private String telefonoContacto;
    private String url;

    @ManyToOne
    @JoinColumn(name="id_categoria")
    private Categoria categoria;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    private String imagenEvento;
    public Evento() {

    }

    public Evento(String nombre, String descripcion, String tipoAsistencia, int numAsistentes, int numInscritos, String estado, String fechaEvento, String direccion, String emailContacto, String telefonoContacto, Categoria categoria, Usuario usuario, String imagenEvento, String url) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoAsistencia = tipoAsistencia;
        this.numAsistentes = numAsistentes;
        this.numInscritos = numInscritos;
        this.estado = estado;
        this.fechaEvento = fechaEvento;
        this.direccion = direccion;
        this.emailContacto = emailContacto;
        this.telefonoContacto = telefonoContacto;
        this.categoria = categoria;
        this.usuario = usuario;
        this.imagenEvento = imagenEvento;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoAsistencia() {
        return tipoAsistencia;
    }

    public int getNumAsistentes() {
        return numAsistentes;
    }

    public int getNumInscritos() {
        return numInscritos;
    }

    public String getEstado() {
        return estado;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getImagenEvento() {
        return imagenEvento;
    }
    public String getUrl() {
        return url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTipoAsistencia(String tipoAsistencia) {
        this.tipoAsistencia = tipoAsistencia;
    }

    public void setNumAsistentes(int numAsistentes) {
        this.numAsistentes = numAsistentes;
    }

    public void setNumInscritos(int numInscritos) {
        this.numInscritos = numInscritos;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setFechaEvento(String fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setImagenEvento(String imagenEvento) {
        this.imagenEvento = imagenEvento;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipoAsistencia='" + tipoAsistencia + '\'' +
                ", numAsistentes=" + numAsistentes +
                ", numInscritos=" + numInscritos +
                ", estado='" + estado + '\'' +
                ", fechaEvento='" + fechaEvento + '\'' +
                ", direccion='" + direccion + '\'' +
                ", emailContacto='" + emailContacto + '\'' +
                ", telefonoContacto='" + telefonoContacto + '\'' +
                ", url='" + url + '\'' +
                ", categoria=" + categoria +
                ", usuario=" + usuario +
                ", imagenEvento='" + imagenEvento + '\'' +
                '}';
    }
}