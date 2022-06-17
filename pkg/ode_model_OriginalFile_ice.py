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

if 'PixelsOriginalFileMap' not in _M_ode.model.__dict__:
    _M_ode.model._t_PixelsOriginalFileMap = IcePy.declareClass('::ode::model::PixelsOriginalFileMap')
    _M_ode.model._t_PixelsOriginalFileMapPrx = IcePy.declareProxy('::ode::model::PixelsOriginalFileMap')

if 'Pixels' not in _M_ode.model.__dict__:
    _M_ode.model._t_Pixels = IcePy.declareClass('::ode::model::Pixels')
    _M_ode.model._t_PixelsPrx = IcePy.declareProxy('::ode::model::Pixels')

if 'ChecksumAlgorithm' not in _M_ode.model.__dict__:
    _M_ode.model._t_ChecksumAlgorithm = IcePy.declareClass('::ode::model::ChecksumAlgorithm')
    _M_ode.model._t_ChecksumAlgorithmPrx = IcePy.declareProxy('::ode::model::ChecksumAlgorithm')

if 'FilesetEntry' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetEntry = IcePy.declareClass('::ode::model::FilesetEntry')
    _M_ode.model._t_FilesetEntryPrx = IcePy.declareProxy('::ode::model::FilesetEntry')

