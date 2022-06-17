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
import ode_System_ice
import ode_ServerErrors_ice
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

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Start of module ode
__name__ = 'ode'

if 'Internal' not in _M_ode.__dict__:
    _M_ode.Internal = Ice.createTempClass()
    class Internal(Ice.Object):
        """
        Base class similar to ode.model.IObject but for non-model-objects.
        """
        def __init__(self):
            pass

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::Internal')

        def ice_id(self, current=None):
            return '::ode::Internal'

        def ice_staticId():
            return '::ode::Internal'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_Internal)

        __repr__ = __str__

    _M_ode.InternalPrx = Ice.createTempClass()
    class InternalPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.InternalPrx.ice_checkedCast(proxy, '::ode::Internal', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.InternalPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::Internal'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_InternalPrx = IcePy.defineProxy('::ode::Internal', InternalPrx)

    _M_ode._t_Internal = IcePy.defineClass('::ode::Internal', Internal, -1, (), False, False, None, (), ())
    Internal._ice_type = _M_ode._t_Internal

    _M_ode.Internal = Internal
    del Internal

    _M_ode.InternalPrx = InternalPrx
    del InternalPrx

if 'RInternal' not in _M_ode.__dict__:
    _M_ode.RInternal = Ice.createTempClass()
    class RInternal(_M_ode.RType):
        """
        Base type for RTypes whose contents will not be parsed by
        the server. This allows Blitz-specific types to be safely
        passed in as the inputs/outputs of scripts.
        """
        def __init__(self, _val=None):
            if Ice.getType(self) == _M_ode.RInternal:
                raise RuntimeError('ode.RInternal is an abstract class')
            _M_ode.RType.__init__(self)
            self._val = _val

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::RInternal', '::ode::RType')

        def ice_id(self, current=None):
            return '::ode::RInternal'

        def ice_staticId():
            return '::ode::RInternal'
        ice_staticId = staticmethod(ice_staticId)

        def getValue(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_RInternal)

        __repr__ = __str__

    _M_ode.RInternalPrx = Ice.createTempClass()
    class RInternalPrx(_M_ode.RTypePrx):

        def getValue(self, _ctx=None):
            return _M_ode.RInternal._op_getValue.invoke(self, ((), _ctx))

        def begin_getValue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.RInternal._op_getValue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getValue(self, _r):
            return _M_ode.RInternal._op_getValue.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.RInternalPrx.ice_checkedCast(proxy, '::ode::RInternal', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.RInternalPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::RInternal'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_RInternalPrx = IcePy.defineProxy('::ode::RInternal', RInternalPrx)

    _M_ode._t_RInternal = IcePy.declareClass('::ode::RInternal')

    _M_ode._t_RInternal = IcePy.defineClass('::ode::RInternal', RInternal, -1, (), True, False, _M_ode._t_RType, (), (('_val', (), _M_ode._t_Internal, False, 0),))
    RInternal._ice_type = _M_ode._t_RInternal

    RInternal._op_getValue = IcePy.Operation('getValue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_Internal, False, 0), ())

    _M_ode.RInternal = RInternal
    del RInternal

    _M_ode.RInternalPrx = RInternalPrx
    del RInternalPrx

if '_t_Bytes2D' not in _M_ode.__dict__:
    _M_ode._t_Bytes2D = IcePy.defineSequence('::ode::Bytes2D', (), _M_Ice._t_ByteSeq)

if 'Plane' not in _M_ode.__dict__:
    _M_ode.Plane = Ice.createTempClass()
    class Plane(_M_ode.Internal):
        """
        Sequences cannot subclass other types, so the Plane
        class extends {@code Internal} and wraps a {@code Bytes2D} instance.
        """
        def __init__(self, data=None):
            _M_ode.Internal.__init__(self)
            self.data = data

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::Internal', '::ode::Plane')

        def ice_id(self, current=None):
            return '::ode::Plane'

        def ice_staticId():
            return '::ode::Plane'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_Plane)

        __repr__ = __str__

    _M_ode.PlanePrx = Ice.createTempClass()
    class PlanePrx(_M_ode.InternalPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.PlanePrx.ice_checkedCast(proxy, '::ode::Plane', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.PlanePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::Plane'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_PlanePrx = IcePy.defineProxy('::ode::Plane', PlanePrx)

    _M_ode._t_Plane = IcePy.defineClass('::ode::Plane', Plane, -1, (), False, False, _M_ode._t_Internal, (), (('data', (), _M_ode._t_Bytes2D, False, 0),))
    Plane._ice_type = _M_ode._t_Plane

    _M_ode.Plane = Plane
    del Plane

    _M_ode.PlanePrx = PlanePrx
    del PlanePrx

if 'Point' not in _M_ode.__dict__:
    _M_ode.Point = Ice.createTempClass()
    class Point(_M_ode.Internal):
        """
        XY-point in space.
        """
        def __init__(self, x=0, y=0):
            _M_ode.Internal.__init__(self)
            self.x = x
            self.y = y

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::Internal', '::ode::Point')

        def ice_id(self, current=None):
            return '::ode::Point'

        def ice_staticId():
            return '::ode::Point'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_Point)

        __repr__ = __str__

    _M_ode.PointPrx = Ice.createTempClass()
    class PointPrx(_M_ode.InternalPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.PointPrx.ice_checkedCast(proxy, '::ode::Point', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.PointPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::Point'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_PointPrx = IcePy.defineProxy('::ode::Point', PointPrx)

    _M_ode._t_Point = IcePy.defineClass('::ode::Point', Point, -1, (), False, False, _M_ode._t_Internal, (), (
        ('x', (), IcePy._t_int, False, 0),
        ('y', (), IcePy._t_int, False, 0)
    ))
    Point._ice_type = _M_ode._t_Point

    _M_ode.Point = Point
    del Point

    _M_ode.PointPrx = PointPrx
    del PointPrx

if 'Color' not in _M_ode.__dict__:
    _M_ode.Color = Ice.createTempClass()
    class Color(_M_ode.Internal):
        """
        RGBA-color packed into a single long.
        """
        def __init__(self, packedColor=0):
            _M_ode.Internal.__init__(self)
            self.packedColor = packedColor

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::Color', '::ode::Internal')

        def ice_id(self, current=None):
            return '::ode::Color'

        def ice_staticId():
            return '::ode::Color'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode._t_Color)

        __repr__ = __str__

    _M_ode.ColorPrx = Ice.createTempClass()
    class ColorPrx(_M_ode.InternalPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.ColorPrx.ice_checkedCast(proxy, '::ode::Color', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.ColorPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::Color'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode._t_ColorPrx = IcePy.defineProxy('::ode::Color', ColorPrx)

    _M_ode._t_Color = IcePy.defineClass('::ode::Color', Color, -1, (), False, False, _M_ode._t_Internal, (), (('packedColor', (), IcePy._t_long, False, 0),))
    Color._ice_type = _M_ode._t_Color

    _M_ode.Color = Color
    del Color

    _M_ode.ColorPrx = ColorPrx
    del ColorPrx

# Start of module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')
__name__ = 'ode.grid'

if 'Param' not in _M_ode.grid.__dict__:
    _M_ode.grid.Param = Ice.createTempClass()
    class Param(Ice.Object):
        """
        A single parameter to a Job. For example, used by
        ScriptJobs to define what the input and output
        environment variables should be. Helper classes are available
        in the Python ode.scripts module, so that the following are
        equivalent:
        # 1
        a = ode.grid.Params()
        a.optional = True
        a.prototype = ode.rtypes.rstring("")
        a.description = "An optional string which will be ignored by the script"
        ode.scripts.client(inputs = {"a":a})
        # 2
        a = ode.scripts.String("a", optional=True, description=         * "An optional string which will be ignored by the script")
        ode.scripts.client(a)
        For advanced setters not available on the Type classes (like ode.script.String)
        use the getter type.param() and then set values directly.
        a = ode.scripts.String("a")
        a.param().values = \["hi", "bye"]
        Members:
        description -- Usage documentation of this param for script users.
        Example of a bad description: ""a long value""
        Example of a good description: ""long representing
        the number of bins to be used by . A sensible
        value would be between 16 and 32""
        optional -- Whether or not a script will require this value to be present
        in the input or output. If an input is missing or None when
        non-optional, then a ode.ValidationException will be
        thrown on {@code processJob}. A missing output param will be
        marked after execution.
        useDefault -- Whether or not the prototype should be used as a default.
        If true, then if the value is missing from the input OR
        output values, the prototype will be substituted.
        param = ...;
        inputs = ...;
        if name in inputs:
        value = inputs\[name]
        elif param.inputs\[name].useDefault:
        value = param.inputs\[name].prototype
        prototype -- ode.RType which represents what the input or output
        value should look like. If this is a collection type (i.e.
        ode.RCollection or ode.RMap or their
        subclasses), then the first contents of the collection will be
        used (recursively).
        param.prototype = rlist(rlist(rstring)))
        requires that a list of list of strings be passed.
        min -- Minimum value which an input may contain. If the prototype
        is a collection type, then the min type must match the type
        of the innermost non-collection instance.
        For example,
        param.prototype = rlong(0)
        param.min = rlong(-5)
        but
        param.prototype = rlist(rlong(0))
        param.min = rlong(-5)
        max -- Maximum value which an input may contain. If the prototype
        is a collection type, then the max type must match the type
        of the innermost non-collection instance.
        For example,
        param.prototype = rlong(0)
        param.max = rlong(5)
        but
        param.prototype = rlist(rlong(0))
        param.max = rlong(5)
        values -- An enumeration of acceptable values which can be used
        for this parameter. If {@code min} and {@code max} are set,
        this value will be ignored. If {@code prototype} is an
        ode.RCollection or ode.RMap instance, then
        the values in this ode.RList will be of the member
        types of the collection or map, and not a collection or map
        instance.
        grouping -- Defines the grouping strategy for this Param.
        A set of {@code Param} objects in a single {@code JobParams} can
        use dot notation to specify that they belong together,
        and in which order they should be presented to the user.
        inputs = {"a" : Param(..., grouping = "1.1"),
        "b" : Param(..., grouping = "1.2"),
        "c" : Param(..., grouping = "2.2"),
        "d" : Param(..., grouping = "2.1")}
        defines two groups of parameters which might be
        display to the user so:
        Group 1:                  Group 2:
        +-----------------------+ +-----------------------+
        | a:                    | | d:                    |
        +-----------------------+ +-----------------------+
        | b:                    | | c:                    |
        +-----------------------+ +-----------------------+
        Further dots (e.g. "1.2.3.5") can be used to specify
        deeper trees of parameters.
        By most clients, Params missing grouping values (e.g. "") will
        be ordered after params with grouping values.
        A group which has a boolean as the top-level object
        can be thought of as a checkbox which turns on or off
        all of the other group members. For example,
        inputs = {"Image_Ids" : Param(prototype=rlist(), grouping = "1"),
        "Scale_Bar" : Param(prototype=rbool(), grouping = "2"),
        "Color"     : Param(prototype=rinternal(Color()), grouping = "2.1"),
        "Size"      : Param(prototype=rlong(), grouping = "2.2")}
        might be displayed as:
        Scale Bar: \[ on/off ]
        ======================
        Color:  \[rgb]
        Size:   \[ 10]
        namespaces -- Defines machine readable interpretations for this parameter.
        Where the description field should provide information for
        users, the assigned namespaces can define how clients may
        interpret the param.
        {@code ode.constants.namespaces.NSDOWNLOAD}, for example,
        indicates that users may want to download the resulting
        file. The {@code prototype} of the {@code Param} should be one
        of: ode.model.OriginalFile,
        ode.model.FileAnnotation,
        or an annotation link (like
        ode.model.ImageAnnotationLink)
        which points to a file annotation.
        """
        def __init__(self, description='', optional=False, useDefault=False, prototype=None, min=None, max=None, values=None, grouping='', namespaces=None):
            self.description = description
            self.optional = optional
            self.useDefault = useDefault
            self.prototype = prototype
            self.min = min
            self.max = max
            self.values = values
            self.grouping = grouping
            self.namespaces = namespaces

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Param')

        def ice_id(self, current=None):
            return '::ode::grid::Param'

        def ice_staticId():
            return '::ode::grid::Param'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Param)

        __repr__ = __str__

    _M_ode.grid.ParamPrx = Ice.createTempClass()
    class ParamPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ParamPrx.ice_checkedCast(proxy, '::ode::grid::Param', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ParamPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Param'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ParamPrx = IcePy.defineProxy('::ode::grid::Param', ParamPrx)

    _M_ode.grid._t_Param = IcePy.declareClass('::ode::grid::Param')

    _M_ode.grid._t_Param = IcePy.defineClass('::ode::grid::Param', Param, -1, (), False, False, None, (), (
        ('description', (), IcePy._t_string, False, 0),
        ('optional', (), IcePy._t_bool, False, 0),
        ('useDefault', (), IcePy._t_bool, False, 0),
        ('prototype', (), _M_ode._t_RType, False, 0),
        ('min', (), _M_ode._t_RType, False, 0),
        ('max', (), _M_ode._t_RType, False, 0),
        ('values', (), _M_ode._t_RList, False, 0),
        ('grouping', (), IcePy._t_string, False, 0),
        ('namespaces', (), _M_ode.api._t_StringSet, False, 0)
    ))
    Param._ice_type = _M_ode.grid._t_Param

    _M_ode.grid.Param = Param
    del Param

    _M_ode.grid.ParamPrx = ParamPrx
    del ParamPrx

if '_t_ParamMap' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_ParamMap = IcePy.defineDictionary('::ode::grid::ParamMap', (), IcePy._t_string, _M_ode.grid._t_Param)

if 'JobParams' not in _M_ode.grid.__dict__:
    _M_ode.grid.JobParams = Ice.createTempClass()
    class JobParams(_M_ode.Internal):
        """
        Complete job description with all input and output Params.
        JobParams contain information about who wrote a script, what its
        purpose is, and how it should be used, and are defined via the
        "ode.scripts.client" method.
        c = ode.scripts.client(name="my algorithm", version="0.0.1")
        Alternatively, a JobParams instance can be passed into the constructor:
        params = ode.grid.JobParams()
        params.authors = \["Pramila", "Kumari"]
        params.version = "0.0.1"
        params.description = \"""
        Clever way to count to 5
        \"""
        c = ode.scripts.client(params)
        A single JobParam instance is parsed from a script and stored by the server.
        Later invocations re-use this instance until the script changes.
        Members:
        name -- Descriptive name for this script. This value should be unique where
        possible, but no assurance is provided by the server that multiple
        scripts with the same name are not present.
        version -- Author-given version number for this script. Please see the script
        authors' guide for information about choosing version numbers.
        description -- A general description of a script, including documentation on how
        it should be used, what data it will access, and other metrics
        like how long it takes to execute, etc.
        contact -- Single, human-readable string for how to contact the script author.
        authors -- Information about the authors who took part in creating this script.
        No particular format is required.
        institutions -- Information about the institutions which took part in creating this script.
        No particular format is required.
        authorsInstitutions -- For authors\[i], authorInstitutions\[i] should be
        and array of indexes j such that author i is a member
        of authorsInstitutions\[i]\[j].
        Example:
        authors = \["Sanjay", "Kumar"]
        institutions = \["Bhojpur Consulting", "Private Corp."]
        authorsInstitutions = \[\[1, 2], \[1]]
        which means that Jane is a member of both "Acme U."
        and "Private Corp." while Sanjay is only a member of
        "Bhojpur Consulting"
        An empty authorsInstitutions array implies that all
        authors are from all institutions.
        inputs -- Definitive list of the inputs which MAY or MUST be provided
        to the script, based on the "optional" flag.
        outputs -- Definitive list of the outputs which MAY or MUST be provided
        to the script, based on the "optional" flag.
        stdoutFormat -- {@code ode.model.Format.value} of the stdout stream produced
        by the script. If this value is not otherwise set (i.e. is
        None), the default of "text/plain" will be set. This is
        typically a good idea if the script uses "print" or the logging
        module.
        If you would like to disable stdout upload, set the value to ""
        (the empty string).
        "text/html" or "application/octet-stream" might also be values of interest.
        stderrFormat -- {@code ode.model.Format.value} of the stderr stream produced by
        the script. If this value is not otherwise set (i.e. is None),
        the default of "text/plain" will be set. This is typically a
        good idea if the script uses "print" or the logging module.
        If you would like to disable stderr upload, set the value to ""
        (the empty string).
        "text/html" or "application/octet-stream" might also be values of interest.
        namespaces -- Defines machine readable interpretations for this
        {@code JobParams}.
        Where the description field should provide information for
        users, the assigned namespaces can define how clients may
        interpret the script, including which categories or algorithm
        types the script belongs to.
        """
        def __init__(self, name='', version='', description='', contact='', authors=None, institutions=None, authorsInstitutions=None, inputs=None, outputs=None, stdoutFormat='', stderrFormat='', namespaces=None):
            _M_ode.Internal.__init__(self)
            self.name = name
            self.version = version
            self.description = description
            self.contact = contact
            self.authors = authors
            self.institutions = institutions
            self.authorsInstitutions = authorsInstitutions
            self.inputs = inputs
            self.outputs = outputs
            self.stdoutFormat = stdoutFormat
            self.stderrFormat = stderrFormat
            self.namespaces = namespaces

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::Internal', '::ode::grid::JobParams')

        def ice_id(self, current=None):
            return '::ode::grid::JobParams'

        def ice_staticId():
            return '::ode::grid::JobParams'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_JobParams)

        __repr__ = __str__

    _M_ode.grid.JobParamsPrx = Ice.createTempClass()
    class JobParamsPrx(_M_ode.InternalPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.JobParamsPrx.ice_checkedCast(proxy, '::ode::grid::JobParams', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.JobParamsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::JobParams'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_JobParamsPrx = IcePy.defineProxy('::ode::grid::JobParams', JobParamsPrx)

    _M_ode.grid._t_JobParams = IcePy.declareClass('::ode::grid::JobParams')

    _M_ode.grid._t_JobParams = IcePy.defineClass('::ode::grid::JobParams', JobParams, -1, (), False, False, _M_ode._t_Internal, (), (
        ('name', (), IcePy._t_string, False, 0),
        ('version', (), IcePy._t_string, False, 0),
        ('description', (), IcePy._t_string, False, 0),
        ('contact', (), IcePy._t_string, False, 0),
        ('authors', (), _M_ode.api._t_StringArray, False, 0),
        ('institutions', (), _M_ode.api._t_StringArray, False, 0),
        ('authorsInstitutions', (), _M_ode.api._t_IntegerArrayArray, False, 0),
        ('inputs', (), _M_ode.grid._t_ParamMap, False, 0),
        ('outputs', (), _M_ode.grid._t_ParamMap, False, 0),
        ('stdoutFormat', (), IcePy._t_string, False, 0),
        ('stderrFormat', (), IcePy._t_string, False, 0),
        ('namespaces', (), _M_ode.api._t_StringSet, False, 0)
    ))
    JobParams._ice_type = _M_ode.grid._t_JobParams

    _M_ode.grid.JobParams = JobParams
    del JobParams

    _M_ode.grid.JobParamsPrx = JobParamsPrx
    del JobParamsPrx

if 'ProcessCallback' not in _M_ode.grid.__dict__:
    _M_ode.grid.ProcessCallback = Ice.createTempClass()
    class ProcessCallback(Ice.Object):
        """
        Callback which can be attached to a Process
        with notification of any of the possible
        ends-of-life that a Process might experience
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.ProcessCallback:
                raise RuntimeError('ode.grid.ProcessCallback is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::ProcessCallback')

        def ice_id(self, current=None):
            return '::ode::grid::ProcessCallback'

        def ice_staticId():
            return '::ode::grid::ProcessCallback'
        ice_staticId = staticmethod(ice_staticId)

        def processFinished(self, returncode, current=None):
            """
            Process terminated normally. Return code provided.
            In the case that a non-Blitz process sent a signal
            (KILL, TERM, ... ), that will represented in the
            return code.
            Arguments:
            returncode -- 
            current -- The Current object for the invocation.
            """
            pass

        def processCancelled(self, success, current=None):
            """
            cancel() was called on this Process. If the Process
            failed to terminate, argument is false, in which calling
            kill() is the last resort.
            Arguments:
            success -- 
            current -- The Current object for the invocation.
            """
            pass

        def processKilled(self, success, current=None):
            """
            kill() was called on this Process. If this does not
            succeed, there is nothing else that Blitz can do to
            stop its execution.
            Arguments:
            success -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ProcessCallback)

        __repr__ = __str__

    _M_ode.grid.ProcessCallbackPrx = Ice.createTempClass()
    class ProcessCallbackPrx(Ice.ObjectPrx):

        """
        Process terminated normally. Return code provided.
        In the case that a non-Blitz process sent a signal
        (KILL, TERM, ... ), that will represented in the
        return code.
        Arguments:
        returncode -- 
        _ctx -- The request context for the invocation.
        """
        def processFinished(self, returncode, _ctx=None):
            return _M_ode.grid.ProcessCallback._op_processFinished.invoke(self, ((returncode, ), _ctx))

        """
        Process terminated normally. Return code provided.
        In the case that a non-Blitz process sent a signal
        (KILL, TERM, ... ), that will represented in the
        return code.
        Arguments:
        returncode -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_processFinished(self, returncode, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ProcessCallback._op_processFinished.begin(self, ((returncode, ), _response, _ex, _sent, _ctx))

        """
        Process terminated normally. Return code provided.
        In the case that a non-Blitz process sent a signal
        (KILL, TERM, ... ), that will represented in the
        return code.
        Arguments:
        returncode -- 
        """
        def end_processFinished(self, _r):
            return _M_ode.grid.ProcessCallback._op_processFinished.end(self, _r)

        """
        cancel() was called on this Process. If the Process
        failed to terminate, argument is false, in which calling
        kill() is the last resort.
        Arguments:
        success -- 
        _ctx -- The request context for the invocation.
        """
        def processCancelled(self, success, _ctx=None):
            return _M_ode.grid.ProcessCallback._op_processCancelled.invoke(self, ((success, ), _ctx))

        """
        cancel() was called on this Process. If the Process
        failed to terminate, argument is false, in which calling
        kill() is the last resort.
        Arguments:
        success -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_processCancelled(self, success, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ProcessCallback._op_processCancelled.begin(self, ((success, ), _response, _ex, _sent, _ctx))

        """
        cancel() was called on this Process. If the Process
        failed to terminate, argument is false, in which calling
        kill() is the last resort.
        Arguments:
        success -- 
        """
        def end_processCancelled(self, _r):
            return _M_ode.grid.ProcessCallback._op_processCancelled.end(self, _r)

        """
        kill() was called on this Process. If this does not
        succeed, there is nothing else that Blitz can do to
        stop its execution.
        Arguments:
        success -- 
        _ctx -- The request context for the invocation.
        """
        def processKilled(self, success, _ctx=None):
            return _M_ode.grid.ProcessCallback._op_processKilled.invoke(self, ((success, ), _ctx))

        """
        kill() was called on this Process. If this does not
        succeed, there is nothing else that Blitz can do to
        stop its execution.
        Arguments:
        success -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_processKilled(self, success, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ProcessCallback._op_processKilled.begin(self, ((success, ), _response, _ex, _sent, _ctx))

        """
        kill() was called on this Process. If this does not
        succeed, there is nothing else that Blitz can do to
        stop its execution.
        Arguments:
        success -- 
        """
        def end_processKilled(self, _r):
            return _M_ode.grid.ProcessCallback._op_processKilled.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ProcessCallbackPrx.ice_checkedCast(proxy, '::ode::grid::ProcessCallback', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ProcessCallbackPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ProcessCallback'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ProcessCallbackPrx = IcePy.defineProxy('::ode::grid::ProcessCallback', ProcessCallbackPrx)

    _M_ode.grid._t_ProcessCallback = IcePy.defineClass('::ode::grid::ProcessCallback', ProcessCallback, -1, (), True, False, None, (), ())
    ProcessCallback._ice_type = _M_ode.grid._t_ProcessCallback

    ProcessCallback._op_processFinished = IcePy.Operation('processFinished', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), None, ())
    ProcessCallback._op_processCancelled = IcePy.Operation('processCancelled', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())
    ProcessCallback._op_processKilled = IcePy.Operation('processKilled', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, ())

    _M_ode.grid.ProcessCallback = ProcessCallback
    del ProcessCallback

    _M_ode.grid.ProcessCallbackPrx = ProcessCallbackPrx
    del ProcessCallbackPrx

if 'Process' not in _M_ode.grid.__dict__:
    _M_ode.grid.Process = Ice.createTempClass()
    class Process(Ice.Object):
        """
        Thin wrapper around a system-level process. Most closely
        resembles Python's subprocess.Popen class.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.Process:
                raise RuntimeError('ode.grid.Process is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Process')

        def ice_id(self, current=None):
            return '::ode::grid::Process'

        def ice_staticId():
            return '::ode::grid::Process'
        ice_staticId = staticmethod(ice_staticId)

        def poll(self, current=None):
            """
            Returns the return code of the process, or null
            if unfinished.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def wait(self, current=None):
            """
            Blocks until poll() would return a non-null return code.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def cancel(self, current=None):
            """
            Signal to the Process that it should terminate. This may
            be done "softly" for a given time period.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def kill(self, current=None):
            """
            Terminate the Process immediately.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def shutdown(self, current=None):
            """
            First attempts cancel() several times and finally
            resorts to kill to force the process to shutdown
            cleanly. This method doesn't return any value or
            throw an exception so that it can be called oneway.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def registerCallback(self, cb, current=None):
            """
            Add a callback for end-of-life events
            Arguments:
            cb -- 
            current -- The Current object for the invocation.
            """
            pass

        def unregisterCallback(self, cb, current=None):
            """
            Remove a callback for end-of-life events
            Arguments:
            cb -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Process)

        __repr__ = __str__

    _M_ode.grid.ProcessPrx = Ice.createTempClass()
    class ProcessPrx(Ice.ObjectPrx):

        """
        Returns the return code of the process, or null
        if unfinished.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def poll(self, _ctx=None):
            return _M_ode.grid.Process._op_poll.invoke(self, ((), _ctx))

        """
        Returns the return code of the process, or null
        if unfinished.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_poll(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Process._op_poll.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the return code of the process, or null
        if unfinished.
        Arguments:
        """
        def end_poll(self, _r):
            return _M_ode.grid.Process._op_poll.end(self, _r)

        """
        Blocks until poll() would return a non-null return code.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def wait(self, _ctx=None):
            return _M_ode.grid.Process._op_wait.invoke(self, ((), _ctx))

        """
        Blocks until poll() would return a non-null return code.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_wait(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Process._op_wait.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Blocks until poll() would return a non-null return code.
        Arguments:
        """
        def end_wait(self, _r):
            return _M_ode.grid.Process._op_wait.end(self, _r)

        """
        Signal to the Process that it should terminate. This may
        be done "softly" for a given time period.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def cancel(self, _ctx=None):
            return _M_ode.grid.Process._op_cancel.invoke(self, ((), _ctx))

        """
        Signal to the Process that it should terminate. This may
        be done "softly" for a given time period.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_cancel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Process._op_cancel.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Signal to the Process that it should terminate. This may
        be done "softly" for a given time period.
        Arguments:
        """
        def end_cancel(self, _r):
            return _M_ode.grid.Process._op_cancel.end(self, _r)

        """
        Terminate the Process immediately.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def kill(self, _ctx=None):
            return _M_ode.grid.Process._op_kill.invoke(self, ((), _ctx))

        """
        Terminate the Process immediately.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_kill(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Process._op_kill.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Terminate the Process immediately.
        Arguments:
        """
        def end_kill(self, _r):
            return _M_ode.grid.Process._op_kill.end(self, _r)

        """
        First attempts cancel() several times and finally
        resorts to kill to force the process to shutdown
        cleanly. This method doesn't return any value or
        throw an exception so that it can be called oneway.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def shutdown(self, _ctx=None):
            return _M_ode.grid.Process._op_shutdown.invoke(self, ((), _ctx))

        """
        First attempts cancel() several times and finally
        resorts to kill to force the process to shutdown
        cleanly. This method doesn't return any value or
        throw an exception so that it can be called oneway.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_shutdown(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Process._op_shutdown.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        First attempts cancel() several times and finally
        resorts to kill to force the process to shutdown
        cleanly. This method doesn't return any value or
        throw an exception so that it can be called oneway.
        Arguments:
        """
        def end_shutdown(self, _r):
            return _M_ode.grid.Process._op_shutdown.end(self, _r)

        """
        Add a callback for end-of-life events
        Arguments:
        cb -- 
        _ctx -- The request context for the invocation.
        """
        def registerCallback(self, cb, _ctx=None):
            return _M_ode.grid.Process._op_registerCallback.invoke(self, ((cb, ), _ctx))

        """
        Add a callback for end-of-life events
        Arguments:
        cb -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_registerCallback(self, cb, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Process._op_registerCallback.begin(self, ((cb, ), _response, _ex, _sent, _ctx))

        """
        Add a callback for end-of-life events
        Arguments:
        cb -- 
        """
        def end_registerCallback(self, _r):
            return _M_ode.grid.Process._op_registerCallback.end(self, _r)

        """
        Remove a callback for end-of-life events
        Arguments:
        cb -- 
        _ctx -- The request context for the invocation.
        """
        def unregisterCallback(self, cb, _ctx=None):
            return _M_ode.grid.Process._op_unregisterCallback.invoke(self, ((cb, ), _ctx))

        """
        Remove a callback for end-of-life events
        Arguments:
        cb -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_unregisterCallback(self, cb, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Process._op_unregisterCallback.begin(self, ((cb, ), _response, _ex, _sent, _ctx))

        """
        Remove a callback for end-of-life events
        Arguments:
        cb -- 
        """
        def end_unregisterCallback(self, _r):
            return _M_ode.grid.Process._op_unregisterCallback.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ProcessPrx.ice_checkedCast(proxy, '::ode::grid::Process', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ProcessPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Process'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ProcessPrx = IcePy.defineProxy('::ode::grid::Process', ProcessPrx)

    _M_ode.grid._t_Process = IcePy.defineClass('::ode::grid::Process', Process, -1, (), True, False, None, (), ())
    Process._ice_type = _M_ode.grid._t_Process

    Process._op_poll = IcePy.Operation('poll', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), (_M_ode._t_ServerError,))
    Process._op_wait = IcePy.Operation('wait', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    Process._op_cancel = IcePy.Operation('cancel', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Process._op_kill = IcePy.Operation('kill', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_bool, False, 0), ())
    Process._op_shutdown = IcePy.Operation('shutdown', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Process._op_registerCallback = IcePy.Operation('registerCallback', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_ProcessCallbackPrx, False, 0),), (), None, (_M_ode._t_ServerError,))
    Process._op_unregisterCallback = IcePy.Operation('unregisterCallback', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_ProcessCallbackPrx, False, 0),), (), None, (_M_ode._t_ServerError,))

    _M_ode.grid.Process = Process
    del Process

    _M_ode.grid.ProcessPrx = ProcessPrx
    del ProcessPrx

if 'ScriptProcess' not in _M_ode.grid.__dict__:
    _M_ode.grid.ScriptProcess = Ice.createTempClass()
    class ScriptProcess(_M_ode.grid.Process):
        """
        Extension of the {@code Process} interface which is returned by
        {@code IScript} when an ode.model.ScriptJob is launched.
        It is critical that instances of (@code ScriptProcess} are closed
        on completion. See the close method for more information.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.ScriptProcess:
                raise RuntimeError('ode.grid.ScriptProcess is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Process', '::ode::grid::ScriptProcess')

        def ice_id(self, current=None):
            return '::ode::grid::ScriptProcess'

        def ice_staticId():
            return '::ode::grid::ScriptProcess'
        ice_staticId = staticmethod(ice_staticId)

        def getJob(self, current=None):
            """
            Returns the job which started this process. Several
            scheduling fields (submitted, scheduledFor, started, finished)
            may be of interest.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def getResults(self, waitSecs, current=None):
            """
            Returns the results immediately if present. If the process
            is not yet finished, waits "waitSecs" before throwing an
            ode.ApiUsageException. If poll has returned a non-null
            value, then this method will always return a non-null value.
            Arguments:
            waitSecs -- 
            current -- The Current object for the invocation.
            """
            pass

        def setMessage(self, message, current=None):
            """
            Sets the message on the ode.model.ScriptJob object.
            This value MAY be overwritten by the server if the script
            fails.
            Arguments:
            message -- 
            current -- The Current object for the invocation.
            """
            pass

        def close(self, detach, current=None):
            """
            Closes this process and frees server resources attached to it.
            If the detach argument is True, then the background process
            will continue executing. The user can reconnect to the process
            via the {@code IScript} service.
            If the detach argument is False, then the background process
            will be shutdown immediately, and all intermediate results
            (stdout, stderr, ...) will be uploaded.
            Arguments:
            detach -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ScriptProcess)

        __repr__ = __str__

    _M_ode.grid.ScriptProcessPrx = Ice.createTempClass()
    class ScriptProcessPrx(_M_ode.grid.ProcessPrx):

        """
        Returns the job which started this process. Several
        scheduling fields (submitted, scheduledFor, started, finished)
        may be of interest.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getJob(self, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_getJob.invoke(self, ((), _ctx))

        """
        Returns the job which started this process. Several
        scheduling fields (submitted, scheduledFor, started, finished)
        may be of interest.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getJob(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_getJob.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the job which started this process. Several
        scheduling fields (submitted, scheduledFor, started, finished)
        may be of interest.
        Arguments:
        """
        def end_getJob(self, _r):
            return _M_ode.grid.ScriptProcess._op_getJob.end(self, _r)

        """
        Returns the results immediately if present. If the process
        is not yet finished, waits "waitSecs" before throwing an
        ode.ApiUsageException. If poll has returned a non-null
        value, then this method will always return a non-null value.
        Arguments:
        waitSecs -- 
        _ctx -- The request context for the invocation.
        """
        def getResults(self, waitSecs, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_getResults.invoke(self, ((waitSecs, ), _ctx))

        """
        Returns the results immediately if present. If the process
        is not yet finished, waits "waitSecs" before throwing an
        ode.ApiUsageException. If poll has returned a non-null
        value, then this method will always return a non-null value.
        Arguments:
        waitSecs -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getResults(self, waitSecs, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_getResults.begin(self, ((waitSecs, ), _response, _ex, _sent, _ctx))

        """
        Returns the results immediately if present. If the process
        is not yet finished, waits "waitSecs" before throwing an
        ode.ApiUsageException. If poll has returned a non-null
        value, then this method will always return a non-null value.
        Arguments:
        waitSecs -- 
        """
        def end_getResults(self, _r):
            return _M_ode.grid.ScriptProcess._op_getResults.end(self, _r)

        """
        Sets the message on the ode.model.ScriptJob object.
        This value MAY be overwritten by the server if the script
        fails.
        Arguments:
        message -- 
        _ctx -- The request context for the invocation.
        """
        def setMessage(self, message, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_setMessage.invoke(self, ((message, ), _ctx))

        """
        Sets the message on the ode.model.ScriptJob object.
        This value MAY be overwritten by the server if the script
        fails.
        Arguments:
        message -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setMessage(self, message, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_setMessage.begin(self, ((message, ), _response, _ex, _sent, _ctx))

        """
        Sets the message on the ode.model.ScriptJob object.
        This value MAY be overwritten by the server if the script
        fails.
        Arguments:
        message -- 
        """
        def end_setMessage(self, _r):
            return _M_ode.grid.ScriptProcess._op_setMessage.end(self, _r)

        """
        Closes this process and frees server resources attached to it.
        If the detach argument is True, then the background process
        will continue executing. The user can reconnect to the process
        via the {@code IScript} service.
        If the detach argument is False, then the background process
        will be shutdown immediately, and all intermediate results
        (stdout, stderr, ...) will be uploaded.
        Arguments:
        detach -- 
        _ctx -- The request context for the invocation.
        """
        def close(self, detach, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_close.invoke(self, ((detach, ), _ctx))

        """
        Closes this process and frees server resources attached to it.
        If the detach argument is True, then the background process
        will continue executing. The user can reconnect to the process
        via the {@code IScript} service.
        If the detach argument is False, then the background process
        will be shutdown immediately, and all intermediate results
        (stdout, stderr, ...) will be uploaded.
        Arguments:
        detach -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_close(self, detach, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ScriptProcess._op_close.begin(self, ((detach, ), _response, _ex, _sent, _ctx))

        """
        Closes this process and frees server resources attached to it.
        If the detach argument is True, then the background process
        will continue executing. The user can reconnect to the process
        via the {@code IScript} service.
        If the detach argument is False, then the background process
        will be shutdown immediately, and all intermediate results
        (stdout, stderr, ...) will be uploaded.
        Arguments:
        detach -- 
        """
        def end_close(self, _r):
            return _M_ode.grid.ScriptProcess._op_close.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ScriptProcessPrx.ice_checkedCast(proxy, '::ode::grid::ScriptProcess', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ScriptProcessPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ScriptProcess'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ScriptProcessPrx = IcePy.defineProxy('::ode::grid::ScriptProcess', ScriptProcessPrx)

    _M_ode.grid._t_ScriptProcess = IcePy.defineClass('::ode::grid::ScriptProcess', ScriptProcess, -1, (), True, False, None, (_M_ode.grid._t_Process,), ())
    ScriptProcess._ice_type = _M_ode.grid._t_ScriptProcess

    ScriptProcess._op_getJob = IcePy.Operation('getJob', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.model._t_ScriptJob, False, 0), (_M_ode._t_ServerError,))
    ScriptProcess._op_getResults = IcePy.Operation('getResults', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode._t_RTypeDict, False, 0), (_M_ode._t_ServerError,))
    ScriptProcess._op_setMessage = IcePy.Operation('setMessage', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    ScriptProcess._op_close = IcePy.Operation('close', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_bool, False, 0),), (), None, (_M_ode._t_ServerError,))

    _M_ode.grid.ScriptProcess = ScriptProcess
    del ScriptProcess

    _M_ode.grid.ScriptProcessPrx = ScriptProcessPrx
    del ScriptProcessPrx

if 'Processor' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_Processor = IcePy.declareClass('::ode::grid::Processor')
    _M_ode.grid._t_ProcessorPrx = IcePy.declareProxy('::ode::grid::Processor')

if 'ProcessorCallback' not in _M_ode.grid.__dict__:
    _M_ode.grid.ProcessorCallback = Ice.createTempClass()
    class ProcessorCallback(Ice.Object):
        """
        Internal callback interface which is passed to the
        {@code Processor.accepts} method
        to query whether or not a processor will accept a certain operation.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.ProcessorCallback:
                raise RuntimeError('ode.grid.ProcessorCallback is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::ProcessorCallback')

        def ice_id(self, current=None):
            return '::ode::grid::ProcessorCallback'

        def ice_staticId():
            return '::ode::grid::ProcessorCallback'
        ice_staticId = staticmethod(ice_staticId)

        def isAccepted(self, accepted, sessionUuid, procConn, current=None):
            pass

        def isProxyAccepted(self, accepted, sessionUuid, procProxy, current=None):
            pass

        def responseRunning(self, jobIds, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ProcessorCallback)

        __repr__ = __str__

    _M_ode.grid.ProcessorCallbackPrx = Ice.createTempClass()
    class ProcessorCallbackPrx(Ice.ObjectPrx):

        def isAccepted(self, accepted, sessionUuid, procConn, _ctx=None):
            return _M_ode.grid.ProcessorCallback._op_isAccepted.invoke(self, ((accepted, sessionUuid, procConn), _ctx))

        def begin_isAccepted(self, accepted, sessionUuid, procConn, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ProcessorCallback._op_isAccepted.begin(self, ((accepted, sessionUuid, procConn), _response, _ex, _sent, _ctx))

        def end_isAccepted(self, _r):
            return _M_ode.grid.ProcessorCallback._op_isAccepted.end(self, _r)

        def isProxyAccepted(self, accepted, sessionUuid, procProxy, _ctx=None):
            return _M_ode.grid.ProcessorCallback._op_isProxyAccepted.invoke(self, ((accepted, sessionUuid, procProxy), _ctx))

        def begin_isProxyAccepted(self, accepted, sessionUuid, procProxy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ProcessorCallback._op_isProxyAccepted.begin(self, ((accepted, sessionUuid, procProxy), _response, _ex, _sent, _ctx))

        def end_isProxyAccepted(self, _r):
            return _M_ode.grid.ProcessorCallback._op_isProxyAccepted.end(self, _r)

        def responseRunning(self, jobIds, _ctx=None):
            return _M_ode.grid.ProcessorCallback._op_responseRunning.invoke(self, ((jobIds, ), _ctx))

        def begin_responseRunning(self, jobIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ProcessorCallback._op_responseRunning.begin(self, ((jobIds, ), _response, _ex, _sent, _ctx))

        def end_responseRunning(self, _r):
            return _M_ode.grid.ProcessorCallback._op_responseRunning.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ProcessorCallbackPrx.ice_checkedCast(proxy, '::ode::grid::ProcessorCallback', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ProcessorCallbackPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ProcessorCallback'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ProcessorCallbackPrx = IcePy.defineProxy('::ode::grid::ProcessorCallback', ProcessorCallbackPrx)

    _M_ode.grid._t_ProcessorCallback = IcePy.defineClass('::ode::grid::ProcessorCallback', ProcessorCallback, -1, (), True, False, None, (), ())
    ProcessorCallback._ice_type = _M_ode.grid._t_ProcessorCallback

    ProcessorCallback._op_isAccepted = IcePy.Operation('isAccepted', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_bool, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), None, ())
    ProcessorCallback._op_isProxyAccepted = IcePy.Operation('isProxyAccepted', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_bool, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode.grid._t_ProcessorPrx, False, 0)), (), None, ())
    ProcessorCallback._op_responseRunning = IcePy.Operation('responseRunning', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.api._t_LongList, False, 0),), (), None, ())

    _M_ode.grid.ProcessorCallback = ProcessorCallback
    del ProcessorCallback

    _M_ode.grid.ProcessorCallbackPrx = ProcessorCallbackPrx
    del ProcessorCallbackPrx

if 'Processor' not in _M_ode.grid.__dict__:
    _M_ode.grid.Processor = Ice.createTempClass()
    class Processor(Ice.Object):
        """
        Simple controller for Processes. Uses the session
        id given to create an Ice.Config file which is used
        as the sole argument to an execution of the given job.
        Jobs are responsible for loading arguments from the
        environment via the session id.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.Processor:
                raise RuntimeError('ode.grid.Processor is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Processor')

        def ice_id(self, current=None):
            return '::ode::grid::Processor'

        def ice_staticId():
            return '::ode::grid::Processor'
        ice_staticId = staticmethod(ice_staticId)

        def willAccept(self, userContext, groupContext, scriptContext, cb, current=None):
            """
            Called by ode.grid.SharedResources to find a suitable
            target for {@code ode.grid.SharedResources.acquireProcessor}.
            New processor instances are added to the checklist by using
            {@code ode.grid.SharedResources.addProcessor}. All processors
            must respond with their session uuid in order to authorize
            the action.
            Arguments:
            userContext -- 
            groupContext -- 
            scriptContext -- 
            cb -- 
            current -- The Current object for the invocation.
            """
            pass

        def requestRunning(self, cb, current=None):
            """
            Used by servers to find out what jobs are still active.
            Response will be sent to
            {@code ProcessorCallback.responseRunning}
            Arguments:
            cb -- 
            current -- The Current object for the invocation.
            """
            pass

        def parseJob(self, session, jobObject, current=None):
            """
            Parses a job and returns metadata definition required
            for properly submitting the job. This object will be
            cached by the server, and passed back into {@code processJob}
            Arguments:
            session -- 
            jobObject -- 
            current -- The Current object for the invocation.
            """
            pass

        def processJob(self, session, params, jobObject, current=None):
            """
            Starts a process based on the given job
            If this processor cannot handle the given job, a
            null process will be returned. The {@code params} argument
            was created by a previously call to {@code parseJob}.
            Arguments:
            session -- 
            params -- 
            jobObject -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Processor)

        __repr__ = __str__

    _M_ode.grid.ProcessorPrx = Ice.createTempClass()
    class ProcessorPrx(Ice.ObjectPrx):

        """
        Called by ode.grid.SharedResources to find a suitable
        target for {@code ode.grid.SharedResources.acquireProcessor}.
        New processor instances are added to the checklist by using
        {@code ode.grid.SharedResources.addProcessor}. All processors
        must respond with their session uuid in order to authorize
        the action.
        Arguments:
        userContext -- 
        groupContext -- 
        scriptContext -- 
        cb -- 
        _ctx -- The request context for the invocation.
        """
        def willAccept(self, userContext, groupContext, scriptContext, cb, _ctx=None):
            return _M_ode.grid.Processor._op_willAccept.invoke(self, ((userContext, groupContext, scriptContext, cb), _ctx))

        """
        Called by ode.grid.SharedResources to find a suitable
        target for {@code ode.grid.SharedResources.acquireProcessor}.
        New processor instances are added to the checklist by using
        {@code ode.grid.SharedResources.addProcessor}. All processors
        must respond with their session uuid in order to authorize
        the action.
        Arguments:
        userContext -- 
        groupContext -- 
        scriptContext -- 
        cb -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_willAccept(self, userContext, groupContext, scriptContext, cb, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Processor._op_willAccept.begin(self, ((userContext, groupContext, scriptContext, cb), _response, _ex, _sent, _ctx))

        """
        Called by ode.grid.SharedResources to find a suitable
        target for {@code ode.grid.SharedResources.acquireProcessor}.
        New processor instances are added to the checklist by using
        {@code ode.grid.SharedResources.addProcessor}. All processors
        must respond with their session uuid in order to authorize
        the action.
        Arguments:
        userContext -- 
        groupContext -- 
        scriptContext -- 
        cb -- 
        """
        def end_willAccept(self, _r):
            return _M_ode.grid.Processor._op_willAccept.end(self, _r)

        """
        Used by servers to find out what jobs are still active.
        Response will be sent to
        {@code ProcessorCallback.responseRunning}
        Arguments:
        cb -- 
        _ctx -- The request context for the invocation.
        """
        def requestRunning(self, cb, _ctx=None):
            return _M_ode.grid.Processor._op_requestRunning.invoke(self, ((cb, ), _ctx))

        """
        Used by servers to find out what jobs are still active.
        Response will be sent to
        {@code ProcessorCallback.responseRunning}
        Arguments:
        cb -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_requestRunning(self, cb, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Processor._op_requestRunning.begin(self, ((cb, ), _response, _ex, _sent, _ctx))

        """
        Used by servers to find out what jobs are still active.
        Response will be sent to
        {@code ProcessorCallback.responseRunning}
        Arguments:
        cb -- 
        """
        def end_requestRunning(self, _r):
            return _M_ode.grid.Processor._op_requestRunning.end(self, _r)

        """
        Parses a job and returns metadata definition required
        for properly submitting the job. This object will be
        cached by the server, and passed back into {@code processJob}
        Arguments:
        session -- 
        jobObject -- 
        _ctx -- The request context for the invocation.
        """
        def parseJob(self, session, jobObject, _ctx=None):
            return _M_ode.grid.Processor._op_parseJob.invoke(self, ((session, jobObject), _ctx))

        """
        Parses a job and returns metadata definition required
        for properly submitting the job. This object will be
        cached by the server, and passed back into {@code processJob}
        Arguments:
        session -- 
        jobObject -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_parseJob(self, session, jobObject, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Processor._op_parseJob.begin(self, ((session, jobObject), _response, _ex, _sent, _ctx))

        """
        Parses a job and returns metadata definition required
        for properly submitting the job. This object will be
        cached by the server, and passed back into {@code processJob}
        Arguments:
        session -- 
        jobObject -- 
        """
        def end_parseJob(self, _r):
            return _M_ode.grid.Processor._op_parseJob.end(self, _r)

        """
        Starts a process based on the given job
        If this processor cannot handle the given job, a
        null process will be returned. The {@code params} argument
        was created by a previously call to {@code parseJob}.
        Arguments:
        session -- 
        params -- 
        jobObject -- 
        _ctx -- The request context for the invocation.
        """
        def processJob(self, session, params, jobObject, _ctx=None):
            return _M_ode.grid.Processor._op_processJob.invoke(self, ((session, params, jobObject), _ctx))

        """
        Starts a process based on the given job
        If this processor cannot handle the given job, a
        null process will be returned. The {@code params} argument
        was created by a previously call to {@code parseJob}.
        Arguments:
        session -- 
        params -- 
        jobObject -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_processJob(self, session, params, jobObject, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Processor._op_processJob.begin(self, ((session, params, jobObject), _response, _ex, _sent, _ctx))

        """
        Starts a process based on the given job
        If this processor cannot handle the given job, a
        null process will be returned. The {@code params} argument
        was created by a previously call to {@code parseJob}.
        Arguments:
        session -- 
        params -- 
        jobObject -- 
        """
        def end_processJob(self, _r):
            return _M_ode.grid.Processor._op_processJob.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ProcessorPrx.ice_checkedCast(proxy, '::ode::grid::Processor', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ProcessorPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Processor'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ProcessorPrx = IcePy.defineProxy('::ode::grid::Processor', ProcessorPrx)

    _M_ode.grid._t_Processor = IcePy.defineClass('::ode::grid::Processor', Processor, -1, (), True, False, None, (), ())
    Processor._ice_type = _M_ode.grid._t_Processor

    Processor._op_willAccept = IcePy.Operation('willAccept', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.model._t_Experimenter, False, 0), ((), _M_ode.model._t_ExperimenterGroup, False, 0), ((), _M_ode.model._t_Job, False, 0), ((), _M_ode.grid._t_ProcessorCallbackPrx, False, 0)), (), None, ())
    Processor._op_requestRunning = IcePy.Operation('requestRunning', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.grid._t_ProcessorCallbackPrx, False, 0),), (), None, ())
    Processor._op_parseJob = IcePy.Operation('parseJob', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.model._t_Job, False, 0)), (), ((), _M_ode.grid._t_JobParams, False, 0), (_M_ode._t_ServerError,))
    Processor._op_processJob = IcePy.Operation('processJob', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.grid._t_JobParams, False, 0), ((), _M_ode.model._t_Job, False, 0)), (), ((), _M_ode.grid._t_ProcessPrx, False, 0), (_M_ode._t_ServerError,))

    _M_ode.grid.Processor = Processor
    del Processor

    _M_ode.grid.ProcessorPrx = ProcessorPrx
    del ProcessorPrx

if 'InteractiveProcessor' not in _M_ode.grid.__dict__:
    _M_ode.grid.InteractiveProcessor = Ice.createTempClass()
    class InteractiveProcessor(Ice.Object):
        """
        Client facing interface to the background processing
        framework. If a user needs interactivity, one of these
        processors should be acquired from the ServiceFactory.
        Otherwise, a Job can be submitted via JobHandle.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.InteractiveProcessor:
                raise RuntimeError('ode.grid.InteractiveProcessor is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::InteractiveProcessor')

        def ice_id(self, current=None):
            return '::ode::grid::InteractiveProcessor'

        def ice_staticId():
            return '::ode::grid::InteractiveProcessor'
        ice_staticId = staticmethod(ice_staticId)

        def expires(self, current=None):
            """
            Returns the system clock time in milliseconds since the epoch
            at which this processor will be reaped.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def getJob(self, current=None):
            """
            Returns the job which defines this processor. This may be
            only the last job associated with the processor if execute
            is called multiple times.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def params(self, current=None):
            """
            Retrieves the parameters needed to be passed in an execution
            and the results which will be passed back out.
            This method is guaranteed to return a non-null value or throw an exception.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def execute(self, inputs, current=None):
            """
            Executes an instance of the job returned by getJob() using
            the given map as inputs.
            Arguments:
            inputs -- 
            current -- The Current object for the invocation.
            """
            pass

        def getResults(self, proc, current=None):
            """
            Retrieve the results for the given process. This will throw
            an ApiUsageException if called before the process has returned.
            Use either process.poll() or process.wait() or a ProcessCallback
            to wait for completion before calling.
            If the user has not overridden or disabled the output values
            "stdout" and "stderr", these will be filled with the OriginalFile
            instances uploaded after completion under the key values of the
            same name.
            Arguments:
            proc -- 
            current -- The Current object for the invocation.
            """
            pass

        def setDetach(self, detach, current=None):
            """
            Sets whether or not cancel will be called on the current
            Process on stop. If detach is true, then the
            Process will continue running. Otherwise,
            Process#cancel will be called, before preparing for
            another run.
            false by default
            Arguments:
            detach -- 
            current -- The Current object for the invocation.
            """
            pass

        def stop(self, current=None):
            """
            Clears the current execution of ode.model.Job from
            the processor to prepare for another execution.
            cancel() will be called on the current Process
            if detach is set to false.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_InteractiveProcessor)

        __repr__ = __str__

    _M_ode.grid.InteractiveProcessorPrx = Ice.createTempClass()
    class InteractiveProcessorPrx(Ice.ObjectPrx):

        """
        Returns the system clock time in milliseconds since the epoch
        at which this processor will be reaped.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def expires(self, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_expires.invoke(self, ((), _ctx))

        """
        Returns the system clock time in milliseconds since the epoch
        at which this processor will be reaped.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_expires(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_expires.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the system clock time in milliseconds since the epoch
        at which this processor will be reaped.
        Arguments:
        """
        def end_expires(self, _r):
            return _M_ode.grid.InteractiveProcessor._op_expires.end(self, _r)

        """
        Returns the job which defines this processor. This may be
        only the last job associated with the processor if execute
        is called multiple times.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getJob(self, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_getJob.invoke(self, ((), _ctx))

        """
        Returns the job which defines this processor. This may be
        only the last job associated with the processor if execute
        is called multiple times.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getJob(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_getJob.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the job which defines this processor. This may be
        only the last job associated with the processor if execute
        is called multiple times.
        Arguments:
        """
        def end_getJob(self, _r):
            return _M_ode.grid.InteractiveProcessor._op_getJob.end(self, _r)

        """
        Retrieves the parameters needed to be passed in an execution
        and the results which will be passed back out.
        This method is guaranteed to return a non-null value or throw an exception.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def params(self, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_params.invoke(self, ((), _ctx))

        """
        Retrieves the parameters needed to be passed in an execution
        and the results which will be passed back out.
        This method is guaranteed to return a non-null value or throw an exception.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_params(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_params.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the parameters needed to be passed in an execution
        and the results which will be passed back out.
        This method is guaranteed to return a non-null value or throw an exception.
        Arguments:
        """
        def end_params(self, _r):
            return _M_ode.grid.InteractiveProcessor._op_params.end(self, _r)

        """
        Executes an instance of the job returned by getJob() using
        the given map as inputs.
        Arguments:
        inputs -- 
        _ctx -- The request context for the invocation.
        """
        def execute(self, inputs, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_execute.invoke(self, ((inputs, ), _ctx))

        """
        Executes an instance of the job returned by getJob() using
        the given map as inputs.
        Arguments:
        inputs -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_execute(self, inputs, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_execute.begin(self, ((inputs, ), _response, _ex, _sent, _ctx))

        """
        Executes an instance of the job returned by getJob() using
        the given map as inputs.
        Arguments:
        inputs -- 
        """
        def end_execute(self, _r):
            return _M_ode.grid.InteractiveProcessor._op_execute.end(self, _r)

        """
        Retrieve the results for the given process. This will throw
        an ApiUsageException if called before the process has returned.
        Use either process.poll() or process.wait() or a ProcessCallback
        to wait for completion before calling.
        If the user has not overridden or disabled the output values
        "stdout" and "stderr", these will be filled with the OriginalFile
        instances uploaded after completion under the key values of the
        same name.
        Arguments:
        proc -- 
        _ctx -- The request context for the invocation.
        """
        def getResults(self, proc, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_getResults.invoke(self, ((proc, ), _ctx))

        """
        Retrieve the results for the given process. This will throw
        an ApiUsageException if called before the process has returned.
        Use either process.poll() or process.wait() or a ProcessCallback
        to wait for completion before calling.
        If the user has not overridden or disabled the output values
        "stdout" and "stderr", these will be filled with the OriginalFile
        instances uploaded after completion under the key values of the
        same name.
        Arguments:
        proc -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getResults(self, proc, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_getResults.begin(self, ((proc, ), _response, _ex, _sent, _ctx))

        """
        Retrieve the results for the given process. This will throw
        an ApiUsageException if called before the process has returned.
        Use either process.poll() or process.wait() or a ProcessCallback
        to wait for completion before calling.
        If the user has not overridden or disabled the output values
        "stdout" and "stderr", these will be filled with the OriginalFile
        instances uploaded after completion under the key values of the
        same name.
        Arguments:
        proc -- 
        """
        def end_getResults(self, _r):
            return _M_ode.grid.InteractiveProcessor._op_getResults.end(self, _r)

        """
        Sets whether or not cancel will be called on the current
        Process on stop. If detach is true, then the
        Process will continue running. Otherwise,
        Process#cancel will be called, before preparing for
        another run.
        false by default
        Arguments:
        detach -- 
        _ctx -- The request context for the invocation.
        """
        def setDetach(self, detach, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_setDetach.invoke(self, ((detach, ), _ctx))

        """
        Sets whether or not cancel will be called on the current
        Process on stop. If detach is true, then the
        Process will continue running. Otherwise,
        Process#cancel will be called, before preparing for
        another run.
        false by default
        Arguments:
        detach -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setDetach(self, detach, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_setDetach.begin(self, ((detach, ), _response, _ex, _sent, _ctx))

        """
        Sets whether or not cancel will be called on the current
        Process on stop. If detach is true, then the
        Process will continue running. Otherwise,
        Process#cancel will be called, before preparing for
        another run.
        false by default
        Arguments:
        detach -- 
        """
        def end_setDetach(self, _r):
            return _M_ode.grid.InteractiveProcessor._op_setDetach.end(self, _r)

        """
        Clears the current execution of ode.model.Job from
        the processor to prepare for another execution.
        cancel() will be called on the current Process
        if detach is set to false.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def stop(self, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_stop.invoke(self, ((), _ctx))

        """
        Clears the current execution of ode.model.Job from
        the processor to prepare for another execution.
        cancel() will be called on the current Process
        if detach is set to false.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_stop(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessor._op_stop.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Clears the current execution of ode.model.Job from
        the processor to prepare for another execution.
        cancel() will be called on the current Process
        if detach is set to false.
        Arguments:
        """
        def end_stop(self, _r):
            return _M_ode.grid.InteractiveProcessor._op_stop.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.InteractiveProcessorPrx.ice_checkedCast(proxy, '::ode::grid::InteractiveProcessor', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.InteractiveProcessorPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::InteractiveProcessor'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_InteractiveProcessorPrx = IcePy.defineProxy('::ode::grid::InteractiveProcessor', InteractiveProcessorPrx)

    _M_ode.grid._t_InteractiveProcessor = IcePy.defineClass('::ode::grid::InteractiveProcessor', InteractiveProcessor, -1, (), True, False, None, (), ())
    InteractiveProcessor._ice_type = _M_ode.grid._t_InteractiveProcessor

    InteractiveProcessor._op_expires = IcePy.Operation('expires', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), IcePy._t_long, False, 0), ())
    InteractiveProcessor._op_getJob = IcePy.Operation('getJob', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.model._t_Job, False, 0), ())
    InteractiveProcessor._op_params = IcePy.Operation('params', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), ((), _M_ode.grid._t_JobParams, False, 0), (_M_ode._t_ServerError,))
    InteractiveProcessor._op_execute = IcePy.Operation('execute', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RMap, False, 0),), (), ((), _M_ode.grid._t_ProcessPrx, False, 0), (_M_ode._t_ServerError,))
    InteractiveProcessor._op_getResults = IcePy.Operation('getResults', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), _M_ode.grid._t_ProcessPrx, False, 0),), (), ((), _M_ode._t_RMap, False, 0), (_M_ode._t_ServerError,))
    InteractiveProcessor._op_setDetach = IcePy.Operation('setDetach', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_bool, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    InteractiveProcessor._op_stop = IcePy.Operation('stop', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (), (), None, (_M_ode._t_ServerError,))

    _M_ode.grid.InteractiveProcessor = InteractiveProcessor
    del InteractiveProcessor

    _M_ode.grid.InteractiveProcessorPrx = InteractiveProcessorPrx
    del InteractiveProcessorPrx

if '_t_InteractiveProcessorList' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_InteractiveProcessorList = IcePy.defineSequence('::ode::grid::InteractiveProcessorList', (), _M_ode.grid._t_InteractiveProcessorPrx)

# End of module ode.grid

__name__ = 'ode'

# End of module ode
