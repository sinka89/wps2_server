package ro.uti.ksme.wps.wps2_client;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessOfferings;
import ro.uti.ksme.wps.wps2.pojo.wps._2.WPSCapabilitiesType;
import ro.uti.ksme.wps.wps2_client.exception.Wps2ServerException;

import java.net.URL;
import java.util.Optional;
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

    @BeforeClass
    public static void init() {
        try {
            PROCESS = Wps2ServerInitializer.getProcess();
            if (PROCESS != null) {
                PROCESS.waitFor(2, TimeUnit.SECONDS);
                if (!PROCESS.isAlive()) {
                    LOGGER.info("Port in use... probably another instance is opened... will continue.");
                }
            } else {
                LOGGER.info("Server init failed... will continue");
            }
            WPS2CLIENT = new WPS2ClientImpl(new URL("http://localhost:9001/wps2_server"));
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
        Optional<WPSCapabilitiesType> capabilities = WPS2CLIENT.getCapabilities();
        Assert.assertTrue(capabilities.isPresent());
    }

    @Test
    public void describeProcessWithSpecificIdentifierTest() {
        Optional<ProcessOfferings> processOfferings = WPS2CLIENT.describeProcess("demoProcessDownloadTiff", "");
        Assert.assertTrue(processOfferings.isPresent());
        Assert.assertTrue(processOfferings.get().getProcessOffering().size() > 0);
    }

    @Test(expected = Wps2ServerException.class)
    public void describeProcessWithNonExistingIdentifierThrowsWps2ServerExceptionTest() {
        WPS2CLIENT.describeProcess("nonExistingProcessName", null);
        exception.expect(Wps2ServerException.class);
    }

    @Test(expected = NullPointerException.class)
    public void describeProcessNoIdentifierProvided() {
        WPS2CLIENT.describeProcess(null, null);
        exception.expect(NullPointerException.class);
    }

    @Test
    public void describeProcessWps2ServerExceptionContainsExceptionReport() {
        try {
            WPS2CLIENT.describeProcess("nonExistingProcessName", null);
        } catch (Wps2ServerException e) {
            Assert.assertNotNull(e.getExceptionReport());
        }
    }

    @Test(expected = NullPointerException.class)
    public void getStatusNoIdentifierProvided() {
        WPS2CLIENT.getStatusInfoForProcess(null);
        exception.expect(NullPointerException.class);
    }

    @Test
    public void getStatusWps2ServerExceptionContainsExceptionReport() {
        try {
            WPS2CLIENT.getStatusInfoForProcess("nonExistingProcessUUID");
        } catch (Wps2ServerException e) {
            Assert.assertNotNull(e.getExceptionReport());
        }
    }

    @Test(expected = NullPointerException.class)
    public void dismissProcessNoIdentifierProvided() {
        WPS2CLIENT.dismissProcess(null);
        exception.expect(NullPointerException.class);
    }

    @Test
    public void dismissProcessWps2ServerExceptionsContainsExceptionReport() {
        try {
            WPS2CLIENT.dismissProcess("nonExistingProcessUUID");
        } catch (Wps2ServerException e) {
            Assert.assertNotNull(e.getExceptionReport());
        }
    }
}
