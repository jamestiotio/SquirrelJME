/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

/**
 * Unit test support.
 * 
 * @since 2023/11/11
 */

#ifndef SQUIRRELJME_UNIT_H
#define SQUIRRELJME_UNIT_H

#include "sjme/nvm.h"
#include "sjme/debug.h"
#include "test.h"

/* Anti-C++. */
#ifdef __cplusplus
	#ifndef SJME_CXX_IS_EXTERNED
		#define SJME_CXX_IS_EXTERNED
		#define SJME_CXX_SQUIRRELJME_UNIT_H
extern "C" {
	#endif /* #ifdef SJME_CXX_IS_EXTERNED */
#endif     /* #ifdef __cplusplus */

/*--------------------------------------------------------------------------*/

/**
 * The operator for test comparisons.
 * 
 * @since 2023/11/20
 */
typedef enum sjme_unitOperator
{
	/** Equality, @c ==. */
	SJME_UNIT_OPERATOR_EQUAL,
	
	/** Inequality, @c !=. */
	SJME_UNIT_OPERATOR_NOT_EQUAL,
	
	/** Less than, @c <. */
	SJME_UNIT_OPERATOR_LESS_THAN,
	
	/** Less than or equal, @c <=. */
	SJME_UNIT_OPERATOR_LESS_EQUAL,
	
	/** Greater than, @c >. */
	SJME_UNIT_OPERATOR_GREATER_THAN,
	
	/** Greater than or equal, @c >=. */
	SJME_UNIT_OPERATOR_GREATER_EQUAL,
	
	/** The number of unit operators. */
	SJME_NUM_UNIT_OPERATORS
} sjme_unitOperator;

/**
 * Checks equality between the two integer values.
 * 
 * @param inverted Is the check inverted?
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/11
 */
sjme_testResult sjme_unitOperatorIR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unitOperator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInValue sjme_jint a,
	sjme_attrInValue sjme_jint b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(7, 8);

/**
 * Checks equality between the two integer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/11
 */
#define sjme_unitEqualI(...) \
	sjme_unitOperatorIR(SJME_DEBUG_FILE_LINE_FUNC, \
		SJME_UNIT_OPERATOR_EQUAL, __VA_ARGS__)

/**
 * Checks less than or equal between the two integer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/11
 */
#define sjme_unitLessEqualI(...) \
	sjme_unitOperatorIR(SJME_DEBUG_FILE_LINE_FUNC, \
		SJME_UNIT_OPERATOR_LESS_EQUAL, __VA_ARGS__)

/**
 * Checks inequality between the two integer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/20
 */
#define sjme_unitNotEqualI(...) \
	sjme_unitOperatorIR(SJME_DEBUG_FILE_LINE_FUNC, \
		SJME_UNIT_OPERATOR_NOT_EQUAL, __VA_ARGS__)

/**
 * Checks equality between the two object values.
 * 
 * @param inverted Is the check inverted?
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/17
 */
sjme_testResult sjme_unitOperatorLR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unitOperator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_jobject a,
	sjme_attrInNullable sjme_jobject b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(7, 8);

/**
 * Checks equality between the two object values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/17
 */
#define sjme_unitEqualL(...) \
	sjme_unitOperatorLR(SJME_DEBUG_FILE_LINE_FUNC, \
		SJME_UNIT_OPERATOR_EQUAL, __VA_ARGS__)

/**
 * Checks inequality between the two object values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/20
 */
#define sjme_unitNotEqualL(...) \
	sjme_unitOperatorLR(SJME_DEBUG_FILE_LINE_FUNC, \
		SJME_UNIT_OPERATOR_NOT_EQUAL, __VA_ARGS__)

/**
 * Checks equality between the two pointer values.
 * 
 * @param inverted Is the check inverted?
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/19
 */
sjme_testResult sjme_unitOperatorPR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInValue sjme_unitOperator operator,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable void* a,
	sjme_attrInNullable void* b,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(7, 8);

/**
 * Checks equality between the two pointer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/19
 */
#define sjme_unitEqualP(...) \
	sjme_unitOperatorPR(SJME_DEBUG_FILE_LINE_FUNC, \
		SJME_UNIT_OPERATOR_EQUAL, __VA_ARGS__)

/**
 * Checks inequality between the two pointer values.
 * 
 * @param test The test data.
 * @param a The first value.
 * @param b The second value.
 * @param format The message.
 * @param ... The message parameters.
 * @return The assertion state.
 * @since 2023/11/20
 */
#define sjme_unitNotEqualP(...) \
	sjme_unitOperatorPR(SJME_DEBUG_FILE_LINE_FUNC, \
		SJME_UNIT_OPERATOR_NOT_EQUAL, __VA_ARGS__)

/**
 * Unit test just fails.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is fail.
 * @since 2023/11/11
 */
sjme_testResult sjme_unitFailR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(4, 5);

/**
 * Unit test just fails.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is fail.
 * @since 2023/11/11
 */
#define sjme_unitFail(...) sjme_unitFailR(SJME_DEBUG_FILE_LINE_FUNC, \
	__VA_ARGS__)

/**
 * Skips the test and stops execution.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is skip.
 * @since 2023/11/19
 */
sjme_testResult sjme_unitSkipR(SJME_DEBUG_DECL_FILE_LINE_FUNC,
	sjme_attrInNotNull sjme_test* test,
	sjme_attrInNullable sjme_attrFormatArg const char* format, ...)
	sjme_attrFormatOuter(4, 5);

/**
 * Skips the test and stops execution.
 * 
 * @param test The test data.
 * @param format The message. 
 * @param ... The formatted string arguments.
 * @return The test result, which is skip.
 * @since 2023/11/19
 */
#define sjme_unitSkip(...) sjme_unitSkipR(SJME_DEBUG_FILE_LINE_FUNC, \
	__VA_ARGS__)

/*--------------------------------------------------------------------------*/

/* Anti-C++. */
#ifdef __cplusplus
	#ifdef SJME_CXX_SQUIRRELJME_UNIT_H
}
		#undef SJME_CXX_SQUIRRELJME_UNIT_H
		#undef SJME_CXX_IS_EXTERNED
	#endif /* #ifdef SJME_CXX_SQUIRRELJME_UNIT_H */
#endif     /* #ifdef __cplusplus */

#endif /* SQUIRRELJME_UNIT_H */
