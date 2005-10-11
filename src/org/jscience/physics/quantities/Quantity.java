/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;

import javolution.util.FastMap;
import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;
import javolution.realtime.RealtimeObject;

import org.jscience.mathematics.numbers.Number;
import org.jscience.physics.units.ConversionException;
import org.jscience.physics.units.Converter;
import org.jscience.physics.units.Unit;

/**
 * <p> This class represents a measurable amount. The nature of the amount
 *     is deduced from the quantity unit. The quality of the measurement
 *     is given by the measurement error.</p>
 *     
 * <p> Errors (including numeric errors) are calculated using numeric intervals.
 *     It is possible to resolve systems of linear equations involving physical
 *     quantities (e.g. using {@link org.jscience.mathematics.matrices.Matrix}),
 *     even if the system is close to singularity; in which case the error 
 *     associated with some (or all) components of the solution may be large.
 *     </p>
 *     
 * <p> The decimal representations of quantities instances are indicative of
 *     their precision as only digits guaranteed to be exact are written out.
 *     For example, the string <code>"2.000 km/s"</code> represents a 
 *     {@link Velocity} of <code>(2.0 ± 0.001) km/s</code>.</p>
 *     
 * <p> Finally, operations between quantities may or may not be authorized 
 *     based upon the current {@link org.jscience.physics.models.PhysicalModel
 *     PhysicalModel} (e.g. adding a {@link Length length} to a {@link Duration
 *     duration} is not allowed by the 
 *     {@link org.jscience.physics.models.StandardModel StandardModel}, 
 *     but is authorized with the {@link
 *     org.jscience.physics.models.RelativisticModel RelativisticModel}).</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 6, 2005
 */
public class Quantity extends Number<Quantity> {

    /**
     * Holds the default XML representation for physical quantities.
     * This representation consists of an <code>amount</code> and  
     * an <code>unit</code> attribute.
     * The unit attribute determinates the quantity type. For example:<pre>
     * &lt;Quantity amount="12.3" unit="µA"/&gt;</pre>
     * represents an {@link ElectricCurrent} instance.
     */
    private static final XmlFormat<Quantity> XML = new XmlFormat<Quantity>(
            Quantity.class) {
        public void format(Quantity q, XmlElement xml) {
            xml.setAttribute("amount", q.getAmount());
//            xml.newAttribute("error")
//                    .append(q.getAbsoluteError(), 4, false, false);
            if (q._unit != Unit.ONE) {
                xml.setAttribute("unit", q._unit.toText());
            }
        }

        public Quantity parse(XmlElement xml) {
            double amount = xml.getAttribute("amount", 0.0);
//            double error = xml.getAttribute("error", 0.0);
            CharSequence unitName = xml.getAttribute("unit");
            Unit unit = unitName != null ? Unit.valueOf(unitName) : Unit.ONE;
            return Quantity.valueOf(amount, unit);
        }
    };

    /**
     * Holds the relative error due to the inexact representation of
     * <code>double</code> values (64 bits IEEE 754 format).
     */
    static final double DOUBLE_RELATIVE_ERROR = MathLib.pow(2, -53);

    /**
     * Holds the factor decrementing <code>double</code> values by
     * exactly one LSB.
     */
    static final double DECREMENT = (1.0 - DOUBLE_RELATIVE_ERROR);

    /**
     * Holds this quantity unit. 
     */
    private Unit _unit;

    /**
     * Holds the minimum amount stated in this quantity base units
     */
    private double _minimum; 

    /**
     * Holds the maximum amount stated in this quantity base units.
     */
    private double _maximum; 

    //////////////////
    // Construction //
    //////////////////

    /**
     * Default constructor.
     */
    protected Quantity() {
    }

    /**
     * Returns the quantity corresponding  to the specified character sequence.
     * The unit of the specified quantity determinates the class of the quantity
     * being returned. For example:<pre>Quantity.valueOf("1.2 GeV")</pre> 
     * returns an {@link Energy} instance.
     *
     * @param  csq the character sequence.
     * @return <code>QuantityFormat.current().parse(csq)</code>
     * @throws IllegalArgumentException if the specified character sequence 
     *         cannot be parsed.
     * @see QuantityFormat
     */
    public static <Q extends Quantity> Q valueOf(CharSequence csq) {
        return (Q) QuantityFormat.current().parse(csq);
    }

