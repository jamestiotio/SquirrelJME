// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.swm;

/**
 * This interface is used for anything which represents a dependency and as
 * such can be used to check if a provided meets the conditions for a match.
 *
 * @since 2017/12/31
 */
public interface MarkedDependency
{
	/**
	 * Is this dependency optional?
	 *
	 * @return {@code true} if this dependency is optional.
	 * @since 2017/12/31
	 */
	boolean isOptional();
	
	/**
	 * Checks if this dependency matches the specified provision in that the
	 * provided entry is acceptable to be used for this dependency.
	 *
	 * @param __mp The provided to check.
	 * @return {@code true} if the provided is valid for this dependency.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	boolean matchesProvided(MarkedProvided __mp)
		throws NullPointerException;
}

