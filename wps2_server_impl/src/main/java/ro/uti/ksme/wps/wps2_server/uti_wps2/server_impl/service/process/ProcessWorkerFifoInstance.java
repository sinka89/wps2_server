package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.util.concurrent.ConcurrentLinkedQueue;

public enum ProcessWorkerFifoInstance {
    INSTANCE;

    public ConcurrentLinkedQueue<ProcessWorker> workerFifo;

    ProcessWorkerFifoInstance() {
        workerFifo = new ConcurrentLinkedQueue<>();
    }
}
