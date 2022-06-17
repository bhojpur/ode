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

if 'Shape' not in _M_ode.model.__dict__:
    _M_ode.model._t_Shape = IcePy.declareClass('::ode::model::Shape')
    _M_ode.model._t_ShapePrx = IcePy.declareProxy('::ode::model::Shape')

if 'Image' not in _M_ode.model.__dict__:
    _M_ode.model._t_Image = IcePy.declareClass('::ode::model::Image')
    _M_ode.model._t_ImagePrx = IcePy.declareProxy('::ode::model::Image')

if 'OriginalFile' not in _M_ode.model.__dict__:
    _M_ode.model._t_OriginalFile = IcePy.declareClass('::ode::model::OriginalFile')
    _M_ode.model._t_OriginalFilePrx = IcePy.declareProxy('::ode::model::OriginalFile')

if 'FolderRoiLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_FolderRoiLink = IcePy.declareClass('::ode::model::FolderRoiLink')
    _M_ode.model._t_FolderRoiLinkPrx = IcePy.declareProxy('::ode::model::FolderRoiLink')

if 'Folder' not in _M_ode.model.__dict__:
    _M_ode.model._t_Folder = IcePy.declareClass('::ode::model::Folder')
    _M_ode.model._t_FolderPrx = IcePy.declareProxy('::ode::model::Folder')

