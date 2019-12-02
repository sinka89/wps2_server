package ro.uti.ksme.wps.wps2_client;

import ro.uti.ksme.wps.wps2_client.connection_util.HttpClient;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.response.WPS2DescribeProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2ExecuteProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2GetCapabilitiesResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2StatusInfoResponse;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/26/2019
 * Time: 11:30 AM
 */
public interface WPS2Client {

    WPS2GetCapabilitiesResponse getCapabilities();

    WPS2DescribeProcessResponse describeProcess(String identifier, String language);

    WPS2StatusInfoResponse getStatusInfoForProcess(String identifier);

    WPS2StatusInfoResponse dismissProcess(String identifier);

    ExecuteProcessRequest createExecuteProcessRequest();

    WPS2ExecuteProcessResponse executeProcess(ExecuteProcessRequest executeRequest);

    WPS2ExecuteProcessResponse getProcessResult(String identifier);

    HttpClient getHttpClient();
}
