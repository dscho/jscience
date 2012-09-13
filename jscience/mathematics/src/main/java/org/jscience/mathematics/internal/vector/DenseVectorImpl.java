/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.internal.vector;

import java.util.List;

import javolution.context.ObjectFactory;
import javolution.context.StackContext;
import javolution.util.FastTable;

import javolution.util.Index;
import org.jscience.mathematics.structure.Field;

/**
 * <p> This class holds the dense vector default implementation.</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, December 12, 2009
 */
final class DenseVectorImpl<F extends Field<F>> extends DenseVector<F> {

    /**
     * Holds the object factory.
     */
    static final ObjectFactory<DenseVectorImpl> FACTORY = new ObjectFactory<DenseVectorImpl>() {

        @Override
        protected DenseVectorImpl create() {
            return new DenseVectorImpl();
        }

        @Override
        protected void cleanup(DenseVectorImpl vector) {
            vector._elements.reset();
        }
    };

    /**
     * Holds the elements.
     */
    final FastTable<F> _elements = new FastTable<F>();

    // See parent static method.
    public static <F extends Field<F>> DenseVectorImpl<F> valueOf(List<F> elements) {
        DenseVectorImpl<F> V = FACTORY.object();
        V._elements.addAll(elements);
        return V;
    }

    // See parent static method.
    public static <F extends Field<F>> DenseVectorImpl<F> valueOf(F... elements) {
        DenseVectorImpl<F> V = FACTORY.object();
        for (F e : elements) {
            V._elements.add(e);
        }
        return V;
    }

    // See parent static method.
    public static <F extends Field<F>> DenseVectorImpl<F> valueOf(Vector<F> that) {
        if (that instanceof DenseVectorImpl)
            return (DenseVectorImpl<F>) that;
        DenseVectorImpl<F> V = FACTORY.object();
        for (int i = 0, n = that.getDimension(); i < n; i++) {
            V._elements.add(that.get(i));
        }
        return V;
    }

    @Override
    public List<F> asList() {
        return _elements.unmodifiable();
    }

    @Override
    public int getDimension() {
        return _elements.size();
    }

    @Override
    public F get(int i) {
        return _elements.get(i);
    }

    @Override
    public DenseVectorImpl<F> getSubVector(List<Index> indices) {
        DenseVectorImpl<F> V = FACTORY.object();
        for (Index index : indices) {
            F element = this._elements.get(index.intValue());
            V._elements.add(element);
        }
        return V;
    }

    @Override
    public DenseVectorImpl<F> opposite() {
        DenseVectorImpl<F> V = FACTORY.object();
        for (F e : _elements) {
            V._elements.add(e.opposite());
        }
        return V;
    }

    @Override
    public DenseVectorImpl<F> plus(Vector<F> that) {
        final int n = _elements.size();
        if (that.getDimension() != n)
            throw new DimensionException();
        DenseVectorImpl<F> V = FACTORY.object();
        for (int i = 0; i < n; i++) {
            V._elements.add(_elements.get(i).plus(that.get(i)));
        }
        return V;
    }

    @Override
    public DenseVectorImpl<F> times(F k) {
        DenseVectorImpl<F> V = FACTORY.object();
        for (F e : _elements) {
            V._elements.add(e.times(k));
        }
        return V;
    }

    @Override
    public F times(Vector<F> that) {
        final int n = _elements.size();
        if (that.getDimension() != n)
            throw new DimensionException();
        StackContext.enter(); // Reduces memory allocation / garbage collection.
        try {
            F sum = _elements.get(0).times(that.get(0));
            for (int i = 1; i < n; i++) {
                sum = sum.plus(_elements.get(i).times(that.get(i)));
            }
            return StackContext.outerCopy(sum);
        } finally {
            StackContext.exit();
        }
    }

    @Override
    public DenseVectorImpl<F> copy() {
        DenseVectorImpl<F> V = FACTORY.object();
        for (int i = 0, n = _elements.size(); i < n; i++) {
            V._elements.add((F) _elements.get(i).copy());
        }
        return V;
    }

    // For internal use only.
    void set(int i, F e) {
        _elements.set(i, e);
    }
    private static final long serialVersionUID = 1L;


}
