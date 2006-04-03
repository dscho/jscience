/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import org.jscience.mathematics.numbers.Float64;

/**
 * <p> This class represents an optimized {@link Matrix matrix} implementation
 *     for 64 bits floating point elements.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Vector_space">
 *      Wikipedia: Vector Space</a>
 */
public final class MatrixFloat64 extends Matrix<Float64> {

    /**
     * Holds the values.
     */
    private double[] _values;

    /**
     * Holds the number of rows.
     */
    private int _m;

    /**
     * Holds the number of columns.
     */
    private int _n;

    /**
     * Returns a matrix holding the specified values.
     *
     * @param values the matrix values.
     * @return the matrix having the specified values.
     * @throws DimensionException if the rows do not have the same length.
     */
    static MatrixFloat64 newInstance(double[][] values) {
        int m = values.length;
        int n = values[0].length;
        MatrixFloat64 M = MatrixFloat64.newInstance(m, n);
        for (int i = 0, k = 0; i < m; i++) {
            if (values[i].length != n)
                throw new DimensionException(
                        "All rows must have the same length.");
            for (int j = 0; j < n; j++) {
                M._values[k++] = values[i][j];
            }
        }
        return M;
    }

    /**
     * Returns a MatrixFloat64 equivalent to the specified matrix.
     *
     * @param that the matrix to convert. 
     * @return <code>that</code> or new equivalent MatrixFloat64.
     */
    static MatrixFloat64 valueOf(Matrix<Float64> that) {
        if (that instanceof MatrixFloat64)
            return (MatrixFloat64) that;
        int m = that.getNumberOfRows();
        int n = that.getNumberOfColumns();
        MatrixFloat64 M = MatrixFloat64.newInstance(m, n);
        for (int i = 0, k = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M._values[k++] = that.get(i, j).doubleValue();
            }
        }
        return M;
    }

    /**
     * Returns the matrix value for the specified element (fast).
     *
     * @param  i the row index (range [0..m[).
     * @param  j the column index (range [0..n[).
     * @return the element read at [i,j].
     * @throws IndexOutOfBoundsException <code>
     *         ((i < 0) || (i >= m)) || ((j < 0) || (j >= n))</code>
     */
    public double getValue(int i, int j) {
        if (((i < 0) || (i >= _m)) || ((j < 0) || (j >= _n)))
            throw new ArrayIndexOutOfBoundsException();
        return _values[i * _n + j];
    }

    @Override
    public int getNumberOfRows() {
        return _m;
    }

    @Override
    public int getNumberOfColumns() {
        return _n;
    }

    @Override
    public Float64 get(int i, int j) {
        if (((i < 0) || (i >= _m)) || ((j < 0) || (j >= _n)))
            throw new ArrayIndexOutOfBoundsException();
        return Float64.valueOf(_values[i * _n + j]);
    }

    @Override
    public MatrixFloat64 opposite() {
        MatrixFloat64 M = MatrixFloat64.newInstance(_m, _n);
        int size = _m * _n;
        for (int i = size; --i >= 0;) {
            M._values[i] = -this._values[i];
        }
        return M;
    }

    @Override
    public MatrixFloat64 plus(Matrix<Float64> that) {
        MatrixFloat64 t = MatrixFloat64.valueOf(that);
        if ((_m != t._m) || (_n != t._n))
            throw new DimensionException();
        MatrixFloat64 M = MatrixFloat64.newInstance(_m, _n);
        int size = _m * _n;
        for (int i = size; --i >= 0;) {
            M._values[i] = this._values[i] + t._values[i];
        }
        return M;
    }

    @Override
    public MatrixFloat64 minus(Matrix<Float64> that) {
        MatrixFloat64 t = MatrixFloat64.valueOf(that);
        if ((_m != t._m) || (_n != t._n))
            throw new DimensionException();
        MatrixFloat64 M = MatrixFloat64.newInstance(_m, _n);
        int size = _m * _n;
        for (int i = size; --i >= 0;) {
            M._values[i] = this._values[i] - t._values[i];
        }
        return M;
    }

    @Override
    public MatrixFloat64 times(Matrix<Float64> that) {
        MatrixFloat64 t = MatrixFloat64.valueOf(that);
        if (_n != t._m)
            throw new DimensionException();
        MatrixFloat64 M = MatrixFloat64.newInstance(_m, t._n);
        for (int i = 0; i < _m; i++) {
            for (int j = 0; j < t._n; j++) {
                double sum = _values[i * _n] * t._values[j];
                for (int k = 1; k < _n; k++) {
                    sum += _values[i * _n + k] * t._values[k * t._n + j];
                }
                M._values[i * M._n + j] = sum;
            }
        }
        return M;
    }

    @Override
    public VectorFloat64 times(Vector<Float64> that) {
        VectorFloat64 v = VectorFloat64.valueOf(that);
        if (v.getDimension() != _n)
            throw new DimensionException();
        VectorFloat64 r = VectorFloat64.newInstance(_m);
        for (int i = 0; i < _m; i++) {
            double sum = 0.0;
            for (int k = 0; k < _n; k++) {
                sum += this.get_(i, k) * v.get_(k);
            }
            r.set_(i, sum);
        }
        return r;
    }

    // Sets the specified element.
    final void set_(int i, int j, double value) {
        _values[i * _n + j] = value;
    }

    // Gets the specified element.
    final double get_(int i, int j) {
        return _values[i * _n + j];
    }

    ///////////////////////
    // Factory creation. //
    ///////////////////////

    static MatrixFloat64 newInstance(int m, int n) {
        MatrixFloat64 M = FACTORY.object();
        int size = m * n;
        if ((M._values == null) || (M._values.length < size)) {
            M._values = new double[size];
        }
        M._m = m;
        M._n = n;
        return M;
    }

    // TODO Use different factories for large matrices.
    private static Factory<MatrixFloat64> FACTORY = new Factory<MatrixFloat64>() {
        protected MatrixFloat64 create() {
            return new MatrixFloat64();
        }
    };

    private MatrixFloat64() {
    }

    private static final long serialVersionUID = 1L;
}