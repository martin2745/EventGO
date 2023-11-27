package entidades;

import javax.persistence.*;

@Entity
@Table(name = "COMENTARIO")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int puntuacion;
    private String comentario;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="id_evento")
    private Evento evento;

    public Comentario(){

    }

    public Comentario(int puntuacion, String comentario, Usuario usuario, Evento evento){
        this.puntuacion =  puntuacion;
        this.comentario = comentario;
        this.usuario = usuario;
        this.evento = evento;
    }

    public Long getId() {
        return id;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
