package ro.uti.ksme.wps.wps2_client;

import ro.uti.ksme.wps.wps2_client.connection_util.HttpClient;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientImpl;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/25/2019
 * Time: 4:11 PM
 */
public abstract class AbstractWps2Client implements WPS2Client {

    protected final URL serverUrl;
    protected HttpClient httpClient;

    public AbstractWps2Client(final URL serverUrl) {
        this(serverUrl, new HttpClientImpl());
    }

    public AbstractWps2Client(final URL serverUrl, int requestTimeout) {
        this(serverUrl, new HttpClientImpl());
        this.httpClient.setConnectionTimeout(requestTimeout);
    }

    public AbstractWps2Client(final URL serverUrl, final HttpClient httpClient) {
        if (serverUrl == null) {
            throw new NullPointerException("NoServerUrlProvided");
        }
        if (httpClient == null) {
            throw new NullPointerException("NoHttpClientGenerated");
        }
        this.serverUrl = serverUrl;
        this.httpClient = httpClient;
    }

}
