//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.12 at 07:05:08 PM BST 
//


package org.savara.activity.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.savara.activity.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Activity_QNAME = new QName("http://www.savara.org/activity", "activity");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.savara.activity.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CorrelationKey }
     * 
     */
    public CorrelationKey createCorrelationKey() {
        return new CorrelationKey();
    }

    /**
     * Create an instance of {@link ComponentActivity }
     * 
     */
    public ComponentActivity createComponentActivity() {
        return new ComponentActivity();
    }

    /**
     * Create an instance of {@link Context }
     * 
     */
    public Context createContext() {
        return new Context();
    }

    /**
     * Create an instance of {@link MessageParameter }
     * 
     */
    public MessageParameter createMessageParameter() {
        return new MessageParameter();
    }

    /**
     * Create an instance of {@link InteractionActivity }
     * 
     */
    public InteractionActivity createInteractionActivity() {
        return new InteractionActivity();
    }

    /**
     * Create an instance of {@link Correlation }
     * 
     */
    public Correlation createCorrelation() {
        return new Correlation();
    }

    /**
     * Create an instance of {@link Activity }
     * 
     */
    public Activity createActivity() {
        return new Activity();
    }

    /**
     * Create an instance of {@link ProtocolAnalysis }
     * 
     */
    public ProtocolAnalysis createProtocolAnalysis() {
        return new ProtocolAnalysis();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Activity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.savara.org/activity", name = "activity")
    public JAXBElement<Activity> createActivity(Activity value) {
        return new JAXBElement<Activity>(_Activity_QNAME, Activity.class, null, value);
    }

}
