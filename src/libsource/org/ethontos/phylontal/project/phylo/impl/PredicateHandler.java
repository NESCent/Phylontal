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
 * Created on Oct 12, 2009
 * Last updated on April 27, 2011
 * 
 */


package org.ethontos.phylontal.project.phylo.impl;

import java.net.URI;


/**
 * A callback for the NeXML reader
 * @author peter
 * created Oct 12, 2009
 *
 */
public abstract class PredicateHandler {
	abstract Object getSubject();
	abstract void setSubject(Object subject);
	
	abstract Object getValue();
	abstract void setValue(Object value);
	
	abstract String getPrefix ();
	abstract void setPrefix(String prefix);
	
	abstract String getPredicate();
	abstract void setPredicate(String predicate);	
	
	abstract boolean getPropertyIsRel();
	abstract void setPropertyIsRel(boolean propertyIsRel);
	
	abstract String getURIString();
	
	public PredicateHandler(Object subject,String predicate,Object object) {
		setSubject(subject);
		setValue(object);
		setPredicate(predicate);
	}
	String getProperty() {
		return getPrefix () + ":" + getPredicate();
	}
		
	public URI getURI () {
		URI uri = null;
		try {
			uri = new URI(getURIString());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return uri;
	}
}
