/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.measures;
import javolution.lang.MathLib;

import javax.measure.quantities.*;
import javax.measure.units.SI;
import javax.measure.units.Unit;

/**
 * <p> This class provides most accurate physical constants {@link Measure 
 *     measures}; the more accurate the constants, the higher the precision 
 *     of the calculations making use of these constants.</p>
 *     
 * <p> Constant names use the full range of Unicode characters and
 *     are mixed uppercase/lowercase to resemble symbolic names as much
 *     as possible </p>
 *
 * <p> Reference: <a href="http://physics.nist.gov/cuu/Constants/index.html">
 *     CODATA Internationally recommended values of the Fundamental Physical
 *     Constants (2002)</a></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.1, April 7, 2006
 */
public final class Constants {

    /**
     * Holds the standard acceleration due to gravity (approximately equal 
     * to the acceleration due to gravity on the Earth's surface).
     * @see <a href="http://en.wikipedia.org/wiki/Acceleration_due_to_gravity">
     *      Wikipedia: Acceleration due to gravity</a>
     */
    public final static Measure<Acceleration> g = 
        Measure.valueOf(980665, SI.METER_PER_SQUARE_SECOND).divide(100000); 

    /**
     * Holds the electron rest mass.
     */
    public final static Measure<Mass> me = 
        Measure.valueOf(9.1093826E-31, 0.0000016E-31, SI.KILOGRAM);

    /**
     * Holds the proton rest mass.
     */
    public final static Measure<Mass> mp 
            = Measure.valueOf(1.67262171E-27, 0.00000029E-27, SI.KILOGRAM);

    /**
     * Holds the neutron rest mass.
     */
    public final static Measure<Mass> mn 
    = Measure.valueOf(1.67492728E-27, 0.00000029E-27, SI.KILOGRAM);

    /**
     * Holds the deuteron rest mass.
     */
    public final static Measure<Mass> md 
    = Measure.valueOf(3.34358335E-27, 0.00000057E-27, SI.KILOGRAM);

    /**
     * Holds the muon rest mass.
     */
    public final static Measure<Mass> mμ 
    = Measure.valueOf(1.88353140E-28, 0.00000033E-28, SI.KILOGRAM);

    /**
     * Holds the ratio of the circumference of a circle to its diameter.
     */
    public final static Measure<Dimensionless> π = Measure.valueOf(MathLib.PI, Unit.ONE);

    /**
     * Holds {@link #π}/2.
     */
    public final static Measure<Dimensionless> half_π = Measure.valueOf(MathLib.HALF_PI, Unit.ONE);

    /**
     * Holds 2·{@link #π}.
     */
    public final static Measure<Dimensionless> two_π = Measure.valueOf(MathLib.TWO_PI, Unit.ONE);

    /**
     * Holds 4·{@link #π}.
     */
    public final static Measure<Dimensionless> four_π = Measure.valueOf(12.566370614359172953850573533118, Unit.ONE);

    /**
     * Holds {@link #π}².
     */
    public final static Measure<Dimensionless> π_square = Measure.valueOf(9.8696044010893586188344909998762, Unit.ONE);

    /**
     * Holds the speed of light in vacuum (exact).
     */
    public final static Measure<Velocity>  c = Measure.valueOf(299792458, SI.METER_PER_SECOND);
    
    /**
     * Holds {@link #c}².
     */
    public final static Measure<?> c_square = 
        Measure.valueOf(299792458L * 299792458L, SI.METER_PER_SECOND.pow(2));

    /**
     * Holds the Boltzmann constant.
     * @see <a href="http://en.wikipedia.org/wiki/Boltzmanns_constant">
     *      Wikipedia: Boltzmann constant</a>
     */
    public final static Measure<?> k = Measure.valueOf(
        1.3806505E-23, 0.0000024E-23, SI.JOULE.divide(SI.KELVIN));

    /**
     * Holds the Planck constant.
     * @see <a href="http://en.wikipedia.org/wiki/Plank%27s_constant">
     *      Wikipedia: Plank's constant</a>
     */
    public final static Measure<?> ℎ = Measure.valueOf(
        6.6260693E-34, 0.0000011E-34, SI.JOULE.times(SI.SECOND));

    /**
     * Holds the Planck constant over 2π.
     */
    public final static Measure<?> ℏ = ℎ.divide(two_π);

    /**
     * Holds the elementary charge (positron charge).
     * @see <a href="http://en.wikipedia.org/wiki/Elementary_charge">
     *      Wikipedia: Elementary Charge</a>
     */
    public final static Measure<ElectricCharge> e =
        Measure.valueOf(
            1.60217653E-19, 0.00000014E-19, SI.COULOMB);

    /**
     * Holds the permeability of vacuum or magnetic constant.
     * @see <a href="http://en.wikipedia.org/wiki/Permeability_%28electromagnetism%29">
     *      Wikipedia: Permeability (electromagnetism)</a>
     */
    public final static Measure<?> µ0 =  Measure.valueOf(
            1.2566370614359172953850573533118E-6,
            SI.NEWTON.divide(SI.AMPERE.pow(2))); // 4π×10−7 N/A²

