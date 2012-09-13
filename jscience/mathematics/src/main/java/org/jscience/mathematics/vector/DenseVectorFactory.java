/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.vector;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import javolution.lang.Realtime;
import javolution.lang.ValueType;
import javolution.text.Cursor;
import javolution.text.Text;
import javolution.text.TextFormat;

import javolution.util.Index;
import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.structure.Field;
import org.jscience.mathematics.structure.VectorSpace;

/**
 * <p> This service provides vector instances.</p>
 * TBD 
 * See http://njbartlett.name/2010/08/10/generic-osgi.html
 * 
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, December 12, 2009
 * @see <a href="http://en.wikipedia.org/wiki/Vector_space">
 *      Wikipedia: Vector Space</a>
 */
interface DenseVectorFactory<F extends Field<F>>  {


    /**
     * Returns a dense vector holding the elements from the specified
     * collection. 
     *
     * @param elements the collection of vector elements.
     * @return the dense vector having the specified elements.
     */
    public Vector<F> valueOf(List<F> elements);

    /**
     * Returns a dense vector holding the specified elements.
     *
     * @param elements the vector elements.
     * @return the dense vector having the specified elements.
     */
    public Vector<F> valueOf(F... elements);

}
