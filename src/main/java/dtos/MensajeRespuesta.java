package dtos;

public class MensajeRespuesta {
    private String codigo;
    private String texto;

    public MensajeRespuesta() {
    }

    public MensajeRespuesta(String codigo, String texto) {
        super();
        this.codigo = codigo;
        this.texto = texto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}
