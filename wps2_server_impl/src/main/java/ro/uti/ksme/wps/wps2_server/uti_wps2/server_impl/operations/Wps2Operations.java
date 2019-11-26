package ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.operations;

import ro.uti.ksme.wps.wps2.pojo.wps._2.*;

public interface Wps2Operations {

    Object getCapabilities(GetCapabilitiesType getCapabilities);

    Object describeProcess(DescribeProcess describeProcess);

    Object execute(ExecuteRequestType execute);

    Object getStatus(GetStatus getStatus);

    Object getResult(GetResult getResult);

    Object dismiss(Dismiss dismiss);
}
