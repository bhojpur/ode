#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

from sys import version_info as _version_info_
import Ice, IcePy
import ode_RTypes_ice
import ode_ModelF_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Start of module ode
__name__ = 'ode'

# Start of module ode.model
__name__ = 'ode.model'

if 'Permissions' not in _M_ode.model.__dict__:
    _M_ode.model.Permissions = Ice.createTempClass()
    class Permissions(Ice.Object):
        """
        Row-level permissions definition available on
        every Bhojpur ODE server type. Represents a similar
        logic to the Unix filesystem.
        Members:
        restrictions -- Restrictions placed on the current object for the current
        user. Indexes into this array are based on constants
        in the {@code ode.constants.permissions} module. If a
        restriction index is not present, then it is safe to
        assume that there is no such restriction.
        If null, this should be assumed to have no restrictions.
        extendedRestrictions -- Further restrictions which are specified by services
        at runtime. Individual service methods will specify
        which strings MAY NOT be present in this field for
        execution to be successful. For example, if an
        ode.model.Image contains a ""DOWNLOAD"" restriction,
        then an attempt to call {@code ode.api.RawFileStore.read}
        will fail with an ode.SecurityViolation.
        perm1 -- Internal representation. May change!
        To make working with this object more straight-forward
        accessors are provided for the perm1 instance though it
        is protected, though NO GUARANTEES are made on the
        representation.
        """
        def __init__(self, _restrictions=None, _extendedRestrictions=None, _perm1=0):
            if Ice.getType(self) == _M_ode.model.Permissions:
                raise RuntimeError('ode.model.Permissions is an abstract class')
            self._restrictions = _restrictions
            self._extendedRestrictions = _extendedRestrictions
            self._perm1 = _perm1

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Permissions')

        def ice_id(self, current=None):
            return '::ode::model::Permissions'

        def ice_staticId():
            return '::ode::model::Permissions'
        ice_staticId = staticmethod(ice_staticId)

        def getPerm1(self, current=None):
            """
            Do not use!
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def setPerm1(self, value, current=None):
            """
            Do not use!
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def isDisallow(self, restriction, current=None):
            """
            The basis for the other canX() methods. If the restriction
            at the given offset in the restriction array is true, then
            this method returns true (otherwise false) and the canX()
            methods return the opposite, i.e.
            isDisallow(ANNOTATERESTRICTION) == ! canAnnotate()
            Arguments:
            restriction -- 
            current -- The Current object for the invocation.
            """
            pass

        def isRestricted(self, restriction, current=None):
            """
            Returns true if the given argument is present in the
            extendedRestrictions set. This implies that some
            service-specific behavior is disallowed.
            Arguments:
            restriction -- 
            current -- The Current object for the invocation.
            """
            pass

        def canAnnotate(self, current=None):
            """
            Whether the current user has permissions
            for annotating this object.
            The fact that the user has this object in hand
            already identifies that it's readable.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def canEdit(self, current=None):
            """
            Whether the current user has the ""edit"" permissions
            for this object. This includes changing the values
            of the object.
            The fact that the user has this object in hand
            already identifies that it's readable.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def canLink(self, current=None):
            """
            Whether the current user has the ""link"" permissions
            for this object. This includes adding it to data graphs.
            The fact that the user has this object in hand
            already identifies that it's readable.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def canDelete(self, current=None):
            """
            Whether the current user has the ""delete"" permissions
            for this object.
            The fact that the user has this object in hand
            already identifies that it's readable.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def canChgrp(self, current=None):
            """
            Whether the current user has the ""chgrp"" permissions
            for this object. This allows them to move it to a different group.
            The fact that the user has this object in hand
            already identifies that it's readable.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def canChown(self, current=None):
            """
            Whether the current user has the ""chown"" permissions
            for this object. This allows them to give it to a different user.
            The fact that the user has this object in hand
            already identifies that it's readable.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def isUserRead(self, current=None):
            pass

        def isUserAnnotate(self, current=None):
            pass

        def isUserWrite(self, current=None):
            pass

        def isGroupRead(self, current=None):
            pass

        def isGroupAnnotate(self, current=None):
            pass

        def isGroupWrite(self, current=None):
            pass

        def isWorldRead(self, current=None):
            pass

        def isWorldAnnotate(self, current=None):
            pass

        def isWorldWrite(self, current=None):
            pass

        def setUserRead(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setUserAnnotate(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setUserWrite(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setGroupRead(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setGroupAnnotate(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setGroupWrite(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setWorldRead(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setWorldAnnotate(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def setWorldWrite(self, value, current=None):
            """
            Throws ode.ClientError if mutation not allowed.
            Arguments:
            value -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Permissions)

        __repr__ = __str__

    _M_ode.model.PermissionsPrx = Ice.createTempClass()
    class PermissionsPrx(Ice.ObjectPrx):

        """
        Do not use!
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getPerm1(self, _ctx=None):
            return _M_ode.model.Permissions._op_getPerm1.invoke(self, ((), _ctx))

        """
        Do not use!
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPerm1(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_getPerm1.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Do not use!
        Arguments:
        """
        def end_getPerm1(self, _r):
            return _M_ode.model.Permissions._op_getPerm1.end(self, _r)

        """
        Do not use!
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setPerm1(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setPerm1.invoke(self, ((value, ), _ctx))

        """
        Do not use!
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setPerm1(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setPerm1.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Do not use!
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setPerm1(self, _r):
            return _M_ode.model.Permissions._op_setPerm1.end(self, _r)

        """
        The basis for the other canX() methods. If the restriction
        at the given offset in the restriction array is true, then
        this method returns true (otherwise false) and the canX()
        methods return the opposite, i.e.
        isDisallow(ANNOTATERESTRICTION) == ! canAnnotate()
        Arguments:
        restriction -- 
        _ctx -- The request context for the invocation.
        """
        def isDisallow(self, restriction, _ctx=None):
            return _M_ode.model.Permissions._op_isDisallow.invoke(self, ((restriction, ), _ctx))

        """
        The basis for the other canX() methods. If the restriction
        at the given offset in the restriction array is true, then
        this method returns true (otherwise false) and the canX()
        methods return the opposite, i.e.
        isDisallow(ANNOTATERESTRICTION) == ! canAnnotate()
        Arguments:
        restriction -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isDisallow(self, restriction, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isDisallow.begin(self, ((restriction, ), _response, _ex, _sent, _ctx))

        """
        The basis for the other canX() methods. If the restriction
        at the given offset in the restriction array is true, then
        this method returns true (otherwise false) and the canX()
        methods return the opposite, i.e.
        isDisallow(ANNOTATERESTRICTION) == ! canAnnotate()
        Arguments:
        restriction -- 
        """
        def end_isDisallow(self, _r):
            return _M_ode.model.Permissions._op_isDisallow.end(self, _r)

        """
        Returns true if the given argument is present in the
        extendedRestrictions set. This implies that some
        service-specific behavior is disallowed.
        Arguments:
        restriction -- 
        _ctx -- The request context for the invocation.
        """
        def isRestricted(self, restriction, _ctx=None):
            return _M_ode.model.Permissions._op_isRestricted.invoke(self, ((restriction, ), _ctx))

        """
        Returns true if the given argument is present in the
        extendedRestrictions set. This implies that some
        service-specific behavior is disallowed.
        Arguments:
        restriction -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isRestricted(self, restriction, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isRestricted.begin(self, ((restriction, ), _response, _ex, _sent, _ctx))

        """
        Returns true if the given argument is present in the
        extendedRestrictions set. This implies that some
        service-specific behavior is disallowed.
        Arguments:
        restriction -- 
        """
        def end_isRestricted(self, _r):
            return _M_ode.model.Permissions._op_isRestricted.end(self, _r)

        """
        Whether the current user has permissions
        for annotating this object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def canAnnotate(self, _ctx=None):
            return _M_ode.model.Permissions._op_canAnnotate.invoke(self, ((), _ctx))

        """
        Whether the current user has permissions
        for annotating this object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canAnnotate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_canAnnotate.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Whether the current user has permissions
        for annotating this object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        """
        def end_canAnnotate(self, _r):
            return _M_ode.model.Permissions._op_canAnnotate.end(self, _r)

        """
        Whether the current user has the ""edit"" permissions
        for this object. This includes changing the values
        of the object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def canEdit(self, _ctx=None):
            return _M_ode.model.Permissions._op_canEdit.invoke(self, ((), _ctx))

        """
        Whether the current user has the ""edit"" permissions
        for this object. This includes changing the values
        of the object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canEdit(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_canEdit.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Whether the current user has the ""edit"" permissions
        for this object. This includes changing the values
        of the object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        """
        def end_canEdit(self, _r):
            return _M_ode.model.Permissions._op_canEdit.end(self, _r)

        """
        Whether the current user has the ""link"" permissions
        for this object. This includes adding it to data graphs.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def canLink(self, _ctx=None):
            return _M_ode.model.Permissions._op_canLink.invoke(self, ((), _ctx))

        """
        Whether the current user has the ""link"" permissions
        for this object. This includes adding it to data graphs.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_canLink.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Whether the current user has the ""link"" permissions
        for this object. This includes adding it to data graphs.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        """
        def end_canLink(self, _r):
            return _M_ode.model.Permissions._op_canLink.end(self, _r)

        """
        Whether the current user has the ""delete"" permissions
        for this object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def canDelete(self, _ctx=None):
            return _M_ode.model.Permissions._op_canDelete.invoke(self, ((), _ctx))

        """
        Whether the current user has the ""delete"" permissions
        for this object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canDelete(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_canDelete.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Whether the current user has the ""delete"" permissions
        for this object.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        """
        def end_canDelete(self, _r):
            return _M_ode.model.Permissions._op_canDelete.end(self, _r)

        """
        Whether the current user has the ""chgrp"" permissions
        for this object. This allows them to move it to a different group.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def canChgrp(self, _ctx=None):
            return _M_ode.model.Permissions._op_canChgrp.invoke(self, ((), _ctx))

        """
        Whether the current user has the ""chgrp"" permissions
        for this object. This allows them to move it to a different group.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canChgrp(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_canChgrp.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Whether the current user has the ""chgrp"" permissions
        for this object. This allows them to move it to a different group.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        """
        def end_canChgrp(self, _r):
            return _M_ode.model.Permissions._op_canChgrp.end(self, _r)

        """
        Whether the current user has the ""chown"" permissions
        for this object. This allows them to give it to a different user.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def canChown(self, _ctx=None):
            return _M_ode.model.Permissions._op_canChown.invoke(self, ((), _ctx))

        """
        Whether the current user has the ""chown"" permissions
        for this object. This allows them to give it to a different user.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_canChown(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_canChown.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Whether the current user has the ""chown"" permissions
        for this object. This allows them to give it to a different user.
        The fact that the user has this object in hand
        already identifies that it's readable.
        Arguments:
        """
        def end_canChown(self, _r):
            return _M_ode.model.Permissions._op_canChown.end(self, _r)

        def isUserRead(self, _ctx=None):
            return _M_ode.model.Permissions._op_isUserRead.invoke(self, ((), _ctx))

        def begin_isUserRead(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isUserRead.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isUserRead(self, _r):
            return _M_ode.model.Permissions._op_isUserRead.end(self, _r)

        def isUserAnnotate(self, _ctx=None):
            return _M_ode.model.Permissions._op_isUserAnnotate.invoke(self, ((), _ctx))

        def begin_isUserAnnotate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isUserAnnotate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isUserAnnotate(self, _r):
            return _M_ode.model.Permissions._op_isUserAnnotate.end(self, _r)

        def isUserWrite(self, _ctx=None):
            return _M_ode.model.Permissions._op_isUserWrite.invoke(self, ((), _ctx))

        def begin_isUserWrite(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isUserWrite.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isUserWrite(self, _r):
            return _M_ode.model.Permissions._op_isUserWrite.end(self, _r)

        def isGroupRead(self, _ctx=None):
            return _M_ode.model.Permissions._op_isGroupRead.invoke(self, ((), _ctx))

        def begin_isGroupRead(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isGroupRead.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isGroupRead(self, _r):
            return _M_ode.model.Permissions._op_isGroupRead.end(self, _r)

        def isGroupAnnotate(self, _ctx=None):
            return _M_ode.model.Permissions._op_isGroupAnnotate.invoke(self, ((), _ctx))

        def begin_isGroupAnnotate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isGroupAnnotate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isGroupAnnotate(self, _r):
            return _M_ode.model.Permissions._op_isGroupAnnotate.end(self, _r)

        def isGroupWrite(self, _ctx=None):
            return _M_ode.model.Permissions._op_isGroupWrite.invoke(self, ((), _ctx))

        def begin_isGroupWrite(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isGroupWrite.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isGroupWrite(self, _r):
            return _M_ode.model.Permissions._op_isGroupWrite.end(self, _r)

        def isWorldRead(self, _ctx=None):
            return _M_ode.model.Permissions._op_isWorldRead.invoke(self, ((), _ctx))

        def begin_isWorldRead(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isWorldRead.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isWorldRead(self, _r):
            return _M_ode.model.Permissions._op_isWorldRead.end(self, _r)

        def isWorldAnnotate(self, _ctx=None):
            return _M_ode.model.Permissions._op_isWorldAnnotate.invoke(self, ((), _ctx))

        def begin_isWorldAnnotate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isWorldAnnotate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isWorldAnnotate(self, _r):
            return _M_ode.model.Permissions._op_isWorldAnnotate.end(self, _r)

        def isWorldWrite(self, _ctx=None):
            return _M_ode.model.Permissions._op_isWorldWrite.invoke(self, ((), _ctx))

        def begin_isWorldWrite(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_isWorldWrite.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_isWorldWrite(self, _r):
            return _M_ode.model.Permissions._op_isWorldWrite.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setUserRead(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setUserRead.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setUserRead(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setUserRead.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setUserRead(self, _r):
            return _M_ode.model.Permissions._op_setUserRead.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setUserAnnotate(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setUserAnnotate.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setUserAnnotate(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setUserAnnotate.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setUserAnnotate(self, _r):
            return _M_ode.model.Permissions._op_setUserAnnotate.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setUserWrite(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setUserWrite.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setUserWrite(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setUserWrite.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setUserWrite(self, _r):
            return _M_ode.model.Permissions._op_setUserWrite.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setGroupRead(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setGroupRead.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setGroupRead(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setGroupRead.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setGroupRead(self, _r):
            return _M_ode.model.Permissions._op_setGroupRead.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setGroupAnnotate(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setGroupAnnotate.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setGroupAnnotate(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setGroupAnnotate.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setGroupAnnotate(self, _r):
            return _M_ode.model.Permissions._op_setGroupAnnotate.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setGroupWrite(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setGroupWrite.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setGroupWrite(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setGroupWrite.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setGroupWrite(self, _r):
            return _M_ode.model.Permissions._op_setGroupWrite.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setWorldRead(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setWorldRead.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setWorldRead(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setWorldRead.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setWorldRead(self, _r):
            return _M_ode.model.Permissions._op_setWorldRead.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setWorldAnnotate(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setWorldAnnotate.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setWorldAnnotate(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setWorldAnnotate.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setWorldAnnotate(self, _r):
            return _M_ode.model.Permissions._op_setWorldAnnotate.end(self, _r)

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _ctx -- The request context for the invocation.
        """
        def setWorldWrite(self, value, _ctx=None):
            return _M_ode.model.Permissions._op_setWorldWrite.invoke(self, ((value, ), _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setWorldWrite(self, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Permissions._op_setWorldWrite.begin(self, ((value, ), _response, _ex, _sent, _ctx))

        """
        Throws ode.ClientError if mutation not allowed.
        Arguments:
        value -- 
        """
        def end_setWorldWrite(self, _r):
            return _M_ode.model.Permissions._op_setWorldWrite.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PermissionsPrx.ice_checkedCast(proxy, '::ode::model::Permissions', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PermissionsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Permissions'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PermissionsPrx = IcePy.defineProxy('::ode::model::Permissions', PermissionsPrx)

    _M_ode.model._t_Permissions = IcePy.defineClass('::ode::model::Permissions', Permissions, -1, (), True, False, None, (), (
        ('_restrictions', (), _M_ode.api._t_BoolArray, False, 0),
        ('_extendedRestrictions', (), _M_ode.api._t_StringSet, False, 0),
        ('_perm1', (), IcePy._t_long, False, 0)
    ))
    Permissions._ice_type = _M_ode.model._t_Permissions

    Permissions._op_getPerm1 = IcePy.Operation('getPerm1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_long, False, 0), ())
    Permissions._op_setPerm1 = IcePy.Operation('setPerm1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_long, False, 0),), (), None, ())
    Permissions._op_isDisallow = IcePy.Operation('isDisallow', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isRestricted = IcePy.Operation('isRestricted', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_canAnnotate = IcePy.Operation('canAnnotate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_canEdit = IcePy.Operation('canEdit', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_canLink = IcePy.Operation('canLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_canDelete = IcePy.Operation('canDelete', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_canChgrp = IcePy.Operation('canChgrp', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_canChown = IcePy.Operation('canChown', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isUserRead = IcePy.Operation('isUserRead', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isUserAnnotate = IcePy.Operation('isUserAnnotate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isUserWrite = IcePy.Operation('isUserWrite', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isGroupRead = IcePy.Operation('isGroupRead', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isGroupAnnotate = IcePy.Operation('isGroupAnnotate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isGroupWrite = IcePy.Operation('isGroupWrite', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isWorldRead = IcePy.Operation('isWorldRead', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isWorldAnnotate = IcePy.Operation('isWorldAnnotate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_isWorldWrite = IcePy.Operation('isWorldWrite', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Permissions._op_setUserRead = IcePy.Operation('setUserRead', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setUserAnnotate = IcePy.Operation('setUserAnnotate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setUserWrite = IcePy.Operation('setUserWrite', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setGroupRead = IcePy.Operation('setGroupRead', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setGroupAnnotate = IcePy.Operation('setGroupAnnotate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setGroupWrite = IcePy.Operation('setGroupWrite', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setWorldRead = IcePy.Operation('setWorldRead', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setWorldAnnotate = IcePy.Operation('setWorldAnnotate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    Permissions._op_setWorldWrite = IcePy.Operation('setWorldWrite', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())

    _M_ode.model.Permissions = Permissions
    del Permissions

    _M_ode.model.PermissionsPrx = PermissionsPrx
    del PermissionsPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
