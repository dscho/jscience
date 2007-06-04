/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.measure.unit;


import java.io.IOException;
import java.lang.CharSequence;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
//@RETROWEAVER import javolution.text.Appendable;
import javax.measure.converter.AddConverter;
import javax.measure.converter.MultiplyConverter;
import javax.measure.converter.RationalConverter;
import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Quantity;

import static javax.measure.unit.SI.*;

/**
 * <p> This class provides the interface for formatting and parsing {@link 
 *     Unit units}.</p>
 *     
 * <p> For all {@link SI} units, the 20 SI prefixes used to form decimal
 *     multiples and sub-multiples of SI units are recognized.
 *     {@link NonSI} units are directly recognized. For example:[code]
 *        Unit.valueOf("m°C").equals(SI.MILLI(SI.CELSIUS))
 *        Unit.valueOf("kW").equals(SI.KILO(SI.WATT))
 *        Unit.valueOf("ft").equals(SI.METER.multiply(0.3048))[/code]</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.2, February 9, 2006
 */
public abstract class UnitFormat extends Format {

    /**
     * Holds the standard unit format.
     */
    private static final StandardFormat STANDARD = new StandardFormat();

    /**
     * Holds the locale unit format.
     */
    private static final LocaleFormat LOCALE = new LocaleFormat();

    /**
     * Holds the ASCII unit format.
     */
    private static final AsciiFormat ASCII = new AsciiFormat();

    /**
     * Holds the HTML unit format.
     */
    private static final HtmlFormat HTML = new HtmlFormat();

    /**
     * Returns the standard unit format (format used by 
     * {@link Unit#valueOf(CharSequence) Unit.valueOf(CharSequence)} and 
     * {@link Unit#toString() Unit.toString()}). This format is independant of 
     * any locale and uses UTF Latin-1 Supplement (range <code>0080-00FF</code>)
     * supported by the majority  of fonts. For example: <code>cm³·A²/kg</code>
     */
    public static UnitFormat getStandardInstance() {
        return UnitFormat.STANDARD;
    }

    /**
     * Returns the locale instance; this format interprets units differently
     * based upon the current runtime locale (e.g.&nbsp;"gal" (gallon) has not 
     * the same value in US and UK)
     */
    public static UnitFormat getLocaleInstance() {
        return UnitFormat.LOCALE;
    }

    /**
     * Returns the ASCII unit format; this format uses characters range
     * <code>0000-007F</code> exclusively.
     * For example: <code>cm^3*kg^-1*A^2</code>
     */
    public static UnitFormat getAsciiInstance() {
        return UnitFormat.ASCII;
    }

    /**
     * Returns the HTML unit format. This format makes use of HTML tags to
     * represent product units.
     * For example: <code>cm&lt;sup&gt;3&lt;/sup&gt;&amp;#183;kg&lt;sup&gt;-1
     * &lt;/sup&gt;&amp;#183;A&lt;sup&gt;2&lt;/sup&gt;</code>
     * (<code>cm<sup>3</sup>&#183;kg<sup>-1</sup>&#183;A<sup>2</sup></code>)
     */
    public static UnitFormat getHtmlInstance() {
        return UnitFormat.HTML;
    }

    /**
     * Base constructor.
     */
    protected UnitFormat() {
    }

    /**
     * Formats the specified unit.
     *
     * @param unit the unit to format.
     * @param appendable the appendable destination.
     * @throws IOException if an error occurs.
     */
    public abstract Appendable format(Unit unit, Appendable appendable)
            throws IOException;

    /**
     * Parses a sequence of character to produce an unit. 
     *
     * @param csq the <code>CharSequence</code> to parse.
     * @param pos an object holding the parsing index and error position.
     * @return an {@link Unit} parsed from the character sequence.
     * @throws IllegalArgumentException if the character sequence contains
     *         an illegal syntax.
     */
    public abstract Unit<? extends Quantity> parse(CharSequence csq, ParsePosition pos);

