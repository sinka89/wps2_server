package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessOffering;

import java.util.Map;

public interface ProcessIdentifier {

    ProcessDescriptionType getProcessDescriptionType();

    ProcessOffering getProcessOffering();

    Map<String, Object> getProperties();

    void setProperties(Map<String, Object> properties);
}
