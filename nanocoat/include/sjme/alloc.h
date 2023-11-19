/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * SquirrelJME allocator.
 * 
 * @since 2023/11/18
 */

#ifndef SQUIRRELJME_ALLOC_H
#define SQUIRRELJME_ALLOC_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_ALLOC_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Structure which stores the pooled memory allocator.
 * 
 * @since 2023/11/18
 */
typedef struct sjme_alloc_pool sjme_alloc_pool;

/**
 * Allocation link chain, each is a chain between each allocation.
 * 
 * @since 2023/11/18
 */
typedef struct sjme_alloc_link sjme_alloc_link;

/**
 * Allocation pool and link space types.
 * 
 * @since 2023/11/18
 */
typedef enum sjme_alloc_poolSpace
{
	/** Free space. */
	SJME_ALLOC_POOL_SPACE_FREE,
	
	/** Used space. */
	SJME_ALLOC_POOL_SPACE_USED,
	
	/** the number of possible spaces. */
	SJME_NUM_ALLOC_POOL_SPACE
} sjme_alloc_poolSpace;

struct sjme_alloc_link
{
	/** The pool this is in. */
	sjme_alloc_pool* pool;
	
	/** Previous link. */
	sjme_alloc_link* prev;
	
	/** Next link. */
	sjme_alloc_link* next;
	
	/** The space this is in. */
	sjme_alloc_poolSpace space;
	
	/** The size of the data area of this block. */
	jint blockSize;
	
	/** The memory block. */
	jubyte block[sjme_flexibleArrayCount];
};

/**
 * Calculates the size of the pool link.
 * 
 * @param size The size to use for the pool link.
 * @return The size of the given pool link.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_ALLOC_LINK(size) \
	(sizeof(sjme_alloc_link) + (((size_t)(size)) * sizeof(jubyte)))

/**
 * Structure which stores the pooled memory allocator.
 * 
 * @since 2023/11/18
 */
struct sjme_alloc_pool
{
	/** The size of the allocation pool. */
	jint size;
	
	/** Free and used space information. */
	struct
	{
		/** Space that can be used. */
		jint usable;
		
		/** Space that is actually reserved due to overhead. */
		jint reserved;
	} space[SJME_NUM_ALLOC_POOL_SPACE];
	
	/** The front chain link. */
	sjme_alloc_link* frontLink;
	
	/** The back chain link. */
	sjme_alloc_link* backLink;
	
	/** The memory block. */
	jubyte block[sjme_flexibleArrayCount];
};

/**
 * Calculates the size of the allocation pool.
 * 
 * @param size The size to use for the allocation pool.
 * @return The size of the given allocation pool.
 * @since 2023/11/16
 */
#define SJME_SIZEOF_ALLOC_POOL(size) \
	(sizeof(sjme_alloc_pool) + (((size_t)(size)) * sizeof(jubyte)))

/**
 * Allocates a pool that is based on @c malloc() .
 * 
 * @param outPool The resultant pool. 
 * @param size The requested pool size.
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/11/18
 */
jboolean sjme_alloc_poolMalloc(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInPositive jint size);

/**
 * Allocates a pool that is based on a static region of memory.
 * 
 * @param outPool The resultant pool.
 * @param baseAddr The base address of the block. 
 * @param size The size of the block.
 * @return Returns @c JNI_TRUE on success.
 * @since 2023/11/18
 */
jboolean sjme_alloc_poolStatic(
	sjme_attrOutNotNull sjme_alloc_pool** outPool,
	sjme_attrInNotNull void* baseAddr,
	sjme_attrInPositive jint size);

jboolean sjme_alloc(
	sjme_attrInNotNull sjme_alloc_pool* pool,
	sjme_attrInPositive jint size,
	sjme_attrOutNotNull void** outAddr); 

jboolean sjme_allocFree(
	sjme_attrInNotNull void* addr);

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_ALLOC_H
}
		#undef SJME_CXX_SQUIRRELJME_ALLOC_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_ALLOC_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_ALLOC_H */
