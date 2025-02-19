// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents any element which has been annotated.
 *
 * @since 2018/03/06
 */
public interface Annotated
{
	/**
	 * Returns all of the annotations which have been specified for this.
	 *
	 * @return The annotated elements.
	 * @since 2018/03/06
	 */
	AnnotationTable annotationTable();
}

