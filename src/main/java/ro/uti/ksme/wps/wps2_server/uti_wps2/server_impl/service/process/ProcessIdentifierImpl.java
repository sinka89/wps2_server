package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessOffering;

import java.util.Map;

public class ProcessIdentifierImpl implements ProcessIdentifier {
    private ProcessOffering processOffering;
    private Map<String, Object> properties;

    public ProcessIdentifierImpl(ProcessOffering processOffering) {
        this.processOffering = processOffering;
    }

    @Override
    public ProcessDescriptionType getProcessDescriptionType() {
        return processOffering.getProcess();
    }

    @Override
    public ProcessOffering getProcessOffering() {
        return processOffering;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
