
package org.savara.purchasing.store;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.4.0
 * 2012-09-02T18:03:56.358+01:00
 * Generated source version: 2.4.0
 * 
 */

@WebFault(name = "BuyFailed", targetNamespace = "http://www.jboss.org/examples/store")
public class InsufficientCreditFault extends Exception {
    public static final long serialVersionUID = 20120902180356L;
    
    private org.jboss.examples.store.BuyFailedType buyFailed;

    public InsufficientCreditFault() {
        super();
    }
    
    public InsufficientCreditFault(String message) {
        super(message);
    }
    
    public InsufficientCreditFault(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientCreditFault(String message, org.jboss.examples.store.BuyFailedType buyFailed) {
        super(message);
        this.buyFailed = buyFailed;
    }

    public InsufficientCreditFault(String message, org.jboss.examples.store.BuyFailedType buyFailed, Throwable cause) {
        super(message, cause);
        this.buyFailed = buyFailed;
    }

    public org.jboss.examples.store.BuyFailedType getFaultInfo() {
        return this.buyFailed;
    }
}