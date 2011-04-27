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


/**
 * A callback for the NeXML reader  - is this necessary?
 * @author peter
 * created Oct 12, 2009
 *
 */
public class PredicateHandlerImpl extends PredicateHandler {
	private String mPredicate;
	private String mPrefix = "poa";
	private boolean mPropertyIsRel;
	private Object mValue;
	private String mProperty;
	private Object mSubject;

	public PredicateHandlerImpl(Object subject, String predicate, Object value) {
		super(subject, predicate, value);
	}
	
	@Override
	void setPredicate(String predicate) {
		mPredicate = predicate;
	}	

	@Override
	String getPredicate() {
		return mPredicate;
	}

	@Override
	void setPrefix(String prefix) {
		mPrefix = prefix;
	}		
	
	@Override
	String getPrefix() {
		return mPrefix;
	}
	
	@Override
	void setPropertyIsRel(boolean propertyIsRel) {
		mPropertyIsRel = propertyIsRel;		
	}	


	@Override
	boolean getPropertyIsRel() {
		return mPropertyIsRel;
	}	
	

	@Override
	String getURIString() {
		return "http://ethontos.org#";
	}

	@Override
	public
	Object getValue() {
		return mValue;
	}
	
	@Override
	public
	void setValue(Object value) {
		mValue = value;
	}

	public String getRel() {
		return getProperty();
	}

	public void setRel(String rel) {
		setProperty(rel); 
		setPropertyIsRel(true);		
	}

	public String getProperty() {
		if ( null != mProperty ) {
			return mProperty;
		}
		else {
			return getPrefix() + ":" + getPredicate();
		}
	}

	public void setProperty(String property) {
		mProperty = property;
	}

	public Object getSubject() {
		return mSubject;
	}

	public void setSubject(Object subject) {
		mSubject = subject;
	}
	
}
