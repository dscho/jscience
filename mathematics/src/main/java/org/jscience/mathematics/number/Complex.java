/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.mathematics.number;

import java.io.IOException;
import java.math.BigDecimal;


import javolution.lang.MathLib;
import javolution.text.TextFormat;
import javolution.text.TypeFormat;
import javolution.context.ObjectFactory;
import javolution.text.CharSet;
import javolution.text.Cursor;

/**
 * <p> This class represents an immutable complex number.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, November 20, 2009
 * @see <a href="http://en.wikipedia.org/wiki/Complex_number">
 *      Wikipedia: Complex number</a>
 */
public final class Complex extends FieldNumber<Complex> {

    /**
     * Defines the default text format for complex numbers (cartesian form).
     *
     * @see TextFormat#getDefault
     */
    protected static final TextFormat<Complex> TEXT_FORMAT = new TextFormat<Complex>(Complex.class) {

        public Appendable format(Complex complex, Appendable out)
                throws IOException {
            out.append('(');
            TypeFormat.format(complex._real, out);
            if (complex._imaginary < 0.0) {
                out.append(" - ");
                TypeFormat.format(-complex._imaginary, out);
            } else {
                out.append(" + ");
                TypeFormat.format(complex._imaginary, out);
            }
            return out.append("i)");
        }

        public Complex parse(CharSequence csq, Cursor cursor) {
            // Skip parenthesis if any.
            boolean parenthesis = cursor.skip('(', csq);

            // Reads real part.
            double real = TypeFormat.parseDouble(csq, cursor);

            // Reads separator (possibly surrounded by whitespaces).
            cursor.skipAny(CharSet.WHITESPACES, csq);
            char op = cursor.nextChar(csq);
            if ((op != '+') && (op != '-'))
                throw new NumberFormatException("'+' or '-' expected");
            cursor.skipAny(CharSet.WHITESPACES, csq);

            // Reads imaginary part.
            double imaginary = TypeFormat.parseDouble(csq, cursor);
            if (!cursor.skip('i', csq))
                throw new NumberFormatException("'i' expected");

            // Skip closing parenthesis if required..
            if (parenthesis && !cursor.skip(')', csq))
                throw new NumberFormatException("Closing ')' expected");

            return Complex.valueOf(real, op == '-' ? -imaginary : imaginary);
        }
    };

    /**
     * The complex number zero.
     */
    public static final Complex ZERO = new Complex(0.0, 0.0);

    /**
     * The complex number one.
     */
    public static final Complex ONE = new Complex(1.0, 0.0);

    /**
     * The imaginary unit <i><b>i</b></i>.
     */
    public static final Complex I = new Complex(0.0, 1.0);

    /**
     * Holds the factory constructing complex instances.
     */
    private static final ObjectFactory<Complex> FACTORY = new ObjectFactory<Complex>() {

        protected Complex create() {
            return new Complex();
        }
    };

    /**
     * Holds the real component.
     */
    private double _real;

    /**
     * Holds the imaginary component.
     */
    private double _imaginary;

    /**
     * Default constructor.
     */
    private Complex() {
    }

    /**
     * Creates a complex number always on the heap independently from the
     * current {@link javolution.context.AllocatorContext allocator context}.
     * To allow for custom object allocation policies, static factory methods
     * <code>valueOf(...)</code> are recommended.
     * 
     * @param  real the real component of this complex number.
     * @param  imaginary the imaginary component of this complex number.
     */
    public Complex(double real, double imaginary) {
        _real = real;
        _imaginary = imaginary;
    }

    /**
     * Returns the complex number having the specified real and imaginary
     * components.
     *
     * @param  real the real component of this complex number.
     * @param  imaginary the imaginary component of this complex number.
     * @return the corresponding complex number.
     * @see    #getReal
     * @see    #getImaginary
     */
    public static Complex valueOf(double real, double imaginary) {
        Complex c = FACTORY.object();
        c._real = real;
        c._imaginary = imaginary;
        return c;
    }

