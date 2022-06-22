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

set(GEN_DIR gen)
file(MAKE_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/${GEN_DIR})

include_directories(src gen)

include_directories("${PROJECT_SOURCE_DIR}/components/ode/generated"
                    "${CMAKE_CURRENT_SOURCE_DIR}/src"
                    "${CMAKE_CURRENT_BINARY_DIR}/${GEN_DIR}"
                    "${Ice_INCLUDE_DIRS}")

option(debug-ice-generation "Dump results of generated Ice source globbing" OFF)

# Find all static Ice slice definitions.
file(GLOB_RECURSE ICE_API_STATIC_SOURCES RELATIVE    "${PROJECT_SOURCE_DIR}"
     "${PROJECT_SOURCE_DIR}/components/ode/resources/ode/api/*.ice")
file(GLOB_RECURSE ICE_MODEL_STATIC_SOURCES RELATIVE  "${PROJECT_SOURCE_DIR}"
     "${PROJECT_SOURCE_DIR}/components/ode/resources/ode/model/*.ice")
file(GLOB_RECURSE ICE_COMMON_STATIC_SOURCES RELATIVE "${PROJECT_SOURCE_DIR}"
     "${PROJECT_SOURCE_DIR}/components/ode/resources/*.ice")

# Move from common to api and model:
list(APPEND ICE_API_STATIC_SOURCES components/ode/resources/ode/API.ice)
list(APPEND ICE_API_STATIC_SOURCES components/ode/resources/ode/ServicesF.ice)
list(APPEND ICE_MODEL_STATIC_SOURCES components/ode/resources/ode/ModelF.ice)

# Remove api and model files from the common files.
if(ICE_API_STATIC_SOURCES)
  list(REMOVE_ITEM ICE_COMMON_STATIC_SOURCES ${ICE_API_STATIC_SOURCES})
endif(ICE_API_STATIC_SOURCES)
if(ICE_MODEL_STATIC_SOURCES)
  list(REMOVE_ITEM ICE_COMMON_STATIC_SOURCES ${ICE_MODEL_STATIC_SOURCES})
endif(ICE_MODEL_STATIC_SOURCES)

# Remove client files from the common files.
set(ICE_CLIENT_STATIC_SOURCES)

if(ICE_CLIENT_STATIC_SOURCES)
  list(REMOVE_ITEM ICE_COMMON_STATIC_SOURCES ${ICE_CLIENT_STATIC_SOURCES})
endif(ICE_CLIENT_STATIC_SOURCES)

if(debug-ice-generation)
  message(STATUS "Static slice definitions (common): ${ICE_COMMON_STATIC_SOURCES}")
  message(STATUS "Static slice definitions (api):    ${ICE_API_STATIC_SOURCES}")
  message(STATUS "Static slice definitions (model):  ${ICE_MODEL_STATIC_SOURCES}")
  message(STATUS "Static slice definitions (client): ${ICE_CLIENT_STATIC_SOURCES}")
endif(debug-ice-generation)

# Find all generated Ice slice definitions.
file(GLOB_RECURSE ICE_API_GENERATED_SOURCES RELATIVE     "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/ode/api/*.ice")
file(GLOB_RECURSE ICE_MODEL_GENERATED_SOURCES RELATIVE   "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/ode/model/*.ice")
file(GLOB_RECURSE ICE_COMMON_GENERATED_SOURCES RELATIVE  "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/*.ice")

# Remove api and model files from the common files.
if(ICE_API_GENERATED_SOURCES)
  list(REMOVE_ITEM ICE_COMMON_GENERATED_SOURCES ${ICE_API_GENERATED_SOURCES})
endif(ICE_API_GENERATED_SOURCES)
if(ICE_MODEL_GENERATED_SOURCES)
  list(REMOVE_ITEM ICE_COMMON_GENERATED_SOURCES ${ICE_MODEL_GENERATED_SOURCES})
endif(ICE_MODEL_GENERATED_SOURCES)

# Remove client files from the common files.
set(ICE_CLIENT_GENERATED_SOURCES)

if(ICE_CLIENT_GENERATED_SOURCES)
  list(REMOVE_ITEM ICE_COMMON_GENERATED_SOURCES ${ICE_CLIENT_GENERATED_SOURCES})
endif(ICE_CLIENT_GENERATED_SOURCES)

