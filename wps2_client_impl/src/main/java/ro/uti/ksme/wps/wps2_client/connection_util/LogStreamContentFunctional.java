package ro.uti.ksme.wps.wps2_client.connection_util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 1/29/2020
 * Time: 10:51 AM
 */
@FunctionalInterface
public interface LogStreamContentFunctional {
    InputStream log(InputStream postContent) throws IOException;
}
