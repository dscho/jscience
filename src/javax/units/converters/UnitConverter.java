/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.units.converters;

import java.io.Serializable;

/**
 * <p> This class represents a converter of numeric values.</p>
 * <p> It is not required for sub-classes to be immutable
 *    (e.g. currency converter).</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, January 14, 2006
 */
public abstract class UnitConverter implements Serializable {

    /**
     * Holds the identity converter. This converter does nothing
     * (<code>IDENTITY.convert(x) == x</code>).
     */
    public static final UnitConverter IDENTITY = new Identity();

    /**
     * Default constructor.
     */
    protected UnitConverter() {
    }

    /**
     * Returns the inverse of this converter. If <code>x</code> is a valid
     * value, then <code>x == inverse().convert(convert(x))</code> to within
     * the accuracy of computer arithmetic.
     *
     * @return the inverse of this converter.
     */
    public abstract UnitConverter inverse();

    /**
     * Converts a double value.
     *
     * @param  x the numeric value to convert.
     * @return the converted numeric value.
     * @throws ConversionException if an error occurs during conversion.
     */
    public abstract double convert(double x) throws ConversionException;

    /**
     * Returns this converter derivative for the specified
     * <code>x</code> value. For linear converters, this method returns
     * a constant (the linear factor) for all <code>x</code> values.
     *
     * @param  x the value for which the derivative is calculated.
     * @return the derivative for the specified value.
     */
    public abstract double derivative(double x);

    /**
     * Indicates if this converter is linear. A converter is linear if
     * <code>convert(u + v) == convert(u) + convert(v)</code> and
     * <code>convert(r * u) == r * convert(u)</code>.
     *
     * @return <code>true</code> if this converter is linear;
     *         <code>false</code> otherwise.
     */
    public abstract boolean isLinear();

    /**
     * Indicates whether some other object is "equal to" this converter.
     *
     * @param  obj the reference object with which to compare.
     * @return <code>true</code> if this object is a linear converter and this
     *         object is also a linear converter and both have same
     *         derivatives; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof UnitConverter))
            return false;
        UnitConverter that = (UnitConverter) obj;
        // Check equality to float precision (allows for some inaccuracies)
        return this.isLinear()
                && that.isLinear()
                && (Float.floatToIntBits((float) this.derivative(0)) == Float
                        .floatToIntBits((float) that.derivative(0)));
    }

    /**
     * Returns a hash code value for this converter. Equals object have equal
     * hash codes.
     *
     * @return this converter hash code value.
     * @see    #equals
     */
    public int hashCode() {
        int h = Float.floatToIntBits((float) derivative(0));
        h += ~(h << 9);
        h ^= (h >>> 14);
        h += (h << 4);
        return h ^ (h >>> 10);
    }

    /**
     * Concatenates this converter with another converter. The resulting
     * converter is equivalent to first converting by the specified converter,
     * and then converting by this converter.
     *
     * @param  converter the other converter.
     * @return the concatenation of this converter with the other converter.
     */
    public UnitConverter concatenate(UnitConverter converter) {
        if (converter == IDENTITY) {
            return this;
        } else {
            return new Compound(converter, this);
        }
    }

    /**
     * This inner class represents the identity converter (singleton).
     */
    private static final class Identity extends UnitConverter {

        // Implements abstract method.
        public UnitConverter inverse() {
            return this;
        }

        // Implements abstract method.
        public double convert(double x) {
            return x;
        }

        // Implements abstract method.
        public double derivative(double x) {
            return 1.0;
        }

        // Implements abstract method.
        public boolean isLinear() {
            return true;
        }

        // Overrides.
        public UnitConverter concatenate(UnitConverter converter) {
            return converter;
        }

        private static final long serialVersionUID = 1L;
    }

    /**
     * This inner class represents a compound converter.
     */
    private static final class Compound extends UnitConverter {

        /**
         * Holds the first converter.
         */
        private final UnitConverter _first;

        /**
         * Holds the second converter.
         */
        private final UnitConverter _second;

        /**
         * Creates a compound converter resulting from the combined
         * transformation of the specified converters.
         *
         * @param  first the first converter.
         * @param  second the second converter.
         */
        private Compound(UnitConverter first, UnitConverter second) {
            _first = first;
            _second = second;
        }

        // Implements abstract method.
        public UnitConverter inverse() {
            return new Compound(_second.inverse(), _first.inverse());
        }

        // Implements abstract method.
        public double convert(double x) {
            return _second.convert(_first.convert(x));
        }

        // Implements abstract method.
        public double derivative(double x) {
            return _first.derivative(x) * _second.derivative(_first.convert(x));
        }

        // Implements abstract method.
        public boolean isLinear() {
            return _first.isLinear() && _second.isLinear();
        }

        // Overrides.
        public boolean equals(Object obj) {
            return super.equals(obj)
                    || ((obj instanceof Compound)
                            && ((Compound) obj)._first.equals(_first) && ((Compound) obj)._second
                            .equals(_second));
        }

        private static final long serialVersionUID = 1L;
    }
}