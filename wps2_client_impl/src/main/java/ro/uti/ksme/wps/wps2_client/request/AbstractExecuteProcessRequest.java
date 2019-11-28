package ro.uti.ksme.wps.wps2_client.request;

import ro.uti.ksme.wps.wps2.pojo.ows._2.CodeType;
import ro.uti.ksme.wps.wps2.pojo.wps._2.*;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/27/2019
 * Time: 10:07 AM
 */
public abstract class AbstractExecuteProcessRequest implements ExecuteProcessRequest {
    private Properties inputs;
    private OutputDefinitionType outputDefinitionType;
    private URL serverUrl;

    public AbstractExecuteProcessRequest(URL serverUrl) {
        this.serverUrl = serverUrl;
        this.inputs = new Properties();
    }

    @Override
    public <T> void addInput(String name, List<? extends T> value) {
        if (value == null) {
            inputs.remove(name);
        } else {
            for (T val : value) {
                if (!(val instanceof Data) && !(val instanceof ReferenceType) && !(val instanceof DataInputType)) {
                    throw new IllegalArgumentException("The values can be either of type ro.uti.ksme.wps.wps2.pojo.wps._2.Data or" +
                            "of type ro.uti.ksme.wps.wps2.pojo.wps._2.ReferenceType");
                }
            }
            inputs.put(name, value);
        }
    }

    @Override
    public void setOutputDefinitionType(OutputDefinitionType output) {
        if (this.outputDefinitionType != null) {
            this.outputDefinitionType = output;
        }
    }

    @Override
    public ExecuteRequestType createExecuteRequestType() {
        ObjectFactory wps2Factory = new ObjectFactory();
        ExecuteRequestType executeRequestType = wps2Factory.createExecuteRequestType();

        if (inputs.size() > 0) {
            for (Map.Entry<Object, Object> entry : inputs.entrySet()) {
                DataInputType in = wps2Factory.createDataInputType();
                in.setId(entry.getKey().toString());

                if (entry.getValue() instanceof List) {
                    List dataInputValues = (List) entry.getValue();
                    int size = dataInputValues.size();
                    for (int i = 0; i < size; ++i) {
                        if (dataInputValues.get(i) instanceof Data) {
                            if (in.getData() == null) {
                                in.setData((Data) dataInputValues.get(i));
                            } else {
                                throw new IllegalArgumentException("Data was already set on the DataInputType with key: " + in.getId());
                            }
                            continue;
                        }
                        if (dataInputValues.get(i) instanceof ReferenceType) {
                            if (in.getReference() == null) {
                                in.setReference((ReferenceType) dataInputValues.get(i));
                            } else {
                                throw new IllegalArgumentException("ReferenceType was already set on the DataInputType with key: " + in.getId());
                            }
                            continue;
                        }
                        if (dataInputValues.get(i) instanceof DataInputType) {
                            in.getInput().add((DataInputType) dataInputValues.get(i));
                        }
                    }
                }
                executeRequestType.getInput().add(in);
            }
        }

        executeRequestType.getOutput().add(this.outputDefinitionType);
        executeRequestType.setMode(getExecutionType().getMode());
        executeRequestType.setResponse(getResponseType().getType());
        CodeType codeTypeIdentifier = new CodeType();
        codeTypeIdentifier.setValue(getIdentifier());
        executeRequestType.setIdentifier(codeTypeIdentifier);

        return executeRequestType;
    }
}
