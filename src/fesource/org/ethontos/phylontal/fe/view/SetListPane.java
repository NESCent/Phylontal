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
 * Created on Nov 25, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe.view;

import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ethontos.phylontal.project.Alignment;
import org.ethontos.phylontal.project.MatchSet;

public class SetListPane extends JPanel {
    
    final DefaultListModel setListModel = new DefaultListModel();
    final JList setJList = new JList(setListModel);
    
    public SetListPane(){
        setJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setJList.setSelectedIndex(0);
        setJList.addListSelectionListener(new SetListListener());
        setJList.setVisibleRowCount(5);  
        this.add(setJList);
    }

    
    public void setSets(Alignment a) {
        setListModel.clear();
        Set<MatchSet> exclude = new HashSet<MatchSet>();
        for(MatchSet t : a.getMatches(exclude))
            setListModel.addElement(t);
    }
    
    static class SetListListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            // TODO Auto-generated method stub
            
        }
        
    }


}
