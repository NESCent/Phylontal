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
 * Created on Nov 1, 2009
 * Last updated on Mar 5, 2010
 * 
 */
package org.ethontos.phylontal.project;

import java.net.URI;
import java.util.Collection;

/**
 * Wrapper class for ontology terms (classes and individuals)
 * @author peter
 * created Mar 5, 2010
 *
 */
public interface OntTerm {

    public Object getBackingTerm();
    
    public URI getURI();
    
    public boolean isRoot();
    
    public String getLabel(OntologyWrapper o);
    
    public Collection<MatchSet> getMatchSets();
}
