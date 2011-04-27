/*
 * Phylontal - a tool for phylogenetic alignment of ontologies
 * Copyright 2009-2011 Peter E. Midford
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
 * Created on Oct 8, 2009
 * Last updated on April 27, 2011
 * 
 */
package org.ethontos.phylontal.project.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ethontos.phylontal.project.NamedEdge;
import org.ethontos.phylontal.project.OntTerm;
import org.ethontos.phylontal.project.OntologyWrapper;
import org.ethontos.phylontal.project.RelationEdge;
import org.jgrapht.DirectedGraph;
import org.obo.dataadapter.DefaultOBOParser;
import org.obo.dataadapter.OBOFileAdapter;
import org.obo.dataadapter.OBOParseEngine;
import org.obo.dataadapter.OBOParseException;
import org.obo.datamodel.IdentifiedObject;
import org.obo.datamodel.OBOClass;
import org.obo.datamodel.OBOSession;
import org.obo.datamodel.ObjectFactory;
import org.obo.util.TermUtil;


public class OBOOntologyWrapper implements OntologyWrapper {
    
    private static OBOFileAdapter fAdaptor = null;  //TODO initialize me
    
    private OBOSession session;   //I don't think we
    private URI sessionURI;
    
    static Logger logger = Logger.getLogger(OBOOntologyWrapper.class.getName());

    
    static boolean canRead(File file){
        logger.setLevel((Level)Level.DEBUG);
        if (file != null){
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                OBOTokenizer ot = new OBOTokenizer(br);
                int type;
                type = ot.nextToken();
                if (type != OBOTokenizer.TT_WORD)
                    return false;
                    System.out.println("token is " + ot.sval);
                    if (ot.sval.equalsIgnoreCase("format-version"))
                        return true;
                    return false;
            }
            catch (FileNotFoundException e) {
                return false;
            }
            catch (IOException e) {
                return false;
            }
        }
        return false; // for now
    }
    

    static OntologyWrapper loadOntology(File file){
        final OBOOntologyWrapper result = new OBOOntologyWrapper();
        result.loadAndProcessFile(file);
        return (OntologyWrapper)result;
    }
    
    
    private OBOOntologyWrapper(){
        
    }
    
    public  DirectedGraph<String,NamedEdge> asStringGraph(){
        return null;  //TODO finish me
    }


    @Override
    public DirectedGraph<OntTerm, RelationEdge> asOntologyGraph() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public URI getOntologyID() {
        return sessionURI;
    }


    @Override
    public List<OntTerm> getPreOrderedTermList(Set<OntTerm> excluded) {
        // TODO Auto-generated method stub
        return null;
    }

    private void loadAndProcessFile(File file){
        sessionURI = file.toURI();
        session = getOBOSession(file.getAbsolutePath());
        final ObjectFactory oboFactory = session.getObjectFactory();
        Collection<OBOClass> terms = TermUtil.getTerms(session);
        Map<String,OBOClass> termNames = getAllTermNamesHash(terms);
        Map<String, IdentifiedObject> termIDs = getAllTermIDsHash(terms);
    }
    
    private OBOSession getOBOSession(String path) {
        DefaultOBOParser parser = new DefaultOBOParser();
        OBOParseEngine engine = new OBOParseEngine(parser);
        engine.setPath(path);
        try {
            engine.parse();
        } catch (IOException e) {
            logger.error("A error occurred while opening or reading from: " + path);
            e.printStackTrace();
        } catch (OBOParseException e) {
            logger.error("A error occurred while parsing the ontology in " + path);
            e.printStackTrace();
        }
        return parser.getSession();
    }

    private Map<String,OBOClass> getAllTermNamesHash(Collection <OBOClass> terms){
        final Map<String,OBOClass> result = new HashMap<String,OBOClass>(terms.size());
        for (OBOClass item : terms){
            if (item.getName() == null)
                System.err.println("Term " + item.getID() + " has null for name");
            else {
                if (result.get(item.getName()) != null)
                    System.err.println("Hash collision in building names hash; Name = " + item.getName() + " old ID = " + ((OBOClass)result.get(item.getName())).getID() + " new ID = " + item.getID());
                else
                    result.put(item.getName(), item);
            }
        }
        return result;
    }

    private Map<String, IdentifiedObject> getAllTermIDsHash(Collection <OBOClass> terms){
        return TermUtil.createIDMap(terms);
    }


    /**
     * 
     * @author peter
     * created Mar 17, 2007 in OwlWatcher
     * copied to Phylontal Apr 21, 2010
     */
    private static class OBOTokenizer extends StreamTokenizer{
        
        protected OBOTokenizer(Reader r){
            super(r);
            resetSyntax();
            wordChars('A', 'Z');
            wordChars('a', 'z');
            wordChars('-', '-');
            whitespaceChars(0,32);
        }
        
        private static boolean checkToken(StreamTokenizer ot, int type, String val, int count) throws IOException{
            ot.nextToken();
            if (ot.ttype != type){
                logger.debug("Token number " + count + " has type " + ot.ttype);
                return false;
            }
            else if ((type == StreamTokenizer.TT_WORD) &&(!ot.sval.equalsIgnoreCase(val))){
                logger.debug("Token number " + count + " has value " + ot.sval);
                return false;
            }
            return true;
        }
    }



    
    
    
    
}
