/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

import org.jscience.physics.unit.SI;
import org.jscience.physics.unit.converter.RationalConverter;
import org.jscience.physics.unit.BaseUnit;
import org.unitsofmeasurement.unit.Dimension;
import org.unitsofmeasurement.unit.UnitConverter;

/**
 * This class represents the relativistic model.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class RelativisticModel extends StandardModel {
    
    /**
     * Holds the meter to time transform.
     */
    private static RationalConverter METRE_TO_TIME 
        = new RationalConverter(1, 299792458);
    
    /**
     * Holds the single instance of this class.
     */
    private final static RelativisticModel INSTANCE = new RelativisticModel();

    /**
     * Returns the relativistic model instance.
     */
    public static RelativisticModel getInstance() {
        return INSTANCE;
    }

    /**
     * Selects the relativistic model as the getCurrent model.
     */
    public static void select() {
        PhysicsModel.setCurrent(INSTANCE);
    }

    /**
     * Default constructor.
     */
    protected RelativisticModel() {
    }

    @Override
    public PhysicsDimension getDimension(BaseUnit<?> unit) {
        if (unit.equals(SI.METRE)) return PhysicsDimension.TIME;
        return super.getDimension(unit);
    }

    @Override
    public UnitConverter getDimensionalTransform(BaseUnit<?> unit) {
        if (unit.equals(SI.METRE)) return METRE_TO_TIME;
        return super.getDimensionalTransform(unit);
    }

}