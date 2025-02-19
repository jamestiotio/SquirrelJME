/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xe-r@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm.h"
#include "sjme/debug.h"

sjme_errorCode sjme_nvm_tick(sjme_nvm_state* state, sjme_jint maxTics)
{
	if (state == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	sjme_todo("sjme_nvm_tick()");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
