package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionType;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.dto.ExceptionDTO;

import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Optional;

@Service
public class ErrorServiceImpl implements ErrorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorServiceImpl.class);

    @Override
    public Optional<String> generateErrorMsgXml(ExceptionDTO exDto) {
        ObjectFactory owsObjFac = new ObjectFactory();
        ExceptionType exceptionType = owsObjFac.createExceptionType();
        exceptionType.setExceptionCode(String.valueOf(exDto.getStatus()));
        exceptionType.getExceptionText().add(exDto.getExMsg());
        ExceptionReport exceptionReport = owsObjFac.createExceptionReport();
        exceptionReport.getException().add(exceptionType);
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(exceptionReport, sw);
            return Optional.of(sw.toString());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return generateErrorMsgXml(e.toString());
        }
    }

    @Override
    public Optional<String> generateErrorMsgXml(String err) {
        return generateErrorMsgXml(new ExceptionDTO(500, err));
    }
}
