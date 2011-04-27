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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.project.TipMapping;
import org.ethontos.phylontal.project.phylo.ExternalNode;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.fe.UserFrontEnd;

public class ProjectTab extends TabView {


    final private String NOPROJECTERROR = "No project created or loaded";

    /**
     * 
     */

    //Areas
    private OntologyListPane oList;
    private JPanel oPanel;
    private JPanel treePanel;
    //private JScrollPane treeScrollPanel;
    private SimpleTreePane treePane;
    private JPanel mappingPanel;
    private TipMappingPane mapPane;


    //Menus
    final private JMenuBar tabMenus = new JMenuBar();

    //other things

    GraphTree selectedTree;
    final Collection<ExternalNode> selectedNodes = new HashSet<ExternalNode>();

    ExternalNode selectedTaxon = null;
    URI selectedOntology = null;
    
    static Logger logger = Logger.getLogger(ProjectTab.class.getName());

    private static final long serialVersionUID = 3579478099385898953L;


    public ProjectTab(UserFrontEnd app){
        super(app);
        theApp = app;
        initializeMenus();
        initializeAreas();
    }


    public JMenuBar initializeMenus() {
        if (tabMenus.getComponentCount()==0){
            final JMenu fileMenu = new JMenu("File");
            final JMenu editMenu = new JMenu("Edit");
            final JMenu helpMenu = new JMenu("Help");
            final JMenu windowMenu = new JMenu("Window");  // probably OSX specfic

            // submenus
            final JMenu openMenu = new JMenu("Open");

            tabMenus.add(fileMenu);
            tabMenus.add(editMenu);
            if (theApp.runningOSX()){
                tabMenus.add(windowMenu);
            }
            tabMenus.add(helpMenu);
            
            fileMenu.add(makeNewProjectItem());
            fileMenu.add(openMenu);
            openMenu.add(makeOpenProjectItem());
            openMenu.add(makeOpenPhylogenyItem(this));
            openMenu.add(makeOpenOntologyFileItem());
            openMenu.add(makeOpenOntologyURLItem());
            openMenu.add(makeOpenMatrixFileItem());
            fileMenu.addSeparator();
            fileMenu.add(makeSaveItem());
            fileMenu.add(makeSaveAsItem());

            helpMenu.add(makeShowLicenseItem(theApp));
        }
        return tabMenus;
    }

