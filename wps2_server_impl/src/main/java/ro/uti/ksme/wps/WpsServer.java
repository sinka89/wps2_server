package ro.uti.ksme.wps;

import java.io.InputStream;

public interface WpsServer {

    Object callOperation(InputStream xml, String path, String requestMethod);
}
