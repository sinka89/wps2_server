package ro.uti.ksme.wps.wps2_client;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_client.response.WPS2DescribeProcessResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2GetCapabilitiesResponse;
import ro.uti.ksme.wps.wps2_client.response.WPS2StatusInfoResponse;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/26/2019
 * Time: 9:41 AM
 */
public class Wps2ClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ClientTest.class);

    private static Process PROCESS = null;
    private static WPS2Client WPS2CLIENT;
    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    public void getCapabilitiesTest() {
        WPS2GetCapabilitiesResponse capabilities = WPS2CLIENT.getCapabilities();
        Assert.assertNotNull(capabilities);
        Assert.assertNull(capabilities.getExceptionResponse());
        Assert.assertNotNull(capabilities.getWpsCapabilitiesTypeResponse());
    }

    @Test
    public void describeProcessWithSpecificIdentifierTest() {
        WPS2DescribeProcessResponse processOfferings = WPS2CLIENT.describeProcess("demoProcessDownloadTiff", "");
        Assert.assertNotNull(processOfferings);
        Assert.assertNotNull(processOfferings.getProcessOfferings());
        Assert.assertNull(processOfferings.getExceptionReport());
        Assert.assertTrue(processOfferings.getProcessOfferings().getProcessOffering().size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void describeProcessNoIdentifierProvided() {
        WPS2CLIENT.describeProcess(null, null);
        exception.expect(NullPointerException.class);
    }

    @Test
    public void describeProcessWps2ServerExceptionContainsExceptionReport() {
        WPS2DescribeProcessResponse nonExistingProcessName = WPS2CLIENT.describeProcess("nonExistingProcessName", null);
        Assert.assertNotNull(nonExistingProcessName);
        Assert.assertNull(nonExistingProcessName.getProcessOfferings());
        Assert.assertNotNull(nonExistingProcessName.getExceptionReport());
    }

    @Test(expected = NullPointerException.class)
    public void getStatusNoIdentifierProvided() {
        WPS2CLIENT.getStatusInfoForProcess(null);
        exception.expect(NullPointerException.class);
    }

    @Test(expected = NullPointerException.class)
    public void getResultNoIdentifierProvided() {
        WPS2CLIENT.getProcessResult(null);
        exception.expect(NullPointerException.class);
    }

    @Test
    public void getStatusContainsExceptionReport() {
        WPS2StatusInfoResponse nonExistingProcessUUID = WPS2CLIENT.getStatusInfoForProcess("nonExistingProcessUUID");
        Assert.assertNotNull(nonExistingProcessUUID);
        Assert.assertNull(nonExistingProcessUUID.getStatusInfo());
        Assert.assertNotNull(nonExistingProcessUUID.getExceptionReport());
    }

    @Test(expected = NullPointerException.class)
    public void dismissProcessNoIdentifierProvided() {
        WPS2CLIENT.dismissProcess(null);
        exception.expect(NullPointerException.class);
    }

    @Test
    public void dismissProcessContainsExceptionReport() {
        WPS2StatusInfoResponse nonExistingProcessUUID = WPS2CLIENT.dismissProcess("nonExistingProcessUUID");
        Assert.assertNotNull(nonExistingProcessUUID);
        Assert.assertNull(nonExistingProcessUUID.getStatusInfo());
        Assert.assertNotNull(nonExistingProcessUUID.getExceptionReport());
    }
}
