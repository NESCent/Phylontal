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
 * Created on Dec 2, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe.view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class NamePanelHeader extends JPanel {

   // private String title;

    JLabel titleLabel;
    JComboBox filterChoices;
    List<Object> ButtonSpecifiers;
    JPanel buttonArea = new JPanel();
    
    public NamePanelHeader(String title, NamePanel owner) {
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        titleLabel = new JLabel(title);
        filterChoices = new JComboBox(owner.getFilters());
        this.add(titleLabel);
        this.add(filterChoices);
        this.add(buttonArea);
    }
    
    
}
