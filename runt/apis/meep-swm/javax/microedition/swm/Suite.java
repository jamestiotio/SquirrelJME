// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.collections.EmptyIterator;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCall;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramControlKey;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramType;
import net.multiphasicapps.squirreljme.runtime.launcher.EntryPoint;
import net.multiphasicapps.squirreljme.runtime.launcher.EntryPoints;
import net.multiphasicapps.squirreljme.runtime.midlet.id.SuiteInfo;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;
import net.multiphasicapps.tool.manifest.JavaManifestKey;

/**
 * This represents an application suite.
 *
 * Created suites by default have their {@link SuiteStateFlag#AVAILABLE} and
 * {@link SuiteStateFlag#ENABLED} flags set.
 *
 * @since 2016/06/24
 */
public class Suite
{
	/** This is a suite that represents the system. */
	public static Suite SYSTEM_SUITE =
		new Suite(Suite.class);
	
	/** The state lock. */
	private final Object _lock =
		new Object();
	
	/** The suite program. */
	private final SystemProgram _program;
	
	/** Cached manifest information. */
	private volatile Reference<JavaManifest> _manifest;
	
	/** Cached suite information. */
	private volatile Reference<SuiteInfo> _suiteinfo;
	
	/**
	 * Initializes the system suite.
	 *
	 * @param __cl Ignored parameter.
	 * @since 2017/12/08
	 */
	private Suite(Class<Suite> __cl)
	{
		this._program = null;
	}
	
	/**
	 * Initializes the suite.
	 *
	 * @param __p The program for this suite.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	Suite(SystemProgram __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this._program = __p;
	}
	
	/**
	 * Checks if two suites are equal to each other, they are equal when
	 * the vendor and name of the suite match.
	 *
	 * @param __o The object to compare against.
	 * @return {@code true} if this suite is equal to the other object.
	 * @since 2016/06/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Not another suite?
		if (!(__o instanceof Suite))
			return false;
		
		// Check
		Suite o = (Suite)__o;
		return Objects.equals(getName(), o.getName()) &&
			Objects.equals(getVendor(), o.getVendor());
	}
	
	/**
	 * Returns the list of attributes which are defined in the JAD or the
	 * manifest.
	 *
	 * @return The iterator of attributes. The system suite always returns
	 * an empty iteration.
	 * @since 2016/06/24
	 */
	public Iterator<String> getAttributes()
	{
		SystemProgram program = this._program;
		if (program == null)
			return EmptyIterator.<String>empty();
		
		Set<String> rv = new LinkedHashSet<>();
		for (JavaManifestKey k : this.__manifest().getMainAttributes().
			keySet())
			rv.add(k.toString());
		return rv.iterator();
	}
	
	/**
	 * Returns the value of an attribute.
	 *
	 * @param __a The name of the attribute to obtain a value for.
	 * @return The value of the given attribute or {@code null} if it was not
	 * found. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getAttributeValue(String __a)
	{
		SystemProgram program = this._program;
		if (program == null)
			return null;
		
		return this.__manifest().getMainAttributes().get(
			new JavaManifestKey(__a));
	}
	
	/**
	 * Returns the library suites which this suite depends on.
	 *
	 * @return The iterator over the suite dependencies. The system suite
	 * always returns an empty iterator.
	 * @since 2016/06/24
	 */
	public Iterator<Suite> getDependencies()
	{
		SystemProgram program = this._program;
		if (program == null)
			return EmptyIterator.<Suite>empty();
		
		List<Suite> rv = new ArrayList<>();
		
		// Use the suite manager to wrap suites so that a large number of
		// suites pointing to the same program are not created
		__SystemSuiteManager__ ssm =
			(__SystemSuiteManager__)ManagerFactory.getSuiteManager();
		
		// Dependencies are internally provided in the control interface
		for (int i = 1; i >= 0; i++)
		{
			// Read all dependency values
			String val = program.controlGet(
				SystemProgramControlKey.DEPENDENCY_PREFIX + i);
			if (val == null)
				break;
			
			// Try to find the system program for each index
			SystemProgram sp = SystemCall.programByIndex(
				Integer.parseInt(val));
			if (sp != null)
				rv.add(ssm.__ofProgram(sp));
		}
		
		return rv.iterator();
	}
	
