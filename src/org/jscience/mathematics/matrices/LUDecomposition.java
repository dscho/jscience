/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.matrices;

import org.jscience.mathematics.numbers.Numeric;
import java.util.Comparator;

import javolution.realtime.LocalContext;
import javolution.realtime.LocalReference;
import javolution.realtime.PoolContext;
import javolution.realtime.RealtimeObject;

/**
 * <p> This class represents the decomposition of a {@link Matrix matrix} into
 *     a product of lower and upper triangular matrices
 *     (L and U, respectively).</p>
 *     
 * <p> This decomposition</a> is typically used to resolve linear systems
 *     of equations (Gaussian elimination) or to calculate the determinant
 *     of a square {@link Matrix} (<code>O(m³)</code>).</p>
 *     
 * <p> Numerical stability is guaranteed through pivoting if the
 *     {@link Operable} elements of this matrix are 
 *     {@link org.jscience.mathematics.numbers.Numeric Numeric}
 *     For others elements types, numerical stability can be ensured by setting
 *     the {@link LocalContext context-local} pivot comparator (see 
 *     {@link #setPivotComparator}).</p>
 *     
 * <p> Instances of this class are created using the {@link Matrix#lu()}
 *     factory method.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2004
 * @see <a href="http://mathworld.wolfram.com/LUDecomposition.html">
 *      LU Decomposition -- from MathWorld</a> 
 */
