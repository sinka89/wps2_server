package ro.uti.ksme.wps.wps2_client.async;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ProcessState;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Data;
import ro.uti.ksme.wps.wps2.pojo.wps._2.DataOutputType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Result;
import ro.uti.ksme.wps.wps2_client.*;
import ro.uti.ksme.wps.wps2_client.request.ExecuteProcessRequest;
import ro.uti.ksme.wps.wps2_client.response.WPS2ExecuteProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2StatusInfoResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/28/2019
 * Time: 11:54 AM
 */
public class Wps2ClientExecutionAsyncMultipleOpsTest {
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
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private WPS2Client getNewWps2Client() {
        try {
            return new WPS2ClientImpl(new URL("http://localhost:9001/wps2_server"));
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
        int timesToExecute = 10;
        if (isServerActiveFromAnotherSource) {
            timesToExecute = 200;
        }
        List<Map<String, Integer>> listOfProcessesToCheckForCorrectResult = new ArrayList<>();
        for (int i = 0; i < timesToExecute; ++i) {
            String uuid = evaluateResponseOfAsyncExecuteMultipleTimes(WPS2CLIENT, executeProcessRequest, i);
            listOfProcessesToCheckForCorrectResult.add(Stream.of(new AbstractMap.SimpleEntry<>(uuid, i)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        while (listOfProcessesToCheckForCorrectResult.size() > 0) {
            int listSize = listOfProcessesToCheckForCorrectResult.size();
            int randomNumberInRange = Wps2ClientExecutionHelper.getRandomNumberInRange(listSize - 1);
            Map<String, Integer> stringIntegerMap = listOfProcessesToCheckForCorrectResult.get(randomNumberInRange);
            String uuidFromMap = null;
            Integer iToCheckFromMap = null;
            if (stringIntegerMap.size() == 1) {
                for (Map.Entry<String, Integer> entry
                        : stringIntegerMap.entrySet()) {
                    uuidFromMap = entry.getKey();
                    iToCheckFromMap = entry.getValue();
                }
            }
            Assert.assertNotNull(uuidFromMap);
            Assert.assertNotNull(iToCheckFromMap);
            final String toCheck = uuidFromMap;
            final int toCheckInt = iToCheckFromMap;
            Runnable runnable = () -> checkOrWaitForProcess(WPS2CLIENT, toCheck, toCheckInt);
            runnable.run();
            listOfProcessesToCheckForCorrectResult.remove(randomNumberInRange);
        }
    }

    @Test
    public void executeProcessSyncMultipleTimesFromDifferentClient() {
        int timesToExecute = 10;
        if (isServerActiveFromAnotherSource) {
            timesToExecute = 200;
        }
        List<Map<String, Integer>> listOfProcessesToCheckForCorrectResult = new ArrayList<>();
        for (int i = 0; i < timesToExecute; ++i) {
            WPS2Client wps2Client = getNewWps2Client();
            Assert.assertNotNull(wps2Client);
            ExecuteProcessRequest executeProcessRequest = wps2Client.createExecuteProcessRequest();
            String uuid = evaluateResponseOfAsyncExecuteMultipleTimes(wps2Client, executeProcessRequest, i);
            listOfProcessesToCheckForCorrectResult.add(Stream.of(new AbstractMap.SimpleEntry<>(uuid, i)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        while (listOfProcessesToCheckForCorrectResult.size() > 0) {
            int listSize = listOfProcessesToCheckForCorrectResult.size();
            int randomNumberInRange = Wps2ClientExecutionHelper.getRandomNumberInRange(listSize - 1);
            Map<String, Integer> stringIntegerMap = listOfProcessesToCheckForCorrectResult.get(randomNumberInRange);
            String uuidFromMap = null;
            Integer iToCheckFromMap = null;
            if (stringIntegerMap.size() == 1) {
                for (Map.Entry<String, Integer> entry
                        : stringIntegerMap.entrySet()) {
                    uuidFromMap = entry.getKey();
                    iToCheckFromMap = entry.getValue();
                }
            }
            Assert.assertNotNull(uuidFromMap);
            Assert.assertNotNull(iToCheckFromMap);
            WPS2Client newWps2Client = getNewWps2Client();
            Assert.assertNotNull(newWps2Client);
            final String toCheck = uuidFromMap;
            final int toCheckInt = iToCheckFromMap;
            Runnable runnable = () -> checkOrWaitForProcess(newWps2Client, toCheck, toCheckInt);
            runnable.run();
            listOfProcessesToCheckForCorrectResult.remove(randomNumberInRange);
        }
    }

    private String evaluateResponseOfAsyncExecuteMultipleTimes(WPS2Client wps2Client, ExecuteProcessRequest executeProcessRequest, int i) {
        ExecuteProcessRequest exec = Wps2ClientExecutionHelper.executeLiteralDataProcess(executeProcessRequest, i);
        exec.setExecutionType(JobControlOps.ASYNC);
        exec.setResponseType(ResponseType.DOCUMENT);
        WPS2ExecuteProcessResponse wps2ExecuteProcessResponse = wps2Client.executeProcess(exec);
        Assert.assertNotNull(wps2ExecuteProcessResponse);
        Assert.assertNotNull(wps2ExecuteProcessResponse.getExecutionStatusAsync());
        Assert.assertNull(wps2ExecuteProcessResponse.getExceptionReport());
        String uuid = wps2ExecuteProcessResponse.getExecutionStatusAsync().getJobID();
        Assert.assertNotNull(uuid);
        //all validation completed the process was started -> return uuid so it will check the process later
        return uuid;
    }

    private void checkOrWaitForProcess(WPS2Client wps2Client, String uuid, int i) {
        WPS2StatusInfoResponse statusInfoForProcess = wps2Client.getStatusInfoForProcess(uuid);
        Assert.assertNotNull(statusInfoForProcess);
        Assert.assertNotNull(statusInfoForProcess.getStatusInfo());
        Assert.assertNull(statusInfoForProcess.getExceptionReport());
        if (statusInfoForProcess.getStatusInfo().getStatus().equals(ProcessState.FINISHED.name())) {
            //process was finished... trying to check result
            WPS2ExecuteProcessResponse processResult = WPS2CLIENT.getProcessResult(uuid);
            Assert.assertNotNull(processResult);
            Assert.assertNotNull(processResult.getExecutionResult());
            Assert.assertNull(processResult.getExceptionReport());
            Result executionResult = processResult.getExecutionResult();
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
        } else if (statusInfoForProcess.getStatusInfo().getStatus().equals(ProcessState.RUNNING.name()) ||
                statusInfoForProcess.getStatusInfo().getStatus().equals(ProcessState.ACCEPTED.name())) {
            //process is still running
            try {
                Thread.sleep(1000);
                this.checkOrWaitForProcess(wps2Client, uuid, i);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            Assert.fail("PROCESS IS NOT RUNNING OR FINISHED IT FAILED");
        }
    }
}
