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
 * Created on Oct 13, 2009
 * Last updated on April 27, 2011
 *
 */
package org.ethontos.phylontal.project.phylo.impl;

import java.util.ArrayList;
import java.util.HashMap;


import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.TaxonBlock;
import org.ethontos.phylontal.project.phylo.PhyloBlock;

public class PhyloBlockFactory {
    
    private PhyloBlockFactory(){
        
    }

    static PhyloBlock makePhyloBlock(){
        return new TreeBlockImpl();
    }
    
    static PhyloBlock makePhyloBlock(int size){
        return new TreeBlockImpl(size);
    }
    
}


class TreeBlockImpl implements PhyloBlock {

    private TaxonBlock taxa;
    private ArrayList<GraphTree> trees;
    private HashMap<String,GraphTree> names;
    private String name;
    
    TreeBlockImpl(){
        trees = new ArrayList<GraphTree>();
        names = new HashMap<String,GraphTree>();
    }
    
    TreeBlockImpl(int size){
        trees = new ArrayList<GraphTree>(size);
    }


    @Override
    public int addTree(GraphTree tree) {
        trees.add(tree);
        return trees.size();
    }
    
    public int addTree(GraphTree tree, String name){
        names.put(name, tree);
        trees.add(tree);
        return trees.size();
    }

    @Override
    public GraphTree getTree(int index) {
        return trees.get(index);
    }

    @Override
    public void setName(String newName) {
        name = newName;
    }

    public String getName(){
        return name;
    }
    
    @Override
    public void setTree(GraphTree tree, int index) {
        trees.add(index,tree);
    }

    @Override
    public int getSize() {
        return trees.size();
    }
    
    

}
