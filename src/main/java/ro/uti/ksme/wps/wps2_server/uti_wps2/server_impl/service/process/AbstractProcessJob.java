package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessDescriptionType;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractProcessJob {

    protected ProcessDescriptionType process;
    protected UUID id;
    protected Map<URI, Object> dataMap;
    protected boolean returnRawData = false;

    AbstractProcessJob(ProcessDescriptionType process, UUID id, Map<URI, Object> dataMap) {
        this.process = process;
        this.id = id;
        this.dataMap = dataMap;
    }

    public Map<URI, Object> getDataMap() {
        return dataMap;
    }

    public ProcessDescriptionType getProcess() {
        return process;
    }

    public UUID getId() {
        return id;
    }

    public boolean isReturnRawData() {
        return returnRawData;
    }

    public void setReturnRawData(boolean returnRawData) {
        this.returnRawData = returnRawData;
    }
}
