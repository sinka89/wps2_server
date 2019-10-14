package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.BoundingBoxData;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.BoundingBoxOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.JobControlOps;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessImplementation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Bogdan-Adrian Sincu created on 10/4/2019
 */
@Process(descriptionType = @DescriptionTypeAttr(
        title = "This is a demo process for demonstrating WPS2 server execution that accepts a BoundingBoxData and returns one",
        identifier = "processBoundingBox",
        keywords = {"boundingBox"},
        description = "Test boundingBox process example"
), processAttr = @ProcessAttr(
        version = "0.0.1",
        jobControl = {JobControlOps.ASYNC, JobControlOps.SYNC}
))
public class DemoBoundingBoxProcess implements ProcessImplementation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoBoundingBoxProcess.class);

    @InputAnnotation
    @BoundingBoxInput(inputAttr = @InputAttr, descriptionType = @DescriptionTypeAttr(
            title = "Field that represents a wps2 BoundingBoxData",
            identifier = "testFieldBBox",
            description = "The value passed must be a valid BoundingBoxData xml element so it can be unmarshall properly in this example. Pass whatever but parse it accordingly inside implementation method"
    ), boundingBoxAttr = @BoundingBoxAttr())
    private Object objectTestInputField1;


    @OutputAnnotation
    @BoundingBoxOutput(outputAttr = @OutputAttr, descriptionType = @DescriptionTypeAttr(
            title = "The result of type BoundingBoxData",
            identifier = "resultBoundingBox",
            description = "The result must be of form ows2 BoundingBoxType so it will wrap itself inside a BoundingBoxData one layer up."
    ), boundingBoxAttr = @BoundingBoxAttr)
    @Override
    public BoundingBoxType execute() {
        try {
            LOGGER.info("Received the following Input from the request: \n" + objectTestInputField1.toString());
            LOGGER.info("Trying to unmarshall data and process it...");
            Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(objectTestInputField1.toString());
            Object unMarshaledObj = unmarshaller.unmarshal(reader);
            if (unMarshaledObj instanceof JAXBElement && ((JAXBElement) unMarshaledObj).getValue() instanceof BoundingBoxData) {
                BoundingBoxData bboxData = (BoundingBoxData) ((JAXBElement) unMarshaledObj).getValue();
                LOGGER.info("Unmarshal of xml received was successful will process...");

                BoundingBoxType bboxType = bboxData.getBoundingBox().getValue();
                StringBuilder logMsg = new StringBuilder("Received the following BBOX_DATA:\n");
                logMsg.append("dimensions = ").append(bboxType.getDimensions()).append("\n");
                logMsg.append("crs = ").append(bboxType.getCrs()).append("\n");
                logMsg.append("UpperCorner = ");
                bboxType.getUpperCorner().forEach(d -> logMsg.append(d).append(" "));
                logMsg.append("\nLowerCorner = ");
                bboxType.getLowerCorner().forEach(d -> logMsg.append(d).append(" "));
                LOGGER.info(logMsg.toString());

                Thread.sleep(500);

                LOGGER.info("Generating BBox to return as result with some data");
                BoundingBoxType bt = new BoundingBoxType();
                bt.setDimensions(BigInteger.valueOf(2));
                bt.setCrs("EPSG:4632");
                bt.getLowerCorner().addAll(Arrays.asList(22.4, 55.2));
                bt.getUpperCorner().addAll(Arrays.asList(55.2, 22.4));

                return bt;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public Object getObjectTestInputField1() {
        return objectTestInputField1;
    }

    public void setObjectTestInputField1(Object objectTestInputField1) {
        this.objectTestInputField1 = objectTestInputField1;
    }
}
