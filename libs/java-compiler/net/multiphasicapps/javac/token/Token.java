// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single token which contains a type and the characters
 * which make up the token.
 *
 * @since 2017/09/04
 */
public final class Token
{
	/** The type of token this is, */
	protected final TokenType type;
	
	/** The token string data. */
	protected final String chars;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the token.
	 *
	 * @param __t The type of token this is.
	 * @param __c The characters which make up the token.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/06
	 */
	public Token(TokenType __t, String __c)
		throws NullPointerException
	{
		// Check
		if (__t == null || __c == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.chars = __c;
	}
	
	/**
	 * Returns the token characters.
	 *
	 * @return The token characters.
	 * @since 2017/09/06
	 */
	public String characters()
	{
		return this.chars;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof Token))
			return false;
		
		Token o = (Token)__o;
		return this.type.equals(o.type) &&
			this.chars.equals(o.chars);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public int hashCode()
	{
		return this.type.hashCode() ^ this.chars.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/04
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s: %s",
				this.type, this.chars)));
		
		return rv;
	}
	
	/**
	 * Returns the type of token this is.
	 *
	 * @return The token type.
	 * @since 2017/09/06
	 */
	public TokenType type()
	{
		return this.type;
	}
}

