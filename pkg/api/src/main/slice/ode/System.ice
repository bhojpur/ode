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

#ifndef ODE_SYSTEM_ICE
#define ODE_SYSTEM_ICE

#include <ode/ModelF.ice>
#include <ode/SystemF.ice>
#include <ode/Collections.ice>

/*
 * The ode::system module combines the ode.system and ode.parameters
 * packages from ODE.server, and represent API arguments which are
 * neither model objects (ode.model.*.ice) nor RTypes (RTypes.ice).
 */
module ode {
  module sys {

    /**
     * Maps the ode.system.EventContext interface. Represents the
     * information known by the server security system about the
     * current user login.
     **/
    class EventContext
    {
      long   shareId;
      long   sessionId;
      string sessionUuid;
      long   userId;
      string userName;
      ode::RLong   sudoerId;
      ode::RString sudoerName;
      long   groupId;
      string groupName;
      bool   isAdmin;
      ode::api::StringSet  adminPrivileges;
      long   eventId;
      string eventType;
      LongList memberOfGroups;
      LongList leaderOfGroups;
      ode::model::Permissions groupPermissions;
    };

    /**
     * Provides common filters which MAY be applied to a
     * query. Check the documentation for the particular
     * method for more information on how these values will
     * be interpreted as well as default values if they
     * are missing. In general they are intended to mean:
     *
     *  unique        := similar to SQL's ""DISTINCT"" keyword
     *
     *  ownerId       := (some) objects queried should belong
     *                   to this user
     *
     *  groupId       := (some) objects queried should belong
     *                   to this group
     *
     *  preferOwner   := true implies if if ownerId and groupId
     *                   are both defined, use only ownerId
     *
     *  offset/limit  := represent a page which should be loaded
     *                   Note: servers may choose to impose a
     *                   maximum limit.
     *
     *  start/endTime := (some) objects queried should have been
     *                   created and/or modified within time span.
     *
     **/
    class Filter
    {
      ode::RBool  unique;
      ode::RLong  ownerId;
      ode::RLong  groupId;
      ode::RInt   offset;
      ode::RInt   limit;
      ode::RTime  startTime;
      ode::RTime  endTime;
      // ode::RBool  preferOwner; Not yet implemented
    };

    /**
     * Similar to Filter, provides common options which MAY be
     * applied on a given method. Check each interface's
     * documentation for more details.
     *
     *  leaves        := whether or not graph leaves (usually images)
     *                   should be loaded
     *
     *  orphan        := whether or not orphaned objects (e.g. datasets
     *                   not in a project) should be loaded
     *
     *  acquisition...:= whether or not acquisitionData (objectives, etc.)
     *                  should be loaded
     *
     * cacheable      := whether or not the query is cacheable by Hibernate
     *                   (use with caution: caching may be counterproductive)
     **/
    class Options
    {
      ode::RBool  leaves;
      ode::RBool  orphan;
      ode::RBool  acquisitionData;
      ["deprecate:experimental: may be wholly removed in next major version"]
      ode::RBool  cacheable;
    };

    /**
     * Holder for all the parameters which can be taken to a query.
     **/
    class Parameters
    {
      /*
       * Contains named arguments which may either be used by
       * a Query implementation or by the method itself for
       * further refinements.
       */
      ParamMap map;
      Filter theFilter;
      Options theOptions;
    };

    /**
     * Principal used for login, etc.
     **/
    class Principal
    {
      string name;
      string group;
      string eventType;
      ode::model::Permissions umask;
    };

    /**
     * Server-constants used for determining particular groups and
     * users.
     **/
    class Roles
    {
      // Root account
      long   rootId;
      string rootName;

      // System group (defines who is an "admin")
      long   systemGroupId;
      string systemGroupName;

      // The group which defines a ""user"". Any user not in the user
      // group is considered inactive.
      long   userGroupId;
      string userGroupName;

      // the guest user
      long   guestId;
      string guestName;

      // ""guest"" group. Can log in and use some methods.
      long   guestGroupId;
      string guestGroupName;
    };

  };
};

#endif // ODE_SYSTEM_ICE