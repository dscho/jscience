/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vector;

import java.util.List;

import javolution.util.FastTable;
import javolution.util.Index;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import org.jscience.mathematics.structure.Field;

/**
 * <p> This class represents a dense vector.</p>
 *     
 * <p> Dense vectors are created through list or arrays of elements.</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, December 12, 2009
 */
public abstract class DenseVector<F extends Field<F>> extends Vector<F> {

    /**
     * Holds the default XML representation for dense vectors. For example:
     * [code]
     *    <DenseVector>
     *        <Rational value="1/3" />
     *        <Rational value="3/5" />
     *    </DenseVector>[/code]
     */
    protected static final XMLFormat<DenseVector> XML_FORMAT = new XMLFormat<DenseVector>(
            DenseVector.class) {

        @Override
        public DenseVector newInstance(Class<DenseVector> cls, InputElement xml)
                throws XMLStreamException {
            FastTable elements = FastTable.newInstance();
            try {
                while (xml.hasNext()) {
                    elements.add(xml.getNext());
                }
                return DenseVector.valueOf(elements);
            } finally {
                FastTable.recycle(elements);
            }
        }

        @Override
        public void read(InputElement xml, DenseVector V)
                throws XMLStreamException {
            // Nothing to do (already parsed by newInstance)
        }

        @Override
        public void write(DenseVector V, OutputElement xml)
                throws XMLStreamException {
            int dimension = V.getDimension();
            for (int i = 0; i < dimension;) {
                xml.add(V.get(i++));
            }
        }
    };

    /**
     * Returns a dense vector holding the elements from the specified
     * collection. 
     *
     * @param elements the collection of vector elements.
     * @return the dense vector having the specified elements.
     */
    public static <F extends Field<F>> DenseVector<F> valueOf(List<F> elements) {
        return DenseVectorImpl.valueOf(elements);
    }

    /**
     * Returns a dense vector holding the specified elements.
     *
     * @param elements the vector elements.
     * @return the dense vector having the specified elements.
     */
    public static <F extends Field<F>> DenseVector<F> valueOf(F... elements) {
        return DenseVectorImpl.valueOf(elements);
    }

    /**
     * Returns a dense vector equivalent to the specified vector.
     *
     * @param that the vector to convert.
     * @return <code>that</code> or a dense vector holding the same elements
     *         as the specified vector.
     */
    public static <F extends Field<F>> DenseVector<F> valueOf(Vector<F> that) {
        if (that instanceof DenseVector)
            return (DenseVector<F>) that;
        return DenseVectorImpl.valueOf(that);
    }

    /**
     * Default constructor.
     */
    protected DenseVector() {
    }

    /**
     * Returns a list view (read only) over the elements of this dense vector.
     *
     * @return this vector elements.
     */
    public abstract List<F> asList();

    @Override
    public abstract DenseVector<F> getSubVector(List<Index> indices);

    @Override
    public abstract DenseVector<F> opposite();

    @Override
    public abstract DenseVector<F> plus(Vector<F> that);

    @Override
    public DenseVector<F> minus(Vector<F> that) {
        return this.plus(that.opposite());
    }

    @Override
    public abstract DenseVector<F> times(F k);

    @Override
    public abstract DenseVector<F> copy();
    private static final long serialVersionUID = 1L;

}
