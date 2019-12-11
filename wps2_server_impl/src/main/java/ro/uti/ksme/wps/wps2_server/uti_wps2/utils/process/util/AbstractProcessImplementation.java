package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.CancellableRunnable;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.MonitorThread;

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
    public final void closeAdditionalResources(CancellableRunnable runnable) {
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
                        }
                    } else if (listOfCancelableResources.get(i) instanceof HttpURLConnection) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) listOfCancelableResources.get(i);
                        httpURLConnection.disconnect();
                    } else if (listOfCancelableResources.get(i) instanceof Thread) {
                        Thread thread = (Thread) listOfCancelableResources.get(i);
                        if (thread.getId() == runnable.getWorker().getId()) {
                            MonitorThread monitorThread = new MonitorThread(runnable);
                            monitorThread.start();
                        }
                    }
                    listOfCancelableResources.remove(i);
                    --size;
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
