/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jun 22, 2010
 * Last updated on Jun 22, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import org.ethontos.phylontal.project.phylo.PMatrixCell;
import org.ethontos.phylontal.project.phylo.PMatrix;
import org.ethontos.phylontal.project.phylo.CharStateType;

public enum PhyloFactory {

    INSTANCE;
    
    /**
     * Creates and returns a MatrixWrapper
     * @param <T>
     * @param p_type specifies the data type of the matrix
     * @return 
     * @return
     */
    public <T extends CharStateType> PMatrix<T> getMatrixWrapper(){
        return new PMatrixImpl<T>();
     }
    

}
