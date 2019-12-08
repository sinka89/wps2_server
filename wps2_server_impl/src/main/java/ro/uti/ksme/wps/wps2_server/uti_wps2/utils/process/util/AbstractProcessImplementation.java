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
 * @author : SinKa
 * Date: 12/6/2019
 * Time: 9:24 PM
 */
public abstract class AbstractProcessImplementation implements ProcessImplementation {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessImplementation.class);

    private static final List<Object> listOfCancelableResources = new ArrayList<>();

    public final void closeAdditionalResources() {
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
