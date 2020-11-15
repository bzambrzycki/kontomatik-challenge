package pl.zambrzyckib.integration.exception;
public class WrongCredentialsFileException extends RuntimeException {
    public WrongCredentialsFileException(String message) {
        super(message);
    }
}