    /**
     * Returns the quantity for the specified amount.
     * The specified unit determinates the class of the quantity being returned.
     * For example:<pre>Quantity.valueOf(30, NonSI.FOOT)</pre> 
     * returns a {@link Length} instance.
     *
     * @param  amount the estimated amount (± 1/2 LSB).
     * @param  unit the amount's unit.
     * @return the corresponding quantity.
     */
    public static <Q extends Quantity> Q valueOf(double amount, Unit<Q> unit) {
        Converter cvtr = unit.toBaseUnits();
        double value = cvtr.convert(amount);
        return (Q) newInstance(unit).setRangeApprox(value, value);
    }

    /**
     * Returns the quantity of specified amount and measurement error.
     * The specified unit determinates the class of the quantity being returned.
     * For example:<pre>Quantity.valueOf(20, 0.1, SI.KILO(SI.HERTZ))</pre>
     * returns a {@link Frequency} instance.
     *
     * @param  amount the estimated amount (± error).
     * @param  error the measurement error (absolute).
     * @param  unit the amount's unit.
     * @return the corresponding quantity.
     */
    public static <Q extends Quantity> Q valueOf(double amount, double error,
            Unit<Q> unit) {
        Converter cvtr = unit.toBaseUnits();
        double min = cvtr.convert(amount - error);
        double max = cvtr.convert(amount + error);
        return (Q) newInstance(unit).setRangeApprox(min, max);
    }

    /**
     * Returns the quantity whose amount is in the specified range.
     *
     * @param  minimum the lower bound for the quantity amount.
     * @param  maximum the upper bound of the quantity amount.
     * @param  unit the bounds unit.
     * @return the corresponding quantity.
     */
    public static <Q extends Quantity> Q rangeOf(double minimum,
            double maximum, Unit<Q> unit) {
        Converter cvtr = unit.toBaseUnits();
        double min = cvtr.convert(minimum);
        double max = cvtr.convert(maximum);
        return (Q) newInstance(unit).setRangeApprox(min, max);
    }

    /**
     * Returns this quantity estimated amount stated in this quantity
     * {@link #getUnit unit}.
     *
     * @return this quantity estimated amount.
     */
    public double getAmount() {
        double baseUnitValue = (_minimum + _maximum) * 0.5;
        Converter cvtr = _unit.toBaseUnits().inverse();
        return cvtr.convert(baseUnitValue);
    }

    /**
     * Returns the unit identifying this quantity type; it is also 
     * the unit in which this quantity is displayed by default.
     *
     * @return the unit for this quantity.
     */
    public Unit getUnit() {
        return _unit;
    }

    /**
     * Returns the minimum value for this quantity stated in base units.
     *
     * @return the minimum value.
     */
    public final double getMinimum() {
        return _minimum;
    }

    /**
     * Returns the maximum value for this quantity stated in base units.
     *
     * @return the maximum amount value.
     */
    public final double getMaximum() {
        return _maximum;
    }

    /**
     * Returns the value by which the {@link #doubleValue estimated value}
     * may differ from the true value (all stated in base units).
     *
     * @return the absolute error stated in base units.
     */
    public final double getAbsoluteError() {
        return MathLib.abs(_maximum - _minimum) / 2.0;
    }

    /**
     * Returns the percentage by which the estimated amount may differ
     * from the true amount.
     *
     * @return the relative error.
     */
    public final double getRelativeError() {
        return (_maximum - _minimum) / (_minimum + _maximum);
    }

    ////////////////
    // Operations //
    ////////////////

    /**
     * Returns the quantity equivalent to this quantity but stated 
     * using the specified unit. This method may return a quantity 
     * of different type than this quantity (the unit determinates 
     * the quantity sub-class). 
     *
     * @param  unit the unit of the quantity being returned.
     * @return a quantity equivalent to this unit but stated using 
     *         a different unit.
     * @throws ConversionException if the current model does not allows for
     *         conversion to the specified unit.
     */
    public final <Q extends Quantity> Q to(Unit<Q> unit) {
        Quantity q = newInstance(unit);
        if (!needConversionTo(unit)) // Most likely.
            return (Q) q.setRangeExact(_minimum, _maximum);
        Converter cvtr = _unit.getBaseUnits().getConverterTo(
                unit.getBaseUnits());
        double min = cvtr.convert(_minimum);
        double max = cvtr.convert(_maximum);
        return (Q) q.setRangeApprox(min, max);
    }

    /**
     * Returns the opposite of this quantity.
     *
     * @return <code>-this</code>.
     */
    public final Quantity opposite() {
        return Quantity.newInstance(_unit).setRangeExact(-_maximum, -_minimum);
    }

