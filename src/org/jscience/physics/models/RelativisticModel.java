/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.models;

import static javax.units.Dimension.*;

import javax.units.converters.MultiplyConverter;

/**
 * This class represents the relativistic model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 13, 2006
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class RelativisticModel extends PhysicalModel {

    /**
     * Holds the single instance of this class.
     */
    private final static RelativisticModel INSTANCE = new RelativisticModel();

    /**
     * Selects the relativistic model as the current model.
     */
    public static void select() {
        INSTANCE.setDimensions();
        PhysicalModel.setCurrent(INSTANCE);
    }

    protected void setDimensions() {
        LENGTH.setEquivalentTo(TIME, new MultiplyConverter(1.0 / 299792458.0));
        
//
//        // SPEED_OF_LIGHT (METER / SECOND) = 1
//        SI.METER.setDimension(SI.SECOND, new MultiplyConverter(1 / c));
//
//        // ENERGY = m²·kg/s² = kg·c²
//        SI.KILOGRAM.setDimension(NonSI.ELECTRON_VOLT, new MultiplyConverter(c
//                * c / ePlus));
//
//        SI.SECOND.setDimension(SI.SECOND, Converter.IDENTITY);
//        SI.KELVIN.setDimension(SI.KELVIN, Converter.IDENTITY);
//        SI.AMPERE.setDimension(SI.AMPERE, Converter.IDENTITY);
//        SI.MOLE.setDimension(SI.MOLE, Converter.IDENTITY);
//        SI.CANDELA.setDimension(SI.CANDELA, Converter.IDENTITY);
    }
}