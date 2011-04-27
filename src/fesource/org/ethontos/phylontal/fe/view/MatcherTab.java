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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.Alignment;
import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.project.impl.DataFactory;
import org.ethontos.phylontal.project.impl.ExtendableStringMetric;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.fe.UserFrontEnd;

public class MatcherTab extends TabView {

    private final String SAVEOPENERROR = "Error opening file for output: ";

    public enum Config {SETONTOLOGY, TWOONTOLOGY, TWOSET, UNDEFINED};

    OntTerm selectedLeft = null;
    OntTerm selectedRight = null;


    //Menus
    final private JMenuBar tabMenus = new JMenuBar();


    // Match Menu Items

    private JMenuItem suggestMatchItem;
    private JMenuItem suggestMatchFromNiece;

    //Help Menu Items
    private JMenuItem showLicenseItem;
    private JMenuItem matchHelpItem;


    private SetListPane resultsPane;
    private SetListPane setsPane1;
    private SetListPane setsPane2;
    private TermListPane termsPane1;
    private TermListPane termsPane2;
    private SimpleTreePane treePane;

    private JPanel resultNamesPanel = new JPanel();  // should always be a setsPane
    private JPanel leftNamesPanel = new JPanel();
    private JPanel rightNamesPanel = new JPanel();
    private JPanel treePanel = new JPanel();

    private JLabel resultTitle = new JLabel();
    private JLabel leftTitle = new JLabel();
    private JLabel rightTitle = new JLabel();
    private JLabel treeTitle = new JLabel();

    private Config paneConfig = Config.UNDEFINED;  //default for new projects

    static Logger logger = Logger.getLogger(MatcherTab.class.getName());


    public MatcherTab(UserFrontEnd app) {
        super(app);
        initializeAreas();
        setConfiguration(Config.TWOONTOLOGY);
        this.setVisible(true);
    }

