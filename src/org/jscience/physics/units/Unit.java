/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;

import java.io.Serializable;
import java.util.Set;

import org.jscience.physics.quantities.Angle;
import org.jscience.physics.quantities.Quantity;
import org.jscience.physics.quantities.Dimensionless;

import javolution.util.FastComparator;
import javolution.util.FastMap;
import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;
import javolution.lang.MathLib;
import javolution.lang.PersistentReference;
import javolution.lang.Text;

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
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author  <a href="mailto:steve@unidata.ucar.edu">Steve Emmerson</a>
 * @author  Martin Desruisseaux
 * @version 1.1, May 24, 2005
 */
public abstract class Unit<Q extends Quantity> implements Serializable {

    /**
     * Holds the unit collection.
     */
    private static final FastMap<Unit, Unit> COLLECTION = new FastMap<Unit, Unit>(
            1024).setKeyComparator(new Unit.Comparator());

    /**
     * Holds the symbol collection (symbol-unit mapping).
     */
    static final FastMap<String, Unit> SYMBOLS = new FastMap<String, Unit>(256)
            .setKeyComparator(FastComparator.LEXICAL);

    /**
     * Holds the default XML representation for units.
     * This representation consists of a <code>"name"</code> attribute 
     * holding the textual representation of the unit.
     */
    private static final XmlFormat<Unit> UNIT_XML = new XmlFormat<Unit>(
            Unit.class) {
        public void format(Unit unit, XmlElement xml) {
            xml.setAttribute("name", unit.toText());
        }

        public Unit parse(XmlElement xml) {
            return Unit.valueOf(xml.getAttribute("name", Text.EMPTY));
        }
    };

    /**
     * Holds the units multiplication table which might be persistent.
     */
    private static final PersistentReference<FastMap<Unit, FastMap<Unit, Unit>>> MULT_TABLE = new PersistentReference<FastMap<Unit, FastMap<Unit, Unit>>>(
            "org.jscience.physics.units.Unit#MULT_TABLE",
            new FastMap<Unit, FastMap<Unit, Unit>>().setKeyComparator(FastComparator.DIRECT));

    /**
     * Holds the dimensionless unit <code>ONE</code>.
     */
    public static final Unit<Dimensionless> ONE = new ProductUnit<Dimensionless>();
    static {
        COLLECTION.put(ONE, ONE);
        ONE._hashCode = ONE.hashCodeImpl();
        ONE._parentUnit = ONE;
        ONE._toParentUnit = Converter.IDENTITY;
    }

    /**
     * Holds the inverse of this unit (if known).
     */
    private transient Unit _inverse;

    /**
     * Holds the hashcode for this unit (calculated once, when the unit
     * is added to the collection).
     */
    private transient int _hashCode;

    /**
     * Holds this unit associated symbol ("" if none).
     */
    String _symbol;

    /**
     * Holds the parent unit for this unit.
     */
    Unit<? super Q> _parentUnit;

    /**
     * Holds the converter to base units.
     */
    Converter _toParentUnit;

    /**
     * Default constructor.
     */
    Unit() {
    }

    //////////////////////////////////////////////////////
    // Contract methods (for sub-classes to implement). //
    //////////////////////////////////////////////////////

    /**
     * Returns the hash code for this unit (calculated only once as units 
     * are unique).
     *
     * @return this unit hashcode value.
     */
    protected abstract int hashCodeImpl();

    /**
     * Indicates if the specified unit can be considered equals to 
     * the one specified (for unicity purpose).
     *
     * @return <code>true</code> if this unit is considered equal to 
     *         that unit; <code>false</code> otherwise.
     */
    protected abstract boolean equalsImpl(Object that);

    /**
     * Returns the parent units this unit is derived from.
     *
     * @return the parent unit or <code>this</code> if this unit is 
     *         a base unit or a product of base units.
     */
    protected abstract Unit< ? super Q> getParentUnitImpl();

    /**
     * Returns the converter to the parent unit.
     *
     * @return the converter to {@link #getParentUnitImpl()}
     */
    protected abstract Converter toParentUnitImpl();

    /////////////////////
    // Public methods. //
    /////////////////////

    /**
     * Returns the unit this unit is derived from.
     *
     * <p><i> Note: Having the same parent unit is not sufficient to ensure
     *              that a converter exists between the two units
     *              (e.g. °C/m and K/m).</i></p>
     * @return the parent unit or <code>this</code> if this unit is 
     *         a base unit or a product of base units.
     */
    public final Unit<? super Q>  getParentUnit() {
        return _parentUnit;
    }

