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

#ifndef CLASS_PERMISSIONS
#define CLASS_PERMISSIONS

#include <ode/RTypes.ice>
#include <ode/ModelF.ice>
#include <ode/Collections.ice>

module ode {

    module model {

      /**
       * Row-level permissions definition available on
       * every ODE.server type. Represents a similar
       * logic to the Unix filesystem.
       */
    ["protected"] class Permissions
    {

      /**
       * Restrictions placed on the current object for the current
       * user. Indexes into this array are based on constants
       * in the {@code ode.constants.permissions} module. If a
       * restriction index is not present, then it is safe to
       * assume that there is no such restriction.
       * If null, this should be assumed to have no restrictions.
       */
      ode::api::BoolArray restrictions;

      /**
       * Further restrictions which are specified by services
       * at runtime. Individual service methods will specify
       * which strings MAY NOT be present in this field for
       * execution to be successful. For example, if an
       * {@link ode.model.Image} contains a ""DOWNLOAD"" restriction,
       * then an attempt to call {@code ode.api.RawFileStore.read}
       * will fail with an {@link ode.SecurityViolation}.
       */
      ode::api::StringSet extendedRestrictions;

      /**
       * Internal representation. May change!
       * To make working with this object more straight-forward
       * accessors are provided for the perm1 instance though it
       * is protected, though NO GUARANTEES are made on the
       * representation.
       */
      long perm1;

      /**
       * Do not use!
       */
      long getPerm1();

      /**
       * Do not use!
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setPerm1(long value);

      // Context-based values
      //======================================================

      /**
       * The basis for the other canX() methods. If the restriction
       * at the given offset in the restriction array is true, then
       * this method returns true (otherwise false) and the canX()
       * methods return the opposite, i.e.
       *
       * isDisallow(ANNOTATERESTRICTION) == ! canAnnotate()
       *
       */
       bool isDisallow(int restriction);

      /**
       * Returns true if the given argument is present in the
       * extendedRestrictions set. This implies that some
       * service-specific behavior is disallowed.
       */
       bool isRestricted(string restriction);

      /**
       * Whether the current user has permissions
       * for annotating this object.
       *
       * The fact that the user has this object in hand
       * already identifies that it's readable.
       */
      bool canAnnotate();

      /**
       * Whether the current user has the ""edit"" permissions
       * for this object. This includes changing the values
       * of the object.
       *
       * The fact that the user has this object in hand
       * already identifies that it's readable.
       */
      bool canEdit();

      /**
       * Whether the current user has the ""link"" permissions
       * for this object. This includes adding it to data graphs.
       *
       * The fact that the user has this object in hand
       * already identifies that it's readable.
       */
      bool canLink();

      /**
       * Whether the current user has the ""delete"" permissions
       * for this object.
       *
       * The fact that the user has this object in hand
       * already identifies that it's readable.
       */
      bool canDelete();

      /**
       * Whether the current user has the ""chgrp"" permissions
       * for this object. This allows them to move it to a different group.
       *
       * The fact that the user has this object in hand
       * already identifies that it's readable.
       */
      bool canChgrp();

      /**
       * Whether the current user has the ""chown"" permissions
       * for this object. This allows them to give it to a different user.
       *
       * The fact that the user has this object in hand
       * already identifies that it's readable.
       */
      bool canChown();

      // Row-based values
      //======================================================

      bool isUserRead();
      bool isUserAnnotate();
      bool isUserWrite();
      bool isGroupRead();
      bool isGroupAnnotate();
      bool isGroupWrite();
      bool isWorldRead();
      bool isWorldAnnotate();
      bool isWorldWrite();

      // Mutators
      //======================================================
      // Note: unless you create the permissions object
      // yourself, mutating the state of the object will
      // throw a ClientError

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setUserRead(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setUserAnnotate(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setUserWrite(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setGroupRead(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setGroupAnnotate(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setGroupWrite(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setWorldRead(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setWorldAnnotate(bool value);

      /**
       * Throws {@link ode.ClientError} if mutation not allowed.
       */
      void setWorldWrite(bool value);

    };
  };
};
#endif