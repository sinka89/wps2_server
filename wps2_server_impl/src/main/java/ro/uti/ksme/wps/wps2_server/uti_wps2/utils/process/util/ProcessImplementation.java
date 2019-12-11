package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.CancellableRunnable;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/11/2019
 * Time: 10:33 AM
 * <p>
 * Contract for Wps2 Process
 * <p>
 * listOfCancelableResources represents a list of resources that have to be manually added in the Process
 * that on dismiss the server will try to force close no matter the state of the process or result.
 */
public interface ProcessImplementation {
    ProcessResultWrapper<?> execute();

    void closeAdditionalResources(CancellableRunnable runnable);
}
