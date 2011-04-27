/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2010 Peter E. Midford
 * 
 * This file is part of Phylontal.
 *
 * Phylontal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Phylontal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Phylontal.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * Created on Oct 5, 2009
 * Last updated on Mar 5, 2010
 * 
 */
package org.ethontos.phylontal.project;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.ethontos.phylontal.project.Commands.Command;
import org.ethontos.phylontal.project.impl.DataFactory;
import org.ethontos.phylontal.project.impl.ExtendableStringMetric;
import org.ethontos.phylontal.project.impl.TripleStore;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.PMatrix;

/**
 * This is the main interface for phylontal - most things should happen within the project
 * @author peter
 * created Oct 5, 2009
 *
 */
public interface Project {


    public void addOntology(File f);
    
    public List<GraphTree> loadTrees(File f);
    
    public void loadAlignment(File f);
    
    public void saveAlignment(File f, Alignment.Format format);
        
    public void saveMergedOntologies(File f);
    
    public void validate();
    
    public Alignment unguidedMatch(Alignment a, OntologyWrapper ont1, List<OntTerm> unit1, OntologyWrapper ont2, List<OntTerm> unit2, ExtendableStringMetric m);
    
    public List<URI> listOntologies();
    
    public OntologyWrapper showOntology(URI u);
    
    public List<GraphTree> showTreeList();
    
    public GraphTree selectTree(int n);
    
    public GraphTree showTree();
    
    public Alignment startGuidedMatch(Alignment a, NamesSet unit1, NamesSet unit2);
    
    public TipMapping getTipMapping();
    
    public void continueGuidedMatch(Alignment a);
        
    public void addTerm();
    
    public DataFactory getDataFactory();
    
    public TripleStore getTripleStore();
    
    public Collection<PMatrix> loadCharacterMatrices(File f);
    
    
    /** sort of a placeholder for a command interface */
    void doCommand(Command cmd, String arguments);

    void saveProject();

    void saveProjectAs(File f);

    public FileNameExtensionFilter projectExtensions();
    
    public FileNameExtensionFilter ontologyExtensions();
    
    public FileNameExtensionFilter phylogenyExtensions();
    
    public String getVersionString();
    public String getCopyrightString();
    public String getAboutString();

    
}
