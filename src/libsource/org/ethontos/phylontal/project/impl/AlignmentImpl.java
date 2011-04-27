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
 * Created on Nov 25, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.ethontos.phylontal.project.Alignment;
import org.ethontos.phylontal.project.MatchSet;

class AlignmentImpl implements Alignment {

    static final List<MatchSet>EMPTY_LIST = new ArrayList<MatchSet>();
    
    @Override
    public List<MatchSet> getMatches(Set<MatchSet> exclude) {        
        // TODO Auto-generated method stub
        return EMPTY_LIST;
    }

}
