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
# Distributed under the OSI-approved BSD 3-Clause License.  See accompanying
# file Copyright.txt or https://cmake.org/licensing for details.

#.rst:
# FindIce
# -------
#
# Find the ZeroC Internet Communication Engine (ICE) programs,
# libraries and datafiles.
#
# This module supports multiple components.
# Components can include any of: ``Freeze``, ``Glacier2``, ``Ice``,
# ``IceBox``, ``IceDB``, ``IceGrid``, ``IcePatch``, ``IceSSL``,
# ``IceStorm``, ``IceUtil``, ``IceXML``, or ``Slice``.
#
# This module reports information about the Ice installation in
# several variables.  General variables::
#
#   Ice_VERSION - Ice release version
#   Ice_FOUND - true if the main programs and libraries were found
#   Ice_LIBRARIES - component libraries to be linked
#   Ice_INCLUDE_DIRS - the directories containing the Ice headers
#   Ice_SLICE_DIRS - the directories containing the Ice slice interface
#                    definitions
#
# Imported targets::
#
#   Ice::<C>
#
# Where ``<C>`` is the name of an Ice component, for example
# ``Ice::Glacier2``.
#
# Ice slice programs are reported in::
#
#   Ice_SLICE2CPP_EXECUTABLE - path to slice2cpp executable
#   Ice_SLICE2CS_EXECUTABLE - path to slice2cs executable
#   Ice_SLICE2FREEZEJ_EXECUTABLE - path to slice2freezej executable
#   Ice_SLICE2FREEZE_EXECUTABLE - path to slice2freeze executable
#   Ice_SLICE2HTML_EXECUTABLE - path to slice2html executable
#   Ice_SLICE2JAVA_EXECUTABLE - path to slice2java executable
#   Ice_SLICE2JS_EXECUTABLE - path to slice2js executable
#   Ice_SLICE2PHP_EXECUTABLE - path to slice2php executable
#   Ice_SLICE2PY_EXECUTABLE - path to slice2py executable
#   Ice_SLICE2RB_EXECUTABLE - path to slice2rb executable
#
# Ice programs are reported in::
#
#   Ice_GLACIER2ROUTER_EXECUTABLE - path to glacier2router executable
#   Ice_ICEBOX_EXECUTABLE - path to icebox executable
#   Ice_ICEBOXADMIN_EXECUTABLE - path to iceboxadmin executable
#   Ice_ICEBOXD_EXECUTABLE - path to iceboxd executable
#   Ice_ICEBOXNET_EXECUTABLE - path to iceboxnet executable
#   Ice_ICEGRIDADMIN_EXECUTABLE - path to icegridadmin executable
#   Ice_ICEGRIDNODE_EXECUTABLE - path to icegridnode executable
#   Ice_ICEGRIDNODED_EXECUTABLE - path to icegridnoded executable
#   Ice_ICEGRIDREGISTRY_EXECUTABLE - path to icegridregistry executable
#   Ice_ICEGRIDREGISTRYD_EXECUTABLE - path to icegridregistryd executable
#   Ice_ICEPATCH2CALC_EXECUTABLE - path to icepatch2calc executable
#   Ice_ICEPATCH2CLIENT_EXECUTABLE - path to icepatch2client executable
#   Ice_ICEPATCH2SERVER_EXECUTABLE - path to icepatch2server executable
#   Ice_ICESERVICEINSTALL_EXECUTABLE - path to iceserviceinstall executable
#   Ice_ICESTORMADMIN_EXECUTABLE - path to icestormadmin executable
#   Ice_ICESTORMMIGRATE_EXECUTABLE - path to icestormmigrate executable
#
# Ice db programs (Windows only; standard system versions on all other
# platforms) are reported in::
#
#   Ice_DB_ARCHIVE_EXECUTABLE - path to db_archive executable
#   Ice_DB_CHECKPOINT_EXECUTABLE - path to db_checkpoint executable
#   Ice_DB_DEADLOCK_EXECUTABLE - path to db_deadlock executable
#   Ice_DB_DUMP_EXECUTABLE - path to db_dump executable
#   Ice_DB_HOTBACKUP_EXECUTABLE - path to db_hotbackup executable
#   Ice_DB_LOAD_EXECUTABLE - path to db_load executable
#   Ice_DB_LOG_VERIFY_EXECUTABLE - path to db_log_verify executable
#   Ice_DB_PRINTLOG_EXECUTABLE - path to db_printlog executable
#   Ice_DB_RECOVER_EXECUTABLE - path to db_recover executable
#   Ice_DB_STAT_EXECUTABLE - path to db_stat executable
#   Ice_DB_TUNER_EXECUTABLE - path to db_tuner executable
#   Ice_DB_UPGRADE_EXECUTABLE - path to db_upgrade executable
#   Ice_DB_VERIFY_EXECUTABLE - path to db_verify executable
#   Ice_DUMPDB_EXECUTABLE - path to dumpdb executable
#   Ice_TRANSFORMDB_EXECUTABLE - path to transformdb executable
#
# Ice component libraries are reported in::
#
#   Ice_<C>_FOUND - ON if component was found
#   Ice_<C>_LIBRARIES - libraries for component
#
# Note that ``<C>`` is the uppercased name of the component.
#
# This module reads hints about search results from::
#
#   Ice_HOME - the root of the Ice installation
#
# The environment variable ``ICE_HOME`` may also be used; the
# Ice_HOME variable takes precedence.
#
# The following cache variables may also be set::
#
#   Ice_<P>_EXECUTABLE - the path to executable <P>
#   Ice_INCLUDE_DIR - the directory containing the Ice headers
#   Ice_SLICE_DIR - the directory containing the Ice slice interface
#                   definitions
#   Ice_<C>_LIBRARY - the library for component <C>
#
# .. note::
#
#   In most cases none of the above variables will require setting,
#   unless multiple Ice versions are available and a specific version
#   is required.  On Windows, the most recent version of Ice will be
#   found through the registry.  On Unix, the programs, headers and
#   libraries will usually be in standard locations, but Ice_SLICE_DIRS
#   might not be automatically detected (commonly known locations are
#   searched).  All the other variables are defaulted using Ice_HOME,
#   if set.  It's possible to set Ice_HOME and selectively specify
#   alternative locations for the other components; this might be
#   required for e.g. newer versions of Visual Studio if the
#   heuristics are not sufficient to identify the correct programs and
#   libraries for the specific Visual Studio version.
#
# Other variables one may set to control this module are::
#
#   Ice_DEBUG - Set to ON to enable debug output from FindIce.

