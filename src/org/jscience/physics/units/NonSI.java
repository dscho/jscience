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

/**
 * <p> This class contains units that are not part of the International
 *     System of Units, that is, they are outside the SI, but are important
 *     and widely used.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class NonSI {

    /**
     * Holds the standard gravity constant.
     */
    private static double STANDARD_GRAVITY = 9.80665; // (m/s²) exact.

    /**
     * Holds the international foot.
     */
    private static double INTERNATIONAL_FOOT = 0.3048; // (m) exact.

    /**
     * Holds the avoirdupois pound.
     */
    private static double AVOIRDUPOIS_POUND = 0.45359237; // (kg) exact.

    /**
     * Holds the Avogadro constant.
     */
    private static double AVOGADRO_CONSTANT = 6.02214199e23; // (1/mol).

    /**
     * Holds the electric charge of one electron.
     */
    private static double ELEMENTARY_CHARGE = 1.602176462e-19; // (C).

    /**
     * Default constructor (prevents this class from being instantiated).
     */
    private NonSI() {
    }

    ///////////////////
    // Dimensionless //
    ///////////////////

    /**
     * A dimensionless unit equals to <code>0.01</code> 
     * (default label <code>%</code>).
     */
    public static final Unit PERCENT = UnitFormat.label(
            Unit.ONE.multiply(0.01), "%");

    /**
     * A logarithmic unit used to describe a ratio
     * (default label <code>dB</code>).
     */
    public static final Unit DECIBEL = UnitFormat.label(TransformedUnit
            .getInstance(Unit.ONE, new LogConverter(10).inverse().concatenate(
                    new MultiplyConverter(0.1))), "dB");

    //////////////////
    // Acceleration //
    //////////////////

    /**
     * A unit of acceleration equal to an acceleration of one centimeter per
     * second per second.
     */
    public static final Unit GALILEO = SI.METER.multiply(1e-2).divide(
            SI.SECOND.pow(2));

    /**
     * A unit of acceleration equal to the gravity at the earth's surface
     * (default label <code>grav</code>).
     */
    public static final Unit G = UnitFormat.label(SI.METER.divide(
            SI.SECOND.pow(2)).multiply(STANDARD_GRAVITY), "grav");

    /////////////////////////
    // Amount of substance //
    /////////////////////////

    /**
     * A unit of amount of substance equals to one atom
     * (default label <code>atom</code>).
     */
    public static final Unit ATOM = UnitFormat.label(SI.MOLE
            .multiply(1.0 / AVOGADRO_CONSTANT), "atom");

    ///////////
    // Angle //
    ///////////

    /**
     * A unit of angle equal to a full circle or <code>2<i>&pi;</i> 
     * {@link SI#RADIAN}</code> (default label <code>rev</code>).
     */
    public static final Unit REVOLUTION = UnitFormat.label(SI.RADIAN
            .multiply(2.0 * Math.PI), "rev");

    /**
     * A unit of angle equal to <code>1/360 {@link #REVOLUTION}</code>
     * (default label <code>°</code>).
     */
    public static final Unit DEGREE_ANGLE = UnitFormat.label(REVOLUTION
            .multiply(1.0 / 360.0), "°");

    /**
     * A unit of angle equal to <code>1/60 {@link #DEGREE_ANGLE}</code>
     * (default label <code>′</code>).
     */
    public static final Unit MINUTE_ANGLE = UnitFormat.label(DEGREE_ANGLE
            .multiply(1.0 / 60.0), "′");

    /**
     *  A unit of angle equal to <code>1/60 {@link #MINUTE_ANGLE}</code>
     * (default label <code>"</code>).
     */
    public static final Unit SECOND_ANGLE = UnitFormat.label(MINUTE_ANGLE
            .multiply(1.0 / 60.0), "″");

    /**
     * A unit of angle equal to <code>0.01 {@link SI#RADIAN}</code>
     * (default label <code>centiradian</code>).
     */
    public static final Unit CENTIRADIAN = UnitFormat.label(SI.RADIAN
            .multiply(0.01), "centiradian");

    /**
     * A unit of angle measure equal to <code>1/400 {@link #REVOLUTION}</code>
     * (default label <code>grade</code>).
     */
    public static final Unit GRADE = UnitFormat.label(REVOLUTION
            .multiply(1.0 / 400.0), "grade");

    //////////
    // Area //
    //////////

    /**
     * A unit of area equal to <code>100 m²</code>
     * (default label <code>a</code>).
     */
    public static final Unit ARE = UnitFormat.label(SI.METER.pow(2).multiply(
            100), "a"); // Exact.

    /**
     * A unit of area equal to <code>100 {@link #ARE}</code>
     * (default label <code>ha</code>).
     */
    public static final Unit HECTARE = UnitFormat
            .label(ARE.multiply(100), "ha"); // Exact.

    /**
     * A unit of area equal to <code>100 fm²</code>
     * (default label <code>b</code>).
     */
    public static final Unit BARN = UnitFormat.label(SI.METER.pow(2).multiply(
            1e-28), "b"); // Exact.

    /////////////////
    // Data Amount //
    /////////////////

    /**
     * A unit of data amount equal to <code>8 {@link SI#BIT}</code>
     * (BinarY TErm, default label <code>byte</code>).
     */
    public static final Unit BYTE = UnitFormat
            .label(SI.BIT.multiply(8), "byte");

    /**
     * Equivalent {@link #BYTE}
     */
    public static final Unit OCTET = BYTE;

    //////////////
    // Duration //
    //////////////

    /**
     * A unit of duration equal to <code>60 s</code>
     * (default label <code>min</code>).
     */
    public static final Unit MINUTE = UnitFormat.label(SI.SECOND.multiply(60),
            "min");

    /**
     * A unit of duration equal to <code>60 {@link #MINUTE}</code>
     * (default label <code>h</code>).
     */
    public static final Unit HOUR = UnitFormat.label(MINUTE.multiply(60), "h");

    /**
     * A unit of duration equal to <code>24 {@link #HOUR}</code>
     * (default label <code>d</code>).
     */
    public static final Unit DAY = UnitFormat.label(HOUR.multiply(24), "day");

    /**
     * A unit of duration equal to <code>7 {@link #DAY}</code>
     * (default label <code>week</code>).
     */
    public static final Unit WEEK = UnitFormat.label(DAY.multiply(7), "week");

    /**
     * A unit of duration equal to 365 days, 5 hours, 49 minutes,
     * and 12 seconds (default label <code>year</code>).
     */
    public static final Unit YEAR = UnitFormat.label(SI.SECOND
            .multiply(31556952), "year");

    /**
     * A unit of duration equal to one twelfth of a year
     * (default label <code>month</code>).
     */
    public static final Unit MONTH = UnitFormat.label(
            YEAR.multiply(1.0 / 12.0), "month");

    /**
     * A unit of duration equal to the time required for a complete rotation of
     * the earth in reference to any star or to the vernal equinox at the
     * meridian, equal to 23 hours, 56 minutes, 4.09 seconds
     * (default label <code>day_sidereal</code>).
     */
    public static final Unit DAY_SIDEREAL = UnitFormat.label(SI.SECOND
            .multiply(86164.09), "day_sidereal");

    /**
     * A unit of duration equal to one complete revolution of the
     * earth about the sun, relative to the fixed stars, or 365 days, 6 hours,
     * 9 minutes, 9.54 seconds (default label <code>year_sidereal</code>).
     */
    public static final Unit YEAR_SIDEREAL = UnitFormat.label(SI.SECOND
            .multiply(31558149.54), "year_sidereal");

    /**
     * A unit of duration equal to <code>365 {@link #DAY}</code>
     * (default label <code>year_calendar</code>).
     */
    public static final Unit YEAR_CALENDAR = UnitFormat.label(
            DAY.multiply(365), "year_calendar");

    /**
     * A unit of duration equal to <code>1E9 {@link #YEAR}</code>.
     */
    public static final Unit AEON = YEAR.multiply(1e9);

    /////////////////////
    // Electric charge //
    /////////////////////

    /**
     * A unit of electric charge equal to the charge on one electron
     * (default label <code>e</code>).
     */
    public static final Unit E = UnitFormat.label(SI.COULOMB
            .multiply(ELEMENTARY_CHARGE), "e");

    /**
     * A unit of electric charge equal to equal to the product of Avogadro's
     * number (see {@link SI#MOLE}) and the charge (1 e) on a single electron
     * (default label <code>Fd</code>).
     */
    public static final Unit FARADAY = UnitFormat.label(SI.COULOMB
            .multiply(ELEMENTARY_CHARGE * AVOGADRO_CONSTANT), "Fd"); // e/mol

    /**
     * A unit of electric charge which exerts a force of one dyne on an equal
     * charge at a distance of one centimeter
     * (default label <code>Fr</code>).
     */
    public static final Unit FRANKLIN = UnitFormat.label(SI.COULOMB
            .multiply(3.3356e-10), "Fr");

    //////////////////////
    // Electric current //
    //////////////////////

    /**
     * A unit of electric charge equal to the centimeter-gram-second
     * electromagnetic unit of magnetomotive force, equal to <code>10/4
     * &pi;ampere-turn</code> (default label <code>Gi</code>).
     */
    public static final Unit GILBERT = UnitFormat.label(SI.AMPERE
            .multiply(10.0 / (4.0 * Math.PI)), "Gi");

    ////////////
    // Energy //
    ////////////

    /**
     * A unit of energy equal to <code>1055.056 J</code>
     * (default label <code>Btu</code>).
     */
    public static final Unit BTU = UnitFormat.label(
            SI.JOULE.multiply(1055.056), "Btu");

    /**
     * A unit of energy equal to <code>1054.350 J</code>, thermochemical
     * (default label <code>Btu_th</code>).
     */
    public static final Unit BTU_TH = UnitFormat.label(SI.JOULE
            .multiply(1054.350), "Btu_th");

    /**
     * A unit of energy equal to <code>1055.87 J</code>, mean
     * (default label <code>Btu_mean</code>).
     */
    public static final Unit BTU_MEAN = UnitFormat.label(SI.JOULE
            .multiply(1055.87), "Btu_mean");

    /**
     * A unit of energy equal to <code>4.1868 J</code>
     * (default label <code>cal</code>).
     */
    public static final Unit CALORIE = UnitFormat.label(SI.JOULE
            .multiply(4.1868), "cal");

    /**
     * A unit of energy equal to one kilo-calorie
     * (default label <code>Cal</code>).
     */
    public static final Unit KILOCALORIE = UnitFormat.label(CALORIE
            .multiply(1e3), "Cal");

    /**
     * A unit of energy equal to <code>1E-7 J</code>
     * (default label <code>erg</code>).
     */
    public static final Unit ERG = UnitFormat.label(SI.JOULE.multiply(1e-7),
            "erg");

    /**
     * A unit of energy equal to one electron-volt (default label 
     * <code>eV</code>, also recognized <code>keV, MeV, GeV</code>).
     */
    public static final Unit ELECTRON_VOLT = UnitFormat.label(SI.JOULE
            .multiply(ELEMENTARY_CHARGE), "eV");
    static {
        UnitFormat.label(ELECTRON_VOLT.multiply(1e3), "keV");
        UnitFormat.label(ELECTRON_VOLT.multiply(1e6), "MeV");
        UnitFormat.label(ELECTRON_VOLT.multiply(1e9), "GeV");
    }

    /**
     * A unit of energy equal to <code>105.4804E6 J</code> (approximation, 
     * default label <code>thm</code>).
     */
    public static final Unit THERM = UnitFormat.label(SI.JOULE
            .multiply(105.4804e6), "thm"); // Appr.

    /////////////////
    // Illuminance //
    /////////////////

    /**
     * A unit of illuminance equal to <code>1E4 Lx</code>
     * (default label <code>La</code>).
     */
    public static final Unit LAMBERT = UnitFormat.label(SI.LUX.multiply(1e4),
            "La");

    ////////////
    // Length //
    ////////////

    /**
     * A unit of length equal to <code>0.3048 m</code> 
     * (default label <code>ft</code>).
     */
    public static final Unit FOOT = UnitFormat.label(SI.METER
            .multiply(INTERNATIONAL_FOOT), "ft");

    /**
     * A unit of length equal to <code>0.9144 m</code>
     * (default label <code>yd</code>).
     */
    public static final Unit YARD = UnitFormat.label(FOOT.multiply(3), "yd");

    /**
     * A unit of length equal to <code>0.0254 m</code> 
     * (default label <code>in</code>).
     */
    public static final Unit INCH = UnitFormat.label(FOOT.multiply(1.0 / 12.0),
            "in");

    /**
     * A unit of length equal to <code>1609.344 m</code>
     * (default label <code>mi</code>).
     */
    public static final Unit MILE = UnitFormat.label(SI.METER
            .multiply(1609.344), "mi");

    /**
     * A unit of length equal to <code>1852.0 m</code>
     * (default label <code>nmi</code>).
     */
    public static final Unit NAUTICAL_MILE = UnitFormat.label(SI.METER
            .multiply(1852.0), "nmi");

    /**
     * A unit of length equal to <code>1E-10 m</code>
     * (default label <code>Å</code>).
     */
    public static final Unit ANGSTROM = UnitFormat.label(SI.METER
            .multiply(1e-10), "Å");

    /**
     * A unit of length equal to the average distance from the center of the
     * Earth to the center of the Sun (default label <code>ua</code>).
     */
    public static final Unit ASTRONOMICAL_UNIT = UnitFormat.label(SI.METER
            .multiply(149597870691.0), "ua");

    /**
     * A unit of length equal to the distance that light travels in one year
     * through a vacuum (default label <code>ly</code>).
     */
    public static final Unit LIGHT_YEAR = UnitFormat.label(SI.METER
            .multiply(9.460528405e15), "ly");

    /**
     * A unit of length equal to the distance at which a star would appear to
     * shift its position by one arcsecond over the course the time
     * (about 3 months) in which the Earth moves a distance of
     * {@link #ASTRONOMICAL_UNIT} in the direction perpendicular to the
     * direction to the star (default label <code>pc</code>).
     */
    public static final Unit PARSEC = UnitFormat.label(SI.METER
            .multiply(30856770e9), "pc");

    /**
     * A unit of length equal to the mean distance between the proton
     * and the electron in an unexcited hydrogen atom
     * (default label <code>Bohr</code>).
     */
    public static final Unit BOHR = UnitFormat.label(SI.METER
            .multiply(52.918e-12), "Bohr");

    /**
     * A unit of length equal to <code>0.013837 {@link #INCH}</code> exactly
     * (default label <code>pt</code>).
     * @see     #PIXEL
     */
    public static final Unit POINT = UnitFormat.label(INCH.multiply(0.013837),
            "pt");

    /**
     * A unit of length equal to <code>1/72 {@link #INCH}</code>
     * (default label <code>pixel</code>).
     * It is the American point rounded to an even 1/72 inch.
     * @see     #POINT
     */
    public static final Unit PIXEL = UnitFormat.label(
            INCH.multiply(1.0 / 72.0), "pixel");

    /**
     * Equivalent {@link #PIXEL}
     */
    public static final Unit COMPUTER_POINT = PIXEL;

    /**
     * A unit of length equal to <code>12 {@link #POINT}</code>
     * (default label <code>pi</code>).
     * @see     #POINT
     */
    public static final Unit PICA = UnitFormat.label(POINT.multiply(12), "pi");

    /**
     * A unit of length equal to <code>0.37592e-3 m</code>
     * (default label <code>Didot</code>).
     */
    public static final Unit DIDOT = UnitFormat.label(SI.METER
            .multiply(0.37592e-3), "Didot");

    /**
     * A unit of length equal to <code>12 {@link #DIDOT}</code>
     * (default label <code>cicero</code>).
     */
    public static final Unit CICERO = UnitFormat.label(DIDOT.multiply(12),
            "cicero");

    ///////////////////
    // Magnetic Flux //
    ///////////////////

    /**
     * A unit of magnetic flux equal <code>1E-8 Wb</code>
     * (default label <code>Mx</code>).
     */
    public static final Unit MAXWELL = UnitFormat.label(
            SI.WEBER.multiply(1e-8), "Mx");

    ///////////////////////////
    // Magnetic Flux Density //
    ///////////////////////////

    /**
     * A unit of magnetic flux density equal <code>1000 A/m</code>
     * (default label <code>G</code>).
     */
    public static final Unit GAUSS = UnitFormat.label(SI.TESLA.multiply(1e-4),
            "G");

    //////////
    // Mass //
    //////////

    /**
     * A unit of mass equal to 1/12 the mass of the carbon-12 atom
     * (default label <code>u</code>).
     */
    public static final Unit ATOMIC_MASS = UnitFormat.label(SI.KILOGRAM
            .multiply(1e-3 / AVOGADRO_CONSTANT), "u");

    /**
     * A unit of mass equal to the mass of the electron
     * (default label <code>me</code>).
     */
    public static final Unit ELECTRON_MASS = UnitFormat.label(SI.KILOGRAM
            .multiply(9.10938188e-31), "me");

    /**
     * A unit of mass equal to <code>200 mg</code>
     * (default label <code>carat</code>).
     */
    public static final Unit CARAT = UnitFormat.label(SI.KILOGRAM
            .multiply(200e-6), "carat");

    /**
     * A unit of mass equal to <code>453.59237 grams</code> (avoirdupois pound,
     * default label <code>lb</code>).
     */
    public static final Unit POUND = UnitFormat.label(SI.KILOGRAM
            .multiply(AVOIRDUPOIS_POUND), "lb");

    /**
     * A unit of mass equal to <code>1 / 16 {@link #POUND}</code>
     * (default label <code>oz</code>).
     */
    public static final Unit OUNCE = UnitFormat.label(POUND
            .multiply(1.0 / 16.0), "oz");

    /**
     * A unit of mass equal to <code>2000 {@link #POUND}</code> (short ton, 
     * default label <code>ton_us</code>).
     */
    public static final Unit TON_US = UnitFormat.label(POUND.multiply(2000),
            "ton_us");

    /**
     * A unit of mass equal to <code>2240 {@link #POUND}</code> (long ton,
     * default label <code>ton_uk</code>).
     */
    public static final Unit TON_UK = UnitFormat.label(POUND.multiply(2240),
            "ton_uk");

    /**
     * A unit of mass equal to <code>1000 kg</code> (metric ton,
     * default label <code>t</code>).
     */
    public static final Unit METRIC_TON = UnitFormat.label(SI.KILOGRAM
            .multiply(1000), "t");

    ///////////
    // Force //
    ///////////

    /**
     * A unit of force equal to <code>1E-5 N</code>
     * (default label <code>dyn</code>).
     */
    public static final Unit DYNE = UnitFormat.label(SI.NEWTON.multiply(1e-5),
            "dyn");

    /**
     * A unit of force equal to <code>9.80665 N</code>
     * (default label <code>kgf</code>).
     */
    public static final Unit KILOGRAM_FORCE = UnitFormat.label(SI.NEWTON
            .multiply(STANDARD_GRAVITY), "kgf");

    /**
     * A unit of force equal to <code>{@link #POUND}·{@link #G}</code>
     * (default label <code>lbf</code>).
     */
    public static final Unit POUND_FORCE = UnitFormat.label(SI.NEWTON
            .multiply(AVOIRDUPOIS_POUND * STANDARD_GRAVITY), "lbf");

    ///////////
    // Power //
    ///////////

    /**
     * A unit of power equal to the power required to raise a mass of 75
     * kilograms at a velocity of 1 meter per second (metric,
     * default label <code>hp</code>).
     */
    public static final Unit HORSEPOWER = UnitFormat.label(SI.WATT
            .multiply(735.499), "hp");

    //////////////
    // Pressure //
    //////////////

    /**
     * A unit of pressure equal to the average pressure of the Earth's
     * atmosphere at sea level (default label <code>atm</code>).
     */
    public static final Unit ATMOSPHERE = UnitFormat.label(SI.PASCAL
            .multiply(101325), "atm");

    /**
     * A unit of pressure equal to <code>100 kPa</code>
     * (default label <code>bar</code>).
     */
    public static final Unit BAR = UnitFormat.label(SI.PASCAL.multiply(100e3),
            "bar");

    /**
     * A unit of pressure equal to <code>1E-3 {@link #BAR}</code>
     * (default label <code>mbar</code>).
     */
    public static final Unit MILLIBAR = UnitFormat.label(BAR
            .multiply(1.0 / 1000.0), "mbar");

    /**
     * A unit of pressure equal to the pressure exerted at the Earth's
     * surface by a column of mercury 1 millimeter high
     * (default label <code>mmHg</code>).
     */
    public static final Unit MILLIMETER_OF_MERCURY = UnitFormat.label(SI.PASCAL
            .multiply(133.322), "mmHg");

    /**
     * A unit of pressure equal to the pressure exerted at the Earth's
     * surface by a column of mercury 1 inch high
     * (default label <code>inHg</code>).
     */
    public static final Unit INCH_OF_MERCURY = UnitFormat.label(SI.PASCAL
            .multiply(3386.388), "inHg");

    /////////////////////////////
    // Radiation dose absorbed //
    /////////////////////////////

    /**
     * A unit of radiation dose absorbed equal to a dose of 0.01 joule of
     * energy per kilogram of mass (J/kg) (default label <code>rd</code>).
     */
    public static final Unit RAD = UnitFormat.label(SI.GRAY.multiply(0.01),
            "rd");

    /**
     * A unit of radiation dose effective equal to <code>0.01 Sv</code>
     * (default label <code>rem</code>).
     */
    public static final Unit REM = UnitFormat.label(SI.SIEVERT.multiply(0.01),
            "rem");

    //////////////////////////
    // Radioactive activity //
    //////////////////////////

    /**
     * A unit of radioctive activity equal to the activity of a gram of radium
     * (default label <code>Ci</code>).
     */
    public static final Unit CURIE = UnitFormat.label(SI.BECQUEREL
            .multiply(3.7e10), "Ci");

    /**
     * A unit of radioctive activity equal to 1 million radioactive
     * disintegrations per second (default label <code>Rd</code>).
     */
    public static final Unit RUTHERFORD = UnitFormat.label(SI.BECQUEREL
            .multiply(1e6), "Rd");

    /////////////////
    // Solid angle //
    /////////////////

    /**
     * A unit of solid angle equal to <code>4 <i>&pi;</i> steradians</code>
     * (default label <code>sphere</code>).
     */
    public static final Unit SPHERE = UnitFormat.label(SI.STERADIAN
            .multiply(4.0 * Math.PI), "sphere");

    /////////////////
    // Temperature //
    /////////////////

    /**
     * A unit of temperature equal to <code>5/9 °K</code>
     * (default label <code>°R</code>).
     */
    public static final Unit RANKINE = UnitFormat.label(SI.KELVIN
            .multiply(5.0 / 9.0), "°R");

    /**
     * A unit of temperature equal to degree Rankine minus 
     * <code>459.67 °R</code> (default label <code>°F</code>).
     * @see    #RANKINE
     */
    public static final Unit FAHRENHEIT = UnitFormat.label(RANKINE.add(459.67),
            "°F");

    //////////////
    // Velocity //
    //////////////

    /**
     * A unit of velocity equal to one {@link #NAUTICAL_MILE} per {@link #HOUR}.
     */
    public static final Unit KNOT = NAUTICAL_MILE.divide(HOUR);

    /**
     * A unit of velocity to express the speed of an aircraft relative to
     * the speed of sound (default label <code>Mach</code>).
     */
    public static final Unit MACH = UnitFormat.label(SI.METER.divide(SI.SECOND)
            .multiply(331.6), "Mach");

    /**
     * A unit of velocity relative to the speed of light
     * (default label <code>c</code>).
     */
    public static final Unit C = UnitFormat.label(SI.METER.divide(SI.SECOND)
            .multiply(299792458.0), "c");

    ////////////
    // Volume //
    ////////////

    /**
     * A unit of volume equal to one cubic decimeter (default label
     * <code>L</code>, also recognized <code>µL, mL, cL, dL</code>).
     */
    public static final Unit LITER = UnitFormat.label(SI.METER.pow(3).multiply(
            0.001), "L");
    static {
        UnitFormat.label(LITER.multiply(1e-6), "µL");
        UnitFormat.label(LITER.multiply(1e-3), "mL");
        UnitFormat.label(LITER.multiply(1e-2), "cL");
        UnitFormat.label(LITER.multiply(1e-1), "dL");
    }

    /**
     * A unit of volume equal to one US gallon, Liquid Unit. The U.S. liquid
     * gallon is based on the Queen Anne or Wine gallon occupying 231 cubic
     * inches (default label <code>gal</code>).
     */
    public static final Unit GALLON_LIQUID_US = UnitFormat.label(INCH.pow(3)
            .multiply(231), "gal");

    /**
     * A unit of volume equal to <code>1 / 8 {@link #GALLON_LIQUID_US}</code>
     * (default label <code>pint</code>).
     */
    public static final Unit PINT_LIQUID_US = UnitFormat.label(GALLON_LIQUID_US
            .multiply(1.0 / 8.0), "pint");

    /**
     * A unit of volume equal to <code>1 / 16 {@link #PINT_LIQUID_US}</code>
     * (default label <code>oz_fl</code>).
     */
    public static final Unit FLUID_OUNCE_US = UnitFormat.label(PINT_LIQUID_US
            .multiply(1.0 / 16.0), "oz_fl");

    /**
     * A unit of volume equal to one US dry gallon.
     * (default label <code>gallon_dry_us</code>).
     */
    public static final Unit GALLON_DRY_US = UnitFormat.label(INCH.pow(3)
            .multiply(268.8025), "gallon_dry_us");

    /**
     * A unit of volume equal to <code>1 / 8 {@link #GALLON_DRY_US}</code>
     * (default label <code>pint_dry_us</code>).
     */
    public static final Unit PINT_DRY_US = UnitFormat.label(GALLON_DRY_US
            .multiply(1.0 / 8.0), "pint_dry_us");

    /**
     * A unit of volume equal to <code>4.546 09 {@link #LITER}</code>
     * (default label <code>gal_uk</code>).
     */
    public static final Unit GALLON_UK = UnitFormat.label(LITER
            .multiply(4.54609), "gallon_uk");

    /**
     * A unit of volume equal to <code>1 / 8 {@link #GALLON_UK}</code>
     * (default label <code>pint_uk</code>).
     */
    public static final Unit PINT_UK = UnitFormat.label(GALLON_UK
            .multiply(1.0 / 8.0), "pint_uk");

    /**
     * A unit of volume equal to <code>1 / 160 {@link #GALLON_UK}</code>
     * (default label <code>oz_fl_uk</code>).
     */
    public static final Unit FLUID_OUNCE_UK = UnitFormat.label(GALLON_UK
            .multiply(1.0 / 160.0), "oz_fl_uk");

    /**
     * A unit of volume equal to <code>8 {@link #FLUID_OUNCE_US}</code>
     * (default label <code>cup_us</code>).
     */
    public static final Unit CUP_US = UnitFormat.label(FLUID_OUNCE_US
            .multiply(8), "cup_us");

    /**
     * A unit of volume equal to <code>10 {@link #FLUID_OUNCE_UK}</code>
     * (default label <code>cup_uk</code>).
     */
    public static final Unit CUP_UK = UnitFormat.label(FLUID_OUNCE_UK
            .multiply(10), "cup_uk");

    /**
     * A unit of volume equal to <code>1 / 2 {@link #FLUID_OUNCE_US}</code>
     * (default label <code>tablespoon_us</code>).
     */
    public static final Unit TABLESPOON_US = UnitFormat.label(FLUID_OUNCE_US
            .multiply(1.0 / 2.0), "tablespoon_us");

    /**
     * A unit of volume equal to one <code>5 / 8 {@link #FLUID_OUNCE_UK}</code>
     * (default label <code>tablespoon_uk</code>).
     */
    public static final Unit TABLESPOON_UK = UnitFormat.label(FLUID_OUNCE_UK
            .multiply(5.0 / 8.0), "tablespoon_uk");

    /**
     * A unit of volume equal to <code>1 / 6 {@link #FLUID_OUNCE_US}</code>
     * (default label <code>teaspoon_us</code>).
     */
    public static final Unit TEASPOON_US = UnitFormat.label(FLUID_OUNCE_US
            .multiply(1.0 / 6.0), "teaspoon_us");

    /**
     * A unit of volume equal to <code>1 / 6 {@link #FLUID_OUNCE_UK}</code>
     * (default label <code>teaspoon_uk</code>).
     */
    public static final Unit TEASPOON_UK = UnitFormat.label(FLUID_OUNCE_UK
            .multiply(1.0 / 6.0), "teaspoon_uk");

    /**
     * A unit of volume equal to <code>20.0 mL</code> (Australian tablespoon,
     * (default label <code>tablespoon_au</code>).
     */
    public static final Unit TABLESPOON_AU = UnitFormat.label(SI.METER.pow(3)
            .multiply(20.0e-6), "tablespoon_au");

    ////////////
    // Others //
    ////////////

    /**
     * A unit used to measure the ionizing ability of radiation
     * (default label <code>Roentgen</code>).
     */
    public static final Unit ROENTGEN = UnitFormat.label(SI.COULOMB.divide(
            SI.KILOGRAM).multiply(2.58e-4), "Roentgen");

    /**
     * Static method to force class initialization.
     */
    static void initializeClass() {
    }

}