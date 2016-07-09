// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.streamwriter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class is used to write to ZIP files in an unknown and stream based
 * manner where the size of the contents is completely unknown.
 *
 * When the stream is closed, the central directory of the ZIP file will be
 * written to the end of the file.
 *
 * @since 2016/07/09
 */
public class ZipStreamWriter
	implements Closeable
{
	/**
	 * This initializes the stream for writing ZIP file data.
	 *
	 * @param __os The output stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/09
	 */
	public ZipStreamWriter(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
}

