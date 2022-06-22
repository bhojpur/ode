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

#ifndef ODE_CLIENT_ERRORS_H
#define ODE_CLIENT_ERRORS_H

#include <ode/IceNoWarnPush.h>
#include <Ice/Ice.h>
#include <ode/IceNoWarnPop.h>

#include <ostream>
#include <iostream>
#include <exception>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

/*
 * Client-side exceptions thrown, especially by the generated
 * model entities.
 */
namespace ode {

  class ODE_CLIENT ClientError : public std::exception
  {
  protected:
    int _line;
    const char* _file;
    const char* _msg;
  public:
    ClientError(const char*, int, const char* message);
    virtual ~ClientError() throw();
    virtual const char* name() const throw();
    virtual const char* file() const throw();
    virtual int line() const throw();
    virtual char const* what() const throw();
  };

  /*
   * Thrown if an object is unloaded (see loaded field) and any
   * method which is expecting valid state is called. (The id
   * of an unloaded object will always be sent by the server.)
   */
  class ODE_CLIENT UnloadedEntityException : public ClientError
  {
  public:
    UnloadedEntityException(const char*, int, const char* message);
  };

  /*
   * Thrown if a collection is unloaded (see collectionNameLoaded fields)
   * and any method which is expecting a valid collection is called.
   */
  class ODE_CLIENT UnloadedCollectionException : public ClientError
  {
  public:
    UnloadedCollectionException(const char*, int, const char* message);
  };

}

std::ostream& operator<<(std::ostream&, const ode::ClientError&);

#endif // ODE_CLIENT_ERRORS_H
