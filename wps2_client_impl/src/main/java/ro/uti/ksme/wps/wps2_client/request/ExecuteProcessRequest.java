package ro.uti.ksme.wps.wps2_client.request;

import ro.uti.ksme.wps.common.utils.enums.JobControlOps;
import ro.uti.ksme.wps.common.utils.enums.ResponseType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.ExecuteRequestType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.OutputDefinitionType;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 9:59 AM
 */
public interface ExecuteProcessRequest {

    String getIdentifier();

    void setIdentifier(String identifier);

    <T> void addInput(String name, List<? extends T> value);

    @Deprecated
    void setOutputDefinitionType(OutputDefinitionType output);

    void addOutputDefinitionType(OutputDefinitionType output);

    void setOutputDefinitionType(List<OutputDefinitionType> outputs);

    JobControlOps getExecutionType();

    void setExecutionType(JobControlOps jobControlOps);

    ResponseType getResponseType();

    void setResponseType(ResponseType responseType);

    ExecuteRequestType createExecuteRequestType();
}
