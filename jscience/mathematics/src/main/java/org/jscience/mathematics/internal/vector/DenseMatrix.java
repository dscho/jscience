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

import javolution.util.FastTable;
import javolution.util.Index;
import javolution.xml.XMLFormat;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import org.jscience.mathematics.structure.Field;

/**
 * <p> This class represents a matrix made of {@link DenseVector dense
 *     vectors} (as rows). To create a dense matrix made of column vectors the 
 *     {@link #transpose} method can be used. 
 *     For example:[code]
 *        DenseVector<Rational> column0 = DenseVector.valueOf(...);
 *        DenseVector<Rational> column1 = DenseVector.valueOf(...);
 *        DenseMatrix<Rational> M = DenseMatrix.valueOf(column0, column1).transpose();
 *     [/code]</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, December 12, 2007
 */
public abstract class DenseMatrix<F extends Field<F>> extends Matrix<F> {

    /**
     * Holds the default XML representation for dense matrices. For example:[code]
     *    <DenseMatrix >
     *        <DenseVector>
     *             <Complex value="1.0 + 0.0i" />
     *             <Complex value="0.0 + 1.0i" />
     *        </DenseVector>
     *        <DenseVector>
     *             <Complex value="0.0 + 1.0i" />
     *             <Complex value="1.0 + 0.0i" />
     *        </DenseVector>
     *    </DenseMatrix>[/code]
     */
    protected static final XMLFormat<DenseMatrix> XML_FORMAT = new XMLFormat<DenseMatrix>(
            DenseMatrix.class) {

        @Override
        public DenseMatrix newInstance(Class<DenseMatrix> cls, InputElement xml)
                throws XMLStreamException {
            FastTable rows = FastTable.newInstance();
            try {
                while (xml.hasNext()) {
                    rows.add(xml.getNext());
                }
                return DenseMatrix.valueOf(rows);
            } finally {
                FastTable.recycle(rows);
            }
        }

        @Override
        public void read(InputElement xml, DenseMatrix M)
                throws XMLStreamException {
            // Nothing to do (already parsed by newInstance)
        }

        @Override
        public void write(DenseMatrix M, OutputElement xml)
                throws XMLStreamException {
            int nbrRows = M.getNumberOfRows();
            for (int i = 0; i < nbrRows;) {
                xml.add(M.getRow(i++));
            }
        }
    };

    /**
     * Returns a dense matrix holding the row vectors from the specified
     * collection (column vectors if {@link #transpose transposed}).
     *
     * @param rows the list of row vectors.
     * @return the matrix having the specified rows.
     * @throws DimensionException if the rows do not have the same dimension.
     */
    public static <F extends Field<F>> DenseMatrix<F> valueOf(
            List<? extends Vector<F>> rows) {
        return DenseMatrixImpl.valueOf(rows);
    }

    /**
     * Returns a dense matrix equivalent to the specified matrix.
     *
     * @param that the matrix to convert.
     * @return <code>that</code> or a dense matrix holding the same elements
     *         as the specified matrix.
     */
    public static <F extends Field<F>> DenseMatrix<F> valueOf(Matrix<F> that) {
        if (that instanceof DenseMatrix)
            return (DenseMatrix<F>) that;
        return DenseMatrixImpl.valueOf(that);
    }

    /**
     * Returns a dense matrix from the specified 2-dimensional array.
     * The first dimension are the rows and the second are the columns.
     * 
     * @param elements this matrix elements.
     * @return the matrix having the specified elements.
     * @throws DimensionException if rows have different length.
     * @see    DenseMatrix 
     */
    public static <F extends Field<F>> DenseMatrix<F> valueOf(F[][] elements) {
        return DenseMatrixImpl.valueOf(elements);
    }

    /**
     * Returns a dense matrix holding the specified row vectors.
     *
     * @param rows the row vectors.
     * @return <code>DenseMatrix.valueOf(Arrays.asList(rows)</code>.
     * @throws DimensionException if the rows do not have the same dimension.
     */
    public static <F extends Field<F>> DenseMatrix<F> valueOf(Vector<F>... rows) {
        return DenseMatrixImpl.valueOf(rows);
    }

    /**
     * Default constructor.
     */
    protected DenseMatrix() {
    }

    @Override
    public abstract DenseVector<F> getRow(int i);

    @Override
    public abstract DenseVector<F> getColumn(int j);

    @Override
    public abstract DenseMatrix<F> getSubMatrix(List<Index> rows, List<Index> columns);

    @Override
    public abstract DenseMatrix<F> opposite();

    @Override
    public abstract DenseMatrix<F> plus(Matrix<F> that);

    @Override
    public DenseMatrix<F> minus(Matrix<F> that) {
        return this.plus(that.opposite());
    }

    @Override
    public abstract DenseMatrix<F> times(F k);

    @Override
    public abstract DenseMatrix<F> times(Matrix<F> that);

    /**
     * Returns the inverse of this matrix (must be square).
     * The default implementation returns
     * <code>LUDecomposition.valueOf(this).inverse()</code>
     *
     * @return <code>1 / this</code>
     * @throws DimensionException if this matrix is not square.
     */
    @Override
    public DenseMatrix<F> inverse() {
        return LUDecomposition.valueOf(this).inverse();
    }

    /**
     * Returns the determinant of this matrix. The default implementation
     * for {@link DenseMatrix} uses the LU decomposition.
     *
     * @return <code>LUDecomposition.valueOf(this).determinant()</code>
     * @throws DimensionException if this matrix is not square.
     */
    @Override
    public F determinant() {
        return LUDecomposition.valueOf(this).determinant();
    }

    /**
     * Solves this matrix for the specified matrix (returns <code>x</code>
     * such as <code>this · x = y</code>). The default implementation
     * for {@link DenseMatrix} uses the LU decomposition.
     *
     * @return <code>LUDecomposition.valueOf(this).solve(y)</code>
     * @throws DimensionException if this matrix is not square.
     */
    @Override
    public DenseMatrix<F> solve(Matrix<F> y) {
        return LUDecomposition.valueOf(this).solve(y);
    }

    @Override
    public abstract DenseMatrix<F> transpose();

    @Override
    public abstract DenseMatrix<F> copy();

    private static final long serialVersionUID = 1L;

}
