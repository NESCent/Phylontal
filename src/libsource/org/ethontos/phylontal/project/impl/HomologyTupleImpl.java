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
 * Created on Apr 21, 2010
 * Last updated on Apr 21, 2010
 * 
 */
package org.ethontos.phylontal.project.impl;

import org.ethontos.phylontal.project.HomologyTuple;

public class HomologyTupleImpl implements HomologyTuple {
    
    
    
    
    
    HomologyTuple getHomologyTuple(String tax1, String e1, String tax2, String e2, String evidenceCode){
        return new HomologyTupleImpl(tax1, e1, tax2, e2, evidenceCode);
    }
    
    private HomologyTupleImpl(String tax1, String e1, String tax2, String e2, String evidenceCode){
        
    }

    @Override
    public String getEntityID1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEntityID2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEvidence() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getParentEntityID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getParentTaxonID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTaxonID1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTaxonID2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toExcelText() {
        // TODO Auto-generated method stub
        return null;
    }

}
