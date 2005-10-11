/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.functions;

import java.io.Serializable;
import java.util.SortedMap;
import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents an estimator of the values at a certain point using
 *     surrounding points and values.
 * </p>
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle </a>
 * @version 1.0, January 24, 2004
 */
public abstract class Interpolator<P, O extends Operable<O>> implements Serializable {

    /**
     * Default constructor.
     */
    protected Interpolator() {
    }

    /**
     * Holds a simple linear interpolator upon
     * {@link org.jscience.mathematics.matrices.Operable Operable}
     * points and values.
     */
    public final static Interpolator LINEAR = new Interpolator() {

		// Implements Interpolator.
        public Operable interpolate(Object point, SortedMap pointValues) {
            
            // Searches exact.
            Operable y = (Operable) pointValues.get(point);
            if (y != null)
                return y;

            // Searches surrounding points/values.
            SortedMap<Operable, Operable> headMap = pointValues.headMap(point);
            Operable x1 = headMap.lastKey();
            Operable y1 = headMap.get(x1);
            SortedMap<Operable, Operable> tailMap = pointValues.tailMap(point);
            Operable x2 = tailMap.firstKey();
            Operable y2 = tailMap.get(x2);

            // Interpolates.
            final Operable x = (Operable) point;
            Operable deltaInv = x2.plus(x1.opposite()).reciprocal();
            Operable k1 = ((Operable)x2.plus(x.opposite())).times(deltaInv);
            Operable k2 = ((Operable)x.plus(x1.opposite())).times(deltaInv);
            return (((Operable)y1.times(k1))).plus(y2.times(k2));
        }

        private static final long serialVersionUID = 1L;
    };

    /**
     * Estimates the value at the specified point.
     * 
     * @param point the point for which the value is estimated.
     * @param pointValues the point-value entries.
     * @return the estimated value at the specified point.
     */
    public abstract O interpolate(P point, SortedMap<P, O> pointValues);
}