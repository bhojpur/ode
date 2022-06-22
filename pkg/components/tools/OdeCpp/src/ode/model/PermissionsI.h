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

#ifndef PERMISSIONSI_H
#define PERMISSIONSI_H

#include <ode/IceNoWarnPush.h>
#include <ode/model/Permissions.h>
#include <ode/IceNoWarnPop.h>
#include <Ice/Config.h>
#include <IceUtil/Config.h>
#include <Ice/Handle.h>
#include <iostream>
#include <string>
#include <vector>

#ifndef ODE_CLIENT
#   ifdef ODE_CLIENT_EXPORTS
#       define ODE_CLIENT ICE_DECLSPEC_EXPORT
#   else
#       define ODE_CLIENT ICE_DECLSPEC_IMPORT
#   endif
#endif

namespace ode {
  namespace model {
    class PermissionsI;
  }
}

namespace IceInternal {
  ODE_CLIENT ::Ice::Object* upCast(::ode::model::PermissionsI*);
}

namespace ode {
  namespace model {

  /*
   * Server wrapper for the permissions related to
   * an entity. Though the internal state is made
   * public (see http://www.zeroc.com/forums/showthread.php?t=3084)
   * it is only intended for clients to use the methods:
   *
   *  -- [is|set][User|Group|World][Read|Write]()
   *  -- [is|set]Locked
   */

  typedef IceInternal::Handle<PermissionsI> PermissionsIPtr;

class ODE_CLIENT PermissionsI : virtual public Permissions {

protected:
    virtual ~PermissionsI(); // protected as outlined in Ice docs.
    bool granted(int mask, int shift);
    void set(int mask, int shift, bool value);
    void throwIfImmutable();
    bool __immutable;
public:

    PermissionsI(const std::string& perms = "");
    virtual void ice_postUnmarshal(); // For setting __immutable

    virtual bool isRestricted(const std::string& restriction, const Ice::Current& current = Ice::Current());
    virtual bool isDisallow(::Ice::Int restriction, const Ice::Current& current = Ice::Current());
    virtual bool canAnnotate(const Ice::Current& current = Ice::Current());
    virtual bool canDelete(const Ice::Current& current = Ice::Current());
    virtual bool canEdit(const Ice::Current& current = Ice::Current());
    virtual bool canLink(const Ice::Current& current = Ice::Current());
    virtual bool canChgrp(const Ice::Current& current = Ice::Current());
    virtual bool canChown(const Ice::Current& current = Ice::Current());

    /*
     * Central methods. The optional argument is a requirement
     * of the Ice runtime and can safely be omitted.
     */
    virtual bool isUserRead(const Ice::Current& c = Ice::Current());
    virtual bool isUserAnnotate(const Ice::Current& c = Ice::Current());
    virtual bool isUserWrite(const Ice::Current& c = Ice::Current());
    virtual bool isGroupRead(const Ice::Current& c = Ice::Current());
    virtual bool isGroupAnnotate(const Ice::Current& c = Ice::Current());
    virtual bool isGroupWrite(const Ice::Current& c = Ice::Current());
    virtual bool isWorldRead(const Ice::Current& c = Ice::Current());
    virtual bool isWorldAnnotate(const Ice::Current& c = Ice::Current());
    virtual bool isWorldWrite(const Ice::Current& c = Ice::Current());
    virtual void setUserRead(bool value, const Ice::Current& c = Ice::Current());
    virtual void setUserAnnotate(bool value, const Ice::Current& c = Ice::Current());
    virtual void setUserWrite(bool value, const Ice::Current& c = Ice::Current());
    virtual void setGroupRead(bool value, const Ice::Current& c = Ice::Current());
    virtual void setGroupAnnotate(bool value, const Ice::Current& c = Ice::Current());
    virtual void setGroupWrite(bool value, const Ice::Current& c = Ice::Current());
    virtual void setWorldRead(bool value, const Ice::Current& c = Ice::Current());
    virtual void setWorldAnnotate(bool value, const Ice::Current& c = Ice::Current());
    virtual void setWorldWrite(bool value, const Ice::Current& c = Ice::Current());

    // Do not use !
    virtual Ice::Long getPerm1(const Ice::Current& current = Ice::Current());

    // Do not use !
    virtual void setPerm1(Ice::Long _perm1, const Ice::Current& current = Ice::Current());

    // Meaningless for Permissions. No complex state.
    virtual void unload(const Ice::Current& c = Ice::Current());

  };

 }
}
#endif // PERMISSIONSI_H
