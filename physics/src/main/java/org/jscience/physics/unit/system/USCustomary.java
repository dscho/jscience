/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2010 - JScience (http://jscience.org/)
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.unit.system;

import java.util.Set;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.jscience.physics.unit.PhysicsUnit;
import org.unitsofmeasurement.quantity.Area;
import org.unitsofmeasurement.quantity.Length;
import org.unitsofmeasurement.quantity.Mass;
import org.unitsofmeasurement.quantity.Quantity;
import org.unitsofmeasurement.quantity.Temperature;
import org.unitsofmeasurement.quantity.Velocity;
import org.unitsofmeasurement.quantity.Volume;
import org.unitsofmeasurement.unit.Dimension;
import org.unitsofmeasurement.unit.SystemOfUnits;

/**
 * <p> This class contains units from the United States customary system.</p>
 *
 * <p> The US customary system uses {@link #getUnit(java.lang.Class) units} different
 *     from {@link SI} for physical quantities. For example "[lb_i]", "[ft_i]"
 *     and "[degF]"  instead of "kg", "m" and "K" for {@link Mass}, {@link Length}
 *     and {@link Temperature}.</p>
 *
 * <p> This version holds the international customary units used by both
 *     the British and US systems as well as a limited set of specific US
 *     customary units when an international equivalent does not exist.</p>
 *
 * @see <a href="http://en.wikipedia.org/wiki/United_States_customary_units">Wikipedia: United State Customary Units</a>
 *
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 5.0, October 12, 2010
 */
public final class USCustomary implements SystemOfUnits {

    /**
     * Holds the units.
     */
    private final FastSet<PhysicsUnit> units = new FastSet<PhysicsUnit>();

    /**
     * Holds the mapping quantity to unit.
     */
    private final FastMap<Class<? extends Quantity>, PhysicsUnit>
            quantityToUnit = new FastMap<Class<? extends Quantity>, PhysicsUnit>();

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
    public static final PhysicsUnit<Length> METER = SI.METRE;

    /**
     * US name for {@link SI#LITRE}.
     */
    public static final PhysicsUnit<Volume> LITER = SI.LITRE;

    //
    // International customary units
    //

    /**
     * The unit {@link UCUM#INCH_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> INCH = addUnit(UCUM.INCH_INTERNATIONAL);

    /**
     * The unit {@link UCUM#FOOT_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> FOOT = addUnit(UCUM.FOOT_INTERNATIONAL);

    /**
     * The unit {@link UCUM#YARD_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> YARD = addUnit(UCUM.YARD_INTERNATIONAL);

    /**
     * The unit {@link UCUM#MILE_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> STATUTE_MILE = addUnit(UCUM.MILE_INTERNATIONAL);

    /**
     * The unit {@link UCUM#FATHOM_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> FATHOM = addUnit(UCUM.FATHOM_INTERNATIONAL);

    /**
     * The unit {@link UCUM#NAUTICAL_MILE_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> NAUTICAL_MILE = addUnit(UCUM.NAUTICAL_MILE_INTERNATIONAL);

    /**
     * The unit {@link UCUM#KNOT_INTERNATIONAL}
     */
    public static final PhysicsUnit<Velocity> KNOT = addUnit(UCUM.KNOT_INTERNATIONAL);

    /**
     * The unit {@link UCUM#SQUARE_INCH_INTERNATIONAL}
     */
    public static final PhysicsUnit<Area> SQUARE_INCH = addUnit(UCUM.SQUARE_INCH_INTERNATIONAL);

    /**
     * The unit {@link UCUM#SQUARE_FOOT_INTERNATIONAL}
     */
    public static final PhysicsUnit<Area> SQUARE_FOOT = addUnit(UCUM.SQUARE_FOOT_INTERNATIONAL);

    /**
     * The unit {@link UCUM#SQUARE_YARD_INTERNATIONAL}
     */
    public static final PhysicsUnit<Area> SQUARE_YARD = addUnit(UCUM.SQUARE_YARD_INTERNATIONAL);

    /**
     * The unit {@link UCUM#CUBIC_INCH_INTERNATIONAL}
     */
    public static final PhysicsUnit<Volume> CUBIC_INCH = addUnit(UCUM.CUBIC_INCH_INTERNATIONAL);

    /**
     * The unit {@link UCUM#CUBIC_FOOT_INTERNATIONAL}
     */
    public static final PhysicsUnit<Volume> CUBIC_FOOT = addUnit(UCUM.CUBIC_FOOT_INTERNATIONAL);

    /**
     * The unit {@link UCUM#CUBIC_YARD_INTERNATIONAL}
     */
    public static final PhysicsUnit<Volume> CUBIC_YARD = addUnit(UCUM.CUBIC_YARD_INTERNATIONAL);

    /**
     * The unit {@link UCUM#BOARD_FOOT_INTERNATIONAL}
     */
    public static final PhysicsUnit<Volume> BOARD_FOOT = addUnit(UCUM.BOARD_FOOT_INTERNATIONAL);

    /**
     * The unit {@link UCUM#MIL_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> MIL = addUnit(UCUM.MIL_INTERNATIONAL);

    /**
     * The unit {@link UCUM#CIRCULAR_MIL_INTERNATIONAL}
     */
    public static final PhysicsUnit<Area> CIRCULAR_MIL = addUnit(UCUM.CIRCULAR_MIL_INTERNATIONAL);

    /**
     * The unit {@link UCUM#HAND_INTERNATIONAL}
     */
    public static final PhysicsUnit<Length> HAND = addUnit(UCUM.HAND_INTERNATIONAL);

    // ////////
    // Mass //
    // ////////

    /**
     * The unit {@link UCUM#POUND}
     */
    public static final PhysicsUnit<Mass> POUND = addUnit(UCUM.POUND);

    /**
     * A unit of mass equal to <code>1 / 16 {@link #POUND}</code> (standard name <code>oz</code>).
     */
    public static final PhysicsUnit<Mass> OUNCE = addUnit(POUND.divide(16));

    /**
     * A unit of mass equal to <code>2000 {@link #POUND}</code> (short ton, standard name
     * <code>ton</code>).
     */
    public static final PhysicsUnit<Mass> TON = addUnit(POUND.multiply(2000));


    // ///////////////
    // Temperature //
    // ///////////////

  /**
     * A unit of temperature equal to <code>5/9 °K</code> (standard name
     * <code>°R</code>).
     */
    public static final PhysicsUnit<Temperature> RANKINE = addUnit(SI.KELVIN.multiply(5).divide(9));

    /**
     * The unit {@link UCUM#FAHRENHEIT}
     */
    public static final PhysicsUnit<Temperature> FAHRENHEIT = addUnit(UCUM.FAHRENHEIT);

    // ////////////
    // Velocity //
    // ////////////

    /**
     * A unit of velocity expressing the number of {@link #FOOT feet} per
     * {@link SI#SECOND second}.
     */
    public static final PhysicsUnit<Velocity> FEET_PER_SECOND = addUnit(FOOT.divide(SI.SECOND)).asType(Velocity.class);

    /**
     * A unit of velocity expressing the number of international {@link #NAUTICAL_MILE
     * nautical miles} per {@link SI#HOUR hour} (abbreviation <code>mph</code>).
     */
    public static final PhysicsUnit<Velocity> MILES_PER_HOUR = addUnit(
            NAUTICAL_MILE.divide(SI.HOUR).asType(Velocity.class), Velocity.class);

    // ////////
    // Area //
    // ////////

    /**
     * The unit {@link UCUM#ACRE_US_SURVEY}
     */
    public static final PhysicsUnit<Area> ACRE = addUnit(UCUM.ACRE_US_SURVEY);

    /**
     * The unit {@link UCUM#ARE}
     */
    public static final PhysicsUnit<Area> ARE = addUnit(UCUM.ARE);


    // //////////
    // Volume //
    // //////////
    
    /**
     * The unit {@link UCUM#GALLON_US}
     */
    public static final PhysicsUnit<Volume> GALLON = addUnit(UCUM.GALLON_US);

    /**
     * A unit of volume equal to <code>1 / 128 {@link #GALLON}</code>.
     */
    public static final PhysicsUnit<Volume> OUNCE_LIQUID = addUnit(GALLON.divide(128));

    /**
     * The unit {@link UCUM#MINIM_US}
     */
    public static final PhysicsUnit<Volume> MINIM = addUnit(UCUM.MINIM_US);

    /**
     * The unit {@link UCUM#FLUID_DRAM_US}
     */
    public static final PhysicsUnit<Volume> FLUID_DRAM = addUnit(UCUM.FLUID_DRAM_US);

    /**
     * The unit {@link UCUM#TEASPOON_US}
     */
    public static final PhysicsUnit<Volume> TEASPOON = addUnit(UCUM.TEASPOON_US);

    /**
     * The unit {@link UCUM#TABLESPOON_US}
     */
    public static final PhysicsUnit<Volume> TABLESPOON = addUnit(UCUM.TABLESPOON_US);

    
    /**
     * The unit {@link UCUM#BARREL_US}
     */
    public static final PhysicsUnit<Volume> BARREL = addUnit(UCUM.BARREL_US);


    /////////////////////
    // Collection View //
    /////////////////////

    @Override
    public String getName() {
        return "USCustomary";
    }

    @Override
    public Set<? extends PhysicsUnit> getUnits() {
        return units.unmodifiable();
    }

    @Override
    public <Q extends Quantity<Q>> PhysicsUnit<Q> getUnit(Class<Q> quantityType) {
        return quantityToUnit.get(quantityType);
    }

    @Override
    public Set<? extends PhysicsUnit> getUnits(Dimension dimension) {
        FastSet<PhysicsUnit> set = FastSet.newInstance();
        for (PhysicsUnit unit : this.getUnits()) {
            if (dimension.equals(unit.getDimension())) {
                set.add(unit);
            }
        }
        return set;
    }

    /**
     * Adds a new unit not mapped to any specified quantity type.
     *
     * @param  unit the unit being added.
     * @return <code>unit</code>.
     */
    private static <U extends PhysicsUnit<?>>  U addUnit(U unit) {
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
    private static <U extends PhysicsUnit<?>>  U addUnit(U unit, Class<? extends Quantity> type) {
        INSTANCE.units.add(unit);
        INSTANCE.quantityToUnit.put(type, unit);
        return unit;
    }

}
