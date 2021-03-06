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
package org.savara.bpmn2.generation.process;

import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.savara.bpmn2.internal.generation.BPMN2GenerationException;
import org.savara.bpmn2.internal.generation.BPMN2ModelFactory;
import org.savara.bpmn2.internal.generation.BPMN2NotationFactory;
import org.savara.bpmn2.internal.generation.components.AbstractBPMNActivity;
import org.savara.bpmn2.internal.generation.components.BPMNActivity;
import org.savara.bpmn2.internal.generation.components.BPMNDiagram;
import org.savara.bpmn2.internal.generation.components.BPMNPool;
import org.savara.bpmn2.internal.generation.components.BoundaryEvent;
import org.savara.bpmn2.internal.generation.components.ChoiceActivity;
import org.savara.bpmn2.internal.generation.components.DoActivity;
import org.savara.bpmn2.internal.generation.components.DoBlockActivity;
import org.savara.bpmn2.internal.generation.components.ForkActivity;
import org.savara.bpmn2.internal.generation.components.JoinActivity;
import org.savara.bpmn2.internal.generation.components.ParallelActivity;
import org.savara.bpmn2.internal.generation.components.ReceiveActivity;
import org.savara.bpmn2.internal.generation.components.RepeatActivity;
import org.savara.bpmn2.internal.generation.components.RunActivity;
import org.savara.bpmn2.internal.generation.components.SendActivity;
import org.savara.bpmn2.internal.generation.components.SequenceActivity;
import org.savara.bpmn2.internal.generation.components.ServiceInvocationActivity;
import org.savara.bpmn2.model.TBoundaryEvent;
import org.savara.bpmn2.model.TDefinitions;
import org.savara.bpmn2.model.TErrorEventDefinition;
import org.savara.bpmn2.model.TImport;
import org.savara.bpmn2.model.TItemDefinition;
import org.savara.bpmn2.model.TMessage;
import org.savara.bpmn2.model.TProcess;
import org.savara.bpmn2.model.TReceiveTask;
import org.savara.bpmn2.model.TSendTask;
import org.savara.bpmn2.model.TServiceTask;
import org.savara.common.logging.FeedbackHandler;
import org.savara.common.model.annotation.AnnotationDefinitions;
import org.savara.common.model.annotation.Annotation;
import org.savara.common.model.generator.ModelGenerator;
import org.savara.common.resources.ResourceLocator;
import org.savara.protocol.model.Join;
import org.savara.protocol.model.Fork;
import org.savara.protocol.model.util.InteractionUtil;
import org.scribble.protocol.model.*;
import org.scribble.protocol.util.RunUtil;

/**
 * This class represents the Protocol to BPMN2 Process implementation of the model
 * generator interface.
 */
public class ProtocolToBPMN2ProcessModelGenerator implements ModelGenerator {

	private static final String BPMN_FILE_EXTENSION = ".bpmn";
	private boolean _consecutiveIds=false;
	private boolean _messageBasedInvocation=false;
	private org.savara.bpmn2.model.ObjectFactory _objectFactory=new org.savara.bpmn2.model.ObjectFactory();
	
	private static final Logger logger=Logger.getLogger(ProtocolToBPMN2ProcessModelGenerator.class.getName());
	
	/**
	 * This method determines whether consecutive ids should be used in the model and
	 * notation. If false (default), then random unique ids will be used.
	 * 
	 * @param b Whether to use consecutive ids
	 */
	public void setUseConsecutiveIds(boolean b) {
		_consecutiveIds = b;
	}
	
	/**
	 * This method sets whether message based invocation should be used. If true
	 * then a process invoking an external service will be represented based on
	 * separate send and receive tasks (and a choice construct if faults may
	 * be received). If false, then a service task will be used, with message
	 * based boundary event definitions to represent received faults.
	 * 
	 * @param b Whether message based invocation should be used
	 */
	public void setMessageBasedInvocation(boolean b) {
		_messageBasedInvocation = b;
	}
	
	/**
	 * This method indicates whether message based invocation should be
	 * used.
	 * 
	 * @return Whether message based invocation should be used
	 */
	public boolean getMessageBasedInvocation() {
		return (_messageBasedInvocation);
	}
	
