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
import org.jscience.physics.unit.SI;
import org.jscience.physics.unit.converter.AbstractUnitConverter;
import org.unitsofmeasurement.quantity.Quantity;
import org.unitsofmeasurement.unit.Dimension;
import org.unitsofmeasurement.unit.SystemOfUnits;
import org.unitsofmeasurement.unit.Unit;
import org.unitsofmeasurement.unit.UnitConverter;

/**
 * <p> This class represents the model holding the mapping between quantity/units
 *     and their actual dimensions. Instances of this class are typically used
 *     for dimensional analysis.</p>
 *
 * <p> To select a model, one may use any of the sub-classes <code>select</code>
 *     static methods.
 *     [code]
 *     LocalContext.enter(); 
 *     try {
 *         QuantumModel.select(); // Allows quantum conversion by current thread.
 *         ...
 *     } finally {
 *         LocalContext.exit();
 *     }
 *     [/code]</p>
 *
 * <p> Applications may enter their own physical model.
 *     [code]
 *     public static void main(String[] args) {
 *          PhysicsModel relativistic = new PhysicsModel() {
 *              public PhysicsDimension getDimension(BaseUnit<?> unit) {
 *                  if (unit.equals(SI.METRE)) return PhysicsDimension.TIME;
 *                  return super.getDimension(unit);
 *              }
 *              public UnitConverter getDimensionalTransform(BaseUnit<?> unit) {
 *                  if (unit.equals(SI.METRE)) return new RationalConverter(1, 299792458)); // Converts to TIME dimensional unit (1/C).
 *                  return super.getDimensionalTransform(unit);
 *              }
 *          };
 *          PhysicsModel.setCurrent(relativistic); // Global setting (main routine).
 *          SI.KILOGRAM.getConverterToAny(SI.JOULE); // Mass to Energy conversion allowed!
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
     * Holds the getCurrent model.
     */
    private static LocalContext.Reference<PhysicsModel> Current = new LocalContext.Reference<PhysicsModel>(new StandardModel());

    /**
     * Returns the physical model used by the getCurrent thread
     * (by default an instance of {@link StandardModel}).
     *
     * @return the getCurrent physical model.
     * @see LocalContext
     */
    public static PhysicsModel getCurrent() {
        return PhysicsModel.Current.get();
    }

    /**
     * Sets the getCurrent model (this method is called when a predefined
     * model is selected).
     *
     * @param  model the context-local physical model.
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
     * Returns the physical dimension of the specified quantity or <code>null</code>
     * if the specified quantity is unknown. The default implementation
     * recognizes only the {@link SI} quantities.
     *
     * @param quantityType the quantity.
     * @return <code>SI.getInstance().getUnit(quantityType).getDimension()</code>
     */
    public <Q extends Quantity<Q>> PhysicsDimension getDimension(Class<Q> quantityType) {
        PhysicsUnit<Q> unit = (PhysicsUnit<Q>) SI.getInstance().getUnit(quantityType);
        return unit != null ? unit.getDimension() : null;
    }

    /**
     * Returns the dimension for the specified base unit.
     * If the specified base unit is unknown, it is assumed to be dimensionless.
     *
     * @param unit the base unit for which the dimension is returned.
     * @return the specified unit dimension.
     */
    public PhysicsDimension getDimension(BaseUnit<?> unit) {
        if (unit.equals(SI.KILOGRAM)) return PhysicsDimension.MASS;
        if (unit.equals(SI.SECOND)) return PhysicsDimension.TIME;
        if (unit.equals(SI.AMPERE)) return PhysicsDimension.ELECTRIC_CURRENT;
        if (unit.equals(SI.KELVIN)) return PhysicsDimension.TEMPERATURE;
        if (unit.equals(SI.MOLE)) return PhysicsDimension.AMOUNT_OF_SUBSTANCE;
        if (unit.equals(SI.CANDELA)) return PhysicsDimension.LUMINOUS_INTENSITY;
        return PhysicsDimension.NONE;
    }

    /**
     * Returns the dimensional transform of the specified base unit
     * (converter to its dimensional unit). The default implementation
     * returns the identity converter.
     *
     * @param unit the base unit for which the dimensional transform is returned.
     * @return the unit converter to the dimensional unit of the specified unit.
     */
    public UnitConverter getDimensionalTransform(BaseUnit<?> unit) {
        return AbstractUnitConverter.IDENTITY;
    }

}
