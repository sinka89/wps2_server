package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/4/2019
 * Time: 12:00 PM
 */
public class ProcessOutputValidationException extends RuntimeException {
    public ProcessOutputValidationException() {
        super();
    }

    public ProcessOutputValidationException(String message) {
        super(message);
    }

    public ProcessOutputValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