    /**
     * Returns the sum of this quantity with the one specified.
     *
     * @param  that the quantity to be added.
     * @return <code>this + that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be added.
     */
    public final Quantity plus(Quantity that) throws ConversionException {
        if (needConversionTo(that._unit))
            return this.plus(that.to(_unit));
        return Quantity.newInstance(_unit).setRangeApprox(
                this._minimum + that._minimum, this._maximum + that._maximum);
    }

    /**
     * Returns the difference of this quantity with the one specified.
     *
     * @param  that the quantity to be subtracted.
     * @return <code>this - that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be subtracted.
     */
    public final Quantity minus(Quantity that) throws ConversionException {
        if (needConversionTo(that._unit))
            return this.minus(that.to(_unit));
        return Quantity.newInstance(_unit).setRangeApprox(
                this._minimum - that._maximum, this._maximum - that._minimum);
    }

    /**
     * Returns this quantity multiplied by the specified factor.
     *
     * @param  factor the multiplier.
     * @return <code>this * factor</code>.
     */
    public final Quantity times(double factor) {
        return (factor > 0) ? Quantity.newInstance(_unit).setRangeApprox(
                this._minimum * factor, this._maximum * factor) : Quantity
                .newInstance(_unit).setRangeApprox(this._maximum * factor,
                        this._minimum * factor);
    }

    /**
     * Returns the product of this quantity with the one specified.
     *
     * @param  that the quantity multiplier.
     * @return <code>this * that</code>.
     */
    public final Quantity times(Quantity that) {
        double min, max;
        if (_minimum >= 0) {
            if (that._minimum >= 0) {
                min = _minimum * that._minimum;
                max = _maximum * that._maximum;
            } else if (that._maximum < 0){
                min = _maximum * that._minimum;
                max = _minimum * that._maximum;
            } else {
                min = _maximum * that._minimum;
                max = _maximum * that._maximum;
            }
        } else if (_maximum < 0) {
            if (that._minimum >= 0) {
                min = _minimum * that._maximum;
                max = _maximum * that._minimum;
            } else if (that._maximum < 0){
                min = _maximum * that._maximum;
                max = _minimum * that._minimum;
            } else {
                min = _minimum * that._maximum;
                max = _minimum * that._minimum;
            }
        } else {
            if (that._minimum >= 0) {
                min = _minimum * that._maximum;
                max = _maximum * that._maximum;
            } else if (that._maximum < 0){
                min = _maximum * that._minimum;
                max = _minimum * that._minimum;
            } else { // Both around zero.
                min = MathLib.min(_minimum * that._maximum, _maximum * that._minimum);
                max = MathLib.max(_minimum * that._minimum, _maximum * that._maximum);
            }
        }
        return newInstance(this._unit.times(that._unit)).setRangeApprox(min,
                max);
    }

    /**
     * Returns the reciprocal of this quantity.
     * If this quantity is possbily zero, then the resulting quantity
     * is unbounded.
     *
     * @return <code>1 / this</code>.
     */
    public final Quantity reciprocal() {
        Quantity q = newInstance(_unit.inverse());
        if ((_minimum <= 0) && (_maximum >= 0)) { // Encompass zero.
            return q.setRangeExact(Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY);
        }
        return q.setRangeApprox(1.0 / _maximum, 1.0 / _minimum);
    }

    /**
     * Returns this quantity divided by the specified divisor.
     *
     * @param  divisor the divisor.
     * @return <code>this / divisor</code>.
     */
    public final Quantity divide(double divisor) {
        return (divisor > 0) ? Quantity.newInstance(_unit).setRangeApprox(
                this._minimum / divisor, this._maximum / divisor) : Quantity
                .newInstance(_unit).setRangeApprox(this._maximum / divisor,
                        this._minimum / divisor);
    }

    /**
     * Returns the square root of this quantity.
     *
     * @return <code>sqrt(this)</code>
     */
    public final Quantity sqrt() {
        return newInstance(_unit.root(2)).setRangeApprox(
                MathLib.sqrt(_minimum), MathLib.sqrt(_maximum));
    }

    /**
     * Returns the given root of this quantity.
     *
     * @param  n the root's order (n &gt;= 0).
     * @return the result of taking the given root of this quantity.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final Quantity root(int n) {
        if (n > 0) {
            Quantity q = newInstance(_unit.root(n));
            return q.setRangeApprox(MathLib.pow(_minimum, 1.0 / n), MathLib
                    .pow(_maximum, 1.0 / n));
        } else if (n < 0) {
            return root(-n).reciprocal();
        } else {
            throw new ArithmeticException("Root's order of zero");
        }
    }

    /**
     * Returns the magnitude (positive) of this quantity.
     *
     * @return  <code>|this|</code>.
     */
    public final Quantity norm() {
        return (_minimum >= -_maximum) ? this : this.opposite();
    }

