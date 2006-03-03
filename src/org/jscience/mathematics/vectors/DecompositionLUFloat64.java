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
    
//    double[] scales = new double[n];
//    double[][] lu = new double[n][n];
//    int[] ps = new int[n];
//
//    // Start timer.
//    long time = nanoTime();
//
//    // Calculates.
//    int pivotindex = 0;
//    double pivot, biggest, mult, tempf;
//    for (int i = 0; i < n; i++) { /* For each row */
//        biggest = 0.0;
//        for (int j = 0; j < n; j++)
//            if (biggest < (tempf = MathLib.abs(lu[i][j] = a[i][j])))
//                biggest = tempf;
//        if (biggest != 0.0)
//            scales[i] = 1.0 / biggest;
//        else {
//            scales[i] = 0.0;
//            throw new Error("Zero row: singular matrix");
//        }
//        ps[i] = i;
//    }
//    for (int k = 0; k < n - 1; k++) {
//        biggest = 0.0;
//        for (int i = k; i < n; i++) {
//            if (biggest < (tempf = MathLib.abs(lu[ps[i]][k])
//                    * scales[ps[i]])) {
//                biggest = tempf;
//                pivotindex = i;
//            }
//        }
//        if (biggest == 0.0)
//            throw new Error("Zero column: singular matrix");
//        if (pivotindex != k) {
//            int j = ps[k];
//            ps[k] = ps[pivotindex];
//            ps[pivotindex] = j;
//        }
//        pivot = lu[ps[k]][k];
//        for (int i = k + 1; i < n; i++) {
//            lu[ps[i]][k] = mult = lu[ps[i]][k] / pivot;
//            if (mult != 0.0) {
//                for (int j = k + 1; j < n; j++)
//                    lu[ps[i]][j] -= mult * lu[ps[k]][j];
//            }
//        }
//    }
//    if (lu[ps[n - 1]][n - 1] == 0.0) {
//        throw new Error("Singular matrix !!!\n");
//    }

    
    @Override
    public Vector<Float64> solve(Vector<Float64> B) {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public Matrix<Float64> solve(Matrix<Float64> B) {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public Matrix<Float64> inverse() {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public Float64 determinant() {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public Matrix<Float64> getLower(Float64 zero, Float64 one) {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public Matrix<Float64> getUpper(Float64 zero) {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public Matrix<Float64> getPermutation(Float64 zero, Float64 one) {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public Matrix<Float64> getLU() {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }

    @Override
    public int[] getPivots() {
        throw new UnsupportedOperationException("Not yet converted to v3.0");
    }


}