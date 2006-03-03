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
 * This interface represents a list of similar quantities all stated in the 
 * the same unit.
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.2, February 6, 2006
 */
public interface QuantityArray<U extends Unit> {

    /**
     * Returns the unit these quantities are stated in.
     * 
     * @return the common unit for all this array values.
     */
    U getUnit();

    /**
     * Returns the value of the quantity at the specified position 
     * as a <code>double</code> stated in this array {@link #getUnit unit}.
     * 
     * @param i the index of the quantity.
     * @return the numeric value after conversion to type <code>double</code>.
     */
    double doubleValue(int i);

    /**
     * Returns the value of the quantity at the specified position 
     * as a <code>float</code> stated in this array {@link #getUnit unit}.
     * 
     * @param i the index of the quantity.
     * @return the numeric value after conversion to type <code>float</code>.
     */
    float floatValue(int i);

    /**
     * Returns the value of the quantity at the specified position 
     * as a <code>int</code> stated in this array {@link #getUnit unit}.
     * 
     * @param i the index of the quantity.
     * @return the numeric value after conversion to type <code>int</code>.
     */
    int intValue(int i);
    
    /**
     * Returns the value of the quantity at the specified position 
     * as a <code>long</code> stated in this array {@link #getUnit unit}.
     * 
     * @param i the index of the quantity.
     * @return the numeric value after conversion to type <code>int</code>.
     */
    long longValue(int i);    
}