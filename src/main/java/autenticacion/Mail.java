package autenticacion;

import java.util.List;

public class Mail {

    private String emisorEmail;
    private String receptorEmail;
    private String asuntoEmail;
    private String contenidoEmail;
    private String tipoContenido;
    private List<Object> adjuntos;

    public Mail() {
        this.tipoContenido = "text/plain";
    }

    public Mail(final String emisorEmail, final String receptorEmail, final String asuntoEmail,
                final String contenidoEmail, final String tipoContenido, final List<Object> adjuntos) {
        super();
        this.emisorEmail = emisorEmail;
        this.receptorEmail = receptorEmail;
        this.asuntoEmail = asuntoEmail;
        this.contenidoEmail = contenidoEmail;
        this.tipoContenido = tipoContenido;
        this.adjuntos = adjuntos;
    }

    public String getEmisorEmail() {
        return emisorEmail;
    }

    public void setEmisorEmail(final String emisorEmail) {
        this.emisorEmail = emisorEmail;
    }

    public String getReceptorEmail() {
        return receptorEmail;
    }

    public void setReceptorEmail(final String receptorEmail) {
        this.receptorEmail = receptorEmail;
    }

    public String getAsuntoEmail() {
        return asuntoEmail;
    }

    public void setAsuntoEmail(final String asuntoEmail) {
        this.asuntoEmail = asuntoEmail;
    }

    public String getContenidoEmail() {
        return contenidoEmail;
    }

    public void setContenidoEmail(final String contenidoEmail) {
        this.contenidoEmail = contenidoEmail;
    }

    public String getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(final String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public List<Object> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(final List<Object> adjuntos) {
        this.adjuntos = adjuntos;
    }

    @Override
    public String toString() {
        return "Mail [emisorEmail=" + emisorEmail + ", receptorEmail=" + receptorEmail + ", asuntoEmail=" + asuntoEmail
                + ", contenidoEmail=" + contenidoEmail + ", tipoContenido=" + tipoContenido + ", adjuntos=" + adjuntos
                + "]";
    }

}