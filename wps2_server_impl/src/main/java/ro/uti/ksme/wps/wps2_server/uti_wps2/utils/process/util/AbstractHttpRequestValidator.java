package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import com.sun.net.httpserver.HttpExchange;
import ro.uti.ksme.wps.wps2.pojo.ows._2.DCP;
import ro.uti.ksme.wps.wps2.pojo.ows._2.HTTP;
import ro.uti.ksme.wps.wps2.pojo.ows._2.Operation;
import ro.uti.ksme.wps.wps2.pojo.ows._2.RequestMethodType;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.RequestMethodInvalid;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.RequestPathInvalid;

import javax.xml.bind.JAXBElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/10/2019
 * Time: 12:50 PM
 */
public abstract class AbstractHttpRequestValidator {

    public static void validateRequest(HttpExchange exchange, Operation... operations) {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();
        validateRequest(path, requestMethod, operations);
    }

    public static void validateRequest(String path, String requestMethod, Operation... operations) {
        boolean success = false;
        boolean correctPath = false;
        boolean allowsMethod = false;
        HTTP http;
        RequestMethodType request;
        for (Operation op : operations) {
            if (success) {
                break;
            }
            List<DCP> dcps = op.getDCP();
            for (DCP d : dcps) {
                if (d.isSetHTTP()) {
                    http = d.getHTTP();
                    List<JAXBElement<RequestMethodType>> getOrPost = http.getGetOrPost();
                    for (JAXBElement<RequestMethodType> requestMethodT : getOrPost) {
                        if (requestMethodT.getName() != null && requestMethodT.getName().getLocalPart() != null &&
                                requestMethodT.getName().getLocalPart().equalsIgnoreCase(requestMethod)) {
                            allowsMethod = true;
                            request = requestMethodT.getValue();
                            if (request.getHref().matches(".*" + path)) {
                                correctPath = true;
                            }
                        }
                        success = correctPath;
                    }
                }
            }
        }
        if (!success) {
            if (!allowsMethod) {
                throw new RequestMethodInvalid("Request Method not supported by server. Use \"POST\" http method!");
            }
            throw new RequestPathInvalid("Requested Path invalid: " + path + " use GetCapabilities Request to discover necessary paths!");
        }
    }
}
