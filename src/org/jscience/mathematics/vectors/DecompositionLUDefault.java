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

/**
 * <p> This class represents the default Lower/Upper matrix decomposition.</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
final class DecompositionLUDefault<F extends Field<F>> extends
        DecompositionLU<F> {

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
    private F[] _lu;

    /**
     * Holds temporary buffer.
     */
    private F[] _tmp;

    /**
     * Holds the number of permutation performed.
     */
    private int _permutationCount;


    @Override
    public Vector<F> solve(Vector<F> B) {
        // TODO Optimized.
        Matrix<F> A = solve(B.asColumnMatrix());
        VectorDefault<F> v = VectorDefault.newInstance(A.getNumberOfRows());
        if (A.getNumberOfColumns() != 1)
            throw new InternalError();
        for (int i = A.getNumberOfRows(); --i >= 0;) {
            v.set_(i, A.get(i, 0));
        }
        return v;
    }

    @Override
    public Matrix<F> solve(Matrix<F> B) {
        if (_n != B.getNumberOfRows())
            throw new DimensionException("Input vector has "
                    + B.getNumberOfRows() + " rows instead of " + _n);

        // Copies B with pivoting.
        final int n = B.getNumberOfColumns();
        MatrixDefault<F> X = MatrixDefault.newInstance(_n, n);
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < n; j++) {
                X.set_(i, j, B.get(_pivots[i], j));
            }
        }

        // Solves L * Y = pivot(B)
        for (int k = 0; k < _n; k++) {
            for (int i = k + 1; i < _n; i++) {
                F luik = _lu[i * _n + k];
                for (int j = 0; j < n; j++) {
                    X.set_(i, j, X.get_(i, j).plus(
                            luik.times(X.get_(k, j).opposite())));
                }
            }
        }

        // Solves U * X = Y;
        for (int k = _n - 1; k >= 0; k--) {
            for (int j = 0; j < n; j++) {
                X.set_(k, j, (_lu[k * _n + k].inverse()).times(X.get_(k, j)));
            }
            for (int i = 0; i < k; i++) {
                F luik = _lu[i * _n + k];
                for (int j = 0; j < n; j++) {
                    X.set_(i, j, X.get_(i, j).plus(
                            luik.times(X.get_(k, j).opposite())));
                }
            }
        }
        return X;
    }

    @Override
    public Matrix<F> inverse() {
        // Calculates inv(U).
        final int n = _n;
        MatrixDefault<F> R = MatrixDefault.newInstance(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                R.set_(i, j, _lu[i * n + j]);
            }
        }
        for (int j = n - 1; j >= 0; j--) {
            R.set_(j, j, R.get_(j, j).inverse());
            for (int i = j - 1; i >= 0; i--) {
                F sum = R.get_(i, j).times(R.get_(j, j).opposite());
                for (int k = j - 1; k > i; k--) {
                    sum = sum.plus(R.get_(i, k).times(R.get_(k, j).opposite()));
                }
                R.set_(i, j, (R.get_(i, i).inverse()).times(sum));
            }
        }
        // Solves inv(A) * L = inv(U)
        for (int i = 0; i < n; i++) {
            for (int j = n - 2; j >= 0; j--) {
                for (int k = j + 1; k < n; k++) {
                    F lukj = _lu[k * n + j];
                    if (R.get_(i, j) != null) {
                        R.set_(i, j, R.get_(i, j).plus(
                                R.get_(i, k).times(lukj.opposite())));
                    } else {
                        R.set_(i, j, R.get_(i, k).times(lukj.opposite()));
                    }
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
    public F determinant() {
        F product = _lu[0];
        for (int i = 1; i < _n; i++) {
            product = product.times(_lu[i * _n + i]);
        }
        return ((_permutationCount & 1) == 0) ? product : product.opposite();
    }

    /**
     * Constructs the LU decomposition of the specified matrix.
     * We make the choise of Lii = ONE (diagonal elements of the
     * lower triangular matrix are multiplicative identities).
     *
     * @param  source the matrix to decompose.
     * @throws MatrixException if the matrix source is not square.
     */
    void construct(Matrix<F> source) {
        for (int i = 0, k = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                _lu[k++] = source.get(i, j);
            }
        }
        for (int i = 0; i < _n; i++) {
            _pivots[i] = i;
        }

        // Main loop.
        Comparator<Field> cmp = DecompositionLU.getPivotComparator();
        final int n = _n;
        for (int k = 0; k < _n; k++) {

            if (cmp != null) { // Pivoting enabled.
                // Rearranges the rows so that the absolutely largest
                // elements of the matrix source in each column lies
                // in the diagonal.
                int pivot = k;
                for (int i = k + 1; i < n; i++) {
                    if (cmp.compare(_lu[i * n + k], _lu[pivot * n + k]) > 0) {
                        pivot = i;
                    }
                }
                if (pivot != k) { // Exchanges.
                    for (int j = 0; j < n; j++) {
                        F tmp = _lu[pivot * n + j];
                        _lu[pivot * n + j] = _lu[k * n + j];
                        _lu[k * n + j] = tmp;
                    }
                    int j = _pivots[pivot];
                    _pivots[pivot] = _pivots[k];
                    _pivots[k] = j;
                    _permutationCount++;
                }
            } 

            // Computes multipliers and eliminate k-th column.
            F lukkInv = _lu[k * n + k].inverse();
            for (int i = k + 1; i < n; i++) {
                // Multiplicative order is important
                // for non-commutative elements.
                _lu[i * n + k] = _lu[i * n + k].times(lukkInv);
                for (int j = k + 1; j < n; j++) {
                    _lu[i * n + j] = _lu[i * n + j].plus(_lu[i * n + k]
                            .times(_lu[k * n + j].opposite()));
                }
            }
        }
    }

    @Override
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            for (int i = _n * _n; i > 0;) {
                _lu[--i].move(os);
            }
            return true;
        }
        return false;
    }

    @Override
    public MatrixDefault<F> getLower(F zero, F one) {
        MatrixDefault<F> L = MatrixDefault.newInstance(_n, _n);
        for (int j = 0; j < _n; j++) {
            for (int i = 0; i < j ; i++) {
                L.set_(i, j,  zero);
            }
            L.set_(j, j, one);
            for (int i = j + 1; i < _n; i++) {
                L.set_(i, j,  _lu[i * _n + j]);
            }
        }
        return L;
    }
    

    @Override
    public Matrix<F> getUpper(F zero) {
        MatrixDefault<F> U = MatrixDefault.newInstance(_n, _n);
        for (int j = 0; j < _n; j++) {
            for (int i = 0; i <= j ; i++) {
                U.set_(i, j,  _lu[i * _n + j]);
            }
            for (int i = j + 1; i < _n; i++) {
                U.set_(i, j, zero);
            }
        }
        return U;
    }

    @Override
    public Matrix<F> getPermutation(F zero, F one) {
        MatrixDefault<F> P = MatrixDefault.newInstance(_n, _n);
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                P.set_(i, j, zero);
            }
        }
        for (int i=0; i < _n; i++) {
            P.set_(_pivots[i], i, one);
        }
        return P;
    }

    public Matrix<F> getLU() {
        MatrixDefault<F> M = MatrixDefault.newInstance(_n, _n);
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
    static <F extends Field<F>> DecompositionLU<F> newInstance(Matrix<F> source) {
        if (!source.isSquare())
            throw new DimensionException("Matrix is not square");
        int dimension = source.getNumberOfRows();
        DecompositionLUDefault lu = FACTORY.object();
        if ((lu._pivots == null) || (lu._pivots.length < dimension)) {
            lu._pivots = new int[dimension];
            lu._lu = (F[]) new Field[dimension * dimension];
            lu._tmp = (F[]) new Field[dimension * dimension];
        }
        lu._n = dimension;
        lu._permutationCount = 0;
        lu.construct(source);
        return lu;
    }

    private static final Factory<DecompositionLUDefault> FACTORY = new Factory<DecompositionLUDefault>() {
        protected DecompositionLUDefault create() {
            return new DecompositionLUDefault();
        }
    };
    
    private DecompositionLUDefault() {        
    }

}