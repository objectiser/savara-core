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
package org.savara.protocol.contract.generator.impl;

import java.text.MessageFormat;

import org.savara.contract.model.Contract;
import org.savara.protocol.contract.generator.ContractGenerator;
import org.scribble.common.logging.Journal;
import org.scribble.protocol.model.Protocol;
import org.scribble.protocol.model.Role;

/**
 * This class generates a contract from a protocol defining the behaviour of
 * multiple interacting roles.
 * 
 */
public class ContractGeneratorImpl implements ContractGenerator {

	/**
	 * This method generates a contract associated with a supplied located
	 * protocol.
	 * 
	 * @param cm The protocol
	 * @param journal The journal
	 * @return The contract
	 */
	public Contract generate(Protocol cm, Journal journal) throws IllegalArgumentException {
		if (cm == null) {
			throw new IllegalArgumentException(MessageFormat.format(
					java.util.PropertyResourceBundle.getBundle(
						"org.savara.contract.Messages").
							getString("SAVARAPC-00001"), (Object)null));
			
		} else if (cm.getRole() == null) {
			throw new IllegalArgumentException(MessageFormat.format(
					java.util.PropertyResourceBundle.getBundle(
						"org.savara.contract.Messages").
							getString("SAVARAPC-00002"), (Object)null));
		}

		return(generate(cm, null, cm.getRole(), journal));
	}

	/**
	 * This method generates a contract, derived from the supplied protocol model,
	 * associated with a supplied server role, optionally constrained by the supplied
	 * client roles. If the client role is not specified, then the contract associated with
	 * all relevant client roles will be generated.
	 * 
	 * @param cm The global or local (located) protocol
	 * @param clients The optional client roles
	 * @param server The server role
	 * @param journal The journal
	 * @return The contract
	 */
	public Contract generate(Protocol cm, java.util.Set<Role> clients, Role server, Journal journal)
								throws IllegalArgumentException {
		// Check parameters
		if (cm == null) {
			throw new IllegalArgumentException(MessageFormat.format(
					java.util.PropertyResourceBundle.getBundle(
						"org.savara.contract.Messages").
							getString("SAVARAPC-00001"), (Object)null));
			
		} else if (server == null) {
			throw new IllegalArgumentException(MessageFormat.format(
					java.util.PropertyResourceBundle.getBundle(
						"org.savara.contract.Messages").
							getString("SAVARAPC-00003"), (Object)null));
		}
		
		ContractIntrospector introspector=new ContractIntrospector(cm, clients, server, journal);
		
		introspector.process();
		
		return(introspector.getContract());
	}
	
	
}