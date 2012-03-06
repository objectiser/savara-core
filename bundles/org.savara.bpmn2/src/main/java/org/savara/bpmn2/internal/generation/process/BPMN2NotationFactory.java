/*
 * Copyright 2005-6 Pi4 Technologies Ltd
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
 * 30 Jan 2007 : Initial version created by gary
 */
package org.savara.bpmn2.internal.generation.process;

import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.savara.bpmn2.model.BPMNDiagram;
import org.savara.bpmn2.model.BPMNEdge;
import org.savara.bpmn2.model.BPMNPlane;
import org.savara.bpmn2.model.BPMNShape;
import org.savara.bpmn2.model.Bounds;
import org.savara.bpmn2.model.DiagramElement;
import org.savara.bpmn2.model.ObjectFactory;
import org.savara.bpmn2.model.Point;
import org.savara.bpmn2.model.TBaseElement;
import org.savara.bpmn2.model.TGateway;
import org.savara.bpmn2.model.TParticipant;
import org.savara.bpmn2.model.TProcess;
import org.savara.bpmn2.model.TSequenceFlow;



public class BPMN2NotationFactory {

	private BPMN2ModelFactory m_modelFactory=null;
	private BPMNPlane m_plane=null;
	private ObjectFactory m_factory=new ObjectFactory();
	private boolean m_consecutiveIds=false;
	private int m_id=1;
	
	public BPMN2NotationFactory(BPMN2ModelFactory modelFactory) {
		m_modelFactory = modelFactory;
	}
	
	public void setUseConsecutiveIds(boolean b) {
		m_consecutiveIds = b;
	}
	
	protected String createId() {
		if (m_consecutiveIds) {
			return("NID"+(m_id++));
		}
		return(UUID.randomUUID().toString());
	}

	public Object createDiagram(BPMN2ModelFactory factory,
			Object diagramModel, int x, int y, int width, int height) {
		BPMNDiagram ret=new BPMNDiagram();
		ret.setId(createId());
		
		m_modelFactory.getDefinitions().getBPMNDiagram().add(ret);
		
		m_plane = new BPMNPlane();
		ret.setBPMNPlane(m_plane);
		
		// TODO: Need to set reference to the Collaboration
		
		ret.setBPMNPlane(m_plane);
		
		return(m_plane);
	}
	
	public Object createPool(BPMN2ModelFactory factory,
				Object poolModel, Object diagramNotation,
				int x, int y, int width, int height) {
		BPMNShape shape=new BPMNShape();
		shape.setId(createId());
		
		if (poolModel instanceof TProcess) {
			TProcess base=(TProcess)poolModel;
			
			// Find participant for this pool
			TParticipant participant=null;
			
			for (TParticipant p : m_modelFactory.getCollaboration().getParticipant()) {
				if (p.getName().equals(base.getName())) {
					participant = p;
					break;
				}
			}
			
			if (participant != null) {
				shape.setBpmnElement(new QName(m_modelFactory.getDefinitions().getTargetNamespace(),
									participant.getId()));
			}
		}
		
		Bounds b=new Bounds();
		b.setX(x);
		b.setY(y);
		b.setWidth(width);
		b.setHeight(height);
		
		shape.setBounds(b);		
		
		if (diagramNotation instanceof BPMNPlane) {
			BPMNPlane plane=(BPMNPlane)diagramNotation;
			
			plane.getDiagramElement().add(m_factory.createBPMNShape(shape));
		}
		
		return(shape);
	}
	
	public Object createTask(BPMN2ModelFactory factory,
			Object taskModel, Object parentNotation,
					int x, int y, int width, int height) {
		BPMNShape shape=new BPMNShape();
		shape.setId(createId());
		
		if (taskModel instanceof TBaseElement) {
			TBaseElement base=(TBaseElement)taskModel;
			
			shape.setBpmnElement(new QName(m_modelFactory.getDefinitions().getTargetNamespace(),
										base.getId()));
		}
		
		Bounds b=new Bounds();
		b.setX(x);
		b.setY(y);
		b.setWidth(width);
		b.setHeight(height);
		
		shape.setBounds(b);
		
		if (parentNotation instanceof BPMNPlane) {
			BPMNPlane plane=(BPMNPlane)parentNotation;
			
			plane.getDiagramElement().add(m_factory.createBPMNShape(shape));
		}
		
		return(shape);
	}
	
