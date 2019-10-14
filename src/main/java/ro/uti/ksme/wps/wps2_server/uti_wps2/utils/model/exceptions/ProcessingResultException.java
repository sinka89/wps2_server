package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/11/2019
 * Time: 10:37 AM
 */
public class ProcessingResultException extends RuntimeException {
    public ProcessingResultException() {
        super();
    }

    public ProcessingResultException(String message) {
        super(message);
    }

    public ProcessingResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
