/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.economics.money;

import javolution.realtime.LocalContext;

import org.jscience.physics.quantities.Quantity;
import org.jscience.physics.units.ConversionException;
import org.jscience.physics.units.Unit;




/**
 * This class represents something generally accepted as a medium of exchange,
 * a measure of value, or a means of payment. The system unit for this quantity
 * is a {@link Currency}.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.3, December 19, 2003
 * @see     Currency
 */
public class Money extends Quantity {

    /**
     * Creates the default factory for money quantities.
     */
    static {
        new Factory(Currency.SYSTEM) {
            protected Quantity newQuantity() {
                return new Money();
            }
        };
    }

    /**
     * Represents a {@link Money} amounting to nothing.
     */
    public final static Money ZERO = (Money) valueOf(0, Currency.SYSTEM);

    /**
     * Holds the context key to the output currency.
     */
    private static final LocalContext.Variable OUT_CURRENCY = new LocalContext.Variable();

    /**
     * Default constructor (allows for derivation).
     */
    protected Money() {
    }

    /**
     * Returns the {@link Money} corresponding to the specified quantity.
     *
     * @param  quantity a quantity compatible with {@link Money}.
     * @return the specified quantity or a new {@link Money} instance stated in
     *         the reference currency.
     * @throws ConversionException if the current model does not allow the
     *         specified quantity to be converted to {@link Money}.
     * @see    Currency#getReferenceCurrency
     */
    public static Money moneyOf(Quantity quantity) {
        if (quantity instanceof Money) {
            return (Money) quantity;
        } else {
            Factory factory = Factory.getInstance(Currency
                    .getReferenceCurrency());
            return (Money) factory.quantity(quantity);
        }
    }

    /**
     * Returns the intrinsic {@link Currency} of this {@link Money} quantity
     * (the original currency from which the money has been created from). 
     *
     * @return <code>(Currency) getSystemUnit()</code>.
     */
    public Currency getCurrency() {
        return (Currency) getSystemUnit();
    }

    /**
     * Shows {@link Money} instances in the specified currency.
     *
     * @param  currency the output currency for {@link Money} instances.
     * @see    Quantity#getOutputUnit
     */
    public static void showAs(Currency currency) {
        OUT_CURRENCY.setValue(currency);
    }

    /**
     * Returns the {@link Currency} unit that this {@link Money} quantity is
     * showed as.  The default is this {@link Money}'s system unit (the currency
     * used to create this quantity). This default can be overriden using the
     * context-local {@link #showAs} static method.
     *
     * @return the output currency.
     */
    public Unit getOutputUnit() {
        Currency outCurrency = (Currency) OUT_CURRENCY.getValue();
        return (outCurrency != null) ? outCurrency : getSystemUnit();
    }

    /**
     * Ensures that quantities stated using the specified {@link Currency}
     * are {@link Money} instances.
     *
     * @param  currency the currency to map to a {@link Money} factory.
     */
    static void useFor(Currency currency) {
        new Factory(currency) {
            protected Quantity newQuantity() {
                return new Money();
            }
        };
    }
    
    private static final long serialVersionUID = -444631427695979639L;
}