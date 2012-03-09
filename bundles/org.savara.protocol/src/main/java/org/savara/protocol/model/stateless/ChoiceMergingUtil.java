/*
 * Copyright 2009-10 www.scribble.org
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
 */
package org.savara.protocol.model.stateless;

import java.text.MessageFormat;
import java.util.logging.Logger;

import org.scribble.common.logging.Journal;
import org.scribble.protocol.model.Activity;
import org.scribble.protocol.model.Block;
import org.scribble.protocol.model.Choice;
import org.scribble.protocol.model.Role;

/**
 * This class provides the Choice Merging utility function.
 * 
 * NOTE: This is a copy of the Scribble class that will be available
 * in 2.1.0.M2 onwards, and should switch back to the scribble class then.
 */
public class ChoiceMergingUtil {

    /**
     * System property: should choice constructs be permitted to have empty paths
     * and therefore be optional?
     */
    public static final String ALLOW_OPTIONAL = "scribble.choice.allowOptional";
    
    private static final Logger LOG=Logger.getLogger(ChoiceMergingUtil.class.getName());
    
    /**
     * This method processes a choice construct.
     * 
     * @param context The context
     * @param projected The projected choice
     * @param role The projected role
     * @param l The journal
     * @param optional Whether the construct is optional
     * @return The processed activities
     */
    public static Object merge(Choice projected, Role role,
                            Journal l, boolean optional) {
        Object ret=null;
        
        ret = extractCommonBehaviour(projected, role, l);
        
        // If multiple paths have common initiator (but not common to all behaviours)
        // then group in sub-choice
        groupSubpathsWithCommonInitiator(projected, role, l);
        
        // Remove all empty paths
        for (int i=projected.getPaths().size()-1; i >= 0; i--) {
            Block b=projected.getPaths().get(i);
            
            if (b.size() == 0) {
                projected.getPaths().remove(i);
                optional = true;
            }
        }
        
        if (projected.getPaths().size() == 0) {
            if (ret == projected) {
                ret = null;
            } else {
                ((java.util.List<?>)ret).remove(projected);
            }
            projected = null;
        } else if (optional) {
            
            // Check if optional permitted
            if (System.getProperty(ALLOW_OPTIONAL, "false").
                            equalsIgnoreCase(Boolean.FALSE.toString())) {
                l.error(MessageFormat.format(
                        java.util.PropertyResourceBundle.getBundle(
                                "org.scribble.protocol.projection.impl.Messages").
                            getString("_CHOICE_EMPTY_PATH"),
                            role.getName()), projected.getProperties());
            }
            
            // Add optional block
            projected.getPaths().add(new Block());
        }

        return (ret);
    }

    /**
     * This method checks whether choice paths should be grouped into sub-paths
     * with common initiating interaction sentences.
     * 
     * @param projected The projected choice
     * @param role The role
     * @param l The journal
     */
    @SuppressWarnings("unchecked")
    protected static void groupSubpathsWithCommonInitiator(Choice projected,
                            Role role, Journal l) {
        java.util.Map<Activity, java.util.List<Block>> pathGroups=
                            new java.util.HashMap<Activity, java.util.List<Block>>();
        
        for (Block path : projected.getPaths()) {
            if (path.size() > 0) {
                java.util.List<Block> plist=pathGroups.get(path.get(0));
                if (plist == null) {
                    plist = new java.util.Vector<Block>();
                    pathGroups.put(path.get(0), plist);
                }
                plist.add(path);
            }
        }
        
        for (Activity act : pathGroups.keySet()) {
            java.util.List<Block> plist=pathGroups.get(act);
            
            if (plist.size() >= 2) {
                
                // Create new choice construct
                Choice sub=new Choice();
                int pos=-1;
                
                // TODO: Do we need to determine/set the role associated with choice?
                // If different then report error?
                
                for (Block b : plist) {
                    if (pos == -1) {
                        pos = projected.getPaths().indexOf(b);
                    }
                    projected.getPaths().remove(b);
                    sub.getPaths().add(b);
                }
                
                Block newPath=new Block();
                
                projected.getPaths().add(pos, newPath);
                
                Object processed=merge(sub, role, l, false);
                
                if (processed instanceof java.util.List<?>) {
                    newPath.getContents().addAll((java.util.List<Activity>)processed);
                } else {
                    LOG.severe("Should have returned a list with extracted common activity(s) followed by choice");
                }
            }
        }
    }

    /**
     * Extract common behaviour.
     * 
     * @param projected The projected choice
     * @param role The role
     * @param l The journal
     * @return Extracted common behaviour
     */
    @SuppressWarnings("unchecked")
    protected static Object extractCommonBehaviour(Choice projected,
                            Role role, Journal l) {
        Object ret=projected;
        
        // Check to see whether common interaction sentences can be extracted
        // out from each path to precede the choice
        boolean checkPaths=true;
        do {
            boolean allSame=projected.getPaths().size() > 1;
            
            for (int i=1; allSame && i < projected.getPaths().size(); i++) {
                Block b1=projected.getPaths().get(0);
                Block b2=projected.getPaths().get(i);
                
                if (b1.size() == 0 || b2.size() == 0) {
                    allSame = false;
                } else if (!b1.get(0).equals(b2.get(0))) {
                    allSame = false;
                }
            }
            
            if (allSame) {
                // Merge first elements from each path and place before the choice
                if (!(ret instanceof java.util.List<?>)) {
                    ret = new java.util.Vector<Object>();
                    ((java.util.List<Object>)ret).add(projected);
                }
                
                ((java.util.List<Object>)ret).add(((java.util.List<Object>)ret).size()-1,
                        projected.getPaths().get(0).getContents().get(0));
                
                for (int i=0; i < projected.getPaths().size(); i++) {
                    // Remove first element
                    projected.getPaths().get(i).getContents().remove(0);
                }
            } else {
                checkPaths = false;
            }
            
        } while(checkPaths);
        
        return (ret);
    }
    
    /**
     * Check whether roles are the same.
     * 
     * @param c1 First choice
     * @param c2 Second choice
     * @return Whether roles are same
     */
    protected boolean isSameRole(Choice c1, Choice c2) {
        if (c1.getRole() == null && c2.getRole() == null) {
            return (true);
        } else {
            return (c1.getRole().equals(c2.getRole()));
        }
    }
}
