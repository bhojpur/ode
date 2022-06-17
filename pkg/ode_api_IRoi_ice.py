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
import ode_ServicesF_ice
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

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'RoiOptions' not in _M_ode.api.__dict__:
    _M_ode.api.RoiOptions = Ice.createTempClass()
    class RoiOptions(Ice.Object):
        """
        Specifies filters used when querying the ROIs.
        """
        def __init__(self, shapes=None, limit=None, offset=None, userId=None, groupId=None):
            self.shapes = shapes
            self.limit = limit
            self.offset = offset
            self.userId = userId
            self.groupId = groupId

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::RoiOptions')

        def ice_id(self, current=None):
            return '::ode::api::RoiOptions'

        def ice_staticId():
            return '::ode::api::RoiOptions'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_RoiOptions)

        __repr__ = __str__

    _M_ode.api.RoiOptionsPrx = Ice.createTempClass()
    class RoiOptionsPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.RoiOptionsPrx.ice_checkedCast(proxy, '::ode::api::RoiOptions', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.RoiOptionsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::RoiOptions'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_RoiOptionsPrx = IcePy.defineProxy('::ode::api::RoiOptions', RoiOptionsPrx)

    _M_ode.api._t_RoiOptions = IcePy.declareClass('::ode::api::RoiOptions')

    _M_ode.api._t_RoiOptions = IcePy.defineClass('::ode::api::RoiOptions', RoiOptions, -1, (), False, False, None, (), (
        ('shapes', (), _M_ode.api._t_StringSet, False, 0),
        ('limit', (), _M_ode._t_RInt, False, 0),
        ('offset', (), _M_ode._t_RInt, False, 0),
        ('userId', (), _M_ode._t_RLong, False, 0),
        ('groupId', (), _M_ode._t_RLong, False, 0)
    ))
    RoiOptions._ice_type = _M_ode.api._t_RoiOptions

    _M_ode.api.RoiOptions = RoiOptions
    del RoiOptions

    _M_ode.api.RoiOptionsPrx = RoiOptionsPrx
    del RoiOptionsPrx

if 'RoiResult' not in _M_ode.api.__dict__:
    _M_ode.api.RoiResult = Ice.createTempClass()
    class RoiResult(Ice.Object):
        """
        Returned by most search methods. The RoiOptions is the options object passed
        into a method, possibly modified by the server if some value was out of range.
        The RoiList contains all the Rois which matched the given query.
        The individual shapes of the Rois which matched can be found in the indexes.
        For example, all the shapes on z=1 can by found by:
        ShapeList shapes = byZ.get(1);
        Shapes which are found on all z or t can be found with:
        byZ.get(-1);
        byT.get(-1);
        respectively.
        """
        def __init__(self, opts=None, rois=None, byZ=None, byT=None):
            self.opts = opts
            self.rois = rois
            self.byZ = byZ
            self.byT = byT

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::RoiResult')

        def ice_id(self, current=None):
            return '::ode::api::RoiResult'

        def ice_staticId():
            return '::ode::api::RoiResult'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_RoiResult)

        __repr__ = __str__

    _M_ode.api.RoiResultPrx = Ice.createTempClass()
    class RoiResultPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.RoiResultPrx.ice_checkedCast(proxy, '::ode::api::RoiResult', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.RoiResultPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::RoiResult'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_RoiResultPrx = IcePy.defineProxy('::ode::api::RoiResult', RoiResultPrx)

    _M_ode.api._t_RoiResult = IcePy.declareClass('::ode::api::RoiResult')

    _M_ode.api._t_RoiResult = IcePy.defineClass('::ode::api::RoiResult', RoiResult, -1, (), False, False, None, (), (
        ('opts', (), _M_ode.api._t_RoiOptions, False, 0),
        ('rois', (), _M_ode.api._t_RoiList, False, 0),
        ('byZ', (), _M_ode.api._t_IntShapeListMap, False, 0),
        ('byT', (), _M_ode.api._t_IntShapeListMap, False, 0)
    ))
    RoiResult._ice_type = _M_ode.api._t_RoiResult

    _M_ode.api.RoiResult = RoiResult
    del RoiResult

    _M_ode.api.RoiResultPrx = RoiResultPrx
    del RoiResultPrx

if 'ShapePoints' not in _M_ode.api.__dict__:
    _M_ode.api.ShapePoints = Ice.createTempClass()
    class ShapePoints(Ice.Object):
        """
        Contains a discrete representation of the geometry of
        an ode::model::Shape. The x and y array are of the
        same size with each pair of entries representing a
        single point in the 2D plane.
        """
        def __init__(self, x=None, y=None):
            self.x = x
            self.y = y

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ShapePoints')

        def ice_id(self, current=None):
            return '::ode::api::ShapePoints'

        def ice_staticId():
            return '::ode::api::ShapePoints'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ShapePoints)

        __repr__ = __str__

    _M_ode.api.ShapePointsPrx = Ice.createTempClass()
    class ShapePointsPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ShapePointsPrx.ice_checkedCast(proxy, '::ode::api::ShapePoints', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ShapePointsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ShapePoints'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ShapePointsPrx = IcePy.defineProxy('::ode::api::ShapePoints', ShapePointsPrx)

    _M_ode.api._t_ShapePoints = IcePy.defineClass('::ode::api::ShapePoints', ShapePoints, -1, (), False, False, None, (), (
        ('x', (), _M_ode.api._t_IntegerArray, False, 0),
        ('y', (), _M_ode.api._t_IntegerArray, False, 0)
    ))
    ShapePoints._ice_type = _M_ode.api._t_ShapePoints

    _M_ode.api.ShapePoints = ShapePoints
    del ShapePoints

    _M_ode.api.ShapePointsPrx = ShapePointsPrx
    del ShapePointsPrx

if 'ShapeStats' not in _M_ode.api.__dict__:
    _M_ode.api.ShapeStats = Ice.createTempClass()
    class ShapeStats(Ice.Object):
        """
        Contains arrays, one entry per channel, of the statistics
        for a given shape. All arrays are the same size, except for
        the channelIds array, which specifies the ids of the logical
        channels which compose this Shape. If the user specified no
        logical channels for the Shape, then all logical channels from
        the Pixels will be in channelIds.
        """
        def __init__(self, shapeId=0, channelIds=None, pointsCount=None, min=None, max=None, sum=None, mean=None, stdDev=None):
            self.shapeId = shapeId
            self.channelIds = channelIds
            self.pointsCount = pointsCount
            self.min = min
            self.max = max
            self.sum = sum
            self.mean = mean
            self.stdDev = stdDev

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ShapeStats')

        def ice_id(self, current=None):
            return '::ode::api::ShapeStats'

        def ice_staticId():
            return '::ode::api::ShapeStats'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ShapeStats)

        __repr__ = __str__

    _M_ode.api.ShapeStatsPrx = Ice.createTempClass()
    class ShapeStatsPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ShapeStatsPrx.ice_checkedCast(proxy, '::ode::api::ShapeStats', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ShapeStatsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ShapeStats'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ShapeStatsPrx = IcePy.defineProxy('::ode::api::ShapeStats', ShapeStatsPrx)

    _M_ode.api._t_ShapeStats = IcePy.defineClass('::ode::api::ShapeStats', ShapeStats, -1, (), False, False, None, (), (
        ('shapeId', (), IcePy._t_long, False, 0),
        ('channelIds', (), _M_ode.api._t_LongArray, False, 0),
        ('pointsCount', (), _M_ode.api._t_LongArray, False, 0),
        ('min', (), _M_ode.api._t_DoubleArray, False, 0),
        ('max', (), _M_ode.api._t_DoubleArray, False, 0),
        ('sum', (), _M_ode.api._t_DoubleArray, False, 0),
        ('mean', (), _M_ode.api._t_DoubleArray, False, 0),
        ('stdDev', (), _M_ode.api._t_DoubleArray, False, 0)
    ))
    ShapeStats._ice_type = _M_ode.api._t_ShapeStats

    _M_ode.api.ShapeStats = ShapeStats
    del ShapeStats

    _M_ode.api.ShapeStatsPrx = ShapeStatsPrx
    del ShapeStatsPrx

if '_t_ShapeStatsList' not in _M_ode.api.__dict__:
    _M_ode.api._t_ShapeStatsList = IcePy.defineSequence('::ode::api::ShapeStatsList', (), _M_ode.api._t_ShapeStats)

if '_t_LongRoiResultMap' not in _M_ode.api.__dict__:
    _M_ode.api._t_LongRoiResultMap = IcePy.defineDictionary('::ode::api::LongRoiResultMap', (), IcePy._t_long, _M_ode.api._t_RoiResult)

if 'RoiStats' not in _M_ode.api.__dict__:
    _M_ode.api.RoiStats = Ice.createTempClass()
    class RoiStats(Ice.Object):
        """
        Container for ShapeStats, one with the combined values,
        and one per shape.
        """
        def __init__(self, roiId=0, imageId=0, pixelsId=0, combined=None, perShape=None):
            self.roiId = roiId
            self.imageId = imageId
            self.pixelsId = pixelsId
            self.combined = combined
            self.perShape = perShape

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::RoiStats')

        def ice_id(self, current=None):
            return '::ode::api::RoiStats'

        def ice_staticId():
            return '::ode::api::RoiStats'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_RoiStats)

        __repr__ = __str__

    _M_ode.api.RoiStatsPrx = Ice.createTempClass()
    class RoiStatsPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.RoiStatsPrx.ice_checkedCast(proxy, '::ode::api::RoiStats', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.RoiStatsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::RoiStats'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_RoiStatsPrx = IcePy.defineProxy('::ode::api::RoiStats', RoiStatsPrx)

    _M_ode.api._t_RoiStats = IcePy.declareClass('::ode::api::RoiStats')

    _M_ode.api._t_RoiStats = IcePy.defineClass('::ode::api::RoiStats', RoiStats, -1, (), False, False, None, (), (
        ('roiId', (), IcePy._t_long, False, 0),
        ('imageId', (), IcePy._t_long, False, 0),
        ('pixelsId', (), IcePy._t_long, False, 0),
        ('combined', (), _M_ode.api._t_ShapeStats, False, 0),
        ('perShape', (), _M_ode.api._t_ShapeStatsList, False, 0)
    ))
    RoiStats._ice_type = _M_ode.api._t_RoiStats

    _M_ode.api.RoiStats = RoiStats
    del RoiStats

    _M_ode.api.RoiStatsPrx = RoiStatsPrx
    del RoiStatsPrx

if 'IRoi' not in _M_ode.api.__dict__:
    _M_ode.api.IRoi = Ice.createTempClass()
    class IRoi(_M_ode.api.ServiceInterface):
        """
        Interface for working with regions of interest.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IRoi:
                raise RuntimeError('ode.api.IRoi is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IRoi', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IRoi'

        def ice_staticId():
            return '::ode::api::IRoi'
        ice_staticId = staticmethod(ice_staticId)

        def findByRoi_async(self, _cb, roiId, opts, current=None):
            """
            Returns a RoiResult with a single Roi member.
            Shape linkages are properly created.
            All Shapes are loaded, as is the Pixels and Image object.
            TODO: Annotations?
            Arguments:
            _cb -- The asynchronous callback object.
            roiId -- 
            opts -- 
            current -- The Current object for the invocation.
            """
            pass

        def findByImage_async(self, _cb, imageId, opts, current=None):
            """
            Returns all the Rois in an Image, indexed via Shape.
            Loads Rois as findByRoi.
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- 
            opts -- 
            current -- The Current object for the invocation.
            """
            pass

        def findByPlane_async(self, _cb, imageId, z, t, opts, current=None):
            """
            Returns all the Rois on the given plane, indexed via Shape.
            Loads Rois as findByRoi.
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- 
            z -- 
            t -- 
            opts -- 
            current -- The Current object for the invocation.
            """
            pass

        def getPoints_async(self, _cb, shapeId, current=None):
            """
            Calculate the points contained within a given shape
            Arguments:
            _cb -- The asynchronous callback object.
            shapeId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getRoiStats_async(self, _cb, roiId, current=None):
            """
            Calculate stats for all the shapes within the given Roi.
            Arguments:
            _cb -- The asynchronous callback object.
            roiId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getShapeStats_async(self, _cb, shapeId, current=None):
            """
            Calculate the stats for the points within the given Shape.
            Arguments:
            _cb -- The asynchronous callback object.
            shapeId -- 
            current -- The Current object for the invocation.
            """
            pass

        def getShapeStatsList_async(self, _cb, shapeIdList, current=None):
            """
            Calculate the stats for the points within the given Shapes.
            Arguments:
            _cb -- The asynchronous callback object.
            shapeIdList -- 
            current -- The Current object for the invocation.
            """
            pass

        def getShapeStatsRestricted_async(self, _cb, shapeIdList, zForUnattached, tForUnattached, channels, current=None):
            """
            Calculate the stats for the points within the given Shapes.
            Varies to the above in the following ways:
            - does not allow tiled images
            - shapes have to be all belonging to the same image
            - unattached z/t use the fallback parameters zForUnattached/tForUnattached
            that is to say there is never more than 1 z/t combination queried
            - if channel list is given, only the channels in that list are iterated over
            - does not request data from reader on each iteration
            Arguments:
            _cb -- The asynchronous callback object.
            shapeIdList -- 
            zForUnattached -- 
            tForUnattached -- 
            channels -- 
            current -- The Current object for the invocation.
            """
            pass

        def getRoiMeasurements_async(self, _cb, imageId, opts, current=None):
            """
            Returns a list of ode.model.FileAnnotation
            instances with the namespace
            bhojpur.net/measurements which are attached
            to the ode.model.Plate containing the given image
            AND which are attached to at least one
            ode.model.Roi
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- 
            opts -- 
            current -- The Current object for the invocation.
            """
            pass

        def getMeasuredRois_async(self, _cb, imageId, annotationId, opts, current=None):
            """
            Loads the ROIs which are linked to by the given
            ode.model.FileAnnotation id for the given image.
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- 
            annotationId -- if -1, logic is identical to findByImage(imageId, opts)
            opts -- 
            current -- The Current object for the invocation.
            """
            pass

        def getMeasuredRoisMap_async(self, _cb, imageId, annotationIds, opts, current=None):
            """
            Returns a map from ode.model.FileAnnotation ids
            to RoiResult instances.
            Logic is identical to getMeasuredRois, but Roi data will not be duplicated. (i.e.
            the objects are referentially identical)
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- 
            annotationIds -- 
            opts -- 
            current -- The Current object for the invocation.
            """
            pass

        def getTable_async(self, _cb, annotationId, current=None):
            """
            Returns the ode.tables service via the
            ode.model.FileAnnotation id returned
            by {@code getImageMeasurements}.
            Arguments:
            _cb -- The asynchronous callback object.
            annotationId -- 
            current -- The Current object for the invocation.
            """
            pass

        def uploadMask_async(self, _cb, roiId, z, t, bytes, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IRoi)

        __repr__ = __str__

    _M_ode.api.IRoiPrx = Ice.createTempClass()
    class IRoiPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Returns a RoiResult with a single Roi member.
        Shape linkages are properly created.
        All Shapes are loaded, as is the Pixels and Image object.
        TODO: Annotations?
        Arguments:
        roiId -- 
        opts -- 
        _ctx -- The request context for the invocation.
        """
        def findByRoi(self, roiId, opts, _ctx=None):
            return _M_ode.api.IRoi._op_findByRoi.invoke(self, ((roiId, opts), _ctx))

        """
        Returns a RoiResult with a single Roi member.
        Shape linkages are properly created.
        All Shapes are loaded, as is the Pixels and Image object.
        TODO: Annotations?
        Arguments:
        roiId -- 
        opts -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findByRoi(self, roiId, opts, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_findByRoi.begin(self, ((roiId, opts), _response, _ex, _sent, _ctx))

        """
        Returns a RoiResult with a single Roi member.
        Shape linkages are properly created.
        All Shapes are loaded, as is the Pixels and Image object.
        TODO: Annotations?
        Arguments:
        roiId -- 
        opts -- 
        """
        def end_findByRoi(self, _r):
            return _M_ode.api.IRoi._op_findByRoi.end(self, _r)

        """
        Returns all the Rois in an Image, indexed via Shape.
        Loads Rois as findByRoi.
        Arguments:
        imageId -- 
        opts -- 
        _ctx -- The request context for the invocation.
        """
        def findByImage(self, imageId, opts, _ctx=None):
            return _M_ode.api.IRoi._op_findByImage.invoke(self, ((imageId, opts), _ctx))

        """
        Returns all the Rois in an Image, indexed via Shape.
        Loads Rois as findByRoi.
        Arguments:
        imageId -- 
        opts -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findByImage(self, imageId, opts, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_findByImage.begin(self, ((imageId, opts), _response, _ex, _sent, _ctx))

        """
        Returns all the Rois in an Image, indexed via Shape.
        Loads Rois as findByRoi.
        Arguments:
        imageId -- 
        opts -- 
        """
        def end_findByImage(self, _r):
            return _M_ode.api.IRoi._op_findByImage.end(self, _r)

        """
        Returns all the Rois on the given plane, indexed via Shape.
        Loads Rois as findByRoi.
        Arguments:
        imageId -- 
        z -- 
        t -- 
        opts -- 
        _ctx -- The request context for the invocation.
        """
        def findByPlane(self, imageId, z, t, opts, _ctx=None):
            return _M_ode.api.IRoi._op_findByPlane.invoke(self, ((imageId, z, t, opts), _ctx))

        """
        Returns all the Rois on the given plane, indexed via Shape.
        Loads Rois as findByRoi.
        Arguments:
        imageId -- 
        z -- 
        t -- 
        opts -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findByPlane(self, imageId, z, t, opts, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_findByPlane.begin(self, ((imageId, z, t, opts), _response, _ex, _sent, _ctx))

        """
        Returns all the Rois on the given plane, indexed via Shape.
        Loads Rois as findByRoi.
        Arguments:
        imageId -- 
        z -- 
        t -- 
        opts -- 
        """
        def end_findByPlane(self, _r):
            return _M_ode.api.IRoi._op_findByPlane.end(self, _r)

        """
        Calculate the points contained within a given shape
        Arguments:
        shapeId -- 
        _ctx -- The request context for the invocation.
        """
        def getPoints(self, shapeId, _ctx=None):
            return _M_ode.api.IRoi._op_getPoints.invoke(self, ((shapeId, ), _ctx))

        """
        Calculate the points contained within a given shape
        Arguments:
        shapeId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPoints(self, shapeId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getPoints.begin(self, ((shapeId, ), _response, _ex, _sent, _ctx))

        """
        Calculate the points contained within a given shape
        Arguments:
        shapeId -- 
        """
        def end_getPoints(self, _r):
            return _M_ode.api.IRoi._op_getPoints.end(self, _r)

        """
        Calculate stats for all the shapes within the given Roi.
        Arguments:
        roiId -- 
        _ctx -- The request context for the invocation.
        """
        def getRoiStats(self, roiId, _ctx=None):
            return _M_ode.api.IRoi._op_getRoiStats.invoke(self, ((roiId, ), _ctx))

        """
        Calculate stats for all the shapes within the given Roi.
        Arguments:
        roiId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRoiStats(self, roiId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getRoiStats.begin(self, ((roiId, ), _response, _ex, _sent, _ctx))

        """
        Calculate stats for all the shapes within the given Roi.
        Arguments:
        roiId -- 
        """
        def end_getRoiStats(self, _r):
            return _M_ode.api.IRoi._op_getRoiStats.end(self, _r)

        """
        Calculate the stats for the points within the given Shape.
        Arguments:
        shapeId -- 
        _ctx -- The request context for the invocation.
        """
        def getShapeStats(self, shapeId, _ctx=None):
            return _M_ode.api.IRoi._op_getShapeStats.invoke(self, ((shapeId, ), _ctx))

        """
        Calculate the stats for the points within the given Shape.
        Arguments:
        shapeId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getShapeStats(self, shapeId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getShapeStats.begin(self, ((shapeId, ), _response, _ex, _sent, _ctx))

        """
        Calculate the stats for the points within the given Shape.
        Arguments:
        shapeId -- 
        """
        def end_getShapeStats(self, _r):
            return _M_ode.api.IRoi._op_getShapeStats.end(self, _r)

        """
        Calculate the stats for the points within the given Shapes.
        Arguments:
        shapeIdList -- 
        _ctx -- The request context for the invocation.
        """
        def getShapeStatsList(self, shapeIdList, _ctx=None):
            return _M_ode.api.IRoi._op_getShapeStatsList.invoke(self, ((shapeIdList, ), _ctx))

        """
        Calculate the stats for the points within the given Shapes.
        Arguments:
        shapeIdList -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getShapeStatsList(self, shapeIdList, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getShapeStatsList.begin(self, ((shapeIdList, ), _response, _ex, _sent, _ctx))

        """
        Calculate the stats for the points within the given Shapes.
        Arguments:
        shapeIdList -- 
        """
        def end_getShapeStatsList(self, _r):
            return _M_ode.api.IRoi._op_getShapeStatsList.end(self, _r)

        """
        Calculate the stats for the points within the given Shapes.
        Varies to the above in the following ways:
        - does not allow tiled images
        - shapes have to be all belonging to the same image
        - unattached z/t use the fallback parameters zForUnattached/tForUnattached
        that is to say there is never more than 1 z/t combination queried
        - if channel list is given, only the channels in that list are iterated over
        - does not request data from reader on each iteration
        Arguments:
        shapeIdList -- 
        zForUnattached -- 
        tForUnattached -- 
        channels -- 
        _ctx -- The request context for the invocation.
        """
        def getShapeStatsRestricted(self, shapeIdList, zForUnattached, tForUnattached, channels, _ctx=None):
            return _M_ode.api.IRoi._op_getShapeStatsRestricted.invoke(self, ((shapeIdList, zForUnattached, tForUnattached, channels), _ctx))

        """
        Calculate the stats for the points within the given Shapes.
        Varies to the above in the following ways:
        - does not allow tiled images
        - shapes have to be all belonging to the same image
        - unattached z/t use the fallback parameters zForUnattached/tForUnattached
        that is to say there is never more than 1 z/t combination queried
        - if channel list is given, only the channels in that list are iterated over
        - does not request data from reader on each iteration
        Arguments:
        shapeIdList -- 
        zForUnattached -- 
        tForUnattached -- 
        channels -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getShapeStatsRestricted(self, shapeIdList, zForUnattached, tForUnattached, channels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getShapeStatsRestricted.begin(self, ((shapeIdList, zForUnattached, tForUnattached, channels), _response, _ex, _sent, _ctx))

        """
        Calculate the stats for the points within the given Shapes.
        Varies to the above in the following ways:
        - does not allow tiled images
        - shapes have to be all belonging to the same image
        - unattached z/t use the fallback parameters zForUnattached/tForUnattached
        that is to say there is never more than 1 z/t combination queried
        - if channel list is given, only the channels in that list are iterated over
        - does not request data from reader on each iteration
        Arguments:
        shapeIdList -- 
        zForUnattached -- 
        tForUnattached -- 
        channels -- 
        """
        def end_getShapeStatsRestricted(self, _r):
            return _M_ode.api.IRoi._op_getShapeStatsRestricted.end(self, _r)

        """
        Returns a list of ode.model.FileAnnotation
        instances with the namespace
        bhojpur.net/measurements which are attached
        to the ode.model.Plate containing the given image
        AND which are attached to at least one
        ode.model.Roi
        Arguments:
        imageId -- 
        opts -- 
        _ctx -- The request context for the invocation.
        """
        def getRoiMeasurements(self, imageId, opts, _ctx=None):
            return _M_ode.api.IRoi._op_getRoiMeasurements.invoke(self, ((imageId, opts), _ctx))

        """
        Returns a list of ode.model.FileAnnotation
        instances with the namespace
        bhojpur.net/measurements which are attached
        to the ode.model.Plate containing the given image
        AND which are attached to at least one
        ode.model.Roi
        Arguments:
        imageId -- 
        opts -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRoiMeasurements(self, imageId, opts, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getRoiMeasurements.begin(self, ((imageId, opts), _response, _ex, _sent, _ctx))

        """
        Returns a list of ode.model.FileAnnotation
        instances with the namespace
        bhojpur.net/measurements which are attached
        to the ode.model.Plate containing the given image
        AND which are attached to at least one
        ode.model.Roi
        Arguments:
        imageId -- 
        opts -- 
        """
        def end_getRoiMeasurements(self, _r):
            return _M_ode.api.IRoi._op_getRoiMeasurements.end(self, _r)

        """
        Loads the ROIs which are linked to by the given
        ode.model.FileAnnotation id for the given image.
        Arguments:
        imageId -- 
        annotationId -- if -1, logic is identical to findByImage(imageId, opts)
        opts -- 
        _ctx -- The request context for the invocation.
        """
        def getMeasuredRois(self, imageId, annotationId, opts, _ctx=None):
            return _M_ode.api.IRoi._op_getMeasuredRois.invoke(self, ((imageId, annotationId, opts), _ctx))

        """
        Loads the ROIs which are linked to by the given
        ode.model.FileAnnotation id for the given image.
        Arguments:
        imageId -- 
        annotationId -- if -1, logic is identical to findByImage(imageId, opts)
        opts -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMeasuredRois(self, imageId, annotationId, opts, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getMeasuredRois.begin(self, ((imageId, annotationId, opts), _response, _ex, _sent, _ctx))

        """
        Loads the ROIs which are linked to by the given
        ode.model.FileAnnotation id for the given image.
        Arguments:
        imageId -- 
        annotationId -- if -1, logic is identical to findByImage(imageId, opts)
        opts -- 
        """
        def end_getMeasuredRois(self, _r):
            return _M_ode.api.IRoi._op_getMeasuredRois.end(self, _r)

        """
        Returns a map from ode.model.FileAnnotation ids
        to RoiResult instances.
        Logic is identical to getMeasuredRois, but Roi data will not be duplicated. (i.e.
        the objects are referentially identical)
        Arguments:
        imageId -- 
        annotationIds -- 
        opts -- 
        _ctx -- The request context for the invocation.
        """
        def getMeasuredRoisMap(self, imageId, annotationIds, opts, _ctx=None):
            return _M_ode.api.IRoi._op_getMeasuredRoisMap.invoke(self, ((imageId, annotationIds, opts), _ctx))

        """
        Returns a map from ode.model.FileAnnotation ids
        to RoiResult instances.
        Logic is identical to getMeasuredRois, but Roi data will not be duplicated. (i.e.
        the objects are referentially identical)
        Arguments:
        imageId -- 
        annotationIds -- 
        opts -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMeasuredRoisMap(self, imageId, annotationIds, opts, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getMeasuredRoisMap.begin(self, ((imageId, annotationIds, opts), _response, _ex, _sent, _ctx))

        """
        Returns a map from ode.model.FileAnnotation ids
        to RoiResult instances.
        Logic is identical to getMeasuredRois, but Roi data will not be duplicated. (i.e.
        the objects are referentially identical)
        Arguments:
        imageId -- 
        annotationIds -- 
        opts -- 
        """
        def end_getMeasuredRoisMap(self, _r):
            return _M_ode.api.IRoi._op_getMeasuredRoisMap.end(self, _r)

        """
        Returns the ode.tables service via the
        ode.model.FileAnnotation id returned
        by {@code getImageMeasurements}.
        Arguments:
        annotationId -- 
        _ctx -- The request context for the invocation.
        """
        def getTable(self, annotationId, _ctx=None):
            return _M_ode.api.IRoi._op_getTable.invoke(self, ((annotationId, ), _ctx))

        """
        Returns the ODE.tables service via the
        ode.model.FileAnnotation id returned
        by {@code getImageMeasurements}.
        Arguments:
        annotationId -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTable(self, annotationId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_getTable.begin(self, ((annotationId, ), _response, _ex, _sent, _ctx))

        """
        Returns the ODE.tables service via the
        ode.model.FileAnnotation id returned
        by {@code getImageMeasurements}.
        Arguments:
        annotationId -- 
        """
        def end_getTable(self, _r):
            return _M_ode.api.IRoi._op_getTable.end(self, _r)

        def uploadMask(self, roiId, z, t, bytes, _ctx=None):
            return _M_ode.api.IRoi._op_uploadMask.invoke(self, ((roiId, z, t, bytes), _ctx))

        def begin_uploadMask(self, roiId, z, t, bytes, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRoi._op_uploadMask.begin(self, ((roiId, z, t, bytes), _response, _ex, _sent, _ctx))

        def end_uploadMask(self, _r):
            return _M_ode.api.IRoi._op_uploadMask.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IRoiPrx.ice_checkedCast(proxy, '::ode::api::IRoi', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IRoiPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IRoi'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IRoiPrx = IcePy.defineProxy('::ode::api::IRoi', IRoiPrx)

    _M_ode.api._t_IRoi = IcePy.defineClass('::ode::api::IRoi', IRoi, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IRoi._ice_type = _M_ode.api._t_IRoi

    IRoi._op_findByRoi = IcePy.Operation('findByRoi', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_RoiOptions, False, 0)), (), ((), _M_ode.api._t_RoiResult, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_findByRoi.deprecate("IROI is deprecated.")
    IRoi._op_findByImage = IcePy.Operation('findByImage', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_RoiOptions, False, 0)), (), ((), _M_ode.api._t_RoiResult, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_findByImage.deprecate("IROI is deprecated.")
    IRoi._op_findByPlane = IcePy.Operation('findByPlane', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), _M_ode.api._t_RoiOptions, False, 0)), (), ((), _M_ode.api._t_RoiResult, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_findByPlane.deprecate("IROI is deprecated.")
    IRoi._op_getPoints = IcePy.Operation('getPoints', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_ShapePoints, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getPoints.deprecate("IROI is deprecated.")
    IRoi._op_getRoiStats = IcePy.Operation('getRoiStats', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_RoiStats, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getRoiStats.deprecate("IROI is deprecated.")
    IRoi._op_getShapeStats = IcePy.Operation('getShapeStats', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_ShapeStats, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getShapeStats.deprecate("IROI is deprecated.")
    IRoi._op_getShapeStatsList = IcePy.Operation('getShapeStatsList', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_LongList, False, 0),), (), ((), _M_ode.api._t_ShapeStatsList, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getShapeStatsList.deprecate("IROI is deprecated.")
    IRoi._op_getShapeStatsRestricted = IcePy.Operation('getShapeStatsRestricted', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_LongList, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), _M_ode.api._t_IntegerArray, False, 0)), (), ((), _M_ode.api._t_ShapeStatsList, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getShapeStatsRestricted.deprecate("IROI is deprecated.")
    IRoi._op_getRoiMeasurements = IcePy.Operation('getRoiMeasurements', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_RoiOptions, False, 0)), (), ((), _M_ode.api._t_AnnotationList, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getRoiMeasurements.deprecate("IROI is deprecated.")
    IRoi._op_getMeasuredRois = IcePy.Operation('getMeasuredRois', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0), ((), _M_ode.api._t_RoiOptions, False, 0)), (), ((), _M_ode.api._t_RoiResult, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getMeasuredRois.deprecate("IROI is deprecated.")
    IRoi._op_getMeasuredRoisMap = IcePy.Operation('getMeasuredRoisMap', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.api._t_LongList, False, 0), ((), _M_ode.api._t_RoiOptions, False, 0)), (), ((), _M_ode.api._t_LongRoiResultMap, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getMeasuredRoisMap.deprecate("IROI is deprecated.")
    IRoi._op_getTable = IcePy.Operation('getTable', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.grid._t_TablePrx, False, 0), (_M_ode._t_ServerError,))
    IRoi._op_getTable.deprecate("IROI is deprecated.")
    IRoi._op_uploadMask = IcePy.Operation('uploadMask', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), _M_Ice._t_ByteSeq, False, 0)), (), None, (_M_ode._t_ServerError,))
    IRoi._op_uploadMask.deprecate("IROI is deprecated.")

    _M_ode.api.IRoi = IRoi
    del IRoi

    _M_ode.api.IRoiPrx = IRoiPrx
    del IRoiPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
