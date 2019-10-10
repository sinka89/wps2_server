package ro.uti.ksme.wps.wps2_server.uti_wps2.utils;

import ro.uti.ksme.wps.wps2_server.custom_pojo_types.ObjectFactoryCustom;

import javax.xml.bind.JAXBContext;

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
                    ro.uti.ksme.wps.wps2_server.pojo.ows._2.ObjectFactory.class,
                    ro.uti.ksme.wps.wps2_server.pojo.wps._2.ObjectFactory.class,
                    ro.uti.ksme.wps.wps2_server.pojo.w3._1999.xlink.ObjectFactory.class,
                    ro.uti.ksme.wps.wps2_server.pojo.w3._2001.xmlschema.ObjectFactory.class,
                    ObjectFactoryCustom.class);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
