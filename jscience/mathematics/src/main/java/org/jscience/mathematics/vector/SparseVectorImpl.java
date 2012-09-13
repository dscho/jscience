/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2007 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vector;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javolution.context.ObjectFactory;
import javolution.util.FastMap;
import javolution.util.Index;

import org.jscience.mathematics.structure.Field;

/**
 * <p> This class holds the sparse vector default implementation.</p>
 *         
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, December 12, 2009
 */
final class SparseVectorImpl<F extends Field<F>> extends SparseVector<F> {

    /**
     * Holds the object factory.
     */
    static final ObjectFactory<SparseVectorImpl> FACTORY = new ObjectFactory<SparseVectorImpl>() {

        protected SparseVectorImpl create() {
            return new SparseVectorImpl();
        }

        @Override
        protected void cleanup(SparseVectorImpl vector) {
            vector._elements.reset();
            vector._zero = null;
        }
    };

    /**
     * Holds this vector dimension.
     */
    int _dimension;

    /**
     * Holds this vector zero.
     */
    transient F _zero;

    /**
     * Holds the index to element mapping.
     */
    final FastMap<Index, F> _elements = new FastMap<Index, F>();

    // See parent static method.
    public static <F extends Field<F>> SparseVectorImpl<F> valueOf(Map<Index, F> elements,
            F zero, int dimension) {
        SparseVectorImpl<F> V = FACTORY.object();
        V._elements.putAll(elements);
        V._zero = zero;
        V._dimension = dimension;
        // Checks dimension.
        for (FastMap.Entry<Index, F> e = V._elements.head(), n = V._elements.tail();
                (e = e.getNext()) != n;) {
            Index index = e.getKey();
            if (index.intValue() >= dimension)
                throw new DimensionException("Found index of " + index +
                        " but vector dimension is " + dimension);
        }
        return V;
    }

    // See parent static method.
    public static <F extends Field<F>> SparseVectorImpl<F> valueOf(int index, F element, int dimension) {
        if (index >= dimension)
            throw new DimensionException();
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = dimension;
        if (element.equals(element.opposite())) {
            V._zero = element;
        } else {
            V._elements.put(Index.valueOf(index), element);
            V._zero = element.plus(element.opposite()); // Creates a zero.
        }
        return V;
    }

    // See parent static method.
    public static <F extends Field<F>> SparseVectorImpl<F> valueOf(
            Vector<F> that, F zero, Comparator<? super F> comparator) {
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = that.getDimension();
        V._zero = zero;
        for (int i = 0; i < V._dimension; i++) {
            F element = that.get(i);
            if (comparator.compare(element, zero) != 0) {
                V._elements.put(Index.valueOf(i), element);
            }
        }
        return V;
    }

    // See parent static method.
    public static <F extends Field<F>> SparseVectorImpl<F> valueOf(Vector<F> that) {
        if (that instanceof SparseVectorImpl)
            return (SparseVectorImpl<F>) that;
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = that.getDimension();
        for (int i = 0; i < V._dimension; i++) {
            F element = that.get(i);
            if (!element.equals(element.opposite())) {
                V._elements.put(Index.valueOf(i), element);
            } else { // Found a zero.
                V._zero = element;
            }
        }
        if (V._zero == null) { // No zero found, create one.
            F elem0 = that.get(0);
            V._zero = elem0.plus(elem0.opposite());
        }
        return V;
    }


    @Override
    public F getZero() {
        return _zero;
    }

    @Override
    public Map<Index, F> asMap() {
        return _elements.unmodifiable();
    }

    @Override
    public int getDimension() {
        return _dimension;
    }

    @Override
    public Map<Index, F> getElements() {
        return _elements;
    }

    @Override
    public F get(int i) {
        if ((i < 0) || (i >= _dimension))
            throw new IndexOutOfBoundsException();
        F element = _elements.get(Index.valueOf(i));
        return (element == null) ? _zero : element;
    }

    @Override
    public SparseVectorImpl<F> getSubVector(List<Index> indices) {
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = indices.size();
        V._zero = _zero;
        int i = 0;
        for (Index index : indices) {
            F element = this._elements.get(index);
            if (element != null) {
                V._elements.put(Index.valueOf(i++), element);
            }
        }
        return V;
    }

    @Override
    public SparseVectorImpl<F> opposite() {
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = _dimension;
        V._zero = _zero;
        for (FastMap.Entry<Index, F> e = _elements.head(), n = _elements.tail(); (e = e.getNext()) != n;) {
            V._elements.put(e.getKey(), e.getValue().opposite());
        }
        return V;
    }

    @Override
    public SparseVectorImpl<F> plus(Vector<F> that) {
        if (that instanceof SparseVectorImpl)
            return plus((SparseVectorImpl<F>) that);
        return plus((SparseVectorImpl) SparseVector.valueOf(that));
    }

    private SparseVectorImpl<F> plus(SparseVectorImpl<F> that) {
        if (this._dimension != that._dimension)
            throw new DimensionException();
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = _dimension;
        V._zero = _zero;
        V._elements.putAll(this._elements);
        for (FastMap.Entry<Index, F> e = that._elements.head(), n = that._elements.tail();
                (e = e.getNext()) != n;) {
            Index index = e.getKey();
            FastMap.Entry<Index, F> entry = V._elements.getEntry(index);
            if (entry == null) {
                V._elements.put(index, e.getValue());
            } else {
                F newElement = entry.getValue().plus(e.getValue());
                if (!_zero.equals(newElement)) {
                    V._elements.put(e.getKey(), newElement);
                } else {
                    V._elements.remove(e.getKey());
                }
            }
        }
        return V;
    }

    @Override
    public SparseVectorImpl<F> times(F k) {
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = _dimension;
        V._zero = _zero;
        for (FastMap.Entry<Index, F> e = _elements.head(), n = _elements.tail(); (e = e.getNext()) != n;) {
            F newElement = e.getValue().times(k);
            if (!_zero.equals(newElement)) {
                V._elements.put(e.getKey(), newElement);
            }
        }
        return V;
    }

    @Override
    public F times(Vector<F> that) {
        if (that.getDimension() != _dimension)
            throw new DimensionException();
        F sum = null;
        for (FastMap.Entry<Index, F> e = _elements.head(), n = _elements.tail(); (e = e.getNext()) != n;) {
            F f = e.getValue().times(that.get(e.getKey().intValue()));
            sum = (sum == null) ? f : sum.plus(f);
        }
        return (sum != null) ? sum : _zero;
    }

    @Override
    public SparseVectorImpl<F> copy() {
        SparseVectorImpl<F> V = FACTORY.object();
        V._dimension = _dimension;
        V._zero = (F) _zero.copy();
        for (Map.Entry<Index, F> e : _elements.entrySet()) {
            V._elements.put(e.getKey(), (F) e.getValue().copy());
        }
        return V;
    }
    private static final long serialVersionUID = 1L;

}