    private boolean initializeAreas(){
        setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

        treePane = new SimpleTreePane();
        NamePanelHeader treeLabel = new NamePanelHeader("Current Tree",treePane);
        treePanel = new JPanel();
        treePanel.setLayout(new BorderLayout());
        treePanel.add(treeLabel,BorderLayout.PAGE_START);
        treePane.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        treePanel.add(treePane,BorderLayout.CENTER);

        mapPane = new TipMappingPane();
        mapPane.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));        
        NamePanelHeader mappingHeader = new NamePanelHeader("Species  Ontology",mapPane);
        mappingPanel = new JPanel();
        //        //mappingPanel.setMaximumSize(d);
        mappingPanel.setLayout(new BorderLayout());
        mappingPanel.add(mappingHeader,BorderLayout.PAGE_START);
        mappingPanel.add(mapPane,BorderLayout.CENTER);
        mappingPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));

        oList = new OntologyListPane();
        oList.addSelectionObserver(this);
        oList.setBorder(BorderFactory.createLineBorder(Color.CYAN));  // visual debugging
        NamePanelHeader ontologyHeader = new NamePanelHeader("Loaded Ontologies",oList);
        oPanel = new JPanel();
        oPanel.setLayout(new BorderLayout());
        oPanel.add(ontologyHeader, BorderLayout.PAGE_START);
        oPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));


        oPanel.add(oList, BorderLayout.CENTER);
        treePanel.setBorder(BorderFactory.createLineBorder(Color.RED));

        this.add(treePanel);
        this.add(Box.createRigidArea(new Dimension(5,0)));
        this.add(mappingPanel);
        this.add(Box.createRigidArea(new Dimension(5,0)));
        this.add(oPanel);
        return true;    //TODO finish me
    }

    @Override
    public boolean useable() {
        return true;
    }


    @Override
    protected void showHelp() {
        // TODO Auto-generated method stub

    }


    @Override
    protected void showPreferences() {
        // TODO Auto-generated method stub

    }

    /**
     * @param o the object (a selection observable from either the tree pane or the ontology pane).
     * @param either a URI specifying an ontology or an ExternalNode - a tip specifying a taxon
     * This handles creating bindings of tips and taxon-specific ontologies after one of each has been selected
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof URI){
            logger.debug("Project tab bound the ontology to :" + arg);
            selectedOntology = (URI)arg;
        }
        if (arg instanceof ExternalNode){
            logger.debug("Project tab bound the taxon to : " + arg);
            selectedTaxon = (ExternalNode)arg;
        }
        if (selectedTaxon != null && selectedOntology != null){
            logger.debug("Ready to bind a node and ontology");
            int userChoice = JOptionPane.showConfirmDialog(this,"Join "+ selectedTaxon.getTaxonLabel() + " and " + selectedOntology + "?");
            if (userChoice == JOptionPane.YES_OPTION){
                final Project project = theApp.getProject();
                TipMapping theMapping = project.getTipMapping();
                theMapping.addPair(selectedTaxon, project.showOntology(selectedOntology));
                mapPane.refreshPairs();
                
                // finally select both selections (we should grey them out at some point...
                treePane.clearSelected();
                oList.clearSelected();
                selectedOntology = null;
                selectedTaxon = null;
                
            }
            else {  // assume last selection was mistaken, clear only it
                if (arg instanceof URI) { //last selection was an ontology
                    oList.clearSelected();
                    selectedOntology = null;                    
                }
                else if (arg instanceof ExternalNode){  //last selection was a tip
                    treePane.clearSelected();
                    selectedTaxon = null;                
                }
            }
        }
    }


    //Listener classes here

    private JMenuItem makeOpenOntologyFileItem(){
        final JMenuItem item = new JMenuItem("Open Ontology from File...");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (theApp.getProject() != null){
                    final Project project = theApp.getProject();
                    final JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(project.ontologyExtensions());
                    final int returnVal = chooser.showOpenDialog(theApp.getMainFrame());
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        project.addOntology(chooser.getSelectedFile());
                        theApp.dirtyProject();
                        oList.setOntologies(project.listOntologies());
                        oList.setVisible(true);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(theApp.getMainFrame(), NOPROJECTERROR);
                }
            }            
        });
        return item;
    }

    private JMenuItem makeOpenOntologyURLItem(){
        final JMenuItem item = new JMenuItem("Open Ontology from URL..."); 
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (theApp.getProject() != null){

                    // TODO finish this

                }
                else {
                    JOptionPane.showMessageDialog(theApp.getMainFrame(), NOPROJECTERROR);
                }
            }
        });
        return item;
    }


    private JMenuItem makeNewProjectItem(){
        final JMenuItem item = new JMenuItem("New Project..."); 
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                theApp.newProject();  //this also takes care of signing up all the tabViews as observers
            }
        });
        return item;
    }

    
    private JMenuItem makeOpenProjectItem(){
        final JMenuItem item = new JMenuItem("Open Project...");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
            }            
        });
        return item;
    }

    private JMenuItem makeOpenPhylogenyItem(final ProjectTab owner){
        final JMenuItem item = new JMenuItem("Open Phylogeny...");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
            if (theApp.getProject() != null){
                final Project project = theApp.getProject();
                final JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(project.phylogenyExtensions());
                final int returnVal = chooser.showOpenDialog(theApp.getMainFrame());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    project.loadTrees(chooser.getSelectedFile());
                    theApp.dirtyProject();
                    selectedTree = project.selectTree(0);
                    treePane.setTree(selectedTree);
                    treePane.addSelectionObserver(owner);
                    mapPane.setMapping(project.getTipMapping());
                    mapPane.addSelectionObserver(owner);
                }
            }
            else {
                JOptionPane.showMessageDialog(theApp.getMainFrame(), NOPROJECTERROR);
            }}});
        return item;
    }
    
    private JMenuItem makeOpenMatrixFileItem(){
        final JMenuItem item = new JMenuItem("Open Annotated Matrix...");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (theApp.getProject() != null){
                    final Project project = theApp.getProject();
                    final JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(project.phylogenyExtensions());
                    final int returnVal = chooser.showOpenDialog(theApp.getMainFrame());
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        project.loadCharacterMatrices(chooser.getSelectedFile());
                    }
                }
            }
        });
        return item;
    }
    
    private JMenuItem makeSaveItem(){
        final JMenuItem item = new JMenuItem("Save");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        return item;
    }
    

    private JMenuItem makeSaveAsItem(){
        final JMenuItem item = new JMenuItem("Save As...");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try{
                    final Project project = theApp.getProject();
                    final JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(project.projectExtensions());
                    final int returnVal = chooser.showSaveDialog(theApp.getMainFrame());
                    if(returnVal == JFileChooser.APPROVE_OPTION) {
                        project.saveProjectAs(chooser.getSelectedFile());
                    }
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(theApp.getMainFrame(),"Error opening file for output: " + ex.toString(),"",JOptionPane.ERROR_MESSAGE);
                }
            }  
        });
        return item;
    }

    

}



