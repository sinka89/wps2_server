package ro.uti.ksme.wps.wps2_client.util;

import ro.uti.ksme.wps.common.utils.processing.Wps2EncoderToString;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/2/2019
 * Time: 12:32 PM
 */
public class Wps2InputEncodingUtil {

    public static String encodeToString(JAXBElement element) throws JAXBException, IOException {
        return encodeToString(element, JaxbContainer.INSTANCE.jaxbContext, (el, context) -> {
            try (StringWriter sw = new StringWriter()) {
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(element, sw);
                sw.flush();
                return sw.toString();
            }
        });
    }

    private static String encodeToString(JAXBElement element, JAXBContext context, Wps2EncoderToString fun) throws JAXBException, IOException {
        return fun.encode(element, context);
    }

    public static String encodeToString(JAXBElement element, Wps2EncoderToString fun) throws JAXBException, IOException {
        return encodeToString(element, JaxbContainer.INSTANCE.jaxbContext, fun);
    }
}
