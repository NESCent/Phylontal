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
 * Created on Nov 16, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe.view;

import java.awt.Component;
import java.net.URI;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;




public class OntologyListPane extends NamePanel{

    private final static String[] filters = {"None"};

    final DefaultListModel ontListModel = new DefaultListModel();
    final JList ontJList = new JList(ontListModel);
    
    private SelectionObservable selectedState;
    


    static Logger logger = Logger.getLogger(OntologyListPane.class.getName());

    public OntologyListPane(){
        ontJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ontJList.setSelectedIndex(0);
        ontJList.addListSelectionListener(new ontListListener());
        ontJList.setVisibleRowCount(5);  
        this.add(ontJList);
        selectedState = new SelectionObservable(this);
    }

    public void setOntologies(List<URI> listOntologies) {
        ontJList.setCellRenderer(new OntologyCellRenderer());
        ontListModel.clear();
        for(URI u : listOntologies)
            ontListModel.addElement(u);

    }

    @Override
    public boolean isSelected(){
        return ontJList.getSelectedIndex() > -1;
    }

    @Override
    public Object getSelected(){
        if (isSelected())
            return ontListModel.get(ontJList.getSelectedIndex());
        else
            return null;
    }
    
    @Override
    public void clearSelected(){
        ontJList.clearSelection();
    }

    @Override
    public String[] getFilters() {
        return filters;
    }

    class ontListListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == true) {
                int selectedIndex = ontJList.getSelectedIndex();
                if (selectedIndex > -1) {
                    selectedState.setSelected(ontListModel.get(selectedIndex));
                    logger.debug("Selected is " + ontListModel.get(selectedIndex));
                }
                else
                    logger.debug("Nothing is selected");
            }
            else{
                logger.debug("Not multiple adjust; selected index is " + ontJList.getSelectedIndex());
            }
        }

    }

    @Override
    public void addSelectionObserver(Observer o) {
        selectedState.addObserver(o);
    }
    
    class SelectionObservable extends Observable{
        
        OntologyListPane owner;
        
        SelectionObservable(OntologyListPane o){
            owner = o;
        }
        
        void setSelected(Object o){
            setChanged();
            notifyObservers(o);
        }
        
        
    }

    // Display an icon and a string for each object in the list.

    class OntologyCellRenderer extends JLabel implements ListCellRenderer {
        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.
        
        public Component getListCellRendererComponent(
          JList list,              // the list
          Object value,            // value to display
          int index,               // cell index
          boolean isSelected,      // is the cell selected
          boolean cellHasFocus)    // does the cell have focus
        {
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