    /**
     * Returns the complex number for the specified character sequence.
     *
     * @param  csq the character sequence.
     * @return <code>TEXT_FORMAT.parse(csq)</code>.
     * @throws IllegalArgumentException if the character sequence does not 
     *         contain a parsable number.
     * @see #TEXT_FORMAT
     */
    public static Complex valueOf(CharSequence csq) {
        return TEXT_FORMAT.parse(csq);
    }

    /**
     * Indicates if either the real or imaginary component of this complex
     * is infinite.
     *
     * @return  <code>true</code> if this complex is infinite;
     *          <code>false</code> otherwise.
     */
    public boolean isInfinite() {
        return Double.isInfinite(_real) | Double.isInfinite(_imaginary);
    }

    /**
     * Indicates if either the real or imaginary component of this complex
     * is not a number.
     *
     * @return  <code>true</code> if this complex is NaN;
     *          <code>false</code> otherwise.
     */
    public boolean isNaN() {
        return Double.isNaN(_real) | Double.isNaN(_imaginary);
    }

    /**
     * Returns the real component of this complex number.
     *
     * @return the real component.
     */
    public double getReal() {
        return _real;
    }

    /**
     * Returns the imaginary component of this complex number.
     *
     * @return the imaginary component.
     */
    public double getImaginary() {
        return _imaginary;
    }

    /**
     * Returns this complex number multiplied by the specified factor.
     *
     * @param  k the factor multiplier.
     * @return <code>this * k</code>.
     */
    public Complex times(double k) {
        return Complex.valueOf(_real * k, _imaginary * k);
    }

    /**
     * Returns this complex divided by the specified factor.
     *
     * @param  k the factor divisor.
     * @return <code>this / k</code>.
     */
    public Complex divide(double k) {
        return Complex.valueOf(_real / k, _imaginary / k);
    }

    /**
     * Returns the conjugate of this complex number.
     *
     * @return <code>(this.real(), - this.imaginary())</code>.
     */
    public Complex conjugate() {
        return Complex.valueOf(_real, -_imaginary);
    }

    /**
     * Returns the magnitude of this complex number, also referred to
     * as the "modulus" or "length".
     *
     * @return the magnitude of this complex number.
     */
    public double magnitude() {
        return MathLib.sqrt(_real * _real + _imaginary * _imaginary);
    }

    /**
     * Returns the argument of this complex number. It is the angle
     * in radians, measured counter-clockwise from the real axis.
     *
     * @return argument of this complex number.
     */
    public double argument() {
        return MathLib.atan2(_imaginary, _real);
    }

    /**
     * Returns one of the two square root of this complex number.
     *
     * @return <code>sqrt(this)</code>.
     */
    public Complex sqrt() {
        double m = MathLib.sqrt(this.magnitude());
        double a = this.argument() / 2.0;
        return Complex.valueOf(m * MathLib.cos(a), m * MathLib.sin(a));
    }

    /**
     * Returns the exponential number <i>e</i> raised to the power of
     * this complex.
     * Note: <code><i><b>e</b></i><sup><font size=+0><b>PI</b>*<i><b>i
     * </b></i></font></sup> = -1</code>
     *
     * @return  <code>exp(this)</code>.
     */
    public Complex exp() {
        double m = MathLib.exp(this._real);
        return Complex.valueOf(m * MathLib.cos(this._imaginary),
                m * MathLib.sin(this._imaginary));
    }

    /**
     * Returns the principal natural logarithm (base e) of this complex.
     * Note: There are an infinity of solutions.
     *
     * @return  <code>log(this)</code>.
     */
    public Complex log() {
        return Complex.valueOf(MathLib.log(this.magnitude()), this.argument());
    }

    /**
     * Returns this complex raised to the specified power.
     *
     * @param   e the exponent.
     * @return  <code>this**e</code>.
     */
    public Complex pow(double e) {
        double m = MathLib.pow(this.magnitude(), e);
        double a = this.argument() * e;
        return Complex.valueOf(m * MathLib.cos(a), m * MathLib.sin(a));
    }

    /**
     * Returns this complex raised to the power of the specified complex
     * exponent.
     *
     * @param   that the exponent.
     * @return  <code>this**that</code>.
     */
    public Complex pow(Complex that) {
        double r1 = MathLib.log(this.magnitude());
        double i1 = this.argument();
        double r2 = (r1 * that._real) - (i1 * that._imaginary);
        double i2 = (r1 * that._imaginary) + (i1 * that._real);
        double m = MathLib.exp(r2);
        return Complex.valueOf(m * MathLib.cos(i2), m * MathLib.sin(i2));
    }

