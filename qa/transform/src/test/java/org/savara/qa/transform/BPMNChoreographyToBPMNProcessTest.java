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
package org.savara.qa.transform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.savara.bpmn2.generation.process.ProtocolToBPMN2ProcessModelGenerator;
import org.savara.bpmn2.model.TDefinitions;
import org.savara.bpmn2.parser.choreo.BPMN2ChoreographyProtocolParser;
import org.savara.bpmn2.model.util.BPMN2ModelUtil;
import org.savara.common.logging.DefaultFeedbackHandler;
import org.savara.common.model.annotation.AnnotationDefinitions;
import org.savara.protocol.util.JournalProxy;
import org.savara.protocol.util.ProtocolServices;
import org.scribble.common.logging.ConsoleJournal;
import org.scribble.common.resource.Content;
import org.scribble.common.resource.ResourceContent;
import org.scribble.protocol.DefaultProtocolContext;
import org.scribble.protocol.model.ProtocolModel;
import org.scribble.protocol.model.Role;

public class BPMNChoreographyToBPMNProcessTest {

    public static Test suite() {
        TestSuite suite = new TestSuite("BPMN2 Choreo->BPMN2 Process Transform Tests");

        // SAVARA-325
        //suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithANDJoinActivity", "Store", false));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithANDJoinActivity", "CreditAgency", false));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithANDJoinActivity", "Logistics", false));

        // SAVARA-325
        //suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithXORJoinActivity", "Store", false));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithXORJoinActivity", "CreditAgency", false));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithXORJoinActivity", "Logistics", false));

        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithANDJoinActivity", "Store", true));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithANDJoinActivity", "CreditAgency", true));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithANDJoinActivity", "Logistics", true));

        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithXORJoinActivity", "Store", true));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithXORJoinActivity", "CreditAgency", true));
        suite.addTest(new BPMN2ChoreographyToBPMNProcessTester("PurchaseGoodsWithXORJoinActivity", "Logistics", true));

        return suite;
    }
    
    protected static class BPMN2ChoreographyToBPMNProcessTester extends TestCase {

    	private String _name=null;
    	private String _role=null;
    	private boolean _useMessageBasedInvocation=false;

    	/**
    	 * This constructor is initialized with the test
    	 * name.
    	 * 
    	 * @param name The test name
    	 */
    	public BPMN2ChoreographyToBPMNProcessTester(String name, String role, boolean mom) {
    		super(name+"["+role+"/mom="+mom+"]");
    		_name = name;
    		_role = role;
    		_useMessageBasedInvocation = mom;
    	}
    	
    	/**
    	 * This method runs the test.
    	 * 
    	 * @param result The test result
    	 */
    	public void run(TestResult result) {
   		
    		// Run test
    		result.startTest(this);
    		
    		String filename="qamodels/bpmn/"+_name+".bpmn";
    		
    		java.net.URL url=
    			ClassLoader.getSystemResource(filename);
    		
    		if (url == null) {
    			result.addError(this,
    					new Throwable("Unable to locate resource: "+filename));
    		} else {			
    			ConsoleJournal journal=new ConsoleJournal();
    			
    			org.scribble.protocol.model.ProtocolModel model=null;
    			
				BPMN2ChoreographyProtocolParser parser=new BPMN2ChoreographyProtocolParser();
				
    			try {
    				Content content=new ResourceContent(url.toURI());
    				
    				model = parser.parse(null, content, journal);
    			} catch(Exception e) {
    				e.printStackTrace();
    				result.addError(this, new Throwable("Parsing BPMN2 failed"));
    			}
    			
    			if (model == null) {
    				result.addError(this, new Throwable("Model is null"));
    			} else {
        			DefaultFeedbackHandler handler=new DefaultFeedbackHandler();
    				
    				org.scribble.protocol.model.Role role=null;
    				
    				java.util.List<org.scribble.protocol.model.Role> roles=model.getRoles();
    				
    				for (int i=0; role == null && i < roles.size(); i++) {
    					if (roles.get(i).getName().equals(_role)) {
    						role = roles.get(i);
    					}
    				}
    				
    				if (role == null) {
    					result.addError(this,
    							new Throwable("Role '"+_role+"' not found"));						
    				} else {
    					DefaultProtocolContext context=
    							new DefaultProtocolContext(ProtocolServices.getParserManager(),
    									null);
    	
    					ProtocolModel local=ProtocolServices.getProtocolProjector().project(context, model,
    									role, new JournalProxy(handler));

    					if (local != null) {
    						// TODO: SAVARA-167 - issue when projection is based on a sub-protocol
    						if (AnnotationDefinitions.getAnnotation(local.getProtocol().getAnnotations(),
    										AnnotationDefinitions.TYPE) == null &&
    								AnnotationDefinitions.getAnnotation(model.getProtocol().getAnnotations(),
    												AnnotationDefinitions.TYPE) != null) {				
    							AnnotationDefinitions.copyAnnotations(model.getProtocol().getAnnotations(),
    									local.getProtocol().getAnnotations(), AnnotationDefinitions.TYPE);
    						}   						
    		   				
    	    				ProtocolToBPMN2ProcessModelGenerator generator=
    	    								new ProtocolToBPMN2ProcessModelGenerator();
    	    				generator.setUseConsecutiveIds(true);
    	    				generator.setMessageBasedInvocation(_useMessageBasedInvocation);
    	    				
    	 					java.util.Map<String,Object> map=generator.generate(local, handler, null);
    						
    						if (map == null || map.size() != 1) {
    							fail("Protocol to BPMN model generator didn't return a single BPMN process definition");
    						}
    						
    						Object target=map.values().iterator().next();   						
    	    				
    	    				if (target instanceof TDefinitions) {

    							try {
    								java.io.ByteArrayOutputStream baos=new java.io.ByteArrayOutputStream();
    								
    								BPMN2ModelUtil.serialize((TDefinitions)target, baos);
    								
    								baos.close();
    								
    								String text=new String(baos.toByteArray());
    								
    								checkResults(result, local.getProtocol().getLocatedRole(), text);
    							} catch(Exception e) {
    								result.addError(this, e);
    							}
    						} else {
    							result.addError(this,
    									new Throwable("No BPMN2 generated"));						
    	    				}
    					}
    				}
    			}
    		}
    		
    		result.endTest(this);
    	}
    	
    	/**
    	 * This method checks the generated protocol against a
    	 * previously stored correct version.
    	 * 
    	 * @param result The test result
    	 * @param role The role
    	 * @param protocol The protocol
    	 */
    	protected void checkResults(TestResult result, Role role, String protocol) {
    		boolean f_valid=false;

    		String filename=_name+"@"+role.getName()+(_useMessageBasedInvocation?"_mom":"")+".bpmn";
    		
    		String filepath="qaresults/bpmn_from_bpmn_choreo/"+filename;
    		
    		java.io.InputStream is=
    				ClassLoader.getSystemResourceAsStream(filepath);
    		
    		if (is != null) {
    			
    			try {
    				byte[] b=new byte[is.available()];
    			
    				is.read(b);
    				
    				is.close();
    				
    				String orig=new String(b);
    				
    				if (orig.equals(protocol) == false) {
    					result.addError(this,
    							new Throwable("Generated protocol does not match stored version"));
    				} else {
    					f_valid = true;
    				}
    			} catch(Exception e) {
    				result.addError(this, e);
    			}
    		} else {
    			/*
    			result.addError(this,
    					new Throwable("Resulting protocol '"+filename+
    							"' not found for comparison"));
    							*/
    			System.err.println("Generating file, as comparison file not found");
    		}
    		
    		if (f_valid == false) {
    			String bpmn2file="qamodels/bpmn";
    			
    			java.net.URL url=ClassLoader.getSystemResource(bpmn2file);
    			
    			if (url != null) {
    				// URL will point to copy of test models in the classes folder, so need
    				// to obtain reference back to source version
    				java.io.File f=null;
    				
    				if (url.getFile().indexOf("target/test-classes") != -1) {
    					f = new java.io.File(url.getFile().replaceFirst("target/test-classes","src/test/resources"));
    				} else if (url.getFile().indexOf("classes") != -1) {
    					f = new java.io.File(url.getFile().replaceFirst("classes","src/test/resources"));
    				} else if (url.getFile().indexOf("bin") != -1) {						
    					f = new java.io.File(url.getFile().replaceFirst("bin","src/test/resources"));
    				} else {
    					result.addError(this, new Exception("Could not locate results folder to record expected result"));
    				}
    				
    				if (f != null && f.exists()) {
    					f = f.getParentFile().getParentFile();
    					
    					java.io.File resultsDir=new java.io.File(f, "qaresults/bpmn_from_bpmn_choreo");
    					
    					if (resultsDir.exists() == false) {
    						resultsDir.mkdirs();
    					}
    					
    					java.io.File resultFile=new java.io.File(resultsDir,
    										filename+".generated");
    					
    					if (resultFile.exists() == false) {
    						try {
    							java.io.FileOutputStream fos=new java.io.FileOutputStream(resultFile);
    							
    							fos.write(protocol.getBytes());
    							
    							fos.flush();
    							fos.close();
    							
    						} catch(Exception e){
    							result.addError(this, e);
    						}
    					} else {
    						System.err.println("NOTE: Generated output '"+resultFile+
    									"' already exists - not being overwritten");
    					}
    				} else {
    					result.addError(this, new Throwable("Unable to obtain URL for BPMN2 model source '"+
    							_name+"' role "+role.getName()+": "+url));
    				}
    			}
    		}
    	}
    }
}
