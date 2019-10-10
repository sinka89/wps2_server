package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.ows._2.ExceptionType;
import ro.uti.ksme.wps.wps2_server.pojo.ows._2.ObjectFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.dto.ExceptionDTO;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.JaxbContainer;

import javax.ejb.*;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Optional;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ErrorServiceImpl implements ErrorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorServiceImpl.class);

    @Override
    @Lock(LockType.READ)
    public Optional<String> generateErrorMsgXml(ExceptionDTO exDto) {
        ObjectFactory owsObjFac = new ObjectFactory();
        ExceptionType exceptionType = owsObjFac.createExceptionType();
        exceptionType.setExceptionCode(String.valueOf(exDto.getStatus()));
        exceptionType.getExceptionText().add(exDto.getExMsg());
        JAXBElement<ExceptionType> exception = owsObjFac.createException(exceptionType);
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = JaxbContainer.INSTANCE.jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(exception, sw);
            return Optional.of(sw.toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return generateErrorMsgXml(e.toString());
        }
    }

    @Override
    @Lock(LockType.READ)
    public Optional<String> generateErrorMsgXml(String err) {
        return generateErrorMsgXml(new ExceptionDTO(500, err));
    }
}
