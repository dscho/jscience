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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import javolution.realtime.ArrayPool;
import javolution.realtime.ConcurrentContext;
import javolution.realtime.PoolContext;
import javolution.realtime.RealtimeObject;
import javolution.realtime.ConcurrentContext.Logic;
import javolution.util.MathLib;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.numbers.Complex;
import org.jscience.mathematics.numbers.Integer32;
import org.jscience.mathematics.numbers.Real;

/**
 * <p> This class represents a rectangular array of {@link Operable}
 *     objects. It may be used to resolve system of linear equations
 *     involving <i>any kind</i> of {@link Operable} elements
 *     (e.g. {@link Real}, {@link Complex}, 
 *     {@link org.jscience.physics.quantities.Quantity Quantity},
 *     {@link org.jscience.mathematics.functions.Function Function}, etc).</p>
 *
 * <p> Non-commutative multiplication is supported and this class itself
 *     implements the {@link Operable} interface. Consequently, this class may
 *     be used to resolve system of linear equations involving matrices
 *     (for which the multiplication is not commutative).</p>
 *
 * <p> <b>Implementation Note:</b> This class uses {@link ConcurrentContext}
 *     to accelerate calculations on multi-processor systems.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see <a href="http://mathworld.wolfram.com/Matrix.html">
 *      Matrix -- from MathWorld</a> 
 */
public class Matrix extends RealtimeObject implements Operable, Serializable {

    /**
     * Holds the default XML representation for {@link Matrix} and its
     * sub-classes.  This representation consists of the matrix's 
     * elements as nested XML elements and the matrix <code>row</code> and
     * <code>column</code> as attributes. For example: <pre>
     *    &lt;math:Matrix row="2" column="1"&gt;
     *      &lt;math:Complex real="1.0" imaginary="0.0"/&gt;
     *      &lt;math:Complex real="0.0" imaginary="1.0"/&gt;
     *    &lt;/math:Matrix&gt;</pre>
     */
    protected static final XmlFormat MATRIX_XML = new XmlFormat(Matrix.class) {
        public void format(Object obj, XmlElement xml) {
            Matrix matrix = (Matrix) obj;
            xml.setAttribute("row", matrix.n);
            xml.setAttribute("column", matrix.m);
            final int nm = matrix.n * matrix.m;
            for (int i = 0; i < nm; i++) {
                xml.add(matrix.o[i]);
            }
        }

        public Object parse(XmlElement xml) {
            return valueOf(xml.getAttribute("row", 1), xml.getAttribute(
                    "column", 1), xml);
        }
    };

    /**
     * Holds the matrix factories.
     */
    private static final MatrixFactory[] FACTORIES;
    static {
        FACTORIES = new MatrixFactory[28];
        for (int i = 0; i < FACTORIES.length; i++) {
            FACTORIES[i] = new MatrixFactory(ArrayPool.MIN_LENGTH << i);
        }
    }

    /**
     * The array of operable objects (M[i][j] = o[i*m+j], i < n, j < m).
     */
    final Operable[] o;

    /**
     * Holds the number of rows.
     */
    int n;

    /**
     * Holds the number of columns.
     */
    int m;

    /**
     * Base constructor.
     * 
     * @param  elements the element array.
     */
    Matrix(Operable[] elements) {
        o = elements;
    }

    /**
     * Creates a matrix of specified dimensions.
     * 
     * @param  rows the number of rows.
     * @param  columns the number of columns.
     */
    public Matrix(int rows, int columns) {
        this(new Operable[rows * columns]);
        this.n = rows;
        this.m = columns;
    }

    /**
     * Returns a n-by-m matrix filled with <code>null</code> elements.
     *
     * @param  n the number of rows.
     * @param  m the number of columns.
     * @return a n-by-m matrix non-initialized (a {@link Vector} instance
     *         if <code>m == 1</code>).
     */
    public static Matrix newInstance(int n, int m) {
        if (m == 1) {
            return Vector.newInstance(n);
        } else {
            final int size = n * m;
            Matrix M = (Matrix) FACTORIES[ArrayPool.indexFor(size)].object();
            M.n = n;
            M.m = m;
            return M;
        }
    }

