package ro.uti.ksme.wps.wps2_client.async;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;
import ro.uti.ksme.wps.wps2_client.*;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.response.WPS2ExecuteProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2StatusInfoResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : SinKa
 * Date: 12/11/2019
 * Time: 10:27 PM
 */
public class Wps2_S_C_MultipleAsyncInfiniteLoopDismiss {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ClientTest.class);
    private static Process PROCESS = null;
    private static WPS2Client WPS2CLIENT;
    private static boolean isServerActiveFromAnotherSource = false;

    @Before
    public void init() {
        try {
            PROCESS = Wps2ServerInitializer.getProcess();
            if (PROCESS != null) {
                PROCESS.waitFor(2, TimeUnit.SECONDS);
                if (!PROCESS.isAlive()) {
                    LOGGER.info("Port in use... probably another instance is opened... will continue.");
                    PROCESS.destroy();
                    isServerActiveFromAnotherSource = true;
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

    @After
    public void closeTest() {
        if (PROCESS != null) {
            PROCESS.destroy();
        }
    }

    private WPS2Client getNewWps2Client() {
        try {
            WPS2ClientImpl wps2Client = new WPS2ClientImpl(new URL(Wps2ClientExecutionHelper.getServerUrl()));
            if (Wps2ClientExecutionHelper.user != null && Wps2ClientExecutionHelper.password != null) {
                wps2Client.getHttpClient().setUser(Wps2ClientExecutionHelper.user);
                wps2Client.getHttpClient().setPassword(Wps2ClientExecutionHelper.password);
            }
            return wps2Client;
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    @Test
    public void testIfProcessesCloseWhenDismissIsInvoked() {
        int executeProcesses = 5;
        if (isServerActiveFromAnotherSource) {
            executeProcesses = 200;
        }
        ExecuteProcessRequest executeProcessRequest = Wps2ClientExecutionHelper.executeDemoProcessDownloadTiff(WPS2CLIENT.createExecuteProcessRequest());
        executeProcessRequest.setResponseType(ResponseType.RAW);
        executeProcessRequest.setExecutionType(JobControlOps.ASYNC);
        List<String> uuids = new ArrayList<>();
        for (int i = 0; i < executeProcesses; ++i) {
            WPS2ExecuteProcessResponse wps2ExecuteProcessResponse = WPS2CLIENT.executeProcess(executeProcessRequest);
            Assert.assertNotNull(wps2ExecuteProcessResponse.getExecutionStatusAsync());
            Assert.assertNull(wps2ExecuteProcessResponse.getExceptionReport());
            Assert.assertNull(wps2ExecuteProcessResponse.getExecutionResult());
            uuids.add(wps2ExecuteProcessResponse.getExecutionStatusAsync().getJobID());
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        while (uuids.size() > 0) {
            int listSize = uuids.size();
            int randomNumberInRange = Wps2ClientExecutionHelper.getRandomNumberInRange(listSize - 1);
            WPS2Client wps2Client = getNewWps2Client();
            Assert.assertNotNull(wps2Client);
            WPS2StatusInfoResponse wps2StatusInfoResponse = wps2Client.dismissProcess(uuids.remove(randomNumberInRange));
            Assert.assertNotNull(wps2StatusInfoResponse.getStatusInfo());
            Assert.assertNull(wps2StatusInfoResponse.getExceptionReport());
        }
    }

}
