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
 * Created on Oct 5, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.fe.view.MatcherTab;
import org.ethontos.phylontal.fe.view.PreferencesDialog;
import org.ethontos.phylontal.fe.view.ProjectTab;
import org.ethontos.phylontal.fe.view.TabView;


public class MainFrame extends JFrame implements ChangeListener{
    
    /**
     * 
     */
    
    private final String aboutTitle = "About Phylontal";

    private String osName;
    private String osVersion;
    
    private UserFrontEnd theApp;
    
    PreferencesDialog preferencesDialog;
    
    private Project project = null;
    
    // File dialog
    private JFileChooser files = new JFileChooser();
    // URL dialog
    private JOptionPane urlPane = null;
    
    private JMenuBar menuBar = new JMenuBar();     // Frame's menu bar

    
    private JTabbedPane tabbedView;
    private ProjectTab projectTab;
    private MatcherTab matcherTab;

    private int currentTabIndex;
    private Component currentTabComponent;

    // icons
    ImageIcon logoIcon;
    
    
    static Logger logger = Logger.getLogger(MainFrame.class.getName());

    private static final long serialVersionUID = 8810169497836516585L;

    public MainFrame(String  title, UserFrontEnd ptheApp){
        super(title);
        theApp = ptheApp;
        //identify the OS

        if (theApp.runningOSX())
            initForOSX();  // OSX specific inits
        else if (theApp.runningWindows()){  // Windows specific inits
            initForWindows();
        }
        else if (theApp.runningLinux()){  
            initForLinux();
        }
        else{
            JOptionPane.showMessageDialog(this, "Phylontal doesn't recognize your operating system (yet!)", "Operating System unknown", JOptionPane.WARNING_MESSAGE);
        }
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // add tabbed pane
        tabbedView = new JTabbedPane(JTabbedPane.TOP);
        tabbedView.addChangeListener(this);
        
        tabbedView.addTab("Project", projectTab = new ProjectTab(theApp));
        tabbedView.addTab("Matcher", matcherTab = new MatcherTab(theApp));
        
        int matchIndex = tabbedView.indexOfComponent(matcherTab);
        tabbedView.setToolTipTextAt(matchIndex,"Disabled until a project is specified");        
        tabbedView.setEnabledAt(matchIndex, false);
        
        int projectIndex = tabbedView.indexOfComponent(projectTab);
        tabbedView.setToolTipTextAt(projectIndex,"Create or Load a project; specify components");
        
        
        currentTabComponent = tabbedView.getSelectedComponent();
        if (currentTabComponent instanceof TabView){
            resetMenuBar();
            JMenuBar tmpBar = ((TabView) currentTabComponent).initializeMenus();
            if (tmpBar != null) {
                menuBar = tmpBar;
                setJMenuBar(menuBar);
            }
        }

        Container content = getContentPane();  // get content pane
        content.setLayout(new BorderLayout());
        content.add(tabbedView, BorderLayout.CENTER);
        
        // add icons

        // add preferences Dialog
        
        preferencesDialog = new PreferencesDialog(this);
       
    }

    private void initForLinux() {
        // TODO Auto-generated method stub
        
    }

    private void initForOSX(){
        // put the menus where Apple suggests
        System.setProperty("apple.laf.useScreenMenuBar", "true");
    }

    private void initForWindows(){
        //TODO something?
    }

    public UserFrontEnd getApp() {
        return theApp;
    }

    @Override
    /**
     * Currently only handles changes in active tab pane.
     */
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source instanceof JTabbedPane){
            JTabbedPane myPane = (JTabbedPane)source;
            int selected = myPane.getSelectedIndex();
            if (currentTabIndex != selected){
                if (myPane.getComponent(selected) instanceof TabView){
                    TabView desiredTab = (TabView)myPane.getComponent(selected);
                    if (desiredTab.useable())
                        viewChanged(currentTabIndex,myPane.getSelectedIndex());  
                    else{
                        JOptionPane.showMessageDialog(this,"The tab you selected is not ready to show","",JOptionPane.ERROR_MESSAGE);
                        myPane.setSelectedIndex(currentTabIndex);
                    }
                }
            }
        }           
        // TODO add support for other events?        
    }

    public void viewChanged(int from, int to){
        viewChanged(tabbedView,from,to);
    }
    
    public void viewChanged(Object source, int from, int to){
        if (source instanceof JTabbedPane){
            JTabbedPane myPane = (JTabbedPane)source;
            currentTabIndex = to;
            currentTabComponent= myPane.getComponent(to);
            if (currentTabComponent instanceof TabView){
                resetMenuBar();
                JMenuBar tmpBar = ((TabView) currentTabComponent).initializeMenus();
                if (tmpBar != null) {
                    menuBar = tmpBar;
                    setJMenuBar(menuBar);
                }
            }
        }
    }
    
    public void enableTab(TabView view, String toolTip) {
        int index = tabbedView.indexOfComponent(view);
        if (index != -1){
            tabbedView.setEnabledAt(index,true);
            tabbedView.setToolTipTextAt(index, toolTip);
        }
    }

    
    private void resetMenuBar(){
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
    }

    

    /**
     * Create about dialog with the menu item as parent
     * 
     * @param owner owning component for the purpose of positioning the dialog
     */ 
    public void showAboutBox() {
        JOptionPane.showMessageDialog(this,
                                      theApp.getAboutString(),
                                      aboutTitle,
                                      JOptionPane.INFORMATION_MESSAGE,
                                      logoIcon);
    }

    public void cleanup() {
        theApp.cleanup();  // last thing to do
    }

    public void showPreferences() {
        // TODO Auto-generated method stub
        
    }

    public List<TabView> getTabs() {
        return Arrays.asList(projectTab,matcherTab);
    }

    
}
