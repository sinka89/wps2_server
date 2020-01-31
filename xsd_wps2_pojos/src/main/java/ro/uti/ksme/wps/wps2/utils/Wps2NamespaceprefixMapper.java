package ro.uti.ksme.wps.wps2.utils;


import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 1/30/2020
 * Time: 4:31 PM
 */
public class Wps2NamespaceprefixMapper extends NamespacePrefixMapper {
    private Map<String, String> namespaceMap = Stream.of(new String[][]{
            {"http://www.opengis.net/wps/2.0", "wps"},
            {"http://www.opengis.net/ows/2.0", "ows"},
            {"http://www.w3.org/2001/XMLSchema-instance", "xsi"},
            {"http://www.w3.org/1999/xlink", "xlink"},
            {"http://www.w3.org/2001/XMLSchema", "xs"},
            {"http://www.w3.org/XML/1998/namespace", "xml"}
    }).collect(Collectors.toMap(d -> d[0], d -> d[1]));

    @Override
    public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
        return namespaceMap.getOrDefault(namespaceUri, suggestion);
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return namespaceMap.keySet().toArray(new String[]{});
    }
}
