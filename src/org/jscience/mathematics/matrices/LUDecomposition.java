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


import java.util.Comparator;

import javolution.realtime.ArrayPool;
import javolution.realtime.LocalContext;
import javolution.realtime.ObjectPool;
import javolution.realtime.PoolContext;
import javolution.realtime.RealtimeObject;


/**
 * <p> This class represents the decomposition of a {@link Matrix} into
 *     a product of lower and upper triangular matrices
 *     (L and U, respectively).</p>
 * <p> This decomposition</a> is typically used to resolve linear systems
 *     of equations (Gaussian elimination) or to calculate the determinant
 *     of a square {@link Matrix} (<code>O(n³)</code>).</p>
 * <p> Numerical stability is guaranteed through pivoting if the
 *     {@link Operable} elements of this matrix are derived
 *     from <code>java.lang.Number</code>. For others elements types,
 *     numerical stability can be ensured by setting the appropriate 
 *     pivot comparator (see {@link #setPivotComparator}).</p>
 * <p> Instances of this class are created using the {@link Matrix#lu()}
 *     factory methods.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 * @see <a href="http://mathworld.wolfram.com/LUDecomposition.html">
 *      LU Decomposition -- from MathWorld</a> 
 */
public final class LUDecomposition extends RealtimeObject {

    /**
     * Holds the comparator for pivoting <code>java.lang.Number</code>
     * instances.
     */
    public static final Comparator NUMBER_COMPARATOR = new Comparator() {

        public int compare(Object left, Object right) {
            if ((left instanceof Number) && (right instanceof Number)) {
                return Math.abs(((Number) left).doubleValue()) > Math
                        .abs(((Number) right).doubleValue()) ? 1 : -1;
            } else {
                return 0;
            }
        }
    };

    /**
     * Holds the local comparator.
     */
    private static final LocalContext.Variable PIVOT_COMPARATOR = new LocalContext.Variable(
            NUMBER_COMPARATOR);

    /**
     * Holds the LU decomposition factories.
     */
    private static final LUDecompositionFactory[] FACTORIES;
    static {
        FACTORIES = new LUDecompositionFactory[28];
        for (int i = 0; i < FACTORIES.length; i++) {
            FACTORIES[i] = new LUDecompositionFactory(ArrayPool.MIN_LENGTH << i);
        }
    }

    /**
     * Holds the dimension of the square matrix source.
     */
    private int _n;

    /**
     * Holds the LU elements.
     */
    private Operable[] _lu;

    /**
     * Holds the pivots indexes.
     */
    private final int[] _pivots;

    /**
     * Holds the number of permutation performed.
     */
    private int _permutationCount;

    /**
     * Base constructor.
     * 
     * @param lu the lu elements array.
     */
    private LUDecomposition(Operable[] lu) {
        _lu = lu;
        _pivots = new int[(int) Math.round(Math.sqrt(lu.length))];
    }

    /**
     * Returns the LU decomposition of the specified matrix..
     *
     * @param  source the matrix for which the LU decomposition is 
     *         returned.
     * @return the corresponding LU decomposition.
     * @throws MatrixException if this matrix is not square.
     */
    static LUDecomposition valueOf(Matrix source) {
        if (source.isSquare()) {
            int size = source.n;
            LUDecomposition lu = (LUDecomposition) FACTORIES[ArrayPool
                    .indexFor(size * size)].object();
            lu._n = size;
            lu._permutationCount = 0;
            lu.construct(source);
            return lu;
        } else {
            throw new MatrixException("Matrix is not square");
        }
    }

    /**
     * Sets the {@link LocalContext local} comparator used 
     * for pivoting during LU decomposition (default 
     * {@link #NUMBER_COMPARATOR}).
     *
     * @param  cmp the comparator for pivoting.
     */
    public void setPivotComparator(Comparator cmp) {
        PIVOT_COMPARATOR.setValue(cmp);
    }

    /**
     * Returns the solution X of the equation: A * X = B  with
     * <code>this = A.lu()</code> using back and forward substitutions.
     *
     * @param  B the input vector.
     * @return the solution X = (1 / A) * B.
     * @throws MatrixException if
     *         <code>A.getRowDimension() != B.getRowDimension()</code>.
     */
    public Matrix solve(Matrix B) {
        if (_n == B.n) {

            // Copies B with pivoting.
            final int n = B.m;
            Matrix X = Matrix.newInstance(_n, n);
            final Operable[] x = X.o;
            for (int i = 0; i < _n; i++) {
                for (int j = 0; j < n; j++) {
                    x[i * n + j] = B.o[_pivots[i] * n + j];
                }
            }

            // Solves L * Y = pivot(B)
            for (int k = 0; k < _n; k++) {
                for (int i = k + 1; i < _n; i++) {
                    Operable luik = _lu[i * _n + k];
                    for (int j = 0; j < n; j++) {
                        x[i * n + j] = x[i * n + j].plus(luik.times(
                                x[k * n + j]).opposite());
                    }
                }
            }

            // Solves U * X = Y;
            for (int k = _n - 1; k >= 0; k--) {
                for (int j = 0; j < n; j++) {
                    x[k * n + j] = _lu[k * _n + k].reciprocal().times(
                            x[k * n + j]);
                }
                for (int i = 0; i < k; i++) {
                    Operable luik = _lu[i * _n + k];
                    for (int j = 0; j < n; j++) {
                        x[i * n + j] = x[i * n + j].plus(luik.times(
                                x[k * n + j]).opposite());
                    }
                }
            }

            return X;

        } else {
            throw new MatrixException("Input vector has " + B.n
                    + " rows instead of " + _n);
        }
    }

