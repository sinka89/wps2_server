package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.ProcessingException;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessImplementation;

import java.util.Random;

/**
 * @author Bogdan-Adrian Sincu
 * Example of process that accepts a literalData input and returns literlData
 */
@Process(descriptionType = @DescriptionTypeAttr(
        title = "Process to demonstrate Literal data passing and processing",
        identifier = "literalDataProcess",
        keywords = {"LiteralData"}
), processAttr = @ProcessAttr(
        version = "0.0.1",
        jobControl = {JobControlOps.ASYNC, JobControlOps.SYNC}
))
public class DemoLiteralDataProcess implements ProcessImplementation {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoLiteralDataProcess.class);

    @InputAnnotation
    @LiteralDataInput(inputAttr = @InputAttr, literalAttr = @LiteralDataAttr, descriptionType = @DescriptionTypeAttr(
            title = "Literal data input that can be of any primitive type",
            identifier = "inputField",
            description = "Will accept also the Object class wrappers of the primitive types"
    ))
    private String inputField;

    @OutputAnnotation
    @LiteralDataOutput(outputAttr = @OutputAttr, literalAttr = @LiteralDataAttr, descriptionType = @DescriptionTypeAttr(
            title = "The result of literal data type",
            identifier = "result"
    ))
    @Override
    public String execute() throws ProcessingException {

        if (inputField != null) {
            LOGGER.info("Received Data: " + inputField);
            try {
                Random r = new Random();
                int sleepTime = r.nextInt(4000 + 1);
                LOGGER.info("Sleeping for " + sleepTime);
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                LOGGER.error("ERROR sleeping....");
                throw new ProcessingException(e.initCause(e));
            }
            LOGGER.info("Will Return the same data...");
            return inputField;
        } else {
            LOGGER.info("NO Data received...");
        }

        return null;
    }
}