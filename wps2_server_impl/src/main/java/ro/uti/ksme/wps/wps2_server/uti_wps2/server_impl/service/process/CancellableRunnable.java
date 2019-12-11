package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/14/2019
 * Time: 10:39 AM
 */
public interface CancellableRunnable extends Runnable {

    void start();

    void cancel();

    void stop();

    void interrupt();

    boolean isRunning();

    boolean isStopped();

    void forceStop();

    Thread getWorker();
}
