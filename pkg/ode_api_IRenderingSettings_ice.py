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
import ode_System_ice
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

if 'IRenderingSettings' not in _M_ode.api.__dict__:
    _M_ode.api.IRenderingSettings = Ice.createTempClass()
    class IRenderingSettings(_M_ode.api.ServiceInterface):
        """
        Provides method to apply rendering settings to a collection of
        images.
        All methods will receive the id of the pixels set to copy the
        rendering settings from.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.api.IRenderingSettings:
                raise RuntimeError('ode.api.IRenderingSettings is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::IRenderingSettings', '::ode::api::ServiceInterface')

        def ice_id(self, current=None):
            return '::ode::api::IRenderingSettings'

        def ice_staticId():
            return '::ode::api::IRenderingSettings'
        ice_staticId = staticmethod(ice_staticId)

        def sanityCheckPixels_async(self, _cb, pFrom, pTo, current=None):
            """
            Checks if the specified sets of pixels are compatible.
            Returns true if the pixels set is valid,
            false otherwise.
            Arguments:
            _cb -- The asynchronous callback object.
            pFrom -- The pixels set to copy the settings from.
            pTo -- The pixels set to copy the settings to.
            current -- The Current object for the invocation.
            """
            pass

        def getRenderingSettings_async(self, _cb, pixelsId, current=None):
            """
            Returns the default rendering settings for a given pixels
            for the current user.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The Id of the Pixels
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the image qualified by imageId is unlocatable.
            """
            pass

        def createNewRenderingDef_async(self, _cb, pixels, current=None):
            """
            Creates a new rendering definition object along with its
            sub-objects.
            Arguments:
            _cb -- The asynchronous callback object.
            pixels -- The Pixels set to link to the rendering definition.
            current -- The Current object for the invocation.
            """
            pass

        def resetDefaults_async(self, _cb, _def, pixels, current=None):
            """
            Resets the given rendering settings to those that are
            specified by the rendering engine intelligent pretty
            good image (PG) logic for the pixels set linked to that
            set of rendering settings. NOTE: This method should
            only be used to reset a rendering definition that has been
            retrieved via {@code getRenderingSettings} as it
            relies on certain objects being loaded. The rendering
            settings are saved upon completion.
            Arguments:
            _cb -- The asynchronous callback object.
            _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
            pixels -- The Pixels set for def.
            current -- The Current object for the invocation.
            """
            pass

        def resetDefaultsNoSave_async(self, _cb, _def, pixels, current=None):
            """
            Resets the given rendering settings to those that are
            specified by the rendering engine intelligent pretty
            good image (PG) logic for the pixels set linked to that
            set of rendering settings. NOTE: This method should
            only be used to reset a rendering definition that has been
            retrieved via {@code getRenderingSettings(long)} as it
            relies on certain objects being loaded. The rendering
            settings are not saved.
            Arguments:
            _cb -- The asynchronous callback object.
            _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
            pixels -- The Pixels set for def.
            current -- The Current object for the invocation.
            """
            pass

        def resetDefaultsInImage_async(self, _cb, imageId, current=None):
            """
            Resets an image's default rendering settings back to those
            that are specified by the rendering engine intelligent
            pretty good image (PG) logic.
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- The Id of the Image.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the image qualified by imageId is unlocatable.
            """
            pass

        def resetDefaultsForPixels_async(self, _cb, pixelsId, current=None):
            """
            Resets a Pixels' default rendering settings back to those
            that are specified by the rendering engine intelligent
            pretty good image (PG) logic.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The Id of the Pixels.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the Pixels qualified by pixelsId is unlocatable.
            """
            pass

        def resetDefaultsInDataset_async(self, _cb, datasetId, current=None):
            """
            Resets a dataset's rendering settings back to those that
            are specified by the rendering engine intelligent pretty
            good image (PG) logic.
            Arguments:
            _cb -- The asynchronous callback object.
            datasetId -- The Id of the Dataset.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the image qualified by datasetId is not locatable.
            """
            pass

        def resetDefaultsInSet_async(self, _cb, type, nodeIds, current=None):
            """
            Resets a rendering settings back to one or many containers
            that are specified by the rendering engine intelligent
            pretty good image (PG) logic. Supported container
            types are:
            ode.model.Project
            ode.model.Dataset
            ode.model.Image
            ode.model.Plate
            ode.model.Pixels
            Arguments:
            _cb -- The asynchronous callback object.
            type -- The type of nodes to handle.
            nodeIds -- Ids of the node type.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if an illegal type is used.
            """
            pass

        def resetDefaultsByOwnerInSet_async(self, _cb, type, nodeIds, current=None):
            """
            Resets the rendering settings of a given group of
            containers based on the owner's (essentially a copy).
            Supported container types are:
            ode.model.Project
            ode.model.Dataset
            ode.model.Image
            ode.model.Plate
            ode.model.Pixels
            Arguments:
            _cb -- The asynchronous callback object.
            type -- The type of nodes to handle.
            nodeIds -- Ids of the node type.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if an illegal type is used.
            """
            pass

        def resetMinMaxInSet_async(self, _cb, type, nodeIds, current=None):
            """
            Resets a the channel windows for one or many containers
            back to their global minimum and global maximum for the
            channel. Supported container types are:
            ode.model.Project
            ode.model.Dataset
            ode.model.Image
            ode.model.Plate
            ode.model.Pixels
            Arguments:
            _cb -- The asynchronous callback object.
            type -- The type of nodes to handle.
            nodeIds -- Ids of the node type.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if an illegal type is used.
            """
            pass

        def applySettingsToSet_async(self, _cb, _from, toType, nodeIds, current=None):
            """
            Applies rendering settings to one or many containers. If a
            container such as Dataset is to be copied to, all images
            within that Dataset will have the rendering settings
            applied. Supported container types are:
            ode.model.Project
            ode.model.Dataset
            ode.model.Image
            ode.model.Plate
            ode.model.Screen
            ode.model.Pixels
            Arguments:
            _cb -- The asynchronous callback object.
            _from -- The Id of the pixels set to copy the rendering settings from.
            toType -- The type of nodes to handle.
            nodeIds -- Ids of the node type.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if an illegal type is used.
            """
            pass

        def applySettingsToProject_async(self, _cb, _from, to, current=None):
            """
            Applies rendering settings to all images in all Datasets of
            a given Project.
            Arguments:
            _cb -- The asynchronous callback object.
            _from -- The Id of the pixels set to copy the rendering settings from.
            to -- The Id of the project container to apply settings to.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the rendering settings from is not locatable or the project to is not locatable.
            """
            pass

        def applySettingsToDataset_async(self, _cb, _from, to, current=None):
            """
            Applies rendering settings to all images in a given Dataset.
            Arguments:
            _cb -- The asynchronous callback object.
            _from -- The Id of the pixels set to copy the rendering settings from.
            to -- The Id of the dataset container to apply settings to.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the rendering settings from is not locatable or the dataset to is not locatable.
            """
            pass

        def applySettingsToImages_async(self, _cb, _from, to, current=None):
            """
            Applies rendering settings to a given Image.
            Arguments:
            _cb -- The asynchronous callback object.
            _from -- The Id of the pixels set to copy the rendering settings from.
            to -- The Id of the image container to apply settings to.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the rendering settings from is not locatable or the image to is not locatable.
            """
            pass

        def applySettingsToImage_async(self, _cb, _from, to, current=None):
            """
            Applies rendering settings to a given Image.
            Arguments:
            _cb -- The asynchronous callback object.
            _from -- The Id of the pixels set to copy the rendering settings from.
            to -- The Id of the image container to apply settings to.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the rendering settings from is not locatable or the image to is not locatable.
            """
            pass

        def applySettingsToPixels_async(self, _cb, _from, to, current=None):
            """
            Applies rendering settings to a given Pixels.
            Arguments:
            _cb -- The asynchronous callback object.
            _from -- The Id of the pixels set to copy the rendering settings from.
            to -- The Id of the pixels container to apply settings to.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the rendering settings from is not locatable or the pixelsto is not locatable.
            """
            pass

        def setOriginalSettingsInImage_async(self, _cb, imageId, current=None):
            """
            Resets an image's default rendering settings back to
            channel global minimum and maximum.
            Arguments:
            _cb -- The asynchronous callback object.
            imageId -- The Id of the Image.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the image qualified by imageId is not locatable.
            """
            pass

        def setOriginalSettingsForPixels_async(self, _cb, pixelsId, current=None):
            """
            Resets an Pixels' default rendering settings back to
            channel global minimum and maximum.
            Arguments:
            _cb -- The asynchronous callback object.
            pixelsId -- The Id of the Pixels set.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the image qualified by pixelsId is not locatable.
            """
            pass

        def setOriginalSettingsInDataset_async(self, _cb, datasetId, current=None):
            """
            Resets a dataset's rendering settings back to channel global
            minimum and maximum.
            Arguments:
            _cb -- The asynchronous callback object.
            datasetId -- The id of the dataset to handle.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if the image qualified by datasetId is not locatable.
            """
            pass

        def setOriginalSettingsInSet_async(self, _cb, type, nodeIds, current=None):
            """
            Resets a rendering settings back to channel global minimum
            and maximum for the specified containers. Supported
            container types are:
            ode.model.Project
            ode.model.Dataset
            ode.model.Image
            ode.model.Plate
            ode.model.Pixels
            Arguments:
            _cb -- The asynchronous callback object.
            type -- The type of nodes to handle.
            nodeIds -- Ids of the node type.
            current -- The Current object for the invocation.
            Throws:
            ValidationException -- if an illegal type is used.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.api._t_IRenderingSettings)

        __repr__ = __str__

    _M_ode.api.IRenderingSettingsPrx = Ice.createTempClass()
    class IRenderingSettingsPrx(_M_ode.api.ServiceInterfacePrx):

        """
        Checks if the specified sets of pixels are compatible.
        Returns true if the pixels set is valid,
        false otherwise.
        Arguments:
        pFrom -- The pixels set to copy the settings from.
        pTo -- The pixels set to copy the settings to.
        _ctx -- The request context for the invocation.
        Returns: See above.
        """
        def sanityCheckPixels(self, pFrom, pTo, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_sanityCheckPixels.invoke(self, ((pFrom, pTo), _ctx))

        """
        Checks if the specified sets of pixels are compatible.
        Returns true if the pixels set is valid,
        false otherwise.
        Arguments:
        pFrom -- The pixels set to copy the settings from.
        pTo -- The pixels set to copy the settings to.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_sanityCheckPixels(self, pFrom, pTo, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_sanityCheckPixels.begin(self, ((pFrom, pTo), _response, _ex, _sent, _ctx))

        """
        Checks if the specified sets of pixels are compatible.
        Returns true if the pixels set is valid,
        false otherwise.
        Arguments:
        pFrom -- The pixels set to copy the settings from.
        pTo -- The pixels set to copy the settings to.
        Returns: See above.
        """
        def end_sanityCheckPixels(self, _r):
            return _M_ode.api.IRenderingSettings._op_sanityCheckPixels.end(self, _r)

        """
        Returns the default rendering settings for a given pixels
        for the current user.
        Arguments:
        pixelsId -- The Id of the Pixels
        _ctx -- The request context for the invocation.
        Returns: See above.
        Throws:
        ValidationException -- if the image qualified by imageId is unlocatable.
        """
        def getRenderingSettings(self, pixelsId, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_getRenderingSettings.invoke(self, ((pixelsId, ), _ctx))

        """
        Returns the default rendering settings for a given pixels
        for the current user.
        Arguments:
        pixelsId -- The Id of the Pixels
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getRenderingSettings(self, pixelsId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_getRenderingSettings.begin(self, ((pixelsId, ), _response, _ex, _sent, _ctx))

        """
        Returns the default rendering settings for a given pixels
        for the current user.
        Arguments:
        pixelsId -- The Id of the Pixels
        Returns: See above.
        Throws:
        ValidationException -- if the image qualified by imageId is unlocatable.
        """
        def end_getRenderingSettings(self, _r):
            return _M_ode.api.IRenderingSettings._op_getRenderingSettings.end(self, _r)

        """
        Creates a new rendering definition object along with its
        sub-objects.
        Arguments:
        pixels -- The Pixels set to link to the rendering definition.
        _ctx -- The request context for the invocation.
        Returns: A new, blank rendering definition and sub-objects. NOTE: the linked Pixels has been unloaded.
        """
        def createNewRenderingDef(self, pixels, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_createNewRenderingDef.invoke(self, ((pixels, ), _ctx))

        """
        Creates a new rendering definition object along with its
        sub-objects.
        Arguments:
        pixels -- The Pixels set to link to the rendering definition.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createNewRenderingDef(self, pixels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_createNewRenderingDef.begin(self, ((pixels, ), _response, _ex, _sent, _ctx))

        """
        Creates a new rendering definition object along with its
        sub-objects.
        Arguments:
        pixels -- The Pixels set to link to the rendering definition.
        Returns: A new, blank rendering definition and sub-objects. NOTE: the linked Pixels has been unloaded.
        """
        def end_createNewRenderingDef(self, _r):
            return _M_ode.api.IRenderingSettings._op_createNewRenderingDef.end(self, _r)

        """
        Resets the given rendering settings to those that are
        specified by the rendering engine intelligent pretty
        good image (PG) logic for the pixels set linked to that
        set of rendering settings. NOTE: This method should
        only be used to reset a rendering definition that has been
        retrieved via {@code getRenderingSettings} as it
        relies on certain objects being loaded. The rendering
        settings are saved upon completion.
        Arguments:
        _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
        pixels -- The Pixels set for def.
        _ctx -- The request context for the invocation.
        """
        def resetDefaults(self, _def, pixels, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaults.invoke(self, ((_def, pixels), _ctx))

        """
        Resets the given rendering settings to those that are
        specified by the rendering engine intelligent pretty
        good image (PG) logic for the pixels set linked to that
        set of rendering settings. NOTE: This method should
        only be used to reset a rendering definition that has been
        retrieved via {@code getRenderingSettings} as it
        relies on certain objects being loaded. The rendering
        settings are saved upon completion.
        Arguments:
        _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
        pixels -- The Pixels set for def.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaults(self, _def, pixels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaults.begin(self, ((_def, pixels), _response, _ex, _sent, _ctx))

        """
        Resets the given rendering settings to those that are
        specified by the rendering engine intelligent pretty
        good image (PG) logic for the pixels set linked to that
        set of rendering settings. NOTE: This method should
        only be used to reset a rendering definition that has been
        retrieved via {@code getRenderingSettings} as it
        relies on certain objects being loaded. The rendering
        settings are saved upon completion.
        Arguments:
        _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
        pixels -- The Pixels set for def.
        """
        def end_resetDefaults(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetDefaults.end(self, _r)

        """
        Resets the given rendering settings to those that are
        specified by the rendering engine intelligent pretty
        good image (PG) logic for the pixels set linked to that
        set of rendering settings. NOTE: This method should
        only be used to reset a rendering definition that has been
        retrieved via {@code getRenderingSettings(long)} as it
        relies on certain objects being loaded. The rendering
        settings are not saved.
        Arguments:
        _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
        pixels -- The Pixels set for def.
        _ctx -- The request context for the invocation.
        Returns: def with the rendering settings reset.
        """
        def resetDefaultsNoSave(self, _def, pixels, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsNoSave.invoke(self, ((_def, pixels), _ctx))

        """
        Resets the given rendering settings to those that are
        specified by the rendering engine intelligent pretty
        good image (PG) logic for the pixels set linked to that
        set of rendering settings. NOTE: This method should
        only be used to reset a rendering definition that has been
        retrieved via {@code getRenderingSettings(long)} as it
        relies on certain objects being loaded. The rendering
        settings are not saved.
        Arguments:
        _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
        pixels -- The Pixels set for def.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaultsNoSave(self, _def, pixels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsNoSave.begin(self, ((_def, pixels), _response, _ex, _sent, _ctx))

        """
        Resets the given rendering settings to those that are
        specified by the rendering engine intelligent pretty
        good image (PG) logic for the pixels set linked to that
        set of rendering settings. NOTE: This method should
        only be used to reset a rendering definition that has been
        retrieved via {@code getRenderingSettings(long)} as it
        relies on certain objects being loaded. The rendering
        settings are not saved.
        Arguments:
        _def -- A RenderingDef to reset. It is expected that def.pixels will be unloaded and that the actual linked Pixels set will be provided in the pixels argument.
        pixels -- The Pixels set for def.
        Returns: def with the rendering settings reset.
        """
        def end_resetDefaultsNoSave(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsNoSave.end(self, _r)

        """
        Resets an image's default rendering settings back to those
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic.
        Arguments:
        imageId -- The Id of the Image.
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- if the image qualified by imageId is unlocatable.
        """
        def resetDefaultsInImage(self, imageId, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInImage.invoke(self, ((imageId, ), _ctx))

        """
        Resets an image's default rendering settings back to those
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic.
        Arguments:
        imageId -- The Id of the Image.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaultsInImage(self, imageId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInImage.begin(self, ((imageId, ), _response, _ex, _sent, _ctx))

        """
        Resets an image's default rendering settings back to those
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic.
        Arguments:
        imageId -- The Id of the Image.
        Throws:
        ValidationException -- if the image qualified by imageId is unlocatable.
        """
        def end_resetDefaultsInImage(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInImage.end(self, _r)

        """
        Resets a Pixels' default rendering settings back to those
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic.
        Arguments:
        pixelsId -- The Id of the Pixels.
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- if the Pixels qualified by pixelsId is unlocatable.
        """
        def resetDefaultsForPixels(self, pixelsId, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsForPixels.invoke(self, ((pixelsId, ), _ctx))

        """
        Resets a Pixels' default rendering settings back to those
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic.
        Arguments:
        pixelsId -- The Id of the Pixels.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaultsForPixels(self, pixelsId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsForPixels.begin(self, ((pixelsId, ), _response, _ex, _sent, _ctx))

        """
        Resets a Pixels' default rendering settings back to those
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic.
        Arguments:
        pixelsId -- The Id of the Pixels.
        Throws:
        ValidationException -- if the Pixels qualified by pixelsId is unlocatable.
        """
        def end_resetDefaultsForPixels(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsForPixels.end(self, _r)

        """
        Resets a dataset's rendering settings back to those that
        are specified by the rendering engine intelligent pretty
        good image (PG) logic.
        Arguments:
        datasetId -- The Id of the Dataset.
        _ctx -- The request context for the invocation.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if the image qualified by datasetId is not locatable.
        """
        def resetDefaultsInDataset(self, datasetId, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInDataset.invoke(self, ((datasetId, ), _ctx))

        """
        Resets a dataset's rendering settings back to those that
        are specified by the rendering engine intelligent pretty
        good image (PG) logic.
        Arguments:
        datasetId -- The Id of the Dataset.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaultsInDataset(self, datasetId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInDataset.begin(self, ((datasetId, ), _response, _ex, _sent, _ctx))

        """
        Resets a dataset's rendering settings back to those that
        are specified by the rendering engine intelligent pretty
        good image (PG) logic.
        Arguments:
        datasetId -- The Id of the Dataset.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if the image qualified by datasetId is not locatable.
        """
        def end_resetDefaultsInDataset(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInDataset.end(self, _r)

        """
        Resets a rendering settings back to one or many containers
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic. Supported container
        types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _ctx -- The request context for the invocation.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def resetDefaultsInSet(self, type, nodeIds, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInSet.invoke(self, ((type, nodeIds), _ctx))

        """
        Resets a rendering settings back to one or many containers
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic. Supported container
        types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaultsInSet(self, type, nodeIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInSet.begin(self, ((type, nodeIds), _response, _ex, _sent, _ctx))

        """
        Resets a rendering settings back to one or many containers
        that are specified by the rendering engine intelligent
        pretty good image (PG) logic. Supported container
        types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def end_resetDefaultsInSet(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsInSet.end(self, _r)

        """
        Resets the rendering settings of a given group of
        containers based on the owner's (essentially a copy).
        Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _ctx -- The request context for the invocation.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def resetDefaultsByOwnerInSet(self, type, nodeIds, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsByOwnerInSet.invoke(self, ((type, nodeIds), _ctx))

        """
        Resets the rendering settings of a given group of
        containers based on the owner's (essentially a copy).
        Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetDefaultsByOwnerInSet(self, type, nodeIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsByOwnerInSet.begin(self, ((type, nodeIds), _response, _ex, _sent, _ctx))

        """
        Resets the rendering settings of a given group of
        containers based on the owner's (essentially a copy).
        Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def end_resetDefaultsByOwnerInSet(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetDefaultsByOwnerInSet.end(self, _r)

        """
        Resets a the channel windows for one or many containers
        back to their global minimum and global maximum for the
        channel. Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _ctx -- The request context for the invocation.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def resetMinMaxInSet(self, type, nodeIds, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetMinMaxInSet.invoke(self, ((type, nodeIds), _ctx))

        """
        Resets a the channel windows for one or many containers
        back to their global minimum and global maximum for the
        channel. Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_resetMinMaxInSet(self, type, nodeIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_resetMinMaxInSet.begin(self, ((type, nodeIds), _response, _ex, _sent, _ctx))

        """
        Resets a the channel windows for one or many containers
        back to their global minimum and global maximum for the
        channel. Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def end_resetMinMaxInSet(self, _r):
            return _M_ode.api.IRenderingSettings._op_resetMinMaxInSet.end(self, _r)

        """
        Applies rendering settings to one or many containers. If a
        container such as Dataset is to be copied to, all images
        within that Dataset will have the rendering settings
        applied. Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Screen
        ode.model.Pixels
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        toType -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _ctx -- The request context for the invocation.
        Returns: A map with two boolean keys. The value of the key TRUE is a collection of image IDs, the settings were successfully applied to. The value of the key FALSE is a collection of image IDs, the settings could not be applied to.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def applySettingsToSet(self, _from, toType, nodeIds, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToSet.invoke(self, ((_from, toType, nodeIds), _ctx))

        """
        Applies rendering settings to one or many containers. If a
        container such as Dataset is to be copied to, all images
        within that Dataset will have the rendering settings
        applied. Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Screen
        ode.model.Pixels
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        toType -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_applySettingsToSet(self, _from, toType, nodeIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToSet.begin(self, ((_from, toType, nodeIds), _response, _ex, _sent, _ctx))

        """
        Applies rendering settings to one or many containers. If a
        container such as Dataset is to be copied to, all images
        within that Dataset will have the rendering settings
        applied. Supported container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Screen
        ode.model.Pixels
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        toType -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        Returns: A map with two boolean keys. The value of the key TRUE is a collection of image IDs, the settings were successfully applied to. The value of the key FALSE is a collection of image IDs, the settings could not be applied to.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def end_applySettingsToSet(self, _r):
            return _M_ode.api.IRenderingSettings._op_applySettingsToSet.end(self, _r)

        """
        Applies rendering settings to all images in all Datasets of
        a given Project.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the project container to apply settings to.
        _ctx -- The request context for the invocation.
        Returns: A map with two boolean keys. The value of the TRUE is a collection of images ID, the settings were successfully applied to. The value of the FALSE is a collection of images ID, the settings could not be applied to.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the project to is not locatable.
        """
        def applySettingsToProject(self, _from, to, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToProject.invoke(self, ((_from, to), _ctx))

        """
        Applies rendering settings to all images in all Datasets of
        a given Project.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the project container to apply settings to.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_applySettingsToProject(self, _from, to, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToProject.begin(self, ((_from, to), _response, _ex, _sent, _ctx))

        """
        Applies rendering settings to all images in all Datasets of
        a given Project.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the project container to apply settings to.
        Returns: A map with two boolean keys. The value of the TRUE is a collection of images ID, the settings were successfully applied to. The value of the FALSE is a collection of images ID, the settings could not be applied to.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the project to is not locatable.
        """
        def end_applySettingsToProject(self, _r):
            return _M_ode.api.IRenderingSettings._op_applySettingsToProject.end(self, _r)

        """
        Applies rendering settings to all images in a given Dataset.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the dataset container to apply settings to.
        _ctx -- The request context for the invocation.
        Returns: A map with two boolean keys. The value of the TRUE is a collection of images ID, the settings were successfully applied to. The value of the FALSE is a collection of images ID, the settings could not be applied to.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the dataset to is not locatable.
        """
        def applySettingsToDataset(self, _from, to, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToDataset.invoke(self, ((_from, to), _ctx))

        """
        Applies rendering settings to all images in a given Dataset.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the dataset container to apply settings to.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_applySettingsToDataset(self, _from, to, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToDataset.begin(self, ((_from, to), _response, _ex, _sent, _ctx))

        """
        Applies rendering settings to all images in a given Dataset.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the dataset container to apply settings to.
        Returns: A map with two boolean keys. The value of the TRUE is a collection of images ID, the settings were successfully applied to. The value of the FALSE is a collection of images ID, the settings could not be applied to.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the dataset to is not locatable.
        """
        def end_applySettingsToDataset(self, _r):
            return _M_ode.api.IRenderingSettings._op_applySettingsToDataset.end(self, _r)

        """
        Applies rendering settings to a given Image.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the image container to apply settings to.
        _ctx -- The request context for the invocation.
        Returns: true if the settings were successfully applied, false otherwise.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the image to is not locatable.
        """
        def applySettingsToImages(self, _from, to, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToImages.invoke(self, ((_from, to), _ctx))

        """
        Applies rendering settings to a given Image.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the image container to apply settings to.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_applySettingsToImages(self, _from, to, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToImages.begin(self, ((_from, to), _response, _ex, _sent, _ctx))

        """
        Applies rendering settings to a given Image.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the image container to apply settings to.
        Returns: true if the settings were successfully applied, false otherwise.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the image to is not locatable.
        """
        def end_applySettingsToImages(self, _r):
            return _M_ode.api.IRenderingSettings._op_applySettingsToImages.end(self, _r)

        """
        Applies rendering settings to a given Image.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the image container to apply settings to.
        _ctx -- The request context for the invocation.
        Returns: true if the settings were successfully applied, false otherwise.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the image to is not locatable.
        """
        def applySettingsToImage(self, _from, to, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToImage.invoke(self, ((_from, to), _ctx))

        """
        Applies rendering settings to a given Image.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the image container to apply settings to.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_applySettingsToImage(self, _from, to, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToImage.begin(self, ((_from, to), _response, _ex, _sent, _ctx))

        """
        Applies rendering settings to a given Image.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the image container to apply settings to.
        Returns: true if the settings were successfully applied, false otherwise.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the image to is not locatable.
        """
        def end_applySettingsToImage(self, _r):
            return _M_ode.api.IRenderingSettings._op_applySettingsToImage.end(self, _r)

        """
        Applies rendering settings to a given Pixels.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the pixels container to apply settings to.
        _ctx -- The request context for the invocation.
        Returns: See above.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the pixelsto is not locatable.
        """
        def applySettingsToPixels(self, _from, to, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToPixels.invoke(self, ((_from, to), _ctx))

        """
        Applies rendering settings to a given Pixels.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the pixels container to apply settings to.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_applySettingsToPixels(self, _from, to, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_applySettingsToPixels.begin(self, ((_from, to), _response, _ex, _sent, _ctx))

        """
        Applies rendering settings to a given Pixels.
        Arguments:
        _from -- The Id of the pixels set to copy the rendering settings from.
        to -- The Id of the pixels container to apply settings to.
        Returns: See above.
        Throws:
        ValidationException -- if the rendering settings from is not locatable or the pixelsto is not locatable.
        """
        def end_applySettingsToPixels(self, _r):
            return _M_ode.api.IRenderingSettings._op_applySettingsToPixels.end(self, _r)

        """
        Resets an image's default rendering settings back to
        channel global minimum and maximum.
        Arguments:
        imageId -- The Id of the Image.
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- if the image qualified by imageId is not locatable.
        """
        def setOriginalSettingsInImage(self, imageId, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInImage.invoke(self, ((imageId, ), _ctx))

        """
        Resets an image's default rendering settings back to
        channel global minimum and maximum.
        Arguments:
        imageId -- The Id of the Image.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setOriginalSettingsInImage(self, imageId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInImage.begin(self, ((imageId, ), _response, _ex, _sent, _ctx))

        """
        Resets an image's default rendering settings back to
        channel global minimum and maximum.
        Arguments:
        imageId -- The Id of the Image.
        Throws:
        ValidationException -- if the image qualified by imageId is not locatable.
        """
        def end_setOriginalSettingsInImage(self, _r):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInImage.end(self, _r)

        """
        Resets an Pixels' default rendering settings back to
        channel global minimum and maximum.
        Arguments:
        pixelsId -- The Id of the Pixels set.
        _ctx -- The request context for the invocation.
        Throws:
        ValidationException -- if the image qualified by pixelsId is not locatable.
        """
        def setOriginalSettingsForPixels(self, pixelsId, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsForPixels.invoke(self, ((pixelsId, ), _ctx))

        """
        Resets an Pixels' default rendering settings back to
        channel global minimum and maximum.
        Arguments:
        pixelsId -- The Id of the Pixels set.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setOriginalSettingsForPixels(self, pixelsId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsForPixels.begin(self, ((pixelsId, ), _response, _ex, _sent, _ctx))

        """
        Resets an Pixels' default rendering settings back to
        channel global minimum and maximum.
        Arguments:
        pixelsId -- The Id of the Pixels set.
        Throws:
        ValidationException -- if the image qualified by pixelsId is not locatable.
        """
        def end_setOriginalSettingsForPixels(self, _r):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsForPixels.end(self, _r)

        """
        Resets a dataset's rendering settings back to channel global
        minimum and maximum.
        Arguments:
        datasetId -- The id of the dataset to handle.
        _ctx -- The request context for the invocation.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if the image qualified by datasetId is not locatable.
        """
        def setOriginalSettingsInDataset(self, datasetId, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInDataset.invoke(self, ((datasetId, ), _ctx))

        """
        Resets a dataset's rendering settings back to channel global
        minimum and maximum.
        Arguments:
        datasetId -- The id of the dataset to handle.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setOriginalSettingsInDataset(self, datasetId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInDataset.begin(self, ((datasetId, ), _response, _ex, _sent, _ctx))

        """
        Resets a dataset's rendering settings back to channel global
        minimum and maximum.
        Arguments:
        datasetId -- The id of the dataset to handle.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if the image qualified by datasetId is not locatable.
        """
        def end_setOriginalSettingsInDataset(self, _r):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInDataset.end(self, _r)

        """
        Resets a rendering settings back to channel global minimum
        and maximum for the specified containers. Supported
        container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _ctx -- The request context for the invocation.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def setOriginalSettingsInSet(self, type, nodeIds, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInSet.invoke(self, ((type, nodeIds), _ctx))

        """
        Resets a rendering settings back to channel global minimum
        and maximum for the specified containers. Supported
        container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setOriginalSettingsInSet(self, type, nodeIds, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInSet.begin(self, ((type, nodeIds), _response, _ex, _sent, _ctx))

        """
        Resets a rendering settings back to channel global minimum
        and maximum for the specified containers. Supported
        container types are:
        ode.model.Project
        ode.model.Dataset
        ode.model.Image
        ode.model.Plate
        ode.model.Pixels
        Arguments:
        type -- The type of nodes to handle.
        nodeIds -- Ids of the node type.
        Returns: A java.util.Set of image IDs that have had their rendering settings reset.
        Throws:
        ValidationException -- if an illegal type is used.
        """
        def end_setOriginalSettingsInSet(self, _r):
            return _M_ode.api.IRenderingSettings._op_setOriginalSettingsInSet.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.api.IRenderingSettingsPrx.ice_checkedCast(proxy, '::ode::api::IRenderingSettings', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.api.IRenderingSettingsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::api::IRenderingSettings'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.api._t_IRenderingSettingsPrx = IcePy.defineProxy('::ode::api::IRenderingSettings', IRenderingSettingsPrx)

    _M_ode.api._t_IRenderingSettings = IcePy.defineClass('::ode::api::IRenderingSettings', IRenderingSettings, -1, (), True, False, None, (_M_ode.api._t_ServiceInterface,), ())
    IRenderingSettings._ice_type = _M_ode.api._t_IRenderingSettings

    IRenderingSettings._op_sanityCheckPixels = IcePy.Operation('sanityCheckPixels', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), _M_ode.model._t_Pixels, False, 0), ((), _M_ode.model._t_Pixels, False, 0)), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_getRenderingSettings = IcePy.Operation('getRenderingSettings', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.model._t_RenderingDef, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_createNewRenderingDef = IcePy.Operation('createNewRenderingDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), ((), _M_ode.model._t_RenderingDef, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetDefaults = IcePy.Operation('resetDefaults', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_RenderingDef, False, 0), ((), _M_ode.model._t_Pixels, False, 0)), (), None, (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetDefaultsNoSave = IcePy.Operation('resetDefaultsNoSave', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), _M_ode.model._t_RenderingDef, False, 0), ((), _M_ode.model._t_Pixels, False, 0)), (), ((), _M_ode.model._t_RenderingDef, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetDefaultsInImage = IcePy.Operation('resetDefaultsInImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetDefaultsForPixels = IcePy.Operation('resetDefaultsForPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetDefaultsInDataset = IcePy.Operation('resetDefaultsInDataset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.sys._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetDefaultsInSet = IcePy.Operation('resetDefaultsInSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.sys._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetDefaultsByOwnerInSet = IcePy.Operation('resetDefaultsByOwnerInSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.sys._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_resetMinMaxInSet = IcePy.Operation('resetMinMaxInSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.sys._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_applySettingsToSet = IcePy.Operation('applySettingsToSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.api._t_BooleanIdListMap, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_applySettingsToProject = IcePy.Operation('applySettingsToProject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.api._t_BooleanIdListMap, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_applySettingsToDataset = IcePy.Operation('applySettingsToDataset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), _M_ode.api._t_BooleanIdListMap, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_applySettingsToImages = IcePy.Operation('applySettingsToImages', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.api._t_BooleanIdListMap, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_applySettingsToImage = IcePy.Operation('applySettingsToImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_applySettingsToPixels = IcePy.Operation('applySettingsToPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0), ((), IcePy._t_long, False, 0)), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_setOriginalSettingsInImage = IcePy.Operation('setOriginalSettingsInImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    IRenderingSettings._op_setOriginalSettingsForPixels = IcePy.Operation('setOriginalSettingsForPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), None, (_M_ode._t_ServerError,))
    IRenderingSettings._op_setOriginalSettingsInDataset = IcePy.Operation('setOriginalSettingsInDataset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.sys._t_LongList, False, 0), (_M_ode._t_ServerError,))
    IRenderingSettings._op_setOriginalSettingsInSet = IcePy.Operation('setOriginalSettingsInSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, True, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.sys._t_LongList, False, 0)), (), ((), _M_ode.sys._t_LongList, False, 0), (_M_ode._t_ServerError,))

    _M_ode.api.IRenderingSettings = IRenderingSettings
    del IRenderingSettings

    _M_ode.api.IRenderingSettingsPrx = IRenderingSettingsPrx
    del IRenderingSettingsPrx

# End of module ode.api

__name__ = 'ode'

# End of module ode
