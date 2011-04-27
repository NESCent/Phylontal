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
 * Last updated on Mar 5, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo;

import java.util.List;
import java.util.Set;



public interface GraphTree {

    public void setName(String p_name);
    
    public String getName();
    
    public ExternalNode addExternalNode(int taxonIndex);
    
    public PhyloNode addInternalNode(List <PhyloNode> children);
    
    public PhyloEdge addBranch(PhyloNode parent, PhyloNode child);
    
    public PhyloNode getRoot();

    public Set<ExternalNode> getExternalNodes();

    public Set<PhyloNode> getInternalNodes();

    public PhyloNode getParent(PhyloNode node);

    public boolean isExternal(PhyloNode node);

    public List<PhyloNode> getChildren(PhyloNode node);
    
    public TaxonBlock getTaxa();
    
    
    
}
