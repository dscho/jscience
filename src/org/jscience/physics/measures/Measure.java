/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.measures;

import java.io.Serializable;

import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.realtime.RealtimeObject;
import javolution.util.FastComparator;
import javolution.util.FastMap;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.structures.Field;

import javax.units.converters.ConversionException;
import javax.units.converters.UnitConverter;
import javax.units.Unit;
import javax.quantities.Dimensionless;
import javax.quantities.Quantity;
import javax.realtime.MemoryArea;

/**
 * <p> This class represents a measurable amount; the nature of the amount
 *     is deduced from the measure {@link #getUnit() unit}; the quality of 
 *     the measure is given by the measurement {@link #getAbsoluteError()
 *     error}.</p>
 *     
 * <p> Measures can be {@link #isExact() exact}, in which case they can be
 *     expressed as an exact <code>long</code> integer in the measure unit.
 *     The framework tries to keep measures exact as much as possible.
 *     For example:[code]
 *        Measure<Length> m = Measure.valueOf(100, FOOT).sqrt().divide(5).to(FOOT);
 *        System.out.println(m);
 *        System.out.println(m.isExact() ? "exact" : "inexact");
 *        System.out.println(m.getExactValue());
 *        > 2 ft
 *        > exact
 *        > 2[/code] 
 *     </p>
 *     
 * <p> Errors (including numeric errors) are calculated using numeric intervals.
 *     It is possible to resolve systems of linear equations involving 
 *     measures (ref. {@link org.jscience.mathematics.vectors.Matrix Matrix}),
 *     even if the system is close to singularity; in which case the error 
 *     associated with some (or all) components of the solution may be large.
 *     </p>
 *     
 * <p> By default, non-exact measure instances are shown using the plus/minus  
 *     character ('±') (see {@link MeasureFormat}). For example, 
 *     <code>"(2.0 ± 0.001) km/s"</code> represents a velocity of 
 *     2 km/s with an absolute error of ± 1 m/s. Exact measurements use an
 *     integer notation (no decimal point, e.g. <code>"2000 m"</code>).</p>
 *     
 * <p> Operations between different measures may or may not be authorized 
 *     based upon the current {@link org.jscience.physics.models.PhysicalModel
 *     PhysicalModel}. For example, adding <code>Measure&lt;Length&gt; and 
 *     <code>Measure&lt;Duration&gt; is not allowed by the 
 *     {@link org.jscience.physics.models.StandardModel StandardModel}, 
 *     but is authorized with the {@link
 *     org.jscience.physics.models.RelativisticModel RelativisticModel}).</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 26, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Measuring">
 *       Wikipedia: Measuring</a>
 */
