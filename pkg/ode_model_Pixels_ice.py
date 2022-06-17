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
import ode_model_IObject_ice
import ode_RTypes_ice
import ode_model_RTypes_ice
import ode_System_ice
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

# Start of module ode
__name__ = 'ode'

# Start of module ode.model
__name__ = 'ode.model'

if 'Image' not in _M_ode.model.__dict__:
    _M_ode.model._t_Image = IcePy.declareClass('::ode::model::Image')
    _M_ode.model._t_ImagePrx = IcePy.declareProxy('::ode::model::Image')

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pixels = IcePy.declareClass('::ode::model::Pixels')
    _M_ode.model._t_PixelsPrx = IcePy.declareProxy('::ode::model::Pixels')

if 'PixelsType' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsType = IcePy.declareClass('::ode::model::PixelsType')
    _M_ode.model._t_PixelsTypePrx = IcePy.declareProxy('::ode::model::PixelsType')

if 'DimensionOrder' not in _M_ode.model.__dict__:
    _M_ode.model._t_DimensionOrder = IcePy.declareClass('::ode::model::DimensionOrder')
    _M_ode.model._t_DimensionOrderPrx = IcePy.declareProxy('::ode::model::DimensionOrder')

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'Time' not in _M_ode.model.__dict__:
    _M_ode.model._t_Time = IcePy.declareClass('::ode::model::Time')
    _M_ode.model._t_TimePrx = IcePy.declareProxy('::ode::model::Time')

if 'PlaneInfo' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlaneInfo = IcePy.declareClass('::ode::model::PlaneInfo')
    _M_ode.model._t_PlaneInfoPrx = IcePy.declareProxy('::ode::model::PlaneInfo')

if 'PixelsOriginalFileMap' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsOriginalFileMap = IcePy.declareClass('::ode::model::PixelsOriginalFileMap')
    _M_ode.model._t_PixelsOriginalFileMapPrx = IcePy.declareProxy('::ode::model::PixelsOriginalFileMap')

if 'OriginalFile' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFile = IcePy.declareClass('::ode::model::OriginalFile')
    _M_ode.model._t_OriginalFilePrx = IcePy.declareProxy('::ode::model::OriginalFile')

if 'Channel' not in _M_ode.model.__dict__:
    _M_ode.model._t_Channel = IcePy.declareClass('::ode::model::Channel')
    _M_ode.model._t_ChannelPrx = IcePy.declareProxy('::ode::model::Channel')

if 'RenderingDef' not in _M_ode.model.__dict__:
    _M_ode.model._t_RenderingDef = IcePy.declareClass('::ode::model::RenderingDef')
    _M_ode.model._t_RenderingDefPrx = IcePy.declareProxy('::ode::model::RenderingDef')

if 'Thumbnail' not in _M_ode.model.__dict__:
    _M_ode.model._t_Thumbnail = IcePy.declareClass('::ode::model::Thumbnail')
    _M_ode.model._t_ThumbnailPrx = IcePy.declareProxy('::ode::model::Thumbnail')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_PixelsPlaneInfoSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsPlaneInfoSeq = IcePy.defineSequence('::ode::model::PixelsPlaneInfoSeq', (), _M_ode.model._t_PlaneInfo)

if '_t_PixelsPixelsFileMapsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsPixelsFileMapsSeq = IcePy.defineSequence('::ode::model::PixelsPixelsFileMapsSeq', (), _M_ode.model._t_PixelsOriginalFileMap)

if '_t_PixelsLinkedOriginalFileSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsLinkedOriginalFileSeq = IcePy.defineSequence('::ode::model::PixelsLinkedOriginalFileSeq', (), _M_ode.model._t_OriginalFile)

if '_t_PixelsChannelsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsChannelsSeq = IcePy.defineSequence('::ode::model::PixelsChannelsSeq', (), _M_ode.model._t_Channel)

if '_t_PixelsSettingsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsSettingsSeq = IcePy.defineSequence('::ode::model::PixelsSettingsSeq', (), _M_ode.model._t_RenderingDef)

