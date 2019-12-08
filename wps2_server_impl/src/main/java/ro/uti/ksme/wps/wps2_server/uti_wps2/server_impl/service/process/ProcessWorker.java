package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.ProcessState;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ProcessorService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class ProcessWorker implements CancellableRunnable, PropertyChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessWorker.class);

    private final ProcessJob job;
    private final ProcessIdentifier processIdentifier;
    private final ProcessManager processManager;
    private final Map<URI, Object> dataMap;

    private final ProgressMonitor progressMonitor;
    private final ProcessorService processorService;

    public ProcessWorker(ProcessJob job, ProcessIdentifier processIdentifier, ProcessManager processManager, Map<URI, Object> dataMap, ProcessorService processorService) {
        this.job = job;
        this.processIdentifier = processIdentifier;
        this.processManager = processManager;
        this.dataMap = dataMap;
        this.processorService = processorService;
        progressMonitor = new ProgressMonitor(job.getProcess().getTitle().get(0).getValue());
        progressMonitor.addPropertyChangeListener(ProgressMonitor.PROP_PROGRESS, this.job);
        progressMonitor.addPropertyChangeListener(ProgressMonitor.PROP_CANCEL, this);
    }

    @Override
    public void run() {
        String title = job.getProcess().getTitle().get(0).getValue();
        progressMonitor.setTaskName(title + " : Preprocessing");
        job.setProcessState(ProcessState.RUNNING);
        ProcessDescriptionType process = processIdentifier.getProcessDescriptionType();
        try {

            job.appendLog(ProcessExecutionListener.LogType.INFO, "Starting process...");

            job.appendLog(ProcessExecutionListener.LogType.INFO, "Pre-Processing...");

            job.appendLog(ProcessExecutionListener.LogType.INFO, "Executing process...");

            progressMonitor.setTaskName(title + " : Execution");
            processManager.executeProcess(job.getId(), processIdentifier, dataMap, processIdentifier.getProperties(), progressMonitor);
            if (progressMonitor.isCanceled()) {
                return;
            }

            progressMonitor.setTaskName(title + " : PostProcessing");
            job.appendLog(ProcessExecutionListener.LogType.INFO, "Post-Processing.");

            if (progressMonitor.isCanceled()) {
                return;
            }


            job.appendLog(ProcessExecutionListener.LogType.INFO, "End of process.");
            job.setProcessState(ProcessState.FINISHED);

            progressMonitor.endOfProgress();
            processorService.onProcessWFinish();
        } catch (Exception e) {
            job.setProcessState(ProcessState.FAILED);
            LOGGER.error(e.getMessage(), e);
            job.appendLog(ProcessExecutionListener.LogType.ERROR, e.getMessage());
            processorService.onProcessWFinish();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals(ProgressMonitor.PROP_CANCEL)) {
            job.setProcessState(ProcessState.CANCELED);
            processManager.cancelProcess(job.getId());
        }
    }

    @Override
    public void cancel() {
        progressMonitor.cancel();
    }

    public UUID getJobId() {
        return job.getId();
    }
}
