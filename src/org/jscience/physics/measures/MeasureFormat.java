/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.measures;

import java.io.IOException;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import javax.units.Unit;
import javax.units.UnitFormat;

import javolution.lang.MathLib;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.lang.TextFormat;
import javolution.lang.TypeFormat;
import javolution.realtime.LocalReference;

/**
 * <p> This class provides the interface for formatting and parsing {@link 
 *     Measure measures} instances.</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, February 21, 2006
 */
public abstract class MeasureFormat extends TextFormat<Measure> {

    /**
     * Holds current format.
     */
    private static final LocalReference<MeasureFormat> CURRENT =
        new LocalReference<MeasureFormat>(new PlusMinusError(2));

    /**
     * Default constructor.
     */
    protected MeasureFormat() {
    }

    /**
     * Returns the current {@link javolution.realtime.LocalContext local}  
     * format (default <code>MeasureFormat.getPlusMinusErrorInstance(2)</code>).
     *
     * @return the context local format.
     * @see #getPlusMinusErrorInstance(int)
     */
    public static MeasureFormat getInstance() {
        return CURRENT.get();
    }

    /**
     * Sets the current {@link javolution.realtime.LocalContext local} format.
     *
     * @param format the new format.
     */
    public static void setInstance(MeasureFormat format) {
        CURRENT.set(format);
    }

    /**
     * Returns a format for which the error (if present) is stated using 
     * the '±' character; for example <code>"(1.34 ± 0.01) m"</code>.
     * This format can be used for formatting as well as for parsing.
     * 
     * @param digitsInError the maximum number of digits in error.
     */
    public static MeasureFormat getPlusMinusErrorInstance(int digitsInError) {
        return new PlusMinusError(digitsInError);
    }

    /**
     * Returns a format for which the digits in error (if present) are 
     * in brackets; for example <code>"1.34[56] m"</code>. This format can be 
     * used for formatting only.
     * 
     * @param digitsInError the maximum number of digits in error.
     */
    public static MeasureFormat getBracketErrorInstance(int digitsInError) {
        return new BracketError(digitsInError);
    }

    /**
     * Returns a format for which only digits guaranteed to be exact are 
     * written out. In other words, the error is always on the
     * last digit and less than the last digit weight. For example,
     * <code>"1.34 m"</code> means a length between <code>1.32 m</code> and
     * <code>1.35 m</code>. This format can be used for formatting only.
     */
    public static MeasureFormat getExactDigitsInstance() {
        return new ExactDigitsOnly();
    }
        
    /**
     * This class represents the plus minus error format.
     */
    private static class PlusMinusError extends MeasureFormat {
        
        /**
         * Holds the number of digits in error.
         */
        private int _errorDigits;

        /**
         * Creates a plus-minus error format having the specified 
         * number of digits in error.
         * 
         * @param errorDigits the number of digits in error.
         */
        private PlusMinusError(int errorDigits) {
            _errorDigits = errorDigits;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Appendable format(Measure arg0, Appendable arg1)
                throws IOException {
            if (arg0.getUnit() instanceof Currency) 
                return formatMoney(arg0, arg1);
            if (arg0.isExact()) {
                TypeFormat.format(arg0.getExactValue(), arg1);
                arg1.append(' ');
                return UnitFormat.getStandardInstance().format(arg0.getUnit(), arg1);
            }
           double value = arg0.getEstimatedValue();
           double relativeError = arg0.getRelativeError();
           int digits = (int) -(MathLib.log10(relativeError));
           if (digits <= 0) {
                digits = 1;
           }
           digits += _errorDigits;
           boolean scientific = (MathLib.abs(value) >= 1E6)
                  || (MathLib.abs(value) < 1E-6);
           boolean showZeros = false;
           arg1.append('(');
           TypeFormat.format(value, digits, scientific, showZeros, arg1);
           arg1.append(" ± ");
           double error = arg0.getAbsoluteError();
           scientific = (MathLib.abs(error) >= 1E6) || (MathLib.abs(error) < 1E-6);
           showZeros = true;
           TypeFormat.format(error, _errorDigits, scientific, showZeros, arg1);
           arg1.append(") ");
           return UnitFormat.getStandardInstance().format(arg0.getUnit(), arg1);
        }

        @Override
        public Measure parse(CharSequence arg0, Cursor arg1) {
            arg1.skip('(', arg0);
            int start = arg1.getIndex();
            long value = TypeFormat.parseLong(arg0, 10, arg1);
            if (arg0.charAt(arg1.getIndex()) == ' ') { // Exact! 
                arg1.skip(' ', arg0);
                Unit<?> unit = UnitFormat.getStandardInstance().parse(arg0, arg1);
                return Measure.valueOf(value, unit);
            }
            arg1.setIndex(start);
            double amount = TypeFormat.parseDouble(arg0, arg1);
            arg1.skip(' ', arg0);
            double error = 0;
            if (arg0.charAt(arg1.getIndex()) == '±') { // Error specified. 
                arg1.skip('±', arg0);
                arg1.skip(' ', arg0);
                error = TypeFormat.parseDouble(arg0, arg1);
            }
            arg1.skip(')', arg0);
            arg1.skip(' ', arg0);
            Unit<?> unit = UnitFormat.getStandardInstance().parse(arg0, arg1);
            return Measure.valueOf(amount, error, unit);
        }
    }

