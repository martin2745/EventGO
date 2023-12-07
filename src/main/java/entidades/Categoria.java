package entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.awt.*;
import java.util.List;

@Entity
@Table(name = "CATEGORIA")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private String imagenCategoria;

    @Column(columnDefinition = "VARCHAR(1) DEFAULT '0'")
    private String borradoLogico;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Evento> eventosCategoria;

    public Categoria() {

    }

    public Categoria(String nombre, String descripcion, String imagenCategoria, String borradoLogico) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenCategoria = imagenCategoria;
        this.borradoLogico = borradoLogico;
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

    public String getImagenCategoria() {
        return imagenCategoria;
    }

    public List<Evento> getEventosCategoria() {
        return eventosCategoria;
    }

    public String getBorradoLogico() {
        return borradoLogico;
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

    public void setImagenCategoria(String imagenCategoria) {
        this.imagenCategoria = imagenCategoria;
    }

    public void setEventosCategoria(List<Evento> eventosCategoria) {
        this.eventosCategoria = eventosCategoria;
    }

    public void setBorradoLogico(String borradoLogico) {
        this.borradoLogico = borradoLogico;
    }
}
