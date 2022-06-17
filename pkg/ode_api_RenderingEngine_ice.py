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
import ode_Constants_ice
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

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Start of module ode
__name__ = 'ode'

# Start of module ode.api
__name__ = 'ode.api'

if 'RenderingEngine' not in _M_ode.api.__dict__:
    _M_ode.api.RenderingEngine = Ice.createTempClass()
    class RenderingEngine(_M_ode.api.PyramidService):
        """
        Defines a service to render a given pixels set.
        A pixels set is a 5D array that stores the pixels data of an
        image, that is the pixels intensity values. Every instance of this
        service is paired up to a pixels set. Use this service to transform
        planes within the pixels set onto an RGB image.
        The RenderingEngine allows to fine-tune the settings that
        define the transformation context, that is, a specification
        of how raw pixels data is to be transformed into an image that can
        be displayed on screen. Those settings are referred to as rendering
        settings or display options. After tuning those settings it is
        possible to save them to the metadata repository so that they can
        be used the next time the pixels set is accessed for rendering; for
        example by another RenderingEngine instance. Note that the display
        options are specific to the given pixels set and are experimenter
        scoped i.e. two different users can specify different
        display options for the same pixels set. (A RenderingEngine
        instance takes this into account automatically as it is always
        bound to a given experimenter.)
        This service is thread-safe.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.RenderingEngine:
                raise RuntimeError('ode.api.RenderingEngine is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::PyramidService', '::ode::api::RenderingEngine', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::RenderingEngine'

        def ice_staticId():
            return '::ode::api::RenderingEngine'
        ice_staticId = staticmethod(ice_staticId)

        def render_async(self, _cb, _def, current=None):
            """
            Renders the data selected by def according to
            the current rendering settings.
            The passed argument selects a plane orthogonal to one
            of the X, Y, or Z axes. How many
            wavelengths are rendered and what color model is used
            depends on the current rendering settings.
            Arguments:
            _cb -- The asynchronous callback object.
            _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If def is null.
            """
            pass

        def renderAsPackedInt_async(self, _cb, _def, current=None):
            """
            Renders the data selected by def according to
            the current rendering settings.
            The passed argument selects a plane orthogonal to one
            of the X, Y, or Z axes. How many
            wavelengths are rendered and what color model is used
            depends on the current rendering settings.
            Arguments:
            _cb -- The asynchronous callback object.
            _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If def is null.
            """
            pass

        def renderProjectedAsPackedInt_async(self, _cb, algorithm, timepoint, stepping, start, end, current=None):
            """
            Performs a projection through selected optical sections of
            a particular timepoint with the currently active channels
            and renders the data for display.
            Arguments:
            _cb -- The asynchronous callback object.
            algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
            timepoint -- 
            stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
            start -- Optical section to start projecting from.
            end -- Optical section to finish projecting.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- Where: algorithm is unknown timepoint is out of range start is out of range end is out of range start is greater than end
            """
            pass

        def renderCompressed_async(self, _cb, _def, current=None):
            """
            Renders the data selected by def according to
            the current rendering settings and compresses the resulting
            RGBA composite image.
            Arguments:
            _cb -- The asynchronous callback object.
            _def -- Selects a plane orthogonal to one of the X, Y or Z axes.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If def is null.
            """
            pass

        def renderProjectedCompressed_async(self, _cb, algorithm, timepoint, stepping, start, end, current=None):
            """
            Performs a projection through selected optical sections of
            a particular timepoint with the currently active channels,
            renders the data for display and compresses the resulting
            RGBA composite image.
            Arguments:
            _cb -- The asynchronous callback object.
            algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
            timepoint -- 
            stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
            start -- Optical section to start projecting from.
            end -- Optical section to finish projecting.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- Where: algorithm is unknown timepoint is out of range start is out of range end is out of range startis greater than end
            """
            pass

        def getRenderingDefId_async(self, _cb, current=None):
            """
            Returns the id of the ode.model.RenderingDef
            loaded by either {@code lookupRenderingDef} or
            {@code loadRenderingDef}.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def lookupPixels_async(self, _cb, pixelsId, current=None):
            """
            Loads the Pixels set this Rendering Engine is for.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The pixels set ID.
            current -- The Current object for the invocation.
            """
            pass

        def lookupRenderingDef_async(self, _cb, pixelsId, current=None):
            """
            Loads the rendering settings associated to the specified
            pixels set.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The pixels set ID.
            current -- The Current object for the invocation.
            """
            pass

        def loadRenderingDef_async(self, _cb, renderingDefId, current=None):
            """
            Loads a specific set of rendering settings that does not
            necessarily have to be linked to the given Pixels set.
            However, the rendering settings must be linked to a
            compatible Pixels set as defined by
            {@code ode.api.IRenderingSettings.sanityCheckPixels}.
            Arguments:
            _cb -- The asynchronous callback object.
            renderingDefId -- The rendering definition ID.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- If a RenderingDef does not exist with the ID renderingDefId or if the RenderingDef is incompatible due to differing pixels sets.
            """
            pass

        def setOverlays_async(self, _cb, tablesId, imageId, rowColorMap, current=None):
            """
            Informs the rendering engine that it should render a set of
            overlays on each rendered frame. These are expected to be
            binary masks.
            Arguments:
            _cb -- The asynchronous callback object.
            tablesId -- 
            imageId -- 
            rowColorMap -- Binary mask to color map.
            current -- The Current object for the invocation.
            """
            pass

        def load_async(self, _cb, current=None):
            """
            Creates an instance of the rendering engine.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setModel_async(self, _cb, model, current=None):
            """
            Specifies the model that dictates how transformed raw data
            has to be mapped onto a color space.
            Arguments:
            _cb -- The asynchronous callback object.
            model -- Identifies the color space model.
            current -- The Current object for the invocation.
            """
            pass

        def getModel_async(self, _cb, current=None):
            """
            Returns the model that dictates how transformed raw data
            has to be mapped onto a color space.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getDefaultZ_async(self, _cb, current=None):
            """
            Returns the index of the default focal section.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getDefaultT_async(self, _cb, current=None):
            """
            Returns the default timepoint index.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setDefaultZ_async(self, _cb, z, current=None):
            """
            Sets the index of the default focal section. This index is
            used to define a default plane.
            Arguments:
            _cb -- The asynchronous callback object.
            z -- The value to set.
            current -- The Current object for the invocation.
            """
            pass

        def setDefaultT_async(self, _cb, t, current=None):
            """
            Sets the default timepoint index. This index is used to
            define a default plane.
            Arguments:
            _cb -- The asynchronous callback object.
            t -- The value to set.
            current -- The Current object for the invocation.
            """
            pass

        def getPixels_async(self, _cb, current=None):
            """
            Returns the ode.model.Pixels set the Rendering
            engine is for.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getAvailableModels_async(self, _cb, current=None):
            """
            Returns the list of color models supported by the Rendering
            engine.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getAvailableFamilies_async(self, _cb, current=None):
            """
            Returns the list of mapping families supported by the
            Rendering engine.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setQuantumStrategy_async(self, _cb, bitResolution, current=None):
            """
            Sets the quantization strategy. The strategy is common to
            all channels.
            Arguments:
            _cb -- The asynchronous callback object.
            bitResolution -- The bit resolution defining associated to the strategy.
            current -- The Current object for the invocation.
            """
            pass

        def setCodomainInterval_async(self, _cb, start, end, current=None):
            """
            Sets the sub-interval of the device space i.e. a discrete
            sub-interval of \[0, 255].
            Arguments:
            _cb -- The asynchronous callback object.
            start -- The lower bound of the interval.
            end -- The upper bound of the interval.
            current -- The Current object for the invocation.
            """
            pass

        def getQuantumDef_async(self, _cb, current=None):
            """
            Returns the quantization object.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def setQuantizationMap_async(self, _cb, w, family, coefficient, noiseReduction, current=None):
            """
            Sets the quantization map, one per channel.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            family -- The mapping family.
            coefficient -- The coefficient identifying a curve in the family.
            noiseReduction -- Pass true to turn the noise reduction algorithm on, false otherwise.
            current -- The Current object for the invocation.
            """
            pass

        def getChannelFamily_async(self, _cb, w, current=None):
            """
            Returns the family associated to the specified channel.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def getChannelNoiseReduction_async(self, _cb, w, current=None):
            """
            Returns true if the noise reduction algorithm
            used to map the pixels intensity values is turned on,
            false if the algorithm is turned off. Each
            channel has an algorithm associated to it.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def getChannelStats_async(self, _cb, w, current=None):
            pass

        def getChannelCurveCoefficient_async(self, _cb, w, current=None):
            """
            Returns the coefficient identifying a map in the family.
            Each channel has a map associated to it.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def setChannelWindow_async(self, _cb, w, start, end, current=None):
            """
            Returns the pixels intensity interval. Each channel has a
            pixels intensity interval associated to it.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            start -- The lower bound of the interval.
            end -- The upper bound of the interval.
            current -- The Current object for the invocation.
            """
            pass

        def getChannelWindowStart_async(self, _cb, w, current=None):
            """
            Returns the lower bound of the pixels intensity interval.
            Each channel has a pixels intensity interval associated to
            it.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def getChannelWindowEnd_async(self, _cb, w, current=None):
            """
            Returns the upper bound of the pixels intensity interval.
            Each channel has a pixels intensity interval associated to
            it.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def setRGBA_async(self, _cb, w, red, green, blue, alpha, current=None):
            """
            Sets the four components composing the color associated to
            the specified channel.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            red -- The red component. A value between 0 and 255.
            green -- The green component. A value between 0 and 255.
            blue -- The blue component. A value between 0 and 255.
            alpha -- The alpha component. A value between 0 and 255.
            current -- The Current object for the invocation.
            """
            pass

        def getRGBA_async(self, _cb, w, current=None):
            """
            Returns a 4D-array representing the color associated to the
            specified channel. The first element corresponds to the red
            component (value between 0 and 255). The second corresponds
            to the green component (value between 0 and 255). The third
            corresponds to the blue component (value between 0 and
            255). The fourth corresponds to the alpha component (value
            between 0 and 255).
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def setActive_async(self, _cb, w, active, current=None):
            """
            Maps the specified channel if true, unmaps the
            channel otherwise.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            active -- Pass true to map the channel, false otherwise.
            current -- The Current object for the invocation.
            """
            pass

        def isActive_async(self, _cb, w, current=None):
            """
            Returns true if the channel is mapped,
            false otherwise.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def setChannelLookupTable_async(self, _cb, w, lookup, current=None):
            pass

        def getChannelLookupTable_async(self, _cb, w, current=None):
            pass

        def addCodomainMap_async(self, _cb, mapCtx, current=None):
            """
            Adds the context to the mapping chain. Only one context of
            the same type can be added to the chain. The codomain
            transformations are functions from the device space to
            device space. Each time a new context is added, the second
            LUT is rebuilt.
            Arguments:
            _cb -- The asynchronous callback object.
            mapCtx -- The context to add.
            current -- The Current object for the invocation.
            """
            pass

        def updateCodomainMap_async(self, _cb, mapCtx, current=None):
            """
            Updates the specified context. The codomain chain already
            contains the specified context. Each time a new context is
            updated, the second LUT is rebuilt.
            Arguments:
            _cb -- The asynchronous callback object.
            mapCtx -- The context to update.
            current -- The Current object for the invocation.
            """
            pass

        def removeCodomainMap_async(self, _cb, mapCtx, current=None):
            """
            Removes the specified context from the chain. Each time a
            new context is removed, the second LUT is rebuilt.
            Arguments:
            _cb -- The asynchronous callback object.
            mapCtx -- The context to remove.
            current -- The Current object for the invocation.
            """
            pass

        def addCodomainMapToChannel_async(self, _cb, mapCtx, w, current=None):
            """
            Adds the context to the mapping chain. Only one context of
            the same type can be added to the chain. The codomain
            transformations are functions from the device space to
            device space. Each time a new context is added, the second
            LUT is rebuilt.
            Arguments:
            _cb -- The asynchronous callback object.
            mapCtx -- The context to add.
            w -- The channel to add the context to.
            current -- The Current object for the invocation.
            """
            pass

        def removeCodomainMapFromChannel_async(self, _cb, mapCtx, w, current=None):
            """
            Removes the specified context from the chain. Each time a
            new context is removed, the second LUT is rebuilt.
            Arguments:
            _cb -- The asynchronous callback object.
            mapCtx -- The context to remove.
            w -- The channel to remove the context from.
            current -- The Current object for the invocation.
            """
            pass

        def updateSettings_async(self, _cb, settings, current=None):
            """
            Updates the current rendering settings based on a provided rendering
            definition and associated sub-objects.
            Arguments:
            _cb -- The asynchronous callback object.
            settings -- Rendering definition to copy from. Each sub-object will be processed as though the specific method was called with related attributes provided as arguments. The following methods are called underneath:  {@code RenderingEngine.setModel} {@code RenderingEngine.setDefaultZ} {@code RenderingEngine.setDefaultT} {@code RenderingEngine.setQuantumStrategy} {@code RenderingEngine.setCodomainInterval} {@code RenderingEngine.setActive} {@code RenderingEngine.setChannelWindow} {@code RenderingEngine.setQuantizationMap} {@code RenderingEngine.setRGBA} {@code RenderingEngine.setChannelLookupTable} If one or more attributes that apply to a particular method are null it will be skipped in its entirety. The underlying Renderer is not able to handle partial field updates. Furthermore, ode.model.display.ChannelBinding references that are null and indexes in the {@code RenderingDef.WAVERENDERING} array greater than the currently looked up {@code Pixels.SIZEC} will be skipped.
            current -- The Current object for the invocation.
            """
            pass

        def saveCurrentSettings_async(self, _cb, current=None):
            """
            Saves the current rendering settings in the database.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def saveAsNewSettings_async(self, _cb, current=None):
            """
            Saves the current rendering settings in the database
            as a new ode.model.RenderingDef and loads the
            object into the current ode.api.RenderingEngine.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def resetDefaultSettings_async(self, _cb, save, current=None):
            """
            Resets the default settings i.e. the default values
            internal to the Rendering engine. The settings will be
            saved.
            Arguments:
            _cb -- The asynchronous callback object.
            save -- Pass true to save the settings, false otherwise.
            current -- The Current object for the invocation.
            """
            pass

        def setCompressionLevel_async(self, _cb, percentage, current=None):
            """
            Sets the current compression level for the service. (The default is 85%)
            Arguments:
            _cb -- The asynchronous callback object.
            percentage -- A percentage compression level from 1.00 (100%) to 0.01 (1%).
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the percentage is out of range.
            """
            pass

        def getCompressionLevel_async(self, _cb, current=None):
            """
            Returns the current compression level for the service.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def isPixelsTypeSigned_async(self, _cb, current=None):
            """
            Returns true if the pixels type is signed,
            false otherwise.
            Arguments:
            _cb -- The asynchronous callback object.
            current -- The Current object for the invocation.
            """
            pass

        def getPixelsTypeUpperBound_async(self, _cb, w, current=None):
            """
            Returns the minimum value for that channels depending on
            the pixels type and the original range (globalmin, globalmax)
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def getPixelsTypeLowerBound_async(self, _cb, w, current=None):
            """
            Returns the maximum value for that channels depending on
            the pixels type and the original range (globalmin, globalmax)
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def getCodomainMapContext_async(self, _cb, w, current=None):
            """
            Returns the list of codomain contexts for the specified
            channel.
            Arguments:
            _cb -- The asynchronous callback object.
            w -- The channel index.
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_RenderingEngine)

        __repr__ = __str__

    _M_ode.api.RenderingEnginePrx = Ice.createTempClass()
    class RenderingEnginePrx(_M_ode.api.PyramidServicePrx):

        """
        Renders the data selected by def according to
        the current rendering settings.
        The passed argument selects a plane orthogonal to one
        of the X, Y, or Z axes. How many
        wavelengths are rendered and what color model is used
        depends on the current rendering settings.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
        _ctx -- The request context for the invocation.
        Returns: An RGB image ready to be displayed on screen.
        Throws:
        ValidationException -- If def is null.
        """
        def render(self, _def, _ctx=None):
            return _M_ode.api.RenderingEngine._op_render.invoke(self, ((_def, ), _ctx))

        """
        Renders the data selected by def according to
        the current rendering settings.
        The passed argument selects a plane orthogonal to one
        of the X, Y, or Z axes. How many
        wavelengths are rendered and what color model is used
        depends on the current rendering settings.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_render(self, _def, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_render.begin(self, ((_def, ), _response, _ex, _sent, _ctx))

        """
        Renders the data selected by def according to
        the current rendering settings.
        The passed argument selects a plane orthogonal to one
        of the X, Y, or Z axes. How many
        wavelengths are rendered and what color model is used
        depends on the current rendering settings.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
        Returns: An RGB image ready to be displayed on screen.
        Throws:
        ValidationException -- If def is null.
        """
        def end_render(self, _r):
            return _M_ode.api.RenderingEngine._op_render.end(self, _r)

        """
        Renders the data selected by def according to
        the current rendering settings.
        The passed argument selects a plane orthogonal to one
        of the X, Y, or Z axes. How many
        wavelengths are rendered and what color model is used
        depends on the current rendering settings.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
        _ctx -- The request context for the invocation.
        Returns: An RGB image ready to be displayed on screen.
        Throws:
        ValidationException -- If def is null.
        """
        def renderAsPackedInt(self, _def, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderAsPackedInt.invoke(self, ((_def, ), _ctx))

        """
        Renders the data selected by def according to
        the current rendering settings.
        The passed argument selects a plane orthogonal to one
        of the X, Y, or Z axes. How many
        wavelengths are rendered and what color model is used
        depends on the current rendering settings.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_renderAsPackedInt(self, _def, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderAsPackedInt.begin(self, ((_def, ), _response, _ex, _sent, _ctx))

        """
        Renders the data selected by def according to
        the current rendering settings.
        The passed argument selects a plane orthogonal to one
        of the X, Y, or Z axes. How many
        wavelengths are rendered and what color model is used
        depends on the current rendering settings.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y, or Z axes.
        Returns: An RGB image ready to be displayed on screen.
        Throws:
        ValidationException -- If def is null.
        """
        def end_renderAsPackedInt(self, _r):
            return _M_ode.api.RenderingEngine._op_renderAsPackedInt.end(self, _r)

        """
        Performs a projection through selected optical sections of
        a particular timepoint with the currently active channels
        and renders the data for display.
        Arguments:
        algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
        timepoint -- 
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        _ctx -- The request context for the invocation.
        Returns: A packed-integer RGBA rendered image of the projected pixels.
        Throws:
        ValidationException -- Where: algorithm is unknown timepoint is out of range start is out of range end is out of range start is greater than end
        """
        def renderProjectedAsPackedInt(self, algorithm, timepoint, stepping, start, end, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderProjectedAsPackedInt.invoke(self, ((algorithm, timepoint, stepping, start, end), _ctx))

        """
        Performs a projection through selected optical sections of
        a particular timepoint with the currently active channels
        and renders the data for display.
        Arguments:
        algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
        timepoint -- 
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_renderProjectedAsPackedInt(self, algorithm, timepoint, stepping, start, end, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderProjectedAsPackedInt.begin(self, ((algorithm, timepoint, stepping, start, end), _response, _ex, _sent, _ctx))

        """
        Performs a projection through selected optical sections of
        a particular timepoint with the currently active channels
        and renders the data for display.
        Arguments:
        algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
        timepoint -- 
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        Returns: A packed-integer RGBA rendered image of the projected pixels.
        Throws:
        ValidationException -- Where: algorithm is unknown timepoint is out of range start is out of range end is out of range start is greater than end
        """
        def end_renderProjectedAsPackedInt(self, _r):
            return _M_ode.api.RenderingEngine._op_renderProjectedAsPackedInt.end(self, _r)

        """
        Renders the data selected by def according to
        the current rendering settings and compresses the resulting
        RGBA composite image.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y or Z axes.
        _ctx -- The request context for the invocation.
        Returns: A compressed RGBA JPEG for display.
        Throws:
        ValidationException -- If def is null.
        """
        def renderCompressed(self, _def, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderCompressed.invoke(self, ((_def, ), _ctx))

        """
        Renders the data selected by def according to
        the current rendering settings and compresses the resulting
        RGBA composite image.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y or Z axes.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_renderCompressed(self, _def, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderCompressed.begin(self, ((_def, ), _response, _ex, _sent, _ctx))

        """
        Renders the data selected by def according to
        the current rendering settings and compresses the resulting
        RGBA composite image.
        Arguments:
        _def -- Selects a plane orthogonal to one of the X, Y or Z axes.
        Returns: A compressed RGBA JPEG for display.
        Throws:
        ValidationException -- If def is null.
        """
        def end_renderCompressed(self, _r):
            return _M_ode.api.RenderingEngine._op_renderCompressed.end(self, _r)

        """
        Performs a projection through selected optical sections of
        a particular timepoint with the currently active channels,
        renders the data for display and compresses the resulting
        RGBA composite image.
        Arguments:
        algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
        timepoint -- 
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        _ctx -- The request context for the invocation.
        Returns: A compressed RGBA rendered JPEG image of the projected pixels.
        Throws:
        ValidationException -- Where: algorithm is unknown timepoint is out of range start is out of range end is out of range startis greater than end
        """
        def renderProjectedCompressed(self, algorithm, timepoint, stepping, start, end, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderProjectedCompressed.invoke(self, ((algorithm, timepoint, stepping, start, end), _ctx))

        """
        Performs a projection through selected optical sections of
        a particular timepoint with the currently active channels,
        renders the data for display and compresses the resulting
        RGBA composite image.
        Arguments:
        algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
        timepoint -- 
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_renderProjectedCompressed(self, algorithm, timepoint, stepping, start, end, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_renderProjectedCompressed.begin(self, ((algorithm, timepoint, stepping, start, end), _response, _ex, _sent, _ctx))

        """
        Performs a projection through selected optical sections of
        a particular timepoint with the currently active channels,
        renders the data for display and compresses the resulting
        RGBA composite image.
        Arguments:
        algorithm -- ode.api.IProjection#MAXIMUM_INTENSITY, ode.api.IProjection#MEAN_INTENSITY or ode.api.IProjection#SUM_INTENSITY.
        timepoint -- 
        stepping -- Stepping value to use while calculating the projection. For example, stepping=1 will use every optical section from start to end where stepping=2 will use every other section from start to end to perform the projection.
        start -- Optical section to start projecting from.
        end -- Optical section to finish projecting.
        Returns: A compressed RGBA rendered JPEG image of the projected pixels.
        Throws:
        ValidationException -- Where: algorithm is unknown timepoint is out of range start is out of range end is out of range startis greater than end
        """
        def end_renderProjectedCompressed(self, _r):
            return _M_ode.api.RenderingEngine._op_renderProjectedCompressed.end(self, _r)

        """
        Returns the id of the ode.model.RenderingDef
        loaded by either {@code lookupRenderingDef} or
        {@code loadRenderingDef}.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getRenderingDefId(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getRenderingDefId.invoke(self, ((), _ctx))

        """
        Returns the id of the ode.model.RenderingDef
        loaded by either {@code lookupRenderingDef} or
        {@code loadRenderingDef}.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRenderingDefId(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getRenderingDefId.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the id of the ode.model.RenderingDef
        loaded by either {@code lookupRenderingDef} or
        {@code loadRenderingDef}.
        Arguments:
        """
        def end_getRenderingDefId(self, _r):
            return _M_ode.api.RenderingEngine._op_getRenderingDefId.end(self, _r)

        """
        Loads the Pixels set this Rendering Engine is for.
        Arguments:
        pixelsId -- The pixels set ID.
        _ctx -- The request context for the invocation.
        """
        def lookupPixels(self, pixelsId, _ctx=None):
            return _M_ode.api.RenderingEngine._op_lookupPixels.invoke(self, ((pixelsId, ), _ctx))

        """
        Loads the Pixels set this Rendering Engine is for.
        Arguments:
        pixelsId -- The pixels set ID.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupPixels(self, pixelsId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_lookupPixels.begin(self, ((pixelsId, ), _response, _ex, _sent, _ctx))

        """
        Loads the Pixels set this Rendering Engine is for.
        Arguments:
        pixelsId -- The pixels set ID.
        """
        def end_lookupPixels(self, _r):
            return _M_ode.api.RenderingEngine._op_lookupPixels.end(self, _r)

        """
        Loads the rendering settings associated to the specified
        pixels set.
        Arguments:
        pixelsId -- The pixels set ID.
        _ctx -- The request context for the invocation.
        Returns: true if a RenderingDef exists for the Pixels set, otherwise false.
        """
        def lookupRenderingDef(self, pixelsId, _ctx=None):
            return _M_ode.api.RenderingEngine._op_lookupRenderingDef.invoke(self, ((pixelsId, ), _ctx))

        """
        Loads the rendering settings associated to the specified
        pixels set.
        Arguments:
        pixelsId -- The pixels set ID.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_lookupRenderingDef(self, pixelsId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_lookupRenderingDef.begin(self, ((pixelsId, ), _response, _ex, _sent, _ctx))

        """
        Loads the rendering settings associated to the specified
        pixels set.
        Arguments:
        pixelsId -- The pixels set ID.
        Returns: true if a RenderingDef exists for the Pixels set, otherwise false.
        """
        def end_lookupRenderingDef(self, _r):
            return _M_ode.api.RenderingEngine._op_lookupRenderingDef.end(self, _r)

        """
        Loads a specific set of rendering settings that does not
        necessarily have to be linked to the given Pixels set.
        However, the rendering settings must be linked to a
        compatible Pixels set as defined by
        {@code ode.api.IRenderingSettings.sanityCheckPixels}.
        Arguments:
        renderingDefId -- The rendering definition ID.
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- If a RenderingDef does not exist with the ID renderingDefId or if the RenderingDef is incompatible due to differing pixels sets.
        """
        def loadRenderingDef(self, renderingDefId, _ctx=None):
            return _M_ode.api.RenderingEngine._op_loadRenderingDef.invoke(self, ((renderingDefId, ), _ctx))

        """
        Loads a specific set of rendering settings that does not
        necessarily have to be linked to the given Pixels set.
        However, the rendering settings must be linked to a
        compatible Pixels set as defined by
        {@code ode.api.IRenderingSettings.sanityCheckPixels}.
        Arguments:
        renderingDefId -- The rendering definition ID.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_loadRenderingDef(self, renderingDefId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_loadRenderingDef.begin(self, ((renderingDefId, ), _response, _ex, _sent, _ctx))

        """
        Loads a specific set of rendering settings that does not
        necessarily have to be linked to the given Pixels set.
        However, the rendering settings must be linked to a
        compatible Pixels set as defined by
        {@code ode.api.IRenderingSettings.sanityCheckPixels}.
        Arguments:
        renderingDefId -- The rendering definition ID.
        Throws:
        ValidationException -- If a RenderingDef does not exist with the ID renderingDefId or if the RenderingDef is incompatible due to differing pixels sets.
        """
        def end_loadRenderingDef(self, _r):
            return _M_ode.api.RenderingEngine._op_loadRenderingDef.end(self, _r)

        """
        Informs the rendering engine that it should render a set of
        overlays on each rendered frame. These are expected to be
        binary masks.
        Arguments:
        tablesId -- 
        imageId -- 
        rowColorMap -- Binary mask to color map.
        _ctx -- The request context for the invocation.
        """
        def setOverlays(self, tablesId, imageId, rowColorMap, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setOverlays.invoke(self, ((tablesId, imageId, rowColorMap), _ctx))

        """
        Informs the rendering engine that it should render a set of
        overlays on each rendered frame. These are expected to be
        binary masks.
        Arguments:
        tablesId -- 
        imageId -- 
        rowColorMap -- Binary mask to color map.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setOverlays(self, tablesId, imageId, rowColorMap, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setOverlays.begin(self, ((tablesId, imageId, rowColorMap), _response, _ex, _sent, _ctx))

        """
        Informs the rendering engine that it should render a set of
        overlays on each rendered frame. These are expected to be
        binary masks.
        Arguments:
        tablesId -- 
        imageId -- 
        rowColorMap -- Binary mask to color map.
        """
        def end_setOverlays(self, _r):
            return _M_ode.api.RenderingEngine._op_setOverlays.end(self, _r)

        """
        Creates an instance of the rendering engine.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def load(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_load.invoke(self, ((), _ctx))

        """
        Creates an instance of the rendering engine.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_load(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_load.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Creates an instance of the rendering engine.
        Arguments:
        """
        def end_load(self, _r):
            return _M_ode.api.RenderingEngine._op_load.end(self, _r)

        """
        Specifies the model that dictates how transformed raw data
        has to be mapped onto a color space.
        Arguments:
        model -- Identifies the color space model.
        _ctx -- The request context for the invocation.
        """
        def setModel(self, model, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setModel.invoke(self, ((model, ), _ctx))

        """
        Specifies the model that dictates how transformed raw data
        has to be mapped onto a color space.
        Arguments:
        model -- Identifies the color space model.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setModel(self, model, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setModel.begin(self, ((model, ), _response, _ex, _sent, _ctx))

        """
        Specifies the model that dictates how transformed raw data
        has to be mapped onto a color space.
        Arguments:
        model -- Identifies the color space model.
        """
        def end_setModel(self, _r):
            return _M_ode.api.RenderingEngine._op_setModel.end(self, _r)

        """
        Returns the model that dictates how transformed raw data
        has to be mapped onto a color space.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getModel(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getModel.invoke(self, ((), _ctx))

        """
        Returns the model that dictates how transformed raw data
        has to be mapped onto a color space.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getModel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getModel.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the model that dictates how transformed raw data
        has to be mapped onto a color space.
        Arguments:
        """
        def end_getModel(self, _r):
            return _M_ode.api.RenderingEngine._op_getModel.end(self, _r)

        """
        Returns the index of the default focal section.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getDefaultZ(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getDefaultZ.invoke(self, ((), _ctx))

        """
        Returns the index of the default focal section.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getDefaultZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getDefaultZ.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the index of the default focal section.
        Arguments:
        """
        def end_getDefaultZ(self, _r):
            return _M_ode.api.RenderingEngine._op_getDefaultZ.end(self, _r)

        """
        Returns the default timepoint index.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getDefaultT(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getDefaultT.invoke(self, ((), _ctx))

        """
        Returns the default timepoint index.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getDefaultT(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getDefaultT.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the default timepoint index.
        Arguments:
        """
        def end_getDefaultT(self, _r):
            return _M_ode.api.RenderingEngine._op_getDefaultT.end(self, _r)

        """
        Sets the index of the default focal section. This index is
        used to define a default plane.
        Arguments:
        z -- The value to set.
        _ctx -- The request context for the invocation.
        """
        def setDefaultZ(self, z, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setDefaultZ.invoke(self, ((z, ), _ctx))

        """
        Sets the index of the default focal section. This index is
        used to define a default plane.
        Arguments:
        z -- The value to set.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setDefaultZ(self, z, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setDefaultZ.begin(self, ((z, ), _response, _ex, _sent, _ctx))

        """
        Sets the index of the default focal section. This index is
        used to define a default plane.
        Arguments:
        z -- The value to set.
        """
        def end_setDefaultZ(self, _r):
            return _M_ode.api.RenderingEngine._op_setDefaultZ.end(self, _r)

        """
        Sets the default timepoint index. This index is used to
        define a default plane.
        Arguments:
        t -- The value to set.
        _ctx -- The request context for the invocation.
        """
        def setDefaultT(self, t, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setDefaultT.invoke(self, ((t, ), _ctx))

        """
        Sets the default timepoint index. This index is used to
        define a default plane.
        Arguments:
        t -- The value to set.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setDefaultT(self, t, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setDefaultT.begin(self, ((t, ), _response, _ex, _sent, _ctx))

        """
        Sets the default timepoint index. This index is used to
        define a default plane.
        Arguments:
        t -- The value to set.
        """
        def end_setDefaultT(self, _r):
            return _M_ode.api.RenderingEngine._op_setDefaultT.end(self, _r)

        """
        Returns the ode.model.Pixels set the Rendering
        engine is for.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getPixels(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getPixels.invoke(self, ((), _ctx))

        """
        Returns the ode.model.Pixels set the Rendering
        engine is for.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the ode.model.Pixels set the Rendering
        engine is for.
        Arguments:
        """
        def end_getPixels(self, _r):
            return _M_ode.api.RenderingEngine._op_getPixels.end(self, _r)

        """
        Returns the list of color models supported by the Rendering
        engine.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getAvailableModels(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getAvailableModels.invoke(self, ((), _ctx))

        """
        Returns the list of color models supported by the Rendering
        engine.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAvailableModels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getAvailableModels.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the list of color models supported by the Rendering
        engine.
        Arguments:
        """
        def end_getAvailableModels(self, _r):
            return _M_ode.api.RenderingEngine._op_getAvailableModels.end(self, _r)

        """
        Returns the list of mapping families supported by the
        Rendering engine.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getAvailableFamilies(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getAvailableFamilies.invoke(self, ((), _ctx))

        """
        Returns the list of mapping families supported by the
        Rendering engine.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getAvailableFamilies(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getAvailableFamilies.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the list of mapping families supported by the
        Rendering engine.
        Arguments:
        """
        def end_getAvailableFamilies(self, _r):
            return _M_ode.api.RenderingEngine._op_getAvailableFamilies.end(self, _r)

        """
        Sets the quantization strategy. The strategy is common to
        all channels.
        Arguments:
        bitResolution -- The bit resolution defining associated to the strategy.
        _ctx -- The request context for the invocation.
        """
        def setQuantumStrategy(self, bitResolution, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setQuantumStrategy.invoke(self, ((bitResolution, ), _ctx))

        """
        Sets the quantization strategy. The strategy is common to
        all channels.
        Arguments:
        bitResolution -- The bit resolution defining associated to the strategy.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setQuantumStrategy(self, bitResolution, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setQuantumStrategy.begin(self, ((bitResolution, ), _response, _ex, _sent, _ctx))

        """
        Sets the quantization strategy. The strategy is common to
        all channels.
        Arguments:
        bitResolution -- The bit resolution defining associated to the strategy.
        """
        def end_setQuantumStrategy(self, _r):
            return _M_ode.api.RenderingEngine._op_setQuantumStrategy.end(self, _r)

        """
        Sets the sub-interval of the device space i.e. a discrete
        sub-interval of \[0, 255].
        Arguments:
        start -- The lower bound of the interval.
        end -- The upper bound of the interval.
        _ctx -- The request context for the invocation.
        """
        def setCodomainInterval(self, start, end, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setCodomainInterval.invoke(self, ((start, end), _ctx))

        """
        Sets the sub-interval of the device space i.e. a discrete
        sub-interval of \[0, 255].
        Arguments:
        start -- The lower bound of the interval.
        end -- The upper bound of the interval.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setCodomainInterval(self, start, end, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setCodomainInterval.begin(self, ((start, end), _response, _ex, _sent, _ctx))

        """
        Sets the sub-interval of the device space i.e. a discrete
        sub-interval of \[0, 255].
        Arguments:
        start -- The lower bound of the interval.
        end -- The upper bound of the interval.
        """
        def end_setCodomainInterval(self, _r):
            return _M_ode.api.RenderingEngine._op_setCodomainInterval.end(self, _r)

        """
        Returns the quantization object.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getQuantumDef(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getQuantumDef.invoke(self, ((), _ctx))

        """
        Returns the quantization object.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getQuantumDef(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getQuantumDef.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the quantization object.
        Arguments:
        """
        def end_getQuantumDef(self, _r):
            return _M_ode.api.RenderingEngine._op_getQuantumDef.end(self, _r)

        """
        Sets the quantization map, one per channel.
        Arguments:
        w -- The channel index.
        family -- The mapping family.
        coefficient -- The coefficient identifying a curve in the family.
        noiseReduction -- Pass true to turn the noise reduction algorithm on, false otherwise.
        _ctx -- The request context for the invocation.
        """
        def setQuantizationMap(self, w, family, coefficient, noiseReduction, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setQuantizationMap.invoke(self, ((w, family, coefficient, noiseReduction), _ctx))

        """
        Sets the quantization map, one per channel.
        Arguments:
        w -- The channel index.
        family -- The mapping family.
        coefficient -- The coefficient identifying a curve in the family.
        noiseReduction -- Pass true to turn the noise reduction algorithm on, false otherwise.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setQuantizationMap(self, w, family, coefficient, noiseReduction, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setQuantizationMap.begin(self, ((w, family, coefficient, noiseReduction), _response, _ex, _sent, _ctx))

        """
        Sets the quantization map, one per channel.
        Arguments:
        w -- The channel index.
        family -- The mapping family.
        coefficient -- The coefficient identifying a curve in the family.
        noiseReduction -- Pass true to turn the noise reduction algorithm on, false otherwise.
        """
        def end_setQuantizationMap(self, _r):
            return _M_ode.api.RenderingEngine._op_setQuantizationMap.end(self, _r)

        """
        Returns the family associated to the specified channel.
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getChannelFamily(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelFamily.invoke(self, ((w, ), _ctx))

        """
        Returns the family associated to the specified channel.
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getChannelFamily(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelFamily.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns the family associated to the specified channel.
        Arguments:
        w -- The channel index.
        Returns: See above.
        """
        def end_getChannelFamily(self, _r):
            return _M_ode.api.RenderingEngine._op_getChannelFamily.end(self, _r)

        """
        Returns true if the noise reduction algorithm
        used to map the pixels intensity values is turned on,
        false if the algorithm is turned off. Each
        channel has an algorithm associated to it.
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getChannelNoiseReduction(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelNoiseReduction.invoke(self, ((w, ), _ctx))

        """
        Returns true if the noise reduction algorithm
        used to map the pixels intensity values is turned on,
        false if the algorithm is turned off. Each
        channel has an algorithm associated to it.
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getChannelNoiseReduction(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelNoiseReduction.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns true if the noise reduction algorithm
        used to map the pixels intensity values is turned on,
        false if the algorithm is turned off. Each
        channel has an algorithm associated to it.
        Arguments:
        w -- The channel index.
        Returns: See above.
        """
        def end_getChannelNoiseReduction(self, _r):
            return _M_ode.api.RenderingEngine._op_getChannelNoiseReduction.end(self, _r)

        def getChannelStats(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelStats.invoke(self, ((w, ), _ctx))

        def begin_getChannelStats(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelStats.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        def end_getChannelStats(self, _r):
            return _M_ode.api.RenderingEngine._op_getChannelStats.end(self, _r)

        """
        Returns the coefficient identifying a map in the family.
        Each channel has a map associated to it.
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def getChannelCurveCoefficient(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelCurveCoefficient.invoke(self, ((w, ), _ctx))

        """
        Returns the coefficient identifying a map in the family.
        Each channel has a map associated to it.
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getChannelCurveCoefficient(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelCurveCoefficient.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns the coefficient identifying a map in the family.
        Each channel has a map associated to it.
        Arguments:
        w -- The channel index.
        Returns: See above.
        """
        def end_getChannelCurveCoefficient(self, _r):
            return _M_ode.api.RenderingEngine._op_getChannelCurveCoefficient.end(self, _r)

        """
        Returns the pixels intensity interval. Each channel has a
        pixels intensity interval associated to it.
        Arguments:
        w -- The channel index.
        start -- The lower bound of the interval.
        end -- The upper bound of the interval.
        _ctx -- The request context for the invocation.
        """
        def setChannelWindow(self, w, start, end, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setChannelWindow.invoke(self, ((w, start, end), _ctx))

        """
        Returns the pixels intensity interval. Each channel has a
        pixels intensity interval associated to it.
        Arguments:
        w -- The channel index.
        start -- The lower bound of the interval.
        end -- The upper bound of the interval.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setChannelWindow(self, w, start, end, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setChannelWindow.begin(self, ((w, start, end), _response, _ex, _sent, _ctx))

        """
        Returns the pixels intensity interval. Each channel has a
        pixels intensity interval associated to it.
        Arguments:
        w -- The channel index.
        start -- The lower bound of the interval.
        end -- The upper bound of the interval.
        """
        def end_setChannelWindow(self, _r):
            return _M_ode.api.RenderingEngine._op_setChannelWindow.end(self, _r)

        """
        Returns the lower bound of the pixels intensity interval.
        Each channel has a pixels intensity interval associated to
        it.
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        """
        def getChannelWindowStart(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelWindowStart.invoke(self, ((w, ), _ctx))

        """
        Returns the lower bound of the pixels intensity interval.
        Each channel has a pixels intensity interval associated to
        it.
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getChannelWindowStart(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelWindowStart.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns the lower bound of the pixels intensity interval.
        Each channel has a pixels intensity interval associated to
        it.
        Arguments:
        w -- The channel index.
        """
        def end_getChannelWindowStart(self, _r):
            return _M_ode.api.RenderingEngine._op_getChannelWindowStart.end(self, _r)

        """
        Returns the upper bound of the pixels intensity interval.
        Each channel has a pixels intensity interval associated to
        it.
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        """
        def getChannelWindowEnd(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelWindowEnd.invoke(self, ((w, ), _ctx))

        """
        Returns the upper bound of the pixels intensity interval.
        Each channel has a pixels intensity interval associated to
        it.
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getChannelWindowEnd(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelWindowEnd.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns the upper bound of the pixels intensity interval.
        Each channel has a pixels intensity interval associated to
        it.
        Arguments:
        w -- The channel index.
        """
        def end_getChannelWindowEnd(self, _r):
            return _M_ode.api.RenderingEngine._op_getChannelWindowEnd.end(self, _r)

        """
        Sets the four components composing the color associated to
        the specified channel.
        Arguments:
        w -- The channel index.
        red -- The red component. A value between 0 and 255.
        green -- The green component. A value between 0 and 255.
        blue -- The blue component. A value between 0 and 255.
        alpha -- The alpha component. A value between 0 and 255.
        _ctx -- The request context for the invocation.
        """
        def setRGBA(self, w, red, green, blue, alpha, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setRGBA.invoke(self, ((w, red, green, blue, alpha), _ctx))

        """
        Sets the four components composing the color associated to
        the specified channel.
        Arguments:
        w -- The channel index.
        red -- The red component. A value between 0 and 255.
        green -- The green component. A value between 0 and 255.
        blue -- The blue component. A value between 0 and 255.
        alpha -- The alpha component. A value between 0 and 255.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setRGBA(self, w, red, green, blue, alpha, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setRGBA.begin(self, ((w, red, green, blue, alpha), _response, _ex, _sent, _ctx))

        """
        Sets the four components composing the color associated to
        the specified channel.
        Arguments:
        w -- The channel index.
        red -- The red component. A value between 0 and 255.
        green -- The green component. A value between 0 and 255.
        blue -- The blue component. A value between 0 and 255.
        alpha -- The alpha component. A value between 0 and 255.
        """
        def end_setRGBA(self, _r):
            return _M_ode.api.RenderingEngine._op_setRGBA.end(self, _r)

        """
        Returns a 4D-array representing the color associated to the
        specified channel. The first element corresponds to the red
        component (value between 0 and 255). The second corresponds
        to the green component (value between 0 and 255). The third
        corresponds to the blue component (value between 0 and
        255). The fourth corresponds to the alpha component (value
        between 0 and 255).
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        """
        def getRGBA(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getRGBA.invoke(self, ((w, ), _ctx))

        """
        Returns a 4D-array representing the color associated to the
        specified channel. The first element corresponds to the red
        component (value between 0 and 255). The second corresponds
        to the green component (value between 0 and 255). The third
        corresponds to the blue component (value between 0 and
        255). The fourth corresponds to the alpha component (value
        between 0 and 255).
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRGBA(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getRGBA.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns a 4D-array representing the color associated to the
        specified channel. The first element corresponds to the red
        component (value between 0 and 255). The second corresponds
        to the green component (value between 0 and 255). The third
        corresponds to the blue component (value between 0 and
        255). The fourth corresponds to the alpha component (value
        between 0 and 255).
        Arguments:
        w -- The channel index.
        """
        def end_getRGBA(self, _r):
            return _M_ode.api.RenderingEngine._op_getRGBA.end(self, _r)

        """
        Maps the specified channel if true, unmaps the
        channel otherwise.
        Arguments:
        w -- The channel index.
        active -- Pass true to map the channel, false otherwise.
        _ctx -- The request context for the invocation.
        """
        def setActive(self, w, active, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setActive.invoke(self, ((w, active), _ctx))

        """
        Maps the specified channel if true, unmaps the
        channel otherwise.
        Arguments:
        w -- The channel index.
        active -- Pass true to map the channel, false otherwise.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setActive(self, w, active, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setActive.begin(self, ((w, active), _response, _ex, _sent, _ctx))

        """
        Maps the specified channel if true, unmaps the
        channel otherwise.
        Arguments:
        w -- The channel index.
        active -- Pass true to map the channel, false otherwise.
        """
        def end_setActive(self, _r):
            return _M_ode.api.RenderingEngine._op_setActive.end(self, _r)

        """
        Returns true if the channel is mapped,
        false otherwise.
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        """
        def isActive(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_isActive.invoke(self, ((w, ), _ctx))

        """
        Returns true if the channel is mapped,
        false otherwise.
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isActive(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_isActive.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns true if the channel is mapped,
        false otherwise.
        Arguments:
        w -- The channel index.
        """
        def end_isActive(self, _r):
            return _M_ode.api.RenderingEngine._op_isActive.end(self, _r)

        def setChannelLookupTable(self, w, lookup, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setChannelLookupTable.invoke(self, ((w, lookup), _ctx))

        def begin_setChannelLookupTable(self, w, lookup, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setChannelLookupTable.begin(self, ((w, lookup), _response, _ex, _sent, _ctx))

        def end_setChannelLookupTable(self, _r):
            return _M_ode.api.RenderingEngine._op_setChannelLookupTable.end(self, _r)

        def getChannelLookupTable(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelLookupTable.invoke(self, ((w, ), _ctx))

        def begin_getChannelLookupTable(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getChannelLookupTable.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        def end_getChannelLookupTable(self, _r):
            return _M_ode.api.RenderingEngine._op_getChannelLookupTable.end(self, _r)

        """
        Adds the context to the mapping chain. Only one context of
        the same type can be added to the chain. The codomain
        transformations are functions from the device space to
        device space. Each time a new context is added, the second
        LUT is rebuilt.
        Arguments:
        mapCtx -- The context to add.
        _ctx -- The request context for the invocation.
        """
        def addCodomainMap(self, mapCtx, _ctx=None):
            return _M_ode.api.RenderingEngine._op_addCodomainMap.invoke(self, ((mapCtx, ), _ctx))

        """
        Adds the context to the mapping chain. Only one context of
        the same type can be added to the chain. The codomain
        transformations are functions from the device space to
        device space. Each time a new context is added, the second
        LUT is rebuilt.
        Arguments:
        mapCtx -- The context to add.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addCodomainMap(self, mapCtx, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_addCodomainMap.begin(self, ((mapCtx, ), _response, _ex, _sent, _ctx))

        """
        Adds the context to the mapping chain. Only one context of
        the same type can be added to the chain. The codomain
        transformations are functions from the device space to
        device space. Each time a new context is added, the second
        LUT is rebuilt.
        Arguments:
        mapCtx -- The context to add.
        """
        def end_addCodomainMap(self, _r):
            return _M_ode.api.RenderingEngine._op_addCodomainMap.end(self, _r)

        """
        Updates the specified context. The codomain chain already
        contains the specified context. Each time a new context is
        updated, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to update.
        _ctx -- The request context for the invocation.
        """
        def updateCodomainMap(self, mapCtx, _ctx=None):
            return _M_ode.api.RenderingEngine._op_updateCodomainMap.invoke(self, ((mapCtx, ), _ctx))

        """
        Updates the specified context. The codomain chain already
        contains the specified context. Each time a new context is
        updated, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to update.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateCodomainMap(self, mapCtx, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_updateCodomainMap.begin(self, ((mapCtx, ), _response, _ex, _sent, _ctx))

        """
        Updates the specified context. The codomain chain already
        contains the specified context. Each time a new context is
        updated, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to update.
        """
        def end_updateCodomainMap(self, _r):
            return _M_ode.api.RenderingEngine._op_updateCodomainMap.end(self, _r)

        """
        Removes the specified context from the chain. Each time a
        new context is removed, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to remove.
        _ctx -- The request context for the invocation.
        """
        def removeCodomainMap(self, mapCtx, _ctx=None):
            return _M_ode.api.RenderingEngine._op_removeCodomainMap.invoke(self, ((mapCtx, ), _ctx))

        """
        Removes the specified context from the chain. Each time a
        new context is removed, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to remove.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeCodomainMap(self, mapCtx, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_removeCodomainMap.begin(self, ((mapCtx, ), _response, _ex, _sent, _ctx))

        """
        Removes the specified context from the chain. Each time a
        new context is removed, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to remove.
        """
        def end_removeCodomainMap(self, _r):
            return _M_ode.api.RenderingEngine._op_removeCodomainMap.end(self, _r)

        """
        Adds the context to the mapping chain. Only one context of
        the same type can be added to the chain. The codomain
        transformations are functions from the device space to
        device space. Each time a new context is added, the second
        LUT is rebuilt.
        Arguments:
        mapCtx -- The context to add.
        w -- The channel to add the context to.
        _ctx -- The request context for the invocation.
        """
        def addCodomainMapToChannel(self, mapCtx, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_addCodomainMapToChannel.invoke(self, ((mapCtx, w), _ctx))

        """
        Adds the context to the mapping chain. Only one context of
        the same type can be added to the chain. The codomain
        transformations are functions from the device space to
        device space. Each time a new context is added, the second
        LUT is rebuilt.
        Arguments:
        mapCtx -- The context to add.
        w -- The channel to add the context to.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_addCodomainMapToChannel(self, mapCtx, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_addCodomainMapToChannel.begin(self, ((mapCtx, w), _response, _ex, _sent, _ctx))

        """
        Adds the context to the mapping chain. Only one context of
        the same type can be added to the chain. The codomain
        transformations are functions from the device space to
        device space. Each time a new context is added, the second
        LUT is rebuilt.
        Arguments:
        mapCtx -- The context to add.
        w -- The channel to add the context to.
        """
        def end_addCodomainMapToChannel(self, _r):
            return _M_ode.api.RenderingEngine._op_addCodomainMapToChannel.end(self, _r)

        """
        Removes the specified context from the chain. Each time a
        new context is removed, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to remove.
        w -- The channel to remove the context from.
        _ctx -- The request context for the invocation.
        """
        def removeCodomainMapFromChannel(self, mapCtx, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_removeCodomainMapFromChannel.invoke(self, ((mapCtx, w), _ctx))

        """
        Removes the specified context from the chain. Each time a
        new context is removed, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to remove.
        w -- The channel to remove the context from.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_removeCodomainMapFromChannel(self, mapCtx, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_removeCodomainMapFromChannel.begin(self, ((mapCtx, w), _response, _ex, _sent, _ctx))

        """
        Removes the specified context from the chain. Each time a
        new context is removed, the second LUT is rebuilt.
        Arguments:
        mapCtx -- The context to remove.
        w -- The channel to remove the context from.
        """
        def end_removeCodomainMapFromChannel(self, _r):
            return _M_ode.api.RenderingEngine._op_removeCodomainMapFromChannel.end(self, _r)

        """
        Updates the current rendering settings based on a provided rendering
        definition and associated sub-objects.
        Arguments:
        settings -- Rendering definition to copy from. Each sub-object will be processed as though the specific method was called with related attributes provided as arguments. The following methods are called underneath:  {@code RenderingEngine.setModel} {@code RenderingEngine.setDefaultZ} {@code RenderingEngine.setDefaultT} {@code RenderingEngine.setQuantumStrategy} {@code RenderingEngine.setCodomainInterval} {@code RenderingEngine.setActive} {@code RenderingEngine.setChannelWindow} {@code RenderingEngine.setQuantizationMap} {@code RenderingEngine.setRGBA} {@code RenderingEngine.setChannelLookupTable} If one or more attributes that apply to a particular method are null it will be skipped in its entirety. The underlying Renderer is not able to handle partial field updates. Furthermore, ode.model.display.ChannelBinding references that are null and indexes in the {@code RenderingDef.WAVERENDERING} array greater than the currently looked up {@code Pixels.SIZEC} will be skipped.
        _ctx -- The request context for the invocation.
        """
        def updateSettings(self, settings, _ctx=None):
            return _M_ode.api.RenderingEngine._op_updateSettings.invoke(self, ((settings, ), _ctx))

        """
        Updates the current rendering settings based on a provided rendering
        definition and associated sub-objects.
        Arguments:
        settings -- Rendering definition to copy from. Each sub-object will be processed as though the specific method was called with related attributes provided as arguments. The following methods are called underneath:  {@code RenderingEngine.setModel} {@code RenderingEngine.setDefaultZ} {@code RenderingEngine.setDefaultT} {@code RenderingEngine.setQuantumStrategy} {@code RenderingEngine.setCodomainInterval} {@code RenderingEngine.setActive} {@code RenderingEngine.setChannelWindow} {@code RenderingEngine.setQuantizationMap} {@code RenderingEngine.setRGBA} {@code RenderingEngine.setChannelLookupTable} If one or more attributes that apply to a particular method are null it will be skipped in its entirety. The underlying Renderer is not able to handle partial field updates. Furthermore, ode.model.display.ChannelBinding references that are null and indexes in the {@code RenderingDef.WAVERENDERING} array greater than the currently looked up {@code Pixels.SIZEC} will be skipped.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_updateSettings(self, settings, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_updateSettings.begin(self, ((settings, ), _response, _ex, _sent, _ctx))

        """
        Updates the current rendering settings based on a provided rendering
        definition and associated sub-objects.
        Arguments:
        settings -- Rendering definition to copy from. Each sub-object will be processed as though the specific method was called with related attributes provided as arguments. The following methods are called underneath:  {@code RenderingEngine.setModel} {@code RenderingEngine.setDefaultZ} {@code RenderingEngine.setDefaultT} {@code RenderingEngine.setQuantumStrategy} {@code RenderingEngine.setCodomainInterval} {@code RenderingEngine.setActive} {@code RenderingEngine.setChannelWindow} {@code RenderingEngine.setQuantizationMap} {@code RenderingEngine.setRGBA} {@code RenderingEngine.setChannelLookupTable} If one or more attributes that apply to a particular method are null it will be skipped in its entirety. The underlying Renderer is not able to handle partial field updates. Furthermore, ode.model.display.ChannelBinding references that are null and indexes in the {@code RenderingDef.WAVERENDERING} array greater than the currently looked up {@code Pixels.SIZEC} will be skipped.
        """
        def end_updateSettings(self, _r):
            return _M_ode.api.RenderingEngine._op_updateSettings.end(self, _r)

        """
        Saves the current rendering settings in the database.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def saveCurrentSettings(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_saveCurrentSettings.invoke(self, ((), _ctx))

        """
        Saves the current rendering settings in the database.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_saveCurrentSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_saveCurrentSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Saves the current rendering settings in the database.
        Arguments:
        """
        def end_saveCurrentSettings(self, _r):
            return _M_ode.api.RenderingEngine._op_saveCurrentSettings.end(self, _r)

        """
        Saves the current rendering settings in the database
        as a new ode.model.RenderingDef and loads the
        object into the current ode.api.RenderingEngine.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def saveAsNewSettings(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_saveAsNewSettings.invoke(self, ((), _ctx))

        """
        Saves the current rendering settings in the database
        as a new ode.model.RenderingDef and loads the
        object into the current ode.api.RenderingEngine.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_saveAsNewSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_saveAsNewSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Saves the current rendering settings in the database
        as a new ode.model.RenderingDef and loads the
        object into the current ode.api.RenderingEngine.
        Arguments:
        """
        def end_saveAsNewSettings(self, _r):
            return _M_ode.api.RenderingEngine._op_saveAsNewSettings.end(self, _r)

        """
        Resets the default settings i.e. the default values
        internal to the Rendering engine. The settings will be
        saved.
        Arguments:
        save -- Pass true to save the settings, false otherwise.
        _ctx -- The request context for the invocation.
        """
        def resetDefaultSettings(self, save, _ctx=None):
            return _M_ode.api.RenderingEngine._op_resetDefaultSettings.invoke(self, ((save, ), _ctx))

        """
        Resets the default settings i.e. the default values
        internal to the Rendering engine. The settings will be
        saved.
        Arguments:
        save -- Pass true to save the settings, false otherwise.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaultSettings(self, save, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_resetDefaultSettings.begin(self, ((save, ), _response, _ex, _sent, _ctx))

        """
        Resets the default settings i.e. the default values
        internal to the Rendering engine. The settings will be
        saved.
        Arguments:
        save -- Pass true to save the settings, false otherwise.
        """
        def end_resetDefaultSettings(self, _r):
            return _M_ode.api.RenderingEngine._op_resetDefaultSettings.end(self, _r)

        """
        Sets the current compression level for the service. (The default is 85%)
        Arguments:
        percentage -- A percentage compression level from 1.00 (100%) to 0.01 (1%).
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- if the percentage is out of range.
        """
        def setCompressionLevel(self, percentage, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setCompressionLevel.invoke(self, ((percentage, ), _ctx))

        """
        Sets the current compression level for the service. (The default is 85%)
        Arguments:
        percentage -- A percentage compression level from 1.00 (100%) to 0.01 (1%).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setCompressionLevel(self, percentage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_setCompressionLevel.begin(self, ((percentage, ), _response, _ex, _sent, _ctx))

        """
        Sets the current compression level for the service. (The default is 85%)
        Arguments:
        percentage -- A percentage compression level from 1.00 (100%) to 0.01 (1%).
        Throws:
        ValidationException -- if the percentage is out of range.
        """
        def end_setCompressionLevel(self, _r):
            return _M_ode.api.RenderingEngine._op_setCompressionLevel.end(self, _r)

        """
        Returns the current compression level for the service.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getCompressionLevel(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getCompressionLevel.invoke(self, ((), _ctx))

        """
        Returns the current compression level for the service.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCompressionLevel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getCompressionLevel.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns the current compression level for the service.
        Arguments:
        """
        def end_getCompressionLevel(self, _r):
            return _M_ode.api.RenderingEngine._op_getCompressionLevel.end(self, _r)

        """
        Returns true if the pixels type is signed,
        false otherwise.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def isPixelsTypeSigned(self, _ctx=None):
            return _M_ode.api.RenderingEngine._op_isPixelsTypeSigned.invoke(self, ((), _ctx))

        """
        Returns true if the pixels type is signed,
        false otherwise.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isPixelsTypeSigned(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_isPixelsTypeSigned.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Returns true if the pixels type is signed,
        false otherwise.
        Arguments:
        """
        def end_isPixelsTypeSigned(self, _r):
            return _M_ode.api.RenderingEngine._op_isPixelsTypeSigned.end(self, _r)

        """
        Returns the minimum value for that channels depending on
        the pixels type and the original range (globalmin, globalmax)
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        """
        def getPixelsTypeUpperBound(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getPixelsTypeUpperBound.invoke(self, ((w, ), _ctx))

        """
        Returns the minimum value for that channels depending on
        the pixels type and the original range (globalmin, globalmax)
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPixelsTypeUpperBound(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getPixelsTypeUpperBound.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns the minimum value for that channels depending on
        the pixels type and the original range (globalmin, globalmax)
        Arguments:
        w -- The channel index.
        """
        def end_getPixelsTypeUpperBound(self, _r):
            return _M_ode.api.RenderingEngine._op_getPixelsTypeUpperBound.end(self, _r)

        """
        Returns the maximum value for that channels depending on
        the pixels type and the original range (globalmin, globalmax)
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        """
        def getPixelsTypeLowerBound(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getPixelsTypeLowerBound.invoke(self, ((w, ), _ctx))

        """
        Returns the maximum value for that channels depending on
        the pixels type and the original range (globalmin, globalmax)
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getPixelsTypeLowerBound(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getPixelsTypeLowerBound.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns the maximum value for that channels depending on
        the pixels type and the original range (globalmin, globalmax)
        Arguments:
        w -- The channel index.
        """
        def end_getPixelsTypeLowerBound(self, _r):
            return _M_ode.api.RenderingEngine._op_getPixelsTypeLowerBound.end(self, _r)

        """
        Returns the list of codomain contexts for the specified
        channel.
        Arguments:
        w -- The channel index.
        _ctx -- The request context for the invocation.
        """
        def getCodomainMapContext(self, w, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getCodomainMapContext.invoke(self, ((w, ), _ctx))

        """
        Returns the list of codomain contexts for the specified
        channel.
        Arguments:
        w -- The channel index.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCodomainMapContext(self, w, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.RenderingEngine._op_getCodomainMapContext.begin(self, ((w, ), _response, _ex, _sent, _ctx))

        """
        Returns the list of codomain contexts for the specified
        channel.
        Arguments:
        w -- The channel index.
        """
        def end_getCodomainMapContext(self, _r):
            return _M_ode.api.RenderingEngine._op_getCodomainMapContext.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.RenderingEnginePrx.ice_checkedCast(proxy, '::ode::api::RenderingEngine', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.RenderingEnginePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::RenderingEngine'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_RenderingEnginePrx = IcePy.defineProxy('::ode::api::RenderingEngine', RenderingEnginePrx)

    _M_ode.api._t_RenderingEngine = IcePy.defineClass('::ode::api::RenderingEngine', RenderingEngine, -1, (), True, False, None, (_M_ode.api._t_PyramidService,), ())
    RenderingEngine._ice_type = _M_ode.api._t_RenderingEngine

    RenderingEngine._op_render = IcePy.Operation('render', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.romio._t_PlaneDef, False, 0),), (), ((), _M_ode.romio._t_RGBBuffer, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_renderAsPackedInt = IcePy.Operation('renderAsPackedInt', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.romio._t_PlaneDef, False, 0),), (), ((), _M_Ice._t_IntSeq, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_renderProjectedAsPackedInt = IcePy.Operation('renderProjectedAsPackedInt', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.constants.projection._t_ProjectionType, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_IntSeq, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_renderCompressed = IcePy.Operation('renderCompressed', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.romio._t_PlaneDef, False, 0),), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_renderProjectedCompressed = IcePy.Operation('renderProjectedCompressed', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.constants.projection._t_ProjectionType, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getRenderingDefId = IcePy.Operation('getRenderingDefId', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_lookupPixels = IcePy.Operation('lookupPixels', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_lookupRenderingDef = IcePy.Operation('lookupRenderingDef', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_loadRenderingDef = IcePy.Operation('loadRenderingDef', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_setOverlays = IcePy.Operation('setOverlays', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode._t_RLong, False, 0), ((), _M_ode._t_RLong, False, 0), ((), _M_ode.api._t_LongIntMap, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_setOverlays.deprecate(" use ode::romio::PlaneDefWithMasks instead")
    RenderingEngine._op_load = IcePy.Operation('load', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_setModel = IcePy.Operation('setModel', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_RenderingModel, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getModel = IcePy.Operation('getModel', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.model._t_RenderingModel, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getDefaultZ = IcePy.Operation('getDefaultZ', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getDefaultT = IcePy.Operation('getDefaultT', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_int, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setDefaultZ = IcePy.Operation('setDefaultZ', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_setDefaultT = IcePy.Operation('setDefaultT', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getPixels = IcePy.Operation('getPixels', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.model._t_Pixels, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getAvailableModels = IcePy.Operation('getAvailableModels', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getAvailableFamilies = IcePy.Operation('getAvailableFamilies', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setQuantumStrategy = IcePy.Operation('setQuantumStrategy', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_setCodomainInterval = IcePy.Operation('setCodomainInterval', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getQuantumDef = IcePy.Operation('getQuantumDef', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), _M_ode.model._t_QuantumDef, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setQuantizationMap = IcePy.Operation('setQuantizationMap', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_Family, False, 0), ((), IcePy._t_double, False, 0), ((), IcePy._t_bool, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getChannelFamily = IcePy.Operation('getChannelFamily', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_Family, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getChannelNoiseReduction = IcePy.Operation('getChannelNoiseReduction', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getChannelStats = IcePy.Operation('getChannelStats', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_Ice._t_DoubleSeq, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getChannelCurveCoefficient = IcePy.Operation('getChannelCurveCoefficient', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_double, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setChannelWindow = IcePy.Operation('setChannelWindow', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_double, False, 0), ((), IcePy._t_double, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getChannelWindowStart = IcePy.Operation('getChannelWindowStart', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_double, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getChannelWindowEnd = IcePy.Operation('getChannelWindowEnd', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_double, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setRGBA = IcePy.Operation('setRGBA', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getRGBA = IcePy.Operation('getRGBA', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_Ice._t_IntSeq, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setActive = IcePy.Operation('setActive', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_bool, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_isActive = IcePy.Operation('isActive', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setChannelLookupTable = IcePy.Operation('setChannelLookupTable', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0), ((), IcePy._t_string, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getChannelLookupTable = IcePy.Operation('getChannelLookupTable', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_addCodomainMap = IcePy.Operation('addCodomainMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.romio._t_CodomainMapContext, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_addCodomainMap.deprecate("addCodomainMap() is deprecated. use addCodomainMapToChannel instead.")
    RenderingEngine._op_updateCodomainMap = IcePy.Operation('updateCodomainMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.romio._t_CodomainMapContext, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_updateCodomainMap.deprecate("removeCodomainMap() is deprecated.")
    RenderingEngine._op_removeCodomainMap = IcePy.Operation('removeCodomainMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.romio._t_CodomainMapContext, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_removeCodomainMap.deprecate("removeCodomainMap() is deprecated. use removeCodomainMapFromChannel instead.")
    RenderingEngine._op_addCodomainMapToChannel = IcePy.Operation('addCodomainMapToChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.romio._t_CodomainMapContext, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_removeCodomainMapFromChannel = IcePy.Operation('removeCodomainMapFromChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.romio._t_CodomainMapContext, False, 0), ((), IcePy._t_int, False, 0)), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_updateSettings = IcePy.Operation('updateSettings', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_saveCurrentSettings = IcePy.Operation('saveCurrentSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_saveAsNewSettings = IcePy.Operation('saveAsNewSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_resetDefaultSettings = IcePy.Operation('resetDefaultSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_bool, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_setCompressionLevel = IcePy.Operation('setCompressionLevel', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_float, False, 0),), (), None, (_M_ode._t_ServerError,))
    RenderingEngine._op_getCompressionLevel = IcePy.Operation('getCompressionLevel', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_float, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_isPixelsTypeSigned = IcePy.Operation('isPixelsTypeSigned', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getPixelsTypeUpperBound = IcePy.Operation('getPixelsTypeUpperBound', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_double, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getPixelsTypeLowerBound = IcePy.Operation('getPixelsTypeLowerBound', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_double, False, 0), (_M_ode._t_ServerError,))
    RenderingEngine._op_getCodomainMapContext = IcePy.Operation('getCodomainMapContext', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.api._t_IObjectList, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.RenderingEngine = RenderingEngine
    del RenderingEngine

    _M_ode.api.RenderingEnginePrx = RenderingEnginePrx
    del RenderingEnginePrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
