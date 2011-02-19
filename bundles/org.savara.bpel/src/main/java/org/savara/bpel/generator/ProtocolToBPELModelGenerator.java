/*
 * Copyright 2005-8 Pi4 Technologies Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Change History:
 * 24 Jul 2008 : Initial version created by gary
 */
package org.savara.bpel.generator;

import org.savara.bpel.BPELDefinitions;
import org.savara.bpel.model.TProcess;
import org.savara.bpel.model.change.BPELModelChangeContext;
import org.savara.bpel.util.BPELModelUtil;
import org.savara.common.model.annotation.Annotation;
import org.savara.common.model.annotation.AnnotationDefinitions;
import org.savara.common.model.generator.ModelGenerator;
import org.savara.contract.model.Contract;
import org.savara.protocol.contract.generator.ContractGenerator;
import org.savara.protocol.contract.generator.ContractGeneratorFactory;
import org.savara.protocol.model.change.ModelChangeUtils;
import org.scribble.common.resource.ResourceLocator;
import org.scribble.common.logging.CachedJournal;
import org.scribble.common.logging.Journal;
import org.scribble.protocol.ProtocolDefinitions;
import org.scribble.protocol.model.*;

/**
 * This class represents the Protocol to BPEL implementation of the model
 * generator interface.
 */
public class ProtocolToBPELModelGenerator implements ModelGenerator {

	public boolean isSupported(String sourceType, String targetType) {
		return(sourceType.equals(ProtocolDefinitions.PROTOCOL_TYPE) &&
							targetType.equals(BPELDefinitions.BPEL_TYPE));
	}

	public Object generate(Object source, Journal journal, ResourceLocator locator) {
		BPELModelChangeContext context=
			new BPELModelChangeContext(null, new CachedJournal());
		ProtocolModel pm=(ProtocolModel)source;
		
		// SAVARA-175:
		// Add namespace prefix mapping to a BPEL process defined in text and
		// deserialize the process to create the initial model. This is the only
		// way to enable the namespace prefix mapping info to be associated with
		// the top level element in the exported text representation, without
		// having to resort to using internal Sun classes that change between
		// JDK versions.
		String process="<process xmlns=\"http://docs.oasis-open.org/wsbpel/2.0/process/executable\" ";
		
		java.util.Map<String, String> prefixes=
			new java.util.HashMap<String, String>();
	
		java.util.List<Annotation> list=
				AnnotationDefinitions.getAnnotations(pm.getProtocol().getAnnotations(),
				AnnotationDefinitions.TYPE);
	
		for (Annotation annotation : list) {
			if (annotation.getProperties().containsKey(AnnotationDefinitions.NAMESPACE_PROPERTY) &&
					annotation.getProperties().containsKey(AnnotationDefinitions.PREFIX_PROPERTY)) {
				prefixes.put((String)annotation.getProperties().get(AnnotationDefinitions.NAMESPACE_PROPERTY),
						(String)annotation.getProperties().get(AnnotationDefinitions.PREFIX_PROPERTY));
				
				process += "xmlns:"+(String)annotation.getProperties().get(AnnotationDefinitions.PREFIX_PROPERTY)+
						"=\""+(String)annotation.getProperties().get(AnnotationDefinitions.NAMESPACE_PROPERTY)+"\" ";
			}
		}
		
		process += "/>";
		
		// Create BPEL model
		TProcess bpel=null;
		
		try {
			java.io.ByteArrayInputStream io=new java.io.ByteArrayInputStream(process.getBytes());
			
			bpel = BPELModelUtil.deserialize(io);
			
			io.close();
		} catch(Exception e) {
			journal.error("Failed to create initial BPEL process", null);
		}
		
		context.setParent(bpel);
		
		ProtocolModel bpelModel=new ProtocolModel();
		bpelModel.getProperties().put(BPELDefinitions.BPEL_MODEL_PROPERTY, bpel);
		
		context.insert(bpelModel, pm, null);
		
		return(bpel);
	}
	
}