    /**
     * Attaches a system-wide label to the specified unit. For example:
     * [code]
     *     UnitFormat.getStandardInstance().label(DAY.multiply(365), "year");
     *     UnitFormat.getStandardInstance().label(METER.multiply(0.3048), "ft");
     * [/code]
     * If the specified label is already associated to an unit the previous 
     * association is discarded or ignored.
     *  
     * @param  unit the unit being labelled. 
     * @param  label the new label for this unit.
     * @throws IllegalArgumentException if the label is not a 
     *         {@link UnitFormat#isValidIdentifier(String)} valid identifier.
     */
    public abstract void label(Unit unit, String label);

    /**
     * Attaches a system-wide alias to this unit. Multiple aliases may
     * be attached to the same unit. Aliases are used during parsing to
     * recognize different variants of the same unit. For example:
     * [code]
     *     UnitFormat.getLocaleInstance().alias(METER.multiply(0.3048), "foot");
     *     UnitFormat.getLocaleInstance().alias(METER.multiply(0.3048), "feet");
     *     UnitFormat.getLocaleInstance().alias(METER, "meter");
     *     UnitFormat.getLocaleInstance().alias(METER, "metre");
     * [/code]
     * If the specified label is already associated to an unit the previous 
     * association is discarded or ignored.
     *
     * @param  unit the unit being aliased.
     * @param  alias the alias attached to this unit.
     * @throws IllegalArgumentException if the label is not a 
     *         {@link UnitFormat#isValidIdentifier(String)} valid identifier.
     */
    public abstract void alias(Unit unit, String alias);

    /**
     * Indicates if the specified name can be used as unit identifier.
     * For the standard format ({@link #getStandardInstance}) identifiers with 
     * separators (e.g. blank spaces) are not valid.
     *
     * @param  name the identifier to be tested.
     * @return <code>true</code> if the name specified can be used as 
     *         label or alias for this format;<code>false</code> otherwise.
     */
    public abstract boolean isValidIdentifier(String name);

    /**
     * Formats an unit and appends the resulting text to a given string
     * buffer (implements <code>java.text.Format</code>).
     *
     * @param unit the unit to format.
     * @param toAppendTo where the text is to be appended
     * @param pos the field position (not used).
     * @return <code>toAppendTo</code>
     */
    public final StringBuffer format(Object unit, final StringBuffer toAppendTo,
            FieldPosition pos) {
        try {
            Object dest = toAppendTo;
            if (dest instanceof Appendable) { 
                format((Unit) unit, (Appendable)dest);                        
            } else {  // When retroweaver is used to produce 1.4 binaries.
                format((Unit) unit, new Appendable() {

                    public Appendable append(char arg0) throws IOException {
                        toAppendTo.append(arg0);
                        return null;
                    }

                    public Appendable append(CharSequence arg0) throws IOException {
                        toAppendTo.append(arg0);
                        return null;
                    }

                    public Appendable append(CharSequence arg0, int arg1, int arg2) throws IOException {
                        toAppendTo.append(arg0.subSequence(arg1, arg2));
                        return null;
                    }});
            }
            return toAppendTo;
        } catch (IOException e) {
            throw new Error(e); // Should never happen.
        }
    }

    /**
     * Parses the text from a string to produce an object
     * (implements <code>java.text.Format</code>).
     * 
     * @param source the string source, part of which should be parsed.
     * @param pos the cursor position.
     * @return the corresponding unit or <code>null</code> if the string 
     *         cannot be parsed.
     */
    public final Object parseObject(String source, ParsePosition pos) {
        return parse(source, pos);
    }

    /**
     * This class represents the standard format.
     */
    private static class StandardFormat extends UnitFormat {

        /**
         * Holds the name to unit mapping.
         */
        final HashMap<String, Unit> _nameToUnit = new HashMap<String, Unit>();

        /**
         * Holds the unit to name mapping.
         */
        final HashMap<Unit, String> _unitToName = new HashMap<Unit, String>();

        @Override
        public void label(Unit unit, String label) {
            if (!isValidIdentifier(label))
                throw new IllegalArgumentException("Label: " + label
                        + " is not a valid identifier.");
            synchronized (this) {
                _nameToUnit.put(label, unit);
                _unitToName.put(unit, label);
            }
        }

        @Override
        public void alias(Unit unit, String alias) {
            if (!isValidIdentifier(alias))
                throw new IllegalArgumentException("Alias: " + alias
                        + " is not a valid identifier.");
            synchronized (this) {
                _nameToUnit.put(alias, unit);
            }
        }

