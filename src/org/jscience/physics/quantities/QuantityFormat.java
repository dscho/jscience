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

import javolution.realtime.LocalContext;
import javolution.util.MathLib;
import javolution.lang.TextFormat;
import javolution.lang.TypeFormat;
import javolution.lang.Appendable;

import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.physics.units.Converter;
import org.jscience.physics.units.Unit;
import org.jscience.physics.units.UnitFormat;

/**
 * <p> This class provides the interface for formatting and parsing
 *     {@link Quantity quantities}.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public abstract class QuantityFormat extends TextFormat {

    /**
     * <p> Holds a quantity format instance for which only digits guaranteed to 
     *     be exact are written out. In other words, the error is always on the
     *     last digit and less than the last digit weight. For example,
     *     <code>"1.34 m"</code> means a length between <code>1.32 m</code> and
     *     <code>1.35 m</code>.</p>
     * <p> For {@link org.jscience.economics.money.Money monetary} quantities, the number of 
     *     digits after the decimal point is determinated by the output currency
     *     (see {@link org.jscience.economics.money.Currency#getDefaultFractionDigits
     *     getDefaultFractionDigits}).</p>
     */
    public static final QuantityFormat EXACT_DIGITS_ONLY = new ExactDigitsOnly();

    /**
     * Base constructor.
     */
    protected QuantityFormat() {
    }

    /**
     * Returns the {@link LocalContext local} quantity format (default 
     * {@link #EXACT_DIGITS_ONLY}).
     *
     * @return the quantity format for the current thread.
     */
    public static QuantityFormat current() {
        return (QuantityFormat) FORMAT.getValue();
    }

    private static final LocalContext.Variable FORMAT = new LocalContext.Variable(
            QuantityFormat.EXACT_DIGITS_ONLY);

    /**
     * Sets the {@link LocalContext local} quantity format.
     *
     * @param format the new local/global format.
     */
    public static void setCurrent(QuantityFormat format) {
        FORMAT.setValue(format);
    }

    /**
     * Formats a quantity stated in the specified unit.
     *
     * @param  q the quantity to format.
     * @param  u the output unit for the quantity.
     * @param  a the <code>Appendable</code>.
     * @return the specified <code>Appendable</code>.
     * @throws IOException if an I/O exception occurs.
     */
    public abstract Appendable format(Quantity q, Unit u, Appendable a)
            throws IOException;

    /**
     * Formats a quantity stated in its output unit.
     *
     * @param  obj the quantity to format.
     * @param  dest the <code>Appendable</code> destination.
     * @return the specified <code>Appendable</code>.
     * @throws IOException if an I/O exception occurs.
     * @see Quantity#getOutputUnit()
     */
    public final Appendable format(Object obj, Appendable dest)
            throws IOException {
        Quantity q = (Quantity) obj;
        return format(q, q.getOutputUnit(), dest);
    }

    /**
     * This inner class represents a quantity format for which only 
     * digits guaranteed exact are displayed.
     */
    private static final class ExactDigitsOnly extends QuantityFormat {

        /**
         * Default constructor.
         */
        private ExactDigitsOnly() {
        }

        // Implements abstract method.
        public Appendable format(Quantity q, Unit u, Appendable a)
                throws IOException {
            // Special processing for monetary quantities.
            if (q instanceof Money) {
                Currency currency = (Currency) u;
                int fractionDigits = Currency
                        .getDefaultFractionDigits(currency);
                if (fractionDigits == 0) {
                    TypeFormat.format(q.doubleValue(currency), 1, a);
                    a.append(' ');
                    return UnitFormat.current().format(currency, a);
                } else { // 2 digits.
                    TypeFormat.format(q.doubleValue(currency), 0.01, a);
                    a.append(' ');
                    return UnitFormat.current().format(currency, a);
                }
            }

            double min = q.getMinimum();
            double max = q.getMaximum();
            if (q.getSystemUnit() != u) { // Convert.
                Converter cvtr = q.getSystemUnit().getConverterTo(u);
                min = cvtr.convert(q.getMinimum());
                max = cvtr.convert(q.getMaximum());
            }
            double value = (min + max) / 2.0;
            double error = MathLib.max(Double.MIN_VALUE, MathLib.max(max - min, MathLib
                    .abs(value)
                    * Quantity.DOUBLE_RELATIVE_ERROR) * 10);
            TypeFormat.format(value, error, a);
            a.append(' ');
            return UnitFormat.current().format(u, a);
        }

        // Implements abstract method.
        public Object parse(CharSequence csq, ParsePosition pos) {
            int sepIndex = TypeFormat.indexOf(" ", csq, pos.index);
            if (sepIndex <= 0) {
                sepIndex = csq.length();
            }
            double amount = TypeFormat.parseDouble(csq.subSequence(pos.index,
                    sepIndex));
            Unit unit = Unit.valueOf(csq.subSequence(sepIndex, csq.length()));
            pos.index = csq.length();
            return Quantity.valueOf(amount, unit);
        }

        private static final long serialVersionUID = 8254048145276562600L;
    }
}