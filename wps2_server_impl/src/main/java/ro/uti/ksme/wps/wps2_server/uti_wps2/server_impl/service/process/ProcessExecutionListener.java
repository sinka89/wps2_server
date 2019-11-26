package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

public interface ProcessExecutionListener {

    void appendLog(LogType logType, String message);

    void setProcessState(ProcessState processState);

    enum ProcessState {RUNNING, FINISHED, FAILED, ACCEPTED, IDLE, CANCELED}

    enum LogType {INFO, WARN, ERROR}
}
