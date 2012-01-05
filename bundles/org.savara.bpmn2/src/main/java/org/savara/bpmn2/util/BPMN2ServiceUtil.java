/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008-11, Red Hat Middleware LLC, and others contributors as indicated
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
package org.savara.bpmn2.util;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.savara.bpmn2.model.TChoreography;
import org.savara.bpmn2.model.TChoreographyTask;
import org.savara.bpmn2.model.TDefinitions;
import org.savara.bpmn2.model.TFlowElement;
import org.savara.bpmn2.model.TFlowNode;
import org.savara.bpmn2.model.TInterface;
import org.savara.bpmn2.model.TMessage;
import org.savara.bpmn2.model.TMessageFlow;
import org.savara.bpmn2.model.TOperation;
import org.savara.bpmn2.model.TParticipant;
import org.savara.bpmn2.model.TRootElement;
import org.savara.bpmn2.model.TSequenceFlow;
import org.savara.bpmn2.model.TStartEvent;

/**
 * This class provides capabilities associated with the service
 * (or interface) declarations within BPMN model.
 *
 */
public class BPMN2ServiceUtil {

	private static final String INTERFACE_NAME_SUFFIX = "Interface";
	private static final Logger LOG=Logger.getLogger(BPMN2ServiceUtil.class.getName());
	
	/**
	 * This method identifies the service interfaces (and their operations) associated
	 * with the participants defined within the supplied BPMN model. This method only
	 * creates a single interface per participant. The supplied BPMN model is not modified
	 * by this operation, the service interface details can be applied to the definition
	 * (after making any necessary changes) using the merge method.
	 * 
	 * @param defns The BPMN model
	 * @return The map of participants to interfaces
	 */
	public static java.util.Map<TParticipant,TInterface> introspect(TDefinitions defns) {
		java.util.Map<TParticipant,TInterface> ret=new java.util.HashMap<TParticipant,TInterface>();

		for (JAXBElement<? extends TRootElement> elem : defns.getRootElement()) {
			if (elem.getDeclaredType() == TChoreography.class) {
				TChoreography choreo=(TChoreography)elem.getValue();
				
				// Find initial node
				TStartEvent startEvent=null;
				
				for (JAXBElement<? extends TFlowElement> jaxb : choreo.getFlowElement()) {
					if (jaxb.getValue().getClass() == TStartEvent.class) {
						if (startEvent != null) {
							LOG.severe("Multiple start events found in choreography");
						} else {
							startEvent = (TStartEvent)jaxb.getValue();
						}
					}
				}
				
				if (startEvent != null) {
					processNode(startEvent, ret, new java.util.Vector<InteractionInfo>(),
								new ModelInfo(choreo.getParticipant(),
									choreo.getMessageFlow(), choreo.getFlowElement(),
									defns.getRootElement()));
				}
			}
		}
				
		return (ret);
	}
	
	protected static void processNode(TFlowNode node, java.util.Map<TParticipant,TInterface> intfs,
			java.util.List<InteractionInfo> ii, ModelInfo modelInfo) {
		
		// Check if node is an interaction
		if (node instanceof TChoreographyTask) {
			processChoreographyTask((TChoreographyTask)node, intfs, ii, modelInfo);
		}
		
		for (QName outgoing : node.getOutgoing()) {
			TSequenceFlow sf=(TSequenceFlow)modelInfo.getFlowElement(outgoing.getLocalPart());
			
			if (sf != null) {
				processNode((TFlowNode)sf.getTargetRef(), intfs,
						new java.util.Vector<InteractionInfo>(ii), modelInfo);
			}
		}
	}
	
