package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessIdentifier;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessJobSync;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessExecutionHelper;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.WpsProcessReflectionUtil;

import javax.ejb.*;
import java.net.URI;
import java.util.Map;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ProcessorServiceSyncImpl implements ProcessorServiceSync {

    @Override
    @Lock(LockType.WRITE)
    public ProcessJobSync executeProcessSync(ProcessJobSync job, ProcessIdentifier processIdentifier, Map<URI, Object> dataMap) {
        ProcessDescriptionType process = processIdentifier.getProcessDescriptionType();
        Class<?> clazz = WpsProcessReflectionUtil.getProcessClassBasedOnIdentifier(process.getIdentifier().getValue());
        ProcessExecutionHelper.createProcessAndExecute(process, clazz, dataMap);
        return job;
    }
}
