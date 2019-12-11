package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : SinKa
 * Date: 12/11/2019
 * Time: 9:52 PM
 */
public class MonitorThread implements Runnable {
    private final CancellableRunnable processWorker;
    private Thread worker;
    private int interval = 1000;
    private int maximumPasses = 10;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicBoolean stopped = new AtomicBoolean(true);

    public MonitorThread(CancellableRunnable processWorker) {
        this.processWorker = processWorker;
    }

    @Override
    public void run() {
        running.set(true);
        stopped.set(false);
        AtomicInteger passes = new AtomicInteger(0);
        while (running.get()) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Monitor Thread interrupted, Failed to complete operation");
            }
            processWorker.stop();
            if (!processWorker.isStopped()) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Monitor Thread interrupted, Failed to complete operation");
                }
                if (passes.getAndAdd(1) > maximumPasses) {
                    processWorker.forceStop();
                }
            } else {
                running.set(false);
            }
        }
        stopped.set(true);
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    public void interrupt() {
        running.set(false);
        worker.interrupt();
    }

    public boolean isRunning() {
        return running.get();
    }

    public boolean isStopped() {
        return stopped.get();
    }
}
