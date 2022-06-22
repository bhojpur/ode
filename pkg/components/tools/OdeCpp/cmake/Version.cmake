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

cmake_policy(SET CMP0007 NEW)

function(ode_version)
  set(ODE_VERSION UNKNOWN)
  set(ODE_VERSION_SHORT UNKNOWN)
  set(ODE_VCS_SHORTREVISION UNKNOWN)
  set(ODE_VCS_REVISION UNKNOWN)
  set(ODE_VCS_DATE UNKNOWN)
  set(ODE_VCS_DATE_S UNKNOWN)

  if(EXISTS "${PROJECT_SOURCE_DIR}/components/tools/OdeCpp/cmake/GitVersion.cmake")
    message(STATUS "Obtaining release version from source")
    include("${PROJECT_SOURCE_DIR}/components/tools/OdeCpp/cmake/GitVersion.cmake")
  else(EXISTS "${PROJECT_SOURCE_DIR}/components/tools/OdeCpp/cmake/GitVersion.cmake")
    message(STATUS "Obtaining version from git")

    find_package(Git)

    if(NOT GIT_FOUND)
      message(FATAL_ERROR "No git executable found for getting version")
    endif(NOT GIT_FOUND)

    execute_process(COMMAND "${GIT_EXECUTABLE}" log -1 HEAD --pretty=%h
      OUTPUT_VARIABLE commit_hash_short RESULT_VARIABLE git_log_fail ERROR_QUIET
      WORKING_DIRECTORY ${PROJECT_SOURCE_DIR})
    if (git_log_fail)
      message(FATAL_ERROR "Could not obtain release commit hash from git")
    endif (git_log_fail)
    string(REPLACE "\n" "" commit_hash_short "${commit_hash_short}")

    execute_process(COMMAND "${GIT_EXECUTABLE}" log -1 HEAD --pretty=%H
      OUTPUT_VARIABLE commit_hash RESULT_VARIABLE git_log_fail ERROR_QUIET
      WORKING_DIRECTORY ${PROJECT_SOURCE_DIR})
    if (git_log_fail)
      message(FATAL_ERROR "Could not obtain release commit hash from git")
    endif (git_log_fail)
    string(REPLACE "\n" "" commit_hash "${commit_hash}")

    execute_process(COMMAND "${GIT_EXECUTABLE}" log -1 "${commit_hash}" --pretty=%ai
      OUTPUT_VARIABLE commit_date_string RESULT_VARIABLE git_log_fail ERROR_QUIET
      WORKING_DIRECTORY ${PROJECT_SOURCE_DIR})
    if (git_log_fail)
      message(FATAL_ERROR "Could not obtain release commit timestamp string from git")
    endif (git_log_fail)
    string(REPLACE "\n" "" commit_date_string "${commit_date_string}")

    execute_process(COMMAND "${GIT_EXECUTABLE}" log -1 "${commit_hash}" --pretty=%at
      OUTPUT_VARIABLE commit_date_unix RESULT_VARIABLE git_log_fail ERROR_QUIET
      WORKING_DIRECTORY ${PROJECT_SOURCE_DIR})
    if (git_log_fail)
      message(FATAL_ERROR "Could not obtain release commit timestamp from git")
    endif (git_log_fail)
    string(REPLACE "\n" "" commit_date_unix "${commit_date_unix}")

    set(ODE_VCS_SHORTREVISION ${commit_hash_short} PARENT_SCOPE)
    set(ODE_VCS_REVISION ${commit_hash} PARENT_SCOPE)
    set(ODE_VCS_DATE ${commit_date_unix} PARENT_SCOPE)
    set(ODE_VCS_DATE_S ${commit_date_string} PARENT_SCOPE)

    execute_process(COMMAND "${GIT_EXECUTABLE}" describe "--match=v*[0-9]*.[0-9]*.[0-9]*" --exact
                    OUTPUT_VARIABLE describe_exact_output
                    RESULT_VARIABLE describe_exact_fail
                    ERROR_QUIET
                    WORKING_DIRECTORY ${PROJECT_SOURCE_DIR})
    string(REPLACE "\n" "" describe_exact_output "${describe_exact_output}")

    execute_process(COMMAND "${GIT_EXECUTABLE}" describe "--match=v*[0-9]*.[0-9]*.[0-9]*"
                    OUTPUT_VARIABLE describe_output
                    RESULT_VARIABLE describe_fail
                    ERROR_QUIET
                    WORKING_DIRECTORY ${PROJECT_SOURCE_DIR})
    string(REPLACE "\n" "" describe_output "${describe_output}")

    if(NOT describe_exact_fail)
      set(ODE_VERSION ${describe_exact_output})
    else(NOT describe_exact_fail)
      if(NOT describe_fail)
        set(ODE_VERSION ${describe_output})
      else(NOT describe_fail)
        # ARGH!  We need a valid version for library and release
        # versioning, so this isn't acceptable.
        message(FATAL_ERROR "Release version is not known")
      endif(NOT describe_fail)
    endif(NOT describe_exact_fail)
  endif(EXISTS "${PROJECT_SOURCE_DIR}/components/tools/OdeCpp/cmake/GitVersion.cmake")

  set(ODE_VCS_SHORTREVISION "${ODE_VCS_SHORTREVISION}" PARENT_SCOPE)
  set(ODE_VCS_REVISION "${ODE_VCS_REVISION}" PARENT_SCOPE)
  set(ODE_VCS_DATE "${ODE_VCS_DATE}" PARENT_SCOPE)
  set(ODE_VCS_DATE_S "${ODE_VCS_DATE_S}" PARENT_SCOPE)

  string(REGEX MATCH "^v\\.?(.*)" commit_valid1 ${ODE_VERSION})
  if (commit_valid1)
    string(REGEX REPLACE "^v\\.?(.*)" "\\1" ODE_VERSION ${ODE_VERSION})
  endif (commit_valid1)

  set(ODE_VERSION ${ODE_VERSION} PARENT_SCOPE)

  string(REGEX MATCH "([0-9]+)\\.([0-9]+)\\.([0-9]+)(.*)?" commit_valid ${ODE_VERSION})
  if (commit_valid)
    string(REGEX REPLACE "^([0-9]+)\\.([0-9]+)\\.([0-9]+)(.*)?" "\\1;\\2;\\3;\\4" version_parts ${ODE_VERSION})
    list(GET version_parts 0 version_major)
    set(ODE_VERSION_MAJOR ${version_major} PARENT_SCOPE)
    list(GET version_parts 1 version_minor)
    set(ODE_VERSION_MINOR ${version_minor} PARENT_SCOPE)
    list(GET version_parts 2 version_patch)
    set(ODE_VERSION_PATCH ${version_patch} PARENT_SCOPE)
    list(GET version_parts 3 version_extra)
    set(ODE_VERSION_EXTRA ${version_extra} PARENT_SCOPE)
    set(ODE_VERSION_SHORT "${version_major}.${version_minor}.${version_patch}" PARENT_SCOPE)
  else(commit_valid)
    set(ODE_VERSION_MAJOR UNKNOWN PARENT_SCOPE)
    set(ODE_VERSION_MINOR UNKNOWN PARENT_SCOPE)
    set(ODE_VERSION_PATCH UNKNOWN PARENT_SCOPE)
    set(ODE_VERSION_EXTRA UNKNOWN PARENT_SCOPE)
    set(ODE_VERSION_SHORT UNKNOWN PARENT_SCOPE)
  endif(commit_valid)
endfunction(ode_version)

ode_version()

message(STATUS "Configuring Bhojpur ODE version ${ODE_VERSION}")
if(ODE_VCS_SHORTREVISION AND ODE_VCS_DATE_S)
  message(STATUS "Using git commit ${ODE_VCS_SHORTREVISION} on ${ODE_VCS_DATE_S}")
endif()