    @Override
    public boolean useable() {
        Project p = theApp.getProject();
        if (p == null)
            return false;
        GraphTree t = p.showTree();
        if (t == null)
            return false;
        if (p.listOntologies().size()<2){
            logger.debug("Ontologies count is " + p.listOntologies().size());
            return false;
        }
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

    @Override
    public JMenuBar initializeMenus() {
        if (tabMenus.getComponentCount() == 0){
            final JMenu fileMenu = new JMenu("File");
            final JMenu editMenu = new JMenu("Edit");
            final JMenu matchMenu = new JMenu("Match");
            final JMenu helpMenu = new JMenu("Help");
            
            //submenus
            final JMenu saveAlignmentAsMenu = new JMenu("Save Alignment As...");

            tabMenus.add(fileMenu);
            tabMenus.add(editMenu);
            tabMenus.add(matchMenu);
            if (theApp.runningOSX()){
                JMenu windowMenu = new JMenu("Window");
                tabMenus.add(windowMenu);
            }
            tabMenus.add(helpMenu);

            fileMenu.add(makeSaveProjectAsItem());
            fileMenu.add(saveAlignmentAsMenu);

            saveAlignmentAsMenu.add(makeSaveAlignmentAsTuplesItem());
            saveAlignmentAsMenu.add(makeSaveAlignmentAsOntologyItem());

            matchMenu.add(makeUnGuidedMatchItem());
            matchMenu.add(makeSuggestMatchItem());
            matchMenu.add(makeSuggestMatchFromNieceItem());
            
            helpMenu.add(makeShowLicenseItem(theApp));
        }
        return tabMenus;        
    }

    private void initializeAreas(){
        resultsPane = new SetListPane();
        resultsPane.setBorder(BorderFactory.createLineBorder(Color.RED));
        resultsPane.setMinimumSize(new Dimension(200,200));
        resultNamesPanel.add(resultTitle);
        resultNamesPanel.add(resultsPane);

        termsPane1 = new TermListPane();
        termsPane1.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        termsPane2 = new TermListPane();
        termsPane2.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        treePane = new SimpleTreePane();
        treePane.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        treeTitle.setText("Current Tree");
        treePanel.setMinimumSize(new Dimension(200,200));
        treePanel.add(treeTitle);
        treePanel.add(treePane);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        resultNamesPanel.setMinimumSize(new Dimension(200,200));
        resultNamesPanel.setLayout(new BoxLayout(resultNamesPanel,BoxLayout.Y_AXIS));
        leftNamesPanel.setLayout(new BoxLayout(leftNamesPanel,BoxLayout.Y_AXIS));
        rightNamesPanel.setLayout(new BoxLayout(rightNamesPanel,BoxLayout.Y_AXIS));
        treePanel.setLayout(new BoxLayout(treePanel,BoxLayout.Y_AXIS));
        this.add(resultNamesPanel);
        leftNamesPanel.setMinimumSize(new Dimension(200,200));
        this.add(leftNamesPanel);
        rightNamesPanel.setMinimumSize(new Dimension(200,200));
        this.add(rightNamesPanel);
        treePanel.setMinimumSize(new Dimension(200,200));
        this.add(treePanel);
    }

    public void setConfiguration(MatcherTab.Config c){
        switch (c){
        case SETONTOLOGY:
            configureSetOntology();
            break;
        case TWOSET:
            configureTwoSet();
            break;
        case TWOONTOLOGY:
        default:
            configureTwoOntology();
        }
    }

    private void configureTwoSet(){
        if (!paneConfig.equals(Config.TWOSET)){
            leftNamesPanel.removeAll();
            leftNamesPanel.add(leftTitle);
            leftNamesPanel.add(setsPane1);
            rightNamesPanel.removeAll();
            rightNamesPanel.add(rightTitle);
            rightNamesPanel.add(setsPane2);
            paneConfig = Config.TWOSET;
        }
    }

    private void configureSetOntology(){
        if (!paneConfig.equals(Config.SETONTOLOGY)){
            leftNamesPanel.removeAll();
            leftNamesPanel.add(leftTitle);
            leftNamesPanel.add(setsPane1);
            rightNamesPanel.removeAll();
            rightNamesPanel.add(rightTitle);
            rightNamesPanel.add(setsPane1);
            paneConfig = Config.SETONTOLOGY;
        }
    }

    private void configureTwoOntology(){
        if (!paneConfig.equals(Config.TWOONTOLOGY)){
            leftNamesPanel.removeAll();
            leftNamesPanel.add(leftTitle);
            leftNamesPanel.add(termsPane1);
            rightNamesPanel.removeAll();
            rightNamesPanel.add(rightTitle);
            rightNamesPanel.add(termsPane2);
            paneConfig = Config.TWOONTOLOGY;
        }
    }

    private void updatePanels(){
        GraphTree selectedTree = theApp.getProject().selectTree(0);
        treePane.setTree(selectedTree);
        // cheating here
        List <URI> onts = getProject().listOntologies();
        if (onts.size()>1){
            OntologyWrapper w1 = getProject().showOntology(onts.get(0));
            OntologyWrapper w2 = getProject().showOntology(onts.get(1));
            termsPane1.setTerms(w1);
            termsPane2.setTerms(w2);
        }
        else {
            JOptionPane.showMessageDialog(this, "Ontologies haven't been loaded yet", "No Ontologies", JOptionPane.ERROR_MESSAGE);
        }
        treePane.setVisible(true);
    }


    public void update(Observable o, Object arg){
        // super.update(o,arg);  //tabview is currently abstract, so nothing to do here
        if (o.equals(getProject())){
            if (useable()){
                theApp.getMainFrame().enableTab(this, "Construct alignment");
                updatePanels();
            }
        }
        if (arg != null && arg instanceof GraphTree){
            // if another tree is selected, something should happen in the tree pane
        }
        if (selectedLeft != null && selectedRight != null){
            switch(paneConfig){
            case TWOONTOLOGY:{

                logger.debug("Ready to match terms from right and left"); 
                int userChoice = JOptionPane.showConfirmDialog(this,"Join "+ selectedLeft.getURI() + " and " + selectedRight.getURI() + "?");
                if (userChoice == JOptionPane.YES_OPTION){
                    final Project project = getProject();
                    // TipMapping theMapping = project.getTipMapping();
                    // theMapping.addPair(selectedTaxon, project.showOntology(selectedOntology));
                    // mapPane.refreshPairs();

                    // finally select both selections (we should grey them out at some point...
                    termsPane1.clearSelected();
                    termsPane2.clearSelected();
                    selectedLeft = null;
                    selectedRight = null;

                }
                else {  // assume last selection was mistaken, clear only it
                    if (arg.equals(selectedLeft)) { //last selection from the left panel (ontology term)
                        termsPane1.clearSelected();
                        selectedLeft = null;                    
                    }
                    else if (arg.equals(selectedRight)){  //last selection was from the right panel (ontology term)
                        termsPane2.clearSelected();
                        selectedRight = null;                
                    }
                }
                break;
            }
            case SETONTOLOGY:{
                break;
            }
            case TWOSET:{
                break;
            }
            case UNDEFINED:
            default:{

            }
            }

        }
    }
    //Menu handlers

    //saveProjectItem = addMenuItem(fileMenu,"SaveProject", new saveProjectListener());

    private JMenuItem makeSaveProjectAsItem(){
        final JMenuItem item = new JMenuItem("Save Project As...");
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


    private JMenuItem makeSaveAlignmentAsTuplesItem(){
        final JMenuItem item = new JMenuItem("Tuples...");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            }
        });
        return item;
    }
    

    private JMenuItem makeSaveAlignmentAsOntologyItem(){
        final JMenuItem item = new JMenuItem("Ontology...");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                
            }
        });
        return item;
    }
    

    private JMenuItem makeUnGuidedMatchItem(){
        final JMenuItem item = new JMenuItem("Run UnGuided Match");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                final Project project = getProject();
                final DataFactory factory = project.getDataFactory();
                List <URI> onts = project.listOntologies();  //need to get this pair from somewhere
                if (onts.size()>1){
                    JOptionPane.showMessageDialog(theApp.getMainFrame(), "About to start unguided match");
                    Alignment newAlignment = factory.getAlignment();
                    ExtendableStringMetric m = factory.getExtendableStringMetric(ExtendableStringMetric.Metric.MongeElkan);
                    OntologyWrapper w1 = project.showOntology(onts.get(0));
                    OntologyWrapper w2 = project.showOntology(onts.get(1));
                    List <OntTerm> names1 = w1.getPreOrderedTermList(new HashSet<OntTerm>());
                    List <OntTerm> names2 = w2.getPreOrderedTermList(new HashSet<OntTerm>());
                    project.unguidedMatch(newAlignment, w1, names1, w2, names2,m);
                    JOptionPane.showMessageDialog(theApp.getMainFrame(), "Unguided match completed - top 30 pairs on console");
                }
                else {
                    JOptionPane.showMessageDialog(theApp.getMainFrame(), "Ontologies haven't been loaded yet", "No Ontologies", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return item;
        
    }
    
    
    private JMenuItem makeSuggestMatchItem(){
        final JMenuItem item = new JMenuItem("Suggest Match");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // TODO stub
            }            
        });
        return item;
    }
    
    private JMenuItem makeSuggestMatchFromNieceItem(){
        final JMenuItem item = new JMenuItem("Suggest Match from daughter of other set");
        item.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // TODO stub
            }
        });
        return item;
    }

    

}
