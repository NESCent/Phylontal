/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2011 Peter E. Midford
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
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;



import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.Alignment;
import org.ethontos.phylontal.project.AnnotationTriple;
import org.ethontos.phylontal.project.NamesSet;
import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.ethontos.phylontal.project.Project;
import org.ethontos.phylontal.project.TipMapping;
import org.ethontos.phylontal.project.Commands.Command;
import org.ethontos.phylontal.project.phylo.GraphTree;
import org.ethontos.phylontal.project.phylo.PMatrix;
import org.ethontos.phylontal.project.phylo.PhyloBlock;
import org.ethontos.phylontal.project.phylo.TaxonBlock;
import org.ethontos.phylontal.project.phylo.impl.NexmlReader;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChangeException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

public class ProjectImpl extends Observable implements Project{

    private final static String versionString = "Phylontal 0.1";
    private final static String copyrightString = "Copyright Peter E. Midford 2009";
    private final static String aboutString = versionString + "\n" + copyrightString;

    TaxonBlock taxa;
    PhyloBlock trees;
    private List<GraphTree> treeList;
    private LinkedHashMap<URI,PMatrix> matrixList = new LinkedHashMap<URI,PMatrix>();   //annotated matrices from nexmlfiles
    private GraphTree selectedTree = null;

    LinkedHashMap<URI,OntologyWrapper> ontList = new LinkedHashMap<URI,OntologyWrapper>();
    int treeIndex;
    
    private TripleStore tripleStore = new TripleStore();
    private DataFactory factory = DataFactory.INSTANCE;

    private TipMapping tipOntMapping = new TipMapping();
    
    private boolean OBOMode = false;  //force reading data as OBO (mixed should default to OWL mode)



    private final FileNameExtensionFilter projectExtensions = new FileNameExtensionFilter("Phylontal Project Files", "rdf");
    private final FileNameExtensionFilter phylogenyExtensions = new FileNameExtensionFilter("NeXML Files", "xml");
    private final FileNameExtensionFilter ontologyExtensions = new FileNameExtensionFilter("Ontology Files", "owl", "obo"); 

    static Logger logger = Logger.getLogger(ProjectImpl.class.getName());

    
    @Override
    public void addOntology(File f) {
        OntologyWrapper w = FileUtil.getWrapper(f);
        ontList.put(w.getOntologyID(),w);
        setChanged();
        notifyObservers();
    }

    @Override
    public void addTerm() {
        // TODO Auto-generated method stub

    }

    @Override
    public void continueGuidedMatch(Alignment a) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<URI> listOntologies() {
        final List <URI> result = new ArrayList<URI>();
        for(URI u : ontList.keySet()){
            result.add(u);
        }
        return result;
    }

    @Override
    public void loadAlignment(File f) {
        // TODO Auto-generated method stub
        setChanged();
        this.notifyObservers();
    }

    @Override
    public List<GraphTree> loadTrees(File f) {
        NexmlReader reader = new NexmlReader(this);
        reader.readFromFile(f);
        treeList = reader.getTrees();  
        setChanged();
        if (treeList.size() == 1)
            selectedTree = treeList.get(0);
        this.notifyObservers();
        return treeList;
    }
    
    @Override
    public Collection<PMatrix> loadCharacterMatrices(File f){
        NexmlReader reader = new NexmlReader(this);
        matrixList = reader.getMatrices(matrixList);
        return matrixList.values();
    }

    @Override
    public void saveAlignment(File f, Alignment.Format format) {
        switch (format){
        case FIVETUPLE: {
            saveAlignmentAsFiveTuple(f);
            break;
        }
        case SEVENTUPLE: {
            saveAlignmentAsSevenTuple(f);
            break;
        }
        case OWLONTOLOGY: {
            saveAlignmentAsOWL(f);
        }
        }
    }
    
    
    private void saveAlignmentAsFiveTuple(File f) {
        // TODO Auto-generated method stub
        
    }

    private void saveAlignmentAsSevenTuple(File f) {
        // TODO Auto-generated method stub
        
    }

    private void saveAlignmentAsOWL(File f){
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        IRI ontologyIRI = IRI.create("http://www.ethontos.org/ontologies/testAlignment.owl");
        // Create a physical URI which can be resolved to point to where our ontology will be saved.
        URI physicalURI = f.toURI();
        // Set up a mapping, which maps the ontology URI to the physical URI
        SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, physicalURI);
        manager.addIRIMapper(mapper);

