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
 * Created on Nov 4, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestOWLOntologyWrapper {
    
    // relativize this
    final private static String ZFA_OWL = "/Users/peter/Projects/workspace/phylontal/test/testFiles/zebrafish_anatomy.owl";
    final private static File ZFA_OWL_FILE = new File(ZFA_OWL);
    final private static URI ZFA_OWL_URI = ZFA_OWL_FILE.toURI();  // maybe try the real URI?

    private OntologyWrapper testOntFromFile;
    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        testOntFromFile = OWLOntologyWrapper.loadOntology(ZFA_OWL_FILE);                
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoadOntologyFile() {
       OntologyWrapper fileTestWrapper = OWLOntologyWrapper.loadOntology(ZFA_OWL_FILE);
     }

    @Test
    public void testLoadOntologyURI() {
       OntologyWrapper uriTestWrapper = OWLOntologyWrapper.loadOntology(ZFA_OWL_URI);
    }

//    @Test
//    public void testAsStringGraph() {
//        fail("Not yet implemented"); // TODO
//    }
//
//    @Test
//    public void testAsOntologyGrpah() {
//        fail("Not yet implemented"); // TODO
//    }

    @Test
    public void testGetPreOrderedTermList() {
        List <OntTerm> terms = testOntFromFile.getPreOrderedTermList(new HashSet<OntTerm>());
        assertNotNull(terms);
        assertTrue(terms.size()>2000);
        System.out.println(terms.size());
    }

}
