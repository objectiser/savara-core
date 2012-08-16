package org.savara.examples.store;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.4.0
 * 2012-08-10T09:46:11.535+01:00
 * Generated source version: 2.4.0
 * 
 */
 
@WebService(targetNamespace = "http://www.savara.org/examples/Store", name = "Store")
@XmlSeeAlso({org.jboss.examples.store.ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Store {

    @WebResult(name = "BuyConfirmed", targetNamespace = "http://www.jboss.org/examples/store", partName = "content")
    @WebMethod(action = "http://www.savara.org/examples/Store/buy")
    public org.jboss.examples.store.BuyConfirmedType buy(
        @WebParam(partName = "content", name = "BuyRequest", targetNamespace = "http://www.jboss.org/examples/store")
        org.jboss.examples.store.BuyRequestType content
    ) throws AccountNotFoundFault
    		//, InsufficientCreditFault
    		;
}
