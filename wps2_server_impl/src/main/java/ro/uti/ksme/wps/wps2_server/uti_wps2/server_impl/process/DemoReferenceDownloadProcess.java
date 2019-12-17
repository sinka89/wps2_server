package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.wps2.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.BoundingBoxData;
import ro.uti.ksme.wps.wps2.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.ProcessingException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.AbstractProcessImplementation;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.FormatFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessResultWrapper;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/17/2019
 * Time: 2:57 PM
 */
@Process(processAttr = @ProcessAttr(
        version = "0.0.0_a",
        dataTransmissionType = DataTransmissionModeType.REFERENCE,
        jobControl = {JobControlOps.ASYNC, JobControlOps.SYNC}
),
        descriptionType = @DescriptionTypeAttr(
                title = "This process simulates a processing that returns a reference to a resource that can be downloaded",
                identifier = "processExecuteDownloadRef"
        ))
public class DemoReferenceDownloadProcess extends AbstractProcessImplementation {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoReferenceDownloadProcess.class);

    @InputAnnotation
    @BoundingBoxInput(
            inputAttr = @InputAttr(minOccurs = 1, maxOccurs = 1),
            descriptionType = @DescriptionTypeAttr(
                    title = "This input accepts wps:BoundingBoxData chained with ows:BoundingBoxType, it will be parsed and after the logger will print the values",
                    identifier = "bboxInputNonOptional"
            ),
            boundingBoxAttr = @BoundingBoxAttr
    )
    private Object boundingBoxInput;

    @OutputAnnotation
    @LiteralDataOutput(outputAttr = @OutputAttr,
            descriptionType = @DescriptionTypeAttr(
                    title = "The response represents a reference to a downloadable resource.",
                    identifier = "result"
            ),
            literalAttr = @LiteralDataAttr(
                    formats = {FormatFactory.PNG_EXTENSION},
                    defaultFormat = FormatFactory.PNG_EXTENSION
            ))
    @Override
    public ProcessResultWrapper<String> execute() {
        LOGGER.info("The process was identified and called via reflection...");
        if (this.boundingBoxInput != null) {
            LOGGER.info("Received the following Input of type BoundingBoxData from the request: \n" + this.boundingBoxInput.toString());
        }
        try {
            if (this.boundingBoxInput != null) {
                LOGGER.info("Unmarshalling the data based on the xml received...");
                Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(this.boundingBoxInput.toString());
                Object unMarshaledObj = unmarshaller.unmarshal(reader);
                if (unMarshaledObj instanceof JAXBElement && ((JAXBElement) unMarshaledObj).getValue() instanceof BoundingBoxData) {
                    BoundingBoxData receivedBbox = (BoundingBoxData) ((JAXBElement) unMarshaledObj).getValue();
                    LOGGER.info("Unmarshall of xml received was successful will do some processing...");
                    BoundingBoxType bboxType = receivedBbox.getBoundingBox().getValue();
                    StringBuilder logMsg = new StringBuilder("Received the following BBOX_DATA:\n");
                    logMsg.append("dimensions = ").append(bboxType.getDimensions()).append("\n");
                    logMsg.append("crs = ").append(bboxType.getCrs()).append("\n");
                    logMsg.append("UpperCorner = ");
                    bboxType.getUpperCorner().forEach(d -> logMsg.append(d).append(" "));
                    logMsg.append("\nLowerCorner = ");
                    bboxType.getLowerCorner().forEach(d -> logMsg.append(d).append(" "));
                    LOGGER.info(logMsg.toString());
                    LOGGER.info("Dummy Processing completed will return a downloadable content reference...");
                    ProcessResultWrapper<String> resultWrapper = new ProcessResultWrapper<>();
                    resultWrapper.setData("https://upload.wikimedia.org/wikipedia/en/b/b7/Norwegian_Forest_Cat_01.png");
                    resultWrapper.setMimeType("image/png");
                    return resultWrapper;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ProcessingException(e.initCause(e));
        }
        return null;
    }

    public Object getBoundingBoxInput() {
        return boundingBoxInput;
    }

    public void setBoundingBoxInput(Object boundingBoxInput) {
        this.boundingBoxInput = boundingBoxInput;
    }
}
