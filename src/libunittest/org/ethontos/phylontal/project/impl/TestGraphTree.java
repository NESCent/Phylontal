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
 * Created on Dec 16, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.BasicConfigurator;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.project.ProjectFactory;
import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.PhyloNode;
import org.ethontos.phylontal.project.phylo.TaxonBlock;
import org.ethontos.phylontal.project.phylo.impl.GraphTreeFactory;
import org.ethontos.phylontal.project.phylo.impl.NexmlReader;
import org.ethontos.phylontal.project.phylo.impl.PhyloNodeFactory;
import org.ethontos.phylontal.project.phylo.impl.TaxonBlockFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestGraphTree {
    
    private GraphTree testTree;
    private GraphTree readTree;
    private ExternalNode leaf1 = null;
    private ExternalNode leaf2 = null;
    private PhyloNode parent = null;
    
    // this needs to be relativized
    final private static String SIXTREENAME = "/Users/peter/Projects/workspace/phylontal/test/testFiles/TreeSix.xml";
    
    final private static String TESTTAXONBLOCKID = "trees55";
    final private static String TESTTAXONBLOCKLABEL = "Test trees";
    final private static int TEXTTAXONBLOCKSIZE = 2;
    
    private Project p;
    private static TaxonBlock testTaxa;
    private static Object leafTaxon1; 
    private static Object leafTaxon2;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        BasicConfigurator.configure();
        testTaxa = TaxonBlockFactory.makeTaxonBlock(TESTTAXONBLOCKLABEL,TESTTAXONBLOCKID,2);
        leafTaxon1 = testTaxa.addTaxon("Xenopus","leaf1");
        leafTaxon2 = testTaxa.addTaxon("Dario","leaf2");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        p = ProjectFactory.createProject();
        testTree = GraphTreeFactory.getTree(testTaxa);
        assertFalse(testTree==null);
        NexmlReader reader = new NexmlReader(p);
        reader.readFromFile(new File(SIXTREENAME));
        List<GraphTree>treeList = reader.getTrees();
        assertFalse(treeList==null);
        assertFalse(treeList.isEmpty());
        readTree = treeList.get(0);
        Assert.assertNotNull(readTree);
    }

    @After
    public void tearDown() throws Exception {
    }
    

    @Test
    public void testSetName(){
        final String testName = "TestSet";
        testTree.setName(testName);
        assertTrue(testName.equals(testTree.getName()));
    }
    
    @Test
    public void testAddExternalNode(){
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        assertFalse(leaf1==null);
    }

    
    @Test
    public void testAddBranch(){
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
    }
 

    @Test
    public void testGetRoot() {
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
        assertFalse(testTree.getRoot() == null);
        assertTrue(parent.equals(testTree.getRoot()));   
    }

    @Test
    public void testGetExternalNodes() {
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        parent.setLabel("Parent");
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
        Set<ExternalNode> leaves = testTree.getExternalNodes();
        assertFalse(leaves == null);
        assertFalse(leaves.isEmpty());
        assertTrue(leaves.size() == 2);
        assertTrue(leaves.contains(leaf1));
        assertTrue(leaves.contains(leaf2));
    }

    @Test
    public void testGetInternalNodes() {
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
        Set<PhyloNode> internals = testTree.getInternalNodes();
        assertFalse(internals == null);
        assertFalse(internals.isEmpty());
        assertTrue(internals.size() == 1);
        assertTrue(internals.contains(parent));
    }

    @Test
    public void testGetChildren() {
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        parent.setLabel("parent");
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
        assertFalse(testTree.getChildren(parent) == null);
        assertTrue(testTree.getChildren(parent).size() ==2);
        assertTrue(testTree.getChildren(parent).contains(leaf1));
        assertTrue(testTree.getChildren(parent).contains(leaf2));
    }

    @Test
    public void testGetParent() {
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        parent.setLabel("parent");
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
        assertFalse(testTree.getParent(leaf1) == null);
        assertTrue(testTree.getParent(leaf1).equals(parent));
        assertFalse(testTree.getParent(leaf2) == null);
        assertTrue(testTree.getParent(leaf2).equals(parent));
    }

    @Test
    public void isExternal() {
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
        assertTrue(testTree.isExternal(leaf1));
        assertTrue(testTree.isExternal(leaf2));
        assertFalse(testTree.isExternal(parent));
    }

    @Test
    public void testAddInternalNode(){
        leaf1 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf1"));
        leaf2 = testTree.addExternalNode(testTaxa.getTaxonIndexById("leaf2"));
        assertFalse(leaf1 == null);
        assertFalse(leaf2 == null);
        List<PhyloNode>children = new ArrayList<PhyloNode>(2);
        children.add(leaf1);
        children.add(leaf2);
        PhyloNode parent = testTree.addInternalNode(children);
        parent.setLabel("parent");
        assertFalse(parent == null);
        testTree.addBranch(parent, leaf1);
        testTree.addBranch(parent, leaf2);
        assertFalse(testTree.getParent(leaf1) == null);
        assertTrue(testTree.getParent(leaf1).equals(parent));
        assertFalse(testTree.getParent(leaf2) == null);
        assertTrue(testTree.getParent(leaf2).equals(parent));
    }

}
