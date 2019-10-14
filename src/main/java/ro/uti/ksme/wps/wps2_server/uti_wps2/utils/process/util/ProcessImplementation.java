package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/11/2019
 * Time: 10:33 AM
 *
 * Contract for Wps2 Process
 *
 * listOfCancelableResources represents a list of resources that have to be manually added in the Process
 * that on dismiss the server will try to force close no matter the state of the process or result.
 */
public interface ProcessImplementation {
    Logger LOGGER = LoggerFactory.getLogger(ProcessImplementation.class);

    List<Object> listOfCancelableResources = new ArrayList<>();

    Object execute();

    static void closeAdditionalResources() {
        if (!listOfCancelableResources.isEmpty()) {
            for (Object o : listOfCancelableResources) {
                if (o instanceof InputStream) {
                    try {
                        ((InputStream) o).close();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
                if (o instanceof HttpURLConnection) {
                    ((HttpURLConnection) o).disconnect();
                }
            }
        }
    }
}
