package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import org.springframework.stereotype.Service;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessIdentifier;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessJobSync;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessExecutionHelper;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessImplementation;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.WpsProcessReflectionUtil;

import java.net.URI;
import java.util.Map;

/**
 * @author Bogdan-Adrian Sincu
 * Class used to process sync execution requests
 */
@Service
public class ProcessorServiceSyncImpl implements ProcessorServiceSync {

    @Override
    public ProcessJobSync executeProcessSync(ProcessJobSync job, ProcessIdentifier processIdentifier, Map<URI, Object> dataMap) {
        ProcessDescriptionType process = processIdentifier.getProcessDescriptionType();
        Class<ProcessImplementation> clazz = WpsProcessReflectionUtil.getProcessClassBasedOnIdentifier(process.getIdentifier().getValue());
        Object processObj = ProcessExecutionHelper.createProcess(process, clazz, dataMap);
        ProcessExecutionHelper.executeAndGetResult(process, processObj, dataMap);
        return job;
    }
}
