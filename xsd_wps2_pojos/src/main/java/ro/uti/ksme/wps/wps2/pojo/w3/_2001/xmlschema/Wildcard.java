//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.w3._2001.xmlschema;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for wildcard complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="wildcard"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}annotated"&gt;
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}namespaceList" default="##any" /&gt;
 *       &lt;attribute name="processContents" default="strict"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *             &lt;enumeration value="skip"/&gt;
 *             &lt;enumeration value="lax"/&gt;
 *             &lt;enumeration value="strict"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;anyAttribute processContents='lax' namespace='##other'/&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wildcard")
@XmlSeeAlso({
        Any.class
})
public class Wildcard
        extends Annotated {

    @XmlAttribute(name = "namespace")
    @XmlSchemaType(name = "namespaceList")
    protected List<String> namespace;
    @XmlAttribute(name = "processContents")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String processContents;

    /**
     * Gets the value of the namespace property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the namespace property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNamespace().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getNamespace() {
        if (namespace == null) {
            namespace = new ArrayList<String>();
        }
        return this.namespace;
    }

    public boolean isSetNamespace() {
        return ((this.namespace != null) && (!this.namespace.isEmpty()));
    }

    public void unsetNamespace() {
        this.namespace = null;
    }

    /**
     * Gets the value of the processContents property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getProcessContents() {
        if (processContents == null) {
            return "strict";
        } else {
            return processContents;
        }
    }

    /**
     * Sets the value of the processContents property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProcessContents(String value) {
        this.processContents = value;
    }

    public boolean isSetProcessContents() {
        return (this.processContents != null);
    }

}
