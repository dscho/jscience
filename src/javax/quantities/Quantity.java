/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.quantities;

import javax.units.Unit;

/**
 * This interface represents the measurable, countable, or comparable property 
 * or aspect of a thing.
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, January 14, 2006
 */
public interface Quantity<Q extends Quantity>  {
    
    /**
     * Returns the estimated value of this quantity stated in the specified 
     * unit as a <code>double</code>.
     * 
     * @param unit the unit in which the measurement value is stated.
     * @return the numeric value after conversion to type <code>double</code>.
     */
    double doubleValue(Unit<Q> unit);

    /**
     * Returns the estimated value of this quantity stated in the specified 
     * unit as a <code>long</code>.
     * 
     * @param unit the unit in which the measurement value is stated.
     * @return the numeric value after conversion to type <code>long</code>.
     * @throws ArithmeticException if this quantity cannot be represented 
     *         as a <code>long</code> number in the specified unit.
     */
    long longValue(Unit<Q> unit) throws ArithmeticException;
    
}
