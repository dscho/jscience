/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import java.util.Comparator;

import org.jscience.mathematics.structures.Field;
import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.numbers.Number;

import javolution.realtime.LocalReference;
import javolution.realtime.RealtimeObject;

/**
 * <p> This class represents the decomposition of a {@link Matrix matrix} 
 *     <code>A</code> into a product of a {@link #getLower lower} 
 *     and {@link #getUpper upper} triangular matrices, <code>L</code>
 *     and <code>U</code> respectively, such as <code>A = P·L·U<code> with 
 *     <code>P<code> a {@link #getPermutation permutation} matrix.</p>
 *     
 * <p> This decomposition</a> is typically used to resolve linear systems
 *     of equations (Gaussian elimination) or to calculate the determinant
 *     of a square {@link Matrix} (<code>O(m³)</code>).</p>
 *     
 * <p> Numerical stability is guaranteed through pivoting if the
 *     {@link Field} elements are {@link Number numbers}
 *     For others elements types, numerical stability can be ensured by setting
 *     the {@link javolution.realtime.LocalContext context-local} pivot 
 *     comparator (see {@link #setPivotComparator}).</p>
 *     
 * <p> Pivoting can be disabled by setting the {@link #setPivotComparator 
 *     pivot comparator} to <code>null</code> ({@link #getPermutation P} 
 *     is then the matrix identity).</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 16, 2006
 * @see <a href="http://en.wikipedia.org/wiki/LU_decomposition">
 *      Wikipedia: LU decomposition</a>
 */
public abstract class DecompositionLU<F extends Field> extends RealtimeObject {

    /**
     * Holds the default comparator for pivoting.
     */
    public static final Comparator<Field> NUMERIC_COMPARATOR = new Comparator<Field>() {

        @SuppressWarnings("unchecked")
        public int compare(Field left, Field right) {
            if ((left instanceof Number) && (right instanceof Number))
                return ((Number) left).isLargerThan((Number) right) ? 1 : -1;
            if (left.equals(left.plus(left))) // Zero
                return -1;
            if (right.equals(right.plus(right))) // Zero
                return 1;
            return 0;
        }
    };

    /**
     * Holds the local comparator.
     */
    private static final LocalReference<Comparator<Field>>
         PIVOT_COMPARATOR = new LocalReference<Comparator<Field>>(
            NUMERIC_COMPARATOR);

    /**
     * Default constructor (for sub-classes).
     */
    protected DecompositionLU() {
    }

    /**
     * Returns the lower/upper decomposition of the specified matrix.
     *
     * @param  source the matrix for which the decomposition is calculated.
     * @return the lower/upper decomposition of the specified matrix.
     * @throws DimensionException if the specified matrix is not square.
     */
    public static <F extends Field> DecompositionLU<F> valueOf(Matrix<F> source) {
         return DecompositionLUDefault.newInstance(source);
    }

    /**
     * Returns the lower/upper decomposition of the specified 64 bits 
     * float matrix.
     *
     * @param  source the matrix for which the decomposition is calculated.
     * @return the lower/upper decomposition of the specified matrix.
     * @throws DimensionException if the specified matrix is not square.
     */
    public static <F extends Field> DecompositionLU<Float64> valueOf(MatrixFloat64 source) {
         return DecompositionLUDefault.newInstance(source); // TODO
    }

    /**
     * Sets the {@link javolution.realtime.LocalContext local} comparator used 
     * for pivoting or <code>null</code> to disable pivoting.
     *
     * @param  cmp the comparator for pivoting or <code>null</code>.
     */
    public static void setPivotComparator(Comparator<Field> cmp) {
        PIVOT_COMPARATOR.set(cmp);
    }

    /**
     * Returns the {@link javolution.realtime.LocalContext local} 
     * comparator used for pivoting or <code>null</code> if pivoting 
     * is not performed (default {@link #NUMERIC_COMPARATOR}).
     *
     * @return the comparator for pivoting or <code>null</code>.
     */
    public static Comparator<Field> getPivotComparator() {
        return PIVOT_COMPARATOR.get();
    }

    /**
     * Returns the solution X of the equation: A * X = B  with
     * <code>this = A.lu()</code> using back and forward substitutions.
     *
     * @param  B the input vector.
     * @return the solution X = (1 / A) * B.
     * @throws DimensionException if the dimensions do not match.
     */
    public abstract Vector<F> solve(Vector<F> B);
    
    /**
     * Returns the solution X of the equation: A * X = B  with
     * <code>this = A.lu()</code> using back and forward substitutions.
     *
     * @param  B the input matrix.
     * @return the solution X = (1 / A) * B.
     * @throws DimensionException if the dimensions do not match.
     */
    public abstract Matrix<F> solve(Matrix<F> B);
    
    /**
     * Returns the solution X of the equation: A * X = Identity  with
     * <code>this = A.lu()</code> using back and forward substitutions.
     *
     * @return <code>this.solve(Identity)</code>
     */
    public abstract Matrix<F> inverse();
    
    /**
     * Returns the determinant of the {@link Matrix} having this
     * decomposition.
     *
     * @return the determinant of the matrix source.
     */
    public abstract F determinant();

    /**
     * Returns the lower matrix decomposition (<code>L</code>) with diagonal
     * elements equal to the multiplicative identity for F. 
     *  
     *
     * @param zero the additive identity for F.
     * @param one the multiplicative identity for F.
     * @return the lower matrix.
     */
    public abstract Matrix<F> getLower(F zero, F one);

    /**
     * Returns the upper matrix decomposition (<code>U</code>). 
     *
     * @param zero the additive identity for F.
     * @return the upper matrix.
     */
    public abstract Matrix<F> getUpper(F zero);
    
    /**
     * Returns the permutation matrix (<code>P</code>). 
     *
     * @param zero the additive identity for F.
     * @param one the multiplicative identity for F.
     * @return the permutation matrix.
     */
    public abstract Matrix<F> getPermutation(F zero, F one);
    
    /**
     * Returns the lower/upper decomposition in one single matrix. 
     *
     * @return the lower/upper matrix merged in a single matrix.
     */
    public abstract Matrix<F> getLU();
    
    /**
     * Returns the pivots elements of this decomposition. 
     *
     * @return the row indices after permutation.
     */
    public abstract int[] getPivots();

}