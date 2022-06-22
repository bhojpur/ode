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

#ifndef ODE_OBJECTFACTORY_H
#define ODE_OBJECTFACTORY_H

#include <Ice/Ice.h>
#include <IceUtil/IceUtil.h>
#include <ode/clientF.h>

namespace ode {

    /*
     * Responsible for creating model instances based
     * on string representations of their type. An
     * instance of this class can take an Ice::Communicator
     * and add itself as the ObjectFactory to be used
     * for all known types. If another type has already
     * been registered, this instance will not register
     * itself. (Normal Ice logic is to throw an exception
     * if a type has already been registered.)
     */
  void registerObjectFactory(const Ice::CommunicatorPtr& ic,
                             const ode::client* client);

}

#endif // ODE_OBJECTFACTORY_H
