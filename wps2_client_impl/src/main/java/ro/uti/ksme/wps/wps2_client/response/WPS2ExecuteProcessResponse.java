package ro.uti.ksme.wps.wps2_client.response;

import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Result;
import ro.uti.ksme.wps.wps2.pojo.wps._2.StatusInfo;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_client.connection_util.HttpClientResponse;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 1:33 PM
 */
public class WPS2ExecuteProcessResponse extends GenericResponse implements PropertyChangeListener {
    private ExceptionReport exceptionReport;
    private ProgressInputStream rawResponseStream;
    private String rawContentType;
    private Result executionResult;
    private StatusInfo executionStatusAsync;

    public WPS2ExecuteProcessResponse(HttpClientResponse response) throws IOException, JAXBException {
        super(response);

        ProgressInputStream inputStream = null;

        rawContentType = response.getContentType();
        try {
            if (rawContentType.matches(".*/xml.*")) {
                Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                inputStream = new ProgressInputStream(new BufferedInputStream(httpClientResponse.getResponseInputStream()), httpClientResponse.getResponseInputStream().available());
                inputStream.addPropertyChangeListener(this);
                Object obj = unmarshaller.unmarshal(inputStream);
                if (obj instanceof Result) {
                    executionResult = (Result) obj;
                } else if (obj instanceof StatusInfo) {
                    executionStatusAsync = (StatusInfo) obj;
                } else if (obj instanceof ExceptionReport) {
                    exceptionReport = (ExceptionReport) obj;
                }
            } else {
                inputStream = new ProgressInputStream(httpClientResponse.getResponseInputStream(), httpClientResponse.getResponseInputStream().available());
                inputStream.addPropertyChangeListener(this);
            }
            rawResponseStream = inputStream;
            inputStream = null;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public ExceptionReport getExceptionReport() {
        return exceptionReport;
    }

    public InputStream getRawResponseStream() {
        return rawResponseStream;
    }

    public String getRawContentType() {
        return rawContentType;
    }

    public Result getExecutionResult() {
        return executionResult;
    }

    public StatusInfo getExecutionStatusAsync() {
        return executionStatusAsync;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProgressInputStream.PROP_ALL_BYTES_READ)) {
            super.dismiss();
        }
    }
}
