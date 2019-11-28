package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.wps2.custom_pojo_types.RawData;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.RawDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.FormatFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessImplementation;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 * RawData extension example... receives something -> process -> returns a RawData Object with some byte data inside
 */
@SuppressWarnings("ALL")
@Process(processAttr = @ProcessAttr(
        jobControl = JobControlOps.SYNC
),
        descriptionType = @DescriptionTypeAttr(
                title = "Raw Data Process Ex",
                identifier = "rawDataEx"
        ))
public class DemoRawDataProcess implements ProcessImplementation {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoRawDataProcess.class);

    @InputAnnotation
    @RawDataInput(inputAttr = @InputAttr(minOccurs = 0),
            descriptionType = @DescriptionTypeAttr(
                    title = "Raw data input field",
                    identifier = "rawDataInput"
            ),
            rawDataAttr = @RawDataAttr(
                    fileTypes = {".txt"}
            ))
    private Object rawDataField;


    @OutputAnnotation
    @RawDataOutput(outputAttr = @OutputAttr,
            descriptionType = @DescriptionTypeAttr(
                    title = "Raw Data Output Method",
                    identifier = "rawDataOutput"
            ),
            rawDataAttr = @RawDataAttr(
                    fileTypes = {".xml"}
            ))
    @Override
    public RawData execute() {
        try {
            Object toReturn = null;
            byte[] bytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/dummy_result.tiff"));
            RawData rawData = new RawData(FormatFactory.getFormatFromExtensions(FormatFactory.TIFF_EXTENSION));
            rawData.setFile(true);
            rawData.setDirectory(false);
            rawData.setByteData(bytes);
            return rawData;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public Object getRawDataField() {
        return rawDataField;
    }

    public void setRawDataField(Object rawDataField) {
        this.rawDataField = rawDataField;
    }
}