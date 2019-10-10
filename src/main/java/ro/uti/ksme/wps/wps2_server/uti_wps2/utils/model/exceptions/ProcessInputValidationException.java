package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions;

/**
 * @author Bogdan-Adrian Sincu created on 10/9/2019
 */
public class ProcessInputValidationException extends RuntimeException {
    public ProcessInputValidationException() {
        super();
    }

    public ProcessInputValidationException(String message) {
        super(message);
    }

    public ProcessInputValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
