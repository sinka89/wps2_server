//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 01:22:16 PM EEST 
//


package ro.uti.ksme.wps.wps2_server.pojo.w3._1999.xlink;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for arcType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="arcType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;group ref="{http://www.w3.org/1999/xlink}arcModel"/&gt;
 *       &lt;attGroup ref="{http://www.w3.org/1999/xlink}arcAttrs"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "arcType", propOrder = {
        "arcModelTitle"
})
public class ArcType {

    /**
     *
     *
     */
    @XmlAttribute(name = "type", namespace = "http://www.w3.org/1999/xlink", required = true)
    public final static TypeType TYPE = TypeType.ARC;
    @XmlElement(name = "title")
    protected List<TitleEltType> arcModelTitle;
    @XmlAttribute(name = "arcrole", namespace = "http://www.w3.org/1999/xlink")
    protected String arcrole;
    @XmlAttribute(name = "title", namespace = "http://www.w3.org/1999/xlink")
    protected String arcAttrsTitle;
    @XmlAttribute(name = "show", namespace = "http://www.w3.org/1999/xlink")
    protected ShowType show;
    @XmlAttribute(name = "actuate", namespace = "http://www.w3.org/1999/xlink")
    protected ActuateType actuate;
    @XmlAttribute(name = "from", namespace = "http://www.w3.org/1999/xlink")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String from;
    @XmlAttribute(name = "to", namespace = "http://www.w3.org/1999/xlink")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String to;

    /**
     * Gets the value of the arcModelTitle property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the arcModelTitle property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArcModelTitle().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TitleEltType }
     */
    public List<TitleEltType> getArcModelTitle() {
        if (arcModelTitle == null) {
            arcModelTitle = new ArrayList<TitleEltType>();
        }
        return this.arcModelTitle;
    }

    public boolean isSetArcModelTitle() {
        return ((this.arcModelTitle != null) && (!this.arcModelTitle.isEmpty()));
    }

    public void unsetArcModelTitle() {
        this.arcModelTitle = null;
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
     * Gets the value of the arcAttrsTitle property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getArcAttrsTitle() {
        return arcAttrsTitle;
    }

    /**
     * Sets the value of the arcAttrsTitle property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setArcAttrsTitle(String value) {
        this.arcAttrsTitle = value;
    }

    public boolean isSetArcAttrsTitle() {
        return (this.arcAttrsTitle != null);
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

    /**
     * Gets the value of the from property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFrom(String value) {
        this.from = value;
    }

    public boolean isSetFrom() {
        return (this.from != null);
    }

    /**
     * from and to have default behavior when values are missing
     *
     * @return possible object is
     * {@link String }
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTo(String value) {
        this.to = value;
    }

    public boolean isSetTo() {
        return (this.to != null);
    }

}