	protected static void processChoreographyTask(TChoreographyTask node,
					java.util.Map<TParticipant,TInterface> intfs,
					java.util.List<InteractionInfo> ii, ModelInfo modelInfo) {
		InteractionInfo initiatingii=null;
		InteractionInfo respondingii=null;
		
		for (QName mflowQName : node.getMessageFlowRef()) {
			TMessageFlow mflow=modelInfo.getMessageFlow(mflowQName.getLocalPart());
			
			if (mflow != null) {
				TParticipant from=modelInfo.getParticipant(mflow.getSourceRef().getLocalPart());
				TParticipant to=modelInfo.getParticipant(mflow.getTargetRef().getLocalPart());
				TMessage mesg=modelInfo.getMessage(mflow.getMessageRef().getLocalPart());
				
				if (from != null && to != null && mesg != null) {
					if (mflow.getSourceRef().equals(node.getInitiatingParticipantRef())) {
						initiatingii = new InteractionInfo(from, to, mesg);
					} else {
						respondingii = new InteractionInfo(to, from, mesg);
					}
				}
			}
		}
		
		// Check if both initiating and responding interactions set
		if (initiatingii != null && respondingii != null) {
			// Assume matched pair
			storeMEP(initiatingii, respondingii, intfs);
			
		} else if (initiatingii != null) {
			// Attempt to match with previous interaction
			InteractionInfo match=null;
			for (int i=ii.size()-1; match == null && i >= 0; i--) {
				InteractionInfo cur=ii.get(i);
				
				if (cur.getFrom().equals(initiatingii.getTo()) &&
						cur.getTo().equals(initiatingii.getFrom())) {
					match = cur;		
				}
			}
			
			if (match != null) {
				storeMEP(match, initiatingii, intfs);
				
				ii.remove(match);
			} else {
			
				for (int i=ii.size()-1; match == null && i >= 0; i--) {
					InteractionInfo cur=ii.get(i);
					
					if (cur.getFrom().equals(initiatingii.getTo()) &&
							cur.getTo().equals(initiatingii.getFrom())) {
						match = cur;		
					}
				}
			
				// If previous interaction found with same from/to, then
				// record previous as one-way op
				if (match != null) {
					storeMEP(match, null, intfs);
					
					ii.remove(match);
				}

				// Record new interaction
				ii.add(initiatingii);
			}
		} else if (respondingii != null) {
			// Error, can't have just a response??
			LOG.severe("Only a response interaction has been found for ChoreographyTask: "+node);
		} else {
			// Error no interactions found on choreography task
			LOG.severe("No interactions have been found for ChoreographyTask: "+node);
		}
	}
	
	protected static void storeMEP(InteractionInfo req, InteractionInfo resp,
					java.util.Map<TParticipant,TInterface> intfs) {
		TParticipant participant=req.getTo();
		TInterface intf=intfs.get(participant);
		
		// Check if interface has been defined - if not, then create
		if (intf == null) {
			intf = new TInterface();
			intf.setId(getInterfaceName(participant));
			intf.setName(intf.getId());
			
			intfs.put(participant, intf);
		}
		
		// Find operation with request message type
		TOperation operation=null;
		
		for (TOperation op : intf.getOperation()) {
			if (op.getInMessageRef().getLocalPart().equals(req.getMessage().getId())) {
				operation = op;
				break;
			}
		}
		
		if (operation == null) {
			operation = new TOperation();
			
			String opname=getOperationName(req.getMessage());
			
			operation.setId(opname);
			operation.setName(opname);
			
			operation.setInMessageRef(new QName(null, req.getMessage().getId()));
			
			intf.getOperation().add(operation);
		}
		
		if (resp != null) {
			// Check if response message already exists on operation
			if (operation.getOutMessageRef() == null) {
				operation.setOutMessageRef(new QName(null, resp.getMessage().getId()));
			} else {
				boolean found=operation.getOutMessageRef().getLocalPart().equals(
								resp.getMessage().getId());
								
				for (int i=0; !found && i < operation.getErrorRef().size(); i++) {
					found = operation.getErrorRef().get(i).getLocalPart().equals(
										resp.getMessage().getId());
				}
				
				if (!found) {
					operation.getErrorRef().add(new QName(null, resp.getMessage().getId()));
				}
			}
		}
	}
	
