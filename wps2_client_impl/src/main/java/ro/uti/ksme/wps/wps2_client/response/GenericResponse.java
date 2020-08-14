package ro.uti.ksme.wps.wps2_client.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;
import ro.uti.ksme.wps.wps2_client.connection_util.LogContentFunctional;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 12:10 PM
 */
public abstract class GenericResponse implements HttpClientResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericResponse.class);
    protected HttpClientResponse httpClientResponse;
    protected ExceptionReport exceptionReport;
    protected static final LogContentFunctional LOG_DATA = data -> {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("INFO_WPS2_CLIENT >>>> The following data was received from the Wps2 Server implementation \n" + new String(data) + "\n");
        }
    };

    public GenericResponse(HttpClientResponse response) throws IOException {
        if (response.getResponseInputStream() == null) {
            throw new NullPointerException("An inputStream is required for " + getClass().getName());
        }
        if (response.getContentType() == null) {
            response.getResponseInputStream().close();
            response.dismiss();
        }
        this.httpClientResponse = response;
    }

    @Override
    public void dismiss() {
        httpClientResponse.dismiss();
    }

    @Override
    public String getContentType() {
        return httpClientResponse.getContentType();
    }

    @Override
    public String getResponseHeader(String headerName) {
        return httpClientResponse.getResponseHeader(headerName);
    }

    @Override
    public InputStream getResponseInputStream() {
        return httpClientResponse.getResponseInputStream();
    }

    @Override
    public long getRequestContentLength() {
        return httpClientResponse.getRequestContentLength();
    }

    @Override
    public int getHttpResponseCode() {
        return httpClientResponse.getHttpResponseCode();
    }

    public ExceptionReport getExceptionReport() {
        return exceptionReport;
    }
}
