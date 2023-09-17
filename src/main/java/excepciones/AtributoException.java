package excepciones;

public class AtributoException extends Exception{

    private final String code;

    public AtributoException(final String code, final String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
