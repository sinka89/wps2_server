package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessOffering;

import java.util.Map;

/**
 * @author Bogdan-Adrian Sincu
 * Class for wrapping over decladerd processes... it instantiates and creates itself after Container init...
 */
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
