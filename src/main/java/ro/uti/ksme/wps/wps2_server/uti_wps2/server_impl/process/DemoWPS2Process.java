package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.BoundingBoxData;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.JobControlOps;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author Bogdan-Adrian Sincu created on 10/8/2019
 * Example of building a process using WPS2 server annotation parser
 * It supports multiple inputs and only one output atm
 */
@Process(processAttr = @ProcessAttr(
        version = "0.0.0_a",
        jobControl = {JobControlOps.ASYNC, JobControlOps.SYNC},
        dataTransmissionType = {DataTransmissionModeType.VALUE, DataTransmissionModeType.REFERENCE}
),
        descriptionType = @DescriptionTypeAttr(
                keywords = {"ProcessKeyWord"},
                title = "This is a demo process for demonstrating WPS2 server execution that returns a static local file .tiff",
                identifier = "demoProcessDownloadTiff"
        ))
public class DemoWPS2Process {
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
    public Object execute() {
        byte[] bytes = null;
        LOGGER.info("The process was identified and called via reflection...");
        if (this.boundingBoxInput != null) {
            LOGGER.info("Received the following Input of type BoundingBoxData from the request:\n" + this.boundingBoxInput.toString());
        }
        try {
            Thread.sleep(1000);
            if (this.boundingBoxInput != null) {
                LOGGER.info("Unmarshalling the data based on the xml received...");
                Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(this.boundingBoxInput.toString());
                Object unMarshaledObj = unmarshaller.unmarshal(reader);
                if (unMarshaledObj instanceof JAXBElement && ((JAXBElement) unMarshaledObj).getValue() instanceof BoundingBoxData) {
                    BoundingBoxData bboxData = (BoundingBoxData) ((JAXBElement) unMarshaledObj).getValue();
                    LOGGER.info("Unmarshal of xml received was successful will process...");
                    Thread.sleep(500);

                    BoundingBoxType bboxType = bboxData.getBoundingBox().getValue();
                    StringBuilder logMsg = new StringBuilder("Received the following BBOX_DATA:\n");
                    logMsg.append("dimensions = ").append(bboxType.getDimensions()).append("\n");
                    logMsg.append("crs = ").append(bboxType.getCrs()).append("\n");
                    logMsg.append("UpperCorner = ");
                    bboxType.getUpperCorner().forEach(d -> logMsg.append(d).append(" "));
                    logMsg.append("\nLowerCorner = ");
                    bboxType.getLowerCorner().forEach(d -> logMsg.append(d).append(" "));
                    Thread.sleep(500);
                    LOGGER.info("Continuing processing to return dummy .tiff...");

                    URL url = this.getClass().getClassLoader().getResource("dummy_result.tiff");
                    if (url != null) {
                        File file = Paths.get(url.toURI()).toFile();
                        if (file.exists()) {
                            bytes = IOUtils.toByteArray(new FileInputStream(file));
                        }
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("ERROR >>>> In processing: " + e.getMessage(), e);
        }

        LOGGER.info("Returning processing result...");
        return bytes;
    }

    public Object getBoundingBoxInput() {
        return boundingBoxInput;
    }

    public void setBoundingBoxInput(Object boundingBoxInput) {
        this.boundingBoxInput = boundingBoxInput;
    }
}