        @Override
        public boolean isValidIdentifier(String name) {
            if ((name == null) || (name.length() == 0))
                return false;
            for (int i = 0; i < name.length(); i++) {
                if (!isUnitIdentifierPart(name.charAt(i)))
                    return false;
            }
            return true;
        }

        static boolean isUnitIdentifierPart(char ch) {
            return (ch > '"') && ((ch <= '%') || (ch > '?')) && (ch != '^')
                    && (ch != '¹') && (ch != '²') && (ch != '³') && (ch != '·');
        }

        // Returns the name for the specified unit or null if product unit.
        public String nameFor(Unit unit) {
            // Searches label database.
            String label = _unitToName.get(unit);
            if (label != null)
                return label;
            if (unit instanceof BaseUnit)
                return ((BaseUnit) unit).getSymbol();
            if (unit instanceof AlternateUnit)
                return ((AlternateUnit) unit).getSymbol();
            if (unit instanceof TransformedUnit) {
                TransformedUnit tfmUnit = (TransformedUnit) unit;
                Unit baseUnits = tfmUnit.getSystemUnit();
                UnitConverter cvtr = tfmUnit.toSystemUnit();
                if (cvtr instanceof AddConverter) {
                    return "[" + baseUnits + "+"
                            + (((AddConverter) cvtr).getOffset()) + "]";
                } else if (cvtr instanceof RationalConverter) {
                    return "[" + baseUnits + "*"
                            + ((RationalConverter) cvtr).getDividend() + "/" +
                               ((RationalConverter) cvtr).getDivisor() + "]";
                } else if (cvtr instanceof MultiplyConverter) {
                    return "[" + baseUnits + "*"
                            + (((MultiplyConverter) cvtr).getFactor()) + "]";
                } else { // Others converters.
                    return "[" + baseUnits + "?]";
                }
            }
            // Compound unit.
            if (unit instanceof CompoundUnit) {
                CompoundUnit cpdUnit = (CompoundUnit) unit;
                return nameFor(cpdUnit.getFirst()).toString() + ":"
                        + nameFor(cpdUnit.getNext());
            }
            return null; // Product unit.
        }

        // Returns the unit for the specified name.
        public Unit unitFor(String name) {
            Unit unit = _nameToUnit.get(name);
            if (unit != null)
                return unit;
            unit = Unit.SYMBOL_TO_UNIT.get(name);
            return unit;
        }

        ////////////////////////////
        // Parsing.

