/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.savara.bpel.util;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.savara.bpel.model.*;

public class BPELModelUtil {

	public static TProcess deserialize(java.io.InputStream is) throws IOException {
		TProcess ret=null;
		
		try {
			JAXBContext context = JAXBContext.newInstance("org.savara.bpel.model",
					BPELModelUtil.class.getClassLoader());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			//note: setting schema to null will turn validator off
			//unmarshaller.setSchema(null);
			Object xmlObject = unmarshaller.unmarshal(is);
			
			if (xmlObject instanceof JAXBElement) {
				ret = (TProcess)((JAXBElement<?>)xmlObject).getValue();
			}
			
		} catch(Exception e) {
			throw new IOException("Failed to deserialize model", e);
		}
		
		return(ret);
	}
	
	public static void serialize(TProcess desc, java.io.OutputStream os,
						java.util.Map<String, String> prefixes) throws IOException {
		
		try {
			org.savara.bpel.model.ObjectFactory factory=
						new org.savara.bpel.model.ObjectFactory();
			
			JAXBContext context = JAXBContext.newInstance(TProcess.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// SAVARA-175 - it seems that to get jaxb to generate the namespace prefix
			// mapping at the top of the document, it is necessary to (1) have the TProcess
			// created with the original namespace prefix mappings, which on initial
			// marshalling will be moved on to the elements that have the prefix, and
			// then (2) reapply the prefixes by building the DOM, adding the prefix
			// namespace info, and then transforming back to text.
			if (prefixes != null) {
				java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream();
				
				marshaller.marshal(factory.createProcess(desc), baos);
				
				// Convert to DOM
				javax.xml.parsers.DocumentBuilderFactory dbfactory=
								javax.xml.parsers.DocumentBuilderFactory.newInstance();
				dbfactory.setNamespaceAware(true);
				
				org.w3c.dom.Document doc=
					dbfactory.newDocumentBuilder().parse(new java.io.ByteArrayInputStream(baos.toByteArray()));
				
				for (String ns : prefixes.keySet()) {
					String prefix=prefixes.get(ns);
					
					doc.getDocumentElement().setAttribute("xmlns:"+prefix, ns); 
				}
				
				doc.normalizeDocument();
				
				javax.xml.transform.dom.DOMSource source=new javax.xml.transform.dom.DOMSource(doc);
				javax.xml.transform.stream.StreamResult result=new javax.xml.transform.stream.StreamResult(os);
				
				javax.xml.transform.Transformer transformer=
						javax.xml.transform.TransformerFactory.newInstance().newTransformer();
				transformer.transform(source, result);
				
			} else {
				marshaller.marshal(factory.createProcess(desc), os);
			}
		} catch(Exception e) {
			throw new IOException("Failed to serialize model", e);
		}
	}
}
