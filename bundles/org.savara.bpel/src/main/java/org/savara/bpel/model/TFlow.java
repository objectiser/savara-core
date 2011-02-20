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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tFlow complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tFlow">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tActivity">
 *       &lt;sequence>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}links" minOccurs="0"/>
 *         &lt;group ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}activity" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tFlow", propOrder = {
    "links",
    "activity"
})
public class TFlow
    extends TActivity
{

    protected TLinks links;
    @XmlElements({
        @XmlElement(name = "while", type = TWhile.class),
        @XmlElement(name = "assign", type = TAssign.class),
        @XmlElement(name = "exit", type = TExit.class),
        @XmlElement(name = "receive", type = TReceive.class),
        @XmlElement(name = "sequence", type = TSequence.class),
        @XmlElement(name = "pick", type = TPick.class),
        @XmlElement(name = "compensateScope", type = TCompensateScope.class),
        @XmlElement(name = "wait", type = TWait.class),
        @XmlElement(name = "if", type = TIf.class),
        @XmlElement(name = "forEach", type = TForEach.class),
        @XmlElement(name = "invoke", type = TInvoke.class),
        @XmlElement(name = "compensate", type = TCompensate.class),
        @XmlElement(name = "extensionActivity", type = TExtensionActivity.class),
        @XmlElement(name = "rethrow", type = TRethrow.class),
        @XmlElement(name = "flow", type = TFlow.class),
        @XmlElement(name = "reply", type = TReply.class),
        @XmlElement(name = "repeatUntil", type = TRepeatUntil.class),
        @XmlElement(name = "validate", type = TValidate.class),
        @XmlElement(name = "throw", type = TThrow.class),
        @XmlElement(name = "scope", type = TScope.class),
        @XmlElement(name = "empty", type = TEmpty.class)
    })
    protected List<Object> activity;

    /**
     * Gets the value of the links property.
     * 
     * @return
     *     possible object is
     *     {@link TLinks }
     *     
     */
    public TLinks getLinks() {
        return links;
    }

    /**
     * Sets the value of the links property.
     * 
     * @param value
     *     allowed object is
     *     {@link TLinks }
     *     
     */
    public void setLinks(TLinks value) {
        this.links = value;
    }

    /**
     * Gets the value of the activity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the activity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActivity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TWhile }
     * {@link TAssign }
     * {@link TExit }
     * {@link TReceive }
     * {@link TSequence }
     * {@link TPick }
     * {@link TCompensateScope }
     * {@link TWait }
     * {@link TIf }
     * {@link TForEach }
     * {@link TInvoke }
     * {@link TCompensate }
     * {@link TExtensionActivity }
     * {@link TRethrow }
     * {@link TFlow }
     * {@link TReply }
     * {@link TRepeatUntil }
     * {@link TValidate }
     * {@link TThrow }
     * {@link TScope }
     * {@link TEmpty }
     * 
     * 
     */
    public List<Object> getActivity() {
        if (activity == null) {
            activity = new ArrayList<Object>();
        }
        return this.activity;
    }

}