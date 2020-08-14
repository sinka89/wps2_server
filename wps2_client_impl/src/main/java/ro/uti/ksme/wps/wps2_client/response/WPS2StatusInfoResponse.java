package ro.uti.ksme.wps.wps2_client.response;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.wps._2.StatusInfo;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 1:10 PM
 */
public class WPS2StatusInfoResponse extends GenericResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(WPS2StatusInfoResponse.class);
    private StatusInfo statusInfo;

    public WPS2StatusInfoResponse(HttpClientResponse response) throws IOException, JAXBException {
        super(response);
        Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
        Object obj;
        if (LOGGER.isDebugEnabled()) {
            byte[] bytes = IOUtils.toByteArray(httpClientResponse.getResponseInputStream());
            LOG_DATA.log(bytes);
            obj = unmarshaller.unmarshal(new ByteArrayInputStream(bytes));
        } else {
            obj = unmarshaller.unmarshal(new BufferedInputStream(httpClientResponse.getResponseInputStream()));
        }
        if (obj instanceof StatusInfo) {
            this.statusInfo = (StatusInfo) obj;
        } else if (obj instanceof ExceptionReport) {
            this.exceptionReport = (ExceptionReport) obj;
        }
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }
}
