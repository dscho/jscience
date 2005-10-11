/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2005 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.physics.quantities;
import javolution.lang.MathLib;

import org.jscience.physics.units.SI;
import org.jscience.physics.units.Unit;

/**
 * <p> This class defines fundamental physical constants in their symbolic
 *     form.</p>
 * <p> The new JDK1.5 (Tiger), lets you avoid qualifying static members with
 *     class names, making it easier to use this class. For example:<pre>
 *     import org.jscience.physics.quantities.Constants.*; // JDK1.5+ new feature.
 *     public class Ellipse {
 *         Length a, b;
 *         public Area area() {
 *              return (Area) π.multiply(a).multiply(b);
 *         }
 *    }</pre></p>
 * <p> Fundamental constants may also be defined in non-symbolic form in
 *     the corresponding quantity class (e.g. {@link Velocity#SPEED_OF_LIGHT}
 *     for {@link #c} or {@link Mass#ELECTRON} for {@link #me}).</p>
 *
 * <p> Note: Constant names use the full range of Unicode characters and
 *           are mixed uppercase/lowercase to resemble symbolic names as much
 *           as possible </p>
 *
 * <p> Reference: <a href="http://physics.nist.gov/cuu/Constants/index.html">
 *    CODATA Internationally recommended values of the Fundamental Physical
 *    Constants (1998)</a></p>
 *
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.6, July 15, 2003
 */
public abstract class Constants {

    /**
     * Holds the standard acceleration of gravity.
     * @see    Acceleration#GRAVITY
     */
    public final static Acceleration g = Acceleration.GRAVITY;

    /**
     * Holds the electron rest mass.
     * @see    Mass#ELECTRON
     */
    public final static Mass me = Mass.ELECTRON;

    /**
     * Holds the proton rest mass.
     * @see    Mass#PROTON
     */
    public final static Mass mp = Mass.PROTON;

    /**
     * Holds the neutron rest mass.
     * @see    Mass#NEUTRON
     */
    public final static Mass mn = Mass.NEUTRON;

    /**
     * Holds the deuteron rest mass.
     * @see    Mass#DEUTERON
     */
    public final static Mass md = Mass.DEUTERON;

    /**
     * Holds the muon rest mass.
     * @see    Mass#MUON
     */
    public final static Mass mµ = Mass.MUON;

    /**
     * Holds the ratio of the circumference of a circle to its diameter.
     */
    public final static Dimensionless π = Quantity.valueOf(MathLib.PI, Unit.ONE);

    /**
     * Holds {@link #π}/2.
     */
    public final static Dimensionless half_π = Quantity.valueOf(MathLib.PI / 2, Unit.ONE);

    /**
     * Holds 2·{@link #π}.
     */
    public final static Dimensionless two_π = Quantity.valueOf(MathLib.PI * 2, Unit.ONE);

    /**
     * Holds 4·{@link #π}.
     */
    public final static Dimensionless four_π = Quantity.valueOf(MathLib.PI * 4, Unit.ONE);

    /**
     * Holds {@link #π}².
     */
    public final static Dimensionless π_square = Quantity.valueOf(MathLib.PI * MathLib.PI, Unit.ONE);

    /**
     * Holds the speed of light in vacuum.
     * @see    Velocity#SPEED_OF_LIGHT
     */
    public final static Velocity c = Velocity.SPEED_OF_LIGHT;

    /**
     * Holds {@link #c}².
     */
    public final static Quantity c_square
        = Velocity.SPEED_OF_LIGHT.times(Velocity.SPEED_OF_LIGHT);

    /**
     * Holds the Boltzmann constant.
     */
    public final static Quantity k = Quantity.valueOf(
        1.3806503E-23, 0.0000024E-23, SI.JOULE.divide(SI.KELVIN));

    /**
     * Holds the Planck constant.
     */
    public final static Quantity h = Quantity.valueOf(
        6.62606876E-34, 0.00000052E-34, SI.JOULE.times(SI.SECOND));

    /**
     * Holds the Planck constant over 2π.
     */
    public final static Quantity hBar = h.divide(two_π);

