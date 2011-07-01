/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

import javolution.context.LocalContext;
import org.jscience.physics.unit.BaseUnit;
import org.jscience.physics.unit.PhysicsUnit;
import org.jscience.physics.unit.system.SI;
import org.jscience.physics.unit.converter.AbstractUnitConverter;
import org.unitsofmeasurement.quantity.Quantity;
import org.unitsofmeasurement.unit.UnitConverter;

/**
 * <p> This class represents the model used to perform dimensional analysis.</p>
 *
 * <p> To select a model, one may use any of the physics model sub-classes.
 *     [code]
 *     LocalContext.enter(); 
 *     try {
 *         PhysicsModel.setCurrent(new RelativisticModel());
 *         SI.KILOGRAM.getConverterToAny(SI.JOULE); // Mass to Energy conversion allowed!
 *     } finally {
 *         LocalContext.exit(); // Revert to previous model.
 *     }
 *     [/code]</p>
 *     
 * @see <a href="http://en.wikipedia.org/wiki/Dimensional_analysis">Wikipedia: Dimensional Analysis</a>
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public abstract class PhysicsModel {

    /**
     * Holds the getCurrent model.
     */
    private static LocalContext.Reference<PhysicsModel> Current = new LocalContext.Reference<PhysicsModel>(new StandardModel());

    /**
     * Returns the physics model used by the current thread
     * (by default an instance of {@link StandardModel}).
     *
     * @return the getCurrent physical model.
     * @see LocalContext
     */
    public static PhysicsModel getCurrent() {
        return PhysicsModel.Current.get();
    }

    /**
     * Sets the current physics model (local to the current thread when executing
     * within a {@link LocalContext}).
     *
     * @param  model the context-local physics model.
     * @see    #getCurrent
     */
    public static void setCurrent(PhysicsModel model) {
        PhysicsModel.Current.set(model);
    }

    /**
     * Default constructor (allows for derivation).
     */
    protected PhysicsModel() {
    }

    /**
     * Returns the dimensions is equivalent to the one specified but made up
     * of independent dimensions. The default implementation returns
     * the specified dimension.
     *
     * @param dimension the dimension for which a commensurate dimension is returned.
     * @return <code>this</code>
     */
    public PhysicsDimension getBaseDimension(PhysicsDimension dimension) {
        return dimension;
    }

    /**
     * Returns the converter from dimensional transform of the specified dimension to its
     * commensurate dimension. For example, using a relativistic model
     * the dimensional transform of length is <code>RationalConverter(1, 299792458)</code>
     * (convertion to TIME dimensional unit (1/C).
 *
     *
     * to its commensurate
     * dimension.base unit
     * (converter to its dimensional unit). The default implementation
     * returns the identity converter.
     *
     * @param unit the base unit for which the dimensional transform is returned.
     * @return the unit converter to the dimensional unit of the specified unit.
     */
    public UnitConverter getConverterToBase(PhysicsDimension dimension) {
        return AbstractUnitConverter.IDENTITY;
    }

}
