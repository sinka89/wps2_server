package ro.uti.ksme.wps.wps2_client.connection_util;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 1/29/2020
 * Time: 10:51 AM
 */
@FunctionalInterface
public interface LogContentFunctional {
    void log(byte[] postContent) throws IOException;
}
