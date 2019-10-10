package ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.util;

import org.reflections.Reflections;
import ro.uti.ksme.wps.wps2_server.uti_wps2.utils.process.annotations.process.Process;

import java.util.Collection;
import java.util.Set;

/**
 * @author Bogdan-Adrian Sincu
 * <p>
 * Utility class that searches the package defined by prop 'CLASS_PACKAGE_PREFIX' for @Process annotation classes
 */
public class WpsProcessReflectionUtil {

    private static final String CLASS_PACKAGE_PREFIX = "ro.uti.ksme.wps.wps2_server.uti_wps2.server_impl.process";

    public static Collection<Class<?>> getListOfProcessesClass() {
        Reflections reflections = new Reflections(CLASS_PACKAGE_PREFIX);
        return reflections.getTypesAnnotatedWith(Process.class);
    }

    public static Class<?> getProcessClassBasedOnIdentifier(String identifier) {
        Reflections reflections = new Reflections(CLASS_PACKAGE_PREFIX);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Process.class);
        Class toReturn = null;
        for (Class c : typesAnnotatedWith) {
            Process annotation = (Process) c.getAnnotation(Process.class);
            if (annotation.descriptionType().identifier().equalsIgnoreCase(identifier)) {
                toReturn = c;
                break;
            }
        }
        return toReturn;
    }
}
