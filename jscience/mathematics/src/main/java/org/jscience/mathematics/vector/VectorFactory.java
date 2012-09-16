/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vector;


import java.util.List;
import java.util.Map;
import javolution.util.Index;

import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.structure.Field;

/**
 * <p> This service creates vector instances.</p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, September 16, 2012
 */
interface VectorFactory {

    /**
     * Returns a new vector holding the specified <code>double</code> values.
     *
     * @param values the vector values.
     * @return the vector having the specified values.
     */
    public Vector<Float64> valueOf(double... values);

    /**
     * Returns a dense vector holding the elements from the specified
     * collection. 
     *
     * @param elements the collection of vector elements.
     * @return the dense vector having the specified elements.
     */
    public <F extends Field<F>> Vector<F> valueOf(List<F> elements);

    /**
     * Returns a dense vector holding the specified elements.
     *
     * @param elements the vector elements.
     * @return the dense vector having the specified elements.
     */
    public <F extends Field<F>> Vector<F> valueOf(F... elements);

  /**
     * Returns a sparse vector from the specified arguments.
     *
     * @param elements the index to element mapping.
     * @param zero the element representing zero.
     * @param dimension this vector dimension.
     * @return the corresponding vector.
     */
    public <F extends Field<F>> Vector<F> valueOf(Map<Index, F> elements,
            F zero, int dimension);
    
}
