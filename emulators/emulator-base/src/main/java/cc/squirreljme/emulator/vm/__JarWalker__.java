// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

/**
 * Walks the file tree for wildcard JARs.
 *
 * @since 2020/06/22
 */
public final class __JarWalker__
	extends SimpleFileVisitor<Path>
{
	/** Where to place found files. */
	private final Collection<String> _files;
	
	/**
	 * Initializes the Jar walker.
	 * 
	 * @param __files The files to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/22
	 */
	public __JarWalker__(Collection<String> __files)
		throws NullPointerException
	{
		if (__files == null)
			throw new NullPointerException("NARG");
		
		this._files = __files;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public FileVisitResult visitFile(Path __path, BasicFileAttributes __attrib)
		throws IOException
	{
		// If this is a JAR, we will grab it
		String fn = __path.getFileName().toString();
		if (fn.endsWith(".jar") || fn.endsWith(".JAR") ||
			fn.endsWith(".jad") || fn.endsWith(".JAD") ||
			fn.endsWith(".jam") || fn.endsWith(".JAM") ||
			fn.endsWith(".kjx") || fn.endsWith(".KJX"))
			this._files.add(__path.toString());
		
		// The default way is to just handle it
		return super.visitFile(__path, __attrib);
	}
}
