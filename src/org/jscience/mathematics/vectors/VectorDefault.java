/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import org.jscience.mathematics.structures.Field;

/**
 * <p> This class represents the default vector implementation.</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
final class VectorDefault<F extends Field<F>> extends Vector<F> {

    /**
     * Holds the vector's elements.
     */
    private F[] _elements;

    /**
     * Holds the vector's dimension.
     */
    private int _dimension;

    @Override
    public int getDimension() {
        return _dimension;
    }

    @Override
    public F get(int i) {
        if (i >= _dimension)
            throw new ArrayIndexOutOfBoundsException();
        return _elements[i];
    }

    // Sets the specified element.
    final void set_(int i, F f) {
        _elements[i] = f;
    }

    // Gets the specified element.
    final F get_(int i) {
        return _elements[i];
    }

    ///////////////////////
    // Factory creation. //
    ///////////////////////
    
    @SuppressWarnings("unchecked")
    static <F extends Field<F>> VectorDefault<F> newInstance(int dimension) {
        VectorDefault<F> v = FACTORY.object();
        if ((v._elements == null) || (v._elements.length < dimension)) {
            v._elements = (F[]) new Field[dimension];
        }
        v._dimension = dimension;
        return v;
    }

    // TODO Use different factories for large vectors.
    private static Factory<VectorDefault> FACTORY = new Factory<VectorDefault>() {
        protected VectorDefault create() {
            return new VectorDefault();
        }
    };

    private VectorDefault() {
    }

    private static final long serialVersionUID = 1L;
}