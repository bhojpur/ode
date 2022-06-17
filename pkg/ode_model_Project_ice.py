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

if 'Dataset' not in _M_ode.model.__dict__:
    _M_ode.model._t_Dataset = IcePy.declareClass('::ode::model::Dataset')
    _M_ode.model._t_DatasetPrx = IcePy.declareProxy('::ode::model::Dataset')

if 'ProjectAnnotationLink' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectAnnotationLink = IcePy.declareClass('::ode::model::ProjectAnnotationLink')
    _M_ode.model._t_ProjectAnnotationLinkPrx = IcePy.declareProxy('::ode::model::ProjectAnnotationLink')

if 'Annotation' not in _M_ode.model.__dict__:
    _M_ode.model._t_Annotation = IcePy.declareClass('::ode::model::Annotation')
    _M_ode.model._t_AnnotationPrx = IcePy.declareProxy('::ode::model::Annotation')

if 'Details' not in _M_ode.model.__dict__:
    _M_ode.model._t_Details = IcePy.declareClass('::ode::model::Details')
    _M_ode.model._t_DetailsPrx = IcePy.declareProxy('::ode::model::Details')

if '_t_ProjectDatasetLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectDatasetLinksSeq = IcePy.defineSequence('::ode::model::ProjectDatasetLinksSeq', (), _M_ode.model._t_ProjectDatasetLink)

if '_t_ProjectLinkedDatasetSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectLinkedDatasetSeq = IcePy.defineSequence('::ode::model::ProjectLinkedDatasetSeq', (), _M_ode.model._t_Dataset)

if '_t_ProjectAnnotationLinksSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectAnnotationLinksSeq = IcePy.defineSequence('::ode::model::ProjectAnnotationLinksSeq', (), _M_ode.model._t_ProjectAnnotationLink)

if '_t_ProjectLinkedAnnotationSeq' not in _M_ode.model.__dict__:
    _M_ode.model._t_ProjectLinkedAnnotationSeq = IcePy.defineSequence('::ode::model::ProjectLinkedAnnotationSeq', (), _M_ode.model._t_Annotation)

