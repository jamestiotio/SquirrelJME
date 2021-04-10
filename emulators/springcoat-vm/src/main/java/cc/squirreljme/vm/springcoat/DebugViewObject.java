// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.JDWPState;
import cc.squirreljme.jdwp.JDWPValue;
import cc.squirreljme.jdwp.JDWPViewObject;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * This is a view around an object within the debugger.
 *
 * @since 2021/04/10
 */
public class DebugViewObject
	implements JDWPViewObject
{
	/** The state of the debugger. */
	protected final Reference<JDWPState> state;
	
	/**
	 * Initializes the object viewer.
	 * 
	 * @param __state The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public DebugViewObject(Reference<JDWPState> __state)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean isArray(Object __what)
	{
		return (__what instanceof SpringArrayObject);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean isValid(Object __what)
	{
		return (__what instanceof SpringObject);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean readValue(Object __what, int __index, JDWPValue __out)
	{
		// Nulls are never valid
		if (__what == null || __what == SpringNullObject.NULL)
			return false;
		
		// Is a simple object representation, so we can read a field value
		// directly from the object representation without knowing about
		// the class details.
		if (__what instanceof SpringSimpleObject)
		{
			SpringFieldStorage[] store = ((SpringSimpleObject)__what)._fields;
			if (__index >= 0 && __index < store.length)
			{
				__out.set(store[__index].get());
				return true;
			}
		}
		
		// Not a valid object or one where a value can be read from
		return false;
	}
}
