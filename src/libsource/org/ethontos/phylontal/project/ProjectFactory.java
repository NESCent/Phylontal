/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2011 Peter E. Midford
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
 * Created on Oct 5, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project;

import org.ethontos.phylontal.project.impl.ProjectImpl;

/**
 * Factory class for phylontal projects
 * @author peter
 * created Mar 5, 2010
 *
 */
public class ProjectFactory {
    
    
    /**
     * Constructs a new project
     * 
     * @return ProjectImpl
     */
    public static Project createProject(){
        return new ProjectImpl();
    }

}
