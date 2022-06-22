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
import ode_cmd_API_ice
import ode_Collections_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Start of module ode
__name__ = 'ode'

# Start of module ode.cmd
__name__ = 'ode.cmd'

# Start of module ode.cmd.graphs
_M_ode.cmd.graphs = Ice.openModule('ode.cmd.graphs')
__name__ = 'ode.cmd.graphs'
_M_ode.cmd.graphs.__doc__ = """
Options that modify GraphModify2 request execution.
By default, a user's related ""orphaned"" objects are typically
included in a request's operation. These options override that
behavior, allowing the client to specify whether to always or
never include given kinds of child object regardless of if they
are orphans.
For annotations, each override is limited to specific annotation
namespaces. (If no namespaces are specified, defaults apply
according to the configuration of the graph request factory.)
"""

if 'ChildOption' not in _M_ode.cmd.graphs.__dict__:
    _M_ode.cmd.graphs.ChildOption = Ice.createTempClass()
    class ChildOption(Ice.Object):
        """
        How GraphModify2 requests should deal with kinds of children,
        related to the target objects.
        By default, it is usual for only orphans to be operated on.
        At least one of includeType or excludeType must be used;
        if a type matches both, then it is included.
        No more than one of includeNs and excludeNs may be used.
        Members:
        includeType -- Include in the operation all children of these types.
        excludeType -- Include in the operation no children of these types.
        includeNs -- For annotations, limit the applicability of this option
        to only those in these namespaces.
        excludeNs -- For annotations, limit the applicability of this option
        to only those not in these namespaces.
        """
        def __init__(self, includeType=None, excludeType=None, includeNs=None, excludeNs=None):
            self.includeType = includeType
            self.excludeType = excludeType
            self.includeNs = includeNs
            self.excludeNs = excludeNs

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::graphs::ChildOption')

        def ice_id(self, current=None):
            return '::ode::cmd::graphs::ChildOption'

        def ice_staticId():
            return '::ode::cmd::graphs::ChildOption'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd.graphs._t_ChildOption)

        __repr__ = __str__

    _M_ode.cmd.graphs.ChildOptionPrx = Ice.createTempClass()
    class ChildOptionPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.graphs.ChildOptionPrx.ice_checkedCast(proxy, '::ode::cmd::graphs::ChildOption', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.graphs.ChildOptionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::graphs::ChildOption'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd.graphs._t_ChildOptionPrx = IcePy.defineProxy('::ode::cmd::graphs::ChildOption', ChildOptionPrx)

    _M_ode.cmd.graphs._t_ChildOption = IcePy.defineClass('::ode::cmd::graphs::ChildOption', ChildOption, -1, (), False, False, None, (), (
        ('includeType', (), _M_ode.api._t_StringSet, False, 0),
        ('excludeType', (), _M_ode.api._t_StringSet, False, 0),
        ('includeNs', (), _M_ode.api._t_StringSet, False, 0),
        ('excludeNs', (), _M_ode.api._t_StringSet, False, 0)
    ))
    ChildOption._ice_type = _M_ode.cmd.graphs._t_ChildOption

    _M_ode.cmd.graphs.ChildOption = ChildOption
    del ChildOption

    _M_ode.cmd.graphs.ChildOptionPrx = ChildOptionPrx
    del ChildOptionPrx

if '_t_ChildOptions' not in _M_ode.cmd.graphs.__dict__:
    _M_ode.cmd.graphs._t_ChildOptions = IcePy.defineSequence('::ode::cmd::graphs::ChildOptions', (), _M_ode.cmd.graphs._t_ChildOption)

# End of module ode.cmd.graphs

__name__ = 'ode.cmd'

if 'GraphQuery' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.GraphQuery = Ice.createTempClass()
    class GraphQuery(_M_ode.cmd.Request):
        """
        Base class for new requests for reading the model object graph.
        Members:
        targetObjects -- The model objects upon which to operate.
        Related model objects may also be targeted.
        """
        def __init__(self, targetObjects=None):
            _M_ode.cmd.Request.__init__(self)
            self.targetObjects = targetObjects

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::GraphQuery'

        def ice_staticId():
            return '::ode::cmd::GraphQuery'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_GraphQuery)

        __repr__ = __str__

    _M_ode.cmd.GraphQueryPrx = Ice.createTempClass()
    class GraphQueryPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.GraphQueryPrx.ice_checkedCast(proxy, '::ode::cmd::GraphQuery', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.GraphQueryPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::GraphQuery'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_GraphQueryPrx = IcePy.defineProxy('::ode::cmd::GraphQuery', GraphQueryPrx)

    _M_ode.cmd._t_GraphQuery = IcePy.defineClass('::ode::cmd::GraphQuery', GraphQuery, -1, (), False, False, _M_ode.cmd._t_Request, (), (('targetObjects', (), _M_ode.api._t_StringLongListMap, False, 0),))
    GraphQuery._ice_type = _M_ode.cmd._t_GraphQuery

    _M_ode.cmd.GraphQuery = GraphQuery
    del GraphQuery

    _M_ode.cmd.GraphQueryPrx = GraphQueryPrx
    del GraphQueryPrx

if 'GraphModify2' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.GraphModify2 = Ice.createTempClass()
    class GraphModify2(_M_ode.cmd.GraphQuery):
        """
        Base class for new requests for modifying the model object graph.
        Members:
        childOptions -- If the request should operate on specific kinds of children.
        Only the first applicable option takes effect.
        dryRun -- If this request should skip the actual model object updates.
        The response is still as if the operation actually occurred,
        indicating what would have been done to which objects.
        """
        def __init__(self, targetObjects=None, childOptions=None, dryRun=False):
            _M_ode.cmd.GraphQuery.__init__(self, targetObjects)
            self.childOptions = childOptions
            self.dryRun = dryRun

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::GraphModify2', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::GraphModify2'

        def ice_staticId():
            return '::ode::cmd::GraphModify2'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_GraphModify2)

        __repr__ = __str__

    _M_ode.cmd.GraphModify2Prx = Ice.createTempClass()
    class GraphModify2Prx(_M_ode.cmd.GraphQueryPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.GraphModify2Prx.ice_checkedCast(proxy, '::ode::cmd::GraphModify2', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.GraphModify2Prx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::GraphModify2'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_GraphModify2Prx = IcePy.defineProxy('::ode::cmd::GraphModify2', GraphModify2Prx)

    _M_ode.cmd._t_GraphModify2 = IcePy.declareClass('::ode::cmd::GraphModify2')

    _M_ode.cmd._t_GraphModify2 = IcePy.defineClass('::ode::cmd::GraphModify2', GraphModify2, -1, (), False, False, _M_ode.cmd._t_GraphQuery, (), (
        ('childOptions', (), _M_ode.cmd.graphs._t_ChildOptions, False, 0),
        ('dryRun', (), IcePy._t_bool, False, 0)
    ))
    GraphModify2._ice_type = _M_ode.cmd._t_GraphModify2

    _M_ode.cmd.GraphModify2 = GraphModify2
    del GraphModify2

    _M_ode.cmd.GraphModify2Prx = GraphModify2Prx
    del GraphModify2Prx

if 'Chgrp2' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Chgrp2 = Ice.createTempClass()
    class Chgrp2(_M_ode.cmd.GraphModify2):
        """
        Move model objects into a different experimenter group.
        The user must be either an administrator,
        or the owner of the objects and a member of the target group.
        Members:
        groupId -- The ID of the experimenter group into which to move the model
        objects.
        """
        def __init__(self, targetObjects=None, childOptions=None, dryRun=False, groupId=0):
            _M_ode.cmd.GraphModify2.__init__(self, targetObjects, childOptions, dryRun)
            self.groupId = groupId

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Chgrp2', '::ode::cmd::GraphModify2', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::Chgrp2'

        def ice_staticId():
            return '::ode::cmd::Chgrp2'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Chgrp2)

        __repr__ = __str__

    _M_ode.cmd.Chgrp2Prx = Ice.createTempClass()
    class Chgrp2Prx(_M_ode.cmd.GraphModify2Prx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Chgrp2Prx.ice_checkedCast(proxy, '::ode::cmd::Chgrp2', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Chgrp2Prx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Chgrp2'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Chgrp2Prx = IcePy.defineProxy('::ode::cmd::Chgrp2', Chgrp2Prx)

    _M_ode.cmd._t_Chgrp2 = IcePy.declareClass('::ode::cmd::Chgrp2')

    _M_ode.cmd._t_Chgrp2 = IcePy.defineClass('::ode::cmd::Chgrp2', Chgrp2, -1, (), False, False, _M_ode.cmd._t_GraphModify2, (), (('groupId', (), IcePy._t_long, False, 0),))
    Chgrp2._ice_type = _M_ode.cmd._t_Chgrp2

    _M_ode.cmd.Chgrp2 = Chgrp2
    del Chgrp2

    _M_ode.cmd.Chgrp2Prx = Chgrp2Prx
    del Chgrp2Prx

if 'Chgrp2Response' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Chgrp2Response = Ice.createTempClass()
    class Chgrp2Response(_M_ode.cmd.OK):
        """
        Result of moving model objects into a different experimenter
        group.
        Members:
        includedObjects -- The model objects that were moved.
        deletedObjects -- The model objects that were deleted.
        """
        def __init__(self, includedObjects=None, deletedObjects=None):
            _M_ode.cmd.OK.__init__(self)
            self.includedObjects = includedObjects
            self.deletedObjects = deletedObjects

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Chgrp2Response', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::Chgrp2Response'

        def ice_staticId():
            return '::ode::cmd::Chgrp2Response'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Chgrp2Response)

        __repr__ = __str__

    _M_ode.cmd.Chgrp2ResponsePrx = Ice.createTempClass()
    class Chgrp2ResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Chgrp2ResponsePrx.ice_checkedCast(proxy, '::ode::cmd::Chgrp2Response', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Chgrp2ResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Chgrp2Response'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Chgrp2ResponsePrx = IcePy.defineProxy('::ode::cmd::Chgrp2Response', Chgrp2ResponsePrx)

    _M_ode.cmd._t_Chgrp2Response = IcePy.defineClass('::ode::cmd::Chgrp2Response', Chgrp2Response, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('includedObjects', (), _M_ode.api._t_StringLongListMap, False, 0),
        ('deletedObjects', (), _M_ode.api._t_StringLongListMap, False, 0)
    ))
    Chgrp2Response._ice_type = _M_ode.cmd._t_Chgrp2Response

    _M_ode.cmd.Chgrp2Response = Chgrp2Response
    del Chgrp2Response

    _M_ode.cmd.Chgrp2ResponsePrx = Chgrp2ResponsePrx
    del Chgrp2ResponsePrx

if 'Chmod2' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Chmod2 = Ice.createTempClass()
    class Chmod2(_M_ode.cmd.GraphModify2):
        """
        Change the permissions on model objects.
        The user must be an administrator, the owner of the objects,
        or an owner of the objects' group.
        The only permitted target object type is ExperimenterGroup.
        Members:
        permissions -- The permissions to set on the model objects.
        """
        def __init__(self, targetObjects=None, childOptions=None, dryRun=False, permissions=''):
            _M_ode.cmd.GraphModify2.__init__(self, targetObjects, childOptions, dryRun)
            self.permissions = permissions

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Chmod2', '::ode::cmd::GraphModify2', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::Chmod2'

        def ice_staticId():
            return '::ode::cmd::Chmod2'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Chmod2)

        __repr__ = __str__

    _M_ode.cmd.Chmod2Prx = Ice.createTempClass()
    class Chmod2Prx(_M_ode.cmd.GraphModify2Prx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Chmod2Prx.ice_checkedCast(proxy, '::ode::cmd::Chmod2', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Chmod2Prx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Chmod2'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Chmod2Prx = IcePy.defineProxy('::ode::cmd::Chmod2', Chmod2Prx)

    _M_ode.cmd._t_Chmod2 = IcePy.declareClass('::ode::cmd::Chmod2')

    _M_ode.cmd._t_Chmod2 = IcePy.defineClass('::ode::cmd::Chmod2', Chmod2, -1, (), False, False, _M_ode.cmd._t_GraphModify2, (), (('permissions', (), IcePy._t_string, False, 0),))
    Chmod2._ice_type = _M_ode.cmd._t_Chmod2

    _M_ode.cmd.Chmod2 = Chmod2
    del Chmod2

    _M_ode.cmd.Chmod2Prx = Chmod2Prx
    del Chmod2Prx

if 'Chmod2Response' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Chmod2Response = Ice.createTempClass()
    class Chmod2Response(_M_ode.cmd.OK):
        """
        Result of changing the permissions on model objects.
        Members:
        includedObjects -- The model objects with changed permissions.
        deletedObjects -- The model objects that were deleted.
        """
        def __init__(self, includedObjects=None, deletedObjects=None):
            _M_ode.cmd.OK.__init__(self)
            self.includedObjects = includedObjects
            self.deletedObjects = deletedObjects

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Chmod2Response', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::Chmod2Response'

        def ice_staticId():
            return '::ode::cmd::Chmod2Response'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Chmod2Response)

        __repr__ = __str__

    _M_ode.cmd.Chmod2ResponsePrx = Ice.createTempClass()
    class Chmod2ResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Chmod2ResponsePrx.ice_checkedCast(proxy, '::ode::cmd::Chmod2Response', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Chmod2ResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Chmod2Response'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Chmod2ResponsePrx = IcePy.defineProxy('::ode::cmd::Chmod2Response', Chmod2ResponsePrx)

    _M_ode.cmd._t_Chmod2Response = IcePy.defineClass('::ode::cmd::Chmod2Response', Chmod2Response, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('includedObjects', (), _M_ode.api._t_StringLongListMap, False, 0),
        ('deletedObjects', (), _M_ode.api._t_StringLongListMap, False, 0)
    ))
    Chmod2Response._ice_type = _M_ode.cmd._t_Chmod2Response

    _M_ode.cmd.Chmod2Response = Chmod2Response
    del Chmod2Response

    _M_ode.cmd.Chmod2ResponsePrx = Chmod2ResponsePrx
    del Chmod2ResponsePrx

if 'Chown2' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Chown2 = Ice.createTempClass()
    class Chown2(_M_ode.cmd.GraphModify2):
        """
        Change the ownership of model objects.
        The user must be an administrator, or they
        must be an owner of the objects' group, with
        the target user a member of the objects' group.
        Members:
        userId -- The ID of the experimenter to which to give the model
        objects.
        targetUsers -- The users who should have all their data targeted.
        """
        def __init__(self, targetObjects=None, childOptions=None, dryRun=False, userId=0, targetUsers=None):
            _M_ode.cmd.GraphModify2.__init__(self, targetObjects, childOptions, dryRun)
            self.userId = userId
            self.targetUsers = targetUsers

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Chown2', '::ode::cmd::GraphModify2', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::Chown2'

        def ice_staticId():
            return '::ode::cmd::Chown2'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Chown2)

        __repr__ = __str__

    _M_ode.cmd.Chown2Prx = Ice.createTempClass()
    class Chown2Prx(_M_ode.cmd.GraphModify2Prx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Chown2Prx.ice_checkedCast(proxy, '::ode::cmd::Chown2', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Chown2Prx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Chown2'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Chown2Prx = IcePy.defineProxy('::ode::cmd::Chown2', Chown2Prx)

    _M_ode.cmd._t_Chown2 = IcePy.declareClass('::ode::cmd::Chown2')

    _M_ode.cmd._t_Chown2 = IcePy.defineClass('::ode::cmd::Chown2', Chown2, -1, (), False, False, _M_ode.cmd._t_GraphModify2, (), (
        ('userId', (), IcePy._t_long, False, 0),
        ('targetUsers', (), _M_ode.api._t_LongList, False, 0)
    ))
    Chown2._ice_type = _M_ode.cmd._t_Chown2

    _M_ode.cmd.Chown2 = Chown2
    del Chown2

    _M_ode.cmd.Chown2Prx = Chown2Prx
    del Chown2Prx

if 'Chown2Response' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Chown2Response = Ice.createTempClass()
    class Chown2Response(_M_ode.cmd.OK):
        """
        Result of changing the ownership of model objects.
        Members:
        includedObjects -- The model objects that were given.
        deletedObjects -- The model objects that were deleted.
        """
        def __init__(self, includedObjects=None, deletedObjects=None):
            _M_ode.cmd.OK.__init__(self)
            self.includedObjects = includedObjects
            self.deletedObjects = deletedObjects

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Chown2Response', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::Chown2Response'

        def ice_staticId():
            return '::ode::cmd::Chown2Response'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Chown2Response)

        __repr__ = __str__

    _M_ode.cmd.Chown2ResponsePrx = Ice.createTempClass()
    class Chown2ResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Chown2ResponsePrx.ice_checkedCast(proxy, '::ode::cmd::Chown2Response', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Chown2ResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Chown2Response'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Chown2ResponsePrx = IcePy.defineProxy('::ode::cmd::Chown2Response', Chown2ResponsePrx)

    _M_ode.cmd._t_Chown2Response = IcePy.defineClass('::ode::cmd::Chown2Response', Chown2Response, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('includedObjects', (), _M_ode.api._t_StringLongListMap, False, 0),
        ('deletedObjects', (), _M_ode.api._t_StringLongListMap, False, 0)
    ))
    Chown2Response._ice_type = _M_ode.cmd._t_Chown2Response

    _M_ode.cmd.Chown2Response = Chown2Response
    del Chown2Response

    _M_ode.cmd.Chown2ResponsePrx = Chown2ResponsePrx
    del Chown2ResponsePrx

if 'Delete2' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Delete2 = Ice.createTempClass()
    class Delete2(_M_ode.cmd.GraphModify2):
        """
        Delete model objects.
        Members:
        typesToIgnore -- Ignore in the operation all objects of these types.
        """
        def __init__(self, targetObjects=None, childOptions=None, dryRun=False, typesToIgnore=None):
            _M_ode.cmd.GraphModify2.__init__(self, targetObjects, childOptions, dryRun)
            self.typesToIgnore = typesToIgnore

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Delete2', '::ode::cmd::GraphModify2', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::Delete2'

        def ice_staticId():
            return '::ode::cmd::Delete2'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Delete2)

        __repr__ = __str__

    _M_ode.cmd.Delete2Prx = Ice.createTempClass()
    class Delete2Prx(_M_ode.cmd.GraphModify2Prx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Delete2Prx.ice_checkedCast(proxy, '::ode::cmd::Delete2', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Delete2Prx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Delete2'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Delete2Prx = IcePy.defineProxy('::ode::cmd::Delete2', Delete2Prx)

    _M_ode.cmd._t_Delete2 = IcePy.declareClass('::ode::cmd::Delete2')

    _M_ode.cmd._t_Delete2 = IcePy.defineClass('::ode::cmd::Delete2', Delete2, -1, (), False, False, _M_ode.cmd._t_GraphModify2, (), (('typesToIgnore', (), _M_ode.api._t_StringSet, False, 0),))
    Delete2._ice_type = _M_ode.cmd._t_Delete2

    _M_ode.cmd.Delete2 = Delete2
    del Delete2

    _M_ode.cmd.Delete2Prx = Delete2Prx
    del Delete2Prx

if 'Delete2Response' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Delete2Response = Ice.createTempClass()
    class Delete2Response(_M_ode.cmd.OK):
        """
        Result of deleting model objects.
        Members:
        deletedObjects -- The model objects that were deleted.
        """
        def __init__(self, deletedObjects=None):
            _M_ode.cmd.OK.__init__(self)
            self.deletedObjects = deletedObjects

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Delete2Response', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::Delete2Response'

        def ice_staticId():
            return '::ode::cmd::Delete2Response'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Delete2Response)

        __repr__ = __str__

    _M_ode.cmd.Delete2ResponsePrx = Ice.createTempClass()
    class Delete2ResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.Delete2ResponsePrx.ice_checkedCast(proxy, '::ode::cmd::Delete2Response', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.Delete2ResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Delete2Response'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_Delete2ResponsePrx = IcePy.defineProxy('::ode::cmd::Delete2Response', Delete2ResponsePrx)

    _M_ode.cmd._t_Delete2Response = IcePy.defineClass('::ode::cmd::Delete2Response', Delete2Response, -1, (), False, False, _M_ode.cmd._t_OK, (), (('deletedObjects', (), _M_ode.api._t_StringLongListMap, False, 0),))
    Delete2Response._ice_type = _M_ode.cmd._t_Delete2Response

    _M_ode.cmd.Delete2Response = Delete2Response
    del Delete2Response

    _M_ode.cmd.Delete2ResponsePrx = Delete2ResponsePrx
    del Delete2ResponsePrx

if 'SkipHead' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.SkipHead = Ice.createTempClass()
    class SkipHead(_M_ode.cmd.GraphModify2):
        """
        Perform a request skipping the top-most model objects in the
        graph. This permits operating upon the (possibly indirect)
        children of given objects. The arguments of this SkipHead
        request override those of the given request only until the
        targeted children are reached, except that if this SkipHead
        request's dryRun is set to true then the dryRun override
        persists throughout the operation. The response from SkipHead
        is as from the given request.
        Members:
        startFrom -- Classes of model objects from which to actually start the
        operation. These are children, directly or indirectly, of
        the target objects. These children become the true target
        objects of the underlying request.
        request -- The operation to perform on the targeted model objects.
        The given request's targetObjects property is ignored: it
        is the SkipHead request that specifies the parent objects.
        Only specific request types are supported
        (those implementing WrappableRequest).
        """
        def __init__(self, targetObjects=None, childOptions=None, dryRun=False, startFrom=None, request=None):
            _M_ode.cmd.GraphModify2.__init__(self, targetObjects, childOptions, dryRun)
            self.startFrom = startFrom
            self.request = request

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::GraphModify2', '::ode::cmd::GraphQuery', '::ode::cmd::Request', '::ode::cmd::SkipHead')

        def ice_id(self, current=None):
            return '::ode::cmd::SkipHead'

        def ice_staticId():
            return '::ode::cmd::SkipHead'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_SkipHead)

        __repr__ = __str__

    _M_ode.cmd.SkipHeadPrx = Ice.createTempClass()
    class SkipHeadPrx(_M_ode.cmd.GraphModify2Prx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.SkipHeadPrx.ice_checkedCast(proxy, '::ode::cmd::SkipHead', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.SkipHeadPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::SkipHead'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_SkipHeadPrx = IcePy.defineProxy('::ode::cmd::SkipHead', SkipHeadPrx)

    _M_ode.cmd._t_SkipHead = IcePy.declareClass('::ode::cmd::SkipHead')

    _M_ode.cmd._t_SkipHead = IcePy.defineClass('::ode::cmd::SkipHead', SkipHead, -1, (), False, False, _M_ode.cmd._t_GraphModify2, (), (
        ('startFrom', (), _M_ode.api._t_StringSet, False, 0),
        ('request', (), _M_ode.cmd._t_GraphModify2, False, 0)
    ))
    SkipHead._ice_type = _M_ode.cmd._t_SkipHead

    _M_ode.cmd.SkipHead = SkipHead
    del SkipHead

    _M_ode.cmd.SkipHeadPrx = SkipHeadPrx
    del SkipHeadPrx

if 'DiskUsage2' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.DiskUsage2 = Ice.createTempClass()
    class DiskUsage2(_M_ode.cmd.GraphQuery):
        """
        Request to determine the disk usage of the given objects
        and their contents. File-system paths used by multiple objects
        are de-duplicated in the total count. Specifying a class is
        equivalent to specifying all its instances as objects.
        Permissible classes include:
        ExperimenterGroup, Experimenter, Project, Dataset,
        Folder, Screen, Plate, Well, WellSample,
        Image, Pixels, Annotation, Job, Fileset, OriginalFile.
        """
        def __init__(self, targetObjects=None, targetClasses=None):
            _M_ode.cmd.GraphQuery.__init__(self, targetObjects)
            self.targetClasses = targetClasses

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::DiskUsage2', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::DiskUsage2'

        def ice_staticId():
            return '::ode::cmd::DiskUsage2'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_DiskUsage2)

        __repr__ = __str__

    _M_ode.cmd.DiskUsage2Prx = Ice.createTempClass()
    class DiskUsage2Prx(_M_ode.cmd.GraphQueryPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DiskUsage2Prx.ice_checkedCast(proxy, '::ode::cmd::DiskUsage2', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DiskUsage2Prx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::DiskUsage2'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DiskUsage2Prx = IcePy.defineProxy('::ode::cmd::DiskUsage2', DiskUsage2Prx)

    _M_ode.cmd._t_DiskUsage2 = IcePy.defineClass('::ode::cmd::DiskUsage2', DiskUsage2, -1, (), False, False, _M_ode.cmd._t_GraphQuery, (), (('targetClasses', (), _M_ode.api._t_StringSet, False, 0),))
    DiskUsage2._ice_type = _M_ode.cmd._t_DiskUsage2

    _M_ode.cmd.DiskUsage2 = DiskUsage2
    del DiskUsage2

    _M_ode.cmd.DiskUsage2Prx = DiskUsage2Prx
    del DiskUsage2Prx

if 'DiskUsage2Response' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.DiskUsage2Response = Ice.createTempClass()
    class DiskUsage2Response(_M_ode.cmd.OK):
        """
        Disk usage report: bytes used and non-empty file counts on the
        repository file-system for specific objects. The counts from the
        maps may sum to more than the total if different types of object
        refer to the same file. Common referers include:
        Annotation for file annotations
        FilesetEntry for ode 5 image files (ode.fs)
        Job for import logs
        Pixels for pyramids and ode 4 images and archived files
        Thumbnail for the image thumbnails
        The above map values are broken down by owner-group keys.
        """
        def __init__(self, fileCountByReferer=None, bytesUsedByReferer=None, totalFileCount=None, totalBytesUsed=None):
            _M_ode.cmd.OK.__init__(self)
            self.fileCountByReferer = fileCountByReferer
            self.bytesUsedByReferer = bytesUsedByReferer
            self.totalFileCount = totalFileCount
            self.totalBytesUsed = totalBytesUsed

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::DiskUsage2Response', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::DiskUsage2Response'

        def ice_staticId():
            return '::ode::cmd::DiskUsage2Response'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_DiskUsage2Response)

        __repr__ = __str__

    _M_ode.cmd.DiskUsage2ResponsePrx = Ice.createTempClass()
    class DiskUsage2ResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DiskUsage2ResponsePrx.ice_checkedCast(proxy, '::ode::cmd::DiskUsage2Response', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DiskUsage2ResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::DiskUsage2Response'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DiskUsage2ResponsePrx = IcePy.defineProxy('::ode::cmd::DiskUsage2Response', DiskUsage2ResponsePrx)

    _M_ode.cmd._t_DiskUsage2Response = IcePy.defineClass('::ode::cmd::DiskUsage2Response', DiskUsage2Response, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('fileCountByReferer', (), _M_ode.api._t_LongPairToStringIntMap, False, 0),
        ('bytesUsedByReferer', (), _M_ode.api._t_LongPairToStringLongMap, False, 0),
        ('totalFileCount', (), _M_ode.api._t_LongPairIntMap, False, 0),
        ('totalBytesUsed', (), _M_ode.api._t_LongPairLongMap, False, 0)
    ))
    DiskUsage2Response._ice_type = _M_ode.cmd._t_DiskUsage2Response

    _M_ode.cmd.DiskUsage2Response = DiskUsage2Response
    del DiskUsage2Response

    _M_ode.cmd.DiskUsage2ResponsePrx = DiskUsage2ResponsePrx
    del DiskUsage2ResponsePrx

if 'Duplicate' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.Duplicate = Ice.createTempClass()
    class Duplicate(_M_ode.cmd.GraphModify2):
        """
        Duplicate model objects with some selection of their subgraph.
        All target model objects must be in the current group context.
        The extra three data members allow adjustment of the related
        subgraph. The same type must not be listed in more than one of
        those data members. Use of a more specific sub-type in a data
        member always overrides the more general type in another.
        Members:
        typesToDuplicate -- The types of the model objects to actually duplicate.
        typesToReference -- The types of the model objects that should not be duplicated
        but that may participate in references involving duplicates.
        typesToIgnore -- The types of the model objects that should not be duplicated
        and that may not participate in references involving duplicates.
        """
        def __init__(self, targetObjects=None, childOptions=None, dryRun=False, typesToDuplicate=None, typesToReference=None, typesToIgnore=None):
            _M_ode.cmd.GraphModify2.__init__(self, targetObjects, childOptions, dryRun)
            self.typesToDuplicate = typesToDuplicate
            self.typesToReference = typesToReference
            self.typesToIgnore = typesToIgnore

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Duplicate', '::ode::cmd::GraphModify2', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::Duplicate'

        def ice_staticId():
            return '::ode::cmd::Duplicate'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_Duplicate)

        __repr__ = __str__

    _M_ode.cmd.DuplicatePrx = Ice.createTempClass()
    class DuplicatePrx(_M_ode.cmd.GraphModify2Prx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DuplicatePrx.ice_checkedCast(proxy, '::ode::cmd::Duplicate', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DuplicatePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::Duplicate'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DuplicatePrx = IcePy.defineProxy('::ode::cmd::Duplicate', DuplicatePrx)

    _M_ode.cmd._t_Duplicate = IcePy.declareClass('::ode::cmd::Duplicate')

    _M_ode.cmd._t_Duplicate = IcePy.defineClass('::ode::cmd::Duplicate', Duplicate, -1, (), False, False, _M_ode.cmd._t_GraphModify2, (), (
        ('typesToDuplicate', (), _M_ode.api._t_StringSet, False, 0),
        ('typesToReference', (), _M_ode.api._t_StringSet, False, 0),
        ('typesToIgnore', (), _M_ode.api._t_StringSet, False, 0)
    ))
    Duplicate._ice_type = _M_ode.cmd._t_Duplicate

    _M_ode.cmd.Duplicate = Duplicate
    del Duplicate

    _M_ode.cmd.DuplicatePrx = DuplicatePrx
    del DuplicatePrx

if 'DuplicateResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.DuplicateResponse = Ice.createTempClass()
    class DuplicateResponse(_M_ode.cmd.OK):
        """
        Result of duplicating model objects.
        Members:
        duplicates -- The duplicate model objects created by the request.
        Note: If dryRun is set to true then this instead lists the model
        objects that would have been duplicated.
        """
        def __init__(self, duplicates=None):
            _M_ode.cmd.OK.__init__(self)
            self.duplicates = duplicates

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::DuplicateResponse', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::DuplicateResponse'

        def ice_staticId():
            return '::ode::cmd::DuplicateResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_DuplicateResponse)

        __repr__ = __str__

    _M_ode.cmd.DuplicateResponsePrx = Ice.createTempClass()
    class DuplicateResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.DuplicateResponsePrx.ice_checkedCast(proxy, '::ode::cmd::DuplicateResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.DuplicateResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::DuplicateResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_DuplicateResponsePrx = IcePy.defineProxy('::ode::cmd::DuplicateResponse', DuplicateResponsePrx)

    _M_ode.cmd._t_DuplicateResponse = IcePy.defineClass('::ode::cmd::DuplicateResponse', DuplicateResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), (('duplicates', (), _M_ode.api._t_StringLongListMap, False, 0),))
    DuplicateResponse._ice_type = _M_ode.cmd._t_DuplicateResponse

    _M_ode.cmd.DuplicateResponse = DuplicateResponse
    del DuplicateResponse

    _M_ode.cmd.DuplicateResponsePrx = DuplicateResponsePrx
    del DuplicateResponsePrx

if 'FindParents' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FindParents = Ice.createTempClass()
    class FindParents(_M_ode.cmd.GraphQuery):
        """
        Identify the parents or containers of model objects.
        Traverses the model graph to identify indirect relationships.
        Members:
        typesOfParents -- The types of parents being sought.
        stopBefore -- Classes of model objects to exclude from the recursive
        search. Search does not include or pass such objects.
        For efficiency the server automatically excludes various
        classes depending on the other arguments of the request.
        """
        def __init__(self, targetObjects=None, typesOfParents=None, stopBefore=None):
            _M_ode.cmd.GraphQuery.__init__(self, targetObjects)
            self.typesOfParents = typesOfParents
            self.stopBefore = stopBefore

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FindParents', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::FindParents'

        def ice_staticId():
            return '::ode::cmd::FindParents'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FindParents)

        __repr__ = __str__

    _M_ode.cmd.FindParentsPrx = Ice.createTempClass()
    class FindParentsPrx(_M_ode.cmd.GraphQueryPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FindParentsPrx.ice_checkedCast(proxy, '::ode::cmd::FindParents', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FindParentsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FindParents'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FindParentsPrx = IcePy.defineProxy('::ode::cmd::FindParents', FindParentsPrx)

    _M_ode.cmd._t_FindParents = IcePy.defineClass('::ode::cmd::FindParents', FindParents, -1, (), False, False, _M_ode.cmd._t_GraphQuery, (), (
        ('typesOfParents', (), _M_ode.api._t_StringSet, False, 0),
        ('stopBefore', (), _M_ode.api._t_StringSet, False, 0)
    ))
    FindParents._ice_type = _M_ode.cmd._t_FindParents

    _M_ode.cmd.FindParents = FindParents
    del FindParents

    _M_ode.cmd.FindParentsPrx = FindParentsPrx
    del FindParentsPrx

if 'FoundParents' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FoundParents = Ice.createTempClass()
    class FoundParents(_M_ode.cmd.OK):
        """
        Result of identifying the parents or containers of model objects.
        Members:
        parents -- The parents that were identified.
        """
        def __init__(self, parents=None):
            _M_ode.cmd.OK.__init__(self)
            self.parents = parents

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FoundParents', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::FoundParents'

        def ice_staticId():
            return '::ode::cmd::FoundParents'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FoundParents)

        __repr__ = __str__

    _M_ode.cmd.FoundParentsPrx = Ice.createTempClass()
    class FoundParentsPrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FoundParentsPrx.ice_checkedCast(proxy, '::ode::cmd::FoundParents', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FoundParentsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FoundParents'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FoundParentsPrx = IcePy.defineProxy('::ode::cmd::FoundParents', FoundParentsPrx)

    _M_ode.cmd._t_FoundParents = IcePy.defineClass('::ode::cmd::FoundParents', FoundParents, -1, (), False, False, _M_ode.cmd._t_OK, (), (('parents', (), _M_ode.api._t_StringLongListMap, False, 0),))
    FoundParents._ice_type = _M_ode.cmd._t_FoundParents

    _M_ode.cmd.FoundParents = FoundParents
    del FoundParents

    _M_ode.cmd.FoundParentsPrx = FoundParentsPrx
    del FoundParentsPrx

if 'FindChildren' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FindChildren = Ice.createTempClass()
    class FindChildren(_M_ode.cmd.GraphQuery):
        """
        Identify the children or contents of model objects.
        Traverses the model graph to identify indirect relationships.
        Members:
        typesOfChildren -- The types of children being sought.
        stopBefore -- Classes of model objects to exclude from the recursive
        search. Search does not include or pass such objects.
        For efficiency the server automatically excludes various
        classes depending on the other arguments of the request.
        """
        def __init__(self, targetObjects=None, typesOfChildren=None, stopBefore=None):
            _M_ode.cmd.GraphQuery.__init__(self, targetObjects)
            self.typesOfChildren = typesOfChildren
            self.stopBefore = stopBefore

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FindChildren', '::ode::cmd::GraphQuery', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::FindChildren'

        def ice_staticId():
            return '::ode::cmd::FindChildren'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FindChildren)

        __repr__ = __str__

    _M_ode.cmd.FindChildrenPrx = Ice.createTempClass()
    class FindChildrenPrx(_M_ode.cmd.GraphQueryPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FindChildrenPrx.ice_checkedCast(proxy, '::ode::cmd::FindChildren', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FindChildrenPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FindChildren'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FindChildrenPrx = IcePy.defineProxy('::ode::cmd::FindChildren', FindChildrenPrx)

    _M_ode.cmd._t_FindChildren = IcePy.defineClass('::ode::cmd::FindChildren', FindChildren, -1, (), False, False, _M_ode.cmd._t_GraphQuery, (), (
        ('typesOfChildren', (), _M_ode.api._t_StringSet, False, 0),
        ('stopBefore', (), _M_ode.api._t_StringSet, False, 0)
    ))
    FindChildren._ice_type = _M_ode.cmd._t_FindChildren

    _M_ode.cmd.FindChildren = FindChildren
    del FindChildren

    _M_ode.cmd.FindChildrenPrx = FindChildrenPrx
    del FindChildrenPrx

if 'FoundChildren' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.FoundChildren = Ice.createTempClass()
    class FoundChildren(_M_ode.cmd.OK):
        """
        Result of identifying the children or contents of model objects.
        Members:
        children -- The children that were identified.
        """
        def __init__(self, children=None):
            _M_ode.cmd.OK.__init__(self)
            self.children = children

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::FoundChildren', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::FoundChildren'

        def ice_staticId():
            return '::ode::cmd::FoundChildren'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_FoundChildren)

        __repr__ = __str__

    _M_ode.cmd.FoundChildrenPrx = Ice.createTempClass()
    class FoundChildrenPrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.FoundChildrenPrx.ice_checkedCast(proxy, '::ode::cmd::FoundChildren', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.FoundChildrenPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::FoundChildren'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_FoundChildrenPrx = IcePy.defineProxy('::ode::cmd::FoundChildren', FoundChildrenPrx)

    _M_ode.cmd._t_FoundChildren = IcePy.defineClass('::ode::cmd::FoundChildren', FoundChildren, -1, (), False, False, _M_ode.cmd._t_OK, (), (('children', (), _M_ode.api._t_StringLongListMap, False, 0),))
    FoundChildren._ice_type = _M_ode.cmd._t_FoundChildren

    _M_ode.cmd.FoundChildren = FoundChildren
    del FoundChildren

    _M_ode.cmd.FoundChildrenPrx = FoundChildrenPrx
    del FoundChildrenPrx

if 'LegalGraphTargets' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.LegalGraphTargets = Ice.createTempClass()
    class LegalGraphTargets(_M_ode.cmd.Request):
        """
        Graph requests typically allow only specific model object classes
        to be targeted. This request lists the legal targets for a given
        request. The request's fields are ignored, only its class matters.
        Members:
        request -- A request of the type being queried.
        """
        def __init__(self, request=None):
            _M_ode.cmd.Request.__init__(self)
            self.request = request

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::LegalGraphTargets', '::ode::cmd::Request')

        def ice_id(self, current=None):
            return '::ode::cmd::LegalGraphTargets'

        def ice_staticId():
            return '::ode::cmd::LegalGraphTargets'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_LegalGraphTargets)

        __repr__ = __str__

    _M_ode.cmd.LegalGraphTargetsPrx = Ice.createTempClass()
    class LegalGraphTargetsPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.LegalGraphTargetsPrx.ice_checkedCast(proxy, '::ode::cmd::LegalGraphTargets', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.LegalGraphTargetsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::LegalGraphTargets'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_LegalGraphTargetsPrx = IcePy.defineProxy('::ode::cmd::LegalGraphTargets', LegalGraphTargetsPrx)

    _M_ode.cmd._t_LegalGraphTargets = IcePy.declareClass('::ode::cmd::LegalGraphTargets')

    _M_ode.cmd._t_LegalGraphTargets = IcePy.defineClass('::ode::cmd::LegalGraphTargets', LegalGraphTargets, -1, (), False, False, _M_ode.cmd._t_Request, (), (('request', (), _M_ode.cmd._t_GraphQuery, False, 0),))
    LegalGraphTargets._ice_type = _M_ode.cmd._t_LegalGraphTargets

    _M_ode.cmd.LegalGraphTargets = LegalGraphTargets
    del LegalGraphTargets

    _M_ode.cmd.LegalGraphTargetsPrx = LegalGraphTargetsPrx
    del LegalGraphTargetsPrx

if 'LegalGraphTargetsResponse' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.LegalGraphTargetsResponse = Ice.createTempClass()
    class LegalGraphTargetsResponse(_M_ode.cmd.OK):
        """
        A list of the legal targets for a graph request.
        Members:
        targets -- The legal targets for the given request's type.
        """
        def __init__(self, targets=None):
            _M_ode.cmd.OK.__init__(self)
            self.targets = targets

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::LegalGraphTargetsResponse', '::ode::cmd::OK', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::LegalGraphTargetsResponse'

        def ice_staticId():
            return '::ode::cmd::LegalGraphTargetsResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_LegalGraphTargetsResponse)

        __repr__ = __str__

    _M_ode.cmd.LegalGraphTargetsResponsePrx = Ice.createTempClass()
    class LegalGraphTargetsResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.LegalGraphTargetsResponsePrx.ice_checkedCast(proxy, '::ode::cmd::LegalGraphTargetsResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.LegalGraphTargetsResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::LegalGraphTargetsResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_LegalGraphTargetsResponsePrx = IcePy.defineProxy('::ode::cmd::LegalGraphTargetsResponse', LegalGraphTargetsResponsePrx)

    _M_ode.cmd._t_LegalGraphTargetsResponse = IcePy.defineClass('::ode::cmd::LegalGraphTargetsResponse', LegalGraphTargetsResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), (('targets', (), _M_ode.api._t_StringSet, False, 0),))
    LegalGraphTargetsResponse._ice_type = _M_ode.cmd._t_LegalGraphTargetsResponse

    _M_ode.cmd.LegalGraphTargetsResponse = LegalGraphTargetsResponse
    del LegalGraphTargetsResponse

    _M_ode.cmd.LegalGraphTargetsResponsePrx = LegalGraphTargetsResponsePrx
    del LegalGraphTargetsResponsePrx

if 'GraphException' not in _M_ode.cmd.__dict__:
    _M_ode.cmd.GraphException = Ice.createTempClass()
    class GraphException(_M_ode.cmd.ERR):
        """
        Returned when specifically a ode.services.graphs.GraphException
        is thrown. The contents of that internal exception are passed in
        this instance.
        Members:
        message -- The message of the GraphException.
        """
        def __init__(self, category='', name='', parameters=None, message=''):
            _M_ode.cmd.ERR.__init__(self, category, name, parameters)
            self.message = message

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::ERR', '::ode::cmd::GraphException', '::ode::cmd::Response')

        def ice_id(self, current=None):
            return '::ode::cmd::GraphException'

        def ice_staticId():
            return '::ode::cmd::GraphException'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.cmd._t_GraphException)

        __repr__ = __str__

    _M_ode.cmd.GraphExceptionPrx = Ice.createTempClass()
    class GraphExceptionPrx(_M_ode.cmd.ERRPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.cmd.GraphExceptionPrx.ice_checkedCast(proxy, '::ode::cmd::GraphException', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.cmd.GraphExceptionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::cmd::GraphException'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.cmd._t_GraphExceptionPrx = IcePy.defineProxy('::ode::cmd::GraphException', GraphExceptionPrx)

    _M_ode.cmd._t_GraphException = IcePy.defineClass('::ode::cmd::GraphException', GraphException, -1, (), False, False, _M_ode.cmd._t_ERR, (), (('message', (), IcePy._t_string, False, 0),))
    GraphException._ice_type = _M_ode.cmd._t_GraphException

    _M_ode.cmd.GraphException = GraphException
    del GraphException

    _M_ode.cmd.GraphExceptionPrx = GraphExceptionPrx
    del GraphExceptionPrx

# End of module ode.cmd

__name__ = 'ode'

# End of module ode