if 'Project' not in _M_ode.model.__dict__:
    _M_ode.model.Project = Ice.createTempClass()
    class Project(_M_ode.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _datasetLinksSeq=None, _datasetLinksLoaded=False, _datasetLinksCountPerOwner=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None, _description=None):
            if Ice.getType(self) == _M_ode.model.Project:
                raise RuntimeError('ode.model.Project is an abstract class')
            _M_ode.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._datasetLinksSeq = _datasetLinksSeq
            self._datasetLinksLoaded = _datasetLinksLoaded
            self._datasetLinksCountPerOwner = _datasetLinksCountPerOwner
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::model::IObject', '::ode::model::Project')

        def ice_id(self, current=None):
            return '::ode::model::Project'

        def ice_staticId():
            return '::ode::model::Project'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def unloadDatasetLinks(self, current=None):
            pass

        def sizeOfDatasetLinks(self, current=None):
            pass

        def copyDatasetLinks(self, current=None):
            pass

        def addProjectDatasetLink(self, target, current=None):
            pass

        def addAllProjectDatasetLinkSet(self, targets, current=None):
            pass

        def removeProjectDatasetLink(self, theTarget, current=None):
            pass

        def removeAllProjectDatasetLinkSet(self, targets, current=None):
            pass

        def clearDatasetLinks(self, current=None):
            pass

        def reloadDatasetLinks(self, toCopy, current=None):
            pass

        def getDatasetLinksCountPerOwner(self, current=None):
            pass

        def linkDataset(self, addition, current=None):
            pass

        def addProjectDatasetLinkToBoth(self, link, bothSides, current=None):
            pass

        def findProjectDatasetLink(self, removal, current=None):
            pass

        def unlinkDataset(self, removal, current=None):
            pass

        def removeProjectDatasetLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedDatasetList(self, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addProjectAnnotationLink(self, target, current=None):
            pass

        def addAllProjectAnnotationLinkSet(self, targets, current=None):
            pass

        def removeProjectAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllProjectAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addProjectAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findProjectAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeProjectAnnotationLinkFromBoth(self, link, bothSides, current=None):
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
            return IcePy.stringify(self, _M_ode.model._t_Project)

        __repr__ = __str__

    _M_ode.model.ProjectPrx = Ice.createTempClass()
    class ProjectPrx(_M_ode.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_ode.model.Project._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_ode.model.Project._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_ode.model.Project._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_ode.model.Project._op_setVersion.end(self, _r)

        def unloadDatasetLinks(self, _ctx=None):
            return _M_ode.model.Project._op_unloadDatasetLinks.invoke(self, ((), _ctx))

        def begin_unloadDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_unloadDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadDatasetLinks(self, _r):
            return _M_ode.model.Project._op_unloadDatasetLinks.end(self, _r)

        def sizeOfDatasetLinks(self, _ctx=None):
            return _M_ode.model.Project._op_sizeOfDatasetLinks.invoke(self, ((), _ctx))

        def begin_sizeOfDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_sizeOfDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfDatasetLinks(self, _r):
            return _M_ode.model.Project._op_sizeOfDatasetLinks.end(self, _r)

        def copyDatasetLinks(self, _ctx=None):
            return _M_ode.model.Project._op_copyDatasetLinks.invoke(self, ((), _ctx))

        def begin_copyDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_copyDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyDatasetLinks(self, _r):
            return _M_ode.model.Project._op_copyDatasetLinks.end(self, _r)

        def addProjectDatasetLink(self, target, _ctx=None):
            return _M_ode.model.Project._op_addProjectDatasetLink.invoke(self, ((target, ), _ctx))

        def begin_addProjectDatasetLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_addProjectDatasetLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addProjectDatasetLink(self, _r):
            return _M_ode.model.Project._op_addProjectDatasetLink.end(self, _r)

        def addAllProjectDatasetLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Project._op_addAllProjectDatasetLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllProjectDatasetLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_addAllProjectDatasetLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllProjectDatasetLinkSet(self, _r):
            return _M_ode.model.Project._op_addAllProjectDatasetLinkSet.end(self, _r)

        def removeProjectDatasetLink(self, theTarget, _ctx=None):
            return _M_ode.model.Project._op_removeProjectDatasetLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeProjectDatasetLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_removeProjectDatasetLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeProjectDatasetLink(self, _r):
            return _M_ode.model.Project._op_removeProjectDatasetLink.end(self, _r)

        def removeAllProjectDatasetLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Project._op_removeAllProjectDatasetLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllProjectDatasetLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_removeAllProjectDatasetLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllProjectDatasetLinkSet(self, _r):
            return _M_ode.model.Project._op_removeAllProjectDatasetLinkSet.end(self, _r)

        def clearDatasetLinks(self, _ctx=None):
            return _M_ode.model.Project._op_clearDatasetLinks.invoke(self, ((), _ctx))

        def begin_clearDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_clearDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearDatasetLinks(self, _r):
            return _M_ode.model.Project._op_clearDatasetLinks.end(self, _r)

        def reloadDatasetLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Project._op_reloadDatasetLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadDatasetLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_reloadDatasetLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadDatasetLinks(self, _r):
            return _M_ode.model.Project._op_reloadDatasetLinks.end(self, _r)

        def getDatasetLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Project._op_getDatasetLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getDatasetLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_getDatasetLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDatasetLinksCountPerOwner(self, _r):
            return _M_ode.model.Project._op_getDatasetLinksCountPerOwner.end(self, _r)

        def linkDataset(self, addition, _ctx=None):
            return _M_ode.model.Project._op_linkDataset.invoke(self, ((addition, ), _ctx))

        def begin_linkDataset(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_linkDataset.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkDataset(self, _r):
            return _M_ode.model.Project._op_linkDataset.end(self, _r)

        def addProjectDatasetLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Project._op_addProjectDatasetLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addProjectDatasetLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_addProjectDatasetLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addProjectDatasetLinkToBoth(self, _r):
            return _M_ode.model.Project._op_addProjectDatasetLinkToBoth.end(self, _r)

        def findProjectDatasetLink(self, removal, _ctx=None):
            return _M_ode.model.Project._op_findProjectDatasetLink.invoke(self, ((removal, ), _ctx))

        def begin_findProjectDatasetLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_findProjectDatasetLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findProjectDatasetLink(self, _r):
            return _M_ode.model.Project._op_findProjectDatasetLink.end(self, _r)

        def unlinkDataset(self, removal, _ctx=None):
            return _M_ode.model.Project._op_unlinkDataset.invoke(self, ((removal, ), _ctx))

        def begin_unlinkDataset(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_unlinkDataset.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkDataset(self, _r):
            return _M_ode.model.Project._op_unlinkDataset.end(self, _r)

        def removeProjectDatasetLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Project._op_removeProjectDatasetLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeProjectDatasetLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_removeProjectDatasetLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeProjectDatasetLinkFromBoth(self, _r):
            return _M_ode.model.Project._op_removeProjectDatasetLinkFromBoth.end(self, _r)

        def linkedDatasetList(self, _ctx=None):
            return _M_ode.model.Project._op_linkedDatasetList.invoke(self, ((), _ctx))

        def begin_linkedDatasetList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_linkedDatasetList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedDatasetList(self, _r):
            return _M_ode.model.Project._op_linkedDatasetList.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Project._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_ode.model.Project._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Project._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_ode.model.Project._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Project._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_ode.model.Project._op_copyAnnotationLinks.end(self, _r)

        def addProjectAnnotationLink(self, target, _ctx=None):
            return _M_ode.model.Project._op_addProjectAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addProjectAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_addProjectAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addProjectAnnotationLink(self, _r):
            return _M_ode.model.Project._op_addProjectAnnotationLink.end(self, _r)

        def addAllProjectAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Project._op_addAllProjectAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllProjectAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_addAllProjectAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllProjectAnnotationLinkSet(self, _r):
            return _M_ode.model.Project._op_addAllProjectAnnotationLinkSet.end(self, _r)

        def removeProjectAnnotationLink(self, theTarget, _ctx=None):
            return _M_ode.model.Project._op_removeProjectAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeProjectAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_removeProjectAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeProjectAnnotationLink(self, _r):
            return _M_ode.model.Project._op_removeProjectAnnotationLink.end(self, _r)

        def removeAllProjectAnnotationLinkSet(self, targets, _ctx=None):
            return _M_ode.model.Project._op_removeAllProjectAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllProjectAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_removeAllProjectAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllProjectAnnotationLinkSet(self, _r):
            return _M_ode.model.Project._op_removeAllProjectAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_ode.model.Project._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_ode.model.Project._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_ode.model.Project._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_ode.model.Project._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_ode.model.Project._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_ode.model.Project._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_ode.model.Project._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_ode.model.Project._op_linkAnnotation.end(self, _r)

        def addProjectAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Project._op_addProjectAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addProjectAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_addProjectAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addProjectAnnotationLinkToBoth(self, _r):
            return _M_ode.model.Project._op_addProjectAnnotationLinkToBoth.end(self, _r)

        def findProjectAnnotationLink(self, removal, _ctx=None):
            return _M_ode.model.Project._op_findProjectAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findProjectAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_findProjectAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findProjectAnnotationLink(self, _r):
            return _M_ode.model.Project._op_findProjectAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_ode.model.Project._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_ode.model.Project._op_unlinkAnnotation.end(self, _r)

        def removeProjectAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_ode.model.Project._op_removeProjectAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeProjectAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_removeProjectAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeProjectAnnotationLinkFromBoth(self, _r):
            return _M_ode.model.Project._op_removeProjectAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_ode.model.Project._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_ode.model.Project._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_ode.model.Project._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_ode.model.Project._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_ode.model.Project._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_ode.model.Project._op_setName.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.model.Project._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.model.Project._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_ode.model.Project._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.model.Project._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_ode.model.Project._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.model.ProjectPrx.ice_checkedCast(proxy, '::ode::model::Project', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.model.ProjectPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::model::Project'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.model._t_ProjectPrx = IcePy.defineProxy('::ode::model::Project', ProjectPrx)

    _M_ode.model._t_Project = IcePy.declareClass('::ode::model::Project')

    _M_ode.model._t_Project = IcePy.defineClass('::ode::model::Project', Project, -1, (), True, False, _M_ode.model._t_IObject, (), (
        ('_version', (), _M_ode._t_RInt, False, 0),
        ('_datasetLinksSeq', (), _M_ode.model._t_ProjectDatasetLinksSeq, False, 0),
        ('_datasetLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_datasetLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_annotationLinksSeq', (), _M_ode.model._t_ProjectAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_ode.sys._t_CountMap, False, 0),
        ('_name', (), _M_ode._t_RString, False, 0),
        ('_description', (), _M_ode._t_RString, False, 0)
    ))
    Project._ice_type = _M_ode.model._t_Project

    Project._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RInt, False, 0), ())
    Project._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RInt, False, 0),), (), None, ())
    Project._op_unloadDatasetLinks = IcePy.Operation('unloadDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Project._op_sizeOfDatasetLinks = IcePy.Operation('sizeOfDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Project._op_copyDatasetLinks = IcePy.Operation('copyDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ProjectDatasetLinksSeq, False, 0), ())
    Project._op_addProjectDatasetLink = IcePy.Operation('addProjectDatasetLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0),), (), None, ())
    Project._op_addAllProjectDatasetLinkSet = IcePy.Operation('addAllProjectDatasetLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLinksSeq, False, 0),), (), None, ())
    Project._op_removeProjectDatasetLink = IcePy.Operation('removeProjectDatasetLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0),), (), None, ())
    Project._op_removeAllProjectDatasetLinkSet = IcePy.Operation('removeAllProjectDatasetLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLinksSeq, False, 0),), (), None, ())
    Project._op_clearDatasetLinks = IcePy.Operation('clearDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Project._op_reloadDatasetLinks = IcePy.Operation('reloadDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Project, False, 0),), (), None, ())
    Project._op_getDatasetLinksCountPerOwner = IcePy.Operation('getDatasetLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Project._op_linkDataset = IcePy.Operation('linkDataset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Dataset, False, 0),), (), ((), _M_ode.model._t_ProjectDatasetLink, False, 0), ())
    Project._op_addProjectDatasetLinkToBoth = IcePy.Operation('addProjectDatasetLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Project._op_findProjectDatasetLink = IcePy.Operation('findProjectDatasetLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Dataset, False, 0),), (), ((), _M_ode.model._t_ProjectDatasetLinksSeq, False, 0), ())
    Project._op_unlinkDataset = IcePy.Operation('unlinkDataset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Dataset, False, 0),), (), None, ())
    Project._op_removeProjectDatasetLinkFromBoth = IcePy.Operation('removeProjectDatasetLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectDatasetLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Project._op_linkedDatasetList = IcePy.Operation('linkedDatasetList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ProjectLinkedDatasetSeq, False, 0), ())
    Project._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Project._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Project._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ProjectAnnotationLinksSeq, False, 0), ())
    Project._op_addProjectAnnotationLink = IcePy.Operation('addProjectAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectAnnotationLink, False, 0),), (), None, ())
    Project._op_addAllProjectAnnotationLinkSet = IcePy.Operation('addAllProjectAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectAnnotationLinksSeq, False, 0),), (), None, ())
    Project._op_removeProjectAnnotationLink = IcePy.Operation('removeProjectAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectAnnotationLink, False, 0),), (), None, ())
    Project._op_removeAllProjectAnnotationLinkSet = IcePy.Operation('removeAllProjectAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectAnnotationLinksSeq, False, 0),), (), None, ())
    Project._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Project._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Project, False, 0),), (), None, ())
    Project._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.sys._t_CountMap, False, 0), ())
    Project._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ProjectAnnotationLink, False, 0), ())
    Project._op_addProjectAnnotationLinkToBoth = IcePy.Operation('addProjectAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Project._op_findProjectAnnotationLink = IcePy.Operation('findProjectAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), ((), _M_ode.model._t_ProjectAnnotationLinksSeq, False, 0), ())
    Project._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Annotation, False, 0),), (), None, ())
    Project._op_removeProjectAnnotationLinkFromBoth = IcePy.Operation('removeProjectAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ProjectAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Project._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_ProjectLinkedAnnotationSeq, False, 0), ())
    Project._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Project._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())
    Project._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode._t_RString, False, 0), ())
    Project._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode._t_RString, False, 0),), (), None, ())

    _M_ode.model.Project = Project
    del Project

    _M_ode.model.ProjectPrx = ProjectPrx
    del ProjectPrx

# End of module ode.model

__name__ = 'ode'

# End of module ode
