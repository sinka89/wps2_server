package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2_server.pojo.ows._2.ObjectFactory;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.BoundingBoxData;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.OutputDescriptionType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.BoundingBoxInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.BoundingBoxOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.FormatFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.JobControlOps;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bogdan-Adrian Sincu created on 10/4/2019
 */
@Process(descriptionType = @DescriptionTypeAttr(
        title = "Bounding Box Ex",
        identifier = "processBoundingBox",
        keywords = {"boundingBox"},
        description = "Test boundingBox"
), processAttr = @ProcessAttr(
        version = "0.0.1",
        jobControl = {JobControlOps.ASYNC, JobControlOps.SYNC},
        dataTransmissionType = {DataTransmissionModeType.REFERENCE, DataTransmissionModeType.VALUE}
))
public class BoundingBoxProcessEx {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoundingBoxProcessEx.class);

    @InputAnnotation
    @BoundingBoxInput(inputAttr = @InputAttr, descriptionType = @DescriptionTypeAttr(
            title = "testField",
            identifier = "testFieldBBox"
    ), boundingBoxAttr = @BoundingBoxAttr())
    private Object objectTestInputField1;


    @OutputAnnotation
    @BoundingBoxOutput(outputAttr = @OutputAttr, descriptionType = @DescriptionTypeAttr(
            title = "The result of the method",
            identifier = "resultBoundingBox"
    ), boundingBoxAttr = @BoundingBoxAttr)
    public Object execute() {
        StringWriter toReturn = new StringWriter();
        LOGGER.info("Received the following Input from the request: \n" + objectTestInputField1.toString());
        try {
            LOGGER.info("Trying to unmarshall data and process it...");
            Unmarshaller unmarshaller = JaxbContainer.INSTANCE.jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(objectTestInputField1.toString());
            Object object = unmarshaller.unmarshal(reader);
            if (object instanceof JAXBElement && ((JAXBElement) object).getValue() instanceof BoundingBoxData) {
                BoundingBoxData bbox = (BoundingBoxData) ((JAXBElement) object).getValue();
                JAXBElement<? extends BoundingBoxType> boundingBox = bbox.getBoundingBox();
                BoundingBoxType value = boundingBox.getValue();
                List<Double> upperCorner = value.getUpperCorner();
                List<Double> lowerCorner = value.getLowerCorner();
                BigInteger dimensions = value.getDimensions();
                String crs = value.getCrs();
                StringBuilder toPrintUpper = new StringBuilder();
                StringBuilder toPrintLower = new StringBuilder();
                for (int i = 0; i < upperCorner.size(); i++) {
                    toPrintUpper.append(upperCorner.get(i));
                    if (i < upperCorner.size() - 1) {
                        toPrintUpper.append(", ");
                    }
                }
                for (int j = 0; j < lowerCorner.size(); j++) {
                    toPrintLower.append(lowerCorner.get(j));
                    if (j < lowerCorner.size() - 1) {
                        toPrintLower.append(", ");
                    }
                }
                LOGGER.info("Received the following BBOX_Data: \ndimensions = " + dimensions + "\nCrs = " + crs + "\nLowerCorner = " + toPrintLower.toString() + "\nUpperCorner = " + toPrintUpper.toString());

                BoundingBoxData bbox2 = new BoundingBoxData();
                bbox2.getFormat().add(FormatFactory.getFormatFromExtension(FormatFactory.XML_EXTENSION));

                BoundingBoxType bt = new BoundingBoxType();
                bt.setDimensions(BigInteger.valueOf(2));
                bt.setCrs("EPSG:4632");
                bt.getLowerCorner().addAll(Arrays.asList(22.4, 55.2));
                bt.getUpperCorner().addAll(Arrays.asList(55.2, 22.4));

                JAXBElement<BoundingBoxType> boundingBox1 = new ObjectFactory().createBoundingBox(bt);

                bbox2.setBoundingBox(boundingBox1);

                //todo: create wrapper for the generic result so it can be serialized... ?
//                byte[] data = SerializationUtils.serialize(bbox2);

                return bbox2;


            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
