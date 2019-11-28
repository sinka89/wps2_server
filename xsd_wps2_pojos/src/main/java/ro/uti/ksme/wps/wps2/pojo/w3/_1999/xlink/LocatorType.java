//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.w3._1999.xlink;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for locatorType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="locatorType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;group ref="{http://www.w3.org/1999/xlink}locatorModel"/&gt;
 *       &lt;attGroup ref="{http://www.w3.org/1999/xlink}locatorAttrs"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "locatorType", propOrder = {
        "locatorModelTitle"
})
public class LocatorType {

    /**
     *
     *
     */
    @XmlAttribute(name = "type", namespace = "http://www.w3.org/1999/xlink", required = true)
    public final static TypeType TYPE = TypeType.LOCATOR;
    @XmlElement(name = "title")
    protected List<TitleEltType> locatorModelTitle;
    @XmlAttribute(name = "href", namespace = "http://www.w3.org/1999/xlink", required = true)
    protected String href;
    @XmlAttribute(name = "role", namespace = "http://www.w3.org/1999/xlink")
    protected String role;
    @XmlAttribute(name = "title", namespace = "http://www.w3.org/1999/xlink")
    protected String locatorAttrsTitle;
    @XmlAttribute(name = "label", namespace = "http://www.w3.org/1999/xlink")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String label;

    /**
     * Gets the value of the locatorModelTitle property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locatorModelTitle property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocatorModelTitle().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TitleEltType }
     */
    public List<TitleEltType> getLocatorModelTitle() {
        if (locatorModelTitle == null) {
            locatorModelTitle = new ArrayList<TitleEltType>();
        }
        return this.locatorModelTitle;
    }

    public boolean isSetLocatorModelTitle() {
        return ((this.locatorModelTitle != null) && (!this.locatorModelTitle.isEmpty()));
    }

    public void unsetLocatorModelTitle() {
        this.locatorModelTitle = null;
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
     * Gets the value of the locatorAttrsTitle property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLocatorAttrsTitle() {
        return locatorAttrsTitle;
    }

    /**
     * Sets the value of the locatorAttrsTitle property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocatorAttrsTitle(String value) {
        this.locatorAttrsTitle = value;
    }

    public boolean isSetLocatorAttrsTitle() {
        return (this.locatorAttrsTitle != null);
    }

    /**
     * label is not required, but locators have no particular
     * XLink function if they are not labeled.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLabel(String value) {
        this.label = value;
    }

    public boolean isSetLabel() {
        return (this.label != null);
    }

}