	/**
	 * This returns the URL which a previously installed suite was downloaded
	 * from.
	 *
	 * @return The URL where the suite was downloaded from. If this is the
	 * system suite, the suite was pre-installed, or was installed using the
	 * raw byte array then this will return null.
	 * @since 2016/06/24
	 */
	public String getDownloadUrl()
	{
		SystemProgram program = this._program;
		if (program == null)
			return null;
		
		return program.controlGet(SystemProgramControlKey.DOWNLOAD_URL);
	}
	
	/**
	 * This returns the names of all classes which are specfied in the MIDlet
	 * attributes in the manifest. The sequence of classes should match the
	 * MIDlet order number.
	 *
	 * @return The names of classes that are specified in the MIDlet attributes
	 * in the manifest. The system suite always returns an empty iterator.
	 * @since 2016/06/24
	 */
	public Iterator<String> getMIDlets()
	{
		SystemProgram program = this._program;
		if (program == null)
			return EmptyIterator.<String>empty();
		
		EntryPoints entries = new EntryPoints(
			this.__manifest().getMainAttributes());
		Set<String> rv = new LinkedHashSet<>();
		
		for (EntryPoint e : entries)
			rv.add(e.entryPoint());
		
		return rv.iterator();
	}
	
	/**
	 * Returns the name of this suite.
	 *
	 * @return The suite name. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getName()
	{
		SystemProgram program = this._program;
		if (program == null)
			return null;
		
		return this.__suiteInfo().name().toString();
	}
	
	/**
	 * Returns the type of suite that this is.
	 *
	 * @return The type of suite this is. The system suite always returns
	 * {@link SuiteType#SYSTEM}.
	 * @since 2016/06/24
	 */
	public SuiteType getSuiteType()
	{
		SystemProgram program = this._program;
		if (program == null)
			return SuiteType.SYSTEM;
		
		switch (program.type())
		{
			case SystemProgramType.APPLICATION:
				return SuiteType.APPLICATION;
				
			case SystemProgramType.LIBRARY:
				return SuiteType.LIBRARY;
				
			case SystemProgramType.SYSTEM:
				return SuiteType.SYSTEM;
			
			default:
				return SuiteType.INVALID;
		}
	}
	
	/**
	 * Returns the vendor of this suite.
	 *
	 * @return The vendor of this suite. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getVendor()
	{
		SystemProgram program = this._program;
		if (program == null)
			return null;
		
		return this.__suiteInfo().vendor().toString();
	}
	
	/**
	 * Returns the version of this suite.
	 *
	 * @return The version of this suite. The system suite always returns null.
	 * @since 2016/06/24
	 */
	public String getVersion()
	{
		SystemProgram program = this._program;
		if (program == null)
			return null;
		
		return this.__suiteInfo().version().toString();
	}
	
	/**
	 * Calculates the hash code of the given suite, the values are derived from
	 * the name and the vendor.
	 *
	 * @return The hash code.
	 * @since 2016/06/24
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getVendor()) ^
			Objects.hashCode(getName());
	}
	
	/**
	 * Returns {@code true} if this suite is installed.
	 *
	 * @return {@code true} if this suite is installed. The system suite always
	 * returns {@code true}.
	 * @since 2016/06/24
	 */
	public boolean isInstalled()
	{
		SystemProgram program = this._program;
		if (program == null)
			return true;
		
		return Boolean.valueOf(
			program.controlGet(SystemProgramControlKey.IS_INSTALLED));
	}
	
	/**
	 * Checks if the suite has the specified flag set.
	 *
	 * @param __f The flag to check.
	 * @return {@code true} if the flag is set.
	 * @since 2016/06/24
	 */
	public boolean isSuiteState(SuiteStateFlag __f)
	{
		// Null is never true
		if (__f == null)
			return false;
		
		// The system suite always has a fixed set of flags
		SystemProgram program = this._program;
		if (program == null)
			switch (__f)
			{
				case AVAILABLE:
				case ENABLED:
				case PREINSTALLED:
				case REMOVE_DENIED:
				case SYSTEM:
				case UPDATE_DENIED:
					return true;
				default:
					return false;
			}
		
		return Boolean.valueOf(
			program.controlGet(SystemProgramControlKey.STATE_FLAG_PREFIX +
				__f.name()));
	}
	
