package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.WpsServer;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.operations.Wps2Operations;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ErrorService;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessManager;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.WpsProcessReflectionUtil;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Bogdan-Adrian Sincu
 * Main Wps2 server class -> handles marshalling and unmarshalling requests from Handler
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class Wps2ServerImpl implements WpsServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ServerImpl.class);

    private ErrorService errorService;
    private Wps2Operations wps2Operations;
    private ProcessManager processManager;

    @Inject
    public void setErrorService(ErrorService errorService) {
        this.errorService = errorService;
    }

    @Inject
    public void setWps2Operations(Wps2Operations wps2Operations) {
        this.wps2Operations = wps2Operations;
    }

    @Inject
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Override
    @Lock(LockType.WRITE)
    public Object callOperation(InputStream xml) {
        Object result = null;
        ObjectFactory factoryWps2 = new ObjectFactory();
        Object objReceived = null;
        try {
            Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
            objReceived = unmarshaller.unmarshal(xml);
            if (objReceived instanceof JAXBElement) {
                objReceived = ((JAXBElement) objReceived).getValue();
            }
            if (objReceived instanceof GetCapabilitiesType) {
                Object resp = wps2Operations.getCapabilities((GetCapabilitiesType) objReceived);
                if (resp instanceof WPSCapabilitiesType) {
                    result = factoryWps2.createCapabilities((WPSCapabilitiesType) resp);
                } else {
                    result = resp;
                }
            } else if (objReceived instanceof DescribeProcess) {
                result = wps2Operations.describeProcess((DescribeProcess) objReceived);
            } else if (objReceived instanceof ExecuteRequestType) {
                result = wps2Operations.execute((ExecuteRequestType) objReceived);
            } else if (objReceived instanceof GetStatus) {
                result = wps2Operations.getStatus((GetStatus) objReceived);
            } else if (objReceived instanceof Dismiss) {
                result = wps2Operations.dismiss((Dismiss) objReceived);
            } else if (objReceived instanceof GetResult) {
                result = wps2Operations.getResult((GetResult) objReceived);
            }
        } catch (JAXBException e) {
            LOGGER.error("Unable to parse incoming xml. \nCause: " + e.getMessage(), e);
            StringWriter stringWriter = new StringWriter();
            Optional<String> s = errorService.generateErrorMsgXml(e.toString());
            if (s.isPresent()) {
                stringWriter.write(s.get());
                return stringWriter;
            }
        }
        if (result instanceof byte[]) {
            return result;
        } else {
            StringWriter out = new StringWriter();
            if (result != null) {
                try {
                    Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.marshal(result, out);
                    out.close();
                } catch (JAXBException e) {
                    LOGGER.error("Unable to parse outgoing xml.\nCause: " + e.getMessage(), e);
                } catch (IOException e) {
                    LOGGER.error("Unable to close StringWriter... \nCause: " + e.getMessage(), e);
                }
            }
            return out;
        }

    }

    @PostConstruct
    private void loadAllProcessesByClass() {
        Collection<Class<?>> listOfProcessesClass = WpsProcessReflectionUtil.getListOfProcessesClass();
        for (Class<?> cls : listOfProcessesClass) {
            processManager.createAndAddProcess(cls);
        }
    }
}