    /**
     * Compares this quantity amount with that quantity amount ignoring 
     * the sign.
     *
     * @return <code>|this| > |that|</code>
     */
    public final boolean isLargerThan(Quantity that) {
        return MathLib.abs(this._minimum + this._maximum) > MathLib
                .abs(that._minimum + that._maximum);
    }

    //////////////////////
    // General Contract //
    //////////////////////

    /**
     * Indicates if this quantity is strictly equals to the object specified
     * (same amount and same system unit).
     *
     * <p> Note: Unlike {@link #approxEquals}, this method does not take into
     *           account possible errors (e.g. numeric errors).</p>
     *
     * @param  obj the object to compare with.
     * @return <code>true</code> if this quantity and the specified object
     *         represent the exact same quantity; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Quantity))
            return false;
        Quantity that = (Quantity) obj;
        if (this._unit != that._unit)
            return false;
        return (this._minimum == that._minimum)
                && (this._maximum == that._maximum);
    }

    /**
     * Returns a well distributed hash code value for this quantity.
     *
     * @return this quantity hash code value.
     * @see    #equals
     */
    public int hashCode() {
        int h = Float.floatToIntBits((float) _maximum);
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        return h ^ (h >>> 10);
    }

    /**
     * Indicates if this quantity is approximately equals to the specified
     * quantity (regardless of the quantity unit). This method takes into
     * account possible errors (e.g. numeric errors) to make this determination.
     *
     * @param  that the quantity to compare with.
     * @return <code>this  &ape; that</code>.
     */
    public boolean approxEquals(Quantity that) {
        if (!this._unit.isCompatible(that._unit))
            return false;
        Quantity diff = this.minus(that);
        return (diff._minimum <= 0) && (diff._maximum >= 0);
    }

    /**
     * Compares this quantity with the specified quantity  for order. 
     * Returns a negative integer, zero, or a positive integer as this quantity
     * is less than, equal to, or greater than the specified quantity.
     * This method does not require both units to be stated using the same
     * units.
     *
     * @param  that the quantity to be compared.
     * @return a negative integer, zero, or a positive integer as this quantity
     *	       is less than, equal to, or greater than that quantity.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be compared.
     * @see    org.jscience.physics.units.Unit#isCompatible
     */
    public int compareTo(Quantity that) {
        if (!this._unit.isCompatible(that._unit))
            throw new ConversionException("Cannot compare quantity in "
                    + this._unit + " with quantity in " + that._unit);
        double diff = this.minus(that).doubleValue();
        if (diff < 0)
            return -1;
        if (diff > 0)
            return 1;
        if (this.equals(that))
            return 0;
        // Special cases (e.g. NaN), do not return 0 for consistency with equal.
        long rawBits = Double.doubleToLongBits(diff);
        return (rawBits < 0) ? -1 : 1;
    }

    /**
     * Returns the textual representation of this quantity.
     * By default the quantity amount stated in the quantity unit.
     *
     * @return the textual representation of this quantity.
     * @see QuantityFormat
     */
    public Text toText() {
        return QuantityFormat.getInstance().format(this);
    }

    /**
     * Returns the estimated value stated in base units represented as a 
     * <code>long</code>.
     *
     * @return the numeric value after conversion to type <code>long</code>.
     */
    public final long longValue() {
        return MathLib.round(doubleValue());
    }

    /**
     * Returns the estimated value stated in base units represented as a 
     * <code>double</code>.
     *
     * @return the numeric value after conversion to type <code>double</code>.
     */
    public final double doubleValue() {
        return 0.5 * (_minimum + _maximum);
    }

    /**
     * Returns a new quantity instance stated in the specified unit.
     *
     * @param unit the unit identifying the quantity type.
     * @return the corresponding quantity.
     */
    private static Quantity newInstance(Unit unit) {
        Factory< ? extends Quantity> factory = Factory.UNIT_TO_FACTORY
                .get(unit);
        if (factory != null) {
            Quantity q = factory.object();
            q._unit = unit;
            return q;
        }
        return newInstanceCacheMiss(unit);
    }

    private static Quantity newInstanceCacheMiss(Unit unit) {
        Factory< ? extends Quantity> factory = Factory.getInstance(unit);
        Quantity q = factory.object();
        q._unit = unit;
        return q;
    }

