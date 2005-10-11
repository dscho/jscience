/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.matrices;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.jscience.mathematics.numbers.Float64;

import javolution.realtime.ConcurrentContext;
import javolution.realtime.PoolContext;
import javolution.realtime.RealtimeObject;
import javolution.util.FastTable;
import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

/**
 * <p> This class represents an immutable matrix of {@link Operable} elements.
 *     It may be used to resolve system of linear equations involving 
 *     <i>any kind</i> of {@link Operable} elements
 *     (e.g. {@link org.jscience.mathematics.numbers.Real Real}, 
 *     {@link org.jscience.mathematics.numbers.Complex Complex}, 
 *     {@link org.jscience.physics.quantities.Quantity Quantity},
 *     {@link org.jscience.mathematics.functions.Function Function}, etc).</p>
 *
 * <p> Non-commutative multiplication is supported and this class itself
 *     implements the {@link Operable} interface. Consequently, this class may
 *     be used to resolve system of linear equations involving matrices
 *     (for which the multiplication is not commutative).</p>
 *
 * <p> <b>Implementation Note:</b> This class uses {@link ConcurrentContext 
 *     concurrent contexts} to accelerate calculations on multi-processor
 *     systems.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 * @see <a href="http://mathworld.wolfram.com/Matrix.html">
 *      Matrix -- from MathWorld</a> 
 */