    /**
     * This class represents the bracket error format.
     */
    private static class BracketError extends MeasureFormat {
        
        /**
         * Holds the number of digits in error.
         */
        private int _errorDigits;

        /**
         * Creates a bracket error format having the specified 
         * number of digits in error.
         * 
         * @param bracket the number of digits in error.
         */
        private BracketError(int errorDigits) {
            _errorDigits = errorDigits;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Appendable format(Measure arg0, Appendable arg1)
                throws IOException {
            if (arg0.getUnit() instanceof Currency) 
                return formatMoney(arg0, arg1);
            if (arg0.isExact()) {
                TypeFormat.format(arg0.getExactValue(), arg1);
                arg1.append(' ');
                return UnitFormat.getStandardInstance().format(arg0.getUnit(), arg1);
            }
           double value = arg0.getEstimatedValue();
           double relativeError = arg0.getRelativeError();
           int digits = (int) -(MathLib.log10(relativeError));
           if (digits <= 0) {
                digits = 1;
           }
           digits += _errorDigits;
           boolean scientific = (MathLib.abs(value) >= 1E6)
                  || (MathLib.abs(value) < 1E-6);
           boolean showZeros = true;
           TextBuilder tb = TextBuilder.newInstance();
           TypeFormat.format(value, digits, scientific, showZeros, tb);
           Text txt = tb.toText();
           int i = txt.indexOf('E', 0);
           i = (i < 0) ? txt.length() : i;
           tb.insert(i, "]");
           tb.insert(i - _errorDigits, "[");
           arg1.append(tb);
           arg1.append(' ');
           return UnitFormat.getStandardInstance().format(arg0.getUnit(), arg1);
        }

        @Override
        public Measure parse(CharSequence arg0, Cursor arg1) {
            throw new UnsupportedOperationException(
                    "This format should not be used for parsing " +
                    "(not enough information on the error");
        }
    }
    /**
     * This class represents the exact digits only format.
     */
    private static class ExactDigitsOnly extends MeasureFormat {

        /**
         * Default constructor.
         */
        private ExactDigitsOnly() {
        }

        @SuppressWarnings("unchecked")
        @Override
        public Appendable format(Measure arg0, Appendable arg1)
                throws IOException {
            if (arg0.getUnit() instanceof Currency) 
                return formatMoney(arg0, arg1);
            if (arg0.isExact()) {
                TypeFormat.format(arg0.getExactValue(), arg1);
                arg1.append(' ');
                return UnitFormat.getStandardInstance().format(arg0.getUnit(), arg1);
            }
           double value = arg0.getEstimatedValue();
           double relativeError = arg0.getRelativeError();
                int digits = (int) -(MathLib.log10(relativeError));
                if (digits <= 0) {
                    digits = 1;
                }
                boolean scientific = (MathLib.abs(value) >= 1E6)
                        || (MathLib.abs(value) < 1E-6);
                boolean showZeros = true;
                arg1.append(Text.valueOf(value, digits, scientific, showZeros));
            arg1.append(' ');
            return UnitFormat.getStandardInstance().format(arg0.getUnit(), arg1);
        }

        @Override
        public Measure parse(CharSequence arg0, Cursor arg1) {
            throw new UnsupportedOperationException(
                    "This format should not be used for parsing " +
                    "(not enough information on the error");
        }
    }

    /**
     * Provides custom formatting for money measurements.
     */
    private static Appendable formatMoney(Measure<Money> arg0, Appendable arg1)
        throws IOException {
        Currency currency = (Currency) arg0.getUnit();
        int fraction = currency.getDefaultFractionDigits();
        if (fraction == 0) {
            long amount = arg0.longValue(currency);
            TypeFormat.format(amount, arg1);
        } else if (fraction == 2) {
            long amount = MathLib.round(arg0.doubleValue(arg0.getUnit()) * 100);
            TypeFormat.format(amount / 100, arg1);
            arg1.append('.');
            arg1.append((char) ('0' + (amount % 100) / 10));
            arg1.append((char) ('0' + (amount % 10)));
        } else {
            throw new UnsupportedOperationException();
        }
        arg1.append(' ');
        return UnitFormat.getStandardInstance().format(currency, arg1);
    }

}