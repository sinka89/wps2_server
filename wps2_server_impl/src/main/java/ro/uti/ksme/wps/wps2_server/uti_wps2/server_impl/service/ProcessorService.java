package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessIdentifier;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessJob;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

public interface ProcessorService {

    void cancelProgress(UUID jobId);

    Future executeNewProcessWorker(ProcessJob job, ProcessIdentifier processIdentifier, Map<URI, Object> dataMap);

    void onProcessWFinish();
}
