/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;

import javolution.util.FastComparator;
import javolution.util.FastMap;

import javolution.realtime.LocalContext;
import javolution.realtime.LocalReference;
import javolution.lang.Text;
import javolution.lang.TextBuilder;
import javolution.lang.TextFormat;
import java.lang.CharSequence;

/**
 * <p> This is the abstract base class for all unit formats.</p>
 * <p> This class provides the interface for formatting and parsing
 *     units.</p>
 * <p> For all {@link SI} units, the 20 SI prefixes used to form decimal
 *     multiples and sub-multiples of SI units are recognized.
 *     {@link NonSI} units are directly recognized. For example:<pre>
 *        Unit.valueOf("m°C") == SI.MILLI(SI.CELSIUS)
 *        Unit.valueOf("kW")  == SI.KILO(SI.WATT)
 *        Unit.valueOf("ft")  == SI.METER.multiply(0.3048)</pre></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public abstract class UnitFormat extends TextFormat<Unit> {

    /**
     * Holds the standard unit format. This format is not locale sensitive
     * (international) and uses UTF Latin-1 Supplement
     * (range <code>0080-00FF</code>) supported by the majority of fonts.
     * For example: <code>cm³·A²/kg</code>
     */
    public final static UnitFormat STANDARD = new Standard();

    /**
     * Holds the ASCII unit format. This format uses characters range
     * <code>0000-007F</code> exclusively.
     * For example: <code>cm^3*kg^-1*A^2</code>
     */
    public final static UnitFormat ASCII = new Ascii();

    /**
     * Holds the HTML unit format. This format makes use of HTML tags to
     * represent product units.
     * For example: <code>cm&lt;sup&gt;3&lt;/sup&gt;&amp;#183;kg&lt;sup&gt;-1
     * &lt;/sup&gt;&amp;#183;A&lt;sup&gt;2&lt;/sup&gt;</code>
     * (<code>cm<sup>3</sup>&#183;kg<sup>-1</sup>&#183;A<sup>2</sup></code>)
     */
    public final static UnitFormat HTML = new Html();

    ////////////////////////////////////////////////////////////////////////////
    // UNIT DATABASE OPERATIONS.
    //
    
    /**
     * Holds the system-wide label-unit mapping.
     */
    static final FastMap<String, Unit> LABEL_TO_UNIT
        = new FastMap<String, Unit>().setKeyComparator(FastComparator.LEXICAL);

    /**
     * Holds the system-wide unit-label mapping.
     */
    static final FastMap<Unit, String> UNIT_TO_LABEL
        = new FastMap<Unit, String>();

    /**
     * Holds the system-wide alias-unit mapping.
     */
    static final FastMap<String, Unit> ALIAS_TO_UNIT
        = new FastMap<String, Unit>().setKeyComparator(FastComparator.LEXICAL);

    /**
     * Base constructor.
     */
    protected UnitFormat() {
    }

    /**
     * Returns the {@link LocalContext local} unit format 
     * (default {@link #STANDARD}).
     *
     * @return the unit format for the current thread.
     */
    public static UnitFormat current() {
        return (UnitFormat) FORMAT.get();
    }

    private static final LocalReference<UnitFormat> FORMAT
        = new LocalReference<UnitFormat>(STANDARD);

    /**
     * Sets the {@link LocalContext local} unit format.
     *
     * @param format the new local/global unit format.
     */
    public static void setCurrent(UnitFormat format) {
        FORMAT.set(format);
    }

    /**
     * Determines if the specified character may be part of a unit
     * identifier. Any letter or symbol which cannot be mistaken for
     * a separator is allowed.
     *
     * @param  ch the character to be tested.
     * @return  <code>true</code> if the character may be part of a unit
     *          identifier; <code>false</code> otherwise.
     */
    public static boolean isUnitIdentifierPart(char ch) {
        return (ch > '"') && ((ch <= '%') || (ch > '?')) && (ch != '^')
                && (ch != '¹') && (ch != '²') && (ch != '³') && (ch != '·');
    }

    /**
     * Parses text to produce an unit. 
     * <p> The expected form of the character sequence is:
     *     <code>{&lt;name&gt;{&lt;power&gt;{&lt;root&gt;}}}</code>
     * <p> For examples:
     *     <code><ul>
     *       <li>kg-2:3</li>
     *       <li>rad·s-²</li>
     *       <li>m*s**-2 <i>(for Fortran users)</i></li>
     *       <li>µ°C</li>
     *       <li>K^+1:2 cd^-2</li>
     *       <li>(rad·m)/(kg·s²)</li>
     *       <li>mol&lt;sup&gt;-1&lt;/sup&gt;·s&lt;sup&gt;-2&lt;/sup&gt;</li>
     *     </ul></code></p>
     * <p> HTML tags are ignored (e.g. &lt;sup&gt;...&lt;/sup&gt;).</p>
     * <p> Escape sequences are considered as separators (e.g. &amp;#183;).</p>
     * <p> Space characters are ignored.</p>
     * <p> Any level of parenthesis can be used.</p>
     *
     * @param csq the <code>CharSequence</code> to parse.
     * @param pos an object holding the parsing index and error position.
     * @return an {@link Unit} parsed from the character sequence.
     * @throws IllegalArgumentException if the character sequence contains
     *         an illegal syntax.
     * @see Character#isSpaceChar(char)
     */
    public Unit parse(CharSequence csq, 
            TextFormat.Cursor pos) {
        Unit result = Unit.ONE;
        Unit unit = null;
        int startIndex;
        boolean inverse = false;
        while (true) {
            int token = nextToken(csq, pos);
            switch (token) {
            case IDENTIFIER:
            	startIndex = pos.getIndex();
                CharSequence name = readIdentifier(csq, pos);
                unit = unitFor(name);
                check(unit != null, name + " not recognized", csq, startIndex);
                token = nextToken(csq, pos);
                if (token == NUMBER) {
                    int power = readNumber(csq, pos);
                    unit = unit.pow(power);
                    token = nextToken(csq, pos);
                    if (token == NUMBER) {
                        int root = readNumber(csq, pos);
                        unit = unit.root(root);
                    }
                }
                result = inverse ? result.divide(unit) : result.times(unit);
                inverse = false;
                break;
            case NUMBER:
            	startIndex = pos.getIndex();
                int number = readNumber(csq, pos);
                check(number == 1, "Invalid number", csq, startIndex);
                break;
            case OPEN_PARENTHESIS:
                pos.increment(1);
                unit = (Unit) parse(csq, pos);
                token = nextToken(csq, pos);
                check(token == CLOSE_PARENTHESIS, "')' expected", csq, pos.getIndex());
                pos.increment(1);
                result = inverse ? result.divide(unit) : result.times(unit);
                inverse = false;
                break;
            case DIVIDE:
                pos.increment(1);
                inverse = true;
                break;
            case END_SEQUENCE:
            case CLOSE_PARENTHESIS:
                return result; // Terminate.
            default:
                throw new InternalError("Unknown Token: " + token);
            }
        }
    }

    private int nextToken(CharSequence csq, Cursor pos) {
        final int length = csq.length();
        while (pos.getIndex() < length) {
            char c = csq.charAt(pos.getIndex());
            if (isUnitIdentifierPart(c)) {
                return IDENTIFIER;
            } else if (c == '(') {
                return OPEN_PARENTHESIS;
            } else if (c == ')') {
                return CLOSE_PARENTHESIS;
            } else if ((c == '+') || (c == '-') || isDigit(c)) {
                return NUMBER;
            } else if (c == '&') { // Ignores escape.
                int i = pos.getIndex();
                while ((++i < length) && csq.charAt(i) != ';') {
                }
                check(i != length, "';' expected", csq, pos.getIndex());
                pos.setIndex(i);
            } else if (c == '<') { // Ignores markup.
                int i = pos.getIndex();
                while ((++i < length) && csq.charAt(i) != '>') {
                }
                check(i != length, "';' expected", csq, pos.getIndex());
                pos.setIndex(i);
            } else if (c == '/') {
                return DIVIDE;
            } // Else separator or space ignore.
            pos.increment(1);
        }
        return END_SEQUENCE;
    }

    private static final int END_SEQUENCE = 0;

    private static final int IDENTIFIER = 1;

    private static final int NUMBER = 2;

    private static final int OPEN_PARENTHESIS = 3;

    private static final int CLOSE_PARENTHESIS = 4;

    private static final int DIVIDE = 5;

    private static boolean isDigit(char c) {
        return Character.isDigit(c) || (c == '¹') || (c == '²') || (c == '³');
    }

    private CharSequence readIdentifier(CharSequence csq, Cursor pos) {
        final int length = csq.length();
        int start = pos.getIndex();
        int i = start;
        while ((++i < length) && isUnitIdentifierPart(csq.charAt(i))) {
        }
        pos.setIndex(i);
        return csq.subSequence(start, i);
    }

    private int readNumber(CharSequence seq, Cursor pos) {
        final int length = seq.length();
        int exp = 0;
        boolean isNegative = false;
        while (pos.getIndex() < length) {
            char c = seq.charAt(pos.getIndex());
            if ((c == '-') || (c == '+')) {
                isNegative = (c == '-');
            } else if (c == '¹') {
                exp = exp * 10 + 1;
            } else if (c == '²') {
                exp = exp * 10 + 2;
            } else if (c == '³') {
                exp = exp * 10 + 3;
            } else if ((c >= '0') && (c <= '9')) {
                exp = exp * 10 + (c - '0');
            } else {
                break; // Done.
            }
            pos.increment(1);
        }
        return isNegative ? -exp : exp;
    }

    private void check(boolean expr, String message, CharSequence csq,
            int index) {
        if (!expr) {
            throw new IllegalArgumentException(message + " (in " + csq
                    + " at index " + index + ")");
        }
    }

    /**
     * Returns the label for the specified unit. The default behavior of
     * this method (which may be overridden) is first to search the label
     * database.
     *
     * @param  unit the unit for which the label is searched for.
     * @return the database label, the unit symbol, some other representation
     *         of the specified unit (e.g. <code>[K+273.15], [m*0.01]</code>) or
     *         <code>null</code> if the unit has no intrinsic label (e.g. 
     *         {@link ProductUnit}).
     * @see    #unitFor
     */
    public CharSequence labelFor(Unit unit) {
        // Searches label database.
        String label = UNIT_TO_LABEL.get(unit);
        if (label != null) return label;
            
        // Look up symbol.
        String symbol = unit._symbol;
        if (symbol != null) return symbol;
        
        // Transformed unit.
        if (unit instanceof TransformedUnit) {
            TransformedUnit tfmUnit = (TransformedUnit) unit;
            Unit parentUnit = tfmUnit.getParentUnit();
            Converter cvtr = tfmUnit.toParentUnit();
            if (cvtr instanceof AddConverter) {
                return Text.valueOf('[').concat(parentUnit.toText()).concat(
                        Text.valueOf('+')).concat(
                                Text.valueOf(((AddConverter) cvtr).getOffset())).
                                concat(Text.valueOf(']'));
            } else if (cvtr instanceof MultiplyConverter) {
                return Text.valueOf('[').concat(parentUnit.toText()).concat(
                        Text.valueOf('*')).concat(
                                Text.valueOf(((MultiplyConverter) cvtr).derivative(0))).
                                concat(Text.valueOf(']'));
            } else { // Custom converters.
                return Text.valueOf('[').concat(parentUnit.toText()).concat(
                        Text.valueOf("?]"));
            }
        }
        // Compound unit.
        if (unit instanceof CompoundUnit) {
            CompoundUnit cpdUnit = (CompoundUnit) unit;
            return labelFor(cpdUnit.getMainUnit()); 
        }
        return null;
    }

    /**
     * Returns the unit identified by the specified label. The default behavior
     * of this method (which may be overridden) is first to search the label
     * database, then the alias database and finally the symbols collection.
     *
     * @param  label the label, alias, or symbol identifying the unit.
     * @return the unit identified by the specified label or
     *         <code>null</code> if the identification fails.
     * @see    #labelFor
     */
    public Unit unitFor(CharSequence label) {
        // Label database.
        Unit unit = LABEL_TO_UNIT.get(label);
        if (unit != null) return unit;
        // Alias database.
        unit = ALIAS_TO_UNIT.get(label);
        if (unit != null) return unit;
        // Symbol.
        unit = Unit.SYMBOLS.get(label);
        return unit;
    }

    /**
     * This inner class represents the standard unit format.
     */
    private static final class Standard extends UnitFormat {

        // Implements abstract method.
        public Text format(Unit unit) {
            CharSequence label = labelFor(unit);
            if (label != null) {
                return Text.valueOf(label);
            }
            TextBuilder tb = TextBuilder.newInstance();
            if (unit instanceof ProductUnit) {
                ProductUnit productUnit = (ProductUnit) unit;
                int invNbr = 0;

                // Write positive exponents first.
                boolean start = true;
                for (int i = 0; i < productUnit.size(); i++) {
                    label = labelFor(productUnit.get(i).getUnit());
                    int pow = productUnit.get(i).getPow();
                    int root = productUnit.get(i).getRoot();
                    if (pow >= 0) {
                        if (!start) {
                            tb.append('·'); // Separator.
                        }
                        append(tb, label, pow, root);
                        start = false;
                    } else {
                        invNbr++;
                    }
                }

                // Write negative exponents.
                if (invNbr != 0) {
                    if (start) {
                        tb.append('1'); // e.g. 1/s
                    }
                    tb.append('/');
                    if (invNbr > 1) {
                        tb.append('(');
                    }
                    start = true;
                    for (int i = 0; i < productUnit.size(); i++) {
                        label = labelFor(productUnit.get(i).getUnit());
                        int pow = productUnit.get(i).getPow();
                        int root = productUnit.get(i).getRoot();
                        if (pow < 0) {
                            if (!start) {
                                tb.append('·'); // Separator.
                            }
                            append(tb, label, -pow, root);
                            start = false;
                        }
                    }
                    if (invNbr > 1) {
                        tb.append(')');
                    }
                }
            } else {
                throw new IllegalArgumentException(
                        "Cannot format given Object as a Unit");
            }
            return tb.toText();
        }

        private static void append(TextBuilder tb, CharSequence symbol, int pow,
                int root) {
            tb.append(symbol);
            if ((pow != 1) || (root != 1)) {
                // Write exponent.
                if ((pow == 2) && (root == 1)) {
                    tb.append('²'); // Square
                } else if ((pow == 3) && (root == 1)) {
                    tb.append('³'); // Cubic
                } else {
                    // Use general exponent form.
                    tb.append('^').append(pow);
                    if (root != 1) {
                        tb.append(':').append(root);
                    }
                }
            }
        }
       
        private static final long serialVersionUID = 1L;
    }

    /**
     * This inner class represents the HTML unit format.
     */
    private static final class Html extends UnitFormat {

        // Implements abstract method.
        public Text format(Unit unit) {
            CharSequence label = labelFor(unit);
            if (label != null) {
                return Text.valueOf(label);
            }
            TextBuilder tb = TextBuilder.newInstance();
            if (unit instanceof ProductUnit) {
                ProductUnit productUnit = (ProductUnit) unit;
                for (int i = 0; i < productUnit.size(); i++) {
                    if (i != 0) {
                        tb.append("&#183;"); // Separator.
                    }
                    label = labelFor(productUnit.get(i).getUnit());
                    int pow = productUnit.get(i).getPow();
                    int root = productUnit.get(i).getRoot();
                    tb.append(label);
                    if ((pow != 1) || (root != 1)) {
                        // Write exponent.
                        tb.append("<sup>").append(pow);
                        if (root != 1) {
                            tb.append(':').append(root);
                        }
                        tb.append("</sup>");
                    }
                }
            } else {
                throw new IllegalArgumentException(
                        "Cannot format given Object as a Unit");
            }
            return tb.toText();
        }

        private static final long serialVersionUID = 1L;
    }

    /**
     * This inner class represents the ASCII unit format.
     */
    private static final class Ascii extends UnitFormat {

        // Implements abstract method.
        public Text format(Unit unit) {
            CharSequence label = labelFor(unit);
            if (label != null) {
                return Text.valueOf(label);
            }
            TextBuilder tb = TextBuilder.newInstance();
            if (unit instanceof ProductUnit) {
                ProductUnit productUnit = (ProductUnit) unit;
                for (int i = 0; i < productUnit.size(); i++) {
                    if (i != 0) {
                        tb.append(' '); // Separator.
                    }
                    label = labelFor(productUnit.get(i).getUnit());
                    int pow = productUnit.get(i).getPow();
                    int root = productUnit.get(i).getRoot();
                    tb.append(label);
                    if ((pow != 1) || (root != 1)) {
                        // Write exponent.
                        tb.append('^').append(pow);
                        if (root != 1) {
                            tb.append(':').append(root);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException(
                        "Cannot format given Object as a Unit");
            }
            return tb.toText();
        }

        private static final long serialVersionUID = 1L;
    }

    // Forces SI/NonSI initialization (load unit database).
    static {
        SI.initializeClass();
        NonSI.initializeClass();
    }
}