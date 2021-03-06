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

# Bio-Formats C/C++ libraries (cmake build infrastructure)

# Use new variable expansion policy.
if (POLICY CMP0053)
  cmake_policy(SET CMP0053 NEW)
endif(POLICY CMP0053)
if (POLICY CMP0054)
  cmake_policy(SET CMP0054 NEW)
endif(POLICY CMP0054)

function(cxx_std_check flag var)
  check_cxx_compiler_flag("${flag}" ${var})
  if (${var})
    set(CMAKE_CXX_FLAGS_SAVE "${CMAKE_CXX_FLAGS}")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${flag}")

    check_cxx_source_compiles("#include <cstdarg>

void format(const char *fmt, va_list ap)
{
  va_list ap2;
  va_copy(ap2, ap);
}

int main() {
}"
"${var}_CSTDARG")

    check_cxx_source_compiles("#include <stdarg.h>

void format(const char *fmt, va_list ap)
{
  va_list ap2;
  va_copy(ap2, ap);
}

int main() {
}"
"${var}_STDARG")

    if("${var}" STREQUAL "CXX_FLAG_CXX11")
      check_cxx_source_compiles("#include <type_traits>

// overloads are enabled via the return type
template<class T>
typename std::enable_if<std::is_floating_point<T>::value, T>::type
test(T t)
{
  return t;
}

int main()
{
  test(2.4);
}"
"${var}_ENABLE_IF")
    endif("${var}" STREQUAL "CXX_FLAG_CXX11")
  else(${var})
    set("${var}_CSTDARG" OFF)
    set("${var}_STDARG" OFF)
    set("${var}_ENABLE_IF" OFF)
  endif(${var})

  if(${${var}_CSTDARG} OR ${${var}_STDARG})
    set(${var} ${${var}} PARENT_SCOPE)
  else()
    set(${var} FALSE PARENT_SCOPE)
  endif()

  if("${var}" STREQUAL "CXX_FLAG_CXX11")
    if(${var} AND NOT ${${var}_ENABLE_IF})
      set(${var} FALSE PARENT_SCOPE)
    endif()
  endif()

  set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS_SAVE}")
endfunction(cxx_std_check)


# Try to put the compiler into the most recent standard mode.  This
# will generally have the most features, and will remove the need for
# Boost fallbacks if native implementations are available.
option(cxxstd-autodetect "Enable C++14 features if possible, otherwise fall back to C++11, C++03 or C++98" OFF)
if (cxxstd-autodetect)
  if (NOT MSVC)
    cxx_std_check(-std=c++14 CXX_FLAG_CXX14)
    if (CXX_FLAG_CXX14)
      set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14")
    else()
      cxx_std_check(-std=c++11 CXX_FLAG_CXX11)
      if (CXX_FLAG_CXX11)
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
      else()
        cxx_std_check(-std=c++03 CXX_FLAG_CXX03)
        if (CXX_FLAG_CXX03)
          set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++03")
        else()
          cxx_std_check(-std=c++98 CXX_FLAG_CXX98)
          if (CXX_FLAG_CXX98)
            set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++98")
          else()
            cxx_std_check("" CXX_FLAG_NONE)
            if (NOT CXX_FLAG_NONE)
              message(WARNING "Could not determine compiler options for enabling the most recent C++ standard; this might be expected for your compiler")
            endif (NOT CXX_FLAG_NONE)
          endif(CXX_FLAG_CXX98)
        endif(CXX_FLAG_CXX03)
      endif(CXX_FLAG_CXX11)
    endif(CXX_FLAG_CXX14)
  endif (NOT MSVC)
endif (cxxstd-autodetect)

