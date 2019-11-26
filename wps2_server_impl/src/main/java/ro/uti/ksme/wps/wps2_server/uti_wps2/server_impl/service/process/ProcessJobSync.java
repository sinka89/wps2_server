package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessDescriptionType;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

public class ProcessJobSync extends AbstractProcessJob {

    public ProcessJobSync(ProcessDescriptionType process, UUID id, Map<URI, Object> dataMap) {
        super(process, id, dataMap);
    }
}
