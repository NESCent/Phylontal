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
 * Created on Jun 22, 2010
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.phylo.impl;

import org.ethontos.phylontal.project.phylo.CharStateType;
import org.ethontos.phylontal.project.phylo.PMatrix;

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
