// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is used to write class details on output.
 *
 * @since 2016/07/06
 */
public interface JITClassWriter
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2016/07/06
	 */
	@Override
	public abstract void close()
		throws JITException;
}