# Written by Roger Leigh <rleigh@codelibre.net>

# The Ice checks are contained in a function due to the large number
# of temporary variables needed.
function(_Ice_FIND)
  # Released versions of Ice, including generic short forms
  set(ice_versions
      3
      3.6
      3.6.3
      3.6.2
      3.6.1
      3.6.0
      3.5
      3.5.1
      3.5.0
      3.4
      3.4.2
      3.4.1
      3.4.0
      3.3
      3.3.1
      3.3.0)

  # Set up search paths, taking compiler into account.  Search Ice_HOME,
  # with ICE_HOME in the environment as a fallback if unset.
  if(Ice_HOME)
    list(APPEND ice_roots "${Ice_HOME}")
  else()
    if(NOT "$ENV{ICE_HOME}" STREQUAL "")
      file(TO_CMAKE_PATH "$ENV{ICE_HOME}" NATIVE_PATH)
      list(APPEND ice_roots "${NATIVE_PATH}")
      set(Ice_HOME "${NATIVE_PATH}"
          CACHE PATH "Location of the Ice installation" FORCE)
    endif()
  endif()

  if(CMAKE_SIZEOF_VOID_P EQUAL 8)
    # 64-bit path suffix
    set(_x64 "/x64")
    # 64-bit library directory
    set(_lib64 "lib64")
  endif()

  if(MSVC_VERSION)
    # VS 8.0
    if(NOT MSVC_VERSION VERSION_LESS 1400 AND MSVC_VERSION VERSION_LESS 1500)
      set(vcver "vc80")
      set(vcyear "2005")
    # VS 9.0
    elseif(NOT MSVC_VERSION VERSION_LESS 1500 AND MSVC_VERSION VERSION_LESS 1600)
      set(vcver "vc90")
      set(vcyear "2008")
    # VS 10.0
    elseif(NOT MSVC_VERSION VERSION_LESS 1600 AND MSVC_VERSION VERSION_LESS 1700)
      set(vcver "vc100")
    # VS 11.0
    elseif(NOT MSVC_VERSION VERSION_LESS 1700 AND MSVC_VERSION VERSION_LESS 1800)
      set(vcver "vc110")
    # VS 12.0
    elseif(NOT MSVC_VERSION VERSION_LESS 1800 AND MSVC_VERSION VERSION_LESS 1900)
      set(vcver "vc120")
    # VS 14.0
    elseif(NOT MSVC_VERSION VERSION_LESS 1900 AND MSVC_VERSION VERSION_LESS 2000)
      set(vcver "vc140")
    endif()
  endif()

  # For compatibility with ZeroC Windows builds.
  if(vcver)
    # Earlier Ice (3.3) builds don't use vcnnn subdirectories, but are harmless to check.
    list(APPEND ice_binary_suffixes "bin/${vcver}${_x64}" "bin/${vcver}")
    list(APPEND ice_library_suffixes "lib/${vcver}${_x64}" "lib/${vcver}")
  endif()
  # Generic 64-bit and 32-bit directories
  list(APPEND ice_binary_suffixes "bin${_x64}" "bin")
  list(APPEND ice_library_suffixes "${_lib64}" "lib${_x64}" "lib")
  list(APPEND ice_include_suffixes "include")
  list(APPEND ice_slice_suffixes "slice")

  # On Windows, look in the registry for install locations.  Different
  # versions of Ice install support different compiler versions.
  if(vcver)
    foreach(ice_version ${ice_versions})
      # Ice 3.3 releases use a Visual Studio year suffix and value is
      # enclosed in double quotes, though only the leading quote is
      # returned by get_filename_component.
      unset(ice_location)
      if(vcyear)
        get_filename_component(ice_location
                               "[HKEY_LOCAL_MACHINE\\SOFTWARE\\ZeroC\\Ice ${ice_version} for Visual Studio ${vcyear};InstallDir]"
                               PATH)
        if(ice_location AND NOT ("${ice_location}" STREQUAL "/registry" OR "${ice_location}" STREQUAL "/"))
          string(REGEX REPLACE "^\"(.*)\"?$" "\\1" ice_location "${ice_location}")
          get_filename_component(ice_location "${ice_location}" ABSOLUTE)
        else()
          unset(ice_location)
        endif()
      endif()
      # Ice 3.4+ releases don't use a suffix
      if(NOT ice_location OR "${ice_location}" STREQUAL "/registry")
        get_filename_component(ice_location
                               "[HKEY_LOCAL_MACHINE\\SOFTWARE\\ZeroC\\Ice ${ice_version};InstallDir]"
                               ABSOLUTE)
      endif()

      if(ice_location AND NOT "${ice_location}" STREQUAL "/registry")
        list(APPEND ice_roots "${ice_location}")
      endif()
    endforeach()
  else()
    foreach(ice_version ${ice_versions})
      # Prefer 64-bit variants if present (and using a 64-bit compiler)
      list(APPEND ice_roots "/opt/Ice-${ice_version}")
    endforeach()
  endif()

  set(db_programs
      db_archive
      db_checkpoint
      db_deadlock
      db_dump
      db_hotbackup
      db_load
      db_log_verify
      db_printlog
      db_recover
      db_stat
      db_tuner
      db_upgrade
      db_verify
      dumpdb
      transformdb)

  set(ice_programs
      glacier2router
      icebox
      iceboxadmin
      iceboxd
      iceboxnet
      icegridadmin
      icegridnode
      icegridnoded
      icegridregistry
      icegridregistryd
      icepatch2calc
      icepatch2client
      icepatch2server
      iceserviceinstall
      icestormadmin
      icestormmigrate)

  set(slice_programs
      slice2cpp
      slice2cs
      slice2freezej
      slice2freeze
      slice2html
      slice2java
      slice2js
      slice2php
      slice2py
      slice2rb)

  # Find all Ice programs
  foreach(program ${db_programs} ${ice_programs} ${slice_programs})
    string(TOUPPER "${program}" program_upcase)
    set(cache_var "Ice_${program_upcase}_EXECUTABLE")
    set(program_var "Ice_${program_upcase}_EXECUTABLE")
    find_program("${cache_var}" "${program}"
      HINTS ${ice_roots}
      PATH_SUFFIXES ${ice_binary_suffixes}
      DOC "Ice ${program} executable")
    mark_as_advanced(cache_var)
    set("${program_var}" "${${cache_var}}" PARENT_SCOPE)
  endforeach()

  # Get version.
  if(Ice_SLICE2CPP_EXECUTABLE)
    # Execute in C locale for safety
    set(_Ice_SAVED_LC_ALL "$ENV{LC_ALL}")
    set(ENV{LC_ALL} C)

    execute_process(COMMAND ${Ice_SLICE2CPP_EXECUTABLE} --version
      ERROR_VARIABLE Ice_VERSION_SLICE2CPP_FULL
      ERROR_STRIP_TRAILING_WHITESPACE)

    # restore the previous LC_ALL
    set(ENV{LC_ALL} ${_Ice_SAVED_LC_ALL})

    # Make short version
    string(REGEX REPLACE "^(.*)\\.[^.]*$" "\\1" Ice_VERSION_SLICE2CPP_SHORT "${Ice_VERSION_SLICE2CPP_FULL}")
    set(Ice_VERSION "${Ice_VERSION_SLICE2CPP_FULL}" PARENT_SCOPE)
  endif()

  if(NOT Ice_FIND_QUIETLY)
    message(STATUS "Ice version: ${Ice_VERSION_SLICE2CPP_FULL}")
  endif()

  # Find include directory
  find_path(Ice_INCLUDE_DIR
            NAMES "Ice/Ice.h"
            HINTS ${ice_roots}
            PATH_SUFFIXES ${ice_include_suffixes}
            DOC "Ice include directory")
  set(Ice_INCLUDE_DIR "${Ice_INCLUDE_DIR}" PARENT_SCOPE)

  # In common use on Linux, MacOS X (homebrew) and FreeBSD; prefer
  # version-specific dir
  list(APPEND ice_slice_paths
       /usr/local/share /usr/share)
  list(APPEND ice_slice_suffixes
       "Ice-${Ice_VERSION_SLICE2CPP_FULL}/slice"
       "Ice-${Ice_VERSION_SLICE2CPP_SHORT}/slice"
       Ice)

  # Find slice directory
  find_path(Ice_SLICE_DIR
            NAMES "Ice/Connection.ice"
            HINTS ${ice_roots}
                  ${ice_slice_paths}
            PATH_SUFFIXES ${ice_slice_suffixes}
            NO_DEFAULT_PATH
            DOC "Ice slice directory")
  set(Ice_SLICE_DIR "${Ice_SLICE_DIR}" PARENT_SCOPE)

  # Find all Ice libraries
  set(Ice_REQUIRED_LIBS_FOUND ON)
  foreach(component ${Ice_FIND_COMPONENTS})
    string(TOUPPER "${component}" component_upcase)
    set(component_cache "Ice_${component_upcase}_LIBRARY")
    set(component_cache_release "${component_cache}_RELEASE")
    set(component_cache_debug "${component_cache}_DEBUG")
    set(component_found "${component_upcase}_FOUND")
    find_library("${component_cache_release}" "${component}"
      HINTS ${ice_roots}
      PATH_SUFFIXES ${ice_library_suffixes}
      DOC "Ice ${component} library (release)")
    find_library("${component_cache_debug}" "${component}d"
      HINTS ${ice_roots}
      PATH_SUFFIXES ${ice_library_suffixes}
      DOC "Ice ${component} library (debug)")
    include(SelectLibraryConfigurations)
    select_library_configurations(Ice_${component_upcase})
    mark_as_advanced("${component_cache_release}" "${component_cache_debug}")
    if(${component_cache})
      set("${component_found}" ON)
      list(APPEND Ice_LIBRARY "${${component_cache}}")
    endif()
    mark_as_advanced("${component_found}")
    set("${component_cache}" "${${component_cache}}" PARENT_SCOPE)
    set("${component_found}" "${${component_found}}" PARENT_SCOPE)
    if(${component_found})
      if (Ice_FIND_REQUIRED_${component})
        list(APPEND Ice_LIBS_FOUND "${component} (required)")
      else()
        list(APPEND Ice_LIBS_FOUND "${component} (optional)")
      endif()
    else()
      if (Ice_FIND_REQUIRED_${component})
        set(Ice_REQUIRED_LIBS_FOUND OFF)
        list(APPEND Ice_LIBS_NOTFOUND "${component} (required)")
      else()
        list(APPEND Ice_LIBS_NOTFOUND "${component} (optional)")
      endif()
    endif()
  endforeach()
  set(_Ice_REQUIRED_LIBS_FOUND "${Ice_REQUIRED_LIBS_FOUND}" PARENT_SCOPE)
  set(Ice_LIBRARY "${Ice_LIBRARY}" PARENT_SCOPE)

  if(NOT Ice_FIND_QUIETLY)
    if(Ice_LIBS_FOUND)
      message(STATUS "Found the following Ice libraries:")
      foreach(found ${Ice_LIBS_FOUND})
        message(STATUS "  ${found}")
      endforeach()
    endif()
    if(Ice_LIBS_NOTFOUND)
      message(STATUS "The following Ice libraries were not found:")
      foreach(notfound ${Ice_LIBS_NOTFOUND})
        message(STATUS "  ${notfound}")
      endforeach()
    endif()
  endif()

  if(Ice_DEBUG)
    message(STATUS "--------FindIce.cmake search debug--------")
    message(STATUS "ICE binary path search order: ${ice_roots}")
    message(STATUS "ICE include path search order: ${ice_roots}")
    message(STATUS "ICE slice path search order: ${ice_roots} ${ice_slice_paths}")
    message(STATUS "ICE library path search order: ${ice_roots}")
    message(STATUS "----------------")
  endif()
