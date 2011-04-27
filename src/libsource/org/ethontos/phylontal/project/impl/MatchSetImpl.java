/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2011 Peter E. Midford
 * This file is part of Phylontal.
 *
 * Phylontal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Phylontal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Phylontal.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Created on Nov 1, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.util.Collection;

import org.ethontos.phylontal.project.MatchSet;
import org.ethontos.phylontal.project.MatchType;
import org.ethontos.phylontal.project.NamedEdge;
import org.ethontos.phylontal.project.OntTerm;

public class MatchSetImpl implements MatchSet {
    
    private MatchType myType;
    private Object myEvidence;  //TODO make a type for this

    private Collection<OntTerm> myTermSet;   //might be alignment of terms
    private Collection<NamedEdge> myEdgeSet; //or relations? (maybe need more than an edge class here...)
    
    
    
    @Override
    public MatchType getMatchType() {
        // TODO Auto-generated method stub
        return myType;
    }

}
