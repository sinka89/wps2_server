//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="restriction" type="{http://www.w3.org/2001/XMLSchema}complexRestrictionType"/&gt;
 *         &lt;element name="extension" type="{http://www.w3.org/2001/XMLSchema}extensionType"/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="mixed" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "restriction",
        "extension"
})
@XmlRootElement(name = "complexContent")
public class ComplexContent
        extends Annotated {

    protected ComplexRestrictionType restriction;
    protected ExtensionType extension;
    @XmlAttribute(name = "mixed")
    protected Boolean mixed;

    /**
     * Gets the value of the restriction property.
     *
     * @return possible object is
     * {@link ComplexRestrictionType }
     */
    public ComplexRestrictionType getRestriction() {
        return restriction;
    }

    /**
     * Sets the value of the restriction property.
     *
     * @param value allowed object is
     *              {@link ComplexRestrictionType }
     */
    public void setRestriction(ComplexRestrictionType value) {
        this.restriction = value;
    }

    public boolean isSetRestriction() {
        return (this.restriction != null);
    }

    /**
     * Gets the value of the extension property.
     *
     * @return possible object is
     * {@link ExtensionType }
     */
    public ExtensionType getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     *
     * @param value allowed object is
     *              {@link ExtensionType }
     */
    public void setExtension(ExtensionType value) {
        this.extension = value;
    }

    public boolean isSetExtension() {
        return (this.extension != null);
    }

    /**
     * Gets the value of the mixed property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public boolean isMixed() {
        return mixed;
    }

    /**
     * Sets the value of the mixed property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setMixed(boolean value) {
        this.mixed = value;
    }

    public boolean isSetMixed() {
        return (this.mixed != null);
    }

    public void unsetMixed() {
        this.mixed = null;
    }

}