    /**
     * Returns the converter from this unit to its parent unit.
     * 
     * @return <code>this.getConverterTo(this.getParentUnit())</code>
     */
    public final Converter toParentUnit() {
        return _toParentUnit;
    }

    /**
     * Returns the {@link BaseUnit base unit} or product of base units this
     * unit is derived from. 
     *
     * <p><i> Note: Having the same base units is not sufficient to ensure
     *              that a converter exists between the two units
     *              (e.g. °C/m and K/m).</i></p>
     * @return the base units this unit is derived from.
     */
    public final Unit<? super Q>  getBaseUnits() {
        if (_parentUnit == this) return this;
        return _parentUnit.getBaseUnits();
    }

    /**
     * Returns the converter from this unit to its base units.
     * 
     * @return <code>this.getConverterTo(this.getBaseUnits())</code>
     */
    public final Converter toBaseUnits() {
        if (_parentUnit == this) return Converter.IDENTITY;
        return _parentUnit.toBaseUnits().concatenate(this._toParentUnit);
    }
    
    /**
     * Indicates if this unit is compatible with the unit specified.
     * Units don't need to be equals to be compatible. For example:
     * <pre>
     *     <code>RADIAN.equals(ONE) == false</code>
     *     <code>RADIAN.isCompatible(ONE) == true</code>.
     * </pre>
     * @param  that the other unit.
     * @return <code>this.getDimension() == that.getDimension()</code>
     */
    public final boolean isCompatible(Unit that) {
        return (this._parentUnit == that._parentUnit)
                || (this.getDimension() == that.getDimension());
    }

    /**
     * Returns the {@link javolution.realtime.LocalContext local} dimension 
     * of this unit.
     *
     * @return this unit dimension.
     * @see    BaseUnit#setDimension
     */
    public final Dimension getDimension() {
        if (_parentUnit != this)
            return _parentUnit.getDimension();
        if (_parentUnit instanceof BaseUnit)
            return ((BaseUnit< ? super Q>) _parentUnit)._dimension.get();
        ProductUnit< ? super Q> productUnit = (ProductUnit< ? super Q>) _parentUnit;
        Dimension dimension = Dimension.NONE;
        for (int i = 0; i < productUnit.size(); i++) {
            ProductUnit.Element e = productUnit.get(i);
            Dimension d = e.getUnit().getDimension().pow(e.getPow()).root(
                    e.getRoot());
            dimension = dimension.multiply(d);
        }
        return dimension;
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
        if (this == that)
            return Converter.IDENTITY;
        if (this._parentUnit == that._parentUnit)
            return that._toParentUnit.inverse().concatenate(this._toParentUnit);
        Converter thisTransform = this.getTransform();
        Converter thatTransform = that.getTransform();
        if (this.isCompatible(that))
            return thatTransform.inverse().concatenate(thisTransform);
        throw new ConversionException(this + " is not compatible with " + that
                + " in current context");
    }

    private Converter getTransform() { // Intrinsic dimensional transform.
        if (_parentUnit != this) 
            return _parentUnit.getTransform().concatenate(_toParentUnit);
        if (_parentUnit instanceof BaseUnit) {
            Converter transform = ((BaseUnit< ? super Q>) _parentUnit)._transform
                    .get();
            return transform.concatenate(_toParentUnit);
        }
        ProductUnit< ? super Q> productUnit = (ProductUnit< ? super Q>) _parentUnit;
        double factor = 1.0;
        for (int i = 0; i < productUnit.size(); i++) {
            ProductUnit.Element e = productUnit.get(i);
            Converter cvtr = e.getUnit().getTransform();
            if (!cvtr.isLinear())
                throw new ConversionException(e.getUnit()
                        + " is non-linear, cannot convert");
            factor *= MathLib.pow(cvtr.derivative(0), ((double) e.getPow())
                    / ((double) e.getRoot()));
        }
        return (MathLib.abs(factor - 1.0) < 1e-9) ? _toParentUnit
                : new MultiplyConverter(factor).concatenate(_toParentUnit);
    }

