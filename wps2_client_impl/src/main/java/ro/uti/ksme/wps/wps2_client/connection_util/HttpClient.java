package ro.uti.ksme.wps.wps2_client.connection_util;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/25/2019
 * Time: 5:23 PM
 */
public interface HttpClient {

    String getUser();

    void setUser(String user);

    String getPassword();

    void setPassword(String password);

    int getConnectionTimeout();

    void setConnectionTimeout(int timeout);

    HttpClientResponse post(URL serverUrl, ByteArrayOutputStream postContent, String postContentType) throws IOException;
}