public class Matrix<O extends Operable<O>> extends RealtimeObject implements
        Operable<Matrix<O>>, Serializable {

    /**
     * Holds the default XML representation for {@link Matrix} and its
     * sub-classes.  This representation consists of the matrix 
     * elements as nested XML elements and the matrix <code>row</code> and
     * <code>column</code> as attributes. For example: <pre>
     *    &lt;math:Matrix row="2" column="1"&gt;
     *      &lt;math:Complex real="1.0" imaginary="0.0"/&gt;
     *      &lt;math:Complex real="0.0" imaginary="1.0"/&gt;
     *    &lt;/math:Matrix&gt;</pre>
     */
    protected static final XmlFormat<Matrix> XML = new XmlFormat<Matrix>(
            Matrix.class) {
        public void format(Matrix matrix, XmlElement xml) {
            xml.setAttribute("rows", matrix.m);
            xml.setAttribute("columns", matrix.n);
            final int nm = matrix.m * matrix.n;
            for (int i = 0; i < nm; i++) {
                xml.add(matrix.o[i]);
            }
        }

        public Matrix parse(XmlElement xml) {
        	int rows = xml.getAttribute("rows", 1);
        	int columns = xml.getAttribute("columns", 1);
        	FastTable elements = FastTable.newInstance();
        	while (xml.hasNext()) {
        		elements.add(xml.getNext());
        	}
            return Matrix.valueOf(rows, columns, elements);
        }
    };

    /**
     * The array of operable objects (M[i,j] = o[i*n+j], i < m, j < n).
     */
    final O[] o;

    /**
     * Holds the number of rows.
     */
    int m;

    /**
     * Holds the number of columns.
     */
    int n;

    /**
     * Base constructor.
     * 
     * @param size the maximum number of elements.
     */
    Matrix(int size) {
        this.o = (O[]) new Operable[size];
    }

    /**
     * Creates a matrix from the specified 2-dimensional array of 
     * {@link Operable} objects.
     * The first dimension being the row and the second being the column.
     * 
     * <p>Note: It is safe to reuse the specified array as it is not internally
     *          referenced by the matrix being returned.</p>
     * @param elements the array of {@link Operable} elements.
     * @throws IllegalArgumentException if rows have different length.
     */
    public Matrix(O[][] elements) {
        this.m = elements.length;
        this.n = elements[0].length;
        this.o = (O[]) new Operable[m * n];
        for (int i = 0; i < m; i++) {
            if (elements[i].length != n)
                throw new IllegalArgumentException(
                        "All rows must have the same length.");
            System.arraycopy(elements[i], 0, o, i * n, n);
        }
    }

    /**
     * Returns a m-by-n matrix filled with the specified diagonal element
     * and the specified non-diagonal element.
     * This constructor may be used to create an identity matrix
     * (e.g. <code>valueOf(m, m, ONE, ZERO)</code>).
     *
     * @param  m the number of rows.
     * @param  n the number of columns.
     * @param  diagonal the diagonal element.
     * @param  other the non-diagonal element.
     * @return a m-by-n matrix with <code>d</code> on the diagonal and
     *         <code>o</code> elsewhere.
     */
    public static <O extends Operable<O>> Matrix<O> valueOf(int m, int n,
            O diagonal, O other) {
        Matrix<O> M = newInstance(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.o[i * n + j] = (i != j) ? other : diagonal;
            }
        }
        return M;
    }

    /**
     * Returns a matrix from a 2-dimensional array of <code>double</code>
     * values (convenience method). The first dimension being the number
     * of rows and the second being the number of columns ([m,n]).
     *
     * @param  values the array of <code>double</code> values.
     * @return the corresponding matrix of {@link Float64} elements.
     * @throws IllegalArgumentException if rows have different length.
     */
    public static Matrix<Float64> valueOf(double[][] values) {
        int m = values.length;
        int n = values[0].length;
        Matrix<Float64> M = newInstance(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.set(i, j, Float64.valueOf(values[i][j]));
            }
        }
        return M;
    }

    /**
     * Returns a matrix from a 2-dimensional array of {@link Operable} objects.
     * The first dimension being the number of rows and the second being the
     * number of columns ([m,n]).
     *
     * <p>Note: It is safe to reuse the specifed array as it is not internally
     *          referenced by the matrix being returned.</p>
     *
     * @param  elements the array of {@link Operable} objects.
     * @return the matrix having the specified components.
     * @throws IllegalArgumentException if rows have different length.
     */
    public static <O extends Operable<O>> Matrix<O> valueOf(O[][] elements) {
        int m = elements.length;
        int n = elements[0].length;
        Matrix<O> M = newInstance(m, n);
        for (int i = 0; i < m; i++) {
            if (elements[i].length != n)
                throw new IllegalArgumentException(
                        "All rows must have the same length.");
            System.arraycopy(elements[i], 0, M.o, i * n, n);
        }
        return M;
    }

    /**
     * Returns a m-by-n matrix populated from the specified collection of
     * {@link Operable} objects (rows first).
     *
     * @param  m the number of rows.
     * @param  n the number of columns.
     * @param  elements the collection of {@link Operable} objects.
     * @return the matrix having the specified size and elements.
     * @throws MatrixException if <code>elements.size() != m * n</code>
     * @throws ClassCastException if any of the element is not {@link Operable}.
     */
    public static <O extends Operable<O>> Matrix<O> valueOf(int m, int n,
            Collection<O> elements) {
        if (elements.size() != m * n)
            throw new MatrixException(m * n + " elements expected but found "
                    + elements.size());
        Matrix<O> M = newInstance(m, n);
        Iterator<O> iterator = elements.iterator();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.o[i * n + j] = iterator.next();
            }
        }
        return M;
    }

    /**
     * Returns the number of rows for this matrix.
     *
     * @return m, the number of rows.
     */
    public final int getNumberOfRows() {
        return m;
    }

    /**
     * Returns the number of columns for this matrix.
     *
     * @return n, the number of columns.
     */
    public final int getNumberOfColumns() {
        return n;
    }

    /**
     * Indicates if this matrix is square.
     *
     * @return <code>nbrOfRows() == nbrOfColumns()</code>
     */
    public final boolean isSquare() {
        return m == n;
    }

    /**
     * Returns a single element from this matrix.
     *
     * @param  i the row index (range [0..m[).
     * @param  j the column index (range [0..n[).
     * @return the element read at [i,j].
     * @throws IndexOutOfBoundsException <code>
     *         (i < 0) || (i >= m)) || (j < 0) || (j >= n))</code>
     */
    public final O get(int i, int j) {
        if ((i < 0) || (i >= m) || (j < 0) || (j >= n))
            throw new IndexOutOfBoundsException("i: " + i + ", j: " + j
                    + " (matrix[" + m + "," + n + "])");
        return o[i * n + j];
    }

    /**
     * Sets a element from this matrix.
     *
     * @param  i the row index (range [0..m[).
     * @param  j the column index (range [0..n[).
     * @param  element the element at [i,j].
     */
    final void set(int i, int j, O element) {
    	o[i * n + j] = element;
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
    public final Matrix<O> getMatrix(int i0, int i1, int j0, int j1) {
        int Mm = i1 - i0 + 1;
        int Mn = j1 - j0 + 1;
        Matrix<O> M = newInstance(Mm, Mn);
        for (int i = 0; i < Mm; i++) {
            for (int j = 0; j < Mn; j++) {
                M.o[i * Mn + j] = this.o[(i + i0) * n + j + j0];
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
    public final Matrix<O> getMatrix(int[] rows, int[] columns) {
        int Mn = columns.length;
        int Mm = rows.length;
        Matrix<O> M = newInstance(Mm, Mn);
        for (int i = 0; i < Mm; i++) {
            for (int j = 0; j < Mn; j++) {
                M.o[i * Mn + j] = this.o[rows[i] * n + columns[j]];
            }
        }
        return M;
    }

    /**
     * Returns the {@link LUDecomposition} of this {@link Matrix}.
     * Numerical stability is guaranteed  (through pivoting) if the
     * {@link Operable} elements of this matrix are derived from 
     * {@link org.jscience.mathematics.numbers.Numeric Numeric}.
     * For others elements types, numerical stability can be ensured by setting
     * a local {@link LUDecomposition#setPivotComparator pivot comparator}.
     *
     * @return the decomposition of this matrix into a product of upper and
     *         lower triangular matrices.
     */
    public final LUDecomposition<O> lu() {
        return LUDecomposition.valueOf(this);
    }

    /**
     * Indicates if this matrix is equal to the object specified.
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if this matrix and the specified object are
     *         considered equal; <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof Matrix) {
            Matrix M = (Matrix) that;
            if (M.m != this.m || M.n != this.n) {
                return false;
            } else {
                for (int i = m * n; i > 0;) {
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
    public int hashCode() {
        int code = 0;
        for (int i = m * n; i > 0;) {
            code += o[--i].hashCode();
        }
        return code;
    }

    /**
     * Returns the negation of this matrix.
     *
     * @return <code>-this</code>.
     */
    public Matrix<O> opposite() {
        Matrix<O> M = newInstance(m, n);
        for (int i = m * n; i > 0;) {
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
    public Matrix<O> plus(Matrix<O> that) {
        if ((this.m == that.m) && (this.n == that.n)) {
            Matrix<O> M = newInstance(m, n);
            for (int i = m * n; i > 0;) {
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
    public Matrix<O> minus(Matrix<O> that) {
        if ((this.m == that.m) && (this.n == that.n)) {
            Matrix<O> M = newInstance(m, n);
            for (int i = m * n; i > 0;) {
                M.o[--i] = this.o[i].plus(that.o[i].opposite());
            }
            return M;
        } else {
            throw new MatrixException();
        }
    }

    /**
     * Return the product of this matrix with the specified factor.
     *
     * @param  k the coefficient multiplier.
     * @return <code>k * M</code>.
     */
    public Matrix<O> times(O k) {
        Matrix<O> M = newInstance(m, n);
        for (int i = m * n; i > 0;) {
            M.o[--i] = k.times(this.o[i]);
        }
        return M;
    }

    /**
     * Returns the product of this matrix with the one specified.
     *
     * @param  that the matrix multiplier.
     * @return <code>this * that</code>.
     * @throws MatrixException <code>M.getRowDimension()
     *         != this.getColumnDimension()</code>.
     */
    public Matrix<O> times(Matrix<O> that) {
        if (this.n == that.m) {
            Matrix<O> M = newInstance(this.m, that.n);
            for (int i = 0; i < this.m; i++) {
                final int i_m = i * n;
                for (int j = 0; j < that.n; j++) {
                    O sum = this.o[i_m].times(that.o[j]);
                    for (int k = 1; k < this.n; k++) {
                        sum = sum.plus(this.o[i_m + k].times(that.o[k * that.n
                                + j]));
                    }
                    M.o[i * M.n + j] = sum;
                }
            }
            return M;
        } else {
            throw new MatrixException();
        }
    }

    /**
     * Returns this matrix divided by the one specified.
     *
     * @param  that the matrix divisor.
     * @return <code>this / that</code>.
     * @throws MatrixException if that matrix is not square or dimensions 
     *         do not match.
     */
    public Matrix<O> divide(Matrix<O> that) {
        return this.times(that.reciprocal());
    }

    /**
     * Returns the reciprocal of this matrix (must be square).
     *
     * @return <code>1 / this</code>
     * @throws MatrixException if this matrix is not square.
     * @see     #lu
     */
    public Matrix<O> reciprocal() {
        if (!isSquare())
            throw new MatrixException("Matrix not square");
        return lu().inverse();
    }

    /**
     * Returns the inverse or pseudo-inverse if this matrix if not square.
     *
     * <p> Note: To resolve the equation <code>A * X = B</code>,
     *           it is usually faster to calculate <code>A.lu().solve(B)</code>
     *           rather than <code>A.inverse().times(B)</code>.</p>
     *
     * @return  the inverse or pseudo-inverse of this matrix.
     * @see     #lu
     */
    public Matrix<O> inverse() {
        if (isSquare())
            return lu().inverse();
        Matrix<O> thisTranspose = this.transpose();
        return (thisTranspose.times(this)).inverse().times(thisTranspose);
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
    public Matrix<O> tensor(Matrix<O> that) {
        Matrix<O> C = newInstance(this.m * that.m, this.n * that.n);
        boolean endCol = false;
        int cCount = 0, rCount = 0;
        int subMatrix = 0, iref = 0, jref = 0;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                Matrix<O> X = that.times(o[i * this.n + j]);
                rCount = subMatrix % m;
                if (rCount > 0) {
                    endCol = true;
                }
                if ((rCount == 0) && (endCol == true)) {
                    cCount++;
                }
                for (int y = 0; y < that.n; y++) {
                    for (int x = 0; x < that.m; x++) {
                        iref = x + (rCount * that.m);
                        jref = y + (cCount * that.m);
                        C.o[iref * C.n + jref] = X.get(x, y);
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
        for (int i = 0; i < m; i++) {
            cb.append("{");
            for (int j = 0; j < n; j++) {
                cb.append(o[i * n + j]);
                if (j != n - 1) {
                    cb.append(", ");
                }
            }
            if (i != m - 1) {
                cb.append("},\n");
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
    public Matrix<O> transpose() {
        Matrix<O> M = newInstance(n, m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.o[j * m + i] = o[i * n + j];
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
    public O determinant() {
        return (O) lu().determinant();
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
    public O cofactor(int i, int j) {
        Matrix<O> M = newInstance(m - 1, n - 1);
        int row = 0;
        for (int k1 = 0; k1 < m; k1++) {
            if (k1 == i) {
                continue;
            }
            int column = 0;
            for (int k2 = 0; k2 < n; k2++) {
                if (k2 == j) {
                    continue;
                }
                M.o[row * M.n + column] = o[k1 * n + k2];
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
    public O trace() {
        O sum = o[0];
        for (int i = MathLib.min(m, n); i > 1;) {
            sum = sum.plus(o[--i * n + i]);
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
    public Matrix<O> adjoint() {
        Matrix<O> M = newInstance(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.o[i * n + j] = ((i + j) % 2 == 0) ? this.cofactor(i, j)
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
    public Matrix<O> pow(int exp) {
        if (exp > 0) {
            PoolContext.enter();
            try {
                Matrix<O> pow2 = this;
                Matrix<O> result = null;
                while (exp >= 1) { // Iteration.
                    if ((exp & 1) == 1) {
                        result = (result == null) ? pow2 : result.times(pow2);
                    }
                    pow2 = pow2.times(pow2);
                    exp >>>= 1;
                }
                result.export();
                return result;
            } finally {
                PoolContext.exit();
            }
        } else if (exp == 0) {
            return this.times(this.inverse()); // Identity.
        } else {
            return this.pow(-exp).inverse();
        }
    }

    // Overrides.
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            for (int i = m * n; i > 0;) {
                o[--i].move(os);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a m-by-n matrix filled with <code>null</code> elements.
     *
     * @param  m the number of rows.
     * @param  n the number of columns.
     * @return a m-by-n matrix non-initialized (a {@link Vector} instance
     *         if <code>n == 1</code>).
     */
    static <O extends Operable<O>> Matrix<O> newInstance(int m, int n) {
        if (n == 1)
            return Vector.newInstance(m);
        Matrix M;
        final int size = n * m;
        if (size <= 1 << 3) {
            M = FACTORY_3.object();
        } else if (size <= 1 << 6) {
            M = FACTORY_6.object();
        } else if (size <= 1 << 9) {
            M = FACTORY_9.object();
        } else if (size <= 1 << 12) {
            M = FACTORY_12.object();
        } else if (size <= 1 << 15) {
            M = FACTORY_15.object();
        } else if (size <= 1 << 18) {
            M = FACTORY_18.object();
        } else if (size <= 1 << 21) {
            M = FACTORY_21.object();
        } else if (size <= 1 << 24) {
            M = FACTORY_24.object();
        } else if (size <= 1 << 27) {
            M = FACTORY_27.object();
        } else if (size <= 1 << 30) {
            M = FACTORY_30.object();
        } else {
            throw new UnsupportedOperationException("Matrix too large");
        }
        M.m = m;
        M.n = n;
        return M;
    }

    // TBD: Use recursive structures (North, East, South, West)
    //      instead of large arrays.

    private static final Factory<Matrix> FACTORY_3 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 3);
        }
    };

    private static final Factory<Matrix> FACTORY_6 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 6);
        }
    };

    private static final Factory<Matrix> FACTORY_9 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 9);
        }
    };

    private static final Factory<Matrix> FACTORY_12 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 12);
        }
    };

    private static final Factory<Matrix> FACTORY_15 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 15);
        }
    };

    private static final Factory<Matrix> FACTORY_18 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 18);
        }
    };

    private static final Factory<Matrix> FACTORY_21 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 21);
        }
    };

    private static final Factory<Matrix> FACTORY_24 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 24);
        }
    };

    private static final Factory<Matrix> FACTORY_27 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 27);
        }
    };

    private static final Factory<Matrix> FACTORY_30 = new Factory<Matrix>() {
        protected Matrix create() {
            return new Matrix(1 << 30);
        }
    };

    private static final long serialVersionUID = 1L;

}