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

#ifndef ODE_TEMPLATES_H
#define ODE_TEMPLATES_H

#include <vector>
#include <algorithm>

/*
 * General use templates
 */

namespace ode {

    /**
     * Functoid which takes a IObjectPtr in its constructor and tests
     * via "==" comparision when operator() is called.
     */
    template<class T> struct ContainsPointer {
        const T test;
        ContainsPointer(const T lookfor) : test(lookfor) {}
        bool operator()(T const& o) {
            return o == test;
        }
    };

    /**
     * Functoid which takes a sequence in its constructor and tests
     * via std::find when operator() is called.
     */
    template<class T> struct VectorContainsPointer {
        const std::vector<T> test;
        VectorContainsPointer(const std::vector<T> lookfor) : test(lookfor) {}
        bool operator()(T const& o) {
            return std::find(test.begin(), test.end(), o) != test.end();
        }
    };

    /**
     * Return the index in the vector of the given element.
     */
    template<class T> int indexOf(const std::vector<T>& v, const T& t) {
        return static_cast<int>(std::find(v.begin(), v.end(), t) - v.begin());
    }

    /**
     * Cast an IObjectList vector to a vector of the template type.
     */
    template<class T> std::vector<T> cast(ode::api::IObjectList& v) {
        std::vector<T> rv;
        ode::api::IObjectList::iterator beg = v.begin();
        while (beg != v.end()) {
            rv.push_back(T::dynamicCast(*beg));
            beg++;
        }
        return rv;
    }

}
#endif // ODE_TEMPLATES_H
