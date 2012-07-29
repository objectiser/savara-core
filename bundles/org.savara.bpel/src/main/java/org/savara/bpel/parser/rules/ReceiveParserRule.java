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
package org.savara.bpel.parser.rules;

import java.util.List;

import org.savara.bpel.model.TReceive;
import org.savara.bpel.model.TVariable;
import org.savara.bpel.util.BPELInteractionUtil;
import org.savara.bpel.util.PartnerLinkUtil;
import org.savara.bpel.util.TypeReferenceUtil;
import org.savara.common.logging.FeedbackHandler;
import org.savara.common.model.annotation.Annotation;
import org.savara.common.model.annotation.AnnotationDefinitions;
import org.scribble.protocol.model.*;

/**
 * This class represents an unsupported (or custom) action within
 * the conversation based ESB service descriptor.
 *  
 * @author gary
 */
public class ReceiveParserRule implements ProtocolParserRule {

	public boolean isSupported(Object component) {
		return(component instanceof TReceive);
	}
		
	public void parse(ParserContext context, Object component, List<Activity> activities,
									FeedbackHandler handler) {
		TReceive elem=(TReceive)component;
		
		//getSource().setComponentURI(getURI());
		
		Interaction interaction=new Interaction();
		//interaction.derivedFrom(this);
		
		TVariable var=context.getVariable(elem.getVariable());
		
		String xmlType=BPELInteractionUtil.getXMLType(context.getProcess(), var.getMessageType(),
						context.getResourceLocator());
		
		TypeReference tref=TypeReferenceUtil.createTypeReference(xmlType, context);
		
		MessageSignature ms=new MessageSignature();
		//ms.derivedFrom(this);
		
		ms.setOperation(elem.getOperation());
		ms.getTypeReferences().add(tref);
		
		String fromRole=PartnerLinkUtil.getServerPartnerRole(elem.getPartnerLink());
		
		if (fromRole != null && fromRole.equals(context.getRole())) {
			fromRole = PartnerLinkUtil.getClientPartnerRole(elem.getPartnerLink());
		}
		
		if (fromRole != null) {
			interaction.setFromRole(new Role(fromRole));
			
			if (!context.getIntroducers().containsKey(context.getRole())) {
				context.getIntroducers().put(context.getRole(), fromRole);
			}
		}
		
		//if (context.getRole() != null) {
		//	interaction.getToRoles().add(new Role(context.getRole()));
		//}
		
		interaction.setMessageSignature(ms);
		
		Annotation annotation=new Annotation(AnnotationDefinitions.CORRELATION);
		annotation.getProperties().put(AnnotationDefinitions.REQUEST_PROPERTY,
					elem.getOperation());
		interaction.getAnnotations().add(annotation);
		
		activities.add(interaction);
	}

}
