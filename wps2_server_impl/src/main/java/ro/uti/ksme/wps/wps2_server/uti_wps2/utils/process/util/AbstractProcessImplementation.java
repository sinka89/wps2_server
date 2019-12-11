package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : SinKa
 * Date: 12/6/2019
 * Time: 9:24 PM
 */
public abstract class AbstractProcessImplementation implements ProcessImplementation {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessImplementation.class);

    private final ReentrantLock lock = new ReentrantLock();
    private List<Object> listOfCancelableResources;

    /**
     * Take all closable resources from list and call appropriate action.
     */
    public final void closeAdditionalResources() {
        if (listOfCancelableResources != null && !listOfCancelableResources.isEmpty()) {
            lock.lock();
            try {
                int size = listOfCancelableResources.size();
                for (int i = 0; i < size; ++i) {
                    if (listOfCancelableResources.get(i) instanceof InputStream) {
                        try {
                            InputStream inputStream = (InputStream) listOfCancelableResources.get(i);
                            inputStream.close();
                        } catch (IOException e) {
                            LOGGER.error(e.getMessage(), e);
                        } finally {
                            listOfCancelableResources.remove(i);
                            --size;
                        }
                    } else if (listOfCancelableResources.get(i) instanceof HttpURLConnection) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) listOfCancelableResources.get(i);
                        httpURLConnection.disconnect();
                        listOfCancelableResources.remove(i);
                        --size;
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public final void addClosableResource(Object obj) {
        if (this.listOfCancelableResources == null) {
            this.listOfCancelableResources = new ArrayList<>();
        }
        listOfCancelableResources.add(obj);
    }
}
