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
import java.nio.file.Files;
import java.nio.file.Path;
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

    public void handle(HttpExchange httpExchange) {
        OutputStream os = null;
        InputStream is = null;
        try {
            os = httpExchange.getResponseBody();
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
                    int length;
                    while ((length = is.read(buf)) != -1) {
                        os.write(buf, 0, length);
                    }
                } else if (output instanceof ProcessResultWrapper) {
                    if (((ProcessResultWrapper) output).getData() instanceof byte[]) {
                        is = new ByteArrayInputStream(((byte[]) ((ProcessResultWrapper) output).getData()));
                    } else if (((ProcessResultWrapper) output).getData() instanceof Path) {
                        is = new BufferedInputStream(Files.newInputStream((Path) ((ProcessResultWrapper) output).getData()));
                    }
                    if (is != null) {
                        if (is.available() != 0) {
                            byte[] buffer = new byte[1024 * 4];
                            int length;
                            if (((ProcessResultWrapper) output).getMimeType() != null) {
                                responseHeaders.add("Content-Type", ((ProcessResultWrapper) output).getMimeType());
                            } else {
                                responseHeaders.add("Content-Type", "application/octet-stream");
                            }
                            httpExchange.sendResponseHeaders(200, ((ProcessResultWrapper) output).getContentSize());
                            while ((length = is.read(buffer)) != -1) {
                                os.write(buffer, 0, length);
                            }
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
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException ignored) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private void validateRequestOnServerDeclaration(HttpExchange request) {
        AbstractHttpRequestValidator.validateRequest(request, wps2ServerProps.OPERATIONS_METADATA_PROPS.getAllOperationsDetails());
    }
}
