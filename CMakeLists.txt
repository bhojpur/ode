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
# Bhojpur ODE C/C++ libraries (cmake build infrastructure)

cmake_minimum_required(VERSION 3.2.0)

project(ode)

set_property(GLOBAL PROPERTY USE_FOLDERS ON)

set(ODE_CPP_DIR pkg/components/tools/OdeCpp)
set(ODE_CPP_SOURCE_DIR ${PROJECT_SOURCE_DIR}/${ODE_CPP_DIR})
set(ODE_CPP_BINARY_DIR ${PROJECT_BINARY_DIR}/${ODE_CPP_DIR})

include(${ODE_CPP_DIR}/cmake/Version.cmake)

if("${PROJECT_SOURCE_DIR}" STREQUAL "${PROJECT_BINARY_DIR}")
  message(FATAL_ERROR "In-tree builds are not supported; please run cmake from a separate build directory.")
endif("${PROJECT_SOURCE_DIR}" STREQUAL "${PROJECT_BINARY_DIR}")

enable_language(CXX)

list(APPEND CMAKE_MODULE_PATH
     "${ODE_CPP_SOURCE_DIR}/cmake")

include(CheckIncludeFileCXX)
include(CheckCXXCompilerFlag)
include(CheckCXXSourceCompiles)

include(GNUInstallDirs)
include(CompilerChecks)
include(GTest)

# For compatibility with scons and java build
if($ENV{SLICEPATH})
  set(ICE_SLICE_DIR "$ENV{SLICEPATH}")
endif($ENV{SLICEPATH})
# Find Ice programs, includes, slice definitions and libraries
find_package(Ice 3.6.0 REQUIRED COMPONENTS Ice IceUtil Glacier2)

find_package(Threads REQUIRED)

# Set Ice environment based upon the FindIce results (for build.py).
get_filename_component(ICE_BINARY_DIR "${Ice_SLICE2CPP_EXECUTABLE}" PATH)
file(TO_NATIVE_PATH "${ICE_BINARY_DIR}" NATIVE_ICE_BINARY_DIR)
if(WIN32)
  set(ENV{PATH} "${NATIVE_ICE_BINARY_DIR};$ENV{PATH}")
else(WIN32)
  set(ENV{PATH} "${NATIVE_ICE_BINARY_DIR}:$ENV{PATH}")
endif(WIN32)
if(Ice_HOME)
  file(TO_NATIVE_PATH "${Ice_HOME}" NATIVE_ICE_HOME)
  set(ENV{ICE_HOME} "${NATIVE_ICE_HOME}")
endif(Ice_HOME)
if(Ice_SLICE_DIRS)
  file(TO_NATIVE_PATH "${Ice_SLICE_DIRS}" NATIVE_ICE_SLICE_DIR)
  set(ENV{SLICEPATH} "${NATIVE_ICE_SLICE_DIR}")
endif(Ice_SLICE_DIRS)

# Run build.py to generate C++ and Ice sources.
if(NOT EXISTS ${PROJECT_SOURCE_DIR}/components/server/generated/ode/model/Image.ice)
  message(STATUS "Running build.py to generate C++ and Ice sources; this may take some time")
  execute_process(COMMAND python build.py build-default
                  RESULT_VARIABLE build_py_fail
                  WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
                  OUTPUT_FILE "${PROJECT_BINARY_DIR}/CMakeFiles/OdeBuildOutput.txt"
                  ERROR_FILE "${PROJECT_BINARY_DIR}/CMakeFiles/OdeBuildError.txt")
  if(build_py_fail)
    # On failure we must clean to ensure that we don't leave the
    # sources half-generated since this just leads to build failures
    # later on.
    message(STATUS "Failed to run build.py to generate C++ and Ice sources; cleaning.")
    execute_process(COMMAND python build.py clean
                    RESULT_VARIABLE build_py_fail2
                    WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
                    OUTPUT_FILE "${PROJECT_BINARY_DIR}/CMakeFiles/OdeCleanOutput.txt"
                    ERROR_FILE "${PROJECT_BINARY_DIR}/CMakeFiles/OdeCleanError.txt")
    message(FATAL_ERROR "Failed to run build.py to generate C/C++ and Ice sources.  See CMakeFiles/OdeBuildOutput.txt and CMakeFiles/OdeBuildError.txt")
  endif(build_py_fail)
else(NOT EXISTS ${PROJECT_SOURCE_DIR}/components/server/generated/ode/model/Image.ice)
  message(STATUS "C++ and Ice sources already generated; skipping running build.py")
endif(NOT EXISTS ${PROJECT_SOURCE_DIR}/components/server/generated/ode/model/Image.ice)

add_subdirectory(${ODE_CPP_DIR})
add_subdirectory(examples)

set(LIBRARY_PREFIX OME)
configure_file(${ODE_CPP_SOURCE_DIR}/cmake/TemplateShellConfig.cmake.in
               ${PROJECT_BINARY_DIR}/config @ONLY)
if(WIN32)
  configure_file(${ODE_CPP_SOURCE_DIR}/cmake/TemplateCmdConfig.cmake.in
                 ${PROJECT_BINARY_DIR}/config.bat @ONLY)
endif()