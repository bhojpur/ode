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
import ode_Collections_ice
import ode_ServicesF_ice

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

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'IPixels' not in _M_ode.api.__dict__:
    _M_ode.api.IPixels = Ice.createTempClass()
    class IPixels(_M_ode.api.ServiceInterface):
        """
        Metadata gateway for the ode.api.RenderingEngine and
        clients. This service provides all DB access that the rendering
        engine needs as well as Pixels services to a client. It also allows
        the rendering  engine to also be run external to the server (e.g.
        client-side).
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IPixels:
                raise RuntimeError('ode.api.IPixels is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IPixels', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IPixels'

        def ice_staticId():
            return '::ode::api::IPixels'
        ice_staticId = staticmethod(ice_staticId)

        def retrievePixDescription_async(self, _cb, pixId, current=None):
            """
            Retrieves the pixels metadata. The following objects are
            pre-linked:
            pixels.pixelsType
            pixels.pixelsDimensions
            pixels.channels
            pixels.channnels.statsInfo
            pixels.channnels.colorComponent
            pixels.channnels.logicalChannel
            pixels.channnels.logicalChannel.photometricInterpretation
            Arguments:
            _cb -- The asynchronous callback object.
            pixId -- Pixels id.
            current -- The Current object for the invocation.
            """
            pass

        def retrieveRndSettings_async(self, _cb, pixId, current=None):
            """
            Retrieves the rendering settings for a given pixels set and
            the currently logged in user. If the current user has no
            ode.model.RenderingDef, and the user is an
            administrator, then a ode.model.RenderingDef may
            be returned for the owner of the
            ode.model.Pixels. This matches the behavior of the
            Rendering service.
            The following objects will be pre-linked:
            renderingDef.quantization
            renderingDef.model
            renderingDef.waveRendering
            renderingDef.waveRendering.color
            renderingDef.waveRendering.family
            renderingDef.spatialDomainEnhancement
            Arguments:
            _cb -- The asynchronous callback object.
            pixId -- Pixels id.
            current -- The Current object for the invocation.
            """
            pass

        def retrieveRndSettingsFor_async(self, _cb, pixId, userId, current=None):
            """
            Retrieves the rendering settings for a given pixels set and
            the passed user. The following objects are pre-linked:
            renderingDef.quantization
            renderingDef.model
            renderingDef.waveRendering
            renderingDef.waveRendering.color
            renderingDef.waveRendering.family
            renderingDef.spatialDomainEnhancement
            Arguments:
            _cb -- The asynchronous callback object.
            pixId -- Pixels id.
            userId -- The id of the user.
            current -- The Current object for the invocation.
            """
            pass

        def retrieveAllRndSettings_async(self, _cb, pixId, userId, current=None):
            """
            Retrieves all the rendering settings for a given pixels set
            and the passed user. The following objects are pre-linked:
            renderingDef.quantization
            renderingDef.model
            renderingDef.waveRendering
            renderingDef.waveRendering.color
            renderingDef.waveRendering.family
            renderingDef.spatialDomainEnhancement
            Arguments:
            _cb -- The asynchronous callback object.
            pixId -- Pixels id.
            userId -- The id of the user.
            current -- The Current object for the invocation.
            """
            pass

        def loadRndSettings_async(self, _cb, renderingSettingsId, current=None):
            """
            Loads a specific set of rendering settings. The
            following objects are pre-linked:
            renderingDef.quantization
            renderingDef.model
            renderingDef.waveRendering
            renderingDef.waveRendering.color
            renderingDef.waveRendering.family
            renderingDef.spatialDomainEnhancement
            Arguments:
            _cb -- The asynchronous callback object.
            renderingSettingsId -- Rendering definition id.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If no RenderingDef matches the ID renderingDefId.
            """
            pass

        def saveRndSettings_async(self, _cb, rndSettings, current=None):
            """
            Saves the specified rendering settings.
            Arguments:
            _cb -- The asynchronous callback object.
            rndSettings -- Rendering settings.
            current -- The Current object for the invocation.
            """
            pass

        def getBitDepth_async(self, _cb, type, current=None):
            """
            Bit depth for a given pixel type.
            Arguments:
            _cb -- The asynchronous callback object.
            type -- Pixels type.
            current -- The Current object for the invocation.
            """
            pass

        def getEnumeration_async(self, _cb, enumClass, value, current=None):
            """
            Retrieves a particular enumeration for a given enumeration
            class.
            Arguments:
            _cb -- The asynchronous callback object.
            enumClass -- Enumeration class.
            value -- Enumeration string value.
            current -- The Current object for the invocation.
            """
            pass

        def getAllEnumerations_async(self, _cb, enumClass, current=None):
            """
            Retrieves the exhaustive list of enumerations for a given
            enumeration class.
            Arguments:
            _cb -- The asynchronous callback object.
            enumClass -- Enumeration class.
            current -- The Current object for the invocation.
            """
            pass

        def copyAndResizePixels_async(self, _cb, pixelsId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats, current=None):
            """
            Copies the metadata, and only the metadata linked to
            a Pixels object into a new Pixels object of equal or
            differing size across one or many of its three physical
            dimensions or temporal dimension.
            It is beyond the scope of this method to handle updates or
            changes to the raw pixel data available through
            ode.api.RawPixelsStore or to add
            and link ode.model.PlaneInfo and/or other Pixels
            set specific metadata.
            It is also assumed that the caller wishes the pixels
            dimensions and ode.model.PixelsType to remain the
            same; changing these is outside the scope of this method.
            NOTE: As ode.model.Channel objects are
            only able to apply to a single set of Pixels any
            annotations or linkage to these objects will be lost.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The source Pixels set id.
            sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
            sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
            sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
            sizeT -- The new number of timepoints. null if the copy should maintain the same number.
            channelList -- The channels that should be copied into the new Pixels set.
            methodology -- An optional string signifying the methodology that will be used to produce this new Pixels set.
            copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If the X, Y, Z, T or channelList dimensions are out of bounds or the Pixels object corresponding to pixelsId is unlocatable.
            """
            pass

        def copyAndResizeImage_async(self, _cb, imageId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats, current=None):
            """
            Copies the metadata, and only the metadata linked to
            a Image object into a new Image object of equal or
            differing size across one or many of its three physical
            dimensions or temporal dimension.
            It is beyond the scope of this method to handle updates or
            changes to  the raw pixel data available through
            ode.api.RawPixelsStore or to add
            and link ode.model.PlaneInfo and/or other Pixels
            set specific metadata.
            It is also assumed that the caller wishes the pixels
            dimensions and ode.model.PixelsType to remain the
            same; changing these is outside the scope of this method.
            NOTE: As ode.model.Channel objects are
            only able to apply to a single set of Pixels any
            annotations or linkage to these objects will be lost.
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- The source Image id.
            sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
            sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
            sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
            sizeT -- The new number of timepoints. null if the copy should maintain the same number.
            channelList -- The channels that should be copied into the new Pixels set.
            methodology -- The name of the new Image.
            copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If the X, Y, Z, T or channelList dimensions are out of bounds or the Pixels object corresponding to pixelsId is unlocatable.
            """
            pass

        def createImage_async(self, _cb, sizeX, sizeY, sizeZ, sizeT, channelList, pixelsType, name, description, current=None):
            """
            Creates the metadata, and only the metadata linked
            to an Image object. It is beyond the scope of this method
            to handle updates or changes to the raw pixel data
            available through ode.api.RawPixelsStore or to
            add and link ode.model.PlaneInfo or
            ode.model.StatsInfo objects and/or other Pixels
            set specific metadata. It is also up to the caller to
            update the pixels dimensions.
            Arguments:
            _cb -- The asynchronous callback object.
            sizeX -- The new size across the X-axis.
            sizeY -- The new size across the Y-axis.
            sizeZ -- The new size across the Z-axis.
            sizeT -- The new number of timepoints.
            channelList -- 
            pixelsType -- The pixelsType
            name -- The name of the new Image.
            description -- The description of the new Image.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If the channel list is null or of size == 0.
            """
            pass

        def setChannelGlobalMinMax_async(self, _cb, pixelsId, channelIndex, min, max, current=None):
            """
            Sets the channel global (all 2D optical sections
            corresponding to a particular channel) minimum and maximum
            for a Pixels set.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The source Pixels set id.
            channelIndex -- The channel index within the Pixels set.
            min -- The channel global minimum.
            max -- The channel global maximum.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IPixels)

        __repr__ = __str__

    _M_ode.api.IPixelsPrx = Ice.createTempClass()
    class IPixelsPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Retrieves the pixels metadata. The following objects are
        pre-linked:
        pixels.pixelsType
        pixels.pixelsDimensions
        pixels.channels
        pixels.channnels.statsInfo
        pixels.channnels.colorComponent
        pixels.channnels.logicalChannel
        pixels.channnels.logicalChannel.photometricInterpretation
        Arguments:
        pixId -- Pixels id.
        _ctx -- The request context for the invocation.
        Returns: Pixels object which matches id.
        """
        def retrievePixDescription(self, pixId, _ctx=None):
            return _M_ode.api.IPixels._op_retrievePixDescription.invoke(self, ((pixId, ), _ctx))

        """
        Retrieves the pixels metadata. The following objects are
        pre-linked:
        pixels.pixelsType
        pixels.pixelsDimensions
        pixels.channels
        pixels.channnels.statsInfo
        pixels.channnels.colorComponent
        pixels.channnels.logicalChannel
        pixels.channnels.logicalChannel.photometricInterpretation
        Arguments:
        pixId -- Pixels id.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_retrievePixDescription(self, pixId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_retrievePixDescription.begin(self, ((pixId, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the pixels metadata. The following objects are
        pre-linked:
        pixels.pixelsType
        pixels.pixelsDimensions
        pixels.channels
        pixels.channnels.statsInfo
        pixels.channnels.colorComponent
        pixels.channnels.logicalChannel
        pixels.channnels.logicalChannel.photometricInterpretation
        Arguments:
        pixId -- Pixels id.
        Returns: Pixels object which matches id.
        """
        def end_retrievePixDescription(self, _r):
            return _M_ode.api.IPixels._op_retrievePixDescription.end(self, _r)

        """
        Retrieves the rendering settings for a given pixels set and
        the currently logged in user. If the current user has no
        ode.model.RenderingDef, and the user is an
        administrator, then a ode.model.RenderingDef may
        be returned for the owner of the
        ode.model.Pixels. This matches the behavior of the
        Rendering service.
        The following objects will be pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        _ctx -- The request context for the invocation.
        Returns: Rendering definition.
        """
        def retrieveRndSettings(self, pixId, _ctx=None):
            return _M_ode.api.IPixels._op_retrieveRndSettings.invoke(self, ((pixId, ), _ctx))

        """
        Retrieves the rendering settings for a given pixels set and
        the currently logged in user. If the current user has no
        ode.model.RenderingDef, and the user is an
        administrator, then a ode.model.RenderingDef may
        be returned for the owner of the
        ode.model.Pixels. This matches the behavior of the
        Rendering service.
        The following objects will be pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_retrieveRndSettings(self, pixId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_retrieveRndSettings.begin(self, ((pixId, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the rendering settings for a given pixels set and
        the currently logged in user. If the current user has no
        ode.model.RenderingDef, and the user is an
        administrator, then a ode.model.RenderingDef may
        be returned for the owner of the
        ode.model.Pixels. This matches the behavior of the
        Rendering service.
        The following objects will be pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        Returns: Rendering definition.
        """
        def end_retrieveRndSettings(self, _r):
            return _M_ode.api.IPixels._op_retrieveRndSettings.end(self, _r)

        """
        Retrieves the rendering settings for a given pixels set and
        the passed user. The following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        userId -- The id of the user.
        _ctx -- The request context for the invocation.
        Returns: Rendering definition.
        """
        def retrieveRndSettingsFor(self, pixId, userId, _ctx=None):
            return _M_ode.api.IPixels._op_retrieveRndSettingsFor.invoke(self, ((pixId, userId), _ctx))

        """
        Retrieves the rendering settings for a given pixels set and
        the passed user. The following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        userId -- The id of the user.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_retrieveRndSettingsFor(self, pixId, userId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_retrieveRndSettingsFor.begin(self, ((pixId, userId), _response, _ex, _sent, _ctx))

        """
        Retrieves the rendering settings for a given pixels set and
        the passed user. The following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        userId -- The id of the user.
        Returns: Rendering definition.
        """
        def end_retrieveRndSettingsFor(self, _r):
            return _M_ode.api.IPixels._op_retrieveRndSettingsFor.end(self, _r)

        """
        Retrieves all the rendering settings for a given pixels set
        and the passed user. The following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        userId -- The id of the user.
        _ctx -- The request context for the invocation.
        Returns: Rendering definition.
        """
        def retrieveAllRndSettings(self, pixId, userId, _ctx=None):
            return _M_ode.api.IPixels._op_retrieveAllRndSettings.invoke(self, ((pixId, userId), _ctx))

        """
        Retrieves all the rendering settings for a given pixels set
        and the passed user. The following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        userId -- The id of the user.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_retrieveAllRndSettings(self, pixId, userId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_retrieveAllRndSettings.begin(self, ((pixId, userId), _response, _ex, _sent, _ctx))

        """
        Retrieves all the rendering settings for a given pixels set
        and the passed user. The following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        pixId -- Pixels id.
        userId -- The id of the user.
        Returns: Rendering definition.
        """
        def end_retrieveAllRndSettings(self, _r):
            return _M_ode.api.IPixels._op_retrieveAllRndSettings.end(self, _r)

        """
        Loads a specific set of rendering settings. The
        following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        renderingSettingsId -- Rendering definition id.
        _ctx -- The request context for the invocation.
        Returns: Rendering definition.
        Throws:
        ValidationException -- If no RenderingDef matches the ID renderingDefId.
        """
        def loadRndSettings(self, renderingSettingsId, _ctx=None):
            return _M_ode.api.IPixels._op_loadRndSettings.invoke(self, ((renderingSettingsId, ), _ctx))

        """
        Loads a specific set of rendering settings. The
        following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        renderingSettingsId -- Rendering definition id.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadRndSettings(self, renderingSettingsId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_loadRndSettings.begin(self, ((renderingSettingsId, ), _response, _ex, _sent, _ctx))

        """
        Loads a specific set of rendering settings. The
        following objects are pre-linked:
        renderingDef.quantization
        renderingDef.model
        renderingDef.waveRendering
        renderingDef.waveRendering.color
        renderingDef.waveRendering.family
        renderingDef.spatialDomainEnhancement
        Arguments:
        renderingSettingsId -- Rendering definition id.
        Returns: Rendering definition.
        Throws:
        ValidationException -- If no RenderingDef matches the ID renderingDefId.
        """
        def end_loadRndSettings(self, _r):
            return _M_ode.api.IPixels._op_loadRndSettings.end(self, _r)

        """
        Saves the specified rendering settings.
        Arguments:
        rndSettings -- Rendering settings.
        _ctx -- The request context for the invocation.
        """
        def saveRndSettings(self, rndSettings, _ctx=None):
            return _M_ode.api.IPixels._op_saveRndSettings.invoke(self, ((rndSettings, ), _ctx))

        """
        Saves the specified rendering settings.
        Arguments:
        rndSettings -- Rendering settings.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_saveRndSettings(self, rndSettings, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_saveRndSettings.begin(self, ((rndSettings, ), _response, _ex, _sent, _ctx))

        """
        Saves the specified rendering settings.
        Arguments:
        rndSettings -- Rendering settings.
        """
        def end_saveRndSettings(self, _r):
            return _M_ode.api.IPixels._op_saveRndSettings.end(self, _r)

        """
        Bit depth for a given pixel type.
        Arguments:
        type -- Pixels type.
        _ctx -- The request context for the invocation.
        Returns: Bit depth in bits.
        """
        def getBitDepth(self, type, _ctx=None):
            return _M_ode.api.IPixels._op_getBitDepth.invoke(self, ((type, ), _ctx))

        """
        Bit depth for a given pixel type.
        Arguments:
        type -- Pixels type.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getBitDepth(self, type, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_getBitDepth.begin(self, ((type, ), _response, _ex, _sent, _ctx))

        """
        Bit depth for a given pixel type.
        Arguments:
        type -- Pixels type.
        Returns: Bit depth in bits.
        """
        def end_getBitDepth(self, _r):
            return _M_ode.api.IPixels._op_getBitDepth.end(self, _r)

        """
        Retrieves a particular enumeration for a given enumeration
        class.
        Arguments:
        enumClass -- Enumeration class.
        value -- Enumeration string value.
        _ctx -- The request context for the invocation.
        Returns: Enumeration object.
        """
        def getEnumeration(self, enumClass, value, _ctx=None):
            return _M_ode.api.IPixels._op_getEnumeration.invoke(self, ((enumClass, value), _ctx))

        """
        Retrieves a particular enumeration for a given enumeration
        class.
        Arguments:
        enumClass -- Enumeration class.
        value -- Enumeration string value.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getEnumeration(self, enumClass, value, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_getEnumeration.begin(self, ((enumClass, value), _response, _ex, _sent, _ctx))

        """
        Retrieves a particular enumeration for a given enumeration
        class.
        Arguments:
        enumClass -- Enumeration class.
        value -- Enumeration string value.
        Returns: Enumeration object.
        """
        def end_getEnumeration(self, _r):
            return _M_ode.api.IPixels._op_getEnumeration.end(self, _r)

        """
        Retrieves the exhaustive list of enumerations for a given
        enumeration class.
        Arguments:
        enumClass -- Enumeration class.
        _ctx -- The request context for the invocation.
        Returns: List of all enumeration objects for the enumClass.
        """
        def getAllEnumerations(self, enumClass, _ctx=None):
            return _M_ode.api.IPixels._op_getAllEnumerations.invoke(self, ((enumClass, ), _ctx))

        """
        Retrieves the exhaustive list of enumerations for a given
        enumeration class.
        Arguments:
        enumClass -- Enumeration class.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAllEnumerations(self, enumClass, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_getAllEnumerations.begin(self, ((enumClass, ), _response, _ex, _sent, _ctx))

        """
        Retrieves the exhaustive list of enumerations for a given
        enumeration class.
        Arguments:
        enumClass -- Enumeration class.
        Returns: List of all enumeration objects for the enumClass.
        """
        def end_getAllEnumerations(self, _r):
            return _M_ode.api.IPixels._op_getAllEnumerations.end(self, _r)

        """
        Copies the metadata, and only the metadata linked to
        a Pixels object into a new Pixels object of equal or
        differing size across one or many of its three physical
        dimensions or temporal dimension.
        It is beyond the scope of this method to handle updates or
        changes to the raw pixel data available through
        ode.api.RawPixelsStore or to add
        and link ode.model.PlaneInfo and/or other Pixels
        set specific metadata.
        It is also assumed that the caller wishes the pixels
        dimensions and ode.model.PixelsType to remain the
        same; changing these is outside the scope of this method.
        NOTE: As ode.model.Channel objects are
        only able to apply to a single set of Pixels any
        annotations or linkage to these objects will be lost.
        Arguments:
        pixelsId -- The source Pixels set id.
        sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
        sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
        sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
        sizeT -- The new number of timepoints. null if the copy should maintain the same number.
        channelList -- The channels that should be copied into the new Pixels set.
        methodology -- An optional string signifying the methodology that will be used to produce this new Pixels set.
        copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
        _ctx -- The request context for the invocation.
        Returns: Id of the new Pixels object on success or null on failure.
        Throws:
        ValidationException -- If the X, Y, Z, T or channelList dimensions are out of bounds or the Pixels object corresponding to pixelsId is unlocatable.
        """
        def copyAndResizePixels(self, pixelsId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats, _ctx=None):
            return _M_ode.api.IPixels._op_copyAndResizePixels.invoke(self, ((pixelsId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats), _ctx))

        """
        Copies the metadata, and only the metadata linked to
        a Pixels object into a new Pixels object of equal or
        differing size across one or many of its three physical
        dimensions or temporal dimension.
        It is beyond the scope of this method to handle updates or
        changes to the raw pixel data available through
        ode.api.RawPixelsStore or to add
        and link ode.model.PlaneInfo and/or other Pixels
        set specific metadata.
        It is also assumed that the caller wishes the pixels
        dimensions and ode.model.PixelsType to remain the
        same; changing these is outside the scope of this method.
        NOTE: As ode.model.Channel objects are
        only able to apply to a single set of Pixels any
        annotations or linkage to these objects will be lost.
        Arguments:
        pixelsId -- The source Pixels set id.
        sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
        sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
        sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
        sizeT -- The new number of timepoints. null if the copy should maintain the same number.
        channelList -- The channels that should be copied into the new Pixels set.
        methodology -- An optional string signifying the methodology that will be used to produce this new Pixels set.
        copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_copyAndResizePixels(self, pixelsId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_copyAndResizePixels.begin(self, ((pixelsId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats), _response, _ex, _sent, _ctx))

        """
        Copies the metadata, and only the metadata linked to
        a Pixels object into a new Pixels object of equal or
        differing size across one or many of its three physical
        dimensions or temporal dimension.
        It is beyond the scope of this method to handle updates or
        changes to the raw pixel data available through
        ode.api.RawPixelsStore or to add
        and link ode.model.PlaneInfo and/or other Pixels
        set specific metadata.
        It is also assumed that the caller wishes the pixels
        dimensions and ode.model.PixelsType to remain the
        same; changing these is outside the scope of this method.
        NOTE: As ode.model.Channel objects are
        only able to apply to a single set of Pixels any
        annotations or linkage to these objects will be lost.
        Arguments:
        pixelsId -- The source Pixels set id.
        sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
        sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
        sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
        sizeT -- The new number of timepoints. null if the copy should maintain the same number.
        channelList -- The channels that should be copied into the new Pixels set.
        methodology -- An optional string signifying the methodology that will be used to produce this new Pixels set.
        copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
        Returns: Id of the new Pixels object on success or null on failure.
        Throws:
        ValidationException -- If the X, Y, Z, T or channelList dimensions are out of bounds or the Pixels object corresponding to pixelsId is unlocatable.
        """
        def end_copyAndResizePixels(self, _r):
            return _M_ode.api.IPixels._op_copyAndResizePixels.end(self, _r)

        """
        Copies the metadata, and only the metadata linked to
        a Image object into a new Image object of equal or
        differing size across one or many of its three physical
        dimensions or temporal dimension.
        It is beyond the scope of this method to handle updates or
        changes to  the raw pixel data available through
        ode.api.RawPixelsStore or to add
        and link ode.model.PlaneInfo and/or other Pixels
        set specific metadata.
        It is also assumed that the caller wishes the pixels
        dimensions and ode.model.PixelsType to remain the
        same; changing these is outside the scope of this method.
        NOTE: As ode.model.Channel objects are
        only able to apply to a single set of Pixels any
        annotations or linkage to these objects will be lost.
        Arguments:
        imageId -- The source Image id.
        sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
        sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
        sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
        sizeT -- The new number of timepoints. null if the copy should maintain the same number.
        channelList -- The channels that should be copied into the new Pixels set.
        methodology -- The name of the new Image.
        copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
        _ctx -- The request context for the invocation.
        Returns: Id of the new Pixels object on success or null on failure.
        Throws:
        ValidationException -- If the X, Y, Z, T or channelList dimensions are out of bounds or the Pixels object corresponding to pixelsId is unlocatable.
        """
        def copyAndResizeImage(self, imageId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats, _ctx=None):
            return _M_ode.api.IPixels._op_copyAndResizeImage.invoke(self, ((imageId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats), _ctx))

        """
        Copies the metadata, and only the metadata linked to
        a Image object into a new Image object of equal or
        differing size across one or many of its three physical
        dimensions or temporal dimension.
        It is beyond the scope of this method to handle updates or
        changes to  the raw pixel data available through
        ode.api.RawPixelsStore or to add
        and link ode.model.PlaneInfo and/or other Pixels
        set specific metadata.
        It is also assumed that the caller wishes the pixels
        dimensions and ode.model.PixelsType to remain the
        same; changing these is outside the scope of this method.
        NOTE: As ode.model.Channel objects are
        only able to apply to a single set of Pixels any
        annotations or linkage to these objects will be lost.
        Arguments:
        imageId -- The source Image id.
        sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
        sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
        sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
        sizeT -- The new number of timepoints. null if the copy should maintain the same number.
        channelList -- The channels that should be copied into the new Pixels set.
        methodology -- The name of the new Image.
        copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_copyAndResizeImage(self, imageId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_copyAndResizeImage.begin(self, ((imageId, sizeX, sizeY, sizeZ, sizeT, channelList, methodology, copyStats), _response, _ex, _sent, _ctx))

        """
        Copies the metadata, and only the metadata linked to
        a Image object into a new Image object of equal or
        differing size across one or many of its three physical
        dimensions or temporal dimension.
        It is beyond the scope of this method to handle updates or
        changes to  the raw pixel data available through
        ode.api.RawPixelsStore or to add
        and link ode.model.PlaneInfo and/or other Pixels
        set specific metadata.
        It is also assumed that the caller wishes the pixels
        dimensions and ode.model.PixelsType to remain the
        same; changing these is outside the scope of this method.
        NOTE: As ode.model.Channel objects are
        only able to apply to a single set of Pixels any
        annotations or linkage to these objects will be lost.
        Arguments:
        imageId -- The source Image id.
        sizeX -- The new size across the X-axis. null if the copy should maintain the same size.
        sizeY -- The new size across the Y-axis. null if the copy should maintain the same size.
        sizeZ -- The new size across the Z-axis. null if the copy should maintain the same size.
        sizeT -- The new number of timepoints. null if the copy should maintain the same number.
        channelList -- The channels that should be copied into the new Pixels set.
        methodology -- The name of the new Image.
        copyStats -- Whether or not to copy the ode.model.StatsInfo for each channel.
        Returns: Id of the new Pixels object on success or null on failure.
        Throws:
        ValidationException -- If the X, Y, Z, T or channelList dimensions are out of bounds or the Pixels object corresponding to pixelsId is unlocatable.
        """
        def end_copyAndResizeImage(self, _r):
            return _M_ode.api.IPixels._op_copyAndResizeImage.end(self, _r)

        """
        Creates the metadata, and only the metadata linked
        to an Image object. It is beyond the scope of this method
        to handle updates or changes to the raw pixel data
        available through ode.api.RawPixelsStore or to
        add and link ode.model.PlaneInfo or
        ode.model.StatsInfo objects and/or other Pixels
        set specific metadata. It is also up to the caller to
        update the pixels dimensions.
        Arguments:
        sizeX -- The new size across the X-axis.
        sizeY -- The new size across the Y-axis.
        sizeZ -- The new size across the Z-axis.
        sizeT -- The new number of timepoints.
        channelList -- 
        pixelsType -- The pixelsType
        name -- The name of the new Image.
        description -- The description of the new Image.
        _ctx -- The request context for the invocation.
        Returns: Id of the new Image object on success or null on failure.
        Throws:
        ValidationException -- If the channel list is null or of size == 0.
        """
        def createImage(self, sizeX, sizeY, sizeZ, sizeT, channelList, pixelsType, name, description, _ctx=None):
            return _M_ode.api.IPixels._op_createImage.invoke(self, ((sizeX, sizeY, sizeZ, sizeT, channelList, pixelsType, name, description), _ctx))

        """
        Creates the metadata, and only the metadata linked
        to an Image object. It is beyond the scope of this method
        to handle updates or changes to the raw pixel data
        available through ode.api.RawPixelsStore or to
        add and link ode.model.PlaneInfo or
        ode.model.StatsInfo objects and/or other Pixels
        set specific metadata. It is also up to the caller to
        update the pixels dimensions.
        Arguments:
        sizeX -- The new size across the X-axis.
        sizeY -- The new size across the Y-axis.
        sizeZ -- The new size across the Z-axis.
        sizeT -- The new number of timepoints.
        channelList -- 
        pixelsType -- The pixelsType
        name -- The name of the new Image.
        description -- The description of the new Image.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createImage(self, sizeX, sizeY, sizeZ, sizeT, channelList, pixelsType, name, description, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_createImage.begin(self, ((sizeX, sizeY, sizeZ, sizeT, channelList, pixelsType, name, description), _response, _ex, _sent, _ctx))

        """
        Creates the metadata, and only the metadata linked
        to an Image object. It is beyond the scope of this method
        to handle updates or changes to the raw pixel data
        available through ode.api.RawPixelsStore or to
        add and link ode.model.PlaneInfo or
        ode.model.StatsInfo objects and/or other Pixels
        set specific metadata. It is also up to the caller to
        update the pixels dimensions.
        Arguments:
        sizeX -- The new size across the X-axis.
        sizeY -- The new size across the Y-axis.
        sizeZ -- The new size across the Z-axis.
        sizeT -- The new number of timepoints.
        channelList -- 
        pixelsType -- The pixelsType
        name -- The name of the new Image.
        description -- The description of the new Image.
        Returns: Id of the new Image object on success or null on failure.
        Throws:
        ValidationException -- If the channel list is null or of size == 0.
        """
        def end_createImage(self, _r):
            return _M_ode.api.IPixels._op_createImage.end(self, _r)

        """
        Sets the channel global (all 2D optical sections
        corresponding to a particular channel) minimum and maximum
        for a Pixels set.
        Arguments:
        pixelsId -- The source Pixels set id.
        channelIndex -- The channel index within the Pixels set.
        min -- The channel global minimum.
        max -- The channel global maximum.
        _ctx -- The request context for the invocation.
        """
        def setChannelGlobalMinMax(self, pixelsId, channelIndex, min, max, _ctx=None):
            return _M_ode.api.IPixels._op_setChannelGlobalMinMax.invoke(self, ((pixelsId, channelIndex, min, max), _ctx))

        """
        Sets the channel global (all 2D optical sections
        corresponding to a particular channel) minimum and maximum
        for a Pixels set.
        Arguments:
        pixelsId -- The source Pixels set id.
        channelIndex -- The channel index within the Pixels set.
        min -- The channel global minimum.
        max -- The channel global maximum.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setChannelGlobalMinMax(self, pixelsId, channelIndex, min, max, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IPixels._op_setChannelGlobalMinMax.begin(self, ((pixelsId, channelIndex, min, max), _response, _ex, _sent, _ctx))

        """
        Sets the channel global (all 2D optical sections
        corresponding to a particular channel) minimum and maximum
        for a Pixels set.
        Arguments:
        pixelsId -- The source Pixels set id.
        channelIndex -- The channel index within the Pixels set.
        min -- The channel global minimum.
        max -- The channel global maximum.
        """
        def end_setChannelGlobalMinMax(self, _r):
            return _M_ode.api.IPixels._op_setChannelGlobalMinMax.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IPixelsPrx.ice_checkedCast(proxy, '::ode::api::IPixels', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IPixelsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IPixels'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IPixelsPrx = IcePy.defineProxy('::ode::api::IPixels', IPixelsPrx)

    _M_ode.api._t_IPixels = IcePy.defineClass('::ode::api::IPixels', IPixels, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IPixels._ice_type = _M_ode.api._t_IPixels

    IPixels._op_retrievePixDescription = IcePy.Operation('retrievePixDescription', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_Pixels, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_retrieveRndSettings = IcePy.Operation('retrieveRndSettings', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_RenderingDef, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_retrieveRndSettingsFor = IcePy.Operation('retrieveRndSettingsFor', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.model._t_RenderingDef, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_retrieveAllRndSettings = IcePy.Operation('retrieveAllRndSettings', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_loadRndSettings = IcePy.Operation('loadRndSettings', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_RenderingDef, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_saveRndSettings = IcePy.Operation('saveRndSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, (_M_ode._t_ServerError,))
    IPixels._op_getBitDepth = IcePy.Operation('getBitDepth', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_PixelsType, False, 0),), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_getEnumeration = IcePy.Operation('getEnumeration', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.model._t_IObject, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_getEnumeration.deprecate("Use ITypes#getEnumeration(string, string) instead.")
    IPixels._op_getAllEnumerations = IcePy.Operation('getAllEnumerations', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_getAllEnumerations.deprecate("Use ITypes#allEnumerations(string) instead.")
    IPixels._op_copyAndResizePixels = IcePy.Operation('copyAndResizePixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode.sys._t_IntList, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_bool, False, 0)), (), ((), _M_ode._t_RLong, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_copyAndResizeImage = IcePy.Operation('copyAndResizeImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode._t_RInt, False, 0), ((), _M_ode.sys._t_IntList, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_bool, False, 0)), (), ((), _M_ode._t_RLong, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_createImage = IcePy.Operation('createImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), _M_ode.sys._t_IntList, False, 0), ((), _M_ode.model._t_PixelsType, False, 0), ((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode._t_RLong, False, 0), (_M_ode._t_ServerError,))
    IPixels._op_setChannelGlobalMinMax = IcePy.Operation('setChannelGlobalMinMax', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_double, False, 0), ((), IcePy._t_double, False, 0)), (), None, (_M_ode._t_ServerError,))

    _M_ode.api.IPixels = IPixels
    del IPixels

    _M_ode.api.IPixelsPrx = IPixelsPrx
    del IPixelsPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
