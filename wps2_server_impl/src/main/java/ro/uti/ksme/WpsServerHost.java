package ro.uti.ksme;

import com.sun.net.httpserver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ro.uti.ksme.wps.wps2_server.handlers.WpsPostHandler;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ErrorService;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.KeyStore;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WpsServerHost {

    private static final Logger LOGGER = LoggerFactory.getLogger(WpsServerHost.class);
    private static final String PROP_NAME = "wps2_config.properties";

    public static void main(String[] args) {

        try {
            long exec = 0;
            if (LOGGER.isDebugEnabled()) {
                exec = System.currentTimeMillis();
            }
            final Properties properties = getProperties();
            final String server_url = properties.getProperty("SERVER_URL");
            final String server_port = properties.getProperty("SERVER_PORT");
            boolean isHttps = Boolean.parseBoolean(properties.getProperty("IS_SERVER_HTTPS"));
            if (server_url != null && server_port != null) {
                //create context
                ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring_app_context.xml");
                WpsPostHandler wpsPostHandler = applicationContext.getBean("wpsPostHandler", WpsPostHandler.class);

                //create httpServer or httpsServer
                InetSocketAddress inetSocketAddress = new InetSocketAddress(server_url, Integer.valueOf(server_port));
                if (!isHttps) {
                    HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
                    HttpContext context = httpServer.createContext("/wps", wpsPostHandler);
                    setAuthIfAvailable(context, properties);
                    ExecutorService pool = Executors.newFixedThreadPool(Wps2ServerProps.getHttpServerExecutorThreadPool());
                    httpServer.setExecutor(pool);
                    httpServer.start();
                } else {
                    HttpsServer httpsServer = HttpsServer.create(inetSocketAddress, 0);
                    SSLContext sslContext = SSLContext.getInstance("TLS");
                    char[] password = Wps2ServerProps.getjksTokenPassword().toCharArray();
                    KeyStore ks = KeyStore.getInstance("PKCS12");
                    FileInputStream fis = new FileInputStream("wps2_server.pfx");
                    ks.load(fis, password);
                    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                    kmf.init(ks, password);
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                    tmf.init(ks);
                    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                    httpsServer.setHttpsConfigurator(getHttpsConfig(sslContext));
                    HttpContext context = httpsServer.createContext("/wps", wpsPostHandler);
                    setAuthIfAvailable(context, properties);
                    ExecutorService pool = Executors.newFixedThreadPool(Wps2ServerProps.getHttpServerExecutorThreadPool());
                    httpsServer.setExecutor(pool);
                    httpsServer.start();

                }
                if (LOGGER.isDebugEnabled()) {
                    exec = System.currentTimeMillis() - exec;
                    StringBuilder sb = new StringBuilder();
                    if (isHttps) {
                        sb.append("HttpsServer Started in ");
                    } else {
                        sb.append("HttpServer Started in ");
                    }
                    sb.append(exec).append(" ms");
                    LOGGER.info(sb.toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(-1);
        }
    }

    private static HttpsConfigurator getHttpsConfig(SSLContext sslContext) {
        return new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters httpsParameters) {
                try {
                    SSLContext context = getSSLContext();
                    SSLEngine engine = context.createSSLEngine();
                    httpsParameters.setNeedClientAuth(false);
                    httpsParameters.setCipherSuites(engine.getEnabledCipherSuites());
                    httpsParameters.setProtocols(engine.getEnabledProtocols());

                    SSLParameters sslParameters = context.getSupportedSSLParameters();

                    httpsParameters.setSSLParameters(sslParameters);
                } catch (Exception e) {
                    LOGGER.error("Failed to create HTTPS port " + e.getMessage(), e);
                }
            }
        };
    }

    private static Properties getProperties() {
        Properties properties = null;
        try {
            URL url = Wps2ServerProps.getExternalPropsUrl();
            if (url == null) {
                url = WpsServerHost.class.getClassLoader().getResource(PROP_NAME);
            }
            if (url == null) {
                LOGGER.error("ERROR >>>> Unable to find properties file with name: " + PROP_NAME);
                throw new Exception("ERROR >>>> Unable to find properties file with name: " + PROP_NAME);
            }
            properties = new Properties();
            properties.load(new InputStreamReader(url.openStream()));
        } catch (Exception e) {
            LOGGER.error("ERROR >>>> Could not load properties " + e.getMessage(), e);
        }
        return properties;
    }

    private static void setAuthIfAvailable(HttpContext context, Properties props) {
        final String usernameProp = props.getProperty("SERVER_AUTH_USER");
        final String passwordProp = props.getProperty("SERVER_AUTH_PASSWORD");
        if (usernameProp != null && usernameProp.trim().length() > 0 && passwordProp != null && passwordProp.trim().length() > 0) {
            context.setAuthenticator(new BasicAuthenticator("/") {
                @Override
                public boolean checkCredentials(String user, String password) {
                    return user.equals(usernameProp) && password.equals(passwordProp);
                }

                @Override
                public Result authenticate(HttpExchange httpExchange) {
                    Result authenticate = super.authenticate(httpExchange);
                    if (authenticate instanceof Retry && ((Retry) authenticate).getResponseCode() == 401) {
                        Authenticator.Retry retry = (Authenticator.Retry) authenticate;
                        InputStream is = null;
                        try (OutputStream out = httpExchange.getResponseBody()) {
                            String s = ErrorService.generateErrorMessageXml(retry);
                            httpExchange.sendResponseHeaders(retry.getResponseCode(), s.length());
                            httpExchange.getResponseHeaders().add("Content-Type", "application/xml");
                            is = new ByteArrayInputStream(s.getBytes());
                            byte[] buf = new byte[1024];
                            int length = is.read(buf);
                            while (length != -1) {
                                out.write(buf, 0, length);
                                length = is.read(buf);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (is != null) {
                                    is.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return authenticate;
                }
            });
        }
    }
}
