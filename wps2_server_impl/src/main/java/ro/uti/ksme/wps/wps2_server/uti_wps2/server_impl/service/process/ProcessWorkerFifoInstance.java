package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public enum ProcessWorkerFifoInstance {
    INSTANCE;

    public ConcurrentLinkedQueue<ProcessWorker> workerFifo;
    public ConcurrentLinkedQueue<UUID> workerUuidFifo;
    public ConcurrentHashMap<UUID, ProcessWorker> uuidWorkerMap;

    ProcessWorkerFifoInstance() {
        workerFifo = new ConcurrentLinkedQueue<>();
        workerUuidFifo = new ConcurrentLinkedQueue<>();
        uuidWorkerMap = new ConcurrentHashMap<>();
    }
}
