package ro.uti.ksme.wps.wps2_server.uti_wps2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Bogdan-Adrian Sincu
 */
public class Wps2ServerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Wps2ServerUtils.class);

    public static XMLGregorianCalendar getXMLGregorianCalendarWithDelay(long durationInMillis) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        XMLGregorianCalendar date = null;
        try {
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            date = datatypeFactory.newXMLGregorianCalendar(calendar);
            Duration duration = datatypeFactory.newDuration(durationInMillis);
            date.add(duration);
        } catch (DatatypeConfigurationException e) {
            LOGGER.error("Unable to generate XMLGregorianCalendar object: " + e.getMessage(), e);
        }
        return date;
    }

    public static XMLGregorianCalendar getXMLGregorianCalendar(long actualTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(actualTime));
        XMLGregorianCalendar date = null;
        try {
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            date = datatypeFactory.newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            LOGGER.error("Unable to generate XMLGregorianCalendar object: " + e.getMessage(), e);
        }
        return date;
    }
}
