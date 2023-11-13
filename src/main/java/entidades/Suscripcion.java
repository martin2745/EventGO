package entidades;

import javax.persistence.*;

@Entity
@Table(name = "SUSCRIPCION")
public class Suscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_evento", referencedColumnName = "id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    private String fechaSuscripcion;
    public Suscripcion() {

    }

    public Suscripcion(Evento evento, Usuario usuario, String fechaSuscripcion){
        this.evento = evento;
        this.usuario = usuario;
        this.fechaSuscripcion = fechaSuscripcion;
    }

    public Long getId() {
        return id;
    }

    public Evento getEvento() {
        return evento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getFechaSuscripcion() {
        return fechaSuscripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setFechaSuscripcion(String fechaSuscripcion) {
        this.fechaSuscripcion = fechaSuscripcion;
    }

    @Override
    public String toString() {
        return "Suscripcion{" +
                "id=" + id +
                ", evento=" + evento +
                ", usuario=" + usuario +
                ", fechaSuscripcion='" + fechaSuscripcion + '\'' +
                '}';
    }
}