if 'RoiAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_RoiAnnotationLink = IcePy.declareClass('::ode::model::RoiAnnotationLink')
    _M_ode.model._t_RoiAnnotationLinkPrx = IcePy.declareProxy('::ode::model::RoiAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_RoiShapesSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_RoiShapesSeq = IcePy.defineSequence('::ode::model::RoiShapesSeq', (), _M_ode.model._t_Shape)

if '_t_RoiFolderLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_RoiFolderLinksSeq = IcePy.defineSequence('::ode::model::RoiFolderLinksSeq', (), _M_ode.model._t_FolderRoiLink)

if '_t_RoiLinkedFolderSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_RoiLinkedFolderSeq = IcePy.defineSequence('::ode::model::RoiLinkedFolderSeq', (), _M_ode.model._t_Folder)

if '_t_RoiAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_RoiAnnotationLinksSeq = IcePy.defineSequence('::ode::model::RoiAnnotationLinksSeq', (), _M_ode.model._t_RoiAnnotationLink)

if '_t_RoiLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_RoiLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::RoiLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Roi' not in _M_ode.model.__dict__:
    _M_ode.model.Roi = Ice.createTempClass()
    class Roi(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _name=None, _shapesSeq=None, _shapesLoaded=False, _image=None, _source=None, _folderLinksSeq=None, _folderLinksLoaded=False, _folderLinksCountPerOwner=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _description=None):
            if Ice.getType(self) == _M_ode.model.Roi:
                raise RuntimeError('ode.model.Roi is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._name = _name
            self._shapesSeq = _shapesSeq
            self._shapesLoaded = _shapesLoaded
            self._image = _image
            self._source = _source
            self._folderLinksSeq = _folderLinksSeq
            self._folderLinksLoaded = _folderLinksLoaded
            self._folderLinksCountPerOwner = _folderLinksCountPerOwner
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Roi')

        def ice_id(self, current=None):
            return '::ode::model::Roi'

        def ice_staticId():
            return '::ode::model::Roi'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getName(self, current=None):
            pass

        def setName(self, theName, current=None):
            pass

        def unloadShapes(self, current=None):
            pass

        def sizeOfShapes(self, current=None):
            pass

        def copyShapes(self, current=None):
            pass

        def addShape(self, target, current=None):
            pass

        def addAllShapeSet(self, targets, current=None):
            pass

        def removeShape(self, theTarget, current=None):
            pass

        def removeAllShapeSet(self, targets, current=None):
            pass

        def clearShapes(self, current=None):
            pass

        def reloadShapes(self, toCopy, current=None):
            pass

        def getShape(self, index, current=None):
            pass

        def setShape(self, index, theElement, current=None):
            pass

        def getPrimaryShape(self, current=None):
            pass

        def setPrimaryShape(self, theElement, current=None):
            pass

        def getImage(self, current=None):
            pass

        def setImage(self, theImage, current=None):
            pass

        def getSource(self, current=None):
            pass

        def setSource(self, theSource, current=None):
            pass

        def unloadFolderLinks(self, current=None):
            pass

        def sizeOfFolderLinks(self, current=None):
            pass

        def copyFolderLinks(self, current=None):
            pass

        def addFolderRoiLink(self, target, current=None):
            pass

        def addAllFolderRoiLinkSet(self, targets, current=None):
            pass

        def removeFolderRoiLink(self, theTarget, current=None):
            pass

        def removeAllFolderRoiLinkSet(self, targets, current=None):
            pass

        def clearFolderLinks(self, current=None):
            pass

        def reloadFolderLinks(self, toCopy, current=None):
            pass

        def getFolderLinksCountPerOwner(self, current=None):
            pass

        def linkFolder(self, addition, current=None):
            pass

        def addFolderRoiLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFolderRoiLink(self, removal, current=None):
            pass

        def unlinkFolder(self, removal, current=None):
            pass

        def removeFolderRoiLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedFolderList(self, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addRoiAnnotationLink(self, target, current=None):
            pass

        def addAllRoiAnnotationLinkSet(self, targets, current=None):
            pass

        def removeRoiAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllRoiAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addRoiAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findRoiAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeRoiAnnotationLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedAnnotationList(self, current=None):
            pass

        def getDescription(self, current=None):
            pass

        def setDescription(self, theDescription, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.model._t_Roi)

        __repr__ = __str__

    _M_ode.model.RoiPrx = Ice.createTempClass()
    class RoiPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Roi._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Roi._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Roi._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Roi._op_setVersion.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.Roi._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.Roi._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.Roi._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.Roi._op_setName.end(self, _r)

        def unloadShapes(self, _ctx=None):
            return _M_ode.model.Roi._op_unloadShapes.invoke(self, ((), _ctx))

        def begin_unloadShapes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_unloadShapes.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadShapes(self, _r):
            return _M_ode.model.Roi._op_unloadShapes.end(self, _r)

        def sizeOfShapes(self, _ctx=None):
            return _M_ode.model.Roi._op_sizeOfShapes.invoke(self, ((), _ctx))

        def begin_sizeOfShapes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_sizeOfShapes.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfShapes(self, _r):
            return _M_ode.model.Roi._op_sizeOfShapes.end(self, _r)

        def copyShapes(self, _ctx=None):
            return _M_ode.model.Roi._op_copyShapes.invoke(self, ((), _ctx))

        def begin_copyShapes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_copyShapes.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyShapes(self, _r):
            return _M_ode.model.Roi._op_copyShapes.end(self, _r)

        def addShape(self, target, _ctx=None):
            return _M_ode.model.Roi._op_addShape.invoke(self, ((target, ), _ctx))

        def begin_addShape(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addShape.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addShape(self, _r):
            return _M_ode.model.Roi._op_addShape.end(self, _r)

        def addAllShapeSet(self, targets, _ctx=None):
            return _M_ode.model.Roi._op_addAllShapeSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllShapeSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addAllShapeSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllShapeSet(self, _r):
            return _M_ode.model.Roi._op_addAllShapeSet.end(self, _r)

        def removeShape(self, theTarget, _ctx=None):
            return _M_ode.model.Roi._op_removeShape.invoke(self, ((theTarget, ), _ctx))

        def begin_removeShape(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeShape.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeShape(self, _r):
            return _M_ode.model.Roi._op_removeShape.end(self, _r)

        def removeAllShapeSet(self, targets, _ctx=None):
            return _M_ode.model.Roi._op_removeAllShapeSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllShapeSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeAllShapeSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllShapeSet(self, _r):
            return _M_ode.model.Roi._op_removeAllShapeSet.end(self, _r)

        def clearShapes(self, _ctx=None):
            return _M_ode.model.Roi._op_clearShapes.invoke(self, ((), _ctx))

        def begin_clearShapes(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_clearShapes.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearShapes(self, _r):
            return _M_ode.model.Roi._op_clearShapes.end(self, _r)

        def reloadShapes(self, toCopy, _ctx=None):
            return _M_ode.model.Roi._op_reloadShapes.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadShapes(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_reloadShapes.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadShapes(self, _r):
            return _M_ode.model.Roi._op_reloadShapes.end(self, _r)

        def getShape(self, index, _ctx=None):
            return _M_ode.model.Roi._op_getShape.invoke(self, ((index, ), _ctx))

        def begin_getShape(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getShape.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getShape(self, _r):
            return _M_ode.model.Roi._op_getShape.end(self, _r)

        def setShape(self, index, theElement, _ctx=None):
            return _M_ode.model.Roi._op_setShape.invoke(self, ((index, theElement), _ctx))

        def begin_setShape(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_setShape.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setShape(self, _r):
            return _M_ode.model.Roi._op_setShape.end(self, _r)

        def getPrimaryShape(self, _ctx=None):
            return _M_ode.model.Roi._op_getPrimaryShape.invoke(self, ((), _ctx))

        def begin_getPrimaryShape(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getPrimaryShape.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryShape(self, _r):
            return _M_ode.model.Roi._op_getPrimaryShape.end(self, _r)

        def setPrimaryShape(self, theElement, _ctx=None):
            return _M_ode.model.Roi._op_setPrimaryShape.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryShape(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_setPrimaryShape.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryShape(self, _r):
            return _M_ode.model.Roi._op_setPrimaryShape.end(self, _r)

        def getImage(self, _ctx=None):
            return _M_ode.model.Roi._op_getImage.invoke(self, ((), _ctx))

        def begin_getImage(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getImage.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImage(self, _r):
            return _M_ode.model.Roi._op_getImage.end(self, _r)

        def setImage(self, theImage, _ctx=None):
            return _M_ode.model.Roi._op_setImage.invoke(self, ((theImage, ), _ctx))

        def begin_setImage(self, theImage, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_setImage.begin(self, ((theImage, ), _response, _ex, _sent, _ctx))

        def end_setImage(self, _r):
            return _M_ode.model.Roi._op_setImage.end(self, _r)

        def getSource(self, _ctx=None):
            return _M_ode.model.Roi._op_getSource.invoke(self, ((), _ctx))

        def begin_getSource(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getSource.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSource(self, _r):
            return _M_ode.model.Roi._op_getSource.end(self, _r)

        def setSource(self, theSource, _ctx=None):
            return _M_ode.model.Roi._op_setSource.invoke(self, ((theSource, ), _ctx))

        def begin_setSource(self, theSource, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_setSource.begin(self, ((theSource, ), _response, _ex, _sent, _ctx))

        def end_setSource(self, _r):
            return _M_ode.model.Roi._op_setSource.end(self, _r)

        def unloadFolderLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_unloadFolderLinks.invoke(self, ((), _ctx))

        def begin_unloadFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_unloadFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadFolderLinks(self, _r):
            return _M_ode.model.Roi._op_unloadFolderLinks.end(self, _r)

        def sizeOfFolderLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_sizeOfFolderLinks.invoke(self, ((), _ctx))

        def begin_sizeOfFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_sizeOfFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfFolderLinks(self, _r):
            return _M_ode.model.Roi._op_sizeOfFolderLinks.end(self, _r)

        def copyFolderLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_copyFolderLinks.invoke(self, ((), _ctx))

        def begin_copyFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_copyFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyFolderLinks(self, _r):
            return _M_ode.model.Roi._op_copyFolderLinks.end(self, _r)

        def addFolderRoiLink(self, target, _ctx=None):
            return _M_ode.model.Roi._op_addFolderRoiLink.invoke(self, ((target, ), _ctx))

        def begin_addFolderRoiLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addFolderRoiLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFolderRoiLink(self, _r):
            return _M_ode.model.Roi._op_addFolderRoiLink.end(self, _r)

        def addAllFolderRoiLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Roi._op_addAllFolderRoiLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFolderRoiLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addAllFolderRoiLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFolderRoiLinkSet(self, _r):
            return _M_ode.model.Roi._op_addAllFolderRoiLinkSet.end(self, _r)

        def removeFolderRoiLink(self, theTarget, _ctx=None):
            return _M_ode.model.Roi._op_removeFolderRoiLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFolderRoiLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeFolderRoiLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFolderRoiLink(self, _r):
            return _M_ode.model.Roi._op_removeFolderRoiLink.end(self, _r)

        def removeAllFolderRoiLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Roi._op_removeAllFolderRoiLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFolderRoiLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeAllFolderRoiLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFolderRoiLinkSet(self, _r):
            return _M_ode.model.Roi._op_removeAllFolderRoiLinkSet.end(self, _r)

        def clearFolderLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_clearFolderLinks.invoke(self, ((), _ctx))

        def begin_clearFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_clearFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearFolderLinks(self, _r):
            return _M_ode.model.Roi._op_clearFolderLinks.end(self, _r)

        def reloadFolderLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Roi._op_reloadFolderLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadFolderLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_reloadFolderLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadFolderLinks(self, _r):
            return _M_ode.model.Roi._op_reloadFolderLinks.end(self, _r)

        def getFolderLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Roi._op_getFolderLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getFolderLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getFolderLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFolderLinksCountPerOwner(self, _r):
            return _M_ode.model.Roi._op_getFolderLinksCountPerOwner.end(self, _r)

        def linkFolder(self, addition, _ctx=None):
            return _M_ode.model.Roi._op_linkFolder.invoke(self, ((addition, ), _ctx))

        def begin_linkFolder(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_linkFolder.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkFolder(self, _r):
            return _M_ode.model.Roi._op_linkFolder.end(self, _r)

        def addFolderRoiLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Roi._op_addFolderRoiLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFolderRoiLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addFolderRoiLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFolderRoiLinkToBoth(self, _r):
            return _M_ode.model.Roi._op_addFolderRoiLinkToBoth.end(self, _r)

        def findFolderRoiLink(self, removal, _ctx=None):
            return _M_ode.model.Roi._op_findFolderRoiLink.invoke(self, ((removal, ), _ctx))

        def begin_findFolderRoiLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_findFolderRoiLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFolderRoiLink(self, _r):
            return _M_ode.model.Roi._op_findFolderRoiLink.end(self, _r)

        def unlinkFolder(self, removal, _ctx=None):
            return _M_ode.model.Roi._op_unlinkFolder.invoke(self, ((removal, ), _ctx))

        def begin_unlinkFolder(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_unlinkFolder.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkFolder(self, _r):
            return _M_ode.model.Roi._op_unlinkFolder.end(self, _r)

        def removeFolderRoiLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Roi._op_removeFolderRoiLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFolderRoiLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeFolderRoiLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFolderRoiLinkFromBoth(self, _r):
            return _M_ode.model.Roi._op_removeFolderRoiLinkFromBoth.end(self, _r)

        def linkedFolderList(self, _ctx=None):
            return _M_ode.model.Roi._op_linkedFolderList.invoke(self, ((), _ctx))

        def begin_linkedFolderList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_linkedFolderList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedFolderList(self, _r):
            return _M_ode.model.Roi._op_linkedFolderList.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Roi._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Roi._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Roi._op_copyAnnotationLinks.end(self, _r)

        def addRoiAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Roi._op_addRoiAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addRoiAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addRoiAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addRoiAnnotationLink(self, _r):
            return _M_ode.model.Roi._op_addRoiAnnotationLink.end(self, _r)

        def addAllRoiAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Roi._op_addAllRoiAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllRoiAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addAllRoiAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllRoiAnnotationLinkSet(self, _r):
            return _M_ode.model.Roi._op_addAllRoiAnnotationLinkSet.end(self, _r)

        def removeRoiAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Roi._op_removeRoiAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeRoiAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeRoiAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeRoiAnnotationLink(self, _r):
            return _M_ode.model.Roi._op_removeRoiAnnotationLink.end(self, _r)

        def removeAllRoiAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Roi._op_removeAllRoiAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllRoiAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeAllRoiAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllRoiAnnotationLinkSet(self, _r):
            return _M_ode.model.Roi._op_removeAllRoiAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Roi._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Roi._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Roi._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Roi._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Roi._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Roi._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Roi._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Roi._op_linkAnnotation.end(self, _r)

        def addRoiAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Roi._op_addRoiAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addRoiAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_addRoiAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addRoiAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Roi._op_addRoiAnnotationLinkToBoth.end(self, _r)

        def findRoiAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Roi._op_findRoiAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findRoiAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_findRoiAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findRoiAnnotationLink(self, _r):
            return _M_ode.model.Roi._op_findRoiAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Roi._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Roi._op_unlinkAnnotation.end(self, _r)

        def removeRoiAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Roi._op_removeRoiAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeRoiAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_removeRoiAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeRoiAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Roi._op_removeRoiAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Roi._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Roi._op_linkedAnnotationList.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.Roi._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.Roi._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.Roi._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Roi._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.Roi._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.RoiPrx.ice_checkedCast(proxy, '::ode::model::Roi', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.RoiPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Roi'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_RoiPrx = IcePy.defineProxy('::ode::model::Roi', RoiPrx)

    _M_ode.model._t_Roi = IcePy.declareClass('::ode::model::Roi')

    _M_ode.model._t_Roi = IcePy.defineClass('::ode::model::Roi', Roi, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_shapesSeq', (), _M_ode.model._t_RoiShapesSeq, False, 0),
        ('_shapesLoaded', (), IcePy._t_bool, False, 0),
        ('_image', (), _M_ode.model._t_Image, False, 0),
        ('_source', (), _M_ode.model._t_OriginalFile, False, 0),
        ('_folderLinksSeq', (), _M_ode.model._t_RoiFolderLinksSeq, False, 0),
        ('_folderLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_folderLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_RoiAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    Roi._ice_type = _M_ode.model._t_Roi

    Roi._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Roi._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Roi._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Roi._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Roi._op_unloadShapes = IcePy.Operation('unloadShapes', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Roi._op_sizeOfShapes = IcePy.Operation('sizeOfShapes', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Roi._op_copyShapes = IcePy.Operation('copyShapes', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RoiShapesSeq, False, 0), ())
    Roi._op_addShape = IcePy.Operation('addShape', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Shape, False, 0),), (), None, ())
    Roi._op_addAllShapeSet = IcePy.Operation('addAllShapeSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiShapesSeq, False, 0),), (), None, ())
    Roi._op_removeShape = IcePy.Operation('removeShape', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Shape, False, 0),), (), None, ())
    Roi._op_removeAllShapeSet = IcePy.Operation('removeAllShapeSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiShapesSeq, False, 0),), (), None, ())
    Roi._op_clearShapes = IcePy.Operation('clearShapes', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Roi._op_reloadShapes = IcePy.Operation('reloadShapes', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Roi, False, 0),), (), None, ())
    Roi._op_getShape = IcePy.Operation('getShape', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.model._t_Shape, False, 0), ())
    Roi._op_setShape = IcePy.Operation('setShape', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_ode.model._t_Shape, False, 0)), (), ((), _M_ode.model._t_Shape, False, 0), ())
    Roi._op_getPrimaryShape = IcePy.Operation('getPrimaryShape', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Shape, False, 0), ())
    Roi._op_setPrimaryShape = IcePy.Operation('setPrimaryShape', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Shape, False, 0),), (), ((), _M_ode.model._t_Shape, False, 0), ())
    Roi._op_getImage = IcePy.Operation('getImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_Image, False, 0), ())
    Roi._op_setImage = IcePy.Operation('setImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), None, ())
    Roi._op_getSource = IcePy.Operation('getSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFile, False, 0), ())
    Roi._op_setSource = IcePy.Operation('setSource', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), None, ())
    Roi._op_unloadFolderLinks = IcePy.Operation('unloadFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Roi._op_sizeOfFolderLinks = IcePy.Operation('sizeOfFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Roi._op_copyFolderLinks = IcePy.Operation('copyFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RoiFolderLinksSeq, False, 0), ())
    Roi._op_addFolderRoiLink = IcePy.Operation('addFolderRoiLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FolderRoiLink, False, 0),), (), None, ())
    Roi._op_addAllFolderRoiLinkSet = IcePy.Operation('addAllFolderRoiLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiFolderLinksSeq, False, 0),), (), None, ())
    Roi._op_removeFolderRoiLink = IcePy.Operation('removeFolderRoiLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FolderRoiLink, False, 0),), (), None, ())
    Roi._op_removeAllFolderRoiLinkSet = IcePy.Operation('removeAllFolderRoiLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiFolderLinksSeq, False, 0),), (), None, ())
    Roi._op_clearFolderLinks = IcePy.Operation('clearFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Roi._op_reloadFolderLinks = IcePy.Operation('reloadFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Roi, False, 0),), (), None, ())
    Roi._op_getFolderLinksCountPerOwner = IcePy.Operation('getFolderLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Roi._op_linkFolder = IcePy.Operation('linkFolder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Folder, False, 0),), (), ((), _M_ode.model._t_FolderRoiLink, False, 0), ())
    Roi._op_addFolderRoiLinkToBoth = IcePy.Operation('addFolderRoiLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FolderRoiLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Roi._op_findFolderRoiLink = IcePy.Operation('findFolderRoiLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Folder, False, 0),), (), ((), _M_ode.model._t_RoiFolderLinksSeq, False, 0), ())
    Roi._op_unlinkFolder = IcePy.Operation('unlinkFolder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Folder, False, 0),), (), None, ())
    Roi._op_removeFolderRoiLinkFromBoth = IcePy.Operation('removeFolderRoiLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_FolderRoiLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Roi._op_linkedFolderList = IcePy.Operation('linkedFolderList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RoiLinkedFolderSeq, False, 0), ())
    Roi._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Roi._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Roi._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RoiAnnotationLinksSeq, False, 0), ())
    Roi._op_addRoiAnnotationLink = IcePy.Operation('addRoiAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiAnnotationLink, False, 0),), (), None, ())
    Roi._op_addAllRoiAnnotationLinkSet = IcePy.Operation('addAllRoiAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiAnnotationLinksSeq, False, 0),), (), None, ())
    Roi._op_removeRoiAnnotationLink = IcePy.Operation('removeRoiAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiAnnotationLink, False, 0),), (), None, ())
    Roi._op_removeAllRoiAnnotationLinkSet = IcePy.Operation('removeAllRoiAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiAnnotationLinksSeq, False, 0),), (), None, ())
    Roi._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Roi._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Roi, False, 0),), (), None, ())
    Roi._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Roi._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_RoiAnnotationLink, False, 0), ())
    Roi._op_addRoiAnnotationLinkToBoth = IcePy.Operation('addRoiAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Roi._op_findRoiAnnotationLink = IcePy.Operation('findRoiAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_RoiAnnotationLinksSeq, False, 0), ())
    Roi._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Roi._op_removeRoiAnnotationLinkFromBoth = IcePy.Operation('removeRoiAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_RoiAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Roi._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_RoiLinkedAnnotationSeq, False, 0), ())
    Roi._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Roi._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Roi = Roi
    del Roi

    _M_ode.model.RoiPrx = RoiPrx
    del RoiPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
