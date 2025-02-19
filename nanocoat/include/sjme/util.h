/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Utilities.
 * 
 * @since 2023/07/26
 */

#ifndef SQUIRRELJME_UTIL_H
#define SQUIRRELJME_UTIL_H

#include "sjme/nvm.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_UTIL_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * Function for returning the number of entries within a tree.
 * 
 * @param in The tree to get the size of.
 * @return The number of items in the tree.
 * @since 2023/07/26
 */
typedef sjme_jint (*sjme_treeFindCount)(void* tree);

/**
 * Function for returning the hash of the search item.
 * 
 * @param what What to get the hash of.
 * @return The hash of the given search item.
 * @since 2023/07/26
 */
typedef sjme_jint (*sjme_treeFindHash)(void* what);

/**
 * Compares an entry in the tree at the given index with the given hash and
 * item.
 * 
 * @param tree The tree to search in.
 * @param what What to being searched for in the tree.
 * @param hash The hash generated from @c sjme_treeFindHash .
 * @param withIndex Compare @c hash and @c what against the given tree.
 * @return A negative value if lower, zero if equal, or a positive value if
 * greater.
 * @since 2023/07/26
 */
typedef sjme_jint (*sjme_treeFindCompare)(void* tree, void* what, sjme_jint hash,
	sjme_jint withIndex);

/**
 * Random number state.
 * 
 * @since 2023/12/02
 */
typedef struct sjme_random
{
	/** The current seed value. */
	sjme_jlong seed;
} sjme_random;

/**
 * Tree finding functions, used with @c sjme_treeFind to determine how to
 * search through a given tree.
 * 
 * @since 2023/07/26
 */
typedef struct sjme_treeFindFunc
{
	/** Count function. */
	sjme_treeFindCount count;
	
	/** Hash function. */
	sjme_treeFindHash hash;
	
	/** Compare function. */
	sjme_treeFindCompare compare;
} sjme_treeFindFunc;

/**
 * Decodes the given UTF-8 character.
 * 
 * @param at The character sequence to decode.
 * @param stringP Adjustable pointer to the string, when the character is
 * decoded then this will increment accordingly.
 * @return The decoded character.
 * @since 2023/07/27
 */
sjme_jchar sjme_decodeUtfChar(const char* at, const char** stringP);

/**
 * Initializes the random number generator.
 * 
 * @param outRandom The random state to initialize. 
 * @param seedHi The high seed value.
 * @param seedLo The low seed value.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_randomInit(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jint seedHi,
	sjme_attrInValue sjme_jint seedLo);

/**
 * Initializes the random number generator.
 * 
 * @param outRandom The random state to initialize. 
 * @param seed The seed value.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_randomInitL(
	sjme_attrInOutNotNull sjme_random* outRandom,
	sjme_attrInValue sjme_jlong seed);
	
/**
 * Returns the next random value.
 * 
 * @param random The random state.
 * @param outValue The output value.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_randomNextInt(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue);
	
/**
 * Returns the next random value within the given range.
 * 
 * @param random The random state.
 * @param outValue The output value.
 * @param maxValue The maximum exclusive value.
 * @return Returns @c SJME_JNI_TRUE on success.
 * @since 2023/12/02
 */
sjme_errorCode sjme_randomNextIntMax(
	sjme_attrInOutNotNull sjme_random* random,
	sjme_attrOutNotNull sjme_jint* outValue,
	sjme_attrInPositiveNonZero sjme_jint maxValue);

/**
 * Hashes the given string in accordance to @c String::hashCode() .
 * 
 * @param string The string to hash.
 * @return The hashcode of the given string.
 * @since 2023/07/26
 */
sjme_jint sjme_stringHash(const char* string);

/**
 * Returns the length of the string in accordance to @c String::length() .
 * 
 * @param string The string to get the length of.
 * @return The string length.
 * @since 2023/07/29
 */
sjme_jint sjme_stringLength(const char* string);

/**
 * Locates an item within a tree.
 * 
 * @param tree The tree to search in.
 * @param what What is being searched for.
 * @param functions Functions used for the tree search logic.
 * @return The index where the item was found.
 * @since 2023/07/26
 */
sjme_jint sjme_treeFind(void* tree, void* what,
	const sjme_treeFindFunc* functions);
	
/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_UTIL_H
}
		#undef SJME_CXX_SQUIRRELJME_UTIL_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_UTIL_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_UTIL_H */
