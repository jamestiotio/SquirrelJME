// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classwriter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This class is used to write standard Java class files.
 *
 * @since 2016/06/20
 */
public class OutputClass
{
	/** The class magic number. */
	public static final int MAGIC_NUMBER =
		0xCAFE_BABE;
	
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The class version number. */
	private volatile OutputVersion _version;
	
	/**
	 * Sets the version of the class.
	 *
	 * @param __ver The class version number.
	 * @throws IllegalArgumentException
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public void setVersion(OutputVersion __ver)
		throws NullPointerException
	{
		// Check
		if (__ver == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._version = __ver;
		}
	}
	
	/**
	 * Writes the class to the given output stream.
	 *
	 * @param __os The stream to write to.
	 * @throws IllegalStateException If required information is missing or
	 * is invalid.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public void write(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Wrap as a data stream
		DataOutputStream dos = new DataOutputStream(__os);
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error CO01 No version was specified for the
			// output class.}
			OutputVersion version = this._version;
			if (version == null)
				throw new IllegalStateException("CO01");
			
			if (true)
				throw new Error("TODO");
			
			// Write magic
			dos.writeInt(MAGIC_NUMBER);
			
			// Write the version
			int magic = version.version();
			dos.writeShort((short)(magic & 0xFFFF));
			dos.writeShort((short)((magic >>> 16) & 0xFFFF));
			
			if (true)
				throw new Error("TODO");
		}
	}
}

