package ECIMS.exception;

/** Thrown when a customer lookup yields no result. */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) { super(message); }
}