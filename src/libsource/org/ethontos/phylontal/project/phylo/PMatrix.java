/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jun 14, 2010
 * Last updated on Jun 14, 2010
 * 
 */
package org.ethontos.phylontal.project.phylo;


public interface PMatrix<T extends CharStateType> {
    
    
    public PMatrixCell<T> makeCell(T value);
    
    public StateSet makeStateSet();
    
    void setUID(String id);
    
    void setName(String name);
    
    void addCharacter(String id, StateSet s);   //simply adds one column (character) to the end
    
    void addTaxon(String taxonID);  
    
    void setCell(String charID, String taxonID, PMatrixCell<T> w);

}
