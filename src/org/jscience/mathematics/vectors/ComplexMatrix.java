/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import java.util.Iterator;
import java.util.List;

import javolution.context.ConcurrentContext;
import javolution.lang.MathLib;
import javolution.util.FastTable;
import javolution.util.Index;

import org.jscience.mathematics.numbers.Complex;

/**
 * <p> This class represents a matrix made of {@link ComplexVector dense
 *     vectors} (as rows). To create a dense matrix made of column vectors the 
 *     {@link #transpose} method can be used. 
 *     For example:[code]
 *        ComplexVector<Rational> column0 = ComplexVector.valueOf(...);
 *        ComplexVector<Rational> column1 = ComplexVector.valueOf(...);
 *        ComplexMatrix<Rational> M = ComplexMatrix.valueOf(column0, column1).transpose();
 *     [/code]</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.3, January 2, 2007
 */
public final class ComplexMatrix extends Matrix<Complex> {

    /**
     * Holds the number of columns n.
     */
    int _n;;

    /**
     * Indicates if this matrix is transposed (the rows are then the columns).
     */
    boolean _transposed;

    /**
     * Holds this matrix rows (or columns when transposed).
     */
    final FastTable<ComplexVector> _rows = new FastTable<ComplexVector>();

    /**
     * Returns a complex matrix from the specified 2-dimensional array.
     * The first dimension being the row and the second being the column.
     * 
     * @param elements this matrix elements.
     * @return the matrix having the specified elements.
     * @throws DimensionException if rows have different length.
     * @see    ComplexVector
     */
    public static ComplexMatrix valueOf(Complex[][] elements) {
        int m = elements.length;
        int n = elements[0].length;
        ComplexMatrix M = ComplexMatrix.newInstance(n, false);
        for (int i = 0; i < m; i++) {
            ComplexVector row = ComplexVector.valueOf(elements[i]);
            if (row.getDimension() != n)
                throw new DimensionException();
            M._rows.add(row);
        }
        return M;
    }
    
    /**
     * Returns a complex matrix holding the specified row vectors 
     * (column vectors if {@link #transpose transposed}).
     *
     * @param rows the row vectors.
     * @return the matrix having the specified rows.
     * @throws DimensionException if the rows do not have the same dimension.
     */
    public static ComplexMatrix valueOf(ComplexVector... rows) {
        final int n = rows[0].getDimension();
        ComplexMatrix M = ComplexMatrix.newInstance(n, false);
        for (int i = 0, m = rows.length; i < m; i++) {
            ComplexVector rowi = rows[i];
            if (rowi.getDimension() != n)
                throw new DimensionException(
                        "All vectors must have the same dimension.");
            M._rows.add(rowi);
        }
        return M;
    }

    /**
     * Returns a complex matrix holding the row vectors from the specified 
     * collection (column vectors if {@link #transpose transposed}).
     *
     * @param rows the list of row vectors.
     * @return the matrix having the specified rows.
     * @throws DimensionException if the rows do not have the same dimension.
     */
    public static ComplexMatrix valueOf(List<ComplexVector> rows) {
        final int n = rows.get(0).getDimension();
        ComplexMatrix M = ComplexMatrix.newInstance(n, false);
        Iterator<ComplexVector> iterator = rows.iterator();
        for (int i = 0, m = rows.size(); i < m; i++) {
            ComplexVector rowi = iterator.next();
            if (rowi.getDimension() != n)
                throw new DimensionException(
                        "All vectors must have the same dimension.");
            M._rows.add(rowi);
        }
        return M;
    }
    
    /**
     * Returns a complex matrix equivalent to the specified matrix.
     *
     * @param that the matrix to convert.
     * @return <code>that</code> or a complex matrix holding the same elements
     *         as the specified matrix.
     */
    public static ComplexMatrix valueOf(Matrix<Complex> that) {
        if (that instanceof ComplexMatrix) return (ComplexMatrix)that;
        int n = that.getNumberOfColumns();
        int m = that.getNumberOfRows();
        ComplexMatrix M = ComplexMatrix.newInstance(n, false);
        for (int i = 0; i < m; i++) {
            ComplexVector rowi = ComplexVector.valueOf(that.getRow(i));
            M._rows.add(rowi);
        }
        return M;
    }

    @Override
    public int getNumberOfRows() {
        return _transposed ? _n : _rows.size();
    }