	/**
	 * This method returns the default interface name associated
	 * with the supplied participant.
	 * 
	 * @param participant The participant
	 * @return The interface name
	 */
	public static String getInterfaceName(TParticipant participant) {
		return(participant.getName()+INTERFACE_NAME_SUFFIX);
	}
	
	/**
	 * This method derives an operation name from the supplied message.
	 * 
	 * @param message The message
	 * @return The operation name
	 */
	public static String getOperationName(TMessage message) {
		String ret=Character.toLowerCase(message.getName().charAt(0))+
						message.getName().substring(1);
		
		ret = ret.replaceAll("Request", "");
		ret = ret.replaceAll("Response", "");
		ret = ret.replaceAll("Req", "");
		ret = ret.replaceAll("Resp", "");
		
		return(ret);
	}
		
	/**
	 * This method merges the supplied service interface information into the supplied
	 * BPMN model. If no interfaces exist in the model, then the supplied interfaces will be
	 * added. If interfaces already exist, then it will attempt to merge the operations
	 * into the existing one or more interfaces defined per participant.
	 * 
	 * @param defns The BPMN model
	 * @param interfaces The map of participants to interfaces
	 */
	public static void merge(TDefinitions defns, java.util.Map<TParticipant,TInterface> interfaces) {
		
	}
	
	/**
	 * This class is a wrapper for the pieces of information that
	 * constitute an interaction.
	 *
	 */
	protected static class InteractionInfo {
		
		private TParticipant _from=null;
		private TParticipant _to=null;
		private TMessage _message=null;
		
		public InteractionInfo(TParticipant from, TParticipant to, TMessage message) {
			_from = from;
			_to = to;
			_message = message;
		}
		
		public TParticipant getFrom() {
			return (_from);
		}
		
		public TParticipant getTo() {
			return (_to);
		}
		
		public TMessage getMessage() {
			return (_message);
		}
	}
	
	protected static class ModelInfo {
		
		private List<TParticipant> _participants=null;
		private List<TMessageFlow> _messageFlows=null;
		private List<JAXBElement<? extends TFlowElement>> _flowElements;
		private List<JAXBElement<? extends TRootElement>> _rootElements;
		
		public ModelInfo(List<TParticipant> participants, List<TMessageFlow> messageFlows,
				List<JAXBElement<? extends TFlowElement>> flowElements,
				List<JAXBElement<? extends TRootElement>> rootElements) {
			_participants = participants;
			_messageFlows = messageFlows;
			_flowElements = flowElements;
			_rootElements = rootElements;
		}
		
		public TParticipant getParticipant(String id) {
			TParticipant ret=null;
			
			for (TParticipant part : _participants) {
				if (part.getId().equals(id)) {
					ret = part;
					break;
				}
			}
			
			return (ret);
		}
		
		public TMessageFlow getMessageFlow(String id) {
			TMessageFlow ret=null;
			
			for (TMessageFlow mflow : _messageFlows) {
				if (mflow.getId().equals(id)) {
					ret = mflow;
					break;
				}
			}
			
			return (ret);
		}
		
		public TMessage getMessage(String id) {
			return((TMessage)getRootElement(id));
		}
		
		public TRootElement getRootElement(String id) {
			TRootElement ret=null;
			
			for (JAXBElement<? extends TRootElement> fejaxb : _rootElements) {
				TRootElement fe=fejaxb.getValue();
				if (fe.getId().equals(id)) {
					ret = fe;
					break;
				}
			}
			
			return (ret);
		}
		
		public TFlowElement getFlowElement(String id) {
			TFlowElement ret=null;
			
			for (JAXBElement<? extends TFlowElement> fejaxb : _flowElements) {
				TFlowElement fe=fejaxb.getValue();
				if (fe.getId().equals(id)) {
					ret = fe;
					break;
				}
			}
			
			return (ret);
		}
		
	}
}