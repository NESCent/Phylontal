/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2010 Peter E. Midford
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
 * Created on Dec 14, 2009
 * Last updated on Mar 4, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.PhyloEdge;
import org.ethontos.phylontal.project.phylo.PhyloNode;
import org.ethontos.phylontal.project.phylo.TaxonBlock;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;



public class GraphTreeFactory {

    static Logger logger = Logger.getLogger(GraphTreeFactory.class.getName());

    
    public static GraphTree getTree(TaxonBlock pTaxa){
        return new GraphTreeImpl(pTaxa);
    }

    
    static class GraphTreeImpl implements GraphTree{
        
        private String name;
        private String UID;
        SimpleDirectedWeightedGraph<PhyloNode,PhyloEdge> backingGraph;
        private PhyloNode root;  //we'll try setting the root by the last addInternal...
        private TaxonBlock taxa;
        
        GraphTreeImpl(TaxonBlock pTaxa){
            backingGraph = new SimpleDirectedWeightedGraph<PhyloNode,PhyloEdge>(PhyloEdge.class);
            taxa = pTaxa;
        }
        
        public TaxonBlock getTaxa(){
            return taxa;
        }
        
        public void setName(String p_name){
            name = p_name;
        }
        
        public String getName(){
            return name;
        }
        
        public PhyloNode addInternalNode(List <PhyloNode> children){
            PhyloNode newParent = PhyloNodeFactory.getPhyloNode();
            backingGraph.addVertex(newParent);
            for(PhyloNode p : children){
                backingGraph.addVertex(p);
                backingGraph.addEdge(newParent, p);
            }
            root = newParent;  // will this work?
            return newParent;
        }
     
        public ExternalNode addExternalNode(int taxonNumber){
            return taxa.getExternalNode(taxonNumber);
        }

        @Override
        public PhyloEdge addBranch(PhyloNode parent, PhyloNode child) {
            if (!backingGraph.containsVertex(parent))
                backingGraph.addVertex(parent);
            backingGraph.addVertex(child);
            root = parent;   //risky, but should work with nexml files
            return backingGraph.addEdge(parent,child);
        }

        @Override
        public PhyloNode getRoot() {
            return root;
        }

        @Override
        public Set<ExternalNode> getExternalNodes() {
            Set<ExternalNode> result = new HashSet<ExternalNode>();  
            for (PhyloNode n : backingGraph.vertexSet()){
                if (n instanceof ExternalNode)
                    result.add((ExternalNode)n);
            }
            return result;
        }

        @Override
        public Set<PhyloNode> getInternalNodes() {
            Set<PhyloNode> result = new HashSet<PhyloNode>();  
            for (PhyloNode n : backingGraph.vertexSet()){
                if (!(n instanceof ExternalNode))
                    result.add(n);
            }
            return result;
        }

        @Override
        public List<PhyloNode> getChildren(PhyloNode node) {
            Set<PhyloEdge> outEdges = backingGraph.outgoingEdgesOf(node);
            List<PhyloNode> result = new ArrayList<PhyloNode>();
            for(PhyloEdge e : outEdges){
                result.add(backingGraph.getEdgeTarget(e));
            }
            return result;
        }

        @Override
        public PhyloNode getParent(PhyloNode node) {
            Set<PhyloEdge> inEdges = backingGraph.incomingEdgesOf(node);
            if (inEdges.size()>1){
                logger.error("Node has multiple parents" + node);
            }
            if (inEdges.size()== 0){
                logger.error("Node has no parents: " + node.getLabel());
                
            }
            return backingGraph.getEdgeSource(inEdges.iterator().next());
        }

        @Override
        public boolean isExternal(PhyloNode node) {
            if (node instanceof ExternalNode && backingGraph.outDegreeOf(node)==0)
                return true;
            return false;
        }
        
    }
    
}
