/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.quantities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javolution.realtime.LocalContext;
import javolution.util.FastMap;
import javolution.util.Text;
import javolution.util.TextBuilder;
import javolution.util.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.matrices.Matrix;
import org.jscience.mathematics.matrices.Operable;
import org.jscience.mathematics.numbers.RealtimeNumber;
import org.jscience.physics.models.PhysicalModel;
import org.jscience.physics.units.ConversionException;
import org.jscience.physics.units.Converter;
import org.jscience.physics.units.Unit;

/**
 * <p> This class represents a measurable amount. The nature of the amount
 *     is deduced from the quantity's unit. The quality of the measurement
 *     is given by the measurement error.</p>
 * <p> Errors (including numeric errors) are calculated using numeric intervals.
 *     It is possible to resolve systems of linear equations involving physical
 *     quantities (e.g. using {@link org.jscience.mathematics.matrices.Matrix}),
 *     even if thesystem is close to singularity. In this latter case the error 
 *     associated with some (or all) components of the solution is potentially
 *     large.</p>
 * <p> The decimal representations of quantities instances are indicative of
 *     their precision as only digits guaranteed to be exact are written out.
 *     For example, the string <code>"2.000 km/s"</code> represents a 
 *     {@link Velocity} of <code>(2.0 ± 0.001) km/s</code>.</p>
 * <p> Finally, operations between quantities may or may not be authorized 
 *     based upon the current {@link PhysicalModel PhysicalModel}
 *     (e.g. adding a {@link Length length} to a {@link Duration duration} 
 *     is not allowed by the {@link org.jscience.physics.models.StandardModel
 *     StandardModel}, but is authorized with the {@link
 *     org.jscience.physics.models.RelativisticModel RelativisticModel}).</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public class Quantity extends RealtimeNumber implements Comparable {

    /**
     * Holds the default XML representation for physical quantities.
     * This representation consists of an <code>amount</code>, 
     * an <code>error</code> and an <code>unit</code> attribute.
     * The unit attribute determinates the quantity type. For example:<pre>
     * &lt;Quantity amount="12.3" error="1.0E-4" unit="µA"/&gt;</pre>
     * returns an {@link ElectricCurrent} instance.
     */
    protected static final XmlFormat QUANTITY_XML = new XmlFormat(Quantity.class) {
        public void format(Object obj, XmlElement xml) {
            Quantity q = (Quantity) obj;
            double min = q._minimum;
            double max = q._maximum;
            Unit outputUnit = q.getOutputUnit();
            if (q._factory._unit != outputUnit) { // Convert.
                Converter cvtr = q._factory._unit.getConverterTo(outputUnit);
                min = cvtr.convert(q._minimum);
                max = cvtr.convert(q._maximum);
            }
            double amount = (min + max) / 2.0;
            double error = Math.abs(max - min) / 2.0;
            xml.setAttribute("amount", amount);
            if (error != 0) {
                TextBuilder tb = TextBuilder.newInstance();
                try {
                    TypeFormat.format(error, 4, tb);
                } catch (IOException e) {
                    throw new Error(); // Should never get there.
                }
                xml.setAttribute("error", tb);
            }
            if (outputUnit != Unit.ONE) {
                xml.setAttribute("unit", outputUnit.toText());
            }
        }

        public Object parse(XmlElement xml) {
            double amount = xml.getAttribute("amount", 0.0);
            double error = xml.getAttribute("error", 0.0);
            CharSequence unitName = xml.getAttribute("unit");
            Unit unit = unitName != null ? Unit.valueOf(unitName) : Unit.ONE;
            return valueOf(amount, error, unit);
        }
    };
    
    /**
     * Holds the relative error due to the inexact representation of
     * <code>double</code> values (64 bits IEEE 754 format).
     */
    static final double DOUBLE_RELATIVE_ERROR = Math.pow(2, -53);

    /**
     * Holds the factor decrementing <code>double</code> values by
     * exactly one LSB.
     */
    static final double DECREMENT = (1.0 - DOUBLE_RELATIVE_ERROR);

    /**
     * Holds the minimum amount stated in system unit (± 1/2 LSB).
     */
    private transient double _minimum;

    /**
     * Holds the maximum amount stated in system unit (± 1/2 LSB).
     */
    private transient double _maximum;

    /**
     * Holds the quantity factory. 
     */
    private transient Factory _factory;

    //////////////////
    // Construction //
    //////////////////

    /**
     * Default constructor.
     */
    protected Quantity() {
    }

    /**
     * Returns the {@link Quantity} from the specified character sequence.
     * The system unit of the specified quantity determinates
     * the class of the quantity being returned. For example:<pre>
     * Quantity.valueOf("1.2 GeV")</pre> returns an {@link Energy} instance.
     *
     * @param  csq the character sequence.
     * @return <code>QuantityFormat.current().parse(csq)</code>
     * @throws IllegalArgumentException if the specified character sequence 
     *         cannot be parsed.
     * @see QuantityFormat
     */
    public static Quantity valueOf(CharSequence csq) {
        return (Quantity) QuantityFormat.current().parse(csq);
    }

    /**
     * Returns the {@link Quantity} of specified amount.
     * The system unit of the specified unit determinates the class of the
     * quantity being returned. For example:<pre>
     * Quantity.valueOf(30, NonSI.FOOT)</pre> returns a {@link Length} instance.
     *
     * @param  amount the estimated amount (± 1/2 LSB).
     * @param  unit the amount's unit.
     * @return the corresponding {@link Quantity}.
     */
    public static Quantity valueOf(double amount, Unit unit) {
        Unit systemUnit = unit.getSystemUnit();
        Converter cvtr = unit.getConverterTo(systemUnit);
        double value = cvtr.convert(amount);
        return Factory.getInstance(systemUnit).rangeApprox(value, value);
    }

    /**
     * Returns the {@link Quantity} of specified amount and measurement error.
     * The system unit of the specified unit determinates the class of the
     * quantity being returned. For example:<pre>
     * Quantity.valueOf(20, 0.1, SI.KILO(SI.HERTZ))</pre> returns
     * a {@link Frequency} instance.
     *
     * @param  amount the estimated amount (± error).
     * @param  error the measurement error (absolute).
     * @param  unit the amount's unit.
     * @return the corresponding {@link Quantity}.
     */
    public static Quantity valueOf(double amount, double error, Unit unit) {
        Unit systemUnit = unit.getSystemUnit();
        Converter cvtr = unit.getConverterTo(systemUnit);
        double value0 = cvtr.convert(amount - error);
        double value1 = cvtr.convert(amount + error);
        return (value0 <= value1) ? Factory.getInstance(systemUnit)
                .rangeApprox(value0, value1) : Factory.getInstance(systemUnit)
                .rangeApprox(value1, value0);
    }


    /**
     * Returns the system unit for this quantity. The system unit identifies
     * the nature of the quantity and can be mapped to quantity's sub-classes
     * for automatic instantiation of the proper quantity class.
     *
     * @return this quantity's system unit.
     */
    public final Unit getSystemUnit() {
        return _factory._unit;
    }

    /**
     * Returns the unit that this quantity is showed as. The default output
     * unit is specified by the current model. This default may be overriden
     * for predefined quantities using the context-local <code>showAs(Unit)
     * </code> static method.
     *
     * @return <code>getModel().unitFor(this)</code>
     * @see    #toString
     */
    public Unit getOutputUnit() {
        Unit ctxUnit = (Unit) _factory.OUTPUT_UNIT.getValue();
        return (ctxUnit != null) ? ctxUnit : PhysicalModel.current().unitFor(
                this);
    }

    /**
     * Returns the value by which the estimated amount may differ from
     * the true amount.
     *
     * @return the absolute error stated in this quantity's system unit.
     */
    public final double getAbsoluteError() {
        return (_maximum - _minimum) / 2.0;
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

    /**
     * Returns the minimum amount for this quantity.
     *
     * @return the minimun amount stated in this quantity's system unit.
     */
    public final double getMinimum() {
        return _minimum;
    }

    /**
     * Returns the maximum amount for this quantity.
     *
     * @return the maximun amount stated in this quantity's system unit.
     */
    public final double getMaximum() {
        return _maximum;
    }

    /**
     * Indicates if this quantity is possibly amounting to nothing.
     *
     * @return  <code>true</code> if this quantity can be zero;
     *          <code>false</code> otherwise.
     */
    public final boolean isPossiblyZero() {
        return (_minimum <= 0) && (_maximum >= 0);
    }

    ////////////////
    // Operations //
    ////////////////

    /**
     * Returns the negation of this quantity.
     *
     * @return <code>-this</code>.
     */
    public final Quantity negate() {
        return _factory.rangeExact(-_maximum, -_minimum);
    }

    /**
     * Returns the sum of this quantity with the one specified.
     * The returned quantity is of the same class that this quantity.
     *
     * @param  that the quantity to be added.
     * @return <code>this + that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be added.
     */
    public final Quantity add(Quantity that) throws ConversionException {
        return (_factory == that._factory) ? _factory.rangeApprox(this._minimum
                + that._minimum, this._maximum + that._maximum) : add(_factory
                .quantity(that));
    }

    /**
     * Returns the difference of this quantity with the one specified.
     * The returned quantity is of the same class that this quantity.
     *
     * @param  that the quantity to be subtracted.
     * @return <code>this - that</code>.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be subtracted.
     */
    public final Quantity subtract(Quantity that) throws ConversionException {
        return (_factory == that._factory) ? _factory.rangeApprox(this._minimum
                - that._maximum, this._maximum - that._minimum)
                : subtract(_factory.quantity(that));
    }

    /**
     * Returns this quantity multiplied by the specified factor.
     *
     * @param  factor the multiplier.
     * @return <code>this * factor</code>.
     */
    public final Quantity multiply(double factor) {
        return (factor > 0) ? _factory.rangeApprox(this._minimum * factor,
                this._maximum * factor) : _factory.rangeApprox(this._maximum
                * factor, this._minimum * factor);
    }

    /**
     * Returns the product of this quantity with the one specified.
     *
     * @param  that the quantity multiplier.
     * @return <code>this * that</code>.
     */
    public final Quantity multiply(Quantity that) {
        double min, max;
        if (_minimum > -_maximum) {
            if (that._minimum > -that._maximum) {
                min = _minimum * that._minimum;
                max = _maximum * that._maximum;
            } else {
                min = _maximum * that._minimum;
                max = _minimum * that._maximum;
            }
        } else {
            if (that._minimum > -that._maximum) {
                min = _minimum * that._maximum;
                max = _maximum * that._minimum;
            } else {
                min = _maximum * that._maximum;
                max = _minimum * that._minimum;
            }
        }
        return _factory.multiply(that._factory).rangeApprox(min, max);
    }

    /**
     * Returns the inverse of this quantity.
     * If this quantity is possbily zero, then the resulting quantity
     * is unbounded.
     *
     * @return <code>1 / this</code>.
     * @see    #isPossiblyZero
     */
    public final Quantity inverse() {
        Factory factory = _factory.inverse();
        return (!isPossiblyZero()) ? factory.rangeApprox(1.0 / _maximum,
                1.0 / _minimum) : factory.rangeExact(Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY); // Unbounded.
    }

    /**
     * Returns this quantity divided by the specified divisor.
     *
     * @param  divisor the divisor.
     * @return <code>this / divisor</code>.
     */
    public final Quantity divide(double divisor) {
        return multiply(1.0 / divisor);
    }

    /**
     * Returns the division of this quantity by the one specified.
     *
     * @param  that the quantity divisor.
     * @return <code>this / that</code>.
     */
    public final Quantity divide(Quantity that) {
        if (!that.isPossiblyZero()) {
            // Inverses that.
            double thatMinimum = 1 / that._maximum;
            double thatMaximum = 1 / that._minimum;
            // Applies multiplication formula.
            // Applies multiplication formula.
            double min, max;
            if (_minimum > -_maximum) {
                if (thatMinimum > -thatMaximum) {
                    min = _minimum * thatMinimum;
                    max = _maximum * thatMaximum;
                } else {
                    min = _maximum * thatMinimum;
                    max = _minimum * thatMaximum;
                }
            } else {
                if (thatMinimum > -thatMaximum) {
                    min = _minimum * thatMaximum;
                    max = _maximum * thatMinimum;
                } else {
                    min = _maximum * thatMaximum;
                    max = _minimum * thatMinimum;
                }
            }
            return _factory.multiply(that._factory.inverse()).rangeApprox(min,
                    max);
        } else {
            return _factory.multiply(that._factory.inverse()).rangeExact(
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
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
            Factory factory = Factory.getInstance(_factory._unit.root(n));
            return factory.rangeApprox(Math.pow(_minimum, 1.0 / n), Math.pow(
                    _maximum, 1.0 / n));
        } else if (n < 0) {
            return root(-n).inverse();
        } else {
            throw new ArithmeticException("Root's order of zero");
        }
    }

    /**
     * Returns the quantity equals to this quantity raised to an exponent.
     *
     * @param  exp the exponent.
     * @return <code>this<sup>exp</sup></code>
     * @throws ArithmeticException if the exponent is a non-integer value and
     *         this quantity is not dimensionless.
     */
    public final Quantity pow(double exp) {
        if (exp >= 0) {
            int n = (int) exp;
            if (n == exp) { // Integer exponent.
                Factory factory = Factory.getInstance(_factory._unit.pow(n));
                return factory.rangeApprox(Math.pow(_minimum, n), Math.pow(
                        _maximum, n));
            } else if (_factory._unit == Unit.ONE) { // Dimensionless.
                Factory factory = Factory.getInstance(Unit.ONE);
                return factory.rangeApprox(Math.pow(_minimum, exp), Math.pow(
                        _maximum, exp));
            } else {
                throw new java.lang.ArithmeticException(
                        "Non-integer exponent for " + _factory._unit + " unit");
            }
        } else {
            return pow(-exp).inverse();
        }
    }

    /**
     * Returns the absolute value of this quantity.
     *
     * @return  <code>abs(this)</code>.
     */
    public final Quantity abs() {
        return (_minimum >= -_maximum) ? this : this.negate();
    }

    /**
     * Returns a random but possible value for this quantity.
     *
     * @return a double value r such as
     *         <code>this.getMinimum{} &lt; r &gt; this.getMaximum()</code>.
     */
    public final double random() {
        return _minimum + Math.random() * (_maximum - _minimum);
    }

    //////////////////////
    // General Contract //
    //////////////////////

    /**
     * Indicates if this quantity is strictly equals to the object specified.
     *
     * <p> Note: Unlike {@link #approxEquals}, this method does not take into
     *           account possible errors (e.g. numeric errors).</p>
     *
     * @param  obj the object to compare with.
     * @return <code>true</code> if this quantity and the specified object
     *         represent the exact same quantity; <code>false</code> otherwise.
     */
    public final boolean equals(Object obj) {
        if (obj instanceof Quantity) {
            Quantity that = (Quantity) obj;
            if (this._factory == that._factory) {
                return (this._minimum == that._minimum)
                        && (this._maximum == that._maximum);
            } else if (that._factory._unit.isCompatible(_factory._unit)) {
                Converter cvtr = that._factory._unit
                        .getConverterTo(_factory._unit);
                double thatMin = cvtr.convert(that._minimum);
                double thatMax = cvtr.convert(that._maximum);
                return (this._minimum == thatMin) && (this._maximum == thatMax);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code value for this quantity.
     *
     * @return this quantity hash code value.
     * @see    #equals
     */
    public final int hashCode() {
        long bits = Double.doubleToLongBits(_minimum);
        return (int) (bits ^ (bits >>> 32));
    }

    /**
     * Indicates if this quantity is approximately equals to the specified
     * quantity. This method takes into account possible errors (e.g. numeric
     * errors) to make this determination.
     *
     * @param  that the quantity to compare with.
     * @return <code>this  &ape; that</code>.
     */
    public final boolean approxEquals(Quantity that) {
        if (this._factory == that._factory) {
            return (this._maximum >= that._minimum)
                    && (this._minimum <= that._maximum);
        } else if (that._factory._unit.isCompatible(_factory._unit)) {
            Converter cvtr = that._factory._unit.getConverterTo(_factory._unit);
            double thatMin = cvtr.convert(that._minimum);
            double thatMax = cvtr.convert(that._maximum);
            return (this._maximum >= thatMin) && (this._minimum <= thatMax);
        } else {
            return false;
        }
    }

    /**
     * Indicates if the {@link Quantity} elements of the specified
     * matrices are approximately equals.
     *
     * @param  left the first matrix to compare.
     * @param  right the second matrix to compare.
     * @return <code>true</code> if both matrices have same dimension and
     *         their quantity elements are approximately equals;
     *         <code>false</code> otherwise.
     * @throws ClassCastException if the specified matrices are not composed of
     *         {@link Quantity} elements.
     * @see    #approxEquals
     */
    public static final boolean approxEquals(Matrix left, Matrix right) {
        if ((left.getRowDimension() == right.getRowDimension())
                && (left.getColumnDimension() == right.getColumnDimension())) {
            for (int i = 0; i < left.getRowDimension(); i++) {
                for (int j = 0; j < left.getColumnDimension(); j++) {
                    Quantity qLeft = (Quantity) left.get(i, j);
                    Quantity qRight = (Quantity) right.get(i, j);
                    if (!qLeft.approxEquals(qRight)) { return false; }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p> Note: This comparator imposes orderings that are inconsistent
     *     with equals. In particular, <code>this.compareTo(that) == 0</code>
     *     does not imply <code>this.equals(that)</code> (but the reverse
     *     is true).</p>
     *
     * @param  obj the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     *	       is less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     * @throws ConversionException if the current model does not allows for
     *         these quantities to be compared.
     * @see    org.jscience.physics.units.Unit#isCompatible
     */
    public final int compareTo(Object obj) {
        Quantity that = (Quantity) obj;
        if (this._factory == that._factory) {
            return compareDouble(this._minimum + this._maximum, that._minimum
                    + that._maximum);
        } else if (that._factory._unit.isCompatible(_factory._unit)) {
            Converter cvtr = that._factory._unit.getConverterTo(_factory._unit);
            double thatMin = cvtr.convert(that._minimum);
            double thatMax = cvtr.convert(that._maximum);
            return compareDouble(this._minimum + this._maximum, thatMin
                    + thatMax);
        } else {
            throw new ConversionException("Cannot compare quantity in "
                    + this._factory._unit + " with quantity in "
                    + that._factory._unit);
        }
    }

    private static int compareDouble(double d1, double d2) {
        if (d1 < d2) {
            return -1;
        } else if (d1 > d2) {
            return 1;
        } else {
            long l1 = Double.doubleToLongBits(d1);
            long l2 = Double.doubleToLongBits(d2);
            return (l1 == l2 ? 0 : (l1 < l2 ? -1 : 1));
        }
    }

    /**
     * Returns the textual representation of this quantity using the 
     * local quantity format.
     *
     * @return <code>toText(getOutputUnit())</code>
     * @see    #toText(Unit)
     */
    public final Text toText() {
        return toText(getOutputUnit());
    }

    /**
     * Returns the textual representation of this quantity stated in the
     * specified unit.
     *
     * @param  unit the output unit this quantity is showed as.
     * @return the text representation using the local format.
     * @see QuantityFormat
     */
    public Text toText(Unit unit) {
        try {
            TextBuilder tb = TextBuilder.newInstance();
            QuantityFormat.current().format(this, unit, tb);
            return tb.toText();
        } catch (IOException ioError) {
            throw new InternalError(); // Should never get there.
        }
    }

    /**
     * Requires special handling during serialization process.
     *
     * @param  stream the object output stream.
     * @throws IOException if an I/O error occurs.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeDouble(_minimum);
        stream.writeDouble(_maximum);
        stream.writeObject(_factory._unit);
    }

    /**
     * Requires special handling during de-serialization process.
     *
     * @param  stream the object input stream.
     * @throws IOException if an I/O error occurs.
     * @throws ClassNotFoundException if the class for the object de-serialized
     *         is not found.
     */
    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        _minimum = stream.readDouble();
        _maximum = stream.readDouble();
        Unit unit = (Unit) stream.readObject();
        _factory = Factory.getInstance(unit);
    }

    /////////////////////////////////
    // Numeric Values Calculations //
    /////////////////////////////////

    /**
     * Returns the estimated value stated in this quantity's system unit as a
     * <code>int</code>. This may involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion
     *         to type <code>int</code>.
     */
    public final int intValue() {
        return (int) Math.round(doubleValue());
    }

    /**
     * Returns the estimated value stated in this quantity's system unit as a
     * <code>long</code>. This may involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion
     *         to type <code>long</code>.
     */
    public final long longValue() {
        return Math.round(doubleValue());
    }

    /**
     * Returns the estimated value stated in this quantity's system unit as a
     * <code>float</code>. This may involve rounding.
     *
     * @return the numeric value represented by this object after conversion
     *         to type <code>float</code>.
     */
    public final float floatValue() {
        return (float) doubleValue();
    }

    /**
     * Returns the estimated value stated in this quantity's system unit as a
     * <code>double</code>.
     *
     * @return the numeric value represented by this object after conversion
     *         to type <code>double</code>.
     */
    public final double doubleValue() {
        return (_minimum + _maximum) / 2.0;
    }

    /**
     * Returns the estimated value of this quantity stated in the specified
     * unit as a <code>int</code>. This may involve rounding or truncation.
     *
     * @param  unit the unit for the returned value.
     * @return the numeric value represented by this object after conversion
     *         to type <code>int</code>.
     * @throws ConversionException if the current model does not allow this
     *         quantity to be stated in the specified unit.
     */
    public final int intValue(Unit unit) {
        return (int) Math.round(doubleValue(unit));
    }

    /**
     * Returns the estimated value of this quantity stated in the specified
     * unit as a <code>long</code>. This may involve rounding or truncation.
     *
     * @param  unit the unit for the returned value.
     * @return the numeric value represented by this object after conversion
     *         to type <code>long</code>.
     * @throws ConversionException if the current model does not allow this
     *         quantity to be stated in the specified unit.
     */
    public final long longValue(Unit unit) {
        return Math.round(doubleValue(unit));
    }

    /**
     * Returns the estimated value of this quantity stated in the specified
     * unit as a <code>float</code>. This may involve rounding.
     *
     * @param  unit the unit for the returned value.
     * @return the numeric value represented by this object after conversion
     *         to type <code>float</code>.
     * @throws ConversionException if the current model does not allow this
     *         quantity to be stated in the specified unit.
     */
    public final float floatValue(Unit unit) {
        return (float) doubleValue(unit);
    }

    /**
     * Returns the estimated value of this quantity stated in the specified
     * unit as a <code>double</code>.
     *
     * @param  unit the unit for the returned value.
     * @return the numeric value represented by this object after conversion
     *         to type <code>double</code>.
     * @throws ConversionException if the current model does not allow this
     *         quantity to be stated in the specified unit.
     */
    public final double doubleValue(Unit unit) {
        if (_factory._unit == unit) {
            return (_minimum + _maximum) / 2.0;
        } else { // Convert.
            Converter cvtr = _factory._unit.getConverterTo(unit);
            return (cvtr.convert(_minimum) + cvtr.convert(_maximum)) / 2.0;
        }
    }

    // Implements Operable.

    public final Operable plus(Operable that) {
        return this.add((Quantity) that);
    }

    public final Operable opposite() {
        return this.negate();
    }

    public final Operable times(Operable that) {
        return this.multiply((Quantity) that);
    }

    public final Operable reciprocal() {
        return this.inverse();
    }


    /**
     * This inner-class represents the 
     * {@link javolution.realtime.ObjectFactory ObjectFactory} producing
     * {@link Quantity} instances.
     */
    public static class Factory extends RealtimeNumber.Factory {

        /**
         * Holds the unit-to-factory mapping.
         */
        private static final FastMap UNIT_TO_FACTORY = new FastMap(MAX);

        /**
         * Holds the multiplication look-up table.
         */
        private final Factory[] _multTable = new Factory[MAX];

        /**
         * Holds the inverse.
         */
        private Factory _inverse = null;

        /**
         * Holds the unique id for this factory.
         */
        private final int _id;

        /**
         * Holds the system unit of the quantities produced by this factory.
         */
        private final Unit _unit;

        /**
         * Holds the context key to the output unit for quantities from this
         * factory.
         */
        private final LocalContext.Variable OUTPUT_UNIT = new LocalContext.Variable();

        /**
         * Creates a new factory producing quantities whose nature is identified
         * by the specified system unit.
         *
         * @param  systemUnit the system unit for the quantities produced by
         *         this factory.
         * @throws IllegalArgumentException if the specified unit is not a
         *         system unit.
         * @throws IllegalArgumentException if another factory already
         *         produces the same quantities.
         */
        protected Factory(Unit systemUnit) {
            if (systemUnit.isSystemUnit()) {
                synchronized (UNIT_TO_FACTORY) {
                    if (UNIT_TO_FACTORY.get(systemUnit) == null) {
                        _unit = systemUnit;
                        _id = newId();
                        this.useFor(systemUnit);
                    } else {
                        throw new IllegalArgumentException(
                                "systemUnit: "
                                        + systemUnit
                                        + " another factory is already mapped to this unit");
                    }
                }
            } else {
                throw new IllegalArgumentException("systemUnit: " + systemUnit
                        + " is not a system unit");
            }
        }

        private static synchronized int newId() {
            return count++;
        }

        private static int count;

        /**
         * Returns the {@link Quantity.Factory} producing quantities identified
         * by the specified unit.
         *
         * @param  unit the unit identifying the factory to return.
         * @return an existing factory or a new one.
         */
        public static Factory getInstance(Unit unit) {
            Unit systemUnit = unit.getSystemUnit();
            synchronized (UNIT_TO_FACTORY) {
                Factory factory = (Factory) UNIT_TO_FACTORY.get(systemUnit);
                return (factory != null) ? factory : new Factory(systemUnit);
            }
        }

        /**
         * Returns the system unit for the {@link Quantity} instances created
         * by this {@link Quantity.Factory}.
         *
         * @return the system unit for this factory.
         */
        public final Unit getUnit() {
            return _unit;
        }

        /**
         * Maps the specified unit to this {@link Quantity.Factory}.
         * This method  allows for additional units to be mapped to this
         * {@link Quantity.Factory}. For example:<pre>
         *     Factory.getInstance(Unit.valueOf("1/s")).useFor(SI.HERTZ);</pre>
         * Typically, such additional mapping is the responsibility of the
         * physical models 
         * (e.g. {@link org.jscience.physics.models.StandardModel StandardModel}
         * for the mapping of SI derived units).
         *
         * @param  unit the unit being mapped to this {@link Quantity.Factory}.
         * @return this;
         * @throws IllegalArgumentException if this factory's unit and
         *         the specified unit are not compatible.
         */
        public Factory useFor(Unit unit) {
            if (unit.isCompatible(_unit)) {
                synchronized (UNIT_TO_FACTORY) {
                    UNIT_TO_FACTORY.put(unit.getSystemUnit(), this);
                }
                return this;
            } else {
                throw new IllegalArgumentException("unit: " + unit
                        + " is not compatible with " + _unit);
            }
        }

        /**
         * Shows instances of this factory in the specified output unit
         * (context-local). This method overrides the default output unit
         * specified by the current model.
         *
         * @param  outputUnit the output unit for instances of this
         *         {@link Quantity.Factory}.
         * @see    PhysicalModel#current
         * @see    javolution.realtime.LocalContext
         */
        public void showInstancesAs(Unit outputUnit) {
            OUTPUT_UNIT.setValue(outputUnit);
        }

        /**
         * This method should be overriden by sub-classes to return a new
         * instance allocated on the heap.
         *
         * <p><i> Note: This method should never be called directly.
         *              Allocation of a new quantity amounting to nothing
         *              should be performed using {@link #create}.</i></p>
         *
         * @return a new instance (not initialized).
         */
        protected Quantity newQuantity() {
            return new Quantity();
        }

        /**
         * Returns a new quantity product of this factory.
         *
         * @return a new quantity allocated from the heap.
         */
        public final Object create() {
            Quantity q = newQuantity();
            q._factory = this;
            return q;
        }

        /**
         * Returns an instance of this factory equivalent to the specified
         * quantity.
         *
         * @param  quantity the quantity reference.
         * @return the specified quantity instance or a quantity from this
         *         factory equivalent to the specified quantity.
         * @throws ConversionException if the current model does not allow the
         *         specified quantity to be stated in this factory's system
         *         unit.
         */
        public final Quantity quantity(Quantity quantity) {
            if (quantity._factory == this) {
                return quantity;
            } else { // Convert.
                Converter cvtr = quantity._factory._unit.getConverterTo(_unit);
                Quantity result = (Quantity) object();
                result._minimum = cvtr.convert(quantity._minimum);
                result._maximum = cvtr.convert(quantity._maximum);
                return result;
            }
        }

        /**
         * Returns the quantity corresponding to the specified exact range.
         *
         * @param  min the minimum amount stated in this factory system unit.
         * @param  max the maximum amount stated in this factory system unit.
         * @return the corresponding quantity instance of this factory.
         */
        public final Quantity rangeExact(double min, double max) {
            Quantity result = (Quantity) object();
            result._minimum = min;
            result._maximum = max;
            return result;
        }

        /**
         * Returns the quantity corresponding to the specified approximate
         * range.
         *
         * @param  min the minimum amount stated in this factory system unit.
         * @param  max the maximum amount stated in this factory system unit.
         * @return the corresponding quantity instance of this factory.
         */
        public final Quantity rangeApprox(double min, double max) {
            Quantity result = (Quantity) object();
            if (min >= 0) { // max >= 0 as well.
                result._minimum = min * DECREMENT;
                result._maximum = max + max * DOUBLE_RELATIVE_ERROR;
                return result;
            } else if (max <= 0) { // min <= 0 as well.
                result._minimum = min + min * DOUBLE_RELATIVE_ERROR;
                result._maximum = max * DECREMENT;
                return result;
            } else { // min < 0 < max
                result._minimum = min + min * DOUBLE_RELATIVE_ERROR;
                result._maximum = max + max * DOUBLE_RELATIVE_ERROR;
                return result;
            }
        }

        /**
         * Returns the product of this factory with the one specified.
         * It is the factory producing the product of the quantities
         * originating from this factory and the one specified.
         *
         * @param  that the factory multiplier.
         * @return <code>this * that</code>.
         */
        private final Factory multiply(Factory that) {
            return (_multTable[that._id] != null) ? _multTable[that._id]
                    : multiply2(that);
        }

        private final Factory multiply2(Factory that) {
            Factory factory = Factory.getInstance(_unit.multiply(that._unit));
            _multTable[that._id] = factory;
            return factory;
        }

        /**
         * Returns the inverse of this factory.
         * It is the factory producing the inverse of the quantities
         * originating from this factory.
         *
         * @return <code>1 / this</code>.
         */
        private final Factory inverse() {
            return (_inverse != null) ? _inverse : inverse2();
        }

        private final Factory inverse2() {
            _inverse = Factory.getInstance(Unit.ONE.divide(_unit));
            return _inverse;
        }

    }

    // Forces initialization of predefined quantities.
    static {
        org.jscience.JScience.initialize();
    }
}