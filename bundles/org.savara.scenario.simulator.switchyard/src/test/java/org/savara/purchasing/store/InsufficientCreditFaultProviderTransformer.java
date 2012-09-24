/*
 * Generated by Savara.
 */
package org.savara.purchasing.store;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.switchyard.common.xml.QNameUtil;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.Transformer;

public class InsufficientCreditFaultProviderTransformer extends org.switchyard.transform.BaseTransformer<org.savara.purchasing.store.InsufficientCreditFault,String>
				implements Transformer<org.savara.purchasing.store.InsufficientCreditFault,String> {
    
	public QName getTo() {
		return (QName.valueOf("{http://www.jboss.org/examples/store}BuyFailed"));
	}
	
	public String transform(org.savara.purchasing.store.InsufficientCreditFault type) {
		Marshaller marshaller;
		JAXBContext _jaxbContext;

		try {
			_jaxbContext = JAXBContext.newInstance("org.jboss.examples.store");
		} catch (JAXBException e) {
			throw new SwitchYardException("Failed to create JAXBContext for '" + getFrom() + "'.", e);
		}

		try {
			marshaller = _jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			throw new SwitchYardException("Failed to create Marshaller for type '" + getFrom() + "'.", e);
		}

		try {
			StringWriter resultWriter = new StringWriter();
			Object javaObject = type.getFaultInfo();
			JAXBElement jaxbElement = new JAXBElement(getTo(), QNameUtil.toJavaMessageType(getFrom()), javaObject);

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(jaxbElement, resultWriter);

			return (resultWriter.toString());
		} catch (JAXBException e) {
			throw new SwitchYardException("Failed to unmarshall for type '" + getFrom() + "'.", e);
		}
	}
}