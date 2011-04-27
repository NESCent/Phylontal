/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jun 6, 2010
 * Last updated on Jun 6, 2010
 * 
 */
package org.ethontos.phylontal.project.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;

import org.apache.log4j.BasicConfigurator;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestOBOOntologyWrapper {

    // relativize this
    final private static String ZFA_OBO = "/Users/peter/Projects/workspace/phylontal/test/testFiles/zebrafish_anatomy.obo";
    final private static File ZFA_OBO_FILE = new File(ZFA_OBO);
    final private static String ZFA_OWL = "/Users/peter/Projects/workspace/phylontal/test/testFiles/zebrafish_anatomy.owl";
    final private static File ZFA_OWL_FILE = new File(ZFA_OWL);


    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCanRead() {
        assertTrue(OBOOntologyWrapper.canRead(ZFA_OBO_FILE));
        assertFalse(OBOOntologyWrapper.canRead(ZFA_OWL_FILE));
    }

    
    @Test
    public void testLoadOntology() {
        OntologyWrapper o = OBOOntologyWrapper.loadOntology(ZFA_OBO_FILE);
        assertNotNull(o);
    }

    @Test
    public void testGetOntologyID() {
        OntologyWrapper o = OBOOntologyWrapper.loadOntology(ZFA_OBO_FILE);
        assertNotNull(o);
        URI u = o.getOntologyID();
        assertNotNull(u);
    }
    
    
    @Test
    public void testAsStringGraph() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testAsOntologyGraph() {
        fail("Not yet implemented"); // TODO
    }


    @Test
    public void testGetPreOrderedTermList() {
        fail("Not yet implemented"); // TODO
    }

}
