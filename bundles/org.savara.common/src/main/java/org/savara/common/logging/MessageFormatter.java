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
package org.savara.common.logging;

import java.text.MessageFormat;

/**
 * This class provides utility functions for formatting messages intended to be
 * displayed to users, and subject to internationalization.
 *
 */
public class MessageFormatter {

	/**
	 * This method formats a text message based on the supplied module and code
	 * ids, with optional arguments.
	 * 
	 * @param bundle The bundle
	 * @param code The message's code
	 * @param args The optional list of arguments for inclusion in the message
	 * @return The formatted text
	 */
	public static String format(java.util.ResourceBundle bundle, String code, Object... args) {
		String ret=MessageFormat.format(bundle.getString(code), args);
		
		if (ret != null) {
			ret += " ["+code+"]";
		}
		
		return(ret);
	}
}
