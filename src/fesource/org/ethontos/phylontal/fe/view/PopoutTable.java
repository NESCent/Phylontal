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
 * Created on Nov 1, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class PopoutTable extends JPanel {
    
    private JTable content;
    private JScrollPane contentScrollPane;
    
    public PopoutTable(){
        super();
        content = new JTable(new MyTableModel());
        content.setPreferredScrollableViewportSize(new Dimension(500, 70));
        content.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        contentScrollPane= new JScrollPane(content);

        //Set up renderer and editor for the Favorite Color column.
        content.setDefaultRenderer(LeafSet.class,
                                 new LeavesRenderer(true));
        content.setDefaultEditor(LeafSet.class,
                               new LeavesEditor());

        //Add the scroll pane to this panel.
        add(contentScrollPane);

    }
    
    public static class MyTableModel extends AbstractTableModel{

        @Override
        public int getColumnCount() {
            return 2;  // for now...
        }

        @Override
        public int getRowCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    class LeafSet{
        
    }
    
    static class LeavesRenderer implements TableCellRenderer{

        public LeavesRenderer(boolean b) {
            // TODO Auto-generated constructor stub
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    static class LeavesEditor implements TableCellEditor{

        @Override
        public Object getCellEditorValue() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                Object value, boolean isSelected, int row, int column) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void cancelCellEditing() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean stopCellEditing() {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
    
}
