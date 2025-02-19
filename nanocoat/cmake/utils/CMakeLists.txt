# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various utilities

# Just allow a very early 3.0 release so all utilities work regardless
cmake_minimum_required(VERSION 3.0)

# Declare Project
project(CMakeUtils)

# Always output binaries into the binary directory because we do not want
# to bother with how things should be found...
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY
	"${CMAKE_BINARY_DIR}")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_DEBUG
	"${CMAKE_BINARY_DIR}")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_RELEASE
	"${CMAKE_BINARY_DIR}")

# Dump executable suffix, since target could be different from host
file(WRITE "${CMAKE_BINARY_DIR}/suffix"
	"${CMAKE_EXECUTABLE_SUFFIX_C}")

# Hex decoding to binary
add_executable(decode
	decode.c)

# Simple test binary
add_executable(simple
	simple.c)

# Turn binary file into source code
add_executable(sourceize
	sourceize.c)
