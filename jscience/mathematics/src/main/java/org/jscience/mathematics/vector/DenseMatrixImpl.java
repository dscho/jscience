/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vector;

import java.util.Arrays;
import java.util.List;
import javolution.context.ObjectFactory;
import javolution.util.FastTable;
import javolution.util.Index;
import org.jscience.mathematics.structure.Field;

/**
 * <p> This class represents the dense matrix default implementation.</p>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, December 12, 2007
 */
final class DenseMatrixImpl<F extends Field<F>> extends DenseMatrix<F> {

    /**
     * Holds the object factory.
     */
    static ObjectFactory<DenseMatrixImpl> FACTORY = new ObjectFactory<DenseMatrixImpl>() {

        @Override
        protected DenseMatrixImpl create() {
            return new DenseMatrixImpl();
        }

        @Override
        protected void cleanup(DenseMatrixImpl matrix) {
            matrix._rows.reset();
        }
    };

    /**
     * Holds this matrix rows.
     */
    final FastTable<DenseVectorImpl<F>> _rows = new FastTable<DenseVectorImpl<F>>();

    /**
     * Holds the transposed view of this matrix
     */
    private final TransposedView _transposedView = new TransposedView();

    // See parent static method.
    public static <F extends Field<F>> DenseMatrixImpl<F> valueOf(List<? extends Vector<F>> rows) {
        DenseMatrixImpl<F> M = DenseMatrixImpl.FACTORY.object();
        final int n = rows.get(0).getDimension();
        for (Vector<F> row : rows) {
            if (row.getDimension() != n)
                throw new DimensionException();
            M._rows.add(DenseVectorImpl.valueOf(row));
        }
        return M;
    }

    // See parent static method.
    public static <F extends Field<F>> DenseMatrixImpl<F> valueOf(Matrix<F> that) {
        if (that instanceof DenseMatrixImpl)
            return (DenseMatrixImpl) that;
        DenseMatrixImpl<F> M = DenseMatrixImpl.FACTORY.object();
        for (int i = 0, m = that.getNumberOfRows(); i < m; i++) {
            M._rows.add(DenseVectorImpl.valueOf(that.getRow(i)));
        }
        return M;
    }

    // See parent static method.
    public static <F extends Field<F>> DenseMatrixImpl<F> valueOf(F[][] elements) {
        DenseMatrixImpl<F> M = DenseMatrixImpl.FACTORY.object();
        for (int i = 0, m = elements.length; i < m; i++) {
            DenseVectorImpl<F> row = DenseVectorImpl.valueOf(elements[i]);
            M._rows.add(row);
        }
        return M;
    }

    // See parent static method.
    public static <F extends Field<F>> DenseMatrixImpl<F> valueOf(Vector<F>... rows) {
        return DenseMatrixImpl.valueOf(Arrays.asList(rows));
    }

    @Override
    public int getNumberOfRows() {
        return _rows.size();
    }

    @Override
    public int getNumberOfColumns() {
        return _rows.get(0).getDimension();
    }

    @Override
    public F get(int i, int j) {
        return _rows.get(i).get(j);
    }

    @Override
    public DenseVectorImpl<F> getRow(int i) {
        return _rows.get(i);
    }

    @Override
    public DenseVectorImpl<F> getColumn(int j) {
        DenseVectorImpl<F> V = DenseVectorImpl.FACTORY.object();
        for (int i = 0, m = _rows.size(); i < m; i++) {
            V._elements.add(_rows.get(i).get(j));
        }
        return V;
    }

    @Override
    public DenseMatrixImpl<F> getSubMatrix(List<Index> rows, List<Index> columns) {
        DenseMatrixImpl<F> M = FACTORY.object();
        for (int i = 0; i < rows.size(); i++) {
            DenseVectorImpl<F> row = this.getRow(rows.get(i).intValue());
            M._rows.add(row.getSubVector(columns));
        }
        return M;
    }

