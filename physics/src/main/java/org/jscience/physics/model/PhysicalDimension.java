/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.model;

import javolution.util.FastMap;
import java.util.Map;
import javolution.context.ImmortalContext;
import javolution.context.ObjectFactory;
import javolution.lang.ValueType;
import javolution.text.TextBuilder;
import javolution.xml.XMLSerializable;
import org.jscience.physics.unit.BaseUnit;
import org.jscience.physics.unit.PhysicalUnit;

import org.unitsofmeasurement.unit.Dimension;

/**
*  <p> This class represents a physical dimension.</p>
 *
 * <p> The dimension for any given quantity can be retrieved from the current
 *     {@link PhysicalModel physical model}.[code]
 *        PhysicalDimension velocityDimension
 *            = PhysicalModel.currentPhysicalModel().getDimension(Velocity.class);
 *     [/code]
 * <p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class PhysicalDimension implements Dimension, ValueType, XMLSerializable {

    /**
     * Holds dimensionless.
     */
    public static final PhysicalDimension NONE;

    /**
     * Holds length dimension (L).
     */
    public static final PhysicalDimension LENGTH;

    /**
     * Holds mass dimension (M).
     */
    public static final PhysicalDimension MASS;

    /**
     * Holds time dimension (T).
     */
    public static final PhysicalDimension TIME;

    /**
     * Holds electric current dimension (I).
     */
    public static final PhysicalDimension ELECTRIC_CURRENT;

    /**
     * Holds temperature dimension (Θ).
     */
    public static final PhysicalDimension TEMPERATURE;

    /**
     * Holds amount of substance dimension (N).
     */
    public static final PhysicalDimension AMOUNT_OF_SUBSTANCE;

    /**
     * Holds luminous intensity dimension (J).
     */
    public static final PhysicalDimension LUMINOUS_INTENSITY;

    /**
     * Returns the physical dimension having the specified symbol.
     *
     * @param symbol the associated symbol.
     */
    public static PhysicalDimension valueOf(char symbol) {
        TextBuilder label = TextBuilder.newInstance();
        label.append('[').append(symbol).append(']');
        return PhysicalDimension.valueOf(BaseUnit.valueOf(label));
    }
    private static PhysicalDimension valueOf(PhysicalUnit<?> pseudoUnit) {
        PhysicalDimension d = FACTORY.object();
        d.pseudoUnit = pseudoUnit;
        return d;
    }

    /**
     * Holds the pseudo unit associated to this dimension.
     */
    private PhysicalUnit<?> pseudoUnit;

    /**
     * Default constructor (not visible).
     */
    private PhysicalDimension() {
    }

    /**
     * Returns the product of this dimension with the one specified.
     *
     * @param  that the dimension multiplicand.
     * @return <code>this * that</code>
     */
    public final PhysicalDimension multiply(Dimension that) {
        return PhysicalDimension.valueOf(this.pseudoUnit.multiply(((PhysicalDimension)that).pseudoUnit));
    }

    /**
     * Returns the quotient of this dimension with the one specified.
     *
     * @param  that the dimension divisor.
     * @return <code>this / that</code>
     */
    public final PhysicalDimension divide(Dimension that) {
        return PhysicalDimension.valueOf(this.pseudoUnit.divide(((PhysicalDimension)that).pseudoUnit));
    }

    /**
     * Returns this dimension raised to an exponent.
     *
     * @param  n the exponent.
     * @return the result of raising this dimension to the exponent.
     */
    public final PhysicalDimension pow(int n) {
        return PhysicalDimension.valueOf(this.pseudoUnit.pow(n));
    }

    /**
     * Returns the given root of this dimension.
     *
     * @param  n the root's order.
     * @return the result of taking the given root of this dimension.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final PhysicalDimension root(int n) {
        return PhysicalDimension.valueOf(this.pseudoUnit.root(n));
    }

    /**
     * Returns the fundamental dimensions and their exponent whose product is
     * this dimension or <code>null</code> if this dimension is a fundamental
     * dimension.
     *
     * @return the mapping between the fundamental dimensions and their exponent.
     */
    public Map<? extends PhysicalDimension, Integer> getProductDimensions() {
        Map<? extends PhysicalUnit, Integer> pseudoUnits = pseudoUnit.getProductUnits();
        if (pseudoUnit == null) return null;
        FastMap<PhysicalDimension, Integer> fundamentalDimensions = FastMap.newInstance();
        for (Map.Entry<? extends PhysicalUnit, Integer> entry : pseudoUnits.entrySet()) {
            fundamentalDimensions.put(PhysicalDimension.valueOf(entry.getKey()), entry.getValue());
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
        return (that instanceof PhysicalDimension) && pseudoUnit.equals(((PhysicalDimension) that).pseudoUnit);
    }

    @Override
    public int hashCode() {
        return pseudoUnit.hashCode();
    }

    @Override
    public PhysicalDimension copy() {
        return PhysicalDimension.valueOf(pseudoUnit.copy());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Javolution Context Allocations.
    private static final ObjectFactory<PhysicalDimension> FACTORY
            = new ObjectFactory<PhysicalDimension>() {
        protected PhysicalDimension create() {
            return new PhysicalDimension();
        }
    };
    static {
        ImmortalContext.enter();
        try { // Ensures factory produced constants are in immortal memory.
             NONE = PhysicalDimension.valueOf(PhysicalUnit.ONE);
             LENGTH = PhysicalDimension.valueOf('L');
             MASS = PhysicalDimension.valueOf('M');
             TIME = PhysicalDimension.valueOf('T');
             ELECTRIC_CURRENT = PhysicalDimension.valueOf('I');
             TEMPERATURE = PhysicalDimension.valueOf('Θ');
             AMOUNT_OF_SUBSTANCE = PhysicalDimension.valueOf('N');
             LUMINOUS_INTENSITY = PhysicalDimension.valueOf('J');
        } finally {
           ImmortalContext.exit();
        }
    }
}
