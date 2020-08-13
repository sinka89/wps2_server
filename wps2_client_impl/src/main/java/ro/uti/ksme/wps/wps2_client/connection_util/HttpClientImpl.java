package ro.uti.ksme.wps.wps2_client.connection_util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_client.exception.Wps2ServerException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/25/2019
 * Time: 5:24 PM
 */
public class HttpClientImpl implements HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientImpl.class);
    private static final int DEFAULT_TIMEOUT = 30;
    private static final LogStreamContentFunctional LOG_STREAM_XML = postContent -> {
        byte[] bytes = IOUtils.toByteArray(postContent);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("INFO_WPS2_CLIENT >>>> The following data is sent to the Wps2 Server implementation \n" + new String(bytes) + "\n");
        }
        return new ByteArrayInputStream(bytes);
    };

    private String user;
    private String password;
    private int connectionTimeout = DEFAULT_TIMEOUT;

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }

    @Override
    public HttpClientResponse post(URL serverUrl, InputStream postContent, String postContentType) throws IOException {
        if (postContentType != null && postContentType.matches(".*/xml.*")) {
            postContent = LOG_STREAM_XML.log(postContent);
        }
        URLConnection connection = openConnection(serverUrl);
        if (connection instanceof HttpURLConnection) {
            final int status = ((HttpURLConnection) connection).getResponseCode();
            boolean redirected = false;
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == 401) {
                    throw new Wps2ServerException("Response code 401, Unauthorized");
                } else if (status > 300 && status < 400) {
                    redirected = true;
                }
            }
            if (redirected) {
                String newUrl = connection.getHeaderField("Location");
                if (!hasText(newUrl)) {
                    throw new Wps2ServerException(String.format("Http status received: %1$s for url: %2$s, tried and failed to redirect!", status, newUrl));
                }
                connection = openConnection(new URL(newUrl));
            }
            ((HttpURLConnection) connection).setRequestMethod("POST");
        }
        connection.setDoOutput(true);
        if (postContentType != null) {
            connection.setRequestProperty("Content-type", postContentType);
        }
        connection.connect();

        OutputStream os = connection.getOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = postContent.read(buffer)) > -1) {
                os.write(buffer, 0, count);
            }
        } finally {
            os.flush();
            os.close();
        }
        return new HttpClientResponseImpl(connection);
    }

    private URLConnection openConnection(URL serverUrl) throws IOException {
        URLConnection connection = serverUrl.openConnection();
        final boolean http = connection instanceof HttpURLConnection;
        if (http && this.connectionTimeout > 0) {
            connection.setConnectTimeout(1000 * this.connectionTimeout);
        }
        if (http && this.user != null && this.password != null) {
            String userAndPass = this.user + ":" + this.password;
            String encodedAuth = Base64.getEncoder().encodeToString(userAndPass.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        }
        return connection;
    }

    private static boolean hasText(String str) {
        if (str != null && str.length() > 0) {
            int length = str.length();
            for (int i = 0; i < length; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
