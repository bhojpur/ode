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

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'ThumbnailStore' not in _M_ode.api.__dict__:
    _M_ode.api.ThumbnailStore = Ice.createTempClass()
    class ThumbnailStore(_M_ode.api.StatefulServiceInterface):
        """
        Provides methods for dealing with thumbnails. Provision is provided
        to retrieve thumbnails using the on-disk cache (provided by
        ROMIO) or on the fly.
        NOTE: The calling order for the service is as follows:
        {@code setPixelsId}
        any of the thumbnail accessor methods or
        {@code resetDefaults}
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.ThumbnailStore:
                raise RuntimeError('ode.api.ThumbnailStore is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface', '::ode::api::ThumbnailStore')

        def ice_id(self, current=None):
            return '::ode::api::ThumbnailStore'

        def ice_staticId():
            return '::ode::api::ThumbnailStore'
        ice_staticId = staticmethod(ice_staticId)

        def setPixelsId_async(self, _cb, pixelsId, current=None):
            """
            This method manages the state of the service; it must be
            invoked before using any other methods. As the
            ode.api.ThumbnailStore relies on the
            ode.api.RenderingEngine, a valid rendering
            definition must be available for it to work.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- an ode.model.Pixels id.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if no pixels object exists with the ID pixelsId.
            """
            pass

        def isInProgress_async(self, _cb, current=None):
            """
            This returns the last available in progress state
            for a thumbnail. Its return value is only expected
            to be valid after the call to any of the individual
            thumbnail retrieval methods.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setRenderingDefId_async(self, _cb, renderingDefId, current=None):
            """
            This method manages the state of the service; it should be
            invoked directly after {@code setPixelsId}. If it is not
            invoked with a valid rendering definition ID before using
            the thumbnail accessor methods execution continues as if
            renderingDefId were set to null.
            Arguments:
            _cb -- The asynchronous callback object.
            renderingDefId -- an ode.model.RenderingDef id. null specifies the user's currently active rendering settings to be used.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if no rendering definition exists with the ID renderingDefId.
            """
            pass

        def getRenderingDefId_async(self, _cb, current=None):
            """
            Return the id of the ode.model.RenderingDef
            loaded in this instance.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getThumbnail_async(self, _cb, sizeX, sizeY, current=None):
            """
            Retrieves a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef). If the thumbnail exists
            in the on-disk cache it will be returned directly,
            otherwise it will be created as in
            {@code getThumbnailDirect}, placed in the on-disk
            cache and returned. If the thumbnail is missing, a clock will
            be returned to signify that the thumbnail is yet to be generated.
            Arguments:
            _cb -- The asynchronous callback object.
            sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
            sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
            """
            pass

        def getThumbnailWithoutDefault_async(self, _cb, sizeX, sizeY, current=None):
            """
            Retrieves a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef). If the thumbnail exists
            in the on-disk cache it will be returned directly,
            otherwise it will be created as in
            {@code getThumbnailDirect}, placed in the on-disk
            cache and returned. If the thumbnail is still to be generated, an empty array will
            be returned.
            Arguments:
            _cb -- The asynchronous callback object.
            sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
            sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
            """
            pass

        def getThumbnailSet_async(self, _cb, sizeX, sizeY, pixelsIds, current=None):
            """
            Retrieves a number of thumbnails for pixels sets using
            given sets of rendering settings (RenderingDef). If the
            thumbnails exist in the on-disk cache they will be returned
            directly, otherwise they will be created as in
            {@code getThumbnailDirect}, placed in the on-disk cache
            and returned. Unlike the other thumbnail retrieval methods,
            this method may be called without first calling
            {@code setPixelsId}.
            Arguments:
            _cb -- The asynchronous callback object.
            sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
            sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
            pixelsIds -- the Pixels sets to retrieve thumbnails for.
            current -- The Current object for the invocation.
            """
            pass

        def getThumbnailByLongestSideSet_async(self, _cb, size, pixelsIds, current=None):
            """
            Retrieves a number of thumbnails for pixels sets using
            given sets of rendering settings (RenderingDef). If the
            Thumbnails exist in the on-disk cache they will be returned
            directly, otherwise they will be created as in
            {@code getThumbnailByLongestSideDirect}. The longest
            side of the image will be used to calculate the size for
            the smaller side in order to keep the aspect ratio of the
            original image. Unlike the other thumbnail retrieval
            methods, this method may be called without first
            calling {@code setPixelsId}.
            Arguments:
            _cb -- The asynchronous callback object.
            size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
            pixelsIds -- the Pixels sets to retrieve thumbnails for.
            current -- The Current object for the invocation.
            """
            pass

        def getThumbnailByLongestSide_async(self, _cb, size, current=None):
            """
            Retrieves a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef). If the thumbnail exists
            in the on-disk cache it will be returned directly, otherwise
            it will be created as in {@code getThumbnailDirect},
            placed in the on-disk cache and returned. The longest side
            of the image will be used to calculate the size for the
            smaller side in order to keep the aspect ratio of the
            original image.
            Arguments:
            _cb -- The asynchronous callback object.
            size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
            """
            pass

        def getThumbnailByLongestSideDirect_async(self, _cb, size, current=None):
            """
            Retrieves a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef). The Thumbnail will
            always be created directly, ignoring the on-disk cache. The
            longest side of the image will be used to calculate the
            size for the smaller side in order to keep the aspect ratio
            of the original image.
            Arguments:
            _cb -- The asynchronous callback object.
            size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
            """
            pass

        def getThumbnailDirect_async(self, _cb, sizeX, sizeY, current=None):
            """
            Retrieves a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef). The Thumbnail will
            always be created directly, ignoring the on-disk cache.
            Arguments:
            _cb -- The asynchronous callback object.
            sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
            sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
            """
            pass

        def getThumbnailForSectionDirect_async(self, _cb, theZ, theT, sizeX, sizeY, current=None):
            """
            Retrieves a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef) for a particular section.
            The Thumbnail will always be created directly, ignoring the
            on-disk cache.
            Arguments:
            _cb -- The asynchronous callback object.
            theZ -- the optical section (offset across the Z-axis) to use.
            theT -- the timepoint (offset across the T-axis) to use.
            sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
            sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative theZ is out of range theT is out of range {@code setPixelsId} has not yet been called
            """
            pass

        def getThumbnailForSectionByLongestSideDirect_async(self, _cb, theZ, theT, size, current=None):
            """
            Retrieves a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef) for a particular section.
            The Thumbnail will always be created directly, ignoring the
            on-disk cache. The longest side of the image will be used
            to calculate the size for the smaller side in order to keep
            the aspect ratio of the original image.
            Arguments:
            _cb -- The asynchronous callback object.
            theZ -- the optical section (offset across the Z-axis) to use.
            theT -- the timepoint (offset across the T-axis) to use.
            size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
            """
            pass

        def createThumbnails_async(self, _cb, current=None):
            """
            Creates thumbnails for a pixels set using a given set of
            rendering settings (RenderingDef) in the on-disk cache for
            every sizeX/sizeY combination already cached.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def createThumbnail_async(self, _cb, sizeX, sizeY, current=None):
            """
            Creates a thumbnail for a pixels set using a given set of
            rendering settings (RenderingDef) in the on-disk cache.
            Arguments:
            _cb -- The asynchronous callback object.
            sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
            sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
            """
            pass

        def createThumbnailsByLongestSideSet_async(self, _cb, size, pixelsIds, current=None):
            """
            Creates thumbnails for a number of pixels sets using a
            given set of rendering settings (RenderingDef) in the
            on-disk cache. Unlike the other thumbnail creation methods,
            this method may be called without first calling
            {@code setPixelsId}. This method will not reset or
            modify rendering settings in any way. If rendering settings
            for a pixels set are not present, thumbnail creation for
            that pixels set will not be performed.
            Arguments:
            _cb -- The asynchronous callback object.
            size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
            pixelsIds -- the Pixels sets to retrieve thumbnails for.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY size is negative
            """
            pass

        def thumbnailExists_async(self, _cb, sizeX, sizeY, current=None):
            """
            Checks if a thumbnail of a particular size exists for a
            pixels set.
            Arguments:
            _cb -- The asynchronous callback object.
            sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
            sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
            current -- The Current object for the invocation.
            Throws:
            ApiUsageException -- if: sizeX is negative sizeY is negative setPixelsId has not yet been called
            """
            pass

        def resetDefaults_async(self, _cb, current=None):
            """
            Resets the rendering definition for the active pixels set
            to its default settings.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_ThumbnailStore)

        __repr__ = __str__

    _M_ode.api.ThumbnailStorePrx = Ice.createTempClass()
    class ThumbnailStorePrx(_M_ode.api.StatefulServiceInterfacePrx):

        """
        This method manages the state of the service; it must be
        invoked before using any other methods. As the
        ode.api.ThumbnailStore relies on the
        ode.api.RenderingEngine, a valid rendering
        definition must be available for it to work.
        Arguments:
        pixelsId -- an ode.model.Pixels id.
        _ctx -- The request context for the invocation.
        Returns: true if a ode.model.RenderingDef exists for the ode.model.Pixels set, otherwise false
        Throws:
        ApiUsageException -- if no pixels object exists with the ID pixelsId.
        """
        def setPixelsId(self, pixelsId, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_setPixelsId.invoke(self, ((pixelsId, ), _ctx))

        """
        This method manages the state of the service; it must be
        invoked before using any other methods. As the
        ode.api.ThumbnailStore relies on the
        ode.api.RenderingEngine, a valid rendering
        definition must be available for it to work.
        Arguments:
        pixelsId -- an ode.model.Pixels id.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setPixelsId(self, pixelsId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_setPixelsId.begin(self, ((pixelsId, ), _response, _ex, _sent, _ctx))

        """
        This method manages the state of the service; it must be
        invoked before using any other methods. As the
        ode.api.ThumbnailStore relies on the
        ode.api.RenderingEngine, a valid rendering
        definition must be available for it to work.
        Arguments:
        pixelsId -- an ode.model.Pixels id.
        Returns: true if a ode.model.RenderingDef exists for the ode.model.Pixels set, otherwise false
        Throws:
        ApiUsageException -- if no pixels object exists with the ID pixelsId.
        """
        def end_setPixelsId(self, _r):
            return _M_ode.api.ThumbnailStore._op_setPixelsId.end(self, _r)

        """
        This returns the last available in progress state
        for a thumbnail. Its return value is only expected
        to be valid after the call to any of the individual
        thumbnail retrieval methods.
        Arguments:
        _ctx -- The request context for the invocation.
        Returns: true if the image is in the process of being imported or a pyramid is being generated for it.
        """
        def isInProgress(self, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_isInProgress.invoke(self, ((), _ctx))

        """
        This returns the last available in progress state
        for a thumbnail. Its return value is only expected
        to be valid after the call to any of the individual
        thumbnail retrieval methods.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isInProgress(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_isInProgress.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        This returns the last available in progress state
        for a thumbnail. Its return value is only expected
        to be valid after the call to any of the individual
        thumbnail retrieval methods.
        Arguments:
        Returns: true if the image is in the process of being imported or a pyramid is being generated for it.
        """
        def end_isInProgress(self, _r):
            return _M_ode.api.ThumbnailStore._op_isInProgress.end(self, _r)

        """
        This method manages the state of the service; it should be
        invoked directly after {@code setPixelsId}. If it is not
        invoked with a valid rendering definition ID before using
        the thumbnail accessor methods execution continues as if
        renderingDefId were set to null.
        Arguments:
        renderingDefId -- an ode.model.RenderingDef id. null specifies the user's currently active rendering settings to be used.
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- if no rendering definition exists with the ID renderingDefId.
        """
        def setRenderingDefId(self, renderingDefId, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_setRenderingDefId.invoke(self, ((renderingDefId, ), _ctx))

        """
        This method manages the state of the service; it should be
        invoked directly after {@code setPixelsId}. If it is not
        invoked with a valid rendering definition ID before using
        the thumbnail accessor methods execution continues as if
        renderingDefId were set to null.
        Arguments:
        renderingDefId -- an ode.model.RenderingDef id. null specifies the user's currently active rendering settings to be used.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setRenderingDefId(self, renderingDefId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_setRenderingDefId.begin(self, ((renderingDefId, ), _response, _ex, _sent, _ctx))

        """
        This method manages the state of the service; it should be
        invoked directly after {@code setPixelsId}. If it is not
        invoked with a valid rendering definition ID before using
        the thumbnail accessor methods execution continues as if
        renderingDefId were set to null.
        Arguments:
        renderingDefId -- an ode.model.RenderingDef id. null specifies the user's currently active rendering settings to be used.
        Throws:
        ValidationException -- if no rendering definition exists with the ID renderingDefId.
        """
        def end_setRenderingDefId(self, _r):
            return _M_ode.api.ThumbnailStore._op_setRenderingDefId.end(self, _r)

        """
        Return the id of the ode.model.RenderingDef
        loaded in this instance.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getRenderingDefId(self, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getRenderingDefId.invoke(self, ((), _ctx))

        """
        Return the id of the ode.model.RenderingDef
        loaded in this instance.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRenderingDefId(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getRenderingDefId.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Return the id of the ode.model.RenderingDef
        loaded in this instance.
        Arguments:
        """
        def end_getRenderingDefId(self, _r):
            return _M_ode.api.ThumbnailStore._op_getRenderingDefId.end(self, _r)

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly,
        otherwise it will be created as in
        {@code getThumbnailDirect}, placed in the on-disk
        cache and returned. If the thumbnail is missing, a clock will
        be returned to signify that the thumbnail is yet to be generated.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def getThumbnail(self, sizeX, sizeY, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnail.invoke(self, ((sizeX, sizeY), _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly,
        otherwise it will be created as in
        {@code getThumbnailDirect}, placed in the on-disk
        cache and returned. If the thumbnail is missing, a clock will
        be returned to signify that the thumbnail is yet to be generated.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnail(self, sizeX, sizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnail.begin(self, ((sizeX, sizeY), _response, _ex, _sent, _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly,
        otherwise it will be created as in
        {@code getThumbnailDirect}, placed in the on-disk
        cache and returned. If the thumbnail is missing, a clock will
        be returned to signify that the thumbnail is yet to be generated.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def end_getThumbnail(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnail.end(self, _r)

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly,
        otherwise it will be created as in
        {@code getThumbnailDirect}, placed in the on-disk
        cache and returned. If the thumbnail is still to be generated, an empty array will
        be returned.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Returns: a JPEG thumbnail byte buffer
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def getThumbnailWithoutDefault(self, sizeX, sizeY, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailWithoutDefault.invoke(self, ((sizeX, sizeY), _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly,
        otherwise it will be created as in
        {@code getThumbnailDirect}, placed in the on-disk
        cache and returned. If the thumbnail is still to be generated, an empty array will
        be returned.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailWithoutDefault(self, sizeX, sizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailWithoutDefault.begin(self, ((sizeX, sizeY), _response, _ex, _sent, _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly,
        otherwise it will be created as in
        {@code getThumbnailDirect}, placed in the on-disk
        cache and returned. If the thumbnail is still to be generated, an empty array will
        be returned.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        Returns: a JPEG thumbnail byte buffer
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def end_getThumbnailWithoutDefault(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailWithoutDefault.end(self, _r)

        """
        Retrieves a number of thumbnails for pixels sets using
        given sets of rendering settings (RenderingDef). If the
        thumbnails exist in the on-disk cache they will be returned
        directly, otherwise they will be created as in
        {@code getThumbnailDirect}, placed in the on-disk cache
        and returned. Unlike the other thumbnail retrieval methods,
        this method may be called without first calling
        {@code setPixelsId}.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        _ctx -- The request context for the invocation.
        Returns: a map whose keys are pixels ids and values are JPEG thumbnail byte buffers or null if an exception was thrown while attempting to retrieve the thumbnail for that particular Pixels set.
        """
        def getThumbnailSet(self, sizeX, sizeY, pixelsIds, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailSet.invoke(self, ((sizeX, sizeY, pixelsIds), _ctx))

        """
        Retrieves a number of thumbnails for pixels sets using
        given sets of rendering settings (RenderingDef). If the
        thumbnails exist in the on-disk cache they will be returned
        directly, otherwise they will be created as in
        {@code getThumbnailDirect}, placed in the on-disk cache
        and returned. Unlike the other thumbnail retrieval methods,
        this method may be called without first calling
        {@code setPixelsId}.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailSet(self, sizeX, sizeY, pixelsIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailSet.begin(self, ((sizeX, sizeY, pixelsIds), _response, _ex, _sent, _ctx))

        """
        Retrieves a number of thumbnails for pixels sets using
        given sets of rendering settings (RenderingDef). If the
        thumbnails exist in the on-disk cache they will be returned
        directly, otherwise they will be created as in
        {@code getThumbnailDirect}, placed in the on-disk cache
        and returned. Unlike the other thumbnail retrieval methods,
        this method may be called without first calling
        {@code setPixelsId}.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        Returns: a map whose keys are pixels ids and values are JPEG thumbnail byte buffers or null if an exception was thrown while attempting to retrieve the thumbnail for that particular Pixels set.
        """
        def end_getThumbnailSet(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailSet.end(self, _r)

        """
        Retrieves a number of thumbnails for pixels sets using
        given sets of rendering settings (RenderingDef). If the
        Thumbnails exist in the on-disk cache they will be returned
        directly, otherwise they will be created as in
        {@code getThumbnailByLongestSideDirect}. The longest
        side of the image will be used to calculate the size for
        the smaller side in order to keep the aspect ratio of the
        original image. Unlike the other thumbnail retrieval
        methods, this method may be called without first
        calling {@code setPixelsId}.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        _ctx -- The request context for the invocation.
        Returns: a map whose keys are pixels ids and values are JPEG thumbnail byte buffers or null if an exception was thrown while attempting to retrieve the thumbnail for that particular Pixels set.
        """
        def getThumbnailByLongestSideSet(self, size, pixelsIds, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSideSet.invoke(self, ((size, pixelsIds), _ctx))

        """
        Retrieves a number of thumbnails for pixels sets using
        given sets of rendering settings (RenderingDef). If the
        Thumbnails exist in the on-disk cache they will be returned
        directly, otherwise they will be created as in
        {@code getThumbnailByLongestSideDirect}. The longest
        side of the image will be used to calculate the size for
        the smaller side in order to keep the aspect ratio of the
        original image. Unlike the other thumbnail retrieval
        methods, this method may be called without first
        calling {@code setPixelsId}.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailByLongestSideSet(self, size, pixelsIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSideSet.begin(self, ((size, pixelsIds), _response, _ex, _sent, _ctx))

        """
        Retrieves a number of thumbnails for pixels sets using
        given sets of rendering settings (RenderingDef). If the
        Thumbnails exist in the on-disk cache they will be returned
        directly, otherwise they will be created as in
        {@code getThumbnailByLongestSideDirect}. The longest
        side of the image will be used to calculate the size for
        the smaller side in order to keep the aspect ratio of the
        original image. Unlike the other thumbnail retrieval
        methods, this method may be called without first
        calling {@code setPixelsId}.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        Returns: a map whose keys are pixels ids and values are JPEG thumbnail byte buffers or null if an exception was thrown while attempting to retrieve the thumbnail for that particular Pixels set.
        """
        def end_getThumbnailByLongestSideSet(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSideSet.end(self, _r)

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly, otherwise
        it will be created as in {@code getThumbnailDirect},
        placed in the on-disk cache and returned. The longest side
        of the image will be used to calculate the size for the
        smaller side in order to keep the aspect ratio of the
        original image.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
        """
        def getThumbnailByLongestSide(self, size, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSide.invoke(self, ((size, ), _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly, otherwise
        it will be created as in {@code getThumbnailDirect},
        placed in the on-disk cache and returned. The longest side
        of the image will be used to calculate the size for the
        smaller side in order to keep the aspect ratio of the
        original image.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailByLongestSide(self, size, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSide.begin(self, ((size, ), _response, _ex, _sent, _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). If the thumbnail exists
        in the on-disk cache it will be returned directly, otherwise
        it will be created as in {@code getThumbnailDirect},
        placed in the on-disk cache and returned. The longest side
        of the image will be used to calculate the size for the
        smaller side in order to keep the aspect ratio of the
        original image.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
        """
        def end_getThumbnailByLongestSide(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSide.end(self, _r)

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). The Thumbnail will
        always be created directly, ignoring the on-disk cache. The
        longest side of the image will be used to calculate the
        size for the smaller side in order to keep the aspect ratio
        of the original image.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
        """
        def getThumbnailByLongestSideDirect(self, size, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSideDirect.invoke(self, ((size, ), _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). The Thumbnail will
        always be created directly, ignoring the on-disk cache. The
        longest side of the image will be used to calculate the
        size for the smaller side in order to keep the aspect ratio
        of the original image.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailByLongestSideDirect(self, size, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSideDirect.begin(self, ((size, ), _response, _ex, _sent, _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). The Thumbnail will
        always be created directly, ignoring the on-disk cache. The
        longest side of the image will be used to calculate the
        size for the smaller side in order to keep the aspect ratio
        of the original image.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
        """
        def end_getThumbnailByLongestSideDirect(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailByLongestSideDirect.end(self, _r)

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). The Thumbnail will
        always be created directly, ignoring the on-disk cache.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def getThumbnailDirect(self, sizeX, sizeY, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailDirect.invoke(self, ((sizeX, sizeY), _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). The Thumbnail will
        always be created directly, ignoring the on-disk cache.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailDirect(self, sizeX, sizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailDirect.begin(self, ((sizeX, sizeY), _response, _ex, _sent, _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef). The Thumbnail will
        always be created directly, ignoring the on-disk cache.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def end_getThumbnailDirect(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailDirect.end(self, _r)

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) for a particular section.
        The Thumbnail will always be created directly, ignoring the
        on-disk cache.
        Arguments:
        theZ -- the optical section (offset across the Z-axis) to use.
        theT -- the timepoint (offset across the T-axis) to use.
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative theZ is out of range theT is out of range {@code setPixelsId} has not yet been called
        """
        def getThumbnailForSectionDirect(self, theZ, theT, sizeX, sizeY, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailForSectionDirect.invoke(self, ((theZ, theT, sizeX, sizeY), _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) for a particular section.
        The Thumbnail will always be created directly, ignoring the
        on-disk cache.
        Arguments:
        theZ -- the optical section (offset across the Z-axis) to use.
        theT -- the timepoint (offset across the T-axis) to use.
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailForSectionDirect(self, theZ, theT, sizeX, sizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailForSectionDirect.begin(self, ((theZ, theT, sizeX, sizeY), _response, _ex, _sent, _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) for a particular section.
        The Thumbnail will always be created directly, ignoring the
        on-disk cache.
        Arguments:
        theZ -- the optical section (offset across the Z-axis) to use.
        theT -- the timepoint (offset across the T-axis) to use.
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative theZ is out of range theT is out of range {@code setPixelsId} has not yet been called
        """
        def end_getThumbnailForSectionDirect(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailForSectionDirect.end(self, _r)

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) for a particular section.
        The Thumbnail will always be created directly, ignoring the
        on-disk cache. The longest side of the image will be used
        to calculate the size for the smaller side in order to keep
        the aspect ratio of the original image.
        Arguments:
        theZ -- the optical section (offset across the Z-axis) to use.
        theT -- the timepoint (offset across the T-axis) to use.
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
        """
        def getThumbnailForSectionByLongestSideDirect(self, theZ, theT, size, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailForSectionByLongestSideDirect.invoke(self, ((theZ, theT, size), _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) for a particular section.
        The Thumbnail will always be created directly, ignoring the
        on-disk cache. The longest side of the image will be used
        to calculate the size for the smaller side in order to keep
        the aspect ratio of the original image.
        Arguments:
        theZ -- the optical section (offset across the Z-axis) to use.
        theT -- the timepoint (offset across the T-axis) to use.
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getThumbnailForSectionByLongestSideDirect(self, theZ, theT, size, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_getThumbnailForSectionByLongestSideDirect.begin(self, ((theZ, theT, size), _response, _ex, _sent, _ctx))

        """
        Retrieves a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) for a particular section.
        The Thumbnail will always be created directly, ignoring the
        on-disk cache. The longest side of the image will be used
        to calculate the size for the smaller side in order to keep
        the aspect ratio of the original image.
        Arguments:
        theZ -- the optical section (offset across the Z-axis) to use.
        theT -- the timepoint (offset across the T-axis) to use.
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        Returns: a JPEG thumbnail byte buffer.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY {@code setPixelsId} has not yet been called
        """
        def end_getThumbnailForSectionByLongestSideDirect(self, _r):
            return _M_ode.api.ThumbnailStore._op_getThumbnailForSectionByLongestSideDirect.end(self, _r)

        """
        Creates thumbnails for a pixels set using a given set of
        rendering settings (RenderingDef) in the on-disk cache for
        every sizeX/sizeY combination already cached.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def createThumbnails(self, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_createThumbnails.invoke(self, ((), _ctx))

        """
        Creates thumbnails for a pixels set using a given set of
        rendering settings (RenderingDef) in the on-disk cache for
        every sizeX/sizeY combination already cached.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createThumbnails(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_createThumbnails.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Creates thumbnails for a pixels set using a given set of
        rendering settings (RenderingDef) in the on-disk cache for
        every sizeX/sizeY combination already cached.
        Arguments:
        """
        def end_createThumbnails(self, _r):
            return _M_ode.api.ThumbnailStore._op_createThumbnails.end(self, _r)

        """
        Creates a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) in the on-disk cache.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def createThumbnail(self, sizeX, sizeY, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_createThumbnail.invoke(self, ((sizeX, sizeY), _ctx))

        """
        Creates a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) in the on-disk cache.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createThumbnail(self, sizeX, sizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_createThumbnail.begin(self, ((sizeX, sizeY), _response, _ex, _sent, _ctx))

        """
        Creates a thumbnail for a pixels set using a given set of
        rendering settings (RenderingDef) in the on-disk cache.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        Throws:
        ApiUsageException -- if: sizeX is greater than pixels.sizeX sizeX is negative sizeY is greater than pixels.sizeY sizeY is negative {@code setPixelsId} has not yet been called
        """
        def end_createThumbnail(self, _r):
            return _M_ode.api.ThumbnailStore._op_createThumbnail.end(self, _r)

        """
        Creates thumbnails for a number of pixels sets using a
        given set of rendering settings (RenderingDef) in the
        on-disk cache. Unlike the other thumbnail creation methods,
        this method may be called without first calling
        {@code setPixelsId}. This method will not reset or
        modify rendering settings in any way. If rendering settings
        for a pixels set are not present, thumbnail creation for
        that pixels set will not be performed.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY size is negative
        """
        def createThumbnailsByLongestSideSet(self, size, pixelsIds, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_createThumbnailsByLongestSideSet.invoke(self, ((size, pixelsIds), _ctx))

        """
        Creates thumbnails for a number of pixels sets using a
        given set of rendering settings (RenderingDef) in the
        on-disk cache. Unlike the other thumbnail creation methods,
        this method may be called without first calling
        {@code setPixelsId}. This method will not reset or
        modify rendering settings in any way. If rendering settings
        for a pixels set are not present, thumbnail creation for
        that pixels set will not be performed.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createThumbnailsByLongestSideSet(self, size, pixelsIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_createThumbnailsByLongestSideSet.begin(self, ((size, pixelsIds), _response, _ex, _sent, _ctx))

        """
        Creates thumbnails for a number of pixels sets using a
        given set of rendering settings (RenderingDef) in the
        on-disk cache. Unlike the other thumbnail creation methods,
        this method may be called without first calling
        {@code setPixelsId}. This method will not reset or
        modify rendering settings in any way. If rendering settings
        for a pixels set are not present, thumbnail creation for
        that pixels set will not be performed.
        Arguments:
        size -- the size of the longest side of the thumbnail requested. null specifies the default size of 48.
        pixelsIds -- the Pixels sets to retrieve thumbnails for.
        Throws:
        ApiUsageException -- if: size is greater than pixels.sizeX and pixels.sizeY size is negative
        """
        def end_createThumbnailsByLongestSideSet(self, _r):
            return _M_ode.api.ThumbnailStore._op_createThumbnailsByLongestSideSet.end(self, _r)

        """
        Checks if a thumbnail of a particular size exists for a
        pixels set.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _ctx -- The request context for the invocation.
        Throws:
        ApiUsageException -- if: sizeX is negative sizeY is negative setPixelsId has not yet been called
        """
        def thumbnailExists(self, sizeX, sizeY, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_thumbnailExists.invoke(self, ((sizeX, sizeY), _ctx))

        """
        Checks if a thumbnail of a particular size exists for a
        pixels set.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_thumbnailExists(self, sizeX, sizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_thumbnailExists.begin(self, ((sizeX, sizeY), _response, _ex, _sent, _ctx))

        """
        Checks if a thumbnail of a particular size exists for a
        pixels set.
        Arguments:
        sizeX -- the X-axis width of the thumbnail. null specifies the default size of 48.
        sizeY -- the Y-axis width of the thumbnail. null specifies the default size of 48.
        Throws:
        ApiUsageException -- if: sizeX is negative sizeY is negative setPixelsId has not yet been called
        """
        def end_thumbnailExists(self, _r):
            return _M_ode.api.ThumbnailStore._op_thumbnailExists.end(self, _r)

        """
        Resets the rendering definition for the active pixels set
        to its default settings.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def resetDefaults(self, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_resetDefaults.invoke(self, ((), _ctx))

        """
        Resets the rendering definition for the active pixels set
        to its default settings.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaults(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.ThumbnailStore._op_resetDefaults.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Resets the rendering definition for the active pixels set
        to its default settings.
        Arguments:
        """
        def end_resetDefaults(self, _r):
            return _M_ode.api.ThumbnailStore._op_resetDefaults.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.ThumbnailStorePrx.ice_checkedCast(proxy, '::ode::api::ThumbnailStore', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.ThumbnailStorePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::ThumbnailStore'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_ThumbnailStorePrx = IcePy.defineProxy('::ode::api::ThumbnailStore', ThumbnailStorePrx)

    _M_ode.api._t_ThumbnailStore = IcePy.defineClass('::ode::api::ThumbnailStore', ThumbnailStore, -1, (), True, False, None, (_M_ode.api._t_StatefulServiceInterface,), ())
    ThumbnailStore._ice_type = _M_ode.api._t_ThumbnailStore

    ThumbnailStore._op_setPixelsId = IcePy.Operation('setPixelsId', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_isInProgress = IcePy.Operation('isInProgress', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_setRenderingDefId = IcePy.Operation('setRenderingDefId', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    ThumbnailStore._op_getRenderingDefId = IcePy.Operation('getRenderingDefId', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnail = IcePy.Operation('getThumbnail', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailWithoutDefault = IcePy.Operation('getThumbnailWithoutDefault', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailSet = IcePy.Operation('getThumbnailSet', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.sys._t_IdByteMap, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailByLongestSideSet = IcePy.Operation('getThumbnailByLongestSideSet', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.sys._t_IdByteMap, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailByLongestSide = IcePy.Operation('getThumbnailByLongestSide', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0),), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailByLongestSideDirect = IcePy.Operation('getThumbnailByLongestSideDirect', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0),), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailDirect = IcePy.Operation('getThumbnailDirect', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailForSectionDirect = IcePy.Operation('getThumbnailForSectionDirect', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_getThumbnailForSectionByLongestSideDirect = IcePy.Operation('getThumbnailForSectionByLongestSideDirect', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), _M_ode._t_RInt, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_createThumbnails = IcePy.Operation('createThumbnails', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    ThumbnailStore._op_createThumbnail = IcePy.Operation('createThumbnail', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0)), (), None, (_M_ode._t_ServerError,))
    ThumbnailStore._op_createThumbnailsByLongestSideSet = IcePy.Operation('createThumbnailsByLongestSideSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), None, (_M_ode._t_ServerError,))
    ThumbnailStore._op_thumbnailExists = IcePy.Operation('thumbnailExists', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0)), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    ThumbnailStore._op_resetDefaults = IcePy.Operation('resetDefaults', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), None, (_M_ode._t_ServerError,))

    _M_ode.api.ThumbnailStore = ThumbnailStore
    del ThumbnailStore

    _M_ode.api.ThumbnailStorePrx = ThumbnailStorePrx
    del ThumbnailStorePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
