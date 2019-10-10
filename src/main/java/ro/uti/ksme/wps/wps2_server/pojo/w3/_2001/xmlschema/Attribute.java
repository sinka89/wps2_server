//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 01:22:16 PM EEST 
//


package ro.uti.ksme.wps.wps2_server.pojo.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * <p>Java class for attribute complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="attribute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="simpleType" type="{http://www.w3.org/2001/XMLSchema}localSimpleType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}defRef"/&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}QName" /&gt;
 *       &lt;attribute name="use" default="optional"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="prohibited"/&gt;
 *             &lt;enumeration value="optional"/&gt;
 *             &lt;enumeration value="required"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="fixed" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="form" type="{http://www.w3.org/2001/XMLSchema}formChoice" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attribute", propOrder = {
        "simpleType"
})
@XmlSeeAlso({
        TopLevelAttribute.class
})
public class Attribute
        extends Annotated {

    protected LocalSimpleType simpleType;
    @XmlAttribute(name = "type")
    protected QName type;
    @XmlAttribute(name = "use")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String use;
    @XmlAttribute(name = "default")
    protected String _default;
    @XmlAttribute(name = "fixed")
    protected String fixed;
    @XmlAttribute(name = "form")
    protected FormChoice form;
    @XmlAttribute(name = "name")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlAttribute(name = "ref")
    protected QName ref;

    /**
     * Gets the value of the simpleType property.
     *
     * @return possible object is
     * {@link LocalSimpleType }
     */
    public LocalSimpleType getSimpleType() {
        return simpleType;
    }

    /**
     * Sets the value of the simpleType property.
     *
     * @param value allowed object is
     *              {@link LocalSimpleType }
     */
    public void setSimpleType(LocalSimpleType value) {
        this.simpleType = value;
    }

    public boolean isSetSimpleType() {
        return (this.simpleType != null);
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link QName }
     */
    public QName getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link QName }
     */
    public void setType(QName value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type != null);
    }

    /**
     * Gets the value of the use property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUse() {
        if (use == null) {
            return "optional";
        } else {
            return use;
        }
    }

    /**
     * Sets the value of the use property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUse(String value) {
        this.use = value;
    }

    public boolean isSetUse() {
        return (this.use != null);
    }

    /**
     * Gets the value of the default property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDefault(String value) {
        this._default = value;
    }

    public boolean isSetDefault() {
        return (this._default != null);
    }

    /**
     * Gets the value of the fixed property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFixed() {
        return fixed;
    }

    /**
     * Sets the value of the fixed property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFixed(String value) {
        this.fixed = value;
    }

    public boolean isSetFixed() {
        return (this.fixed != null);
    }

    /**
     * Gets the value of the form property.
     *
     * @return possible object is
     * {@link FormChoice }
     */
    public FormChoice getForm() {
        return form;
    }

    /**
     * Sets the value of the form property.
     *
     * @param value allowed object is
     *              {@link FormChoice }
     */
    public void setForm(FormChoice value) {
        this.form = value;
    }

    public boolean isSetForm() {
        return (this.form != null);
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name != null);
    }

    /**
     * Gets the value of the ref property.
     *
     * @return possible object is
     * {@link QName }
     */
    public QName getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     *
     * @param value allowed object is
     *              {@link QName }
     */
    public void setRef(QName value) {
        this.ref = value;
    }

    public boolean isSetRef() {
        return (this.ref != null);
    }

}
