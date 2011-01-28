/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import javolution.util.FastMap;
import java.util.Map;
import javolution.context.ImmortalContext;
import javolution.context.ObjectFactory;
import javolution.lang.ValueType;
import javolution.text.TextBuilder;
import javolution.xml.XMLSerializable;
import org.jscience.physics.unit.BaseUnit;
import org.jscience.physics.unit.PhysicsUnit;

import org.unitsofmeasurement.unit.Dimension;

/**
*  <p> This class represents a physical dimension.</p>
 *
 * <p> The dimension for any given quantity can be retrieved from the current
 *     {@link PhysicalModel physical model}.[code]
 *        PhysicsDimension velocityDimension
 *            = PhysicalModel.currentPhysicalModel().getDimension(Velocity.class);
 *     [/code]
 * <p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class PhysicsDimension implements Dimension, ValueType, XMLSerializable {

    /**
     * Holds dimensionless.
     */
    public static final PhysicsDimension NONE;

    /**
     * Holds length dimension (L).
     */
    public static final PhysicsDimension LENGTH;

    /**
     * Holds mass dimension (M).
     */
    public static final PhysicsDimension MASS;

    /**
     * Holds time dimension (T).
     */
    public static final PhysicsDimension TIME;

    /**
     * Holds electric current dimension (I).
     */
    public static final PhysicsDimension ELECTRIC_CURRENT;

    /**
     * Holds temperature dimension (Θ).
     */
    public static final PhysicsDimension TEMPERATURE;

    /**
     * Holds amount of substance dimension (N).
     */
    public static final PhysicsDimension AMOUNT_OF_SUBSTANCE;

    /**
     * Holds luminous intensity dimension (J).
     */
    public static final PhysicsDimension LUMINOUS_INTENSITY;

    /**
     * Returns the physical dimension having the specified symbol.
     *
     * @param symbol the associated symbol.
     */
    public static PhysicsDimension valueOf(char symbol) {
        TextBuilder label = TextBuilder.newInstance();
        label.append('[').append(symbol).append(']');
        return PhysicsDimension.valueOf(BaseUnit.valueOf(label));
    }
    private static PhysicsDimension valueOf(PhysicsUnit<?> pseudoUnit) {
        PhysicsDimension d = FACTORY.object();
        d.pseudoUnit = pseudoUnit;
        return d;
    }

    /**
     * Holds the pseudo unit associated to this dimension.
     */
    private PhysicsUnit<?> pseudoUnit;

    /**
     * Default constructor (not visible).
     */
    private PhysicsDimension() {
    }

    /**
     * Returns the product of this dimension with the one specified.
     *
     * @param  that the dimension multiplicand.
     * @return <code>this * that</code>
     */
    public final PhysicsDimension multiply(Dimension that) {
        return PhysicsDimension.valueOf(this.pseudoUnit.multiply(((PhysicsDimension)that).pseudoUnit));
    }

    /**
     * Returns the quotient of this dimension with the one specified.
     *
     * @param  that the dimension divisor.
     * @return <code>this / that</code>
     */
    public final PhysicsDimension divide(Dimension that) {
        return PhysicsDimension.valueOf(this.pseudoUnit.divide(((PhysicsDimension)that).pseudoUnit));
    }

    /**
     * Returns this dimension raised to an exponent.
     *
     * @param  n the exponent.
     * @return the result of raising this dimension to the exponent.
     */
    public final PhysicsDimension pow(int n) {
        return PhysicsDimension.valueOf(this.pseudoUnit.pow(n));
    }

    /**
     * Returns the given root of this dimension.
     *
     * @param  n the root's order.
     * @return the result of taking the given root of this dimension.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final PhysicsDimension root(int n) {
        return PhysicsDimension.valueOf(this.pseudoUnit.root(n));
    }

    /**
     * Returns the fundamental dimensions and their exponent whose product is
     * this dimension or <code>null</code> if this dimension is a fundamental
     * dimension.
     *
     * @return the mapping between the fundamental dimensions and their exponent.
     */
    public Map<? extends PhysicsDimension, Integer> getProductDimensions() {
        Map<? extends PhysicsUnit, Integer> pseudoUnits = pseudoUnit.getProductUnits();
        if (pseudoUnit == null) return null;
        FastMap<PhysicsDimension, Integer> fundamentalDimensions = FastMap.newInstance();
        for (Map.Entry<? extends PhysicsUnit, Integer> entry : pseudoUnits.entrySet()) {
            fundamentalDimensions.put(PhysicsDimension.valueOf(entry.getKey()), entry.getValue());
        }
        return fundamentalDimensions;
    }

    @Override
    public String toString() {
        return pseudoUnit.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;
        return (that instanceof PhysicsDimension) && pseudoUnit.equals(((PhysicsDimension) that).pseudoUnit);
    }

    @Override
    public int hashCode() {
        return pseudoUnit.hashCode();
    }

    @Override
    public PhysicsDimension copy() {
        return PhysicsDimension.valueOf(pseudoUnit.copy());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Javolution Context Allocations.
    private static final ObjectFactory<PhysicsDimension> FACTORY
            = new ObjectFactory<PhysicsDimension>() {
        protected PhysicsDimension create() {
            return new PhysicsDimension();
        }
    };
    static {
        ImmortalContext.enter();
        try { // Ensures factory produced constants are in immortal memory.
             NONE = PhysicsDimension.valueOf(PhysicsUnit.ONE);
             LENGTH = PhysicsDimension.valueOf('L');
             MASS = PhysicsDimension.valueOf('M');
             TIME = PhysicsDimension.valueOf('T');
             ELECTRIC_CURRENT = PhysicsDimension.valueOf('I');
             TEMPERATURE = PhysicsDimension.valueOf('Θ');
             AMOUNT_OF_SUBSTANCE = PhysicsDimension.valueOf('N');
             LUMINOUS_INTENSITY = PhysicsDimension.valueOf('J');
        } finally {
           ImmortalContext.exit();
        }
    }
}