if(debug-ice-generation)
  message(STATUS "Generated slice definitions (common): ${ICE_COMMON_GENERATED_SOURCES}")
  message(STATUS "Generated slice definitions (api):    ${ICE_API_GENERATED_SOURCES}")
  message(STATUS "Generated slice definitions (model):  ${ICE_MODEL_GENERATED_SOURCES}")
  message(STATUS "Generated slice definitions (client): ${ICE_CLIENT_GENERATED_SOURCES}")
endif(debug-ice-generation)

# Find all generated C++ sources and headers.
file(GLOB_RECURSE ODE_API_GENERATED_HEADERS RELATIVE   "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/ode/api/*.h")
file(GLOB_RECURSE ODE_API_GENERATED_SOURCES RELATIVE   "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/ode/api/*.cpp")
file(GLOB_RECURSE ODE_MODEL_GENERATED_HEADERS RELATIVE "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/ode/model/*.h")
file(GLOB_RECURSE ODE_MODEL_GENERATED_SOURCES RELATIVE "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/ode/model/*.cpp")
file(GLOB_RECURSE ODE_COMMON_GENERATED_HEADERS RELATIVE "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/*.h")
file(GLOB_RECURSE ODE_COMMON_GENERATED_SOURCES RELATIVE "${PROJECT_SOURCE_DIR}" "${PROJECT_SOURCE_DIR}/components/ode/generated/*.cpp")

# Remove api and model files from the common files.
if(ODE_API_GENERATED_HEADERS)
  list(REMOVE_ITEM ODE_COMMON_GENERATED_HEADERS ${ODE_API_GENERATED_HEADERS})
endif(ODE_API_GENERATED_HEADERS)
if(ODE_MODEL_GENERATED_HEADERS)
  list(REMOVE_ITEM ODE_COMMON_GENERATED_HEADERS ${ODE_MODEL_GENERATED_HEADERS})
endif(ODE_MODEL_GENERATED_HEADERS)
if(ODE_API_GENERATED_SOURCES)
  list(REMOVE_ITEM ODE_COMMON_GENERATED_SOURCES ${ODE_API_GENERATED_SOURCES})
endif(ODE_API_GENERATED_SOURCES)
if(ODE_MODEL_GENERATED_SOURCES)
  list(REMOVE_ITEM ODE_COMMON_GENERATED_SOURCES ${ODE_MODEL_GENERATED_SOURCES})
endif(ODE_MODEL_GENERATED_SOURCES)

if(debug-ice-generation)
  message(STATUS "Generated C++ headers (common): ${ODE_COMMON_GENERATED_HEADERS}")
  message(STATUS "Generated C++ sources (common): ${ODE_COMMON_GENERATED_SOURCES}")
  message(STATUS "Generated C++ headers (api):    ${ODE_API_GENERATED_HEADERS}")
  message(STATUS "Generated C++ sources (api):    ${ODE_API_GENERATED_SOURCES}")
  message(STATUS "Generated C++ headers (model):  ${ODE_MODEL_GENERATED_HEADERS}")
  message(STATUS "Generated C++ sources (model):  ${ODE_MODEL_GENERATED_SOURCES}")
  message(STATUS "Generated C++ headers (client): ${ODE_CLIENT_GENERATED_HEADERS}")
  message(STATUS "Generated C++ sources (client): ${ODE_CLIENT_GENERATED_SOURCES}")
endif(debug-ice-generation)

# Remove client files from the common files.
set(ODE_CLIENT_GENERATED_HEADERS)
set(ODE_CLIENT_GENERATED_SOURCES)

if(ODE_CLIENT_GENERATED_HEADERS)
  list(REMOVE_ITEM ODE_COMMON_GENERATED_HEADERS ${ODE_CLIENT_GENERATED_HEADERS})
endif(ODE_CLIENT_GENERATED_HEADERS)
if(ODE_CLIENT_GENERATED_SOURCES)
  list(REMOVE_ITEM ODE_COMMON_GENERATED_SOURCES ${ODE_CLIENT_GENERATED_SOURCES})
endif(ODE_CLIENT_GENERATED_SOURCES)

list(APPEND SLICEOPTS "-I${PROJECT_SOURCE_DIR}/components/ode/generated" "-I${PROJECT_SOURCE_DIR}/components/ode/resources")
foreach(slicedir ${Ice_SLICE_DIRS})
  list(APPEND SLICEOPTS "-I${slicedir}")
