package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessIdentifier;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessJobSync;

import java.net.URI;
import java.util.Map;

public interface ProcessorServiceSync {
    ProcessJobSync executeProcessSync(ProcessJobSync job, ProcessIdentifier processIdentifier, Map<URI, Object> dataMap);
}
