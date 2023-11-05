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

    @JsonIgnore
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Evento> eventosCategoria;

    public Categoria() {

    }

    public Categoria(String nombre, String descripcion, String imagenCategoria) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenCategoria = imagenCategoria;
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

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagenCategoria='" + imagenCategoria + '\'' +
                ", eventosCategoria=" + eventosCategoria +
                '}';
    }
}