    @Override
    public DenseMatrixImpl<F> opposite() {
        DenseMatrixImpl<F> M = FACTORY.object();
        for (int i = 0, m = _rows.size(); i < m; i++) {
            M._rows.add(_rows.get(i).opposite());
        }
        return M;
    }

    @Override
    public DenseMatrixImpl<F> plus(Matrix<F> that) {
        DenseMatrixImpl<F> M = FACTORY.object();
        final int m = _rows.size();
        if (that.getNumberOfRows() != m)
            throw new DimensionException();
        for (int i = 0; i < m; i++) {
            M._rows.add(_rows.get(i).plus(that.getRow(i)));
        }
        return M;
    }

    @Override
    public DenseMatrixImpl<F> times(F k) {
        DenseMatrixImpl<F> M = FACTORY.object();
        for (int i = 0, m = _rows.size(); i < m; i++) {
            M._rows.add(_rows.get(i).times(k));
        }
        return M;
    }

    @Override
    public DenseMatrixImpl<F> times(Matrix<F> that) {
        //  This is a m-by-n matrix and that is a n-by-p matrix, the matrix result is mxp
        final int m = _rows.size();
        final int n = _rows.get(0).getDimension(); // Number of columns of this.
        final int p = that.getNumberOfColumns(); // Number of columns of that.
        if (n != that.getNumberOfRows())
            throw new DimensionException();
        DenseMatrixImpl<F> M = FACTORY.object();
        for (int i = 0; i < m; i++) {
            DenseVectorImpl<F> V = DenseVectorImpl.FACTORY.object();
            for (int j = 0; j < p; j++) {
                F element = _rows.get(i).times(that.getColumn(j));
                V._elements.add(element);
            }
            M._rows.add(V);
        }
        return M;
    }

    @Override
    public DenseMatrix<F> transpose() {
        return _transposedView;
    }

    @Override
    public DenseMatrixImpl<F> copy() {
        DenseMatrixImpl<F> M = DenseMatrixImpl.FACTORY.object();
        for (int i = 0, m = _rows.size(); i < m; i++) {
            M._rows.add(_rows.get(i).copy());
        }
        return M;
    }

    /**
     * Represents a transposed view of the outer matrix.
     */
    private class TransposedView extends DenseMatrix<F> {

        @Override
        public DenseVectorImpl<F> getRow(int i) {
            return DenseMatrixImpl.this.getColumn(i);
        }

        @Override
        public DenseVectorImpl<F> getColumn(int j) {
            return DenseMatrixImpl.this.getRow(j);
        }

        @Override
        public DenseMatrix<F> getSubMatrix(List<Index> rows, List<Index> columns) {
            return DenseMatrixImpl.this.getSubMatrix(columns, rows)._transposedView;
        }

        @Override
        public DenseMatrix<F> opposite() {
            return DenseMatrixImpl.this.opposite()._transposedView;
        }

        @Override
        public DenseMatrix<F> plus(Matrix<F> that) {
            return DenseMatrixImpl.this.plus(that.transpose())._transposedView;
        }

        @Override
        public DenseMatrix<F> times(F k) {
            return DenseMatrixImpl.this.times(k)._transposedView;
        }

        @Override
        public DenseMatrixImpl<F> times(Matrix<F> that) {
            return DenseMatrixImpl.valueOf(this).times(that);
        }

        @Override
        public DenseMatrixImpl<F> transpose() {
            return DenseMatrixImpl.this;
        }

        @Override
        public DenseMatrixImpl<F> copy() {
            return DenseMatrixImpl.valueOf(this).copy();
        }

        @Override
        public int getNumberOfRows() {
            return DenseMatrixImpl.this.getNumberOfColumns();
        }

        @Override
        public int getNumberOfColumns() {
            return DenseMatrixImpl.this.getNumberOfRows();
        }

        @Override
        public F get(int i, int j) {
            return DenseMatrixImpl.this.get(j, i);
        }
    }

    // For internal use only.
    void set(int i, int j, F e) {
        _rows.get(i).set(j, e);
    }
    private static final long serialVersionUID = 1L;

}
