package app.exeptions;

public class DomainException extends  RuntimeException{

    public DomainException(String message) {
        super(message);
    }
}
