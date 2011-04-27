/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jun 4, 2010
 * Last updated on Jun 4, 2010
 * 
 */
package org.ethontos.phylontal.project;

import java.util.Collection;

public interface ProjectOptions {

        void setLexicalMatcher(String matcher);
        
        Collection<String> getAvailableLexicalMatchers();
        

}
