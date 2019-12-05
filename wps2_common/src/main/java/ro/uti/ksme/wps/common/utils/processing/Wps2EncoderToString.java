package ro.uti.ksme.wps.common.utils.processing;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author : Bogdan-Adrian Sincu
 * Date: 12/2/2019
 * Time: 12:20 PM
 */
@FunctionalInterface
public interface Wps2EncoderToString {

    String encode(Object element, JAXBContext context) throws JAXBException, IOException;
}
