package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ProcessorService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class ProcessWorker implements CancellableRunnable, PropertyChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessWorker.class);

    private ProcessJob job;
    private ProcessIdentifier processIdentifier;
    private ProcessManager processManager;
    private Map<URI, Object> dataMap;

    private ProgressMonitor progressMonitor;
    private ProcessorService processorService;

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
        if (job != null) {
            job.setProcessState(ProcessExecutionListener.ProcessState.RUNNING);
        }
        ProcessDescriptionType process = processIdentifier.getProcessDescriptionType();
        try {

            if (job != null) {
                job.appendLog(ProcessExecutionListener.LogType.INFO, "Starting process...");
            }

            if (job != null) {
                job.appendLog(ProcessExecutionListener.LogType.INFO, "Pre-Processing...");
            }

            if (job != null) {
                job.appendLog(ProcessExecutionListener.LogType.INFO, "Executing process...");
            }
            progressMonitor.setTaskName(title + " : Execution");
            processManager.executeProcess(job.getId(), processIdentifier, dataMap, processIdentifier.getProperties(), progressMonitor);
            if (progressMonitor.isCanceled()) {
                return;
            }

            progressMonitor.setTaskName(title + " : PostProcessing");
            if (job != null) {
                job.appendLog(ProcessExecutionListener.LogType.INFO, "Post-Processing.");
            }

            if (progressMonitor.isCanceled()) {
                return;
            }


            if (job != null) {
                job.appendLog(ProcessExecutionListener.LogType.INFO, "End of process.");
                job.setProcessState(ProcessExecutionListener.ProcessState.FINISHED);
            }

            progressMonitor.endOfProgress();
            processorService.onProcessWFinish();
        } catch (Exception e) {
            if (job != null) {
                job.setProcessState(ProcessExecutionListener.ProcessState.FAILED);
                LOGGER.error(e.getMessage(), e);
                job.appendLog(ProcessExecutionListener.LogType.ERROR, e.getMessage());
            } else {
                LOGGER.error("Error on executing the wps process: " + process.getTitle() + " with msg:\nCause: " + e.getMessage(), e);
            }
            processorService.onProcessWFinish();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals(ProgressMonitor.PROP_CANCEL)) {
            job.setProcessState(ProcessExecutionListener.ProcessState.CANCELED);
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
