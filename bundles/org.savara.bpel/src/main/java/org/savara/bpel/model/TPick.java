//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-146 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.10 at 11:11:33 PM GMT 
//


package org.savara.bpel.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				XSD Authors: The child element onAlarm needs to be a Local Element Declaration, 
 * 				because there is another onAlarm element defined for event handlers.
 * 			
 * 
 * <p>Java class for tPick complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tPick">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tActivity">
 *       &lt;sequence>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}onMessage" maxOccurs="unbounded"/>
 *         &lt;element name="onAlarm" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tOnAlarmPick" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="createInstance" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tBoolean" default="no" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPick", propOrder = {
    "onMessage",
    "onAlarm"
})
public class TPick
    extends TActivity
{

    @XmlElement(required = true)
    protected List<TOnMessage> onMessage;
    protected List<TOnAlarmPick> onAlarm;
    @XmlAttribute(name = "createInstance")
    protected TBoolean createInstance;

    /**
     * Gets the value of the onMessage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the onMessage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOnMessage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TOnMessage }
     * 
     * 
     */
    public List<TOnMessage> getOnMessage() {
        if (onMessage == null) {
            onMessage = new ArrayList<TOnMessage>();
        }
        return this.onMessage;
    }

    /**
     * Gets the value of the onAlarm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the onAlarm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOnAlarm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TOnAlarmPick }
     * 
     * 
     */
    public List<TOnAlarmPick> getOnAlarm() {
        if (onAlarm == null) {
            onAlarm = new ArrayList<TOnAlarmPick>();
        }
        return this.onAlarm;
    }

    /**
     * Gets the value of the createInstance property.
     * 
     * @return
     *     possible object is
     *     {@link TBoolean }
     *     
     */
    public TBoolean getCreateInstance() {
        if (createInstance == null) {
            return TBoolean.NO;
        } else {
            return createInstance;
        }
    }

    /**
     * Sets the value of the createInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBoolean }
     *     
     */
    public void setCreateInstance(TBoolean value) {
        this.createInstance = value;
    }

}
