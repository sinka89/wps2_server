package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import org.apache.commons.io.IOUtils;
import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.RawDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.RawDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.JobControlOps;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Bogdan-Adrian Sincu created on 10/7/2019
 */
@SuppressWarnings("ALL")
@Process(processAttr = @ProcessAttr(
        jobControl = JobControlOps.SYNC,
        dataTransmissionType = {DataTransmissionModeType.VALUE, DataTransmissionModeType.REFERENCE}
),
        descriptionType = @DescriptionTypeAttr(
                title = "Raw Data Process Ex",
                identifier = "rawDataEx"
        ))
public class RawDataProcessEx {

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
                    fileTypes = {".png"}
            ))
    public Object execute() {
        byte[] bytes = null;
        try {
            //dummy endpoint that returns random at interval from 30000 milli -> 60000 milli a text with the duration to test concurrency
            URL url = new URL("http://localhost:8081/test_random_with_img");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                bytes = IOUtils.toByteArray(conn.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public Object getRawDataField() {
        return rawDataField;
    }

    public void setRawDataField(Object rawDataField) {
        this.rawDataField = rawDataField;
    }
}
