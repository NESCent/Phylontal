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
 * Created on Oct 3, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.fe;

import java.awt.EventQueue;
import java.util.Observable;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.project.ProjectFactory;
import org.ethontos.phylontal.fe.view.FEOutput;
import org.ethontos.phylontal.fe.view.TabView;



/**
 * @author peter
 * created Oct 3, 2009
 *
 */
public class UserFrontEnd{
    
    private final static String titleString = "Phylontal Frontend";
    private final static String versionString = titleString + "0.01a";
    private final static String copyrightString = "Copyright Peter E. Midford 2009-2010";
    private final static String aboutString = versionString + "\n" + copyrightString;


    MainFrame mainFrame;

    private String osName;

    private String osVersion;


    private FEOutput textWindow = null;

    private Project project = null;
    private boolean projectDirty = false;

    static Logger logger = Logger.getLogger(UserFrontEnd.class.getName());


    /**
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                BasicConfigurator.configure();
                
                MainFrame mf = new MainFrame(titleString, new UserFrontEnd());
                mf.getApp().runUI(mf);
            }
        });
    }

    UserFrontEnd(){
        super();
        osName = System.getProperty("os.name");
        osVersion = System.getProperty("os.version");  
    }

    void runUI(MainFrame mf){
        mainFrame = mf;
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            logger.error(e);
        }

        final int wndHeight = mf.getToolkit().getScreenSize().height;
        final int wndWidth = mf.getToolkit().getScreenSize().width;

        // Set the position and size of the frame
        mf.setBounds(wndWidth/10, wndHeight/10,wndWidth*4/5,wndHeight*4/5);
        //aFrame.pack();      // not always desirable results           
        mf.setVisible(true);   
        
        project = ProjectFactory.createProject();
        if (project instanceof Observable){
            for (TabView t : mainFrame.getTabs())
                ((Observable) project).addObserver(t);
         }
        projectDirty = false;
        
        // initialize other goodies
        textWindow = new FEOutput();
    }

    public MainFrame getMainFrame(){
        return mainFrame;
    }

    public Project getProject(){
        return project;
    }
    
    public Project newProject(){
        if (projectDirty){
            JOptionPane.showConfirmDialog(mainFrame, "Lose Changes to Existing Project?");
        }
        project = ProjectFactory.createProject();
        if (project instanceof Observable){
           for (TabView t : mainFrame.getTabs())
               ((Observable) project).addObserver(t);
        }
        projectDirty = false;
        return project; 
    }

    
    public void dirtyProject(){
        projectDirty = true;
    }
    
    /**
     * 
     * @return true if running a supported version of OSX
     */
    public boolean runningOSX(){
        return osName.startsWith("Mac OS X");
    }

    /**
     * 
     * @return true if running a supported version of Windows
     */
    public boolean runningWindows(){
        return osName.startsWith("Windows");
    }

    /**
     * 
     * @return true if running a supported version of Linux
     */
    public boolean runningLinux(){
        return osName.startsWith("Linux");
    }

    void cleanup() {
        // TODO Auto-generated method stub
    }
    
    /**
     * @return OWOutput the application's output window
     */
    public FEOutput getTextWindow(){
        return textWindow;
    }



    public String getVersionString(){
        return versionString;
    }
    
    public String getCopyrightString(){
        return copyrightString;
    }

    public String getAboutString(){
        return aboutString;
    }

}
