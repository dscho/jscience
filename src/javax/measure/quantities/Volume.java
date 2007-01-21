/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure.quantities;
import javax.measure.units.SI;
import javax.measure.units.Unit;

/**
 * This interface represents the amount of space occupied by a three-dimensional
 * object or region of space, expressed in cubic units. The system unit for
 * this quantity is "m³" (cubic meter).
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, January 14, 2006
 */
public interface Volume extends Quantity<Volume> {

    /**
     * Holds the SI unit (Système International d'Unités) for this quantity.
     */
    public final static Unit<Volume> UNIT = SI.CUBIC_METER;

}