# Try to enable the -pedantic flag.  This one needs special casing
# since it may break building with older compilers where int64_t (long
# long) isn't available in pedantic mode because it's not part of the
# C++98 standard.  Newer compilers support long long properly.
if (NOT MSVC)
  set(flag -pedantic)
  set(test_cxx_flag "CXX_FLAG${flag}")
  CHECK_CXX_COMPILER_FLAG(${flag} "${test_cxx_flag}")
  if (${test_cxx_flag})
    SET(CMAKE_CXX_FLAGS_SAVE ${CMAKE_CXX_FLAGS})
    SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${flag}")
    check_cxx_source_compiles(
  "int main() {
    long long l;
  }"
  CXX_PEDANTIC_LONG_LONG)
    SET(CMAKE_CXX_FLAGS ${CMAKE_CXX_FLAGS_SAVE})
    if (${CXX_PEDANTIC_LONG_LONG})
      set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${flag}")
    endif (${CXX_PEDANTIC_LONG_LONG})
  endif (${test_cxx_flag})
endif (NOT MSVC)

# These are annoyingly verbose, produce false positives or don't work
# nicely with all supported compiler versions, so are disabled unless
# explicitly enabled.
option(extra-warnings "Enable extra compiler warnings" OFF)

# This will cause the compiler to fail when an error occurs.
option(fatal-warnings "Compiler warnings are errors" OFF)

# Check if the compiler supports each of the following additional
# warning flags, and enable them if supported.  This greatly improves
# the quality of the build by checking for a number of common
# problems, some of which are quite serious.
if (NOT MSVC)
  set(test_flags
      -Wall
      -Wcast-align
      -Wcast-qual
      -Wctor-dtor-privacy
      -Wextra
      -Wformat=2
      -Wimplicit-atomic-properties
      -Wmissing-declarations
      -Wno-long-long
      -Wnon-virtual-dtor
      -Wold-style-cast
      -Woverlength-strings
      -Woverloaded-virtual
      -Wredundant-decls
      -Wreorder
      -Wswitch-default
      -Wunused-variable
      -Wwrite-strings
      -fstrict-aliasing)
  if (extra-warnings)
    list(APPEND test_flags
        -Wconversion
        -Wdocumentation
        -Wfloat-equal
        -Wmissing-prototypes
        -Wunreachable-code)
  endif (extra-warnings)
  if (fatal-warnings)
    list(APPEND test_flags
         -Werror)
  endif (fatal-warnings)
else (NOT MSVC)
  set(test_flags
      /bigobj)
  if (extra-warnings)
    list(APPEND test_flags
         /W4)
  else (extra-warnings)
    list(APPEND test_flags
         /W3)
  endif (extra-warnings)
  if (fatal-warnings)
    list(APPEND test_flags
         /WX)
  endif (fatal-warnings)
endif (NOT MSVC)

foreach(flag ${test_flags})
  string(REGEX REPLACE "[^A-Za-z0-9]" "_" flag_var "${flag}")
  set(test_cxx_flag "CXX_FLAG${flag_var}")
  CHECK_CXX_COMPILER_FLAG(${flag} "${test_cxx_flag}")
  if (${test_cxx_flag})
     set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${flag}")
  endif (${test_cxx_flag})
endforeach(flag ${test_flags})

check_cxx_source_compiles("
#include <tuple>
int main() { std::tuple<int,double> t; }
" ODE_HAVE_TUPLE)

check_cxx_source_compiles("
#include <tr1/tuple>
int main() { std::tr1::tuple<int,double> t; }
" ODE_HAVE_TR1_TUPLE)

check_cxx_source_compiles("
#include <cstdint>
int main() { uint16_t test(134); }
" ODE_HAVE_CSTDINT)

check_cxx_source_compiles("
#include <memory>
struct foo : public ode::compat::enable_shared_from_this<foo>
{
        foo() {}
};
int main() { ode::compat::shared_ptr<foo> f(new foo()); }
" ODE_HAVE_MEMORY)

check_cxx_source_compiles("
void foo() noexcept{}
int main() { foo(); }
" ODE_HAVE_NOEXCEPT)

check_cxx_source_compiles("
#include <array>
int main() { std::array<int,3> a; a[0] = 5; }
" ODE_HAVE_ARRAY)

check_cxx_source_compiles("
#include <cstdarg>

void print(const char *fmt, ...)
{
  va_list va1, va2;
  va_start(va1, fmt);
  va_copy(va2, va1);
  va_end(va1);
}

int main() { print(\"%d %s\", 43, \"test\"); }
" ODE_HAVE_CSTDARG)
