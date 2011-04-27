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
 * Created on Apr 21, 2010
 * Last updated on Apr 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.io.File;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.OntologyWrapper;

public class FileUtil {
    
    static Logger logger = Logger.getLogger(FileUtil.class.getName());

    static OntologyWrapper getWrapper(File f){
        if (OBOOntologyWrapper.canRead(f)){
            return OBOOntologyWrapper.loadOntology(f);
        }
        if (OWLOntologyWrapper.canRead(f)){
            return OWLOntologyWrapper.loadOntology(f);  //TODO worry about detecting OWL vs. OBO files
        }
        logger.error("Failed to read an ontology from " + f);
        return null;
    }
    

}
