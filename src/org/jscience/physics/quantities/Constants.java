/*
 * jScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2004 - The jScience Consortium (http://jscience.org/)
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation (http://www.gnu.org/copyleft/lesser.html); either version
 * 2.1 of the License, or any later version.
 */
package org.jscience.physics.quantities;
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
    public final static Scalar π = Scalar.valueOf(Math.PI);

    /**
     * Holds {@link #π}/2.
     */
    public final static Scalar half_π = Scalar.valueOf(Math.PI / 2);

    /**
     * Holds 2·{@link #π}.
     */
    public final static Scalar two_π = Scalar.valueOf(2 * Math.PI);

    /**
     * Holds 4·{@link #π}.
     */
    public final static Scalar four_π = Scalar.valueOf(4 * Math.PI);

    /**
     * Holds {@link #π}².
     */
    public final static Scalar π_square = Scalar.valueOf(Math.PI * Math.PI);

    /**
     * Holds the speed of light in vacuum.
     * @see    Velocity#SPEED_OF_LIGHT
     */
    public final static Velocity c = Velocity.SPEED_OF_LIGHT;

    /**
     * Holds {@link #c}².
     */
    public final static Quantity c_square
        = Velocity.SPEED_OF_LIGHT.multiply(Velocity.SPEED_OF_LIGHT);

    /**
     * Holds the Boltzmann constant.
     */
    public final static Quantity k = Quantity.valueOf(
        1.3806503E-23, 0.0000024E-23,
        SI.JOULE.divide(SI.KELVIN).getDimension());

    /**
     * Holds the Planck constant.
     */
    public final static Quantity h = Quantity.valueOf(
        6.62606876E-34, 0.00000052E-34,
        SI.JOULE.multiply(SI.SECOND).getDimension());

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
        4 * Math.PI * 1E-7,
        SI.NEWTON.divide(SI.AMPERE.pow(2)).getDimension()); // Exact.

    /**
     * Holds the permittivity of vacuum or electric constant (1 / (µ0·c²))
     */
    public final static Quantity ε0
        = Scalar.ONE.divide(µ0.multiply(c.pow(2.0)));

    /**
     * Holds the characteristic impedance of vacuum (µ0·c).
     */
    public final static ElectricResistance Z0
        = (ElectricResistance) µ0.multiply(c);

    /**
     * Holds the fine structure constant (e²/(4π·ε0·hBar·c))
     */
    public final static Scalar α
        = (Scalar) ePlus.pow(2.0).divide(
            four_π.multiply(ε0).multiply(hBar).multiply(c));

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
    public final static Quantity R = N.multiply(k);

    /**
     * Holds the Faraday constant (N·ePlus)
     */
    public final static Quantity F = N.multiply(ePlus);

    /**
     * Holds the Stefan-Boltzmann constant ((π²/60)·k<sup>4</sup>/(hBar³·c²))
     */
    public final static Quantity σ
        = π.pow(2L).divide(60).multiply(k.pow(4.0)).divide(
            hBar.pow(3.0).multiply(c.pow(2.0)));

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
        = α.pow(2L).multiply(me).multiply(c).divide(h.multiply(2));

    /**
     * Holds the Bohr radius (α/(4π·Rinf))
     */
    public final static Length a0
        = (Length) α.divide(π.multiply(Rinf).multiply(4));

    /**
     * Holds the Hartree energy (2Rinf·h·c)
     */
    public final static Quantity Eh
        =  Rinf.multiply(h).multiply(c).multiply(2);

    /**
     * Holds the magnetic flux quantum (h/2ePlus)
     */
    public final static MagneticFlux Φ0
        = (MagneticFlux) h.divide(ePlus).divide(2);

    /**
     * Holds the conductance quantum (2ePlus³/h)
     */
    public final static ElectricConductance G0
        = (ElectricConductance) ePlus.pow(2.0).divide(h).multiply(2);

    /**
     * Holds the Bohr magneton (ePlus·hBar/2me)
     */
    public final static Quantity µB
        = ePlus.multiply(hBar).divide(me).divide(2);

    /**
     * Holds the nuclear magneton (ePlus·hBar/2mp)
     */
    public final static Quantity µN
        = ePlus.multiply(hBar).divide(mp).divide(2);

    /**
     * Holds the Planck mass (hBar·c/G)<sup>1/2</sup>
     */
    public final static Mass mP = (Mass) hBar.multiply(c).divide(G).root(2);

    /**
     * Holds the Planck length (hBar/(mP·c))
     */
    public final static Length lP = (Length) hBar.divide(mP.multiply(c));

    /**
     * Holds the Planck time (lP/c)
     */
    public final static Duration tP = (Duration) lP.divide(c);

    /**
     * Default constructor (allows for extension).
     */
    protected Constants() {}
}