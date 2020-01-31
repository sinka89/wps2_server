package ro.uti.ksme.wps.wps2.utils;

import ro.uti.ksme.wps.wps2.custom_pojo_types.ObjectFactoryCustom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * @author Bogdan-Adrian Sincu
 * <p>
 * Enum defines the JAXBContext instance (singleton)
 */
public enum JaxbContainer {

    INSTANCE;

    public JAXBContext jaxbContext;

    JaxbContainer() {
        try {
            this.jaxbContext = JAXBContext.newInstance(
                    ro.uti.ksme.wps.wps2.pojo.ows._2.ObjectFactory.class,
                    ro.uti.ksme.wps.wps2.pojo.wps._2.ObjectFactory.class,
                    ro.uti.ksme.wps.wps2.pojo.w3._1999.xlink.ObjectFactory.class,
                    ro.uti.ksme.wps.wps2.pojo.w3._2001.xmlschema.ObjectFactory.class,
                    ObjectFactoryCustom.class);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Marshaller getMarshallerWithPrefixMapper() throws JAXBException {
        Marshaller marshaller = this.jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new Wps2NamespaceprefixMapper());
        return marshaller;
    }
}
