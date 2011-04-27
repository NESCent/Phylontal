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

import java.awt.Component;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.ethontos.phylontal.project.TipMapping;
import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.fe.view.TermListPane.SelectionObservable;
import org.ethontos.phylontal.fe.view.TermListPane.TermCellRenderer;

public class TipMappingPane extends NamePanel {
    
    private final static String[] filters = {"None"};
    
    DefaultListModel pairListModel = new DefaultListModel();
    JList mappingJList = new JList(pairListModel);
    
    SelectionObservable selectedState;

    TipMapping theMapping;
    
    public TipMappingPane(){
        mappingJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mappingJList.setSelectedIndex(0);
        mappingJList.addListSelectionListener(new MappingListListener());
        mappingJList.setVisibleRowCount(5); 
        this.add(mappingJList);
        selectedState = new SelectionObservable(this);
    }

    public void setMapping(TipMapping tm){
        theMapping = tm;
        mappingJList.setCellRenderer(new TipMappingRenderer());

    }
    
    public void clearTerms() {
        pairListModel.clear();
    }
    
    public void refreshPairs(){
        clearTerms();
        for(Map.Entry<ExternalNode, OntologyWrapper> e : theMapping.getPairings()){
            if (e.getKey() != null && e.getValue() != null){
                String item = e.getKey().getTaxonLabel() + "<->" + e.getValue().getOntologyID();
                    pairListModel.addElement(item);
            }
        }
    }
    
    @Override
    public String[] getFilters() {
        return filters;
    }

    @Override
    public boolean isSelected() {
        return mappingJList.getSelectedIndex() > -1;
    }
    
    @Override
    public Object getSelected() {
        if (isSelected())
            return pairListModel.get(mappingJList.getSelectedIndex());
        else
            return null;
    }
    
    public void clearSelected(){
        //TODO do something
    }

    @Override
    public void addSelectionObserver(Observer o) {
        selectedState.addObserver(o);                
    }

    static class SelectionObservable extends Observable{
        
        TipMappingPane owner;
        
        SelectionObservable(TipMappingPane o){
            owner = o;
        }
    }

    static class MappingListListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            // TODO Auto-generated method stub
            
        }
        
    }


    // Display an icon and a string for each object in the list.

    static class TipMappingRenderer extends JLabel implements ListCellRenderer {
        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.
        
        public Component getListCellRendererComponent(
          JList list,              // the list
          Object value,            // value to display
          int index,               // cell index
          boolean isSelected,      // is the cell selected
          boolean cellHasFocus)    // does the cell have focus
        {
            if (value instanceof String)
                setText((String)value);
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
