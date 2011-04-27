/*
 * Phylontal FrontEnd - a GUI frontend for Phylontal
 * Copyright 2009-2011 Peter E. Midford
 * This file is part of Phylontal Frontend
 *
 * Phylontal Frontend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Phylontal Frontend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Phylontal Frontend.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Created on Oct 10, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe.view;

import java.io.FileNotFoundException;

public interface AbstractView {

    enum QueryValues {YES,NO,CANCEL};

    /**
     * 
     * @return true if the view thinks it can usefully display something
     */
    public abstract boolean useable();
    
    // for now, mostly a set of methods for accepting errors
    
    public void fileNotFoundMessage(FileNotFoundException ex);
    

    
    /**
     * 
     * @param query
     * @return QueryValues
     */

    public QueryValues queryUser(String query);

    public QueryValues queryUser(String query,String title);
    

}
