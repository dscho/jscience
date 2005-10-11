/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.units;

import org.jscience.physics.quantities.*;
import static org.jscience.physics.units.SI.*;
import static javolution.lang.MathLib.*;

/**
 * <p> This class contains units that are not part of the International
 *     System of Units, that is, they are outside the SI, but are important
 *     and widely used.</p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 2.0, June 16, 2004
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
	public static final Unit<Dimensionless> PERCENT = Unit.ONE.times(0.01)
			.label("%");

	/**
	 * A logarithmic unit used to describe a ratio
	 * (default label <code>dB</code>).
	 */
	public static final Unit<Dimensionless> DECIBEL = Unit.ONE.transform(
			new LogConverter(10).inverse().concatenate(
					new MultiplyConverter(0.1))).label("dB");

	//////////////////
	// Acceleration //
	//////////////////

	/**
	 * A unit of acceleration equal to an acceleration of one centimeter per
	 * second per second.
	 */
	public static final Unit<Acceleration> GALILEO = METER_PER_SQUARE_SECOND
			.times(1e-2);

	/**
	 * A unit of acceleration equal to the gravity at the earth's surface
	 * (default label <code>grav</code>).
	 */
	public static final Unit<Acceleration> G = METER_PER_SQUARE_SECOND.times(
			STANDARD_GRAVITY).label("grav");

	/////////////////////////
	// Amount of substance //
	/////////////////////////

	/**
	 * A unit of amount of substance equals to one atom
	 * (default label <code>atom</code>).
	 */
	public static final Unit<AmountOfSubstance> ATOM = MOLE.times(
			1.0 / AVOGADRO_CONSTANT).label("atom");

	///////////
	// Angle //
	///////////

	/**
	 * A unit of angle equal to a full circle or <code>2<i>&pi;</i> 
	 * {@link SI#RADIAN}</code> (default label <code>rev</code>).
	 */
	public static final Unit<Angle> REVOLUTION = RADIAN.times(2.0 * PI).label(
			"rev");

	/**
	 * A unit of angle equal to <code>1/360 {@link #REVOLUTION}</code>
	 * (default label <code>°</code>).
	 */
	public static final Unit<Angle> DEGREE_ANGLE = REVOLUTION
			.times(1.0 / 360.0).label("°");

	/**
	 * A unit of angle equal to <code>1/60 {@link #DEGREE_ANGLE}</code>
	 * (default label <code>′</code>).
	 */
	public static final Unit<Angle> MINUTE_ANGLE = DEGREE_ANGLE.times(
			1.0 / 60.0).label("′");

	/**
	 *  A unit of angle equal to <code>1/60 {@link #MINUTE_ANGLE}</code>
	 * (default label <code>"</code>).
	 */
	public static final Unit<Angle> SECOND_ANGLE = MINUTE_ANGLE.times(
			1.0 / 60.0).label("″");

	/**
	 * A unit of angle equal to <code>0.01 {@link SI#RADIAN}</code>
	 * (default label <code>centiradian</code>).
	 */
	public static final Unit<Angle> CENTIRADIAN = RADIAN.times(0.01).label(
			"centiradian");

	/**
	 * A unit of angle measure equal to <code>1/400 {@link #REVOLUTION}</code>
	 * (default label <code>grade</code>).
	 */
	public static final Unit<Angle> GRADE = REVOLUTION.times(1.0 / 400.0)
			.label("grade");

	//////////
	// Area //
	//////////

	/**
	 * A unit of area equal to <code>100 m²</code>
	 * (default label <code>a</code>).
	 */
	public static final Unit<Area> ARE = SQUARE_METER.times(100).label("a"); // Exact.

	/**
	 * A unit of area equal to <code>100 {@link #ARE}</code>
	 * (default label <code>ha</code>).
	 */
	public static final Unit<Area> HECTARE = ARE.times(100).label("ha"); // Exact.

	/**
	 * A unit of area equal to <code>100 fm²</code>
	 * (default label <code>b</code>).
	 */
	public static final Unit<Area> BARN = SQUARE_METER.times(1e-28).label("b"); // Exact.

	/////////////////
	// Data Amount //
	/////////////////

	/**
	 * A unit of data amount equal to <code>8 {@link SI#BIT}</code>
	 * (BinarY TErm, default label <code>byte</code>).
	 */
	public static final Unit<DataAmount> BYTE = BIT.times(8).label("byte");

	/**
	 * Equivalent {@link #BYTE}
	 */
	public static final Unit<DataAmount> OCTET = BYTE;

	//////////////
	// Duration //
	//////////////

	/**
	 * A unit of duration equal to <code>60 s</code>
	 * (default label <code>min</code>).
	 */
	public static final Unit<Duration> MINUTE = SI.SECOND.times(60)
			.label("min");

	/**
	 * A unit of duration equal to <code>60 {@link #MINUTE}</code>
	 * (default label <code>h</code>).
	 */
	public static final Unit<Duration> HOUR = MINUTE.times(60).label("h");

	/**
	 * A unit of duration equal to <code>24 {@link #HOUR}</code>
	 * (default label <code>d</code>).
	 */
	public static final Unit<Duration> DAY = HOUR.times(24).label("day");

	/**
	 * A unit of duration equal to <code>7 {@link #DAY}</code>
	 * (default label <code>week</code>).
	 */
	public static final Unit<Duration> WEEK = DAY.times(7).label("week");

	/**
	 * A unit of duration equal to 365 days, 5 hours, 49 minutes,
	 * and 12 seconds (default label <code>year</code>).
	 */
	public static final Unit<Duration> YEAR = SECOND.times(31556952).label(
			"year");

	/**
	 * A unit of duration equal to one twelfth of a year
	 * (default label <code>month</code>).
	 */
	public static final Unit<Duration> MONTH = YEAR.times(1.0 / 12.0).label(
			"month");

	/**
	 * A unit of duration equal to the time required for a complete rotation of
	 * the earth in reference to any star or to the vernal equinox at the
	 * meridian, equal to 23 hours, 56 minutes, 4.09 seconds
	 * (default label <code>day_sidereal</code>).
	 */
	public static final Unit<Duration> DAY_SIDEREAL = SECOND.times(86164.09)
			.label("day_sidereal");

	/**
	 * A unit of duration equal to one complete revolution of the
	 * earth about the sun, relative to the fixed stars, or 365 days, 6 hours,
	 * 9 minutes, 9.54 seconds (default label <code>year_sidereal</code>).
	 */
	public static final Unit<Duration> YEAR_SIDEREAL = SECOND
			.times(31558149.54).label("year_sidereal");

	/**
	 * A unit of duration equal to <code>365 {@link #DAY}</code>
	 * (default label <code>year_calendar</code>).
	 */
	public static final Unit<Duration> YEAR_CALENDAR = DAY.times(365).label(
			"year_calendar");

	/**
	 * A unit of duration equal to <code>1E9 {@link #YEAR}</code>.
	 */
	public static final Unit<Duration> AEON = YEAR.times(1e9);

	/////////////////////
	// Electric charge //
	/////////////////////

	/**
	 * A unit of electric charge equal to the charge on one electron
	 * (default label <code>e</code>).
	 */
	public static final Unit<ElectricCharge> E = COULOMB.times(
			ELEMENTARY_CHARGE).label("e");

	/**
	 * A unit of electric charge equal to equal to the product of Avogadro's
	 * number (see {@link SI#MOLE}) and the charge (1 e) on a single electron
	 * (default label <code>Fd</code>).
	 */
	public static final Unit<ElectricCharge> FARADAY = COULOMB.times(
			ELEMENTARY_CHARGE * AVOGADRO_CONSTANT).label("Fd"); // e/mol

	/**
	 * A unit of electric charge which exerts a force of one dyne on an equal
	 * charge at a distance of one centimeter
	 * (default label <code>Fr</code>).
	 */
	public static final Unit<ElectricCharge> FRANKLIN = COULOMB.times(
			3.3356e-10).label("Fr");

	//////////////////////
	// Electric current //
	//////////////////////

	/**
	 * A unit of electric charge equal to the centimeter-gram-second
	 * electromagnetic unit of magnetomotive force, equal to <code>10/4
	 * &pi;ampere-turn</code> (default label <code>Gi</code>).
	 */
	public static final Unit<ElectricCurrent> GILBERT = SI.AMPERE.times(
			10.0 / (4.0 * PI)).label("Gi");

	////////////
	// Energy //
	////////////

	/**
	 * A unit of energy equal to <code>1055.056 J</code>
	 * (default label <code>Btu</code>).
	 */
	public static final Unit<Energy> BTU = JOULE.times(1055.056).label("Btu");

	/**
	 * A unit of energy equal to <code>1054.350 J</code>, thermochemical
	 * (default label <code>Btu_th</code>).
	 */
	public static final Unit<Energy> BTU_TH = JOULE.times(1054.350).label(
			"Btu_th");

	/**
	 * A unit of energy equal to <code>1055.87 J</code>, mean
	 * (default label <code>Btu_mean</code>).
	 */
	public static final Unit<Energy> BTU_MEAN = JOULE.times(1055.87).label(
			"Btu_mean");

	/**
	 * A unit of energy equal to <code>4.1868 J</code>
	 * (default label <code>cal</code>).
	 */
	public static final Unit<Energy> CALORIE = JOULE.times(4.1868).label("cal");

	/**
	 * A unit of energy equal to one kilo-calorie
	 * (default label <code>Cal</code>).
	 */
	public static final Unit<Energy> KILOCALORIE = CALORIE.times(1e3).label(
			"Cal");

	/**
	 * A unit of energy equal to <code>1E-7 J</code>
	 * (default label <code>erg</code>).
	 */
	public static final Unit<Energy> ERG = JOULE.times(1e-7).label("erg");

	/**
	 * A unit of energy equal to one electron-volt (default label 
	 * <code>eV</code>, also recognized <code>keV, MeV, GeV</code>).
	 */
	public static final Unit<Energy> ELECTRON_VOLT = JOULE.times(
			ELEMENTARY_CHARGE).label("eV");
	static {
		ELECTRON_VOLT.times(1e3).label("keV");
		ELECTRON_VOLT.times(1e6).label("MeV");
		ELECTRON_VOLT.times(1e9).label("GeV");
	}

	/**
	 * A unit of energy equal to <code>105.4804E6 J</code> (approximation, 
	 * default label <code>thm</code>).
	 */
	public static final Unit<Energy> THERM = JOULE.times(105.4804e6).label(
			"thm"); // Appr.

	/////////////////
	// Illuminance //
	/////////////////

	/**
	 * A unit of illuminance equal to <code>1E4 Lx</code>
	 * (default label <code>La</code>).
	 */
	public static final Unit<Illuminance> LAMBERT = LUX.times(1e4).label("La");

	////////////
	// Length //
	////////////

	/**
	 * A unit of length equal to <code>0.3048 m</code> 
	 * (default label <code>ft</code>).
	 */
	public static final Unit<Length> FOOT = METER.times(INTERNATIONAL_FOOT)
			.label("ft");

	/**
	 * A unit of length equal to <code>1200/3937 m</code> 
	 * (default label <code>foot_survey_us</code>).
	 * See also: <a href="http://www.sizes.com/units/foot.htm">foot</a>
	 */
	public static final Unit<Length> FOOT_SURVEY_US = METER.times(
			1200.0 / 3937.0).label("foot_survey_us");

	/**
	 * A unit of length equal to <code>0.9144 m</code>
	 * (default label <code>yd</code>).
	 */
	public static final Unit<Length> YARD = FOOT.times(3).label("yd");

	/**
	 * A unit of length equal to <code>0.0254 m</code> 
	 * (default label <code>in</code>).
	 */
	public static final Unit<Length> INCH = FOOT.times(1.0 / 12.0).label("in");

	/**
	 * A unit of length equal to <code>1609.344 m</code>
	 * (default label <code>mi</code>).
	 */
	public static final Unit<Length> MILE = METER.times(1609.344).label("mi");

	/**
	 * A unit of length equal to <code>1852.0 m</code>
	 * (default label <code>nmi</code>).
	 */
	public static final Unit<Length> NAUTICAL_MILE = METER.times(1852.0).label(
			"nmi");

	/**
	 * A unit of length equal to <code>1E-10 m</code>
	 * (default label <code>Å</code>).
	 */
	public static final Unit<Length> ANGSTROM = METER.times(1e-10).label("Å");

	/**
	 * A unit of length equal to the average distance from the center of the
	 * Earth to the center of the Sun (default label <code>ua</code>).
	 */
	public static final Unit<Length> ASTRONOMICAL_UNIT = METER.times(
			149597870691.0).label("ua");

	/**
	 * A unit of length equal to the distance that light travels in one year
	 * through a vacuum (default label <code>ly</code>).
	 */
	public static final Unit<Length> LIGHT_YEAR = METER.times(9.460528405e15)
			.label("ly");

	/**
	 * A unit of length equal to the distance at which a star would appear to
	 * shift its position by one arcsecond over the course the time
	 * (about 3 months) in which the Earth moves a distance of
	 * {@link #ASTRONOMICAL_UNIT} in the direction perpendicular to the
	 * direction to the star (default label <code>pc</code>).
	 */
	public static final Unit<Length> PARSEC = METER.times(30856770e9).label(
			"pc");

	/**
	 * A unit of length equal to the mean distance between the proton
	 * and the electron in an unexcited hydrogen atom
	 * (default label <code>Bohr</code>).
	 */
	public static final Unit<Length> BOHR = METER.times(52.918e-12).label(
			"Bohr");

	/**
	 * A unit of length equal to <code>0.013837 {@link #INCH}</code> exactly
	 * (default label <code>pt</code>).
	 * @see     #PIXEL
	 */
	public static final Unit<Length> POINT = INCH.times(0.013837).label("pt");

	/**
	 * A unit of length equal to <code>1/72 {@link #INCH}</code>
	 * (default label <code>pixel</code>).
	 * It is the American point rounded to an even 1/72 inch.
	 * @see     #POINT
	 */
	public static final Unit<Length> PIXEL = INCH.times(1.0 / 72.0).label(
			"pixel");

	/**
	 * Equivalent {@link #PIXEL}
	 */
	public static final Unit<Length> COMPUTER_POINT = PIXEL;

	/**
	 * A unit of length equal to <code>12 {@link #POINT}</code>
	 * (default label <code>pi</code>).
	 * @see     #POINT
	 */
	public static final Unit<Length> PICA = POINT.times(12).label("pi");

	/**
	 * A unit of length equal to <code>0.37592e-3 m</code>
	 * (default label <code>Didot</code>).
	 */
	public static final Unit<Length> DIDOT = METER.times(0.37592e-3).label(
			"Didot");

	/**
	 * A unit of length equal to <code>12 {@link #DIDOT}</code>
	 * (default label <code>cicero</code>).
	 */
	public static final Unit<Length> CICERO = DIDOT.times(12).label("cicero");

	///////////////////
	// Magnetic Flux //
	///////////////////

	/**
	 * A unit of magnetic flux equal <code>1E-8 Wb</code>
	 * (default label <code>Mx</code>).
	 */
	public static final Unit<MagneticFlux> MAXWELL = WEBER.times(1e-8).label(
			"Mx");

	///////////////////////////
	// Magnetic Flux Density //
	///////////////////////////

	/**
	 * A unit of magnetic flux density equal <code>1000 A/m</code>
	 * (default label <code>G</code>).
	 */
	public static final Unit<MagneticFluxDensity> GAUSS = TESLA.times(1e-4)
			.label("G");

	//////////
	// Mass //
	//////////

	/**
	 * A unit of mass equal to 1/12 the mass of the carbon-12 atom
	 * (default label <code>u</code>).
	 */
	public static final Unit<Mass> ATOMIC_MASS = KILOGRAM.times(
			1e-3 / AVOGADRO_CONSTANT).label("u");

	/**
	 * A unit of mass equal to the mass of the electron
	 * (default label <code>me</code>).
	 */
	public static final Unit<Mass> ELECTRON_MASS = KILOGRAM.times(
			9.10938188e-31).label("me");

	/**
	 * A unit of mass equal to <code>200 mg</code>
	 * (default label <code>carat</code>).
	 */
	public static final Unit<Mass> CARAT = KILOGRAM.times(200e-6)
			.label("carat");

	/**
	 * A unit of mass equal to <code>453.59237 grams</code> (avoirdupois pound,
	 * default label <code>lb</code>).
	 */
	public static final Unit<Mass> POUND = KILOGRAM.times(AVOIRDUPOIS_POUND)
			.label("lb");

	/**
	 * A unit of mass equal to <code>1 / 16 {@link #POUND}</code>
	 * (default label <code>oz</code>).
	 */
	public static final Unit<Mass> OUNCE = POUND.times(1.0 / 16.0).label("oz");

	/**
	 * A unit of mass equal to <code>2000 {@link #POUND}</code> (short ton, 
	 * default label <code>ton_us</code>).
	 */
	public static final Unit<Mass> TON_US = POUND.times(2000).label("ton_us");

	/**
	 * A unit of mass equal to <code>2240 {@link #POUND}</code> (long ton,
	 * default label <code>ton_uk</code>).
	 */
	public static final Unit<Mass> TON_UK = POUND.times(2240).label("ton_uk");

	/**
	 * A unit of mass equal to <code>1000 kg</code> (metric ton,
	 * default label <code>t</code>).
	 */
	public static final Unit<Mass> METRIC_TON = KILOGRAM.times(1000).label("t");

	///////////
	// Force //
	///////////

	/**
	 * A unit of force equal to <code>1E-5 N</code>
	 * (default label <code>dyn</code>).
	 */
	public static final Unit<Force> DYNE = NEWTON.times(1e-5).label("dyn");

	/**
	 * A unit of force equal to <code>9.80665 N</code>
	 * (default label <code>kgf</code>).
	 */
	public static final Unit<Force> KILOGRAM_FORCE = NEWTON.times(
			STANDARD_GRAVITY).label("kgf");

	/**
	 * A unit of force equal to <code>{@link #POUND}·{@link #G}</code>
	 * (default label <code>lbf</code>).
	 */
	public static final Unit<Force> POUND_FORCE = NEWTON.times(
			AVOIRDUPOIS_POUND * STANDARD_GRAVITY).label("lbf");

	///////////
	// Power //
	///////////

	/**
	 * A unit of power equal to the power required to raise a mass of 75
	 * kilograms at a velocity of 1 meter per second (metric,
	 * default label <code>hp</code>).
	 */
	public static final Unit<Power> HORSEPOWER = WATT.times(735.499)
			.label("hp");

	//////////////
	// Pressure //
	//////////////

	/**
	 * A unit of pressure equal to the average pressure of the Earth's
	 * atmosphere at sea level (default label <code>atm</code>).
	 */
	public static final Unit<Pressure> ATMOSPHERE = PASCAL.times(101325).label(
			"atm");

	/**
	 * A unit of pressure equal to <code>100 kPa</code>
	 * (default label <code>bar</code>).
	 */
	public static final Unit<Pressure> BAR = PASCAL.times(100e3).label("bar");

	/**
	 * A unit of pressure equal to <code>1 hPa</code>
	 * (default alias <code>mbar</code>).
	 */
	public static final Unit<Pressure> MILLIBAR = BAR.times(1.0 / 1000.0)
			.alias("mbar");

	/**
	 * A unit of pressure equal to the pressure exerted at the Earth's
	 * surface by a column of mercury 1 millimeter high
	 * (default label <code>mmHg</code>).
	 */
	public static final Unit<Pressure> MILLIMETER_OF_MERCURY = PASCAL.times(
			133.322).label("mmHg");

	/**
	 * A unit of pressure equal to the pressure exerted at the Earth's
	 * surface by a column of mercury 1 inch high
	 * (default label <code>inHg</code>).
	 */
	public static final Unit<Pressure> INCH_OF_MERCURY = PASCAL.times(3386.388)
			.label("inHg");

	/////////////////////////////
	// Radiation dose absorbed //
	/////////////////////////////

	/**
	 * A unit of radiation dose absorbed equal to a dose of 0.01 joule of
	 * energy per kilogram of mass (J/kg) (default label <code>rd</code>).
	 */
	public static final Unit<RadiationDoseAbsorbed> RAD = GRAY.times(0.01)
			.label("rd");

	/**
	 * A unit of radiation dose effective equal to <code>0.01 Sv</code>
	 * (default label <code>rem</code>).
	 */
	public static final Unit<RadiationDoseEffective> REM = SIEVERT.times(0.01)
			.label("rem");

	//////////////////////////
	// Radioactive activity //
	//////////////////////////

	/**
	 * A unit of radioctive activity equal to the activity of a gram of radium
	 * (default label <code>Ci</code>).
	 */
	public static final Unit<RadioactiveActivity> CURIE = BECQUEREL.times(
			3.7e10).label("Ci");

	/**
	 * A unit of radioctive activity equal to 1 million radioactive
	 * disintegrations per second (default label <code>Rd</code>).
	 */
	public static final Unit<RadioactiveActivity> RUTHERFORD = SI.BECQUEREL
			.times(1e6).label("Rd");

	/////////////////
	// Solid angle //
	/////////////////

	/**
	 * A unit of solid angle equal to <code>4 <i>&pi;</i> steradians</code>
	 * (default label <code>sphere</code>).
	 */
	public static final Unit<SolidAngle> SPHERE = STERADIAN.times(4.0 * PI)
			.label("sphere");

	/////////////////
	// Temperature //
	/////////////////

	/**
	 * A unit of temperature equal to <code>5/9 °K</code>
	 * (default label <code>°R</code>).
	 */
	public static final Unit<Temperature> RANKINE = KELVIN.times(5.0 / 9.0)
			.label("°R");

	/**
	 * A unit of temperature equal to degree Rankine minus 
	 * <code>459.67 °R</code> (default label <code>°F</code>).
	 * @see    #RANKINE
	 */
	public static final Unit<Temperature> FAHRENHEIT = RANKINE.plus(459.67)
			.label("°F");

	//////////////
	// Velocity //
	//////////////

	/**
	 * A unit of velocity equal to one {@link #NAUTICAL_MILE} per {@link #HOUR}.
	 */
	public static final Unit<Velocity> KNOT = NAUTICAL_MILE.divide(HOUR);

	/**
	 * A unit of velocity to express the speed of an aircraft relative to
	 * the speed of sound (default label <code>Mach</code>).
	 */
	public static final Unit<Velocity> MACH = METER_PER_SECOND.times(331.6)
			.label("Mach");

	/**
	 * A unit of velocity relative to the speed of light
	 * (default label <code>c</code>).
	 */
	public static final Unit<Velocity> C = METER_PER_SECOND.times(299792458.0)
			.label("c");

	////////////
	// Volume //
	////////////

	/**
	 * A unit of volume equal to one cubic decimeter (default label
	 * <code>L</code>, also recognized <code>µL, mL, cL, dL</code>).
	 */
	public static final Unit<Volume> LITER = CUBIC_METER.times(0.001)
			.label("L");
	static {
		LITER.times(1e-6).label("µL");
		LITER.times(1e-3).label("mL");
		LITER.times(1e-2).label("cL");
		LITER.times(1e-1).label("dL");
	}

	/**
	 * A unit of volume equal to one cubic inch (<code>in³</code>).
	 */
	public static final Unit<Volume> CUBIC_INCH = INCH.pow(3);

	/**
	 * A unit of volume equal to one US gallon, Liquid Unit. The U.S. liquid
	 * gallon is based on the Queen Anne or Wine gallon occupying 231 cubic
	 * inches (default label <code>gal</code>).
	 */
	public static final Unit<Volume> GALLON_LIQUID_US = CUBIC_INCH.times(231)
			.label("gal");

	/**
	 * A unit of volume equal to <code>1 / 8 {@link #GALLON_LIQUID_US}</code>
	 * (default label <code>pint</code>).
	 */
	public static final Unit<Volume> PINT_LIQUID_US = GALLON_LIQUID_US.times(
			1.0 / 8.0).label("pint");

	/**
	 * A unit of volume equal to <code>1 / 16 {@link #PINT_LIQUID_US}</code>
	 * (default label <code>oz_fl</code>).
	 */
	public static final Unit<Volume> FLUID_OUNCE_US = PINT_LIQUID_US.times(
			1.0 / 16.0).label("oz_fl");

	/**
	 * A unit of volume equal to one US dry gallon.
	 * (default label <code>gallon_dry_us</code>).
	 */
	public static final Unit<Volume> GALLON_DRY_US = CUBIC_INCH.times(268.8025)
			.label("gallon_dry_us");

	/**
	 * A unit of volume equal to <code>1 / 8 {@link #GALLON_DRY_US}</code>
	 * (default label <code>pint_dry_us</code>).
	 */
	public static final Unit<Volume> PINT_DRY_US = GALLON_DRY_US.times(
			1.0 / 8.0).label("pint_dry_us");

	/**
	 * A unit of volume equal to <code>4.546 09 {@link #LITER}</code>
	 * (default label <code>gal_uk</code>).
	 */
	public static final Unit<Volume> GALLON_UK = LITER.times(4.54609).label(
			"gallon_uk");

	/**
	 * A unit of volume equal to <code>1 / 8 {@link #GALLON_UK}</code>
	 * (default label <code>pint_uk</code>).
	 */
	public static final Unit<Volume> PINT_UK = GALLON_UK.times(1.0 / 8.0)
			.label("pint_uk");

	/**
	 * A unit of volume equal to <code>1 / 160 {@link #GALLON_UK}</code>
	 * (default label <code>oz_fl_uk</code>).
	 */
	public static final Unit<Volume> FLUID_OUNCE_UK = GALLON_UK.times(
			1.0 / 160.0).label("oz_fl_uk");

	/**
	 * A unit of volume equal to <code>8 {@link #FLUID_OUNCE_US}</code>
	 * (default label <code>cup_us</code>).
	 */
	public static final Unit<Volume> CUP_US = FLUID_OUNCE_US.times(8).label(
			"cup_us");

	/**
	 * A unit of volume equal to <code>10 {@link #FLUID_OUNCE_UK}</code>
	 * (default label <code>cup_uk</code>).
	 */
	public static final Unit<Volume> CUP_UK = FLUID_OUNCE_UK.times(10).label(
			"cup_uk");

	/**
	 * A unit of volume equal to <code>1 / 2 {@link #FLUID_OUNCE_US}</code>
	 * (default label <code>tablespoon_us</code>).
	 */
	public static final Unit<Volume> TABLESPOON_US = FLUID_OUNCE_US.times(
			1.0 / 2.0).label("tablespoon_us");

	/**
	 * A unit of volume equal to one <code>5 / 8 {@link #FLUID_OUNCE_UK}</code>
	 * (default label <code>tablespoon_uk</code>).
	 */
	public static final Unit<Volume> TABLESPOON_UK = FLUID_OUNCE_UK.times(
			5.0 / 8.0).label("tablespoon_uk");

	/**
	 * A unit of volume equal to <code>1 / 6 {@link #FLUID_OUNCE_US}</code>
	 * (default label <code>teaspoon_us</code>).
	 */
	public static final Unit<Volume> TEASPOON_US = FLUID_OUNCE_US.times(
			1.0 / 6.0).label("teaspoon_us");

	/**
	 * A unit of volume equal to <code>1 / 6 {@link #FLUID_OUNCE_UK}</code>
	 * (default label <code>teaspoon_uk</code>).
	 */
	public static final Unit<Volume> TEASPOON_UK = FLUID_OUNCE_UK.times(
			1.0 / 6.0).label("teaspoon_uk");

	/**
	 * A unit of volume equal to <code>20.0 mL</code> (Australian tablespoon,
	 * (default label <code>tablespoon_au</code>).
	 */
	public static final Unit<Volume> TABLESPOON_AU = CUBIC_METER.times(20.0e-6)
			.label("tablespoon_au");

	////////////
	// Others //
	////////////

	/**
	 * A unit used to measure the ionizing ability of radiation
	 * (default label <code>Roentgen</code>).
	 */
	public static final Unit ROENTGEN = COULOMB.divide(KILOGRAM).times(2.58e-4)
			.label("Roentgen");

	/**
	 * Static method to force class initialization.
	 */
	static void initializeClass() {
	}

}