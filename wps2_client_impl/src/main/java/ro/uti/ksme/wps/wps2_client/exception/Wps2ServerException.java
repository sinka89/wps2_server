package ro.uti.ksme.wps.wps2_client.exception;

import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/26/2019
 * Time: 1:52 PM
 */
public class Wps2ServerException extends RuntimeException {
    private ExceptionReport exceptionReport;

    public Wps2ServerException(String message, ExceptionReport exceptionReport) {
        super(message);
        this.exceptionReport = exceptionReport;
    }

    public ExceptionReport getExceptionReport() {
        return this.exceptionReport;
    }
}
