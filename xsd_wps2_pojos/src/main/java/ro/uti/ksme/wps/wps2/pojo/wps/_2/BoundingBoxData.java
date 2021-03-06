//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.wps._2;

import ro.uti.ksme.wps.wps2.pojo.ows._2.BoundingBoxType;
import ro.uti.ksme.wps.wps2.pojo.ows._2.WGS84BoundingBoxType;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengis.net/wps/2.0}DataDescriptionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.opengis.net/wps/2.0}SupportedCRS" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.opengis.net/ows/2.0}BoundingBox"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "supportedCRS",
        "boundingBox"
})
public class BoundingBoxData
        extends DataDescriptionType {

    @XmlElement(name = "SupportedCRS", required = true)
    protected List<SupportedCRS> supportedCRS;
    @XmlElementRef(name = "BoundingBox", namespace = "http://www.opengis.net/ows/2.0", type = JAXBElement.class)
    protected JAXBElement<? extends BoundingBoxType> boundingBox;

    /**
     * Gets the value of the supportedCRS property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supportedCRS property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupportedCRS().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SupportedCRS }
     */
    public List<SupportedCRS> getSupportedCRS() {
        if (supportedCRS == null) {
            supportedCRS = new ArrayList<SupportedCRS>();
        }
        return this.supportedCRS;
    }

    public boolean isSetSupportedCRS() {
        return ((this.supportedCRS != null) && (!this.supportedCRS.isEmpty()));
    }

    public void unsetSupportedCRS() {
        this.supportedCRS = null;
    }

    /**
     * Gets the value of the boundingBox property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link WGS84BoundingBoxType }{@code >}
     * {@link JAXBElement }{@code <}{@link BoundingBoxType }{@code >}
     */
    public JAXBElement<? extends BoundingBoxType> getBoundingBox() {
        return boundingBox;
    }

    /**
     * Sets the value of the boundingBox property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link WGS84BoundingBoxType }{@code >}
     *              {@link JAXBElement }{@code <}{@link BoundingBoxType }{@code >}
     */
    public void setBoundingBox(JAXBElement<? extends BoundingBoxType> value) {
        this.boundingBox = value;
    }

    public boolean isSetBoundingBox() {
        return (this.boundingBox != null);
    }

}
