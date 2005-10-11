/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;

import java.text.DecimalFormat;
import java.text.ParseException;

import javolution.realtime.LocalReference;
import javolution.util.FastMap;
import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.lang.TextFormat;
import javolution.lang.TypeFormat;

import org.jscience.physics.units.Unit;

/**
 * <p> This class provides the interface for formatting and parsing
 *     {@link Quantity quantities}.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 30, 2005
 */
public abstract class QuantityFormat extends TextFormat<Quantity> {

    /**
     * Holds the current (local) class to display unit mapping.
     */
    private static FastMap<Class, LocalReference<Unit>> CLASS_TO_DISPLAY_UNIT = new FastMap<Class, LocalReference<Unit>>();

    /**
     * Holds a quantity format instance for which only digits guaranteed to 
     * be exact are written out. In other words, the error is always on the
     * last digit and less than the last digit weight. For example,
     * <code>"1.34 m"</code> means a length between <code>1.32 m</code> and
     * <code>1.35 m</code>.
     */
    public static final QuantityFormat EXACT_DIGITS_ONLY = new ExactDigitsOnly();

    /**
     * Holds current format.
     */
    private static final LocalReference<QuantityFormat> CURRENT = new LocalReference<QuantityFormat>(
            QuantityFormat.EXACT_DIGITS_ONLY);

    /**
     * Base constructor.
     */
    protected QuantityFormat() {
    }

    /**
     * Returns the {@link javolution.realtime.LocalContext local} quantity 
     * format (default {@link #EXACT_DIGITS_ONLY}).
     *
     * @return the quantity format for the current thread.
     */
    public static QuantityFormat getInstance() {
        return CURRENT.get();
    }

    /**
     * @deprecated
     */
    public static QuantityFormat current() {
        return getInstance();
    }
    
    /**
     * Sets the {@link javolution.realtime.LocalContext local} quantity format.
     *
     * @param format the new format.
     */
    public static void setCurrent(QuantityFormat format) {
        CURRENT.set(format);
    }

    /**
     * Returns the output unit for the specified quantity.
     * 
     * @param q the quantity for which the output unit is returned.
     * @return the output unit.
     */
    public Unit getOutputUnit(Quantity q) {
        LocalReference<Unit> unit = CLASS_TO_DISPLAY_UNIT.get(q.getClass());
        if ((unit != null) && (unit.get() != null))
            return unit.get();
        return q.getUnit();
    }

    /**
     * This class implements the default {@link #EXACT_DIGITS_ONLY} format.
     */
    private static class ExactDigitsOnly extends QuantityFormat {

        /**
         * Default constructor.
         */
        private ExactDigitsOnly() {
        }

        // Implements abstract method.
        public Text format(Quantity q) {
            Unit u = getOutputUnit(q);
            double error = MathLib.abs(q.getRelativeError());
            int digits = (int) -(MathLib.log10(error));
            if (digits <= 0) {
                digits = 1;
            }
            double amount = q.to(u).getAmount();
            boolean scientific = (MathLib.abs(amount) >= 1E6)
                    || (MathLib.abs(amount) < 1E-6);
            boolean showZeros = true;
            return Text.valueOf(amount, digits, scientific, showZeros).concat(
                    Text.valueOf(' ').concat(u.toText()));
        }

        // Implements abstract method.
        public Quantity parse(CharSequence csq, Cursor pos) {
            int sepIndex = TypeFormat.indexOf(" ", csq, pos.getIndex());
            if (sepIndex <= 0) {
                sepIndex = csq.length();
            }
            double amount = TypeFormat.parseDouble(csq.subSequence(pos
                    .getIndex(), sepIndex));
            Unit unit = Unit.valueOf(csq.subSequence(sepIndex, csq.length()));
            pos.setIndex(csq.length());
            return Quantity.valueOf(amount, unit);
        }

    }

    /**
     * This class represents a decimal quantity format based upon a 
     * specific pattern.
     * 
     * @see java.text.DecimalFormat
     */
    public static class Decimal extends QuantityFormat {

        /**
         * Holds the decimal format.
         */
        private final DecimalFormat _decimalFormat;

        /**
         * Creates a DecimalFormat using the given pattern.
         * 
         * @param pattern a pattern string.
         * @throws IllegalArgumentException if the given pattern is invalid.
         * @see java.text.DecimalFormat
         */
        public Decimal(String pattern) {
            _decimalFormat = new DecimalFormat(pattern);
        }

        // Implements abstract method.
        public Text format(Quantity q) {
            Unit outputUnit = getOutputUnit(q);
            double amount = q.to(outputUnit).getAmount();
            String amountStr = _decimalFormat.format(amount);
            return Text.valueOf(amountStr).concat(
                    Text.valueOf(' ').concat(outputUnit.toText()));
        }

        // Implements abstract method.
        public Quantity parse(CharSequence csq, Cursor pos) {
            int sepIndex = TypeFormat.indexOf(" ", csq, pos.getIndex());
            if (sepIndex <= 0) {
                sepIndex = csq.length();
            }
            String amountStr = csq.subSequence(pos.getIndex(), sepIndex)
                    .toString();
            double amount;
            try {
                amount = _decimalFormat.parse(amountStr).doubleValue();
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
            Unit unit = Unit.valueOf(csq.subSequence(sepIndex, csq.length()));
            pos.setIndex(csq.length());
            return Quantity.valueOf(amount, unit);
        }
    }

    /**
     * Shows instances of the specified class using the specified unit.
     *
     * @param clazz the quantity class for which the specified 
     *        output unit has to be used. 
     * @param inUnit the display unit for the specified class instances.
     */
    public static void show(Class< ? extends Quantity> clazz, Unit inUnit) {
        synchronized (CLASS_TO_DISPLAY_UNIT) {
            LocalReference<Unit> displayUnit = CLASS_TO_DISPLAY_UNIT.get(clazz);
            if (displayUnit == null) {
                displayUnit = new LocalReference();
                CLASS_TO_DISPLAY_UNIT.put(clazz, displayUnit);
            }
            displayUnit.set(inUnit);
        }
    }
}