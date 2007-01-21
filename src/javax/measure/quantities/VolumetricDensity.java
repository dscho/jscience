/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure.quantities;

import javax.measure.units.ProductUnit;
import javax.measure.units.SI;
import javax.measure.units.Unit;

/**
 * This interface represents a mass per unit volume of a substance under
 * specified conditions of pressure and temperature. The system unit for
 * this quantity is "kg/m³" (kilogram per cubic meter).
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, January 14, 2006
 */
public interface VolumetricDensity extends Quantity<VolumetricDensity> {

    /**
     * Holds the SI unit (Système International d'Unités) for this quantity.
     */
    public final static Unit<VolumetricDensity> UNIT = new ProductUnit<VolumetricDensity>(
            SI.KILOGRAM.divide(SI.METER.pow(3)));
}