    /**
     * Returns a n-by-m matrix filled with the specified diagonal element
     * and the specified non-diagonal element.
     * This constructor may be used to create an identity matrix
     * (e.g. <code>valueOf(n, n, ONE, ZERO)</code>).
     *
     * @param  n the number of rows.
     * @param  m the number of columns.
     * @param  diagonal the diagonal element.
     * @param  other the non-diagonal element.
     * @return a n-by-m matrix with <code>d</code> on the diagonal and
     *         <code>o</code> elsewhere.
     */
    public static Matrix valueOf(int n, int m, Operable diagonal, Operable other) {
        Matrix M = newInstance(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                M.o[i * m + j] = (i != j) ? other : diagonal;
            }
        }
        return M;
    }

    /**
     * Returns a matrix from a 2-dimensional array of {@link Operable} objects.
     * The first dimension being the row and the second being the column.
     *
     * <p>Note: It is safe to reuse the specifed array as it is not internally
     *          referenced by the matrix being returned.</p>
     *
     * @param  elements the array of {@link Operable} objects.
     * @return the matrix having the specified components.
     * @throws IllegalArgumentException all rows must have the same length.
     */
    public static Matrix valueOf(Operable[][] elements) {
        int n = elements.length;
        int m = elements[0].length;
        Matrix M = newInstance(n, m);
        for (int i = 0; i < n; i++) {
            if (elements[i].length == m) {
                System.arraycopy(elements[i], 0, M.o, i * m, m);
            } else {
                throw new IllegalArgumentException(
                        "All rows must have the same length.");
            }
        }
        return M;
    }

    /**
     * Returns a n-by-m matrix populated from the specified collection of
     * {@link Operable} objects (rows first).
     *
     * @param  n the number of rows.
     * @param  m the number of columns.
     * @param  elements the collection of {@link Operable} objects.
     * @return the matrix having the specified size and elements.
     * @throws MatrixException if <code>elements.size() != n * m</code>
     * @throws ClassCastException if any of the element is not {@link Operable}.
     */
    public static Matrix valueOf(int n, int m, Collection elements) {
        if (elements.size() == n * m) {
            Matrix M = newInstance(n, m);
            Iterator iterator = elements.iterator();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    M.o[i * m + j] = (Operable) iterator.next();
                }
            }
            return M;
        } else {
            throw new MatrixException(n * m
                    + " elements expected but found " + elements.size());
        }
    }

    /**
     * Returns the row dimension of this matrix.
     *
     * @return n, the number of rows.
     */
    public final int getRowDimension() {
        return n;
    }

    /**
     * Returns the column dimension of this matrix.
     *
     * @return m, the number of columns.
     */
    public final int getColumnDimension() {
        return m;
    }

    /**
     * Indicates if this matrix is square.
     *
     * @return <code>getRowDimension == getColumnDimension</code>
     */
    public final boolean isSquare() {
        return n == m;
    }

    /**
     * Returns a single element from this matrix.
     *
     * @param  i the row index (range [0..n[).
     * @param  j the column index (range [0..m[).
     * @return the element read at [i,j].
     * @throws IndexOutOfBoundsException <code>i &gt;= getRowDimension() |
     *         j &gt;= getColumnDimension()</code>.
     */
    public final Operable get(int i, int j) {
        if ((i < n) && (j < m)) {
            return o[i * m + j];
        } else {
            throw new IndexOutOfBoundsException("i: " + i + ", j: " + j
                    + " (matrix[" + m + "," + n + "])");
        }
    }

    /**
     * Sets a single element of this matrix.
     *
     * @param  i the row index (range [0..n[).
     * @param  j the column index (range [0..m[).
     * @param  element the element set at [i,j].
     * @throws IndexOutOfBoundsException <code>i &gt;= getRowDimension() |
     *         j &gt;= getColumnDimension()</code>.
     */
    public final void set(int i, int j, Operable element) {
        if ((i < n) && (j < m)) {
            o[i * m + j] = element;
        } else {
            throw new IndexOutOfBoundsException("i: " + i + ", j: " + j
                    + " (matrix[" + m + "," + n + "])");
        }
    }

    /**
     * Returns a sub-matrix of this matrix given the range of its rows and
     * columns indices.
     *
     * @param  i0 the initial row index.
     * @param  i1 the final row index.
     * @param  j0 the initial column index.
     * @param  j1 the final column index.
     * @return <code>THIS(i0:i1, j0:j1)</code>
     */
    public final Matrix getMatrix(int i0, int i1, int j0, int j1) {
        int Mn = i1 - i0 + 1;
        int Mm = j1 - j0 + 1;
        Matrix M = newInstance(Mn, Mm);
        for (int i = 0; i < Mn; i++) {
            for (int j = 0; j < Mm; j++) {
                M.o[i * Mm + j] = this.o[(i + i0) * m + j + j0];
            }
        }
        return M;
    }

    /**
     * Returns a sub-matrix composed of the specified rows and columns from
     * this matrix.
     *
     * @param  rows the indices of the rows to return.
     * @param  columns the indices of the columns to return.
     * @return the sub-matrix from the specified rows and columns.
     */
    public final Matrix getMatrix(int[] rows, int[] columns) {
        int Mn = rows.length;
        int Mm = columns.length;
        Matrix M = newInstance(Mn, Mm);
        for (int i = 0; i < Mn; i++) {
            for (int j = 0; j < Mm; j++) {
                M.o[i * Mm + j] = this.o[rows[i] * m + columns[j]];
            }
        }
        return M;
    }

    /**
     * Returns the {@link LUDecomposition} of this {@link Matrix}.
     * Numerical stability is guaranteed  (through pivoting) if the
     * {@link Operable} elements of this matrix are derived
     * from <code>java.lang.Number</code>. For others elements types,
     * numerical stability can be ensured by setting the 
     * {@link LUDecomposition#setPivotComparator local pivot comparator}.
     *
     * @return the decomposition of this matrix into a product of upper and
     *         lower triangular matrices.
     */
    public final LUDecomposition lu() {
        return LUDecomposition.valueOf(this);
    }

    /**
     * Indicates if this matrix is equal to the object specified.
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if this matrix and the specified object are
     *         considered equal; <code>false</code> otherwise.
     */
    public final boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Matrix) {
            Matrix M = (Matrix) that;
            if (M.n != this.n || M.m != this.m) {
                return false;
            } else {
                for (int i = n * m; i > 0;) {
                    if (!this.o[--i].equals(M.o[i])) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code value for this matrix.
     * Equals object have equal hash codes.
     *
     * @return this matrix hash code value.
     * @see    #equals
     */
    public final int hashCode() {
        int code = 0;
        for (int i = n * m; i > 0;) {
            code += o[--i].hashCode();
        }
        return code;
    }

    /**
     * Returns the negation of this matrix.
     *
     * @return <code>-this</code>.
     */
    public final Matrix negate() {
        Matrix M = newInstance(n, m);
        for (int i = n * m; i > 0;) {
            M.o[--i] = this.o[i].opposite();
        }
        return M;
    }

    /**
     * Returns the sum of this matrix with the one specified.
     *
     * @param   that the matrix to be added.
     * @return  <code>this + that</code>.
     * @throws  MatrixException matrices's dimensions are different.
     */
    public final Matrix add(Matrix that) {
        if ((this.n == that.n) && (this.m == that.m)) {
            Matrix M = newInstance(n, m);
            for (int i = n * m; i > 0;) {
                M.o[--i] = this.o[i].plus(that.o[i]);
            }
            return M;
        } else {
            throw new MatrixException();
        }
    }

    /**
     * Returns the difference between this matrix and the one specified.
     *
     * @param  that the matrix to be subtracted.
     * @return <code>this - that</code>.
     * @throws MatrixException matrices's dimensions are different.
     */
    public final Matrix subtract(Matrix that) {
        if ((this.n == that.n) && (this.m == that.m)) {
            Matrix M = newInstance(n, m);
            for (int i = n * m; i > 0;) {
                M.o[--i] = this.o[i].plus(that.o[i].opposite());
            }
            return M;
        } else {
            throw new MatrixException();
        }
    }

    /**
     * Return the multiplication of this matrix with the specified factor.
     *
     * @param  k the coefficient multiplier.
     * @return <code>k * M</code>.
     */
    public final Matrix multiply(Operable k) {
        Matrix M = newInstance(n, m);
        for (int i = n * m; i > 0;) {
            M.o[--i] = k.times(this.o[i]);
        }
        return M;
    }

    /**
     * Returns the product of this matrix with the one specified.

     * <p> Note: This method uses {@link ConcurrentContext} to reduce garbage 
     *           and accelerate calculation on multi-processor systems.</p>  
     *
     * @param  that the matrix multiplier.
     * @return <code>this * that</code>.
     * @throws MatrixException <code>M.getRowDimension()
     *         != this.getColumnDimension()</code>.
     */
    public Matrix multiply(Matrix that) {
        if (this.m == that.n) {
            if (m < 5) { // Sequential calculation.
                Matrix M = newInstance(this.n, that.m);
                for (int i = 0; i < this.n; i++) {
                    final int i_m = i * m;
                    for (int j = 0; j < that.m; j++) {
                        Operable sum = this.o[i_m].times(that.o[j]);
                        for (int k = 1; k < this.m; k++) {
                            sum = sum.plus(this.o[i_m + k].times(that.o[k
                                    * that.m + j]));
                        }
                        M.o[i * M.m + j] = sum;
                    }
                }
                return M;
            } else { // Concurrent calculation.
                return concurrentMultiply(that);
            }
        } else {
            throw new MatrixException();
        }
    }

    private Matrix concurrentMultiply(Matrix that) {
        ConcurrentContext.enter();
        try {
            Matrix M = newInstance(this.n, that.m);
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < that.m; j++) {
                    ConcurrentContext.execute(MULTIPLY_ROW_COLUMN, this, that,
                            M, Integer32.valueOf(i), Integer32.valueOf(j));
                }
            }
            return M; // No need to export result, only the concurrent
        } finally { // logic is performed in a pool context.
            ConcurrentContext.exit();
        }
    }

    private static final Logic MULTIPLY_ROW_COLUMN = new Logic() {

        public void run(Object[] args) {
            Matrix left = (Matrix) args[0];
            Matrix right = (Matrix) args[1];
            Matrix result = (Matrix) args[2];
            int i = ((Integer32) args[3]).intValue();
            int j = ((Integer32) args[4]).intValue();
            int i_m = i * left.m;
            Operable sum = left.o[i_m].times(right.o[j]);
            for (int k = 1; k < left.m; k++) {
                sum = sum.plus(left.o[i_m + k].times(right.o[k * right.m + j]));
            }
            result.o[i * result.m + j] = sum;
            sum.move(ContextSpace.OUTER);
        }
    };

    /**
     * Returns this matrix divided by the one specified.
     *
     * @param  that the matrix divisor.
     * @return <code>this.multiply(that.inverse())</code>.
     */
    public Matrix divide(Matrix that) {
        return this.multiply(that.inverse());
    }

    /**
     * Returns the pseudo-inverse of this matrix. The matrix doesn't need
     * to be square and can be ill-formed. Stability is guaranteed.
     *
     * @return the pseudo-inverse of this matrix.
     */
    public Matrix pseudoInverse() {
        Matrix thisTranspose = this.transpose();
        return (thisTranspose.multiply(this)).inverse().multiply(thisTranspose);
    }

    /**
     * Returns the inverse of this matrix.
     *
     * <p> Note: To resolve the equation <code>A * X = B</code>,
     *           it is usually faster to calculate <code>A.lu().solve(B)</code>
     *           rather than <code>A.inverse().times(B)</code>.</p>
     *
     * @return  the inverse or this matrix or its {@link #pseudoInverse} if 
     *          the matrix is not square.
     * @see     #lu
     */
    public Matrix inverse() {
        if (isSquare()) {
            return lu().inverse();
        } else {
            return pseudoInverse();
        }
    }

    /**
     * Returns the linear algebraic matrix tensor product of this matrix
     * and another.
     *
     * @param  that the second matrix.
     * @return the Kronecker Tensor (direct) product of <code>this</code>
     *         and <code>that</code>.
     * @since  January 2002, by Jonathan Grattage (jjg99c@cs.nott.ac.uk).
     */
    public Matrix tensor(Matrix that) {
        Matrix C = newInstance(this.n * that.n, this.m * that.m);
        boolean endCol = false;
        int cCount = 0, rCount = 0;
        int subMatrix = 0, iref = 0, jref = 0;
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                Matrix X = that.multiply(o[i * this.m + j]);
                rCount = subMatrix % n;
                if (rCount > 0) {
                    endCol = true;
                }
                if ((rCount == 0) && (endCol == true)) {
                    cCount++;
                }
                for (int y = 0; y < that.m; y++) {
                    for (int x = 0; x < that.n; x++) {
                        iref = x + (rCount * that.n);
                        jref = y + (cCount * that.n);
                        C.o[iref * C.m + jref] = X.get(x, y);
                    }
                }
                subMatrix++;
            }
        }
        return C;
    }

    /**
     * Returns the text representation of this matrix.
     *
     * @return the text representation of this matrix.
     */
    public Text toText() {
        TextBuilder cb = TextBuilder.newInstance();
        cb.append("{");
        for (int i = 0; i < n; i++) {
            cb.append("{");
            for (int j = 0; j < m; j++) {
                cb.append(o[i * m + j]);
                if (j != m - 1) {
                    cb.append(", ");
                }
            }
            if (i != n - 1) {
                cb.append("},\n ");
            }
        }
        cb.append("}}");
        return cb.toText();
    }

    /**
     * Returns the transpose of this matrix.
     *
     * @return <code>A'</code>.
     */
    public Matrix transpose() {
        Matrix M = newInstance(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                M.o[j * n + i] = o[i * m + j];
            }
        }
        return M;
    }

    /**
     * Returns the determinant of this matrix. This method uses the matrix 
     * LU decomposition to guarantee correct results even for 
     * non-commutative operables (the famous formula: <i>a00·a11-a10·a01</i> 
     * for 2x2 matrices is incorrect when operables are non-commutative).
     *
     * @return <code>lu().determinant()</code>
     * @throws MatrixException matrix is not square.
     */
    public Operable determinant() {
        return lu().determinant();
    }

    /**
     * Returns the cofactor of an element in this matrix. It is the value
     * obtained by evaluating the determinant formed by the elements not in
     * that particular row or column.
     *
     * @param  i the row index.
     * @param  j the column index.
     * @return the cofactor of <code>THIS[i,j]</code>.
     * @throws MatrixException matrix is not square or its dimension
     *         is less than 2.
     */
    public Operable cofactor(int i, int j) {
        Matrix M = newInstance(n - 1, m - 1);
        int row = 0;
        for (int k1 = 0; k1 < n; k1++) {
            if (k1 == i) {
                continue;
            }
            int column = 0;
            for (int k2 = 0; k2 < m; k2++) {
                if (k2 == j) {
                    continue;
                }
                M.o[row * M.m + column] = o[k1 * m + k2];
                column++;
            }
            row++;
        }
        return M.determinant();
    }

    /**
     * Returns the trace of this matrix.
     *
     * @return the sum of the diagonal elements.
     */
    public Operable trace() {
        Operable sum = o[0];
        for (int i = MathLib.min(n, m); i > 1;) {
            sum = sum.plus(o[--i * m + i]);
        }
        return sum;
    }

    /**
     * Returns the adjoint of this matrix. It is obtained by replacing each
     * element in this matrix with its cofactor and applying a + or - sign
     * according (-1)**(i+j), and then finding the transpose of the resulting
     * matrix.
     *
     * @return the adjoint of this matrix.
     * @throws MatrixException if this matrix is not square or if
     *         its dimension is less than 2.
     */
    public Matrix adjoint() {
        Matrix M = newInstance(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                M.o[i * m + j] = ((i + j) % 2 == 0) ? this.cofactor(i, j)
                        : this.cofactor(i, j).opposite();
            }
        }
        return M.transpose();
    }

    /**
     * Returns this matrix raised at the specified exponent.
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     * @throws MatrixException if this matrix is not square.
     */
    public Matrix pow(int exp) {
        if (exp > 0) {
            PoolContext.enter();
            try {
                Matrix pow2 = this;
                Matrix result = null;
                while (exp >= 1) { // Iteration.
                    if ((exp & 1) == 1) {
                        result = (result == null) ? pow2 : result
                                .multiply(pow2);
                    }
                    pow2 = pow2.multiply(pow2);
                    exp >>>= 1;
                }
                return (Matrix) result.export();
            } finally {
                PoolContext.exit();
            }
        } else if (exp == 0) {
            return this.multiply(this.inverse()); // Identity.
        } else {
            return this.pow(-exp).inverse();
        }
    }

    // Implements Operable.
    public Operable plus(Operable that) {
        return add((Matrix) that);
    }

    // Implements Operable.
    public Operable opposite() {
        return this.negate();
    }

    // Implements Operable.
    public Operable times(Operable that) {
        return multiply((Matrix) that);
    }

    // Implements Operable.
    public Operable reciprocal() {
        return this.inverse();
    }

    // Overrides.
    public void move(ContextSpace cs) {
        for (int i = n * m; i > 0;) {
            o[--i].move(cs);
        }
    }

    /**
     * This inner class represents a factory producing {@link Matrix} instances.
     */
    private final static class MatrixFactory extends Factory {

        private final int _size;

        private MatrixFactory(int size) {
            _size = size;
        }

        public Object create() {
            return new Matrix(new Operable[_size]);
        }

        public void cleanup(Object obj) {
            Matrix M = (Matrix) obj;
            ArrayPool.clear(M.o, 0, M.m * M.n);
        }
    }

    private static final long serialVersionUID = 3257285837854291768L;
}