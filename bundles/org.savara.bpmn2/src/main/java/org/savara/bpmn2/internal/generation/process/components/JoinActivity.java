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
package org.savara.bpmn2.internal.generation.process.components;

import org.savara.bpmn2.model.BPMNEdge;
import org.savara.protocol.model.Join;
import org.scribble.protocol.model.Activity;

/**
 * This class represents the BPMN2 activity node for a Join activity.
 * 
 */
public class JoinActivity extends SimpleActivity {
	
	private Join _join=null;

	/**
	 * This constructor initializes the sync.
	 * 
	 * @param act The behavioral activity
	 * @param parent The parent BPMN state
	 * @param model The BPMN model
	 */
	public JoinActivity(Join act,
			BPMNActivity parent, org.savara.bpmn2.internal.generation.process.BPMN2ModelFactory model,
			org.savara.bpmn2.internal.generation.process.BPMN2NotationFactory notation) {
		super(act, parent, model, notation);
		
		_join = act;
	}
	
	protected Object createNode(Activity act) {
		Join join=(Join)act;
		
		java.util.List<Object> ret=new java.util.Vector<Object>();
		
		ret.add(getModelFactory().createJoinTask(getContainer(), act));
		
		for (String label : join.getLabels()) {
			ret.add(getModelFactory().createLinkTarget(getContainer(), label));
		}
		
		return(ret);
	}
	
	/**
	 * This method returns the behavioral join activity.
	 * 
	 * @return The join activity
	 */
	public Join getJoin() {
		return(_join);
	}
	
	public int getWidth() {
		if (getNodeList().size() > 1) {
			return(120);
		}
		return(30);
	}
	
	public int getHeight() {
		int count=getNodeList().size();
		if (count > 1) {
			count--;
		}
		return(30*count+((count-1)*20));
	}
	
	protected java.util.List<Object> getNodeList() {
		@SuppressWarnings("unchecked")
		java.util.List<Object> list=(java.util.List<Object>)getNode();
		return(list);
	}
	
	@Override
	public Object getStartNode() {
		return(getNodeList().get(0));
	}
	
	@Override
	public Object getEndNode() {
		return(getNodeList().get(0));
	}
	
	@Override
	public void draw(Object parent) {
		java.util.List<Object> list=getNodeList();

		getNotationFactory().createTask(getModelFactory(),
				list.get(0), parent, getX()+90, getY()+(getHeight()-30)/2, 30, 30);

		for (int i=1; i < list.size(); i++) {
			getNotationFactory().createEvent(getModelFactory(),
					list.get(i), parent, getX(), getY()+((i-1)*50), 30, 30);
			
			Object link=getModelFactory().createControlLink(getContainer(),
					list.get(i), list.get(0), null);
			
			getNotationFactory().createSequenceLink(getModelFactory(), link, parent);
		}
	}

}
