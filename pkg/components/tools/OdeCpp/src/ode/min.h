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

#if !defined(ODE_IMPORT_STYLE) || ODE_IMPORT_STYLE == 'A'
    // Here we allow the defining of style
    // only if we are not being called by
    // all.h. If so, we assume that its
    // ifdef directive will prevent this
    // from being called multiple times.
    #if !defined(ODE_IMPORT_STYLE)
        #define ODE_IMPORT_STYLE 'M'
    #endif
#include <ode/IceNoWarnPush.h>
#include<ode/API.h>
#include<ode/ServicesF.h>
#include<ode/Constants.h>
#include<ode/cmd/API.h>
#include <ode/IceNoWarnPop.h>
#include<ode/RTypesI.h>
#include<ode/model/NamedValue.h>
#endif
