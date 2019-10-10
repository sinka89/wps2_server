package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;

import javax.ejb.*;
import javax.inject.Inject;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ProcessorServiceImpl implements ProcessorService {

    private final ExecutorService executorService;
    private final int threadPool;
    private boolean processRunning = false;
    private ProcessManager processManager;

    public ProcessorServiceImpl() {
        threadPool = Wps2ServerProps.getWpsProcessesExecutorThreadPool();
        executorService = Executors.newFixedThreadPool(threadPool);
//        new CleanUpService();
    }

    @Inject
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Override
    public void cancelProgress(UUID jobId) {
        ProcessWorkerMapInstance.INSTANCE.workerMap.get(jobId).cancel(true);
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
            ProcessWorkerMapInstance.INSTANCE.workerMap.put(worker.getJobId(), future);
            return future;
        }
        return null;
    }


    @Override
    @Lock(LockType.WRITE)
    public void onProcessWFinish() {
        for (Map.Entry<UUID, Future> entry : ProcessWorkerMapInstance.INSTANCE.workerMap.entrySet()) {
            if (entry.getValue().isDone()) {
                ProcessWorkerMapInstance.INSTANCE.workerMap.remove(entry.getKey());
            }
        }
        synchronized (ProcessorServiceImpl.class) {
            while (!ProcessWorkerFifoInstance.INSTANCE.workerFifo.isEmpty() && ProcessWorkerMapInstance.INSTANCE.workerMap.size() < threadPool) {
                ProcessWorker processWorker = ProcessWorkerFifoInstance.INSTANCE.workerFifo.poll();
                if (processWorker != null) {
                    Future future = executorService.submit(processWorker);
                    ProcessWorkerMapInstance.INSTANCE.workerMap.put(processWorker.getJobId(), future);
                }
            }
        }
    }
}
