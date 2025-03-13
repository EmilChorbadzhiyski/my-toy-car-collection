package app.exeptions;

public class UsernameAlreadyExistException extends RuntimeException{

    public UsernameAlreadyExistException(String message) {
        super(message);
    }

    public UsernameAlreadyExistException() {
    }
}
