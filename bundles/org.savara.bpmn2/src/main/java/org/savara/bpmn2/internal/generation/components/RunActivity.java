/*
 * Copyright 2005-7 Pi4 Technologies Ltd
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
 * Jan 25, 2007 : Initial version created by gary
 */
package org.savara.bpmn2.internal.generation.components;

import javax.xml.namespace.QName;

import org.savara.bpmn2.model.TCallActivity;
import org.savara.bpmn2.model.TCallChoreography;
import org.scribble.protocol.model.Activity;
import org.scribble.protocol.model.Run;

/**
 * This class represents the BPMN activity node for a Perform activity.
 * 
 */
public class RunActivity extends SimpleActivity {

	/**
	 * This constructor initializes the receive state.
	 * 
	 * @param act The behavioral activity
	 * @param parent The parent BPMN state
	 * @param model The BPMN model
	 */
	public RunActivity(Activity act, BPMNActivity parent,
			org.savara.bpmn2.internal.generation.BPMN2ModelFactory model,
			org.savara.bpmn2.internal.generation.BPMN2NotationFactory notation) {
		super(act, parent, model, notation);
	}
	
	protected Object createNode(Activity act) {
		return(getModelFactory().createCallActivity(getContainer(), (Run)act));
	}
	
	/**
	 * This method sets the id of the called activity.
	 * 
	 * @param value The called activity id
	 */
	public void setCalledActivityId(QName value) {
		Object node=getNode();
		
		if (node instanceof TCallActivity) {
			((TCallActivity)node).setCalledElement(value);
		} else if (node instanceof TCallChoreography) {
			((TCallChoreography)node).setCalledChoreographyRef(value);
		}
	}
	
	/**
	 * This method causes the receive activity to break any
	 * existing control links, including removing preceeding
	 * nodes that only have these links as their outgoing
	 * links.
	 *
	 */
	public void breakLinks() {
		Object node=getStartNode();
		
		java.util.List<Object> list=getModelFactory().getInboundControlLinks(node);
		
		for (int i=list.size()-1; i >= 0; i--) {
			Object edge=list.get(i);
			
			tidyUpEdge(edge);
		}
 	}
	
	/**
	 * This method tidies up an activity edge, recursively being
	 * called if its source node only has a single incoming
	 * activity edge.
	 * 
	 * @param edge The activity edge
	 */
	protected void tidyUpEdge(Object edge) {
		
		// Check if source node has only a single incoming link
		// and if so, then remove the node
		Object sourceNode=getModelFactory().getSource(edge);
		
		getModelFactory().delete(edge);
		
		// If has less than 2 incoming links and no
		// outgoing link, then tidyup node and links recursively
		if (getModelFactory().getInboundControlLinks(sourceNode).size() <= 1 &&
				getModelFactory().getOutboundControlLinks(sourceNode).size() == 0 &&
				(getModelFactory().isDecision(sourceNode) ||	// was decision node
					getModelFactory().isJoin(sourceNode))) {	// was merge node
			if (getModelFactory().getInboundControlLinks(sourceNode).size() == 1) {
				Object actedge=getModelFactory().
						getInboundControlLinks(sourceNode).get(0);
				
				tidyUpEdge(actedge);
			}
			
			getModelFactory().delete(sourceNode);
		}
	}
}
