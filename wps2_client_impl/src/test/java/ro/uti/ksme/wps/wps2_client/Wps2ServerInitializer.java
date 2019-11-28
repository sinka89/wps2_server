package ro.uti.ksme.wps.wps2_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 11/26/2019
 * Time: 4:29 PM
 */
public class Wps2ServerInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ServerInitializer.class);

    private static final String WPS2_SERVER_IMPL_JAR = "wps2_server_impl-1.0.0.jar";
    private static final String WPS2_SERVER_MAIN_CLASS = "ro.uti.ksme.WpsServerHost";

    public static Process getProcess() {
        Process result = null;
        try {
            File wpsServer = new File("../wps2_server_impl");
            if (wpsServer.exists()) {
                File[] buildTarget = wpsServer.listFiles((dir, name) -> name.equals("target"));
                //if target build exists try to run.
                if (buildTarget != null && buildTarget.length == 1) {
                    File target = buildTarget[0];
                    File[] jarAndLib = target.listFiles((dir, name) -> name.equalsIgnoreCase(WPS2_SERVER_IMPL_JAR) || name.equalsIgnoreCase("lib"));
                    if (jarAndLib != null && jarAndLib.length == 2) {
                        String jarPath = null;
                        String libPath = null;
                        for (File f : jarAndLib) {
                            if (f.getName().equalsIgnoreCase(WPS2_SERVER_IMPL_JAR)) {
                                jarPath = f.getCanonicalPath();
                            } else if (f.getName().equalsIgnoreCase("lib")) {
                                libPath = f.getCanonicalPath();
                            }
                        }
                        String toExecute = "java -Xmx512m -cp \"" + jarPath + ";" + libPath + "\\*\" " + WPS2_SERVER_MAIN_CLASS;
                        result = Runtime.getRuntime().exec(toExecute);
                    }
                } else {
                    LOGGER.info("Wps2_server_impl build was not found... will continue probably in another location and active.");
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
}
