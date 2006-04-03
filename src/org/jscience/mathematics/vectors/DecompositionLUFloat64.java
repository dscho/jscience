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
 * <p> This class represents the Lower/Upper matrix decomposition for 
 *     Float64 matrices (optimization).</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
final class DecompositionLUFloat64 extends DecompositionLU<Float64> {
    
    /**
     * Holds the dimension of the square matrix source.
     */
    private int _n;

    /**
     * Holds the pivots indexes.
     */
    private int[] _pivots;

    /**
     * Holds the LU elements.
     */
    private double[] _lu;

    /**
     * Holds temporary buffer.
     */
    private double[] _tmp;

    /**
     * Holds the number of permutation performed.
     */
    private int _permutationCount;


    @Override
    public VectorFloat64 solve(Vector<Float64> BB) {
        // TODO Optimize.
        VectorFloat64 B = VectorFloat64.valueOf(BB);
        MatrixFloat64 A = solve(B.asColumnMatrix());
        VectorFloat64 v = VectorFloat64.newInstance(A.getNumberOfRows());
        if (A.getNumberOfColumns() != 1)
            throw new InternalError();
        for (int i = A.getNumberOfRows(); --i >= 0;) {
            v.set_(i, A.getValue(i, 0));
        }
        return v;
    }

    @Override
    public MatrixFloat64 solve(Matrix<Float64> BB) {
        MatrixFloat64 B = MatrixFloat64.valueOf(BB);
        if (_n != B.getNumberOfRows())
            throw new DimensionException("Input vector has "
                    + B.getNumberOfRows() + " rows instead of " + _n);

        // Copies B with pivoting.
        final int n = B.getNumberOfColumns();
        MatrixFloat64 X = MatrixFloat64.newInstance(_n, n);
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < n; j++) {
                X.set_(i, j, B.getValue(_pivots[i], j));
            }
        }

        // Solves L * Y = pivot(B)
        for (int k = 0; k < _n; k++) {
            for (int i = k + 1; i < _n; i++) {
                double luik = _lu[i * _n + k];
                for (int j = 0; j < n; j++) {
                    X.set_(i, j, X.getValue(i, j) - luik * X.getValue(k, j));
                }
            }
        }

        // Solves U * X = Y;
        for (int k = _n - 1; k >= 0; k--) {
            for (int j = 0; j < n; j++) {
                X.set_(k, j, X.getValue(k, j) / _lu[k * _n + k]);
            }
            for (int i = 0; i < k; i++) {
                double luik = _lu[i * _n + k];
                for (int j = 0; j < n; j++) {
                    X.set_(i, j, X.getValue(i, j) - luik * X.getValue(k, j));
                }
            }
        }
        return X;
    }

    @Override
    public MatrixFloat64 inverse() {
        // Calculates inv(U).
        final int n = _n;
        MatrixFloat64 R = MatrixFloat64.newInstance(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                R.set_(i, j, 0.0);
            }
            for (int j = i; j < n; j++) {
                R.set_(i, j, _lu[i * n + j]);
            }
        }
        for (int j = n - 1; j >= 0; j--) {
            R.set_(j, j, 1.0 / R.get_(j, j));
            for (int i = j - 1; i >= 0; i--) {
                double sum = - R.get_(i, j) * R.get_(j, j);
                for (int k = j - 1; k > i; k--) {
                    sum -= R.get_(i, k) * R.get_(k, j);
                }
                R.set_(i, j, sum / R.get_(i, i));
            }
        }
        // Solves inv(A) * L = inv(U)
        for (int i = 0; i < n; i++) {
            for (int j = n - 2; j >= 0; j--) {
                for (int k = j + 1; k < n; k++) {
                    double lukj = _lu[k * n + j];
                    R.set_(i, j, R.get_(i, j) -  R.get_(i, k) *lukj);
                }
            }
        }
        // Swaps columns (reverses pivots permutations).
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                _tmp[j] = R.get_(i, j);
            }
            for (int j = 0; j < n; j++) {
                R.set_(i, _pivots[j], _tmp[j]);
            }
        }
        return R;
    }

    @Override
    public Float64 determinant() {
        double product = _lu[0];
        for (int i = 1; i < _n; i++) {
            product = product * _lu[i * _n + i];
        }
        return Float64.valueOf(((_permutationCount & 1) == 0) ? product : -product);
    }

    /**
     * Constructs the LU decomposition of the specified matrix.
     * We make the choise of Lii = ONE (diagonal elements of the
     * lower triangular matrix are multiplicative identities).
     *
     * @param  source the matrix to decompose.
     * @throws MatrixException if the matrix source is not square.
     */
    void construct(MatrixFloat64 source) {
        for (int i = 0, k = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                _lu[k++] = source.get_(i, j);
            }
        }
        for (int i = 0; i < _n; i++) {
            _pivots[i] = i;
        }

        // Main loop.
        final int n = _n;
        for (int k = 0; k < _n; k++) {

                // Rearranges the rows so that the absolutely largest
                // elements of the matrix source in each column lies
                // in the diagonal.
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (_lu[i * n + k] > _lu[pivot * n + k]) {
                        pivot = i;
                    }
                }
                if (pivot != k) { // Exchanges.
                    for (int j = 0; j < n; j++) {
                        double tmp = _lu[pivot * n + j];
                        _lu[pivot * n + j] = _lu[k * n + j];
                        _lu[k * n + j] = tmp;
                    }
                    int j = _pivots[pivot];
                    _pivots[pivot] = _pivots[k];
                    _pivots[k] = j;
                    _permutationCount++;
                }

            // Computes multipliers and eliminate k-th column.
            double lukkInv = 1.0 / _lu[k * n + k];
            for (int i = k + 1; i < n; i++) {
                // Multiplicative order is important
                // for non-commutative elements.
                _lu[i * n + k] = _lu[i * n + k] * lukkInv;
                for (int j = k + 1; j < n; j++) {
                    _lu[i * n + j] = _lu[i * n + j] - _lu[i * n + k] * _lu[k * n + j];
                }
            }
        }
    }

    @Override
    public MatrixFloat64 getLower(Float64 zero, Float64 one) {
        MatrixFloat64 L = MatrixFloat64.newInstance(_n, _n);
        for (int j = 0; j < _n; j++) {
            for (int i = 0; i < j ; i++) {
                L.set_(i, j,  zero.doubleValue());
            }
            L.set_(j, j, one.doubleValue());
            for (int i = j + 1; i < _n; i++) {
                L.set_(i, j,  _lu[i * _n + j]);
            }
        }
        return L;
    }
    

    @Override
    public MatrixFloat64 getUpper(Float64 zero) {
        MatrixFloat64 U = MatrixFloat64.newInstance(_n, _n);
        for (int j = 0; j < _n; j++) {
            for (int i = 0; i <= j ; i++) {
                U.set_(i, j,  _lu[i * _n + j]);
            }
            for (int i = j + 1; i < _n; i++) {
                U.set_(i, j, zero.doubleValue());
            }
        }
        return U;
    }

    @Override
    public MatrixFloat64 getPermutation(Float64 zero, Float64 one) {
        MatrixFloat64 P = MatrixFloat64.newInstance(_n, _n);
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                P.set_(i, j, zero.doubleValue());
            }
        }
        for (int i=0; i < _n; i++) {
            P.set_(_pivots[i], i, one.doubleValue());
        }
        return P;
    }

    public MatrixFloat64 getLU() {
        MatrixFloat64 M = MatrixFloat64.newInstance(_n, _n);
        for (int j = 0; j < _n; j++) {
            for (int i = 0; i < _n; i++) {
                M.set_(i, j, _lu[i * _n + j]);
            }
        }
        return M;
    }
    
    public int[] getPivots() {
        return _pivots.clone();
    }
    
    ///////////////////////
    // Factory creation. //
    ///////////////////////
    
    
    @SuppressWarnings("unchecked")
    static DecompositionLUFloat64 newInstance(MatrixFloat64 source) {
        if (!source.isSquare())
            throw new DimensionException("Matrix is not square");
        int dimension = source.getNumberOfRows();
        DecompositionLUFloat64 lu = FACTORY.object();
        if ((lu._pivots == null) || (lu._pivots.length < dimension)) {
            lu._pivots = new int[dimension];
            lu._lu = new double[dimension * dimension];
            lu._tmp = new double[dimension * dimension];
        }
        lu._n = dimension;
        lu._permutationCount = 0;
        lu.construct(source);
        return lu;
    }

    private static final Factory<DecompositionLUFloat64> FACTORY = new Factory<DecompositionLUFloat64>() {
        protected DecompositionLUFloat64 create() {
            return new DecompositionLUFloat64();
        }
    };
    
    private DecompositionLUFloat64() {        
    }
}