if 'OriginalFileAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFileAnnotationLink = IcePy.declareClass('::ode::model::OriginalFileAnnotationLink')
    _M_ode.model._t_OriginalFileAnnotationLinkPrx = IcePy.declareProxy('::ode::model::OriginalFileAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_OriginalFilePixelsFileMapsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFilePixelsFileMapsSeq = IcePy.defineSequence('::ode::model::OriginalFilePixelsFileMapsSeq', (), _M_ode.model._t_PixelsOriginalFileMap)

if '_t_OriginalFileLinkedPixelsSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFileLinkedPixelsSeq = IcePy.defineSequence('::ode::model::OriginalFileLinkedPixelsSeq', (), _M_ode.model._t_Pixels)

if '_t_OriginalFileFilesetEntriesSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFileFilesetEntriesSeq = IcePy.defineSequence('::ode::model::OriginalFileFilesetEntriesSeq', (), _M_ode.model._t_FilesetEntry)

if '_t_OriginalFileAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFileAnnotationLinksSeq = IcePy.defineSequence('::ode::model::OriginalFileAnnotationLinksSeq', (), _M_ode.model._t_OriginalFileAnnotationLink)

if '_t_OriginalFileLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFileLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::OriginalFileLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'OriginalFile' not in _M_ode.model.__dict__:
    _M_ode.model.OriginalFile = Ice.createTempClass()
    class OriginalFile(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _pixelsFileMapsSeq=None, _pixelsFileMapsLoaded=False, _pixelsFileMapsCountPerOwner=None, _path=None, _repo=None, _size=None, _atime=None, _mtime=None, _ctime=None, _hasher=None, _hash=None, _mimetype=None, _filesetEntriesSeq=None, _filesetEntriesLoaded=False, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None):
            if Ice.getType(self) == _M_ode.model.OriginalFile:
                raise RuntimeError('ode.model.OriginalFile is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._pixelsFileMapsSeq = _pixelsFileMapsSeq
            self._pixelsFileMapsLoaded = _pixelsFileMapsLoaded
            self._pixelsFileMapsCountPerOwner = _pixelsFileMapsCountPerOwner
            self._path = _path
            self._repo = _repo
            self._size = _size
            self._atime = _atime
            self._mtime = _mtime
            self._ctime = _ctime
            self._hasher = _hasher
            self._hash = _hash
            self._mimetype = _mimetype
            self._filesetEntriesSeq = _filesetEntriesSeq
            self._filesetEntriesLoaded = _filesetEntriesLoaded
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::OriginalFile')

        def ice_id(self, current=None):
            return '::ode::model::OriginalFile'

        def ice_staticId():
            return '::ode::model::OriginalFile'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
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

        def linkPixels(self, addition, current=None):
            pass

        def addPixelsOriginalFileMapToBoth(self, link, bothSides, current=None):
            pass

        def findPixelsOriginalFileMap(self, removal, current=None):
            pass

        def unlinkPixels(self, removal, current=None):
            pass

        def removePixelsOriginalFileMapFromBoth(self, link, bothSides, current=None):
            pass

        def linkedPixelsList(self, current=None):
            pass

        def getPath(self, current=None):
            pass

        def setPath(self, thePath, current=None):
            pass

        def getRepo(self, current=None):
            pass

        def setRepo(self, theRepo, current=None):
            pass

        def getSize(self, current=None):
            pass

        def setSize(self, theSize, current=None):
            pass

        def getAtime(self, current=None):
            pass

        def setAtime(self, theAtime, current=None):
            pass

        def getMtime(self, current=None):
            pass

        def setMtime(self, theMtime, current=None):
            pass

        def getCtime(self, current=None):
            pass

        def setCtime(self, theCtime, current=None):
            pass

        def getHasher(self, current=None):
            pass

        def setHasher(self, theHasher, current=None):
            pass

        def getHash(self, current=None):
            pass

        def setHash(self, theHash, current=None):
            pass

        def getMimetype(self, current=None):
            pass

        def setMimetype(self, theMimetype, current=None):
            pass

        def unloadFilesetEntries(self, current=None):
            pass

        def sizeOfFilesetEntries(self, current=None):
            pass

        def copyFilesetEntries(self, current=None):
            pass

        def addFilesetEntry(self, target, current=None):
            pass

        def addAllFilesetEntrySet(self, targets, current=None):
            pass

        def removeFilesetEntry(self, theTarget, current=None):
            pass

        def removeAllFilesetEntrySet(self, targets, current=None):
            pass

        def clearFilesetEntries(self, current=None):
            pass

        def reloadFilesetEntries(self, toCopy, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addOriginalFileAnnotationLink(self, target, current=None):
            pass

        def addAllOriginalFileAnnotationLinkSet(self, targets, current=None):
            pass

        def removeOriginalFileAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllOriginalFileAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addOriginalFileAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findOriginalFileAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeOriginalFileAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_OriginalFile)

        __repr__ = __str__

    _M_ode.model.OriginalFilePrx = Ice.createTempClass()
    class OriginalFilePrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.OriginalFile._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.OriginalFile._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.OriginalFile._op_setVersion.end(self, _r)

        def unloadPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_unloadPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_unloadPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_unloadPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadPixelsFileMaps(self, _r):
            return _M_ode.model.OriginalFile._op_unloadPixelsFileMaps.end(self, _r)

        def sizeOfPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_sizeOfPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_sizeOfPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_sizeOfPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfPixelsFileMaps(self, _r):
            return _M_ode.model.OriginalFile._op_sizeOfPixelsFileMaps.end(self, _r)

        def copyPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_copyPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_copyPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_copyPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyPixelsFileMaps(self, _r):
            return _M_ode.model.OriginalFile._op_copyPixelsFileMaps.end(self, _r)

        def addPixelsOriginalFileMap(self, target, _ctx=None):
            return _M_ode.model.OriginalFile._op_addPixelsOriginalFileMap.invoke(self, ((target, ), _ctx))

        def begin_addPixelsOriginalFileMap(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addPixelsOriginalFileMap.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPixelsOriginalFileMap(self, _r):
            return _M_ode.model.OriginalFile._op_addPixelsOriginalFileMap.end(self, _r)

        def addAllPixelsOriginalFileMapSet(self, targets, _ctx=None):
            return _M_ode.model.OriginalFile._op_addAllPixelsOriginalFileMapSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPixelsOriginalFileMapSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addAllPixelsOriginalFileMapSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPixelsOriginalFileMapSet(self, _r):
            return _M_ode.model.OriginalFile._op_addAllPixelsOriginalFileMapSet.end(self, _r)

        def removePixelsOriginalFileMap(self, theTarget, _ctx=None):
            return _M_ode.model.OriginalFile._op_removePixelsOriginalFileMap.invoke(self, ((theTarget, ), _ctx))

        def begin_removePixelsOriginalFileMap(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removePixelsOriginalFileMap.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePixelsOriginalFileMap(self, _r):
            return _M_ode.model.OriginalFile._op_removePixelsOriginalFileMap.end(self, _r)

        def removeAllPixelsOriginalFileMapSet(self, targets, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeAllPixelsOriginalFileMapSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPixelsOriginalFileMapSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeAllPixelsOriginalFileMapSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPixelsOriginalFileMapSet(self, _r):
            return _M_ode.model.OriginalFile._op_removeAllPixelsOriginalFileMapSet.end(self, _r)

        def clearPixelsFileMaps(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_clearPixelsFileMaps.invoke(self, ((), _ctx))

        def begin_clearPixelsFileMaps(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_clearPixelsFileMaps.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearPixelsFileMaps(self, _r):
            return _M_ode.model.OriginalFile._op_clearPixelsFileMaps.end(self, _r)

        def reloadPixelsFileMaps(self, toCopy, _ctx=None):
            return _M_ode.model.OriginalFile._op_reloadPixelsFileMaps.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadPixelsFileMaps(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_reloadPixelsFileMaps.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadPixelsFileMaps(self, _r):
            return _M_ode.model.OriginalFile._op_reloadPixelsFileMaps.end(self, _r)

        def getPixelsFileMapsCountPerOwner(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getPixelsFileMapsCountPerOwner.invoke(self, ((), _ctx))

        def begin_getPixelsFileMapsCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getPixelsFileMapsCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPixelsFileMapsCountPerOwner(self, _r):
            return _M_ode.model.OriginalFile._op_getPixelsFileMapsCountPerOwner.end(self, _r)

        def linkPixels(self, addition, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkPixels.invoke(self, ((addition, ), _ctx))

        def begin_linkPixels(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkPixels.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkPixels(self, _r):
            return _M_ode.model.OriginalFile._op_linkPixels.end(self, _r)

        def addPixelsOriginalFileMapToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.OriginalFile._op_addPixelsOriginalFileMapToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addPixelsOriginalFileMapToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addPixelsOriginalFileMapToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addPixelsOriginalFileMapToBoth(self, _r):
            return _M_ode.model.OriginalFile._op_addPixelsOriginalFileMapToBoth.end(self, _r)

        def findPixelsOriginalFileMap(self, removal, _ctx=None):
            return _M_ode.model.OriginalFile._op_findPixelsOriginalFileMap.invoke(self, ((removal, ), _ctx))

        def begin_findPixelsOriginalFileMap(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_findPixelsOriginalFileMap.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findPixelsOriginalFileMap(self, _r):
            return _M_ode.model.OriginalFile._op_findPixelsOriginalFileMap.end(self, _r)

        def unlinkPixels(self, removal, _ctx=None):
            return _M_ode.model.OriginalFile._op_unlinkPixels.invoke(self, ((removal, ), _ctx))

        def begin_unlinkPixels(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_unlinkPixels.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkPixels(self, _r):
            return _M_ode.model.OriginalFile._op_unlinkPixels.end(self, _r)

        def removePixelsOriginalFileMapFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.OriginalFile._op_removePixelsOriginalFileMapFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removePixelsOriginalFileMapFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removePixelsOriginalFileMapFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removePixelsOriginalFileMapFromBoth(self, _r):
            return _M_ode.model.OriginalFile._op_removePixelsOriginalFileMapFromBoth.end(self, _r)

        def linkedPixelsList(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkedPixelsList.invoke(self, ((), _ctx))

        def begin_linkedPixelsList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkedPixelsList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedPixelsList(self, _r):
            return _M_ode.model.OriginalFile._op_linkedPixelsList.end(self, _r)

        def getPath(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getPath.invoke(self, ((), _ctx))

        def begin_getPath(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getPath.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPath(self, _r):
            return _M_ode.model.OriginalFile._op_getPath.end(self, _r)

        def setPath(self, thePath, _ctx=None):
            return _M_ode.model.OriginalFile._op_setPath.invoke(self, ((thePath, ), _ctx))

        def begin_setPath(self, thePath, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setPath.begin(self, ((thePath, ), _response, _ex, _sent, _ctx))

        def end_setPath(self, _r):
            return _M_ode.model.OriginalFile._op_setPath.end(self, _r)

        def getRepo(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getRepo.invoke(self, ((), _ctx))

        def begin_getRepo(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getRepo.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRepo(self, _r):
            return _M_ode.model.OriginalFile._op_getRepo.end(self, _r)

        def setRepo(self, theRepo, _ctx=None):
            return _M_ode.model.OriginalFile._op_setRepo.invoke(self, ((theRepo, ), _ctx))

        def begin_setRepo(self, theRepo, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setRepo.begin(self, ((theRepo, ), _response, _ex, _sent, _ctx))

        def end_setRepo(self, _r):
            return _M_ode.model.OriginalFile._op_setRepo.end(self, _r)

        def getSize(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getSize.invoke(self, ((), _ctx))

        def begin_getSize(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getSize.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSize(self, _r):
            return _M_ode.model.OriginalFile._op_getSize.end(self, _r)

        def setSize(self, theSize, _ctx=None):
            return _M_ode.model.OriginalFile._op_setSize.invoke(self, ((theSize, ), _ctx))

        def begin_setSize(self, theSize, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setSize.begin(self, ((theSize, ), _response, _ex, _sent, _ctx))

        def end_setSize(self, _r):
            return _M_ode.model.OriginalFile._op_setSize.end(self, _r)

        def getAtime(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getAtime.invoke(self, ((), _ctx))

        def begin_getAtime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getAtime.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAtime(self, _r):
            return _M_ode.model.OriginalFile._op_getAtime.end(self, _r)

        def setAtime(self, theAtime, _ctx=None):
            return _M_ode.model.OriginalFile._op_setAtime.invoke(self, ((theAtime, ), _ctx))

        def begin_setAtime(self, theAtime, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setAtime.begin(self, ((theAtime, ), _response, _ex, _sent, _ctx))

        def end_setAtime(self, _r):
            return _M_ode.model.OriginalFile._op_setAtime.end(self, _r)

        def getMtime(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getMtime.invoke(self, ((), _ctx))

        def begin_getMtime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getMtime.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMtime(self, _r):
            return _M_ode.model.OriginalFile._op_getMtime.end(self, _r)

        def setMtime(self, theMtime, _ctx=None):
            return _M_ode.model.OriginalFile._op_setMtime.invoke(self, ((theMtime, ), _ctx))

        def begin_setMtime(self, theMtime, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setMtime.begin(self, ((theMtime, ), _response, _ex, _sent, _ctx))

        def end_setMtime(self, _r):
            return _M_ode.model.OriginalFile._op_setMtime.end(self, _r)

        def getCtime(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getCtime.invoke(self, ((), _ctx))

        def begin_getCtime(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getCtime.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getCtime(self, _r):
            return _M_ode.model.OriginalFile._op_getCtime.end(self, _r)

        def setCtime(self, theCtime, _ctx=None):
            return _M_ode.model.OriginalFile._op_setCtime.invoke(self, ((theCtime, ), _ctx))

        def begin_setCtime(self, theCtime, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setCtime.begin(self, ((theCtime, ), _response, _ex, _sent, _ctx))

        def end_setCtime(self, _r):
            return _M_ode.model.OriginalFile._op_setCtime.end(self, _r)

        def getHasher(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getHasher.invoke(self, ((), _ctx))

        def begin_getHasher(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getHasher.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getHasher(self, _r):
            return _M_ode.model.OriginalFile._op_getHasher.end(self, _r)

        def setHasher(self, theHasher, _ctx=None):
            return _M_ode.model.OriginalFile._op_setHasher.invoke(self, ((theHasher, ), _ctx))

        def begin_setHasher(self, theHasher, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setHasher.begin(self, ((theHasher, ), _response, _ex, _sent, _ctx))

        def end_setHasher(self, _r):
            return _M_ode.model.OriginalFile._op_setHasher.end(self, _r)

        def getHash(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getHash.invoke(self, ((), _ctx))

        def begin_getHash(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getHash.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getHash(self, _r):
            return _M_ode.model.OriginalFile._op_getHash.end(self, _r)

        def setHash(self, theHash, _ctx=None):
            return _M_ode.model.OriginalFile._op_setHash.invoke(self, ((theHash, ), _ctx))

        def begin_setHash(self, theHash, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setHash.begin(self, ((theHash, ), _response, _ex, _sent, _ctx))

        def end_setHash(self, _r):
            return _M_ode.model.OriginalFile._op_setHash.end(self, _r)

        def getMimetype(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getMimetype.invoke(self, ((), _ctx))

        def begin_getMimetype(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getMimetype.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getMimetype(self, _r):
            return _M_ode.model.OriginalFile._op_getMimetype.end(self, _r)

        def setMimetype(self, theMimetype, _ctx=None):
            return _M_ode.model.OriginalFile._op_setMimetype.invoke(self, ((theMimetype, ), _ctx))

        def begin_setMimetype(self, theMimetype, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setMimetype.begin(self, ((theMimetype, ), _response, _ex, _sent, _ctx))

        def end_setMimetype(self, _r):
            return _M_ode.model.OriginalFile._op_setMimetype.end(self, _r)

        def unloadFilesetEntries(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_unloadFilesetEntries.invoke(self, ((), _ctx))

        def begin_unloadFilesetEntries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_unloadFilesetEntries.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadFilesetEntries(self, _r):
            return _M_ode.model.OriginalFile._op_unloadFilesetEntries.end(self, _r)

        def sizeOfFilesetEntries(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_sizeOfFilesetEntries.invoke(self, ((), _ctx))

        def begin_sizeOfFilesetEntries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_sizeOfFilesetEntries.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfFilesetEntries(self, _r):
            return _M_ode.model.OriginalFile._op_sizeOfFilesetEntries.end(self, _r)

        def copyFilesetEntries(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_copyFilesetEntries.invoke(self, ((), _ctx))

        def begin_copyFilesetEntries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_copyFilesetEntries.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyFilesetEntries(self, _r):
            return _M_ode.model.OriginalFile._op_copyFilesetEntries.end(self, _r)

        def addFilesetEntry(self, target, _ctx=None):
            return _M_ode.model.OriginalFile._op_addFilesetEntry.invoke(self, ((target, ), _ctx))

        def begin_addFilesetEntry(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addFilesetEntry.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilesetEntry(self, _r):
            return _M_ode.model.OriginalFile._op_addFilesetEntry.end(self, _r)

        def addAllFilesetEntrySet(self, targets, _ctx=None):
            return _M_ode.model.OriginalFile._op_addAllFilesetEntrySet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilesetEntrySet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addAllFilesetEntrySet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilesetEntrySet(self, _r):
            return _M_ode.model.OriginalFile._op_addAllFilesetEntrySet.end(self, _r)

        def removeFilesetEntry(self, theTarget, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeFilesetEntry.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilesetEntry(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeFilesetEntry.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilesetEntry(self, _r):
            return _M_ode.model.OriginalFile._op_removeFilesetEntry.end(self, _r)

        def removeAllFilesetEntrySet(self, targets, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeAllFilesetEntrySet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilesetEntrySet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeAllFilesetEntrySet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilesetEntrySet(self, _r):
            return _M_ode.model.OriginalFile._op_removeAllFilesetEntrySet.end(self, _r)

        def clearFilesetEntries(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_clearFilesetEntries.invoke(self, ((), _ctx))

        def begin_clearFilesetEntries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_clearFilesetEntries.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearFilesetEntries(self, _r):
            return _M_ode.model.OriginalFile._op_clearFilesetEntries.end(self, _r)

        def reloadFilesetEntries(self, toCopy, _ctx=None):
            return _M_ode.model.OriginalFile._op_reloadFilesetEntries.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadFilesetEntries(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_reloadFilesetEntries.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadFilesetEntries(self, _r):
            return _M_ode.model.OriginalFile._op_reloadFilesetEntries.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.OriginalFile._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.OriginalFile._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.OriginalFile._op_copyAnnotationLinks.end(self, _r)

        def addOriginalFileAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.OriginalFile._op_addOriginalFileAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addOriginalFileAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addOriginalFileAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addOriginalFileAnnotationLink(self, _r):
            return _M_ode.model.OriginalFile._op_addOriginalFileAnnotationLink.end(self, _r)

        def addAllOriginalFileAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.OriginalFile._op_addAllOriginalFileAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllOriginalFileAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addAllOriginalFileAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllOriginalFileAnnotationLinkSet(self, _r):
            return _M_ode.model.OriginalFile._op_addAllOriginalFileAnnotationLinkSet.end(self, _r)

        def removeOriginalFileAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeOriginalFileAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeOriginalFileAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeOriginalFileAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeOriginalFileAnnotationLink(self, _r):
            return _M_ode.model.OriginalFile._op_removeOriginalFileAnnotationLink.end(self, _r)

        def removeAllOriginalFileAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeAllOriginalFileAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllOriginalFileAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeAllOriginalFileAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllOriginalFileAnnotationLinkSet(self, _r):
            return _M_ode.model.OriginalFile._op_removeAllOriginalFileAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.OriginalFile._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.OriginalFile._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.OriginalFile._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.OriginalFile._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.OriginalFile._op_linkAnnotation.end(self, _r)

        def addOriginalFileAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.OriginalFile._op_addOriginalFileAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addOriginalFileAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_addOriginalFileAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addOriginalFileAnnotationLinkToBoth(self, _r):
            return _M_ode.model.OriginalFile._op_addOriginalFileAnnotationLinkToBoth.end(self, _r)

        def findOriginalFileAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.OriginalFile._op_findOriginalFileAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findOriginalFileAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_findOriginalFileAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findOriginalFileAnnotationLink(self, _r):
            return _M_ode.model.OriginalFile._op_findOriginalFileAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.OriginalFile._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.OriginalFile._op_unlinkAnnotation.end(self, _r)

        def removeOriginalFileAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeOriginalFileAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeOriginalFileAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_removeOriginalFileAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeOriginalFileAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.OriginalFile._op_removeOriginalFileAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.OriginalFile._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.OriginalFile._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.OriginalFile._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.OriginalFile._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.OriginalFile._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.OriginalFile._op_setName.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.OriginalFilePrx.ice_checkedCast(proxy, '::ode::model::OriginalFile', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.OriginalFilePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::OriginalFile'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_OriginalFilePrx = IcePy.defineProxy('::ode::model::OriginalFile', OriginalFilePrx)

    _M_ode.model._t_OriginalFile = IcePy.declareClass('::ode::model::OriginalFile')

    _M_ode.model._t_OriginalFile = IcePy.defineClass('::ode::model::OriginalFile', OriginalFile, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_pixelsFileMapsSeq', (), _M_ode.model._t_OriginalFilePixelsFileMapsSeq, False, 0),
        ('_pixelsFileMapsLoaded', (), IcePy._t_bool, False, 0),
        ('_pixelsFileMapsCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_path', (), _M_ode._t_RString, False, 0),
        ('_repo', (), _M_ode._t_RString, False, 0),
        ('_size', (), _M_ode._t_RLong, False, 0),
        ('_atime', (), _M_ode._t_RTime, False, 0),
        ('_mtime', (), _M_ode._t_RTime, False, 0),
        ('_ctime', (), _M_ode._t_RTime, False, 0),
        ('_hasher', (), _M_ode.model._t_ChecksumAlgorithm, False, 0),
        ('_hash', (), _M_ode._t_RString, False, 0),
        ('_mimetype', (), _M_ode._t_RString, False, 0),
        ('_filesetEntriesSeq', (), _M_ode.model._t_OriginalFileFilesetEntriesSeq, False, 0),
        ('_filesetEntriesLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_OriginalFileAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0)
    ))
    OriginalFile._ice_type = _M_ode.model._t_OriginalFile

    OriginalFile._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    OriginalFile._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    OriginalFile._op_unloadPixelsFileMaps = IcePy.Operation('unloadPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    OriginalFile._op_sizeOfPixelsFileMaps = IcePy.Operation('sizeOfPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    OriginalFile._op_copyPixelsFileMaps = IcePy.Operation('copyPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFilePixelsFileMapsSeq, False, 0), ())
    OriginalFile._op_addPixelsOriginalFileMap = IcePy.Operation('addPixelsOriginalFileMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0),), (), None, ())
    OriginalFile._op_addAllPixelsOriginalFileMapSet = IcePy.Operation('addAllPixelsOriginalFileMapSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFilePixelsFileMapsSeq, False, 0),), (), None, ())
    OriginalFile._op_removePixelsOriginalFileMap = IcePy.Operation('removePixelsOriginalFileMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0),), (), None, ())
    OriginalFile._op_removeAllPixelsOriginalFileMapSet = IcePy.Operation('removeAllPixelsOriginalFileMapSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFilePixelsFileMapsSeq, False, 0),), (), None, ())
    OriginalFile._op_clearPixelsFileMaps = IcePy.Operation('clearPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    OriginalFile._op_reloadPixelsFileMaps = IcePy.Operation('reloadPixelsFileMaps', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())
    OriginalFile._op_getPixelsFileMapsCountPerOwner = IcePy.Operation('getPixelsFileMapsCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    OriginalFile._op_linkPixels = IcePy.Operation('linkPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), ((), _M_ode.model._t_PixelsOriginalFileMap, False, 0), ())
    OriginalFile._op_addPixelsOriginalFileMapToBoth = IcePy.Operation('addPixelsOriginalFileMapToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    OriginalFile._op_findPixelsOriginalFileMap = IcePy.Operation('findPixelsOriginalFileMap', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), ((), _M_ode.model._t_OriginalFilePixelsFileMapsSeq, False, 0), ())
    OriginalFile._op_unlinkPixels = IcePy.Operation('unlinkPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Pixels, False, 0),), (), None, ())
    OriginalFile._op_removePixelsOriginalFileMapFromBoth = IcePy.Operation('removePixelsOriginalFileMapFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_PixelsOriginalFileMap, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    OriginalFile._op_linkedPixelsList = IcePy.Operation('linkedPixelsList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFileLinkedPixelsSeq, False, 0), ())
    OriginalFile._op_getPath = IcePy.Operation('getPath', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    OriginalFile._op_setPath = IcePy.Operation('setPath', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    OriginalFile._op_getRepo = IcePy.Operation('getRepo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    OriginalFile._op_setRepo = IcePy.Operation('setRepo', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    OriginalFile._op_getSize = IcePy.Operation('getSize', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RLong, False, 0), ())
    OriginalFile._op_setSize = IcePy.Operation('setSize', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RLong, False, 0),), (), None, ())
    OriginalFile._op_getAtime = IcePy.Operation('getAtime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    OriginalFile._op_setAtime = IcePy.Operation('setAtime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    OriginalFile._op_getMtime = IcePy.Operation('getMtime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    OriginalFile._op_setMtime = IcePy.Operation('setMtime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    OriginalFile._op_getCtime = IcePy.Operation('getCtime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RTime, False, 0), ())
    OriginalFile._op_setCtime = IcePy.Operation('setCtime', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RTime, False, 0),), (), None, ())
    OriginalFile._op_getHasher = IcePy.Operation('getHasher', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ChecksumAlgorithm, False, 0), ())
    OriginalFile._op_setHasher = IcePy.Operation('setHasher', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChecksumAlgorithm, False, 0),), (), None, ())
    OriginalFile._op_getHash = IcePy.Operation('getHash', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    OriginalFile._op_setHash = IcePy.Operation('setHash', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    OriginalFile._op_getMimetype = IcePy.Operation('getMimetype', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    OriginalFile._op_setMimetype = IcePy.Operation('setMimetype', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    OriginalFile._op_unloadFilesetEntries = IcePy.Operation('unloadFilesetEntries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    OriginalFile._op_sizeOfFilesetEntries = IcePy.Operation('sizeOfFilesetEntries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    OriginalFile._op_copyFilesetEntries = IcePy.Operation('copyFilesetEntries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFileFilesetEntriesSeq, False, 0), ())
    OriginalFile._op_addFilesetEntry = IcePy.Operation('addFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetEntry, False, 0),), (), None, ())
    OriginalFile._op_addAllFilesetEntrySet = IcePy.Operation('addAllFilesetEntrySet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileFilesetEntriesSeq, False, 0),), (), None, ())
    OriginalFile._op_removeFilesetEntry = IcePy.Operation('removeFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetEntry, False, 0),), (), None, ())
    OriginalFile._op_removeAllFilesetEntrySet = IcePy.Operation('removeAllFilesetEntrySet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileFilesetEntriesSeq, False, 0),), (), None, ())
    OriginalFile._op_clearFilesetEntries = IcePy.Operation('clearFilesetEntries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    OriginalFile._op_reloadFilesetEntries = IcePy.Operation('reloadFilesetEntries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())
    OriginalFile._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    OriginalFile._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    OriginalFile._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFileAnnotationLinksSeq, False, 0), ())
    OriginalFile._op_addOriginalFileAnnotationLink = IcePy.Operation('addOriginalFileAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileAnnotationLink, False, 0),), (), None, ())
    OriginalFile._op_addAllOriginalFileAnnotationLinkSet = IcePy.Operation('addAllOriginalFileAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileAnnotationLinksSeq, False, 0),), (), None, ())
    OriginalFile._op_removeOriginalFileAnnotationLink = IcePy.Operation('removeOriginalFileAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileAnnotationLink, False, 0),), (), None, ())
    OriginalFile._op_removeAllOriginalFileAnnotationLinkSet = IcePy.Operation('removeAllOriginalFileAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileAnnotationLinksSeq, False, 0),), (), None, ())
    OriginalFile._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    OriginalFile._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())
    OriginalFile._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    OriginalFile._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_OriginalFileAnnotationLink, False, 0), ())
    OriginalFile._op_addOriginalFileAnnotationLinkToBoth = IcePy.Operation('addOriginalFileAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    OriginalFile._op_findOriginalFileAnnotationLink = IcePy.Operation('findOriginalFileAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_OriginalFileAnnotationLinksSeq, False, 0), ())
    OriginalFile._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    OriginalFile._op_removeOriginalFileAnnotationLinkFromBoth = IcePy.Operation('removeOriginalFileAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFileAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    OriginalFile._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFileLinkedAnnotationSeq, False, 0), ())
    OriginalFile._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    OriginalFile._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.OriginalFile = OriginalFile
    del OriginalFile

    _M_ode.model.OriginalFilePrx = OriginalFilePrx
    del OriginalFilePrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
