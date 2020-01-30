package ro.uti.ksme.wps.wps2_client.response;

import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.wps._2.StatusInfo;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 1:10 PM
 */
public class WPS2StatusInfoResponse extends GenericResponse {
    private StatusInfo statusInfo;
    private ExceptionReport exceptionReport;

    public WPS2StatusInfoResponse(HttpClientResponse response) throws IOException, JAXBException {
        super(response);
        Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(new BufferedInputStream(LOG_STREAM_XML.log(httpClientResponse.getResponseInputStream())));
        if (obj instanceof StatusInfo) {
            this.statusInfo = (StatusInfo) obj;
        } else if (obj instanceof ExceptionReport) {
            this.exceptionReport = (ExceptionReport) obj;
        }
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public ExceptionReport getExceptionReport() {
        return exceptionReport;
    }
}