	public Object createEvent(BPMN2ModelFactory factory,
			Object eventModel, Object parentNotation,
					int x, int y, int width, int height) {
		BPMNShape shape=new BPMNShape();
		shape.setId(createId());
		
		if (eventModel instanceof TBaseElement) {
			TBaseElement base=(TBaseElement)eventModel;
			
			shape.setBpmnElement(new QName(m_modelFactory.getDefinitions().getTargetNamespace(),
									base.getId()));
		}

		Bounds b=new Bounds();
		b.setX(x);
		b.setY(y);
		b.setWidth(width);
		b.setHeight(height);
		
		shape.setBounds(b);
		
		if (parentNotation instanceof BPMNPlane) {
			BPMNPlane plane=(BPMNPlane)parentNotation;
			
			plane.getDiagramElement().add(m_factory.createBPMNShape(shape));
		}
		
		return(shape);
	}
	
	public Object createJunction(BPMN2ModelFactory factory,
			Object junctionModel, Object parentNotation,
					int x, int y, int width, int height) {
		BPMNShape shape=new BPMNShape();
		shape.setId(createId());
		
		if (junctionModel instanceof TBaseElement) {
			TBaseElement base=(TBaseElement)junctionModel;
			
			shape.setBpmnElement(new QName(m_modelFactory.getDefinitions().getTargetNamespace(),
									base.getId()));
		}
		
		Bounds b=new Bounds();
		b.setX(x);
		b.setY(y);
		b.setWidth(width);
		b.setHeight(height);
		
		shape.setBounds(b);
		
		if (parentNotation instanceof BPMNPlane) {
			BPMNPlane plane=(BPMNPlane)parentNotation;
			
			plane.getDiagramElement().add(m_factory.createBPMNShape(shape));
		}
		
		return(shape);
	}
	
	public Object createSequenceLink(BPMN2ModelFactory factory,
			Object linkModel, Object diagramNotation) {
		BPMNEdge edge=new BPMNEdge();
		edge.setId(createId());
		BPMNShape source=null;
		BPMNShape target=null;
		int extrax=0;
		
		if (linkModel instanceof TSequenceFlow) {
			TSequenceFlow seqflow=(TSequenceFlow)linkModel;
			
			edge.setBpmnElement(new QName(m_modelFactory.getDefinitions().getTargetNamespace(),
									seqflow.getId()));
			
			// Find source and target diagram shapes
			if (diagramNotation instanceof BPMNPlane &&
					seqflow.getSourceRef() instanceof TBaseElement &&
					seqflow.getTargetRef() instanceof TBaseElement) {
				BPMNPlane plane=(BPMNPlane)diagramNotation;
				
				for (JAXBElement<? extends DiagramElement> jbde : plane.getDiagramElement()) {
					DiagramElement de=jbde.getValue();
					
					if (de instanceof BPMNShape && ((BPMNShape)de).getBpmnElement() != null) {
						if (((BPMNShape)de).getBpmnElement().getLocalPart().equals(
									((TBaseElement)seqflow.getSourceRef()).getId())) {
							source = (BPMNShape)de;
						} else if (((BPMNShape)de).getBpmnElement().getLocalPart().equals(
									((TBaseElement)seqflow.getTargetRef()).getId())) {
							target = (BPMNShape)de;
						}
					}
				}
			}
			
			if (seqflow.getSourceRef() instanceof TGateway) {
				extrax = 20;
			}			
		}
		
		if (diagramNotation instanceof BPMNPlane && source != null && target != null) {
			BPMNPlane plane=(BPMNPlane)diagramNotation;
			
			edge.setSourceElement(new QName(m_modelFactory.getDefinitions().getTargetNamespace(),
										source.getId()));
			edge.setTargetElement(new QName(m_modelFactory.getDefinitions().getTargetNamespace(),
										target.getId()));
			
			Point sourcep=new Point();
			sourcep.setX(source.getBounds().getX()+source.getBounds().getWidth()+extrax);
			sourcep.setY(source.getBounds().getY()+(source.getBounds().getHeight()/2));
			edge.getWaypoint().add(sourcep);
			
			Point targetp=new Point();
			targetp.setX(target.getBounds().getX());
			targetp.setY(target.getBounds().getY()+(target.getBounds().getHeight()/2));
			edge.getWaypoint().add(targetp);

			/*
			Point sourcep=new Point();
			sourcep.setX(source.getBounds().getX()+(source.getBounds().getWidth()/2));
			sourcep.setY(source.getBounds().getY()+(source.getBounds().getHeight()/2));
			edge.getWaypoint().add(sourcep);
			
			Point targetp=new Point();
			targetp.setX(target.getBounds().getX()+(target.getBounds().getWidth()/2));
			targetp.setY(target.getBounds().getY()+(target.getBounds().getHeight()/2));
			edge.getWaypoint().add(targetp);
			*/
			
			plane.getDiagramElement().add(m_factory.createBPMNEdge(edge));
		}
		
		return(edge);
	}
	
	public Object createMessageLink(BPMN2ModelFactory factory,
			Object linkModel, Object diagramNotation) {
		BPMNEdge edge=new BPMNEdge();
		edge.setId(createId());
		
		return(edge);
	}
	
}
