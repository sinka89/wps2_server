package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.wps2.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.ProcessingException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.AbstractProcessImplementation;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.FormatFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessResultWrapper;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/11/2019
 * Time: 4:09 PM
 */
@Process(processAttr = @ProcessAttr(
        version = "0.0.0_a",
        dataTransmissionType = DataTransmissionModeType.REFERENCE,
        jobControl = {JobControlOps.SYNC, JobControlOps.ASYNC}
),
        descriptionType = @DescriptionTypeAttr(
                title = "This process simulates a processing that returns a reference to a geoserver layer. By convention the result reference must be of form geoserver://workspace:layer_name so that the client gui will display appropriately. In this scenario the client implementation will try to find it on the associated geoserver.",
                identifier = "layerReference"
        ))
public class DemoRawDataProcessWithRef extends AbstractProcessImplementation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoRawDataProcessWithRef.class);

    @InputAnnotation
    @LiteralDataInput(inputAttr = @InputAttr,
            descriptionType = @DescriptionTypeAttr(
                    title = "Literal data input. for testing purpose it accepts the real layer name in the associated geoserver (workspace:layer_name). Will return a reference format for the input layer ex: \"neuport:neuport_v_roads\"",
                    identifier = "layerNameInput"
            ),
            literalAttr = @LiteralDataAttr)
    private String layerNameProvided = "neuport:neuport_v_roads";


    @OutputAnnotation
    @LiteralDataOutput(outputAttr = @OutputAttr,
            descriptionType = @DescriptionTypeAttr(
                    title = "The response that represents a reference to a processable result location. In the case of a layer use convention and the mimeType must be a subtype wfs.",
                    identifier = "result"
            ),
            literalAttr = @LiteralDataAttr(
                    formats = {FormatFactory.WFS_EXTENSION},
                    defaultFormat = FormatFactory.WFS_EXTENSION
            ))
    @Override
    public ProcessResultWrapper<String> execute() throws ProcessingException {
        try {
            LOGGER.info("You provided the following layer name " + layerNameProvided);
            Thread.sleep(200);
            LOGGER.info("Intense processing... :)");
            Thread.sleep(800);

            LOGGER.info("More processing...");
            Thread.sleep(300);
            layerNameProvided = "geoserver://" + layerNameProvided;
            LOGGER.info("Processing complete the requested resource is a layer on associated geoserver with the name: " + layerNameProvided);
            ProcessResultWrapper<String> result = new ProcessResultWrapper<>();
            result.setMimeType("text/xml;subtype=wfs-collection/1.0");
            result.setData(layerNameProvided);
            return result;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new ProcessingException(e.initCause(e));
        }
    }

    public String getLayerNameProvided() {
        return layerNameProvided;
    }

    public void setLayerNameProvided(String layerNameProvided) {
        this.layerNameProvided = layerNameProvided;
    }
}