endfunction()

_Ice_FIND()

include(FindPackageHandleStandardArgs)
FIND_PACKAGE_HANDLE_STANDARD_ARGS(Ice
                                  FOUND_VAR Ice_FOUND
                                  REQUIRED_VARS Ice_SLICE2CPP_EXECUTABLE
                                                Ice_INCLUDE_DIR
                                                Ice_SLICE_DIR
                                                Ice_LIBRARY
                                                _Ice_REQUIRED_LIBS_FOUND
                                  VERSION_VAR Ice_VERSION
                                  FAIL_MESSAGE "Failed to find all Ice components")

unset(_Ice_REQUIRED_LIBS_FOUND)

if(Ice_FOUND)
  set(Ice_INCLUDE_DIRS "${Ice_INCLUDE_DIR}")
  set(Ice_SLICE_DIRS "${Ice_SLICE_DIR}")
  set(Ice_LIBRARIES "${Ice_LIBRARY}")
  foreach(_Ice_component ${Ice_FIND_COMPONENTS})
    string(TOUPPER "${_Ice_component}" _Ice_component_upcase)
    set(_Ice_component_cache "Ice_${_Ice_component_upcase}_LIBRARY")
    set(_Ice_component_cache_release "Ice_${_Ice_component_upcase}_LIBRARY_RELEASE")
    set(_Ice_component_cache_debug "Ice_${_Ice_component_upcase}_LIBRARY_DEBUG")
    set(_Ice_component_lib "Ice_${_Ice_component_upcase}_LIBRARIES")
    set(_Ice_component_found "${_Ice_component_upcase}_FOUND")
    set(_Ice_imported_target "Ice::${_Ice_component}")
    if(${_Ice_component_found})
      set("${_Ice_component_lib}" "${${_Ice_component_cache}}")
      if(NOT TARGET ${_Ice_imported_target})
        add_library(${_Ice_imported_target} UNKNOWN IMPORTED)
        if()
          set_target_properties(${_Ice_imported_target} PROPERTIES
            INTERFACE_INCLUDE_DIRECTORIES "${Ice_INCLUDE_DIR}")
        endif()
        if(EXISTS "${${_Ice_component_cache}}")
          set_target_properties(${_Ice_imported_target} PROPERTIES
            IMPORTED_LINK_INTERFACE_LANGUAGES "CXX"
            IMPORTED_LOCATION "${${_Ice_component_cache}}")
        endif()
        if(EXISTS "${${_Ice_component_cache_release}}")
          set_property(TARGET ${_Ice_imported_target} APPEND PROPERTY
            IMPORTED_CONFIGURATIONS RELEASE)
          set_target_properties(${_Ice_imported_target} PROPERTIES
            IMPORTED_LINK_INTERFACE_LANGUAGES_RELEASE "CXX"
            IMPORTED_LOCATION_RELEASE "${${_Ice_component_cache_release}}")
        endif()
        if(EXISTS "${${_Ice_component_cache_debug}}")
          set_property(TARGET ${_Ice_imported_target} APPEND PROPERTY
            IMPORTED_CONFIGURATIONS DEBUG)
          set_target_properties(${_Ice_imported_target} PROPERTIES
            IMPORTED_LINK_INTERFACE_LANGUAGES_DEBUG "CXX"
            IMPORTED_LOCATION_DEBUG "${${_Ice_component_cache_debug}}")
        endif()
      endif()
    endif()
    unset(_Ice_component_upcase)
    unset(_Ice_component_cache)
    unset(_Ice_component_lib)
    unset(_Ice_component_found)
    unset(_Ice_imported_target)
  endforeach()
