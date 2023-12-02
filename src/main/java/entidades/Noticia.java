package entidades;

import javax.persistence.*;

@Entity
@Table(name = "NOTICIA")
public class Noticia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private String fechaNoticia;
    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    public Noticia(){

    }

    public Noticia(String titulo, String descripcion, String fechaNoticia, Usuario usuario) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaNoticia = fechaNoticia;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFechaNoticia() {
        return fechaNoticia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaNoticia(String fechaNoticia) {
        this.fechaNoticia = fechaNoticia;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