    /**
     * Returns a unit compatible to this unit except it uses the specified
     * symbol. The alternate unit can itself be used to form expressions and
     * symbols for other derived units. For example:
     * <pre><code>
     *   Unit&lt;Angle&gt; RADIAN = Unit.ONE.alternate("rad");
     *   Unit&lt;Force&gt; NEWTON = METER.multiply(KILOGRAM).divide(SECOND.pow(2)).alternate("N");
     *   Unit&lt;Pressure&gt; PASCAL = NEWTON.divide(METER.pow(2)).alternate("Pa");
     * </code></pre>
     *
     * @param  symbol the unique symbol for the alternate unit.
     * @return <code>AlternateUnit.getInstance(symbol, this)</code>
     * @throws IllegalArgumentException if the specified symbol is currently
     *         associated to a different unit.
     */
    public final <T extends Q> AlternateUnit<T> alternate(String symbol) {
        AlternateUnit<T> newUnit = new AlternateUnit<T>(symbol, _parentUnit,
                _toParentUnit);
        return (AlternateUnit<T>) getInstance(newUnit); // Ensures unicity.
    }

    /**
     * Returns the combination of this unit with the specified sub-unit.
     * Compound units are typically used for formatting purpose. 
     * Examples of compound units:<pre>
     *   HOUR_MINUTE = NonSI.HOUR.compound(NonSI.MINUTE);
     *   DEGREE_MINUTE_SECOND_ANGLE = NonSI.DEGREE_ANGLE.compound(
     *       NonSI.DEGREE_MINUTE).compound(NonSI.SECOND_ANGLE);</pre>
     *
     * @param  subunit the sub-unit to combine with this unit.
     * @return <code>CompoundUnit.getInstance(this, subUnit)</code>
     */
    public final CompoundUnit<Q> compound(Unit<Q> subunit) {
        CompoundUnit<Q> newUnit = new CompoundUnit<Q>(this, subunit);
        return (CompoundUnit<Q>) getInstance(newUnit); // Ensures unicity.
    }

    /**
     * Returns the unit derived from this unit using the specified converter.
     * The converter does not need to be linear. For example:<pre>
     * Unit&gt;Dimensionless> DECIBEL = Unit.ONE.transform(
     *     new LogConverter(10).inverse().concatenate(
     *           new MultiplyConverter(0.1))).label("dB");</pre>
     *
     * @param operation the converter from the transformed unit to this unit.
     * @return the unit after the specified transformation.
     */
    public final Unit<Q> transform(Converter operation) {
        TransformedUnit<Q> newUnit 
            = new TransformedUnit<Q>(this, operation);
        return (Unit<Q>) getInstance(newUnit); // Ensures unicity.
    }

    /**
     * Returns the result of adding an offset to this unit. The returned unit
     * is convertible with all units that are convertible with this unit.
     *
     * @param  offset the offset added (expressed in this unit,
     *         e.g. <code>CELSIUS = KELVIN.plus(273.15)</code>).
     * @return <code>this.transform(new AddConverter(offset))</code>
     */
    public final Unit<Q> plus(double offset) {
        return transform(new AddConverter(offset));
    }

    /**
     * Returns the result of multiplying this unit by a scale factor. The
     * returned unit is convertible with all units that are convertible with
     * this unit.
     *
     * @param  scale the scale factor
     *         (e.g. <code>KILOMETER = METER.multiply(1000)</code>).
     * @return <code>this.transform(new MultiplyConverter(scale))</code>
     */
    public final Unit<Q> times(double scale) {
        return transform(new MultiplyConverter(scale));
    }

    /**
     * Returns the product of this unit with the one specified.
     *
     * @param  that the unit multiplicand.
     * @return <code>this * that</code>
     */
    public final <Q extends Quantity> Unit<Q> times(Unit that) {
        FastMap<Unit, Unit> thisMult = MULT_TABLE.get().get(this);
        if (thisMult == null) {
            thisMult = new FastMap<Unit, Unit>().setKeyComparator(FastComparator.DIRECT);
            synchronized (MULT_TABLE) {
                MULT_TABLE.get().put(this, thisMult);
            }
        }
        Unit product = thisMult.get(that);
        if (product == null) {
            product = ProductUnit.getProductInstance(this, that);
            synchronized (thisMult) {
                thisMult.put(that, product);
            }
        }
        return product;
    }

    /**
     * Returns the inverse of this unit.
     *
     * @return <code>1 / this</code>
     */
    public final <Q extends Quantity> Unit<Q> inverse() {
        if (_inverse != null)
            return _inverse;
        _inverse = ProductUnit.getQuotientInstance(ONE, this);
        return _inverse;
    }

