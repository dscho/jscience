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
import org.jscience.physics.unit.PhysicalSystemOfUnits;
import org.jscience.physics.unit.SI;
import org.jscience.physics.unit.converter.PhysicalUnitConverter;

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
 *          PhysicalModel relativistic = new PhysicalModel() {
 *              public PhysicalDimension getDimension(BaseUnit<?> baseUnit) {
 *                  if (baseUnit.equals(SI.METRE)) return PhysicalDimension.TIME;
 *                  return super.getDimension(baseUnit);
 *              }
 *              public PhysicalUnitConverter getDimensionalTransform(BaseUnit<?> baseUnit) {
 *                  if (baseUnit.equals(SI.METRE)) return new MultiplyConverter(1 / c)); // Converter to TIME.
 *                  return super.getDimensionalTransform(baseUnit);
 *              }
 *          };
 *          PhysicalModel.setInstance(relativistic); // Global (LocalContext should be used for thread-local settings).
 *          SI.KILOGRAM.getConverterToAny(SI.JOULE); // Allowed.
 *          ...
 *     }
 *     [/code]
 *     
 * @see <a href="http://en.wikipedia.org/wiki/Dimensional_analysis">Wikipedia: Dimensional Analysis</a>
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public abstract class PhysicalModel {

    /**
     * Holds the currentPhysicalModel model.
     */
    private static LocalContext.Reference<PhysicalModel> Current = new LocalContext.Reference<PhysicalModel>(new StandardModel());

    /**
     * Returns the physical model used by the currentPhysicalModel thread
     * (by default an instance of {@link StandardModel}).
     *
     * @return the currentPhysicalModel physical model.
     * @see LocalContext
     */
    public static PhysicalModel currentPhysicalModel() {
        return PhysicalModel.Current.get();
    }

    /**
     * Sets the currentPhysicalModel model (this method is called when a predefined
     * model is selected).
     *
     * @param  model the context-local physical model.
     * @see    #currentPhysicalModel
     */
    protected static void setCurrentPhysicalModel(PhysicalModel model) {
        PhysicalModel.Current.set(model);
    }

    /**
     * Default constructor (allows for derivation).
     */
    protected PhysicalModel() {
    }

    /**
     * Returns the system of units used with this model (default {@link SI}).
     */
    public PhysicalSystemOfUnits getSystemOfUnits() {
        return SI.getInstance();
    }

    /**
     * Returns the dimension of the specified base unit.
     * If the specified base unit is unknown, it is assumed to be dimensionless.
     *
     * @param unit the base unit for which the dimension is returned.
     * @return the physical dimension for the unit.
     */
    public PhysicalDimension getDimension(BaseUnit<?> unit) {
        if (unit.equals(SI.METRE)) return PhysicalDimension.LENGTH;
        if (unit.equals(SI.KILOGRAM)) return PhysicalDimension.MASS;
        if (unit.equals(SI.SECOND)) return PhysicalDimension.TIME;
        if (unit.equals(SI.AMPERE)) return PhysicalDimension.ELECTRIC_CURRENT;
        if (unit.equals(SI.KELVIN)) return PhysicalDimension.TEMPERATURE;
        if (unit.equals(SI.MOLE)) return PhysicalDimension.AMOUNT_OF_SUBSTANCE;
        if (unit.equals(SI.CANDELA)) return PhysicalDimension.LUMINOUS_INTENSITY;
        return PhysicalDimension.NONE;
    }

    /**
     * Returns the dimensional transform of the specified base unit.
     *
     * @param unit the base unit for which the dimensional transform is returned.
     * @return the dimensional transform of the specified base unit.
     */
    public PhysicalUnitConverter getDimensionalTransform(BaseUnit<?> unit) {
        return PhysicalUnitConverter.IDENTITY;
    }

}
