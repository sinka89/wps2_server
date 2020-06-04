package ro.uti.ksme.wps.wps2_client.util;

import ro.uti.ksme.wps.common.utils.processing.Wps2EncoderToString;
import ro.uti.ksme.wps.wps2.pojo.ows._2.ExceptionReport;
import ro.uti.ksme.wps.wps2.pojo.wps._2.Result;
import ro.uti.ksme.wps.wps2.utils.JaxbContainer;
import ro.uti.ksme.wps.wps2.utils.Wps2NamespaceprefixMapper;

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
        return encodeToString(element, JaxbContainer.INSTANCE.jaxbContext, getWps2EncoderToString(element));
    }

    public static String encodeToString(Result result) throws JAXBException, IOException {
        return encodeToString(result, JaxbContainer.INSTANCE.jaxbContext, getWps2EncoderToString(result));
    }

    public static String encodeToString(ExceptionReport exceptionReport) throws JAXBException, IOException {
        return encodeToString(exceptionReport, JaxbContainer.INSTANCE.jaxbContext, getWps2EncoderToString(exceptionReport));
    }

    private static Wps2EncoderToString getWps2EncoderToString(Object object) {
        return (el, context) -> {
            try (StringWriter sw = new StringWriter()) {
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new Wps2NamespaceprefixMapper());
                marshaller.marshal(object, sw);
                sw.flush();
                return sw.toString();
            }
        };
    }

    private static String encodeToString(JAXBElement element, JAXBContext context, Wps2EncoderToString fun) throws JAXBException, IOException {
        return fun.encode(element, context);
    }

    public static String encodeToString(JAXBElement element, Wps2EncoderToString fun) throws JAXBException, IOException {
        return encodeToString(element, JaxbContainer.INSTANCE.jaxbContext, fun);
    }

    private static String encodeToString(Result result, JAXBContext context, Wps2EncoderToString fun) throws JAXBException, IOException {
        return fun.encode(result, context);
    }

    public static String encodeToString(Result result, Wps2EncoderToString fun) throws JAXBException, IOException {
        return encodeToString(result, JaxbContainer.INSTANCE.jaxbContext, fun);
    }

    private static String encodeToString(ExceptionReport exceptionReport, JAXBContext context, Wps2EncoderToString fun) throws JAXBException, IOException {
        return fun.encode(exceptionReport, context);
    }

    public static String encodeToString(ExceptionReport exceptionReport, Wps2EncoderToString fun) throws JAXBException, IOException {
        return encodeToString(exceptionReport, JaxbContainer.INSTANCE.jaxbContext, fun);
    }
}
