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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tProcess complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tProcess">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}extensions" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}import" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}partnerLinks" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}messageExchanges" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}variables" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}correlationSets" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}faultHandlers" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}eventHandlers" minOccurs="0"/>
 *         &lt;group ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}activity"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="targetNamespace" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="queryLanguage" type="{http://www.w3.org/2001/XMLSchema}anyURI" default="urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0" />
 *       &lt;attribute name="expressionLanguage" type="{http://www.w3.org/2001/XMLSchema}anyURI" default="urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0" />
 *       &lt;attribute name="suppressJoinFailure" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tBoolean" default="no" />
 *       &lt;attribute name="exitOnStandardFault" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tBoolean" default="no" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tProcess", propOrder = {
    "extensions",
    "_import",
    "partnerLinks",
    "messageExchanges",
    "variables",
    "correlationSets",
    "faultHandlers",
    "eventHandlers",
    "assign",
    "compensate",
    "compensateScope",
    "empty",
    "exit",
    "extensionActivity",
    "flow",
    "forEach",
    "_if",
    "invoke",
    "pick",
    "receive",
    "repeatUntil",
    "reply",
    "rethrow",
    "scope",
    "sequence",
    "_throw",
    "validate",
    "wait",
    "_while"
})
public class TProcess
    extends TExtensibleElements
{

    protected TExtensions extensions;
    @XmlElement(name = "import")
    protected List<TImport> _import;
    protected TPartnerLinks partnerLinks;
    protected TMessageExchanges messageExchanges;
    protected TVariables variables;
    protected TCorrelationSets correlationSets;
    protected TFaultHandlers faultHandlers;
    protected TEventHandlers eventHandlers;
    protected TAssign assign;
    protected TCompensate compensate;
    protected TCompensateScope compensateScope;
    protected TEmpty empty;
    protected TExit exit;
    protected TExtensionActivity extensionActivity;
    protected TFlow flow;
    protected TForEach forEach;
    @XmlElement(name = "if")
    protected TIf _if;
    protected TInvoke invoke;
    protected TPick pick;
    protected TReceive receive;
    protected TRepeatUntil repeatUntil;
    protected TReply reply;
    protected TRethrow rethrow;
    protected TScope scope;
    protected TSequence sequence;
    @XmlElement(name = "throw")
    protected TThrow _throw;
    protected TValidate validate;
    protected TWait wait;
    @XmlElement(name = "while")
    protected TWhile _while;
    @XmlAttribute(name = "name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlAttribute(name = "targetNamespace", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String targetNamespace;
    @XmlAttribute(name = "queryLanguage")
    @XmlSchemaType(name = "anyURI")
    protected String queryLanguage;
    @XmlAttribute(name = "expressionLanguage")
    @XmlSchemaType(name = "anyURI")
    protected String expressionLanguage;
    @XmlAttribute(name = "suppressJoinFailure")
    protected TBoolean suppressJoinFailure;
    @XmlAttribute(name = "exitOnStandardFault")
    protected TBoolean exitOnStandardFault;

    /**
     * Gets the value of the extensions property.
     * 
     * @return
     *     possible object is
     *     {@link TExtensions }
     *     
     */
    public TExtensions getExtensions() {
        return extensions;
    }

    /**
     * Sets the value of the extensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtensions }
     *     
     */
    public void setExtensions(TExtensions value) {
        this.extensions = value;
    }

    /**
     * Gets the value of the import property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the import property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TImport }
     * 
     * 
     */
    public List<TImport> getImport() {
        if (_import == null) {
            _import = new ArrayList<TImport>();
        }
        return this._import;
    }

    /**
     * Gets the value of the partnerLinks property.
     * 
     * @return
     *     possible object is
     *     {@link TPartnerLinks }
     *     
     */
    public TPartnerLinks getPartnerLinks() {
        return partnerLinks;
    }

    /**
     * Sets the value of the partnerLinks property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPartnerLinks }
     *     
     */
    public void setPartnerLinks(TPartnerLinks value) {
        this.partnerLinks = value;
    }

    /**
     * Gets the value of the messageExchanges property.
     * 
     * @return
     *     possible object is
     *     {@link TMessageExchanges }
     *     
     */
    public TMessageExchanges getMessageExchanges() {
        return messageExchanges;
    }

    /**
     * Sets the value of the messageExchanges property.
     * 
     * @param value
     *     allowed object is
     *     {@link TMessageExchanges }
     *     
     */
    public void setMessageExchanges(TMessageExchanges value) {
        this.messageExchanges = value;
    }

    /**
     * Gets the value of the variables property.
     * 
     * @return
     *     possible object is
     *     {@link TVariables }
     *     
     */
    public TVariables getVariables() {
        return variables;
    }

    /**
     * Sets the value of the variables property.
     * 
     * @param value
     *     allowed object is
     *     {@link TVariables }
     *     
     */
    public void setVariables(TVariables value) {
        this.variables = value;
    }

    /**
     * Gets the value of the correlationSets property.
     * 
     * @return
     *     possible object is
     *     {@link TCorrelationSets }
     *     
     */
    public TCorrelationSets getCorrelationSets() {
        return correlationSets;
    }

    /**
     * Sets the value of the correlationSets property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCorrelationSets }
     *     
     */
    public void setCorrelationSets(TCorrelationSets value) {
        this.correlationSets = value;
    }

    /**
     * Gets the value of the faultHandlers property.
     * 
     * @return
     *     possible object is
     *     {@link TFaultHandlers }
     *     
     */
    public TFaultHandlers getFaultHandlers() {
        return faultHandlers;
    }

    /**
     * Sets the value of the faultHandlers property.
     * 
     * @param value
     *     allowed object is
     *     {@link TFaultHandlers }
     *     
     */
    public void setFaultHandlers(TFaultHandlers value) {
        this.faultHandlers = value;
    }

    /**
     * Gets the value of the eventHandlers property.
     * 
     * @return
     *     possible object is
     *     {@link TEventHandlers }
     *     
     */
    public TEventHandlers getEventHandlers() {
        return eventHandlers;
    }

    /**
     * Sets the value of the eventHandlers property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEventHandlers }
     *     
     */
    public void setEventHandlers(TEventHandlers value) {
        this.eventHandlers = value;
    }

    /**
     * Gets the value of the assign property.
     * 
     * @return
     *     possible object is
     *     {@link TAssign }
     *     
     */
    public TAssign getAssign() {
        return assign;
    }

    /**
     * Sets the value of the assign property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAssign }
     *     
     */
    public void setAssign(TAssign value) {
        this.assign = value;
    }

    /**
     * Gets the value of the compensate property.
     * 
     * @return
     *     possible object is
     *     {@link TCompensate }
     *     
     */
    public TCompensate getCompensate() {
        return compensate;
    }

    /**
     * Sets the value of the compensate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCompensate }
     *     
     */
    public void setCompensate(TCompensate value) {
        this.compensate = value;
    }

    /**
     * Gets the value of the compensateScope property.
     * 
     * @return
     *     possible object is
     *     {@link TCompensateScope }
     *     
     */
    public TCompensateScope getCompensateScope() {
        return compensateScope;
    }

    /**
     * Sets the value of the compensateScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCompensateScope }
     *     
     */
    public void setCompensateScope(TCompensateScope value) {
        this.compensateScope = value;
    }

    /**
     * Gets the value of the empty property.
     * 
     * @return
     *     possible object is
     *     {@link TEmpty }
     *     
     */
    public TEmpty getEmpty() {
        return empty;
    }

    /**
     * Sets the value of the empty property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEmpty }
     *     
     */
    public void setEmpty(TEmpty value) {
        this.empty = value;
    }

    /**
     * Gets the value of the exit property.
     * 
     * @return
     *     possible object is
     *     {@link TExit }
     *     
     */
    public TExit getExit() {
        return exit;
    }

    /**
     * Sets the value of the exit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExit }
     *     
     */
    public void setExit(TExit value) {
        this.exit = value;
    }

    /**
     * Gets the value of the extensionActivity property.
     * 
     * @return
     *     possible object is
     *     {@link TExtensionActivity }
     *     
     */
    public TExtensionActivity getExtensionActivity() {
        return extensionActivity;
    }

    /**
     * Sets the value of the extensionActivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtensionActivity }
     *     
     */
    public void setExtensionActivity(TExtensionActivity value) {
        this.extensionActivity = value;
    }

    /**
     * Gets the value of the flow property.
     * 
     * @return
     *     possible object is
     *     {@link TFlow }
     *     
     */
    public TFlow getFlow() {
        return flow;
    }

    /**
     * Sets the value of the flow property.
     * 
     * @param value
     *     allowed object is
     *     {@link TFlow }
     *     
     */
    public void setFlow(TFlow value) {
        this.flow = value;
    }

    /**
     * Gets the value of the forEach property.
     * 
     * @return
     *     possible object is
     *     {@link TForEach }
     *     
     */
    public TForEach getForEach() {
        return forEach;
    }

    /**
     * Sets the value of the forEach property.
     * 
     * @param value
     *     allowed object is
     *     {@link TForEach }
     *     
     */
    public void setForEach(TForEach value) {
        this.forEach = value;
    }

    /**
     * Gets the value of the if property.
     * 
     * @return
     *     possible object is
     *     {@link TIf }
     *     
     */
    public TIf getIf() {
        return _if;
    }

    /**
     * Sets the value of the if property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIf }
     *     
     */
    public void setIf(TIf value) {
        this._if = value;
    }

    /**
     * Gets the value of the invoke property.
     * 
     * @return
     *     possible object is
     *     {@link TInvoke }
     *     
     */
    public TInvoke getInvoke() {
        return invoke;
    }

    /**
     * Sets the value of the invoke property.
     * 
     * @param value
     *     allowed object is
     *     {@link TInvoke }
     *     
     */
    public void setInvoke(TInvoke value) {
        this.invoke = value;
    }

    /**
     * Gets the value of the pick property.
     * 
     * @return
     *     possible object is
     *     {@link TPick }
     *     
     */
    public TPick getPick() {
        return pick;
    }

    /**
     * Sets the value of the pick property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPick }
     *     
     */
    public void setPick(TPick value) {
        this.pick = value;
    }

    /**
     * Gets the value of the receive property.
     * 
     * @return
     *     possible object is
     *     {@link TReceive }
     *     
     */
    public TReceive getReceive() {
        return receive;
    }

    /**
     * Sets the value of the receive property.
     * 
     * @param value
     *     allowed object is
     *     {@link TReceive }
     *     
     */
    public void setReceive(TReceive value) {
        this.receive = value;
    }

    /**
     * Gets the value of the repeatUntil property.
     * 
     * @return
     *     possible object is
     *     {@link TRepeatUntil }
     *     
     */
    public TRepeatUntil getRepeatUntil() {
        return repeatUntil;
    }

    /**
     * Sets the value of the repeatUntil property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRepeatUntil }
     *     
     */
    public void setRepeatUntil(TRepeatUntil value) {
        this.repeatUntil = value;
    }

    /**
     * Gets the value of the reply property.
     * 
     * @return
     *     possible object is
     *     {@link TReply }
     *     
     */
    public TReply getReply() {
        return reply;
    }

    /**
     * Sets the value of the reply property.
     * 
     * @param value
     *     allowed object is
     *     {@link TReply }
     *     
     */
    public void setReply(TReply value) {
        this.reply = value;
    }

    /**
     * Gets the value of the rethrow property.
     * 
     * @return
     *     possible object is
     *     {@link TRethrow }
     *     
     */
    public TRethrow getRethrow() {
        return rethrow;
    }

    /**
     * Sets the value of the rethrow property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRethrow }
     *     
     */
    public void setRethrow(TRethrow value) {
        this.rethrow = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link TScope }
     *     
     */
    public TScope getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link TScope }
     *     
     */
    public void setScope(TScope value) {
        this.scope = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link TSequence }
     *     
     */
    public TSequence getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSequence }
     *     
     */
    public void setSequence(TSequence value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the throw property.
     * 
     * @return
     *     possible object is
     *     {@link TThrow }
     *     
     */
    public TThrow getThrow() {
        return _throw;
    }

    /**
     * Sets the value of the throw property.
     * 
     * @param value
     *     allowed object is
     *     {@link TThrow }
     *     
     */
    public void setThrow(TThrow value) {
        this._throw = value;
    }

    /**
     * Gets the value of the validate property.
     * 
     * @return
     *     possible object is
     *     {@link TValidate }
     *     
     */
    public TValidate getValidate() {
        return validate;
    }

    /**
     * Sets the value of the validate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TValidate }
     *     
     */
    public void setValidate(TValidate value) {
        this.validate = value;
    }

    /**
     * Gets the value of the wait property.
     * 
     * @return
     *     possible object is
     *     {@link TWait }
     *     
     */
    public TWait getWait() {
        return wait;
    }

    /**
     * Sets the value of the wait property.
     * 
     * @param value
     *     allowed object is
     *     {@link TWait }
     *     
     */
    public void setWait(TWait value) {
        this.wait = value;
    }

    /**
     * Gets the value of the while property.
     * 
     * @return
     *     possible object is
     *     {@link TWhile }
     *     
     */
    public TWhile getWhile() {
        return _while;
    }

    /**
     * Sets the value of the while property.
     * 
     * @param value
     *     allowed object is
     *     {@link TWhile }
     *     
     */
    public void setWhile(TWhile value) {
        this._while = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the targetNamespace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * Sets the value of the targetNamespace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    /**
     * Gets the value of the queryLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryLanguage() {
        if (queryLanguage == null) {
            return "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";
        } else {
            return queryLanguage;
        }
    }

    /**
     * Sets the value of the queryLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryLanguage(String value) {
        this.queryLanguage = value;
    }

    /**
     * Gets the value of the expressionLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressionLanguage() {
        if (expressionLanguage == null) {
            return "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";
        } else {
            return expressionLanguage;
        }
    }

    /**
     * Sets the value of the expressionLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressionLanguage(String value) {
        this.expressionLanguage = value;
    }

    /**
     * Gets the value of the suppressJoinFailure property.
     * 
     * @return
     *     possible object is
     *     {@link TBoolean }
     *     
     */
    public TBoolean getSuppressJoinFailure() {
        if (suppressJoinFailure == null) {
            return TBoolean.NO;
        } else {
            return suppressJoinFailure;
        }
    }

    /**
     * Sets the value of the suppressJoinFailure property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBoolean }
     *     
     */
    public void setSuppressJoinFailure(TBoolean value) {
        this.suppressJoinFailure = value;
    }

    /**
     * Gets the value of the exitOnStandardFault property.
     * 
     * @return
     *     possible object is
     *     {@link TBoolean }
     *     
     */
    public TBoolean getExitOnStandardFault() {
        if (exitOnStandardFault == null) {
            return TBoolean.NO;
        } else {
            return exitOnStandardFault;
        }
    }

    /**
     * Sets the value of the exitOnStandardFault property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBoolean }
     *     
     */
    public void setExitOnStandardFault(TBoolean value) {
        this.exitOnStandardFault = value;
    }

}