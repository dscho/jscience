/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

import org.jscience.physics.unit.PhysicsDimension;
import javolution.context.LocalContext;
import org.jscience.physics.unit.BaseUnit;
import org.jscience.physics.unit.SI;
import org.jscience.physics.unit.converter.AbstractUnitConverter;
import org.unitsofmeasurement.unit.SystemOfUnits;

/**
 * <p> This class represents the model defining the properties of physical
 *     quantities such as their physical dimensions, their default units, etc.
 *     Instances of this class are typically used when performing dimensional
 *     analysis.</p>
 *     
 * <p> To select a model, one may use sub-classes <code>select</code> methods.
 *     [code]
 *     LocalContext.enter(); 
 *     try {
 *         QuantumModel.select(); // Allows quantum conversion by the current thread.
 *         ...
 *     } finally {
 *         LocalContext.exit();
 *     }
 *     [/code]</p>
 *
 * <p> Applications may set their own physical model.
 *     [code]
 *     public static void main(String[] args) {
 *          PhysicsModel relativistic = new PhysicsModel() {
 *              public PhysicsDimension getDimension(BaseUnit<?> baseUnit) {
 *                  if (baseUnit.equals(SI.METRE)) return PhysicsDimension.TIME;
 *                  return super.getDimension(baseUnit);
 *              }
 *              public AbstractUnitConverter getDimensionalTransform(BaseUnit<?> baseUnit) {
 *                  if (baseUnit.equals(SI.METRE)) return new MultiplyConverter(1 / c)); // Converter to TIME.
 *                  return super.getDimensionalTransform(baseUnit);
 *              }
 *          };
 *          PhysicsModel.setInstance(relativistic); // Global (LocalContext should be used for thread-local settings).
 *          SI.KILOGRAM.getConverterToAny(SI.JOULE); // Allowed.
 *          ...
 *     }
 *     [/code]
 *     
 * @see <a href="http://en.wikipedia.org/wiki/Dimensional_analysis">Wikipedia: Dimensional Analysis</a>
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public abstract class PhysicsModel {

    /**
     * Holds the currentPhysicalModel model.
     */
    private static LocalContext.Reference<PhysicsModel> Current = new LocalContext.Reference<PhysicsModel>(new StandardModel());

    /**
     * Returns the physical model used by the currentPhysicalModel thread
     * (by default an instance of {@link StandardModel}).
     *
     * @return the currentPhysicalModel physical model.
     * @see LocalContext
     */
    public static PhysicsModel currentPhysicalModel() {
        return PhysicsModel.Current.get();
    }

    /**
     * Sets the currentPhysicalModel model (this method is called when a predefined
     * model is selected).
     *
     * @param  model the context-local physical model.
     * @see    #currentPhysicalModel
     */
    protected static void setCurrentPhysicalModel(PhysicsModel model) {
        PhysicsModel.Current.set(model);
    }

    /**
     * Default constructor (allows for derivation).
     */
    protected PhysicsModel() {
    }

    /**
     * Returns the system of units used with this model (default {@link SI}).
     */
    public SystemOfUnits getSystemOfUnits() {
        return SI.getInstance();
    }

    /**
     * Returns the dimension of the specified base unit.
     * If the specified base unit is unknown, it is assumed to be dimensionless.
     *
     * @param unit the base unit for which the dimension is returned.
     * @return the physical dimension for the unit.
     */
    public PhysicsDimension getDimension(BaseUnit<?> unit) {
        if (unit.equals(SI.METRE)) return PhysicsDimension.LENGTH;
        if (unit.equals(SI.KILOGRAM)) return PhysicsDimension.MASS;
        if (unit.equals(SI.SECOND)) return PhysicsDimension.TIME;
        if (unit.equals(SI.AMPERE)) return PhysicsDimension.ELECTRIC_CURRENT;
        if (unit.equals(SI.KELVIN)) return PhysicsDimension.TEMPERATURE;
        if (unit.equals(SI.MOLE)) return PhysicsDimension.AMOUNT_OF_SUBSTANCE;
        if (unit.equals(SI.CANDELA)) return PhysicsDimension.LUMINOUS_INTENSITY;
        return PhysicsDimension.NONE;
    }

    /**
     * Returns the dimensional transform of the specified base unit.
     *
     * @param unit the base unit for which the dimensional transform is returned.
     * @return the dimensional transform of the specified base unit.
     */
    public AbstractUnitConverter getDimensionalTransform(BaseUnit<?> unit) {
        return AbstractUnitConverter.IDENTITY;
    }

}
