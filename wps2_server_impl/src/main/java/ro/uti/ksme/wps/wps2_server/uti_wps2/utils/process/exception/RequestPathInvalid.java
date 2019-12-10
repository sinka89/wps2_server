package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/10/2019
 * Time: 1:32 PM
 */
public class RequestPathInvalid extends RuntimeException {
    public RequestPathInvalid() {
        super();
    }

    public RequestPathInvalid(String message) {
        super(message);
    }

    public RequestPathInvalid(String message, Throwable cause) {
        super(message, cause);
    }
}