    /**
     * Returns the solution X of the equation: A * X = Identity  with
     * <code>this = A.lu()</code> using back and forward substitutions.
     * This method uses {@link PoolContext} internally.
     *
     * @return <code>this.solve(Identity)</code>
     */
    public Matrix inverse() {
        // Calculates inv(U).
        final int n = _n;
        Matrix R = Matrix.newInstance(n, n);
        final Operable[] r = R.o;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                r[i * n + j] = _lu[i * n + j];
            }
        }
        for (int j = n - 1; j >= 0; j--) {
            r[j * n + j] = r[j * n + j].reciprocal();
            for (int i = j - 1; i >= 0; i--) {
                PoolContext.enter();
                try {
                    Operable sum = r[i * n + j].times(r[j * n + j]).opposite();
                    for (int k = j - 1; k > i; k--) {
                        sum = sum.plus(r[i * n + k].times(r[k * n + j])
                                .opposite());
                    }
                    r[i * n + j] = r[i * n + i].reciprocal().times(sum);
                    r[i * n + j].move(ContextSpace.OUTER);
                } finally {
                    PoolContext.exit();
                }
            }
        }
        // Solves inv(A) * L = inv(U)
        for (int i = 0; i < n; i++) {
            for (int j = n - 2; j >= 0; j--) {
                PoolContext.enter();
                try {
                    for (int k = j + 1; k < n; k++) {
                        Operable lukj = _lu[k * n + j];
                        if (r[i * n + j] != null) {
                            r[i * n + j] = r[i * n + j].plus(r[i * n + k]
                                    .times(lukj).opposite());
                        } else {
                            r[i * n + j] = r[i * n + k].times(lukj).opposite();
                        }
                    }
                    r[i * n + j].move(ContextSpace.OUTER);
                } finally {
                    PoolContext.exit();
                }
            }
        }
        // Swaps columns (reverses pivots permutations).
        ObjectPool pool = ArrayPool.objectArray(n);
        Object[] tmp = (Object[]) pool.next();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tmp[j] = r[i * n + j];
            }
            for (int j = 0; j < n; j++) {
                r[i * n + _pivots[j]] = (Operable) tmp[j];
            }
        }
        pool.recycle(tmp);
        return R;
    }

    /**
     * Returns the determinant of the {@link Matrix} having this
     * {@link LUDecomposition}.
     *
     * @return the determinant of the matrix source.
     */
    public Operable determinant() {
        Operable product = _lu[0];
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
    private void construct(Matrix source) {
        System.arraycopy(source.o, 0, _lu, 0, _n * _n);
        for (int i = 0; i < _n; i++) {
            _pivots[i] = i;
        }

        // Main loop.
        Comparator cmp = (Comparator) PIVOT_COMPARATOR.getValue();
        final int n = _n;
        for (int k = 0; k < _n; k++) {

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
                    Operable tmp = _lu[pivot * n + j];
                    _lu[pivot * n + j] = _lu[k * n + j];
                    _lu[k * n + j] = tmp;
                }
                int j = _pivots[pivot];
                _pivots[pivot] = _pivots[k];
                _pivots[k] = j;
                _permutationCount++;
            }

            // Computes multipliers and eliminate k-th column.
            Operable lukkInv = _lu[k * n + k].reciprocal();
            for (int i = k + 1; i < n; i++) {
                // Multiplicative order is important
                // for non-commutative elements.
                _lu[i * n + k] = _lu[i * n + k].times(lukkInv);
                for (int j = k + 1; j < n; j++) {
                    _lu[i * n + j] = _lu[i * n + j].plus(_lu[i * n + k].times(
                            _lu[k * n + j]).opposite());
                }
            }
        }
    }

    // Overrides.
    public void move(ContextSpace cs) {
        for (int i = _n * _n; i > 0;) {
            _lu[--i].move(cs);
        }
    }

    /**
     * This inner class represents a factory producing {@link LUDecomposition}
     * instances.
     */
    private final static class LUDecompositionFactory extends Factory {

        private final int _luLength;

        private LUDecompositionFactory(int luLength) {
            _luLength = luLength;
        }

        public Object create() {
            return new LUDecomposition(new Operable[_luLength]);
        }

        public void cleanup(Object obj) {
            LUDecomposition lu = (LUDecomposition) obj;
            ArrayPool.clear(lu._lu, 0, lu._n);
        }
    }
}