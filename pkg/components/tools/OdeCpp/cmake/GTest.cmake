# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#
# Bio-Formats C/C++ libraries (cmake build infrastructure)

enable_testing()
option(test "Enable unit tests (requires gtest)" ON)
set(BUILD_TESTS ${test})
option(extended-tests "Enable extended tests (more comprehensive, longer run time)" ON)
set(EXTENDED_TESTS ${extended-tests})

# Unit tests
find_package(Threads REQUIRED)

option(embedded-gtest "Use embedded gtest rather than an external build" OFF)
if(NOT embedded-gtest)
  find_package(GTest)
endif()

if(NOT GTEST_FOUND)
  message(STATUS "Using embedded GTest")
  # If not using a shared runtime, gtest hardcodes its own (which breaks linking)
  set(gtest_force_shared_crt ON CACHE BOOL "Force gtest to use shared runtime")
  set(BUILD_SHARED_LIBS ON CACHE BOOL "Force gtest to use shared libraries")
  # VS2012 Faux variadic templates workaround.
  if(NOT MSVC_VERSION VERSION_LESS 1700 AND MSVC_VERSION VERSION_LESS 1800)
    add_definitions(-D_VARIADIC_MAX=10)
  endif()

  # Remove warnings triggered by gtest since they aren't our responsibility.
  set(SAVED_CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
  string(REPLACE " " ";" GTEST_FLAG_LIST "${CMAKE_CXX_FLAGS}")
  list(REMOVE_ITEM GTEST_FLAG_LIST
       -Wconversion
       -Wctor-dtor-privacy
       -Wmissing-declarations)
  string(REPLACE ";" " " CMAKE_CXX_FLAGS "${GTEST_FLAG_LIST}")
  unset(GTEST_FLAG_LIST)

  # Build gtest using its own CMake support.
  add_subdirectory("${CMAKE_CURRENT_LIST_DIR}/../ext/gtest-1.7.0")
  set(GTEST_INCLUDE_DIR "${CMAKE_CURRENT_LIST_DIR}/../ext/gtest-1.7.0/include")
  set(GTEST_LIBRARIES gtest)
  set(GTEST_FOUND ON)
  set_property(TARGET gtest gtest_main PROPERTY FOLDER "External/Google Test")

  # Restore saved flags.
  set(CMAKE_CXX_FLAGS "${SAVED_CMAKE_CXX_FLAGS}")
endif()
