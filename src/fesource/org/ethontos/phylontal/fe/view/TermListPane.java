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

import java.awt.Component;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;

public class TermListPane extends NamePanel{

    private final static String[] filters = {"None"};
    
    DefaultListModel termListModel = new DefaultListModel();
    JList termJList = new JList(termListModel);
    
    SelectionObservable selectedState;

    public TermListPane(){
        termJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        termJList.setSelectedIndex(0);
        termJList.addListSelectionListener(new termListListener());
        termJList.setVisibleRowCount(5);  
        this.add(termJList);
        selectedState = new SelectionObservable(this);
    }

    
    public void setTerms(OntologyWrapper o) {
        termListModel.clear();
        Set<OntTerm> exclude = new HashSet<OntTerm>();
        termJList.setCellRenderer(new TermCellRenderer(o));
        for(OntTerm t : o.getPreOrderedTermList(exclude))
            termListModel.addElement(t);    //maybe we need a renderer for OntTerms here...
    }
    
    @Override
    public String[] getFilters() {
        return filters;
    }

    @Override
    public boolean isSelected() {
        return termJList.getSelectedIndex() > -1;
    }
    
    @Override
    public Object getSelected() {
        if (isSelected())
            return termListModel.get(termJList.getSelectedIndex());
        else
            return null;
    }
    
    @Override
    public void clearSelected(){
        //TODO do something
    }

    @Override
    public void addSelectionObserver(Observer o) {
        selectedState.addObserver(o);        
    }

    static class SelectionObservable extends Observable{
        
        TermListPane owner;
        
        SelectionObservable(TermListPane o){
            owner = o;
        }
    }

    static class termListListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            // TODO Auto-generated method stub
            
        }
        
    }

    // Display an icon and a string for each object in the list.

    static class TermCellRenderer extends JLabel implements ListCellRenderer {
        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.

        OntologyWrapper ow;
        TermCellRenderer(OntologyWrapper o){
            ow = o;
        }
        
        public Component getListCellRendererComponent(
          JList list,              // the list
          Object value,            // value to display
          int index,               // cell index
          boolean isSelected,      // is the cell selected
          boolean cellHasFocus)    // does the cell have focus
        {
            if (value instanceof OntTerm)
                setText(((OntTerm)value).getLabel(ow));
            else
                setText(value.toString());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

    

    
}
