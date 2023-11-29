package entidades;

import javax.persistence.*;

@Entity
@Table(name = "AMISTAD")
public class Amistad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id_gerente")
    private Usuario gerente;

    @ManyToOne
    @JoinColumn(name="id_seguidor")
    private Usuario seguidor;

    public Amistad(){

    }

    public Amistad(Usuario gerente, Usuario seguidor){
        this.gerente =  gerente;
        this.seguidor = seguidor;
    }

    public Long getId() {
        return id;
    }

    public Usuario getGerente() {
        return gerente;
    }

    public Usuario getSeguidor() {
        return seguidor;
    }

    public void setGerente(Usuario gerente) {
        this.gerente = gerente;
    }

    public void setSeguidor(Usuario seguidor) {
        this.seguidor = seguidor;
    }
}