endforeach(slicedir)
if(WIN32)
  list(APPEND SLICEOPTS --dll-export ODE_ICE)
endif(WIN32)

set(ode_includedir "${CMAKE_INSTALL_FULL_INCLUDEDIR}/ode")

# Generate C++ headers and sources from ICE definitions with slice2cpp
# Install generated headers.
function(icegen in outsrc outhdr)
  foreach(icesrc ${${in}})
    string(REGEX REPLACE "^components/ode/(resources|generated)/" "" ICEBASE ${icesrc})
    string(REGEX REPLACE "\\.ice$" ".cpp" ICECPP ${ICEBASE})
    string(REGEX REPLACE "\\.ice$" ".h" ICEHPP ${ICEBASE})
    get_filename_component(ICE_PATH ${ICECPP} PATH)
    if("${ICE_PATH}" STREQUAL "")
      set(ICE_PATH .)
    endif("${ICE_PATH}" STREQUAL "")

    if("${ICE_PATH}" STREQUAL ".")
      # README.ice is skipped.
      unset(incdir)
    else("${ICE_PATH}" STREQUAL ".")
      set(incdir "--include-dir=${ICE_PATH}")
      list(APPEND output_sources ${GEN_DIR}/${ICECPP})
      list(APPEND output_headers ${GEN_DIR}/${ICEHPP})
      add_custom_command(
        OUTPUT ${GEN_DIR}/${ICECPP} ${GEN_DIR}/${ICEHPP}
        WORKING_DIRECTORY gen
        COMMAND ${CMAKE_COMMAND} -E make_directory "${ICE_PATH}"
        COMMAND "${Ice_SLICE2CPP_EXECUTABLE}" ${SLICEOPTS} ${incdir} --add-header "ode/internal/fixes.h" "--output-dir=${ICE_PATH}" "${PROJECT_SOURCE_DIR}/${icesrc}"
        DEPENDS "${PROJECT_SOURCE_DIR}/${icesrc}")
      install(FILES ${CMAKE_CURRENT_BINARY_DIR}/${GEN_DIR}/${ICEHPP}
              DESTINATION "${CMAKE_INSTALL_FULL_INCLUDEDIR}/${ICE_PATH}")
    endif("${ICE_PATH}" STREQUAL ".")
  endforeach(icesrc)
  set(${outsrc} ${output_sources} PARENT_SCOPE)
  set(${outhdr} ${output_headers} PARENT_SCOPE)
endfunction(icegen in out)

icegen(ICE_COMMON_STATIC_SOURCES ODE_COMMON_STATIC_ICE_SOURCES ODE_COMMON_STATIC_ICE_HEADERS)
icegen(ICE_COMMON_GENERATED_SOURCES ODE_COMMON_GENERATED_ICE_SOURCES ODE_COMMON_GENERATED_ICE_HEADERS)
icegen(ICE_API_STATIC_SOURCES ODE_API_STATIC_ICE_SOURCES ODE_API_STATIC_ICE_HEADERS)
icegen(ICE_API_GENERATED_SOURCES ODE_API_GENERATED_ICE_SOURCES ODE_API_GENERATED_ICE_HEADERS)
icegen(ICE_MODEL_STATIC_SOURCES ODE_MODEL_STATIC_ICE_SOURCES ODE_MODEL_STATIC_ICE_HEADERS)
icegen(ICE_MODEL_GENERATED_SOURCES ODE_MODEL_GENERATED_ICE_SOURCES ODE_MODEL_GENERATED_ICE_HEADERS)
icegen(ICE_CLIENT_STATIC_SOURCES ODE_CLIENT_STATIC_ICE_SOURCES ODE_CLIENT_STATIC_ICE_HEADERS)
icegen(ICE_CLIENT_GENERATED_SOURCES ODE_CLIENT_GENERATED_ICE_SOURCES ODE_CLIENT_GENERATED_ICE_HEADERS)

