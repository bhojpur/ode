/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef CLASS_DETAILS
#define CLASS_DETAILS

#include <ode/ModelF.ice>
#include <ode/System.ice>
#include <Ice/Current.ice>

module ode {

  module model {

    /**
     * Embedded component of every ODE.server type. Since this is
     * not an IObject subclass, no attempt is made to hide the state
     * of this object, since it cannot be ""unloaded"".
     */
    ["protected"] class Details
    {

      //  ode.model.meta.Experimenter owner;
      ode::model::Experimenter owner;
      ode::model::Experimenter getOwner();
      void setOwner(ode::model::Experimenter theOwner);

      //  ode.model.meta.ExperimenterGroup group;
      ode::model::ExperimenterGroup group;
      ode::model::ExperimenterGroup getGroup();
      void setGroup(ode::model::ExperimenterGroup theGroup);

      //  ode.model.meta.Event creationEvent;
      ode::model::Event creationEvent;
      ode::model::Event getCreationEvent();
      void setCreationEvent(ode::model::Event theCreationEvent);

      //  ode.model.meta.Event updateEvent;
      ode::model::Event updateEvent;
      ode::model::Event getUpdateEvent();
      void setUpdateEvent(ode::model::Event theUpdateEvent);

      //  ode.model.internal.Permissions permissions;
      ode::model::Permissions permissions;
      ode::model::Permissions getPermissions();
      void setPermissions(ode::model::Permissions thePermissions);

      //  ode.model.meta.ExternalInfo externalInfo;
      ode::model::ExternalInfo externalInfo;
      ode::model::ExternalInfo getExternalInfo();
      void setExternalInfo(ode::model::ExternalInfo theExternalInfo);

      //
      // Context parameters
      //

      /**
       * Context which was active during the call which
       * returned this object. This context is set as
       * the last (optional) argument of any remote
       * Ice invocation. This is used to change the
       * user, group, share, etc. of the current session.
       */
      Ice::Context call;

      /**
       * Context which would have been returned by a
       * simultaneous call to {@code ode.api.IAdmin.getEventContext}
       * while this object was being loaded.
       */
      ode::sys::EventContext event;

    };

  };

};
#endif