    /**
     * Returns the quotient of this unit with the one specified.
     *
     * @param  that the unit divisor.
     * @return <code>this / that</code>
     */
    public final <Q extends Quantity> Unit<Q> divide(Unit that) {
        if (that._inverse != null)
            return this.times(that._inverse);
        that._inverse = ProductUnit.getQuotientInstance(ONE, that);
        return this.times(that._inverse);
    }

    /**
     * Returns a unit equals to the given root of this unit.
     *
     * @param  n the root's order.
     * @return the result of taking the given root of this unit.
     * @throws ArithmeticException if <code>n == 0</code>.
     */
    public final <Q extends Quantity> Unit<Q> root(int n) {
        if (n > 0) {
            return ProductUnit.getRootInstance(this, n);
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
    public final <Q extends Quantity> Unit<Q> pow(int n) {
        if (n > 0) {
            return this.times(this.pow(n - 1));
        } else if (n == 0) {
            return (Unit<Q>) ONE;
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
    public Unit<Q> label(String label) {
        synchronized (UnitFormat.UNIT_TO_LABEL) {
            String prevLabel = UnitFormat.UNIT_TO_LABEL.put(this, label);
            if (prevLabel != null) {
            	// System.out.println("Replace " + prevLabel + " with " + label);
            }
            if (label != null) {
                UnitFormat.LABEL_TO_UNIT.put(label, this);
            }
            return this;
        }
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
    public Unit<Q> alias(String alias) {
        synchronized (UnitFormat.ALIAS_TO_UNIT) {
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
        return UnitFormat.current().parse(csq);
    }

    /**
     * Returns a read-only/thread safe set of the currently-defined units.
     * The collection returned is backed by the actual collection of units
     * -- so it grows as more units are defined.
     *
     * @return an unmodifiable view of the units collection.
     */
    public static Set<Unit> getInstances() {
        return Unit.COLLECTION.unmodifiable().keySet();
    }

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
        return UnitFormat.current().format(this);
    }

    /**
     * Returns the standard <code>String</code> representation of this unit.
     *
     * @return <code>toText().toString();</code>
     */
    public final String toString() {
        return toText().toString();
    }

    /**
     * This method returns an {@link Unit} from the collection 
     * identified by the specified template; if such units does not
     * exists the specified template is added to the unit collection 
     * and returned.
     *
     * @param  template the unit template for comparison.
     * @return a unit from the collection equals to the specified template;
     *         or the template itself.
     * @throws IllegalArgumentException if the template uses an intrinsic 
     *         symbol already taken by a different unit.
     * @see Unit#equalsImpl(Object)
     */
    protected static <Q extends Quantity> Unit<Q> getInstance(Unit<Q> template) {
        synchronized (COLLECTION) {
            Unit<Q> unit = COLLECTION.get(template);
            if (unit != null)
                return unit; // Already exists.
            if ((template._symbol != null)
                    && SYMBOLS.containsKey(template._symbol))
                throw new IllegalArgumentException("The symbol: "
                        + template._symbol
                        + " is currently associated to an instance of "
                        + unit.getClass());
            SYMBOLS.put(template._symbol, template);
            COLLECTION.put(template, template);
            template._hashCode = template.hashCodeImpl();
            template._parentUnit = template.getParentUnitImpl();
            template._toParentUnit = template.toParentUnitImpl();
            return template;
        }
    }

    /**
     * Indicates if this unit is equal to the object specified.
     * Units are unique and immutable, therefore users might want to use
     * <code>==</code> to test for equality.
     *
     * @param  that the object to compare for equality.
     * @return <code>this == that</code>
     */
    public final boolean equals(Object that) {
        return this == that;
    }

    /**
     * Returns the hash code value for this unit.
     *
     * @return the unit hash code.
     */
    public final int hashCode() {
        return _hashCode;
    }

    /**
     * Overrides <code>readResolve()</code> to ensure that deserialization
     * maintains unit's unicity.
     *
     * @return a new unit or an existing unit.
     */
    protected Object readResolve() {
        return getInstance(this);
    }

    /**
     * This class represents an unit comparator (used to test for unicity).
     */
    private static final class Comparator extends FastComparator<Unit> {

        public int hashCodeOf(Unit unit) {
            return unit.hashCodeImpl();
        }

        public boolean areEqual(Unit u1, Unit u2) {
            return u1.equalsImpl(u2);
        }

        public int compare(Unit u1, Unit u2) {
            throw new UnsupportedOperationException();
        }

        private static final long serialVersionUID = 1L;
    }
}