        @SuppressWarnings("unchecked")
        @Override
        public Unit<? extends Quantity> parse(CharSequence csq, ParsePosition pos) {
            Unit result = Unit.ONE;
            Unit unit = null;
            int startIndex;
            boolean inverse = false;
            while (true) {
                int token = nextToken(csq, pos);
                switch (token) {
                case IDENTIFIER:
                    startIndex = pos.getIndex();
                    String name = readIdentifier(csq, pos);
                    unit = unitFor(name);
                    check(unit != null, name + " not recognized", csq,
                            startIndex);
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
                    pos.setIndex(pos.getIndex() + 1);
                    unit = (Unit) parse(csq, pos);
                    token = nextToken(csq, pos);
                    check(token == CLOSE_PARENTHESIS, "')' expected", csq, pos
                            .getIndex());
                    pos.setIndex(pos.getIndex() + 1);
                    result = inverse ? result.divide(unit) : result.times(unit);
                    inverse = false;
                    break;
                case DIVIDE:
                    pos.setIndex(pos.getIndex() + 1);
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

        private static final int END_SEQUENCE = 0;

        private static final int IDENTIFIER = 1;

        private static final int NUMBER = 2;

        private static final int OPEN_PARENTHESIS = 3;

        private static final int CLOSE_PARENTHESIS = 4;

        private static final int DIVIDE = 5;

        private int nextToken(CharSequence csq, ParsePosition pos) {
            final int length = csq.length();
            while (pos.getIndex() < length) {
                char c = csq.charAt(pos.getIndex());
                if (StandardFormat.isUnitIdentifierPart(c)) {
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
                pos.setIndex(pos.getIndex() + 1);
            }
            return END_SEQUENCE;
        }

        private static boolean isDigit(char c) {
            return ((c >= '0') && (c <= '9')) || (c == '¹') || (c == '²')
                    || (c == '³');
        }

        private String readIdentifier(CharSequence csq, ParsePosition pos) {
            final int length = csq.length();
            int start = pos.getIndex();
            int i = start;
            while ((++i < length)
                    && StandardFormat.isUnitIdentifierPart(csq.charAt(i))) {
            }
            pos.setIndex(i);
            return csq.subSequence(start, i).toString();
        }

        private int readNumber(CharSequence seq, ParsePosition pos) {
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
                pos.setIndex(pos.getIndex() + 1);
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

        ////////////////////////////
        // Formatting.

        @Override
        public Appendable format(Unit unit, Appendable appendable)
                throws IOException {
            String name = nameFor(unit);
            if (name != null)
                return appendable.append(name);
            if (!(unit instanceof ProductUnit))
                throw new IllegalArgumentException(
                        "Cannot format given Object as a Unit");

            // Product unit.
            ProductUnit productUnit = (ProductUnit) unit;
            int invNbr = 0;

            // Write positive exponents first.
            boolean start = true;
            for (int i = 0; i < productUnit.getUnitCount(); i++) {
                int pow = productUnit.getUnitPow(i);
                if (pow >= 0) {
                    if (!start) {
                        appendable.append('·'); // Separator.
                    }
                    name = nameFor(productUnit.getUnit(i));
                    int root = productUnit.getUnitRoot(i);
                    append(appendable, name, pow, root);
                    start = false;
                } else {
                    invNbr++;
                }
            }

            // Write negative exponents.
            if (invNbr != 0) {
                if (start) {
                    appendable.append('1'); // e.g. 1/s
                }
                appendable.append('/');
                if (invNbr > 1) {
                    appendable.append('(');
                }
                start = true;
                for (int i = 0; i < productUnit.getUnitCount(); i++) {
                    int pow = productUnit.getUnitPow(i);
                    if (pow < 0) {
                        name = nameFor(productUnit.getUnit(i));
                        int root = productUnit.getUnitRoot(i);
                        if (!start) {
                            appendable.append('·'); // Separator.
                        }
                        append(appendable, name, -pow, root);
                        start = false;
                    }
                }
                if (invNbr > 1) {
                    appendable.append(')');
                }
            }
            return appendable;
        }

        private void append(Appendable appendable, CharSequence symbol,
                int pow, int root) throws IOException {
            appendable.append(symbol);
            if ((pow != 1) || (root != 1)) {
                // Write exponent.
                if ((pow == 2) && (root == 1)) {
                    appendable.append('²'); // Square
                } else if ((pow == 3) && (root == 1)) {
                    appendable.append('³'); // Cubic
                } else {
                    // Use general exponent form.
                    appendable.append('^');
                    appendable.append(String.valueOf(pow));
                    if (root != 1) {
                        appendable.append(':');
                        appendable.append(String.valueOf(root));
                    }
                }
            }
        }
        
        private static final long serialVersionUID = 1L;
    }

    /**
     * This class represents the locale format.
     */
    private static class LocaleFormat extends StandardFormat {

        @Override
        public String nameFor(Unit unit) {
            // Searches locale database first.
            String name = super.nameFor(unit);
            if (name != null)
                return name;
            return STANDARD.nameFor(unit);
        }

        @Override
        public Unit unitFor(String name) {
            // Searches locale database first.
            Unit unit = super.unitFor(name);
            if (unit != null)
                return unit;
            return STANDARD.unitFor(name);
        }
        
        private static final long serialVersionUID = 1L;
    }

    /**
     * This class represents the ASCII format.
     */
    private static class AsciiFormat extends StandardFormat {

        @Override
        public String nameFor(Unit unit) {
            // Searches ASCII label database first.
            String name = super.nameFor(unit);
            if (name != null)
                return name;
            return STANDARD.nameFor(unit);
        }

        @Override
        public Unit unitFor(String name) {
            // Searches ASCII unit database first.
            Unit unit = super.unitFor(name);
            if (unit != null)
                return unit;
            return STANDARD.unitFor(name);
        }

        @Override
        public Appendable format(Unit unit, Appendable appendable)
                throws IOException {
            String name = nameFor(unit);
            if (name != null)
                return appendable.append(name);
            if (!(unit instanceof ProductUnit))
                throw new IllegalArgumentException(
                        "Cannot format given Object as a Unit");

            ProductUnit productUnit = (ProductUnit) unit;
            for (int i = 0; i < productUnit.getUnitCount(); i++) {
                if (i != 0) {
                    appendable.append(' '); // Separator.
                }
                name = nameFor(productUnit.getUnit(i));
                int pow = productUnit.getUnitPow(i);
                int root = productUnit.getUnitRoot(i);
                appendable.append(name);
                if ((pow != 1) || (root != 1)) {
                    // Use general exponent form.
                    appendable.append('^');
                    appendable.append(String.valueOf(pow));
                    if (root != 1) {
                        appendable.append(':');
                        appendable.append(String.valueOf(root));
                    }
                }
            }
            return appendable;
        }

        private static final long serialVersionUID = 1L;
    }

    /**
     * This class represents the HTML format.
     */
    private static class HtmlFormat extends StandardFormat {

        @Override
        public String nameFor(Unit unit) {
            // Searches HTML label database first.
            String name = super.nameFor(unit);
            if (name != null)
                return name;
            return STANDARD.nameFor(unit);
        }

        @Override
        public Unit unitFor(String name) {
            // Searches HTML unit database first.
            Unit unit = super.unitFor(name);
            if (unit != null)
                return unit;
            return STANDARD.unitFor(name);
        }

        @Override
        public Appendable format(Unit unit, Appendable appendable)
                throws IOException {
            String name = nameFor(unit);
            if (name != null)
                return appendable.append(name);
            if (!(unit instanceof ProductUnit))
                throw new IllegalArgumentException(
                        "Cannot format given Object as a Unit");

            ProductUnit productUnit = (ProductUnit) unit;
            for (int i = 0; i < productUnit.getUnitCount(); i++) {
                if (i != 0) {
                    appendable.append("&#183;"); // Separator.
                }
                name = nameFor(productUnit.getUnit(i));
                int pow = productUnit.getUnitPow(i);
                int root = productUnit.getUnitRoot(i);
                appendable.append(name);
                if ((pow != 1) || (root != 1)) {
                    // Write exponent.
                    appendable.append("<sup>");
                    appendable.append(String.valueOf(pow));
                    if (root != 1) {
                        appendable.append(':');
                        appendable.append(String.valueOf(root));
                    }
                    appendable.append("</sup>");
                }
            }
            return appendable;
        }

        private static final long serialVersionUID = 1L;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Initializes the standard unit database for SI units.
    
    private static final Unit[] SI_UNITS = { SI.AMPERE, SI.BECQUEREL,
            SI.CANDELA, SI.COULOMB, SI.FARAD, SI.GRAY, SI.HENRY, SI.HERTZ,
            SI.JOULE, SI.KATAL, SI.KELVIN, SI.LUMEN, SI.LUX, SI.METER, SI.MOLE,
            SI.NEWTON, SI.OHM, SI.PASCAL, SI.RADIAN, SI.SECOND, SI.SIEMENS,
            SI.SIEVERT, SI.STERADIAN, SI.TESLA, SI.VOLT, SI.WATT, SI.WEBER };

    private static final String[] PREFIXES = { "Y", "Z", "E", "P", "T", "G",
            "M", "k", "h", "da", "d", "c", "m", "µ", "n", "p", "f", "a", "z",
            "y" };

    private static final UnitConverter[] CONVERTERS = { E24, E21, E18, E15, E12,
            E9, E6, E3, E2, E1, Em1, Em2, Em3, Em6, Em9, Em12,
            Em15, Em18, Em21, Em24 };
    
    private static String asciiPrefix(String prefix) {
        return prefix == "µ" ? "micro" : prefix;
    }
    
    
    static {
        for (int i = 0; i < SI_UNITS.length; i++) {
            for (int j = 0; j < PREFIXES.length; j++) {
                Unit si = SI_UNITS[i];
                Unit u = si.transform(CONVERTERS[j]);
                String symbol = (si instanceof BaseUnit) ? ((BaseUnit) si)
                        .getSymbol() : ((AlternateUnit) si).getSymbol();
                STANDARD.label(u, PREFIXES[j] + symbol);
                if (PREFIXES[j] == "µ") {
                    ASCII.label(u, "micro" + symbol);
                }
            }
        }
        // Special case for KILOGRAM.
        STANDARD.label(SI.GRAM, "g");
        for (int i = 0; i < PREFIXES.length; i++) {
            if (CONVERTERS[i] == E3) continue;  // kg is already defined.
            STANDARD.label(SI.KILOGRAM.transform(CONVERTERS[i].concatenate(Em3)),
                        PREFIXES[i] + "g");
            if (PREFIXES[i] == "µ") {
                 ASCII.label(SI.KILOGRAM.transform(CONVERTERS[i].concatenate(Em3)), "microg");
            }   
        }

        // Alias and ASCII for Ohm
        STANDARD.alias(SI.OHM, "Ohm");
        ASCII.label(SI.OHM, "Ohm");
        for (int i = 0; i < PREFIXES.length; i++) {
            STANDARD.alias(SI.OHM.transform(CONVERTERS[i]), PREFIXES[i] + "Ohm");
            ASCII.label(SI.OHM.transform(CONVERTERS[i]), asciiPrefix(PREFIXES[i]) + "Ohm");
        }
        
        // Special case for DEGREE_CElSIUS.
        STANDARD.label(SI.CELSIUS, "℃");
        STANDARD.alias(SI.CELSIUS, "°C");
        ASCII.label(SI.CELSIUS, "Celsius");
        for (int i = 0; i < PREFIXES.length; i++) {
            STANDARD.label(SI.CELSIUS.transform(CONVERTERS[i]), PREFIXES[i] + "℃");
            STANDARD.alias(SI.CELSIUS.transform(CONVERTERS[i]), PREFIXES[i] + "°C");
            ASCII.label(SI.CELSIUS.transform(CONVERTERS[i]), asciiPrefix(PREFIXES[i]) + "Celsius");
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // To be moved in resource bundle in future release (locale dependent). 
    static {
        STANDARD.label(NonSI.PERCENT, "%");
        STANDARD.label(NonSI.DECIBEL, "dB");
        STANDARD.label(NonSI.G, "grav");
        STANDARD.label(NonSI.ATOM, "atom");
        STANDARD.label(NonSI.REVOLUTION, "rev");
        STANDARD.label(NonSI.DEGREE_ANGLE, "°");
        ASCII.label(NonSI.DEGREE_ANGLE, "degree_angle");
        STANDARD.label(NonSI.MINUTE_ANGLE, "′");
        ASCII.label(NonSI.MINUTE_ANGLE, "minute_angle");
        STANDARD.label(NonSI.SECOND_ANGLE, "″");
        ASCII.label(NonSI.SECOND_ANGLE, "second_angle");
        STANDARD.label(NonSI.CENTIRADIAN, "centiradian");
        STANDARD.label(NonSI.GRADE, "grade");
        STANDARD.label(NonSI.ARE, "a");
        STANDARD.label(NonSI.HECTARE, "ha");
        STANDARD.label(NonSI.BYTE, "byte");
        STANDARD.label(NonSI.MINUTE, "min");
        STANDARD.label(NonSI.HOUR, "h");
        STANDARD.label(NonSI.DAY, "day");
        STANDARD.label(NonSI.WEEK, "week");
        STANDARD.label(NonSI.YEAR, "year");
        STANDARD.label(NonSI.MONTH, "month");
        STANDARD.label(NonSI.DAY_SIDEREAL, "day_sidereal");
        STANDARD.label(NonSI.YEAR_SIDEREAL, "year_sidereal");
        STANDARD.label(NonSI.YEAR_CALENDAR, "year_calendar");
        STANDARD.label(NonSI.E, "e");
        STANDARD.label(NonSI.FARADAY, "Fd");
        STANDARD.label(NonSI.FRANKLIN, "Fr");
        STANDARD.label(NonSI.GILBERT, "Gi");
        STANDARD.label(NonSI.ERG, "erg");
        STANDARD.label(NonSI.ELECTRON_VOLT, "eV");
        STANDARD.label(SI.KILO(NonSI.ELECTRON_VOLT), "keV");
        STANDARD.label(SI.MEGA(NonSI.ELECTRON_VOLT), "MeV");
        STANDARD.label(SI.GIGA(NonSI.ELECTRON_VOLT), "GeV");
        STANDARD.label(NonSI.LAMBERT, "La");
        STANDARD.label(NonSI.FOOT, "ft");
        STANDARD.label(NonSI.FOOT_SURVEY_US, "foot_survey_us");
        STANDARD.label(NonSI.YARD, "yd");
        STANDARD.label(NonSI.INCH, "in");
        STANDARD.label(NonSI.MILE, "mi");
        STANDARD.label(NonSI.NAUTICAL_MILE, "nmi");
        STANDARD.label(NonSI.ANGSTROM, "Å");
        ASCII.label(NonSI.ANGSTROM, "Angstrom");
        STANDARD.label(NonSI.ASTRONOMICAL_UNIT, "ua");
        STANDARD.label(NonSI.LIGHT_YEAR, "ly");
        STANDARD.label(NonSI.PARSEC, "pc");
        STANDARD.label(NonSI.POINT, "pt");
        STANDARD.label(NonSI.PIXEL, "pixel");
        STANDARD.label(NonSI.MAXWELL, "Mx");
        STANDARD.label(NonSI.GAUSS, "G");
        STANDARD.label(NonSI.ATOMIC_MASS, "u");
        STANDARD.label(NonSI.ELECTRON_MASS, "me");
        STANDARD.label(NonSI.POUND, "lb");
        STANDARD.label(NonSI.OUNCE, "oz");
        STANDARD.label(NonSI.TON_US, "ton_us");
        STANDARD.label(NonSI.TON_UK, "ton_uk");
        STANDARD.label(NonSI.METRIC_TON, "t");
        STANDARD.label(NonSI.DYNE, "dyn");
        STANDARD.label(NonSI.KILOGRAM_FORCE, "kgf");
        STANDARD.label(NonSI.POUND_FORCE, "lbf");
        STANDARD.label(NonSI.HORSEPOWER, "hp");
        STANDARD.label(NonSI.ATMOSPHERE, "atm");
        STANDARD.label(NonSI.BAR, "bar");
        STANDARD.label(NonSI.MILLIMETER_OF_MERCURY, "mmHg");
        STANDARD.label(NonSI.INCH_OF_MERCURY, "inHg");
        STANDARD.label(NonSI.RAD, "rd");
        STANDARD.label(NonSI.REM, "rem");
        STANDARD.label(NonSI.CURIE, "Ci");
        STANDARD.label(NonSI.RUTHERFORD, "Rd");
        STANDARD.label(NonSI.SPHERE, "sphere");
        STANDARD.label(NonSI.RANKINE, "°R");
        ASCII.label(NonSI.RANKINE, "degree_rankine");
        STANDARD.label(NonSI.FAHRENHEIT, "°F");
        ASCII.label(NonSI.FAHRENHEIT, "degree_fahrenheit");
        STANDARD.label(NonSI.KNOT, "knot");
        STANDARD.label(NonSI.MACH, "Mach");
        STANDARD.label(NonSI.C, "c");
        STANDARD.label(NonSI.LITER, "L");
        STANDARD.label(SI.MICRO(NonSI.LITER), "µL");
        ASCII.label(SI.MICRO(NonSI.LITER), "microL");
        STANDARD.label(SI.MILLI(NonSI.LITER), "mL");
        STANDARD.label(SI.CENTI(NonSI.LITER), "cL");
        STANDARD.label(SI.DECI(NonSI.LITER), "dL");
        STANDARD.label(NonSI.GALLON_LIQUID_US, "gal");
        STANDARD.label(NonSI.OUNCE_LIQUID_US, "oz");
        STANDARD.label(NonSI.GALLON_DRY_US, "gallon_dry_us");
        STANDARD.label(NonSI.GALLON_UK, "gallon_uk");
        STANDARD.label(NonSI.OUNCE_LIQUID_UK, "oz_uk");
        STANDARD.label(NonSI.ROENTGEN, "Roentgen");
        if (Locale.getDefault().getCountry().equals("GB")) {
            LOCALE.label(NonSI.GALLON_UK, "gal");
            LOCALE.label(NonSI.OUNCE_LIQUID_UK, "oz");
        }
    }
}
