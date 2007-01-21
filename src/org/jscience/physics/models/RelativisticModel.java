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
 * This class represents the relativistic model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 22, 2006
 */
public class RelativisticModel extends PhysicalModel {

    
    /**
     * Holds the meter to time transform.
     */
    private static RationalConverter METER_TO_TIME 
        = new RationalConverter(1, 299792458);
    
    /**
     * Holds the single instance of this class.
     */
    private final static RelativisticModel INSTANCE = new RelativisticModel();

    /**
     * Selects the relativistic model as the current model.
     */
    public static void select() {
        PhysicalModel.setCurrent(INSTANCE);
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
}