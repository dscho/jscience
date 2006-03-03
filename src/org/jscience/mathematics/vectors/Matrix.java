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
import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.realtime.ConcurrentContext;
import javolution.realtime.PoolContext;
import javolution.realtime.RealtimeObject;
import javolution.util.FastTable;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.numbers.Float64;
import org.jscience.mathematics.structures.Field;
import org.jscience.mathematics.structures.Ring;
import org.jscience.mathematics.structures.VectorSpace;

/**
 * <p> This class represents a rectangular table of elements of a ring-like 
 *     algebraic structure.</p>
 *     
 * <p> Instances of this class can be used to resolve system of linear equations
 *     involving <i>any kind</i> of {@link Field Field} elements
 *     (e.g. {@link org.jscience.mathematics.numbers.Real Real}, 
 *     {@link org.jscience.mathematics.numbers.Complex Complex}, 
 *     {@link org.jscience.physics.measures.Measure Measure},
 *     {@link org.jscience.mathematics.functions.Function Function}, etc).
 *     For example:[code]
 *        // Creates a matrix of complex elements.
 *        Complex[][] complexElements = ...;
 *        Matrix<Complex> M = Matrix.valueOf(complexElements);
 *     [/code]</p>
 *     
 * <p> Non-commutative field multiplication is supported. Invertible square 
 *     matrices may form a non-commutative field (also called a division
 *     ring). In which case this class may be used to resolve system of linear
 *     equations with matrix coefficients.</p>
 *     
 * <p> For maximum performance, {@link javolution.realtime.PoolContext 
 *     PoolContext} are recommended. Matrices implementation may 
 *     also take advantage of {@link ConcurrentContext concurrent contexts}
 *     to accelerate calculations on multi-processor systems.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Matrix_%28mathematics%29">
 *      Wikipedia: Matrix (mathematics)</a>
 */
