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


import java.util.ArrayList;
import java.util.Locale;

import javolution.realtime.LocalContext;
import javolution.util.FastMap;

import org.jscience.physics.units.BaseUnit;
import org.jscience.physics.units.ConversionException;
import org.jscience.physics.units.Converter;
import org.jscience.physics.units.MultiplyConverter;



/**
 * <p> This class represents a currency {@link org.jscience.physics.units.Unit Unit}.
 *     Currencies are {@link BaseUnit}, conversions between currencies is 
 *     possible if their respective exchange rates have been set.</p>
 * <p> Quantities stated in {@link Currency} are instances of {@link Money}.</p>
 * <p> By default, the label associated to a currency is its ISO-4217 code
 *     (see the <a href="http://www.bsi-global.com/iso4217currency"> ISO 4217
 *     maintenance agency</a> for a table of currency codes). An application may
 *     change this default using the {@link org.jscience.physics.units.UnitFormat#label
 *     UnitFormat.label(...)} static method.
 *     For example:<pre>
 *     UnitFormat.label(Currency.EUR, "€");
 *     UnitFormat.label(Currency.GBP, "£");
 *     UnitFormat.label(Currency.JPY, "¥");
 *     UnitFormat.label(Currency.USD, "$");
 *     </pre></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.3, March 10, 2003
 * @see     #setExchangeRate
 */
public final class Currency extends BaseUnit {

    /**
     * Holds the list of all currencies currently known.
     */
    private static final ArrayList CURRENCIES = new ArrayList();

    /**
     * A generic currency (currency sign "&curren;"), used when the locale
     * currency is unknown.
     */
    public static final Currency SYSTEM =  new Currency("¤");

    /**
     * The Australian Dollar currency unit.
     */
    public static final Currency AUD = (Currency) getInstance("AUD");

    /**
     * The Canadian Dollar currency unit.
     */
    public static final Currency CAD = (Currency) getInstance("CAD");

    /**
     * The China Yan currency.
     */
    public static final Currency CNY = (Currency) getInstance("CNY");

    /**
     * The Euro currency.
     */
    public static final Currency EUR = (Currency) getInstance("EUR");

    /**
     * The British Pound currency.
     */
    public static final Currency GBP = (Currency) getInstance("GBP");

    /**
     * The Japanese Yen currency.
     */
    public static final Currency JPY = (Currency) getInstance("JPY");

    /**
     * The Korean Republic Won currency.
     */
    public static final Currency KRW = (Currency) getInstance("KRW");

    /**
     * The Taiwanese dollar currency.
     */
    public static final Currency TWD = (Currency) getInstance("TWD");

    /**
     * The United State dollar currency.
     */
    public static final Currency USD = (Currency) getInstance("USD");

    /**
     * Holds the context key to the reference currency.
     */
    private static final LocalContext.Variable REF_CURRENCY
        = new LocalContext.Variable();

    /**
     * Holds the locale to currency mapping.
     */
    private static final FastMap COUNTRY_TO_CURRENCY = new FastMap(32);
    static {
        // World.
        COUNTRY_TO_CURRENCY.put("AU", AUD); // Australia
        COUNTRY_TO_CURRENCY.put("CA", CAD); // Canada
        COUNTRY_TO_CURRENCY.put("CN", CNY); // China
        COUNTRY_TO_CURRENCY.put("JP", JPY); // Japan
        COUNTRY_TO_CURRENCY.put("KO", KRW); // Korea
        COUNTRY_TO_CURRENCY.put("TW", TWD); // Taiwan
        COUNTRY_TO_CURRENCY.put("UK", GBP); // United Kingdom
        COUNTRY_TO_CURRENCY.put("US", USD); // United State
        // Europe.
        COUNTRY_TO_CURRENCY.put("AT", EUR); // Austria
        COUNTRY_TO_CURRENCY.put("BE", EUR); // Belgium
        COUNTRY_TO_CURRENCY.put("FI", EUR); // Finland
        COUNTRY_TO_CURRENCY.put("FR", EUR); // France
        COUNTRY_TO_CURRENCY.put("DE", EUR); // Germany
        COUNTRY_TO_CURRENCY.put("GR", EUR); // Greece
        COUNTRY_TO_CURRENCY.put("IE", EUR); // Ireland
        COUNTRY_TO_CURRENCY.put("IT", EUR); // Italy
        COUNTRY_TO_CURRENCY.put("LU", EUR); // Luxemburg
        COUNTRY_TO_CURRENCY.put("NL", EUR); // Netherlands
        COUNTRY_TO_CURRENCY.put("PT", EUR); // Portugal
        COUNTRY_TO_CURRENCY.put("ES", EUR); // Spain
    }

    /**
     * Holds the currency to fraction digits mapping (for digits others than 2).
     */
    private static final FastMap CURRENCY_TO_DIGITS = new FastMap();
    static {
        CURRENCY_TO_DIGITS.put(JPY, new Integer(0));
        CURRENCY_TO_DIGITS.put(KRW, new Integer(0));
    }

