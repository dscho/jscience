/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.models;

import javax.measure.converters.RationalConverter;
import javax.measure.converters.UnitConverter;
import javax.measure.units.BaseUnit;
import javax.measure.units.Dimension;
import javax.measure.units.SI;

/**
 * This class represents the high-energy model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 */
public class HighEnergyModel extends PhysicalModel {

    /**
     * Holds the single instance of this class.
     */
    final static HighEnergyModel INSTANCE = new HighEnergyModel();

    /**
     * Holds the meter to time transform.
     */
    private static RationalConverter METER_TO_TIME 
        = new RationalConverter(1, 299792458);
    
    /**
     * Selects the relativistic model as the current model.
     */
    public static void select() {
        throw new UnsupportedOperationException("Not implemented");
    }

    // Implements Dimension.Model
    public Dimension getDimension(BaseUnit unit) {
        if (unit.equals(SI.METER)) return Dimension.TIME;
        return Dimension.Model.STANDARD.getDimension(unit);
    }

    // Implements Dimension.Model
    public UnitConverter getTransform(BaseUnit unit) {
        if (unit.equals(SI.METER)) return METER_TO_TIME;
        return Dimension.Model.STANDARD.getTransform(unit);
    }
    
//        // SPEED_OF_LIGHT (METER / SECOND) = 1
//        SI.SECOND.setDimension(SI.NANO(SI.SECOND), new MultiplyConverter(1E9));
//        SI.METER.setDimension(SI.NANO(SI.SECOND),
//                new MultiplyConverter(1E9 / c));
//
//        // ENERGY = m²·kg/s² = kg·c²
//        SI.KILOGRAM.setDimension(SI.GIGA(NonSI.ELECTRON_VOLT),
//                new MultiplyConverter(c * c / ePlus / 1E9));
//
//        // BOLTZMANN (JOULE / KELVIN = (KILOGRAM / C^2 ) / KELVIN) = 1
//        SI.KELVIN.setDimension(SI.GIGA(NonSI.ELECTRON_VOLT),
//                new MultiplyConverter(k / ePlus / 1E9));
//
//        // ELEMENTARY_CHARGE (SECOND * AMPERE) = 1
//        SI.AMPERE.setDimension(Unit.ONE.divide(SI.NANO(SI.SECOND)),
//                new MultiplyConverter(1E-9 / ePlus));
//
//        SI.MOLE.setDimension(SI.MOLE, Converter.IDENTITY);
//        SI.CANDELA.setDimension(SI.CANDELA, Converter.IDENTITY);
}