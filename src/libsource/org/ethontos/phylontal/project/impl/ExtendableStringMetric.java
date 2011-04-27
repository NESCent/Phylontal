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
 * Created on Jun 4, 2010
 * Last updated on Jun 4, 2010
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.util.ArrayList;
import java.util.List;

import com.wcohen.ss.api.StringDistance;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.MongeElkan;

public class ExtendableStringMetric {
    
    public enum Metric {MongeElkan,Equals};
    
    public enum Operator {Mean, Max, Sum, Product};
    
    private Operator myOperator;
    private List<CombinableStringMetric> myMetrics;     //maybe this could be devolved to an unordered collection?
    
    ExtendableStringMetric(CombinableStringMetric met, Operator op){
        myOperator = op;
        myMetrics = new ArrayList<CombinableStringMetric>();
        myMetrics.add(met);
    }
    
    void addMetric(CombinableStringMetric met){
        myMetrics.add(met);
    }
    
    void deleteMetric(int index){
        myMetrics.remove(index);
    }
    
    double calculate(String str1, String str2){
        double acc;
        if (myMetrics.size() == 1){
            return myMetrics.get(0).calculate(str1, str2);
        }
        else{
            if (myOperator == Operator.Product){
                acc = 1;
            }
            else if (myOperator == Operator.Max){
                acc = Double.NEGATIVE_INFINITY;
            }
            else
                acc = 0;
            for(CombinableStringMetric metric : myMetrics){
                double tmp = metric.calculate(str1, str2);
                switch(myOperator){
                case Mean:
                case Sum:
                    acc += tmp;
                    break;
                case Max:
                    if (tmp > acc)
                        acc = tmp;
                    break;
                case Product:
                    acc *= tmp;
                }
            }
            if (myOperator == Operator.Mean)
                return acc/myMetrics.size();
            else
                return acc;
        }
    }
    
    static class CombinableStringMetric{
        final private AbstractStringMetric simmetricsMetric;
        final private StringDistance secondStringMetric;
        final double normalization;
        final Metric idForPrinting;
        
        CombinableStringMetric(Metric id, double pNormalization){
            normalization = pNormalization;
            idForPrinting = id;
            switch(id){
            case MongeElkan:{
                simmetricsMetric = new MongeElkan();
                secondStringMetric = null;
                break;
            }
            default: {
                simmetricsMetric = null;
                secondStringMetric = null;
            }
            }
            
        }
        
        double calculate(String str1, String str2){
            if (simmetricsMetric == null && secondStringMetric == null){
                if (str1.equals(str2))
                    return 1.0*normalization;
                else
                    return 0.0*normalization;
            }
            else if (simmetricsMetric != null){
                return simmetricsMetric.getSimilarity(str1, str2)*normalization;
            }
            else return secondStringMetric.score(str1, str2)*normalization;
            
        }
    }

}
