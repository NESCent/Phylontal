/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jun 14, 2010
 * Last updated on Jun 14, 2010
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
