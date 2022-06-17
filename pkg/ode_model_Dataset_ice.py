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

if 'ProjectDatasetLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectDatasetLink = IcePy.declareClass('::ode::model::ProjectDatasetLink')
    _M_ode.model._t_ProjectDatasetLinkPrx = IcePy.declareProxy('::ode::model::ProjectDatasetLink')

if 'Project' not in _M_ode.model.__dict__:
    _M_ode.model._t_Project = IcePy.declareClass('::ode::model::Project')
    _M_ode.model._t_ProjectPrx = IcePy.declareProxy('::ode::model::Project')

if 'DatasetImageLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetImageLink = IcePy.declareClass('::ode::model::DatasetImageLink')
    _M_ode.model._t_DatasetImageLinkPrx = IcePy.declareProxy('::ode::model::DatasetImageLink')

if 'Image' not in _M_ode.model.__dict__:
    _M_ode.model._t_Image = IcePy.declareClass('::ode::model::Image')
    _M_ode.model._t_ImagePrx = IcePy.declareProxy('::ode::model::Image')

if 'DatasetAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetAnnotationLink = IcePy.declareClass('::ode::model::DatasetAnnotationLink')
    _M_ode.model._t_DatasetAnnotationLinkPrx = IcePy.declareProxy('::ode::model::DatasetAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_DatasetProjectLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetProjectLinksSeq = IcePy.defineSequence('::ode::model::DatasetProjectLinksSeq', (), _M_ode.model._t_ProjectDatasetLink)

if '_t_DatasetLinkedProjectSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetLinkedProjectSeq = IcePy.defineSequence('::ode::model::DatasetLinkedProjectSeq', (), _M_ode.model._t_Project)

if '_t_DatasetImageLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetImageLinksSeq = IcePy.defineSequence('::ode::model::DatasetImageLinksSeq', (), _M_ode.model._t_DatasetImageLink)

if '_t_DatasetLinkedImageSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetLinkedImageSeq = IcePy.defineSequence('::ode::model::DatasetLinkedImageSeq', (), _M_ode.model._t_Image)

if '_t_DatasetAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetAnnotationLinksSeq = IcePy.defineSequence('::ode::model::DatasetAnnotationLinksSeq', (), _M_ode.model._t_DatasetAnnotationLink)

if '_t_DatasetLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_DatasetLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::DatasetLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Dataset' not in _M_ode.model.__dict__:
    _M_ode.model.Dataset = Ice.createTempClass()
    class Dataset(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _projectLinksSeq=None, _projectLinksLoaded=False, _projectLinksCountPerOwner=None, _imageLinksSeq=None, _imageLinksLoaded=False, _imageLinksCountPerOwner=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None, _description=None):
            if Ice.getType(self) == _M_ode.model.Dataset:
                raise RuntimeError('ode.model.Dataset is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._projectLinksSeq = _projectLinksSeq
            self._projectLinksLoaded = _projectLinksLoaded
            self._projectLinksCountPerOwner = _projectLinksCountPerOwner
            self._imageLinksSeq = _imageLinksSeq
            self._imageLinksLoaded = _imageLinksLoaded
            self._imageLinksCountPerOwner = _imageLinksCountPerOwner
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::Dataset', '::ode::model::IObject')

        def ice_id(self, current=None):
            return '::ode::model::Dataset'

        def ice_staticId():
            return '::ode::model::Dataset'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def unloadProjectLinks(self, current=None):
            pass

        def sizeOfProjectLinks(self, current=None):
            pass

        def copyProjectLinks(self, current=None):
            pass

        def addProjectDatasetLink(self, target, current=None):
            pass

        def addAllProjectDatasetLinkSet(self, targets, current=None):
            pass

        def removeProjectDatasetLink(self, theTarget, current=None):
            pass

        def removeAllProjectDatasetLinkSet(self, targets, current=None):
            pass

        def clearProjectLinks(self, current=None):
            pass

        def reloadProjectLinks(self, toCopy, current=None):
            pass

        def getProjectLinksCountPerOwner(self, current=None):
            pass

        def linkProject(self, addition, current=None):
            pass

        def addProjectDatasetLinkToBoth(self, link, bothSides, current=None):
            pass

        def findProjectDatasetLink(self, removal, current=None):
            pass

        def unlinkProject(self, removal, current=None):
            pass

        def removeProjectDatasetLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedProjectList(self, current=None):
            pass

        def unloadImageLinks(self, current=None):
            pass

        def sizeOfImageLinks(self, current=None):
            pass

        def copyImageLinks(self, current=None):
            pass

        def addDatasetImageLink(self, target, current=None):
            pass

        def addAllDatasetImageLinkSet(self, targets, current=None):
            pass

        def removeDatasetImageLink(self, theTarget, current=None):
            pass

        def removeAllDatasetImageLinkSet(self, targets, current=None):
            pass

        def clearImageLinks(self, current=None):
            pass

        def reloadImageLinks(self, toCopy, current=None):
            pass

        def getImageLinksCountPerOwner(self, current=None):
            pass

        def linkImage(self, addition, current=None):
            pass

        def addDatasetImageLinkToBoth(self, link, bothSides, current=None):
            pass

        def findDatasetImageLink(self, removal, current=None):
            pass

        def unlinkImage(self, removal, current=None):
            pass

        def removeDatasetImageLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedImageList(self, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addDatasetAnnotationLink(self, target, current=None):
            pass

        def addAllDatasetAnnotationLinkSet(self, targets, current=None):
            pass

        def removeDatasetAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllDatasetAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addDatasetAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findDatasetAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeDatasetAnnotationLinkFromBoth(self, link, bothSides, current=None):
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
            return IcePy.stringify(self, _M_ode.model._t_Dataset)

        __repr__ = __str__

    _M_ode.model.DatasetPrx = Ice.createTempClass()
    class DatasetPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Dataset._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Dataset._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Dataset._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Dataset._op_setVersion.end(self, _r)

        def unloadProjectLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_unloadProjectLinks.invoke(self, ((), _ctx))

        def begin_unloadProjectLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_unloadProjectLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadProjectLinks(self, _r):
            return _M_ode.model.Dataset._op_unloadProjectLinks.end(self, _r)

        def sizeOfProjectLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_sizeOfProjectLinks.invoke(self, ((), _ctx))

        def begin_sizeOfProjectLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_sizeOfProjectLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfProjectLinks(self, _r):
            return _M_ode.model.Dataset._op_sizeOfProjectLinks.end(self, _r)

        def copyProjectLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_copyProjectLinks.invoke(self, ((), _ctx))

        def begin_copyProjectLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_copyProjectLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyProjectLinks(self, _r):
            return _M_ode.model.Dataset._op_copyProjectLinks.end(self, _r)

        def addProjectDatasetLink(self, target, _ctx=None):
            return _M_ode.model.Dataset._op_addProjectDatasetLink.invoke(self, ((target, ), _ctx))

        def begin_addProjectDatasetLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addProjectDatasetLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addProjectDatasetLink(self, _r):
            return _M_ode.model.Dataset._op_addProjectDatasetLink.end(self, _r)

        def addAllProjectDatasetLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Dataset._op_addAllProjectDatasetLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllProjectDatasetLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addAllProjectDatasetLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllProjectDatasetLinkSet(self, _r):
            return _M_ode.model.Dataset._op_addAllProjectDatasetLinkSet.end(self, _r)

        def removeProjectDatasetLink(self, theTarget, _ctx=None):
            return _M_ode.model.Dataset._op_removeProjectDatasetLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeProjectDatasetLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeProjectDatasetLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeProjectDatasetLink(self, _r):
            return _M_ode.model.Dataset._op_removeProjectDatasetLink.end(self, _r)

        def removeAllProjectDatasetLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Dataset._op_removeAllProjectDatasetLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllProjectDatasetLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeAllProjectDatasetLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllProjectDatasetLinkSet(self, _r):
            return _M_ode.model.Dataset._op_removeAllProjectDatasetLinkSet.end(self, _r)

        def clearProjectLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_clearProjectLinks.invoke(self, ((), _ctx))

        def begin_clearProjectLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_clearProjectLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearProjectLinks(self, _r):
            return _M_ode.model.Dataset._op_clearProjectLinks.end(self, _r)

        def reloadProjectLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Dataset._op_reloadProjectLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadProjectLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_reloadProjectLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadProjectLinks(self, _r):
            return _M_ode.model.Dataset._op_reloadProjectLinks.end(self, _r)

        def getProjectLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Dataset._op_getProjectLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getProjectLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_getProjectLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getProjectLinksCountPerOwner(self, _r):
            return _M_ode.model.Dataset._op_getProjectLinksCountPerOwner.end(self, _r)

        def linkProject(self, addition, _ctx=None):
            return _M_ode.model.Dataset._op_linkProject.invoke(self, ((addition, ), _ctx))

        def begin_linkProject(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_linkProject.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkProject(self, _r):
            return _M_ode.model.Dataset._op_linkProject.end(self, _r)

        def addProjectDatasetLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Dataset._op_addProjectDatasetLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addProjectDatasetLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addProjectDatasetLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addProjectDatasetLinkToBoth(self, _r):
            return _M_ode.model.Dataset._op_addProjectDatasetLinkToBoth.end(self, _r)

        def findProjectDatasetLink(self, removal, _ctx=None):
            return _M_ode.model.Dataset._op_findProjectDatasetLink.invoke(self, ((removal, ), _ctx))

        def begin_findProjectDatasetLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_findProjectDatasetLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findProjectDatasetLink(self, _r):
            return _M_ode.model.Dataset._op_findProjectDatasetLink.end(self, _r)

        def unlinkProject(self, removal, _ctx=None):
            return _M_ode.model.Dataset._op_unlinkProject.invoke(self, ((removal, ), _ctx))

        def begin_unlinkProject(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_unlinkProject.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkProject(self, _r):
            return _M_ode.model.Dataset._op_unlinkProject.end(self, _r)

        def removeProjectDatasetLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Dataset._op_removeProjectDatasetLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeProjectDatasetLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeProjectDatasetLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeProjectDatasetLinkFromBoth(self, _r):
            return _M_ode.model.Dataset._op_removeProjectDatasetLinkFromBoth.end(self, _r)

        def linkedProjectList(self, _ctx=None):
            return _M_ode.model.Dataset._op_linkedProjectList.invoke(self, ((), _ctx))

        def begin_linkedProjectList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_linkedProjectList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedProjectList(self, _r):
            return _M_ode.model.Dataset._op_linkedProjectList.end(self, _r)

        def unloadImageLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_unloadImageLinks.invoke(self, ((), _ctx))

        def begin_unloadImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_unloadImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadImageLinks(self, _r):
            return _M_ode.model.Dataset._op_unloadImageLinks.end(self, _r)

        def sizeOfImageLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_sizeOfImageLinks.invoke(self, ((), _ctx))

        def begin_sizeOfImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_sizeOfImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfImageLinks(self, _r):
            return _M_ode.model.Dataset._op_sizeOfImageLinks.end(self, _r)

        def copyImageLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_copyImageLinks.invoke(self, ((), _ctx))

        def begin_copyImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_copyImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyImageLinks(self, _r):
            return _M_ode.model.Dataset._op_copyImageLinks.end(self, _r)

        def addDatasetImageLink(self, target, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetImageLink.invoke(self, ((target, ), _ctx))

        def begin_addDatasetImageLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetImageLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addDatasetImageLink(self, _r):
            return _M_ode.model.Dataset._op_addDatasetImageLink.end(self, _r)

        def addAllDatasetImageLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Dataset._op_addAllDatasetImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllDatasetImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addAllDatasetImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllDatasetImageLinkSet(self, _r):
            return _M_ode.model.Dataset._op_addAllDatasetImageLinkSet.end(self, _r)

        def removeDatasetImageLink(self, theTarget, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetImageLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeDatasetImageLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetImageLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeDatasetImageLink(self, _r):
            return _M_ode.model.Dataset._op_removeDatasetImageLink.end(self, _r)

        def removeAllDatasetImageLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Dataset._op_removeAllDatasetImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllDatasetImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeAllDatasetImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllDatasetImageLinkSet(self, _r):
            return _M_ode.model.Dataset._op_removeAllDatasetImageLinkSet.end(self, _r)

        def clearImageLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_clearImageLinks.invoke(self, ((), _ctx))

        def begin_clearImageLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_clearImageLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearImageLinks(self, _r):
            return _M_ode.model.Dataset._op_clearImageLinks.end(self, _r)

        def reloadImageLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Dataset._op_reloadImageLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadImageLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_reloadImageLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadImageLinks(self, _r):
            return _M_ode.model.Dataset._op_reloadImageLinks.end(self, _r)

        def getImageLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Dataset._op_getImageLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getImageLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_getImageLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImageLinksCountPerOwner(self, _r):
            return _M_ode.model.Dataset._op_getImageLinksCountPerOwner.end(self, _r)

        def linkImage(self, addition, _ctx=None):
            return _M_ode.model.Dataset._op_linkImage.invoke(self, ((addition, ), _ctx))

        def begin_linkImage(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_linkImage.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkImage(self, _r):
            return _M_ode.model.Dataset._op_linkImage.end(self, _r)

        def addDatasetImageLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetImageLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addDatasetImageLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetImageLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addDatasetImageLinkToBoth(self, _r):
            return _M_ode.model.Dataset._op_addDatasetImageLinkToBoth.end(self, _r)

        def findDatasetImageLink(self, removal, _ctx=None):
            return _M_ode.model.Dataset._op_findDatasetImageLink.invoke(self, ((removal, ), _ctx))

        def begin_findDatasetImageLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_findDatasetImageLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findDatasetImageLink(self, _r):
            return _M_ode.model.Dataset._op_findDatasetImageLink.end(self, _r)

        def unlinkImage(self, removal, _ctx=None):
            return _M_ode.model.Dataset._op_unlinkImage.invoke(self, ((removal, ), _ctx))

        def begin_unlinkImage(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_unlinkImage.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkImage(self, _r):
            return _M_ode.model.Dataset._op_unlinkImage.end(self, _r)

        def removeDatasetImageLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetImageLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeDatasetImageLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetImageLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeDatasetImageLinkFromBoth(self, _r):
            return _M_ode.model.Dataset._op_removeDatasetImageLinkFromBoth.end(self, _r)

        def linkedImageList(self, _ctx=None):
            return _M_ode.model.Dataset._op_linkedImageList.invoke(self, ((), _ctx))

        def begin_linkedImageList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_linkedImageList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedImageList(self, _r):
            return _M_ode.model.Dataset._op_linkedImageList.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Dataset._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Dataset._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Dataset._op_copyAnnotationLinks.end(self, _r)

        def addDatasetAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addDatasetAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addDatasetAnnotationLink(self, _r):
            return _M_ode.model.Dataset._op_addDatasetAnnotationLink.end(self, _r)

        def addAllDatasetAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Dataset._op_addAllDatasetAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllDatasetAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addAllDatasetAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllDatasetAnnotationLinkSet(self, _r):
            return _M_ode.model.Dataset._op_addAllDatasetAnnotationLinkSet.end(self, _r)

        def removeDatasetAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeDatasetAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeDatasetAnnotationLink(self, _r):
            return _M_ode.model.Dataset._op_removeDatasetAnnotationLink.end(self, _r)

        def removeAllDatasetAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Dataset._op_removeAllDatasetAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllDatasetAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeAllDatasetAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllDatasetAnnotationLinkSet(self, _r):
            return _M_ode.model.Dataset._op_removeAllDatasetAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Dataset._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Dataset._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Dataset._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Dataset._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Dataset._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Dataset._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Dataset._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Dataset._op_linkAnnotation.end(self, _r)

        def addDatasetAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addDatasetAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_addDatasetAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addDatasetAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Dataset._op_addDatasetAnnotationLinkToBoth.end(self, _r)

        def findDatasetAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Dataset._op_findDatasetAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findDatasetAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_findDatasetAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findDatasetAnnotationLink(self, _r):
            return _M_ode.model.Dataset._op_findDatasetAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Dataset._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Dataset._op_unlinkAnnotation.end(self, _r)

        def removeDatasetAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeDatasetAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_removeDatasetAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeDatasetAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Dataset._op_removeDatasetAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Dataset._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Dataset._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.Dataset._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.Dataset._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.Dataset._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.Dataset._op_setName.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.Dataset._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.Dataset._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.Dataset._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Dataset._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.Dataset._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.DatasetPrx.ice_checkedCast(proxy, '::ode::model::Dataset', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.DatasetPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Dataset'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_DatasetPrx = IcePy.defineProxy('::ode::model::Dataset', DatasetPrx)

    _M_ode.model._t_Dataset = IcePy.declareClass('::ode::model::Dataset')

    _M_ode.model._t_Dataset = IcePy.defineClass('::ode::model::Dataset', Dataset, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_projectLinksSeq', (), _M_ode.model._t_DatasetProjectLinksSeq, False, 0),
        ('_projectLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_projectLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_imageLinksSeq', (), _M_ode.model._t_DatasetImageLinksSeq, False, 0),
        ('_imageLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_imageLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_DatasetAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    Dataset._ice_type = _M_ode.model._t_Dataset

    Dataset._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Dataset._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Dataset._op_unloadProjectLinks = IcePy.Operation('unloadProjectLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Dataset._op_sizeOfProjectLinks = IcePy.Operation('sizeOfProjectLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Dataset._op_copyProjectLinks = IcePy.Operation('copyProjectLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DatasetProjectLinksSeq, False, 0), ())
    Dataset._op_addProjectDatasetLink = IcePy.Operation('addProjectDatasetLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0),), (), None, ())
    Dataset._op_addAllProjectDatasetLinkSet = IcePy.Operation('addAllProjectDatasetLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetProjectLinksSeq, False, 0),), (), None, ())
    Dataset._op_removeProjectDatasetLink = IcePy.Operation('removeProjectDatasetLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0),), (), None, ())
    Dataset._op_removeAllProjectDatasetLinkSet = IcePy.Operation('removeAllProjectDatasetLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetProjectLinksSeq, False, 0),), (), None, ())
    Dataset._op_clearProjectLinks = IcePy.Operation('clearProjectLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Dataset._op_reloadProjectLinks = IcePy.Operation('reloadProjectLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Dataset, False, 0),), (), None, ())
    Dataset._op_getProjectLinksCountPerOwner = IcePy.Operation('getProjectLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Dataset._op_linkProject = IcePy.Operation('linkProject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Project, False, 0),), (), ((), _M_ode.model._t_ProjectDatasetLink, False, 0), ())
    Dataset._op_addProjectDatasetLinkToBoth = IcePy.Operation('addProjectDatasetLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Dataset._op_findProjectDatasetLink = IcePy.Operation('findProjectDatasetLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Project, False, 0),), (), ((), _M_ode.model._t_DatasetProjectLinksSeq, False, 0), ())
    Dataset._op_unlinkProject = IcePy.Operation('unlinkProject', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Project, False, 0),), (), None, ())
    Dataset._op_removeProjectDatasetLinkFromBoth = IcePy.Operation('removeProjectDatasetLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Dataset._op_linkedProjectList = IcePy.Operation('linkedProjectList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DatasetLinkedProjectSeq, False, 0), ())
    Dataset._op_unloadImageLinks = IcePy.Operation('unloadImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Dataset._op_sizeOfImageLinks = IcePy.Operation('sizeOfImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Dataset._op_copyImageLinks = IcePy.Operation('copyImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DatasetImageLinksSeq, False, 0), ())
    Dataset._op_addDatasetImageLink = IcePy.Operation('addDatasetImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetImageLink, False, 0),), (), None, ())
    Dataset._op_addAllDatasetImageLinkSet = IcePy.Operation('addAllDatasetImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetImageLinksSeq, False, 0),), (), None, ())
    Dataset._op_removeDatasetImageLink = IcePy.Operation('removeDatasetImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetImageLink, False, 0),), (), None, ())
    Dataset._op_removeAllDatasetImageLinkSet = IcePy.Operation('removeAllDatasetImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetImageLinksSeq, False, 0),), (), None, ())
    Dataset._op_clearImageLinks = IcePy.Operation('clearImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Dataset._op_reloadImageLinks = IcePy.Operation('reloadImageLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Dataset, False, 0),), (), None, ())
    Dataset._op_getImageLinksCountPerOwner = IcePy.Operation('getImageLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Dataset._op_linkImage = IcePy.Operation('linkImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), ((), _M_ode.model._t_DatasetImageLink, False, 0), ())
    Dataset._op_addDatasetImageLinkToBoth = IcePy.Operation('addDatasetImageLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Dataset._op_findDatasetImageLink = IcePy.Operation('findDatasetImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), ((), _M_ode.model._t_DatasetImageLinksSeq, False, 0), ())
    Dataset._op_unlinkImage = IcePy.Operation('unlinkImage', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Image, False, 0),), (), None, ())
    Dataset._op_removeDatasetImageLinkFromBoth = IcePy.Operation('removeDatasetImageLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Dataset._op_linkedImageList = IcePy.Operation('linkedImageList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DatasetLinkedImageSeq, False, 0), ())
    Dataset._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Dataset._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Dataset._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DatasetAnnotationLinksSeq, False, 0), ())
    Dataset._op_addDatasetAnnotationLink = IcePy.Operation('addDatasetAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetAnnotationLink, False, 0),), (), None, ())
    Dataset._op_addAllDatasetAnnotationLinkSet = IcePy.Operation('addAllDatasetAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetAnnotationLinksSeq, False, 0),), (), None, ())
    Dataset._op_removeDatasetAnnotationLink = IcePy.Operation('removeDatasetAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetAnnotationLink, False, 0),), (), None, ())
    Dataset._op_removeAllDatasetAnnotationLinkSet = IcePy.Operation('removeAllDatasetAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetAnnotationLinksSeq, False, 0),), (), None, ())
    Dataset._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Dataset._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Dataset, False, 0),), (), None, ())
    Dataset._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Dataset._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_DatasetAnnotationLink, False, 0), ())
    Dataset._op_addDatasetAnnotationLinkToBoth = IcePy.Operation('addDatasetAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Dataset._op_findDatasetAnnotationLink = IcePy.Operation('findDatasetAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_DatasetAnnotationLinksSeq, False, 0), ())
    Dataset._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Dataset._op_removeDatasetAnnotationLinkFromBoth = IcePy.Operation('removeDatasetAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_DatasetAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Dataset._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_DatasetLinkedAnnotationSeq, False, 0), ())
    Dataset._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Dataset._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Dataset._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Dataset._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Dataset = Dataset
    del Dataset

    _M_ode.model.DatasetPrx = DatasetPrx
    del DatasetPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
