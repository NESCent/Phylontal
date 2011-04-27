/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009 Peter E. Midford
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
 * Created on Oct 13, 2009
 * Last updated on Oct 13, 2009
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.ethontos.phylontal.project.AnnotationTriple;

public class TripleStore {

    private static HashMap<Object,ArrayList<AnnotationTriple>> tripleStore = new HashMap<Object,ArrayList<AnnotationTriple>>();


    public void put(Object subject, AnnotationTriple triple) {
        if (!tripleStore.containsKey(subject)){
            final ArrayList<AnnotationTriple> newList = new ArrayList<AnnotationTriple>();
            newList.add(triple);
            tripleStore.put(subject, newList);
        }
        else{
            final ArrayList<AnnotationTriple> oldList = tripleStore.get(subject);
            oldList.add(triple);
        }   
    }
    
    public AnnotationTriple getFirstSubject(Object s){
        if (tripleStore.containsKey(s))
            return tripleStore.get(s).get(0);
        else
            return null;
    }

    public Collection<AnnotationTriple> getAllSubject(Object s){
        if (tripleStore.containsKey(s))
            return (Collection<AnnotationTriple>)tripleStore.get(s);
        else 
            return null;
    }
}
