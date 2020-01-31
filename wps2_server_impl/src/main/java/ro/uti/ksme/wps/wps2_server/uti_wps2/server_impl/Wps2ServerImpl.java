package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.uti.ksme.wps.WpsServer;
import ro.uti.ksme.wps.wps2.pojo.wps._2.*;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.operations.Wps2Operations;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ErrorService;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.process.ProcessManager;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.model.exceptions.MalformedModelException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.*;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Bogdan-Adrian Sincu
 * Main Wps2 server class -> handles marshalling and unmarshalling requests from Handler
 */
@Service
public class Wps2ServerImpl implements WpsServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ServerImpl.class);

    private ErrorService errorService;
    private Wps2Operations wps2Operations;
    private ProcessManager processManager;
    private Wps2ServerProps wps2ServerProps;

    @Autowired
    public void setErrorService(ErrorService errorService) {
        this.errorService = errorService;
    }

    @Autowired
    public void setWps2Operations(Wps2Operations wps2Operations) {
        this.wps2Operations = wps2Operations;
    }

    @Autowired
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }

    @Autowired
    public void setWps2ServerProps(Wps2ServerProps wps2ServerProps) {
        this.wps2ServerProps = wps2ServerProps;
    }

    @Override
    public Object callOperation(InputStream xml, String path, String requestMethod) {
        Object result = null;
        ObjectFactory factoryWps2 = new ObjectFactory();
        Object objReceived;
        try {
            Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
            objReceived = unmarshaller.unmarshal(xml);
            if (objReceived instanceof JAXBElement) {
                objReceived = ((JAXBElement) objReceived).getValue();
            }
            if (objReceived instanceof GetCapabilitiesType) {
                AbstractHttpRequestValidator.validateRequest(path, requestMethod, wps2ServerProps.OPERATIONS_METADATA_PROPS.GET_CAPABILITIES_OPERATION);
                Object resp = wps2Operations.getCapabilities((GetCapabilitiesType) objReceived);
                if (resp instanceof WPSCapabilitiesType) {
                    result = factoryWps2.createCapabilities((WPSCapabilitiesType) resp);
                } else {
                    result = resp;
                }
            } else if (objReceived instanceof DescribeProcess) {
                AbstractHttpRequestValidator.validateRequest(path, requestMethod, wps2ServerProps.OPERATIONS_METADATA_PROPS.DESCRIBE_PROCESS_OPERATION);
                result = wps2Operations.describeProcess((DescribeProcess) objReceived);
            } else if (objReceived instanceof ExecuteRequestType) {
                AbstractHttpRequestValidator.validateRequest(path, requestMethod, wps2ServerProps.OPERATIONS_METADATA_PROPS.EXECUTE_OPERATION);
                result = wps2Operations.execute((ExecuteRequestType) objReceived);
            } else if (objReceived instanceof GetStatus) {
                AbstractHttpRequestValidator.validateRequest(path, requestMethod, wps2ServerProps.OPERATIONS_METADATA_PROPS.GET_STATUS_OPERATION);
                result = wps2Operations.getStatus((GetStatus) objReceived);
            } else if (objReceived instanceof Dismiss) {
                AbstractHttpRequestValidator.validateRequest(path, requestMethod, wps2ServerProps.OPERATIONS_METADATA_PROPS.DISMISS_OPERATION);
                result = wps2Operations.dismiss((Dismiss) objReceived);
            } else if (objReceived instanceof GetResult) {
                AbstractHttpRequestValidator.validateRequest(path, requestMethod, wps2ServerProps.OPERATIONS_METADATA_PROPS.GET_RESULT_OPERATION);
                result = wps2Operations.getResult((GetResult) objReceived);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to parse incoming xml. \nCause: " + e.getMessage(), e);
            StringWriter stringWriter = new StringWriter();
            Optional<String> s = errorService.generateErrorMsgXml(e.toString());
            if (s.isPresent()) {
                stringWriter.write(s.get());
                return stringWriter;
            }
        }
        if (result instanceof ProcessResultWrapper) {
            return result;
        } else {
            StringWriter out = new StringWriter();
            if (result != null) {
                try {
                    Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
//                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
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
            if (!cls.getSuperclass().getTypeName().equalsIgnoreCase(AbstractProcessImplementation.class.getTypeName())) {
                Type[] genericInterfaces = cls.getGenericInterfaces();
                boolean interfaceFound = false;
                for (Type aInterface : genericInterfaces) {
                    if (aInterface.getTypeName().equalsIgnoreCase(ProcessImplementation.class.getTypeName())) {
                        interfaceFound = true;
                        break;
                    }
                }
                if (!interfaceFound) {
                    throw new MalformedModelException(cls, "NoProperInheritanceFound", "Could not load the defined process! A correct process class must be defined by @Process annotation" +
                            "and must extend the AbstractProcessImplementation.class or implement the ProcessImplementation.class");
                }
            } else {
                processManager.createAndAddProcess(cls);
            }
        }
    }
}
