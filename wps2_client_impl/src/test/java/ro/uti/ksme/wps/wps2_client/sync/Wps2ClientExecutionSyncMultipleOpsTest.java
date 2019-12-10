package ro.uti.ksme.wps.wps2_client.sync;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Data;
import ro.uti.ksme.wps.wps2.pojo.wps._2.DataOutputType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Result;
import ro.uti.ksme.wps.wps2_client.*;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.response.WPS2ExecuteProcessResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/28/2019
 * Time: 11:44 AM
 */
public class Wps2ClientExecutionSyncMultipleOpsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ClientTest.class);
    private static Process PROCESS = null;
    private static WPS2Client WPS2CLIENT;
    private static boolean isServerActiveFromAnotherSource = false;

    @SuppressWarnings("Duplicates")
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

    @After
    public void closeTest() {
        if (PROCESS != null) {
            PROCESS.destroy();
        }
    }

    @Test
    public void executeProcessSyncMultipleTimesFromSameClient() {
        ExecuteProcessRequest executeProcessRequest = WPS2CLIENT.createExecuteProcessRequest();
        int timesToExecute = 5;
        if (isServerActiveFromAnotherSource) {
            timesToExecute = 20;
        }
        for (int i = 0; i < timesToExecute; ++i) {
            evaluateResponseOfSyncExecuteMultipleTimes(WPS2CLIENT, executeProcessRequest, i);
        }
    }

    @Test
    public void executeProcessSyncMultipleTimesFromDifferentClient() {
        int timesToExecute = 5;
        if (isServerActiveFromAnotherSource) {
            timesToExecute = 20;
        }
        for (int i = 0; i < timesToExecute; ++i) {
            WPS2Client wps2Client = getNewWps2Client();
            Assert.assertNotNull(wps2Client);
            ExecuteProcessRequest executeProcessRequest = wps2Client.createExecuteProcessRequest();
            evaluateResponseOfSyncExecuteMultipleTimes(wps2Client, executeProcessRequest, i);
        }
    }


    private void evaluateResponseOfSyncExecuteMultipleTimes(WPS2Client wps2Client, ExecuteProcessRequest executeProcessRequest, int i) {
        ExecuteProcessRequest exec = Wps2ClientExecutionHelper.executeLiteralDataProcess(executeProcessRequest, i);
        exec.setExecutionType(JobControlOps.SYNC);
        exec.setResponseType(ResponseType.DOCUMENT);
        WPS2ExecuteProcessResponse wps2ExecuteProcessResponse = wps2Client.executeProcess(exec);
        Assert.assertNotNull(wps2ExecuteProcessResponse);
        Assert.assertNull(wps2ExecuteProcessResponse.getExceptionReport());
        Assert.assertNotNull(wps2ExecuteProcessResponse.getExecutionResult());
        Result executionResult = wps2ExecuteProcessResponse.getExecutionResult();
        List<DataOutputType> output = executionResult.getOutput();
        //has only one output so no iterating
        Assert.assertEquals(1, output.size());
        DataOutputType dataOutputType = output.get(0);
        Assert.assertNotNull(dataOutputType.getData());
        Data data = dataOutputType.getData();
        Assert.assertNotNull(data.getContent());
        Assert.assertEquals(1, data.getContent().size());
        Object o = data.getContent().get(0);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof String);
        String result = o.toString();
        Assert.assertEquals("TestData&Increment" + i, result);
    }
}
