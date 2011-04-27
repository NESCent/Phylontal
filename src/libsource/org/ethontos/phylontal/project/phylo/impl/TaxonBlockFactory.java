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
 * Created on Oct 23, 2009
 * Last updated on mar 5, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import java.util.ArrayList;
import java.util.HashMap;


import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.project.phylo.TaxonBlock;
import org.ethontos.phylontal.project.phylo.impl.PhyloNodeFactory.PhyloNodeImpl;

public class TaxonBlockFactory {

    private TaxonBlockFactory(){
    }

    static TaxonBlock makeTaxonBlock(String name, String uid){
        return new TaxonBlockImpl(name,uid);
    }

    public static TaxonBlock makeTaxonBlock(String name, String uid, int size){
        return new TaxonBlockImpl(name, uid, size);
    }
    


    static class TaxonBlockImpl implements TaxonBlock {

        
        private ArrayList<TerminalOTU> taxa;
        private HashMap<String,TerminalOTU> taxonNames;
        private HashMap<String,TerminalOTU> taxonUIDs;
        private String label;
        private String uid;

        TaxonBlockImpl(String p_label, String p_uid){
            label = p_label;
            uid = p_uid;
            taxa = new ArrayList<TerminalOTU>();
            taxonNames = new HashMap<String,TerminalOTU>();
            taxonUIDs = new HashMap<String,TerminalOTU>();
        }

        TaxonBlockImpl(String p_label, String p_uid, int size){
            label = p_label;
            uid = p_uid;
            taxa = new ArrayList<TerminalOTU>(size);
            taxonNames = new HashMap<String,TerminalOTU>();
            taxonUIDs = new HashMap<String,TerminalOTU>();
        }

        @Override
        public String getUid() {
            return uid;
        }

        @Override
        public String getLabel() {
            return label;
        }
        
        @Override
        public ExternalNode getExternalNode(int taxonNumber){
            return new ExternalNodeImpl(this,taxonNumber);
        }



        public Object addTaxon(String name, String uid){
            TerminalOTU newTaxon = new TerminalOTU();
            newTaxon.setLabel(name);
            newTaxon.setUID(uid);
            taxa.add(newTaxon);
            taxonNames.put(name, newTaxon);
            taxonUIDs.put(uid,newTaxon);
            return newTaxon;
        }


        @Override
        public String[] getTaxaIds() {
            final String[] result = new String[taxa.size()];
            for(int i= 0;i<result.length;i++){
                result[i]= taxa.get(i).getUID();
            }
            return result;
        }

        @Override
        public String[] getTaxaLabels() {
            final String[] result = new String[taxa.size()];
            for(int i= 0;i<result.length;i++){
                result[i]= taxa.get(i).getLabel();
            }
            return result;
        }

        @Override
        public Object getTaxon(int index) {
            return taxa.get(index);
        }

        @Override
        public int getTaxonIndexById(String id) {
            if (!taxonUIDs.containsKey(id))
                return -1;
            TerminalOTU t = taxonUIDs.get(id);
            for(int i=0;i<taxa.size();i++)
                if (t.equals(taxa.get(i)))
                    return i;
            return -1;
        }


    }
    


    static class ExternalNodeImpl extends PhyloNodeImpl implements ExternalNode{

        private int taxonNumber;
        private TaxonBlock taxa;

        private ExternalNodeImpl(TaxonBlock pTaxa, int tn){
            super();
            taxa = pTaxa;
            taxonNumber = tn;
        }
        @Override
        public int getTaxonNumber() {
            return taxonNumber;
        }
        
        @Override
        public String getTaxonLabel() {
            return taxa.getTaxaLabels()[taxonNumber];
        }



    }

}