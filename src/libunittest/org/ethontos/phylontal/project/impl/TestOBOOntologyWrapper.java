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
 *  * 
 * Created on Jun 6, 2010
 * Last updated on April 27, 2011
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
