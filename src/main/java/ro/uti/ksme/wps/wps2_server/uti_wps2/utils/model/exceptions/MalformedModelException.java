package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions;

public class MalformedModelException extends RuntimeException {
    public MalformedModelException(Class wpsModelClass, String propArg, String reason) {
        super("Error implementing " + wpsModelClass.getSimpleName() + ", the argument " + propArg + " " + reason + ".");
    }
}
