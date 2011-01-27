/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

import org.jscience.physics.unit.converter.PhysicalUnitConverter;
import org.jscience.physics.unit.SI;
import java.math.BigInteger;
import org.jscience.physics.unit.converter.RationalConverter;
import org.jscience.physics.unit.BaseUnit;

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
        = new RationalConverter(BigInteger.ONE, BigInteger.valueOf(299792458));
    
    /**
     * Holds the single instance of this class.
     */
    private final static RelativisticModel INSTANCE = new RelativisticModel();

    /**
     * Selects the relativistic model as the currentPhysicalModel model.
     */
    public static void select() {
        PhysicalModel.setCurrent(INSTANCE);
    }

    /**
     * Default constructor.
     */
    protected RelativisticModel() {
    }

    @Override
    public PhysicalDimension getDimension(BaseUnit<?> unit) {
        if (unit.equals(SI.METRE)) return PhysicalDimension.TIME;
        return super.getDimension(unit);
    }

    @Override
    public PhysicalUnitConverter getDimensionalTransform(BaseUnit<?> unit) {
        if (unit.equals(SI.METRE)) return METRE_TO_TIME;
        return super.getDimensionalTransform(unit);
    }

}