    /**
     * Holds the elementary charge (positron charge).
     */
    public final static ElectricCharge ePlus = ElectricCharge.ELEMENTARY;

    /**
     * Holds the permeability of vacuum or magnetic constant.
     */
    public final static Quantity µ0 = Quantity.valueOf(
        4 * MathLib.PI * 1E-7,
        SI.NEWTON.divide(SI.AMPERE.pow(2))); // Exact.

    /**
     * Holds the permittivity of vacuum or electric constant (1 / (µ0·c²))
     */
    public final static Quantity ε0
        = Dimensionless.ONE.divide(µ0.times(c.pow(2)));

    /**
     * Holds the characteristic impedance of vacuum (µ0·c).
     */
    public final static ElectricResistance Z0
        = (ElectricResistance) µ0.times(c);

    /**
     * Holds the fine structure constant (e²/(4π·ε0·hBar·c))
     */
    public final static Dimensionless α
        = ePlus.pow(2).divide(four_π.times(ε0).times(hBar).times(c)).to(Unit.ONE);

    /**
     * Holds the Newtonian constant of gravitation.
     */
    public final static Quantity G = Quantity.valueOf(
        6.673e-11, 0.010e-11,
        SI.METER.pow(3).divide(SI.KILOGRAM).divide(SI.SECOND.pow(2)));

    /**
     * Holds the Avogadro constant.
     */
    public final static Quantity N = Quantity.valueOf(
        6.02214199e23, 0.00000047e23, Unit.ONE.divide(SI.MOLE));

    /**
     * Holds the molar gas constant (N·k)
     */
    public final static Quantity R = N.times(k);

    /**
     * Holds the Faraday constant (N·ePlus)
     */
    public final static Quantity F = N.times(ePlus);

    /**
     * Holds the Stefan-Boltzmann constant ((π²/60)·k<sup>4</sup>/(hBar³·c²))
     */
    public final static Quantity σ
        = π.pow(2).divide(60).times(k.pow(4)).divide(
            hBar.pow(3).times(c.pow(2)));

    /**
     * Holds the unified atomic mass unit (0.001 kg/mol)/N
     */
    public final static Mass amu
        = (Mass) Quantity.valueOf(
            1E-3, SI.KILOGRAM.divide(SI.MOLE)).divide(N);

    /**
     * Holds the Rydberg constant (α³·me·c/2h)
     */
    public final static Quantity Rinf
        = α.pow(2).times(me).times(c).divide(h.times(2));

    /**
     * Holds the Bohr radius (α/(4π·Rinf))
     */
    public final static Length a0
        = (Length) α.divide(π.times(Rinf).times(4));

    /**
     * Holds the Hartree energy (2Rinf·h·c)
     */
    public final static Quantity Eh
        =  Rinf.times(h).times(c).times(2);

    /**
     * Holds the magnetic flux quantum (h/2ePlus)
     */
    public final static MagneticFlux Φ0
        = (MagneticFlux) h.divide(ePlus).divide(2);

    /**
     * Holds the conductance quantum (2ePlus³/h)
     */
    public final static ElectricConductance G0
        = (ElectricConductance) ePlus.pow(2).divide(h).times(2);

    /**
     * Holds the Bohr magneton (ePlus·hBar/2me)
     */
    public final static Quantity µB
        = ePlus.times(hBar).divide(me).divide(2);

    /**
     * Holds the nuclear magneton (ePlus·hBar/2mp)
     */
    public final static Quantity µN
        = ePlus.times(hBar).divide(mp).divide(2);

    /**
     * Holds the Planck mass (hBar·c/G)<sup>1/2</sup>
     */
    public final static Mass mP = (Mass) hBar.times(c).divide(G).root(2);

    /**
     * Holds the Planck length (hBar/(mP·c))
     */
    public final static Length lP = (Length) hBar.divide(mP.times(c));

    /**
     * Holds the Planck time (lP/c)
     */
    public final static Duration tP = (Duration) lP.divide(c);

    /**
     * Default constructor (allows for extension).
     */
    protected Constants() {}
}