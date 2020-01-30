package ro.uti.ksme.wps.wps2_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Data;
import ro.uti.ksme.wps.wps2.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDefinitionType;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/28/2019
 * Time: 11:57 AM
 */
public class Wps2ClientExecutionHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ClientExecutionHelper.class);
    private static String serverUrl = null;
    public static String user = null;
    public static String password = null;

    public static ExecuteProcessRequest executeDemoProcessDownloadTiff(ExecuteProcessRequest executeProcessRequest) {
        executeProcessRequest.setIdentifier("downloadTiff");
        Data data = new Data();
        data.getContent().add(
                "<wps:BoundingBoxData xmlns:wps=\"http://www.opengis.net/wps/2.0\" xmlns:ows=\"http://www.opengis.net/ows/2.0\">\n" +
                        "        \t\t<wps:Format mimeType=\"text/plain\"/>\n" +
                        "        \t\t<wps:SupportedCRS default=\"true\">EPSG:4326</wps:SupportedCRS>\n" +
                        "        \t\t<ows:BoundingBox dimensions=\"2\">\n" +
                        "        \t\t\t<ows:LowerCorner>22.4 55.6</ows:LowerCorner>\n" +
                        "            \t\t<ows:UpperCorner>44.2 23.6</ows:UpperCorner>\n" +
                        "        \t\t</ows:BoundingBox>\n" +
                        "\t\t\t</wps:BoundingBoxData>\n");
        executeProcessRequest.addInput("downloadTiff:bboxInput", Collections.singletonList(data));
        OutputDefinitionType out = new OutputDefinitionType();
        out.setId("downloadTiff:rawDataExecuteResponse");
        out.setTransmission(DataTransmissionModeType.VALUE);
        executeProcessRequest.setOutputDefinitionType(out);
        return executeProcessRequest;
    }

    public static ExecuteProcessRequest executeLiteralDataProcess(ExecuteProcessRequest executeProcessRequest, int i) {
        executeProcessRequest.setIdentifier("literalDataProcess");
        Data data = new Data();
        data.getContent().add("TestData&Increment" + i);
        executeProcessRequest.addInput("literalDataProcess:inputField", Collections.singletonList(data));
        OutputDefinitionType out = new OutputDefinitionType();
        out.setId("literalDataProcess:result");
        out.setTransmission(DataTransmissionModeType.VALUE);
        executeProcessRequest.setOutputDefinitionType(out);
        return executeProcessRequest;
    }

    public static int getRandomNumberInRange(int max) {
        Random r = new Random();
        return r.nextInt((max) + 1);
    }

    public static String getServerUrl() {
        if (serverUrl != null) {
            return serverUrl;
        }
        String staticUrl = "http://localhost:9001/wps";
        try {
            Properties testProps = new Properties();
            URL url = Wps2ClientExecutionHelper.class.getClassLoader().getResource("wps2_server.properties");
            if (url == null) {
                LOGGER.info("Could not find wps2_server.properties... will use default url " + staticUrl);
                serverUrl = staticUrl;
            } else {
                testProps.load(new InputStreamReader(url.openStream()));
                String fromProps = testProps.getProperty("wps2_server_url_to_use_in_testing");
                user = testProps.getProperty("wps2_server_auth_user");
                password = testProps.getProperty("wps2_server_auth_password");
                if (fromProps != null && fromProps.trim().length() > 0) {
                    serverUrl = fromProps;
                } else {
                    serverUrl = staticUrl;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in trying to get url from props... using default url " + staticUrl + " \nCause:\n" + e.getMessage(), e);
        }
        return staticUrl;
    }
}
