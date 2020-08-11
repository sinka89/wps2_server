package ro.uti.ksme.wps.wps2_client.response;

import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ProcessOfferings;
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
 * Time: 12:29 PM
 */
public class WPS2DescribeProcessResponse extends GenericResponse {
    private ProcessOfferings processOfferings;

    public WPS2DescribeProcessResponse(HttpClientResponse response) throws IOException, JAXBException {
        super(response);
        Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(new BufferedInputStream(LOG_STREAM_XML.log(httpClientResponse.getResponseInputStream())));
        if (obj instanceof ProcessOfferings) {
            this.processOfferings = (ProcessOfferings) obj;
        } else if (obj instanceof ExceptionReport) {
            this.exceptionReport = (ExceptionReport) obj;
        }
    }

    public ProcessOfferings getProcessOfferings() {
        return processOfferings;
    }
}
