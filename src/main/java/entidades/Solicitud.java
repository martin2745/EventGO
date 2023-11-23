package entidades;
import javax.persistence.*;

@Entity
@Table(name = "SOLICITUD")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_evento", referencedColumnName = "id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    private String fechaSolicitud;
    public Solicitud() {

    }

    public Solicitud(Evento evento, Usuario usuario, String fechaSolicitud){
        this.evento = evento;
        this.usuario = usuario;
        this.fechaSolicitud = fechaSolicitud;
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

    public String getFechaSolicitud() {
        return fechaSolicitud;
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

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}