endif()

if(Ice_DEBUG)
  message(STATUS "--------FindIce.cmake results debug--------")
  message(STATUS "Ice_VERSION number: ${Ice_VERSION}")
  message(STATUS "Ice_HOME directory: ${Ice_HOME}")
  message(STATUS "Ice_INCLUDE_DIR directory: ${Ice_INCLUDE_DIR}")
  message(STATUS "Ice_SLICE_DIR directory: ${Ice_SLICE_DIR}")
  message(STATUS "Ice_LIBRARIES: ${Ice_LIBRARIES}")

  message(STATUS "slice2cpp executable: ${Ice_SLICE2CPP_EXECUTABLE}")
  message(STATUS "slice2cs executable: ${Ice_SLICE2CS_EXECUTABLE}")
  message(STATUS "slice2freezej executable: ${Ice_SLICE2FREEZEJ_EXECUTABLE}")
  message(STATUS "slice2freeze executable: ${Ice_SLICE2FREEZE_EXECUTABLE}")
  message(STATUS "slice2html executable: ${Ice_SLICE2HTML_EXECUTABLE}")
  message(STATUS "slice2java executable: ${Ice_SLICE2JAVA_EXECUTABLE}")
  message(STATUS "slice2js executable: ${Ice_SLICE2JS_EXECUTABLE}")
  message(STATUS "slice2php executable: ${Ice_SLICE2PHP_EXECUTABLE}")
  message(STATUS "slice2py executable: ${Ice_SLICE2PY_EXECUTABLE}")
  message(STATUS "slice2rb executable: ${Ice_SLICE2RB_EXECUTABLE}")
  message(STATUS "glacier2router executable: ${Ice_GLACIER2ROUTER_EXECUTABLE}")

  message(STATUS "icebox executable: ${Ice_ICEBOX_EXECUTABLE}")
  message(STATUS "iceboxadmin executable: ${Ice_ICEBOXADMIN_EXECUTABLE}")
  message(STATUS "iceboxd executable: ${Ice_ICEBOXD_EXECUTABLE}")
  message(STATUS "iceboxnet executable: ${Ice_ICEBOXNET_EXECUTABLE}")
  message(STATUS "icegridadmin executable: ${Ice_ICEGRIDADMIN_EXECUTABLE}")
  message(STATUS "icegridnode executable: ${Ice_ICEGRIDNODE_EXECUTABLE}")
  message(STATUS "icegridnoded executable: ${Ice_ICEGRIDNODED_EXECUTABLE}")
  message(STATUS "icegridregistry executable: ${Ice_ICEGRIDREGISTRY_EXECUTABLE}")
  message(STATUS "icegridregistryd executable: ${Ice_ICEGRIDREGISTRYD_EXECUTABLE}")
  message(STATUS "icepatch2calc executable: ${Ice_ICEPATCH2CALC_EXECUTABLE}")
  message(STATUS "icepatch2client executable: ${Ice_ICEPATCH2CLIENT_EXECUTABLE}")
  message(STATUS "icepatch2server executable: ${Ice_ICEPATCH2SERVER_EXECUTABLE}")
  message(STATUS "iceserviceinstall executable: ${Ice_ICESERVICEINSTALL_EXECUTABLE}")
  message(STATUS "icestormadmin executable: ${Ice_ICESTORMADMIN_EXECUTABLE}")
  message(STATUS "icestormmigrate executable: ${Ice_ICESTORMMIGRATE_EXECUTABLE}")

  message(STATUS "db_archive executable: ${Ice_DB_ARCHIVE_EXECUTABLE}")
  message(STATUS "db_checkpoint executable: ${Ice_DB_CHECKPOINT_EXECUTABLE}")
  message(STATUS "db_deadlock executable: ${Ice_DB_DEADLOCK_EXECUTABLE}")
  message(STATUS "db_dump executable: ${Ice_DB_DUMP_EXECUTABLE}")
  message(STATUS "db_hotbackup executable: ${Ice_DB_HOTBACKUP_EXECUTABLE}")
  message(STATUS "db_load executable: ${Ice_DB_LOAD_EXECUTABLE}")
  message(STATUS "db_log_verify executable: ${Ice_DB_LOG_VERIFY_EXECUTABLE}")
  message(STATUS "db_printlog executable: ${Ice_DB_PRINTLOG_EXECUTABLE}")
  message(STATUS "db_recover executable: ${Ice_DB_RECOVER_EXECUTABLE}")
  message(STATUS "db_stat executable: ${Ice_DB_STAT_EXECUTABLE}")
  message(STATUS "db_tuner executable: ${Ice_DB_TUNER_EXECUTABLE}")
  message(STATUS "db_upgrade executable: ${Ice_DB_UPGRADE_EXECUTABLE}")
  message(STATUS "db_verify executable: ${Ice_DB_VERIFY_EXECUTABLE}")
  message(STATUS "dumpdb executable: ${Ice_DUMPDB_EXECUTABLE}")
  message(STATUS "transformdb executable: ${Ice_TRANSFORMDB_EXECUTABLE}")

  foreach(component ${Ice_FIND_COMPONENTS})
    string(TOUPPER "${component}" component_upcase)
    set(component_lib "Ice_${component_upcase}_LIBRARIES")
    set(component_found "${component_upcase}_FOUND")
    message(STATUS "${component} library found: ${${component_found}}")
    message(STATUS "${component} library: ${${component_lib}}")
  endforeach()
  message(STATUS "----------------")
endif()
