//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.wps._2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * Description of an input to a process.
 * <p>
 * <p>
 * In this use, the DescriptionType shall describe a process input.
 *
 *
 * <p>Java class for InputDescriptionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="InputDescriptionType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengis.net/wps/2.0}DescriptionType"&gt;
 *       &lt;choice&gt;
 *         &lt;element ref="{http://www.opengis.net/wps/2.0}DataDescription"/&gt;
 *         &lt;element name="Input" type="{http://www.opengis.net/wps/2.0}InputDescriptionType" maxOccurs="unbounded"/&gt;
 *       &lt;/choice&gt;
 *       &lt;attGroup ref="{http://www.w3.org/2001/XMLSchema}occurs"/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InputDescriptionType", propOrder = {
        "dataDescription",
        "input"
})
public class InputDescriptionType
        extends DescriptionType {

    @XmlElementRef(name = "DataDescription", namespace = "http://www.opengis.net/wps/2.0", type = JAXBElement.class, required = false)
    protected JAXBElement<? extends DataDescriptionType> dataDescription;
    @XmlElement(name = "Input")
    protected List<InputDescriptionType> input;
    @XmlAttribute(name = "minOccurs")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger minOccurs;
    @XmlAttribute(name = "maxOccurs")
    @XmlSchemaType(name = "allNNI")
    protected String maxOccurs;

    /**
     * Gets the value of the dataDescription property.
     *
     * @return possible object is
     * {@link JAXBElement }{@code <}{@link BoundingBoxData }{@code >}
     * {@link JAXBElement }{@code <}{@link LiteralDataType }{@code >}
     * {@link JAXBElement }{@code <}{@link ComplexDataType }{@code >}
     * {@link JAXBElement }{@code <}{@link DataDescriptionType }{@code >}
     */
    public JAXBElement<? extends DataDescriptionType> getDataDescription() {
        return dataDescription;
    }

    /**
     * Sets the value of the dataDescription property.
     *
     * @param value allowed object is
     *              {@link JAXBElement }{@code <}{@link BoundingBoxData }{@code >}
     *              {@link JAXBElement }{@code <}{@link LiteralDataType }{@code >}
     *              {@link JAXBElement }{@code <}{@link ComplexDataType }{@code >}
     *              {@link JAXBElement }{@code <}{@link DataDescriptionType }{@code >}
     */
    public void setDataDescription(JAXBElement<? extends DataDescriptionType> value) {
        this.dataDescription = value;
    }

    public boolean isSetDataDescription() {
        return (this.dataDescription != null);
    }

    /**
     * Gets the value of the input property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the input property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInput().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InputDescriptionType }
     */
    public List<InputDescriptionType> getInput() {
        if (input == null) {
            input = new ArrayList<InputDescriptionType>();
        }
        return this.input;
    }

    public boolean isSetInput() {
        return ((this.input != null) && (!this.input.isEmpty()));
    }

    public void unsetInput() {
        this.input = null;
    }

    /**
     * Gets the value of the minOccurs property.
     *
     * @return possible object is
     * {@link BigInteger }
     */
    public BigInteger getMinOccurs() {
        if (minOccurs == null) {
            return new BigInteger("1");
        } else {
            return minOccurs;
        }
    }

    /**
     * Sets the value of the minOccurs property.
     *
     * @param value allowed object is
     *              {@link BigInteger }
     */
    public void setMinOccurs(BigInteger value) {
        this.minOccurs = value;
    }

    public boolean isSetMinOccurs() {
        return (this.minOccurs != null);
    }

    /**
     * Gets the value of the maxOccurs property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMaxOccurs() {
        if (maxOccurs == null) {
            return "1";
        } else {
            return maxOccurs;
        }
    }

    /**
     * Sets the value of the maxOccurs property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMaxOccurs(String value) {
        this.maxOccurs = value;
    }

    public boolean isSetMaxOccurs() {
        return (this.maxOccurs != null);
    }

}
