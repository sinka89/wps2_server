package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.uti.ksme.wps.wps2_server.custom_pojo_types.RawData;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.RawDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.FormatFactory;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.JobControlOps;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessImplementation;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Paths;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
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
            URL url = this.getClass().getClassLoader().getResource("dummy_result.tiff");
            if (url != null) {
                File file = Paths.get(url.toURI()).toFile();
                if (file.exists()) {
                    byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
                    RawData rawData = new RawData(FormatFactory.getFormatFromExtensions(FormatFactory.TIFF_EXTENSION));
                    rawData.setFile(true);
                    rawData.setDirectory(false);
                    rawData.setBase64ConvertedData(bytes);
                    return rawData;
                }
            }
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
