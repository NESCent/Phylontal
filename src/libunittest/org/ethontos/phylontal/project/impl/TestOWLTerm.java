/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2010 Peter E. Midford
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
 * Created on Nov 4, 2009
 * Last updated on Mar 5, 2010
 * 
 */
package org.ethontos.phylontal.project.impl;

import static org.junit.Assert.*;


import java.net.URI;

import org.ethontos.phylontal.project.OntTerm;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class TestOWLTerm {

    private OWLClass unitTestClass1 = null;
    private OWLClass unitTestClass2 = null;
    private OWLOntologyManager manager; 
    
    @Before
    public void setUp() throws Exception {
     manager = OWLManager.createOWLOntologyManager();
     IRI ontologyIRI = IRI.create("http://www.ethontos.org/unittests/phylontal/owlTermTest");  // fake, but it could be real...
     URI physicalURI = URI.create("file:/tmp/owlTermTest");
     SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, physicalURI);
     manager.addIRIMapper(mapper);

     OWLOntology ontology = manager.createOntology(ontologyIRI);
     assertNotNull(ontology);
     OWLDataFactory factory = manager.getOWLDataFactory();
     assertNotNull(factory);
     unitTestClass1 = factory.getOWLClass(IRI.create(ontologyIRI + "#A"));
     assertNotNull(unitTestClass1);
     unitTestClass2 = factory.getOWLClass(IRI.create(ontologyIRI + "#B"));
     assertNotNull(unitTestClass2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetOntTerm() {
        OntTerm term1 = OWLTerm.getOntTerm(unitTestClass1);
        assertNotNull(term1);
        OntTerm term2 = OWLTerm.getOntTerm(unitTestClass2);
        assertNotNull(term2);
        assertTrue(!(term1.equals(term2)));   // crazy, but can't hurt to check
    }

    @Test
    public void testGetBackingTerm() {
        OntTerm term1 = OWLTerm.getOntTerm(unitTestClass1);
        OntTerm term2 = OWLTerm.getOntTerm(unitTestClass2);
        Object backing1 = term1.getBackingTerm();
        assertNotNull(backing1);
        assertTrue(backing1 instanceof OWLClass);
        Object backing2 = term2.getBackingTerm();
        assertNotNull(backing2);
        assertTrue(backing2 instanceof OWLClass);
    }


}
