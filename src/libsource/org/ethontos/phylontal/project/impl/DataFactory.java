/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Jun 5, 2010
 * Last updated on Jun 5, 2010
 * 
 */
package org.ethontos.phylontal.project.impl;

import org.ethontos.phylontal.project.Alignment;
import org.ethontos.phylontal.project.AnnotationTriple;
import org.ethontos.phylontal.project.MatchSet;
import org.ethontos.phylontal.project.NamesSet;

public enum DataFactory {

    INSTANCE;
        
    /**
     * Just creates and returns an AnnotationTriple (doesn't put it in a triple store).
     * @param s Subject of the triple (implementation ambivalent?)
     * @param p Predicate of the triple
     * @param o Object of the triple
     * @return freshly allocated triple with subject, predicate, object as requested
     */
    public AnnotationTriple getTriple(Object s, Object p, Object o){
        AnnotationTriple result = new AnnotationTripleImpl(s,p,o);
        return result;
    }
    
    /**
     * Just creates and returns a NamesSet
     * @return freshly allocated, empty NamesSet
     */
    public  NamesSet getNamesSet(){
        NamesSet result = new NamesSetImpl();
        return result;
    }

    
    /**
     * Just creates a returns an Alignment
     * @return freshly allocated, empty Alignment
     */
    public Alignment getAlignment(){
        AlignmentImpl result = new AlignmentImpl();
        return result;
    }

    /**
     * Just creates a returns an Alignment
     * @return freshly allocated, empty Alignment
     */
    public MatchSet getMatchSet(){
        MatchSetImpl result = new MatchSetImpl();
        return result;
    }
   
    
    /**
     * creates a combinable string metric, specifying a weight
     * @param id specifies a string distance metric
     * @param weight applied to the metric (e.g., for combination)
     * @return a CombinableStringMetric - generally useful only for installing in an ExtendableStringMetric
     */

    public ExtendableStringMetric.CombinableStringMetric getCombinableStringMetric(ExtendableStringMetric.Metric id, double weight){
        return new ExtendableStringMetric.CombinableStringMetric(id, weight);
    }

    /**
     * creates a combinable string metric with default weight
     * @param id specifies a string distance metric
     * @return a CombinableStringMetric - generally useful only for installing in an ExtendableStringMetric
     */
    public ExtendableStringMetric.CombinableStringMetric getCombinableStringMetric(ExtendableStringMetric.Metric id){
        return new ExtendableStringMetric.CombinableStringMetric(id,1.0);
    }
    
    
    /**
     * creates the simplest ExtendableMetric - a singleton with no weighting
     * @param id species a string distance metric
     * @return
     */
    public ExtendableStringMetric getExtendableStringMetric(ExtendableStringMetric.Metric id){
        ExtendableStringMetric.CombinableStringMetric cm =  new ExtendableStringMetric.CombinableStringMetric(id,1.0);
        return new ExtendableStringMetric(cm, ExtendableStringMetric.Operator.Sum);  //operator doesn't matter in this case...
    }
    
}
