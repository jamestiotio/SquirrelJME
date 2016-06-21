// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.pvmjvm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.multiphasicapps.classwriter.OutputClass;
import net.multiphasicapps.classwriter.OutputVersion;
import net.multiphasicapps.descriptors.ClassLoaderNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.classpath.ClassPath;

/**
 * This is used to wrap the class loader which paravirtualizes all classes in
 * a process by prefixing and creating virtual classes so that all class
 * access done by a process is handled by this loader.
 *
 * @since 2016/06/19
 */
public class PVMClassLoader
	extends ClassLoader
{
	/** The process which owns this class loader. */
	protected final PVMProcess process;
	
	/** The prefix for mangled field types. */
	protected final String mangledprefix;
	
	/** The class path of the process. */
	protected final ClassPath classpath;
	
	/**
	 * Initializes the class loader.
	 *
	 * @param __proc The owning process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	PVMClassLoader(PVMProcess __proc)
		throws NullPointerException
	{
		// Check
		if (__proc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.process = __proc;
		this.mangledprefix = "__squirreljme#" + __proc.pid();
		this.classpath = __proc.classPath();
	}
	
	/**
	 * Demangles the given mangled field symbol so that the actual class (to
	 * the process itself) is obtained.
	 *
	 * @param __f The field symbol to demangle.
	 * @return The original unmangle field.
	 * @throws IllegalArgumentException If the symbol is not mangled.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	public FieldSymbol fieldDemangle(FieldSymbol __f)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Primitives go through unchanged
		if (__f.primitiveType() != null)
			return __f;
		
		// Get the class name of the given symbol
		ClassNameSymbol cns = __f.asClassName();
		String form = cns.toString();
		
		// {@squirreljme.error CL01 The input field symbol is not mangled for
		// this process. (The not-mangled field)}
		String mp = this.mangledprefix;
		if (!form.startsWith(mp) || form.length() < mp.length() + 1 ||
			form.charAt(mp.length()) != '/')
			throw new IllegalArgumentException(String.format("CL01 %s", __f));
		
		// Remove the prefix
		form = form.substring(mp.length() + 1);
		
		// Target class name
		StringBuilder sb = new StringBuilder();
		int n = form.length();
		for (int i = 0; i < n; i++)
		{
			char c = form.charAt(i);
			
			// Demangle?
			if (c == '?')
				c = __charMangle(false, form.charAt(++i));
			
			sb.append(c);
		}
		
		// Return it
		return FieldSymbol.of(sb.toString());
	}
	
	/**
	 * Mangles the given field symbol to the field symbol that should be used
	 * in this process.
	 *
	 * @param __f The field symbol to mangle.
	 * @return The class which represents the mangled field.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	public FieldSymbol fieldMangle(FieldSymbol __f)
		throws NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Primitives go through unchanged
		if (__f.primitiveType() != null)
			return __f;
		
		// The target string
		StringBuilder sb = new StringBuilder(this.mangledprefix);
		sb.append('/');
		
		// Transform field characters
		String s = __f.toString();
		int n = s.length();
		for (int i = 0; i < n; i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
					// Must be escaped
				case '.':
				case ';':
				case '[':
				case '/':
				case '?':
					sb.append('?');
					sb.append(__charMangle(true, c));
					break;
				
					// Normal
				default:
					sb.append(c);
					break;
			}
		}
		
		// As a class
		return ClassNameSymbol.of(sb.toString()).asField();
	}
	
	/**
	 * Locates the given class by the specified name, the name should be in
	 * the mangled form.
	 *
	 * @param __name The name of the class to locate.
	 * @return The class by the given name.
	 * @throws ClassNotFoundException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	protected Class<?> findClass(String __name)
		throws ClassNotFoundException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Demangle
		FieldSymbol mangled = ClassLoaderNameSymbol.of(__name).asClassName().
			asField();
		FieldSymbol demang = fieldDemangle(mangled);
		System.err.printf("DEBUG -- Load %s -> %s%n", __name, demang);
		
		// The class path
		ClassPath cp = this.classpath;
		
		// Lock
		synchronized (getClassLoadingLock(__name))
		{
			// If an array a virtual class must be created that represents and
			// stores the array data.
			if (demang.isArray())
				throw new Error("TODO");
			
			// Get the represented class name
			ClassNameSymbol cn = demang.asClassName();
			
			// Could fail to load or not find at all
			CIClass cic;
			try
			{
				cic = cp.locateClass(cn);
			}
			
			// {@squirreljme.error CL05 Could not find a class in the class
			// path with the specified name, or the class is not correctly
			// formed and failed to be verified. (The name of the requested
			// class)}
			catch (CIException e)
			{
				throw new ClassNotFoundException(String.format("CL05 %s", cn),
					e);
			}
			
			// Setup the output class writer
			OutputClass oc = new OutputClass();
			
			// Convert the class data
			__virtualizeClass(oc, cic);
			
			// Write it out
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
			{
				// Write to the output
				oc.write(baos);
				
				// Define the class
				byte[] buf = baos.toByteArray();
				return defineClass(__name, buf, 0, buf.length);
			}
			
			// {@squirreljme.error CL06 Failed to generate a translated class
			// file. (The class to generate)}
			catch (IOException|RuntimeException e)
			{
				throw new ClassNotFoundException(String.format("CL06 %s", cn),
					e);
			}
		}
	}
	
	/**
	 * Locates the given virtualized class.
	 *
	 * @param __cl The class to find.
	 * @return The virtual representation of the given class.
	 * @throws ClassNotFoundException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final Class<?> virtualFindClass(ClassNameSymbol __cl)
		throws ClassNotFoundException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Use as field
		return virtualFindClass(__cl.asField());
	}
	
	/**
	 * Locates the given virtualized class which represents the given field
	 * type.
	 *
	 * @param __f The field type to locate.
	 * @return The virtual representation of the given field.
	 * @throws ClassNotFoundException If the class was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final Class<?> virtualFindClass(FieldSymbol __f)
		throws ClassNotFoundException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Mangle it
		FieldSymbol mangled = fieldMangle(__f);
		
		// {@squirreljme.error CL04 Cannot load a class which represents a
		// primitive type. (The mangled class)}
		if (mangled.isPrimitive())
			throw new ClassNotFoundException(String.format("CL04 %s",
				mangled));
		
		// Get the class loading name form
		String actual = mangled.asClassName().asClassLoaderName().toString();
		
		// Lock
		synchronized (getClassLoadingLock(actual))
		{
			return loadClass(actual, true);
		}
	}
	
	/**
	 * Virtualizes the given class.
	 *
	 * @param __oc The output class.
	 * @param __ic The input class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	private final void __virtualizeClass(OutputClass __oc, CIClass __ic)
		throws NullPointerException
	{
		// Check
		if (__oc == null || __ic == null)
			throw new NullPointerException("NARG");
		
		// Set version
		__oc.setVersion(OutputVersion.CLDC_8);
		
		// Set class name
		__oc.setThisName(fieldMangle(__ic.thisName().asField()).asClassName().
			asBinaryName());
		
		throw new Error("TODO");
	}
	
	/**
	 * Mangles the given character.
	 *
	 * @param __mangle If {@code true} then the character is to be mangled,
	 * otherwise it is unmangled.
	 * @param __c The character to mangle or unmangle.
	 * @return The mangled or unmangled variant of the character, or the
	 * original character if the mangle is not valid.
	 * @since 2016/06/19
	 */
	private static char __charMangle(boolean __mangle, char __c)
	{
		// Mangling
		if (__mangle)
			switch (__c)
			{
				case '.': return ':';
				case ';': return ',';
				case '[': return '(';
				case '/': return '|';
				case '?': return '!';
				
					// Unknown
				default:
					return __c;
			}
		
		// Unmangling
		else
			switch (__c)
			{
				case ':': return '.';
				case ',': return ';';
				case '(': return '[';
				case '|': return '/';
				case '!': return '?';
				
					// Unknown
				default:
					return __c;
			}
	}
}

