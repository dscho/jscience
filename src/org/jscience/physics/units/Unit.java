/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.units;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javolution.realtime.LocalContext;
import javolution.util.FastMap;
import javolution.lang.Text;
import javolution.lang.TextBuilder;

/**
 * <p> This class represents a unit of physical quantity.</p>
 *
 * <p> It is helpful to think of instances of this class as recording the
 *     history by which they are created. Thus, for example, the string
 *     "g/kg" (which is a dimensionless unit) would result from invoking
 *     the method toString() on a unit that was created by dividing a
 *     gram unit by a kilogram unit. Yet, "kg" divided by "kg" returns
 *     {@link #ONE} and not "kg/kg" due to automatic unit factorization.</p>
 *
 * <p> This class supports the multiplication of offsets units. The result is
 *     usually a unit not convertible to its system unit. Such units may
 *     appear in derivative quantities. For example °C/m is a unit of
 *     gradient, which is common in atmospheric and oceanographic research.</p>
 *
 * <p> Units raised at rational powers are also supported. For example
 *     the cubic root of "liter" is a unit compatible with meter.</p>
 *
 * <p> Instances of this class (and sub-classes) are immutable and unique.</p>
 *
 * <p> This class has been largely inspired from <code>org.jscience.physics.units.Unit</code>
 *     specification currently being developped under
 *     <a href="http://www.jcp.org/jsr/detail/108.jsp">JSR-108</a>.</p>
 *
 * <p><i> This class requires proper initialization prior to its use by
 *        calling {@link org.jscience.JScience#initialize}. The initialization
 *        process ensures that the unit database has been correctly loaded
 *        (e.g. symbols such as "ft", "µm" are recognized).</i></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author  <a href="mailto:steve@unidata.ucar.edu">Steve Emmerson</a>
 * @author  Martin Desruisseaux
 * @version 1.0, October 24, 2004
 */
