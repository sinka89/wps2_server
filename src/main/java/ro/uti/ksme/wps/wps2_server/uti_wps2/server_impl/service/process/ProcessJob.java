package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProcessJob extends AbstractProcessJob implements ProcessExecutionListener, PropertyChangeListener, Serializable {

    private final long MAX_PROCESS_POOLING_DELAY_MILLI;
    private final long BASE_PROCESS_POOLING_DELAY_MILLI;

    private Map<String, ProcessExecutionListener.LogType> mapLogs;

    private long startTime = -1;

    private ProcessState state;

    private long processPoolingDelay;

    private int progress = 0;

    public ProcessJob(ProcessDescriptionType process, UUID id, Map<URI, Object> dataMap, long maxPoolingDelay, long basePoolingDelay) {
        super(process, id, dataMap);
        this.process = process;
        this.id = id;
        mapLogs = new HashMap<>();
        state = ProcessState.ACCEPTED;
        this.dataMap = dataMap;
        MAX_PROCESS_POOLING_DELAY_MILLI = maxPoolingDelay;
        BASE_PROCESS_POOLING_DELAY_MILLI = basePoolingDelay;
        processPoolingDelay = basePoolingDelay;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProgressMonitor.PROP_PROGRESS)) {
            Object value = evt.getNewValue();
            if (value instanceof Double) {
                setProgress(((Double) value).intValue());
            } else {
                setProgress((int) value);
            }
        }
    }

    @Override
    public void appendLog(LogType logType, String message) {
        mapLogs.put(message, logType);
    }

    @Override
    public void setProcessState(ProcessState processState) {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
        if (processState.equals(ProcessState.FINISHED) && timeToDestroy == 0) {
            timeToDestroy = System.currentTimeMillis() + Wps2ServerProps.DESTROY_TIME_MILLI;
        }
        state = processState;
    }

    public ProcessState getState() {
        return state;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getProgress() {
        return progress;
    }

    private void setProgress(int progress) {
        this.progress = progress;
    }

    public long getProcessPoolingTime() {
        long time = processPoolingDelay;
        if (processPoolingDelay < MAX_PROCESS_POOLING_DELAY_MILLI) {
            processPoolingDelay += BASE_PROCESS_POOLING_DELAY_MILLI;
        }
        return time;
    }

}