    /**
     * Indicates if two complexes are "sufficiently" alike to be considered
     * equal.
     *
     * @param  that the complex to compare with.
     * @param  tolerance the maximum magnitude of the difference between
     *         them before they are considered <i>not</i> equal.
     * @return <code>true</code> if they are considered equal;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Complex that, double tolerance) {
        return MathLib.abs(this.minus(that).magnitude()) <= tolerance;
    }

    // Implements abstract class Number.
    public Complex abs() {
        return Complex.valueOf(this.magnitude(), 0);
    }

    // Implements GroupAdditive.
    public Complex opposite() {
        return Complex.valueOf(-_real, -_imaginary);
    }

    // Implements GroupAdditive.
    public Complex plus(Complex that) {
        return Complex.valueOf(this._real + that._real, this._imaginary + that._imaginary);
    }

    @Override
    public Complex minus(Complex that) {
        return Complex.valueOf(this._real - that._real, this._imaginary - that._imaginary);
    }

    @Override
    public Complex times(long n) {
        return this.times((double) n);
    }

    // Implements GroupMultiplicative.
    public Complex times(Complex that) {
        return Complex.valueOf(this._real * that._real - this._imaginary * that._imaginary,
                this._real * that._imaginary + this._imaginary * that._real);
    }

    // Implements GroupMultiplicative.
    public Complex inverse() {
        double tmp = (this._real * this._real) + (this._imaginary * this._imaginary);
        return Complex.valueOf(this._real / tmp, -this._imaginary / tmp);
    }

    @Override
    public Complex divide(long n) {
        return this.divide((double) n);
    }

    @Override
    public Complex divide(Complex that) {
        double tmp = (that._real * that._real) + (that._imaginary * that._imaginary);
        double thatInvReal = that._real / tmp;
        double thatInvImaginary = -that._imaginary / tmp;
        return Complex.valueOf(this._real * thatInvReal - this._imaginary * thatInvImaginary,
                this._real * thatInvImaginary + this._imaginary * thatInvReal);
    }

    @Override
    public Complex pow(int exp) {
        return this.pow((double) exp);
    }

    /**
     * Returns the {@link #getReal real} component of this {@link Complex}
     * number as a <code>long</code>.
     *
     * @return <code>(long) this.getReal()</code>
     */
    public long longValue() {
        return (long) _real;
    }

    /**
     * Returns the {@link #getReal real} component of this {@link Complex}
     * number as a <code>double</code>.
     *
     * @return <code>(double) this.getReal()</code>
     */
    public double doubleValue() {
        return _real;
    }

    /**
     * Returns the {@link #getReal real} component of this {@link Complex}
     * number as a <code>BigDecimal</code>.
     *
     * @return <code>(double) this.getReal()</code>
     */
    public BigDecimal decimalValue() {
        return BigDecimal.valueOf(_real);
    }

    /**
     * Compares two complex numbers, the real components are compared first,
     * then if equal, the imaginary components.
     *
     * @param that the complex number to be compared with.
     * @return -1, 0, 1 based upon the ordering. 
     */
    public int compareTo(Complex that) {
        if (this._real < that._real)
            return -1;
        if (this._real > that._real)
            return 1;
        long l1 = Double.doubleToLongBits(this._real);
        long l2 = Double.doubleToLongBits(that._real);
        if (l1 < l2)
            return -1;
        if (l2 > l1)
            return 1;
        if (this._imaginary < that._imaginary)
            return -1;
        if (this._imaginary > that._imaginary)
            return 1;
        l1 = Double.doubleToLongBits(this._imaginary);
        l2 = Double.doubleToLongBits(that._imaginary);
        if (l1 < l2)
            return -1;
        if (l2 > l1)
            return 1;
        return 0;
    }

    // Implements abstract class Number.
    public Complex copy() {
        return Complex.valueOf(_real, _imaginary);
    }
    private static final long serialVersionUID = 1L;

}
