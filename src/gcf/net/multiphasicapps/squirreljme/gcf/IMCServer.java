// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gcf;

import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.IMCServerConnection;
import javax.microedition.io.StreamConnection;
import net.multiphasicapps.squirreljme.midletid.MidletVersion;

/**
 * This implements the server side of an IMC connection.
 *
 * @since 2016/10/12
 */
public class IMCServer
	implements IMCServerConnection
{
	/** The server name. */
	protected final String name;
	
	/** The server version. */
	protected final MidletVersion version;
	
	/** Use authentication mode? */
	protected final boolean authmode;
	
	/**
	 * Initializes the server IMC connection.
	 *
	 * @param __name The name of the server.
	 * @param __ver The version of the server.
	 * @param __auth Is authorization mode used?
	 * @throws IOException On socket initialization error.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public IMCServer(String __name, MidletVersion __ver, boolean __auth)
		throws IOException, NullPointerException
	{
		// Check
		if (__name == null || __ver == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.version = __ver;
		this.authmode = __auth;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public StreamConnection acceptAndOpen()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String getVersion()
	{
		return this.version.toString();
	}
}

