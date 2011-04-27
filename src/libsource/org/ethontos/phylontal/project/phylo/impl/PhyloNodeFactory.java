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
 * Created on Oct 12, 2009
 * Last updated on Mar 4, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import org.ethontos.phylontal.project.phylo.PhyloNode;

public class PhyloNodeFactory {


    public static PhyloNode getPhyloNode(){
        return new PhyloNodeImpl();
    }

    static class PhyloNodeImpl implements PhyloNode{


        private String label;
        private String uid;


        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }    

}