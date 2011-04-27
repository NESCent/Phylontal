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
 * Created on Oct 8, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.fe.UserFrontEnd;


public abstract class TabView extends JPanel implements AbstractView, Observer{

    protected UserFrontEnd theApp;
    
    public TabView(UserFrontEnd app) {
        super();
        theApp = app;
    }
    
    protected JMenuItem addMenuItem(JMenu menu, String name, ActionListener listener) {
        final JMenuItem item = new JMenuItem(name); 
        item.addActionListener(listener);
        menu.add(item);         // Add the menu item
        return item;
    }
    
    protected JMenu addSubMenu(JMenu menu, String name){
        final JMenu subMenu = new JMenu(name);
        menu.add(subMenu);
        return subMenu;
    }
    

   // Methods for displaying error dialogs
    
    public void fileNotFoundMessage(FileNotFoundException ex){
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,"Error opening file: " + ex.toString(),"",JOptionPane.ERROR_MESSAGE);                
    }

    
    public QueryValues queryUser(String query){
        int userChoice = JOptionPane.showConfirmDialog(this,query, query, JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        switch (userChoice){
        case JOptionPane.YES_OPTION:
            return AbstractView.QueryValues.YES;
        case JOptionPane.NO_OPTION:
            return AbstractView.QueryValues.NO;
        default: 
            return AbstractView.QueryValues.CANCEL;
        }
    }

    public QueryValues queryUser(String query,String title){
        int userChoice = JOptionPane.showConfirmDialog(this,query, title, JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        switch (userChoice){
        case JOptionPane.YES_OPTION:
            return AbstractView.QueryValues.YES;
        case JOptionPane.NO_OPTION:
            return AbstractView.QueryValues.NO;
        default: 
            return AbstractView.QueryValues.CANCEL;
        }
    }

    /**
     * Only need one copy of this...
     * @return true if yes selected (so quit the application)
     */
    public boolean quitDialog(){
        final int value = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to quit?", 
                "Quit Phylontal", 
                JOptionPane.YES_NO_OPTION);
        return (value == JOptionPane.YES_OPTION);  //just return whether yes was selected
    }
    
    protected UserFrontEnd getApp(){
        return theApp;
    }
    
    protected Project getProject(){
        return theApp.getProject();
    }

    
    protected abstract void showHelp();
    
    protected abstract void showPreferences();
    
    public abstract JMenuBar initializeMenus();
       
        

    protected JMenuItem makeShowLicenseItem(final UserFrontEnd app){
        final JMenuItem item = new JMenuItem("Show License");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                app.getTextWindow().showLicense();            
            }
        });
        return item;

    }

}
