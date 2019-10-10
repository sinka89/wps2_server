//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 01:22:16 PM EEST 
//


package ro.uti.ksme.wps.wps2_server.pojo.ows._2;

import javax.xml.bind.annotation.*;


/**
 * References metadata about a quantity, and provides a name
 * for this metadata. (Informative: This element was simplified from the
 * metaDataProperty element in GML 3.0.)
 * <p>
 * Human-readable name of the metadata described by
 * associated referenced document.
 *
 * <p>Java class for DomainMetadataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DomainMetadataType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute ref="{http://www.opengis.net/ows/2.0}reference"/&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DomainMetadataType", propOrder = {
        "value"
})
public class DomainMetadataType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "reference", namespace = "http://www.opengis.net/ows/2.0")
    @XmlSchemaType(name = "anyURI")
    protected String reference;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return (this.value != null);
    }

    /**
     * Gets the value of the reference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setReference(String value) {
        this.reference = value;
    }

    public boolean isSetReference() {
        return (this.reference != null);
    }

}
