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
 * Created on Oct 19, 2009
 * Last updated on Mar 5, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.ethontos.phylontal.project.AnnotationTriple;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.project.ProjectFactory;
import org.ethontos.phylontal.project.impl.TripleStore;
import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.PhyloNode;
import org.ethontos.phylontal.project.phylo.TaxonBlock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Matrix;
import org.nexml.model.Network;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;
import org.nexml.model.TreeBlock;

public class TestNexmlReader {
    
    private Project p;
    private TripleStore ts;
    private NexmlReader testReader;
    final URL testURL1 = ClassLoader.getSystemResource("testFiles/TreeSix.xml");
    final URL testURL2 = ClassLoader.getSystemResource("testFiles/multitest.xml");
    final URL testURL3 = ClassLoader.getSystemResource("testFiles/Lundberg_1992.xml");
    private Document testDocument1;
    private Document testDocument2;
    private Document testDocument3;
    private OTU firstOTU;
    private List<TaxonBlock> testTaxonBlockList1;
    private List<TaxonBlock> testTaxonBlockList2;
    private List<TreeBlock> testTreeBlockList1;
    private List<TreeBlock> testTreeBlockList2;
    private TreeBlock testTreeBlock1;
    private TreeBlock testTreeBlock2;
    private Network<?> testTreeNetwork1;
    private Network<?> testTreeNetwork2;
    //private Network<?> testXMLTree;
    private GraphTree testTree1;
    private GraphTree testTree2;
    private List<Matrix<?>> mList1; 

    
    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        p = ProjectFactory.createProject();
        ts = p.getTripleStore();
        testReader = new NexmlReader(p);
        testDocument1 = DocumentFactory.parse(testURL1.openStream());
        testDocument2 = DocumentFactory.parse(testURL2.openStream());
        testDocument3 = DocumentFactory.parse(testURL3.openStream());
        assertNotNull(testDocument1);
        testTreeBlockList1 = testDocument1.getTreeBlockList();
        assertFalse((0 == testTreeBlockList1.size()));
        testTreeBlock1 = testTreeBlockList1.get(0);
        List<OTUs> testOTUs1 = testDocument1.getOTUsList();
        assertNotNull(testOTUs1);
        testTaxonBlockList1 = testReader.readTaxaBlocks(testOTUs1);
        testTreeNetwork1 = testTreeBlock1.iterator().next();
        testTree1 = GraphTreeFactory.getTree(testTaxonBlockList1.get(0));

        
        List<OTUs> testOTUs2 = testDocument2.getOTUsList();
        assertNotNull(testOTUs2);
        testTaxonBlockList2 = testReader.readTaxaBlocks(testOTUs2);
        testTree2 = GraphTreeFactory.getTree(testTaxonBlockList2.get(0));

        OTUs firstOTUBlock = testOTUs1.get(0);
        List<OTU> firstOTUList = firstOTUBlock.getAllOTUs();
        firstOTU = firstOTUList.get(0);
        testTreeBlockList2 = testDocument2.getTreeBlockList();
        
        mList1 = new ArrayList<Matrix<?>>();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReadAnnotations(){
        assertNotNull(ts);
        assertNotNull(firstOTU);
        testReader.readAnnotations(ts, firstOTU, firstOTU);
        Collection <AnnotationTriple> triples = ts.getAllSubject(firstOTU);
        assertNotNull(triples);
        int count = 0;
        Iterator<AnnotationTriple> tIter = triples.iterator();
        while(tIter.hasNext()){
            AnnotationTriple triple = tIter.next();
            System.out.println("Triple is " + triple);
            count++;
        }
        assertTrue((count>0));
    }
    
    @Test
    public void testReadTaxaBlocks(){
        List<OTUs> xmlOTUsList1 = testDocument1.getOTUsList(); 
        for (OTUs xmlOTUs : xmlOTUsList1){
            int xmlOTUListSize = xmlOTUs.getAllOTUs().size();
            System.out.println("xmlOTUs = " + xmlOTUs + "xmlOTUListSize = " + xmlOTUListSize);            
        }
        List<TaxonBlock> taxaBlocks1 = testReader.readTaxaBlocks(xmlOTUsList1);
        for (TaxonBlock tBlock : taxaBlocks1){
            System.out.println(tBlock.getLabel());
            String [] names = tBlock.getTaxaLabels();
            for(String name : names){
                System.out.println("   " + name);
            }
        }
        
        List<OTUs> xmlOTUsList2 = testDocument2.getOTUsList();     
        for (OTUs xmlOTUs : xmlOTUsList2){
            int xmlOTUListSize = xmlOTUs.getAllOTUs().size();
            System.out.println("xmlOTUs = " + xmlOTUs + "xmlOTUListSize = " + xmlOTUListSize);            
        }
        List<TaxonBlock> taxaBlocks2 = testReader.readTaxaBlocks(xmlOTUsList2);
        for (TaxonBlock tBlock : taxaBlocks2){
            System.out.println(tBlock.getLabel());
            String [] names = tBlock.getTaxaLabels();
            for(String name : names){
                System.out.println("   " + name);
            }
        }
    }
    
    @Test
    public void testReadTree(){
        List<TaxonBlock> blocks = testTaxonBlockList1;
        TaxonBlock tb1 = blocks.iterator().next();
        GraphTree t = testReader.readTree(testTreeNetwork1,tb1);
        for(ExternalNode e : t.getExternalNodes())
            System.out.println(" External taxon " + ((TerminalOTU)(tb1.getTaxon(e.getTaxonNumber()))).getLabel());
        for(PhyloNode i : t.getInternalNodes())
            System.out.println(" Internal " + i.getLabel());
    }
    
    @Test
    public void testReadTreeBlocks() {
        testReader.readTreeBlocks(testTreeBlockList1, testTaxonBlockList1);
        testReader.readTreeBlocks(testTreeBlockList2, testTaxonBlockList2);
    }
    
    
    @Test
    public void testNexmlURLReader() {
        testReader.readFromURL(testURL1);
        testReader.readFromURL(testURL2);
    }

    @Test
    public void testReadMatrices(){
        testReader.readFromURL(testURL3);
        //testReader.readMatrices(testDocument3.getMatrices(),mList1);
    }
    
}
