/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2011 Peter E. Midford
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
 * Created on Oct 12, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.project.impl.TripleStore;
import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.PMatrix;
import org.ethontos.phylontal.project.phylo.PhyloBlock;
import org.ethontos.phylontal.project.phylo.PhyloEdge;
import org.ethontos.phylontal.project.phylo.PhyloNode;
import org.ethontos.phylontal.project.phylo.StateSet;
import org.ethontos.phylontal.project.phylo.TaxonBlock;
import org.nexml.model.Annotatable;
import org.nexml.model.Annotation;
import org.nexml.model.CategoricalMatrix;
import org.nexml.model.CharacterStateSet;
import org.nexml.model.ContinuousMatrix;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Matrix;
import org.nexml.model.MatrixCell;
import org.nexml.model.MolecularMatrix;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.OTU;
import org.nexml.model.OTUs;
import org.nexml.model.Tree;
import org.nexml.model.TreeBlock;



public class NexmlReader {

    private Document xmlDocument = null;
    GraphTree gTree;
    private List<GraphTree> trees = new ArrayList<GraphTree>();
    private List<PMatrix<?>> matrices = new ArrayList<PMatrix<?>>();
    private List<TaxonBlock> taxaBlockList;
    private List<PhyloBlock> treeBlockList;
//    private List<MatrixBlock> matrixBlockList;
    private Project owner;

    private static URI phoalURI;
    private String phoalPrefix = "phoal";
    private String phoalTaxaUID = phoalPrefix + ":taxaUID";
    private String phoalTaxonUID = phoalPrefix + ":taxonUID";
    private String phoalTreeUID = phoalPrefix + ":treeUID";
    private static Properties pPredicateHandlerMapping = new Properties();
    static Logger logger = Logger.getLogger(NexmlReader.class.getName());

    
    public NexmlReader(Project p){
        owner = p;
    }

