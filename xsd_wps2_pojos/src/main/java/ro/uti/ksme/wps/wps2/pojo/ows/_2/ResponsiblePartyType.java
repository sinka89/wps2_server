//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.ows._2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Identification of, and means of communication with,
 * person responsible for the server. At least one of IndividualName,
 * OrganisationName, or PositionName shall be included.
 *
 *
 * <p>Java class for ResponsiblePartyType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ResponsiblePartyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.opengis.net/ows/2.0}IndividualName" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.opengis.net/ows/2.0}OrganisationName" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.opengis.net/ows/2.0}PositionName" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.opengis.net/ows/2.0}ContactInfo" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.opengis.net/ows/2.0}Role"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponsiblePartyType", propOrder = {
        "individualName",
        "organisationName",
        "positionName",
        "contactInfo",
        "role"
})
public class ResponsiblePartyType {

    @XmlElement(name = "IndividualName")
    protected String individualName;
    @XmlElement(name = "OrganisationName")
    protected String organisationName;
    @XmlElement(name = "PositionName")
    protected String positionName;
    @XmlElement(name = "ContactInfo")
    protected ContactType contactInfo;
    @XmlElement(name = "Role", required = true)
    protected CodeType role;

    /**
     * Gets the value of the individualName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIndividualName() {
        return individualName;
    }

    /**
     * Sets the value of the individualName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIndividualName(String value) {
        this.individualName = value;
    }

    public boolean isSetIndividualName() {
        return (this.individualName != null);
    }

    /**
     * Gets the value of the organisationName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOrganisationName() {
        return organisationName;
    }

    /**
     * Sets the value of the organisationName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrganisationName(String value) {
        this.organisationName = value;
    }

    public boolean isSetOrganisationName() {
        return (this.organisationName != null);
    }

    /**
     * Gets the value of the positionName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPositionName() {
        return positionName;
    }

    /**
     * Sets the value of the positionName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPositionName(String value) {
        this.positionName = value;
    }

    public boolean isSetPositionName() {
        return (this.positionName != null);
    }

    /**
     * Gets the value of the contactInfo property.
     *
     * @return possible object is
     * {@link ContactType }
     */
    public ContactType getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the value of the contactInfo property.
     *
     * @param value allowed object is
     *              {@link ContactType }
     */
    public void setContactInfo(ContactType value) {
        this.contactInfo = value;
    }

    public boolean isSetContactInfo() {
        return (this.contactInfo != null);
    }

    /**
     * Gets the value of the role property.
     *
     * @return possible object is
     * {@link CodeType }
     */
    public CodeType getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param value allowed object is
     *              {@link CodeType }
     */
    public void setRole(CodeType value) {
        this.role = value;
    }

    public boolean isSetRole() {
        return (this.role != null);
    }

}
