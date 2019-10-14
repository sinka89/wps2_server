package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;

import javax.ejb.*;
import javax.inject.Inject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Bogdan-Adrian Sincu
 * Class used to process async process execution requests
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ProcessorServiceImpl implements ProcessorService {

    private final ExecutorService executorService;
    private final int threadPool;
    private ProcessManager processManager;

    public ProcessorServiceImpl() {
        threadPool = Wps2ServerProps.getWpsProcessesExecutorThreadPool();
        executorService = Executors.newFixedThreadPool(threadPool);
    }

    @Inject
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Override
    public void cancelProgress(UUID jobId) {
//        ProcessWorkerMapInstance.INSTANCE.workerMap.get(jobId).cancel(true);
        ((CancellableRunnable) ProcessWorkerMapInstance.INSTANCE.workerMap.get(jobId).get("runnable")).cancel();
        ((Future) ProcessWorkerMapInstance.INSTANCE.workerMap.get(jobId).get("future")).cancel(true);
        ProcessWorkerMapInstance.INSTANCE.workerMap.remove(jobId);
        processManager.cancelProcess(jobId);
    }

    @Override
    @Lock(LockType.WRITE)
    public Future executeNewProcessWorker(ProcessJob job, ProcessIdentifier processIdentifier, Map<URI, Object> dataMap) {
        ProcessWorker worker = new ProcessWorker(job, processIdentifier, processManager, dataMap, this);
        if (ProcessWorkerMapInstance.INSTANCE.workerMap.size() >= threadPool) {
            ProcessWorkerFifoInstance.INSTANCE.workerFifo.add(worker);
        } else {
            Future future = executorService.submit(worker);
//            ProcessWorkerMapInstance.INSTANCE.workerMap.put(worker.getJobId(), future);
            synchronized (ProcessorServiceImpl.class) {
                Map<String, Object> m = new HashMap<>();
                m.put("future", future);
                m.put("runnable", worker);
                ProcessWorkerMapInstance.INSTANCE.workerMap.put(worker.getJobId(), m);
            }
            return future;
        }
        return null;
    }


    @Override
    @Lock(LockType.WRITE)
    public void onProcessWFinish() {
        for (Map.Entry<UUID, Map<String, Object>> entry : ProcessWorkerMapInstance.INSTANCE.workerMap.entrySet()) {
            if (((Future) entry.getValue().get("future")).isDone()) {
//        for (Map.Entry<UUID, Future> entry : ProcessWorkerMapInstance.INSTANCE.workerMap.entrySet()) {
//            if (entry.getValue().isDone()) {
                ProcessWorkerMapInstance.INSTANCE.workerMap.remove(entry.getKey());
                ProcessCloserMap.INSTANCE.closureMap.remove(entry.getKey());
            }
        }
        synchronized (ProcessorServiceImpl.class) {
            while (!ProcessWorkerFifoInstance.INSTANCE.workerFifo.isEmpty() && ProcessWorkerMapInstance.INSTANCE.workerMap.size() < threadPool) {
                ProcessWorker processWorker = ProcessWorkerFifoInstance.INSTANCE.workerFifo.poll();
                if (processWorker != null) {
                    Future future = executorService.submit(processWorker);
//                    ProcessWorkerMapInstance.INSTANCE.workerMap.put(processWorker.getJobId(), future);
                    Map<String, Object> m = new HashMap<>();
                    m.put("future", future);
                    m.put("runnable", processWorker);
                    ProcessWorkerMapInstance.INSTANCE.workerMap.put(processWorker.getJobId(), m);
                }
            }
        }
    }
}