	/**
	 * This method determines whether the generator is appropriate for
	 * the specified source and target types.
	 * 
	 * @param source The source
	 * @param targetType The target type
	 * @return Whether the specified types are supported
	 */
	public boolean isSupported(Object source, String targetType) {
		return(source instanceof ProtocolModel &&
				((ProtocolModel)source).isLocated() &&
				(targetType.equals("bpmn2") || targetType.equals("bpmn")));
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map<String,Object> generate(Object source, FeedbackHandler handler,
							final ResourceLocator locator) {
		java.util.Map<String,Object> ret=null;

		if (source instanceof ProtocolModel) {
			ProtocolModel pm=(ProtocolModel)source;
			
			TDefinitions defns=new TDefinitions();
			
			// Setup prefixes
			// Obtain any namespace prefix map
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
				}
			}

			for (String ns : prefixes.keySet()) {
				String prefix=prefixes.get(ns);
				
				defns.getOtherAttributes().put(new QName(null,"xmlns:"+prefix), ns);
			}

			// Set an id on the definition - not strictly required,
			// although the BPMN2 modeler currently seems to want
			// it
			defns.setId("id-"+pm.getProtocol().getName()+"-"+pm.getProtocol().getLocatedRole());				
						
			org.savara.bpmn2.internal.generation.BPMN2ModelFactory model=
				new org.savara.bpmn2.internal.generation.BPMN2ModelFactory(defns);
			org.savara.bpmn2.internal.generation.BPMN2NotationFactory notation=
				new org.savara.bpmn2.internal.generation.BPMN2NotationFactory(model);
			
			model.setUseConsecutiveIds(_consecutiveIds);
			notation.setUseConsecutiveIds(_consecutiveIds);
			
			// Find namespace for role
			initNamespace(defns, pm);
			
			initImports(defns, pm);
			
			initMessages(defns, pm, prefixes);
			
			BPMN2ModelVisitor visitor=
				new BPMN2ModelVisitor(pm.getProtocol().getName(),
						model, notation);
			
			generateProcess(pm, visitor, handler, locator);
			
			visitor.completeModels();
			
			ret = new java.util.HashMap<String,Object>();
			ret.put(pm.getProtocol().getName()+"_"+pm.getProtocol().getLocatedRole().getName()+
								BPMN_FILE_EXTENSION, defns);
		}
		
