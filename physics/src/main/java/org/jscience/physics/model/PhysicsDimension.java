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
import javolution.text.TextBuilder;
import javolution.xml.XMLSerializable;
import org.jscience.physics.unit.BaseUnit;
import org.jscience.physics.unit.PhysicsUnit;
import org.jscience.physics.unit.SI;

import org.unitsofmeasurement.unit.Dimension;

/**
*  <p> This class represents a physical dimension.</p>
 *
 * <p> The dimension for any given quantity can be retrieved from the current
 *     {@link PhysicsModel physical model}.
 *     [code]
 *        PhysicsDimension velocityDimension
 *            = PhysicsModel.getCurrent().getDimension(Velocity.class);
 *     [/code]<p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public class PhysicsDimension implements Dimension, XMLSerializable {

    /**
     * Holds dimensionless.
     */
    public static final PhysicsDimension NONE = new PhysicsDimension(SI.ONE);

    /**
     * Holds length dimension (L).
     */
    public static final PhysicsDimension LENGTH = new PhysicsDimension('L');

    /**
     * Holds mass dimension (M).
     */
    public static final PhysicsDimension MASS = new PhysicsDimension('M');

    /**
     * Holds time dimension (T).
     */
    public static final PhysicsDimension TIME = new PhysicsDimension('T');

    /**
     * Holds electric current dimension (I).
     */
    public static final PhysicsDimension ELECTRIC_CURRENT = new PhysicsDimension('I');

    /**
     * Holds temperature dimension (Θ).
     */
    public static final PhysicsDimension TEMPERATURE = new PhysicsDimension('Θ');

    /**
     * Holds amount of substance dimension (N).
     */
    public static final PhysicsDimension AMOUNT_OF_SUBSTANCE = new PhysicsDimension('N');

    /**
     * Holds luminous intensity dimension (J).
     */
    public static final PhysicsDimension LUMINOUS_INTENSITY = new PhysicsDimension('J');

    /**
     * Holds the pseudo unit associated to this dimension.
     */
    private PhysicsUnit<?> pseudoUnit;

    /**
     * Returns the physical dimension having the specified symbol.
     *
     * @param symbol the associated symbol.
     */
    public PhysicsDimension(char symbol) {
        TextBuilder label = TextBuilder.newInstance();
        label.append('[').append(symbol).append(']');
        pseudoUnit = new BaseUnit(label.toString());
    }

    /**
     * Constructor from pseudo-unit (not visible).
     *
     * @param pseudoUnit the pseudo-unit.
     */
    private PhysicsDimension(PhysicsUnit<?> pseudoUnit) {
        this.pseudoUnit = pseudoUnit;
    }

    /**
     * Returns the product of this dimension with the one specified.
     *
     * @param  that the dimension multiplicand.
     * @return <code>this * that</code>
     */
    public final PhysicsDimension multiply(Dimension that) {
        PhysicsUnit thisPseudoUnit = this.pseudoUnit;
        PhysicsUnit thatPseudoUnit = (that instanceof PhysicsDimension) ?
            ((PhysicsDimension)that).pseudoUnit : new BaseUnit(that.toString());
        return new PhysicsDimension(thisPseudoUnit.multiply(thatPseudoUnit));
    }

    /**
     * Returns the quotient of this dimension with the one specified.
     *
     * @param  that the dimension divisor.
     * @return <code>this / that</code>
     */
    public final PhysicsDimension divide(Dimension that) {
        return this.multiply(that.pow(-1));
    }

    /**
     * Returns this dimension raised to an exponent.
     *
     * @param  n the exponent.
     * @return the result of raising this dimension to the exponent.
     */
    public final PhysicsDimension pow(int n) {
        return new PhysicsDimension(this.pseudoUnit.pow(n));
    }

    /**
     * Returns the given root of this dimension.
     *
     * @param  n the root's order.
     * @return the result of taking the given root of this dimension.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final PhysicsDimension root(int n) {
        return new PhysicsDimension(this.pseudoUnit.root(n));
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
            fundamentalDimensions.put(new PhysicsDimension(entry.getKey()), entry.getValue());
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

}
