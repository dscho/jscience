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
 * <p> This class contains SI (Système International d'Unités) base units,
 *     and derived units.</p>
 * <p> It also defines the 20 SI prefixes used to form decimal multiples and
 *     submultiples of SI units
 *     (e.g. <code>Unit HECTO_PASCAL = SI.HECTO(SI.PASCAL);</code>)</p>
 * See also: <a href="http://www.bipm.fr/">BIPM</a>, <a href=
 * "http://physics.nist.gov/cuu/Units/">International System of Units</a>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, October 24, 2004
 */
public final class SI  {

    /**
     * Default constructor (prevents this class from being instantiated).
     */
    private SI() {}

    ////////////////
    // BASE UNITS //
    ////////////////

    /**
     * The base unit for electric current quantities (<code>A</code>).
     * The Ampere is that constant current which, if maintained in two straight
     * parallel conductors of infinite length, of negligible circular
     * cross-section, and placed 1 metre apart in vacuum, would produce between
     * these conductors a force equal to 2 × 10-7 newton per metre of length.
     * It is named after the French physicist Andre Ampere (1775-1836).
     */
    public static final BaseUnit AMPERE = BaseUnit.getInstance("A");

    /**
     * The base unit for luminous intensity quantities (<code>cd</code>).
     * The candela is the luminous intensity, in a given direction,
     * of a source that emits monochromatic radiation of frequency
     * 540 × 1012 hertz and that has a radiant intensity in that
     * direction of 1/683 watt per steradian
     */
    public static final BaseUnit CANDELA = BaseUnit.getInstance("cd");

    /**
     * The base unit for thermodynamic temperature quantities (<code>K</code>).
     * The kelvin is the 1/273.16th of the thermodynamic temperature of the
     * triple point of water. It is named after the Scottish mathematician and
     * physicist William Thomson 1st Lord Kelvin (1824-1907)
     */
    public static final BaseUnit KELVIN = BaseUnit.getInstance("K");

    /**
     * The base unit for mass quantities (<code>kg</code>).
     * It is the only SI unit with a prefix as part of its name and symbol.
     * The kilogram is equal to the mass of an international prototype in the
     * form of a platinum-iridium cylinder kept at Sevres in France.
     * @see   #GRAM
     */
    public static final BaseUnit KILOGRAM = BaseUnit.getInstance("kg");

    /**
     * The base unit for length quantities (<code>m</code>).
     * One meter was redefined in 1983 as the distance traveled by light in
     * a vacuum in 1/299,792,458 of a second.
     */
    public static final BaseUnit METER = BaseUnit.getInstance("m");

    /**
     * The base unit for amount of substance quantities (<code>mol</code>).
     * The mole is the amount of substance of a system which contains as many
     * elementary entities as there are atoms in 0.012 kilogram of carbon 12.
     */
    public static final BaseUnit MOLE = BaseUnit.getInstance("mol");

    /**
     * The base unit for duration quantities (<code>s</code>).
     * It is defined as the duration of 9,192,631,770 cycles of radiation
     * corresponding to the transition between two hyperfine levels of
     * the ground state of cesium (1967 Standard).
     */
    public static final BaseUnit SECOND = BaseUnit.getInstance("s");

    //////////////////////
    // SI DERIVED UNITS //
    //////////////////////

    /**
     * The derived unit for mass quantities (<code>g</code>).
     * The base unit for mass quantity is {@link #KILOGRAM}.
     */
    public static final Unit GRAM = KILOGRAM.multiply(1e-3);

    /**
     * The unit for plane angle quantities (<code>rad</code>).
     * One radian is the angle between two radii of a circle such that the
     * length of the arc between them is equal to the radius.
     */
    public static final Unit RADIAN = Unit.ONE.alternate("rad");

    /**
     * The unit for solid angle quantities (<code>sr</code>).
     * One steradian is the solid angle subtended at the center of a sphere by
     * an area on the surface of the sphere that is equal to the radius squared.
     * The total solid angle of a sphere is 4*Pi steradians.
     */
    public static final Unit STERADIAN = Unit.ONE.alternate("sr");

    /**
     * The unit for binary information (<code>bit</code>).
     */
    public static final Unit BIT = Unit.ONE.alternate("bit");

    /**
     * The derived unit for frequency (<code>Hz</code>).
     * A unit of frequency equal to one cycle per second.
     * After Heinrich Rudolf Hertz (1857-1894), German physicist who was the
     * first to produce radio waves artificially.
     */
    public static final Unit HERTZ = Unit.ONE.divide(SECOND).alternate("Hz");

    /**
     * The derived unit for force (<code>N</code>).
     * One newton is the force required to give a mass of 1 kilogram an Force
     * of 1 metre per second per second. It is named after the English
     * mathematician and physicist Sir Isaac Newton (1642-1727).
     */
    public static final Unit NEWTON
        = METER.multiply(KILOGRAM).divide(SECOND.pow(2)).alternate("N");

    /**
     * The derived unit for pressure, stress (<code>Pa</code>).
     * One pascal is equal to one newton per square meter. It is named after
     * the French philosopher and mathematician Blaise Pascal (1623-1662).
     */
    public static final Unit PASCAL
        = NEWTON.divide(METER.pow(2)).alternate("Pa");

    /**
     * The derived unit for energy, work, quantity of heat (<code>J</code>).
     * One joule is the amount of work done when an applied force of 1 newton
     * moves through a distance of 1 metre in the direction of the force.
     * It is named after the English physicist James Prescott Joule (1818-1889).
     */
    public static final Unit JOULE = NEWTON.multiply(METER).alternate("J");

    /**
     * The derived unit for power, radiant, flux (<code>W</code>).
     * One watt is equal to one joule per second. It is named after the British
     * scientist James Watt (1736-1819).
     */
    public static final Unit WATT = JOULE.divide(SECOND).alternate("W");

    /**
     * The derived unit for electric charge, quantity of electricity
     * (<code>C</code>).
     * One Coulomb is equal to the quantity of charge transferred in one second
     * by a steady current of one ampere. It is named after the French physicist
     * Charles Augustin de Coulomb (1736-1806).
     */
    public static final Unit COULOMB = SECOND.multiply(AMPERE).alternate("C");

    /**
     * The derived unit for electric potential difference, electromotive force
     * (<code>V</code>).
     * One Volt is equal to the difference of electric potential between two
     * points on a conducting wire carrying a constant current of one ampere
     * when the power dissipated between the points is one watt. It is named
     * after the Italian physicist Count Alessandro Volta (1745-1827).
     */
    public static final Unit VOLT = WATT.divide(AMPERE).alternate("V");

    /**
     * The derived unit for capacitance (<code>F</code>).
     * One Farad is equal to the capacitance of a capacitor having an equal
     * and opposite charge of 1 coulomb on each plate and a potential difference
     * of 1 volt between the plates. It is named after the British physicist
     * and chemist Michael Faraday (1791-1867).
     */
    public static final Unit FARAD = COULOMB.divide(VOLT).alternate("F");

    /**
     * The derived unit for electric resistance (<code>Ω</code>).
     * One Ohm is equal to the resistance of a conductor in which a current of
     * one ampere is produced by a potential of one volt across its terminals.
     * It is named after the German physicist Georg Simon Ohm (1789-1854).
     */
    public static final Unit OHM = VOLT.divide(AMPERE).alternate("Ω");

    /**
     * The derived unit for electric conductance (<code>S</code>).
     * One Siemens is equal to one ampere per volt. It is named after
     * the German engineer Ernst Werner von Siemens (1816-1892).
     */
    public static final Unit SIEMENS = AMPERE.divide(VOLT).alternate("S");

    /**
     * The derived unit for magnetic flux (<code>Wb</code>).
     * One Weber is equal to the magnetic flux that in linking a circuit of one
     * turn produces in it an electromotive force of one volt as it is uniformly
     * reduced to zero within one second. It is named after the German physicist
     * Wilhelm Eduard Weber (1804-1891).
     */
    public static final Unit WEBER = VOLT.multiply(SECOND).alternate("Wb");

    /**
     * The derived unit for magnetic flux density (<code>T</code>).
     * One Tesla is equal equal to one weber per square meter. It is named
     * after the Serbian-born American electrical engineer and physicist
     * Nikola Tesla (1856-1943).
     */
    public static final Unit TESLA = WEBER.divide(METER.pow(2)).alternate("T");

    /**
     * The derived unit for inductance (<code>H</code>).
     * One Henry is equal to the inductance for which an induced electromotive
     * force of one volt is produced when the current is varied at the rate of
     * one ampere per second. It is named after the American physicist
     * Joseph Henry (1791-1878).
     */
    public static final Unit HENRY = WEBER.divide(AMPERE).alternate("H");

    /**
     * The derived unit for Celsius temperature (<code>°C</code>).
     * This is a unit of temperature such as the freezing point of water
     * (at one atmosphere of pressure) is 0 °C, while the boiling point is
     * 100 °C.
     */
    public static final Unit CELSIUS = KELVIN.add(273.15);

    /**
     * The derived unit for luminous flux (<code>lm</code>).
     * One Lumen is equal to the amount of light given out through a solid angle
     * by a source of one candela intensity radiating equally in all directions.
     */
    public static final Unit LUMEN
        = CANDELA.multiply(STERADIAN).alternate("lm");

    /**
     * The derived unit for illuminance (<code>lx</code>).
     * One Lux is equal to one lumen per square meter.
     */
    public static final Unit LUX
        = LUMEN.divide(METER.pow(2)).alternate("lx");

    /**
     * The derived unit for activity of a radionuclide (<code>Bq</code>).
     * One becquerel is the radiation caused by one disintegration per second.
     * It is named after the French physicist, Antoine-Henri Becquerel
     * (1852-1908).
     */
    public static final Unit BECQUEREL
        = Unit.ONE.divide(SECOND).alternate("Bq");

    /**
     * The derived unit for absorbed dose, specific energy (imparted), kerma
     * (<code>Gy</code>).
     * One gray is equal to the dose of one joule of energy absorbed per one
     * kilogram of matter. It is named after the British physician
     * L. H. Gray (1905-1965).
     */
    public static final Unit GRAY = JOULE.divide(KILOGRAM).alternate("Gy");

    /**
     * The derived unit for dose equivalent (<code>Sv</code>).
     * One Sievert is equal  is equal to the actual dose, in grays, multiplied
     * by a "quality factor" which is larger for more dangerous forms of
     * radiation. It is named after the Swedish physicist Rolf Sievert
     * (1898-1966).
     */
    public static final Unit SIEVERT = JOULE.divide(KILOGRAM).alternate("Sv");

    /**
     * The derived unit for catalytic activity (<code>kat</code>).
     */
    public static final Unit KATAL = MOLE.divide(SECOND).alternate("kat");

    /////////////////
    // SI PREFIXES //
    /////////////////

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>24</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e24)</code>.
     */
    public static Unit YOTTA(Unit unit) {
        return unit.multiply(1e24);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>21</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e21)</code>.
     */
    public static Unit ZETTA(Unit unit) {
        return unit.multiply(1e21);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>18</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e18)</code>.
     */
    public static Unit EXA(Unit unit) {
        return unit.multiply(1e18);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>15</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e15)</code>.
     */
    public static Unit PETA(Unit unit) {
        return unit.multiply(1e15);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>12</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e12)</code>.
     */
    public static Unit TERA(Unit unit) {
        return unit.multiply(1e12);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>9</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e9)</code>.
     */
    public static Unit GIGA(Unit unit) {
        return unit.multiply(1e9);
    }
    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>6</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e6)</code>.
     */
    public static Unit MEGA(Unit unit) {
        return unit.multiply(1e6);
    }
    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>3</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e3)</code>.
     */
    public static Unit KILO(Unit unit) {
        return unit.multiply(1e3);
    }
    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>2</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e2)</code>.
     */
    public static Unit HECTO(Unit unit) {
        return unit.multiply(1e2);
    }
    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>1</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e1)</code>.
     */
    public static Unit DEKA(Unit unit) {
        return unit.multiply(1e1);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-1</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-1)</code>.
     */
    public static Unit DECI(Unit unit) {
        return unit.multiply(1e-1);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-2</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-2)</code>.
     */
    public static Unit CENTI(Unit unit) {
        return unit.multiply(1e-2);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-3</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-3)</code>.
     */
    public static Unit MILLI(Unit unit) {
        return unit.multiply(1e-3);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-6</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-6)</code>.
     */
    public static Unit MICRO(Unit unit) {
        return unit.multiply(1e-6);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-9</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-9)</code>.
     */
    public static Unit NANO(Unit unit) {
        return unit.multiply(1e-9);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-12</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-12)</code>.
     */
    public static Unit PICO(Unit unit) {
        return unit.multiply(1e-12);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-15</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-15)</code>.
     */
    public static Unit FEMTO(Unit unit) {
        return unit.multiply(1e-15);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-18</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-18)</code>.
     */
    public static Unit ATTO(Unit unit) {
        return unit.multiply(1e-18);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-21</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-21)</code>.
     */
    public static Unit ZEPTO(Unit unit) {
        return unit.multiply(1e-21);
    }

    /**
     * Returns the specified unit multiplied by the factor
     * <code>10<sup>-24</sup></code>
     *
     * @param  unit any unit.
     * @return <code>unit.multiply(1e-24)</code>.
     */
    public static Unit YOCTO(Unit unit) {
        return unit.multiply(1e-24);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Initializes the unit database for SI units.
    private static final Unit[] SI_UNITS
        = { SI.AMPERE, SI.BECQUEREL, SI.CANDELA, SI.COULOMB, SI.FARAD,
            SI.GRAY, SI.HENRY, SI.HERTZ, SI.JOULE, SI.KATAL, SI.KELVIN,
            SI.LUMEN, SI.LUX, SI.METER, SI.MOLE, SI.NEWTON, SI.OHM, SI.PASCAL,
            SI.RADIAN, SI.SECOND, SI.SIEMENS, SI.SIEVERT, SI.STERADIAN,
            SI.TESLA, SI.VOLT, SI.WATT, SI.WEBER };
    private static final String[] PREFIXES
        = { "Y", "Z", "E", "P", "T", "G", "M", "k", "h", "da",
            "d", "c", "m", "µ", "n", "p", "f", "a", "z", "y" };
    private static final double[] FACTORS
        = { 1e24, 1e21, 1e18, 1e15, 1e12, 1e9, 1e6, 1e3, 1e2, 1e1,
            1e-1, 1e-2, 1e-3, 1e-6, 1e-9, 1e-12, 1e-15, 1e-18, 1e-21, 1e-24 };
    static {
        for (int i=0; i < SI_UNITS.length; i++) {
            for (int j=0; j < PREFIXES.length; j++) {
                Unit u = SI_UNITS[i].multiply(FACTORS[j]);
                UnitFormat.label(u, PREFIXES[j] + SI_UNITS[i]._symbol);
            }
        }
        // Special case for KILOGRAM.
        UnitFormat.label(SI.GRAM, "g");
        for (int i=0; i < PREFIXES.length; i++) {
            if (FACTORS[i] != 1e3) { // kg is already defined.
                UnitFormat.label(SI.KILOGRAM.multiply(FACTORS[i] * 1e-3),
                                 PREFIXES[i] + "g");
            }
        }

        // Special case for DEGREE_CElSIUS.
        UnitFormat.label(SI.CELSIUS, "°C");
        for (int i=0; i < PREFIXES.length; i++) {
            UnitFormat.label(SI.CELSIUS.multiply(FACTORS[i]),
                             PREFIXES[i] + "°C");
        }
    }
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Static method to force class initialization.
     */
    static void initializeClass() {
    }

}