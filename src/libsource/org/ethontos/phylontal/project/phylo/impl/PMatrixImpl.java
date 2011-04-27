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
 * Created on Jun 14, 2010
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import java.util.ArrayList;

import org.ethontos.phylontal.project.phylo.CharStateType;
import org.ethontos.phylontal.project.phylo.PMatrixCell;
import org.ethontos.phylontal.project.phylo.PMatrix;
import org.ethontos.phylontal.project.phylo.StateSet;
import org.ethontos.phylontal.project.phylo.TaxonBlock;

class PMatrixImpl<T extends CharStateType> implements PMatrix<T> {
    
    private String name;
    private TaxonBlock taxa;
    private ArrayList<Integer> rows;  // each integer is an index to the taxa block


    @Override
    public void addTaxon(String taxonId) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setName(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setUID(String id) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public PMatrixCell<T> makeCell(T value) {
        PMatrixCell<T> result = new PMatrixCellImpl<T>(value);
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StateSet makeStateSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addCharacter(String id, StateSet s) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCell(String charID, String taxonID, PMatrixCell<T> w) {
        // TODO Auto-generated method stub
        
    }

    

}
