/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vectors;

import org.jscience.mathematics.structures.Field;

/**
 * <p> This class represents the default matrix implementation.</p>
 *     
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
final class MatrixDefault<F extends Field> extends Matrix<F> {

    /**
     * The array of field elements (M[i,j] = o[i*n+j], i < m, j < n).
     */
    private F[] o;

    /**
     * Holds the number of rows (m).
     */
    private int _numberOfRows; 

    /**
     * Holds the number of columns (n).
     */
    private int _numberOfColumns;
    
    @Override
    public int getNumberOfRows() {
        return _numberOfRows;
    }

    @Override
    public int getNumberOfColumns() {
        return _numberOfColumns;
    }

    @Override
    public F get(int i, int j) {
        if ((i < 0) || (i >= _numberOfRows) || (j < 0) || (j >= _numberOfColumns))
            throw new IndexOutOfBoundsException("i: " + i + ", j: " + j
                    + " (matrix[" + _numberOfRows + "," + _numberOfColumns + "])");
        return o[i * _numberOfColumns + j];
    }

    // Sets the specified element.
    final void set_(int i, int j, F f) {
        o[i * _numberOfColumns + j] = f;
    }

    // Gets the specified element.
    final F get_(int i, int j) {
        return o[i * _numberOfColumns + j];
    }
    
    ///////////////////////
    // Factory creation. //
    ///////////////////////
    
    static <F extends Field> MatrixDefault<F> newInstance(int m, int n) {
        MatrixDefault<F> M = FACTORY.object();
        M._numberOfRows = m;
        M._numberOfColumns = n;
        final int size = m * n;
        if ((M.o == null) || (M.o.length < size)) {
            M.o = (F[]) new Field[size];
        }
        return M;
    }
    
    // TODO Use different factories for large matrices.
    private static Factory<MatrixDefault> FACTORY = new Factory<MatrixDefault>() {
        protected MatrixDefault create() {
            return new MatrixDefault();
        }
    };

    private MatrixDefault() {        
    }

}