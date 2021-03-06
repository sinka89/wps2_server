//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.wps._2;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This structure contains information elements to supply input data for process execution.
 *
 *
 * <p>Java class for DataInputType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DataInputType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element ref="{http://www.opengis.net/wps/2.0}Data"/&gt;
 *           &lt;element ref="{http://www.opengis.net/wps/2.0}Reference"/&gt;
 *           &lt;element name="Input" type="{http://www.opengis.net/wps/2.0}DataInputType" maxOccurs="unbounded"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataInputType", propOrder = {
        "data",
        "reference",
        "input"
})
public class DataInputType {

    @XmlElement(name = "Data")
    protected Data data;
    @XmlElement(name = "Reference")
    protected ReferenceType reference;
    @XmlElement(name = "Input")
    protected List<DataInputType> input;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String id;

    /**
     * Gets the value of the data property.
     *
     * @return possible object is
     * {@link Data }
     */
    public Data getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     *
     * @param value allowed object is
     *              {@link Data }
     */
    public void setData(Data value) {
        this.data = value;
    }

    public boolean isSetData() {
        return (this.data != null);
    }

    /**
     * Gets the value of the reference property.
     *
     * @return possible object is
     * {@link ReferenceType }
     */
    public ReferenceType getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     *
     * @param value allowed object is
     *              {@link ReferenceType }
     */
    public void setReference(ReferenceType value) {
        this.reference = value;
    }

    public boolean isSetReference() {
        return (this.reference != null);
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
     * {@link DataInputType }
     */
    public List<DataInputType> getInput() {
        if (input == null) {
            input = new ArrayList<DataInputType>();
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
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id != null);
    }

}
