package ro.uti.ksme;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ro.uti.ksme.wps.wps2_server.handlers.WpsPostHandler;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
            Properties properties = getProperties();
            String server_url = properties.getProperty("SERVER_URL");
            String server_port = properties.getProperty("SERVER_PORT");
            Boolean isHttps = Boolean.valueOf(properties.getProperty("IS_SERVER_HTTPS"));
            if (server_url != null && server_port != null) {
                //create context
                ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring_app_context.xml");
                WpsPostHandler wpsPostHandler = applicationContext.getBean("wpsPostHandler", WpsPostHandler.class);
//                Weld weld = new Weld();
//                WeldContainer container = weld.initialize();
//                WpsPostHandler wpsPostHandler = container.select(WpsPostHandler.class).get();


                //create httpServer or httpsServer
                InetSocketAddress inetSocketAddress = new InetSocketAddress(server_url, Integer.valueOf(server_port));
                if (!isHttps) {
                    HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
                    httpServer.createContext("/wps", wpsPostHandler);
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
                    httpsServer.createContext("/wps", wpsPostHandler);
                    ExecutorService pool = Executors.newFixedThreadPool(Wps2ServerProps.getHttpServerExecutorThreadPool());
                    httpsServer.setExecutor(pool);
                    httpsServer.start();

                }
//                container.shutdown();
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
            URL url = WpsServerHost.class.getClassLoader().getResource(PROP_NAME);
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
}
