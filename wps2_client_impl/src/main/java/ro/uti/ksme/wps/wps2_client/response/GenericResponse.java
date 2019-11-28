package ro.uti.ksme.wps.wps2_client.response;

import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;

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
    protected HttpClientResponse httpClientResponse;

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
}
