/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import org.jscience.physics.unit.converter.AbstractUnitConverter;
import java.io.IOException;
import javolution.util.FastMap;
import org.unitsofmeasurement.unit.IncommensurableException;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Map;
import javolution.lang.ValueType;
import javolution.text.TextBuilder;
import javolution.xml.XMLSerializable;
import org.jscience.physics.model.PhysicsModel;
import org.unitsofmeasurement.quantity.Quantity;

import org.unitsofmeasurement.quantity.Dimensionless;
import org.jscience.physics.unit.converter.AddConverter;
import org.jscience.physics.unit.converter.MultiplyConverter;
import org.jscience.physics.unit.converter.RationalConverter;
import org.jscience.physics.unit.format.UCUMFormat;
import org.unitsofmeasurement.unit.Dimension;
import org.unitsofmeasurement.unit.UnconvertibleException;
import org.unitsofmeasurement.unit.Unit;
import org.unitsofmeasurement.unit.UnitConverter;

/**
 * <p> The class represents physical units.</p>
 *
 * <p> All physical units can be devised around the seven base units defined
 *     by the {@link SI} units.</p>
 *
 * <p> For all physical units, units conversions are symmetrical:
 *     <code>u1.getConverterTo(u2).equals(u2.getConverterTo(u1).inverse()</code>.
 *     Non-physical units (e.g. currency units) for which conversion is
 *     not symmetrical should have their own separate class hierarchy and
 *     are considered distinct (e.g. financial units), although
 *     they can always be combined with physical units (e.g."€/Kg", "$/h").</p>
 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public abstract class PhysicsUnit<Q extends Quantity<Q>> implements Unit<Q>, ValueType, XMLSerializable {

    /**
     * Holds the dimensionless unit <code>ONE</code>.
     */
    public static final PhysicsUnit<Dimensionless> ONE = new ProductUnit<Dimensionless>();

    /**
     * Holds the units symbols (base unit or alternate units).
     */
    protected static final FastMap<String, PhysicsUnit<?>> SYMBOL_TO_UNIT = new FastMap<String, PhysicsUnit<?>>();

    /**
     * Default constructor.
     */
    protected PhysicsUnit() {
    }

    /**
     * Returns the {@link SI} unit from which this unit is derived.
     *
     * <p> SI units are either base units, {@link #alternate alternate
     *     units} or product of rational powers of SI units.</p>
     *
     * <p> Because the SI unit is unique by quantity type, it can be
     *     be used to identify the quantity given the unit. For example:[code]
     *     static boolean isAngularVelocity(Unit<?> unit) {
     *         return unit.toSI().equals(RADIAN.divide(SECOND));
     *     }
     *     assert(REVOLUTION.divide(MINUTE).isAngularVelocity()); // Returns true.
     * [/code]
     *
     * @return the SI unit this unit is derived from or <code>this</code>
     *         if this unit is a SI unit.
     */
    public abstract PhysicsUnit<Q> toSI();

    /**
     * Returns the converter from this physical unit to its {@link #toSI
     * SI unit}.
     *
     * <p><i> Note: Not all instances of {@link PhysicsUnit} have a converter
     *             to their SI unit. For example, no such converter can
     *             be constructed from "°C/m" to its SI unit "K/m".</i></p>
     *
     * @return the unit converter from this unit to its metric unit.
     * @throws UnsupportedOperationException if a converter to the SI unit
     *         cannot be constructed.
     */
    public abstract AbstractUnitConverter getConverterToSI() throws UnsupportedOperationException;

    /**
     * Returns the physical unit represented by the specified characters
     * as per standard <a href="http://www.unitsofmeasure.org/">UCUM</a> format.
     *
     * Locale-sensitive unit parsing should be handled using the OSGi
     * {@link org.unitsofmeasurement.service.UnitFormat} service (or
     * the {@link org.jscience.physics.unit.format.LocalUnitFormat} class
     * for non-OSGi applications).
     *
     * <p>Note: The standard UCUM format supports dimensionless units.[code]
     *       PhysicsUnit<Dimensionless> PERCENT = PhysicsUnit.valueOf("100").inverse().asType(Dimensionless.class);
     * [/code]</p>
     *
     * @param charSequence the character sequence to parse.
     * @return <code>UCUMFormat.getCaseSensitiveInstance().parse(csq, new ParsePosition(0))</code>
     * @throws IllegalArgumentException if the specified character sequence
     *         cannot be correctly parsed (e.g. not UCUM compliant).
     */
    public static PhysicsUnit<?> valueOf(CharSequence charSequence) {
        return UCUMFormat.getCaseSensitiveInstance().parse(charSequence, new ParsePosition(0));
    }

    /**
     * Returns the standard <a href="http://unitsofmeasure.org/">UCUM</a>
     * representation of this physical unit. The string produced for a given unit is
     * always the same; it is not affected by the locale. It can be used as a
     * canonical string representation for exchanging units, or as a key for a
     * Hashtable, etc.
     *
     * Locale-sensitive unit parsing should be handled using the OSGi
     * {@link org.unitsofmeasurement.service.UnitFormat} service (or
     * the {@link org.jscience.physics.unit.format.LocalUnitFormat} class
     * for non-OSGi applications).
     *
     * @return <code>UCUMFormat.getCaseSensitiveInstance().format(this)</code>
     */
    @Override
    public String toString() {
        TextBuilder tmp = TextBuilder.newInstance();
        try {
            return UCUMFormat.getCaseSensitiveInstance().format(this, tmp).toString();
        } catch (IOException ioException) {
             throw new Error(ioException); // Should never happen.
        } finally {
            TextBuilder.recycle(tmp);
        }
    }

    /////////////////////////////////////////////////////////
    // Implements org.unitsofmeasurement.Unit<Q> interface //
    /////////////////////////////////////////////////////////

    @Override
    public abstract String getSymbol();

    @Override
    public abstract PhysicsDimension getDimension();

    @Override
    public final PhysicsUnit<Q> getSystemUnit() {
        return toSI();
    }

    @Override
    public abstract Map<? extends PhysicsUnit, Integer> getProductUnits();

    @Override
    public boolean isCompatible(Unit<?> that) {
        Dimension thisDimension = this.getDimension();
        Dimension thatDimension = that.getDimension();
        return thisDimension.equals(thatDimension) ? true :

    }

    @Override
    public <T extends Quantity<T>> PhysicsUnit<T> asType(Class<T> type) {
        Unit<T> typeUnit = PhysicsModel.currentPhysicalModel().getSystemOfUnits().getUnit(type);
        if (isCompatible(typeUnit))
            return (PhysicsUnit<T>) this;
        throw new ClassCastException("The unit: " + this + " is not compatible with quantities of type " + type);
    }

    @Override
    public UnitConverter getConverterTo(Unit<Q> that) throws UnconvertibleException {
        if (!(that instanceof PhysicsUnit))
            return that.getConverterTo(this).inverse();
        AbstractUnitConverter thisToSI= this.getConverterToSI();
        AbstractUnitConverter thatToSI= ((PhysicsUnit)that).getConverterToSI();
        return thatToSI.inverse().concatenate(thisToSI);
    }

    @Override
    public UnitConverter getConverterToAny(Unit<?> that) throws IncommensurableException,
            UnconvertibleException {
        if (!(that instanceof PhysicsUnit))
            return that.getConverterToAny(this).inverse();
        if (!this.isCompatible(that))
            throw new IncommensurableException(this + " is not compatible with " + that);
        AbstractUnitConverter thisToSI= this.getConverterToSystemUnit();
        AbstractUnitConverter thatToSI= ((PhysicsUnit)that).getConverterToSystemUnit();

        PhysicsDimension thisDimension = this.getDimension();
        PhysicsDimension thatDimension = ((PhysicsUnit) that).getDimension();
        if (thisDimension.equals(thatDimension))
            return thatToSI.inverse().concatenate(thisToSI);
        UnitConverter dimensionConverter = PhysicsModel.getInstance().getConverter(thisDimension, thatDimension);
        return thatToSI.inverse().concatenate(dimensionConverter).concatenate(thisToSI);
    }

    /**
     * Returns a system unit equivalent to this unscaled standard unit but used
     * in expressions to distinguish between quantities of a different nature
     * but of the same dimensions.
     *
     * <p> Examples of alternate units:[code]
     *     Unit<Angle> RADIAN = ONE.alternate("rad").asType(Angle.class);
     *     Unit<Force> NEWTON = METRE.times(KILOGRAM).divide(SECOND.pow(2)).alternate("N").asType(Force.class);
     *     Unit<Pressure> PASCAL = NEWTON.divide(METRE.pow(2)).alternate("Pa").asType(Pressure.class);
     * [/code]
     * </p>
     *
     * @param symbol the new symbol for the alternate unit.
     * @return the alternate unit.
     * @throws UnsupportedOperationException if this unit is not an unscaled standard unit.
     * @throws IllegalArgumentException if the specified symbol is already
     *         associated to a different unit.
     */
    @Override
    public PhysicsUnit<?> alternate(String symbol) {
        return new AlternateUnit(symbol, this);
    }

    /**
     * Returns the unit derived from this unit using the specified converter.
     * The converter does not need to be linear. For example:[code]
     *     Unit<Dimensionless> DECIBEL = Unit.ONE.transform(
     *         new LogConverter(10).inverse().concatenate(
     *             new RationalConverter(1, 10)));
     * [/code]
     *
     * @param operation the converter from the transformed unit to this unit.
     * @return the unit after the specified transformation.
     */
    @Override
    public PhysicsUnit<Q> transform(UnitConverter operation) {
        if (this instanceof TransformedUnit<?>) {
            TransformedUnit<Q> tf = (TransformedUnit<Q>) this;
            PhysicsUnit<Q> parent = tf.getParentUnit();
            AbstractUnitConverter toParent = tf.toParentUnit().concatenate(operation);
            if (toParent == AbstractUnitConverter.IDENTITY)
                return parent;
            return new TransformedUnit<Q>(parent, toParent);
        }
        if (operation == AbstractUnitConverter.IDENTITY)
            return this;
        return new TransformedUnit<Q>(this, AbstractUnitConverter.valueOf(operation));
    }

    /**
     * Returns the result of adding an offset to this unit. The returned unit is
     * convertible with all units that are convertible with this unit.
     *
     * @param offset the offset added (expressed in this unit, e.g.
     * <code>CELSIUS = KELVIN.add(273.15)</code>).
     * @return this unit offset by the specified value.
     */
    @Override
    public PhysicsUnit<Q> add(double offset) {
        if (offset == 0)
            return this;
        return transform(new AddConverter(offset));
    }

    /**
     * Returns the result of multiplying this unit by the specified factor.
     * If the factor is an integer value, the multiplication is exact
     * (recommended). For example:<pre><code>
     *    FOOT = METRE.multiply(3048).divide(10000); // Exact definition.
     *    ELECTRON_MASS = KILOGRAM.multiply(9.10938188e-31); // Approximation.
     * </code></pre>
     *
     * @param factor the factor
     * @return this unit scaled by the specified factor.
     */
    @Override
    public PhysicsUnit<Q> multiply(double factor) {
        if (factor == 1)
            return this;
        if (isLongValue(factor))
            return transform(new RationalConverter(BigInteger.valueOf((long)factor), BigInteger.ONE));
        return transform(new MultiplyConverter(factor));
    }
    private static boolean isLongValue(double value) {
        if ((value < Long.MIN_VALUE) || (value > Long.MAX_VALUE)) return false;
        return Math.floor(value) == value;
    }

    /**
     * Returns the product of this unit with the one specified.
     *
     * <p> Note: If the specified unit (that) is not a physical unit, then
     * <code>that.multiply(this)</code> is returned.</p>
     *
     * @param that the unit multiplicand.
     * @return <code>this * that</code>
     */
    @Override
    public Unit<?> multiply(Unit<?> that) {
        if (that instanceof PhysicsUnit)
            return multiply((PhysicsUnit<?>) that);
        return that.multiply(this); // Commutatif.
    }

    /**
     * Returns the product of this physical unit with the one specified.
     *
     * @param that the physical unit multiplicand.
     * @return <code>this * that</code>
     */
    public PhysicsUnit<?> multiply(PhysicsUnit<?> that) {
        if (this.equals(ONE))
            return that;
        if (that.equals(ONE))
            return this;
        return ProductUnit.getProductInstance(this, that);
    }

    /**
     * Returns the inverse of this physical unit.
     *
     * @return <code>1 / this</code>
     */
    @Override
    public PhysicsUnit<?> inverse() {
        if (this.equals(ONE))
            return this;
        return ProductUnit.getQuotientInstance(ONE, this);
    }

    /**
     * Returns the result of dividing this unit by the specifified divisor.
     * If the factor is an integer value, the division is exact.
     * For example:<pre><code>
     *    QUART = GALLON_LIQUID_US.divide(4); // Exact definition.
     * </code></pre>
     * @param divisor the divisor value.
     * @return this unit divided by the specified divisor.
     */
    @Override
    public PhysicsUnit<Q> divide(double divisor) {
        if (divisor == 1)
            return this;
        if (isLongValue(divisor))
            return transform(new RationalConverter(BigInteger.ONE, BigInteger.valueOf((long)divisor)));
        return transform(new MultiplyConverter(1.0/divisor));
    }

    /**
     * Returns the quotient of this unit with the one specified.
     *
     * @param that the unit divisor.
     * @return <code>this.multiply(that.inverse())</code>
     */
    @Override
    public Unit<?> divide(Unit<?> that) {
        return this.multiply(that.inverse());
    }

    /**
     * Returns the quotient of this physical unit with the one specified.
     *
     * @param that the physical unit divisor.
     * @return <code>this.multiply(that.inverse())</code>
     */
    public PhysicsUnit<?> divide(PhysicsUnit<?> that) {
        return this.multiply(that.inverse());
    }

    /**
     * Returns a unit equals to the given root of this unit.
     *
     * @param n the root's order.
     * @return the result of taking the given root of this unit.
     * @throws ArithmeticException if <code>n == 0</code> or if this operation
     *         would result in an unit with a fractional exponent.
     */
    @Override
    public PhysicsUnit<?> root(int n) {
        if (n > 0)
            return ProductUnit.getRootInstance(this, n);
        else if (n == 0)
            throw new ArithmeticException("Root's order of zero");
        else // n < 0
            return ONE.divide(this.root(-n));
    }

    /**
     * Returns a unit equals to this unit raised to an exponent.
     *
     * @param n the exponent.
     * @return the result of raising this unit to the exponent.
     */
    @Override
    public PhysicsUnit<?> pow(int n) {
        if (n > 0)
            return this.multiply(this.pow(n - 1));
        else if (n == 0)
            return ONE;
        else // n < 0
            return ONE.divide(this.pow(-n));
    }
    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object that);


}