public abstract class Unit implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // LOOK-UP ACCELERATION (must be initialized first)
    /**
     * Holds an empty array of units (immutable).
     */
    private static final Unit[] NONE = new Unit[0];

    /**
     * Holds the unit's unique identifier (for look-up tables and unit's
     * internal order).
     */
    transient int _id;

    /**
     * Holds multiply look-up table.
     */
    private transient Unit[] _multiplyLookUp = NONE;

    /**
     * Holds divide look-up table.
     */
    private transient Unit[] _divideLookUp = NONE;

    /**
     * Holds root look-up table.
     */
    private transient Unit[] _rootLookUp = NONE;

    /**
     * Holds pow look-up table.
     */
    private transient Unit[] _powLookUp = NONE;
    //
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Holds the unit collection.
     */
    private static final FastMap UNITS = new FastMap(1024);

    /**
     * Holds the symbol collection (symbol-unit mapping).
     */
    private static final FastMap SYMBOLS = new FastMap(256);

    /**
     * Indicates if DIMENSIONS and TO_DIMENSIONS mappings are valid.
     */
    static final LocalContext.Variable DIMENSIONS_VALID 
        = new LocalContext.Variable(Boolean.FALSE);

    /**
     * Holds the key to unit's dimensions (context-sensitive).
     */
    private static final LocalContext.Variable DIMENSIONS
        = new LocalContext.Variable();

    /**
     * Holds the key to dimension converters (context-sensitive).
     */
    private static final LocalContext.Variable TO_DIMENSIONS
        = new LocalContext.Variable();

    /**
     * Holds the dimensionless unit <code>ONE</code>.
     */
    public static final Unit ONE = new ProductUnit();
    static {
        UNITS.put(ONE, ONE);
    }

    /**
     * Holds the unit's symbol or <code>null</code> if none
     * (e.g. {@link ProductUnit}, {@link TransformedUnit}).
     */
    final String _symbol;

    /**
     * Holds the hashcode for this unit (calculated once, when the unit
     * is added to the collection).
     */
    private transient int _hashCode;

    /**
     * Base constructor.
     *
     * @param  symbol the unit's symbol or <code>null</code> if none.
     */
    Unit(String symbol) {
        _symbol = symbol;
    }

    /**
     * Indicates if this unit is compatible with the unit specified.
     * Units don't need to be equals to be compatible. For example:
     * <pre>
     *     <code>RADIAN.equals(ONE) == false</code>
     *     <code>RADIAN.isCompatible(ONE) == true</code>.
     * </pre>
     * @param  that the other unit.
     * @return <code>true</code> if both units have the same dimension;
     *         <code>false</code> otherwise.
     * @see    #getDimension
     */
    public final boolean isCompatible(Unit that) {
        return (this == that) || (this.getDimension() == that.getDimension());
    }

    /**
     * Returns the units identifying the dimension of this unit.
     * The dimension returned is context-sensitve and based upon the
     * current base units' dimensions.
     *
     * @return the dimensional unit for this unit.
     * @see    BaseUnit#setDimension
     */
    public final Unit getDimension() {
        checkDimensions();
        FastMap dimensions = (FastMap) DIMENSIONS.getValue();
        Unit dimension = (Unit) dimensions.get(this);
        if (dimension == null) { // Caches current dimension.
            dimension = getCtxDimension();
            dimensions.put(this, dimension);
        }
        return dimension;
    }
    private static void checkDimensions() {
        if (DIMENSIONS_VALID.getValue() == Boolean.FALSE) {
            DIMENSIONS.setValue(new FastMap(256));
            TO_DIMENSIONS.setValue(new FastMap(256));
            DIMENSIONS_VALID.setValue(Boolean.TRUE);
        }
    }

    /**
     * Returns a converter of numeric values from this unit to another unit.
     *
     * @param  that the unit to which to convert the numeric values.
     * @return the converter from this unit to <code>that</code> unit.
     * @throws ConversionException if the conveter cannot be constructed
     *         (e.g. <code>!this.isCompatible(that)</code>).
     */
    public final Converter getConverterTo(Unit that) throws ConversionException {
        if (this == that) {
            return Converter.IDENTITY;
        } else if (this.isCompatible(that)) { // Same dimensions.
            checkDimensions();
            FastMap toDimensions = (FastMap) TO_DIMENSIONS.getValue();
            Converter thisToDimension = (Converter) toDimensions.get(this);
            if (thisToDimension == null) { // Caches current converter.
                thisToDimension = this.getCtxToDimension();
                toDimensions.put(this, thisToDimension);
            }
            Converter thatToDimension = (Converter) toDimensions.get(that);
            if (thatToDimension == null) { // Caches current converter.
                thatToDimension = that.getCtxToDimension();
                toDimensions.put(that, thatToDimension);
            }
            return thatToDimension.inverse().concatenate(thisToDimension);
        } else {
            throw new ConversionException(this + " is not compatible with "
                    + that + " in current context");
        }
    }

    /**
     * Indicates if this unit is a system unit.  A system unit
     * is either a base unit, an alternate unit or a product of base units
     * and alternate units.
     *
     * @return <code>true</code> if this unit is a system unit;
     *         <code>false</code> otherwise.
     * @see    #getSystemUnit
     */
    public final boolean isSystemUnit() {
        return this.getSystemUnit() == this;
    }

    /**
     * Returns the system unit for this unit. The system unit identifies the
     * nature of the quantity being measured using this unit.
     *
     * <p><i> Note: Having the same system units is not sufficient to ensure
     *              that a converter exists between the two units
     *              (e.g. °C/m and K/m).</i></p>
     * @return the system unit for this unit.
     * @see    #isSystemUnit
     */
    public abstract Unit getSystemUnit();

    /**
     * Returns a unit compatible to this unit except it uses the specified
     * symbol. The alternate unit can itself be used to form expressions and
     * symbols for other derived units. For example:
     * <pre><code>
     *   RADIAN = ONE.alternate("rad");
     *   NEWTON = METER.multiply(KILOGRAM).divide(SECOND.pow(2)).alternate("N");
     *   PASCAL = NEWTON.divide(METER.pow(2)).alternate("Pa");
     * </code></pre>
     *
     * @param  symbol the unique symbol for the alternate unit.
     * @return a unit compatible with this unit but of different nature.
     * @throws IllegalArgumentException if the specified symbol is currently
     *         associated to a different unit.
     * @throws UnsupportedOperationException if the specified unit is not
     *         a system unit.
     * @see    #isSystemUnit
     */
    public final Unit alternate(String symbol) {
        return AlternateUnit.getInstance(symbol, this);
    }

    /**
     * Returns the result of adding an offset to this unit. The returned unit
     * is convertible with all units that are convertible with this unit.
     *
     * @param  offset the offset added (expressed in this unit,
     *         e.g. <code>CELSIUS = KELVIN.add(273.15)</code>).
     * @return <code>this + offset</code>.
     */
    public final Unit add(double offset) {
        return TransformedUnit.getInstance(this, new AddConverter(offset));
    }

    /**
     * Returns the result of multiplying this unit by a scale factor. The
     * returned unit is convertible with all units that are convertible with
     * this unit.
     *
     * @param  scale the scale factor
     *         (e.g. <code>KILOMETER = METER.multiply(1000)</code>).
     * @return <code>this * scale</code>
     */
    public final Unit multiply(double scale) {
        return TransformedUnit.getInstance(this, new MultiplyConverter(scale));
    }

    /**
     * Returns the product of this unit with the one specified.
     *
     * @param  that the unit multiplicand.
     * @return <code>this * that</code>
     */
    public final Unit multiply(Unit that) {
        if (that._id >= _multiplyLookUp.length) {
            // Resizes.
            Unit[] tmp = new Unit[that._id + 1];
            System
                    .arraycopy(_multiplyLookUp, 0, tmp, 0,
                            _multiplyLookUp.length);
            _multiplyLookUp = tmp;
        }
        Unit result = _multiplyLookUp[that._id];
        if (result != null) {
            return result; // Hit.
        } else {
            result = ProductUnit.getProductInstance(this, that);
            _multiplyLookUp[that._id] = result;
            return result;
        }
    }

    /**
     * Returns the quotient of this unit with the one specified.
     *
     * @param  that the unit divisor.
     * @return <code>this / that</code>
     */
    public final Unit divide(Unit that) {
        if (that._id >= _divideLookUp.length) {
            // Resizes.
            Unit[] tmp = new Unit[that._id + 1];
            System.arraycopy(_divideLookUp, 0, tmp, 0, _divideLookUp.length);
            _divideLookUp = tmp;
        }
        Unit result = _divideLookUp[that._id];
        if (result != null) {
            return result; // Hit.
        } else {
            result = ProductUnit.getQuotientInstance(this, that);
            _divideLookUp[that._id] = result;
            return result;
        }
    }

    /**
     * Returns a unit equals to the given root of this unit.
     *
     * @param  n the root's order.
     * @return the result of taking the given root of this unit.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final Unit root(int n) {
        if (n > 1) {
            if (_rootLookUp.length < n - 1) {
                _rootLookUp = new Unit[n - 1]; // Resizes.
            }
            if (_rootLookUp[n - 2] == null) {
                _rootLookUp[n - 2] = ProductUnit.getRootInstance(this, n);
            }
            return _rootLookUp[n - 2];
        } else if (n == 1) {
            return this;
        } else if (n == 0) {
            throw new ArithmeticException("Root's order of zero");
        } else { // n < 0
            return ONE.divide(this.root(-n));
        }
    }

    /**
     * Returns a unit equals to this unit raised to an exponent.
     *
     * @param  n the exponent.
     * @return the result of raising this unit to the exponent.
     */
    public final Unit pow(int n) {
        if (n > 1) {
            if (_powLookUp.length < n - 1) {
                _powLookUp = new Unit[n - 1]; // Resizes.
            }
            if (_powLookUp[n - 2] == null) {
                _powLookUp[n - 2] = ProductUnit.getPowInstance(this, n);
            }
            return _powLookUp[n - 2];
        } else if (n == 1) {
            return this;
        } else if (n == 0) {
            return ONE;
        } else { // n < 0
            return ONE.divide(this.pow(-n));
        }
    }

    /**
     * Attaches a system-wide label to the specified unit. This method overrides
     * the previous unit's label (e.g. label from unit database) as units may
     * only have one label (but multiple aliases). For example:
     * <pre><code>
     *     DAY.multiply(365).label("year");
     *     Unit FOOT = METER.multiply(0.3048).label("ft");
     * </code></pre>
     *
     * @param  label the new label for this unit or <code>null</code>
     *         to detache the previous label (if any).
     * @return this unit.
     * @throws IllegalArgumentException if the specified label is a known symbol
     *         or if the specified label is already attached to a different
     *         unit (must be detached first).
     * @see    #alias
     */
    public Unit label(String label) {
        // Checks label argument.
        if (label != null) {
            if (Unit.searchSymbol(label) != null) {
                throw new IllegalArgumentException("Label: " + label
                        + " is a known symbol");
            } else {
                Unit u = (Unit) UnitFormat.LABEL_TO_UNIT.get(label);
                if ((u != null) && (u != this)) {
                    throw new IllegalArgumentException("Label: " + label
                            + " is attached to a different unit"
                            + " (must be detached first)");
                }
            }
        }
        // Updates unit database.
        synchronized (UnitFormat.class) {
            // Removes previous unit mapping.
            String prevLabel = (String) UnitFormat.UNIT_TO_LABEL.remove(this);
            UnitFormat.LABEL_TO_UNIT.remove(prevLabel);

            // Removes previous label mapping.
            Unit prevUnit = (Unit) UnitFormat.LABEL_TO_UNIT.remove(label);
            UnitFormat.UNIT_TO_LABEL.remove(prevUnit);

            // Maps unit and label together.
            UnitFormat.LABEL_TO_UNIT.put(label, this);
            UnitFormat.UNIT_TO_LABEL.put(this, label);
        }
        return this;
    }

    /**
     * Attaches a system-wide alias to this unit. Multiple aliases may
     * be attached to the same unit. Aliases are used during parsing to
     * recognize different variants of the same unit. For example:
     * <pre><code>
     *     METER.multiply(0.3048).alias("foot");
     *     METER.multiply(0.3048).alias("feet");
     *     METER.alias("meter");
     *     METER.alias("metre");
     * </code></pre>
     *
     * @param  alias the alias being attached to this unit.
     * @return this unit.
     */
    public Unit alias(String alias) {
        synchronized (UnitFormat.class) {
            UnitFormat.ALIAS_TO_UNIT.put(alias, this);
        }
        return this;
    }

    /**
     * Returns a unit instance that is defined from the specified
     * character sequence. If the specified character sequence is a
     * combination of units (e.g. {@link ProductUnit}), then the corresponding
     * unit is created if not already defined.
     * <p> Examples of valid entries (all for meters per second squared) are:
     *     <code><ul>
     *       <li>m*s-2</li>
     *       <li>m/s²</li>
     *       <li>m·s-²</li>
     *       <li>m*s**-2</li>
     *       <li>m^+1 s^-2</li>
     *       <li>m&lt;sup&gt;1&lt;/sup&gt;·s&lt;sup&gt;-2&lt;/sup&gt;</li>
     *     </ul></code></p>
     *
     * @param  csq the character sequence to parse.
     * @return <code>UnitFormat.current().parse(csq)</code>
     * @throws IllegalArgumentException if the specified character sequence
     *         cannot be correctly parsed (e.g. symbol unknown).
     * @see    UnitFormat#current
     */
    public static Unit valueOf(CharSequence csq) {
        return (Unit) UnitFormat.current().parse(csq);
    }

    /**
     * Returns a read-only list of the currently-defined units.
     * The collection returned is backed by the actual collection of units
     * -- so it grows as more units are defined.
     *
     * @return an unmodifiable view of the units collection.
     */
    public static Collection getInstances() {
        return Collections.unmodifiableCollection(UNITS.values());
    }

    /**
     * Retrieves a unit from its symbol.
     *
     *  <p> Note: Unlike {@link #valueOf(CharSequence)}, this method does not
     *            parse the given string (it does not raise an exception either
     *            if the specified symbol is not yet defined).</p>
     *
     * @param  symbol the symbol searched for.
     * @return the unit with the specified symbol or <code>null</code>
     *         if such unit cannot be found.
     */
    public static Unit searchSymbol(CharSequence symbol) {
        synchronized (Unit.class) {
            return (Unit) SYMBOLS.get(symbol);
        }
    }

    /**
     * Returns the current dimension for this unit (context sensitive).
     *
     * @return the unit's dimension.
     */
    abstract Unit getCtxDimension();

    /**
     * Returns the converter to this unit's current dimension
     * (context sensitive).
     *
     * @return the converter to this unit's dimension.
     */
    abstract Converter getCtxToDimension();

    //////////////////////
    // GENERAL CONTRACT //
    //////////////////////

    /**
     * Returns the textual representation of this unit.
     *
     * @return the text representation of this unit using the current format.
     * @see UnitFormat
     */
    public Text toText() {
        try {
            TextBuilder tb = TextBuilder.newInstance();
            UnitFormat.current().format(this, tb);
            return tb.toText();
        } catch (IOException ioError) {
            throw new Error(); // Should never get there.
        }
    }

    /**
     * Returns the standard <code>String</code> representation of this unit.
     *
     * @return <code>toText().toString();</code>
     */
    public final String toString() {
        return this.toText().toString();
    }

    /**
     * Indicates if this unit is equal to the object specified.
     * Units are unique and immutable, therefore users might want to use
     * <code>==</code> to test for equality.
     *
     * @param  that the object to compare for equality.
     * @return <code>true</code> if this unit and the specified object are
     *         considered equal; <code>false</code> otherwise.
     */
    public abstract boolean equals(Object that);

    /**
     * Returns the hash code value for this unit.
     *
     * @return the unit's hash code.
     */
    public final int hashCode() {
        return _hashCode;
    }

    /**
     * This method returns an {@link Unit} from the collection equals to the
     * specified template. If the unit is not found, the specified template is
     * added to the collection and returned. This method is typically used
     * by sub-classes to ensure unicity of {@link Unit} instances.
     *
     * @param  template the unit template for comparaison.
     * @return a unit from the collection equals to the specified template;
     *         or the template itself.
     * @throws UnsupportedOperationException if the template cannot be added to
     *         the collection (e.g. symbol already taken by a different unit).
     * @see    #equals
     */
    protected static Unit getInstance(Unit template) {
        synchronized (Unit.class) {

            // Checks if already exist.
            template._hashCode = template.calculateHashCode();
            Object obj = UNITS.get(template);
            if (obj != null) {
                return (Unit) obj; // Already defined.
            }

            // Registers symbol if any.
            if (template._symbol != null) {
                obj = SYMBOLS.get(template._symbol);
                if (obj != null) {
                    throw new UnsupportedOperationException("The symbol: "
                            + template._symbol
                            + " is currently associated to an instance of "
                            + obj.getClass());
                }
                SYMBOLS.put(template._symbol, template);
            }
            UNITS.put(template, template);
            template._id = UNITS.size();

            // Sets the default dimension for base unit instances.
            if (template instanceof BaseUnit) {
                ((BaseUnit) template).dimensionVariable 
                    = new LocalContext.Variable(template);
                ((BaseUnit) template).toDimensionVariable
                    = new LocalContext.Variable(Converter.IDENTITY);
            }

            return template;
        }
    }

    /**
     * Calculates the hash code of this unit (optimization).
     *
     * @return this unit's hashcode.
     */
    abstract int calculateHashCode();

    /**
     * Overrides <code>readResolve()</code> to ensure that deserialization
     * maintains unit's unicity.
     *
     * @return a new unit or an existing unit.
     */
    protected Object readResolve() {
        return getInstance(this);
    }
}