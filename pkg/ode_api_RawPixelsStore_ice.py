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
import ode_Collections_ice
import ode_ROMIO_ice
import ode_api_PyramidService_ice

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

# Included module ode.romio
_M_ode.romio = Ice.openModule('ode.romio')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'RawPixelsStore' not in _M_ode.api.__dict__:
    _M_ode.api.RawPixelsStore = Ice.createTempClass()
    class RawPixelsStore(_M_ode.api.PyramidService):
        """
        Binary data provider. Initialized with the ID of a
        ode.model.Pixels instance, this service can provide
        various slices, stacks, regions of the 5-dimensional (X-Y planes with
        multiple Z-sections and Channels over Time). The byte array returned
        by the getter methods and passed to the setter methods can and will
        be interpreted according to results of {@code getByteWidth},
        {@code isFloat}, and {@code isSigned}.
        Read-only caveat:
        Mutating methods (set*) are only available during the first access.
        Once the Pixels data has been successfully saved (via the save or
        close methods on this interface), then the data should be treated
        read-only. If Pixels data writing fails and the service is
        inadvertently closed, delete the Pixels object, and create a new
        one. Any partially written data will be removed.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.RawPixelsStore:
                raise RuntimeError('ode.api.RawPixelsStore is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::PyramidService', '::ode::api::RawPixelsStore', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::RawPixelsStore'

        def ice_staticId():
            return '::ode::api::RawPixelsStore'
        ice_staticId = staticmethod(ice_staticId)

        def setPixelsId_async(self, _cb, pixelsId, bypassOriginalFile, current=None):
            """
            Initializes the stateful service for a given Pixels set.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- Pixels set identifier.
            bypassOriginalFile -- Whether or not to bypass checking for an original file to back the pixel buffer used by this service. If requests are predominantly write-only or involve the population of a brand new pixel buffer using true here is a safe optimization otherwise false is expected. See Read-only caveat under RawPixelsStore
            current -- The Current object for the invocation.
            """
            pass

        def getPixelsId_async(self, _cb, current=None):
            """
            Returns the current Pixels set identifier.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getPixelsPath_async(self, _cb, current=None):
            """
            Returns the current Pixels path.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def prepare_async(self, _cb, pixelsIds, current=None):
            """
            Prepares the stateful service with a cache of loaded Pixels
            objects.
            This method is designed to combat query overhead, where
            many sets of Pixels are to be read from or written to, by
            loading all the Pixels sets at once. Multiple calls will
            result in the existing cache being overwritten.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsIds -- Pixels IDs to cache.
            current -- The Current object for the invocation.
            """
            pass

        def getPlaneSize_async(self, _cb, current=None):
            """
            Retrieves the in memory size of a 2D image plane in this
            pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getRowSize_async(self, _cb, current=None):
            """
            Retrieves the in memory size of a row or scanline of pixels
            in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getStackSize_async(self, _cb, current=None):
            """
            Retrieves the in memory size of the entire number of
            optical sections for a single wavelength or channel
            at a particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getTimepointSize_async(self, _cb, current=None):
            """
            Retrieves the in memory size of the entire number of
            optical sections for all wavelengths or channels at
            a particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getTotalSize_async(self, _cb, current=None):
            """
            Retrieves the in memory size of the entire pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getRowOffset_async(self, _cb, y, z, c, t, current=None):
            """
            Retrieves the offset for a particular row or scanline in
            this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            y -- offset across the Y-axis of the pixel buffer.
            z -- offset across the Z-axis of the pixel buffer.
            c -- offset across the C-axis of the pixel buffer.
            t -- offset across the T-axis of the pixel buffer.
            current -- The Current object for the invocation.
            """
            pass

        def getPlaneOffset_async(self, _cb, z, c, t, current=None):
            """
            Retrieves the offset for a particular 2D image plane in this pixel
            store.
            Arguments:
            _cb -- The asynchronous callback object.
            z -- offset across the Z-axis of the pixel buffer.
            c -- offset across the C-axis of the pixel buffer.
            t -- offset across the T-axis of the pixel buffer.
            current -- The Current object for the invocation.
            """
            pass

        def getStackOffset_async(self, _cb, c, t, current=None):
            """
            Retrieves the offset for the entire number of optical
            sections for a single wavelength or channel at a
            particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            c -- offset across the C-axis of the pixel buffer.
            t -- offset across the T-axis of the pixel buffer.
            current -- The Current object for the invocation.
            """
            pass

        def getTimepointOffset_async(self, _cb, t, current=None):
            """
            Retrieves the in memory size of the entire number of
            optical sections for all wavelengths or channels at
            a particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            t -- offset across the T-axis of the pixel buffer.
            current -- The Current object for the invocation.
            """
            pass

        def getTile_async(self, _cb, z, c, t, x, y, w, h, current=None):
            """
            Retrieves a tile from this pixel buffer.
            Arguments:
            _cb -- The asynchronous callback object.
            z -- offset across the Z-axis of the pixel buffer.
            c -- offset across the C-axis of the pixel buffer.
            t -- offset across the T-axis of the pixel buffer.
            x -- Top left corner of the tile, X offset.
            y -- Top left corner of the tile, Y offset.
            w -- Width of the tile.
            h -- Height of the tile.
            current -- The Current object for the invocation.
            """
            pass

        def getHypercube_async(self, _cb, offset, size, step, current=None):
            """
            Retrieves a n-dimensional block from this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            offset -- offset for each dimension within pixel store.
            size -- of each dimension (dependent on dimension).
            step -- needed of each dimension (dependent on dimension).
            current -- The Current object for the invocation.
            """
            pass

        def getRegion_async(self, _cb, size, offset, current=None):
            """
            Retrieves a region from this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            size -- byte width of the region to retrieve.
            offset -- offset within the pixel store.
            current -- The Current object for the invocation.
            """
            pass

        def getRow_async(self, _cb, y, z, c, t, current=None):
            """
            Retrieves a particular row or scanline from this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            y -- offset across the Y-axis of the pixel store.
            z -- offset across the Z-axis of the pixel store.
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store.
            current -- The Current object for the invocation.
            """
            pass

        def getCol_async(self, _cb, x, z, c, t, current=None):
            """
            Retrieves a particular column from this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            x -- offset across the X-axis of the pixel store.
            z -- offset across the Z-axis of the pixel store.
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store.
            current -- The Current object for the invocation.
            """
            pass

        def getPlane_async(self, _cb, z, c, t, current=None):
            """
            Retrieves a particular 2D image plane from this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            z -- offset across the Z-axis of the pixel store.
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store.
            current -- The Current object for the invocation.
            """
            pass

        def getPlaneRegion_async(self, _cb, z, c, t, size, offset, current=None):
            """
            Retrieves a region from a given plane from this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            z -- offset across the Z-axis of the pixel store.
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store.
            size -- the number of pixels to retrieve.
            offset -- the offset at which to retrieve size pixels.
            current -- The Current object for the invocation.
            """
            pass

        def getStack_async(self, _cb, c, t, current=None):
            """
            Retrieves the the entire number of optical sections for a single
            wavelength or channel at a particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store.
            current -- The Current object for the invocation.
            """
            pass

        def getTimepoint_async(self, _cb, t, current=None):
            """
            Retrieves the entire number of optical sections for all
            wavelengths or channels at a particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            t -- offset across the T-axis of the pixel store.
            current -- The Current object for the invocation.
            """
            pass

        def setTile_async(self, _cb, buf, z, c, t, x, y, w, h, current=None):
            """
            Sets a tile in this pixel buffer.
            Arguments:
            _cb -- The asynchronous callback object.
            buf -- A byte array of the data.
            z -- offset across the Z-axis of the pixel buffer.
            c -- offset across the C-axis of the pixel buffer.
            t -- offset across the T-axis of the pixel buffer.
            x -- Top left corner of the tile, X offset.
            y -- Top left corner of the tile, Y offset.
            w -- Width of the tile.
            h -- Height of the tile.
            current -- The Current object for the invocation.
            Throws:
            BufferOverflowException -- if an attempt is made to write off the end of the file. See Read-only caveat under RawPixelsStore
            IOException -- if there is a problem writing to the pixel buffer.
            """
            pass

        def setRegion_async(self, _cb, size, offset, buf, current=None):
            """
            Sets a region in this pixel buffer.
            Arguments:
            _cb -- The asynchronous callback object.
            size -- byte width of the region to set.
            offset -- offset within the pixel buffer.
            buf -- a byte array of the data. See Read-only caveat under RawPixelsStore
            current -- The Current object for the invocation.
            """
            pass

        def setRow_async(self, _cb, buf, y, z, c, t, current=None):
            """
            Sets a particular row or scanline in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            buf -- a byte array of the data comprising this row or scanline.
            y -- offset across the Y-axis of the pixel store.
            z -- offset across the Z-axis of the pixel store.
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
            current -- The Current object for the invocation.
            """
            pass

        def setPlane_async(self, _cb, buf, z, c, t, current=None):
            """
            Sets a particular 2D image plane in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            buf -- a byte array of the data comprising this 2D image plane.
            z -- offset across the Z-axis of the pixel store.
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
            current -- The Current object for the invocation.
            """
            pass

        def setStack_async(self, _cb, buf, z, c, t, current=None):
            """
            Sets the entire number of optical sections for a single
            wavelength or channel at a particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            buf -- a byte array of the data comprising this stack.
            z -- 
            c -- offset across the C-axis of the pixel store.
            t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
            current -- The Current object for the invocation.
            """
            pass

        def setTimepoint_async(self, _cb, buf, t, current=None):
            """
            Sets the entire number of optical sections for all
            wavelengths or channels at a particular timepoint in this pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            buf -- a byte array of the data comprising this timepoint.
            t -- offset across the T-axis of the pixel buffer. See Read-only caveat under RawPixelsStore
            current -- The Current object for the invocation.
            """
            pass

        def getHistogram_async(self, _cb, channels, binCount, globalRange, plane, current=None):
            """
            Retrieves the histogram data for the specified plane and channels. This method can currently only handle non-pyramid images.
            Arguments:
            _cb -- The asynchronous callback object.
            channels -- the channels to generate the histogram data for
            binCount -- the number of the histogram bins (optional, default: 256)
            globalRange -- use the global minimum/maximum to determine the histogram range, otherwise use plane minimum/maximum value
            plane -- the plane (optional, default: whole region of first z/t plane)
            current -- The Current object for the invocation.
            """
            pass

        def findMinMax_async(self, _cb, channels, current=None):
            """
            Find the minimum and maximum pixel values for the specified channels by iterating over a full plane.
            In case of multi-z/t images only the 'middle' plane with index maxZ/2, respectively maxT/2 is taken into account.
            Note: This method can currently only handle non-pyramid images, otherwise an empty map will be returned.
            Arguments:
            _cb -- The asynchronous callback object.
            channels -- the channels
            current -- The Current object for the invocation.
            """
            pass

        def getByteWidth_async(self, _cb, current=None):
            """
            Returns the byte width for the pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def isSigned_async(self, _cb, current=None):
            """
            Returns whether or not the pixel store has signed pixels.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def isFloat_async(self, _cb, current=None):
            """
            Returns whether or not the pixel buffer has floating point pixels.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def calculateMessageDigest_async(self, _cb, current=None):
            """
            Calculates a SHA-1 message digest for the entire pixel store.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def save_async(self, _cb, current=None):
            """
            Save the current state of the pixels, updating the SHA1. This should
            only be called AFTER all data is successfully set. Future invocations
            of set methods may be disallowed. This read-only status will allow
            background processing (generation of thumbnails, compression, etc.)
            to begin. More information under RawPixelsStore.
            A null instance will be returned if no save was performed.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_RawPixelsStore)

        __repr__ = __str__

    _M_ode.api.RawPixelsStorePrx = Ice.createTempClass()
    class RawPixelsStorePrx(_M_ode.api.PyramidServicePrx):

        """
        Initializes the stateful service for a given Pixels set.
        Arguments:
        pixelsId -- Pixels set identifier.
        bypassOriginalFile -- Whether or not to bypass checking for an original file to back the pixel buffer used by this service. If requests are predominantly write-only or involve the population of a brand new pixel buffer using true here is a safe optimization otherwise false is expected. See Read-only caveat under RawPixelsStore
        _ctx -- The request context for the invocation.
        """
        def setPixelsId(self, pixelsId, bypassOriginalFile, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setPixelsId.invoke(self, ((pixelsId, bypassOriginalFile), _ctx))

        """
        Initializes the stateful service for a given Pixels set.
        Arguments:
        pixelsId -- Pixels set identifier.
        bypassOriginalFile -- Whether or not to bypass checking for an original file to back the pixel buffer used by this service. If requests are predominantly write-only or involve the population of a brand new pixel buffer using true here is a safe optimization otherwise false is expected. See Read-only caveat under RawPixelsStore
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setPixelsId(self, pixelsId, bypassOriginalFile, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setPixelsId.begin(self, ((pixelsId, bypassOriginalFile), _response, _ex, _sent, _ctx))

        """
        Initializes the stateful service for a given Pixels set.
        Arguments:
        pixelsId -- Pixels set identifier.
        bypassOriginalFile -- Whether or not to bypass checking for an original file to back the pixel buffer used by this service. If requests are predominantly write-only or involve the population of a brand new pixel buffer using true here is a safe optimization otherwise false is expected. See Read-only caveat under RawPixelsStore
        """
        def end_setPixelsId(self, _r):
            return _M_ode.api.RawPixelsStore._op_setPixelsId.end(self, _r)

        """
        Returns the current Pixels set identifier.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getPixelsId(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPixelsId.invoke(self, ((), _ctx))

        """
        Returns the current Pixels set identifier.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPixelsId(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPixelsId.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current Pixels set identifier.
        Arguments:
        Returns: See above.
        """
        def end_getPixelsId(self, _r):
            return _M_ode.api.RawPixelsStore._op_getPixelsId.end(self, _r)

        """
        Returns the current Pixels path.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getPixelsPath(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPixelsPath.invoke(self, ((), _ctx))

        """
        Returns the current Pixels path.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPixelsPath(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPixelsPath.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current Pixels path.
        Arguments:
        Returns: See above.
        """
        def end_getPixelsPath(self, _r):
            return _M_ode.api.RawPixelsStore._op_getPixelsPath.end(self, _r)

        """
        Prepares the stateful service with a cache of loaded Pixels
        objects.
        This method is designed to combat query overhead, where
        many sets of Pixels are to be read from or written to, by
        loading all the Pixels sets at once. Multiple calls will
        result in the existing cache being overwritten.
        Arguments:
        pixelsIds -- Pixels IDs to cache.
        _ctx -- The request context for the invocation.
        """
        def prepare(self, pixelsIds, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_prepare.invoke(self, ((pixelsIds, ), _ctx))

        """
        Prepares the stateful service with a cache of loaded Pixels
        objects.
        This method is designed to combat query overhead, where
        many sets of Pixels are to be read from or written to, by
        loading all the Pixels sets at once. Multiple calls will
        result in the existing cache being overwritten.
        Arguments:
        pixelsIds -- Pixels IDs to cache.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_prepare(self, pixelsIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_prepare.begin(self, ((pixelsIds, ), _response, _ex, _sent, _ctx))

        """
        Prepares the stateful service with a cache of loaded Pixels
        objects.
        This method is designed to combat query overhead, where
        many sets of Pixels are to be read from or written to, by
        loading all the Pixels sets at once. Multiple calls will
        result in the existing cache being overwritten.
        Arguments:
        pixelsIds -- Pixels IDs to cache.
        """
        def end_prepare(self, _r):
            return _M_ode.api.RawPixelsStore._op_prepare.end(self, _r)

        """
        Retrieves the in memory size of a 2D image plane in this
        pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: 2D image plane size in bytes sizeX*sizeY*byteWidth.
        """
        def getPlaneSize(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlaneSize.invoke(self, ((), _ctx))

        """
        Retrieves the in memory size of a 2D image plane in this
        pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPlaneSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlaneSize.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the in memory size of a 2D image plane in this
        pixel store.
        Arguments:
        Returns: 2D image plane size in bytes sizeX*sizeY*byteWidth.
        """
        def end_getPlaneSize(self, _r):
            return _M_ode.api.RawPixelsStore._op_getPlaneSize.end(self, _r)

        """
        Retrieves the in memory size of a row or scanline of pixels
        in this pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: row or scanline size in bytes sizeX*byteWidth
        """
        def getRowSize(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRowSize.invoke(self, ((), _ctx))

        """
        Retrieves the in memory size of a row or scanline of pixels
        in this pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRowSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRowSize.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the in memory size of a row or scanline of pixels
        in this pixel store.
        Arguments:
        Returns: row or scanline size in bytes sizeX*byteWidth
        """
        def end_getRowSize(self, _r):
            return _M_ode.api.RawPixelsStore._op_getRowSize.end(self, _r)

        """
        Retrieves the in memory size of the entire number of
        optical sections for a single wavelength or channel
        at a particular timepoint in this pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: stack size in bytes sizeX*sizeY*byteWidth.
        """
        def getStackSize(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getStackSize.invoke(self, ((), _ctx))

        """
        Retrieves the in memory size of the entire number of
        optical sections for a single wavelength or channel
        at a particular timepoint in this pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getStackSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getStackSize.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the in memory size of the entire number of
        optical sections for a single wavelength or channel
        at a particular timepoint in this pixel store.
        Arguments:
        Returns: stack size in bytes sizeX*sizeY*byteWidth.
        """
        def end_getStackSize(self, _r):
            return _M_ode.api.RawPixelsStore._op_getStackSize.end(self, _r)

        """
        Retrieves the in memory size of the entire number of
        optical sections for all wavelengths or channels at
        a particular timepoint in this pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: timepoint size in bytes sizeX*sizeY*sizeZ*sizeC*byteWidth.
        """
        def getTimepointSize(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTimepointSize.invoke(self, ((), _ctx))

        """
        Retrieves the in memory size of the entire number of
        optical sections for all wavelengths or channels at
        a particular timepoint in this pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTimepointSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTimepointSize.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the in memory size of the entire number of
        optical sections for all wavelengths or channels at
        a particular timepoint in this pixel store.
        Arguments:
        Returns: timepoint size in bytes sizeX*sizeY*sizeZ*sizeC*byteWidth.
        """
        def end_getTimepointSize(self, _r):
            return _M_ode.api.RawPixelsStore._op_getTimepointSize.end(self, _r)

        """
        Retrieves the in memory size of the entire pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: total size of the pixel size in bytes sizeX*sizeY*sizeZ*sizeC*sizeT*byteWidth.
        """
        def getTotalSize(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTotalSize.invoke(self, ((), _ctx))

        """
        Retrieves the in memory size of the entire pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTotalSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTotalSize.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Retrieves the in memory size of the entire pixel store.
        Arguments:
        Returns: total size of the pixel size in bytes sizeX*sizeY*sizeZ*sizeC*sizeT*byteWidth.
        """
        def end_getTotalSize(self, _r):
            return _M_ode.api.RawPixelsStore._op_getTotalSize.end(self, _r)

        """
        Retrieves the offset for a particular row or scanline in
        this pixel store.
        Arguments:
        y -- offset across the Y-axis of the pixel buffer.
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        _ctx -- The request context for the invocation.
        Returns: offset of the row or scanline.
        """
        def getRowOffset(self, y, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRowOffset.invoke(self, ((y, z, c, t), _ctx))

        """
        Retrieves the offset for a particular row or scanline in
        this pixel store.
        Arguments:
        y -- offset across the Y-axis of the pixel buffer.
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRowOffset(self, y, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRowOffset.begin(self, ((y, z, c, t), _response, _ex, _sent, _ctx))

        """
        Retrieves the offset for a particular row or scanline in
        this pixel store.
        Arguments:
        y -- offset across the Y-axis of the pixel buffer.
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        Returns: offset of the row or scanline.
        """
        def end_getRowOffset(self, _r):
            return _M_ode.api.RawPixelsStore._op_getRowOffset.end(self, _r)

        """
        Retrieves the offset for a particular 2D image plane in this pixel
        store.
        Arguments:
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        _ctx -- The request context for the invocation.
        Returns: offset of the 2D image plane.
        """
        def getPlaneOffset(self, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlaneOffset.invoke(self, ((z, c, t), _ctx))

        """
        Retrieves the offset for a particular 2D image plane in this pixel
        store.
        Arguments:
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPlaneOffset(self, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlaneOffset.begin(self, ((z, c, t), _response, _ex, _sent, _ctx))

        """
        Retrieves the offset for a particular 2D image plane in this pixel
        store.
        Arguments:
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        Returns: offset of the 2D image plane.
        """
        def end_getPlaneOffset(self, _r):
            return _M_ode.api.RawPixelsStore._op_getPlaneOffset.end(self, _r)

        """
        Retrieves the offset for the entire number of optical
        sections for a single wavelength or channel at a
        particular timepoint in this pixel store.
        Arguments:
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        _ctx -- The request context for the invocation.
        Returns: offset of the stack.
        """
        def getStackOffset(self, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getStackOffset.invoke(self, ((c, t), _ctx))

        """
        Retrieves the offset for the entire number of optical
        sections for a single wavelength or channel at a
        particular timepoint in this pixel store.
        Arguments:
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getStackOffset(self, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getStackOffset.begin(self, ((c, t), _response, _ex, _sent, _ctx))

        """
        Retrieves the offset for the entire number of optical
        sections for a single wavelength or channel at a
        particular timepoint in this pixel store.
        Arguments:
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        Returns: offset of the stack.
        """
        def end_getStackOffset(self, _r):
            return _M_ode.api.RawPixelsStore._op_getStackOffset.end(self, _r)

        """
        Retrieves the in memory size of the entire number of
        optical sections for all wavelengths or channels at
        a particular timepoint in this pixel store.
        Arguments:
        t -- offset across the T-axis of the pixel buffer.
        _ctx -- The request context for the invocation.
        Returns: offset of the timepoint.
        """
        def getTimepointOffset(self, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTimepointOffset.invoke(self, ((t, ), _ctx))

        """
        Retrieves the in memory size of the entire number of
        optical sections for all wavelengths or channels at
        a particular timepoint in this pixel store.
        Arguments:
        t -- offset across the T-axis of the pixel buffer.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTimepointOffset(self, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTimepointOffset.begin(self, ((t, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the in memory size of the entire number of
        optical sections for all wavelengths or channels at
        a particular timepoint in this pixel store.
        Arguments:
        t -- offset across the T-axis of the pixel buffer.
        Returns: offset of the timepoint.
        """
        def end_getTimepointOffset(self, _r):
            return _M_ode.api.RawPixelsStore._op_getTimepointOffset.end(self, _r)

        """
        Retrieves a tile from this pixel buffer.
        Arguments:
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        x -- Top left corner of the tile, X offset.
        y -- Top left corner of the tile, Y offset.
        w -- Width of the tile.
        h -- Height of the tile.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data.
        """
        def getTile(self, z, c, t, x, y, w, h, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTile.invoke(self, ((z, c, t, x, y, w, h), _ctx))

        """
        Retrieves a tile from this pixel buffer.
        Arguments:
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        x -- Top left corner of the tile, X offset.
        y -- Top left corner of the tile, Y offset.
        w -- Width of the tile.
        h -- Height of the tile.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTile(self, z, c, t, x, y, w, h, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTile.begin(self, ((z, c, t, x, y, w, h), _response, _ex, _sent, _ctx))

        """
        Retrieves a tile from this pixel buffer.
        Arguments:
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        x -- Top left corner of the tile, X offset.
        y -- Top left corner of the tile, Y offset.
        w -- Width of the tile.
        h -- Height of the tile.
        Returns: buffer containing the data.
        """
        def end_getTile(self, _r):
            return _M_ode.api.RawPixelsStore._op_getTile.end(self, _r)

        """
        Retrieves a n-dimensional block from this pixel store.
        Arguments:
        offset -- offset for each dimension within pixel store.
        size -- of each dimension (dependent on dimension).
        step -- needed of each dimension (dependent on dimension).
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data.
        """
        def getHypercube(self, offset, size, step, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getHypercube.invoke(self, ((offset, size, step), _ctx))

        """
        Retrieves a n-dimensional block from this pixel store.
        Arguments:
        offset -- offset for each dimension within pixel store.
        size -- of each dimension (dependent on dimension).
        step -- needed of each dimension (dependent on dimension).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getHypercube(self, offset, size, step, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getHypercube.begin(self, ((offset, size, step), _response, _ex, _sent, _ctx))

        """
        Retrieves a n-dimensional block from this pixel store.
        Arguments:
        offset -- offset for each dimension within pixel store.
        size -- of each dimension (dependent on dimension).
        step -- needed of each dimension (dependent on dimension).
        Returns: buffer containing the data.
        """
        def end_getHypercube(self, _r):
            return _M_ode.api.RawPixelsStore._op_getHypercube.end(self, _r)

        """
        Retrieves a region from this pixel store.
        Arguments:
        size -- byte width of the region to retrieve.
        offset -- offset within the pixel store.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data.
        """
        def getRegion(self, size, offset, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRegion.invoke(self, ((size, offset), _ctx))

        """
        Retrieves a region from this pixel store.
        Arguments:
        size -- byte width of the region to retrieve.
        offset -- offset within the pixel store.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRegion(self, size, offset, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRegion.begin(self, ((size, offset), _response, _ex, _sent, _ctx))

        """
        Retrieves a region from this pixel store.
        Arguments:
        size -- byte width of the region to retrieve.
        offset -- offset within the pixel store.
        Returns: buffer containing the data.
        """
        def end_getRegion(self, _r):
            return _M_ode.api.RawPixelsStore._op_getRegion.end(self, _r)

        """
        Retrieves a particular row or scanline from this pixel store.
        Arguments:
        y -- offset across the Y-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data which comprises this row or scanline.
        """
        def getRow(self, y, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRow.invoke(self, ((y, z, c, t), _ctx))

        """
        Retrieves a particular row or scanline from this pixel store.
        Arguments:
        y -- offset across the Y-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRow(self, y, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getRow.begin(self, ((y, z, c, t), _response, _ex, _sent, _ctx))

        """
        Retrieves a particular row or scanline from this pixel store.
        Arguments:
        y -- offset across the Y-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        Returns: buffer containing the data which comprises this row or scanline.
        """
        def end_getRow(self, _r):
            return _M_ode.api.RawPixelsStore._op_getRow.end(self, _r)

        """
        Retrieves a particular column from this pixel store.
        Arguments:
        x -- offset across the X-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data which comprises this column.
        """
        def getCol(self, x, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getCol.invoke(self, ((x, z, c, t), _ctx))

        """
        Retrieves a particular column from this pixel store.
        Arguments:
        x -- offset across the X-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCol(self, x, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getCol.begin(self, ((x, z, c, t), _response, _ex, _sent, _ctx))

        """
        Retrieves a particular column from this pixel store.
        Arguments:
        x -- offset across the X-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        Returns: buffer containing the data which comprises this column.
        """
        def end_getCol(self, _r):
            return _M_ode.api.RawPixelsStore._op_getCol.end(self, _r)

        """
        Retrieves a particular 2D image plane from this pixel store.
        Arguments:
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data which comprises this 2D image plane.
        """
        def getPlane(self, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlane.invoke(self, ((z, c, t), _ctx))

        """
        Retrieves a particular 2D image plane from this pixel store.
        Arguments:
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPlane(self, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlane.begin(self, ((z, c, t), _response, _ex, _sent, _ctx))

        """
        Retrieves a particular 2D image plane from this pixel store.
        Arguments:
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        Returns: buffer containing the data which comprises this 2D image plane.
        """
        def end_getPlane(self, _r):
            return _M_ode.api.RawPixelsStore._op_getPlane.end(self, _r)

        """
        Retrieves a region from a given plane from this pixel store.
        Arguments:
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        size -- the number of pixels to retrieve.
        offset -- the offset at which to retrieve size pixels.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data which comprises the region of the given 2D image plane. It is guaranteed that this buffer will have been byte swapped.
        """
        def getPlaneRegion(self, z, c, t, size, offset, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlaneRegion.invoke(self, ((z, c, t, size, offset), _ctx))

        """
        Retrieves a region from a given plane from this pixel store.
        Arguments:
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        size -- the number of pixels to retrieve.
        offset -- the offset at which to retrieve size pixels.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPlaneRegion(self, z, c, t, size, offset, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getPlaneRegion.begin(self, ((z, c, t, size, offset), _response, _ex, _sent, _ctx))

        """
        Retrieves a region from a given plane from this pixel store.
        Arguments:
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        size -- the number of pixels to retrieve.
        offset -- the offset at which to retrieve size pixels.
        Returns: buffer containing the data which comprises the region of the given 2D image plane. It is guaranteed that this buffer will have been byte swapped.
        """
        def end_getPlaneRegion(self, _r):
            return _M_ode.api.RawPixelsStore._op_getPlaneRegion.end(self, _r)

        """
        Retrieves the the entire number of optical sections for a single
        wavelength or channel at a particular timepoint in this pixel store.
        Arguments:
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data which comprises this stack.
        """
        def getStack(self, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getStack.invoke(self, ((c, t), _ctx))

        """
        Retrieves the the entire number of optical sections for a single
        wavelength or channel at a particular timepoint in this pixel store.
        Arguments:
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getStack(self, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getStack.begin(self, ((c, t), _response, _ex, _sent, _ctx))

        """
        Retrieves the the entire number of optical sections for a single
        wavelength or channel at a particular timepoint in this pixel store.
        Arguments:
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store.
        Returns: buffer containing the data which comprises this stack.
        """
        def end_getStack(self, _r):
            return _M_ode.api.RawPixelsStore._op_getStack.end(self, _r)

        """
        Retrieves the entire number of optical sections for all
        wavelengths or channels at a particular timepoint in this pixel store.
        Arguments:
        t -- offset across the T-axis of the pixel store.
        _ctx -- The request context for the invocation.
        Returns: buffer containing the data which comprises this stack.
        """
        def getTimepoint(self, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTimepoint.invoke(self, ((t, ), _ctx))

        """
        Retrieves the entire number of optical sections for all
        wavelengths or channels at a particular timepoint in this pixel store.
        Arguments:
        t -- offset across the T-axis of the pixel store.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getTimepoint(self, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getTimepoint.begin(self, ((t, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the entire number of optical sections for all
        wavelengths or channels at a particular timepoint in this pixel store.
        Arguments:
        t -- offset across the T-axis of the pixel store.
        Returns: buffer containing the data which comprises this stack.
        """
        def end_getTimepoint(self, _r):
            return _M_ode.api.RawPixelsStore._op_getTimepoint.end(self, _r)

        """
        Sets a tile in this pixel buffer.
        Arguments:
        buf -- A byte array of the data.
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        x -- Top left corner of the tile, X offset.
        y -- Top left corner of the tile, Y offset.
        w -- Width of the tile.
        h -- Height of the tile.
        _ctx -- The request context for the invocation.
        Throws:
        BufferOverflowException -- if an attempt is made to write off the end of the file. See Read-only caveat under RawPixelsStore
        IOException -- if there is a problem writing to the pixel buffer.
        """
        def setTile(self, buf, z, c, t, x, y, w, h, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setTile.invoke(self, ((buf, z, c, t, x, y, w, h), _ctx))

        """
        Sets a tile in this pixel buffer.
        Arguments:
        buf -- A byte array of the data.
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        x -- Top left corner of the tile, X offset.
        y -- Top left corner of the tile, Y offset.
        w -- Width of the tile.
        h -- Height of the tile.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setTile(self, buf, z, c, t, x, y, w, h, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setTile.begin(self, ((buf, z, c, t, x, y, w, h), _response, _ex, _sent, _ctx))

        """
        Sets a tile in this pixel buffer.
        Arguments:
        buf -- A byte array of the data.
        z -- offset across the Z-axis of the pixel buffer.
        c -- offset across the C-axis of the pixel buffer.
        t -- offset across the T-axis of the pixel buffer.
        x -- Top left corner of the tile, X offset.
        y -- Top left corner of the tile, Y offset.
        w -- Width of the tile.
        h -- Height of the tile.
        Throws:
        BufferOverflowException -- if an attempt is made to write off the end of the file. See Read-only caveat under RawPixelsStore
        IOException -- if there is a problem writing to the pixel buffer.
        """
        def end_setTile(self, _r):
            return _M_ode.api.RawPixelsStore._op_setTile.end(self, _r)

        """
        Sets a region in this pixel buffer.
        Arguments:
        size -- byte width of the region to set.
        offset -- offset within the pixel buffer.
        buf -- a byte array of the data. See Read-only caveat under RawPixelsStore
        _ctx -- The request context for the invocation.
        """
        def setRegion(self, size, offset, buf, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setRegion.invoke(self, ((size, offset, buf), _ctx))

        """
        Sets a region in this pixel buffer.
        Arguments:
        size -- byte width of the region to set.
        offset -- offset within the pixel buffer.
        buf -- a byte array of the data. See Read-only caveat under RawPixelsStore
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setRegion(self, size, offset, buf, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setRegion.begin(self, ((size, offset, buf), _response, _ex, _sent, _ctx))

        """
        Sets a region in this pixel buffer.
        Arguments:
        size -- byte width of the region to set.
        offset -- offset within the pixel buffer.
        buf -- a byte array of the data. See Read-only caveat under RawPixelsStore
        """
        def end_setRegion(self, _r):
            return _M_ode.api.RawPixelsStore._op_setRegion.end(self, _r)

        """
        Sets a particular row or scanline in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this row or scanline.
        y -- offset across the Y-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        _ctx -- The request context for the invocation.
        """
        def setRow(self, buf, y, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setRow.invoke(self, ((buf, y, z, c, t), _ctx))

        """
        Sets a particular row or scanline in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this row or scanline.
        y -- offset across the Y-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setRow(self, buf, y, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setRow.begin(self, ((buf, y, z, c, t), _response, _ex, _sent, _ctx))

        """
        Sets a particular row or scanline in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this row or scanline.
        y -- offset across the Y-axis of the pixel store.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        """
        def end_setRow(self, _r):
            return _M_ode.api.RawPixelsStore._op_setRow.end(self, _r)

        """
        Sets a particular 2D image plane in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this 2D image plane.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        _ctx -- The request context for the invocation.
        """
        def setPlane(self, buf, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setPlane.invoke(self, ((buf, z, c, t), _ctx))

        """
        Sets a particular 2D image plane in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this 2D image plane.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setPlane(self, buf, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setPlane.begin(self, ((buf, z, c, t), _response, _ex, _sent, _ctx))

        """
        Sets a particular 2D image plane in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this 2D image plane.
        z -- offset across the Z-axis of the pixel store.
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        """
        def end_setPlane(self, _r):
            return _M_ode.api.RawPixelsStore._op_setPlane.end(self, _r)

        """
        Sets the entire number of optical sections for a single
        wavelength or channel at a particular timepoint in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this stack.
        z -- 
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        _ctx -- The request context for the invocation.
        """
        def setStack(self, buf, z, c, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setStack.invoke(self, ((buf, z, c, t), _ctx))

        """
        Sets the entire number of optical sections for a single
        wavelength or channel at a particular timepoint in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this stack.
        z -- 
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setStack(self, buf, z, c, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setStack.begin(self, ((buf, z, c, t), _response, _ex, _sent, _ctx))

        """
        Sets the entire number of optical sections for a single
        wavelength or channel at a particular timepoint in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this stack.
        z -- 
        c -- offset across the C-axis of the pixel store.
        t -- offset across the T-axis of the pixel store. See Read-only caveat under RawPixelsStore
        """
        def end_setStack(self, _r):
            return _M_ode.api.RawPixelsStore._op_setStack.end(self, _r)

        """
        Sets the entire number of optical sections for all
        wavelengths or channels at a particular timepoint in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this timepoint.
        t -- offset across the T-axis of the pixel buffer. See Read-only caveat under RawPixelsStore
        _ctx -- The request context for the invocation.
        """
        def setTimepoint(self, buf, t, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setTimepoint.invoke(self, ((buf, t), _ctx))

        """
        Sets the entire number of optical sections for all
        wavelengths or channels at a particular timepoint in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this timepoint.
        t -- offset across the T-axis of the pixel buffer. See Read-only caveat under RawPixelsStore
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setTimepoint(self, buf, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_setTimepoint.begin(self, ((buf, t), _response, _ex, _sent, _ctx))

        """
        Sets the entire number of optical sections for all
        wavelengths or channels at a particular timepoint in this pixel store.
        Arguments:
        buf -- a byte array of the data comprising this timepoint.
        t -- offset across the T-axis of the pixel buffer. See Read-only caveat under RawPixelsStore
        """
        def end_setTimepoint(self, _r):
            return _M_ode.api.RawPixelsStore._op_setTimepoint.end(self, _r)

        """
        Retrieves the histogram data for the specified plane and channels. This method can currently only handle non-pyramid images.
        Arguments:
        channels -- the channels to generate the histogram data for
        binCount -- the number of the histogram bins (optional, default: 256)
        globalRange -- use the global minimum/maximum to determine the histogram range, otherwise use plane minimum/maximum value
        plane -- the plane (optional, default: whole region of first z/t plane)
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getHistogram(self, channels, binCount, globalRange, plane, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getHistogram.invoke(self, ((channels, binCount, globalRange, plane), _ctx))

        """
        Retrieves the histogram data for the specified plane and channels. This method can currently only handle non-pyramid images.
        Arguments:
        channels -- the channels to generate the histogram data for
        binCount -- the number of the histogram bins (optional, default: 256)
        globalRange -- use the global minimum/maximum to determine the histogram range, otherwise use plane minimum/maximum value
        plane -- the plane (optional, default: whole region of first z/t plane)
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getHistogram(self, channels, binCount, globalRange, plane, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getHistogram.begin(self, ((channels, binCount, globalRange, plane), _response, _ex, _sent, _ctx))

        """
        Retrieves the histogram data for the specified plane and channels. This method can currently only handle non-pyramid images.
        Arguments:
        channels -- the channels to generate the histogram data for
        binCount -- the number of the histogram bins (optional, default: 256)
        globalRange -- use the global minimum/maximum to determine the histogram range, otherwise use plane minimum/maximum value
        plane -- the plane (optional, default: whole region of first z/t plane)
        Returns: See above.
        """
        def end_getHistogram(self, _r):
            return _M_ode.api.RawPixelsStore._op_getHistogram.end(self, _r)

        """
        Find the minimum and maximum pixel values for the specified channels by iterating over a full plane.
        In case of multi-z/t images only the 'middle' plane with index maxZ/2, respectively maxT/2 is taken into account.
        Note: This method can currently only handle non-pyramid images, otherwise an empty map will be returned.
        Arguments:
        channels -- the channels
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def findMinMax(self, channels, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_findMinMax.invoke(self, ((channels, ), _ctx))

        """
        Find the minimum and maximum pixel values for the specified channels by iterating over a full plane.
        In case of multi-z/t images only the 'middle' plane with index maxZ/2, respectively maxT/2 is taken into account.
        Note: This method can currently only handle non-pyramid images, otherwise an empty map will be returned.
        Arguments:
        channels -- the channels
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_findMinMax(self, channels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_findMinMax.begin(self, ((channels, ), _response, _ex, _sent, _ctx))

        """
        Find the minimum and maximum pixel values for the specified channels by iterating over a full plane.
        In case of multi-z/t images only the 'middle' plane with index maxZ/2, respectively maxT/2 is taken into account.
        Note: This method can currently only handle non-pyramid images, otherwise an empty map will be returned.
        Arguments:
        channels -- the channels
        Returns: See above.
        """
        def end_findMinMax(self, _r):
            return _M_ode.api.RawPixelsStore._op_findMinMax.end(self, _r)

        """
        Returns the byte width for the pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getByteWidth(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getByteWidth.invoke(self, ((), _ctx))

        """
        Returns the byte width for the pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getByteWidth(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_getByteWidth.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the byte width for the pixel store.
        Arguments:
        Returns: See above.
        """
        def end_getByteWidth(self, _r):
            return _M_ode.api.RawPixelsStore._op_getByteWidth.end(self, _r)

        """
        Returns whether or not the pixel store has signed pixels.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def isSigned(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_isSigned.invoke(self, ((), _ctx))

        """
        Returns whether or not the pixel store has signed pixels.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isSigned(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_isSigned.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns whether or not the pixel store has signed pixels.
        Arguments:
        Returns: See above.
        """
        def end_isSigned(self, _r):
            return _M_ode.api.RawPixelsStore._op_isSigned.end(self, _r)

        """
        Returns whether or not the pixel buffer has floating point pixels.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def isFloat(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_isFloat.invoke(self, ((), _ctx))

        """
        Returns whether or not the pixel buffer has floating point pixels.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isFloat(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_isFloat.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns whether or not the pixel buffer has floating point pixels.
        Arguments:
        Returns: See above.
        """
        def end_isFloat(self, _r):
            return _M_ode.api.RawPixelsStore._op_isFloat.end(self, _r)

        """
        Calculates a SHA-1 message digest for the entire pixel store.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: byte array containing the message digest.
        """
        def calculateMessageDigest(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_calculateMessageDigest.invoke(self, ((), _ctx))

        """
        Calculates a SHA-1 message digest for the entire pixel store.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_calculateMessageDigest(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_calculateMessageDigest.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Calculates a SHA-1 message digest for the entire pixel store.
        Arguments:
        Returns: byte array containing the message digest.
        """
        def end_calculateMessageDigest(self, _r):
            return _M_ode.api.RawPixelsStore._op_calculateMessageDigest.end(self, _r)

        """
        Save the current state of the pixels, updating the SHA1. This should
        only be called AFTER all data is successfully set. Future invocations
        of set methods may be disallowed. This read-only status will allow
        background processing (generation of thumbnails, compression, etc.)
        to begin. More information under RawPixelsStore.
        A null instance will be returned if no save was performed.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def save(self, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_save.invoke(self, ((), _ctx))

        """
        Save the current state of the pixels, updating the SHA1. This should
        only be called AFTER all data is successfully set. Future invocations
        of set methods may be disallowed. This read-only status will allow
        background processing (generation of thumbnails, compression, etc.)
        to begin. More information under RawPixelsStore.
        A null instance will be returned if no save was performed.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_save(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RawPixelsStore._op_save.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Save the current state of the pixels, updating the SHA1. This should
        only be called AFTER all data is successfully set. Future invocations
        of set methods may be disallowed. This read-only status will allow
        background processing (generation of thumbnails, compression, etc.)
        to begin. More information under RawPixelsStore.
        A null instance will be returned if no save was performed.
        Arguments:
        """
        def end_save(self, _r):
            return _M_ode.api.RawPixelsStore._op_save.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.RawPixelsStorePrx.ice_checkedCast(proxy, '::ode::api::RawPixelsStore', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.RawPixelsStorePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::RawPixelsStore'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_RawPixelsStorePrx = IcePy.defineProxy('::ode::api::RawPixelsStore', RawPixelsStorePrx)

    _M_ode.api._t_RawPixelsStore = IcePy.defineClass('::ode::api::RawPixelsStore', RawPixelsStore, -1, (), True, False, None, (_M_ode.api._t_PyramidService,), ())
    RawPixelsStore._ice_type = _M_ode.api._t_RawPixelsStore

    RawPixelsStore._op_setPixelsId = IcePy.Operation('setPixelsId', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_bool, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_getPixelsId = IcePy.Operation('getPixelsId', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getPixelsPath = IcePy.Operation('getPixelsPath', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_prepare = IcePy.Operation('prepare', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_LongList, False, 0),), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_getPlaneSize = IcePy.Operation('getPlaneSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getRowSize = IcePy.Operation('getRowSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getStackSize = IcePy.Operation('getStackSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getTimepointSize = IcePy.Operation('getTimepointSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getTotalSize = IcePy.Operation('getTotalSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getRowOffset = IcePy.Operation('getRowOffset', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getPlaneOffset = IcePy.Operation('getPlaneOffset', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getStackOffset = IcePy.Operation('getStackOffset', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getTimepointOffset = IcePy.Operation('getTimepointOffset', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getTile = IcePy.Operation('getTile', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getHypercube = IcePy.Operation('getHypercube', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.sys._t_IntList, False, 0), ((), _M_ode.sys._t_IntList, False, 0), ((), _M_ode.sys._t_IntList, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getRegion = IcePy.Operation('getRegion', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getRow = IcePy.Operation('getRow', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getCol = IcePy.Operation('getCol', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getPlane = IcePy.Operation('getPlane', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getPlaneRegion = IcePy.Operation('getPlaneRegion', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getStack = IcePy.Operation('getStack', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getTimepoint = IcePy.Operation('getTimepoint', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_setTile = IcePy.Operation('setTile', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_Ice._t_ByteSeq, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_setRegion = IcePy.Operation('setRegion', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_long, False, 0), ((), _M_Ice._t_ByteSeq, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_setRow = IcePy.Operation('setRow', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_Ice._t_ByteSeq, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_setPlane = IcePy.Operation('setPlane', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_Ice._t_ByteSeq, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_setStack = IcePy.Operation('setStack', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_Ice._t_ByteSeq, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_setTimepoint = IcePy.Operation('setTimepoint', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_Ice._t_ByteSeq, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RawPixelsStore._op_getHistogram = IcePy.Operation('getHistogram', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_IntegerArray, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_bool, False, 0), ((), _M_ode.romio._t_PlaneDef, False, 0)), (), ((), _M_ode.api._t_IntegerIntegerArrayMap, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_findMinMax = IcePy.Operation('findMinMax', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.api._t_IntegerArray, False, 0),), (), ((), _M_ode.api._t_IntegerDoubleArrayMap, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_getByteWidth = IcePy.Operation('getByteWidth', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_isSigned = IcePy.Operation('isSigned', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_isFloat = IcePy.Operation('isFloat', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_calculateMessageDigest = IcePy.Operation('calculateMessageDigest', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RawPixelsStore._op_save = IcePy.Operation('save', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.model._t_Pixels, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.RawPixelsStore = RawPixelsStore
    del RawPixelsStore

    _M_ode.api.RawPixelsStorePrx = RawPixelsStorePrx
    del RawPixelsStorePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
