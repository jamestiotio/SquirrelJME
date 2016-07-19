// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceContent;
import net.multiphasicapps.zip.blockreader.ZipEntry;

/**
 * This represents an entry which exists within a directory.
 *
 * @since 2016/07/07
 */
public class BuildEntry
	implements JITNamespaceContent.Entry
{
	/** The entry to use. */
	protected final ZipEntry entry;
	
	/** The owning builder. */
	protected final Builder builder;
	
	/**
	 * Initializes the build entry.
	 *
	 * @param __b The owning builder.
	 * @param __ze The entry to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/07
	 */
	BuildEntry(Builder __b, ZipEntry __ze)
		throws NullPointerException
	{
		// Check
		if (__b == null || __ze == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.builder = __b;
		this.entry = __ze;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/07
	 */
	@Override
	public String name()
		throws JITException
	{
		try
		{
			return this.entry.name();
		}
		
		// {@squirreljme.error DW0d Could not read the entry name.}
		catch (IOException e)
		{
			throw new JITException("DW0d", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/07
	 */
	@Override
	public InputStream open()
		throws IOException
	{
		return this.entry.open();
	}
}

