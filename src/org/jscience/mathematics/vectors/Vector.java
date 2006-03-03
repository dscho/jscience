/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import javolution.lang.Immutable;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.realtime.PoolContext;
import javolution.realtime.RealtimeObject;
import javolution.util.FastTable;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.structures.Field;
import org.jscience.mathematics.structures.VectorSpace;

/**
 * <p> This class represents an immutable element of a vector space.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Vector_space">
 *      Wikipedia: Vector Space</a>
 */
public abstract class Vector<F extends Field> extends RealtimeObject implements
        VectorSpace<Vector<F>, F>, Immutable {

    /**
     * Holds the default XML representation for {@link Vector} and its
     * sub-classes.  This representation consists of the vector 
     * elements as nested XML elements and the vector <code>dimension</code>
     * as attributes. For example:[code]
     *    <vectors:Vector dimension="2">
     *      <numbers:Complex real="1.0" imaginary="0.0">
     *      <numbers:Complex real="0.0" imaginary="1.0">
     *    </vectors:Vector>[/code]
     */
    protected static final XmlFormat<Vector> XML = new XmlFormat<Vector>(
            Vector.class) {
        public void format(Vector v, XmlElement xml) {
            final int d = v.getDimension();
            xml.setAttribute("dimension", d);
            for (int i = 0; i < d; i++) {
                xml.add(v.get(i));
            }
        }

        public Vector parse(XmlElement xml) {
            int d = xml.getAttribute("dimension", 1);
            FastTable<Field> elements = FastTable.newInstance();
            for (int i = d; --i >= 0;) {
                Field element = xml.getNext();
                elements.add(element);
            }
            return Vector.valueOf(elements);
        }
    };

    /**
     * Returns a vector implementation holding the specified elements.
     *
     * @param elements the vector elements.
     * @return the vector having the specified elements.
     */
    public static <F extends Field> Vector<F> valueOf(F... elements) {
        final int d = elements.length;
        VectorDefault<F> v = VectorDefault.newInstance(d);
        for (int i = 0; i < d; i++) {
            v.set_(i, elements[i]);
        }
        return v;
    }

    /**
     * Returns a vector implementation ({@link VectorFloat64}) holding the 
     * specified elements values (convenience method).
     *
     * @param values the values of the vector elements.
     * @return the vector having the specified elements values.
     */
    public static VectorFloat64 valueOf(double... values) {
        return VectorFloat64.newInstance(values);
    }

    /**
     * Returns a vector populated from the specified collection of
     * {@link Field Field} objects.
     *
     * @param  elements the collection of field objects.
     * @return the vector having the specified elements.
     */
    public static <F extends Field> Vector<F> valueOf(Collection<F> elements) {
        final int d = elements.size();
        VectorDefault<F> v = VectorDefault.newInstance(elements.size());
        Iterator<F> iterator = elements.iterator();
        for (int i = 0; i < d; i++) {
            v.set_(i, iterator.next());
        }
        return v;
    }

    /**
     * Default constructor (for sub-classes).
     */
    protected Vector() {
    }

    /**
     * Returns the number of elements  held by this vector.
     *
     * @return this vector dimension.
     */
    public abstract int getDimension();

    /**
     * Returns a single element from this vector.
     *
     * @param  i the element index (range [0..n[).
     * @return the element at <code>i</code>.
     * @throws IndexOutOfBoundsException <code>(i < 0) || (i >= size())</code>
     */
    public abstract F get(int i);

    /**
     * Returns this vector as a row matrix.
     *
     * @return this vector as a row matrix.
     */
    public Matrix<F> asRowMatrix() {
        int d = this.getDimension();
        MatrixDefault<F> M = MatrixDefault.newInstance(1, d);
        for (int i = 0; i < d; i++) {
            M.set_(0, i, this.get(i));
        }
        return M;
    }

    /**
     * Returns this vector as a column matrix.
     *
     * @return this vector as a column matrix.
     */
    public Matrix<F> asColumnMatrix() {
        int d = this.getDimension();
        MatrixDefault<F> M = MatrixDefault.newInstance(d, 1);
        for (int i = 0; i < d; i++) {
            M.set_(i, 0, this.get(i));
        }
        return M;
    }

    /**
     * Returns the difference between this vector and the one specified.
     *
     * @param  that the vector to be subtracted.
     * @return <code>this - that</code>.
     */
    public Vector<F> minus(Vector<F> that) {
        return this.plus(that.opposite());
    }

    /**
     * Returns the dot product of this vector with the one specified.
     *
     * @param  that the vector multiplier.
     * @return <code>this · that</code>
     * @throws DimensionException if <code>this.dimension() != that.dimension()</code>
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product">
     *      Wikipedia: Dot Product</a>
     */
    public F times(Vector<F> that) {
        if (this.getDimension() != that.getDimension())
            throw new DimensionException();
        PoolContext.enter();
        try {
            F sum = (F) this.get(0).times(that.get(0));
            for (int i = 1; i < this.getDimension(); i++) {
                sum = (F) sum.plus(this.get(i).times(that.get(i)));
            }
            sum.move(ObjectSpace.OUTER);
            return sum;
        } finally {
            PoolContext.exit();
        }
    }

    /**
     * Returns the cross product of two 3-dimensional vectors.
     *
     * @param  that the vector multiplier.
     * @return <code>this x that</code>
     * @throws DimensionException if 
     *         <code>(this.getDimension() != 3) && (that.getDimension() != 3)</code> 
     */
    public final Vector<F> cross(Vector<F> that) {
        if ((this.getDimension() != 3) || (that.getDimension() != 3))
            throw new DimensionException(
                    "The cross product of two vectors requires "
                            + "3-dimensional vectors");
        VectorDefault<F> v = VectorDefault.newInstance(3);
        v.set_(0, (F) ((F) this.get(1).times(that.get(2))).plus(((F) this
                .get(2).times(that.get(1))).opposite()));
        v.set_(1, (F) ((F) this.get(2).times(that.get(0))).plus(((F) this
                .get(0).times(that.get(2))).opposite()));
        v.set_(2, (F) ((F) this.get(0).times(that.get(1))).plus(((F) this
                .get(1).times(that.get(0))).opposite()));
        return v;
    }

    /**
     * Returns the text representation of this vector.
     *
     * @return the text representation of this vector.
     */
    public Text toText() {
        final int dimension = this.getDimension();
        TextBuilder tb = TextBuilder.newInstance();
        tb.append('{');
        for (int i = 0; i < dimension; i++) {
            tb.append(get(i).toText());
            if (i != dimension - 1) {
                tb.append(", ");
            }
        }
        tb.append('}');
        return tb.toText();
    }

    /**
     * Indicates if this vector can be considered equals to the one 
     * specified using the specified comparator when testing for 
     * element equality. The specified comparator may allow for some 
     * tolerance in the difference between the vector elements.
     *
     * @param  that the vector to compare for equality.
     * @param  cmp the comparator to use when testing for element equality.
     * @return <code>true</code> if this vector and the specified matrix are
     *         both vector with equal elements according to the specified
     *         comparator; <code>false</code> otherwise.
     */
    public boolean equals(Vector<F> that, Comparator<F> cmp) {
        if (this == that)
            return true;
        final int dimension = this.getDimension();
        if (that.getDimension() != dimension)
            return false;
        for (int i = dimension; --i >= 0;) {
            if (cmp.compare(this.get(i), that.get(i)) != 0)
                return false;
        }
        return true;
    }

    /**
     * Indicates if this vector is equal to the object specified.
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if this vector and the specified object are
     *         both vectors with equal elements; <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof Vector))
            return false;
        final int dimension = this.getDimension();
        Vector v = (Vector) that;
        if (v.getDimension() != dimension)
            return false;
        for (int i = dimension; --i >= 0;) {
            if (!this.get(i).equals(v.get(i)))
                return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for this vector.
     * Equals objects have equal hash codes.
     *
     * @return this vector hash code value.
     * @see    #equals
     */
    public int hashCode() {
        final int dimension = this.getDimension();
        int code = 0;
        for (int i = dimension; --i >= 0;) {
            code += get(i).hashCode();
        }
        return code;
    }

    @Override
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            final int dimension = this.getDimension();
            for (int i = dimension; --i >= 0;) {
                get(i).move(os);
            }
            return true;
        }
        return false;
    }

    //////////////////////////////////////
    // Default Interface implementation //
    //////////////////////////////////////

    public Vector<F> times(F a) {
        final int d = this.getDimension();
        VectorDefault<F> v = VectorDefault.newInstance(d);
        for (int i = d; --i >= 0;) {
            v.set_(i, (F)this.get(i).times(a));
        }
        return v;
    }

    public Vector<F> plus(Vector<F> that) {
        final int d = this.getDimension();
        if (that.getDimension() != d)
            throw new DimensionException();
        VectorDefault<F> v = VectorDefault.newInstance(d);
        for (int i = d; --i >= 0;) {
            v.set_(i, (F)this.get(i).plus(that.get(i)));
        }
        return v;
    }

    public Vector<F> opposite() {
        final int d = this.getDimension();
        VectorDefault<F> v = VectorDefault.newInstance(d);
        for (int i = d; --i >= 0;) {
            v.set_(i, (F)this.get(i).opposite());
        }
        return v;
    }
}