    @Override
    public int getNumberOfColumns() {
        return _transposed ? _rows.size() : _n;
    }

    @Override
    public Complex get(int i, int j) {
        return _transposed ? _rows.get(j).get(i) : _rows.get(i).get(j);
    }

    @Override
    public ComplexVector getRow(int i) {
        if (!_transposed) return _rows.get(i);
        // Else transposed.
        int n = _rows.size();
        int m = _n;
        if ((i < 0) || (i >= m)) throw new DimensionException();
        ComplexVector V = ComplexVector.newInstance(n);
        for (int j=0; j < n; j++) {
            V.set(j, _rows.get(j).get(i));
        }
        return V;
    }

    @Override
    public ComplexVector getColumn(int j) {
        if (_transposed) return _rows.get(j);
        int m = _rows.size();
        if ((j < 0) || (j >= _n)) throw new DimensionException();
        ComplexVector V = ComplexVector.newInstance(m);
        for (int i=0; i < m; i++) {
            V.set(i, _rows.get(i).get(j));
        }
        return V;
    }

    @Override
    public ComplexVector getDiagonal() {
        int m = this.getNumberOfRows();
        int n = this.getNumberOfColumns();
        int dimension = MathLib.min(m, n);
        ComplexVector V = ComplexVector.newInstance(dimension);
        for (int i=0; i < dimension; i++) {
            V.set(i, this.get(i, i));
        }
        return V;
    }
    
    @Override
    public ComplexMatrix opposite() {
        ComplexMatrix M = ComplexMatrix.newInstance(_n, _transposed);
        for (int i = 0, p = _rows.size(); i < p; i++) {
            M._rows.add(_rows.get(i).opposite());
        }
        return M;
    }

    @Override
    public ComplexMatrix plus(Matrix<Complex> that) {
        if (this.getNumberOfRows() != that.getNumberOfRows())
            throw new DimensionException();
        ComplexMatrix M = ComplexMatrix.newInstance(_n, _transposed);
        for (int i = 0, p = _rows.size(); i < p; i++) {
            M._rows.add(_rows.get(i).plus(_transposed ? that.getColumn(i) : that.getRow(i)));
        }
        return M;
    }

    @Override
    public ComplexMatrix minus(Matrix<Complex> that) { // Returns more specialized type.
        return this.plus(that.opposite()); 
    }

    @Override
    public ComplexMatrix times(Complex k) {
        ComplexMatrix M = ComplexMatrix.newInstance(_n, _transposed);
        for (int i = 0, p = _rows.size(); i < p; i++) {
            M._rows.add(_rows.get(i).times(k));
        }
        return M;
    }

    @Override
    public ComplexVector times(Vector<Complex> v) {
        if (v.getDimension() != this.getNumberOfColumns())
            throw new DimensionException();
        final int m = this.getNumberOfRows();
        ComplexVector V = ComplexVector.newInstance(m);
        for (int i = 0; i < m; i++) {
            V.set(i, this.getRow(i).times(v));
        }
        return V;
    }

    @Override
    public ComplexMatrix times(Matrix<Complex> that) {
        if (_transposed) // this.inverse().transpose() == this.transpose().inverse()
            return this.transpose().inverse().transpose();
        // this is a row matrix.
        final int p = that.getNumberOfColumns();
        if (that.getNumberOfRows() != _n)
            throw new DimensionException();
        ComplexMatrix M = ComplexMatrix.newInstance(p, true); // Transposed.
        multiply(that, Index.valueOf(0), Index.valueOf(p), M._rows);
        return M;
    }
    
