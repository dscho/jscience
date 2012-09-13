/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vector;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import javolution.lang.Realtime;
import javolution.lang.ValueType;
import javolution.text.Cursor;
import javolution.text.Text;
import javolution.text.TextFormat;

import javolution.util.Index;
import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.structure.Field;
import org.jscience.mathematics.structure.VectorSpace;

/**
 * <p> This interface class represents an immutable element of a vector space.</p>
 *
 * <p> Instances of this class are usually created from VectorFactory services.
 *     TBD 
 *     [code]
 *        // Creates a vector of 64 bits floating points numbers.
 *        Vector<Float64> V0 = Vector.valueOf(1.1, 1.2, 1.3);
 *
 *        // Creates a dense vector of rational numbers.
 *        DenseVector<Rational> V1 = DenseVector.valueOf(Rational.valueOf(23, 45), Rational.valueOf(33, 75));
 *
 *        // Creates the sparse vector { 0, 0, 0, 3.3, 0, 0, 0, -3.7 } of decimal numbers.
 *        SparseVector<Decimal> V2 =
 *            SparseVector.valueOf(3, Decimal.valueOf("3.3"), 8).plus(
 *            SparseVector.valueOf(7, Decimal.valueOf("-3.7"), 8));
 *     [/code]

 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, December 12, 2009
 * @see <a href="http://en.wikipedia.org/wiki/Vector_space">
 *      Wikipedia: Vector Space</a>
 */
interface Vector<F extends Field<F>>
        extends VectorSpace<Vector<F>, F>, ValueType, Realtime {

    /**
     * Returns the number of elements  held by this vector.
     *
     * @return this vector dimension.
     */
    public int getDimension();

    /**
     * Returns a single element from this vector.
     *
     * @param  i the element index (range [0..n[).
     * @return the element at <code>i</code>.
     * @throws IndexOutOfBoundsException <code>(i &lt; 0) || (i &gt;= getDimension())</code>
     */
    public F get(int i);

    /**
     * Returns the sub-vector formed by the elements having the specified
     * indices. The indices do not have to be ordered, for example
     * <code>getSubVector(Index.valuesOf(1, 0))</code> returns the subvector
     * holding the first two elements of this vector exchanged.
     *
     * @returnthe corresponding sub-vector.
     * @throws IndexOutOfBoundsException if any of the indices is greater
     *         than this vector dimension.
     */
    public Vector<F> getSubVector(List<Index> indices);

    /**
     * Returns the negation of this vector.
     *
     * @return <code>-this</code>.
     */
    public Vector<F> opposite();

    /**
     * Returns the sum of this vector with the one specified.
     *
     * @param   that the vector to be added.
     * @return  <code>this + that</code>.
     * @throws  DimensionException is vectors dimensions are different.
     */
    public Vector<F> plus(Vector<F> that);

    /**
     * Returns the difference between this vector and the one specified.
     *
     * @param  that the vector to be subtracted.
     * @return <code>this - that</code>.
     */
    public Vector<F> minus(Vector<F> that);

    /**
     * Returns the product of this vector with the specified coefficient.
     *
     * @param  k the coefficient multiplier.
     * @return <code>this · k</code>
     */
    public Vector<F> times(F k);

    /**
     * Returns the dot product of this vector with the one specified.
     *
     * @param  that the vector multiplier.
     * @return <code>this · that</code>
     * @throws DimensionException if <code>this.dimension() != that.dimension()</code>
     * @see <a href="http://en.wikipedia.org/wiki/Dot_product">
     *      Wikipedia: Dot Product</a>
     */
    public F times(Vector<F> that);

    /**
     * Returns the cross product of two 3-dimensional vectors.
     *
     * @param  that the vector multiplier.
     * @return <code>this x that</code>
     * @throws DimensionException if 
     *         <code>(this.getDimension() != 3) && (that.getDimension() != 3)</code> 
     */
    public Vector<F> cross(Vector<F> that);
 
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
    public boolean equals(Vector<F> that, Comparator<F> cmp);
    
    /**
     * Indicates if this vector is equal to the object specified.
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if this vector and the specified object are
     *         both vectors with equal elements; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object that);
    
    /**
     * Returns a hash code value for this vector.
     * Equals objects have equal hash codes.
     *
     * @return this vector hash code value.
     * @see    #equals
     */
    @Override
    public int hashCode();

    /**
     * Returns a copy of this vector 
     * {@link javolution.context.AllocatorContext allocated} 
     * by the calling thread (possibly on the stack).
     *     
     * @return an identical and independant copy of this matrix.
     */
    public Vector<F> copy();
}
