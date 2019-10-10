//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.08 at 01:22:16 PM EEST 
//


package ro.uti.ksme.wps.wps2_server.pojo.wps._2;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for LiteralDataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LiteralDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengis.net/wps/2.0}DataDescriptionType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LiteralDataDomain" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;extension base="{http://www.opengis.net/wps/2.0}LiteralDataDomainType"&gt;
 *                 &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LiteralDataType", propOrder = {
        "literalDataDomain"
})
public class LiteralDataType
        extends DataDescriptionType {

    @XmlElement(name = "LiteralDataDomain", namespace = "", required = true)
    protected List<LiteralDataType.LiteralDataDomain> literalDataDomain;

    /**
     * Gets the value of the literalDataDomain property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the literalDataDomain property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLiteralDataDomain().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LiteralDataType.LiteralDataDomain }
     */
    public List<LiteralDataType.LiteralDataDomain> getLiteralDataDomain() {
        if (literalDataDomain == null) {
            literalDataDomain = new ArrayList<LiteralDataType.LiteralDataDomain>();
        }
        return this.literalDataDomain;
    }

    public boolean isSetLiteralDataDomain() {
        return ((this.literalDataDomain != null) && (!this.literalDataDomain.isEmpty()));
    }

    public void unsetLiteralDataDomain() {
        this.literalDataDomain = null;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;extension base="{http://www.opengis.net/wps/2.0}LiteralDataDomainType"&gt;
     *       &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *     &lt;/extension&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class LiteralDataDomain
            extends LiteralDataDomainType {

        @XmlAttribute(name = "default")
        protected Boolean _default;

        /**
         * Gets the value of the default property.
         *
         * @return possible object is
         * {@link Boolean }
         */
        public boolean isDefault() {
            return _default;
        }

        /**
         * Sets the value of the default property.
         *
         * @param value allowed object is
         *              {@link Boolean }
         */
        public void setDefault(boolean value) {
            this._default = value;
        }

        public boolean isSetDefault() {
            return (this._default != null);
        }

        public void unsetDefault() {
            this._default = null;
        }

    }

}