# Install generated headers and compute absolute path.
function(abspath_headers in out)
  list(APPEND output_headers ${${out}})
  foreach(genhdr ${${in}})
    string(REGEX REPLACE "^components/ode/generated/" "" HDRBASE ${genhdr})
    get_filename_component(HDR_PATH ${HDRBASE} PATH)
    if("${HDR_PATH}" STREQUAL "")
      set(HDR_PATH .)
    endif("${HDR_PATH}" STREQUAL "")

    if("${HDR_PATH}" STREQUAL ".")
      # README.h is skipped.
    else("${HDR_PATH}" STREQUAL ".")
      install(FILES ${PROJECT_SOURCE_DIR}/${genhdr}
              DESTINATION "${CMAKE_INSTALL_FULL_INCLUDEDIR}/${HDR_PATH}")
    endif("${HDR_PATH}" STREQUAL ".")

    list(APPEND output_headers "${PROJECT_SOURCE_DIR}/${genhdr}")
  endforeach(genhdr)
  set(${out} ${output_headers} PARENT_SCOPE)
endfunction(abspath_headers in out)

abspath_headers(ODE_API_GENERATED_HEADERS ODE_API_GENERATED_HEADERS_ABS)
abspath_headers(ODE_MODEL_GENERATED_HEADERS ODE_MODEL_GENERATED_HEADERS_ABS)
abspath_headers(ODE_COMMON_GENERATED_HEADERS ODE_COMMON_GENERATED_HEADERS_ABS)

# Absolute path for generated sources
function(abspath_sources in out)
  list(APPEND output_sources ${${out}})
  foreach(gensrc ${${in}})
    list(APPEND output_sources "${PROJECT_SOURCE_DIR}/${gensrc}")
  endforeach(gensrc)
  set(${out} ${output_sources} PARENT_SCOPE)
endfunction(abspath_sources)

abspath_sources(ODE_API_GENERATED_SOURCES ODE_API_GENERATED_SOURCES_ABS)
abspath_sources(ODE_MODEL_GENERATED_SOURCES ODE_MODEL_GENERATED_SOURCES_ABS)
abspath_sources(ODE_COMMON_GENERATED_SOURCES ODE_COMMON_GENERATED_SOURCES_ABS)

# Private headers which aren't installed.  Used only at compile time.
set(ODE_INTERNAL_HEADERS
    src/ode/internal/fixes.h)

set(ODE_ICE_HEADERS
    ${ODE_INTERNAL_HEADERS}
    ${ODE_COMMON_STATIC_ICE_HEADERS}
    ${ODE_COMMON_GENERATED_ICE_HEADERS}
    ${ODE_MODEL_STATIC_ICE_HEADERS}
    ${ODE_MODEL_GENERATED_ICE_HEADERS}
    ${ODE_API_STATIC_ICE_HEADERS}
    ${ODE_API_GENERATED_ICE_HEADERS}
    ${ODE_CLIENT_STATIC_ICE_HEADERS}
    ${ODE_CLIENT_GENERATED_ICE_HEADERS})
set(ODE_ICE_SOURCES
    ${ODE_COMMON_STATIC_ICE_SOURCES}
    ${ODE_COMMON_GENERATED_ICE_SOURCES}
    ${ODE_MODEL_STATIC_ICE_SOURCES}
    ${ODE_MODEL_GENERATED_ICE_SOURCES}
    ${ODE_API_STATIC_ICE_SOURCES}
    ${ODE_API_GENERATED_ICE_SOURCES}
    ${ODE_CLIENT_STATIC_ICE_SOURCES}
    ${ODE_CLIENT_GENERATED_ICE_SOURCES})

add_library(ode-ice SHARED
            ${ODE_ICE_HEADERS} ${ODE_ICE_SOURCES})
target_link_libraries(ode-ice Ice::Ice Ice::IceUtil Ice::Glacier2)
set_target_properties(ode-ice PROPERTIES
                      VERSION ${ODE_VERSION_SHORT})

if(MSVC)
  set_target_properties(ode-ice PROPERTIES
                        LINK_FLAGS "/INCREMENTAL:NO"
                        DEFINE_SYMBOL "ODE_ICE_EXPORTS")
endif(MSVC)

set(ODE_COMMON_STATIC_HEADERS
    src/ode/ClientErrors.h
    src/ode/IceNoWarnPop.h
    src/ode/IceNoWarnPush.h
    src/ode/IcePortPop.h
    src/ode/IcePortPush.h
    src/ode/ObjectFactoryRegistrar.h
    src/ode/RTypesI.h
    src/ode/templates.h)

