package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.uti.ksme.wps.wps2.pojo.ows._2.CodeType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.InputDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessDescriptionType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessOffering;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.parser.ParseController;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessExecutionHelper;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessImplementation;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.WpsProcessReflectionUtil;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

//@Startup
//@Singleton
//@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Component
public class ProcessManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessManager.class);

    private ParseController parseController;

    @Autowired
    public void setParseController(ParseController parseController) {
        this.parseController = parseController;
    }

    //    @Lock(LockType.WRITE)
    public void createAndAddProcess(Class<?> cls) {
        Optional<ProcessOffering> clsProcess = parseController.parseProcess(cls);
        if (clsProcess.isPresent()) {
            ProcessOffering processOffering = clsProcess.get();
            if (getProcess(processOffering.getProcess().getIdentifier()) != null) {
                LOGGER.error("A process with the identifier " + processOffering.getProcess().getIdentifier().getValue() + " already exists.");
                System.exit(-1);
            }
            //treat metadata && db
            ProcessIdentifier pi = new ProcessIdentifierImpl(processOffering);
            ProcessorListInstance.INSTANCE.processes.add(pi);

        } else {
            LOGGER.error("Unable to parse the process for init for class: " + cls.getSimpleName());
        }
    }

    private ProcessDescriptionType getProcess(CodeType identifier) {
        for (ProcessIdentifier pi : ProcessorListInstance.INSTANCE.processes) {
            if (pi.getProcessDescriptionType().getIdentifier().getValue().equals(identifier.getValue())) {
                return pi.getProcessDescriptionType();
            }
        }
        for (ProcessIdentifier pi : ProcessorListInstance.INSTANCE.processes) {
            for (InputDescriptionType input : pi.getProcessDescriptionType().getInput()) {
                if (input.getIdentifier().getValue().equals(identifier.getValue())) {
                    return pi.getProcessDescriptionType();
                }
            }
            for (OutputDescriptionType output : pi.getProcessDescriptionType().getOutput()) {
                if (output.getIdentifier().getValue().equals(identifier.getValue())) {
                    return pi.getProcessDescriptionType();
                }
            }
        }
        return null;
    }

    public List<ProcessIdentifier> getAllProcessIdentifier() {
        return ProcessorListInstance.INSTANCE.processes;
    }

    public ProcessIdentifier getProcessIdentifier(CodeType identifier) {
        for (ProcessIdentifier pi : ProcessorListInstance.INSTANCE.processes) {
            if (pi.getProcessDescriptionType().getIdentifier().getValue().equals(identifier.getValue())) {
                return pi;
            }
        }
        return null;
    }

    //    @Lock(LockType.WRITE)
    void executeProcess(UUID id, ProcessIdentifier processIdentifier, Map<URI, Object> dataMap, Map<String, Object> properties, ProgressMonitor progressMonitor) {
        //set the number of steps and then increment after each process in case it's necessary else don't set sub-process and use endOfProgress()
        ProgressVisitor progressVisitor = progressMonitor.subProcess(3);
        progressVisitor.endStep();
        ProcessDescriptionType process = processIdentifier.getProcessDescriptionType();
        Class<?> clazz = WpsProcessReflectionUtil.getProcessClassBasedOnIdentifier(process.getIdentifier().getValue());
        progressVisitor.endStep();
        Object processObj = ProcessExecutionHelper.createProcess(process, clazz, dataMap);
        ProcessCloserMap.INSTANCE.closureMap.put(id, processObj);
        ProcessExecutionHelper.executeAndGetResult(process, processObj, dataMap);

        progressVisitor.endStep();
    }

    public void cancelProcess(UUID id) {
        if (ProcessCloserMap.INSTANCE.closureMap.containsKey(id)) {
            ProcessImplementation o = (ProcessImplementation) ProcessCloserMap.INSTANCE.closureMap.get(id);
            try {
                Class<?>[] interfaces = o.getClass().getInterfaces();
                for (Class<?> i : interfaces) {
                    if (i.isAssignableFrom(ProcessImplementation.class)) {
                        i.getMethod("closeAdditionalResources").invoke(o);
                    }
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                ProcessCloserMap.INSTANCE.closureMap.remove(id);
            }

        }
    }
}
