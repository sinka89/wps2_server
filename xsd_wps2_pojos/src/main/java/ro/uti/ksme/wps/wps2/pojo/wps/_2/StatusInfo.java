//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.wps._2;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.opengis.net/wps/2.0}JobID"/&gt;
 *         &lt;element name="Status"&gt;
 *           &lt;simpleType&gt;
 *             &lt;union&gt;
 *               &lt;simpleType&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                   &lt;enumeration value="Succeeded"/&gt;
 *                   &lt;enumeration value="Failed"/&gt;
 *                   &lt;enumeration value="Accepted"/&gt;
 *                   &lt;enumeration value="Running"/&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/simpleType&gt;
 *               &lt;simpleType&gt;
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                 &lt;/restriction&gt;
 *               &lt;/simpleType&gt;
 *             &lt;/union&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element ref="{http://www.opengis.net/wps/2.0}ExpirationDate" minOccurs="0"/&gt;
 *         &lt;element name="EstimatedCompletion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="NextPoll" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="PercentCompleted" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;minInclusive value="0"/&gt;
 *               &lt;maxInclusive value="100"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "jobID",
        "status",
        "expirationDate",
        "estimatedCompletion",
        "nextPoll",
        "percentCompleted"
})
@XmlRootElement(name = "StatusInfo")
public class StatusInfo {

    @XmlElement(name = "JobID", required = true)
    protected String jobID;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "ExpirationDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expirationDate;
    @XmlElement(name = "EstimatedCompletion")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar estimatedCompletion;
    @XmlElement(name = "NextPoll")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar nextPoll;
    @XmlElement(name = "PercentCompleted")
    protected Integer percentCompleted;

    /**
     * Gets the value of the jobID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getJobID() {
        return jobID;
    }

    /**
     * Sets the value of the jobID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setJobID(String value) {
        this.jobID = value;
    }

    public boolean isSetJobID() {
        return (this.jobID != null);
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status != null);
    }

    /**
     * Gets the value of the expirationDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setExpirationDate(XMLGregorianCalendar value) {
        this.expirationDate = value;
    }

    public boolean isSetExpirationDate() {
        return (this.expirationDate != null);
    }

    /**
     * Gets the value of the estimatedCompletion property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getEstimatedCompletion() {
        return estimatedCompletion;
    }

    /**
     * Sets the value of the estimatedCompletion property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setEstimatedCompletion(XMLGregorianCalendar value) {
        this.estimatedCompletion = value;
    }

    public boolean isSetEstimatedCompletion() {
        return (this.estimatedCompletion != null);
    }

    /**
     * Gets the value of the nextPoll property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getNextPoll() {
        return nextPoll;
    }

    /**
     * Sets the value of the nextPoll property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setNextPoll(XMLGregorianCalendar value) {
        this.nextPoll = value;
    }

    public boolean isSetNextPoll() {
        return (this.nextPoll != null);
    }

    /**
     * Gets the value of the percentCompleted property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getPercentCompleted() {
        return percentCompleted;
    }

    /**
     * Sets the value of the percentCompleted property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setPercentCompleted(Integer value) {
        this.percentCompleted = value;
    }

    public boolean isSetPercentCompleted() {
        return (this.percentCompleted != null);
    }

}
