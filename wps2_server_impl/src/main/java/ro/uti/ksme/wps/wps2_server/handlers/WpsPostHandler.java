package ro.uti.ksme.wps.wps2_server.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.uti.ksme.wps.WpsServer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ErrorService;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.Wps2ServerProps;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.RequestMethodInvalid;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.exception.RequestPathInvalid;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.AbstractHttpRequestValidator;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util.ProcessResultWrapper;

import java.io.*;
import java.util.Optional;

/**
 * @author Bogdan-Adrian Sincu
 * <p>
 * Class implementation for handling Http requests
 */
@Component
public class WpsPostHandler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WpsPostHandler.class);
    private static final String POST = "POST";
    private ErrorService errorService;
    private WpsServer wps2Sever;
    private Wps2ServerProps wps2ServerProps;

    public WpsPostHandler() {
    }

    @Autowired
    public void setErrorService(ErrorService errorService) {
        this.errorService = errorService;
    }

    @Autowired
    public void setWps2Sever(WpsServer wps2Sever) {
        this.wps2Sever = wps2Sever;
    }

    @Autowired
    public void setWps2ServerProps(Wps2ServerProps wps2ServerProps) {
        this.wps2ServerProps = wps2ServerProps;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            InputStream is = null;
            Headers responseHeaders = httpExchange.getResponseHeaders();
            try {
                this.validateRequestOnServerDeclaration(httpExchange);
                Object output = wps2Sever.callOperation(httpExchange.getRequestBody(), httpExchange.getRequestURI().getPath(), httpExchange.getRequestMethod());
                if (output instanceof StringWriter) {
                    String s = output.toString();
                    responseHeaders.add("Content-Type", "application/xml");
                    httpExchange.sendResponseHeaders(200, s.length());
                    is = new ByteArrayInputStream(s.getBytes());
                    byte[] buf = new byte[1024];
                    int length = is.read(buf);
                    while (length != -1) {
                        os.write(buf, 0, length);
                        length = is.read(buf);
                    }
                } else if (output instanceof ProcessResultWrapper && ((ProcessResultWrapper) output).getData() instanceof byte[]) {
                    is = new ByteArrayInputStream(((byte[]) ((ProcessResultWrapper) output).getData()));
                    int available = is.available();
                    if (available != 0) {
                        byte[] buffer = new byte[1024];
                        int length = is.read(buffer);

                        if (((ProcessResultWrapper) output).getMimeType() != null) {
                            responseHeaders.add("Content-Type", ((ProcessResultWrapper) output).getMimeType());
                        } else {
                            responseHeaders.add("Content-Type", "application/octet-stream");
                        }
                        httpExchange.sendResponseHeaders(200, available);
                        while (length != -1) {
                            os.write(buffer, 0, length);
                            length = is.read(buffer);
                        }
                    }
                }
            } catch (RequestMethodInvalid e) {
                Optional<String> s = errorService.generateErrorMsgXml(e.toString());
                if (s.isPresent()) {
                    responseHeaders.add("Content-Type", "application/xml");
                    httpExchange.sendResponseHeaders(405, s.get().length());
                    os.write(s.get().getBytes());
                }
            } catch (RequestPathInvalid e) {
                Optional<String> s = errorService.generateErrorMsgXml(e.toString());
                if (s.isPresent()) {
                    responseHeaders.add("Content-Type", "application/xml");
                    httpExchange.sendResponseHeaders(404, s.get().length());
                    os.write(s.get().getBytes());
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                Optional<String> s = errorService.generateErrorMsgXml(e.toString());
                if (s.isPresent()) {
                    responseHeaders.add("Content-Type", "application/xml");
                    httpExchange.sendResponseHeaders(500, s.get().length());
                    os.write(s.get().getBytes());
                }
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

    private void validateRequestOnServerDeclaration(HttpExchange request) {
        AbstractHttpRequestValidator.validateRequest(request, wps2ServerProps.OPERATIONS_METADATA_PROPS.getAllOperationsDetails());
    }
}