public abstract class Matrix<F extends Field> extends RealtimeObject implements
     VectorSpace<Matrix<F>, F>, Ring<Matrix<F>>, Immutable {

    /**
     * Holds the default XML representation for {@link Matrix} and its
     * sub-classes.  This representation consists of the matrix 
     * elements as nested XML elements and the matrix <code>row</code> and
     * <code>column</code> as attributes. For example:[code]
     *    <vectors:Matrix row="2" column="1">
     *      <numbers:Complex real="1.0" imaginary="0.0">
     *      <numbers:Complex real="0.0" imaginary="1.0">
     *    </vectors:Matrix>[/code]
     */
    protected static final XmlFormat<Matrix> XML = new XmlFormat<Matrix>(
            Matrix.class) {
        public void format(Matrix M, XmlElement xml) {
            final int m = M.getNumberOfRows();
            final int n = M.getNumberOfColumns();
            xml.setAttribute("rows", m);
            xml.setAttribute("columns", n);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    xml.add(M.get(i, j));
                }
            }
        }

        public Matrix parse(XmlElement xml) {
            int m = xml.getAttribute("rows", 1);
            int n = xml.getAttribute("columns", 1);
            FastTable<Field> elements = FastTable.newInstance();
            for (int i = m * n; --i >= 0;) {
                Field element = xml.getNext();
                elements.add(element);
            }
            return Matrix.valueOf(m, n, elements);
        }
    };

    /**
     * Returns a matrix implementation from the specified 2-dimensional array.
     * The first dimension being the row and the second being the column.
     * 
     * <p>Note: It is safe to reuse the specified array as it is not internally
     *          referenced by the matrix being returned.</p>
     * @param elements this matrix elements.
     * @return the matrix having the specified elements.
     * @throws DimensionException if rows have different length.
     */
    public static <F extends Field> Matrix<F> valueOf(F[][] elements) {
        int m = elements.length;
        int n = elements[0].length;
        MatrixDefault<F> M = MatrixDefault.newInstance(m, n);
        for (int i = 0; i < m; i++) {
            if (elements[i].length != n)
                throw new DimensionException(
                        "All rows must have the same length.");
            for (int j = 0; j < n; j++) {
                M.set_(i, j, elements[i][j]);
            }
        }
        return M;
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
    public static <F extends Field> Matrix<F> valueOf(int m, int n, F diagonal,
            F other) {
        MatrixDefault<F> M = MatrixDefault.newInstance(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.set_(i, j, (i != j) ? other : diagonal);
            }
        }
        return M;
    }

    /**
     * Returns a matrix from a 2-dimensional array of <code>double</code>
     * values. The first dimension being the number
     * of rows and the second being the number of columns ([m,n]).
     *
     * @param  values the array of <code>double</code> values.
     * @return the corresponding matrix of {@link Float64} elements.
     * @throws IllegalArgumentException if rows have different length.
     */
    public static MatrixFloat64 valueOf(double[][] values) {
        return MatrixFloat64.newInstance(values);
    }

    /**
     * Returns a m-by-n matrix populated from the specified collection of
     * {@link Field Field} objects (rows first).
     *
     * @param  m the number of rows.
     * @param  n the number of columns.
     * @param  elements the collection of matrix elements.
     * @return the matrix having the specified size and elements.
     * @throws DimensionException if <code>elements.size() != m * n</code>
     */
    public static <F extends Field> Matrix<F> valueOf(int m, int n,
            Collection<F> elements) {
        if (elements.size() != m * n)
            throw new DimensionException(m * n
                    + " elements expected but found " + elements.size());
        MatrixDefault<F> M = MatrixDefault.newInstance(m, n);
        Iterator<F> iterator = elements.iterator();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.set_(i, j, iterator.next());
            }
        }
        return M;
    }

    /**
     * Default constructor (for sub-classes).
     */
    protected Matrix() {
    }

    /**
     * Returns the number of rows for this matrix.
     *
     * @return m, the number of rows.
     */
    public abstract int getNumberOfRows();

    /**
     * Returns the number of columns for this matrix.
     *
     * @return n, the number of columns.
     */
    public abstract int getNumberOfColumns();

    /**
     * Returns a single element from this matrix.
     *
     * @param  i the row index (range [0..m[).
     * @param  j the column index (range [0..n[).
     * @return the element read at [i,j].
     * @throws IndexOutOfBoundsException <code>
     *         ((i < 0) || (i >= m)) || ((j < 0) || (j >= n))</code>
     */
    public abstract F get(int i, int j);

    ////////////////////////////
    // Default Implementation //
    ////////////////////////////

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
    public final Matrix<F> getMatrix(int i0, int i1, int j0, int j1) {
        int Mm = i1 - i0 + 1;
        int Mn = j1 - j0 + 1;
        MatrixDefault<F> M = MatrixDefault.newInstance(Mm, Mn);
        for (int i = 0; i < Mm; i++) {
            for (int j = 0; j < Mn; j++) {
                M.set_(i, j, this.get(i + i0, j + j0));
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
    public final Matrix<F> getMatrix(int[] rows, int[] columns) {
        int Mn = columns.length;
        int Mm = rows.length;
        MatrixDefault<F> M = MatrixDefault.newInstance(Mm, Mn);
        for (int i = 0; i < Mm; i++) {
            for (int j = 0; j < Mn; j++) {
                M.set_(i, j, this.get(rows[i], columns[j]));
            }
        }
        return M;
    }

    /**
     * Returns the negation of this matrix.
     *
     * @return <code>-this</code>.
     */
    public Matrix<F> opposite() {
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        MatrixDefault<F> M = MatrixDefault.newInstance(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.set_(i, j, (F) this.get(i, j).opposite());
            }
        }
        return M;
    }

    /**
     * Returns the sum of this matrix with the one specified.
     *
     * @param   that the matrix to be added.
     * @return  <code>this + that</code>.
     * @throws  DimensionException matrices's dimensions are different.
     */
    public Matrix<F> plus(Matrix<F> that) {
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        if ((m != that.getNumberOfRows()) || (n != that.getNumberOfColumns()))
            throw new DimensionException();
        MatrixDefault<F> M = MatrixDefault.newInstance(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.set_(i, j, (F) this.get(i, j).plus(that.get(i, j)));
            }
        }
        return M;
    }

    /**
     * Returns the difference between this matrix and the one specified.
     *
     * @param  that the matrix to be subtracted.
     * @return <code>this - that</code>.
     * @throws  DimensionException matrices's dimensions are different.
     \     */
    public Matrix<F> minus(Matrix<F> that) {
        return this.plus(that.opposite());
    }

    /**
     * Returns the product of this matrix by the specified factor.
     *
     * @param  k the coefficient multiplier.
     * @return <code>this · k</code>
     */
    public Matrix<F> times(F k) {
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        MatrixDefault<F> M = MatrixDefault.newInstance(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.set_(i, j, (F) this.get(i, j).times(k));
            }
        }
        return M;
    }

    /**
     * Returns the product of this matrix with the one specified.
     *
     * @param  that the matrix multiplier.
     * @return <code>this · that</code>.
     * @throws DimensionException if <code>
     *         this.getNumberOfColumns() != that.getNumberOfRows()</code>.
     */
    public Matrix<F> times(Matrix<F> that) {
        if (this.getNumberOfColumns() != that.getNumberOfRows())
            throw new DimensionException();
        final int thism = this.getNumberOfRows();
        final int thisn = this.getNumberOfColumns();
        final int thatn = that.getNumberOfColumns();
        MatrixDefault<F> M = MatrixDefault.newInstance(thism, thatn);
        for (int i = 0; i < thism; i++) {
            for (int j = 0; j < thatn; j++) {
                PoolContext.enter();
                try {
                    F sum = (F) this.get(i, 0).times(that.get(0, j));
                    for (int k = 1; k < thisn; k++) {
                        sum = (F) sum
                                .plus(this.get(i, k).times(that.get(k, j)));
                    }
                    M.set_(i, j, sum);
                    sum.move(ObjectSpace.OUTER); // Exports.
                } finally {
                    PoolContext.exit();
                }
            }
        }
        return M;
    }

    /**
     * Returns this matrix divided by the one specified.
     *
     * @param  that the matrix divisor.
     * @return <code>this / that</code>.
     * @throws DimensionException if that matrix is not square or dimensions 
     *         do not match.
     */
    public Matrix<F> divide(Matrix<F> that) {
        return this.times(that.inverse());
    }

    /**
     * Returns the inverse of this matrix (must be square).
     *
     * @return <code>1 / this</code>
     * @throws MatrixException if this matrix is not square.
     */
    public Matrix<F> inverse() {
        if (!isSquare())
            throw new DimensionException("Matrix not square");
        return DecompositionLU.valueOf(this).inverse();
    }

    /**
     * Returns the inverse or pseudo-inverse if this matrix if not square.
     *
     * <p> Note: To resolve the equation <code>A * X = B</code>,
     *           it is usually faster to calculate <code>A.lu().solve(B)</code>
     *           rather than <code>A.inverse().times(B)</code>.</p>
     *
     * @return  the inverse or pseudo-inverse of this matrix.
     */
    public Matrix<F> pseudoInverse() {
        if (isSquare())
            return DecompositionLU.valueOf(this).inverse();
        Matrix<F> thisTranspose = this.transpose();
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
    public Matrix<F> tensor(Matrix<F> that) {
          throw new UnsupportedOperationException("Not yet converted to v3.0");
//        Matrix<O> C = newInstance(this.m * that.m, this.n * that.n);
//        boolean endCol = false;
//        int cCount = 0, rCount = 0;
//        int subMatrix = 0, iref = 0, jref = 0;
//        for (int j = 0; j < n; j++) {
//            for (int i = 0; i < m; i++) {
//                Matrix<O> X = that.times(o[i * this.n + j]);
//                rCount = subMatrix % m;
//                if (rCount > 0) {
//                    endCol = true;
//                }
//                if ((rCount == 0) && (endCol == true)) {
//                    cCount++;
//                }
//                for (int y = 0; y < that.n; y++) {
//                    for (int x = 0; x < that.m; x++) {
//                        iref = x + (rCount * that.m);
//                        jref = y + (cCount * that.m);
//                        C.o[iref * C.n + jref] = X.get(x, y);
//                    }
//                }
//                subMatrix++;
//            }
//        }
//        return C;
    }

    /**
     * Return the product of this matrix by the specified vector.
     *
     * @param  v the vector.
     * @return <code>this · v</code>
     * @throws DimensionException if <code>
     *         v.getDimension() != this.getNumberOfColumns()<code>
     */
    public Vector<F> times(Vector<F> v) {
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        if (v.getDimension() != n)
            throw new DimensionException();
        VectorDefault<F> r = VectorDefault.newInstance(m);
        for (int i=0; i < m; i++) {
            PoolContext.enter();
            try {
                F sum = (F) this.get(i, 0).times(v.get(0));
                for (int k = 1; k < n; k++) {
                    sum = (F) sum.plus(this.get(i, k).times(v.get(k)));
                }
                r.set_(i, sum);
                sum.move(ObjectSpace.OUTER); // Exports.
            } finally {
                PoolContext.exit();
            }
        }
        return r;
    }

    /**
     * Returns the determinant of this matrix.
     *
     * @return this matrix determinant.
     * @throws DimensionException if this matrix is not square.
     */
    public F determinant() {
        return DecompositionLU.valueOf(this).determinant();
    }
 
    /**
     * Returns the transpose of this matrix.
     *
     * @return <code>A'</code>.
     */
    public Matrix<F> transpose() {
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        MatrixDefault<F> M = MatrixDefault.newInstance(n, m);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M.set_(j, i, this.get(i, j));
            }
        }
        return M;
    }

    /**
     * Returns the cofactor of an element in this matrix. It is the value
     * obtained by evaluating the determinant formed by the elements not in
     * that particular row or column.
     *
     * @param  i the row index.
     * @param  j the column index.
     * @return the cofactor of <code>THIS[i,j]</code>.
     * @throws DimensionException matrix is not square or its dimension
     *         is less than 2.
     */
    public F cofactor(int i, int j) {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
//        Matrix<O> M = newInstance(m - 1, n - 1);
//        int row = 0;
//        for (int k1 = 0; k1 < m; k1++) {
//            if (k1 == i) {
//                continue;
//            }
//            int column = 0;
//            for (int k2 = 0; k2 < n; k2++) {
//                if (k2 == j) {
//                    continue;
//                }
//                M.o[row * M.n + column] = o[k1 * n + k2];
//                column++;
//            }
//            row++;
//        }
//        return M.determinant();
    }

    /**
     * Returns the adjoint of this matrix. It is obtained by replacing each
     * element in this matrix with its cofactor and applying a + or - sign
     * according (-1)**(i+j), and then finding the transpose of the resulting
     * matrix.
     *
     * @return the adjoint of this matrix.
     * @throws DimensionException if this matrix is not square or if
     *         its dimension is less than 2.
     */
    public Matrix<F> adjoint() {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
//        Matrix<O> M = newInstance(m, n);
//        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < n; j++) {
//                M.o[i * n + j] = ((i + j) % 2 == 0) ? this.cofactor(i, j)
//                        : this.cofactor(i, j).opposite();
//            }
//        }
//        return M.transpose();
     }

    /**
     * Indicates if this matrix is square.
     *
     * @return <code>getNumberOfRows() == getNumberOfColumns()</code>
     */
    public boolean isSquare() {
        return getNumberOfRows() == getNumberOfColumns();
    }

    /**
     * Solves this matrix for the specified vector (returns <code>x</code>
     * such as <code>this · x = y</code>).
     * 
     * @param  y the vector for which the solution is calculated.
     * @return <code>x</code> such as <code>this · x = y</code>
     * @throws DimensionException if that matrix is not square or dimensions 
     *         do not match.
     */
    public Vector<F> solve(Vector<F> y) {
        return DecompositionLU.valueOf(this).solve(y); // Default implementation.
    }

    /**
     * Solves this matrix for the specified matrix (returns <code>x</code>
     * such as <code>this · x = y</code>).
     * 
     * @param  y the matrix for which the solution is calculated.
     * @return <code>x</code> such as <code>this · x = y</code>
     * @throws DimensionException if that matrix is not square or dimensions 
     *         do not match.
     */
    public Matrix<F> solve(Matrix<F> y) {
        return DecompositionLU.valueOf(this).solve(y); // Default implementation.
    }

    /**
     * Returns this matrix raised at the specified exponent.
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     * @throws DimensionException if this matrix is not square.
     */
    public Matrix<F> pow(int exp) {
        if (exp > 0) {
            PoolContext.enter();
            try {
                Matrix<F> pow2 = this;
                Matrix<F> result = null;
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

    /**
     * Returns the trace of this matrix.
     *
     * @return the sum of the diagonal elements.
     */
    public F trace() {
        F sum = this.get(0, 0);
        for (int i = MathLib.min(getNumberOfColumns(), getNumberOfRows()); --i > 0;) {
            sum = (F) sum.plus(get(i, i));
        }
        return sum;
    }

    /**
     * Returns the text representation of this matrix.
     *
     * @return the text representation of this matrix.
     */
    public Text toText() {
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        TextBuilder tb = TextBuilder.newInstance();
        tb.append('{');
        for (int i = 0; i < m; i++) {
            tb.append('{');
            for (int j = 0; j < n; j++) {
                tb.append(get(i, j).toText());
                if (j != n - 1) {
                    tb.append(", ");
                }
            }
            if (i != m - 1) {
                tb.append("},\n");
            }
        }
        tb.append("}}");
        return tb.toText();
    }

    /**
     * Indicates if this matrix can be considered equals to the one 
     * specified using the specified comparator when testing for 
     * element equality. The specified comparator may allow for some 
     * tolerance in the difference between the matrix elements.
     *
     * @param  that the matrix to compare for equality.
     * @param  cmp the comparator to use when testing for element equality.
     * @return <code>true</code> if this matrix and the specified matrix are
     *         both matrices with equal elements according to the specified
     *         comparator; <code>false</code> otherwise.
     */
    public boolean equals(Matrix<F> that, Comparator<F> cmp) {
        if (this == that)
            return true;
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        if ((that.getNumberOfRows() != m) || (that.getNumberOfColumns() != n))
            return false;
        for (int i = m; --i >= 0;) {
            for (int j = n; --j >= 0;) {
                if (cmp.compare(this.get(i, j), that.get(i, j)) != 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Indicates if this matrix is strictly equal to the object specified.
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if this matrix and the specified object are
     *         both matrices with equal elements; <code>false</code> otherwise.
     * @see    #equals(Matrix, Comparator)
     */
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof Matrix))
            return false;
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        Matrix M = (Matrix) that;
        if ((M.getNumberOfRows() != m) || (M.getNumberOfColumns() != n))
            return false;
        for (int i = m; --i >= 0;) {
            for (int j = n; --j >= 0;) {
                if (!this.get(i, j).equals(M.get(i, j)))
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns a hash code value for this matrix.
     * Equals objects have equal hash codes.
     *
     * @return this matrix hash code value.
     * @see    #equals
     */
    public int hashCode() {
        final int m = this.getNumberOfRows();
        final int n = this.getNumberOfColumns();
        int code = 0;
        for (int i = m; --i >= 0;) {
            for (int j = n; --j >= 0;) {
                code += get(i, j).hashCode();
            }
        }
        return code;
    }

    @Override
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            final int m = this.getNumberOfRows();
            final int n = this.getNumberOfColumns();
            for (int i = m; --i >= 0;) {
                for (int j = n; --j >= 0;) {
                    get(i, j).move(os);
                }
            }
            return true;
        }
        return false;
    }
}