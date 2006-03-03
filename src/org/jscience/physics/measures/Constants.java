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

import javax.units.SI;
import javax.units.Unit;
import javax.quantities.*;

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
 *    CODATA Internationally recommended values of the Fundamental Physical
 *    Constants (1998)</a></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, March 1, 2006
 */
public final class Constants {

    /**
     * Holds the standard acceleration of gravity.
     */
    public final static Measure<Acceleration> g = 
        Measure.valueOf(9.80665, 0.00001, SI.METER_PER_SQUARE_SECOND);

    /**
     * Holds the electron rest mass.
     */
    public final static Measure<Mass> me = 
        Measure.valueOf(9.10938188e-31, 0.00000072e-31, SI.KILOGRAM);

    /**
     * Holds the proton rest mass.
     */
    public final static Measure<Mass> mp 
            = Measure.valueOf(1.67262158e-27, 0.00000013e-27, SI.KILOGRAM);

    /**
     * Holds the neutron rest mass.
     */
    public final static Measure<Mass> mn 
    = Measure.valueOf(1.67492716e-27, 0.00000013e-27, SI.KILOGRAM);

    /**
     * Holds the deuteron rest mass.
     */
    public final static Measure<Mass> md 
    = Measure.valueOf(3.34358309e-27, 0.00000026e-27, SI.KILOGRAM);

    /**
     * Holds the muon rest mass.
     */
    public final static Measure<Mass> mµ 
    = Measure.valueOf(1.88353109e-28, 0.00000016e-28, SI.KILOGRAM);

    /**
     * Holds the ratio of the circumference of a circle to its diameter.
     */
    public final static Measure<Dimensionless> π = Measure.valueOf(MathLib.PI, Unit.ONE);

    /**
     * Holds {@link #π}/2.
     */
    public final static Measure<Dimensionless> half_π = Measure.valueOf(MathLib.PI / 2, Unit.ONE);

    /**
     * Holds 2·{@link #π}.
     */
    public final static Measure<Dimensionless> two_π = Measure.valueOf(MathLib.PI * 2, Unit.ONE);

    /**
     * Holds 4·{@link #π}.
     */
    public final static Measure<Dimensionless> four_π = Measure.valueOf(MathLib.PI * 4, Unit.ONE);

    /**
     * Holds {@link #π}².
     */
    public final static Measure<Dimensionless> π_square = Measure.valueOf(MathLib.PI * MathLib.PI, Unit.ONE);

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
     */
    public final static Measure<?> k = Measure.valueOf(
        1.3806503E-23, 0.0000024E-23, SI.JOULE.divide(SI.KELVIN));

    /**
     * Holds the Planck constant.
     */
    public final static Measure<?> h = Measure.valueOf(
        6.62606876E-34, 0.00000052E-34, SI.JOULE.times(SI.SECOND));

    /**
     * Holds the Planck constant over 2π.
     */
    public final static Measure<?> hBar = 
        Measure.valueOf(
                6.62606876E-34 / MathLib.TWO_PI, 0.00000052E-34 / MathLib.TWO_PI, 
                SI.JOULE.times(SI.SECOND));

    /**
     * Holds the elementary charge (positron charge).
     */
    public final static Measure<ElectricCharge> ePlus =
        Measure.valueOf(
            1.602176462e-19, 0.000000063e-19, SI.COULOMB);

    /**
     * Holds the permeability of vacuum or magnetic constant.
     */
    public final static Measure<?> µ0 = Measure.valueOf(
        4 * MathLib.PI * 1E-7,
        SI.NEWTON.divide(SI.AMPERE.pow(2)));

    /**
     * Holds the permittivity of vacuum or electric constant (1 / (µ0·c²))
     */
    public final static Measure<?> ε0 = µ0.times(c.pow(2));

    /**
     * Holds the characteristic impedance of vacuum (µ0·c).
     */
    public final static Measure<ElectricResistance> Z0 =  µ0.times(c).to(SI.OHM);

    /**
     * Holds the fine structure constant (e²/(4π·ε0·hBar·c))
     */
    public final static Measure<Dimensionless> α
        = ePlus.pow(2).divide(four_π.times(ε0).times(hBar).times(c)).to(Unit.ONE);

    /**
     * Holds the Newtonian constant of gravitation.
     */
    public final static Measure<?> G = Measure.valueOf(
        6.673e-11, 0.010e-11,
        SI.METER.pow(3).divide(SI.KILOGRAM).divide(SI.SECOND.pow(2)));

    /**
     * Holds the Avogadro constant.
     */
    public final static Measure<?> N = Measure.valueOf(
        6.02214199e23, 0.00000047e23, Unit.ONE.divide(SI.MOLE));

    /**
     * Holds the molar gas constant (N·k)
     */
    public final static Measure<?> R = N.times(k);

    /**
     * Holds the Faraday constant (N·ePlus)
     */
    public final static Measure<?> F = N.times(ePlus);

    /**
     * Holds the Stefan-Boltzmann constant ((π²/60)·k<sup>4</sup>/(hBar³·c²))
     */
    public final static Measure<?> σ
        = π.pow(2).divide(60).times(k.pow(4)).divide(
            hBar.pow(3).times(c.pow(2)));

    /**
     * Holds the unified atomic mass unit (0.001 kg/mol)/N
     */
    public final static Measure<Mass> amu
        =  Measure.valueOf(
            1E-3, SI.KILOGRAM.divide(SI.MOLE)).divide(N).to(SI.KILOGRAM);

    /**
     * Holds the Rydberg constant (α³·me·c/2h)
     */
    public final static Measure<?> Rinf
        = α.pow(2).times(me).times(c).divide(h.times(2));

    /**
     * Holds the Bohr radius (α/(4π·Rinf))
     */
    public final static Measure<Length> a0
        = α.divide(π.times(Rinf).times(4)).to(SI.METER);

    /**
     * Holds the Hartree energy (2Rinf·h·c)
     */
    public final static Measure<?> Eh
        =  Rinf.times(h).times(c).times(2);

    /**
     * Holds the magnetic flux quantum (h/2ePlus)
     */
    public final static Measure<MagneticFlux> Φ0
        = h.divide(ePlus).divide(2).to(SI.WEBER);

    /**
     * Holds the conductance quantum (2ePlus³/h)
     */
    public final static Measure<ElectricConductance> G0
        =  ePlus.pow(2).divide(h).times(2).to(ElectricConductance.SI_UNIT);

    /**
     * Holds the Bohr magneton (ePlus·hBar/2me)
     */
    public final static Measure<?> µB
        = ePlus.times(hBar).divide(me).divide(2);

    /**
     * Holds the nuclear magneton (ePlus·hBar/2mp)
     */
    public final static Measure<?> µN
        = ePlus.times(hBar).divide(mp).divide(2);

    /**
     * Holds the Planck mass (hBar·c/G)<sup>1/2</sup>
     */
    public final static Measure<Mass> mP = hBar.times(c).divide(G).root(2).to(SI.KILOGRAM);

    /**
     * Holds the Planck length (hBar/(mP·c))
     */
    public final static Measure<Length> lP = hBar.divide(mP.times(c)).to(SI.METER);

    /**
     * Holds the Planck time (lP/c)
     */
    public final static Measure<Duration> tP = lP.divide(c).to(SI.SECOND);

    /**
     * Default constructor (no derivation allows).
     */
    private Constants() {}
}