    // Multiplies this row matrix with the columns of the specified matrix.
    private void multiply(
            Matrix<Complex> that, Index startColumn, Index endColumn, FastTable<ComplexVector> columnsResult) {
        final int start = startColumn.intValue();
        final int end = endColumn.intValue();
        if (end - start < 32) {  // Direct calculation.
            final int m = this._rows.size();
            for (int j = start; j < end; j++) {
                Vector<Complex> thatColj = that.getColumn(j);
                ComplexVector column = ComplexVector.newInstance(m);
                columnsResult.add(column);
                for (int i = 0; i < m; i++) {
                    ComplexVector thisRowi = this._rows.get(i);
                    column.set(i, thisRowi.times(thatColj));
                }
            }   
        } else { // Concurrent execution.
            FastTable<ComplexVector> r1 = FastTable.newInstance();
            FastTable<ComplexVector> r2 = FastTable.newInstance();
            Index half = Index.valueOf((start + end) >> 1);
            ConcurrentContext.enter();
            try {
                ConcurrentContext.execute(MULTIPLY, this, that, startColumn, half, r1);
                ConcurrentContext.execute(MULTIPLY, this, that, half, endColumn, r2);
            } finally {
                ConcurrentContext.exit();
            }
            columnsResult.addAll(r1);
            columnsResult.addAll(r2);
            FastTable.recycle(r1);
            FastTable.recycle(r2);
        }    
    }
    private static ConcurrentContext.Logic MULTIPLY = new ConcurrentContext.Logic() {
        @SuppressWarnings("unchecked")
        public void run() {
            ComplexMatrix _this = getArgument(0);
            Matrix _that = getArgument(1);
            Index _startColumn = getArgument(2);
            Index _endColumn = getArgument(3);
            FastTable _columnsResult = getArgument(4);
            _this.multiply(_that, _startColumn, _endColumn, _columnsResult);
        }
    };

 
    @Override
    public ComplexMatrix inverse() {
        if (!isSquare())
            throw new DimensionException("Matrix not square");
        return ComplexMatrix.valueOf(LUDecomposition.valueOf(this).inverse());
    }
    
    @Override
    public Complex determinant() {
        return LUDecomposition.valueOf(this).determinant();
    }

    @Override
    public ComplexMatrix transpose() {
       ComplexMatrix M = ComplexMatrix.newInstance(_n, !_transposed);
       M._rows.addAll(this._rows);           
       return M;
    }

    @Override
    public Complex cofactor(int i, int j) {
        if (_transposed) {
            int k = i; i = j; j = k; // Swaps i,j
        }
        int m = _rows.size();
        ComplexMatrix M = ComplexMatrix.newInstance(m - 1, _transposed);
        for (int k1=0; k1 < m; k1++) {
            if (k1 == i) continue;
            ComplexVector row = _rows.get(k1); 
            ComplexVector V = ComplexVector.newInstance(_n - 1);
            M._rows.add(V);
            for (int k2=0, k=0; k2 < _n; k2++) {
                if (k2 == j) continue;
                V.set(k++, row.get(k2));
            }
        }
        return M.determinant();
    }
    
    @Override
    public ComplexMatrix adjoint() {
        ComplexMatrix M = ComplexMatrix.newInstance(_n, _transposed);
        int m = _rows.size();
        for (int i = 0; i < m; i++) {
            ComplexVector row = ComplexVector.newInstance(_n);
            M._rows.add(row);
            for (int j = 0; j < _n; j++) {
                Complex cofactor = _transposed ? cofactor(j, i) : cofactor(i, j);
                row.set(j, ((i + j) % 2 == 0) ? cofactor : cofactor.opposite());
            }
        }
        return M.transpose();
    }

    @Override
    public ComplexMatrix tensor(Matrix<Complex> that) {
        return ComplexMatrix.valueOf(DenseMatrix.valueOf(this).tensor(that));
    }

    @Override
    public ComplexVector vectorization() {
        return ComplexVector.valueOf(DenseMatrix.valueOf(this).vectorization());
    }

    @Override
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            // The rows table itself is intrinsic (all final members are)
            // and implicitely moves with this matrix.
            // But it may refer to locally/stack allocated vectors which
            // need to be moved along.
            for (int i=0, n=_rows.size(); i < n; i++) {
                _rows.get(i).move(os);
            }
            return true;
        }
        return false;
    }  
        
    ///////////////////////
    // Factory creation. //
    ///////////////////////

    static ComplexMatrix newInstance(int n, boolean transposed) {
        ComplexMatrix M = FACTORY.object();
        M._n = n;
        M._transposed = transposed;
        return M;
    }
    
    private static Factory<ComplexMatrix> FACTORY = new Factory<ComplexMatrix>() {
        @Override
        protected ComplexMatrix create() {
            return new ComplexMatrix();
        }
        @Override
        protected void cleanup(ComplexMatrix matrix) {
            matrix._rows.reset();
        }
    };

    private ComplexMatrix() {
    }

    private static final long serialVersionUID = 1L;


}