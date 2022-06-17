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
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_Collections_ice
import ode_Constants_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Included module ode.constants
_M_ode.constants = Ice.openModule('ode.constants')

# Included module ode.constants.cluster
_M_ode.constants.cluster = Ice.openModule('ode.constants.cluster')

# Included module ode.constants.annotation
_M_ode.constants.annotation = Ice.openModule('ode.constants.annotation')

# Included module ode.constants.annotation.file
_M_ode.constants.annotation.file = Ice.openModule('ode.constants.annotation.file')

# Included module ode.constants.data
_M_ode.constants.data = Ice.openModule('ode.constants.data')

# Included module ode.constants.metadata
_M_ode.constants.metadata = Ice.openModule('ode.constants.metadata')

# Included module ode.constants.namespaces
_M_ode.constants.namespaces = Ice.openModule('ode.constants.namespaces')

# Included module ode.constants.analysis
_M_ode.constants.analysis = Ice.openModule('ode.constants.analysis')

# Included module ode.constants.analysis.flim
_M_ode.constants.analysis.flim = Ice.openModule('ode.constants.analysis.flim')

# Included module ode.constants.jobs
_M_ode.constants.jobs = Ice.openModule('ode.constants.jobs')

# Included module ode.constants.permissions
_M_ode.constants.permissions = Ice.openModule('ode.constants.permissions')

# Included module ode.constants.projection
_M_ode.constants.projection = Ice.openModule('ode.constants.projection')

# Included module ode.constants.topics
_M_ode.constants.topics = Ice.openModule('ode.constants.topics')

