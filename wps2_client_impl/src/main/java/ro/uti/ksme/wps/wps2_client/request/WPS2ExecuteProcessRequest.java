package ro.uti.ksme.wps.wps2_client.request;

import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 3:31 PM
 */
public class WPS2ExecuteProcessRequest extends AbstractExecuteProcessRequest {

    private String identifier;
    private JobControlOps jobControlOps;
    private ResponseType responseType;

    public WPS2ExecuteProcessRequest(URL serverUrl) {
        super(serverUrl);
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public JobControlOps getExecutionType() {
        return this.jobControlOps;
    }

    @Override
    public void setExecutionType(JobControlOps jobControlOps) {
        this.jobControlOps = jobControlOps;
    }

    @Override
    public ResponseType getResponseType() {
        return this.responseType;
    }

    @Override
    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }
}
