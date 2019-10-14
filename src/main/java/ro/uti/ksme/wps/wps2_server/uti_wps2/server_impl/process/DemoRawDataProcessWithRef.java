package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessImplementation;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 10/11/2019
 * Time: 4:09 PM
 */
@Process(processAttr = @ProcessAttr(
        version = "0.0.0_a",
        dataTransmissionType = DataTransmissionModeType.REFERENCE
),
        descriptionType = @DescriptionTypeAttr(
                title = "This process executes and returns a reference to a location",
                identifier = "processExecutionRef"
        ))
public class DemoRawDataProcessWithRef implements ProcessImplementation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoRawDataProcessWithRef.class);

    @InputAnnotation
    @LiteralDataInput(inputAttr = @InputAttr,
            descriptionType = @DescriptionTypeAttr(
                    title = "The literal input attr... will try to check the string (path) provided and return a ref if the file is present",
                    identifier = "locationInput"
            ),
            literalAttr = @LiteralDataAttr)
    private String pathProvided;


    @OutputAnnotation
    @LiteralDataOutput(outputAttr = @OutputAttr,
            descriptionType = @DescriptionTypeAttr(
                    title = "The response that represents a reference to a processable result location",
                    identifier = "result"
            ),
            literalAttr = @LiteralDataAttr)
    @Override
    public String execute() {
        try {
            LOGGER.info("You provided the path " + pathProvided);
            Thread.sleep(200);
            LOGGER.info("Checking if dummy file is present and creating \"reference\" (getting path) to it...");
            Thread.sleep(200);
            URL url = this.getClass().getClassLoader().getResource("dummy_result.tiff");
            if (url != null) {
                String location = url.toString().substring(0, url.toString().lastIndexOf("/") + 1);

                LOGGER.info("Processing stuff...");
                Thread.sleep(600);

                LOGGER.info("Processing complete the requested resource is at location: " + location + pathProvided);
                return url.toString();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public String getPathProvided() {
        return pathProvided;
    }

    public void setPathProvided(String pathProvided) {
        this.pathProvided = pathProvided;
    }
}
