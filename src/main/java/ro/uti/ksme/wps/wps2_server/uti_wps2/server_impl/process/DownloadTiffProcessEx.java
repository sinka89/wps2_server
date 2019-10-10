package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process;

import ro.uti.ksme.wps.wps2_server.pojo.wps._2.DataTransmissionModeType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.attributes.*;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.input.LiteralDataInput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.output.LiteralDataOutput;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.JobControlOps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Process(descriptionType = @DescriptionTypeAttr(
        title = "Download Tiff Process",
        identifier = "downloadTiffProcess",
        keywords = {"TiffDownload"},
        description = "Test Download tiff process from GeoServer from static location: 172.17.20.51"
), processAttr = @ProcessAttr(
        version = "0.0.1",
        jobControl = {JobControlOps.ASYNC, JobControlOps.SYNC},
        dataTransmissionType = {DataTransmissionModeType.REFERENCE, DataTransmissionModeType.VALUE}
))
public class DownloadTiffProcessEx {

    @InputAnnotation
    @LiteralDataInput(inputAttr = @InputAttr, literalAttr = @LiteralDataAttr, descriptionType = @DescriptionTypeAttr(
            title = "testField",
            identifier = "testField",
            description = "The input"
    ))
    private String testField;

    @OutputAnnotation
    @LiteralDataOutput(outputAttr = @OutputAttr, literalAttr = @LiteralDataAttr, descriptionType = @DescriptionTypeAttr(
            title = "result",
            identifier = "result",
            description = "The returned result"
    ))
    public String execute() {
        String response = "";
        try {
            //dummy endpoint that returns random at interval from 30000 milli -> 60000 milli a text with the duration to test concurrency
            URL url = new URL("http://localhost:8081/test_random");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputStr;
                StringBuilder serverResp = new StringBuilder();
                while ((inputStr = in.readLine()) != null) {
                    serverResp.append(inputStr);
                }

                in.close();

                response = serverResp.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getTestField() {
        return testField;
    }

    public void setTestField(String testField) {
        this.testField = testField;
    }
}