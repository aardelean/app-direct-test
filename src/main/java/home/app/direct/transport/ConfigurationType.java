//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7-b41 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.06 at 11:44:07 AM CET 
//


package home.app.direct.transport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for configurationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configurationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entry" type="{}entryType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configurationType", propOrder = {
    "entry"
})
public class ConfigurationType {

    @XmlElement(required = true)
    protected EntryType entry;

    /**
     * Gets the value of the entry property.
     * 
     * @return
     *     possible object is
     *     {@link EntryType }
     *     
     */
    public EntryType getEntry() {
        return entry;
    }

    /**
     * Sets the value of the entry property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryType }
     *     
     */
    public void setEntry(EntryType value) {
        this.entry = value;
    }

}
