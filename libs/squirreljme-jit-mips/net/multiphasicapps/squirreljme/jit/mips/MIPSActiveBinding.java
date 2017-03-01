// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.jit.ActiveBinding;
import net.multiphasicapps.squirreljme.jit.Binding;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is an active binding for MIPS.
 *
 * This class is mutable.
 *
 * @since 2017/02/23
 */
public class MIPSActiveBinding
	implements ActiveBinding
{
	/** Registers being used. */
	protected final List<MIPSRegister> registers =
		new ArrayList<>();
	
	/**
	 * Initializes the active binding.
	 *
	 * @since 2017/02/23
	 */
	public MIPSActiveBinding()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the registers which are used for the binding.
	 *
	 * @param __r The registers to use.
	 * @since 2017/03/01
	 */
	public void setRegisters(MIPSRegister... __r)
	{
		List<MIPSRegister> registers = this.registers;
		registers.clear();
		for (MIPSRegister r : __r)
			registers.add(r);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public void switchFrom(Binding __b)
		throws JITException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM05 The input binding is not one for this JIT.}
		if (!(__b instanceof MIPSBinding))
			throw new JITException("AM05");
		MIPSBinding bind = (MIPSBinding)__b;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