		return(ret);
	}
	
	protected void initNamespace(TDefinitions defns, ProtocolModel pm) {
		String role=pm.getProtocol().getLocatedRole().getName();
		
		Annotation ann=AnnotationDefinitions.getAnnotationWithProperty(pm.getProtocol().getAnnotations(),
				AnnotationDefinitions.INTERFACE, AnnotationDefinitions.ROLE_PROPERTY, role);
		
		if (ann != null) {
			defns.setTargetNamespace((String)ann.getProperties().get(AnnotationDefinitions.NAMESPACE_PROPERTY));
			
			// Make sure a namespace prefix is defined for the target namespace
			defns.getOtherAttributes().put(new QName(null, "xmlns:tns"), defns.getTargetNamespace());
		}
	}
	
	protected void initImports(TDefinitions defns, ProtocolModel pm) {
		java.util.List<Annotation> anns=AnnotationDefinitions.getAnnotations(pm.getProtocol().getAnnotations(),
						AnnotationDefinitions.TYPE);

		for (Annotation ann : anns) {
			
			if (ann.getProperties().containsKey(AnnotationDefinitions.LOCATION_PROPERTY)) {
				// Add import
				TImport imp=new TImport();
				imp.setImportType("http://www.w3.org/2001/XMLSchema"); // Assume xsd for now
				imp.setLocation((String)ann.getProperties().get(AnnotationDefinitions.LOCATION_PROPERTY));
				imp.setNamespace((String)ann.getProperties().get(AnnotationDefinitions.NAMESPACE_PROPERTY));
				
				defns.getImport().add(imp);
			}
		}
	}
	
	protected void initMessages(TDefinitions defns, ProtocolModel pm, java.util.Map<String,String> prefixes) {

		for (ImportList il : pm.getImports()) {

			if (il instanceof TypeImportList) {
				TypeImportList til=(TypeImportList)il;
				
				for (TypeImport ti : til.getTypeImports()) {
					TItemDefinition itemDef=new TItemDefinition();
					itemDef.setId("ITEM"+ti.getName());
					
					itemDef.setStructureRef(createQName(ti.getDataType().getDetails(), prefixes));
					defns.getRootElement().add(_objectFactory.createItemDefinition(itemDef));
					
					TMessage mesg=new TMessage();
					mesg.setId("ID"+ti.getName());
					mesg.setName(ti.getName());
					mesg.setItemRef(new QName(defns.getTargetNamespace(),itemDef.getId(), "tns"));
					defns.getRootElement().add(_objectFactory.createMessage(mesg));
				}
			}
		}
	}
	
	protected QName createQName(String qname, java.util.Map<String,String> prefixes) {
		QName ret=QName.valueOf(qname);
		
		String prefix=prefixes.get(ret.getNamespaceURI());
		
		if (prefix != null) {
			ret = new QName(ret.getNamespaceURI(), ret.getLocalPart(), prefix);
		}
		
		return (ret);
	}

	protected void generateProcess(ProtocolModel local, BPMN2ModelVisitor visitor,
					FeedbackHandler handler, ResourceLocator locator) {
		local.visit(visitor);
	}
	
	public class BPMN2ModelVisitor extends DefaultVisitor {
		
		private BPMN2ModelFactory m_modelFactory=null;
		private BPMN2NotationFactory m_notationFactory=null;
		private String m_choreoName=null;
	    private java.util.List<BPMNActivity> m_bpmnActivityStack=new java.util.ArrayList<BPMNActivity>();
	    private java.util.Map<String,BPMNDiagram> m_activityModels=
	    				new java.util.HashMap<String,BPMNDiagram>();
	    private java.util.Map<Protocol, TProcess> _protocolProcessMap=new java.util.HashMap<Protocol, TProcess>();
	    private java.util.Map<Protocol, java.util.List<RunActivity>> _pendingCalledActivityId=
	    				new java.util.HashMap<Protocol, java.util.List<RunActivity>>();

	    /**
		 * The constructor the BPMN model visitor.
		 * 
		 */
		public BPMN2ModelVisitor(String choreoName,
				BPMN2ModelFactory model, BPMN2NotationFactory notation) {
			m_choreoName = choreoName;
			m_modelFactory = model;
			m_notationFactory = notation;
		}
		
		/**
		 * This method starts visiting the behavior description element.
		 * 
		 * @param elem The behavior description
		 */
		public boolean start(Protocol elem) {
			
			try {
				BPMNDiagram diagram=getBPMNModel(elem);
				
				BPMNPool pool=diagram.createPool(getPoolName(elem));
				
				if (pool != null) {
					_protocolProcessMap.put(elem, pool.getProcess());
				
					// See whether this protocol is the target of run
					// activities, and if so, setup the called element id
					if (_pendingCalledActivityId.containsKey(elem)) {
						
						java.util.List<RunActivity> list=_pendingCalledActivityId.get(elem);
						
						for (RunActivity ra : list) {
							ra.setCalledActivityId(new QName(
									m_modelFactory.getDefinitions().getTargetNamespace(),
									pool.getProcess().getId(), "tns"));
						}
					}
				}
				
				//diagram.initialize(elem);
				
				pushBPMNActivity(pool);
			} catch(Exception e) {
				logger.severe("Failed to get state machine " +
						"for behavior '"+elem+"': "+e);
			}
			
			return(true);
		}
		
		protected String getPoolName(Protocol elem) {
			if (elem.getParent() instanceof ProtocolModel) {
				return(elem.getLocatedRole().getName());
			} else {
				return(elem.getName()+"_"+elem.getLocatedRole().getName());
			}
		}
		
		/**
		 * This method ends visiting the behavior description element.
		 * 
		 * @param elem The behavior description
		 */
		public void end(Protocol elem) {

			BPMNActivity umls=getBPMNActivity();
			
			if (umls != null) {
				umls.childrenComplete();
			}

			popBPMNActivity();
		}
		
		/**
		 * This method starts visiting the choice element.
		 * 
		 * @param elem The choice
		 */
		public boolean start(Choice elem) {
			
			if (!(getBPMNActivity() instanceof ServiceInvocationActivity)) {
				try {
					pushBPMNActivity(new ChoiceActivity(elem,
							getBPMNActivity(), m_modelFactory, m_notationFactory));
					
				} catch(Exception e) {
					logger.severe("Failed to create choice state: "+e);
				}
			}
			
			return(true);
		}
		
		/**
		 * This method ends visiting the choice element.
		 * 
		 * @param elem The choice
		 */
		public void end(Choice elem) {
			
			//if (!(getBPMNActivity() instanceof ServiceActivity)) {
				popBPMNActivity();
			//}
		}
		
		/**
		 * This method starts visiting the parallel element.
		 * 
		 * @param elem The parallel
		 */
		public boolean start(Parallel elem) {
			
			try {
				pushBPMNActivity(new ParallelActivity(elem,
						getBPMNActivity(), m_modelFactory, m_notationFactory));
				
			} catch(Exception e) {
				logger.severe("Failed to create parallel state: "+e);
			}
			
			return(true);
		}
		
		/**
		 * This method ends visiting the parallel element.
		 * 
		 * @param elem The parallel
		 */
		public void end(Parallel elem) {
			
			popBPMNActivity();
		}
		
		/**
		 * This method visits the perform activity.
		 * 
		 * @param elem The perform
		 */
		public void accept(Run elem) {
			//AbstractBPMNActivity state=null;
			
			BPMNActivity umls=getBPMNActivity();
			if (umls != null) {
				RunActivity ra=new RunActivity(elem, umls, m_modelFactory, m_notationFactory);
				
				// Check if called protocol is available
				Protocol called=RunUtil.getInnerProtocol(elem.getEnclosingProtocol(),
									elem.getProtocolReference());
				
				if (called != null) {
					TProcess proc=_protocolProcessMap.get(called);
					
					if (proc != null) {
						ra.setCalledActivityId(new QName(
								m_modelFactory.getDefinitions().getTargetNamespace(),
								proc.getId(), "tns"));
					} else {
						java.util.List<RunActivity> list=_pendingCalledActivityId.get(called);
						
						if (list == null) {
							list = new java.util.ArrayList<RunActivity>();
							_pendingCalledActivityId.put(called, list);
						}
						
						list.add(ra);
					}
				}
			}
		}
		
		/**
		 * This method indicates the start of a
		 * global escape.
		 * 
		 * @param elem The global escape
		 * @return Whether to process the contents
		 */
		public boolean start(Do elem) {
			AbstractBPMNActivity state=null;
			
			BPMNActivity umls=getBPMNActivity();
			if (umls != null) {
				state = new DoActivity(elem,
					umls, m_modelFactory, m_notationFactory);
			
				pushBPMNActivity(state);
				
				DoBlockActivity inline=new DoBlockActivity(state, m_modelFactory, m_notationFactory);
				
				pushBPMNActivity(inline);
			}
			
			return(true);
		}
		
		/**
		 * This method indicates the end of a
		 * try escape.
		 * 
		 * @param elem The global escape
		 */
		public void end(Do elem) {
			
			if (getBPMNActivity() instanceof DoBlockActivity) {				
				popBPMNActivity();
			}
			
			popBPMNActivity();
		}
		
		/**
		 * This method indicates the start of a
		 * catch block.
		 * 
		 * @param elem The catch block
		 * @return Whether to process the contents
		 */
		public boolean start(Interrupt elem) {
			if (getBPMNActivity() instanceof DoBlockActivity) {				
				popBPMNActivity();
			}
			
			return(true);
		}
		
		/**
		 * This method indicates the end of a
		 * catch block.
		 * 
		 * @param elem The catch block
		 */
		public void end(Interrupt elem) {
			//popBPMNActivity();
		}

		/**
		 * This method visits the receive activity.
		 * 
		 * @param elem The receive
		 */
		public void accept(Interaction elem) {
			
			// Check if a send
			if (InteractionUtil.isSend(elem)) {
			
				if (getMessageBasedInvocation() || !InteractionUtil.isRequest(elem)) {
					
					BPMNActivity umls=getBPMNActivity();
					if (umls != null) {
						SendActivity sa=
							new SendActivity(elem, umls, m_modelFactory, m_notationFactory);
						
						// Register the send to enable links to be established
						// with an appropriate receive
						BPMNDiagram amodel=umls.getBPMNDiagram();		
						amodel.registerSendActivity(elem, sa);
						
						TSendTask task=sa.getSendTask();
						
						if (task != null) {
							task.setMessageRef(m_modelFactory.getMessageReference(elem));
							task.setOperationRef(m_modelFactory.getOperationReference(elem, task.getMessageRef()));
						}
					}
				} else {
					// Use service task
					BPMNActivity umls=getBPMNActivity();
					if (umls != null) {
						try {
							ServiceInvocationActivity sa=
								new ServiceInvocationActivity(elem, umls, m_modelFactory, m_notationFactory);
							
							// Register the send to enable links to be established
							// with an appropriate receive
							BPMNDiagram amodel=umls.getBPMNDiagram();		
							amodel.registerSendActivity(elem, sa);
							
							TServiceTask task=sa.getServiceTask();
							
							if (task != null) {
								task.setOperationRef(m_modelFactory.getOperationReference(elem,
											m_modelFactory.getMessageReference(elem)));
							}
							
							pushBPMNActivity(sa);
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				
				// Check if service task is on the stack
				ServiceInvocationActivity act=getServiceInvocationActivity();

				if (InteractionUtil.isRequest(elem) ||
						getMessageBasedInvocation() || act == null) {
					
					if (act != null) {
						// Pop the serivce invocation activity, so that the receive
						// occurs after the service task
						popBPMNActivity();
					}
					
					BPMNActivity umls=getBPMNActivity();
					if (umls != null) {
						ReceiveActivity sa=
							new ReceiveActivity(elem, umls, m_modelFactory, m_notationFactory);
						
						// Register the receive to enable links to be established
						// with an appropriate send
						BPMNDiagram amodel=umls.getBPMNDiagram();		
						amodel.registerReceiveActivity(elem, sa);
						
						TReceiveTask task=sa.getReceiveTask();
						
						if (task != null) {
							task.setMessageRef(m_modelFactory.getMessageReference(elem));
							task.setOperationRef(m_modelFactory.getOperationReference(elem, task.getMessageRef()));
						}
					}
				} else {
					if (InteractionUtil.isFaultResponse(elem)) {
						
						BPMNActivity umls=getBPMNActivity();

						// Create boundary event for fault
						TBoundaryEvent event=(TBoundaryEvent)
									m_modelFactory.createBoundaryEvent(umls.getContainer());
						
						TErrorEventDefinition eed=new TErrorEventDefinition();
						eed.setId(m_modelFactory.createId());
						eed.setErrorRef(m_modelFactory.getErrorReference(elem));
						
						// Call this method to establish the error reference on the
						// appropriate service interface (if not already defined)
						m_modelFactory.getOperationReference(elem, eed.getErrorRef());
						
						event.getEventDefinition().add(_objectFactory.createErrorEventDefinition(eed));
						
						event.setAttachedToRef(new QName(
								m_modelFactory.getDefinitions().getTargetNamespace(),
											act.getServiceTask().getId(), "tns"));

						BoundaryEvent be=new BoundaryEvent(event, act.getStartState(),
										m_modelFactory, m_notationFactory);
						
						act.register(umls, be);
						
					} else {
						// Need to associate response with service interface - task will then
						// be automatically associated with it via the operation reference
						m_modelFactory.getOperationReference(elem, m_modelFactory.getMessageReference(elem));
					}
				}
			}
		}
		
		protected ServiceInvocationActivity getServiceInvocationActivity() {
			ServiceInvocationActivity ret=null;
			
			if (m_bpmnActivityStack.size() > 0) {
				BPMNActivity ba=(BPMNActivity)m_bpmnActivityStack.get(0);
				
				if (ba instanceof ServiceInvocationActivity) {
					ret = (ServiceInvocationActivity)ba;
				} else if (ba instanceof SequenceActivity &&
						m_bpmnActivityStack.size() > 1 &&
						//m_bpmnActivityStack.get(1) instanceof ChoiceActivity &&
						m_bpmnActivityStack.get(1) instanceof ServiceInvocationActivity) {
					ret = (ServiceInvocationActivity)m_bpmnActivityStack.get(1);
				}
			}
			return (ret);
		}
		
		/**
		 * This method starts visiting the sequence element.
		 * 
		 * @param elem The sequence
		 */
		public boolean start(Block elem) {
			
			try {
				pushBPMNActivity(new SequenceActivity(getBPMNActivity(), m_modelFactory, m_notationFactory));
				
			} catch(Exception e) {
				logger.severe("Failed to create sequence state: "+e);
			}
			
			return(true);
		}
		
		/**
		 * This method ends visiting the sequence element.
		 * 
		 * @param elem The sequence
		 */
		public void end(Block elem) {
			
			popBPMNActivity();
		}
		
		/**
		 * This method starts visiting the while element.
		 * 
		 * @param elem The while
		 */
		public boolean start(Repeat elem) {
			
			try {
				pushBPMNActivity(new RepeatActivity(elem,
						getBPMNActivity(), m_modelFactory, m_notationFactory));
				
				pushBPMNActivity(new SequenceActivity(getBPMNActivity(), m_modelFactory, m_notationFactory));
				
			} catch(Exception e) {
				logger.severe("Failed to create while Activity: "+e);
			}
			
			return(true);
		}
		
		/**
		 * This method ends visiting the while element.
		 * 
		 * @param elem The while
		 */
		public void end(Repeat elem) {
			
			popBPMNActivity();
			
			popBPMNActivity();
		}
		
		/**
		 * {@inheritDoc}
		 */
		public void accept(CustomActivity act) {
			
			if (act instanceof Fork) {
				BPMNActivity umls=getBPMNActivity();
				if (umls != null) {
					new ForkActivity((Fork)act, umls, m_modelFactory, m_notationFactory);
				}
			} else if (act instanceof Join) {
				BPMNActivity umls=getBPMNActivity();
				if (umls != null) {
					new JoinActivity((Join)act, umls, m_modelFactory, m_notationFactory);
				}
			}
		}
		
		protected BPMNDiagram getBPMNModel(Protocol elem) throws BPMN2GenerationException {
			Protocol main=elem.getTopLevelProtocol();
			String name=main.getName();
			
			BPMNDiagram ret=(BPMNDiagram)
						m_activityModels.get(name);
			
			if (ret == null) {
				ret = new BPMNDiagram(m_choreoName, name,
						null, m_modelFactory, m_notationFactory);
				
				m_activityModels.put(name, ret);
			}
			
			return(ret);
		}
		
		/**
		 * This method pushes the supplied UML activity
		 * onto a stack.
		 * 
		 * @param act The activity
		 */
		protected void pushBPMNActivity(BPMNActivity act) {
			m_bpmnActivityStack.add(0, act);
		}
		
		/**
		 * This method returns the UML activity found at the
		 * top of the stack.
		 * 
		 * @return The activity
		 */
		protected BPMNActivity getBPMNActivity() {
			BPMNActivity ret=null;
			
			if (m_bpmnActivityStack.size() > 0) {
				ret = (BPMNActivity)m_bpmnActivityStack.get(0);
			}
			
			return(ret);
		}
		
		/**
		 * This method returns the UML activity from the
		 * top of the stack.
		 *
		 */
		protected void popBPMNActivity() {
			BPMNActivity umls=null;
			
			do {
				umls = getBPMNActivity();
				
				if (umls != null) {
					umls.childrenComplete();
				}
				
				if (m_bpmnActivityStack.size() > 0) {
					m_bpmnActivityStack.remove(0);
				}
			} while (umls instanceof ServiceInvocationActivity);
		}
		
		/**
		 * This method completes the construction of the activity
		 * models.
		 *
		 */
		public void completeModels() {
			java.util.Iterator<BPMNDiagram> iter=m_activityModels.values().iterator();
			
			while (iter.hasNext()) {
				BPMNDiagram amodel=iter.next();
				
				amodel.completeModel();
			}
		}
		
	}
}
