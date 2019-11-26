package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.util.ArrayList;
import java.util.List;

public enum ProcessorListInstance {
    INSTANCE;

    public List<ProcessIdentifier> processes;

    ProcessorListInstance() {
        this.processes = new ArrayList<>();
    }
}
