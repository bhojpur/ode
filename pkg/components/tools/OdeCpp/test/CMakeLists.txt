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

if(BUILD_TESTS)
  include_directories(${GTEST_INCLUDE_DIR} ${CMAKE_CURRENT_SOURCE_DIR})

  set(test_main_sources main.cpp)

  add_library(testmain STATIC ${test_main_sources})

  set_property(TARGET testmain PROPERTY FOLDER "Libraries")

  set(TEST_LIBS testmain ${GTEST_LIBRARIES} ${CMAKE_THREAD_LIBS_INIT})

  function(headers_test component path)
    set(headerdir ${PROJECT_BINARY_DIR}/test/${component}/headers)
    file(MAKE_DIRECTORY ${headerdir})
    set(dirs ${PROJECT_SOURCE_DIR}/lib ${PROJECT_BINARY_DIR}/lib)
    foreach(dir ${dirs})
      file(GLOB_RECURSE headers RELATIVE
           "${dir}"
           "${dir}/${path}/*.h")

    foreach(header ${headers})
      string(REPLACE "/" "_" genheader ${header})
      string(REPLACE "${PROJECT_SOURCE_DIR}/src/" "" include ${header})
      string(REPLACE "${headerdir}/" "" include ${include})
      string(REGEX REPLACE "\\.h$" ".cpp" genheader ${genheader})
      string(REGEX REPLACE "[/.]" "_" safeheader ${include})
      string(CONFIGURE "#include <@include@>

#include <gtest/gtest.h>

TEST(Header, ${safeheader})
{
}
" src)
      file(WRITE "${headerdir}/${genheader}" "${src}")
      list(APPEND test_headers_SOURCES "${headerdir}/${genheader}")
    endforeach(header)
    endforeach(dir)

    add_executable(${component}-headers ${test_headers_SOURCES})
    target_link_libraries(${component}-headers ${TEST_LIBS})
  endfunction(headers_test)
endif(BUILD_TESTS)

add_subdirectory(ode)
add_subdirectory(integration)
add_subdirectory(unit)
