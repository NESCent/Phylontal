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
 * Created on Dec 3, 2009
 * Last updated on April 27, 2011
 */
package org.ethontos.phylontal.fe.view;

import java.util.Observer;

import javax.swing.JPanel;

public abstract class NamePanel extends JPanel {
    
    public abstract String[] getFilters();

    public abstract boolean isSelected();
    
    public abstract Object getSelected();
    
    public abstract void clearSelected();
    
    public abstract void addSelectionObserver(Observer o);


}