set(ODE_COMMON_STATIC_SYS_HEADERS
    src/ode/sys/ParametersI.h)

set(ODE_COMMON_STATIC_SOURCES
    src/ode/ClientErrors.cpp
    src/ode/ObjectFactoryRegistrar.cpp
    src/ode/RTypesI.cpp
    src/ode/sys/ParametersI.cpp)

set(ODE_COMMON_HEADERS
    ${ODE_INTERNAL_HEADERS}
    ${ODE_COMMON_STATIC_HEADERS}
    ${ODE_COMMON_STATIC_SYS_HEADERS}
    ${ODE_COMMON_GENERATED_HEADERS_ABS})
set(ODE_COMMON_SOURCES
    ${ODE_COMMON_STATIC_SOURCES}
    ${ODE_COMMON_GENERATED_SOURCES_ABS})


set(ODE_MODEL_STATIC_MODEL_HEADERS
    src/ode/model/DetailsI.h
    src/ode/model/ObjectFactory.h
    src/ode/model/PermissionsI.h
    src/ode/model/ElectricPotentialI.h
    src/ode/model/FrequencyI.h
    src/ode/model/LengthI.h
    src/ode/model/PowerI.h
    src/ode/model/PressureI.h
    src/ode/model/TemperatureI.h
    src/ode/model/TimeI.h)

set(ODE_MODEL_STATIC_SOURCES
    src/ode/model/DetailsI.cpp
    src/ode/model/PermissionsI.cpp
    src/ode/model/ElectricPotentialI.cpp
    src/ode/model/FrequencyI.cpp
    src/ode/model/LengthI.cpp
    src/ode/model/PowerI.cpp
    src/ode/model/PressureI.cpp
    src/ode/model/TemperatureI.cpp
    src/ode/model/TimeI.cpp)

set(ODE_MODEL_HEADERS
    ${ODE_MODEL_STATIC_MODEL_HEADERS}
    ${ODE_MODEL_GENERATED_HEADERS_ABS})
set(ODE_MODEL_SOURCES
    ${ODE_MODEL_STATIC_SOURCES}
    ${ODE_MODEL_GENERATED_SOURCES_ABS})


set(ODE_API_STATIC_HEADERS)
set(ODE_API_STATIC_SOURCES)

set(ODE_API_HEADERS
    ${ODE_API_STATIC_HEADERS}
    ${ODE_API_GENERATED_HEADERS_ABS})
set(ODE_API_SOURCES
    ${ODE_API_STATIC_SOURCES}
    ${ODE_API_GENERATED_SOURCES_ABS})


set(ODE_CLIENT_STATIC_HEADERS
    src/ode/all.h
    src/ode/callbacks.h
    src/ode/client.h
    src/ode/clientF.h
    src/ode/conversions.h
    src/ode/min.h
    src/ode/ObjectFactory.h)

set(ODE_CLIENT_STATIC_UTIL_HEADERS
    src/ode/util/concurrency.h
    src/ode/util/tiles.h
    src/ode/util/uuid.h)

set(ODE_CLIENT_STATIC_SOURCES
    src/ode/callbacks.cpp
    src/ode/client.cpp
    src/ode/clientF.cpp
    src/ode/ObjectFactory.cpp
    src/ode/util/concurrency.cpp
    src/ode/conversions.cpp
    src/ode/util/tiles.cpp
    src/ode/util/uuid.cpp)

set(ODE_CLIENT_HEADERS
    ${ODE_CLIENT_STATIC_HEADERS}
    ${ODE_CLIENT_STATIC_UTIL_HEADERS}
    ${ODE_CLIENT_GENERATED_HEADERS_ABS})
set(ODE_CLIENT_SOURCES
    ${ODE_CLIENT_STATIC_SOURCES}
    ${ODE_CLIENT_GENERATED_SOURCES_ABS})

add_library(ode-client SHARED
            ${ODE_COMMON_HEADERS} ${ODE_COMMON_SOURCES}
            ${ODE_MODEL_HEADERS} ${ODE_MODEL_SOURCES}
            ${ODE_API_HEADERS} ${ODE_API_SOURCES}
            ${ODE_CLIENT_HEADERS} ${ODE_CLIENT_SOURCES})
