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

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pixels = IcePy.declareClass('::ode::model::Pixels')
    _M_ode.model._t_PixelsPrx = IcePy.declareProxy('::ode::model::Pixels')

if 'Time' not in _M_ode.model.__dict__:
    _M_ode.model._t_Time = IcePy.declareClass('::ode::model::Time')
    _M_ode.model._t_TimePrx = IcePy.declareProxy('::ode::model::Time')

if 'Length' not in _M_ode.model.__dict__:
    _M_ode.model._t_Length = IcePy.declareClass('::ode::model::Length')
    _M_ode.model._t_LengthPrx = IcePy.declareProxy('::ode::model::Length')

if 'PlaneInfoAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlaneInfoAnnotationLink = IcePy.declareClass('::ode::model::PlaneInfoAnnotationLink')
    _M_ode.model._t_PlaneInfoAnnotationLinkPrx = IcePy.declareProxy('::ode::model::PlaneInfoAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_PlaneInfoAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlaneInfoAnnotationLinksSeq = IcePy.defineSequence('::ode::model::PlaneInfoAnnotationLinksSeq', (), _M_ode.model._t_PlaneInfoAnnotationLink)

if '_t_PlaneInfoLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_PlaneInfoLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::PlaneInfoLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'PlaneInfo' not in _M_ode.model.__dict__:
    _M_ode.model.PlaneInfo = Ice.createTempClass()
    class PlaneInfo(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _pixels=None, _theZ=None, _theC=None, _theT=None, _deltaT=None, _positionX=None, _positionY=None, _positionZ=None, _exposureTime=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.PlaneInfo:
                raise RuntimeError('ode.model.PlaneInfo is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._pixels = _pixels
            self._theZ = _theZ
            self._theC = _theC
            self._theT = _theT
            self._deltaT = _deltaT
            self._positionX = _positionX
            self._positionY = _positionY
            self._positionZ = _positionZ
            self._exposureTime = _exposureTime
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::PlaneInfo')

        def ice_id(self, current=None):
            return '::ode::model::PlaneInfo'

        def ice_staticId():
            return '::ode::model::PlaneInfo'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getPixels(self, current=None):
            pass

        def setPixels(self, thePixels, current=None):
            pass

        def getTheZ(self, current=None):
            pass

        def setTheZ(self, theTheZ, current=None):
            pass

        def getTheC(self, current=None):
            pass

        def setTheC(self, theTheC, current=None):
            pass

        def getTheT(self, current=None):
            pass

        def setTheT(self, theTheT, current=None):
            pass

        def getDeltaT(self, current=None):
            pass

        def setDeltaT(self, theDeltaT, current=None):
            pass

        def getPositionX(self, current=None):
            pass

        def setPositionX(self, thePositionX, current=None):
            pass

        def getPositionY(self, current=None):
            pass

        def setPositionY(self, thePositionY, current=None):
            pass

        def getPositionZ(self, current=None):
            pass

        def setPositionZ(self, thePositionZ, current=None):
            pass

        def getExposureTime(self, current=None):
            pass

        def setExposureTime(self, theExposureTime, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addPlaneInfoAnnotationLink(self, target, current=None):
            pass

        def addAllPlaneInfoAnnotationLinkSet(self, targets, current=None):
            pass

        def removePlaneInfoAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllPlaneInfoAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addPlaneInfoAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findPlaneInfoAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removePlaneInfoAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_PlaneInfo)

        __repr__ = __str__

    _M_ode.model.PlaneInfoPrx = Ice.createTempClass()
    class PlaneInfoPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.PlaneInfo._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.PlaneInfo._op_setVersion.end(self, _r)

        def getPixels(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPixels.invoke(self, ((), _ctx))

        def begin_getPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixels(self, _r):
            return _M_ode.model.PlaneInfo._op_getPixels.end(self, _r)

        def setPixels(self, thePixels, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPixels.invoke(self, ((thePixels, ), _ctx))

        def begin_setPixels(self, thePixels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPixels.begin(self, ((thePixels, ), _response, _ex, _sent, _ctx))

        def end_setPixels(self, _r):
            return _M_ode.model.PlaneInfo._op_setPixels.end(self, _r)

        def getTheZ(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getTheZ.invoke(self, ((), _ctx))

        def begin_getTheZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getTheZ.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTheZ(self, _r):
            return _M_ode.model.PlaneInfo._op_getTheZ.end(self, _r)

        def setTheZ(self, theTheZ, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setTheZ.invoke(self, ((theTheZ, ), _ctx))

        def begin_setTheZ(self, theTheZ, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setTheZ.begin(self, ((theTheZ, ), _response, _ex, _sent, _ctx))

        def end_setTheZ(self, _r):
            return _M_ode.model.PlaneInfo._op_setTheZ.end(self, _r)

        def getTheC(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getTheC.invoke(self, ((), _ctx))

        def begin_getTheC(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getTheC.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTheC(self, _r):
            return _M_ode.model.PlaneInfo._op_getTheC.end(self, _r)

        def setTheC(self, theTheC, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setTheC.invoke(self, ((theTheC, ), _ctx))

        def begin_setTheC(self, theTheC, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setTheC.begin(self, ((theTheC, ), _response, _ex, _sent, _ctx))

        def end_setTheC(self, _r):
            return _M_ode.model.PlaneInfo._op_setTheC.end(self, _r)

        def getTheT(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getTheT.invoke(self, ((), _ctx))

        def begin_getTheT(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getTheT.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTheT(self, _r):
            return _M_ode.model.PlaneInfo._op_getTheT.end(self, _r)

        def setTheT(self, theTheT, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setTheT.invoke(self, ((theTheT, ), _ctx))

        def begin_setTheT(self, theTheT, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setTheT.begin(self, ((theTheT, ), _response, _ex, _sent, _ctx))

        def end_setTheT(self, _r):
            return _M_ode.model.PlaneInfo._op_setTheT.end(self, _r)

        def getDeltaT(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getDeltaT.invoke(self, ((), _ctx))

        def begin_getDeltaT(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getDeltaT.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDeltaT(self, _r):
            return _M_ode.model.PlaneInfo._op_getDeltaT.end(self, _r)

        def setDeltaT(self, theDeltaT, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setDeltaT.invoke(self, ((theDeltaT, ), _ctx))

        def begin_setDeltaT(self, theDeltaT, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setDeltaT.begin(self, ((theDeltaT, ), _response, _ex, _sent, _ctx))

        def end_setDeltaT(self, _r):
            return _M_ode.model.PlaneInfo._op_setDeltaT.end(self, _r)

        def getPositionX(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPositionX.invoke(self, ((), _ctx))

        def begin_getPositionX(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPositionX.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPositionX(self, _r):
            return _M_ode.model.PlaneInfo._op_getPositionX.end(self, _r)

        def setPositionX(self, thePositionX, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPositionX.invoke(self, ((thePositionX, ), _ctx))

        def begin_setPositionX(self, thePositionX, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPositionX.begin(self, ((thePositionX, ), _response, _ex, _sent, _ctx))

        def end_setPositionX(self, _r):
            return _M_ode.model.PlaneInfo._op_setPositionX.end(self, _r)

        def getPositionY(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPositionY.invoke(self, ((), _ctx))

        def begin_getPositionY(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPositionY.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPositionY(self, _r):
            return _M_ode.model.PlaneInfo._op_getPositionY.end(self, _r)

        def setPositionY(self, thePositionY, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPositionY.invoke(self, ((thePositionY, ), _ctx))

        def begin_setPositionY(self, thePositionY, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPositionY.begin(self, ((thePositionY, ), _response, _ex, _sent, _ctx))

        def end_setPositionY(self, _r):
            return _M_ode.model.PlaneInfo._op_setPositionY.end(self, _r)

        def getPositionZ(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPositionZ.invoke(self, ((), _ctx))

        def begin_getPositionZ(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getPositionZ.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPositionZ(self, _r):
            return _M_ode.model.PlaneInfo._op_getPositionZ.end(self, _r)

        def setPositionZ(self, thePositionZ, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPositionZ.invoke(self, ((thePositionZ, ), _ctx))

        def begin_setPositionZ(self, thePositionZ, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setPositionZ.begin(self, ((thePositionZ, ), _response, _ex, _sent, _ctx))

        def end_setPositionZ(self, _r):
            return _M_ode.model.PlaneInfo._op_setPositionZ.end(self, _r)

        def getExposureTime(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getExposureTime.invoke(self, ((), _ctx))

        def begin_getExposureTime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getExposureTime.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExposureTime(self, _r):
            return _M_ode.model.PlaneInfo._op_getExposureTime.end(self, _r)

        def setExposureTime(self, theExposureTime, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setExposureTime.invoke(self, ((theExposureTime, ), _ctx))

        def begin_setExposureTime(self, theExposureTime, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_setExposureTime.begin(self, ((theExposureTime, ), _response, _ex, _sent, _ctx))

        def end_setExposureTime(self, _r):
            return _M_ode.model.PlaneInfo._op_setExposureTime.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.PlaneInfo._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.PlaneInfo._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.PlaneInfo._op_copyAnnotationLinks.end(self, _r)

        def addPlaneInfoAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.PlaneInfo._op_addPlaneInfoAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addPlaneInfoAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_addPlaneInfoAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPlaneInfoAnnotationLink(self, _r):
            return _M_ode.model.PlaneInfo._op_addPlaneInfoAnnotationLink.end(self, _r)

        def addAllPlaneInfoAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.PlaneInfo._op_addAllPlaneInfoAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPlaneInfoAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_addAllPlaneInfoAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPlaneInfoAnnotationLinkSet(self, _r):
            return _M_ode.model.PlaneInfo._op_addAllPlaneInfoAnnotationLinkSet.end(self, _r)

        def removePlaneInfoAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.PlaneInfo._op_removePlaneInfoAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removePlaneInfoAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_removePlaneInfoAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePlaneInfoAnnotationLink(self, _r):
            return _M_ode.model.PlaneInfo._op_removePlaneInfoAnnotationLink.end(self, _r)

        def removeAllPlaneInfoAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.PlaneInfo._op_removeAllPlaneInfoAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPlaneInfoAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_removeAllPlaneInfoAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPlaneInfoAnnotationLinkSet(self, _r):
            return _M_ode.model.PlaneInfo._op_removeAllPlaneInfoAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.PlaneInfo._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.PlaneInfo._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.PlaneInfo._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.PlaneInfo._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.PlaneInfo._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.PlaneInfo._op_linkAnnotation.end(self, _r)

        def addPlaneInfoAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.PlaneInfo._op_addPlaneInfoAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addPlaneInfoAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_addPlaneInfoAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addPlaneInfoAnnotationLinkToBoth(self, _r):
            return _M_ode.model.PlaneInfo._op_addPlaneInfoAnnotationLinkToBoth.end(self, _r)

        def findPlaneInfoAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.PlaneInfo._op_findPlaneInfoAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findPlaneInfoAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_findPlaneInfoAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findPlaneInfoAnnotationLink(self, _r):
            return _M_ode.model.PlaneInfo._op_findPlaneInfoAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.PlaneInfo._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.PlaneInfo._op_unlinkAnnotation.end(self, _r)

        def removePlaneInfoAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.PlaneInfo._op_removePlaneInfoAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removePlaneInfoAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_removePlaneInfoAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removePlaneInfoAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.PlaneInfo._op_removePlaneInfoAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.PlaneInfo._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.PlaneInfo._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.PlaneInfo._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.PlaneInfoPrx.ice_checkedCast(proxy, '::ode::model::PlaneInfo', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.PlaneInfoPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::PlaneInfo'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_PlaneInfoPrx = IcePy.defineProxy('::ode::model::PlaneInfo', PlaneInfoPrx)

    _M_ode.model._t_PlaneInfo = IcePy.declareClass('::ode::model::PlaneInfo')

    _M_ode.model._t_PlaneInfo = IcePy.defineClass('::ode::model::PlaneInfo', PlaneInfo, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_pixels', (), _M_ode.model._t_Pixels, False, 0),
        ('_theZ', (), _M_ode._t_RInt, False, 0),
        ('_theC', (), _M_ode._t_RInt, False, 0),
        ('_theT', (), _M_ode._t_RInt, False, 0),
        ('_deltaT', (), _M_ode.model._t_Time, False, 0),
        ('_positionX', (), _M_ode.model._t_Length, False, 0),
        ('_positionY', (), _M_ode.model._t_Length, False, 0),
        ('_positionZ', (), _M_ode.model._t_Length, False, 0),
        ('_exposureTime', (), _M_ode.model._t_Time, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_PlaneInfoAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    PlaneInfo._ice_type = _M_ode.model._t_PlaneInfo

    PlaneInfo._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneInfo._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneInfo._op_getPixels = IcePy.Operation('getPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Pixels, False, 0), ())
    PlaneInfo._op_setPixels = IcePy.Operation('setPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    PlaneInfo._op_getTheZ = IcePy.Operation('getTheZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneInfo._op_setTheZ = IcePy.Operation('setTheZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneInfo._op_getTheC = IcePy.Operation('getTheC', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneInfo._op_setTheC = IcePy.Operation('setTheC', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneInfo._op_getTheT = IcePy.Operation('getTheT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    PlaneInfo._op_setTheT = IcePy.Operation('setTheT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    PlaneInfo._op_getDeltaT = IcePy.Operation('getDeltaT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Time, False, 0), ())
    PlaneInfo._op_setDeltaT = IcePy.Operation('setDeltaT', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Time, False, 0),), (), None, ())
    PlaneInfo._op_getPositionX = IcePy.Operation('getPositionX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    PlaneInfo._op_setPositionX = IcePy.Operation('setPositionX', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    PlaneInfo._op_getPositionY = IcePy.Operation('getPositionY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    PlaneInfo._op_setPositionY = IcePy.Operation('setPositionY', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    PlaneInfo._op_getPositionZ = IcePy.Operation('getPositionZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Length, False, 0), ())
    PlaneInfo._op_setPositionZ = IcePy.Operation('setPositionZ', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Length, False, 0),), (), None, ())
    PlaneInfo._op_getExposureTime = IcePy.Operation('getExposureTime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Time, False, 0), ())
    PlaneInfo._op_setExposureTime = IcePy.Operation('setExposureTime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Time, False, 0),), (), None, ())
    PlaneInfo._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    PlaneInfo._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    PlaneInfo._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlaneInfoAnnotationLinksSeq, False, 0), ())
    PlaneInfo._op_addPlaneInfoAnnotationLink = IcePy.Operation('addPlaneInfoAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfoAnnotationLink, False, 0),), (), None, ())
    PlaneInfo._op_addAllPlaneInfoAnnotationLinkSet = IcePy.Operation('addAllPlaneInfoAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfoAnnotationLinksSeq, False, 0),), (), None, ())
    PlaneInfo._op_removePlaneInfoAnnotationLink = IcePy.Operation('removePlaneInfoAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfoAnnotationLink, False, 0),), (), None, ())
    PlaneInfo._op_removeAllPlaneInfoAnnotationLinkSet = IcePy.Operation('removeAllPlaneInfoAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfoAnnotationLinksSeq, False, 0),), (), None, ())
    PlaneInfo._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    PlaneInfo._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfo, False, 0),), (), None, ())
    PlaneInfo._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    PlaneInfo._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_PlaneInfoAnnotationLink, False, 0), ())
    PlaneInfo._op_addPlaneInfoAnnotationLinkToBoth = IcePy.Operation('addPlaneInfoAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfoAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    PlaneInfo._op_findPlaneInfoAnnotationLink = IcePy.Operation('findPlaneInfoAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_PlaneInfoAnnotationLinksSeq, False, 0), ())
    PlaneInfo._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    PlaneInfo._op_removePlaneInfoAnnotationLinkFromBoth = IcePy.Operation('removePlaneInfoAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PlaneInfoAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    PlaneInfo._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_PlaneInfoLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.PlaneInfo = PlaneInfo
    del PlaneInfo

    _M_ode.model.PlaneInfoPrx = PlaneInfoPrx
    del PlaneInfoPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
