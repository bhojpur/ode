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

/*
 * Bhojpur ODE Concurrency Utilities
 */

#ifndef ODE_UTIL_CONCURRENCY_H
#define ODE_UTIL_CONCURRENCY_H

#include <ode/IceNoWarnPush.h>
#include <IceUtil/Cond.h>
#include <ode/IceNoWarnPop.h>
#include <IceUtil/Monitor.h>
#include <IceUtil/RecMutex.h>
#include <IceUtil/Time.h>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {
    namespace util {
        namespace concurrency {

            /**
            * Port of Python's threading.Event to C++
            */
            class ODE_CLIENT Event : private IceUtil::Monitor<IceUtil::RecMutex> {
            private:
                bool flag;
                IceUtil::RecMutex mutex;
                IceUtil::Cond cond;
            public:
                Event();
                ~Event();
                bool isSet();
                void set();
                void clear();
                bool wait(const IceUtil::Time& timeout);
            };

        }
    }
}

#endif // ODE_UTIL_CONCURRENCY_H
