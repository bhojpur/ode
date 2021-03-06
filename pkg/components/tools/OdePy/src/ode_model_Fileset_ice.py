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

if 'FilesetEntry' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetEntry = IcePy.declareClass('::ode::model::FilesetEntry')
    _M_ode.model._t_FilesetEntryPrx = IcePy.declareProxy('::ode::model::FilesetEntry')

if 'Image' not in _M_ode.model.__dict__:
    _M_ode.model._t_Image = IcePy.declareClass('::ode::model::Image')
    _M_ode.model._t_ImagePrx = IcePy.declareProxy('::ode::model::Image')

if 'FilesetJobLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetJobLink = IcePy.declareClass('::ode::model::FilesetJobLink')
    _M_ode.model._t_FilesetJobLinkPrx = IcePy.declareProxy('::ode::model::FilesetJobLink')

if 'Job' not in _M_ode.model.__dict__:
    _M_ode.model._t_Job = IcePy.declareClass('::ode::model::Job')
    _M_ode.model._t_JobPrx = IcePy.declareProxy('::ode::model::Job')

if 'FilesetAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetAnnotationLink = IcePy.declareClass('::ode::model::FilesetAnnotationLink')
    _M_ode.model._t_FilesetAnnotationLinkPrx = IcePy.declareProxy('::ode::model::FilesetAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_FilesetUsedFilesSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetUsedFilesSeq = IcePy.defineSequence('::ode::model::FilesetUsedFilesSeq', (), _M_ode.model._t_FilesetEntry)

if '_t_FilesetImagesSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetImagesSeq = IcePy.defineSequence('::ode::model::FilesetImagesSeq', (), _M_ode.model._t_Image)

if '_t_FilesetJobLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetJobLinksSeq = IcePy.defineSequence('::ode::model::FilesetJobLinksSeq', (), _M_ode.model._t_FilesetJobLink)

if '_t_FilesetLinkedJobSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetLinkedJobSeq = IcePy.defineSequence('::ode::model::FilesetLinkedJobSeq', (), _M_ode.model._t_Job)

if '_t_FilesetAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetAnnotationLinksSeq = IcePy.defineSequence('::ode::model::FilesetAnnotationLinksSeq', (), _M_ode.model._t_FilesetAnnotationLink)

if '_t_FilesetLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_FilesetLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::FilesetLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Fileset' not in _M_ode.model.__dict__:
    _M_ode.model.Fileset = Ice.createTempClass()
    class Fileset(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _usedFilesSeq=None, _usedFilesLoaded=False, _imagesSeq=None, _imagesLoaded=False, _jobLinksSeq=None, _jobLinksLoaded=False, _jobLinksCountPerOwner=None, _templatePrefix=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None):
            if Ice.getType(self) == _M_ode.model.Fileset:
                raise RuntimeError('ode.model.Fileset is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._usedFilesSeq = _usedFilesSeq
            self._usedFilesLoaded = _usedFilesLoaded
            self._imagesSeq = _imagesSeq
            self._imagesLoaded = _imagesLoaded
            self._jobLinksSeq = _jobLinksSeq
            self._jobLinksLoaded = _jobLinksLoaded
            self._jobLinksCountPerOwner = _jobLinksCountPerOwner
            self._templatePrefix = _templatePrefix
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Fileset', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::Fileset'

        def ice_staticId():
            return '::ode::model::Fileset'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def unloadUsedFiles(self, current=None):
            pass

        def sizeOfUsedFiles(self, current=None):
            pass

        def copyUsedFiles(self, current=None):
            pass

        def addFilesetEntry(self, target, current=None):
            pass

        def addAllFilesetEntrySet(self, targets, current=None):
            pass

        def removeFilesetEntry(self, theTarget, current=None):
            pass

        def removeAllFilesetEntrySet(self, targets, current=None):
            pass

        def clearUsedFiles(self, current=None):
            pass

        def reloadUsedFiles(self, toCopy, current=None):
            pass

        def getFilesetEntry(self, index, current=None):
            pass

        def setFilesetEntry(self, index, theElement, current=None):
            pass

        def getPrimaryFilesetEntry(self, current=None):
            pass

        def setPrimaryFilesetEntry(self, theElement, current=None):
            pass

        def unloadImages(self, current=None):
            pass

        def sizeOfImages(self, current=None):
            pass

        def copyImages(self, current=None):
            pass

        def addImage(self, target, current=None):
            pass

        def addAllImageSet(self, targets, current=None):
            pass

        def removeImage(self, theTarget, current=None):
            pass

        def removeAllImageSet(self, targets, current=None):
            pass

        def clearImages(self, current=None):
            pass

        def reloadImages(self, toCopy, current=None):
            pass

        def unloadJobLinks(self, current=None):
            pass

        def sizeOfJobLinks(self, current=None):
            pass

        def copyJobLinks(self, current=None):
            pass

        def addFilesetJobLink(self, target, current=None):
            pass

        def addAllFilesetJobLinkSet(self, targets, current=None):
            pass

        def removeFilesetJobLink(self, theTarget, current=None):
            pass

        def removeAllFilesetJobLinkSet(self, targets, current=None):
            pass

        def clearJobLinks(self, current=None):
            pass

        def reloadJobLinks(self, toCopy, current=None):
            pass

        def getFilesetJobLink(self, index, current=None):
            pass

        def setFilesetJobLink(self, index, theElement, current=None):
            pass

        def getPrimaryFilesetJobLink(self, current=None):
            pass

        def setPrimaryFilesetJobLink(self, theElement, current=None):
            pass

        def getJobLinksCountPerOwner(self, current=None):
            pass

        def linkJob(self, addition, current=None):
            pass

        def addFilesetJobLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFilesetJobLink(self, removal, current=None):
            pass

        def unlinkJob(self, removal, current=None):
            pass

        def removeFilesetJobLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedJobList(self, current=None):
            pass

        def getTemplatePrefix(self, current=None):
            pass

        def setTemplatePrefix(self, theTemplatePrefix, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addFilesetAnnotationLink(self, target, current=None):
            pass

        def addAllFilesetAnnotationLinkSet(self, targets, current=None):
            pass

        def removeFilesetAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllFilesetAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addFilesetAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFilesetAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeFilesetAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Fileset)

        __repr__ = __str__

    _M_ode.model.FilesetPrx = Ice.createTempClass()
    class FilesetPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Fileset._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Fileset._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Fileset._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Fileset._op_setVersion.end(self, _r)

        def unloadUsedFiles(self, _ctx=None):
            return _M_ode.model.Fileset._op_unloadUsedFiles.invoke(self, ((), _ctx))

        def begin_unloadUsedFiles(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_unloadUsedFiles.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadUsedFiles(self, _r):
            return _M_ode.model.Fileset._op_unloadUsedFiles.end(self, _r)

        def sizeOfUsedFiles(self, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfUsedFiles.invoke(self, ((), _ctx))

        def begin_sizeOfUsedFiles(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfUsedFiles.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfUsedFiles(self, _r):
            return _M_ode.model.Fileset._op_sizeOfUsedFiles.end(self, _r)

        def copyUsedFiles(self, _ctx=None):
            return _M_ode.model.Fileset._op_copyUsedFiles.invoke(self, ((), _ctx))

        def begin_copyUsedFiles(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_copyUsedFiles.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyUsedFiles(self, _r):
            return _M_ode.model.Fileset._op_copyUsedFiles.end(self, _r)

        def addFilesetEntry(self, target, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetEntry.invoke(self, ((target, ), _ctx))

        def begin_addFilesetEntry(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetEntry.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilesetEntry(self, _r):
            return _M_ode.model.Fileset._op_addFilesetEntry.end(self, _r)

        def addAllFilesetEntrySet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_addAllFilesetEntrySet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilesetEntrySet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addAllFilesetEntrySet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilesetEntrySet(self, _r):
            return _M_ode.model.Fileset._op_addAllFilesetEntrySet.end(self, _r)

        def removeFilesetEntry(self, theTarget, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetEntry.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilesetEntry(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetEntry.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilesetEntry(self, _r):
            return _M_ode.model.Fileset._op_removeFilesetEntry.end(self, _r)

        def removeAllFilesetEntrySet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllFilesetEntrySet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilesetEntrySet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllFilesetEntrySet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilesetEntrySet(self, _r):
            return _M_ode.model.Fileset._op_removeAllFilesetEntrySet.end(self, _r)

        def clearUsedFiles(self, _ctx=None):
            return _M_ode.model.Fileset._op_clearUsedFiles.invoke(self, ((), _ctx))

        def begin_clearUsedFiles(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_clearUsedFiles.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearUsedFiles(self, _r):
            return _M_ode.model.Fileset._op_clearUsedFiles.end(self, _r)

        def reloadUsedFiles(self, toCopy, _ctx=None):
            return _M_ode.model.Fileset._op_reloadUsedFiles.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadUsedFiles(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_reloadUsedFiles.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadUsedFiles(self, _r):
            return _M_ode.model.Fileset._op_reloadUsedFiles.end(self, _r)

        def getFilesetEntry(self, index, _ctx=None):
            return _M_ode.model.Fileset._op_getFilesetEntry.invoke(self, ((index, ), _ctx))

        def begin_getFilesetEntry(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getFilesetEntry.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getFilesetEntry(self, _r):
            return _M_ode.model.Fileset._op_getFilesetEntry.end(self, _r)

        def setFilesetEntry(self, index, theElement, _ctx=None):
            return _M_ode.model.Fileset._op_setFilesetEntry.invoke(self, ((index, theElement), _ctx))

        def begin_setFilesetEntry(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_setFilesetEntry.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setFilesetEntry(self, _r):
            return _M_ode.model.Fileset._op_setFilesetEntry.end(self, _r)

        def getPrimaryFilesetEntry(self, _ctx=None):
            return _M_ode.model.Fileset._op_getPrimaryFilesetEntry.invoke(self, ((), _ctx))

        def begin_getPrimaryFilesetEntry(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getPrimaryFilesetEntry.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryFilesetEntry(self, _r):
            return _M_ode.model.Fileset._op_getPrimaryFilesetEntry.end(self, _r)

        def setPrimaryFilesetEntry(self, theElement, _ctx=None):
            return _M_ode.model.Fileset._op_setPrimaryFilesetEntry.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryFilesetEntry(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_setPrimaryFilesetEntry.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryFilesetEntry(self, _r):
            return _M_ode.model.Fileset._op_setPrimaryFilesetEntry.end(self, _r)

        def unloadImages(self, _ctx=None):
            return _M_ode.model.Fileset._op_unloadImages.invoke(self, ((), _ctx))

        def begin_unloadImages(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_unloadImages.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadImages(self, _r):
            return _M_ode.model.Fileset._op_unloadImages.end(self, _r)

        def sizeOfImages(self, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfImages.invoke(self, ((), _ctx))

        def begin_sizeOfImages(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfImages.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfImages(self, _r):
            return _M_ode.model.Fileset._op_sizeOfImages.end(self, _r)

        def copyImages(self, _ctx=None):
            return _M_ode.model.Fileset._op_copyImages.invoke(self, ((), _ctx))

        def begin_copyImages(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_copyImages.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyImages(self, _r):
            return _M_ode.model.Fileset._op_copyImages.end(self, _r)

        def addImage(self, target, _ctx=None):
            return _M_ode.model.Fileset._op_addImage.invoke(self, ((target, ), _ctx))

        def begin_addImage(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addImage.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addImage(self, _r):
            return _M_ode.model.Fileset._op_addImage.end(self, _r)

        def addAllImageSet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_addAllImageSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllImageSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addAllImageSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllImageSet(self, _r):
            return _M_ode.model.Fileset._op_addAllImageSet.end(self, _r)

        def removeImage(self, theTarget, _ctx=None):
            return _M_ode.model.Fileset._op_removeImage.invoke(self, ((theTarget, ), _ctx))

        def begin_removeImage(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeImage.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeImage(self, _r):
            return _M_ode.model.Fileset._op_removeImage.end(self, _r)

        def removeAllImageSet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllImageSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllImageSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllImageSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllImageSet(self, _r):
            return _M_ode.model.Fileset._op_removeAllImageSet.end(self, _r)

        def clearImages(self, _ctx=None):
            return _M_ode.model.Fileset._op_clearImages.invoke(self, ((), _ctx))

        def begin_clearImages(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_clearImages.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearImages(self, _r):
            return _M_ode.model.Fileset._op_clearImages.end(self, _r)

        def reloadImages(self, toCopy, _ctx=None):
            return _M_ode.model.Fileset._op_reloadImages.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadImages(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_reloadImages.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadImages(self, _r):
            return _M_ode.model.Fileset._op_reloadImages.end(self, _r)

        def unloadJobLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_unloadJobLinks.invoke(self, ((), _ctx))

        def begin_unloadJobLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_unloadJobLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadJobLinks(self, _r):
            return _M_ode.model.Fileset._op_unloadJobLinks.end(self, _r)

        def sizeOfJobLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfJobLinks.invoke(self, ((), _ctx))

        def begin_sizeOfJobLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfJobLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfJobLinks(self, _r):
            return _M_ode.model.Fileset._op_sizeOfJobLinks.end(self, _r)

        def copyJobLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_copyJobLinks.invoke(self, ((), _ctx))

        def begin_copyJobLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_copyJobLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyJobLinks(self, _r):
            return _M_ode.model.Fileset._op_copyJobLinks.end(self, _r)

        def addFilesetJobLink(self, target, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetJobLink.invoke(self, ((target, ), _ctx))

        def begin_addFilesetJobLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetJobLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilesetJobLink(self, _r):
            return _M_ode.model.Fileset._op_addFilesetJobLink.end(self, _r)

        def addAllFilesetJobLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_addAllFilesetJobLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilesetJobLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addAllFilesetJobLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilesetJobLinkSet(self, _r):
            return _M_ode.model.Fileset._op_addAllFilesetJobLinkSet.end(self, _r)

        def removeFilesetJobLink(self, theTarget, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetJobLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilesetJobLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetJobLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilesetJobLink(self, _r):
            return _M_ode.model.Fileset._op_removeFilesetJobLink.end(self, _r)

        def removeAllFilesetJobLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllFilesetJobLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilesetJobLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllFilesetJobLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilesetJobLinkSet(self, _r):
            return _M_ode.model.Fileset._op_removeAllFilesetJobLinkSet.end(self, _r)

        def clearJobLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_clearJobLinks.invoke(self, ((), _ctx))

        def begin_clearJobLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_clearJobLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearJobLinks(self, _r):
            return _M_ode.model.Fileset._op_clearJobLinks.end(self, _r)

        def reloadJobLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Fileset._op_reloadJobLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadJobLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_reloadJobLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadJobLinks(self, _r):
            return _M_ode.model.Fileset._op_reloadJobLinks.end(self, _r)

        def getFilesetJobLink(self, index, _ctx=None):
            return _M_ode.model.Fileset._op_getFilesetJobLink.invoke(self, ((index, ), _ctx))

        def begin_getFilesetJobLink(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getFilesetJobLink.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getFilesetJobLink(self, _r):
            return _M_ode.model.Fileset._op_getFilesetJobLink.end(self, _r)

        def setFilesetJobLink(self, index, theElement, _ctx=None):
            return _M_ode.model.Fileset._op_setFilesetJobLink.invoke(self, ((index, theElement), _ctx))

        def begin_setFilesetJobLink(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_setFilesetJobLink.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setFilesetJobLink(self, _r):
            return _M_ode.model.Fileset._op_setFilesetJobLink.end(self, _r)

        def getPrimaryFilesetJobLink(self, _ctx=None):
            return _M_ode.model.Fileset._op_getPrimaryFilesetJobLink.invoke(self, ((), _ctx))

        def begin_getPrimaryFilesetJobLink(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getPrimaryFilesetJobLink.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryFilesetJobLink(self, _r):
            return _M_ode.model.Fileset._op_getPrimaryFilesetJobLink.end(self, _r)

        def setPrimaryFilesetJobLink(self, theElement, _ctx=None):
            return _M_ode.model.Fileset._op_setPrimaryFilesetJobLink.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryFilesetJobLink(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_setPrimaryFilesetJobLink.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryFilesetJobLink(self, _r):
            return _M_ode.model.Fileset._op_setPrimaryFilesetJobLink.end(self, _r)

        def getJobLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Fileset._op_getJobLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getJobLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getJobLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getJobLinksCountPerOwner(self, _r):
            return _M_ode.model.Fileset._op_getJobLinksCountPerOwner.end(self, _r)

        def linkJob(self, addition, _ctx=None):
            return _M_ode.model.Fileset._op_linkJob.invoke(self, ((addition, ), _ctx))

        def begin_linkJob(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_linkJob.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkJob(self, _r):
            return _M_ode.model.Fileset._op_linkJob.end(self, _r)

        def addFilesetJobLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetJobLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFilesetJobLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetJobLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFilesetJobLinkToBoth(self, _r):
            return _M_ode.model.Fileset._op_addFilesetJobLinkToBoth.end(self, _r)

        def findFilesetJobLink(self, removal, _ctx=None):
            return _M_ode.model.Fileset._op_findFilesetJobLink.invoke(self, ((removal, ), _ctx))

        def begin_findFilesetJobLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_findFilesetJobLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFilesetJobLink(self, _r):
            return _M_ode.model.Fileset._op_findFilesetJobLink.end(self, _r)

        def unlinkJob(self, removal, _ctx=None):
            return _M_ode.model.Fileset._op_unlinkJob.invoke(self, ((removal, ), _ctx))

        def begin_unlinkJob(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_unlinkJob.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkJob(self, _r):
            return _M_ode.model.Fileset._op_unlinkJob.end(self, _r)

        def removeFilesetJobLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetJobLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFilesetJobLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetJobLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFilesetJobLinkFromBoth(self, _r):
            return _M_ode.model.Fileset._op_removeFilesetJobLinkFromBoth.end(self, _r)

        def linkedJobList(self, _ctx=None):
            return _M_ode.model.Fileset._op_linkedJobList.invoke(self, ((), _ctx))

        def begin_linkedJobList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_linkedJobList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedJobList(self, _r):
            return _M_ode.model.Fileset._op_linkedJobList.end(self, _r)

        def getTemplatePrefix(self, _ctx=None):
            return _M_ode.model.Fileset._op_getTemplatePrefix.invoke(self, ((), _ctx))

        def begin_getTemplatePrefix(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getTemplatePrefix.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getTemplatePrefix(self, _r):
            return _M_ode.model.Fileset._op_getTemplatePrefix.end(self, _r)

        def setTemplatePrefix(self, theTemplatePrefix, _ctx=None):
            return _M_ode.model.Fileset._op_setTemplatePrefix.invoke(self, ((theTemplatePrefix, ), _ctx))

        def begin_setTemplatePrefix(self, theTemplatePrefix, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_setTemplatePrefix.begin(self, ((theTemplatePrefix, ), _response, _ex, _sent, _ctx))

        def end_setTemplatePrefix(self, _r):
            return _M_ode.model.Fileset._op_setTemplatePrefix.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Fileset._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Fileset._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Fileset._op_copyAnnotationLinks.end(self, _r)

        def addFilesetAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addFilesetAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFilesetAnnotationLink(self, _r):
            return _M_ode.model.Fileset._op_addFilesetAnnotationLink.end(self, _r)

        def addAllFilesetAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_addAllFilesetAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFilesetAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addAllFilesetAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFilesetAnnotationLinkSet(self, _r):
            return _M_ode.model.Fileset._op_addAllFilesetAnnotationLinkSet.end(self, _r)

        def removeFilesetAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFilesetAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFilesetAnnotationLink(self, _r):
            return _M_ode.model.Fileset._op_removeFilesetAnnotationLink.end(self, _r)

        def removeAllFilesetAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllFilesetAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFilesetAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeAllFilesetAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFilesetAnnotationLinkSet(self, _r):
            return _M_ode.model.Fileset._op_removeAllFilesetAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Fileset._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Fileset._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Fileset._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Fileset._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Fileset._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Fileset._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Fileset._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Fileset._op_linkAnnotation.end(self, _r)

        def addFilesetAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFilesetAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_addFilesetAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFilesetAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Fileset._op_addFilesetAnnotationLinkToBoth.end(self, _r)

        def findFilesetAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Fileset._op_findFilesetAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findFilesetAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_findFilesetAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFilesetAnnotationLink(self, _r):
            return _M_ode.model.Fileset._op_findFilesetAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Fileset._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Fileset._op_unlinkAnnotation.end(self, _r)

        def removeFilesetAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFilesetAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_removeFilesetAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFilesetAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Fileset._op_removeFilesetAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Fileset._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Fileset._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Fileset._op_linkedAnnotationList.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.FilesetPrx.ice_checkedCast(proxy, '::ode::model::Fileset', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.FilesetPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Fileset'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_FilesetPrx = IcePy.defineProxy('::ode::model::Fileset', FilesetPrx)

    _M_ode.model._t_Fileset = IcePy.declareClass('::ode::model::Fileset')

    _M_ode.model._t_Fileset = IcePy.defineClass('::ode::model::Fileset', Fileset, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_usedFilesSeq', (), _M_ode.model._t_FilesetUsedFilesSeq, False, 0),
        ('_usedFilesLoaded', (), IcePy._t_bool, False, 0),
        ('_imagesSeq', (), _M_ode.model._t_FilesetImagesSeq, False, 0),
        ('_imagesLoaded', (), IcePy._t_bool, False, 0),
        ('_jobLinksSeq', (), _M_ode.model._t_FilesetJobLinksSeq, False, 0),
        ('_jobLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_jobLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_templatePrefix', (), _M_ode._t_RString, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_FilesetAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0)
    ))
    Fileset._ice_type = _M_ode.model._t_Fileset

    Fileset._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Fileset._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Fileset._op_unloadUsedFiles = IcePy.Operation('unloadUsedFiles', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_sizeOfUsedFiles = IcePy.Operation('sizeOfUsedFiles', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Fileset._op_copyUsedFiles = IcePy.Operation('copyUsedFiles', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetUsedFilesSeq, False, 0), ())
    Fileset._op_addFilesetEntry = IcePy.Operation('addFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetEntry, False, 0),), (), None, ())
    Fileset._op_addAllFilesetEntrySet = IcePy.Operation('addAllFilesetEntrySet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetUsedFilesSeq, False, 0),), (), None, ())
    Fileset._op_removeFilesetEntry = IcePy.Operation('removeFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetEntry, False, 0),), (), None, ())
    Fileset._op_removeAllFilesetEntrySet = IcePy.Operation('removeAllFilesetEntrySet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetUsedFilesSeq, False, 0),), (), None, ())
    Fileset._op_clearUsedFiles = IcePy.Operation('clearUsedFiles', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_reloadUsedFiles = IcePy.Operation('reloadUsedFiles', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Fileset, False, 0),), (), None, ())
    Fileset._op_getFilesetEntry = IcePy.Operation('getFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_FilesetEntry, False, 0), ())
    Fileset._op_setFilesetEntry = IcePy.Operation('setFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_FilesetEntry, False, 0)), (), ((), _M_ode.model._t_FilesetEntry, False, 0), ())
    Fileset._op_getPrimaryFilesetEntry = IcePy.Operation('getPrimaryFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetEntry, False, 0), ())
    Fileset._op_setPrimaryFilesetEntry = IcePy.Operation('setPrimaryFilesetEntry', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetEntry, False, 0),), (), ((), _M_ode.model._t_FilesetEntry, False, 0), ())
    Fileset._op_unloadImages = IcePy.Operation('unloadImages', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_sizeOfImages = IcePy.Operation('sizeOfImages', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Fileset._op_copyImages = IcePy.Operation('copyImages', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetImagesSeq, False, 0), ())
    Fileset._op_addImage = IcePy.Operation('addImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), None, ())
    Fileset._op_addAllImageSet = IcePy.Operation('addAllImageSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetImagesSeq, False, 0),), (), None, ())
    Fileset._op_removeImage = IcePy.Operation('removeImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), None, ())
    Fileset._op_removeAllImageSet = IcePy.Operation('removeAllImageSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetImagesSeq, False, 0),), (), None, ())
    Fileset._op_clearImages = IcePy.Operation('clearImages', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_reloadImages = IcePy.Operation('reloadImages', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Fileset, False, 0),), (), None, ())
    Fileset._op_unloadJobLinks = IcePy.Operation('unloadJobLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_sizeOfJobLinks = IcePy.Operation('sizeOfJobLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Fileset._op_copyJobLinks = IcePy.Operation('copyJobLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetJobLinksSeq, False, 0), ())
    Fileset._op_addFilesetJobLink = IcePy.Operation('addFilesetJobLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetJobLink, False, 0),), (), None, ())
    Fileset._op_addAllFilesetJobLinkSet = IcePy.Operation('addAllFilesetJobLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetJobLinksSeq, False, 0),), (), None, ())
    Fileset._op_removeFilesetJobLink = IcePy.Operation('removeFilesetJobLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetJobLink, False, 0),), (), None, ())
    Fileset._op_removeAllFilesetJobLinkSet = IcePy.Operation('removeAllFilesetJobLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetJobLinksSeq, False, 0),), (), None, ())
    Fileset._op_clearJobLinks = IcePy.Operation('clearJobLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_reloadJobLinks = IcePy.Operation('reloadJobLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Fileset, False, 0),), (), None, ())
    Fileset._op_getFilesetJobLink = IcePy.Operation('getFilesetJobLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_FilesetJobLink, False, 0), ())
    Fileset._op_setFilesetJobLink = IcePy.Operation('setFilesetJobLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_FilesetJobLink, False, 0)), (), ((), _M_ode.model._t_FilesetJobLink, False, 0), ())
    Fileset._op_getPrimaryFilesetJobLink = IcePy.Operation('getPrimaryFilesetJobLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetJobLink, False, 0), ())
    Fileset._op_setPrimaryFilesetJobLink = IcePy.Operation('setPrimaryFilesetJobLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetJobLink, False, 0),), (), ((), _M_ode.model._t_FilesetJobLink, False, 0), ())
    Fileset._op_getJobLinksCountPerOwner = IcePy.Operation('getJobLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Fileset._op_linkJob = IcePy.Operation('linkJob', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Job, False, 0),), (), ((), _M_ode.model._t_FilesetJobLink, False, 0), ())
    Fileset._op_addFilesetJobLinkToBoth = IcePy.Operation('addFilesetJobLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetJobLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Fileset._op_findFilesetJobLink = IcePy.Operation('findFilesetJobLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Job, False, 0),), (), ((), _M_ode.model._t_FilesetJobLinksSeq, False, 0), ())
    Fileset._op_unlinkJob = IcePy.Operation('unlinkJob', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Job, False, 0),), (), None, ())
    Fileset._op_removeFilesetJobLinkFromBoth = IcePy.Operation('removeFilesetJobLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetJobLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Fileset._op_linkedJobList = IcePy.Operation('linkedJobList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetLinkedJobSeq, False, 0), ())
    Fileset._op_getTemplatePrefix = IcePy.Operation('getTemplatePrefix', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Fileset._op_setTemplatePrefix = IcePy.Operation('setTemplatePrefix', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Fileset._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Fileset._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetAnnotationLinksSeq, False, 0), ())
    Fileset._op_addFilesetAnnotationLink = IcePy.Operation('addFilesetAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetAnnotationLink, False, 0),), (), None, ())
    Fileset._op_addAllFilesetAnnotationLinkSet = IcePy.Operation('addAllFilesetAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetAnnotationLinksSeq, False, 0),), (), None, ())
    Fileset._op_removeFilesetAnnotationLink = IcePy.Operation('removeFilesetAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetAnnotationLink, False, 0),), (), None, ())
    Fileset._op_removeAllFilesetAnnotationLinkSet = IcePy.Operation('removeAllFilesetAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetAnnotationLinksSeq, False, 0),), (), None, ())
    Fileset._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Fileset._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Fileset, False, 0),), (), None, ())
    Fileset._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Fileset._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_FilesetAnnotationLink, False, 0), ())
    Fileset._op_addFilesetAnnotationLinkToBoth = IcePy.Operation('addFilesetAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Fileset._op_findFilesetAnnotationLink = IcePy.Operation('findFilesetAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_FilesetAnnotationLinksSeq, False, 0), ())
    Fileset._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Fileset._op_removeFilesetAnnotationLinkFromBoth = IcePy.Operation('removeFilesetAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FilesetAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Fileset._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_FilesetLinkedAnnotationSeq, False, 0), ())

    _M_ode.model.Fileset = Fileset
    del Fileset

    _M_ode.model.FilesetPrx = FilesetPrx
    del FilesetPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