	/**
	 * Sets whether this suite is trusted or not.
	 *
	 * @return {@code true} if this suite is trusted. The system suite always
	 * returns {@code true}.
	 * @since 2016/06/24
	 */
	public boolean isTrusted()
	{
		SystemProgram program = this._program;
		if (program == null)
			return true;
		
		return Boolean.valueOf(
			program.controlGet(SystemProgramControlKey.IS_TRUSTED));
	}
	
	/**
	 * Sets the given flag to the suite.
	 *
	 * @param __f The flag to set.
	 * @param __v If the flag should be set or cleared.
	 * @throws IllegalArgumentException If an attempt was made to set the
	 * {@link SuiteStateFlag#SYSTEM} or {@link SuiteStateFlag#PREINSTALLED}
	 * flags.
	 * @throws IllegalStateException If the suite was removed or this is the
	 * system suite.
	 * @throws SecurityException If the {@code {@link SWMPermission}("client",
	 * "manageSuite")} or {@code {@link SWMPermission}("crossClient",
	 * "manageSuite")} permission is not permitted.
	 * @since 2016/06/24
	 */
	public void setSuiteStateFlag(SuiteStateFlag __f, boolean __v)
		throws IllegalArgumentException, IllegalStateException,
			SecurityException
	{
		// Ignore
		if (__f == null)
			return;
		
		throw new todo.TODO();
		/*
		// {@squirreljme.error DG02 The current suite has been removed.}
		if (!isInstalled())
			throw new IllegalStateException("DG02");
		
		// {@squirreljme.error DG02 The given state flag cannot be set.
		// (The state flag)}
		if (__f == SuiteStateFlag.SYSTEM || __f == SuiteStateFlag.PREINSTALLED)
			throw new IllegalArgumentException(String.format("DG02 %s", __f));
		
		// Lock
		synchronized (this._lock)
		{
			// {@squirreljme.error DG03 Cannot change flags of the system
			// suite.}
			if (0 != (this._state & (1 << SuiteStateFlag.SYSTEM.ordinal())))
				throw new IllegalStateException("DG03");
			
			// Get the required bit
			int bit = (1 << __f.SYSTEM.ordinal());
			
			// Set or clear?
			if (__v)
				this._state |= bit;
			else
				this._state &= bit;
		}
		*/
	}
	
	/**
	 * Returns the suite manifest.
	 *
	 * @return The suite manifest.
	 * @since 2017/12/31
	 */
	final JavaManifest __manifest()
	{
		Reference<JavaManifest> ref = this._manifest;
		JavaManifest rv;
		
		if (ref == null || null == (rv = ref.get()))
			try (InputStream in =
				this._program.loadResource("META-INF/MANIFEST.MF"))
			{
				// {@squirreljme.error DG09 Suite has no manifest file.}
				if (in == null)
					throw new RuntimeException("DG09");
				
				this._manifest = new WeakReference<>(
					(rv = new JavaManifest(in)));
			}
			
			// {@squirreljme.error DG08 Could not read the suite manifest.}
			catch (IOException e)
			{
				throw new RuntimeException("DG08");
			}
		
		return rv;
	}
	
	/**
	 * Returns the program the suite uses.
	 *
	 * @return The used program.
	 * @since 2017/12/08
	 */
	final SystemProgram __program()
	{
		return this._program;
	}
	
	/**
	 * Returns the information about this suite.
	 *
	 * @return The suite information.
	 * @since 2017/12/31
	 */
	final SuiteInfo __suiteInfo()
	{
		Reference<SuiteInfo> ref = this._suiteinfo;
		SuiteInfo rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._suiteinfo = new WeakReference<>(
				(rv = new SuiteInfo(this.__manifest())));
		
		return rv;
	}
}

