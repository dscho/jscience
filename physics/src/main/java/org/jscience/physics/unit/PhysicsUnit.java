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
import org.unitsofmeasurement.unit.IncommensurableException;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Map;
import javolution.text.TextBuilder;
import javolution.xml.XMLSerializable;
import org.jscience.physics.model.PhysicsDimension;
import org.jscience.physics.model.PhysicsModel;
import org.unitsofmeasurement.quantity.Quantity;

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
7 *
 * @param <Q> The type of the quantity measured by this unit.
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public abstract class PhysicsUnit<Q extends Quantity<Q>> implements Unit<Q>, XMLSerializable {

    /**
     * Default constructor.
     */
    protected PhysicsUnit() {
    }

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
     * Annotates the specified unit. Annotation does not change the unit
     * semantic. Annotations are often written between curly braces behind units.
     * For example:
     * [code]
     *     PhysicsUnit<Volume> PERCENT_VOL = SI.PERCENT.annotate("vol"); // "%{vol}"
     *     PhysicsUnit<Mass> KG_TOTAL = SI.KILOGRAM.annotate("total"); // "kg{total}"
     *     PhysicsUnit<Dimensionless> RED_BLOOD_CELLS = SI.ONE.annotate("RBC"); // "{RBC}"
     * [/code]
     *
     * @param annotation the unit annotation.
     * @return the annotated unit.
     */
    public PhysicsUnit<Q> annotate(String annotation) { // TODO: Move to spec.
        return new AnnotatedUnit<Q>(this, annotation);
    }

    /**
     * Returns the annotation for the specified unit or <code>null</code>
     * if none.
     *
     * @return this unit annotation or <code>null</code>
     */
    public String getAnnotation() { // TODO: Move to spec.
        return null;
    }

    /**
     * Returns this unit without its annotation or <code>this</code> if
     * this unit has no {@link #getAnnotation() annotation}.
     *
     * @return this unit without annotation or <code>this</code>
     */
    public PhysicsUnit<Q> getUnannotatedUnit() {
        return (this instanceof AnnotatedUnit) ? ((AnnotatedUnit)this).getActualUnit() : this;
    }

    /**
     * Indicates if this unit is a system unit.
     * System units are always unscaled metric units.
     *
     * @return <code>this.equals(getSystemUnit())</code>
     */
    public boolean isSystemUnit() {
        return this.equals(getSystemUnit());
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

    /**
     * Returns the converter from this unit to its system unit (SI).
     *
     * @return <code>getConverterTo(this.getSystemUnit())</code>
     */
   public abstract UnitConverter getConverterToSystemUnit();


   /////////////////////////////////////////////////////////
    // Implements org.unitsofmeasurement.Unit<Q> interface //
    /////////////////////////////////////////////////////////

    @Override
    public String getSymbol() {
        return null;
    }

    @Override
    public abstract PhysicsUnit<Q> getSystemUnit();

    @Override
    public abstract Map<? extends PhysicsUnit, Integer> getProductUnits();

    @Override
    public abstract PhysicsDimension getDimension();

    @Override
    public final boolean isCompatible(Unit<?> that) {
        return this.getDimension().equals(that.getDimension());
    }

    @Override
    public final <T extends Quantity<T>> PhysicsUnit<T> asType(Class<T> type) {
        Dimension typeDimension = PhysicsModel.getCurrent().getDimension(type);
        if ((typeDimension != null) && (!this.getDimension().equals(typeDimension)))
           throw new ClassCastException("The unit: " + this + " is not compatible with quantities of type " + type);
        return (PhysicsUnit<T>) this;
    }

    @Override
    public final UnitConverter getConverterTo(Unit<Q> that) throws UnconvertibleException {
        if ((this == that) || this.equals(that)) return AbstractUnitConverter.IDENTITY; // Shortcut.
        Unit<Q> thisSystemUnit = this.getSystemUnit();
        Unit<Q> thatSystemUnit = that.getSystemUnit();
        if (!thisSystemUnit.equals(thatSystemUnit)) return getConverterToAny(that); // They don't have the same system of unit!
        UnitConverter thisToSI= this.getConverterToSystemUnit();
        UnitConverter thatToSI= that.getConverterTo(thatSystemUnit);
        return thatToSI.inverse().concatenate(thisToSI);
    }

    @Override
    public final UnitConverter getConverterToAny(Unit<?> that) throws IncommensurableException,
            UnconvertibleException {
        if (!isCompatible(that))
            throw new IncommensurableException(this + " is not compatible with " + that);
        PhysicsModel model = PhysicsModel.getCurrent();
        Unit thisSystemUnit = this.getSystemUnit();
        UnitConverter thisToDimension = getDimensionalTransform(thisSystemUnit, model).concatenate(this.getConverterToSystemUnit());
        Unit thatSystemUnit = that.getSystemUnit();
        UnitConverter thatToDimension = getDimensionalTransform(thatSystemUnit, model).concatenate(that.getConverterTo(thatSystemUnit));
        return thatToDimension.inverse().concatenate(thisToDimension);
    }

    // Returns the dimensional transform of the specified system unit.
    // System units are always unscaled. There are cases known:
    // BaseUnit, AlternateUnit or ProductUnit (rational product of system units).
    // For other types of system unit, the dimensional transform is assumed to be the identity.
    private static UnitConverter getDimensionalTransform(Unit systemUnit, PhysicsModel model) throws IncommensurableException,
            UnconvertibleException {
        if (systemUnit instanceof BaseUnit) {
            return model.getDimensionalTransform((BaseUnit)systemUnit);
        } else if (systemUnit instanceof AlternateUnit) {
            return getDimensionalTransform(((AlternateUnit)systemUnit).getParentUnit(), model);
        } else if (systemUnit instanceof ProductUnit) {
            ProductUnit productUnit = (ProductUnit) systemUnit;
            UnitConverter dimensionalTransform = AbstractUnitConverter.IDENTITY;
            for (int i = 0; i < productUnit.getUnitCount(); i++) {
                Unit unit = productUnit.getUnit(i); // A system unit.
                int pow = productUnit.getUnitPow(i);
                int root = productUnit.getUnitRoot(i);
                if (root !=1)
                    throw new UnconvertibleException("Fractional exponents not supported");
                UnitConverter cvtr = getDimensionalTransform(unit, model);
                if (!(cvtr.isLinear()))
                    throw new UnconvertibleException(cvtr.getClass() + " is non-linear, cannot convert product unit");
                if (pow < 0) {
                    pow = -pow;
                    cvtr = cvtr.inverse();
                }
                for (int j=0; j < pow; j++) {
                    dimensionalTransform = dimensionalTransform.concatenate(cvtr);
                }
            }
            return dimensionalTransform;
        } else { // System unit of unknown type. Assume the dimensional transform is the identity.
            return AbstractUnitConverter.IDENTITY;
        }
    }
    

    @Override
    public final PhysicsUnit<?> alternate(String symbol) {
        if (!isSystemUnit()) throw new UnsupportedOperationException(this + " is not a system unit");
        return new AlternateUnit(this, symbol);
    }

    @Override
    public final PhysicsUnit<Q> transform(UnitConverter operation) {
        PhysicsUnit<Q> unit = this;
        UnitConverter cvtr = operation;
        if (this instanceof TransformedUnit<?>) {
            unit = this.getSystemUnit();
            cvtr = this.getConverterToSystemUnit().concatenate(operation);
        }
        if (cvtr.equals(AbstractUnitConverter.IDENTITY))
            return unit;
        return new TransformedUnit<Q>(unit, cvtr);
    }

    @Override
    public final PhysicsUnit<Q> add(double offset) {
        if (offset == 0)
            return this;
        return transform(new AddConverter(offset));
    }

    @Override
    public final PhysicsUnit<Q> multiply(double factor) {
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
    public final Unit<?> multiply(Unit<?> that) {
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
    public final PhysicsUnit<?> multiply(PhysicsUnit<?> that) {
        if (this.equals(SI.ONE))
            return that;
        if (that.equals(SI.ONE))
            return this;
        return ProductUnit.getProductInstance(this, that);
    }

    /**
     * Returns the inverse of this physical unit.
     *
     * @return <code>1 / this</code>
     */
    @Override
    public final PhysicsUnit<?> inverse() {
        if (this.equals(SI.ONE))
            return this;
        return ProductUnit.getQuotientInstance(SI.ONE, this);
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
    public final PhysicsUnit<Q> divide(double divisor) {
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
    public final Unit<?> divide(Unit<?> that) {
        return this.multiply(that.inverse());
    }

    /**
     * Returns the quotient of this physical unit with the one specified.
     *
     * @param that the physical unit divisor.
     * @return <code>this.multiply(that.inverse())</code>
     */
    public final PhysicsUnit<?> divide(PhysicsUnit<?> that) {
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
    public final PhysicsUnit<?> root(int n) {
        if (n > 0)
            return ProductUnit.getRootInstance(this, n);
        else if (n == 0)
            throw new ArithmeticException("Root's order of zero");
        else // n < 0
            return SI.ONE.divide(this.root(-n));
    }

    /**
     * Returns a unit equals to this unit raised to an exponent.
     *
     * @param n the exponent.
     * @return the result of raising this unit to the exponent.
     */
    @Override
    public final PhysicsUnit<?> pow(int n) {
        if (n > 0)
            return this.multiply(this.pow(n - 1));
        else if (n == 0)
            return SI.ONE;
        else // n < 0
            return SI.ONE.divide(this.pow(-n));
    }


    ////////////////////////////////////////////////////////////////
    // Ensures that sub-classes implements hashCode/equals method.
    ////////////////////////////////////////////////////////////////

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object that);

}