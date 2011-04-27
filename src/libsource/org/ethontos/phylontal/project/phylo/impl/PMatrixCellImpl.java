/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jun 23, 2010
 * Last updated on Jun 23, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import org.ethontos.phylontal.project.phylo.PMatrixCell;

public class PMatrixCellImpl<T> implements PMatrixCell<T> {
    
    private T contents;

    public PMatrixCellImpl(T value) {
        contents = (T)value;
        // TODO Auto-generated constructor stub
    }


    @Override
    public T getValue() {
        // TODO Auto-generated method stub
        return contents;
    }


}