    /**
     * Indicates if the base units of this quantity are identical to the 
     * base units to this quantity.
     *
     * @param unit the unit the unit to be compared with.
     * @return <code>true</code> if this quantity and quantity stated in
     *         the specified unit need conversions; <code>false</code>
     *         otherwise.
     */
    private boolean needConversionTo(Unit unit) {
        return (_unit != unit) && (_unit.getBaseUnits() != unit.getBaseUnits());
    }

    /**
     * This inner-class represents the factory producing quantity instances.
     * {@link Quantity} instances.
     */
    public static abstract class Factory<Q extends Quantity> extends
            RealtimeObject.Factory<Q> {

        /**
         * Holds the factory producing general purpose quantities.
         */
        private static final Factory GENERAL = new Factory() {
            protected Object create() {
                return new Quantity();
            }
        };

        /**
         * Holds the quantity factories instances (indexed by system unit).
         */
        private static final FastMap<Unit, Factory< ? extends Quantity>> COLLECTION = new FastMap<Unit, Factory< ? extends Quantity>>(
                256);

        /**
         * Holds the unit to quantity factory mapping (cache).
         */
        private static final FastMap<Unit, Factory< ? extends Quantity>> UNIT_TO_FACTORY = new FastMap<Unit, Factory< ? extends Quantity>>(
                1024);

        /**
         * Default constructor.
         */
        protected Factory() {
        }

        /**
         * Creates a new factory and associates it to the specified unit.
         * 
         * @param unit the unit to which this factory is used for.
         * @see #useFor(Unit)
         */
        protected Factory(Unit<Q> unit) {
            useFor(unit);
        }

        /**
         * Maps the specified unit to this factory. Quantities having the 
         * specified unit will be automatically produced by this factory.
         *
         * @param unit the unit mapped to this factory.
         * @return <code>this</code>
         * @throws IllegalArgumentException if the unit is already 
         *         associated to another factory.
         */
        public Factory<Q> useFor(Unit unit) {
            synchronized (COLLECTION) {
                if (COLLECTION.containsKey(unit))
                    throw new IllegalArgumentException("unit: " + unit
                            + " is already mapped");
                COLLECTION.put(unit, this);
                // Clears the factory cache (set values to null to keep 
                // access unsynchronized).
                for (FastMap.Entry<Unit, Factory< ? extends Quantity>> e = UNIT_TO_FACTORY
                        .headEntry(), end = UNIT_TO_FACTORY.tailEntry(); (e = e
                        .getNextEntry()) != end;) {
                    e.setValue(null);
                }
            }
            return this;
        }

        /**
         * Returns the factory for the specified unit, this method searches 
         * for the factory directly mapped to the specified unit, then for the 
         * factory mapped to the base units of the specified unit. 
         * If none is found, a general purpose factory is returned.
         *
         * @param unit the unit for which the current factory is searched for. 
         * @return the factory producing quantities for the specified unit.
         */
        private static Factory< ? extends Quantity> getInstance(Unit unit) {
            Factory< ? extends Quantity> factory = UNIT_TO_FACTORY.get(unit);
            if (factory != null)
                return factory;
            // Not in the cache.
            while (true) {
                factory = COLLECTION.get(unit);
                if (factory != null) break;
                if (unit.getParentUnit() == unit) {
                    factory = GENERAL;
                    break;
                }
                unit = unit.getParentUnit();
            }
            synchronized (UNIT_TO_FACTORY) {
                UNIT_TO_FACTORY.put(unit, factory);
            }
            return factory;
        }
    }

    private Quantity setRangeExact(double min, double max) {
        _minimum = min;
        _maximum = max;
        return this;
    }

    private Quantity setRangeApprox(double min, double max) {
        if (min >= 0) { // max >= 0 as well.
            _minimum = min * DECREMENT;
            _maximum = max + max * DOUBLE_RELATIVE_ERROR;
        } else if (max <= 0) { // min <= 0 as well.
            _minimum = min + min * DOUBLE_RELATIVE_ERROR;
            _maximum = max * DECREMENT;
        } else { // min < 0 < max
            _minimum = min + min * DOUBLE_RELATIVE_ERROR;
            _maximum = max + max * DOUBLE_RELATIVE_ERROR;
        }
        return this;
    }

    // Forces initialization of predefined quantities.
    static {
        org.jscience.JScience.initialize();
    }
    
    private static final long serialVersionUID = 1L;
}