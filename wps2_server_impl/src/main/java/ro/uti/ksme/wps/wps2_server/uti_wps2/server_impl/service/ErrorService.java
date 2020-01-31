package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service;

import com.sun.net.httpserver.Authenticator;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionType;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.dto.ExceptionDTO;

import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Optional;

public interface ErrorService {
    Optional<String> generateErrorMsgXml(ExceptionDTO exDto);

    Optional<String> generateErrorMsgXml(String err);

    static String generateErrorMessageXml(Object obj) {
        ObjectFactory owsObjFac = new ObjectFactory();
        ExceptionType exceptionType = owsObjFac.createExceptionType();
        if (obj instanceof Authenticator.Retry) {
            exceptionType.setExceptionCode(String.valueOf(((Authenticator.Retry) obj).getResponseCode()));
            exceptionType.getExceptionText().add("Unauthorized! Please Log In to access the requested resources.");
        } else if (obj instanceof String) {
            exceptionType.setExceptionCode("500");
            exceptionType.getExceptionText().add(obj.toString());
        }
        ExceptionReport exceptionReport = owsObjFac.createExceptionReport();
        exceptionReport.getException().add(exceptionType);
        try {
            StringWriter sw = new StringWriter();
            Marshaller marshaller = JaxbContainer.INSTANCE.getMarshallerWithPrefixMapper();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(exceptionReport, sw);
            return sw.toString();
        } catch (Exception e) {
            return generateErrorMessageXml(e.toString());
        }
    }
}
