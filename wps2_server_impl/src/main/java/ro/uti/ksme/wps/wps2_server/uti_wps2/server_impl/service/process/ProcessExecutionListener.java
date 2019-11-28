package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import ro.uti.ksme.wps.common.utils.enums.ProcessState;

public interface ProcessExecutionListener {

    void appendLog(LogType logType, String message);

    void setProcessState(ProcessState processState);

    enum LogType {INFO, WARN, ERROR}
}
