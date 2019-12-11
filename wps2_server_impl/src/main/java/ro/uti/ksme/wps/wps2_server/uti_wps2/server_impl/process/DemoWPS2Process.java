package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.wps2.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.BoundingBoxData;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.ProcessingException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.AbstractProcessImplementation;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessResultWrapper;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * @author Bogdan-Adrian Sincu created on 10/8/2019
 * Example of building a process using WPS2 server annotation parser
 * It supports multiple inputs and only one output atm
 */
@Process(processAttr = @ProcessAttr(
        version = "0.0.0_a",
        jobControl = {JobControlOps.ASYNC, JobControlOps.SYNC}
),
        descriptionType = @DescriptionTypeAttr(
                keywords = {"ProcessKeyWord"},
                title = "This is a demo process for demonstrating WPS2 server execution that returns a file in this case a .tiff",
                identifier = "demoProcessDownloadTiff"
        ))
public class DemoWPS2Process extends AbstractProcessImplementation {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoWPS2Process.class);

    @InputAnnotation
    @BoundingBoxInput(
            inputAttr = @InputAttr(minOccurs = 1, maxOccurs = 1),
            descriptionType = @DescriptionTypeAttr(
                    title = "This Input accepts wps:BoundingBoxData chained with ows:BoundingBoxType, it will be parsed and after the logger will print the values",
                    identifier = "bboxInput"
            ),
            boundingBoxAttr = @BoundingBoxAttr)
    private Object boundingBoxInput;

    @InputAnnotation
    @LiteralDataInput(inputAttr = @InputAttr(minOccurs = 0, maxOccurs = 1),
            descriptionType = @DescriptionTypeAttr(
                    title = "This is an optional Input of primitive type",
                    identifier = "optionalInput"
            ),
            literalAttr = @LiteralDataAttr)
    private String testInput;

    @OutputAnnotation
    @RawDataOutput(
            outputAttr = @OutputAttr,
            descriptionType = @DescriptionTypeAttr(
                    title = "The result that the execution is offering",
                    identifier = "rawDataExecuteResponse"
            ),
            rawDataAttr = @RawDataAttr(
                    isDirectory = false,
                    isFile = true,
                    fileTypes = {".tiff"}
            )
    )
    @Override
    public ProcessResultWrapper<byte[]> execute() throws ProcessingException {
        LOGGER.info("The process was identified and called via reflection...");
        if (this.boundingBoxInput != null) {
            LOGGER.info("Received the following Input of type BoundingBoxData from the request:\n" + this.boundingBoxInput.toString());
        }
        try {
//            Thread.sleep(1000);
            if (this.boundingBoxInput != null) {
                LOGGER.info("Unmarshalling the data based on the xml received...");
                Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(this.boundingBoxInput.toString());
                Object unMarshaledObj = unmarshaller.unmarshal(reader);
                if (unMarshaledObj instanceof JAXBElement && ((JAXBElement) unMarshaledObj).getValue() instanceof BoundingBoxData) {
                    BoundingBoxData bboxData = (BoundingBoxData) ((JAXBElement) unMarshaledObj).getValue();
                    LOGGER.info("Unmarshal of xml received was successful will process...");
//                    Thread.sleep(500);

                    BoundingBoxType bboxType = bboxData.getBoundingBox().getValue();
                    StringBuilder logMsg = new StringBuilder("Received the following BBOX_DATA:\n");
                    logMsg.append("dimensions = ").append(bboxType.getDimensions()).append("\n");
                    logMsg.append("crs = ").append(bboxType.getCrs()).append("\n");
                    logMsg.append("UpperCorner = ");
                    bboxType.getUpperCorner().forEach(d -> logMsg.append(d).append(" "));
                    logMsg.append("\nLowerCorner = ");
                    bboxType.getLowerCorner().forEach(d -> logMsg.append(d).append(" "));
                    if (testInput != null) {
                        logMsg.append("\n").append("The optional Input was provided and has value = ").append(testInput);
                    }
                    LOGGER.info(logMsg.toString());
//                    Thread.sleep(500);
                    LOGGER.info("Continuing processing to return dummy .tiff...");
//                    while (true) {
//                        LOGGER.error("Infinite Loop");
//                        Thread.sleep(0, 1);
//                    }
                    byte[] bytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/dummy_result.tiff"));
                    ProcessResultWrapper<byte[]> result = new ProcessResultWrapper<>();
                    result.setData(bytes);
                    result.setMimeType("image/tiff");
                    return result;
                }
            }
            LOGGER.info("Returning processing result...");
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
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
