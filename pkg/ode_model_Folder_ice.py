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
import omero_model_IObject_ice
import omero_RTypes_ice
import omero_model_RTypes_ice
import omero_System_ice
import omero_Collections_ice

# Included module omero
_M_omero = Ice.openModule('omero')

# Included module omero.model
_M_omero.model = Ice.openModule('omero.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module omero.sys
_M_omero.sys = Ice.openModule('omero.sys')

# Included module omero.api
_M_omero.api = Ice.openModule('omero.api')

# Start of module omero
__name__ = 'omero'

# Start of module omero.model
__name__ = 'omero.model'

if 'Folder' not in _M_omero.model.__dict__:
    _M_omero.model._t_Folder = IcePy.declareClass('::omero::model::Folder')
    _M_omero.model._t_FolderPrx = IcePy.declareProxy('::omero::model::Folder')

if 'FolderImageLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderImageLink = IcePy.declareClass('::omero::model::FolderImageLink')
    _M_omero.model._t_FolderImageLinkPrx = IcePy.declareProxy('::omero::model::FolderImageLink')

if 'Image' not in _M_omero.model.__dict__:
    _M_omero.model._t_Image = IcePy.declareClass('::omero::model::Image')
    _M_omero.model._t_ImagePrx = IcePy.declareProxy('::omero::model::Image')

if 'FolderRoiLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderRoiLink = IcePy.declareClass('::omero::model::FolderRoiLink')
    _M_omero.model._t_FolderRoiLinkPrx = IcePy.declareProxy('::omero::model::FolderRoiLink')

if 'Roi' not in _M_omero.model.__dict__:
    _M_omero.model._t_Roi = IcePy.declareClass('::omero::model::Roi')
    _M_omero.model._t_RoiPrx = IcePy.declareProxy('::omero::model::Roi')

if 'FolderAnnotationLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderAnnotationLink = IcePy.declareClass('::omero::model::FolderAnnotationLink')
    _M_omero.model._t_FolderAnnotationLinkPrx = IcePy.declareProxy('::omero::model::FolderAnnotationLink')

if 'Annotation' not in _M_omero.model.__dict__:
    _M_omero.model._t_Annotation = IcePy.declareClass('::omero::model::Annotation')
    _M_omero.model._t_AnnotationPrx = IcePy.declareProxy('::omero::model::Annotation')

if 'Details' not in _M_omero.model.__dict__:
    _M_omero.model._t_Details = IcePy.declareClass('::omero::model::Details')
    _M_omero.model._t_DetailsPrx = IcePy.declareProxy('::omero::model::Details')

if '_t_FolderChildFoldersSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderChildFoldersSeq = IcePy.defineSequence('::omero::model::FolderChildFoldersSeq', (), _M_omero.model._t_Folder)

if '_t_FolderImageLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderImageLinksSeq = IcePy.defineSequence('::omero::model::FolderImageLinksSeq', (), _M_omero.model._t_FolderImageLink)

if '_t_FolderLinkedImageSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderLinkedImageSeq = IcePy.defineSequence('::omero::model::FolderLinkedImageSeq', (), _M_omero.model._t_Image)

if '_t_FolderRoiLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderRoiLinksSeq = IcePy.defineSequence('::omero::model::FolderRoiLinksSeq', (), _M_omero.model._t_FolderRoiLink)

if '_t_FolderLinkedRoiSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderLinkedRoiSeq = IcePy.defineSequence('::omero::model::FolderLinkedRoiSeq', (), _M_omero.model._t_Roi)

if '_t_FolderAnnotationLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderAnnotationLinksSeq = IcePy.defineSequence('::omero::model::FolderAnnotationLinksSeq', (), _M_omero.model._t_FolderAnnotationLink)

if '_t_FolderLinkedAnnotationSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderLinkedAnnotationSeq = IcePy.defineSequence('::omero::model::FolderLinkedAnnotationSeq', (), _M_omero.model._t_Annotation)

if 'Folder' not in _M_omero.model.__dict__:
    _M_omero.model.Folder = Ice.createTempClass()
    class Folder(_M_omero.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _childFoldersSeq=None, _childFoldersLoaded=False, _parentFolder=None, _imageLinksSeq=None, _imageLinksLoaded=False, _imageLinksCountPerOwner=None, _roiLinksSeq=None, _roiLinksLoaded=False, _roiLinksCountPerOwner=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None, _description=None):
            if Ice.getType(self) == _M_omero.model.Folder:
                raise RuntimeError('omero.model.Folder is an abstract class')
            _M_omero.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._childFoldersSeq = _childFoldersSeq
            self._childFoldersLoaded = _childFoldersLoaded
            self._parentFolder = _parentFolder
            self._imageLinksSeq = _imageLinksSeq
            self._imageLinksLoaded = _imageLinksLoaded
            self._imageLinksCountPerOwner = _imageLinksCountPerOwner
            self._roiLinksSeq = _roiLinksSeq
            self._roiLinksLoaded = _roiLinksLoaded
            self._roiLinksCountPerOwner = _roiLinksCountPerOwner
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::omero::model::Folder', '::omero::model::IObject')

        def ice_id(self, current=None):
            return '::omero::model::Folder'

        def ice_staticId():
            return '::omero::model::Folder'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def unloadChildFolders(self, current=None):
            pass

        def sizeOfChildFolders(self, current=None):
            pass

        def copyChildFolders(self, current=None):
            pass

        def addChildFolders(self, target, current=None):
            pass

        def addAllChildFoldersSet(self, targets, current=None):
            pass

        def removeChildFolders(self, theTarget, current=None):
            pass

        def removeAllChildFoldersSet(self, targets, current=None):
            pass

        def clearChildFolders(self, current=None):
            pass

        def reloadChildFolders(self, toCopy, current=None):
            pass

        def getParentFolder(self, current=None):
            pass

        def setParentFolder(self, theParentFolder, current=None):
            pass

        def unloadImageLinks(self, current=None):
            pass

        def sizeOfImageLinks(self, current=None):
            pass

        def copyImageLinks(self, current=None):
            pass

        def addFolderImageLink(self, target, current=None):
            pass

        def addAllFolderImageLinkSet(self, targets, current=None):
            pass

        def removeFolderImageLink(self, theTarget, current=None):
            pass

        def removeAllFolderImageLinkSet(self, targets, current=None):
            pass

        def clearImageLinks(self, current=None):
            pass

        def reloadImageLinks(self, toCopy, current=None):
            pass

        def getImageLinksCountPerOwner(self, current=None):
            pass

        def linkImage(self, addition, current=None):
            pass

        def addFolderImageLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFolderImageLink(self, removal, current=None):
            pass

        def unlinkImage(self, removal, current=None):
            pass

        def removeFolderImageLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedImageList(self, current=None):
            pass

        def unloadRoiLinks(self, current=None):
            pass

        def sizeOfRoiLinks(self, current=None):
            pass

        def copyRoiLinks(self, current=None):
            pass

        def addFolderRoiLink(self, target, current=None):
            pass

        def addAllFolderRoiLinkSet(self, targets, current=None):
            pass

        def removeFolderRoiLink(self, theTarget, current=None):
            pass

        def removeAllFolderRoiLinkSet(self, targets, current=None):
            pass

        def clearRoiLinks(self, current=None):
            pass

        def reloadRoiLinks(self, toCopy, current=None):
            pass

        def getRoiLinksCountPerOwner(self, current=None):
            pass

        def linkRoi(self, addition, current=None):
            pass

        def addFolderRoiLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFolderRoiLink(self, removal, current=None):
            pass

        def unlinkRoi(self, removal, current=None):
            pass

        def removeFolderRoiLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedRoiList(self, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addFolderAnnotationLink(self, target, current=None):
            pass

        def addAllFolderAnnotationLinkSet(self, targets, current=None):
            pass

        def removeFolderAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllFolderAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addFolderAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFolderAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeFolderAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def getDescription(self, current=None):
            pass

        def setDescription(self, theDescription, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_omero.model._t_Folder)

        __repr__ = __str__

    _M_omero.model.FolderPrx = Ice.createTempClass()
    class FolderPrx(_M_omero.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_omero.model.Folder._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_omero.model.Folder._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_omero.model.Folder._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_omero.model.Folder._op_setVersion.end(self, _r)

        def unloadChildFolders(self, _ctx=None):
            return _M_omero.model.Folder._op_unloadChildFolders.invoke(self, ((), _ctx))

        def begin_unloadChildFolders(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_unloadChildFolders.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadChildFolders(self, _r):
            return _M_omero.model.Folder._op_unloadChildFolders.end(self, _r)

        def sizeOfChildFolders(self, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfChildFolders.invoke(self, ((), _ctx))

        def begin_sizeOfChildFolders(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfChildFolders.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfChildFolders(self, _r):
            return _M_omero.model.Folder._op_sizeOfChildFolders.end(self, _r)

        def copyChildFolders(self, _ctx=None):
            return _M_omero.model.Folder._op_copyChildFolders.invoke(self, ((), _ctx))

        def begin_copyChildFolders(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_copyChildFolders.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyChildFolders(self, _r):
            return _M_omero.model.Folder._op_copyChildFolders.end(self, _r)

        def addChildFolders(self, target, _ctx=None):
            return _M_omero.model.Folder._op_addChildFolders.invoke(self, ((target, ), _ctx))

        def begin_addChildFolders(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addChildFolders.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addChildFolders(self, _r):
            return _M_omero.model.Folder._op_addChildFolders.end(self, _r)

        def addAllChildFoldersSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_addAllChildFoldersSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllChildFoldersSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addAllChildFoldersSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllChildFoldersSet(self, _r):
            return _M_omero.model.Folder._op_addAllChildFoldersSet.end(self, _r)

        def removeChildFolders(self, theTarget, _ctx=None):
            return _M_omero.model.Folder._op_removeChildFolders.invoke(self, ((theTarget, ), _ctx))

        def begin_removeChildFolders(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeChildFolders.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeChildFolders(self, _r):
            return _M_omero.model.Folder._op_removeChildFolders.end(self, _r)

        def removeAllChildFoldersSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_removeAllChildFoldersSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllChildFoldersSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeAllChildFoldersSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllChildFoldersSet(self, _r):
            return _M_omero.model.Folder._op_removeAllChildFoldersSet.end(self, _r)

        def clearChildFolders(self, _ctx=None):
            return _M_omero.model.Folder._op_clearChildFolders.invoke(self, ((), _ctx))

        def begin_clearChildFolders(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_clearChildFolders.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearChildFolders(self, _r):
            return _M_omero.model.Folder._op_clearChildFolders.end(self, _r)

        def reloadChildFolders(self, toCopy, _ctx=None):
            return _M_omero.model.Folder._op_reloadChildFolders.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadChildFolders(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_reloadChildFolders.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadChildFolders(self, _r):
            return _M_omero.model.Folder._op_reloadChildFolders.end(self, _r)

        def getParentFolder(self, _ctx=None):
            return _M_omero.model.Folder._op_getParentFolder.invoke(self, ((), _ctx))

        def begin_getParentFolder(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_getParentFolder.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getParentFolder(self, _r):
            return _M_omero.model.Folder._op_getParentFolder.end(self, _r)

        def setParentFolder(self, theParentFolder, _ctx=None):
            return _M_omero.model.Folder._op_setParentFolder.invoke(self, ((theParentFolder, ), _ctx))

        def begin_setParentFolder(self, theParentFolder, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_setParentFolder.begin(self, ((theParentFolder, ), _response, _ex, _sent, _ctx))

        def end_setParentFolder(self, _r):
            return _M_omero.model.Folder._op_setParentFolder.end(self, _r)

        def unloadImageLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_unloadImageLinks.invoke(self, ((), _ctx))

        def begin_unloadImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_unloadImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadImageLinks(self, _r):
            return _M_omero.model.Folder._op_unloadImageLinks.end(self, _r)

        def sizeOfImageLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfImageLinks.invoke(self, ((), _ctx))

        def begin_sizeOfImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfImageLinks(self, _r):
            return _M_omero.model.Folder._op_sizeOfImageLinks.end(self, _r)

        def copyImageLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_copyImageLinks.invoke(self, ((), _ctx))

        def begin_copyImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_copyImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyImageLinks(self, _r):
            return _M_omero.model.Folder._op_copyImageLinks.end(self, _r)

        def addFolderImageLink(self, target, _ctx=None):
            return _M_omero.model.Folder._op_addFolderImageLink.invoke(self, ((target, ), _ctx))

        def begin_addFolderImageLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addFolderImageLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFolderImageLink(self, _r):
            return _M_omero.model.Folder._op_addFolderImageLink.end(self, _r)

        def addAllFolderImageLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_addAllFolderImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFolderImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addAllFolderImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFolderImageLinkSet(self, _r):
            return _M_omero.model.Folder._op_addAllFolderImageLinkSet.end(self, _r)

        def removeFolderImageLink(self, theTarget, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderImageLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFolderImageLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderImageLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFolderImageLink(self, _r):
            return _M_omero.model.Folder._op_removeFolderImageLink.end(self, _r)

        def removeAllFolderImageLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_removeAllFolderImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFolderImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeAllFolderImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFolderImageLinkSet(self, _r):
            return _M_omero.model.Folder._op_removeAllFolderImageLinkSet.end(self, _r)

        def clearImageLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_clearImageLinks.invoke(self, ((), _ctx))

        def begin_clearImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_clearImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearImageLinks(self, _r):
            return _M_omero.model.Folder._op_clearImageLinks.end(self, _r)

        def reloadImageLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Folder._op_reloadImageLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadImageLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_reloadImageLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadImageLinks(self, _r):
            return _M_omero.model.Folder._op_reloadImageLinks.end(self, _r)

        def getImageLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Folder._op_getImageLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getImageLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_getImageLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImageLinksCountPerOwner(self, _r):
            return _M_omero.model.Folder._op_getImageLinksCountPerOwner.end(self, _r)

        def linkImage(self, addition, _ctx=None):
            return _M_omero.model.Folder._op_linkImage.invoke(self, ((addition, ), _ctx))

        def begin_linkImage(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_linkImage.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkImage(self, _r):
            return _M_omero.model.Folder._op_linkImage.end(self, _r)

        def addFolderImageLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Folder._op_addFolderImageLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFolderImageLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addFolderImageLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFolderImageLinkToBoth(self, _r):
            return _M_omero.model.Folder._op_addFolderImageLinkToBoth.end(self, _r)

        def findFolderImageLink(self, removal, _ctx=None):
            return _M_omero.model.Folder._op_findFolderImageLink.invoke(self, ((removal, ), _ctx))

        def begin_findFolderImageLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_findFolderImageLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFolderImageLink(self, _r):
            return _M_omero.model.Folder._op_findFolderImageLink.end(self, _r)

        def unlinkImage(self, removal, _ctx=None):
            return _M_omero.model.Folder._op_unlinkImage.invoke(self, ((removal, ), _ctx))

        def begin_unlinkImage(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_unlinkImage.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkImage(self, _r):
            return _M_omero.model.Folder._op_unlinkImage.end(self, _r)

        def removeFolderImageLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderImageLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFolderImageLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderImageLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFolderImageLinkFromBoth(self, _r):
            return _M_omero.model.Folder._op_removeFolderImageLinkFromBoth.end(self, _r)

        def linkedImageList(self, _ctx=None):
            return _M_omero.model.Folder._op_linkedImageList.invoke(self, ((), _ctx))

        def begin_linkedImageList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_linkedImageList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedImageList(self, _r):
            return _M_omero.model.Folder._op_linkedImageList.end(self, _r)

        def unloadRoiLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_unloadRoiLinks.invoke(self, ((), _ctx))

        def begin_unloadRoiLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_unloadRoiLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadRoiLinks(self, _r):
            return _M_omero.model.Folder._op_unloadRoiLinks.end(self, _r)

        def sizeOfRoiLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfRoiLinks.invoke(self, ((), _ctx))

        def begin_sizeOfRoiLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfRoiLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfRoiLinks(self, _r):
            return _M_omero.model.Folder._op_sizeOfRoiLinks.end(self, _r)

        def copyRoiLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_copyRoiLinks.invoke(self, ((), _ctx))

        def begin_copyRoiLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_copyRoiLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyRoiLinks(self, _r):
            return _M_omero.model.Folder._op_copyRoiLinks.end(self, _r)

        def addFolderRoiLink(self, target, _ctx=None):
            return _M_omero.model.Folder._op_addFolderRoiLink.invoke(self, ((target, ), _ctx))

        def begin_addFolderRoiLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addFolderRoiLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFolderRoiLink(self, _r):
            return _M_omero.model.Folder._op_addFolderRoiLink.end(self, _r)

        def addAllFolderRoiLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_addAllFolderRoiLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFolderRoiLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addAllFolderRoiLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFolderRoiLinkSet(self, _r):
            return _M_omero.model.Folder._op_addAllFolderRoiLinkSet.end(self, _r)

        def removeFolderRoiLink(self, theTarget, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderRoiLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFolderRoiLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderRoiLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFolderRoiLink(self, _r):
            return _M_omero.model.Folder._op_removeFolderRoiLink.end(self, _r)

        def removeAllFolderRoiLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_removeAllFolderRoiLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFolderRoiLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeAllFolderRoiLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFolderRoiLinkSet(self, _r):
            return _M_omero.model.Folder._op_removeAllFolderRoiLinkSet.end(self, _r)

        def clearRoiLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_clearRoiLinks.invoke(self, ((), _ctx))

        def begin_clearRoiLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_clearRoiLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearRoiLinks(self, _r):
            return _M_omero.model.Folder._op_clearRoiLinks.end(self, _r)

        def reloadRoiLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Folder._op_reloadRoiLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadRoiLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_reloadRoiLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadRoiLinks(self, _r):
            return _M_omero.model.Folder._op_reloadRoiLinks.end(self, _r)

        def getRoiLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Folder._op_getRoiLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getRoiLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_getRoiLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getRoiLinksCountPerOwner(self, _r):
            return _M_omero.model.Folder._op_getRoiLinksCountPerOwner.end(self, _r)

        def linkRoi(self, addition, _ctx=None):
            return _M_omero.model.Folder._op_linkRoi.invoke(self, ((addition, ), _ctx))

        def begin_linkRoi(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_linkRoi.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkRoi(self, _r):
            return _M_omero.model.Folder._op_linkRoi.end(self, _r)

        def addFolderRoiLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Folder._op_addFolderRoiLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFolderRoiLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addFolderRoiLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFolderRoiLinkToBoth(self, _r):
            return _M_omero.model.Folder._op_addFolderRoiLinkToBoth.end(self, _r)

        def findFolderRoiLink(self, removal, _ctx=None):
            return _M_omero.model.Folder._op_findFolderRoiLink.invoke(self, ((removal, ), _ctx))

        def begin_findFolderRoiLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_findFolderRoiLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFolderRoiLink(self, _r):
            return _M_omero.model.Folder._op_findFolderRoiLink.end(self, _r)

        def unlinkRoi(self, removal, _ctx=None):
            return _M_omero.model.Folder._op_unlinkRoi.invoke(self, ((removal, ), _ctx))

        def begin_unlinkRoi(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_unlinkRoi.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkRoi(self, _r):
            return _M_omero.model.Folder._op_unlinkRoi.end(self, _r)

        def removeFolderRoiLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderRoiLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFolderRoiLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderRoiLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFolderRoiLinkFromBoth(self, _r):
            return _M_omero.model.Folder._op_removeFolderRoiLinkFromBoth.end(self, _r)

        def linkedRoiList(self, _ctx=None):
            return _M_omero.model.Folder._op_linkedRoiList.invoke(self, ((), _ctx))

        def begin_linkedRoiList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_linkedRoiList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedRoiList(self, _r):
            return _M_omero.model.Folder._op_linkedRoiList.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_omero.model.Folder._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_omero.model.Folder._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_omero.model.Folder._op_copyAnnotationLinks.end(self, _r)

        def addFolderAnnotationLink(self, target, _ctx=None):
            return _M_omero.model.Folder._op_addFolderAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addFolderAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addFolderAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFolderAnnotationLink(self, _r):
            return _M_omero.model.Folder._op_addFolderAnnotationLink.end(self, _r)

        def addAllFolderAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_addAllFolderAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFolderAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addAllFolderAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFolderAnnotationLinkSet(self, _r):
            return _M_omero.model.Folder._op_addAllFolderAnnotationLinkSet.end(self, _r)

        def removeFolderAnnotationLink(self, theTarget, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFolderAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFolderAnnotationLink(self, _r):
            return _M_omero.model.Folder._op_removeFolderAnnotationLink.end(self, _r)

        def removeAllFolderAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Folder._op_removeAllFolderAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFolderAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeAllFolderAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFolderAnnotationLinkSet(self, _r):
            return _M_omero.model.Folder._op_removeAllFolderAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Folder._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_omero.model.Folder._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Folder._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_omero.model.Folder._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Folder._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_omero.model.Folder._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_omero.model.Folder._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_omero.model.Folder._op_linkAnnotation.end(self, _r)

        def addFolderAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Folder._op_addFolderAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFolderAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_addFolderAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFolderAnnotationLinkToBoth(self, _r):
            return _M_omero.model.Folder._op_addFolderAnnotationLinkToBoth.end(self, _r)

        def findFolderAnnotationLink(self, removal, _ctx=None):
            return _M_omero.model.Folder._op_findFolderAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findFolderAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_findFolderAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFolderAnnotationLink(self, _r):
            return _M_omero.model.Folder._op_findFolderAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_omero.model.Folder._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_omero.model.Folder._op_unlinkAnnotation.end(self, _r)

        def removeFolderAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFolderAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_removeFolderAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFolderAnnotationLinkFromBoth(self, _r):
            return _M_omero.model.Folder._op_removeFolderAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_omero.model.Folder._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_omero.model.Folder._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_omero.model.Folder._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_omero.model.Folder._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_omero.model.Folder._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_omero.model.Folder._op_setName.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_omero.model.Folder._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_omero.model.Folder._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_omero.model.Folder._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Folder._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_omero.model.Folder._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_omero.model.FolderPrx.ice_checkedCast(proxy, '::omero::model::Folder', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_omero.model.FolderPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::omero::model::Folder'
        ice_staticId = staticmethod(ice_staticId)

    _M_omero.model._t_FolderPrx = IcePy.defineProxy('::omero::model::Folder', FolderPrx)

    _M_omero.model._t_Folder = IcePy.defineClass('::omero::model::Folder', Folder, -1, (), True, False, _M_omero.model._t_IObject, (), (
        ('_version', (), _M_omero._t_RInt, False, 0),
        ('_childFoldersSeq', (), _M_omero.model._t_FolderChildFoldersSeq, False, 0),
        ('_childFoldersLoaded', (), IcePy._t_bool, False, 0),
        ('_parentFolder', (), _M_omero.model._t_Folder, False, 0),
        ('_imageLinksSeq', (), _M_omero.model._t_FolderImageLinksSeq, False, 0),
        ('_imageLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_imageLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_roiLinksSeq', (), _M_omero.model._t_FolderRoiLinksSeq, False, 0),
        ('_roiLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_roiLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_annotationLinksSeq', (), _M_omero.model._t_FolderAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_name', (), _M_omero._t_RString, False, 0),
        ('_description', (), _M_omero._t_RString, False, 0)
    ))
    Folder._ice_type = _M_omero.model._t_Folder

    Folder._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RInt, False, 0), ())
    Folder._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RInt, False, 0),), (), None, ())
    Folder._op_unloadChildFolders = IcePy.Operation('unloadChildFolders', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_sizeOfChildFolders = IcePy.Operation('sizeOfChildFolders', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Folder._op_copyChildFolders = IcePy.Operation('copyChildFolders', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FolderChildFoldersSeq, False, 0), ())
    Folder._op_addChildFolders = IcePy.Operation('addChildFolders', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Folder._op_addAllChildFoldersSet = IcePy.Operation('addAllChildFoldersSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderChildFoldersSeq, False, 0),), (), None, ())
    Folder._op_removeChildFolders = IcePy.Operation('removeChildFolders', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Folder._op_removeAllChildFoldersSet = IcePy.Operation('removeAllChildFoldersSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderChildFoldersSeq, False, 0),), (), None, ())
    Folder._op_clearChildFolders = IcePy.Operation('clearChildFolders', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_reloadChildFolders = IcePy.Operation('reloadChildFolders', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Folder._op_getParentFolder = IcePy.Operation('getParentFolder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Folder, False, 0), ())
    Folder._op_setParentFolder = IcePy.Operation('setParentFolder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Folder._op_unloadImageLinks = IcePy.Operation('unloadImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_sizeOfImageLinks = IcePy.Operation('sizeOfImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Folder._op_copyImageLinks = IcePy.Operation('copyImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FolderImageLinksSeq, False, 0), ())
    Folder._op_addFolderImageLink = IcePy.Operation('addFolderImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0),), (), None, ())
    Folder._op_addAllFolderImageLinkSet = IcePy.Operation('addAllFolderImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLinksSeq, False, 0),), (), None, ())
    Folder._op_removeFolderImageLink = IcePy.Operation('removeFolderImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0),), (), None, ())
    Folder._op_removeAllFolderImageLinkSet = IcePy.Operation('removeAllFolderImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLinksSeq, False, 0),), (), None, ())
    Folder._op_clearImageLinks = IcePy.Operation('clearImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_reloadImageLinks = IcePy.Operation('reloadImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Folder._op_getImageLinksCountPerOwner = IcePy.Operation('getImageLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Folder._op_linkImage = IcePy.Operation('linkImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), ((), _M_omero.model._t_FolderImageLink, False, 0), ())
    Folder._op_addFolderImageLinkToBoth = IcePy.Operation('addFolderImageLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Folder._op_findFolderImageLink = IcePy.Operation('findFolderImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), ((), _M_omero.model._t_FolderImageLinksSeq, False, 0), ())
    Folder._op_unlinkImage = IcePy.Operation('unlinkImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), None, ())
    Folder._op_removeFolderImageLinkFromBoth = IcePy.Operation('removeFolderImageLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Folder._op_linkedImageList = IcePy.Operation('linkedImageList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FolderLinkedImageSeq, False, 0), ())
    Folder._op_unloadRoiLinks = IcePy.Operation('unloadRoiLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_sizeOfRoiLinks = IcePy.Operation('sizeOfRoiLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Folder._op_copyRoiLinks = IcePy.Operation('copyRoiLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FolderRoiLinksSeq, False, 0), ())
    Folder._op_addFolderRoiLink = IcePy.Operation('addFolderRoiLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderRoiLink, False, 0),), (), None, ())
    Folder._op_addAllFolderRoiLinkSet = IcePy.Operation('addAllFolderRoiLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderRoiLinksSeq, False, 0),), (), None, ())
    Folder._op_removeFolderRoiLink = IcePy.Operation('removeFolderRoiLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderRoiLink, False, 0),), (), None, ())
    Folder._op_removeAllFolderRoiLinkSet = IcePy.Operation('removeAllFolderRoiLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderRoiLinksSeq, False, 0),), (), None, ())
    Folder._op_clearRoiLinks = IcePy.Operation('clearRoiLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_reloadRoiLinks = IcePy.Operation('reloadRoiLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Folder._op_getRoiLinksCountPerOwner = IcePy.Operation('getRoiLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Folder._op_linkRoi = IcePy.Operation('linkRoi', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Roi, False, 0),), (), ((), _M_omero.model._t_FolderRoiLink, False, 0), ())
    Folder._op_addFolderRoiLinkToBoth = IcePy.Operation('addFolderRoiLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderRoiLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Folder._op_findFolderRoiLink = IcePy.Operation('findFolderRoiLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Roi, False, 0),), (), ((), _M_omero.model._t_FolderRoiLinksSeq, False, 0), ())
    Folder._op_unlinkRoi = IcePy.Operation('unlinkRoi', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Roi, False, 0),), (), None, ())
    Folder._op_removeFolderRoiLinkFromBoth = IcePy.Operation('removeFolderRoiLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderRoiLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Folder._op_linkedRoiList = IcePy.Operation('linkedRoiList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FolderLinkedRoiSeq, False, 0), ())
    Folder._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Folder._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FolderAnnotationLinksSeq, False, 0), ())
    Folder._op_addFolderAnnotationLink = IcePy.Operation('addFolderAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderAnnotationLink, False, 0),), (), None, ())
    Folder._op_addAllFolderAnnotationLinkSet = IcePy.Operation('addAllFolderAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderAnnotationLinksSeq, False, 0),), (), None, ())
    Folder._op_removeFolderAnnotationLink = IcePy.Operation('removeFolderAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderAnnotationLink, False, 0),), (), None, ())
    Folder._op_removeAllFolderAnnotationLinkSet = IcePy.Operation('removeAllFolderAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderAnnotationLinksSeq, False, 0),), (), None, ())
    Folder._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Folder._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Folder._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Folder._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_FolderAnnotationLink, False, 0), ())
    Folder._op_addFolderAnnotationLinkToBoth = IcePy.Operation('addFolderAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Folder._op_findFolderAnnotationLink = IcePy.Operation('findFolderAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_FolderAnnotationLinksSeq, False, 0), ())
    Folder._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), None, ())
    Folder._op_removeFolderAnnotationLinkFromBoth = IcePy.Operation('removeFolderAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Folder._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_FolderLinkedAnnotationSeq, False, 0), ())
    Folder._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Folder._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())
    Folder._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Folder._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())

    _M_omero.model.Folder = Folder
    del Folder

    _M_omero.model.FolderPrx = FolderPrx
    del FolderPrx

# End of module omero.model

__name__ = 'omero'

# End of module omero