if '_t_PixelsThumbnailsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsThumbnailsSeq = IcePy.defineSequence('::ode::model::PixelsThumbnailsSeq', (), _M_ode.model._t_Thumbnail)

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model.Pixels = Ice.createTempClass()
    class Pixels(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _image=None, _relatedTo=None, _pixelsType=None, _significantBits=None, _sizeX=None, _sizeY=None, _sizeZ=None, _sizeC=None, _sizeT=None, _sha1=None, _dimensionOrder=None, _physicalSizeX=None, _physicalSizeY=None, _physicalSizeZ=None, _waveStart=None, _waveIncrement=None, _timeIncrement=None, _methodology=None, _planeInfoSeq=None, _planeInfoLoaded=False, _pixelsFileMapsSeq=None, _pixelsFileMapsLoaded=False, _pixelsFileMapsCountPerOwner=None, _channelsSeq=None, _channelsLoaded=False, _settingsSeq=None, _settingsLoaded=False, _thumbnailsSeq=None, _thumbnailsLoaded=False):
            if Ice.getType(self) == _M_ode.model.Pixels:
                raise RuntimeError('ode.model.Pixels is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._image = _image
            self._relatedTo = _relatedTo
            self._pixelsType = _pixelsType
            self._significantBits = _significantBits
            self._sizeX = _sizeX
            self._sizeY = _sizeY
            self._sizeZ = _sizeZ
            self._sizeC = _sizeC
            self._sizeT = _sizeT
            self._sha1 = _sha1
            self._dimensionOrder = _dimensionOrder
            self._physicalSizeX = _physicalSizeX
            self._physicalSizeY = _physicalSizeY
            self._physicalSizeZ = _physicalSizeZ
            self._waveStart = _waveStart
            self._waveIncrement = _waveIncrement
            self._timeIncrement = _timeIncrement
            self._methodology = _methodology
            self._planeInfoSeq = _planeInfoSeq
            self._planeInfoLoaded = _planeInfoLoaded
            self._pixelsFileMapsSeq = _pixelsFileMapsSeq
            self._pixelsFileMapsLoaded = _pixelsFileMapsLoaded
            self._pixelsFileMapsCountPerOwner = _pixelsFileMapsCountPerOwner
            self._channelsSeq = _channelsSeq
            self._channelsLoaded = _channelsLoaded
            self._settingsSeq = _settingsSeq
            self._settingsLoaded = _settingsLoaded
            self._thumbnailsSeq = _thumbnailsSeq
            self._thumbnailsLoaded = _thumbnailsLoaded

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Pixels')

        def ice_id(self, current=None):
            return '::ode::model::Pixels'

        def ice_staticId():
            return '::ode::model::Pixels'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getImage(self, current=None):
            pass

        def setImage(self, theImage, current=None):
            pass

        def getRelatedTo(self, current=None):
            pass

        def setRelatedTo(self, theRelatedTo, current=None):
            pass

        def getPixelsType(self, current=None):
            pass

        def setPixelsType(self, thePixelsType, current=None):
            pass

        def getSignificantBits(self, current=None):
            pass

        def setSignificantBits(self, theSignificantBits, current=None):
            pass

        def getSizeX(self, current=None):
            pass

        def setSizeX(self, theSizeX, current=None):
            pass

        def getSizeY(self, current=None):
            pass

        def setSizeY(self, theSizeY, current=None):
            pass

        def getSizeZ(self, current=None):
            pass

        def setSizeZ(self, theSizeZ, current=None):
            pass

        def getSizeC(self, current=None):
            pass

        def setSizeC(self, theSizeC, current=None):
            pass

        def getSizeT(self, current=None):
            pass

        def setSizeT(self, theSizeT, current=None):
            pass

        def getSha1(self, current=None):
            pass

        def setSha1(self, theSha1, current=None):
            pass

        def getDimensionOrder(self, current=None):
            pass

        def setDimensionOrder(self, theDimensionOrder, current=None):
            pass

        def getPhysicalSizeX(self, current=None):
            pass

        def setPhysicalSizeX(self, thePhysicalSizeX, current=None):
            pass

        def getPhysicalSizeY(self, current=None):
            pass

        def setPhysicalSizeY(self, thePhysicalSizeY, current=None):
            pass

        def getPhysicalSizeZ(self, current=None):
            pass

        def setPhysicalSizeZ(self, thePhysicalSizeZ, current=None):
            pass

        def getWaveStart(self, current=None):
            pass

        def setWaveStart(self, theWaveStart, current=None):
            pass

        def getWaveIncrement(self, current=None):
            pass

        def setWaveIncrement(self, theWaveIncrement, current=None):
            pass

        def getTimeIncrement(self, current=None):
            pass

        def setTimeIncrement(self, theTimeIncrement, current=None):
            pass

        def getMethodology(self, current=None):
            pass

        def setMethodology(self, theMethodology, current=None):
            pass

        def unloadPlaneInfo(self, current=None):
            pass

        def sizeOfPlaneInfo(self, current=None):
            pass

        def copyPlaneInfo(self, current=None):
            pass

        def addPlaneInfo(self, target, current=None):
            pass

        def addAllPlaneInfoSet(self, targets, current=None):
            pass

        def removePlaneInfo(self, theTarget, current=None):
            pass

        def removeAllPlaneInfoSet(self, targets, current=None):
            pass

        def clearPlaneInfo(self, current=None):
            pass

        def reloadPlaneInfo(self, toCopy, current=None):
            pass

        def unloadPixelsFileMaps(self, current=None):
            pass

        def sizeOfPixelsFileMaps(self, current=None):
            pass

        def copyPixelsFileMaps(self, current=None):
            pass

        def addPixelsOriginalFileMap(self, target, current=None):
            pass

        def addAllPixelsOriginalFileMapSet(self, targets, current=None):
            pass

        def removePixelsOriginalFileMap(self, theTarget, current=None):
            pass

        def removeAllPixelsOriginalFileMapSet(self, targets, current=None):
            pass

        def clearPixelsFileMaps(self, current=None):
            pass

        def reloadPixelsFileMaps(self, toCopy, current=None):
            pass

        def getPixelsFileMapsCountPerOwner(self, current=None):
            pass

        def linkOriginalFile(self, addition, current=None):
            pass

        def addPixelsOriginalFileMapToBoth(self, link, bothSides, current=None):
            pass

        def findPixelsOriginalFileMap(self, removal, current=None):
            pass

        def unlinkOriginalFile(self, removal, current=None):
            pass

        def removePixelsOriginalFileMapFromBoth(self, link, bothSides, current=None):
            pass

        def linkedOriginalFileList(self, current=None):
            pass

        def unloadChannels(self, current=None):
            pass

        def sizeOfChannels(self, current=None):
            pass

        def copyChannels(self, current=None):
            pass

        def addChannel(self, target, current=None):
            pass

        def addAllChannelSet(self, targets, current=None):
            pass

        def removeChannel(self, theTarget, current=None):
            pass

        def removeAllChannelSet(self, targets, current=None):
            pass

        def clearChannels(self, current=None):
            pass

        def reloadChannels(self, toCopy, current=None):
            pass

        def getChannel(self, index, current=None):
            pass

        def setChannel(self, index, theElement, current=None):
            pass

        def getPrimaryChannel(self, current=None):
            pass

        def setPrimaryChannel(self, theElement, current=None):
            pass

        def unloadSettings(self, current=None):
            pass

        def sizeOfSettings(self, current=None):
            pass

        def copySettings(self, current=None):
            pass

        def addRenderingDef(self, target, current=None):
            pass

        def addAllRenderingDefSet(self, targets, current=None):
            pass

        def removeRenderingDef(self, theTarget, current=None):
            pass

        def removeAllRenderingDefSet(self, targets, current=None):
            pass

        def clearSettings(self, current=None):
            pass

        def reloadSettings(self, toCopy, current=None):
            pass

        def unloadThumbnails(self, current=None):
            pass

        def sizeOfThumbnails(self, current=None):
            pass

        def copyThumbnails(self, current=None):
            pass

        def addThumbnail(self, target, current=None):
            pass

        def addAllThumbnailSet(self, targets, current=None):
            pass

        def removeThumbnail(self, theTarget, current=None):
            pass

        def removeAllThumbnailSet(self, targets, current=None):
            pass

        def clearThumbnails(self, current=None):
            pass

        def reloadThumbnails(self, toCopy, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Pixels)

        __repr__ = __str__

    _M_ode.model.PixelsPrx = Ice.createTempClass()
    class PixelsPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Pixels._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Pixels._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Pixels._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Pixels._op_setVersion.end(self, _r)

        def getImage(self, _ctx=None):
            return _M_ode.model.Pixels._op_getImage.invoke(self, ((), _ctx))

        def begin_getImage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getImage.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImage(self, _r):
            return _M_ode.model.Pixels._op_getImage.end(self, _r)

        def setImage(self, theImage, _ctx=None):
            return _M_ode.model.Pixels._op_setImage.invoke(self, ((theImage, ), _ctx))

        def begin_setImage(self, theImage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setImage.begin(self, ((theImage, ), _response, _ex, _sent, _ctx))

        def end_setImage(self, _r):
            return _M_ode.model.Pixels._op_setImage.end(self, _r)

        def getRelatedTo(self, _ctx=None):
            return _M_ode.model.Pixels._op_getRelatedTo.invoke(self, ((), _ctx))

        def begin_getRelatedTo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getRelatedTo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRelatedTo(self, _r):
            return _M_ode.model.Pixels._op_getRelatedTo.end(self, _r)

        def setRelatedTo(self, theRelatedTo, _ctx=None):
            return _M_ode.model.Pixels._op_setRelatedTo.invoke(self, ((theRelatedTo, ), _ctx))

        def begin_setRelatedTo(self, theRelatedTo, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setRelatedTo.begin(self, ((theRelatedTo, ), _response, _ex, _sent, _ctx))

        def end_setRelatedTo(self, _r):
            return _M_ode.model.Pixels._op_setRelatedTo.end(self, _r)

        def getPixelsType(self, _ctx=None):
            return _M_ode.model.Pixels._op_getPixelsType.invoke(self, ((), _ctx))

        def begin_getPixelsType(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getPixelsType.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixelsType(self, _r):
            return _M_ode.model.Pixels._op_getPixelsType.end(self, _r)

        def setPixelsType(self, thePixelsType, _ctx=None):
            return _M_ode.model.Pixels._op_setPixelsType.invoke(self, ((thePixelsType, ), _ctx))

        def begin_setPixelsType(self, thePixelsType, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setPixelsType.begin(self, ((thePixelsType, ), _response, _ex, _sent, _ctx))

        def end_setPixelsType(self, _r):
            return _M_ode.model.Pixels._op_setPixelsType.end(self, _r)

        def getSignificantBits(self, _ctx=None):
            return _M_ode.model.Pixels._op_getSignificantBits.invoke(self, ((), _ctx))

        def begin_getSignificantBits(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getSignificantBits.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSignificantBits(self, _r):
            return _M_ode.model.Pixels._op_getSignificantBits.end(self, _r)

        def setSignificantBits(self, theSignificantBits, _ctx=None):
            return _M_ode.model.Pixels._op_setSignificantBits.invoke(self, ((theSignificantBits, ), _ctx))

        def begin_setSignificantBits(self, theSignificantBits, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setSignificantBits.begin(self, ((theSignificantBits, ), _response, _ex, _sent, _ctx))

        def end_setSignificantBits(self, _r):
            return _M_ode.model.Pixels._op_setSignificantBits.end(self, _r)

        def getSizeX(self, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeX.invoke(self, ((), _ctx))

        def begin_getSizeX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSizeX(self, _r):
            return _M_ode.model.Pixels._op_getSizeX.end(self, _r)

        def setSizeX(self, theSizeX, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeX.invoke(self, ((theSizeX, ), _ctx))

        def begin_setSizeX(self, theSizeX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeX.begin(self, ((theSizeX, ), _response, _ex, _sent, _ctx))

        def end_setSizeX(self, _r):
            return _M_ode.model.Pixels._op_setSizeX.end(self, _r)

        def getSizeY(self, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeY.invoke(self, ((), _ctx))

        def begin_getSizeY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSizeY(self, _r):
            return _M_ode.model.Pixels._op_getSizeY.end(self, _r)

        def setSizeY(self, theSizeY, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeY.invoke(self, ((theSizeY, ), _ctx))

        def begin_setSizeY(self, theSizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeY.begin(self, ((theSizeY, ), _response, _ex, _sent, _ctx))

        def end_setSizeY(self, _r):
            return _M_ode.model.Pixels._op_setSizeY.end(self, _r)

        def getSizeZ(self, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeZ.invoke(self, ((), _ctx))

        def begin_getSizeZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeZ.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSizeZ(self, _r):
            return _M_ode.model.Pixels._op_getSizeZ.end(self, _r)

        def setSizeZ(self, theSizeZ, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeZ.invoke(self, ((theSizeZ, ), _ctx))

        def begin_setSizeZ(self, theSizeZ, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeZ.begin(self, ((theSizeZ, ), _response, _ex, _sent, _ctx))

        def end_setSizeZ(self, _r):
            return _M_ode.model.Pixels._op_setSizeZ.end(self, _r)

        def getSizeC(self, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeC.invoke(self, ((), _ctx))

        def begin_getSizeC(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeC.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSizeC(self, _r):
            return _M_ode.model.Pixels._op_getSizeC.end(self, _r)

        def setSizeC(self, theSizeC, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeC.invoke(self, ((theSizeC, ), _ctx))

        def begin_setSizeC(self, theSizeC, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeC.begin(self, ((theSizeC, ), _response, _ex, _sent, _ctx))

        def end_setSizeC(self, _r):
            return _M_ode.model.Pixels._op_setSizeC.end(self, _r)

        def getSizeT(self, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeT.invoke(self, ((), _ctx))

        def begin_getSizeT(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getSizeT.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSizeT(self, _r):
            return _M_ode.model.Pixels._op_getSizeT.end(self, _r)

        def setSizeT(self, theSizeT, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeT.invoke(self, ((theSizeT, ), _ctx))

        def begin_setSizeT(self, theSizeT, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setSizeT.begin(self, ((theSizeT, ), _response, _ex, _sent, _ctx))

        def end_setSizeT(self, _r):
            return _M_ode.model.Pixels._op_setSizeT.end(self, _r)

        def getSha1(self, _ctx=None):
            return _M_ode.model.Pixels._op_getSha1.invoke(self, ((), _ctx))

        def begin_getSha1(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getSha1.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSha1(self, _r):
            return _M_ode.model.Pixels._op_getSha1.end(self, _r)

        def setSha1(self, theSha1, _ctx=None):
            return _M_ode.model.Pixels._op_setSha1.invoke(self, ((theSha1, ), _ctx))

        def begin_setSha1(self, theSha1, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setSha1.begin(self, ((theSha1, ), _response, _ex, _sent, _ctx))

        def end_setSha1(self, _r):
            return _M_ode.model.Pixels._op_setSha1.end(self, _r)

        def getDimensionOrder(self, _ctx=None):
            return _M_ode.model.Pixels._op_getDimensionOrder.invoke(self, ((), _ctx))

        def begin_getDimensionOrder(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getDimensionOrder.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDimensionOrder(self, _r):
            return _M_ode.model.Pixels._op_getDimensionOrder.end(self, _r)

        def setDimensionOrder(self, theDimensionOrder, _ctx=None):
            return _M_ode.model.Pixels._op_setDimensionOrder.invoke(self, ((theDimensionOrder, ), _ctx))

        def begin_setDimensionOrder(self, theDimensionOrder, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setDimensionOrder.begin(self, ((theDimensionOrder, ), _response, _ex, _sent, _ctx))

        def end_setDimensionOrder(self, _r):
            return _M_ode.model.Pixels._op_setDimensionOrder.end(self, _r)

        def getPhysicalSizeX(self, _ctx=None):
            return _M_ode.model.Pixels._op_getPhysicalSizeX.invoke(self, ((), _ctx))

        def begin_getPhysicalSizeX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getPhysicalSizeX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPhysicalSizeX(self, _r):
            return _M_ode.model.Pixels._op_getPhysicalSizeX.end(self, _r)

        def setPhysicalSizeX(self, thePhysicalSizeX, _ctx=None):
            return _M_ode.model.Pixels._op_setPhysicalSizeX.invoke(self, ((thePhysicalSizeX, ), _ctx))

        def begin_setPhysicalSizeX(self, thePhysicalSizeX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setPhysicalSizeX.begin(self, ((thePhysicalSizeX, ), _response, _ex, _sent, _ctx))

        def end_setPhysicalSizeX(self, _r):
            return _M_ode.model.Pixels._op_setPhysicalSizeX.end(self, _r)

        def getPhysicalSizeY(self, _ctx=None):
            return _M_ode.model.Pixels._op_getPhysicalSizeY.invoke(self, ((), _ctx))

        def begin_getPhysicalSizeY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getPhysicalSizeY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPhysicalSizeY(self, _r):
            return _M_ode.model.Pixels._op_getPhysicalSizeY.end(self, _r)

        def setPhysicalSizeY(self, thePhysicalSizeY, _ctx=None):
            return _M_ode.model.Pixels._op_setPhysicalSizeY.invoke(self, ((thePhysicalSizeY, ), _ctx))

        def begin_setPhysicalSizeY(self, thePhysicalSizeY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setPhysicalSizeY.begin(self, ((thePhysicalSizeY, ), _response, _ex, _sent, _ctx))

        def end_setPhysicalSizeY(self, _r):
            return _M_ode.model.Pixels._op_setPhysicalSizeY.end(self, _r)

        def getPhysicalSizeZ(self, _ctx=None):
            return _M_ode.model.Pixels._op_getPhysicalSizeZ.invoke(self, ((), _ctx))

        def begin_getPhysicalSizeZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getPhysicalSizeZ.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPhysicalSizeZ(self, _r):
            return _M_ode.model.Pixels._op_getPhysicalSizeZ.end(self, _r)

        def setPhysicalSizeZ(self, thePhysicalSizeZ, _ctx=None):
            return _M_ode.model.Pixels._op_setPhysicalSizeZ.invoke(self, ((thePhysicalSizeZ, ), _ctx))

        def begin_setPhysicalSizeZ(self, thePhysicalSizeZ, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setPhysicalSizeZ.begin(self, ((thePhysicalSizeZ, ), _response, _ex, _sent, _ctx))

        def end_setPhysicalSizeZ(self, _r):
            return _M_ode.model.Pixels._op_setPhysicalSizeZ.end(self, _r)

        def getWaveStart(self, _ctx=None):
            return _M_ode.model.Pixels._op_getWaveStart.invoke(self, ((), _ctx))

        def begin_getWaveStart(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getWaveStart.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWaveStart(self, _r):
            return _M_ode.model.Pixels._op_getWaveStart.end(self, _r)

        def setWaveStart(self, theWaveStart, _ctx=None):
            return _M_ode.model.Pixels._op_setWaveStart.invoke(self, ((theWaveStart, ), _ctx))

        def begin_setWaveStart(self, theWaveStart, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setWaveStart.begin(self, ((theWaveStart, ), _response, _ex, _sent, _ctx))

        def end_setWaveStart(self, _r):
            return _M_ode.model.Pixels._op_setWaveStart.end(self, _r)

        def getWaveIncrement(self, _ctx=None):
            return _M_ode.model.Pixels._op_getWaveIncrement.invoke(self, ((), _ctx))

        def begin_getWaveIncrement(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getWaveIncrement.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getWaveIncrement(self, _r):
            return _M_ode.model.Pixels._op_getWaveIncrement.end(self, _r)

        def setWaveIncrement(self, theWaveIncrement, _ctx=None):
            return _M_ode.model.Pixels._op_setWaveIncrement.invoke(self, ((theWaveIncrement, ), _ctx))

        def begin_setWaveIncrement(self, theWaveIncrement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setWaveIncrement.begin(self, ((theWaveIncrement, ), _response, _ex, _sent, _ctx))

        def end_setWaveIncrement(self, _r):
            return _M_ode.model.Pixels._op_setWaveIncrement.end(self, _r)

        def getTimeIncrement(self, _ctx=None):
            return _M_ode.model.Pixels._op_getTimeIncrement.invoke(self, ((), _ctx))

        def begin_getTimeIncrement(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getTimeIncrement.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTimeIncrement(self, _r):
            return _M_ode.model.Pixels._op_getTimeIncrement.end(self, _r)

        def setTimeIncrement(self, theTimeIncrement, _ctx=None):
            return _M_ode.model.Pixels._op_setTimeIncrement.invoke(self, ((theTimeIncrement, ), _ctx))

        def begin_setTimeIncrement(self, theTimeIncrement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setTimeIncrement.begin(self, ((theTimeIncrement, ), _response, _ex, _sent, _ctx))

        def end_setTimeIncrement(self, _r):
            return _M_ode.model.Pixels._op_setTimeIncrement.end(self, _r)

        def getMethodology(self, _ctx=None):
            return _M_ode.model.Pixels._op_getMethodology.invoke(self, ((), _ctx))

        def begin_getMethodology(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getMethodology.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMethodology(self, _r):
            return _M_ode.model.Pixels._op_getMethodology.end(self, _r)

        def setMethodology(self, theMethodology, _ctx=None):
            return _M_ode.model.Pixels._op_setMethodology.invoke(self, ((theMethodology, ), _ctx))

        def begin_setMethodology(self, theMethodology, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setMethodology.begin(self, ((theMethodology, ), _response, _ex, _sent, _ctx))

        def end_setMethodology(self, _r):
            return _M_ode.model.Pixels._op_setMethodology.end(self, _r)

        def unloadPlaneInfo(self, _ctx=None):
            return _M_ode.model.Pixels._op_unloadPlaneInfo.invoke(self, ((), _ctx))

        def begin_unloadPlaneInfo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_unloadPlaneInfo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadPlaneInfo(self, _r):
            return _M_ode.model.Pixels._op_unloadPlaneInfo.end(self, _r)

        def sizeOfPlaneInfo(self, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfPlaneInfo.invoke(self, ((), _ctx))

        def begin_sizeOfPlaneInfo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfPlaneInfo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfPlaneInfo(self, _r):
            return _M_ode.model.Pixels._op_sizeOfPlaneInfo.end(self, _r)

        def copyPlaneInfo(self, _ctx=None):
            return _M_ode.model.Pixels._op_copyPlaneInfo.invoke(self, ((), _ctx))

        def begin_copyPlaneInfo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_copyPlaneInfo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyPlaneInfo(self, _r):
            return _M_ode.model.Pixels._op_copyPlaneInfo.end(self, _r)

        def addPlaneInfo(self, target, _ctx=None):
            return _M_ode.model.Pixels._op_addPlaneInfo.invoke(self, ((target, ), _ctx))

        def begin_addPlaneInfo(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addPlaneInfo.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPlaneInfo(self, _r):
            return _M_ode.model.Pixels._op_addPlaneInfo.end(self, _r)

        def addAllPlaneInfoSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_addAllPlaneInfoSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPlaneInfoSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addAllPlaneInfoSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPlaneInfoSet(self, _r):
            return _M_ode.model.Pixels._op_addAllPlaneInfoSet.end(self, _r)

        def removePlaneInfo(self, theTarget, _ctx=None):
            return _M_ode.model.Pixels._op_removePlaneInfo.invoke(self, ((theTarget, ), _ctx))

        def begin_removePlaneInfo(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removePlaneInfo.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePlaneInfo(self, _r):
            return _M_ode.model.Pixels._op_removePlaneInfo.end(self, _r)

        def removeAllPlaneInfoSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllPlaneInfoSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPlaneInfoSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllPlaneInfoSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPlaneInfoSet(self, _r):
            return _M_ode.model.Pixels._op_removeAllPlaneInfoSet.end(self, _r)

        def clearPlaneInfo(self, _ctx=None):
            return _M_ode.model.Pixels._op_clearPlaneInfo.invoke(self, ((), _ctx))

        def begin_clearPlaneInfo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_clearPlaneInfo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearPlaneInfo(self, _r):
            return _M_ode.model.Pixels._op_clearPlaneInfo.end(self, _r)

        def reloadPlaneInfo(self, toCopy, _ctx=None):
            return _M_ode.model.Pixels._op_reloadPlaneInfo.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadPlaneInfo(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_reloadPlaneInfo.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadPlaneInfo(self, _r):
            return _M_ode.model.Pixels._op_reloadPlaneInfo.end(self, _r)

        def unloadPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.Pixels._op_unloadPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_unloadPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_unloadPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadPixelsFileMaps(self, _r):
            return _M_ode.model.Pixels._op_unloadPixelsFileMaps.end(self, _r)

        def sizeOfPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_sizeOfPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfPixelsFileMaps(self, _r):
            return _M_ode.model.Pixels._op_sizeOfPixelsFileMaps.end(self, _r)

        def copyPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.Pixels._op_copyPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_copyPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_copyPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyPixelsFileMaps(self, _r):
            return _M_ode.model.Pixels._op_copyPixelsFileMaps.end(self, _r)

        def addPixelsOriginalFileMap(self, target, _ctx=None):
            return _M_ode.model.Pixels._op_addPixelsOriginalFileMap.invoke(self, ((target, ), _ctx))

        def begin_addPixelsOriginalFileMap(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addPixelsOriginalFileMap.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPixelsOriginalFileMap(self, _r):
            return _M_ode.model.Pixels._op_addPixelsOriginalFileMap.end(self, _r)

        def addAllPixelsOriginalFileMapSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_addAllPixelsOriginalFileMapSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPixelsOriginalFileMapSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addAllPixelsOriginalFileMapSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPixelsOriginalFileMapSet(self, _r):
            return _M_ode.model.Pixels._op_addAllPixelsOriginalFileMapSet.end(self, _r)

        def removePixelsOriginalFileMap(self, theTarget, _ctx=None):
            return _M_ode.model.Pixels._op_removePixelsOriginalFileMap.invoke(self, ((theTarget, ), _ctx))

        def begin_removePixelsOriginalFileMap(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removePixelsOriginalFileMap.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePixelsOriginalFileMap(self, _r):
            return _M_ode.model.Pixels._op_removePixelsOriginalFileMap.end(self, _r)

        def removeAllPixelsOriginalFileMapSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllPixelsOriginalFileMapSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPixelsOriginalFileMapSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllPixelsOriginalFileMapSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPixelsOriginalFileMapSet(self, _r):
            return _M_ode.model.Pixels._op_removeAllPixelsOriginalFileMapSet.end(self, _r)

        def clearPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.Pixels._op_clearPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_clearPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_clearPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearPixelsFileMaps(self, _r):
            return _M_ode.model.Pixels._op_clearPixelsFileMaps.end(self, _r)

        def reloadPixelsFileMaps(self, toCopy, _ctx=None):
            return _M_ode.model.Pixels._op_reloadPixelsFileMaps.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadPixelsFileMaps(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_reloadPixelsFileMaps.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadPixelsFileMaps(self, _r):
            return _M_ode.model.Pixels._op_reloadPixelsFileMaps.end(self, _r)

        def getPixelsFileMapsCountPerOwner(self, _ctx=None):
            return _M_ode.model.Pixels._op_getPixelsFileMapsCountPerOwner.invoke(self, ((), _ctx))

        def begin_getPixelsFileMapsCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getPixelsFileMapsCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixelsFileMapsCountPerOwner(self, _r):
            return _M_ode.model.Pixels._op_getPixelsFileMapsCountPerOwner.end(self, _r)

        def linkOriginalFile(self, addition, _ctx=None):
            return _M_ode.model.Pixels._op_linkOriginalFile.invoke(self, ((addition, ), _ctx))

        def begin_linkOriginalFile(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_linkOriginalFile.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkOriginalFile(self, _r):
            return _M_ode.model.Pixels._op_linkOriginalFile.end(self, _r)

        def addPixelsOriginalFileMapToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Pixels._op_addPixelsOriginalFileMapToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addPixelsOriginalFileMapToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addPixelsOriginalFileMapToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addPixelsOriginalFileMapToBoth(self, _r):
            return _M_ode.model.Pixels._op_addPixelsOriginalFileMapToBoth.end(self, _r)

        def findPixelsOriginalFileMap(self, removal, _ctx=None):
            return _M_ode.model.Pixels._op_findPixelsOriginalFileMap.invoke(self, ((removal, ), _ctx))

        def begin_findPixelsOriginalFileMap(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_findPixelsOriginalFileMap.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findPixelsOriginalFileMap(self, _r):
            return _M_ode.model.Pixels._op_findPixelsOriginalFileMap.end(self, _r)

        def unlinkOriginalFile(self, removal, _ctx=None):
            return _M_ode.model.Pixels._op_unlinkOriginalFile.invoke(self, ((removal, ), _ctx))

        def begin_unlinkOriginalFile(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_unlinkOriginalFile.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkOriginalFile(self, _r):
            return _M_ode.model.Pixels._op_unlinkOriginalFile.end(self, _r)

        def removePixelsOriginalFileMapFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Pixels._op_removePixelsOriginalFileMapFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removePixelsOriginalFileMapFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removePixelsOriginalFileMapFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removePixelsOriginalFileMapFromBoth(self, _r):
            return _M_ode.model.Pixels._op_removePixelsOriginalFileMapFromBoth.end(self, _r)

        def linkedOriginalFileList(self, _ctx=None):
            return _M_ode.model.Pixels._op_linkedOriginalFileList.invoke(self, ((), _ctx))

        def begin_linkedOriginalFileList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_linkedOriginalFileList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedOriginalFileList(self, _r):
            return _M_ode.model.Pixels._op_linkedOriginalFileList.end(self, _r)

        def unloadChannels(self, _ctx=None):
            return _M_ode.model.Pixels._op_unloadChannels.invoke(self, ((), _ctx))

        def begin_unloadChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_unloadChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadChannels(self, _r):
            return _M_ode.model.Pixels._op_unloadChannels.end(self, _r)

        def sizeOfChannels(self, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfChannels.invoke(self, ((), _ctx))

        def begin_sizeOfChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfChannels(self, _r):
            return _M_ode.model.Pixels._op_sizeOfChannels.end(self, _r)

        def copyChannels(self, _ctx=None):
            return _M_ode.model.Pixels._op_copyChannels.invoke(self, ((), _ctx))

        def begin_copyChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_copyChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyChannels(self, _r):
            return _M_ode.model.Pixels._op_copyChannels.end(self, _r)

        def addChannel(self, target, _ctx=None):
            return _M_ode.model.Pixels._op_addChannel.invoke(self, ((target, ), _ctx))

        def begin_addChannel(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addChannel.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addChannel(self, _r):
            return _M_ode.model.Pixels._op_addChannel.end(self, _r)

        def addAllChannelSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_addAllChannelSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllChannelSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addAllChannelSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllChannelSet(self, _r):
            return _M_ode.model.Pixels._op_addAllChannelSet.end(self, _r)

        def removeChannel(self, theTarget, _ctx=None):
            return _M_ode.model.Pixels._op_removeChannel.invoke(self, ((theTarget, ), _ctx))

        def begin_removeChannel(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeChannel.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeChannel(self, _r):
            return _M_ode.model.Pixels._op_removeChannel.end(self, _r)

        def removeAllChannelSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllChannelSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllChannelSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllChannelSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllChannelSet(self, _r):
            return _M_ode.model.Pixels._op_removeAllChannelSet.end(self, _r)

        def clearChannels(self, _ctx=None):
            return _M_ode.model.Pixels._op_clearChannels.invoke(self, ((), _ctx))

        def begin_clearChannels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_clearChannels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearChannels(self, _r):
            return _M_ode.model.Pixels._op_clearChannels.end(self, _r)

        def reloadChannels(self, toCopy, _ctx=None):
            return _M_ode.model.Pixels._op_reloadChannels.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadChannels(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_reloadChannels.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadChannels(self, _r):
            return _M_ode.model.Pixels._op_reloadChannels.end(self, _r)

        def getChannel(self, index, _ctx=None):
            return _M_ode.model.Pixels._op_getChannel.invoke(self, ((index, ), _ctx))

        def begin_getChannel(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getChannel.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getChannel(self, _r):
            return _M_ode.model.Pixels._op_getChannel.end(self, _r)

        def setChannel(self, index, theElement, _ctx=None):
            return _M_ode.model.Pixels._op_setChannel.invoke(self, ((index, theElement), _ctx))

        def begin_setChannel(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setChannel.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setChannel(self, _r):
            return _M_ode.model.Pixels._op_setChannel.end(self, _r)

        def getPrimaryChannel(self, _ctx=None):
            return _M_ode.model.Pixels._op_getPrimaryChannel.invoke(self, ((), _ctx))

        def begin_getPrimaryChannel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_getPrimaryChannel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryChannel(self, _r):
            return _M_ode.model.Pixels._op_getPrimaryChannel.end(self, _r)

        def setPrimaryChannel(self, theElement, _ctx=None):
            return _M_ode.model.Pixels._op_setPrimaryChannel.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryChannel(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_setPrimaryChannel.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryChannel(self, _r):
            return _M_ode.model.Pixels._op_setPrimaryChannel.end(self, _r)

        def unloadSettings(self, _ctx=None):
            return _M_ode.model.Pixels._op_unloadSettings.invoke(self, ((), _ctx))

        def begin_unloadSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_unloadSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadSettings(self, _r):
            return _M_ode.model.Pixels._op_unloadSettings.end(self, _r)

        def sizeOfSettings(self, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfSettings.invoke(self, ((), _ctx))

        def begin_sizeOfSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfSettings(self, _r):
            return _M_ode.model.Pixels._op_sizeOfSettings.end(self, _r)

        def copySettings(self, _ctx=None):
            return _M_ode.model.Pixels._op_copySettings.invoke(self, ((), _ctx))

        def begin_copySettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_copySettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copySettings(self, _r):
            return _M_ode.model.Pixels._op_copySettings.end(self, _r)

        def addRenderingDef(self, target, _ctx=None):
            return _M_ode.model.Pixels._op_addRenderingDef.invoke(self, ((target, ), _ctx))

        def begin_addRenderingDef(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addRenderingDef.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addRenderingDef(self, _r):
            return _M_ode.model.Pixels._op_addRenderingDef.end(self, _r)

        def addAllRenderingDefSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_addAllRenderingDefSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllRenderingDefSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addAllRenderingDefSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllRenderingDefSet(self, _r):
            return _M_ode.model.Pixels._op_addAllRenderingDefSet.end(self, _r)

        def removeRenderingDef(self, theTarget, _ctx=None):
            return _M_ode.model.Pixels._op_removeRenderingDef.invoke(self, ((theTarget, ), _ctx))

        def begin_removeRenderingDef(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeRenderingDef.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeRenderingDef(self, _r):
            return _M_ode.model.Pixels._op_removeRenderingDef.end(self, _r)

        def removeAllRenderingDefSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllRenderingDefSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllRenderingDefSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllRenderingDefSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllRenderingDefSet(self, _r):
            return _M_ode.model.Pixels._op_removeAllRenderingDefSet.end(self, _r)

        def clearSettings(self, _ctx=None):
            return _M_ode.model.Pixels._op_clearSettings.invoke(self, ((), _ctx))

        def begin_clearSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_clearSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearSettings(self, _r):
            return _M_ode.model.Pixels._op_clearSettings.end(self, _r)

        def reloadSettings(self, toCopy, _ctx=None):
            return _M_ode.model.Pixels._op_reloadSettings.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadSettings(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_reloadSettings.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadSettings(self, _r):
            return _M_ode.model.Pixels._op_reloadSettings.end(self, _r)

        def unloadThumbnails(self, _ctx=None):
            return _M_ode.model.Pixels._op_unloadThumbnails.invoke(self, ((), _ctx))

        def begin_unloadThumbnails(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_unloadThumbnails.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadThumbnails(self, _r):
            return _M_ode.model.Pixels._op_unloadThumbnails.end(self, _r)

        def sizeOfThumbnails(self, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfThumbnails.invoke(self, ((), _ctx))

        def begin_sizeOfThumbnails(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_sizeOfThumbnails.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfThumbnails(self, _r):
            return _M_ode.model.Pixels._op_sizeOfThumbnails.end(self, _r)

        def copyThumbnails(self, _ctx=None):
            return _M_ode.model.Pixels._op_copyThumbnails.invoke(self, ((), _ctx))

        def begin_copyThumbnails(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_copyThumbnails.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyThumbnails(self, _r):
            return _M_ode.model.Pixels._op_copyThumbnails.end(self, _r)

        def addThumbnail(self, target, _ctx=None):
            return _M_ode.model.Pixels._op_addThumbnail.invoke(self, ((target, ), _ctx))

        def begin_addThumbnail(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addThumbnail.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addThumbnail(self, _r):
            return _M_ode.model.Pixels._op_addThumbnail.end(self, _r)

        def addAllThumbnailSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_addAllThumbnailSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllThumbnailSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_addAllThumbnailSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllThumbnailSet(self, _r):
            return _M_ode.model.Pixels._op_addAllThumbnailSet.end(self, _r)

        def removeThumbnail(self, theTarget, _ctx=None):
            return _M_ode.model.Pixels._op_removeThumbnail.invoke(self, ((theTarget, ), _ctx))

        def begin_removeThumbnail(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeThumbnail.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeThumbnail(self, _r):
            return _M_ode.model.Pixels._op_removeThumbnail.end(self, _r)

        def removeAllThumbnailSet(self, targets, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllThumbnailSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllThumbnailSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_removeAllThumbnailSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllThumbnailSet(self, _r):
            return _M_ode.model.Pixels._op_removeAllThumbnailSet.end(self, _r)

        def clearThumbnails(self, _ctx=None):
            return _M_ode.model.Pixels._op_clearThumbnails.invoke(self, ((), _ctx))

        def begin_clearThumbnails(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_clearThumbnails.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearThumbnails(self, _r):
            return _M_ode.model.Pixels._op_clearThumbnails.end(self, _r)

        def reloadThumbnails(self, toCopy, _ctx=None):
            return _M_ode.model.Pixels._op_reloadThumbnails.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadThumbnails(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Pixels._op_reloadThumbnails.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadThumbnails(self, _r):
            return _M_ode.model.Pixels._op_reloadThumbnails.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PixelsPrx.ice_checkedCast(proxy, '::ode::model::Pixels', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PixelsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Pixels'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PixelsPrx = IcePy.defineProxy('::ode::model::Pixels', PixelsPrx)

    _M_ode.model._t_Pixels = IcePy.defineClass('::ode::model::Pixels', Pixels, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_image', (), _M_ode.model._t_Image, False, 0),
        ('_relatedTo', (), _M_ode.model._t_Pixels, False, 0),
        ('_pixelsType', (), _M_ode.model._t_PixelsType, False, 0),
        ('_significantBits', (), _M_ode._t_RInt, False, 0),
        ('_sizeX', (), _M_ode._t_RInt, False, 0),
        ('_sizeY', (), _M_ode._t_RInt, False, 0),
        ('_sizeZ', (), _M_ode._t_RInt, False, 0),
        ('_sizeC', (), _M_ode._t_RInt, False, 0),
        ('_sizeT', (), _M_ode._t_RInt, False, 0),
        ('_sha1', (), _M_ode._t_RString, False, 0),
        ('_dimensionOrder', (), _M_ode.model._t_DimensionOrder, False, 0),
        ('_physicalSizeX', (), _M_ode.model._t_Length, False, 0),
        ('_physicalSizeY', (), _M_ode.model._t_Length, False, 0),
        ('_physicalSizeZ', (), _M_ode.model._t_Length, False, 0),
        ('_waveStart', (), _M_ode._t_RInt, False, 0),
        ('_waveIncrement', (), _M_ode._t_RInt, False, 0),
        ('_timeIncrement', (), _M_ode.model._t_Time, False, 0),
        ('_methodology', (), _M_ode._t_RString, False, 0),
        ('_planeInfoSeq', (), _M_ode.model._t_PixelsPlaneInfoSeq, False, 0),
        ('_planeInfoLoaded', (), IcePy._t_bool, False, 0),
        ('_pixelsFileMapsSeq', (), _M_ode.model._t_PixelsPixelsFileMapsSeq, False, 0),
        ('_pixelsFileMapsLoaded', (), IcePy._t_bool, False, 0),
        ('_pixelsFileMapsCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_channelsSeq', (), _M_ode.model._t_PixelsChannelsSeq, False, 0),
        ('_channelsLoaded', (), IcePy._t_bool, False, 0),
        ('_settingsSeq', (), _M_ode.model._t_PixelsSettingsSeq, False, 0),
        ('_settingsLoaded', (), IcePy._t_bool, False, 0),
        ('_thumbnailsSeq', (), _M_ode.model._t_PixelsThumbnailsSeq, False, 0),
        ('_thumbnailsLoaded', (), IcePy._t_bool, False, 0)
    ))
    Pixels._ice_type = _M_ode.model._t_Pixels

    Pixels._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getImage = IcePy.Operation('getImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Image, False, 0), ())
    Pixels._op_setImage = IcePy.Operation('setImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), None, ())
    Pixels._op_getRelatedTo = IcePy.Operation('getRelatedTo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Pixels, False, 0), ())
    Pixels._op_setRelatedTo = IcePy.Operation('setRelatedTo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    Pixels._op_getPixelsType = IcePy.Operation('getPixelsType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PixelsType, False, 0), ())
    Pixels._op_setPixelsType = IcePy.Operation('setPixelsType', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsType, False, 0),), (), None, ())
    Pixels._op_getSignificantBits = IcePy.Operation('getSignificantBits', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setSignificantBits = IcePy.Operation('setSignificantBits', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getSizeX = IcePy.Operation('getSizeX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setSizeX = IcePy.Operation('setSizeX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getSizeY = IcePy.Operation('getSizeY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setSizeY = IcePy.Operation('setSizeY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getSizeZ = IcePy.Operation('getSizeZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setSizeZ = IcePy.Operation('setSizeZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getSizeC = IcePy.Operation('getSizeC', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setSizeC = IcePy.Operation('setSizeC', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getSizeT = IcePy.Operation('getSizeT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setSizeT = IcePy.Operation('setSizeT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getSha1 = IcePy.Operation('getSha1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Pixels._op_setSha1 = IcePy.Operation('setSha1', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Pixels._op_getDimensionOrder = IcePy.Operation('getDimensionOrder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DimensionOrder, False, 0), ())
    Pixels._op_setDimensionOrder = IcePy.Operation('setDimensionOrder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DimensionOrder, False, 0),), (), None, ())
    Pixels._op_getPhysicalSizeX = IcePy.Operation('getPhysicalSizeX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Pixels._op_setPhysicalSizeX = IcePy.Operation('setPhysicalSizeX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Pixels._op_getPhysicalSizeY = IcePy.Operation('getPhysicalSizeY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Pixels._op_setPhysicalSizeY = IcePy.Operation('setPhysicalSizeY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Pixels._op_getPhysicalSizeZ = IcePy.Operation('getPhysicalSizeZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    Pixels._op_setPhysicalSizeZ = IcePy.Operation('setPhysicalSizeZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    Pixels._op_getWaveStart = IcePy.Operation('getWaveStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setWaveStart = IcePy.Operation('setWaveStart', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getWaveIncrement = IcePy.Operation('getWaveIncrement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Pixels._op_setWaveIncrement = IcePy.Operation('setWaveIncrement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Pixels._op_getTimeIncrement = IcePy.Operation('getTimeIncrement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Time, False, 0), ())
    Pixels._op_setTimeIncrement = IcePy.Operation('setTimeIncrement', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Time, False, 0),), (), None, ())
    Pixels._op_getMethodology = IcePy.Operation('getMethodology', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Pixels._op_setMethodology = IcePy.Operation('setMethodology', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Pixels._op_unloadPlaneInfo = IcePy.Operation('unloadPlaneInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_sizeOfPlaneInfo = IcePy.Operation('sizeOfPlaneInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Pixels._op_copyPlaneInfo = IcePy.Operation('copyPlaneInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PixelsPlaneInfoSeq, False, 0), ())
    Pixels._op_addPlaneInfo = IcePy.Operation('addPlaneInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfo, False, 0),), (), None, ())
    Pixels._op_addAllPlaneInfoSet = IcePy.Operation('addAllPlaneInfoSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsPlaneInfoSeq, False, 0),), (), None, ())
    Pixels._op_removePlaneInfo = IcePy.Operation('removePlaneInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfo, False, 0),), (), None, ())
    Pixels._op_removeAllPlaneInfoSet = IcePy.Operation('removeAllPlaneInfoSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsPlaneInfoSeq, False, 0),), (), None, ())
    Pixels._op_clearPlaneInfo = IcePy.Operation('clearPlaneInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_reloadPlaneInfo = IcePy.Operation('reloadPlaneInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    Pixels._op_unloadPixelsFileMaps = IcePy.Operation('unloadPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_sizeOfPixelsFileMaps = IcePy.Operation('sizeOfPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Pixels._op_copyPixelsFileMaps = IcePy.Operation('copyPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PixelsPixelsFileMapsSeq, False, 0), ())
    Pixels._op_addPixelsOriginalFileMap = IcePy.Operation('addPixelsOriginalFileMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0),), (), None, ())
    Pixels._op_addAllPixelsOriginalFileMapSet = IcePy.Operation('addAllPixelsOriginalFileMapSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsPixelsFileMapsSeq, False, 0),), (), None, ())
    Pixels._op_removePixelsOriginalFileMap = IcePy.Operation('removePixelsOriginalFileMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0),), (), None, ())
    Pixels._op_removeAllPixelsOriginalFileMapSet = IcePy.Operation('removeAllPixelsOriginalFileMapSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsPixelsFileMapsSeq, False, 0),), (), None, ())
    Pixels._op_clearPixelsFileMaps = IcePy.Operation('clearPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_reloadPixelsFileMaps = IcePy.Operation('reloadPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    Pixels._op_getPixelsFileMapsCountPerOwner = IcePy.Operation('getPixelsFileMapsCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Pixels._op_linkOriginalFile = IcePy.Operation('linkOriginalFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.model._t_PixelsOriginalFileMap, False, 0), ())
    Pixels._op_addPixelsOriginalFileMapToBoth = IcePy.Operation('addPixelsOriginalFileMapToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Pixels._op_findPixelsOriginalFileMap = IcePy.Operation('findPixelsOriginalFileMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.model._t_PixelsPixelsFileMapsSeq, False, 0), ())
    Pixels._op_unlinkOriginalFile = IcePy.Operation('unlinkOriginalFile', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())
    Pixels._op_removePixelsOriginalFileMapFromBoth = IcePy.Operation('removePixelsOriginalFileMapFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Pixels._op_linkedOriginalFileList = IcePy.Operation('linkedOriginalFileList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PixelsLinkedOriginalFileSeq, False, 0), ())
    Pixels._op_unloadChannels = IcePy.Operation('unloadChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_sizeOfChannels = IcePy.Operation('sizeOfChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Pixels._op_copyChannels = IcePy.Operation('copyChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PixelsChannelsSeq, False, 0), ())
    Pixels._op_addChannel = IcePy.Operation('addChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Channel, False, 0),), (), None, ())
    Pixels._op_addAllChannelSet = IcePy.Operation('addAllChannelSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsChannelsSeq, False, 0),), (), None, ())
    Pixels._op_removeChannel = IcePy.Operation('removeChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Channel, False, 0),), (), None, ())
    Pixels._op_removeAllChannelSet = IcePy.Operation('removeAllChannelSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsChannelsSeq, False, 0),), (), None, ())
    Pixels._op_clearChannels = IcePy.Operation('clearChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_reloadChannels = IcePy.Operation('reloadChannels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    Pixels._op_getChannel = IcePy.Operation('getChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_Channel, False, 0), ())
    Pixels._op_setChannel = IcePy.Operation('setChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_Channel, False, 0)), (), ((), _M_ode.model._t_Channel, False, 0), ())
    Pixels._op_getPrimaryChannel = IcePy.Operation('getPrimaryChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Channel, False, 0), ())
    Pixels._op_setPrimaryChannel = IcePy.Operation('setPrimaryChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Channel, False, 0),), (), ((), _M_ode.model._t_Channel, False, 0), ())
    Pixels._op_unloadSettings = IcePy.Operation('unloadSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_sizeOfSettings = IcePy.Operation('sizeOfSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Pixels._op_copySettings = IcePy.Operation('copySettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PixelsSettingsSeq, False, 0), ())
    Pixels._op_addRenderingDef = IcePy.Operation('addRenderingDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, ())
    Pixels._op_addAllRenderingDefSet = IcePy.Operation('addAllRenderingDefSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsSettingsSeq, False, 0),), (), None, ())
    Pixels._op_removeRenderingDef = IcePy.Operation('removeRenderingDef', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RenderingDef, False, 0),), (), None, ())
    Pixels._op_removeAllRenderingDefSet = IcePy.Operation('removeAllRenderingDefSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsSettingsSeq, False, 0),), (), None, ())
    Pixels._op_clearSettings = IcePy.Operation('clearSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_reloadSettings = IcePy.Operation('reloadSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    Pixels._op_unloadThumbnails = IcePy.Operation('unloadThumbnails', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_sizeOfThumbnails = IcePy.Operation('sizeOfThumbnails', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Pixels._op_copyThumbnails = IcePy.Operation('copyThumbnails', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PixelsThumbnailsSeq, False, 0), ())
    Pixels._op_addThumbnail = IcePy.Operation('addThumbnail', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Thumbnail, False, 0),), (), None, ())
    Pixels._op_addAllThumbnailSet = IcePy.Operation('addAllThumbnailSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsThumbnailsSeq, False, 0),), (), None, ())
    Pixels._op_removeThumbnail = IcePy.Operation('removeThumbnail', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Thumbnail, False, 0),), (), None, ())
    Pixels._op_removeAllThumbnailSet = IcePy.Operation('removeAllThumbnailSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsThumbnailsSeq, False, 0),), (), None, ())
    Pixels._op_clearThumbnails = IcePy.Operation('clearThumbnails', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Pixels._op_reloadThumbnails = IcePy.Operation('reloadThumbnails', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())

    _M_ode.model.Pixels = Pixels
    del Pixels

    _M_ode.model.PixelsPrx = PixelsPrx
    del PixelsPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
