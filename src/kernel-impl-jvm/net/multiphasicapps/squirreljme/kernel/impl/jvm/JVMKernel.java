// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm;

import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelException;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;
import net.multiphasicapps.squirreljme.kernel.KernelThread;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This is the kernel which runs on an existing JVM.
 *
 * @since 2016/05/27
 */
public class JVMKernel
	extends Kernel
{
	/**
	 * Initializes the kernel.
	 *
	 * @param __terp The interpreter backend to use for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/27
	 */
	public JVMKernel(Interpreter __terp, String... __args)
		throws NullPointerException
	{
		super(__terp, __args);
		
		// Check
		if (__terp == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/30
	 */
	@Override
	protected ClassUnitProvider[] internalClassUnitProviders()
		throws KernelException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelProcess internalCreateProcess()
		throws KernelException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelThread internalCreateThread()
		throws KernelException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected KernelThread internalCurrentThread()
		throws KernelException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	protected void internalRunCycle()
		throws KernelException
	{
		// Run a single interpreter cycle
		interpreter().runCycle();
	}
	
	/**
	 * Returns the interpreter being used.
	 *
	 * @return The interpreter to use.
	 * @since 2016/05/30
	 */
	protected Interpreter interpreter()
	{
		return (Interpreter)this.executioncore;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/27
	 */
	@Override
	public void quitKernel()
	{
		System.exit(0);
	}
}

