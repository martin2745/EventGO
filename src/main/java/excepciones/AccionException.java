package excepciones;

public class AccionException extends Exception{

    private final String code;

    public AccionException(final String code, final String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
