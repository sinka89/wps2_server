package ro.uti.ksme.wps.wps2.custom_pojo_types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * @author Bogdan-Adrian Sincu created on 10/4/2019
 * <p>
 * This object contains factory methods for each Java content interface and Java element interface generated in the custom_pojo_types package.
 * An ObjectFactory allows to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding of schema type definitions, element declarations and model groups
 * Factory methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactoryCustom {
    private final static QName _RawData_QNAME = new QName("http://www.opengis.net/ows/2.0", "RawData");

    public ObjectFactoryCustom() {
    }


    public RawData createRawData() {
        return new RawData();
    }

    @XmlElementDecl(namespace = "http://www.opengis.net/ows/2.0",
            name = "RawData",
            substitutionHeadNamespace = "http://www.opengis.net/wps/2.0",
            substitutionHeadName = "DataDescription")
    public JAXBElement<RawData> createRawData(RawData rawData) {
        return new JAXBElement<>(_RawData_QNAME, RawData.class, rawData);
    }
}
