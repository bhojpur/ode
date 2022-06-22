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

#include <ode/ClientErrors.h>

namespace ode {

  ClientError::ClientError(const char* file, int line, const char* msg) :
    std::exception(), _line(line), _file(file), _msg(msg){}

  ClientError::~ClientError() throw(){}

  const char* ClientError::name() const throw()
  {
    return "ClientError";
  }

  const char* ClientError::file() const throw()
  {
    return _file;
  }

  const char* ClientError::what() const throw()
  {
    return _msg;
  }

  int ClientError::line() const throw()
  {
    return _line;
  }

  UnloadedEntityException::UnloadedEntityException(const char* file, int line, const char* msg) :
    ClientError(file,line,msg){}

  UnloadedCollectionException::UnloadedCollectionException(const char* file, int line, const char* msg) :
    ClientError(file,line,msg){}

}

std::ostream& operator<<(std::ostream& os, const ode::ClientError& ex) 
{
  os << ex.name() << " in File " << ex.file() << ", line " << ex.line();
  os << std::endl << ex.what() << std::endl;
  return os;
}
