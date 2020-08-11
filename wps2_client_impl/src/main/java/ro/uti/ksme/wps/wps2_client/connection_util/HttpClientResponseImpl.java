package ro.uti.ksme.wps.wps2_client.connection_util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/25/2019
 * Time: 5:24 PM
 */
public class HttpClientResponseImpl implements HttpClientResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientResponseImpl.class);

    private URLConnection urlConnection;

    private InputStream responseStream;

    private int httpResponseCode;

    private long contentLength = -1;

    public HttpClientResponseImpl(final URLConnection conn) throws IOException {
        this.urlConnection = conn;
        this.httpResponseCode = ((HttpURLConnection) conn).getResponseCode();
        if (((HttpURLConnection) conn).getResponseCode() == HttpURLConnection.HTTP_OK) {
            contentLength = conn.getContentLengthLong();
            responseStream = conn.getInputStream();
        } else {
            responseStream = ((HttpURLConnection) conn).getErrorStream();
        }
    }

    @Override
    public void dismiss() {
        if (responseStream != null) {
            try {
                responseStream.close();
            } catch (IOException e) {
                LOGGER.error(">>>> ERROR closing response stream... Cause:\n" + e.getMessage(), e);
            }
            responseStream = null;
        }
        if (urlConnection != null) {
            if (urlConnection instanceof HttpURLConnection) {
                ((HttpURLConnection) urlConnection).disconnect();
            }
            urlConnection = null;
        }
    }

    @Override
    public String getContentType() {
        return urlConnection.getContentType();
    }

    @Override
    public String getResponseHeader(String headerName) {
        return urlConnection.getHeaderField(headerName);
    }

    @Override
    public InputStream getResponseInputStream() {
        return responseStream;
    }

    @Override
    public long getRequestContentLength() {
        return this.contentLength;
    }

    @Override
    public int getHttpResponseCode() {
        return this.httpResponseCode;
    }
}