    /**
     * Holds the permittivity of vacuum or electric constant (1/(µ0·c²))
     * @see <a href="http://en.wikipedia.org/wiki/Permittivity">
     *      Wikipedia: Permittivity</a>
     */
    public final static Measure<?> ε0 = µ0.times(c.pow(2)).inverse();

    /**
     * Holds the characteristic impedance of vacuum (µ0·c).
     */
    public final static Measure<ElectricResistance> Z0 =  µ0.times(c).to(SI.OHM);

    /**
     * Holds the fine structure constant (e²/(2·ε0·c·h))
     * @see <a href="http://en.wikipedia.org/wiki/Fine_structure_constant">
     *      Wikipedia: Fine Structure Constant</a>
     */
    public final static Measure<Dimensionless> α
        = e.pow(2).divide(ε0.times(c).times(ℎ).times(2)).to(Unit.ONE);

    /**
     * Holds the Newtonian constant of gravitation.
     * @see <a href="http://en.wikipedia.org/wiki/Gravitational_constant">
     *      Wikipedia: Gravitational Constant</a>
     */
    public final static Measure<?> G = Measure.valueOf(
        6.6742E-11, 0.001E-11,
        SI.METER.pow(3).divide(SI.KILOGRAM).divide(SI.SECOND.pow(2)));

    /**
     * Holds the Avogadro constant.
     * @see <a href="http://en.wikipedia.org/wiki/Avogadro%27s_number">
     *      Wikipedia: Avogadro's number</a>
     */
    public final static Measure<?> N = Measure.valueOf(
        6.0221415E23, 0.0000010E23, Unit.ONE.divide(SI.MOLE));

    /**
     * Holds the molar gas constant (N·k)
     * @see <a href="http://en.wikipedia.org/wiki/Gas_constant">
     *      Wikipedia: Gas constant</a>
     */
    public final static Measure<?> R = N.times(k);

    /**
     * Holds the Faraday constant (N·e)
     * @see <a href="http://en.wikipedia.org/wiki/Faraday_constant">
     *      Wikipedia: Faraday constant</a>
     */
    public final static Measure<?> F = N.times(e);

    /**
     * Holds the Stefan-Boltzmann constant ((π²/60)·k<sup>4</sup>/(ℏ³·c²))
     */
    public final static Measure<?> σ
        = π_square.divide(60).times(k.pow(4)).divide(ℏ.pow(3).times(c.pow(2)));

    /**
     * Holds the unified atomic mass unit (0.001 kg/mol)/N
     */
    public final static Measure<Mass> amu
        =  Measure.valueOf(
            1E-3, SI.KILOGRAM.divide(SI.MOLE)).divide(N).to(SI.KILOGRAM);

    /**
     * Holds the Rydberg constant (α²·me·c/2h).
     * @see <a href="http://en.wikipedia.org/wiki/Rydberg_constant">
     *      Wikipedia: Rydberg constant</a>
     */
    public final static Measure<?> Rinf 
        // Do not use formala as experimental incertainty is very low. 
        = Measure.valueOf(10973731.568525, 0.000073, SI.METER.inverse());
    
    /**
     * Holds the Bohr radius (α/(4π·Rinf))
     */
    public final static Measure<Length> a0
        = α.divide(π.times(Rinf).times(4)).to(SI.METER);

    /**
     * Holds the Hartree energy (2Rinf·h·c)
     */
    public final static Measure<?> Eh
        =  Rinf.times(ℎ).times(c).times(2);

    /**
     * Holds the magnetic flux quantum (h/2e)
     */
    public final static Measure<MagneticFlux> Φ0
        = ℎ.divide(e).divide(2).to(SI.WEBER);

    /**
     * Holds the conductance quantum (2e²/h)
     */
    public final static Measure<ElectricConductance> G0
        =  e.pow(2).divide(ℎ).times(2).to(ElectricConductance.UNIT);

    /**
     * Holds the Bohr magneton (ℏ·e/2me)
     */
    public final static Measure<?> µB
        = e.times(ℏ).divide(me).divide(2);

    /**
     * Holds the nuclear magneton (ℏ·e/2mp)
     */
    public final static Measure<?> µN
        = e.times(ℏ).divide(mp).divide(2);

    /**
     * Holds the Planck mass (ℏ·c/G)<sup>1/2</sup>
     */
   public final static Measure<Mass> mP = ℏ.times(c).divide(G).root(2).to(SI.KILOGRAM);

    /**
     * Holds the Planck length (ℏ/(mP·c))
     */
    public final static Measure<Length> lP = ℏ.divide(mP.times(c)).to(SI.METER);

    /**
     * Holds the Planck time (lP/c)
     */
    public final static Measure<Duration> tP = lP.divide(c).to(SI.SECOND);

    /**
     * Default constructor (no derivation allows).
     */
    private Constants() {}
}