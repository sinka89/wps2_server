package ro.uti.ksme.wps.wps2_client.sync;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;
import ro.uti.ksme.wps.wps2_client.*;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.response.WPS2ExecuteProcessResponse;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/28/2019
 * Time: 10:05 AM
 */
public class Wps2ClientExecutionSyncBasicTest {
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
            if (Wps2ClientExecutionHelper.user != null && Wps2ClientExecutionHelper.password != null) {
                WPS2CLIENT.getHttpClient().setUser(Wps2ClientExecutionHelper.user);
                WPS2CLIENT.getHttpClient().setPassword(Wps2ClientExecutionHelper.password);
            }

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
        executeProcessRequest.setExecutionType(JobControlOps.SYNC);
        WPS2ExecuteProcessResponse wps2ExecuteProcessResponse = WPS2CLIENT.executeProcess(executeProcessRequest);
        Assert.assertNotNull(wps2ExecuteProcessResponse);
        Assert.assertNotNull(wps2ExecuteProcessResponse.getExecutionResult());
        Assert.assertNull(wps2ExecuteProcessResponse.getExceptionReport());
    }

    @Test
    public void executeProcessRequestSyncRaw() {
        ExecuteProcessRequest executeProcessRequest = Wps2ClientExecutionHelper.executeDemoProcessDownloadTiff(WPS2CLIENT.createExecuteProcessRequest());
        executeProcessRequest.setResponseType(ResponseType.RAW);
        executeProcessRequest.setExecutionType(JobControlOps.SYNC);
        WPS2ExecuteProcessResponse wps2ExecuteProcessResponse = WPS2CLIENT.executeProcess(executeProcessRequest);
        Assert.assertNotNull(wps2ExecuteProcessResponse);
        Assert.assertNotNull(wps2ExecuteProcessResponse.getRawResponseStream());
        Assert.assertNull(wps2ExecuteProcessResponse.getExceptionReport());
    }


}