    public void readFromFile(File nexmlFile){
        try {
            processStream(new FileInputStream(nexmlFile), nexmlFile.getPath());
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }

    public void readFromURL(URL u){
        try {
            processStream(u.openStream(),u.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public List<GraphTree> getTrees(){
        return trees;
    }


    private void processStream(InputStream s, String inputSource){
        try {
            xmlDocument = DocumentFactory.parse(s);
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Properties properties = new Properties();
        InputStream resStream = null;
        try {
            resStream = this.getClass().getResourceAsStream("predicateHandlerMapping.properties");
            if (resStream == null){
                URL bar = this.getClass().getResource("predicateHandlerMapping.properties");
                System.out.println("Tried to find resources at: " + bar);
            }
            else {
                properties.load(resStream);
            }
            properties.put("path", inputSource);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            if (resStream != null)
                try {
                    resStream.close();
                } catch (IOException e) {
                    System.out.println("Error trying to close predicateHandlerMapping stream: " + resStream.toString());
                    e.printStackTrace();
                }
        }
        buildPhylogenyFromNexml(xmlDocument, properties);
        buildMatricesFromNexml(xmlDocument, properties);
    }


    void buildPhylogenyFromNexml(Document xmlDocument, Properties properties){
        final List<OTUs> xmlOTUsList = xmlDocument.getOTUsList();     
        taxaBlockList = readTaxaBlocks(xmlOTUsList);
        final List<TreeBlock> xmlTreeBlockList = xmlDocument.getTreeBlockList();
        treeBlockList = readTreeBlocks(xmlTreeBlockList,taxaBlockList);
        for(PhyloBlock b : treeBlockList){
            for(int i=0;i<b.getSize();i++)
                trees.add((GraphTree)b.getTree(i));
        }
    }


    List<TaxonBlock> readTaxaBlocks (List<OTUs> xmlOTUsList) {
        final List<TaxonBlock> result = new ArrayList<TaxonBlock>(xmlOTUsList.size());
        for(OTUs xmlOTUs : xmlOTUsList){
            final int xmlOTUListSize = xmlOTUs.getAllOTUs().size();
            TaxonBlock taxa = TaxonBlockFactory.makeTaxonBlock(xmlOTUs.getLabel(),xmlOTUs.getId(),xmlOTUListSize);
            if (taxa == null)
                System.err.println("Failed to make taxon Block");
            final TripleStore ts = owner.getTripleStore();
            for ( OTU xmlOTU : xmlOTUs.getAllOTUs() ) {
                Object graphOTU = taxa.addTaxon(xmlOTU.getLabel(),xmlOTU.getId());
                readAnnotations(ts,xmlOTU,graphOTU);
            }
            result.add(taxa);
        }   
        return result;
    }   


    List<PhyloBlock>readTreeBlocks (List<TreeBlock> xmlTreeBlocks, List<TaxonBlock> allTaxa) {
        final List<PhyloBlock> result = new ArrayList<PhyloBlock>(xmlTreeBlocks.size());
        for (TreeBlock xmlTreeBlock : xmlTreeBlocks){
            OTUs xmlOTUs = xmlTreeBlock.getOTUs();
            final TaxonBlock referencedTaxa =lookupTaxonBlock(allTaxa, xmlOTUs.getId()); 
            System.out.println("Tree is " + xmlTreeBlock + "Taxon Block is " + referencedTaxa);
            PhyloBlock trees = PhyloBlockFactory.makePhyloBlock();
            for (Network<?> xmlNetwork : xmlTreeBlock) {
                final GraphTree tree = readTree(xmlNetwork, referencedTaxa);
                trees.addTree(tree);
            } 
            result.add(trees);
        }
        return result;
    }
    
    public GraphTree readTree(Network<?> xmlNetwork, TaxonBlock taxa){
        final TripleStore ts = owner.getTripleStore();
        GraphTree tree = GraphTreeFactory.getTree(taxa);
        tree.setName(xmlNetwork.getLabel());
        readAnnotations(ts,xmlNetwork,tree);
        Set<Node> xmlNodeSet = xmlNetwork.getNodes();
        Node xmlRoot = null;
        //The code below suggests something very sloppy is going on in nexml trees
        if ( xmlNetwork instanceof Tree ) {
            xmlRoot = ((Tree<?>)xmlNetwork).getRoot();
        }
        final Iterator<Node> xmlnIt = xmlNodeSet.iterator();
        while(xmlnIt.hasNext() && xmlRoot == null){
            final Node xmlNode = xmlnIt.next();
            if (xmlNetwork.getInNodes(xmlNode).size() == 0 ) {
                xmlRoot = xmlNode;
            }
        }
        readInternalNode(tree,xmlNetwork, xmlRoot,taxa);
        return tree;
    }

    private PhyloNode readInternalNode(GraphTree tree, Network<?> xmlNetwork, Node xmlNode, TaxonBlock taxa){
        PhyloNode parent = PhyloNodeFactory.getPhyloNode();
        for (Node xmlChild : xmlNetwork.getOutNodes(xmlNode)){
            readBranch(tree,parent,xmlNetwork,xmlChild,taxa);
        }
        return parent;
    }

    private ExternalNode readExternalNode(GraphTree tree, Network<?> xmlNetwork, Node xmlNode, TaxonBlock taxa){
        int index = taxa.getTaxonIndexById(xmlNode.getOTU().getId());
        if (index != -1){
            return taxa.getExternalNode(index);
        }
        else{
            throw new RuntimeException("Failed to find taxon with id " + xmlNode.getOTU().getId());
        }
            
    }
    
    private void readBranch(GraphTree tree, PhyloNode parent, Network<?> xmlNetwork, Node xmlNode, TaxonBlock taxa){
        PhyloEdge branch;
        if (xmlNetwork.getOutNodes(xmlNode).isEmpty()) { // it's external
            ExternalNode child = readExternalNode(tree, xmlNetwork,  xmlNode, taxa);
            branch = tree.addBranch(parent,child);
        }
        else { // it's internal
            PhyloNode child = readInternalNode(tree, xmlNetwork, xmlNode, taxa);
            branch = tree.addBranch(parent, child);
        }
        //TODO assign a length to the branch
    }

    
    private TaxonBlock lookupTaxonBlock(List<TaxonBlock> tbList, String id){
        final Iterator<TaxonBlock> tbIter = tbList.iterator();
        while(tbIter.hasNext()){
            final TaxonBlock tb = tbIter.next();
            if (id.equals(tb.getUid()))
                return tb;
        }
        return null;
    }

    
    

    void readAnnotations(TripleStore ts, Annotatable annotatable,Object subject) {
        Set<Annotation> allAnnotations = annotatable.getAllAnnotations();
        for ( Annotation annotation : allAnnotations ) {
            String property = annotation.getProperty();
            if ("".equals(property)) {
                property = annotation.getRel();
            }
            String[] curie = property.split(":");
            String localProperty = curie[1]; // NameReference;  lookup in properties        
            Object value = annotation.getValue();
            ts.put(subject, owner.getDataFactory().getTriple(subject, property, value));  //this is either a hack or absolutely correct
            PredicateHandler ph = getPredicateHandler(subject,localProperty,value);
        }

    }
    
    void buildMatricesFromNexml(Document xmlDocument, Properties properties){
        final List<Matrix<?>> xmlMatrixList = xmlDocument.getMatrices();   
        if (!xmlMatrixList.isEmpty()){
            final List<OTUs> xmlOTUsList = xmlDocument.getOTUsList();     
            taxaBlockList = readTaxaBlocks(xmlOTUsList);
            matrices= readMatrices(xmlMatrixList,taxaBlockList,matrices);
            for(PMatrix<?> m : matrices){
            }
        }
    }


    List<PMatrix<?>> readMatrices(List<Matrix<?>> xmlMatrixList, List<TaxonBlock> allTaxa, List<PMatrix<?>> mList) {
        for(Matrix<?> matrix : xmlMatrixList){
            readMatrix(matrix,allTaxa,mList);
        }
        // TODO Auto-generated method stub
        return mList;
    }
    
    void readMatrix(Matrix<?> xmlMatrix, List<TaxonBlock> allTaxa, List <PMatrix<?>> mList){
        PMatrix<?> newMatrix;
        if (xmlMatrix instanceof ContinuousMatrix){
            newMatrix = PhyloFactory.INSTANCE.<ContinuousCharState>getMatrixWrapper();            
        }
        else if (xmlMatrix instanceof CategoricalMatrix){
            newMatrix =  PhyloFactory.INSTANCE.<DiscreteCharState>getMatrixWrapper();
        }
        else if (xmlMatrix instanceof MolecularMatrix){  // this type of matrix isn't well supported by Phylontal at present; just treat as discrete
            newMatrix =  PhyloFactory.INSTANCE.<DiscreteCharState>getMatrixWrapper();            
        }
        else {
            logger.error("Character Matrix from NeXML has unknown type: " + xmlMatrix);
            return;
        }
        newMatrix.setName(xmlMatrix.getLabel());
        final List<org.nexml.model.Character> xmlCharacterList = xmlMatrix.getCharacters();
        for (org.nexml.model.Character xmlCharacter : xmlCharacterList){
            CharacterStateSet xmlStateSet = xmlCharacter.getCharacterStateSet();
            Set<org.nexml.model.CharacterState> xmlStates = xmlStateSet.getCharacterStates();
            StateSet ss = newMatrix.makeStateSet();
            for(org.nexml.model.CharacterState xmlState : xmlStates){
                logger.info("State is " + xmlState);
                ss.addState(xmlState.getSymbol().toString());  // this may be a hack since getSymbol returns an Object
            }
            newMatrix.addCharacter(xmlCharacter.getId(),ss);
        }
        OTUs xmlOTUs = xmlMatrix.getOTUs();
        final TaxonBlock referencedTaxa =lookupTaxonBlock(allTaxa, xmlOTUs.getId()); 
        for (OTU xmlOTU : xmlOTUs.getAllOTUs()){
            String xmlOTUID = xmlOTU.getId();
            System.out.println("OTU id is " + xmlOTUID);
            newMatrix.addTaxon(xmlOTUID);
            for (org.nexml.model.Character xmlCharacter : xmlCharacterList){
                CharacterStateSet xmlStateSet = xmlCharacter.getCharacterStateSet();
                Set<org.nexml.model.CharacterState> xmlStates = xmlStateSet.getCharacterStates();

                MatrixCell<?> xmlCell = xmlMatrix.getCell(xmlOTU, xmlCharacter);
               // PMatrixCell<?> newCell = newMatrix.addCell(myOTU, myCharacter);
               // newCell.setValue(xmlCell.getValue());
            }
        }
        mList.add(newMatrix);
    }

    public static Properties getPredicateHandlerMapping(){
        return pPredicateHandlerMapping;
    }
    
    private PredicateHandler getPredicateHandler(Object subject, String predicate, Object value) {
        String handlerClassName = getPredicateHandlerMapping().getProperty(predicate);
        PredicateHandler ph = null;
        if (handlerClassName != null){
            try {
                Class<?> handlerClass = Class.forName(handlerClassName);
                Constructor<?> declaredConstructor = handlerClass.getDeclaredConstructor(Object.class,String.class,Object.class);
                ph = (PredicateHandler) declaredConstructor.newInstance(subject,predicate,value);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        if (ph == null)
            ph = new PredicateHandlerImpl(subject,predicate,value);
        logger.info("Using predicateHandler " + ph.toString());
        return null;
    }

    public LinkedHashMap<URI, PMatrix> getMatrices(LinkedHashMap<URI, PMatrix> matrices) {
        // TODO Auto-generated method stub
        return matrices;
    }   



}
