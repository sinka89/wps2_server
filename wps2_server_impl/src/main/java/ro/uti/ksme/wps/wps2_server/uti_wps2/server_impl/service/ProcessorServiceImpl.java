package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Bogdan-Adrian Sincu
 * Class used to process async process execution requests
 */
@Service
public class ProcessorServiceImpl implements ProcessorService {

    private final ExecutorService executorService;
    private final int threadPool;
    private final ReentrantLock lock = new ReentrantLock(true);
    private ProcessManager processManager;

    public ProcessorServiceImpl() {
        threadPool = Wps2ServerProps.getWpsProcessesExecutorThreadPool();
        executorService = Executors.newFixedThreadPool(threadPool);
    }

    @Autowired
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Override
    public void cancelProgress(UUID jobId) {
        ((CancellableRunnable) ProcessWorkerMapInstance.INSTANCE.workerMap.get(jobId).get("runnable")).cancel();
        processManager.cancelProcess(jobId);
        ((Future<?>) ProcessWorkerMapInstance.INSTANCE.workerMap.get(jobId).get("future")).cancel(true);
        ProcessWorkerMapInstance.INSTANCE.workerMap.remove(jobId);
    }

    @Override
    public Future<?> executeNewProcessWorker(ProcessJob job, ProcessIdentifier processIdentifier, Map<URI, Object> dataMap) {
        ProcessWorker worker = new ProcessWorker(job, processIdentifier, processManager, dataMap, this);
        if (ProcessWorkerMapInstance.INSTANCE.workerMap.size() >= threadPool) {
            ProcessWorkerFifoInstance.INSTANCE.workerFifo.add(worker);
        } else {
            Future<?> future = executorService.submit(worker);
            lock.lock();
            try {
                Map<String, Object> m = new HashMap<>();
                m.put("future", future);
                m.put("runnable", worker);
                ProcessWorkerMapInstance.INSTANCE.workerMap.put(worker.getJobId(), m);
            } finally {
                lock.unlock();
            }
            return future;
        }
        return null;
    }


    @Override
    public void onProcessWFinish() {
        for (Map.Entry<UUID, Map<String, Object>> entry : ProcessWorkerMapInstance.INSTANCE.workerMap.entrySet()) {
            if (((Future<?>) entry.getValue().get("future")).isDone()) {
                ProcessWorkerMapInstance.INSTANCE.workerMap.remove(entry.getKey());
                ProcessCloserMap.INSTANCE.closureMap.remove(entry.getKey());
            }
        }
        lock.lock();
        try {
            while (!ProcessWorkerFifoInstance.INSTANCE.workerFifo.isEmpty() && ProcessWorkerMapInstance.INSTANCE.workerMap.size() < threadPool) {
                ProcessWorker processWorker = ProcessWorkerFifoInstance.INSTANCE.workerFifo.poll();
                if (processWorker != null) {
                    Future future = executorService.submit(processWorker);
                    Map<String, Object> m = new HashMap<>();
                    m.put("future", future);
                    m.put("runnable", processWorker);
                    ProcessWorkerMapInstance.INSTANCE.workerMap.put(processWorker.getJobId(), m);
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
