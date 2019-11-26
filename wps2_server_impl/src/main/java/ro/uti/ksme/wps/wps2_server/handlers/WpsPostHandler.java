package ro.uti.ksme.wps.wps2_server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.uti.ksme.wps.WpsServer;
import ro.uti.ksme.wps.wps2_server.uti_wps2.dto.ExceptionDTO;
import ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.service.ErrorService;

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

    public void handle(HttpExchange httpExchange) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            if (!httpExchange.getRequestMethod().equals(POST)) {
                int status = 405;
                Optional<String> err = errorService.generateErrorMsgXml(new ExceptionDTO(status, "Method \"" + httpExchange.getRequestMethod() + "\" not supported please use \"POST\" http method"));
                if (err.isPresent()) {
                    String msg = err.get();
                    httpExchange.getResponseHeaders().add("Content-Type", "application/xml");
                    httpExchange.sendResponseHeaders(status, msg.length());
                    os.write(msg.getBytes());
                }
            } else {
                InputStream is = null;
                try {
                    Object output = wps2Sever.callOperation(httpExchange.getRequestBody());
                    if (output instanceof StringWriter) {
                        String s = output.toString();
                        httpExchange.getResponseHeaders().add("Content-Type", "application/xml");
                        httpExchange.sendResponseHeaders(200, s.length());
                        is = new ByteArrayInputStream(s.getBytes());
                        byte[] buf = new byte[1024];
                        int length = is.read(buf);
                        while (length != -1) {
                            os.write(buf, 0, length);
                            length = is.read(buf);
                        }
                    } else if (output instanceof byte[]) {
                        is = new ByteArrayInputStream((byte[]) output);
                        int available = is.available();
                        if (available != 0) {
                            httpExchange.getResponseHeaders().add("Content-Type", "application/octet-stream");
                            byte[] buffer = new byte[1024];
                            int length = is.read(buffer);
                            httpExchange.sendResponseHeaders(200, available);
                            while (length != -1) {
                                os.write(buffer, 0, length);
                                length = is.read(buffer);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    Optional<String> s = errorService.generateErrorMsgXml(e.toString());
                    if (s.isPresent()) {
                        httpExchange.getResponseHeaders().add("Content-Type", "application/xml");
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
    }
}
