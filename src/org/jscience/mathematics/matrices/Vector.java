/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.mathematics.matrices;

import java.util.Collection;
import java.util.Iterator;

import javolution.realtime.ArrayPool;
import javolution.util.Text;
import javolution.util.TextBuilder;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

/**
 * This class represents an element of a vector space arranged as 
 * single column {@link Matrix}.
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see <a href="http://mathworld.wolfram.com/Vector.html">
 *      Vector -- from MathWorld</a> 
 */
public class Vector extends Matrix {

    /**
     * Overrides {@link Matrix#MATRIX_XML} representation for {@link Vector}
     * and its sub-classes.  This representation consists of the vector's 
     * elements as nested XML elements. For example: <pre>
     *    &lt;math:Vector&gt;
     *      &lt;math:Complex real="1.0" imaginary="0.0"/&gt;
     *      &lt;math:Complex real="0.0" imaginary="1.0"/&gt;
     *    &lt;/math:Vector&gt;</pre>
     */
    protected static final XmlFormat VECTOR_XML = new XmlFormat(Vector.class) {
        public void format(Object obj, XmlElement xml) {
            Vector vector = (Vector) obj;
            for (int i = 0; i < vector.n; i++) {
                xml.add(vector.o[i]);
            }
        }

        public Object parse(XmlElement xml) {
            return valueOf(xml);
        }
    };

    /**
     * Holds the vector factories.
     */
    private static final VectorFactory[] FACTORIES;
    static {
        FACTORIES = new VectorFactory[28];
        for (int i = 0; i < FACTORIES.length; i++) {
            FACTORIES[i] = new VectorFactory(ArrayPool.MIN_LENGTH << i);
        }
    }

    /**
     * Base constructor.
     * 
     * @param  elements the element array.
     */
    Vector(Operable[] elements) {
        super(elements);
    }

    /**
     * Creates a vector of specified dimension.
     * 
     * @param size the dimension of this vector.
     */
    public Vector(int size) {
        super(size, 1);
    }

    /**
     * Returns a n-dimensional vector filled with <code>null</code> coordinates
     * elements.
     *
     * @param  n the dimension of this vector.
     * @return a n-by-1 matrix non-initialized.
     */
    public static Vector newInstance(int n) {
        Vector V = (Vector) FACTORIES[ArrayPool.indexFor(n)].object();
        V.n = n;
        V.m = 1;
        return V;
    }

    /**
     * Returns the vector corresponding to the specified {@link Operable}
     * coordinates.
     *
     * <p>Note: It is safe to reuse the specifed array as it is not internally
     *          referenced by the vector being returned.</p>
     *
     * @param  coordinates the array of {@link Operable} objects.
     * @return the vector having the specified coordinates.
     */
    public static Vector valueOf(Operable[] coordinates) {
        Vector V = newInstance(coordinates.length);
        for (int i = coordinates.length; i > 0;) {
            V.set(--i, 0, coordinates[i]);
        }
        return V;
    }

    /**
     * Returns a n-dimensional vector populated from the specified collection of
     * {@link Operable} objects.
     *
     * @param  coordinates the collection of {@link Operable} objects.
     * @return the vector having the specified elements.
     * @throws ClassCastException if any of the element is not {@link Operable}.
     */
    public static Vector valueOf(Collection coordinates) {
        Vector V = newInstance(coordinates.size());
        Iterator iterator = coordinates.iterator();
        for (int i = 0; i < V.n; i++) {
            V.o[i] = (Operable) iterator.next();
        }
        return V;
    }

    /**
     * Returns the dimension of this vector.
     *
     * @return n, the number of rows.
     */
    public final int getDimension() {
        return n;
    }

    /**
     * Returns a single coordinate from this vector.
     *
     * @param  i the coordinate index (range [0..n[).
     * @return the coordinates at <code>i</code>.
     * @throws IndexOutOfBoundsException <code>i &gt;= getDimension()</code>
     */
    public final Operable get(int i) {
        if (i < n) {
            return o[i];
        } else {
            throw new IndexOutOfBoundsException("i: " + i + " (Vector[" + n
                    + "])");
        }
    }

    /**
     * Sets a single coordinate of this vector.
     *
     * @param  i the coordinate index (range [0..n[).
     * @param  coordinate the coordinate set at <code>i</code>.
     * @throws IndexOutOfBoundsException <code>i &gt;= getDimension()</code>
     */
    public final void set(int i, Operable coordinate) {
        if (i < n) {
            o[i] = coordinate;
        } else {
            throw new IndexOutOfBoundsException("i: " + i + " (Vector[" + n
                    + "])");
        }
    }

    /**
     * Returns the dot product of this vector with the one specified.
     *
     * @param  that the vector multiplier.
     * @return <code>this · that</code>
     * @throws MatrixException if 
     *         <code>this.getDimension() != that.getDimension()</code> 
     * @see <a href="http://mathworld.wolfram.com/DotProduct.html">
     *      Dot Product -- from MathWorld</a>
     */
    public final Operable dot(Vector that) {
        if (this.n == that.n) {
            Operable sum = this.o[0].times(that.o[0]);
            for (int i = 1; i < n; i++) {
                sum = sum.plus(this.o[0].times(that.o[0]));
            }
            return sum;
        } else {
            throw new MatrixException(this.n + " different from " + that.n);
        }
    }

    /**
     * Returns the cross product of two 3-dimensional vectors.
     *
     * @param  that the vector multiplier.
     * @return <code>this x that</code>
     * @throws MatrixException if 
     *         <code>(this.getDimension() != 3) && 
     *               (that.getDimension() != 3)</code> 
     * @see <a href="http://mathworld.wolfram.com/CrossProduct.html">
     *      Cross Product -- from MathWorld</a>
     */
    public final Vector cross(Vector that) {
        if ((this.n == 3) && (that.n == 3)) {
            Vector V = newInstance(3);
            V.set(0, this.o[1].times(that.o[2]).plus(
                    this.o[2].times(that.o[1]).opposite()));
            V.set(1, this.o[2].times(that.o[0]).plus(
                    this.o[0].times(that.o[2]).opposite()));
            V.set(2, this.o[0].times(that.o[1]).plus(
                    this.o[1].times(that.o[0]).opposite()));
            return V;
        } else {
            throw new MatrixException(
                    "The cross product of two vectors requires "
                            + "3-dimensional vectors");
        }
    }

    /**
     * Returns the text representation of this vector.
     *
     * @return the text representation of this vector.
     */
    public Text toText() {
        TextBuilder cb = TextBuilder.newInstance();
        cb.append("{");
        for (int i = 0; i < n; i++) {
            if (i != 0) {
                cb.append(", ");
            }
            cb.append(o[i]);
        }
        cb.append("}");
        return cb.toText();
    }

    /**
     * This inner class represents a factory producing {@link Vector} instances.
     */
    private final static class VectorFactory extends Factory {

        private final int _size;

        private VectorFactory(int size) {
            _size = size;
        }

        public Object create() {
            return new Vector(new Operable[_size]);
        }

        public void cleanup(Object obj) {
            Vector V = (Vector) obj;
            ArrayPool.clear(V.o, 0, V.n);
        }
    }
}