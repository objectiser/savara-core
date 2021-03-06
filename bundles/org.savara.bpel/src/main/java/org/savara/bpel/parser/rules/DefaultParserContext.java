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

import org.savara.bpel.model.TActivity;
import org.savara.bpel.model.TProcess;
import org.savara.bpel.model.TScope;
import org.savara.bpel.model.TSource;
import org.savara.bpel.model.TTarget;
import org.savara.bpel.model.TVariable;
import org.savara.common.logging.FeedbackHandler;
import org.savara.common.resources.ResourceLocator;
import org.savara.protocol.model.Join;
import org.savara.protocol.model.Fork;
import org.scribble.protocol.model.Activity;
import org.scribble.protocol.model.ProtocolModel;

/**
 * This class provides a default implementation of the conversation
 * context.
 */
public class DefaultParserContext implements ParserContext {
	
	private String m_role=null;
	private TProcess m_process=null;
	private ProtocolModel m_protocolModel=null;
	private ResourceLocator m_resourceLocator=null;
	private java.util.Map<String,TVariable> m_variables=
					new java.util.HashMap<String,TVariable>();
	private java.util.Stack<TScope> m_scopeStack=new java.util.Stack<TScope>();
	private java.util.Map<String,String> _introducers=
					new java.util.HashMap<String,String>();
	
	private static java.util.List<ProtocolParserRule> m_rules=
					new java.util.Vector<ProtocolParserRule>();
	
	static {
		m_rules.add(new ElseParserRule());
		m_rules.add(new ElseifParserRule());
		m_rules.add(new FlowParserRule());
		m_rules.add(new IfParserRule());
		m_rules.add(new InvokeParserRule());
		//m_rules.add(new OnMessageGenerationRule());
		m_rules.add(new PickParserRule());
		m_rules.add(new ProcessParserRule());
		m_rules.add(new ReceiveParserRule());
		m_rules.add(new ReplyParserRule());
		m_rules.add(new ScopeParserRule());
		m_rules.add(new SequenceParserRule());
		m_rules.add(new WhileParserRule());
	}

	/**
	 * This is the constructor for the conversion context, initialised
	 * with the role being played by the converted BPEL process.
	 * 
	 * @param role The role
	 * @param proc The process
	 * @param pm The protocol model
	 * @param locator The resource locator
	 */
	public DefaultParserContext(String role, TProcess proc, ProtocolModel pm,
							ResourceLocator locator) {
		m_role = role;
		m_process = proc;
		m_protocolModel = pm;
		m_resourceLocator = locator;
	}
	
	/**
	 * This method applies the conversion process to the supplied component.
	 * 
	 * @param component The domain component
	 * @param activities The list of protocol activities to place the conversion results
	 */
	public void parse(Object component, java.util.List<Activity> activities, FeedbackHandler handler) {
		ProtocolParserRule rule=null;
		
		for (int i=0; rule == null && i < m_rules.size(); i++) {
			if (m_rules.get(i).isSupported(component)) {
				rule = m_rules.get(i);
			}
		}
		
		if (rule != null) {
			// Check if join should be defined
			if (component instanceof TActivity) {
				TActivity act=(TActivity)component;
				
				if (act.getTargets() != null) {
					Join join=new Join();
					if (act.getTargets().getJoinCondition() == null ||
							act.getTargets().getJoinCondition().getContent().
									get(0).toString().indexOf(" and ") == -1) {
						join.setXOR(true);
					}
					
					for (TTarget target : act.getTargets().getTarget()) {
						join.getLabels().add(target.getLinkName());
					}
					
					activities.add(join);
				}
			}

			rule.parse(this, component, activities, handler);
			
			// Check if sync should be defined
			if (component instanceof TActivity) {
				TActivity act=(TActivity)component;
				
				if (act.getSources() != null) {
					for (TSource src : act.getSources().getSource()) {
						Fork sync=new Fork();
						sync.setLabel(src.getLinkName());
						activities.add(sync);
					}
				}
			}
		}
	}
	
	/**
	 * This method returns the resource locator.
	 * 
	 * @return The resource locator
	 */
	public ResourceLocator getResourceLocator() {
		return(m_resourceLocator);
	}
	
	/**
	 * This method returns the process.
	 * 
	 * @return The process
	 */
	public TProcess getProcess() {
		return(m_process);
	}
	
	/**
	 * This method returns the protocol model being generated by
	 * parsing the BPEL process definition.
	 * 
	 * @return The protocol model
	 */
	public ProtocolModel getProtocolModel() {
		return(m_protocolModel);
	}
	
	/**
	 * This method returns the role associated with the
	 * endpoint being converted.
	 * 
	 *@return The role
	 */
	public String getRole() {
		return(m_role);
	}
	
	/**
	 * This method returns the variable associated with the
	 * supplied name.
	 * 
	 * @param name The name
	 * @return The variable, or null if not found
	 */
	public TVariable getVariable(String name) {
		return(m_variables.get(name));
	}
	
	/**
	 * This method adds a variable to the context.
	 * 
	 * @param var The variable
	 */
	public void addVariable(TVariable var) {
		m_variables.put(var.getName(), var);
	}
	
	/**
	 * This method removes a variable from the context.
	 * 
	 * @param var The variable
	 */
	public void removeVariable(TVariable var) {
		m_variables.remove(var.getName());
	}
	
	/**
	 * This method pushes the supplied scope on the stack.
	 * 
	 * @param scope The scope
	 */
	public void pushScope(TScope scope) {
		m_scopeStack.push(scope);
	}
	
	/**
	 * This method pops the top scope from the stack.
	 * 
	 */
	public void popScope() {
		m_scopeStack.pop();
	}
	
	/**
	 * This method returns the current scope.
	 * 
	 * @return The scope, or null if not within a scope
	 */
	public TScope getScope() {
		return(m_scopeStack.size() > 0 ? m_scopeStack.peek() : null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public java.util.Map<String, String> getIntroducers() {
		return (_introducers);
	}

}
