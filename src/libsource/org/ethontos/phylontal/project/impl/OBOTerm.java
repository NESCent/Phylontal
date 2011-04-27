/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009 Peter E. Midford
 * 
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
 * Created on Apr 20, 2010
 * Last updated on Apr 20, 2010
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;

import org.ethontos.phylontal.project.MatchSet;
import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.obo.datamodel.OBOClass;
import org.semanticweb.owlapi.model.OWLClass;

public class OBOTerm implements OntTerm {

    static final private HashMap<OBOClass,OntTerm> termMap = new HashMap<OBOClass,OntTerm>();
    
    private OBOClass backingTerm;
    

    @Override
    public Object getBackingTerm() {
        return backingTerm;
    }

    @Override
    public String getLabel(OntologyWrapper o) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<MatchSet> getMatchSets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isRoot() {
        // TODO Auto-generated method stub
        return false;
    }

}
