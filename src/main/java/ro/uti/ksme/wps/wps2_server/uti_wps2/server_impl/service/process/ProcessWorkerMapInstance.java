package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public enum ProcessWorkerMapInstance {
    INSTANCE;

    public ConcurrentHashMap<UUID, Map<String, Object>> workerMap;

    ProcessWorkerMapInstance() {
        this.workerMap = new ConcurrentHashMap<>();
    }
}
