/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.mathematics.numbers;


import java.io.IOException;

import javolution.util.MathLib;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.lang.TypeFormat;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.jscience.mathematics.matrices.Matrix;
import org.jscience.mathematics.matrices.Operable;

/**
 * <p> This class represents an immutable complex number.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class Complex extends RealtimeNumber {

    /**
     * Holds the default XML representation for complex numbers.
     * This representation consists of a <code>real</code> and 
     * <code>imaginary</code> attribute.
     */
    protected static final XmlFormat COMPLEX_XML = new XmlFormat(Complex.class) {
        public void format(Object obj, XmlElement xml) {
            Complex complex = (Complex) obj;
            xml.setAttribute("real", complex._real);
            xml.setAttribute("imaginary", complex._imaginary);
        }

        public Object parse(XmlElement xml) {
            return Complex.valueOf(
                    xml.getAttribute("real", 0.0),
                    xml.getAttribute("imaginary", 0.0));
        }
    };
    
    /**
     * Holds the factory constructing complex instances.
     */
    private static final Factory FACTORY = new Factory() {
        public Object create() {
            return new Complex();
        }
    };

    /**
     * The complex number zero.
     */
    public static final Complex ZERO = (Complex) valueOf(0.0, 0.0).moveHeap();

    /**
     * The complex number one.
     */
    public static final Complex ONE = (Complex) valueOf(1.0, 0.0).moveHeap();

    /**
     * The imaginary unit <i><b>i</b></i>.
     */
    public static final Complex I = (Complex) valueOf(0.0, 1.0).moveHeap();

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
        Complex c = (Complex) FACTORY.object();
        c._real = real;
        c._imaginary = imaginary;
        return c;
    }

    /**
     * Returns the complex number for the specified character sequence.
     * The character sequence must contain the cartesian form of the
     * complex number to return.
     * For example: <code>"1.0 + 2.0i"</code>, <code>"1.3 - 2.5i"</code>).
     *
     * @param  chars the character sequence.
     * @return the corresponding real number.
     * @throws NumberFormatException if this character sequence does not contain
     *         a parsable complex number.
     */
    public static Complex valueOf(CharSequence chars) {
        Complex c = (Complex) FACTORY.object();
        try {
            // Reads real part.
            int realEnd = TypeFormat.indexOf(" ", chars, 1);
            c._real = TypeFormat.parseDouble(chars.subSequence(1, realEnd));

            // Reads imaginary part.
            boolean negImaginary = false;
            if (chars.charAt(realEnd + 1) != '+') {
                if (chars.charAt(realEnd + 1) != '-') {
                    throw new NumberFormatException("'+' or '-' expected");
                }
                negImaginary = true;
            }
            double imaginary = TypeFormat.parseDouble(chars.subSequence(
                    realEnd + 3, chars.length() - 1));
            c._imaginary = negImaginary ? -imaginary : imaginary;

            if (chars.charAt(chars.length() - 1) != 'i') {
                throw new NumberFormatException("'i' expected");
            }
            return c;
        } catch (IndexOutOfBoundsException e) {
            throw new NumberFormatException("For input characters: \""
                    + chars.toString() + "\"");
        }
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
     * Returns the negation of this complex.
     *
     * @return <code>-this</code>.
     */
    public Complex negate() {
        Complex c = (Complex) FACTORY.object();
        c._real = -this._real;
        c._imaginary = -this._imaginary;
        return c;
    }

    /**
     * Returns the sum of this complex with the one specified.
     *
     * @param  that the complex to be added.
     * @return <code>this + that</code>.
     */
    public Complex add(Complex that) {
        Complex c = (Complex) FACTORY.object();
        c._real = this._real + that._real;
        c._imaginary = this._imaginary + that._imaginary;
        return c;
    }

    /**
     * Returns the difference between this complex and the one specified.
     *
     * @param  that the complex to be subtracted.
     * @return <code>this - that</code>.
     */
    public Complex subtract(Complex that) {
        Complex c = (Complex) FACTORY.object();
        c._real = this._real - that._real;
        c._imaginary = this._imaginary - that._imaginary;
        return c;
    }

    /**
     * Returns this complex multiplied by the specified factor.
     *
     * @param  k the factor multiplier.
     * @return <code>this * k</code>.
     */
    public Complex multiply(double k) {
        Complex c = (Complex) FACTORY.object();
        c._real = this._real * k;
        c._imaginary = this._imaginary * k;
        return c;
    }

    /**
     * Returns the product of this complex with the one specified.
     *
     * @param  that the complex multiplier.
     * @return <code>this * that</code>.
     */
    public Complex multiply(Complex that) {
        Complex c = (Complex) FACTORY.object();
        c._real = this._real * that._real - this._imaginary * that._imaginary;
        c._imaginary = this._real * that._imaginary + this._imaginary
                * that._real;
        return c;
    }

    /**
     * Returns the inverse of this complex.
     *
     * @return <code>1 / this</code>.
     */
    public Complex inverse() {
        Complex c = (Complex) FACTORY.object();
        double tmp = (this._real * this._real)
                + (this._imaginary * this._imaginary);
        c._real = this._real / tmp;
        c._imaginary = -this._imaginary / tmp;
        return c;
    }

    /**
     * Returns this complex divided by the specified factor.
     *
     * @param  k the factor divisor.
     * @return <code>this / k</code>.
     */
    public Complex divide(double k) {
        Complex c = (Complex) FACTORY.object();
        c._real = this._real / k;
        c._imaginary = this._imaginary / k;
        return c;
    }

    /**
     * Returns this complex divided by the specified complex.
     *
     * @param  that the complex divisor.
     * @return <code>this / that</code>.
     */
    public Complex divide(Complex that) {
        return this.multiply(that.inverse());
    }

    /**
     * Returns the conjugate of this complex number.
     *
     * @return <code>(this.real(), - this.imaginary())</code>.
     */
    public Complex conjugate() {
        Complex c = (Complex) FACTORY.object();
        c._real = this._real;
        c._imaginary = -this._imaginary;
        return c;
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
        Complex c = (Complex) FACTORY.object();
        double m = MathLib.sqrt(this.magnitude());
        double a = this.argument() / 2.0;
        c._real = m * MathLib.cos(a);
        c._imaginary = m * MathLib.sin(a);
        return c;
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
        Complex c = (Complex) FACTORY.object();
        double m = MathLib.exp(this._real);
        c._real = m * MathLib.cos(this._imaginary);
        c._imaginary = m * MathLib.sin(this._imaginary);
        return c;
    }

    /**
     * Returns the principal natural logarithm (base e) of this complex.
     * Note: There are an infinity of solutions.
     *
     * @return  <code>log(this)</code>.
     */
    public Complex log() {
        Complex c = (Complex) FACTORY.object();
        c._real = MathLib.log(this.magnitude());
        c._imaginary = this.argument();
        return c;
    }

    /**
     * Returns this complex raised to the specified power.
     *
     * @param   e the exponent.
     * @return  <code>this**e</code>.
     */
    public Complex pow(double e) {
        Complex c = (Complex) FACTORY.object();
        double m = MathLib.pow(this.magnitude(), e);
        double a = this.argument() * e;
        c._real = m * MathLib.cos(a);
        c._imaginary = m * MathLib.sin(a);
        return c;
    }

    /**
     * Returns this complex raised to the power of the specified complex
     * exponent.
     *
     * @param   that the exponent.
     * @return  <code>this**that</code>.
     */
    public Complex pow(Complex that) {
        Complex c = (Complex) FACTORY.object();
        double r1 = MathLib.log(this.magnitude());
        double i1 = this.argument();
        double r2 = (r1 * that._real) - (i1 * that._imaginary);
        double i2 = (r1 * that._imaginary) + (i1 * that._real);
        double m = MathLib.exp(r2);
        c._real = m * MathLib.cos(i2);
        c._imaginary = m * MathLib.sin(i2);
        return c;
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
        return MathLib.abs(this.subtract(that).magnitude()) <= tolerance;
    }

    /**
     * Indicates if two complex matrices are "sufficiently" alike to be
     * considered equal.
     *
     * @param  A the first complex matrix.
     * @param  B the second complex matrix.
     * @param  tolerance the maximum difference between their complex elements
     *         before they are considered <i>not</i> equal.
     * @return <code>true</code> if <code>A</code> and <code>B</code> are
     *         considered equal; <code>false</code> otherwise.
     * @throws ClassCastException if <code>A</code> or <code>B</code> are not
     *         exclusively composed of complex numbers.
     * @see    #equals(Complex, double)
     */
    public static boolean equals(Matrix A, Matrix B, double tolerance) {
        int m = A.getRowDimension();
        int n = B.getColumnDimension();
        if ((B.getRowDimension() == m) && (B.getColumnDimension() == n)) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    Complex a = (Complex) A.get(i, j);
                    Complex b = (Complex) B.get(i, j);
                    if (!a.equals(b, tolerance)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Compares this complex against the specified Object.
     *
     * @param  that the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object that) {
        return (that instanceof Complex)
                && (this._real == ((Complex) that)._real)
                && (this._imaginary == ((Complex) that)._imaginary);
    }

    /**
     * Returns the hash code for this complex number.
     *
     * @return the hash code value.
     */
    public int hashCode() {
        long bits = Double.doubleToLongBits(_real);
        bits ^= Double.doubleToLongBits(_imaginary) * 31;
        return (((int) bits) ^ ((int) (bits >> 32)));
    }

    /**
     * Returns the text representation of this complex number using
     * its cartesian form.
     * For example: <code>"1.0 + 2.0i"</code>, <code>"1.3 - 2.5i"</code>).
     *
     * @return the text representation of this complex number.
     */
    public Text toText() {
        try {
            TextBuilder tb = TextBuilder.newInstance();
            TypeFormat.format(_real, tb);
            if (_imaginary >= 0) {
                tb.append(" + ");
                TypeFormat.format(_imaginary, tb);
            } else {
                tb.append(" - ");
                TypeFormat.format(-_imaginary, tb);
            }
            tb.append('i');
            return tb.toText();
        } catch (IOException ioError) {
            throw new InternalError(); // Should never get there.
        }
    }

    /**
     * Returns the magnitude of this {@link Complex} as an <code>int</code>.
     *
     * @return <code>(int) magnitude()</code>
     */
    public int intValue() {
        return (int) magnitude();
    }

    /**
     * Returns the magnitude of this {@link Complex} as a <code>long</code>.
     *
     * @return <code>(long) magnitude()</code>
     */
    public long longValue() {
        return (long) magnitude();
    }

    /**
     * Returns the magnitude of this {@link Complex} as a <code>float</code>.
     *
     * @return <code>(float) magnitude()</code>
     */
    public float floatValue() {
        return (float) magnitude();
    }

    /**
     * Returns the magnitude of this {@link Complex} as a <code>double</code>.
     *
     * @return <code>magnitude()</code>
     */
    public double doubleValue() {
        return magnitude();
    }

    // Implements Operable.
    public Operable plus(Operable that) {
        return this.add((Complex) that);
    }

    // Implements Operable.
    public Operable opposite() {
        return this.negate();
    }

    // Implements Operable.
    public Operable times(Operable that) {
        return this.multiply((Complex) that);
    }

    // Implements Operable.
    public Operable reciprocal() {
        return this.inverse();
    }

    private static final long serialVersionUID = 3977298824052355641L;
}