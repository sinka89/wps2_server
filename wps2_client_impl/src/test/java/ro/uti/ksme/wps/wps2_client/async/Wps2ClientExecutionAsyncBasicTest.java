package ro.uti.ksme.wps.wps2_client.async;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ProcessState;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.StatusInfo;
import ro.uti.ksme.wps.wps2_client.*;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.response.WPS2ExecuteProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2StatusInfoResponse;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/28/2019
 * Time: 11:53 AM
 */
public class Wps2ClientExecutionAsyncBasicTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ClientTest.class);
    private static Process PROCESS = null;
    private static WPS2Client WPS2CLIENT;

    @SuppressWarnings("Duplicates")
    @BeforeClass
    public static void init() {
        try {
            PROCESS = Wps2ServerInitializer.getProcess();
            if (PROCESS != null) {
                PROCESS.waitFor(2, TimeUnit.SECONDS);
                if (!PROCESS.isAlive()) {
                    LOGGER.info("Port in use... probably another instance is opened... will continue.");
                    PROCESS.destroy();
                }
            } else {
                LOGGER.info("Server init failed... will continue");
            }
            WPS2CLIENT = new WPS2ClientImpl(new URL(Wps2ClientExecutionHelper.getServerUrl()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @AfterClass
    public static void closeTest() {
        if (PROCESS != null) {
            PROCESS.destroy();
        }
    }

    @Test
    public void executeProcessRequestSyncDocument() {
        ExecuteProcessRequest executeProcessRequest = Wps2ClientExecutionHelper.executeDemoProcessDownloadTiff(WPS2CLIENT.createExecuteProcessRequest());
        executeProcessRequest.setResponseType(ResponseType.DOCUMENT);
        executeProcessRequest.setExecutionType(JobControlOps.ASYNC);
        WPS2ExecuteProcessResponse wps2ExecuteProcessResponse = WPS2CLIENT.executeProcess(executeProcessRequest);
        Assert.assertNotNull(wps2ExecuteProcessResponse);
        Assert.assertNotNull(wps2ExecuteProcessResponse.getExecutionStatusAsync());
        Assert.assertNull(wps2ExecuteProcessResponse.getExceptionReport());
        //get UUID to continue checking for complete execution...
        String uuid = getUUIDForAsyncJobFromStatus(wps2ExecuteProcessResponse.getExecutionStatusAsync());
        WPS2StatusInfoResponse statusInfoForProcess = WPS2CLIENT.getStatusInfoForProcess(uuid);
        Assert.assertNotNull(statusInfoForProcess);
        Assert.assertNotNull(statusInfoForProcess.getStatusInfo());
        Assert.assertNull(statusInfoForProcess.getExceptionReport());
        if (statusInfoForProcess.getStatusInfo().getStatus().equals(ProcessState.FINISHED.name())) {
            //check result
            WPS2ExecuteProcessResponse processResult = WPS2CLIENT.getProcessResult(uuid);
            Assert.assertNotNull(processResult);
            Assert.assertNotNull(processResult.getExecutionResult());
            Assert.assertNull(processResult.getExceptionReport());
        }
    }

    @Test
    public void executeProcessRequestSyncRaw() {
        ExecuteProcessRequest executeProcessRequest = Wps2ClientExecutionHelper.executeDemoProcessDownloadTiff(WPS2CLIENT.createExecuteProcessRequest());
        executeProcessRequest.setResponseType(ResponseType.RAW);
        executeProcessRequest.setExecutionType(JobControlOps.ASYNC);
        WPS2ExecuteProcessResponse wps2ExecuteProcessResponse = WPS2CLIENT.executeProcess(executeProcessRequest);
        Assert.assertNotNull(wps2ExecuteProcessResponse);
        Assert.assertNotNull(wps2ExecuteProcessResponse.getExecutionStatusAsync());
        Assert.assertNull(wps2ExecuteProcessResponse.getExceptionReport());
        //get UUID to continue checking for complete execution...
        String uuid = getUUIDForAsyncJobFromStatus(wps2ExecuteProcessResponse.getExecutionStatusAsync());
        WPS2StatusInfoResponse statusInfoForProcess = WPS2CLIENT.getStatusInfoForProcess(uuid);
        Assert.assertNotNull(statusInfoForProcess);
        Assert.assertNotNull(statusInfoForProcess.getStatusInfo());
        Assert.assertNull(statusInfoForProcess.getExceptionReport());
        if (statusInfoForProcess.getStatusInfo().getStatus().equals(ProcessState.FINISHED.name())) {
            //check result
            WPS2ExecuteProcessResponse processResult = WPS2CLIENT.getProcessResult(uuid);
            Assert.assertNotNull(processResult);
            Assert.assertNotNull(processResult.getRawResponseStream());
            Assert.assertNull(processResult.getExceptionReport());
        }
    }

    private String getUUIDForAsyncJobFromStatus(StatusInfo st) {
        return st.getJobID();
    }

}
