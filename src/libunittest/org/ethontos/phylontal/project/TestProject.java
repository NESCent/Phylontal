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
 * Created on Oct 8, 2009
 * Last updated on Mar 5, 2010
 * 
 */
package org.ethontos.phylontal.project;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.ethontos.phylontal.project.impl.DataFactory;
import org.ethontos.phylontal.project.impl.ExtendableStringMetric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestProject {

    private Project p;
    final URL OntologyURL1 = ClassLoader.getSystemResource("testFiles/spider_anatomy.owl");
    final URL OntologyURL2 = ClassLoader.getSystemResource("testFiles/tick_anatomy.owl");
    File OntologyFile1 = new File(OntologyURL1.getPath());
    File OntologyFile2 = new File(OntologyURL2.getPath());

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        p = ProjectFactory.createProject();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddOntology() {
        p.addOntology(OntologyFile1);
        assertNotNull(p.listOntologies());
    }

    @Test
    public void testAddTerm() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testContinueGuidedMatch() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testListOntologies() {
        p.addOntology(OntologyFile1);
        p.addOntology(OntologyFile2);
        List<URI> result = p.listOntologies();
        assertNotNull(result);
        assert((result.size() == 2));
    }

    @Test
    public void testLoadAlignment() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testLoadTree() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testSaveAlignment() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testSaveMergedOntologies() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testShowOntology() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testShowTree() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testStartGuidedMatch() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testUnguidedMatch() {
        DataFactory factory = p.getDataFactory();
        p.addOntology(OntologyFile1);
        p.addOntology(OntologyFile2);
        List <URI> onts = p.listOntologies();  //need to get this pair from somewhere
        if (onts.size()>1){
            Alignment newAlignment = factory.getAlignment();
            ExtendableStringMetric m = factory.getExtendableStringMetric(ExtendableStringMetric.Metric.MongeElkan);
            OntologyWrapper w1 = p.showOntology(onts.get(0));
            OntologyWrapper w2 = p.showOntology(onts.get(1));
            List <OntTerm> names1 = w1.getPreOrderedTermList(new HashSet<OntTerm>());
            List <OntTerm> names2 = w2.getPreOrderedTermList(new HashSet<OntTerm>());
            p.unguidedMatch(newAlignment, w1, names1, w2, names2, m);
        }
        else
            fail("One or more ontologies failed to load; ont count = " + onts.size());
    }

    @Test
    public void testValidate() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testDoCommand() {
        fail("Not yet implemented"); // TODO
    }

}
