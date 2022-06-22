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

if 'StatsInfo' not in _M_ode.model.__dict__:
    _M_ode.model._t_StatsInfo = IcePy.declareClass('::ode::model::StatsInfo')
    _M_ode.model._t_StatsInfoPrx = IcePy.declareProxy('::ode::model::StatsInfo')

if 'LogicalChannel' not in _M_ode.model.__dict__:
    _M_ode.model._t_LogicalChannel = IcePy.declareClass('::ode::model::LogicalChannel')
    _M_ode.model._t_LogicalChannelPrx = IcePy.declareProxy('::ode::model::LogicalChannel')

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pixels = IcePy.declareClass('::ode::model::Pixels')
    _M_ode.model._t_PixelsPrx = IcePy.declareProxy('::ode::model::Pixels')

if 'ChannelAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChannelAnnotationLink = IcePy.declareClass('::ode::model::ChannelAnnotationLink')
    _M_ode.model._t_ChannelAnnotationLinkPrx = IcePy.declareProxy('::ode::model::ChannelAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_ChannelAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChannelAnnotationLinksSeq = IcePy.defineSequence('::ode::model::ChannelAnnotationLinksSeq', (), _M_ode.model._t_ChannelAnnotationLink)

if '_t_ChannelLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChannelLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::ChannelLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Channel' not in _M_ode.model.__dict__:
    _M_ode.model.Channel = Ice.createTempClass()
    class Channel(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _statsInfo=None, _red=None, _green=None, _blue=None, _alpha=None, _lookupTable=None, _logicalChannel=None, _pixels=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Channel:
                raise RuntimeError('ode.model.Channel is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._statsInfo = _statsInfo
            self._red = _red
            self._green = _green
            self._blue = _blue
            self._alpha = _alpha
            self._lookupTable = _lookupTable
            self._logicalChannel = _logicalChannel
            self._pixels = _pixels
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Channel', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::Channel'

        def ice_staticId():
            return '::ode::model::Channel'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getStatsInfo(self, current=None):
            pass

        def setStatsInfo(self, theStatsInfo, current=None):
            pass

        def getRed(self, current=None):
            pass

        def setRed(self, theRed, current=None):
            pass

        def getGreen(self, current=None):
            pass

        def setGreen(self, theGreen, current=None):
            pass

        def getBlue(self, current=None):
            pass

        def setBlue(self, theBlue, current=None):
            pass

        def getAlpha(self, current=None):
            pass

        def setAlpha(self, theAlpha, current=None):
            pass

        def getLookupTable(self, current=None):
            pass

        def setLookupTable(self, theLookupTable, current=None):
            pass

        def getLogicalChannel(self, current=None):
            pass

        def setLogicalChannel(self, theLogicalChannel, current=None):
            pass

        def getPixels(self, current=None):
            pass

        def setPixels(self, thePixels, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addChannelAnnotationLink(self, target, current=None):
            pass

        def addAllChannelAnnotationLinkSet(self, targets, current=None):
            pass

        def removeChannelAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllChannelAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addChannelAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findChannelAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeChannelAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Channel)

        __repr__ = __str__

    _M_ode.model.ChannelPrx = Ice.createTempClass()
    class ChannelPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Channel._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Channel._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Channel._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Channel._op_setVersion.end(self, _r)

        def getStatsInfo(self, _ctx=None):
            return _M_ode.model.Channel._op_getStatsInfo.invoke(self, ((), _ctx))

        def begin_getStatsInfo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getStatsInfo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStatsInfo(self, _r):
            return _M_ode.model.Channel._op_getStatsInfo.end(self, _r)

        def setStatsInfo(self, theStatsInfo, _ctx=None):
            return _M_ode.model.Channel._op_setStatsInfo.invoke(self, ((theStatsInfo, ), _ctx))

        def begin_setStatsInfo(self, theStatsInfo, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setStatsInfo.begin(self, ((theStatsInfo, ), _response, _ex, _sent, _ctx))

        def end_setStatsInfo(self, _r):
            return _M_ode.model.Channel._op_setStatsInfo.end(self, _r)

        def getRed(self, _ctx=None):
            return _M_ode.model.Channel._op_getRed.invoke(self, ((), _ctx))

        def begin_getRed(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getRed.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRed(self, _r):
            return _M_ode.model.Channel._op_getRed.end(self, _r)

        def setRed(self, theRed, _ctx=None):
            return _M_ode.model.Channel._op_setRed.invoke(self, ((theRed, ), _ctx))

        def begin_setRed(self, theRed, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setRed.begin(self, ((theRed, ), _response, _ex, _sent, _ctx))

        def end_setRed(self, _r):
            return _M_ode.model.Channel._op_setRed.end(self, _r)

        def getGreen(self, _ctx=None):
            return _M_ode.model.Channel._op_getGreen.invoke(self, ((), _ctx))

        def begin_getGreen(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getGreen.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getGreen(self, _r):
            return _M_ode.model.Channel._op_getGreen.end(self, _r)

        def setGreen(self, theGreen, _ctx=None):
            return _M_ode.model.Channel._op_setGreen.invoke(self, ((theGreen, ), _ctx))

        def begin_setGreen(self, theGreen, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setGreen.begin(self, ((theGreen, ), _response, _ex, _sent, _ctx))

        def end_setGreen(self, _r):
            return _M_ode.model.Channel._op_setGreen.end(self, _r)

        def getBlue(self, _ctx=None):
            return _M_ode.model.Channel._op_getBlue.invoke(self, ((), _ctx))

        def begin_getBlue(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getBlue.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getBlue(self, _r):
            return _M_ode.model.Channel._op_getBlue.end(self, _r)

        def setBlue(self, theBlue, _ctx=None):
            return _M_ode.model.Channel._op_setBlue.invoke(self, ((theBlue, ), _ctx))

        def begin_setBlue(self, theBlue, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setBlue.begin(self, ((theBlue, ), _response, _ex, _sent, _ctx))

        def end_setBlue(self, _r):
            return _M_ode.model.Channel._op_setBlue.end(self, _r)

        def getAlpha(self, _ctx=None):
            return _M_ode.model.Channel._op_getAlpha.invoke(self, ((), _ctx))

        def begin_getAlpha(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getAlpha.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAlpha(self, _r):
            return _M_ode.model.Channel._op_getAlpha.end(self, _r)

        def setAlpha(self, theAlpha, _ctx=None):
            return _M_ode.model.Channel._op_setAlpha.invoke(self, ((theAlpha, ), _ctx))

        def begin_setAlpha(self, theAlpha, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setAlpha.begin(self, ((theAlpha, ), _response, _ex, _sent, _ctx))

        def end_setAlpha(self, _r):
            return _M_ode.model.Channel._op_setAlpha.end(self, _r)

        def getLookupTable(self, _ctx=None):
            return _M_ode.model.Channel._op_getLookupTable.invoke(self, ((), _ctx))

        def begin_getLookupTable(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getLookupTable.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLookupTable(self, _r):
            return _M_ode.model.Channel._op_getLookupTable.end(self, _r)

        def setLookupTable(self, theLookupTable, _ctx=None):
            return _M_ode.model.Channel._op_setLookupTable.invoke(self, ((theLookupTable, ), _ctx))

        def begin_setLookupTable(self, theLookupTable, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setLookupTable.begin(self, ((theLookupTable, ), _response, _ex, _sent, _ctx))

        def end_setLookupTable(self, _r):
            return _M_ode.model.Channel._op_setLookupTable.end(self, _r)

        def getLogicalChannel(self, _ctx=None):
            return _M_ode.model.Channel._op_getLogicalChannel.invoke(self, ((), _ctx))

        def begin_getLogicalChannel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getLogicalChannel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getLogicalChannel(self, _r):
            return _M_ode.model.Channel._op_getLogicalChannel.end(self, _r)

        def setLogicalChannel(self, theLogicalChannel, _ctx=None):
            return _M_ode.model.Channel._op_setLogicalChannel.invoke(self, ((theLogicalChannel, ), _ctx))

        def begin_setLogicalChannel(self, theLogicalChannel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setLogicalChannel.begin(self, ((theLogicalChannel, ), _response, _ex, _sent, _ctx))

        def end_setLogicalChannel(self, _r):
            return _M_ode.model.Channel._op_setLogicalChannel.end(self, _r)

        def getPixels(self, _ctx=None):
            return _M_ode.model.Channel._op_getPixels.invoke(self, ((), _ctx))

        def begin_getPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixels(self, _r):
            return _M_ode.model.Channel._op_getPixels.end(self, _r)

        def setPixels(self, thePixels, _ctx=None):
            return _M_ode.model.Channel._op_setPixels.invoke(self, ((thePixels, ), _ctx))

        def begin_setPixels(self, thePixels, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_setPixels.begin(self, ((thePixels, ), _response, _ex, _sent, _ctx))

        def end_setPixels(self, _r):
            return _M_ode.model.Channel._op_setPixels.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Channel._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Channel._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Channel._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Channel._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Channel._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Channel._op_copyAnnotationLinks.end(self, _r)

        def addChannelAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Channel._op_addChannelAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addChannelAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_addChannelAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addChannelAnnotationLink(self, _r):
            return _M_ode.model.Channel._op_addChannelAnnotationLink.end(self, _r)

        def addAllChannelAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Channel._op_addAllChannelAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllChannelAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_addAllChannelAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllChannelAnnotationLinkSet(self, _r):
            return _M_ode.model.Channel._op_addAllChannelAnnotationLinkSet.end(self, _r)

        def removeChannelAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Channel._op_removeChannelAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeChannelAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_removeChannelAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeChannelAnnotationLink(self, _r):
            return _M_ode.model.Channel._op_removeChannelAnnotationLink.end(self, _r)

        def removeAllChannelAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Channel._op_removeAllChannelAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllChannelAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_removeAllChannelAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllChannelAnnotationLinkSet(self, _r):
            return _M_ode.model.Channel._op_removeAllChannelAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Channel._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Channel._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Channel._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Channel._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Channel._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Channel._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Channel._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Channel._op_linkAnnotation.end(self, _r)

        def addChannelAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Channel._op_addChannelAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addChannelAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_addChannelAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addChannelAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Channel._op_addChannelAnnotationLinkToBoth.end(self, _r)

        def findChannelAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Channel._op_findChannelAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findChannelAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_findChannelAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findChannelAnnotationLink(self, _r):
            return _M_ode.model.Channel._op_findChannelAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Channel._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Channel._op_unlinkAnnotation.end(self, _r)

        def removeChannelAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Channel._op_removeChannelAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeChannelAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_removeChannelAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeChannelAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Channel._op_removeChannelAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Channel._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Channel._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Channel._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ChannelPrx.ice_checkedCast(proxy, '::ode::model::Channel', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ChannelPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Channel'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ChannelPrx = IcePy.defineProxy('::ode::model::Channel', ChannelPrx)

    _M_ode.model._t_Channel = IcePy.declareClass('::ode::model::Channel')

    _M_ode.model._t_Channel = IcePy.defineClass('::ode::model::Channel', Channel, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_statsInfo', (), _M_ode.model._t_StatsInfo, False, 0),
        ('_red', (), _M_ode._t_RInt, False, 0),
        ('_green', (), _M_ode._t_RInt, False, 0),
        ('_blue', (), _M_ode._t_RInt, False, 0),
        ('_alpha', (), _M_ode._t_RInt, False, 0),
        ('_lookupTable', (), _M_ode._t_RString, False, 0),
        ('_logicalChannel', (), _M_ode.model._t_LogicalChannel, False, 0),
        ('_pixels', (), _M_ode.model._t_Pixels, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_ChannelAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Channel._ice_type = _M_ode.model._t_Channel

    Channel._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Channel._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Channel._op_getStatsInfo = IcePy.Operation('getStatsInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_StatsInfo, False, 0), ())
    Channel._op_setStatsInfo = IcePy.Operation('setStatsInfo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_StatsInfo, False, 0),), (), None, ())
    Channel._op_getRed = IcePy.Operation('getRed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Channel._op_setRed = IcePy.Operation('setRed', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Channel._op_getGreen = IcePy.Operation('getGreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Channel._op_setGreen = IcePy.Operation('setGreen', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Channel._op_getBlue = IcePy.Operation('getBlue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Channel._op_setBlue = IcePy.Operation('setBlue', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Channel._op_getAlpha = IcePy.Operation('getAlpha', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Channel._op_setAlpha = IcePy.Operation('setAlpha', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Channel._op_getLookupTable = IcePy.Operation('getLookupTable', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Channel._op_setLookupTable = IcePy.Operation('setLookupTable', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Channel._op_getLogicalChannel = IcePy.Operation('getLogicalChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_LogicalChannel, False, 0), ())
    Channel._op_setLogicalChannel = IcePy.Operation('setLogicalChannel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_LogicalChannel, False, 0),), (), None, ())
    Channel._op_getPixels = IcePy.Operation('getPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Pixels, False, 0), ())
    Channel._op_setPixels = IcePy.Operation('setPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    Channel._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Channel._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Channel._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ChannelAnnotationLinksSeq, False, 0), ())
    Channel._op_addChannelAnnotationLink = IcePy.Operation('addChannelAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelAnnotationLink, False, 0),), (), None, ())
    Channel._op_addAllChannelAnnotationLinkSet = IcePy.Operation('addAllChannelAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelAnnotationLinksSeq, False, 0),), (), None, ())
    Channel._op_removeChannelAnnotationLink = IcePy.Operation('removeChannelAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelAnnotationLink, False, 0),), (), None, ())
    Channel._op_removeAllChannelAnnotationLinkSet = IcePy.Operation('removeAllChannelAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelAnnotationLinksSeq, False, 0),), (), None, ())
    Channel._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Channel._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Channel, False, 0),), (), None, ())
    Channel._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Channel._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ChannelAnnotationLink, False, 0), ())
    Channel._op_addChannelAnnotationLinkToBoth = IcePy.Operation('addChannelAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Channel._op_findChannelAnnotationLink = IcePy.Operation('findChannelAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ChannelAnnotationLinksSeq, False, 0), ())
    Channel._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Channel._op_removeChannelAnnotationLinkFromBoth = IcePy.Operation('removeChannelAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChannelAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Channel._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ChannelLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Channel = Channel
    del Channel

    _M_ode.model.ChannelPrx = ChannelPrx
    del ChannelPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
