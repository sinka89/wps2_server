package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/10/2019
 * Time: 2:01 PM
 */
public class RequestMethodInvalid extends RuntimeException {
    public RequestMethodInvalid() {
        super();
    }

    public RequestMethodInvalid(String message) {
        super(message);
    }

    public RequestMethodInvalid(String message, Throwable cause) {
        super(message, cause);
    }
}
