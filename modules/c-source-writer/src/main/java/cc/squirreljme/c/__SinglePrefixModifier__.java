// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Single prefix modifier.
 *
 * @since 2023/06/05
 */
abstract class __SinglePrefixModifier__
	implements CModifier
{
	/** The wrapped modifier. */
	protected final CModifier wrapped;
	
	/** The keyword for the modifier. */
	private final String _keyword;
	
	/** Tokens that represent the modifiers. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes the prefixed modifier wrapper.
	 * 
	 * @param __keyword The key word for the modifier.
	 * @param __modifier The modifier to wrap.
	 * @throws NullPointerException If no keyword was specified.
	 * @since 2023/06/05
	 */
	__SinglePrefixModifier__(String __keyword, CModifier __modifier)
		throws NullPointerException
	{
		if (__keyword == null)
			throw new NullPointerException("NARG");
		
		this._keyword = __keyword;
		this.wrapped = __modifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof __SinglePrefixModifier__))
			return false;
		if (this.getClass() != __o.getClass())
			return false;
		
		__SinglePrefixModifier__ o = (__SinglePrefixModifier__)__o;
		return this._keyword.equals(o._keyword) &&
			Objects.equals(this.wrapped, o.wrapped);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.wrapped);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public List<String> modifierTokens()
	{
		Reference<List<String>> ref = this._tokens;
		List<String> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			List<String> build = new ArrayList<>();
			build.add(this._keyword);
			build.addAll(this.wrapped.modifierTokens());
			
			rv = UnmodifiableList.of(build);
			this._tokens = new WeakReference<>(rv);
		}
		
		return rv;
	}
}
