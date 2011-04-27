/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jul 11, 2010
 * Last updated on Jul 12, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import java.util.HashSet;

import org.ethontos.phylontal.project.phylo.StateSet;

class StateSetImpl implements StateSet{
    HashSet<String> states;
    
    private StateSetImpl(){
        states = new HashSet<String>();
    }

    @Override
    public void addState(String symbol) {
        states.add(symbol);        
    }

}
