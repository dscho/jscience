/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package javax.units;

import javax.quantities.*;
import javax.units.converters.LogConverter;
import javax.units.converters.MultiplyConverter;
import static javax.units.SI.*;

/**
 * <p> This class contains units that are not part of the International
 *     System of Units, that is, they are outside the SI, but are important
 *     and widely used.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.2, February 9, 2006
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
     * (standard name <code>%</code>).
     */
    public static final Unit<Dimensionless> PERCENT = Unit.ONE.divide(100);

    /**
     * A logarithmic unit used to describe a ratio
     * (standard name <code>dB</code>).
     */
    public static final Unit<Dimensionless> DECIBEL = Unit.ONE
            .transform(new LogConverter(10).inverse().concatenate(
                    new MultiplyConverter(0.1)));

    //////////////////
    // Acceleration //
    //////////////////

    /**
     * A unit of acceleration equal to an acceleration of one centimeter per
     * second per second.
     */
    public static final Unit<Acceleration> GALILEO = METER_PER_SQUARE_SECOND
            .divide(100);

    /**
     * A unit of acceleration equal to the gravity at the earth's surface
     * (standard name <code>grav</code>).
     */
    public static final Unit<Acceleration> G = METER_PER_SQUARE_SECOND
            .times(STANDARD_GRAVITY);

    /////////////////////////
    // Amount of substance //
    /////////////////////////

    /**
     * A unit of amount of substance equals to one atom
     * (standard name <code>atom</code>).
     */
    public static final Unit<AmountOfSubstance> ATOM = MOLE
            .divide(AVOGADRO_CONSTANT);

    ///////////
    // Angle //
    ///////////

    /**
     * A unit of angle equal to a full circle or <code>2<i>&pi;</i> 
     * {@link SI#RADIAN}</code> (standard name <code>rev</code>).
     */
    public static final Unit<Angle> REVOLUTION = RADIAN.times(2.0 * Math.PI);

    /**
     * A unit of angle equal to <code>1/360 {@link #REVOLUTION}</code>
     * (standard name <code>°</code>).
     */
    public static final Unit<Angle> DEGREE_ANGLE = REVOLUTION.divide(360);

    /**
     * A unit of angle equal to <code>1/60 {@link #DEGREE_ANGLE}</code>
     * (standard name <code>′</code>).
     */
    public static final Unit<Angle> MINUTE_ANGLE = DEGREE_ANGLE.divide(60);

    /**
     *  A unit of angle equal to <code>1/60 {@link #MINUTE_ANGLE}</code>
     * (standard name <code>"</code>).
     */
    public static final Unit<Angle> SECOND_ANGLE = MINUTE_ANGLE.divide(60);

    /**
     * A unit of angle equal to <code>0.01 {@link SI#RADIAN}</code>
     * (standard name <code>centiradian</code>).
     */
    public static final Unit<Angle> CENTIRADIAN = RADIAN.divide(100);

    /**
     * A unit of angle measure equal to <code>1/400 {@link #REVOLUTION}</code>
     * (standard name <code>grade</code>).
     */
    public static final Unit<Angle> GRADE = REVOLUTION.divide(400);

    //////////
    // Area //
    //////////

    /**
     * A unit of area equal to <code>100 m²</code>
     * (standard name <code>a</code>).
     */
    public static final Unit<Area> ARE = SQUARE_METER.times(100);

    /**
     * A unit of area equal to <code>100 {@link #ARE}</code>
     * (standard name <code>ha</code>).
     */
    public static final Unit<Area> HECTARE = ARE.times(100); // Exact.

    /////////////////
    // Data Amount //
    /////////////////

    /**
     * A unit of data amount equal to <code>8 {@link SI#BIT}</code>
     * (BinarY TErm, standard name <code>byte</code>).
     */
    public static final Unit<DataAmount> BYTE = BIT.times(8);

    /**
     * Equivalent {@link #BYTE}
     */
    public static final Unit<DataAmount> OCTET = BYTE;

    //////////////
    // Duration //
    //////////////

    /**
     * A unit of duration equal to <code>60 s</code>
     * (standard name <code>min</code>).
     */
    public static final Unit<Duration> MINUTE = SI.SECOND.times(60);

    /**
     * A unit of duration equal to <code>60 {@link #MINUTE}</code>
     * (standard name <code>h</code>).
     */
    public static final Unit<Duration> HOUR = MINUTE.times(60);

    /**
     * A unit of duration equal to <code>24 {@link #HOUR}</code>
     * (standard name <code>d</code>).
     */
    public static final Unit<Duration> DAY = HOUR.times(24);

    /**
     * A unit of duration equal to <code>7 {@link #DAY}</code>
     * (standard name <code>week</code>).
     */
    public static final Unit<Duration> WEEK = DAY.times(7);

    /**
     * A unit of duration equal to 365 days, 5 hours, 49 minutes,
     * and 12 seconds (standard name <code>year</code>).
     */
    public static final Unit<Duration> YEAR = SECOND.times(31556952);

    /**
     * A unit of duration equal to one twelfth of a year
     * (standard name <code>month</code>).
     */
    public static final Unit<Duration> MONTH = YEAR.divide(12);

    /**
     * A unit of duration equal to the time required for a complete rotation of
     * the earth in reference to any star or to the vernal equinox at the
     * meridian, equal to 23 hours, 56 minutes, 4.09 seconds
     * (standard name <code>day_sidereal</code>).
     */
    public static final Unit<Duration> DAY_SIDEREAL = SECOND.times(86164.09);

    /**
     * A unit of duration equal to one complete revolution of the
     * earth about the sun, relative to the fixed stars, or 365 days, 6 hours,
     * 9 minutes, 9.54 seconds (standard name <code>year_sidereal</code>).
     */
    public static final Unit<Duration> YEAR_SIDEREAL = SECOND
            .times(31558149.54);

    /**
     * A unit of duration equal to <code>365 {@link #DAY}</code>
     * (standard name <code>year_calendar</code>).
     */
    public static final Unit<Duration> YEAR_CALENDAR = DAY.times(365);

    /**
     * A unit of duration equal to <code>1E9 {@link #YEAR}</code>.
     */
    public static final Unit<Duration> AEON = YEAR.times(1e9);

    /////////////////////
    // Electric charge //
    /////////////////////

    /**
     * A unit of electric charge equal to the charge on one electron
     * (standard name <code>e</code>).
     */
    public static final Unit<ElectricCharge> E = COULOMB
            .times(ELEMENTARY_CHARGE);

    /**
     * A unit of electric charge equal to equal to the product of Avogadro's
     * number (see {@link SI#MOLE}) and the charge (1 e) on a single electron
     * (standard name <code>Fd</code>).
     */
    public static final Unit<ElectricCharge> FARADAY = COULOMB
            .times(ELEMENTARY_CHARGE * AVOGADRO_CONSTANT); // e/mol

    /**
     * A unit of electric charge which exerts a force of one dyne on an equal
     * charge at a distance of one centimeter
     * (standard name <code>Fr</code>).
     */
    public static final Unit<ElectricCharge> FRANKLIN = COULOMB
            .times(3.3356e-10);

    //////////////////////
    // Electric current //
    //////////////////////

    /**
     * A unit of electric charge equal to the centimeter-gram-second
     * electromagnetic unit of magnetomotive force, equal to <code>10/4
     * &pi;ampere-turn</code> (standard name <code>Gi</code>).
     */
    public static final Unit<ElectricCurrent> GILBERT = SI.AMPERE
            .times(10.0 / (4.0 * Math.PI));

    ////////////
    // Energy //
    ////////////

    /**
     * A unit of energy equal to <code>1E-7 J</code>
     * (standard name <code>erg</code>).
     */
    public static final Unit<Energy> ERG = JOULE.times(1e-7);

    /**
     * A unit of energy equal to one electron-volt (standard name 
     * <code>eV</code>, also recognized <code>keV, MeV, GeV</code>).
     */
    public static final Unit<Energy> ELECTRON_VOLT = JOULE
            .times(ELEMENTARY_CHARGE);

    /////////////////
    // Illuminance //
    /////////////////

    /**
     * A unit of illuminance equal to <code>1E4 Lx</code>
     * (standard name <code>La</code>).
     */
    public static final Unit<Illuminance> LAMBERT = LUX.times(1e4);

    ////////////
    // Length //
    ////////////

    /**
     * A unit of length equal to <code>0.3048 m</code> 
     * (standard name <code>ft</code>).
     */
    public static final Unit<Length> FOOT = METER.times(INTERNATIONAL_FOOT);

    /**
     * A unit of length equal to <code>1200/3937 m</code> 
     * (standard name <code>foot_survey_us</code>).
     * See also: <a href="http://www.sizes.com/units/foot.htm">foot</a>
     */
    public static final Unit<Length> FOOT_SURVEY_US = METER
            .times(1200.0 / 3937.0);

    /**
     * A unit of length equal to <code>0.9144 m</code>
     * (standard name <code>yd</code>).
     */
    public static final Unit<Length> YARD = FOOT.times(3);

    /**
     * A unit of length equal to <code>0.0254 m</code> 
     * (standard name <code>in</code>).
     */
    public static final Unit<Length> INCH = FOOT.divide(12);

    /**
     * A unit of length equal to <code>1609.344 m</code>
     * (standard name <code>mi</code>).
     */
    public static final Unit<Length> MILE = METER.times(1609.344);

    /**
     * A unit of length equal to <code>1852.0 m</code>
     * (standard name <code>nmi</code>).
     */
    public static final Unit<Length> NAUTICAL_MILE = METER.times(1852.0);

    /**
     * A unit of length equal to <code>1E-10 m</code>
     * (standard name <code>Å</code>).
     */
    public static final Unit<Length> ANGSTROM = METER.times(1e-10);

    /**
     * A unit of length equal to the average distance from the center of the
     * Earth to the center of the Sun (standard name <code>ua</code>).
     */
    public static final Unit<Length> ASTRONOMICAL_UNIT = METER
            .times(149597870691.0);

    /**
     * A unit of length equal to the distance that light travels in one year
     * through a vacuum (standard name <code>ly</code>).
     */
    public static final Unit<Length> LIGHT_YEAR = METER.times(9.460528405e15);

    /**
     * A unit of length equal to the distance at which a star would appear to
     * shift its position by one arcsecond over the course the time
     * (about 3 months) in which the Earth moves a distance of
     * {@link #ASTRONOMICAL_UNIT} in the direction perpendicular to the
     * direction to the star (standard name <code>pc</code>).
     */
    public static final Unit<Length> PARSEC = METER.times(30856770e9);

    /**
     * A unit of length equal to <code>0.013837 {@link #INCH}</code> exactly
     * (standard name <code>pt</code>).
     * @see     #PIXEL
     */
    public static final Unit<Length> POINT = INCH.times(0.013837);

    /**
     * A unit of length equal to <code>1/72 {@link #INCH}</code>
     * (standard name <code>pixel</code>).
     * It is the American point rounded to an even 1/72 inch.
     * @see     #POINT
     */
    public static final Unit<Length> PIXEL = INCH.divide(72);

    /**
     * Equivalent {@link #PIXEL}
     */
    public static final Unit<Length> COMPUTER_POINT = PIXEL;

    ///////////////////
    // Magnetic Flux //
    ///////////////////

    /**
     * A unit of magnetic flux equal <code>1E-8 Wb</code>
     * (standard name <code>Mx</code>).
     */
    public static final Unit<MagneticFlux> MAXWELL = WEBER.times(1e-8);

    ///////////////////////////
    // Magnetic Flux Density //
    ///////////////////////////

    /**
     * A unit of magnetic flux density equal <code>1000 A/m</code>
     * (standard name <code>G</code>).
     */
    public static final Unit<MagneticFluxDensity> GAUSS = TESLA.times(1e-4);

    //////////
    // Mass //
    //////////

    /**
     * A unit of mass equal to 1/12 the mass of the carbon-12 atom
     * (standard name <code>u</code>).
     */
    public static final Unit<Mass> ATOMIC_MASS = KILOGRAM
            .times(1e-3 / AVOGADRO_CONSTANT);

    /**
     * A unit of mass equal to the mass of the electron
     * (standard name <code>me</code>).
     */
    public static final Unit<Mass> ELECTRON_MASS = KILOGRAM
            .times(9.10938188e-31);

    /**
     * A unit of mass equal to <code>453.59237 grams</code> (avoirdupois pound,
     * standard name <code>lb</code>).
     */
    public static final Unit<Mass> POUND = KILOGRAM.times(AVOIRDUPOIS_POUND);

    /**
     * A unit of mass equal to <code>1 / 16 {@link #POUND}</code>
     * (standard name <code>oz</code>).
     */
    public static final Unit<Mass> OUNCE = POUND.divide(16);

    /**
     * A unit of mass equal to <code>2000 {@link #POUND}</code> (short ton, 
     * standard name <code>ton_us</code>).
     */
    public static final Unit<Mass> TON_US = POUND.times(2000);

    /**
     * A unit of mass equal to <code>2240 {@link #POUND}</code> (long ton,
     * standard name <code>ton_uk</code>).
     */
    public static final Unit<Mass> TON_UK = POUND.times(2240);

    /**
     * A unit of mass equal to <code>1000 kg</code> (metric ton,
     * standard name <code>t</code>).
     */
    public static final Unit<Mass> METRIC_TON = KILOGRAM.times(1000);

    ///////////
    // Force //
    ///////////

    /**
     * A unit of force equal to <code>1E-5 N</code>
     * (standard name <code>dyn</code>).
     */
    public static final Unit<Force> DYNE = NEWTON.times(1e-5);

    /**
     * A unit of force equal to <code>9.80665 N</code>
     * (standard name <code>kgf</code>).
     */
    public static final Unit<Force> KILOGRAM_FORCE = NEWTON
            .times(STANDARD_GRAVITY);

    /**
     * A unit of force equal to <code>{@link #POUND}·{@link #G}</code>
     * (standard name <code>lbf</code>).
     */
    public static final Unit<Force> POUND_FORCE = NEWTON
            .times(AVOIRDUPOIS_POUND * STANDARD_GRAVITY);

    ///////////
    // Power //
    ///////////

    /**
     * A unit of power equal to the power required to raise a mass of 75
     * kilograms at a velocity of 1 meter per second (metric,
     * standard name <code>hp</code>).
     */
    public static final Unit<Power> HORSEPOWER = WATT.times(735.499);

    //////////////
    // Pressure //
    //////////////

    /**
     * A unit of pressure equal to the average pressure of the Earth's
     * atmosphere at sea level (standard name <code>atm</code>).
     */
    public static final Unit<Pressure> ATMOSPHERE = PASCAL.times(101325);

    /**
     * A unit of pressure equal to <code>100 kPa</code>
     * (standard name <code>bar</code>).
     */
    public static final Unit<Pressure> BAR = PASCAL.times(100e3);

    /**
     * A unit of pressure equal to the pressure exerted at the Earth's
     * surface by a column of mercury 1 millimeter high
     * (standard name <code>mmHg</code>).
     */
    public static final Unit<Pressure> MILLIMETER_OF_MERCURY = PASCAL
            .times(133.322);

    /**
     * A unit of pressure equal to the pressure exerted at the Earth's
     * surface by a column of mercury 1 inch high
     * (standard name <code>inHg</code>).
     */
    public static final Unit<Pressure> INCH_OF_MERCURY = PASCAL.times(3386.388);

    /////////////////////////////
    // Radiation dose absorbed //
    /////////////////////////////

    /**
     * A unit of radiation dose absorbed equal to a dose of 0.01 joule of
     * energy per kilogram of mass (J/kg) (standard name <code>rd</code>).
     */
    public static final Unit<RadiationDoseAbsorbed> RAD = GRAY.divide(100);

    /**
     * A unit of radiation dose effective equal to <code>0.01 Sv</code>
     * (standard name <code>rem</code>).
     */
    public static final Unit<RadiationDoseEffective> REM = SIEVERT.divide(100);

    //////////////////////////
    // Radioactive activity //
    //////////////////////////

    /**
     * A unit of radioctive activity equal to the activity of a gram of radium
     * (standard name <code>Ci</code>).
     */
    public static final Unit<RadioactiveActivity> CURIE = BECQUEREL
            .times(3.7e10);

    /**
     * A unit of radioctive activity equal to 1 million radioactive
     * disintegrations per second (standard name <code>Rd</code>).
     */
    public static final Unit<RadioactiveActivity> RUTHERFORD = SI.BECQUEREL
            .times(1e6);

    /////////////////
    // Solid angle //
    /////////////////

    /**
     * A unit of solid angle equal to <code>4 <i>&pi;</i> steradians</code>
     * (standard name <code>sphere</code>).
     */
    public static final Unit<SolidAngle> SPHERE = STERADIAN
            .times(4.0 * Math.PI);

    /////////////////
    // Temperature //
    /////////////////

    /**
     * A unit of temperature equal to <code>5/9 °K</code>
     * (standard name <code>°R</code>).
     */
    public static final Unit<Temperature> RANKINE = KELVIN.times(5.0 / 9.0);

    /**
     * A unit of temperature equal to degree Rankine minus 
     * <code>459.67 °R</code> (standard name <code>°F</code>).
     * @see    #RANKINE
     */
    public static final Unit<Temperature> FAHRENHEIT = RANKINE.plus(459.67);

    //////////////
    // Velocity //
    //////////////

    /**
     * A unit of velocity equal to one {@link #NAUTICAL_MILE} per {@link #HOUR}
     * (standard name <code>Mach</code>).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Velocity> KNOT 
        = (Unit<Velocity>) NAUTICAL_MILE.divide(HOUR);

    /**
     * A unit of velocity to express the speed of an aircraft relative to
     * the speed of sound (standard name <code>Mach</code>).
     */
    public static final Unit<Velocity> MACH = METER_PER_SECOND.times(331.6);

    /**
     * A unit of velocity relative to the speed of light
     * (standard name <code>c</code>).
     */
    public static final Unit<Velocity> C = METER_PER_SECOND.times(299792458.0);

    ////////////
    // Volume //
    ////////////

    /**
     * A unit of volume equal to one cubic decimeter (default label
     * <code>L</code>, also recognized <code>µL, mL, cL, dL</code>).
     */
    public static final Unit<Volume> LITER = CUBIC_METER.divide(1000);

    /**
     * A unit of volume equal to one cubic inch (<code>in³</code>).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<Volume> CUBIC_INCH = (Unit<Volume>) INCH.pow(3);

    /**
     * A unit of volume equal to one US gallon, Liquid Unit. The U.S. liquid
     * gallon is based on the Queen Anne or Wine gallon occupying 231 cubic
     * inches (standard name <code>gal</code>).
     */
    public static final Unit<Volume> GALLON_LIQUID_US = CUBIC_INCH.times(231);

    /**
     * A unit of volume equal to <code>1 / 128 {@link #GALLON_LIQUID_US}</code>
     * (standard name <code>oz_fl</code>).
     */
    public static final Unit<Volume> OUNCE_LIQUID_US = GALLON_LIQUID_US
            .divide(128);

    /**
     * A unit of volume equal to one US dry gallon.
     * (standard name <code>gallon_dry_us</code>).
     */
    public static final Unit<Volume> GALLON_DRY_US = CUBIC_INCH.times(268.8025);

    /**
     * A unit of volume equal to <code>4.546 09 {@link #LITER}</code>
     * (standard name <code>gal_uk</code>).
     */
    public static final Unit<Volume> GALLON_UK = LITER.times(4.54609);

    /**
     * A unit of volume equal to <code>1 / 160 {@link #GALLON_UK}</code>
     * (standard name <code>oz_fl_uk</code>).
     */
    public static final Unit<Volume> OUNCE_LIQUID_UK = GALLON_UK.divide(160);

    ///////////////
    // Viscosity //
    ///////////////

    /**
     * A unit of dynamic viscosity equal to <code>1 g/(cm·s)</code>
     * (cgs unit).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<DynamicViscosity> 
         POISE = (Unit<DynamicViscosity>) GRAM.divide(CENTI(METER).times(SECOND));

    /**
     * A unit of kinematic viscosity equal to <code>1 cm²/s</code>
     * (cgs unit).
     */
    @SuppressWarnings("unchecked")
    public static final Unit<KinematicViscosity> 
         STOKE = (Unit<KinematicViscosity>) CENTI(METER).pow(2).divide(SECOND);
    

    ////////////
    // Others //
    ////////////

    /**
     * A unit used to measure the ionizing ability of radiation
     * (standard name <code>Roentgen</code>).
     */
    public static final Unit<?> ROENTGEN = COULOMB.divide(KILOGRAM).times(2.58e-4);

}