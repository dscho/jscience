/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.geography.coordinates;

import org.jscience.geography.coordinates.crs.CompoundCRS;

/**
 * This class represents a coordinates made up by combining 
 * two coordinates objects together.
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public class CompoundCoordinates<C1 extends Coordinates, C2 extends Coordinates>
    extends Coordinates<CompoundCRS<C1, C2>> {

    /**
     * Holds the first coordinates. 
     */
    private final C1 _first;
    
    /**
     * Holds the next coordinates. 
     */
    private final C2 _next;
    
    /**
     * Creates a compound coordinates made up of the specified coordinates.
     * 
     * @param first the first coordinates.
     * @param next the next coordinates. 
     */
    public CompoundCoordinates(C1 first, C2 next) {
       _first = first;
       _next = next;
    }
    
    /**
     * Returns the first coordinates.
     * 
     * @return the first coordinates. 
     */
    public C1 getFirst() {
        return _first;
    }
    
    /**
     * Returns the next coordinates.
     * 
     * @return the next coordinates. 
     */
    public C2 getNext() {
        return _next;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CompoundCRS<C1, C2> getCoordinateReferenceSystem() {
        return new CompoundCRS<C1, C2>(_first.getCoordinateReferenceSystem(),
                _next.getCoordinateReferenceSystem());
    }

    // OpenGIS Interface.
    public int getDimension() {
        return _first.getDimension() + _next.getDimension();
    }

    // OpenGIS Interface.
    public double getOrdinate(int dimension) throws IndexOutOfBoundsException {
        final int firstDimension = _first.getDimension();
        if (dimension < firstDimension) {
            return _first.getOrdinate(dimension);
        } else {
            return _next.getOrdinate(dimension - firstDimension);
        }
    }
        
}