//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.11.25 at 01:33:12 PM EET 
//


package ro.uti.ksme.wps.wps2.pojo.wps._2;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengis.net/wps/2.0}RequestBaseType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.opengis.net/wps/2.0}JobID"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "jobID"
})
@XmlRootElement(name = "GetStatus")
public class GetStatus
        extends RequestBaseType {

    @XmlElement(name = "JobID", required = true)
    protected String jobID;

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

}
