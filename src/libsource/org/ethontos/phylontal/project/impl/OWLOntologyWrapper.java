/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2011Peter E. Midford
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
 * Created on Oct 8, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;



import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ethontos.phylontal.project.NamedEdge;
import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.ethontos.phylontal.project.RelationEdge;
import org.jgrapht.DirectedGraph;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.ToldClassHierarchyReasoner;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OWLOntologyWrapper implements OntologyWrapper {
    
    private final static String reasonerURL = null;
    
    private final static OWLOntologyManager oManager = OWLManager.createOWLOntologyManager();
    private final static OWLClass owlThing = oManager.getOWLDataFactory().getOWLClass(OWLRDFVocabulary.OWL_THING.getURI());
    
    private OWLOntology ontology;
    private ToldClassHierarchyReasoner reasoner;
    
    
    static boolean canRead(File file){
        try{
            oManager.loadOntologyFromPhysicalURI(file.toURI());
        } catch (Exception e){
            return false;
        }
        return true;
    }

    
    static OntologyWrapper loadOntology(File file){
        final URI ontURI = file.toURI();
        OWLOntologyWrapper result = null;
        try {
            result = new OWLOntologyWrapper(oManager.loadOntologyFromPhysicalURI(ontURI));
        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (OntologyWrapper)result;
    }
    
    static OntologyWrapper loadOntology(URI uri){
        OWLOntologyWrapper result = null;
        try {
            result = new OWLOntologyWrapper(oManager.loadOntologyFromPhysicalURI(uri));
        } catch (OWLOntologyCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    
    public static OWLClass OWL_THING(){
        return owlThing;
    }


    
    private OWLOntologyWrapper(OWLOntology o){
        ontology = o;
        reasoner = new ToldClassHierarchyReasoner(oManager);
    }
    
    public URI getOntologyID(){ 
        return ontology.getOntologyID().getOntologyIRI().toURI();
    }
    
    public DirectedGraph<String,NamedEdge> asStringGraph(){
        return null;  //TODO finish me
    }

    @Override
    public DirectedGraph<OntTerm, RelationEdge> asOntologyGraph() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<OntTerm> getPreOrderedTermList(Set<OntTerm> excluded){
        Collection<OWLClass> dirty = new HashSet<OWLClass>(excluded.size());
        for(OntTerm t : excluded){
            dirty.add((OWLClass)t.getBackingTerm());
        }
        reasoner.loadOntologies(Collections.singleton(ontology));       // Get Thing
        List<OntTerm> result = new ArrayList<OntTerm>();
        traverseHierarchy(owlThing, result, dirty);
        return result;
    }


    private void traverseHierarchy(OWLClass cl, List<OntTerm> result, Collection<OWLClass> dirtyExclude){
        if (reasoner.isSatisfiable(cl)) {
            if (!(dirtyExclude.contains(cl))){
                result.add(OWLTerm.getOntTerm(cl));
                dirtyExclude.add(cl);
            }
            /* Find the children and recurse */
            Set<Set<OWLClass>> children = reasoner.getSubClasses(cl);
            for (Set<OWLClass> setOfClasses : children) {
                for (OWLClass child : setOfClasses) {
                    if (!child.equals(cl)) {
                        traverseHierarchy(child, result, dirtyExclude);
                    }
                }
            }
        }
    }
    
    
    
    public  String labelFor( OWLClass clazz) {
        /*
         * Use a visitor to extract label annotations
         */
        
        LabelExtractor le = new LabelExtractor();
        Set<OWLAnnotation> annotations = clazz.getAnnotations(ontology);
        for (OWLAnnotation anno : annotations) {
            anno.accept(le);
        }
        /* Print out the label if there is one. If not, just use the class URI */
        if (le.getResult() != null) {
            return le.getResult().toString();
        } else {            
            return clazz.getIRI().toString();
        }
    }

//    public String labelFor(OWLClass clazz, OWLOntology ont){
//        /*
//         * Use a visitor to extract label annotations
//         */
//        
//        LabelExtractor le = new LabelExtractor();
//        Set<OWLAnnotation> annotations = clazz.getAnnotations(ontology);
//        for (OWLAnnotation anno : annotations) {
//            anno.accept(le);
//        }
//        /* Print out the label if there is one. If not, just use the class URI */
//        if (le.getResult() != null) {
//            return le.getResult().toString();
//        } else {            
//            return clazz.getIRI().toString();
//        }
//       
//    }
//    

}
