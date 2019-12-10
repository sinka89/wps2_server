package ro.uti.ksme.wps.wps2_server.uti_wps2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ro.uti.ksme.wps.wps2.pojo.ows._2.*;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

/**
 * @author Bogdan-Adrian Sincu
 * <p>
 * Class used to parse and load data provided in wps2_config.properties into specific subclass props that are used for configuring part of the server functionality
 * <p>
 * If the .properties file is not present or cannot be parsed for some reason the System will shutdown with code 2
 */
@Component
public class Wps2ServerProps {

    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ServerProps.class);
    private static final String SERVER_PROPERTIES = "wps2_config.properties";
    public ServerGlobalProperties SERVER_GLOBAL_PROPERTIES;
    public ServerIdentificationProps SERVER_IDENTIFICATION_PROPS;
    public ServiceProviderProps SERVICE_PROVIDER_PROPS;
    public OperationsMetadataProps OPERATIONS_METADATA_PROPS;
    public CustomProps CUSTOM_PROPS;

    public Wps2ServerProps() {
        Properties wpsProps = new Properties();
        URL url = this.getClass().getClassLoader().getResource(SERVER_PROPERTIES);
        if (url == null) {
            LOGGER.error("ERROR >>>> Cannot find wps2_config.properties!!!");
            System.exit(2);
        } else {
            try {
                wpsProps.load(new InputStreamReader(url.openStream()));
                SERVER_GLOBAL_PROPERTIES = new ServerGlobalProperties(wpsProps);
                SERVER_IDENTIFICATION_PROPS = new ServerIdentificationProps(wpsProps);
                SERVICE_PROVIDER_PROPS = new ServiceProviderProps(wpsProps);
                OPERATIONS_METADATA_PROPS = new OperationsMetadataProps(wpsProps);
                CUSTOM_PROPS = new CustomProps(wpsProps);
            } catch (Exception e) {
                LOGGER.error("ERROR >>>> Could not load wps2_config.properties " + e.getMessage(), e);
                System.exit(2);
            }
        }
    }

    public static Integer getHttpServerExecutorThreadPool() {
        Integer threadCount = 24;
        try {
            Properties wpsPros = new Properties();
            URL url = Wps2ServerProps.class.getClassLoader().getResource(SERVER_PROPERTIES);
            if (url == null) {
                return threadCount;
            } else {
                wpsPros.load(new InputStreamReader(url.openStream()));
                threadCount = Integer.decode(wpsPros.getProperty("HTTP_SERVER_EXECUTOR_THREAD_POOL"));
            }
        } catch (Exception e) {
            LOGGER.error("ERROR >>>> Could not get PROP 'HTTP_SERVER_EXECUTOR_THREAD_POOL' using default pool: " + threadCount + "Cause:\n" + e.getMessage(), e);
        }
        return threadCount;
    }

    public static Integer getWpsProcessesExecutorThreadPool() {
        Integer threadCount = 32;
        try {
            Properties wpsProps = new Properties();
            URL url = Wps2ServerProps.class.getClassLoader().getResource(SERVER_PROPERTIES);
            if (url == null) {
                return threadCount;
            } else {
                wpsProps.load(new InputStreamReader(url.openStream()));
                threadCount = Integer.decode(wpsProps.getProperty("WPS_PROCESSES_EXECUTOR_THREAD_POOL"));
            }
        } catch (Exception e) {
            LOGGER.error("ERROR >>>> Could not get PROP 'WPS_PROCESSES_EXECUTOR_THREAD_POOL', using default pool: " + threadCount + " Cause:\n" + e.getMessage(), e);
        }

        return threadCount;
    }

    public static String getjksTokenPassword() throws NullPointerException {
        String pass = "";
        try {
            Properties wpsProps = new Properties();
            URL url = Wps2ServerProps.class.getClassLoader().getResource(SERVER_PROPERTIES);
            wpsProps.load(new InputStreamReader(url.openStream()));
            pass = wpsProps.getProperty("HTTPS_SERVER_JKS_TOKEN_PASS");
        } catch (Exception e) {
            LOGGER.error("ERROR >>>> Could not get 'PROP HTTPS_SERVER_JKS_TOKEN_PASS'");
        }
        return pass;
    }

    public class ServerGlobalProperties {
        public final String SERVICE;
        public final String SERVER_VERSION;
        public final String DEFAULT_LANGUAGE;
        public final String[] SUPPORTED_LANGUAGES;
        public final String[] SUPPORTED_VERSIONS;
        public final String[] PROCESS_CONTROL_OPTIONS;
        public final String[] SUPPORTED_FORMATS;

        public ServerGlobalProperties(Properties props) throws Exception {
            SERVICE = props.getProperty("SERVICE");
            SERVER_VERSION = props.getProperty("SERVER_VERSION");
            DEFAULT_LANGUAGE = props.getProperty("DEFAULT_LANGUAGE");
            String processControlOpts = props.getProperty("PROCESS_CONTROL_OPTIONS");
            if (processControlOpts == null || processControlOpts.isEmpty()) {
                throw new ExceptionInInitializerError("The property PROCESS_CONTROL_OPTIONS is not defined");
            }
            PROCESS_CONTROL_OPTIONS = props.getProperty("PROCESS_CONTROL_OPTIONS").split(",");
            String supportedLangs = props.getProperty("SUPPORTED_LANGUAGES");
            if (supportedLangs == null || supportedLangs.isEmpty()) {
                throw new ExceptionInInitializerError("The property SUPPORTED_LANGUAGES is not defined");
            }
            SUPPORTED_LANGUAGES = props.getProperty("SUPPORTED_LANGUAGES").split(",");
            String supportedVer = props.getProperty("SUPPORTED_VERSIONS");
            if (supportedVer == null || supportedVer.isEmpty()) {
                throw new ExceptionInInitializerError("The property SUPPORTED_VERSIONS is not defined");
            }
            SUPPORTED_VERSIONS = props.getProperty("SUPPORTED_VERSIONS").split(",");

            String supportedFormats = props.getProperty("SUPPORTED_FORMATS");
            if (supportedFormats == null || supportedFormats.isEmpty()) {
                throw new ExceptionInInitializerError("The property SUPPORTED_FORMATS is not defined");
            }
            SUPPORTED_FORMATS = props.getProperty("SUPPORTED_FORMATS").split(",");
        }
    }

    public class ServerIdentificationProps {
        public final CodeType SERVICE_TYPE;
        public final String[] SERVICE_TYPE_VERSIONS;
        public final LanguageStringType[] TITLE;
        public final String FEES;

        public ServerIdentificationProps(Properties props) throws Exception {
            SERVICE_TYPE = new CodeType();
            SERVICE_TYPE.setValue(props.getProperty("SERVICE_TYPE"));

            String serviceTypeVer = props.getProperty("SERVICE_TYPE_VERSIONS");
            if (serviceTypeVer == null || serviceTypeVer.isEmpty()) {
                throw new Exception("The property SERVICE_TYPE_VERSIONS is not defined");
            }
            SERVICE_TYPE_VERSIONS = props.getProperty("SERVICE_TYPE_VERSIONS").split(",");

            String title = props.getProperty("TITLE");
            if (title == null || title.isEmpty()) {
                throw new ExceptionInInitializerError("The property TITLE is not defined");
            }
            String[] titleSplit = title.split(";");
            if (titleSplit.length != SERVER_GLOBAL_PROPERTIES.SUPPORTED_LANGUAGES.length) {
                throw new ExceptionInInitializerError("The prop 'TITLE' does not contain a valid number of strings");
            }
            TITLE = new LanguageStringType[titleSplit.length];
            for (int i = 0; i < titleSplit.length; i++) {
                TITLE[i] = new LanguageStringType();
                TITLE[i].setValue(titleSplit[i]);
                TITLE[i].setLang(SERVER_GLOBAL_PROPERTIES.SUPPORTED_LANGUAGES[i]);
            }
            FEES = props.getProperty("FEES");
        }
    }

    public class ServiceProviderProps {
        public final String PROVIDER_NAME;
        public final OnlineResourceType PROVIDER_SITE;

        public ServiceProviderProps(Properties props) {
            PROVIDER_NAME = props.getProperty("PROVIDER_NAME");
            PROVIDER_SITE = new OnlineResourceType();
            PROVIDER_SITE.setHref(props.getProperty("PROVIDER_SITE"));
        }
    }

    public class OperationsMetadataProps {
        private static final String PORT_DELIMITER = ":";
        private static final String http = "http://";
        private static final String https = "https://";
        public final Operation GET_CAPABILITIES_OPERATION;
        public final Operation DESCRIBE_PROCESS_OPERATION;
        public final Operation EXECUTE_OPERATION;
        public final Operation GET_STATUS_OPERATION;
        public final Operation GET_RESULT_OPERATION;
        public final Operation DISMISS_OPERATION;

        public OperationsMetadataProps(Properties props) {
            if (props.getProperty("SERVER_URL") == null || props.getProperty("SERVER_PORT") == null || props.getProperty("IS_SERVER_HTTPS") == null) {
                throw new ExceptionInInitializerError("ERROR >>>> could not initialize, PROPS FOR DEFINING SERVER are not defined: SERVER_URL or SERVER_PORT or IS_SERVER_HTTPS");
            }
            final Boolean isHttps = Boolean.valueOf(props.getProperty("IS_SERVER_HTTPS"));
            String base;
            String withPort;
            final Integer serverPort = Integer.valueOf(props.getProperty("SERVER_PORT"));
            if (serverPort == 80 || serverPort == 8080 || serverPort == 8008) {
                withPort = "";
            } else {
                withPort = PORT_DELIMITER + props.getProperty("SERVER_PORT");
            }
            if (isHttps) {
                base = https;
            } else {
                base = http;
            }
            final String srvBase = base + props.getProperty("SERVER_URL") + withPort;
            ObjectFactory objectFactory = new ObjectFactory();
            if (props.getProperty("GETCAPABILITIES_POST_HREF") != null) {
                GET_CAPABILITIES_OPERATION = new Operation();
                GET_CAPABILITIES_OPERATION.setName("GetCapabilities");
                DCP dcp = new DCP();
                HTTP http = new HTTP();
                RequestMethodType post = new RequestMethodType();
                post.setHref(srvBase + props.getProperty("GETCAPABILITIES_POST_HREF"));
                http.getGetOrPost().add(objectFactory.createHTTPPost(post));
                dcp.setHTTP(http);
                GET_CAPABILITIES_OPERATION.getDCP().add(dcp);
            } else {
                GET_CAPABILITIES_OPERATION = null;
            }
            if (props.getProperty("DESCRIBEPROCESS_POST_HREF") != null) {
                DESCRIBE_PROCESS_OPERATION = new Operation();
                DESCRIBE_PROCESS_OPERATION.setName("DescribeProcess");
                DCP dcp = new DCP();
                HTTP http = new HTTP();
                RequestMethodType post = new RequestMethodType();
                post.setHref(srvBase + props.getProperty("DESCRIBEPROCESS_POST_HREF"));
                http.getGetOrPost().add(objectFactory.createHTTPPost(post));
                dcp.setHTTP(http);
                DESCRIBE_PROCESS_OPERATION.getDCP().add(dcp);
            } else {
                DESCRIBE_PROCESS_OPERATION = null;
            }
            if (props.getProperty("EXECUTE_POST_HREF") != null) {
                EXECUTE_OPERATION = new Operation();
                EXECUTE_OPERATION.setName("Execute");
                DCP dcp = new DCP();
                HTTP http = new HTTP();
                RequestMethodType post = new RequestMethodType();
                post.setHref(srvBase + props.getProperty("EXECUTE_POST_HREF"));
                http.getGetOrPost().add(objectFactory.createHTTPPost(post));
                dcp.setHTTP(http);
                EXECUTE_OPERATION.getDCP().add(dcp);
            } else {
                EXECUTE_OPERATION = null;
            }
            if (props.getProperty("GETRESULT_POST_HREF") != null) {
                GET_RESULT_OPERATION = new Operation();
                GET_RESULT_OPERATION.setName("GetResult");
                DCP dcp = new DCP();
                HTTP http = new HTTP();
                RequestMethodType post = new RequestMethodType();
                post.setHref(srvBase + props.getProperty("GETRESULT_POST_HREF"));
                http.getGetOrPost().add(objectFactory.createHTTPPost(post));
                dcp.setHTTP(http);
                GET_RESULT_OPERATION.getDCP().add(dcp);
            } else {
                GET_RESULT_OPERATION = null;
            }
            if (props.getProperty("GETSTATUS_POST_HREF") != null) {
                GET_STATUS_OPERATION = new Operation();
                GET_STATUS_OPERATION.setName("GetStatus");
                DCP dcp = new DCP();
                HTTP http = new HTTP();
                RequestMethodType post = new RequestMethodType();
                post.setHref(srvBase + props.getProperty("GETSTATUS_POST_HREF"));
                http.getGetOrPost().add(objectFactory.createHTTPPost(post));
                dcp.setHTTP(http);
                GET_STATUS_OPERATION.getDCP().add(dcp);
            } else {
                GET_STATUS_OPERATION = null;
            }
            if (props.getProperty("DISMISS_POST_HREF") != null) {
                DISMISS_OPERATION = new Operation();
                DISMISS_OPERATION.setName("Dismiss");
                DCP dcp = new DCP();
                HTTP http = new HTTP();
                RequestMethodType post = new RequestMethodType();
                post.setHref(srvBase + props.getProperty("DISMISS_POST_HREF"));
                http.getGetOrPost().add(objectFactory.createHTTPPost(post));
                dcp.setHTTP(http);
                DISMISS_OPERATION.getDCP().add(dcp);
            } else {
                DISMISS_OPERATION = null;
            }
        }

        public Operation[] getAllOperationsDetails() {
            return new Operation[]{
                    this.GET_CAPABILITIES_OPERATION,
                    this.DESCRIBE_PROCESS_OPERATION,
                    this.EXECUTE_OPERATION,
                    this.GET_STATUS_OPERATION,
                    this.GET_RESULT_OPERATION,
                    this.DISMISS_OPERATION
            };
        }
    }

    public class CustomProps {
        public final long BASE_PROCESS_POOLING_DELAY;
        public final long MAX_PROCESS_POOLING_DELAY;

        public CustomProps(Properties props) {
            BASE_PROCESS_POOLING_DELAY = Long.decode(props.getProperty("BASE_PROCESS_POOLING_DELAY"));
            MAX_PROCESS_POOLING_DELAY = Long.decode(props.getProperty("MAX_PROCESS_POOLING_DELAY"));
        }

    }
}
