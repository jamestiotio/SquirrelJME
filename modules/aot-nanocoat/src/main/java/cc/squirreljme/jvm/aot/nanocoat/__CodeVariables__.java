// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CVariable;
import cc.squirreljme.jvm.aot.nanocoat.common.NanoCoatTypes;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Utility class for making code variables.
 *
 * @since 2023/06/19
 */
public final class __CodeVariables__
{
	/** Instance of this class. */
	private static volatile __CodeVariables__ _INSTANCE;
	
	/** Current frame cache. */
	private volatile Reference<CVariable> _currentFrame;
	
	/** Current state cache. */
	private volatile Reference<CVariable> _currentState;
	
	/**
	 * Not used.
	 * 
	 * @since 2023/06/19
	 */
	private __CodeVariables__()
	{
	}
	
	/**
	 * Returns the variable that represents the current stack frame.
	 * 
	 * @return The variable that represents the current stack frame.
	 * @since 2023/06/19
	 */
	public CVariable currentFrame()
	{
		Reference<CVariable> ref = this._currentFrame;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CVariable.of(NanoCoatTypes.VMFRAME.type().pointerType(),
				"currentFrame");
			this._currentFrame = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the current state variable.
	 * 
	 * @return The current state variable.
	 * @since 2023/06/19
	 */
	public CVariable currentState()
	{
		Reference<CVariable> ref = this._currentState;
		CVariable rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CVariable.of(NanoCoatTypes.VMSTATE.type().pointerType(),
				"currentState");
			this._currentState = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns the instance of the code variables.
	 * 
	 * @return The instance of this.
	 * @since 2023/06/19
	 */
	public static final __CodeVariables__ instance()
	{
		__CodeVariables__ rv = __CodeVariables__._INSTANCE;
		if (rv != null)
			return rv;
		
		rv = new __CodeVariables__();
		__CodeVariables__._INSTANCE = rv;
		return rv;
	}
}
