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
import ode_ServerErrors_ice
import ode_Collections_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Start of module ode
__name__ = 'ode'

# Start of module ode.romio
_M_ode.romio = Ice.openModule('ode.romio')
__name__ = 'ode.romio'
_M_ode.romio.__doc__ = """
Primitives for working with binary data.
"""

if '_t_RGBBands' not in _M_ode.romio.__dict__:
    _M_ode.romio._t_RGBBands = IcePy.defineSequence('::ode::romio::RGBBands', (), _M_Ice._t_ByteSeq)

_M_ode.romio.RedBand = 0

_M_ode.romio.GreenBand = 1

_M_ode.romio.BlueBand = 2

if 'RGBBuffer' not in _M_ode.romio.__dict__:
    _M_ode.romio.RGBBuffer = Ice.createTempClass()
    class RGBBuffer(Ice.Object):
        def __init__(self, bands=None, sizeX1=0, sizeX2=0):
            self.bands = bands
            self.sizeX1 = sizeX1
            self.sizeX2 = sizeX2

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::romio::RGBBuffer')

        def ice_id(self, current=None):
            return '::ode::romio::RGBBuffer'

        def ice_staticId():
            return '::ode::romio::RGBBuffer'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.romio._t_RGBBuffer)

        __repr__ = __str__

    _M_ode.romio.RGBBufferPrx = Ice.createTempClass()
    class RGBBufferPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.romio.RGBBufferPrx.ice_checkedCast(proxy, '::ode::romio::RGBBuffer', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.romio.RGBBufferPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::romio::RGBBuffer'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.romio._t_RGBBufferPrx = IcePy.defineProxy('::ode::romio::RGBBuffer', RGBBufferPrx)

    _M_ode.romio._t_RGBBuffer = IcePy.defineClass('::ode::romio::RGBBuffer', RGBBuffer, -1, (), False, False, None, (), (
        ('bands', (), _M_ode.romio._t_RGBBands, False, 0),
        ('sizeX1', (), IcePy._t_int, False, 0),
        ('sizeX2', (), IcePy._t_int, False, 0)
    ))
    RGBBuffer._ice_type = _M_ode.romio._t_RGBBuffer

    _M_ode.romio.RGBBuffer = RGBBuffer
    del RGBBuffer

    _M_ode.romio.RGBBufferPrx = RGBBufferPrx
    del RGBBufferPrx

if 'RegionDef' not in _M_ode.romio.__dict__:
    _M_ode.romio.RegionDef = Ice.createTempClass()
    class RegionDef(Ice.Object):
        def __init__(self, x=0, y=0, width=0, height=0):
            self.x = x
            self.y = y
            self.width = width
            self.height = height

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::romio::RegionDef')

        def ice_id(self, current=None):
            return '::ode::romio::RegionDef'

        def ice_staticId():
            return '::ode::romio::RegionDef'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.romio._t_RegionDef)

        __repr__ = __str__

    _M_ode.romio.RegionDefPrx = Ice.createTempClass()
    class RegionDefPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.romio.RegionDefPrx.ice_checkedCast(proxy, '::ode::romio::RegionDef', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.romio.RegionDefPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::romio::RegionDef'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.romio._t_RegionDefPrx = IcePy.defineProxy('::ode::romio::RegionDef', RegionDefPrx)

    _M_ode.romio._t_RegionDef = IcePy.defineClass('::ode::romio::RegionDef', RegionDef, -1, (), False, False, None, (), (
        ('x', (), IcePy._t_int, False, 0),
        ('y', (), IcePy._t_int, False, 0),
        ('width', (), IcePy._t_int, False, 0),
        ('height', (), IcePy._t_int, False, 0)
    ))
    RegionDef._ice_type = _M_ode.romio._t_RegionDef

    _M_ode.romio.RegionDef = RegionDef
    del RegionDef

    _M_ode.romio.RegionDefPrx = RegionDefPrx
    del RegionDefPrx

_M_ode.romio.XY = 0

_M_ode.romio.ZY = 1

_M_ode.romio.XZ = 2

if 'PlaneDef' not in _M_ode.romio.__dict__:
    _M_ode.romio.PlaneDef = Ice.createTempClass()
    class PlaneDef(Ice.Object):
        def __init__(self, slice=0, x=0, y=0, z=0, t=0, region=None, stride=0):
            self.slice = slice
            self.x = x
            self.y = y
            self.z = z
            self.t = t
            self.region = region
            self.stride = stride

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::romio::PlaneDef')

        def ice_id(self, current=None):
            return '::ode::romio::PlaneDef'

        def ice_staticId():
            return '::ode::romio::PlaneDef'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.romio._t_PlaneDef)

        __repr__ = __str__

    _M_ode.romio.PlaneDefPrx = Ice.createTempClass()
    class PlaneDefPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.romio.PlaneDefPrx.ice_checkedCast(proxy, '::ode::romio::PlaneDef', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.romio.PlaneDefPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::romio::PlaneDef'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.romio._t_PlaneDefPrx = IcePy.defineProxy('::ode::romio::PlaneDef', PlaneDefPrx)

    _M_ode.romio._t_PlaneDef = IcePy.declareClass('::ode::romio::PlaneDef')

    _M_ode.romio._t_PlaneDef = IcePy.defineClass('::ode::romio::PlaneDef', PlaneDef, -1, (), False, False, None, (), (
        ('slice', (), IcePy._t_int, False, 0),
        ('x', (), IcePy._t_int, False, 0),
        ('y', (), IcePy._t_int, False, 0),
        ('z', (), IcePy._t_int, False, 0),
        ('t', (), IcePy._t_int, False, 0),
        ('region', (), _M_ode.romio._t_RegionDef, False, 0),
        ('stride', (), IcePy._t_int, False, 0)
    ))
    PlaneDef._ice_type = _M_ode.romio._t_PlaneDef

    _M_ode.romio.PlaneDef = PlaneDef
    del PlaneDef

    _M_ode.romio.PlaneDefPrx = PlaneDefPrx
    del PlaneDefPrx

if 'PlaneDefWithMasks' not in _M_ode.romio.__dict__:
    _M_ode.romio.PlaneDefWithMasks = Ice.createTempClass()
    class PlaneDefWithMasks(_M_ode.romio.PlaneDef):
        """
        Extends PlaneDef by an option to toggle server side Mask rendering. By
        default all masks attached to the image fulfilling rendering criteria,
        will be rendered. This criteria is currently masks with a width and
        height equal to those of the image.
        Members:
        shapeIds -- Optional (currently unimplemented) list of Masks to render.
        """
        def __init__(self, slice=0, x=0, y=0, z=0, t=0, region=None, stride=0, shapeIds=None):
            _M_ode.romio.PlaneDef.__init__(self, slice, x, y, z, t, region, stride)
            self.shapeIds = shapeIds

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::romio::PlaneDef', '::ode::romio::PlaneDefWithMasks')

        def ice_id(self, current=None):
            return '::ode::romio::PlaneDefWithMasks'

        def ice_staticId():
            return '::ode::romio::PlaneDefWithMasks'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.romio._t_PlaneDefWithMasks)

        __repr__ = __str__

    _M_ode.romio.PlaneDefWithMasksPrx = Ice.createTempClass()
    class PlaneDefWithMasksPrx(_M_ode.romio.PlaneDefPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.romio.PlaneDefWithMasksPrx.ice_checkedCast(proxy, '::ode::romio::PlaneDefWithMasks', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.romio.PlaneDefWithMasksPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::romio::PlaneDefWithMasks'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.romio._t_PlaneDefWithMasksPrx = IcePy.defineProxy('::ode::romio::PlaneDefWithMasks', PlaneDefWithMasksPrx)

    _M_ode.romio._t_PlaneDefWithMasks = IcePy.declareClass('::ode::romio::PlaneDefWithMasks')

    _M_ode.romio._t_PlaneDefWithMasks = IcePy.defineClass('::ode::romio::PlaneDefWithMasks', PlaneDefWithMasks, -1, (), False, False, _M_ode.romio._t_PlaneDef, (), (('shapeIds', (), _M_ode.api._t_LongList, False, 0),))
    PlaneDefWithMasks._ice_type = _M_ode.romio._t_PlaneDefWithMasks

    _M_ode.romio.PlaneDefWithMasks = PlaneDefWithMasks
    del PlaneDefWithMasks

    _M_ode.romio.PlaneDefWithMasksPrx = PlaneDefWithMasksPrx
    del PlaneDefWithMasksPrx

if 'CodomainMapContext' not in _M_ode.romio.__dict__:
    _M_ode.romio.CodomainMapContext = Ice.createTempClass()
    class CodomainMapContext(Ice.Object):
        def __init__(self):
            pass

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::romio::CodomainMapContext')

        def ice_id(self, current=None):
            return '::ode::romio::CodomainMapContext'

        def ice_staticId():
            return '::ode::romio::CodomainMapContext'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.romio._t_CodomainMapContext)

        __repr__ = __str__

    _M_ode.romio.CodomainMapContextPrx = Ice.createTempClass()
    class CodomainMapContextPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.romio.CodomainMapContextPrx.ice_checkedCast(proxy, '::ode::romio::CodomainMapContext', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.romio.CodomainMapContextPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::romio::CodomainMapContext'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.romio._t_CodomainMapContextPrx = IcePy.defineProxy('::ode::romio::CodomainMapContext', CodomainMapContextPrx)

    _M_ode.romio._t_CodomainMapContext = IcePy.defineClass('::ode::romio::CodomainMapContext', CodomainMapContext, -1, (), False, False, None, (), ())
    CodomainMapContext._ice_type = _M_ode.romio._t_CodomainMapContext

    _M_ode.romio.CodomainMapContext = CodomainMapContext
    del CodomainMapContext

    _M_ode.romio.CodomainMapContextPrx = CodomainMapContextPrx
    del CodomainMapContextPrx

if 'ReverseIntensityMapContext' not in _M_ode.romio.__dict__:
    _M_ode.romio.ReverseIntensityMapContext = Ice.createTempClass()
    class ReverseIntensityMapContext(_M_ode.romio.CodomainMapContext):
        """
        The reverse intensity.
        """
        def __init__(self):
            _M_ode.romio.CodomainMapContext.__init__(self)

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::romio::CodomainMapContext', '::ode::romio::ReverseIntensityMapContext')

        def ice_id(self, current=None):
            return '::ode::romio::ReverseIntensityMapContext'

        def ice_staticId():
            return '::ode::romio::ReverseIntensityMapContext'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.romio._t_ReverseIntensityMapContext)

        __repr__ = __str__

    _M_ode.romio.ReverseIntensityMapContextPrx = Ice.createTempClass()
    class ReverseIntensityMapContextPrx(_M_ode.romio.CodomainMapContextPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.romio.ReverseIntensityMapContextPrx.ice_checkedCast(proxy, '::ode::romio::ReverseIntensityMapContext', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.romio.ReverseIntensityMapContextPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::romio::ReverseIntensityMapContext'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.romio._t_ReverseIntensityMapContextPrx = IcePy.defineProxy('::ode::romio::ReverseIntensityMapContext', ReverseIntensityMapContextPrx)

    _M_ode.romio._t_ReverseIntensityMapContext = IcePy.defineClass('::ode::romio::ReverseIntensityMapContext', ReverseIntensityMapContext, -1, (), False, False, _M_ode.romio._t_CodomainMapContext, (), ())
    ReverseIntensityMapContext._ice_type = _M_ode.romio._t_ReverseIntensityMapContext

    _M_ode.romio.ReverseIntensityMapContext = ReverseIntensityMapContext
    del ReverseIntensityMapContext

    _M_ode.romio.ReverseIntensityMapContextPrx = ReverseIntensityMapContextPrx
    del ReverseIntensityMapContextPrx

# End of module ode.romio

__name__ = 'ode'

# End of module ode
