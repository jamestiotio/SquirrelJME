/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Generic tread reading/writing functions.
 * 
 * @since 2023/11/16
 */

#ifndef SQUIRRELJME_TREAD_H
#define SQUIRRELJME_TREAD_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_TREAD_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Reads from a tread.
 * 
 * @param frame The frame the access is in.
 * @param tread The tread to read from.
 * @param treadIndex The index to access.
 * @param outVal The output value.
 * @return Returns @c JNI_TRUE if successful.
 * @since 2023/11/16
 */
typedef jboolean (*sjme_nvm_frameTreadAccessorRead)(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_nvm_frameTread* tread,
	sjme_attrInPositive jint treadIndex,
	sjme_attrOutNotNull void* outVal);

/**
 * Reads from a tread.
 * 
 * @param frame The frame the access is in.
 * @param tread The tread to read from.
 * @param treadIndex The index to access.
 * @param outVal The output value.
 * @return Returns @c JNI_TRUE if successful.
 * @since 2023/11/16
 */
typedef jboolean (*sjme_nvm_frameTreadAccessorWrite)(
	sjme_attrInNotNull sjme_nvm_frame* frame,
	sjme_attrInNotNull sjme_nvm_frameTread* tread,
	sjme_attrInPositive jint treadIndex,
	sjme_attrInNotNull void* inVal);

/**
 * Accessor functions for treads.
 * 
 * @since 2023/11/16
 */
typedef struct sjme_nvm_frameTreadAccessor
{
	/** The size of the type. */
	size_t size;
	
	/** The name of the type. */
	const char* name;
	
	/** Read function. */
	sjme_nvm_frameTreadAccessorRead read;
	
	/** Write function. */
	sjme_nvm_frameTreadAccessorWrite write;
} sjme_nvm_frameTreadAccessor;

/**
 * Returns the accessor for the frame tread.
 * 
 * @param typeId The type ID to get for.
 * @return The accessor for the given tread.
 * @since 2023/11/16
 */
const sjme_nvm_frameTreadAccessor* sjme_nvm_frameTreadAccessorByType(
	sjme_attrInRange(SJME_JAVA_TYPE_ID_INTEGER, SJME_NUM_JAVA_TYPE_IDS = 1)
		sjme_javaTypeId typeId);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_TREAD_H
}
		#undef SJME_CXX_SQUIRRELJME_TREAD_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_TREAD_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_TREAD_H */