        // Now create the ontology - we use the ontology URI (not the physical URI)
        OWLOntology ontology;
        try {
            ontology = manager.createOntology(ontologyIRI);
            // Now we want to specify that A is a subclass of B.  To do this, we add a subclass
            // axiom.  A subclass axiom is simply an object that specifies that one class is a
            // subclass of another class.
            // We need a data factory to create various object from.  Each ontology has a reference
            // to a data factory that we can use.
            OWLDataFactory factory = manager.getOWLDataFactory();
            // Get hold of references to class A and class B.  Note that the ontology does not
            // contain class A or classB, we simply get references to objects from a data factory that represent
            // class A and class B
            OWLClass clsA = factory.getOWLClass(IRI.create(ontologyIRI + "#A"));
            OWLClass clsB = factory.getOWLClass(IRI.create(ontologyIRI + "#B"));
            // Now create the axiom
            OWLAxiom axiom = factory.getOWLSubClassOfAxiom(clsA, clsB);
            // We now add the axiom to the ontology, so that the ontology states that
            // A is a subclass of B.  To do this we create an AddAxiom change object.
            AddAxiom addAxiom = new AddAxiom(ontology, axiom);
            // We now use the manager to apply the change

            manager.applyChange(addAxiom);

            // The ontology will now contain references to class A and class B - let's
            // print them out
            for (OWLClass cls : ontology.getReferencedClasses()) {
                System.out.println("Referenced class: " + cls);
            }
            // We should also find that B is a superclass of A
            Set<OWLClassExpression> superClasses = clsA.getSuperClasses(ontology);
            System.out.println("Superclasses of " + clsA + ":");
            for (OWLClassExpression desc : superClasses) {
                System.out.println(desc);
            }

            // Now save the ontology.  The ontology will be saved to the location where
            // we loaded it from, in the default ontology format
            manager.saveOntology(ontology);
        } catch (OWLOntologyChangeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownOWLOntologyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OWLOntologyStorageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OWLOntologyCreationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @Override
    public void saveMergedOntologies(File f) {
        // TODO Auto-generated method stub

    }

    @Override
    public OntologyWrapper showOntology(URI u) {
        return ontList.get(u);
    }

    @Override
    public GraphTree showTree() {
        return selectedTree;
    }


    @Override
    public void validate() {
        // TODO Auto-generated method stub

    }

    @Override
    public TipMapping getTipMapping(){
        return tipOntMapping;
    }

    @Override
    public void saveProject() {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveProjectAs(File f) {
        // TODO Auto-generated method stub

    }

    public FileNameExtensionFilter projectExtensions(){
        return projectExtensions;
    }

    public FileNameExtensionFilter phylogenyExtensions(){
        return phylogenyExtensions;
    }

    public FileNameExtensionFilter ontologyExtensions(){
        return ontologyExtensions;
    }


    /**
     * Placeholder for now
     * @param cmd token from the command vocabulary
     * @param the arguments to the command (not processed or parsed for now)
     */
    @Override
    public void doCommand(Command cmd, String arguments) {
        throw new IllegalArgumentException("Commands not currently supported");   
    }

    @Override
    public List<GraphTree> showTreeList() {
        return treeList;
    }

    public GraphTree selectTree(int n){
        if (n>=0 && treeList != null && n<treeList.size())
            selectedTree = treeList.get(n);
        return selectedTree;
    }

    @Override
    public String getAboutString() {
        return aboutString;
    }

    @Override
    public String getCopyrightString() {
        return copyrightString;
    }

    @Override
    public String getVersionString() {
        return versionString;
    }

    @Override
    public Alignment startGuidedMatch(Alignment a, NamesSet unit1, NamesSet unit2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Alignment unguidedMatch(Alignment a, OntologyWrapper ont1, List <OntTerm> terms1, OntologyWrapper ont2, List <OntTerm> terms2, ExtendableStringMetric m) {
        final long startTime = System.currentTimeMillis();
        final String processedTerms1[] = new String[terms1.size()];
        final String processedTerms2[] = new String[terms2.size()];
        int ii = 0;
        for (int i = 0; i< terms1.size();i++){
            if (!(terms1.get(i).isRoot()))
                processedTerms1[ii++] = terms1.get(i).getLabel(ont1);
        }
        ii = 0;
        for (int i = 0; i< terms2.size();i++)
            if (!(terms2.get(i).isRoot()))
                processedTerms2[ii++] = terms2.get(i).getLabel(ont2);
        final double stringScores[][] = new double[processedTerms1.length][processedTerms2.length];
        for (int i = 0; i< processedTerms1.length; i++){
            String str1 = processedTerms1[i];
            for (int j=0; j < processedTerms2.length; j++){
                String str2 = processedTerms2[j];
                if (str1 != null && str2 != null){
                    stringScores[i][j] = m.calculate(str1, str2);
                    //System.out.println("Test: str1 = " + str1 + " Str2 = " + str2 + " score = " + stringScores[i][j]);
                }
                else
                    stringScores[i][j] = 0;
            }
        }
        final boolean hits1[] = new boolean[processedTerms1.length];
        final boolean hits2[] = new boolean[processedTerms2.length];
        for(int i=0;i<hits1.length;i++)
            hits1[i] = false;
        for(int i= 0;i<hits2.length;i++)
            hits2[i] = false;
        final int PAIRS = 30;
        double[] best = new double[PAIRS];
        for(int i= 0;i<PAIRS;i++)
            best[i] = Float.NEGATIVE_INFINITY;
        int pairCount = 0;
        int bestPairs[][] = new int[PAIRS][2];
        for (pairCount = 0;pairCount<PAIRS;pairCount++){
            for (int i=0; i< processedTerms1.length; i++){
                if (!hits1[i]){
                    for (int j = 0;j<processedTerms2.length; j++){
                        if (!hits2[j]){
                            if (stringScores[i][j]>best[pairCount]){
                                best[pairCount] = stringScores[i][j];
                                bestPairs[pairCount][0] = i;
                                bestPairs[pairCount][1] = j;
                                hits1[i] = true;
                                hits2[j] = true;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Finished calculating scoring in " + (System.currentTimeMillis()-startTime) + "ms");
        System.out.println(" -- Best Pairs --");
        for(int i=0;i<PAIRS;i++){
            System.out.println("Pair " + i +  " [" + processedTerms1[bestPairs[i][0]] + ", " + processedTerms2[bestPairs[i][1]] + "] score = " + best[i]);
        }
        
        return a;
    }
    
    public AnnotationTriple getTriple(Object s, Object p, Object o){
        AnnotationTriple result = factory.getTriple(s, p, o);
        tripleStore.put(s, result);
        return result;
    }

    
    public TripleStore getTripleStore(){
        return tripleStore;
    }
    
    public DataFactory getDataFactory(){
        return factory;
    }

}
