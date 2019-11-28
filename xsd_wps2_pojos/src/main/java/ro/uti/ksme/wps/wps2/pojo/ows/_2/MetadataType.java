//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.ows._2;

import ro.uti.ksme.wps.wps2.pojo.w3._1999.xlink.ActuateType;
import ro.uti.ksme.wps.wps2.pojo.w3._1999.xlink.ShowType;
import ro.uti.ksme.wps.wps2.pojo.w3._1999.xlink.TypeType;

import javax.xml.bind.annotation.*;


/**
 * This element either references or contains more metadata
 * about the element that includes this element. To reference metadata
 * stored remotely, at least the xlinks:href attribute in xlink:simpleAttrs
 * shall be included. Either at least one of the attributes in
 * xlink:simpleAttrs or a substitute for the AbstractMetaData element shall
 * be included, but not both. An Implementation Specification can restrict
 * the contents of this element to always be a reference or always contain
 * metadata. (Informative: This element was adapted from the
 * metaDataProperty element in GML 3.0.)
 *
 *
 * <p>Java class for MetadataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MetadataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.opengis.net/ows/2.0}AbstractMetaData" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.w3.org/1999/xlink}simpleAttrs"/&gt;
 *       &lt;attribute name="about" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MetadataType", propOrder = {
        "abstractMetaData"
})
public class MetadataType {

    /**
     *
     *
     */
    @XmlAttribute(name = "type", namespace = "http://www.w3.org/1999/xlink")
    public final static TypeType TYPE = TypeType.SIMPLE;
    @XmlElement(name = "AbstractMetaData")
    protected Object abstractMetaData;
    @XmlAttribute(name = "about")
    @XmlSchemaType(name = "anyURI")
    protected String about;
    @XmlAttribute(name = "href", namespace = "http://www.w3.org/1999/xlink")
    protected String href;
    @XmlAttribute(name = "role", namespace = "http://www.w3.org/1999/xlink")
    protected String role;
    @XmlAttribute(name = "arcrole", namespace = "http://www.w3.org/1999/xlink")
    protected String arcrole;
    @XmlAttribute(name = "title", namespace = "http://www.w3.org/1999/xlink")
    protected String title;
    @XmlAttribute(name = "show", namespace = "http://www.w3.org/1999/xlink")
    protected ShowType show;
    @XmlAttribute(name = "actuate", namespace = "http://www.w3.org/1999/xlink")
    protected ActuateType actuate;

    /**
     * Gets the value of the abstractMetaData property.
     *
     * @return possible object is
     * {@link Object }
     */
    public Object getAbstractMetaData() {
        return abstractMetaData;
    }

    /**
     * Sets the value of the abstractMetaData property.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setAbstractMetaData(Object value) {
        this.abstractMetaData = value;
    }

    public boolean isSetAbstractMetaData() {
        return (this.abstractMetaData != null);
    }

    /**
     * Gets the value of the about property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAbout() {
        return about;
    }

    /**
     * Sets the value of the about property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAbout(String value) {
        this.about = value;
    }

    public boolean isSetAbout() {
        return (this.about != null);
    }

    /**
     * Gets the value of the href property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHref(String value) {
        this.href = value;
    }

    public boolean isSetHref() {
        return (this.href != null);
    }

    /**
     * Gets the value of the role property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRole(String value) {
        this.role = value;
    }

    public boolean isSetRole() {
        return (this.role != null);
    }

    /**
     * Gets the value of the arcrole property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getArcrole() {
        return arcrole;
    }

    /**
     * Sets the value of the arcrole property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setArcrole(String value) {
        this.arcrole = value;
    }

    public boolean isSetArcrole() {
        return (this.arcrole != null);
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

    public boolean isSetTitle() {
        return (this.title != null);
    }

    /**
     * Gets the value of the show property.
     *
     * @return possible object is
     * {@link ShowType }
     */
    public ShowType getShow() {
        return show;
    }

    /**
     * Sets the value of the show property.
     *
     * @param value allowed object is
     *              {@link ShowType }
     */
    public void setShow(ShowType value) {
        this.show = value;
    }

    public boolean isSetShow() {
        return (this.show != null);
    }

    /**
     * Gets the value of the actuate property.
     *
     * @return possible object is
     * {@link ActuateType }
     */
    public ActuateType getActuate() {
        return actuate;
    }

    /**
     * Sets the value of the actuate property.
     *
     * @param value allowed object is
     *              {@link ActuateType }
     */
    public void setActuate(ActuateType value) {
        this.actuate = value;
    }

    public boolean isSetActuate() {
        return (this.actuate != null);
    }

}