target_link_libraries(ode-client ode-ice Ice::Ice Ice::IceUtil Ice::Glacier2)
set_target_properties(ode-client PROPERTIES
                      VERSION ${ODE_VERSION_SHORT})

if(MSVC)
  set_target_properties(ode-client PROPERTIES
                        LINK_FLAGS "/INCREMENTAL:NO"
                        DEFINE_SYMBOL "ODE_CLIENT_EXPORTS")
endif(MSVC)

set_property(TARGET ode-ice ode-client PROPERTY FOLDER "Libraries")

if(debug-ice-generation)
  message(STATUS "ode-client (headers): ${ODE_COMMON_HEADERS} ${ODE_MODEL_HEADERS} ${ODE_API_HEADERS} ${ODE_CLIENT_HEADERS}")
  message(STATUS "ode-client (sources): ${ODE_COMMON_SOURCES} ${ODE_MODEL_SOURCES} ${ODE_API_SOURCES} ${ODE_CLIENT_SOURCES}")
endif(debug-ice-generation)

add_custom_target(gensrc
                  DEPENDS ${ODE_COMMON_HEADERS} ${ODE_COMMON_SOURCES}
                          ${ODE_MODEL_HEADERS} ${ODE_MODEL_SOURCES}
                          ${ODE_API_HEADERS} ${ODE_API_SOURCES}
                          ${ODE_CLIENT_HEADERS} ${ODE_CLIENT_SOURCES})

set_property(TARGET gensrc PROPERTY FOLDER "Ice code generation")

install(TARGETS ode-ice ode-client
        LIBRARY DESTINATION ${CMAKE_INSTALL_FULL_LIBDIR}
        ARCHIVE DESTINATION ${CMAKE_INSTALL_FULL_LIBDIR}
        RUNTIME DESTINATION ${CMAKE_INSTALL_FULL_BINDIR})

install(FILES ${ODE_COMMON_STATIC_HEADERS}
              ${ODE_CLIENT_STATIC_HEADERS}
        DESTINATION ${ode_includedir})
install(FILES ${ODE_MODEL_STATIC_MODEL_HEADERS}
        DESTINATION ${ode_includedir}/model)
install(FILES ${ODE_COMMON_STATIC_SYS_HEADERS}
        DESTINATION ${ode_includedir}/sys)
install(FILES ${ODE_CLIENT_STATIC_UTIL_HEADERS}
        DESTINATION ${ode_includedir}/util)

set(LIBRARY_PREFIX ODE_ICE)
set(LIBRARY_NAME ode-ice)
set(LIBRARY_HEADER ode/RTypes.h)
configure_file(${ODE_CPP_SOURCE_DIR}/cmake/TemplateConfig.cmake.in
               ${CMAKE_CURRENT_BINARY_DIR}/ode-ice-config.cmake)
install(FILES ${CMAKE_CURRENT_BINARY_DIR}/ode-ice-config.cmake
        DESTINATION ${CMAKE_INSTALL_FULL_LIBDIR}/cmake)

set(LIBRARY_PREFIX ODE_CLIENT)
set(LIBRARY_NAME ode-client)
set(LIBRARY_HEADER ode/client.h)
configure_file(${ODE_CPP_SOURCE_DIR}/cmake/TemplateConfig.cmake.in
               ${CMAKE_CURRENT_BINARY_DIR}/ode-client-config.cmake)
install(FILES ${CMAKE_CURRENT_BINARY_DIR}/ode-client-config.cmake
        DESTINATION ${CMAKE_INSTALL_FULL_LIBDIR}/cmake)

foreach(flag -Wno-switch-default -Wno-redundant-decls -Wno-overloaded-virtual -Wno-old-style-cast -Wno-deprecated-declarations)
  string(REPLACE "/" "_" flag_var "${flag}")
  set(test_cxx_flag "CXX_FLAG${flag_var}")
  CHECK_CXX_COMPILER_FLAG(${flag} "${test_cxx_flag}")
  if (${test_cxx_flag})
     set(ICE_FLAGS "${ICE_FLAGS} ${flag}")
  endif (${test_cxx_flag})
endforeach(flag ${test_flags})

set_source_files_properties(
    ${ODE_ICE_SOURCES}
    PROPERTIES
    COMPILE_FLAGS "${ICE_FLAGS}")

# Last so that variables are propagated
add_subdirectory(test)
add_subdirectory(utils)
