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
 * Created on Jun 23, 2010
 * Last updated on April 27, 2011
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