# Included module ode.constants.categories
_M_ode.constants.categories = Ice.openModule('ode.constants.categories')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'IProjection' not in _M_ode.api.__dict__:
    _M_ode.api.IProjection = Ice.createTempClass()
    class IProjection(_M_ode.api.ServiceInterface):
        """
        Provides methods for performing projections of Pixels sets.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IProjection:
                raise RuntimeError('ode.api.IProjection is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IProjection', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IProjection'

        def ice_staticId():
            return '::ode::api::IProjection'
        ice_staticId = staticmethod(ice_staticId)

        def projectStack_async(self, _cb, pixelsId, pixelsType, algorithm, timepoint, channelIndex, stepping, start, end, current=None):
            """
            Performs a projection through the optical sections of a
            particular wavelength at a given time point of a Pixels set.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The source Pixels set Id.
            pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
            algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
            timepoint -- Timepoint to perform the projection.
            channelIndex -- Index of the channel to perform the projection.
            stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
            start -- Optical section to start projecting from.
            end -- Optical section to finish projecting.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- Where: algorithm is unknown timepoint is out of range channelIndex is out of range start is out of range end is out of range start is greater than end the Pixels set qualified by pixelsId is not locatable.
            """
            pass

        def projectPixels_async(self, _cb, pixelsId, pixelsType, algorithm, tStart, tEnd, channelList, stepping, zStart, zEnd, name, current=None):
            """
            Performs a projection through selected optical sections and
            optical sections for a given set of time points of a Pixels
            set. The Image which is linked to the Pixels set will be
            copied using
            {@code ode.api.IPixels.copyAndResizeImage}.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The source Pixels set Id.
            pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
            algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
            tStart -- Timepoint to start projecting from.
            tEnd -- Timepoint to finish projecting.
            channelList -- List of the channel indexes to use while calculating the projection.
            stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
            zStart -- Optical section to start projecting from.
            zEnd -- Optical section to finish projecting.
            name -- Name for the newly created image. If null the name of the Image linked to the Pixels qualified by pixelsId will be used with a Projection suffix. For example, GFP-H2B Image of HeLa Cells will have an Image name of GFP-H2B Image of HeLa Cells Projection used for the projection.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- Where: algorithm is unknown tStart is out of range tEnd is out of range tStart is greater than tEnd channelList is null or has indexes out of range zStart is out of range zEnd is out of range zStart is greater than zEnd the Pixels set qualified by pixelsId is not locatable.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IProjection)

        __repr__ = __str__

    _M_ode.api.IProjectionPrx = Ice.createTempClass()
    class IProjectionPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Performs a projection through the optical sections of a
        particular wavelength at a given time point of a Pixels set.
        Arguments:
        pixelsId -- The source Pixels set Id.
        pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
        algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
        timepoint -- Timepoint to perform the projection.
        channelIndex -- Index of the channel to perform the projection.
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        _ctx -- The request context for the invocation.
        Returns: A byte array of projected pixel values whose length is equal to the Pixels set 8         sizeX * sizeY * bytesPerPixel in big-endian format.
        Throws:
        ValidationException -- Where: algorithm is unknown timepoint is out of range channelIndex is out of range start is out of range end is out of range start is greater than end the Pixels set qualified by pixelsId is not locatable.
        """
        def projectStack(self, pixelsId, pixelsType, algorithm, timepoint, channelIndex, stepping, start, end, _ctx=None):
            return _M_ode.api.IProjection._op_projectStack.invoke(self, ((pixelsId, pixelsType, algorithm, timepoint, channelIndex, stepping, start, end), _ctx))

        """
        Performs a projection through the optical sections of a
        particular wavelength at a given time point of a Pixels set.
        Arguments:
        pixelsId -- The source Pixels set Id.
        pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
        algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
        timepoint -- Timepoint to perform the projection.
        channelIndex -- Index of the channel to perform the projection.
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_projectStack(self, pixelsId, pixelsType, algorithm, timepoint, channelIndex, stepping, start, end, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IProjection._op_projectStack.begin(self, ((pixelsId, pixelsType, algorithm, timepoint, channelIndex, stepping, start, end), _response, _ex, _sent, _ctx))

        """
        Performs a projection through the optical sections of a
        particular wavelength at a given time point of a Pixels set.
        Arguments:
        pixelsId -- The source Pixels set Id.
        pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
        algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
        timepoint -- Timepoint to perform the projection.
        channelIndex -- Index of the channel to perform the projection.
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        Returns: A byte array of projected pixel values whose length is equal to the Pixels set 8         sizeX * sizeY * bytesPerPixel in big-endian format.
        Throws:
        ValidationException -- Where: algorithm is unknown timepoint is out of range channelIndex is out of range start is out of range end is out of range start is greater than end the Pixels set qualified by pixelsId is not locatable.
        """
        def end_projectStack(self, _r):
            return _M_ode.api.IProjection._op_projectStack.end(self, _r)

        """
        Performs a projection through selected optical sections and
        optical sections for a given set of time points of a Pixels
        set. The Image which is linked to the Pixels set will be
        copied using
        {@code ode.api.IPixels.copyAndResizeImage}.
        Arguments:
        pixelsId -- The source Pixels set Id.
        pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
        algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
        tStart -- Timepoint to start projecting from.
        tEnd -- Timepoint to finish projecting.
        channelList -- List of the channel indexes to use while calculating the projection.
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        zStart -- Optical section to start projecting from.
        zEnd -- Optical section to finish projecting.
        name -- Name for the newly created image. If null the name of the Image linked to the Pixels qualified by pixelsId will be used with a Projection suffix. For example, GFP-H2B Image of HeLa Cells will have an Image name of GFP-H2B Image of HeLa Cells Projection used for the projection.
        _ctx -- The request context for the invocation.
        Returns: The Id of the newly created Image which has been projected.
        Throws:
        ValidationException -- Where: algorithm is unknown tStart is out of range tEnd is out of range tStart is greater than tEnd channelList is null or has indexes out of range zStart is out of range zEnd is out of range zStart is greater than zEnd the Pixels set qualified by pixelsId is not locatable.
        """
        def projectPixels(self, pixelsId, pixelsType, algorithm, tStart, tEnd, channelList, stepping, zStart, zEnd, name, _ctx=None):
            return _M_ode.api.IProjection._op_projectPixels.invoke(self, ((pixelsId, pixelsType, algorithm, tStart, tEnd, channelList, stepping, zStart, zEnd, name), _ctx))

        """
        Performs a projection through selected optical sections and
        optical sections for a given set of time points of a Pixels
        set. The Image which is linked to the Pixels set will be
        copied using
        {@code ode.api.IPixels.copyAndResizeImage}.
        Arguments:
        pixelsId -- The source Pixels set Id.
        pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
        algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
        tStart -- Timepoint to start projecting from.
        tEnd -- Timepoint to finish projecting.
        channelList -- List of the channel indexes to use while calculating the projection.
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        zStart -- Optical section to start projecting from.
        zEnd -- Optical section to finish projecting.
        name -- Name for the newly created image. If null the name of the Image linked to the Pixels qualified by pixelsId will be used with a Projection suffix. For example, GFP-H2B Image of HeLa Cells will have an Image name of GFP-H2B Image of HeLa Cells Projection used for the projection.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_projectPixels(self, pixelsId, pixelsType, algorithm, tStart, tEnd, channelList, stepping, zStart, zEnd, name, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IProjection._op_projectPixels.begin(self, ((pixelsId, pixelsType, algorithm, tStart, tEnd, channelList, stepping, zStart, zEnd, name), _response, _ex, _sent, _ctx))

        """
        Performs a projection through selected optical sections and
        optical sections for a given set of time points of a Pixels
        set. The Image which is linked to the Pixels set will be
        copied using
        {@code ode.api.IPixels.copyAndResizeImage}.
        Arguments:
        pixelsId -- The source Pixels set Id.
        pixelsType -- The destination Pixels type. If null, the source Pixels set pixels type will be used.
        algorithm -- MAXIMUM_INTENSITY, MEAN_INTENSITY or SUM_INTENSITY. NOTE: When performing a SUM_INTENSITY projection, pixel values will be pinned to the maximum pixel value of the destination Pixels type.
        tStart -- Timepoint to start projecting from.
        tEnd -- Timepoint to finish projecting.
        channelList -- List of the channel indexes to use while calculating the projection.
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        zStart -- Optical section to start projecting from.
        zEnd -- Optical section to finish projecting.
        name -- Name for the newly created image. If null the name of the Image linked to the Pixels qualified by pixelsId will be used with a Projection suffix. For example, GFP-H2B Image of HeLa Cells will have an Image name of GFP-H2B Image of HeLa Cells Projection used for the projection.
        Returns: The Id of the newly created Image which has been projected.
        Throws:
        ValidationException -- Where: algorithm is unknown tStart is out of range tEnd is out of range tStart is greater than tEnd channelList is null or has indexes out of range zStart is out of range zEnd is out of range zStart is greater than zEnd the Pixels set qualified by pixelsId is not locatable.
        """
        def end_projectPixels(self, _r):
            return _M_ode.api.IProjection._op_projectPixels.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IProjectionPrx.ice_checkedCast(proxy, '::ode::api::IProjection', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IProjectionPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IProjection'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IProjectionPrx = IcePy.defineProxy('::ode::api::IProjection', IProjectionPrx)

    _M_ode.api._t_IProjection = IcePy.defineClass('::ode::api::IProjection', IProjection, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IProjection._ice_type = _M_ode.api._t_IProjection

    IProjection._op_projectStack = IcePy.Operation('projectStack', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_PixelsType, False, 0), ((), _M_ode.constants.projection._t_ProjectionType, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    IProjection._op_projectPixels = IcePy.Operation('projectPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.model._t_PixelsType, False, 0), ((), _M_ode.constants.projection._t_ProjectionType, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), _M_ode.sys._t_IntList, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_string, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IProjection = IProjection
    del IProjection

    _M_ode.api.IProjectionPrx = IProjectionPrx
    del IProjectionPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
