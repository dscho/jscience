/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics.money;

import javolution.lang.Text;

import org.jscience.physics.quantities.Quantity;
import org.jscience.physics.quantities.QuantityFormat;

/**
 * This class represents something generally accepted as a medium of exchange,
 * a measure of value, or a means of payment. The system unit for this quantity
 * is a {@link Currency}.
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 16, 2005
 * @see     Currency
 */
public class Money extends Quantity {

    /**
     * Holds the money factory.
     */
    final static Factory<Money> FACTORY = new Factory<Money>() {
        protected Money create() {
            return new Money();
        }
    };

    /**
     * Default constructor (allows for derivation).
     */
    protected Money() {
    }

    /**
     * Returns the currency of this money quantity
     *
     * @return <code>(Currency) getUnit()</code>.
     */
    public Currency getCurrency() {
        return (Currency) getUnit();
    }

    /**
     * Returns the textual representation of this monetary quantity.
     *  The default number of fraction digits depends on the currency.
     *
     * @return the textual representation of this quantity.
     * @see Currency#getDefaultFractionDigits(Currency)
     */
    public Text toText() {
    	Money m = this.to((Currency)QuantityFormat.getInstance().getOutputUnit(this));
        int fraction = Currency.getDefaultFractionDigits(m.getCurrency());
        if (fraction == 0) {
            long amount = Math.round(getAmount());
            return Text.valueOf(amount).concat(
                    Text.valueOf(' ').concat(getUnit().toText()));
        } else if (fraction == 2) {
            long amount = Math.round(getAmount() * 100);
            return Text.valueOf(amount / 100).concat(Text.valueOf('.')).concat(
                    Text.valueOf((char) ('0' + (amount % 100) / 10)).concat(
                            Text.valueOf((char) ('0' + amount % 10)).concat(
                                    Text.valueOf(' ')).concat(
                                    getUnit().toText())));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Shows {@link Money} instances in the specified currency.
     *
     * @param  currency the output currency for {@link Money} instances.
     */
    public static void showAs(Currency currency) {
        QuantityFormat.show(Money.class, currency);
    }

    private static final long serialVersionUID = 1L;
}