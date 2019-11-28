package ro.uti.ksme.wps.wps2_client.response;

import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.wps._2.WPSCapabilitiesType;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 11:57 AM
 */
public class WPS2GetCapabilitiesResponse extends GenericResponse {
    private WPSCapabilitiesType wpsCapabilitiesTypeResponse;
    private ExceptionReport exceptionResponse;

    public WPS2GetCapabilitiesResponse(HttpClientResponse response) throws IOException, JAXBException {
        super(response);
        Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(new BufferedInputStream(httpClientResponse.getResponseInputStream()));
        if (obj instanceof JAXBElement) {
            JAXBElement jaxbElement = (JAXBElement) obj;
            if (jaxbElement.getValue() != null && jaxbElement.getValue() instanceof WPSCapabilitiesType) {
                wpsCapabilitiesTypeResponse = (WPSCapabilitiesType) jaxbElement.getValue();
            }
        } else {
            if (obj instanceof ExceptionReport) {
                exceptionResponse = (ExceptionReport) obj;
            }
        }
    }

    public WPSCapabilitiesType getWpsCapabilitiesTypeResponse() {
        return wpsCapabilitiesTypeResponse;
    }

    public ExceptionReport getExceptionResponse() {
        return exceptionResponse;
    }
}
