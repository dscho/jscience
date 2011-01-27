/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit;

import static org.jscience.physics.unit.SI.*;

import java.util.Set;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.jscience.physics.unit.converter.RationalConverter;
import org.unitsofmeasurement.quantity.Area;
import org.unitsofmeasurement.quantity.Length;
import org.unitsofmeasurement.quantity.Mass;
import org.unitsofmeasurement.quantity.Quantity;
import org.unitsofmeasurement.quantity.Temperature;
import org.unitsofmeasurement.quantity.Velocity;
import org.unitsofmeasurement.quantity.Volume;

/**
 * <p> This class contains units from the United States customary system.</p>
 *
 * <p> The standard symbol/names used for US customary units are case sensitive
 *     <a href="http://aurora.regenstrief.org/~ucum/ucum.html">UCUM</a> names.</p>
 *
 * <p> The US customary system uses {@link #getUnit(java.lang.Class) units} different
 *     from {@link SI} for physical quantities. For example "[lb_i]", "[ft_i]"
 *     and "[degF]"  instead of "kg", "m" and "K" for {@link Mass}, {@link Length}
 *     and {@link Temperature}.</p>
 *
 * <p> This version holds all the international customary units used by both
 *     the British and US systems as well as a limited set of US customary units.
 *     This set will be completed in future releases.</p>
 *
 * @see <a href="http://en.wikipedia.org/wiki/United_States_customary_units">Wikipedia: United State Customary Units</a>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class USCustomary extends PhysicalSystemOfUnits {

    /**
     * Holds the units.
     */
    private final FastSet<PhysicalUnit> units = new FastSet<PhysicalUnit>();

    /**
     * Holds the mapping quantity to unit.
     */
    private final FastMap<Class<? extends Quantity>, PhysicalUnit>
            quantityToUnit = new FastMap<Class<? extends Quantity>, PhysicalUnit>();

    /**
     * The singleton instance.
     */
    private static final USCustomary INSTANCE = new USCustomary();

    /**
     * Default constructor (prevents this class from being instantiated).
     */
    private USCustomary() {
    }

    /**
     * Returns the unique instance of this class.
     *
     * @return the USCustomary instance.
     */
    public static USCustomary getInstance() {
        return INSTANCE;
    }

    /**
     * US name for {@link SI#METRE}.
     */
    public static final PhysicalUnit<Length> METER = METRE;

    /**
     * US name for {@link SI#LITRE}.
     */
    public static final PhysicalUnit<Volume> LITER = LITRE;

    //
    // International customary units
    //

    /**
     * A US customary unit for length quantities equals to <code>2.54 cm</code>
     * (standard name <code>[in_i]</code>).
     */
    public static final TransformedUnit<Length> INCH
           = addUnit(new TransformedUnit<Length>(METER, new RationalConverter(254, 10000)), Length.class);

    /**
     * A unit of length equals to <code>12 [in_i]</code> (standard name
     * <code>[ft_i]</code>).
     */
    public static final TransformedUnit<Length> FOOT
           = addUnit(new TransformedUnit<Length>(METER, new RationalConverter(254 * 12, 10000)));

    /**
     * A unit of length equals to <code>3 [ft_i]</code> (standard name
     * <code>[yd_i]</code>).
     */
    public static final TransformedUnit<Length> YARD
           = addUnit(new TransformedUnit<Length>(METER, new RationalConverter(254 * 12 * 3, 10000)));

    /**
     * A unit of length equals to <code>5280 [ft_i]</code> (standard name
     * <code>[mi_i]</code>).
     */
    public static final TransformedUnit<Length> STATUTE_MILE
           = addUnit(new TransformedUnit<Length>(METER, new RationalConverter(254 * 12 * 5280L, 10000)));

    /**
     * A unit of depth of water equals to <code>6 [ft_i]</code> (standard name
     * <code>[fth_i]</code>).
     */
    public static final TransformedUnit<Length> FATHOM
           = addUnit(new TransformedUnit<Length>(METER, new RationalConverter(254 * 12 * 6, 10000)));

    /**
     * A unit of length equals to <code>1852 m</code> (standard name
     * <code>[nmi_i]</code>).
     */
    public static final TransformedUnit<Length> NAUTICAL_MILE
           = addUnit(new TransformedUnit<Length>(METER, new RationalConverter(1852, 1)));

    /**
     * A unit of velocity equals to <code>1 [nmi_i]/h</code> (standard name
     * <code>[kn_i]</code>).
     */
    public static final ProductUnit<Velocity> KNOT
            = addUnit(new ProductUnit<Velocity>(NAUTICAL_MILE.divide(HOUR)));

    /**
     * A unit of area equals to <code>1 [in_i]2</code> (standard name
     * <code>[sin_i]</code>).
     */
    public static final ProductUnit<Area> SQUARE_INCH
            = addUnit(new ProductUnit<Area>(INCH.pow(2)));

    /**
     * A unit of area equals to <code>1 [ft_i]2</code> (standard name
     * <code>[sft_i]</code>).
     */
    public static final ProductUnit<Area> SQUARE_FOOT
            = addUnit(new ProductUnit<Area>(FOOT.pow(2)));

    /**
     * A unit of area equals to <code>1 [yd_i]2</code> (standard name
     * <code>[syd_i]</code>).
     */
    public static final ProductUnit<Area> SQUARE_YARD
            = addUnit(new ProductUnit<Area>(YARD.pow(2)));

    /**
     * A unit of volume equals to <code>1 [in_i]3</code> (standard name
     * <code>[cin_i]</code>).
     */
    public static final ProductUnit<Volume> CUBIC_INCH
            = addUnit(new ProductUnit<Volume>(INCH.pow(3)));

    /**
     * A unit of volume equals to <code>1 [ft_i]3</code> (standard name
     * <code>[cft_i]</code>).
     */
    public static final ProductUnit<Volume> CUBIC_FOOT
            = addUnit(new ProductUnit<Volume>(FOOT.pow(3)));

    /**
     * A unit of volume equals to <code>1 [yd_i]3</code> (standard name
     * <code>[cyd_i]</code>).
     */
    public static final ProductUnit<Volume> CUBIC_YARD
            = addUnit(new ProductUnit<Volume>(YARD.pow(3)));

    /**
     * A unit of volume equals to <code>144 [in_i]3</code> (standard name
     * <code>[bf_i]</code>).
     */
    public static final TransformedUnit<Volume> BOARD_FOOT
            = addUnit(new TransformedUnit<Volume>(CUBIC_INCH, new RationalConverter(144, 1)));

    /**
     * A unit of volume equals to <code>128 [ft_i]3</code> (standard name
     * <code>[cr_i]</code>).
     */
    public static final TransformedUnit<Volume> CORD
            = addUnit(new TransformedUnit<Volume>(CUBIC_FOOT, new RationalConverter(128, 1)));

    /**
     * A unit of length equals to <code>1 × 10-3 [in_i]</code> (standard name
     * <code>[mil_i]</code>).
     */
    public static final TransformedUnit<Length> MIL
            = addUnit(new TransformedUnit<Length>(INCH, new RationalConverter(1, 1000)));


    // ////////
    // Mass //
    // ////////

    /**
     * A unit of mass equal to <code>453.59237 grams</code> (avoirdupois pound,
     * standard name <code>lb</code>).
     */
    public static final PhysicalUnit<Mass> POUND = addUnit(KILOGRAM.multiply(
            45359237).divide(100000000), Mass.class);

    /**
     * A unit of mass equal to <code>1 / 16 {@link #POUND}</code> (standard name <code>oz</code>).
     */
    public static final PhysicalUnit<Mass> OUNCE = addUnit(POUND.divide(16));

    /**
     * A unit of mass equal to <code>2000 {@link #POUND}</code> (short ton, standard name
     * <code>ton</code>).
     */
    public static final PhysicalUnit<Mass> TON = addUnit(POUND.multiply(2000));


    // ///////////////
    // Temperature //
    // ///////////////

  /**
     * A unit of temperature equal to <code>5/9 °K</code> (standard name
     * <code>°R</code>).
     */
    public static final PhysicalUnit<Temperature> RANKINE = addUnit(KELVIN.multiply(5).divide(9));

    /**
     * A unit of temperature equal to degree Rankine minus
     * <code>459.67 °R</code> (standard name <code>°F</code>).
     *
     * @see #RANKINE
     */
    public static final PhysicalUnit<Temperature> FAHRENHEIT = addUnit(RANKINE.add(459.67), Temperature.class);

    // ////////////
    // Velocity //
    // ////////////

    /**
     * A unit of velocity expressing the number of {@link #FOOT feet} per
     * {@link SI#SECOND second}.
     */
    public static final PhysicalUnit<Velocity> FEET_PER_SECOND = addUnit(
            FOOT.divide(SECOND)).asType(Velocity.class);

    /**
     * A unit of velocity expressing the number of international {@link #NAUTICAL_MILE
     * miles} per {@link #HOUR hour} (abbreviation <code>mph</code>).
     */
    public static final PhysicalUnit<Velocity> MILES_PER_HOUR = addUnit(
            NAUTICAL_MILE.divide(HOUR), Velocity.class).asType(Velocity.class);

    // ////////
    // Area //
    // ////////

      /**
     * A unit of area equal to <code>43 560 square feet </code> 
     * (standard name <code>[acr_us]</code>).
     */
    public static final PhysicalUnit<Area> ACRE = addUnit(SQUARE_FOOT.multiply(43560));

    /**
     * A unit of area equal to <code>100 m²</code> (standard name <code>a</code>).
     */
    public static final PhysicalUnit<Area> ARE = addUnit(SQUARE_METRE.multiply(100));


    // //////////
    // Volume //
    // //////////
    
    /**
     * A unit of volume equal to one US dry gallon. (standard name
     * <code>gallon_dry_us</code>).
     */
    public static final PhysicalUnit<Volume> GALLON_DRY = addUnit(CUBIC_INCH.multiply(
            2688025).divide(10000));
    
    /**
     * A unit of volume equal to one US gallon, Liquid PhysicalUnit. The U.S. liquid
     * gallon is based on the Queen Anne or Wine gallon occupying 231 cubic
     * inches (standard name <code>gal</code>).
     */
    public static final PhysicalUnit<Volume> GALLON_LIQUID = addUnit(CUBIC_INCH.multiply(231));

    /**
     * A unit of volume equal to <code>1 / 128 {@link #GALLON_LIQUID}</code> (standard name
     * <code>oz_fl</code>).
     */
    public static final PhysicalUnit<Volume> OUNCE_LIQUID = addUnit(GALLON_LIQUID.divide(128));

    /**
     * A unit of volume <code>~ 1 drop or 0.95 grain of water </code> (standard name
     * <code>min</code>).
     */
    public static final PhysicalUnit<Volume> MINIM = addUnit(SIPrefix.MICRO(LITER).multiply(61.61152d));

    /**
     * A unit of volume equal to <code>60 {@link #MINIM}</code> (standard name
     * <code>fl dr</code>).
     */
    public static final PhysicalUnit<Volume> FLUID_DRAM = addUnit(MINIM.multiply(60));

    /**
     * A unit of volume equal to <code>80 {@link #MINIM}</code> (standard name
     * <code>tsp</code>).
     */
    public static final PhysicalUnit<Volume> TEASPOON = addUnit(MINIM.multiply(80));

    /**
     * A unit of volume equal to <code>3 {@link #TEASPOON}</code> (standard name
     * <code>Tbsp</code>).
     */
    public static final PhysicalUnit<Volume> TABLESPOON = addUnit(TEASPOON.multiply(3));

    
    /**
     * A unit of volume equal to <code>238.4810 {@link #LITER}</code> (standard name
     * <code>bbl</code>).
     */
    public static final PhysicalUnit<Volume> OIL_BARREL = addUnit(LITER.multiply(238.4810d));


    /////////////////////
    // Collection View //
    /////////////////////

    @Override
    public String getName() {
        return "USCustomary";
    }

    @Override
    public Set<? extends PhysicalUnit> getUnits() {
        return units.unmodifiable();
    }

    @Override
    public <Q extends Quantity<Q>> PhysicalUnit<Q> getUnit(Class<Q> quantityType) {
        PhysicalUnit<Q> unit = quantityToUnit.get(quantityType);
        // If not found, returns the SI one.
        return (unit == null) ? SI.getInstance().getUnit(quantityType) : unit;
    }

    /**
     * Adds a new unit not mapped to any specified quantity type.
     *
     * @param  unit the unit being added.
     * @return <code>unit</code>.
     */
    private static <U extends PhysicalUnit<?>>  U addUnit(U unit) {
        INSTANCE.units.add(unit);
        return unit;
    }

    /**
     * Adds a new unit and maps it to the specified quantity type.
     *
     * @param  unit the unit being added.
     * @param type the quantity type.
     * @return <code>unit</code>.
     */
    private static <U extends PhysicalUnit<?>>  U addUnit(U unit, Class<? extends Quantity> type) {
        INSTANCE.units.add(unit);
        INSTANCE.quantityToUnit.put(type, unit);
        return unit;
    }


}
