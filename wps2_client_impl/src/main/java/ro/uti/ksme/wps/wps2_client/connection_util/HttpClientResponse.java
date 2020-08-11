package ro.uti.ksme.wps.wps2_client.connection_util;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/25/2019
 * Time: 5:24 PM
 */
public interface HttpClientResponse {

    void dismiss();

    String getContentType();

    String getResponseHeader(String headerName);

    long getRequestContentLength();

    InputStream getResponseInputStream();

    int getHttpResponseCode();
}
