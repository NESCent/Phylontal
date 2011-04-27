/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2011 Peter E. Midford
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
 * Created on Nov 2, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.ethontos.phylontal.project.MatchSet;
import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

class OWLTerm implements OntTerm{

    static final private HashMap<OWLClass,OntTerm> termMap = new HashMap<OWLClass,OntTerm>();
    
    private OWLClass backingTerm;
    
    static OntTerm getOntTerm(OWLClass cl){
        if (termMap.containsKey(cl))
            return termMap.get(cl);
        OWLTerm result = new OWLTerm(cl);
        termMap.put(cl, result);
        return result;        
    }
    
    public boolean isRoot(){
        return OWLOntologyWrapper.OWL_THING().equals(getBackingTerm());
    }

    
    private OWLTerm(OWLClass back){
        backingTerm = back;
    }
    
    @Override
    public Object getBackingTerm() {
        return backingTerm;
    }

    @Override
    public Collection<MatchSet> getMatchSets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URI getURI() {
        return backingTerm.getIRI().toURI();
    }

    // Should be using this, once we have the ontology sorted out...
    public String getLabel(OntologyWrapper o){
        if (o instanceof OWLOntologyWrapper){
            return ((OWLOntologyWrapper)o).labelFor(backingTerm);
        }
        else
            return backingTerm.getIRI().toURI().toString();
    }
}
