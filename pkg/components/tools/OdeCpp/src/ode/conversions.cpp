// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#include <ode/conversions.h>
#include <math.h>

namespace ode {

  namespace conversions {

      Conversion::Conversion(const ConversionPtr& c1, const ConversionPtr& c2) {
        conversions.push_back(c1);
        conversions.push_back(c2);
      }

      Conversion::Conversion(const ConversionPtr& c1) {
        conversions.push_back(c1);
      }

      Conversion::Conversion() {

      }

      Conversion::~Conversion() {

      }

      _Add::_Add(const ConversionPtr& c1, const ConversionPtr& c2)
          : Conversion(c1, c2) {
      }

      double _Add::convert(double original) {
          double rv = 0.0;
          std::vector<ConversionPtr>::iterator beg = conversions.begin();
          std::vector<ConversionPtr>::iterator end = conversions.end();
          for(; beg != end; ++beg) {
              rv += (*beg)->convert(original);
          }
          return rv;
      }

      _Int::_Int(long i) : i(i) {

      }

      _Int::_Int(std::string s) : s(s) {

      }

      double _Int::convert(double) {
          return i;
      }

      _Mul::_Mul(const ConversionPtr& c1, const ConversionPtr& c2)
          : Conversion(c1, c2) {
      }

      double _Mul::convert(double original) {
          double rv = 1.0;
          std::vector<ConversionPtr>::iterator beg = conversions.begin();
          std::vector<ConversionPtr>::iterator end = conversions.end();
          for(; beg != end; ++beg) {
              rv *= (*beg)->convert(original);
          }
          return rv;
      }

      _Pow::_Pow(long base, int exp) : base(base), exp(exp) {

      }

      double _Pow::convert(double original) {
          if (conversions.size() > 0) {
            return pow(
                    conversions[0]->convert(original),
                    conversions[1]->convert(original));
          } else {
            return pow(static_cast<double>(base), exp);
          }
      }

      _Rat::_Rat(long num, long denom) : num(num), denom(denom) {

      }

      _Rat::_Rat(const ConversionPtr& c1, const ConversionPtr& c2) :
          Conversion(c1, c2) {

      }

      double _Rat::convert(double original) {
          if (conversions.size() > 0) {
            return conversions[0]->convert(original) /
                   conversions[1]->convert(original);
          } else {
              return static_cast<double>(num) / static_cast<double>(denom);
          }
      }

      _Sym::_Sym(char) {

      }

      _Sym::_Sym(std::string) {

      }

      double _Sym::convert(double original) {
          return original;
      }

  }

}