public final class LUDecomposition<O extends Operable<O>> extends
        RealtimeObject {

    /**
     * Holds the comparator for pivoting 
     * {@link org.jscience.mathematics.numbers.Numeric Numeric}
     * instances.
     */
    public static final Comparator<Operable> NUMERIC_COMPARATOR = new Comparator<Operable>() {

        public int compare(Operable left, Operable right) {
            if ((left instanceof Numeric) && (right instanceof Numeric))
                return ((Numeric) left).isLargerThan((Numeric) right) ? 1 : -1;
            if (left.equals(left.plus(left))) // Zero
                return -1;
            if (right.equals(right.plus(right))) // Zero
                return 1;
            return 0;
        }
    };

    /**
     * Holds the local comparator.
     */
    private static final LocalReference<Comparator<Operable>> PIVOT_COMPARATOR
        = new LocalReference<Comparator<Operable>>(
            NUMERIC_COMPARATOR);

    /**
     * Holds the dimension of the square matrix source.
     */
    private int _n;

    /**
     * Holds the LU elements.
     */
    private O[] _lu;

    /**
     * Holds temporary buffer.
     */
    private O[] _tmp;

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
     * @param dimension the dimension of the square matrix source.
     */
    private LUDecomposition(int dimension) {
        _lu = (O[]) new Operable[dimension * dimension];
        _tmp = (O[]) new Operable[dimension * dimension];
        _pivots = new int[dimension];
    }

    /**
     * Returns the LU decomposition of the specified matrix..
     *
     * @param  source the matrix for which the LU decomposition is 
     *         returned.
     * @return the corresponding LU decomposition.
     * @throws MatrixException if this matrix is not square.
     */
    static <O extends Operable<O>> LUDecomposition<O> valueOf(Matrix<O> source) {
        if (!source.isSquare())
            throw new MatrixException("Matrix is not square");
        int dimension = source.m;
        LUDecomposition lu;
        if (dimension <= 1 << 2) {
            lu = FACTORY_2.object();
        } else if (dimension <= 1 << 3) {
            lu = FACTORY_3.object();
        } else if (dimension <= 1 << 4) {
            lu = FACTORY_4.object();
        } else if (dimension <= 1 << 5) {
            lu = FACTORY_5.object();
        } else if (dimension <= 1 << 6) {
            lu = FACTORY_6.object();
        } else if (dimension <= 1 << 7) {
            lu = FACTORY_7.object();
        } else if (dimension <= 1 << 8) {
            lu = FACTORY_8.object();
        } else if (dimension <= 1 << 9) {
            lu = FACTORY_9.object();
        } else if (dimension <= 1 << 10) {
            lu = FACTORY_10.object();
        } else if (dimension <= 1 << 11) {
            lu = FACTORY_11.object();
        } else if (dimension <= 1 << 12) {
            lu = FACTORY_12.object();
        } else if (dimension <= 1 << 13) {
            lu = FACTORY_13.object();
        } else if (dimension <= 1 << 14) {
            lu = FACTORY_14.object();
        } else if (dimension <= 1 << 15) {
            lu = FACTORY_15.object();
        } else {
            throw new UnsupportedOperationException("Dimension : " + dimension
                    + " too large");
        }
        lu._n = dimension;
        lu._permutationCount = 0;
        lu.construct(source);
        return lu;

    }

    /**
     * Sets the {@link LocalContext local} comparator used 
     * for pivoting during LU decomposition (default 
     * {@link #NUMERIC_COMPARATOR}).
     *
     * @param  cmp the comparator for pivoting.
     */
    public void setPivotComparator(Comparator<Operable> cmp) {
        PIVOT_COMPARATOR.set(cmp);
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
    public Matrix<O> solve(Matrix<O> B) {
        if (_n != B.m)
            throw new MatrixException("Input vector has " + B.m
                    + " rows instead of " + _n);

        // Copies B with pivoting.
        final int n = B.n;
        Matrix<O> X = Matrix.newInstance(_n, n);
        final O[] x = X.o;
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < n; j++) {
                x[i * n + j] = B.o[_pivots[i] * n + j];
            }
        }

        // Solves L * Y = pivot(B)
        for (int k = 0; k < _n; k++) {
            for (int i = k + 1; i < _n; i++) {
                O luik = _lu[i * _n + k];
                for (int j = 0; j < n; j++) {
                    x[i * n + j] = x[i * n + j].plus(luik.times(x[k * n + j])
                            .opposite());
                }
            }
        }

        // Solves U * X = Y;
        for (int k = _n - 1; k >= 0; k--) {
            for (int j = 0; j < n; j++) {
                x[k * n + j] = (_lu[k * _n + k].reciprocal())
                        .times(x[k * n + j]);
            }
            for (int i = 0; i < k; i++) {
                O luik = _lu[i * _n + k];
                for (int j = 0; j < n; j++) {
                    x[i * n + j] = x[i * n + j].plus(luik.times(x[k * n + j])
                            .opposite());
                }
            }
        }
        return X;
    }

    /**
     * Returns the solution X of the equation: A * X = Identity  with
     * <code>this = A.lu()</code> using back and forward substitutions.
     * This method uses {@link PoolContext} internally.
     *
     * @return <code>this.solve(Identity)</code>
     */
    public Matrix<O> inverse() {
        // Calculates inv(U).
        final int n = _n;
        Matrix<O> R = Matrix.newInstance(n, n);
        final O[] r = R.o;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                r[i * n + j] = _lu[i * n + j];
            }
        }
        for (int j = n - 1; j >= 0; j--) {
            r[j * n + j] = r[j * n + j].reciprocal();
            for (int i = j - 1; i >= 0; i--) {
                //PoolContext.enter();
                //try {
                    O sum = r[i * n + j].times(r[j * n + j]).opposite();
                    for (int k = j - 1; k > i; k--) {
                        sum = sum.plus(r[i * n + k].times(r[k * n + j])
                                .opposite());
                    }
                    r[i * n + j] = (r[i * n + i].reciprocal())
                            .times(sum);
                    r[i * n + j].move(ObjectSpace.OUTER);
                //} finally {
                //    PoolContext.exit();
                //}
            }
        }
        // Solves inv(A) * L = inv(U)
        for (int i = 0; i < n; i++) {
            for (int j = n - 2; j >= 0; j--) {
                //PoolContext.enter();
                //try {
                    for (int k = j + 1; k < n; k++) {
                        O lukj = _lu[k * n + j];
                        if (r[i * n + j] != null) {
                            r[i * n + j] = r[i * n + j].plus(r[i * n + k]
                                    .times(lukj).opposite());
                        } else {
                            r[i * n + j] = r[i * n + k].times(lukj).opposite();
                        }
                    }
                    r[i * n + j].move(ObjectSpace.OUTER);
                //} finally {
                //    PoolContext.exit();
                //}
            }
        }
        // Swaps columns (reverses pivots permutations).
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                _tmp[j] = r[i * n + j];
            }
            for (int j = 0; j < n; j++) {
                r[i * n + _pivots[j]] = _tmp[j];
            }
        }
        return R;
    }

    /**
     * Returns the determinant of the {@link Matrix} having this
     * {@link LUDecomposition}.
     *
     * @return the determinant of the matrix source.
     */
    public O determinant() {
        O product = _lu[0];
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
    private void construct(Matrix<O> source) {
        System.arraycopy(source.o, 0, _lu, 0, _n * _n);
        for (int i = 0; i < _n; i++) {
            _pivots[i] = i;
        }

        // Main loop.
        Comparator<Operable> cmp = PIVOT_COMPARATOR.get();
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
                    O tmp = _lu[pivot * n + j];
                    _lu[pivot * n + j] = _lu[k * n + j];
                    _lu[k * n + j] = tmp;
                }
                int j = _pivots[pivot];
                _pivots[pivot] = _pivots[k];
                _pivots[k] = j;
                _permutationCount++;
            }

            // Computes multipliers and eliminate k-th column.
            O lukkInv = _lu[k * n + k].reciprocal();
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
    public boolean move(ObjectSpace os) {
        if (super.move(os)) {
            for (int i = _n * _n; i > 0;) {
                _lu[--i].move(os);
            }
            return true;
        }
        return false;
    }

    // TBD: Use recursive structures instead of large arrays.
    //

    private static final Factory<LUDecomposition> FACTORY_2 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 2);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_3 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 3);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_4 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 4);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_5 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 5);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_6 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 6);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_7 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 7);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_8 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 8);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_9 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 9);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_10 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 10);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_11 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 11);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_12 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 12);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_13 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 13);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_14 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 14);
        }
    };

    private static final Factory<LUDecomposition> FACTORY_15 = new Factory<LUDecomposition>() {
        protected LUDecomposition create() {
            return new LUDecomposition(1 << 15);
        }
    };

}