public final class Measure<Q extends Quantity> extends RealtimeObject implements
        Quantity<Q>, Field<Measure>, Comparable<Measure>, Serializable {

////////////////////////////////////////////////////////////////////////////////
// Note: In the future, Measure might be abstract (with more measure types)   // 
//       We don't provide public constructors, factory methods should be used.//
////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Holds a dimensionless measure of zero (exact).
     */
    public static final Measure<Dimensionless> ZERO = new Measure<Dimensionless>();
    static {
        ZERO._unit = Unit.ONE;
        ZERO._isExact = true;
        ZERO._exactValue = 0L;
        ZERO._minimum = 0;
        ZERO._maximum = 0;        
    }

    /**
     * Holds a dimensionless measure of one (exact).
     */
    public static final Measure<Dimensionless> ONE = new Measure<Dimensionless>();
    static {
        ONE._unit = Unit.ONE;
        ONE._isExact = true;
        ONE._exactValue = 1L;
        ONE._minimum = 1.0;
        ONE._maximum = 1.0;        
    }
    
    /**
     * Holds the default XML representation for measures.
     * This representation consists of a <code>value</code>, 
     * an <code>unit</code> and an optional <code>error</code> attribute 
     * when the measure is not exact.
     * The unit attribute determinates the measurement type. For example:<pre>
     * &lt;Measure value="12" unit="µA"/&gt;</pre>
     * represents an electric current measurement.
     */
    protected static final XmlFormat<Measure> XML = new XmlFormat<Measure>(
            Measure.class) {
        public void format(Measure m, XmlElement xml) {
            if (m._isExact) {
                xml.setAttribute("value", m._exactValue);
            } else {
                xml.setAttribute("value", m.getEstimatedValue());
                xml.setAttribute("error", m.getAbsoluteError());
            }
            xml.setAttribute("unit", m._unit.toString());
        }

        public Measure parse(XmlElement xml) {
            Unit unit = Unit.valueOf(xml.getAttribute("unit"));
            Measure<?> m = Measure.newInstance(unit);
            if (!xml.isAttribute("error")) // Exact.
                return m.setExact(xml.getAttribute("value", 0L));
            m._isExact = false;
            double estimatedValue = xml.getAttribute("value", 0.0);
            double error = xml.getAttribute("error", 0.0);
            m._minimum = estimatedValue - error;
            m._maximum = estimatedValue + error;
            return m;
        }
    };

    /**
     * Returns the exact measure corresponding to the value stated in the 
     * specified unit.
     *
     * @param value the exact value stated in the specified unit.
     * @param unit the unit in which the value is stated.
     * @return the corresponding measure object.
     */
    public static <Q extends Quantity> Measure<Q> valueOf(long value,
            Unit<Q> unit) {
        Measure<Q> m = Measure.newInstance(unit);
        return m.setExact(value);
    }

    /**
     * Returns the measure corresponding to an approximate value 
     * (<code>double</code>) stated in the specified unit; 
     * the precision of the measure is assumed to be the 
     * <code>double</code> precision (64 bits IEEE 754 format).
     *
     * @param value the estimated value (± LSB) stated in the specified unit.
     * @param unit the unit in which the value is stated.
     * @return the corresponding measure object.
     */
    public static <Q extends Quantity> Measure<Q> valueOf(double value,
            Unit<Q> unit) {
        Measure<Q> m = Measure.newInstance(unit);
        m._isExact = false;
        double valInc = value * INCREMENT;
        double valDec = value * DECREMENT;
        m._minimum = (value < 0) ? valInc : valDec;
        m._maximum = (value < 0) ? valDec : valInc;
        return m;
    }

    /**
     * Returns the measure corresponding to the specified approximate value 
     * and measurement error, both stated in the specified unit.
     *
     * @param value the estimated amount (± error) stated in the specified unit.
     * @param error the measurement error (absolute).
     * @param unit the unit in which the amount and the error are stated.
     * @return the corresponding measure object.
     * @throws IllegalArgumentException if <code>error &lt; 0.0</code>
     */
    public static <Q extends Quantity> Measure<Q> valueOf(double value,
            double error, Unit<Q> unit) {
        if (error < 0)
            throw new IllegalArgumentException("error: " + error
                    + " is negative");
        Measure<Q> m = Measure.newInstance(unit);
        double min = value - error;
        double max = value + error;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the measure corresponding to the specified interval stated 
     * in the specified unit.
     *
     * @param  minimum the lower bound for the measure value.
     * @param  maximum the upper bound for the measure value.
     * @param unit the unit for both the minimum and maximum values.
     * @return the corresponding measure object.
     * @throws IllegalArgumentException if <code>minimum &gt; maximum</code>
     */
    public static <Q extends Quantity> Measure<Q> rangeOf(double minimum,
            double maximum, Unit<Q> unit) {
        if (minimum > maximum)
            throw new IllegalArgumentException("minimum: " + minimum
                    + " greater than maximum: " + maximum);
        Measure<Q> m = Measure.newInstance(unit);
        m._isExact = false;
        m._minimum = (minimum < 0) ? minimum * INCREMENT : minimum * DECREMENT;
        m._maximum = (maximum < 0) ? maximum * DECREMENT : maximum * INCREMENT;
        return m;
    }

    /**
     * Returns the measure represented by the specified character sequence.
     *
     * @param csq the character sequence.
     * @return <code>MeasureFormat.getInstance().parse(csq)</code>
     */
    public static Measure<?> valueOf(CharSequence csq) {
        return MeasureFormat.getInstance().parse(csq);
    }

    /**
     * Indicates if this measure is exact.
     */
    private boolean _isExact;

    /**
     * Holds the exact value (when exact) stated in this measure unit.
     */
    private long _exactValue;

    /**
     * Holds the minimum value stated in this measure unit.
     * For inexact measures: _minimum < _maximum 
     */
    private double _minimum;

    /**
     * Holds the maximum value stated in this measure unit.
     * For inexact measures: _maximum > _minimum 
     */
    private double _maximum;

    /**
     * Holds this measure unit. 
     */
    private Unit<Q> _unit;

    /**
     * Indicates if this measure amount is exact. An exact amount is 
     * guarantee exact only when stated in this measure unit
     * (e.g. <code>this.longValue()</code>); stating the amount
     * in any other unit may introduce conversion errors. 
     *
     * @return <code>true</code> if this measure is exact;
     *         <code>false</code> otherwise.
     */
    public boolean isExact() {
        return _isExact;
    }

    /**
     * Returns the unit in which the {@link #getEstimatedValue()
     * estimated value} and {@link #getAbsoluteError() absolute error}
     * are stated.
     *
     * @return the measure unit.
     */
    public Unit<Q> getUnit() {
        return _unit;
    }

    /**
     * Returns the exact value for this measure stated in this measure
     * {@link #getUnit unit}. 
     *
     * @return the exact measure value (<code>long</code>) stated 
     *         in this measure's {@link #getUnit unit}
     * @throws MeasureException if this measure is not {@link #isExact()}
     */
    public long getExactValue() throws MeasureException {
        if (!_isExact)
            throw new MeasureException(
                    "Inexact measures don't have exact values");
        return _exactValue;
    }

    /**
     * Returns the estimated value for this measure stated in this measure
     * {@link #getUnit unit}. 
     *
     * @return the median value of the measure interval.
     */
    public double getEstimatedValue() {
        return (_isExact) ? _exactValue : (_minimum + _maximum) * 0.5;
    }

    /**
     * Returns the lower bound interval value for this measure stated in 
     * this measure unit.
     *
     * @return the minimum value.
     */
    public double getMinimumValue() {
        return _minimum;
    }

    /**
     * Returns the upper bound interval value for this measure stated in 
     * this measure unit.
     *
     * @return the maximum value.
     */
    public double getMaximumValue() {
        return _maximum;
    }

    /**
     * Returns the value by which the{@link #getEstimatedValue() estimated 
     * value} may differ from the true value (all stated in base units).
     *
     * @return the absolute error stated in base units.
     */
    public double getAbsoluteError() {
        return MathLib.abs(_maximum - _minimum) * 0.5;
    }

    /**
     * Returns the percentage by which the estimated amount may differ
     * from the true amount.
     *
     * @return the relative error.
     */
    public double getRelativeError() {
        return (_maximum - _minimum) / (_minimum + _maximum);
    }

    /**
     * Returns the measure equivalent to this measure but stated in the 
     * specified unit. The returned measure may not be exact even if this 
     * measure is exact due to conversion errors. 
     *
     * @param  unit the unit of the measure to be returned.
     * @return a measure equivalent to this measure but stated in the 
     *         specified unit.
     * @throws ConversionException if the current model does not allows for
     *         conversion to the specified unit.
     */
    @SuppressWarnings("unchecked")
    public <R extends Quantity> Measure<R> to(Unit<R> unit) {
        if ((_unit == unit) || this._unit.equals(unit))
            return (Measure<R>) this;
        Measure<R> m = Measure.newInstance(unit);
        UnitConverter cvtr = Measure.converterOf(_unit, unit);
        double min = cvtr.convert(_minimum);
        double max = cvtr.convert(_maximum);
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the opposite of this measure.
     *
     * @return <code>-this</code>.
     */
    public Measure<Q> opposite() {
        Measure<Q> m = Measure.newInstance(_unit);
        if ((_isExact) && (_exactValue != Long.MAX_VALUE))
            return m.setExact(-_exactValue);
        m._isExact = false;
        m._minimum = -_maximum;
        m._maximum = -_minimum;
        return m;
    }

    /**
     * Returns the sum of this measure with the one specified.
     *
     * @param  that the measure to be added.
     * @return <code>this + that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be added.
     */
    @SuppressWarnings("unchecked")
    public Measure<Q> plus(Measure that) throws ConversionException {
        if ((this._unit != that._unit) && !this._unit.equals(that._unit))
            return this.plus(that.to(_unit));
        Measure<Q> m = Measure.newInstance(_unit);
        if (this._isExact && that._isExact) {
            long sumLong = this._exactValue + that._exactValue;
            double sumDouble = ((double) this._exactValue)
                    + ((double) that._exactValue);
            if (sumLong == sumDouble)
                return m.setExact(sumLong);
        }
        double min = this._minimum + that._minimum;
        double max = this._maximum + that._maximum;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the difference of this measure with the one specified.
     *
     * @param  that the measure to be subtracted.
     * @return <code>this - that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be subtracted.
     */
    @SuppressWarnings("unchecked")
    public Measure<Q> minus(Measure that) throws ConversionException {
        if ((this._unit != that._unit) && !this._unit.equals(that._unit))
            return this.minus(that.to(_unit));
        Measure<Q> m = Measure.newInstance(_unit);
        if (this._isExact && that._isExact) {
            long diffLong = this._exactValue - that._exactValue;
            double diffDouble = ((double) this._exactValue)
                    - ((double) that._exactValue);
            if (diffLong == diffDouble)
                return m.setExact(diffLong);
        }
        double min = this._minimum - that._maximum;
        double max = this._maximum - that._minimum;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure scaled by the specified exact factor 
     * (dimensionless).
     *
     * @param  factor the scaling factor.
     * @return <code>this · factor</code>.
     */
    public Measure<Q> times(long factor) {
        Measure<Q> m = Measure.newInstance(_unit);
        if (this._isExact) {
            long productLong = _exactValue * factor;
            double productDouble = ((double) _exactValue) * factor;
            if (productLong == productDouble)
                return m.setExact(productLong);
        }
        m._isExact = false;
        m._minimum = (factor > 0) ? _minimum * factor : _maximum * factor;
        m._maximum = (factor > 0) ? _maximum * factor : _minimum * factor;
        return m;
    }

    /**
     * Returns this measure scaled by the specified approximate factor
     * (dimensionless).
     *
     * @param  factor the scaling factor.
     * @return <code>this · factor</code>.
     */
    public Measure<Q> times(double factor) {
        Measure<Q> m = Measure.newInstance(_unit);
        double min = (factor > 0) ? _minimum * factor : _maximum * factor;
        double max = (factor > 0) ? _maximum * factor : _minimum * factor;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the product of this measure with the one specified.
     *
     * @param  that the measure multiplier.
     * @return <code>this · that</code>.
     */
    @SuppressWarnings("unchecked")
    public Measure<? extends Quantity> times(Measure that) {
        Unit<?> unit = Measure.productOf(this._unit, that._unit);
        if (that._isExact) {
            Measure m = this.times(that._exactValue);
            m._unit = unit;
            return m;
        }
        Measure<Q> m = Measure.newInstance(unit);
        double min, max;
        if (_minimum >= 0) {
            if (that._minimum >= 0) {
                min = _minimum * that._minimum;
                max = _maximum * that._maximum;
            } else if (that._maximum < 0) {
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
            } else if (that._maximum < 0) {
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
            } else if (that._maximum < 0) {
                min = _maximum * that._minimum;
                max = _minimum * that._minimum;
            } else { // Both around zero.
                min = MathLib.min(_minimum * that._maximum, _maximum
                        * that._minimum);
                max = MathLib.max(_minimum * that._minimum, _maximum
                        * that._maximum);
            }
        }
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the multiplicative inverse of this measure.
     * If this measure is possibly zero, then the result is unbounded
     * (]-infinity, +infinity[).
     *
     * @return <code>1 / this</code>.
     */
    public Measure<? extends Quantity> inverse() {
        Measure<? extends Quantity> m = newInstance(Measure.inverseOf(_unit));
        if ((_isExact) && (_exactValue == 1L)) { // Only one exact inverse: one
            m.setExact(1L);
            return m;
        }
        m._isExact = false;
        if ((_minimum <= 0) && (_maximum >= 0)) { // Encompass zero.
            m._minimum = Double.NEGATIVE_INFINITY;
            m._maximum = Double.POSITIVE_INFINITY;
            return m;
        }
        double min = 1.0 / _maximum;
        double max = 1.0 / _minimum;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure divided by the specified exact divisor
     * (dimensionless).
     *
     * @param  divisor the exact divisor.
     * @return <code>this / divisor</code>.
     * @throws ArithmeticException if this measure is exact and the 
     *         specified divisor is zero.
     */
    public Measure<Q> divide(long divisor) {
        Measure<Q> m = Measure.newInstance(_unit);
        if (this._isExact) {
            long quotientLong = _exactValue / divisor;
            double quotientDouble = ((double) _exactValue) / divisor;
            if (quotientLong == quotientDouble)
                return m.setExact(quotientLong);
        }
        double min = (divisor > 0) ? _minimum / divisor : _maximum / divisor;
        double max = (divisor > 0) ? _maximum / divisor : _minimum / divisor;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure divided by the specified approximate divisor
     * (dimensionless).
     *
     * @param  divisor the approximated divisor.
     * @return <code>this / divisor</code>.
     */
    public Measure<Q> divide(double divisor) {
        Measure<Q> m = Measure.newInstance(_unit);
        double min = (divisor > 0) ? _minimum / divisor : _maximum / divisor;
        double max = (divisor > 0) ? _maximum / divisor : _minimum / divisor;
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure divided by the one specified.
     *
     * @param  that the measure divisor.
     * @return <code>this / that</code>.
     */
    @SuppressWarnings("unchecked")
    public Measure<? extends Quantity> divide(Measure that) {
        if (that._isExact) {
            Measure m = this.divide(that._exactValue);
            m._unit = Measure.productOf(this._unit, Measure
                    .inverseOf(that._unit));
            return m;
        }
        return this.times(that.inverse());
    }

    /**
     * Returns the absolute value of this measure.
     *
     * @return  <code>|this|</code>.
     */
    public Measure<Q> abs() {
        return (_isExact) ? ((_exactValue < 0) ? this.opposite() : this)
                : (_minimum >= -_maximum) ? this : this.opposite();
    }

    /**
     * Returns the square root of this measure.
     *
     * @return <code>sqrt(this)</code>
     * 
     */
    public Measure<? extends Quantity> sqrt() {
        Measure<Q> m = Measure.newInstance(_unit.root(2));
        if (this._isExact) {
            double sqrtDouble = MathLib.sqrt(_exactValue);
            long sqrtLong = (long) sqrtDouble;
            if (sqrtLong * sqrtLong == _exactValue)
                return m.setExact(sqrtLong);
        }
        double min = MathLib.sqrt(_minimum);
        double max = MathLib.sqrt(_maximum);
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns the given root of this measure.
     *
     * @param  n the root's order (n != 0).
     * @return the result of taking the given root of this quantity.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public Measure<? extends Quantity> root(int n) {
        if (n == 0)
            throw new ArithmeticException("Root's order of zero");
        if (n < 0)
            return this.root(-n).inverse();
        if (n == 2)
            return this.sqrt();
        Measure<Q> m = Measure.newInstance(_unit.root(n));
        if (this._isExact) {
            double rootDouble = MathLib.pow(_exactValue, 1.0 / n);
            long rootLong = (long) rootDouble;
            long thisLong = rootLong;
            for (int i = 1; i < n; i++) {
                thisLong *= rootLong;
            }
            if (thisLong == _exactValue)
                return m.setExact(rootLong);
        }
        double min = MathLib.pow(_minimum, 1.0 / n);
        double max = MathLib.pow(_maximum, 1.0 / n);
        m._isExact = false;
        m._minimum = (min < 0) ? min * INCREMENT : min * DECREMENT;
        m._maximum = (max < 0) ? max * DECREMENT : max * INCREMENT;
        return m;
    }

    /**
     * Returns this measure raised at the specified exponent.
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     */
    public Measure<? extends Quantity> pow(int exp) {
        if (exp < 0)
            return this.pow(-exp).inverse();
        if (exp == 0)
            return ONE;
        Measure<?> pow2 = this;
        Measure<?> result = null;
        while (exp >= 1) { // Iteration.
            if ((exp & 1) == 1) {
                result = (result == null) ? pow2 : result.times(pow2);
            }
            pow2 = pow2.times(pow2);
            exp >>>= 1;
        }
        return result;
    }

    /**
     * Compares this measure with the one specified regardless of the 
     * units in which this measure and that measure are stated.
     *
     * @param  that the measure to compare with.
     * @return a negative integer, zero, or a positive integer as this measure
     *         is less than, equal to, or greater than that measure.
     * @throws ConversionException if the current model does not allows for
     *         these measure to be compared.
     */
    @SuppressWarnings("unchecked")
    public int compareTo(Measure that) {
        if ((this._unit != that._unit) && !this._unit.equals(that._unit))
            return this.compareTo(that.to(_unit));
        if (this._isExact && that._isExact)
            return this._exactValue < that._exactValue ? -1
                    : this._exactValue > that._exactValue ? 1 : 0;
        double thisValue = this.getEstimatedValue();
        double thatValue = this.getEstimatedValue();
        if (thisValue < thatValue) {
            return -1;
        } else if (thisValue > thatValue) {
            return 1;
        } else {
            long l1 = Double.doubleToLongBits(thisValue);
            long l2 = Double.doubleToLongBits(thatValue);
            return (l1 == l2 ? 0 : (l1 < l2 ? -1 : 1));
        }
    }

    /**
     * Compares this measure against the specified object for strict 
     * equality (same value interval and same units).
     *
     * @param  that the object to compare with.
     * @return <code>true</code> if this measure is identical to that
     *         measure; <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        if (this == that)
            return true;
        if (!(that instanceof Measure))
            return false;
        Measure<?> m = (Measure) that;
        if (!_unit.equals(m._unit))
            return false;
        if (_isExact != m._isExact)
            return false;
        if (_isExact && (this._exactValue == m._exactValue))
            return true;
        if (_minimum != m._minimum)
            return false;
        if (_maximum != m._maximum)
            return false;
        return true;
    }

    /**
     * Returns the hash code for this measure.
     * 
     * @return the hash code value.
     */
    public int hashCode() {
        int h = Float.floatToIntBits((float) _minimum);
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        return h ^ (h >>> 10);
    }

    /**
     * Indicates if this measure is definitively distinct from that 
     * measure. Measures are considered distinct if their value intervals
     * do not overlap.
     *
     * @return <code>true</code> if this measure and that measure are 
     *         distinct; <code>false</code> otherwise.
     */
    public boolean isDistinctFrom(Measure<Q> that) {
        return (this._maximum < that._minimum)
                || (that._maximum < this._minimum);
    }

    /**
     * Indicates if this measure is ordered before that measure
     * (independently of the measure unit).
     *
     * @return <code>this.compareTo(that) < 0</code>.
     */
    public boolean isLessThan(Measure<Q> that) {
        return this.compareTo(that) < 0;
    }

    /**
     * Indicates if this measure is ordered after that measure
     * (independently of the measure unit).
     *
     * @return <code>this.compareTo(that) > 0</code>.
     */
    public boolean isGreaterThan(Measure<Q> that) {
        return this.compareTo(that) > 0;
    }

    /**
     * Compares this measure with that measure ignoring the sign.
     *
     * @return <code>|this| > |that|</code>
     */
    public boolean isLargerThan(Measure<Q> that) {
        return this.abs().isGreaterThan(that.abs());
    }

    /**
     * Returns the text representation of this measure.
     *
     * @return <code>MeasureFormat.getInstance().format(this)</code>
     */
    public Text toText() {
        return MeasureFormat.getInstance().format(this);
    }

    // Implements Quantity.
    public double doubleValue(Unit<Q> unit) {
        return ((_unit == unit) || _unit.equals(unit)) ? this
                .getEstimatedValue() : this.to(unit).getEstimatedValue();
    }

    // Implements Quantity.
    public final long longValue(Unit<Q> unit) {
        if (!_unit.equals(unit))
            return this.to(unit).longValue(unit);
        if (_isExact)
            return _exactValue;
        double doubleValue = this.getEstimatedValue();
        if ((doubleValue >= Long.MIN_VALUE) && (doubleValue <= Long.MAX_VALUE))
            return Math.round(doubleValue);
        throw new ArithmeticException(doubleValue + " " + _unit
                + " cannot be represented as long");
    }

    ///////////////////
    // Lookup tables //
    ///////////////////

    static final FastMap<Unit, FastMap<Unit, Unit>> MULT_LOOKUP = new FastMap<Unit, FastMap<Unit, Unit>>(
            "UNITS_MULT_LOOKUP").setKeyComparator(FastComparator.DIRECT)
            .setShared(true);

    static final FastMap<Unit, Unit> INV_LOOKUP = new FastMap<Unit, Unit>(
            "UNITS_INV_LOOKUP").setKeyComparator(FastComparator.DIRECT)
            .setShared(true);

    static final FastMap<Unit, FastMap<Unit, UnitConverter>> CVTR_LOOKUP = new FastMap<Unit, FastMap<Unit, UnitConverter>>(
            "UNITS_CVTR_LOOKUP").setKeyComparator(FastComparator.DIRECT)
            .setShared(true);

    private static Unit productOf(Unit left, Unit right) {
        FastMap<Unit, Unit> leftTable = MULT_LOOKUP.get(left);
        if (leftTable == null)
            return calculateProductOf(left, right);
        Unit result = leftTable.get(right);
        if (result == null)
            return calculateProductOf(left, right);
        return result;
    }

    private static Unit calculateProductOf(final Unit left, final Unit right) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(MULT_LOOKUP);
        memoryArea.executeInArea(new Runnable() {
            public synchronized void run() {
                FastMap<Unit, Unit> leftTable = MULT_LOOKUP.get(left);
                if (leftTable == null) {
                    leftTable = new FastMap<Unit, Unit>().setKeyComparator(
                            FastComparator.DIRECT).setShared(true);
                    MULT_LOOKUP.put(left, leftTable);
                }
                Unit result = leftTable.get(right);
                if (result == null) {
                    result = left.times(right);
                    leftTable.put(right, result);
                }
            }
        });
        return MULT_LOOKUP.get(left).get(right);
    }

    private static Unit inverseOf(Unit unit) {
        Unit inverse = INV_LOOKUP.get(unit);
        if (inverse == null)
            return calculateInverseOf(unit);
        return inverse;
    }

    private static Unit calculateInverseOf(final Unit unit) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(INV_LOOKUP);
        memoryArea.executeInArea(new Runnable() {
            public synchronized void run() {
                Unit inverse = INV_LOOKUP.get(unit);
                if (inverse == null) {
                    inverse = unit.inverse();
                    INV_LOOKUP.put(unit, inverse);
                }
            }
        });
        return INV_LOOKUP.get(unit);
    }

    private static UnitConverter converterOf(Unit left, Unit right) {
        FastMap<Unit, UnitConverter> leftTable = CVTR_LOOKUP.get(left);
        if (leftTable == null)
            return calculateConverterOf(left, right);
        UnitConverter result = leftTable.get(right);
        if (result == null)
            return calculateConverterOf(left, right);
        return result;
    }

    private static UnitConverter calculateConverterOf(final Unit left,
            final Unit right) {
        MemoryArea memoryArea = MemoryArea.getMemoryArea(CVTR_LOOKUP);
        memoryArea.executeInArea(new Runnable() {
            public synchronized void run() {
                FastMap<Unit, UnitConverter> leftTable = CVTR_LOOKUP.get(left);
                if (leftTable == null) {
                    leftTable = new FastMap<Unit, UnitConverter>()
                            .setKeyComparator(FastComparator.DIRECT).setShared(
                                    true);
                    CVTR_LOOKUP.put(left, leftTable);
                }
                UnitConverter result = leftTable.get(right);
                if (result == null) {
                    result = left.getConverterTo(right);
                    leftTable.put(right, result);
                }
            }
        });
        return CVTR_LOOKUP.get(left).get(right);
    }

    //////////////////////
    // Factory Creation //
    //////////////////////

    @SuppressWarnings("unchecked")
    private static <Q extends Quantity> Measure<Q> newInstance(Unit unit) {
        Measure<Q> measure = FACTORY.object();
        measure._unit = unit;
        return measure;
    }

    private static Factory<Measure> FACTORY = new Factory<Measure>() {

        @Override
        protected Measure create() {
            return new Measure();
        }
    };

    private Measure() {
    }

    private Measure<Q> setExact(long exactValue) {
        _isExact = true;
        _exactValue = exactValue;
        double doubleValue = exactValue;
        if (doubleValue == exactValue) {
            _minimum = doubleValue;
            _maximum = doubleValue;
        } else {
            double valInc = exactValue * INCREMENT;
            double valDec = exactValue * DECREMENT;
            _minimum = (_exactValue < 0) ? valInc : valDec;
            _maximum = (_exactValue < 0) ? valDec : valInc;
        }
        return this;
    }

    static final double DOUBLE_RELATIVE_ERROR = MathLib.pow(2, -53);

    static final double DECREMENT = (1.0 - DOUBLE_RELATIVE_ERROR);

    static final double INCREMENT = (1.0 + DOUBLE_RELATIVE_ERROR);

    private static final long serialVersionUID = 4467570852177749786L;
}