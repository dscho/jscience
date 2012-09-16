/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vector;

import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.structure.Field;

/**
 * <p> This service creates matrix instances.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, September 16, 2012
 */
interface MatrixFactory {

    /**
     * Returns a new vector holding the specified <code>double</code> values.
     *
     * @param values the vector values.
     * @return the vector having the specified values.
     */
   public Matrix<Float64> valueOf(double[][] elements);
 
    /**
     * Returns a dense matrix from the specified 2-dimensional array.
     * The first dimension are the rows and the second are the columns.
     * 
     * @param elements this matrix elements.
     * @return the matrix having the specified elements.
     * @throws DimensionException if rows have different length.
     * @see    DenseMatrix 
     */
    public <F extends Field<F>> Matrix<F> valueOf(F[][] elements);

    /**
     * Returns a dense matrix holding the specified row vectors.
     *
     * @param rows the row vectors.
     * @return <code>DenseMatrix.valueOf(Arrays.asList(rows)</code>.
     * @throws DimensionException if the rows do not have the same dimension.
     */
    public <F extends Field<F>> Matrix<F> valueOf(Vector<F>... rows);
    
}