    /**
     * Creates a currency with the specified currency code.
     *
     * @param  code the ISO-4217 code of the currency (e.g.
     *         <code>"EUR", "USD", "JPY"</code>).
     */
    private Currency(String code) {
        super(code);
    }

    /**
     * Sets the reference currency (context sensitive). Changing the
     * reference currency clears all the exchange rates previously set.
     *
     * @param  currency the new reference currency.
     * @see    LocalContext
     */
    public static void setReferenceCurrency(Currency currency) {
        synchronized (CURRENCIES) {
            for (int i=0; i < CURRENCIES.size(); i++) {
                Currency curr = (Currency) CURRENCIES.get(i);
                curr.setDimension(curr, Converter.IDENTITY); // Resets.
            }
        }
        REF_CURRENCY.setValue(currency);
    }

    /**
     * Returns the currency used as reference when setting the exchange rate.
     * By default, the reference currency is the currency for the default
     * country locale.
     *
     * @return the reference currency.
     * @see    #setExchangeRate
     * @see    #getInstance(Locale)
     */
    public static Currency getReferenceCurrency() {
        Currency currency = (Currency) REF_CURRENCY.getValue();
        return (currency != null) ?  currency :
                                     getInstance(Locale.getDefault());
    }

    /**
     * Sets the exchange rate of this {@link Currency} relatively to
     * the reference currency. Setting the exchange rate allows
     * for conversion between {@link Money} stated in different currencies.
     * For example:<pre>
     *     Currency.setReferenceCurrency(Currency.USD);
     *     Currency.EUR.setExchangeRate(1.17); // 1.0 € = 1.17 $
     * </pre>
     *
     * @param  refAmount the amount stated in the {@link #getReferenceCurrency}
     *         equals to one unit of this {@link Currency}.
     * @see    #getReferenceCurrency
     */
    public void setExchangeRate(double refAmount) {
        Currency reference = getReferenceCurrency();
        setDimension(reference, new MultiplyConverter(refAmount));
    }

    /**
     * Returns the exchange rate for this {@link Currency}.
     *
     * @return the amount stated in the {@link #getReferenceCurrency}
     *         equals to one unit of this {@link Currency}.
     * @throws ConversionException if the exchange rate has not be set for
     *         this {@link Currency}.
     */
    public double getExchangeRate() {
        Currency reference = getReferenceCurrency();
        MultiplyConverter cvtr  = (MultiplyConverter) getConverterTo(reference);
        return cvtr.derivative(0);
    }

    /**
     * Returns the currency unit for the given currency code.
     * See the <a href="http://www.bsi-global.com/iso4217currency"> ISO 4217
     * maintenance agency</a> for more information, including a table of
     * currency codes.
     *
     * @param  code the ISO-4217 code of the currency (e.g.
     *         <code>"EUR", "USD", "JPY"</code>).
     * @return the currency unit for the given currency code.
     * @throws IllegalArgumentException if the specified code is not an ISO-4217
     *         code.
     */
    public static BaseUnit getInstance(String code) {
        if (code.length() != 3) {
            throw new IllegalArgumentException("Invalid ISO-4217 code " + code);
        }
        Currency newCurrency = new Currency(code);
        Currency uniqueCurrency = (Currency) getInstance(newCurrency);
        if (newCurrency == uniqueCurrency) {
            // Adds to our list of existing currencies.
            synchronized (CURRENCIES) {
                CURRENCIES.add(newCurrency);
                Money.useFor(newCurrency);
            }
        }
        return uniqueCurrency;
    }

    /**
     * Returns the currency unit for the country of the given locale.
     *
     * @param  locale the locale for whose country a currency unit is returned.
     * @return the currency unit for the country of the given
     *         locale or {@link Currency#SYSTEM} if unknown.</code>.
     */
    public static Currency getInstance(Locale locale) {
        synchronized (COUNTRY_TO_CURRENCY) {
            Currency currency
                = (Currency) COUNTRY_TO_CURRENCY.get(locale.getCountry());
            return (currency != null) ? currency : SYSTEM;
        }
    }

    /**
     * Returns the default number of fraction digits used with the specified
     * currency unit. For example, the default number of fraction digits for
     * the {@link Currency#EUR} is 2, while for the {@link Currency#JPY} (Yen)
     * it's 0.
     *
     * @param  currency the currency unit for which the return the default
     *         number of fraction digits is returned.
     * @return the default number of fraction digits for the specified currency.
     */
    public static int getDefaultFractionDigits(Currency currency) {
        synchronized (CURRENCY_TO_DIGITS) {
            Integer digits = (Integer) CURRENCY_TO_DIGITS.get(currency);
            return (digits != null) ? digits.intValue() : 2;
        }
    }

    private static final long serialVersionUID = 5714606292038806316L;
}