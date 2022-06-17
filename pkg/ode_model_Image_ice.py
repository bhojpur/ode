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

if 'Format' not in _M_omero.model.__dict__:
    _M_omero.model._t_Format = IcePy.declareClass('::omero::model::Format')
    _M_omero.model._t_FormatPrx = IcePy.declareProxy('::omero::model::Format')

if 'ImagingEnvironment' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImagingEnvironment = IcePy.declareClass('::omero::model::ImagingEnvironment')
    _M_omero.model._t_ImagingEnvironmentPrx = IcePy.declareProxy('::omero::model::ImagingEnvironment')

if 'ObjectiveSettings' not in _M_omero.model.__dict__:
    _M_omero.model._t_ObjectiveSettings = IcePy.declareClass('::omero::model::ObjectiveSettings')
    _M_omero.model._t_ObjectiveSettingsPrx = IcePy.declareProxy('::omero::model::ObjectiveSettings')

if 'Instrument' not in _M_omero.model.__dict__:
    _M_omero.model._t_Instrument = IcePy.declareClass('::omero::model::Instrument')
    _M_omero.model._t_InstrumentPrx = IcePy.declareProxy('::omero::model::Instrument')

if 'StageLabel' not in _M_omero.model.__dict__:
    _M_omero.model._t_StageLabel = IcePy.declareClass('::omero::model::StageLabel')
    _M_omero.model._t_StageLabelPrx = IcePy.declareProxy('::omero::model::StageLabel')

if 'Experiment' not in _M_omero.model.__dict__:
    _M_omero.model._t_Experiment = IcePy.declareClass('::omero::model::Experiment')
    _M_omero.model._t_ExperimentPrx = IcePy.declareProxy('::omero::model::Experiment')

if 'Pixels' not in _M_omero.model.__dict__:
    _M_omero.model._t_Pixels = IcePy.declareClass('::omero::model::Pixels')
    _M_omero.model._t_PixelsPrx = IcePy.declareProxy('::omero::model::Pixels')

if 'WellSample' not in _M_omero.model.__dict__:
    _M_omero.model._t_WellSample = IcePy.declareClass('::omero::model::WellSample')
    _M_omero.model._t_WellSamplePrx = IcePy.declareProxy('::omero::model::WellSample')

if 'Roi' not in _M_omero.model.__dict__:
    _M_omero.model._t_Roi = IcePy.declareClass('::omero::model::Roi')
    _M_omero.model._t_RoiPrx = IcePy.declareProxy('::omero::model::Roi')

if 'DatasetImageLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_DatasetImageLink = IcePy.declareClass('::omero::model::DatasetImageLink')
    _M_omero.model._t_DatasetImageLinkPrx = IcePy.declareProxy('::omero::model::DatasetImageLink')

if 'Dataset' not in _M_omero.model.__dict__:
    _M_omero.model._t_Dataset = IcePy.declareClass('::omero::model::Dataset')
    _M_omero.model._t_DatasetPrx = IcePy.declareProxy('::omero::model::Dataset')

if 'FolderImageLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_FolderImageLink = IcePy.declareClass('::omero::model::FolderImageLink')
    _M_omero.model._t_FolderImageLinkPrx = IcePy.declareProxy('::omero::model::FolderImageLink')

if 'Folder' not in _M_omero.model.__dict__:
    _M_omero.model._t_Folder = IcePy.declareClass('::omero::model::Folder')
    _M_omero.model._t_FolderPrx = IcePy.declareProxy('::omero::model::Folder')

if 'Fileset' not in _M_omero.model.__dict__:
    _M_omero.model._t_Fileset = IcePy.declareClass('::omero::model::Fileset')
    _M_omero.model._t_FilesetPrx = IcePy.declareProxy('::omero::model::Fileset')

if 'ImageAnnotationLink' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageAnnotationLink = IcePy.declareClass('::omero::model::ImageAnnotationLink')
    _M_omero.model._t_ImageAnnotationLinkPrx = IcePy.declareProxy('::omero::model::ImageAnnotationLink')

if 'Annotation' not in _M_omero.model.__dict__:
    _M_omero.model._t_Annotation = IcePy.declareClass('::omero::model::Annotation')
    _M_omero.model._t_AnnotationPrx = IcePy.declareProxy('::omero::model::Annotation')

if 'Details' not in _M_omero.model.__dict__:
    _M_omero.model._t_Details = IcePy.declareClass('::omero::model::Details')
    _M_omero.model._t_DetailsPrx = IcePy.declareProxy('::omero::model::Details')

if '_t_ImagePixelsSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImagePixelsSeq = IcePy.defineSequence('::omero::model::ImagePixelsSeq', (), _M_omero.model._t_Pixels)

if '_t_ImageWellSamplesSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageWellSamplesSeq = IcePy.defineSequence('::omero::model::ImageWellSamplesSeq', (), _M_omero.model._t_WellSample)

if '_t_ImageRoisSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageRoisSeq = IcePy.defineSequence('::omero::model::ImageRoisSeq', (), _M_omero.model._t_Roi)

if '_t_ImageDatasetLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageDatasetLinksSeq = IcePy.defineSequence('::omero::model::ImageDatasetLinksSeq', (), _M_omero.model._t_DatasetImageLink)

if '_t_ImageLinkedDatasetSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageLinkedDatasetSeq = IcePy.defineSequence('::omero::model::ImageLinkedDatasetSeq', (), _M_omero.model._t_Dataset)

if '_t_ImageFolderLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageFolderLinksSeq = IcePy.defineSequence('::omero::model::ImageFolderLinksSeq', (), _M_omero.model._t_FolderImageLink)

if '_t_ImageLinkedFolderSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageLinkedFolderSeq = IcePy.defineSequence('::omero::model::ImageLinkedFolderSeq', (), _M_omero.model._t_Folder)

if '_t_ImageAnnotationLinksSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageAnnotationLinksSeq = IcePy.defineSequence('::omero::model::ImageAnnotationLinksSeq', (), _M_omero.model._t_ImageAnnotationLink)

if '_t_ImageLinkedAnnotationSeq' not in _M_omero.model.__dict__:
    _M_omero.model._t_ImageLinkedAnnotationSeq = IcePy.defineSequence('::omero::model::ImageLinkedAnnotationSeq', (), _M_omero.model._t_Annotation)

if 'Image' not in _M_omero.model.__dict__:
    _M_omero.model.Image = Ice.createTempClass()
    class Image(_M_omero.model.IObject):
        def __init__(self, _id=None, _details=None, _loaded=False, _version=None, _series=None, _acquisitionDate=None, _archived=None, _partial=None, _format=None, _imagingEnvironment=None, _objectiveSettings=None, _instrument=None, _stageLabel=None, _experiment=None, _pixelsSeq=None, _pixelsLoaded=False, _wellSamplesSeq=None, _wellSamplesLoaded=False, _roisSeq=None, _roisLoaded=False, _datasetLinksSeq=None, _datasetLinksLoaded=False, _datasetLinksCountPerOwner=None, _folderLinksSeq=None, _folderLinksLoaded=False, _folderLinksCountPerOwner=None, _fileset=None, _annotationLinksSeq=None, _annotationLinksLoaded=False, _annotationLinksCountPerOwner=None, _name=None, _description=None):
            if Ice.getType(self) == _M_omero.model.Image:
                raise RuntimeError('omero.model.Image is an abstract class')
            _M_omero.model.IObject.__init__(self, _id, _details, _loaded)
            self._version = _version
            self._series = _series
            self._acquisitionDate = _acquisitionDate
            self._archived = _archived
            self._partial = _partial
            self._format = _format
            self._imagingEnvironment = _imagingEnvironment
            self._objectiveSettings = _objectiveSettings
            self._instrument = _instrument
            self._stageLabel = _stageLabel
            self._experiment = _experiment
            self._pixelsSeq = _pixelsSeq
            self._pixelsLoaded = _pixelsLoaded
            self._wellSamplesSeq = _wellSamplesSeq
            self._wellSamplesLoaded = _wellSamplesLoaded
            self._roisSeq = _roisSeq
            self._roisLoaded = _roisLoaded
            self._datasetLinksSeq = _datasetLinksSeq
            self._datasetLinksLoaded = _datasetLinksLoaded
            self._datasetLinksCountPerOwner = _datasetLinksCountPerOwner
            self._folderLinksSeq = _folderLinksSeq
            self._folderLinksLoaded = _folderLinksLoaded
            self._folderLinksCountPerOwner = _folderLinksCountPerOwner
            self._fileset = _fileset
            self._annotationLinksSeq = _annotationLinksSeq
            self._annotationLinksLoaded = _annotationLinksLoaded
            self._annotationLinksCountPerOwner = _annotationLinksCountPerOwner
            self._name = _name
            self._description = _description

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::omero::model::IObject', '::omero::model::Image')

        def ice_id(self, current=None):
            return '::omero::model::Image'

        def ice_staticId():
            return '::omero::model::Image'
        ice_staticId = staticmethod(ice_staticId)

        def getVersion(self, current=None):
            pass

        def setVersion(self, theVersion, current=None):
            pass

        def getSeries(self, current=None):
            pass

        def setSeries(self, theSeries, current=None):
            pass

        def getAcquisitionDate(self, current=None):
            pass

        def setAcquisitionDate(self, theAcquisitionDate, current=None):
            pass

        def getArchived(self, current=None):
            pass

        def setArchived(self, theArchived, current=None):
            pass

        def getPartial(self, current=None):
            pass

        def setPartial(self, thePartial, current=None):
            pass

        def getFormat(self, current=None):
            pass

        def setFormat(self, theFormat, current=None):
            pass

        def getImagingEnvironment(self, current=None):
            pass

        def setImagingEnvironment(self, theImagingEnvironment, current=None):
            pass

        def getObjectiveSettings(self, current=None):
            pass

        def setObjectiveSettings(self, theObjectiveSettings, current=None):
            pass

        def getInstrument(self, current=None):
            pass

        def setInstrument(self, theInstrument, current=None):
            pass

        def getStageLabel(self, current=None):
            pass

        def setStageLabel(self, theStageLabel, current=None):
            pass

        def getExperiment(self, current=None):
            pass

        def setExperiment(self, theExperiment, current=None):
            pass

        def unloadPixels(self, current=None):
            pass

        def sizeOfPixels(self, current=None):
            pass

        def copyPixels(self, current=None):
            pass

        def addPixels(self, target, current=None):
            pass

        def addAllPixelsSet(self, targets, current=None):
            pass

        def removePixels(self, theTarget, current=None):
            pass

        def removeAllPixelsSet(self, targets, current=None):
            pass

        def clearPixels(self, current=None):
            pass

        def reloadPixels(self, toCopy, current=None):
            pass

        def getPixels(self, index, current=None):
            pass

        def setPixels(self, index, theElement, current=None):
            pass

        def getPrimaryPixels(self, current=None):
            pass

        def setPrimaryPixels(self, theElement, current=None):
            pass

        def unloadWellSamples(self, current=None):
            pass

        def sizeOfWellSamples(self, current=None):
            pass

        def copyWellSamples(self, current=None):
            pass

        def addWellSample(self, target, current=None):
            pass

        def addAllWellSampleSet(self, targets, current=None):
            pass

        def removeWellSample(self, theTarget, current=None):
            pass

        def removeAllWellSampleSet(self, targets, current=None):
            pass

        def clearWellSamples(self, current=None):
            pass

        def reloadWellSamples(self, toCopy, current=None):
            pass

        def unloadRois(self, current=None):
            pass

        def sizeOfRois(self, current=None):
            pass

        def copyRois(self, current=None):
            pass

        def addRoi(self, target, current=None):
            pass

        def addAllRoiSet(self, targets, current=None):
            pass

        def removeRoi(self, theTarget, current=None):
            pass

        def removeAllRoiSet(self, targets, current=None):
            pass

        def clearRois(self, current=None):
            pass

        def reloadRois(self, toCopy, current=None):
            pass

        def unloadDatasetLinks(self, current=None):
            pass

        def sizeOfDatasetLinks(self, current=None):
            pass

        def copyDatasetLinks(self, current=None):
            pass

        def addDatasetImageLink(self, target, current=None):
            pass

        def addAllDatasetImageLinkSet(self, targets, current=None):
            pass

        def removeDatasetImageLink(self, theTarget, current=None):
            pass

        def removeAllDatasetImageLinkSet(self, targets, current=None):
            pass

        def clearDatasetLinks(self, current=None):
            pass

        def reloadDatasetLinks(self, toCopy, current=None):
            pass

        def getDatasetLinksCountPerOwner(self, current=None):
            pass

        def linkDataset(self, addition, current=None):
            pass

        def addDatasetImageLinkToBoth(self, link, bothSides, current=None):
            pass

        def findDatasetImageLink(self, removal, current=None):
            pass

        def unlinkDataset(self, removal, current=None):
            pass

        def removeDatasetImageLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedDatasetList(self, current=None):
            pass

        def unloadFolderLinks(self, current=None):
            pass

        def sizeOfFolderLinks(self, current=None):
            pass

        def copyFolderLinks(self, current=None):
            pass

        def addFolderImageLink(self, target, current=None):
            pass

        def addAllFolderImageLinkSet(self, targets, current=None):
            pass

        def removeFolderImageLink(self, theTarget, current=None):
            pass

        def removeAllFolderImageLinkSet(self, targets, current=None):
            pass

        def clearFolderLinks(self, current=None):
            pass

        def reloadFolderLinks(self, toCopy, current=None):
            pass

        def getFolderLinksCountPerOwner(self, current=None):
            pass

        def linkFolder(self, addition, current=None):
            pass

        def addFolderImageLinkToBoth(self, link, bothSides, current=None):
            pass

        def findFolderImageLink(self, removal, current=None):
            pass

        def unlinkFolder(self, removal, current=None):
            pass

        def removeFolderImageLinkFromBoth(self, link, bothSides, current=None):
            pass

        def linkedFolderList(self, current=None):
            pass

        def getFileset(self, current=None):
            pass

        def setFileset(self, theFileset, current=None):
            pass

        def unloadAnnotationLinks(self, current=None):
            pass

        def sizeOfAnnotationLinks(self, current=None):
            pass

        def copyAnnotationLinks(self, current=None):
            pass

        def addImageAnnotationLink(self, target, current=None):
            pass

        def addAllImageAnnotationLinkSet(self, targets, current=None):
            pass

        def removeImageAnnotationLink(self, theTarget, current=None):
            pass

        def removeAllImageAnnotationLinkSet(self, targets, current=None):
            pass

        def clearAnnotationLinks(self, current=None):
            pass

        def reloadAnnotationLinks(self, toCopy, current=None):
            pass

        def getAnnotationLinksCountPerOwner(self, current=None):
            pass

        def linkAnnotation(self, addition, current=None):
            pass

        def addImageAnnotationLinkToBoth(self, link, bothSides, current=None):
            pass

        def findImageAnnotationLink(self, removal, current=None):
            pass

        def unlinkAnnotation(self, removal, current=None):
            pass

        def removeImageAnnotationLinkFromBoth(self, link, bothSides, current=None):
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
            return IcePy.stringify(self, _M_omero.model._t_Image)

        __repr__ = __str__

    _M_omero.model.ImagePrx = Ice.createTempClass()
    class ImagePrx(_M_omero.model.IObjectPrx):

        def getVersion(self, _ctx=None):
            return _M_omero.model.Image._op_getVersion.invoke(self, ((), _ctx))

        def begin_getVersion(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getVersion.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getVersion(self, _r):
            return _M_omero.model.Image._op_getVersion.end(self, _r)

        def setVersion(self, theVersion, _ctx=None):
            return _M_omero.model.Image._op_setVersion.invoke(self, ((theVersion, ), _ctx))

        def begin_setVersion(self, theVersion, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setVersion.begin(self, ((theVersion, ), _response, _ex, _sent, _ctx))

        def end_setVersion(self, _r):
            return _M_omero.model.Image._op_setVersion.end(self, _r)

        def getSeries(self, _ctx=None):
            return _M_omero.model.Image._op_getSeries.invoke(self, ((), _ctx))

        def begin_getSeries(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getSeries.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getSeries(self, _r):
            return _M_omero.model.Image._op_getSeries.end(self, _r)

        def setSeries(self, theSeries, _ctx=None):
            return _M_omero.model.Image._op_setSeries.invoke(self, ((theSeries, ), _ctx))

        def begin_setSeries(self, theSeries, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setSeries.begin(self, ((theSeries, ), _response, _ex, _sent, _ctx))

        def end_setSeries(self, _r):
            return _M_omero.model.Image._op_setSeries.end(self, _r)

        def getAcquisitionDate(self, _ctx=None):
            return _M_omero.model.Image._op_getAcquisitionDate.invoke(self, ((), _ctx))

        def begin_getAcquisitionDate(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getAcquisitionDate.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAcquisitionDate(self, _r):
            return _M_omero.model.Image._op_getAcquisitionDate.end(self, _r)

        def setAcquisitionDate(self, theAcquisitionDate, _ctx=None):
            return _M_omero.model.Image._op_setAcquisitionDate.invoke(self, ((theAcquisitionDate, ), _ctx))

        def begin_setAcquisitionDate(self, theAcquisitionDate, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setAcquisitionDate.begin(self, ((theAcquisitionDate, ), _response, _ex, _sent, _ctx))

        def end_setAcquisitionDate(self, _r):
            return _M_omero.model.Image._op_setAcquisitionDate.end(self, _r)

        def getArchived(self, _ctx=None):
            return _M_omero.model.Image._op_getArchived.invoke(self, ((), _ctx))

        def begin_getArchived(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getArchived.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getArchived(self, _r):
            return _M_omero.model.Image._op_getArchived.end(self, _r)

        def setArchived(self, theArchived, _ctx=None):
            return _M_omero.model.Image._op_setArchived.invoke(self, ((theArchived, ), _ctx))

        def begin_setArchived(self, theArchived, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setArchived.begin(self, ((theArchived, ), _response, _ex, _sent, _ctx))

        def end_setArchived(self, _r):
            return _M_omero.model.Image._op_setArchived.end(self, _r)

        def getPartial(self, _ctx=None):
            return _M_omero.model.Image._op_getPartial.invoke(self, ((), _ctx))

        def begin_getPartial(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getPartial.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPartial(self, _r):
            return _M_omero.model.Image._op_getPartial.end(self, _r)

        def setPartial(self, thePartial, _ctx=None):
            return _M_omero.model.Image._op_setPartial.invoke(self, ((thePartial, ), _ctx))

        def begin_setPartial(self, thePartial, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setPartial.begin(self, ((thePartial, ), _response, _ex, _sent, _ctx))

        def end_setPartial(self, _r):
            return _M_omero.model.Image._op_setPartial.end(self, _r)

        def getFormat(self, _ctx=None):
            return _M_omero.model.Image._op_getFormat.invoke(self, ((), _ctx))

        def begin_getFormat(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getFormat.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFormat(self, _r):
            return _M_omero.model.Image._op_getFormat.end(self, _r)

        def setFormat(self, theFormat, _ctx=None):
            return _M_omero.model.Image._op_setFormat.invoke(self, ((theFormat, ), _ctx))

        def begin_setFormat(self, theFormat, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setFormat.begin(self, ((theFormat, ), _response, _ex, _sent, _ctx))

        def end_setFormat(self, _r):
            return _M_omero.model.Image._op_setFormat.end(self, _r)

        def getImagingEnvironment(self, _ctx=None):
            return _M_omero.model.Image._op_getImagingEnvironment.invoke(self, ((), _ctx))

        def begin_getImagingEnvironment(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getImagingEnvironment.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImagingEnvironment(self, _r):
            return _M_omero.model.Image._op_getImagingEnvironment.end(self, _r)

        def setImagingEnvironment(self, theImagingEnvironment, _ctx=None):
            return _M_omero.model.Image._op_setImagingEnvironment.invoke(self, ((theImagingEnvironment, ), _ctx))

        def begin_setImagingEnvironment(self, theImagingEnvironment, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setImagingEnvironment.begin(self, ((theImagingEnvironment, ), _response, _ex, _sent, _ctx))

        def end_setImagingEnvironment(self, _r):
            return _M_omero.model.Image._op_setImagingEnvironment.end(self, _r)

        def getObjectiveSettings(self, _ctx=None):
            return _M_omero.model.Image._op_getObjectiveSettings.invoke(self, ((), _ctx))

        def begin_getObjectiveSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getObjectiveSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getObjectiveSettings(self, _r):
            return _M_omero.model.Image._op_getObjectiveSettings.end(self, _r)

        def setObjectiveSettings(self, theObjectiveSettings, _ctx=None):
            return _M_omero.model.Image._op_setObjectiveSettings.invoke(self, ((theObjectiveSettings, ), _ctx))

        def begin_setObjectiveSettings(self, theObjectiveSettings, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setObjectiveSettings.begin(self, ((theObjectiveSettings, ), _response, _ex, _sent, _ctx))

        def end_setObjectiveSettings(self, _r):
            return _M_omero.model.Image._op_setObjectiveSettings.end(self, _r)

        def getInstrument(self, _ctx=None):
            return _M_omero.model.Image._op_getInstrument.invoke(self, ((), _ctx))

        def begin_getInstrument(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getInstrument.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getInstrument(self, _r):
            return _M_omero.model.Image._op_getInstrument.end(self, _r)

        def setInstrument(self, theInstrument, _ctx=None):
            return _M_omero.model.Image._op_setInstrument.invoke(self, ((theInstrument, ), _ctx))

        def begin_setInstrument(self, theInstrument, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setInstrument.begin(self, ((theInstrument, ), _response, _ex, _sent, _ctx))

        def end_setInstrument(self, _r):
            return _M_omero.model.Image._op_setInstrument.end(self, _r)

        def getStageLabel(self, _ctx=None):
            return _M_omero.model.Image._op_getStageLabel.invoke(self, ((), _ctx))

        def begin_getStageLabel(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getStageLabel.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getStageLabel(self, _r):
            return _M_omero.model.Image._op_getStageLabel.end(self, _r)

        def setStageLabel(self, theStageLabel, _ctx=None):
            return _M_omero.model.Image._op_setStageLabel.invoke(self, ((theStageLabel, ), _ctx))

        def begin_setStageLabel(self, theStageLabel, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setStageLabel.begin(self, ((theStageLabel, ), _response, _ex, _sent, _ctx))

        def end_setStageLabel(self, _r):
            return _M_omero.model.Image._op_setStageLabel.end(self, _r)

        def getExperiment(self, _ctx=None):
            return _M_omero.model.Image._op_getExperiment.invoke(self, ((), _ctx))

        def begin_getExperiment(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getExperiment.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getExperiment(self, _r):
            return _M_omero.model.Image._op_getExperiment.end(self, _r)

        def setExperiment(self, theExperiment, _ctx=None):
            return _M_omero.model.Image._op_setExperiment.invoke(self, ((theExperiment, ), _ctx))

        def begin_setExperiment(self, theExperiment, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setExperiment.begin(self, ((theExperiment, ), _response, _ex, _sent, _ctx))

        def end_setExperiment(self, _r):
            return _M_omero.model.Image._op_setExperiment.end(self, _r)

        def unloadPixels(self, _ctx=None):
            return _M_omero.model.Image._op_unloadPixels.invoke(self, ((), _ctx))

        def begin_unloadPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unloadPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadPixels(self, _r):
            return _M_omero.model.Image._op_unloadPixels.end(self, _r)

        def sizeOfPixels(self, _ctx=None):
            return _M_omero.model.Image._op_sizeOfPixels.invoke(self, ((), _ctx))

        def begin_sizeOfPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_sizeOfPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfPixels(self, _r):
            return _M_omero.model.Image._op_sizeOfPixels.end(self, _r)

        def copyPixels(self, _ctx=None):
            return _M_omero.model.Image._op_copyPixels.invoke(self, ((), _ctx))

        def begin_copyPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_copyPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyPixels(self, _r):
            return _M_omero.model.Image._op_copyPixels.end(self, _r)

        def addPixels(self, target, _ctx=None):
            return _M_omero.model.Image._op_addPixels.invoke(self, ((target, ), _ctx))

        def begin_addPixels(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addPixels.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addPixels(self, _r):
            return _M_omero.model.Image._op_addPixels.end(self, _r)

        def addAllPixelsSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_addAllPixelsSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllPixelsSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addAllPixelsSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllPixelsSet(self, _r):
            return _M_omero.model.Image._op_addAllPixelsSet.end(self, _r)

        def removePixels(self, theTarget, _ctx=None):
            return _M_omero.model.Image._op_removePixels.invoke(self, ((theTarget, ), _ctx))

        def begin_removePixels(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removePixels.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removePixels(self, _r):
            return _M_omero.model.Image._op_removePixels.end(self, _r)

        def removeAllPixelsSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_removeAllPixelsSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllPixelsSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeAllPixelsSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllPixelsSet(self, _r):
            return _M_omero.model.Image._op_removeAllPixelsSet.end(self, _r)

        def clearPixels(self, _ctx=None):
            return _M_omero.model.Image._op_clearPixels.invoke(self, ((), _ctx))

        def begin_clearPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_clearPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearPixels(self, _r):
            return _M_omero.model.Image._op_clearPixels.end(self, _r)

        def reloadPixels(self, toCopy, _ctx=None):
            return _M_omero.model.Image._op_reloadPixels.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadPixels(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_reloadPixels.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadPixels(self, _r):
            return _M_omero.model.Image._op_reloadPixels.end(self, _r)

        def getPixels(self, index, _ctx=None):
            return _M_omero.model.Image._op_getPixels.invoke(self, ((index, ), _ctx))

        def begin_getPixels(self, index, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getPixels.begin(self, ((index, ), _response, _ex, _sent, _ctx))

        def end_getPixels(self, _r):
            return _M_omero.model.Image._op_getPixels.end(self, _r)

        def setPixels(self, index, theElement, _ctx=None):
            return _M_omero.model.Image._op_setPixels.invoke(self, ((index, theElement), _ctx))

        def begin_setPixels(self, index, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setPixels.begin(self, ((index, theElement), _response, _ex, _sent, _ctx))

        def end_setPixels(self, _r):
            return _M_omero.model.Image._op_setPixels.end(self, _r)

        def getPrimaryPixels(self, _ctx=None):
            return _M_omero.model.Image._op_getPrimaryPixels.invoke(self, ((), _ctx))

        def begin_getPrimaryPixels(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getPrimaryPixels.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getPrimaryPixels(self, _r):
            return _M_omero.model.Image._op_getPrimaryPixels.end(self, _r)

        def setPrimaryPixels(self, theElement, _ctx=None):
            return _M_omero.model.Image._op_setPrimaryPixels.invoke(self, ((theElement, ), _ctx))

        def begin_setPrimaryPixels(self, theElement, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setPrimaryPixels.begin(self, ((theElement, ), _response, _ex, _sent, _ctx))

        def end_setPrimaryPixels(self, _r):
            return _M_omero.model.Image._op_setPrimaryPixels.end(self, _r)

        def unloadWellSamples(self, _ctx=None):
            return _M_omero.model.Image._op_unloadWellSamples.invoke(self, ((), _ctx))

        def begin_unloadWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unloadWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadWellSamples(self, _r):
            return _M_omero.model.Image._op_unloadWellSamples.end(self, _r)

        def sizeOfWellSamples(self, _ctx=None):
            return _M_omero.model.Image._op_sizeOfWellSamples.invoke(self, ((), _ctx))

        def begin_sizeOfWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_sizeOfWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfWellSamples(self, _r):
            return _M_omero.model.Image._op_sizeOfWellSamples.end(self, _r)

        def copyWellSamples(self, _ctx=None):
            return _M_omero.model.Image._op_copyWellSamples.invoke(self, ((), _ctx))

        def begin_copyWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_copyWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyWellSamples(self, _r):
            return _M_omero.model.Image._op_copyWellSamples.end(self, _r)

        def addWellSample(self, target, _ctx=None):
            return _M_omero.model.Image._op_addWellSample.invoke(self, ((target, ), _ctx))

        def begin_addWellSample(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addWellSample.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addWellSample(self, _r):
            return _M_omero.model.Image._op_addWellSample.end(self, _r)

        def addAllWellSampleSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_addAllWellSampleSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllWellSampleSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addAllWellSampleSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllWellSampleSet(self, _r):
            return _M_omero.model.Image._op_addAllWellSampleSet.end(self, _r)

        def removeWellSample(self, theTarget, _ctx=None):
            return _M_omero.model.Image._op_removeWellSample.invoke(self, ((theTarget, ), _ctx))

        def begin_removeWellSample(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeWellSample.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeWellSample(self, _r):
            return _M_omero.model.Image._op_removeWellSample.end(self, _r)

        def removeAllWellSampleSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_removeAllWellSampleSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllWellSampleSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeAllWellSampleSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllWellSampleSet(self, _r):
            return _M_omero.model.Image._op_removeAllWellSampleSet.end(self, _r)

        def clearWellSamples(self, _ctx=None):
            return _M_omero.model.Image._op_clearWellSamples.invoke(self, ((), _ctx))

        def begin_clearWellSamples(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_clearWellSamples.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearWellSamples(self, _r):
            return _M_omero.model.Image._op_clearWellSamples.end(self, _r)

        def reloadWellSamples(self, toCopy, _ctx=None):
            return _M_omero.model.Image._op_reloadWellSamples.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadWellSamples(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_reloadWellSamples.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadWellSamples(self, _r):
            return _M_omero.model.Image._op_reloadWellSamples.end(self, _r)

        def unloadRois(self, _ctx=None):
            return _M_omero.model.Image._op_unloadRois.invoke(self, ((), _ctx))

        def begin_unloadRois(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unloadRois.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadRois(self, _r):
            return _M_omero.model.Image._op_unloadRois.end(self, _r)

        def sizeOfRois(self, _ctx=None):
            return _M_omero.model.Image._op_sizeOfRois.invoke(self, ((), _ctx))

        def begin_sizeOfRois(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_sizeOfRois.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfRois(self, _r):
            return _M_omero.model.Image._op_sizeOfRois.end(self, _r)

        def copyRois(self, _ctx=None):
            return _M_omero.model.Image._op_copyRois.invoke(self, ((), _ctx))

        def begin_copyRois(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_copyRois.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyRois(self, _r):
            return _M_omero.model.Image._op_copyRois.end(self, _r)

        def addRoi(self, target, _ctx=None):
            return _M_omero.model.Image._op_addRoi.invoke(self, ((target, ), _ctx))

        def begin_addRoi(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addRoi.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addRoi(self, _r):
            return _M_omero.model.Image._op_addRoi.end(self, _r)

        def addAllRoiSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_addAllRoiSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllRoiSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addAllRoiSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllRoiSet(self, _r):
            return _M_omero.model.Image._op_addAllRoiSet.end(self, _r)

        def removeRoi(self, theTarget, _ctx=None):
            return _M_omero.model.Image._op_removeRoi.invoke(self, ((theTarget, ), _ctx))

        def begin_removeRoi(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeRoi.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeRoi(self, _r):
            return _M_omero.model.Image._op_removeRoi.end(self, _r)

        def removeAllRoiSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_removeAllRoiSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllRoiSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeAllRoiSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllRoiSet(self, _r):
            return _M_omero.model.Image._op_removeAllRoiSet.end(self, _r)

        def clearRois(self, _ctx=None):
            return _M_omero.model.Image._op_clearRois.invoke(self, ((), _ctx))

        def begin_clearRois(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_clearRois.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearRois(self, _r):
            return _M_omero.model.Image._op_clearRois.end(self, _r)

        def reloadRois(self, toCopy, _ctx=None):
            return _M_omero.model.Image._op_reloadRois.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadRois(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_reloadRois.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadRois(self, _r):
            return _M_omero.model.Image._op_reloadRois.end(self, _r)

        def unloadDatasetLinks(self, _ctx=None):
            return _M_omero.model.Image._op_unloadDatasetLinks.invoke(self, ((), _ctx))

        def begin_unloadDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unloadDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadDatasetLinks(self, _r):
            return _M_omero.model.Image._op_unloadDatasetLinks.end(self, _r)

        def sizeOfDatasetLinks(self, _ctx=None):
            return _M_omero.model.Image._op_sizeOfDatasetLinks.invoke(self, ((), _ctx))

        def begin_sizeOfDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_sizeOfDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfDatasetLinks(self, _r):
            return _M_omero.model.Image._op_sizeOfDatasetLinks.end(self, _r)

        def copyDatasetLinks(self, _ctx=None):
            return _M_omero.model.Image._op_copyDatasetLinks.invoke(self, ((), _ctx))

        def begin_copyDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_copyDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyDatasetLinks(self, _r):
            return _M_omero.model.Image._op_copyDatasetLinks.end(self, _r)

        def addDatasetImageLink(self, target, _ctx=None):
            return _M_omero.model.Image._op_addDatasetImageLink.invoke(self, ((target, ), _ctx))

        def begin_addDatasetImageLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addDatasetImageLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addDatasetImageLink(self, _r):
            return _M_omero.model.Image._op_addDatasetImageLink.end(self, _r)

        def addAllDatasetImageLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_addAllDatasetImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllDatasetImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addAllDatasetImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllDatasetImageLinkSet(self, _r):
            return _M_omero.model.Image._op_addAllDatasetImageLinkSet.end(self, _r)

        def removeDatasetImageLink(self, theTarget, _ctx=None):
            return _M_omero.model.Image._op_removeDatasetImageLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeDatasetImageLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeDatasetImageLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeDatasetImageLink(self, _r):
            return _M_omero.model.Image._op_removeDatasetImageLink.end(self, _r)

        def removeAllDatasetImageLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_removeAllDatasetImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllDatasetImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeAllDatasetImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllDatasetImageLinkSet(self, _r):
            return _M_omero.model.Image._op_removeAllDatasetImageLinkSet.end(self, _r)

        def clearDatasetLinks(self, _ctx=None):
            return _M_omero.model.Image._op_clearDatasetLinks.invoke(self, ((), _ctx))

        def begin_clearDatasetLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_clearDatasetLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearDatasetLinks(self, _r):
            return _M_omero.model.Image._op_clearDatasetLinks.end(self, _r)

        def reloadDatasetLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Image._op_reloadDatasetLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadDatasetLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_reloadDatasetLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadDatasetLinks(self, _r):
            return _M_omero.model.Image._op_reloadDatasetLinks.end(self, _r)

        def getDatasetLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Image._op_getDatasetLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getDatasetLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getDatasetLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDatasetLinksCountPerOwner(self, _r):
            return _M_omero.model.Image._op_getDatasetLinksCountPerOwner.end(self, _r)

        def linkDataset(self, addition, _ctx=None):
            return _M_omero.model.Image._op_linkDataset.invoke(self, ((addition, ), _ctx))

        def begin_linkDataset(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_linkDataset.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkDataset(self, _r):
            return _M_omero.model.Image._op_linkDataset.end(self, _r)

        def addDatasetImageLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Image._op_addDatasetImageLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addDatasetImageLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addDatasetImageLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addDatasetImageLinkToBoth(self, _r):
            return _M_omero.model.Image._op_addDatasetImageLinkToBoth.end(self, _r)

        def findDatasetImageLink(self, removal, _ctx=None):
            return _M_omero.model.Image._op_findDatasetImageLink.invoke(self, ((removal, ), _ctx))

        def begin_findDatasetImageLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_findDatasetImageLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findDatasetImageLink(self, _r):
            return _M_omero.model.Image._op_findDatasetImageLink.end(self, _r)

        def unlinkDataset(self, removal, _ctx=None):
            return _M_omero.model.Image._op_unlinkDataset.invoke(self, ((removal, ), _ctx))

        def begin_unlinkDataset(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unlinkDataset.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkDataset(self, _r):
            return _M_omero.model.Image._op_unlinkDataset.end(self, _r)

        def removeDatasetImageLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Image._op_removeDatasetImageLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeDatasetImageLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeDatasetImageLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeDatasetImageLinkFromBoth(self, _r):
            return _M_omero.model.Image._op_removeDatasetImageLinkFromBoth.end(self, _r)

        def linkedDatasetList(self, _ctx=None):
            return _M_omero.model.Image._op_linkedDatasetList.invoke(self, ((), _ctx))

        def begin_linkedDatasetList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_linkedDatasetList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedDatasetList(self, _r):
            return _M_omero.model.Image._op_linkedDatasetList.end(self, _r)

        def unloadFolderLinks(self, _ctx=None):
            return _M_omero.model.Image._op_unloadFolderLinks.invoke(self, ((), _ctx))

        def begin_unloadFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unloadFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadFolderLinks(self, _r):
            return _M_omero.model.Image._op_unloadFolderLinks.end(self, _r)

        def sizeOfFolderLinks(self, _ctx=None):
            return _M_omero.model.Image._op_sizeOfFolderLinks.invoke(self, ((), _ctx))

        def begin_sizeOfFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_sizeOfFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfFolderLinks(self, _r):
            return _M_omero.model.Image._op_sizeOfFolderLinks.end(self, _r)

        def copyFolderLinks(self, _ctx=None):
            return _M_omero.model.Image._op_copyFolderLinks.invoke(self, ((), _ctx))

        def begin_copyFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_copyFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyFolderLinks(self, _r):
            return _M_omero.model.Image._op_copyFolderLinks.end(self, _r)

        def addFolderImageLink(self, target, _ctx=None):
            return _M_omero.model.Image._op_addFolderImageLink.invoke(self, ((target, ), _ctx))

        def begin_addFolderImageLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addFolderImageLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addFolderImageLink(self, _r):
            return _M_omero.model.Image._op_addFolderImageLink.end(self, _r)

        def addAllFolderImageLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_addAllFolderImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllFolderImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addAllFolderImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllFolderImageLinkSet(self, _r):
            return _M_omero.model.Image._op_addAllFolderImageLinkSet.end(self, _r)

        def removeFolderImageLink(self, theTarget, _ctx=None):
            return _M_omero.model.Image._op_removeFolderImageLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeFolderImageLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeFolderImageLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeFolderImageLink(self, _r):
            return _M_omero.model.Image._op_removeFolderImageLink.end(self, _r)

        def removeAllFolderImageLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_removeAllFolderImageLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllFolderImageLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeAllFolderImageLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllFolderImageLinkSet(self, _r):
            return _M_omero.model.Image._op_removeAllFolderImageLinkSet.end(self, _r)

        def clearFolderLinks(self, _ctx=None):
            return _M_omero.model.Image._op_clearFolderLinks.invoke(self, ((), _ctx))

        def begin_clearFolderLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_clearFolderLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearFolderLinks(self, _r):
            return _M_omero.model.Image._op_clearFolderLinks.end(self, _r)

        def reloadFolderLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Image._op_reloadFolderLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadFolderLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_reloadFolderLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadFolderLinks(self, _r):
            return _M_omero.model.Image._op_reloadFolderLinks.end(self, _r)

        def getFolderLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Image._op_getFolderLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getFolderLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getFolderLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFolderLinksCountPerOwner(self, _r):
            return _M_omero.model.Image._op_getFolderLinksCountPerOwner.end(self, _r)

        def linkFolder(self, addition, _ctx=None):
            return _M_omero.model.Image._op_linkFolder.invoke(self, ((addition, ), _ctx))

        def begin_linkFolder(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_linkFolder.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkFolder(self, _r):
            return _M_omero.model.Image._op_linkFolder.end(self, _r)

        def addFolderImageLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Image._op_addFolderImageLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addFolderImageLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addFolderImageLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addFolderImageLinkToBoth(self, _r):
            return _M_omero.model.Image._op_addFolderImageLinkToBoth.end(self, _r)

        def findFolderImageLink(self, removal, _ctx=None):
            return _M_omero.model.Image._op_findFolderImageLink.invoke(self, ((removal, ), _ctx))

        def begin_findFolderImageLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_findFolderImageLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findFolderImageLink(self, _r):
            return _M_omero.model.Image._op_findFolderImageLink.end(self, _r)

        def unlinkFolder(self, removal, _ctx=None):
            return _M_omero.model.Image._op_unlinkFolder.invoke(self, ((removal, ), _ctx))

        def begin_unlinkFolder(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unlinkFolder.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkFolder(self, _r):
            return _M_omero.model.Image._op_unlinkFolder.end(self, _r)

        def removeFolderImageLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Image._op_removeFolderImageLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeFolderImageLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeFolderImageLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeFolderImageLinkFromBoth(self, _r):
            return _M_omero.model.Image._op_removeFolderImageLinkFromBoth.end(self, _r)

        def linkedFolderList(self, _ctx=None):
            return _M_omero.model.Image._op_linkedFolderList.invoke(self, ((), _ctx))

        def begin_linkedFolderList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_linkedFolderList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedFolderList(self, _r):
            return _M_omero.model.Image._op_linkedFolderList.end(self, _r)

        def getFileset(self, _ctx=None):
            return _M_omero.model.Image._op_getFileset.invoke(self, ((), _ctx))

        def begin_getFileset(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getFileset.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getFileset(self, _r):
            return _M_omero.model.Image._op_getFileset.end(self, _r)

        def setFileset(self, theFileset, _ctx=None):
            return _M_omero.model.Image._op_setFileset.invoke(self, ((theFileset, ), _ctx))

        def begin_setFileset(self, theFileset, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setFileset.begin(self, ((theFileset, ), _response, _ex, _sent, _ctx))

        def end_setFileset(self, _r):
            return _M_omero.model.Image._op_setFileset.end(self, _r)

        def unloadAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Image._op_unloadAnnotationLinks.invoke(self, ((), _ctx))

        def begin_unloadAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unloadAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_unloadAnnotationLinks(self, _r):
            return _M_omero.model.Image._op_unloadAnnotationLinks.end(self, _r)

        def sizeOfAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Image._op_sizeOfAnnotationLinks.invoke(self, ((), _ctx))

        def begin_sizeOfAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_sizeOfAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_sizeOfAnnotationLinks(self, _r):
            return _M_omero.model.Image._op_sizeOfAnnotationLinks.end(self, _r)

        def copyAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Image._op_copyAnnotationLinks.invoke(self, ((), _ctx))

        def begin_copyAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_copyAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_copyAnnotationLinks(self, _r):
            return _M_omero.model.Image._op_copyAnnotationLinks.end(self, _r)

        def addImageAnnotationLink(self, target, _ctx=None):
            return _M_omero.model.Image._op_addImageAnnotationLink.invoke(self, ((target, ), _ctx))

        def begin_addImageAnnotationLink(self, target, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addImageAnnotationLink.begin(self, ((target, ), _response, _ex, _sent, _ctx))

        def end_addImageAnnotationLink(self, _r):
            return _M_omero.model.Image._op_addImageAnnotationLink.end(self, _r)

        def addAllImageAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_addAllImageAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_addAllImageAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addAllImageAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_addAllImageAnnotationLinkSet(self, _r):
            return _M_omero.model.Image._op_addAllImageAnnotationLinkSet.end(self, _r)

        def removeImageAnnotationLink(self, theTarget, _ctx=None):
            return _M_omero.model.Image._op_removeImageAnnotationLink.invoke(self, ((theTarget, ), _ctx))

        def begin_removeImageAnnotationLink(self, theTarget, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeImageAnnotationLink.begin(self, ((theTarget, ), _response, _ex, _sent, _ctx))

        def end_removeImageAnnotationLink(self, _r):
            return _M_omero.model.Image._op_removeImageAnnotationLink.end(self, _r)

        def removeAllImageAnnotationLinkSet(self, targets, _ctx=None):
            return _M_omero.model.Image._op_removeAllImageAnnotationLinkSet.invoke(self, ((targets, ), _ctx))

        def begin_removeAllImageAnnotationLinkSet(self, targets, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeAllImageAnnotationLinkSet.begin(self, ((targets, ), _response, _ex, _sent, _ctx))

        def end_removeAllImageAnnotationLinkSet(self, _r):
            return _M_omero.model.Image._op_removeAllImageAnnotationLinkSet.end(self, _r)

        def clearAnnotationLinks(self, _ctx=None):
            return _M_omero.model.Image._op_clearAnnotationLinks.invoke(self, ((), _ctx))

        def begin_clearAnnotationLinks(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_clearAnnotationLinks.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_clearAnnotationLinks(self, _r):
            return _M_omero.model.Image._op_clearAnnotationLinks.end(self, _r)

        def reloadAnnotationLinks(self, toCopy, _ctx=None):
            return _M_omero.model.Image._op_reloadAnnotationLinks.invoke(self, ((toCopy, ), _ctx))

        def begin_reloadAnnotationLinks(self, toCopy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_reloadAnnotationLinks.begin(self, ((toCopy, ), _response, _ex, _sent, _ctx))

        def end_reloadAnnotationLinks(self, _r):
            return _M_omero.model.Image._op_reloadAnnotationLinks.end(self, _r)

        def getAnnotationLinksCountPerOwner(self, _ctx=None):
            return _M_omero.model.Image._op_getAnnotationLinksCountPerOwner.invoke(self, ((), _ctx))

        def begin_getAnnotationLinksCountPerOwner(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getAnnotationLinksCountPerOwner.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getAnnotationLinksCountPerOwner(self, _r):
            return _M_omero.model.Image._op_getAnnotationLinksCountPerOwner.end(self, _r)

        def linkAnnotation(self, addition, _ctx=None):
            return _M_omero.model.Image._op_linkAnnotation.invoke(self, ((addition, ), _ctx))

        def begin_linkAnnotation(self, addition, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_linkAnnotation.begin(self, ((addition, ), _response, _ex, _sent, _ctx))

        def end_linkAnnotation(self, _r):
            return _M_omero.model.Image._op_linkAnnotation.end(self, _r)

        def addImageAnnotationLinkToBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Image._op_addImageAnnotationLinkToBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_addImageAnnotationLinkToBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_addImageAnnotationLinkToBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_addImageAnnotationLinkToBoth(self, _r):
            return _M_omero.model.Image._op_addImageAnnotationLinkToBoth.end(self, _r)

        def findImageAnnotationLink(self, removal, _ctx=None):
            return _M_omero.model.Image._op_findImageAnnotationLink.invoke(self, ((removal, ), _ctx))

        def begin_findImageAnnotationLink(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_findImageAnnotationLink.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_findImageAnnotationLink(self, _r):
            return _M_omero.model.Image._op_findImageAnnotationLink.end(self, _r)

        def unlinkAnnotation(self, removal, _ctx=None):
            return _M_omero.model.Image._op_unlinkAnnotation.invoke(self, ((removal, ), _ctx))

        def begin_unlinkAnnotation(self, removal, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_unlinkAnnotation.begin(self, ((removal, ), _response, _ex, _sent, _ctx))

        def end_unlinkAnnotation(self, _r):
            return _M_omero.model.Image._op_unlinkAnnotation.end(self, _r)

        def removeImageAnnotationLinkFromBoth(self, link, bothSides, _ctx=None):
            return _M_omero.model.Image._op_removeImageAnnotationLinkFromBoth.invoke(self, ((link, bothSides), _ctx))

        def begin_removeImageAnnotationLinkFromBoth(self, link, bothSides, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_removeImageAnnotationLinkFromBoth.begin(self, ((link, bothSides), _response, _ex, _sent, _ctx))

        def end_removeImageAnnotationLinkFromBoth(self, _r):
            return _M_omero.model.Image._op_removeImageAnnotationLinkFromBoth.end(self, _r)

        def linkedAnnotationList(self, _ctx=None):
            return _M_omero.model.Image._op_linkedAnnotationList.invoke(self, ((), _ctx))

        def begin_linkedAnnotationList(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_linkedAnnotationList.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_linkedAnnotationList(self, _r):
            return _M_omero.model.Image._op_linkedAnnotationList.end(self, _r)

        def getName(self, _ctx=None):
            return _M_omero.model.Image._op_getName.invoke(self, ((), _ctx))

        def begin_getName(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getName.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getName(self, _r):
            return _M_omero.model.Image._op_getName.end(self, _r)

        def setName(self, theName, _ctx=None):
            return _M_omero.model.Image._op_setName.invoke(self, ((theName, ), _ctx))

        def begin_setName(self, theName, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setName.begin(self, ((theName, ), _response, _ex, _sent, _ctx))

        def end_setName(self, _r):
            return _M_omero.model.Image._op_setName.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_omero.model.Image._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_omero.model.Image._op_getDescription.end(self, _r)

        def setDescription(self, theDescription, _ctx=None):
            return _M_omero.model.Image._op_setDescription.invoke(self, ((theDescription, ), _ctx))

        def begin_setDescription(self, theDescription, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_omero.model.Image._op_setDescription.begin(self, ((theDescription, ), _response, _ex, _sent, _ctx))

        def end_setDescription(self, _r):
            return _M_omero.model.Image._op_setDescription.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_omero.model.ImagePrx.ice_checkedCast(proxy, '::omero::model::Image', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_omero.model.ImagePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::omero::model::Image'
        ice_staticId = staticmethod(ice_staticId)

    _M_omero.model._t_ImagePrx = IcePy.defineProxy('::omero::model::Image', ImagePrx)

    _M_omero.model._t_Image = IcePy.declareClass('::omero::model::Image')

    _M_omero.model._t_Image = IcePy.defineClass('::omero::model::Image', Image, -1, (), True, False, _M_omero.model._t_IObject, (), (
        ('_version', (), _M_omero._t_RInt, False, 0),
        ('_series', (), _M_omero._t_RInt, False, 0),
        ('_acquisitionDate', (), _M_omero._t_RTime, False, 0),
        ('_archived', (), _M_omero._t_RBool, False, 0),
        ('_partial', (), _M_omero._t_RBool, False, 0),
        ('_format', (), _M_omero.model._t_Format, False, 0),
        ('_imagingEnvironment', (), _M_omero.model._t_ImagingEnvironment, False, 0),
        ('_objectiveSettings', (), _M_omero.model._t_ObjectiveSettings, False, 0),
        ('_instrument', (), _M_omero.model._t_Instrument, False, 0),
        ('_stageLabel', (), _M_omero.model._t_StageLabel, False, 0),
        ('_experiment', (), _M_omero.model._t_Experiment, False, 0),
        ('_pixelsSeq', (), _M_omero.model._t_ImagePixelsSeq, False, 0),
        ('_pixelsLoaded', (), IcePy._t_bool, False, 0),
        ('_wellSamplesSeq', (), _M_omero.model._t_ImageWellSamplesSeq, False, 0),
        ('_wellSamplesLoaded', (), IcePy._t_bool, False, 0),
        ('_roisSeq', (), _M_omero.model._t_ImageRoisSeq, False, 0),
        ('_roisLoaded', (), IcePy._t_bool, False, 0),
        ('_datasetLinksSeq', (), _M_omero.model._t_ImageDatasetLinksSeq, False, 0),
        ('_datasetLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_datasetLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_folderLinksSeq', (), _M_omero.model._t_ImageFolderLinksSeq, False, 0),
        ('_folderLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_folderLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_fileset', (), _M_omero.model._t_Fileset, False, 0),
        ('_annotationLinksSeq', (), _M_omero.model._t_ImageAnnotationLinksSeq, False, 0),
        ('_annotationLinksLoaded', (), IcePy._t_bool, False, 0),
        ('_annotationLinksCountPerOwner', (), _M_omero.sys._t_CountMap, False, 0),
        ('_name', (), _M_omero._t_RString, False, 0),
        ('_description', (), _M_omero._t_RString, False, 0)
    ))
    Image._ice_type = _M_omero.model._t_Image

    Image._op_getVersion = IcePy.Operation('getVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RInt, False, 0), ())
    Image._op_setVersion = IcePy.Operation('setVersion', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RInt, False, 0),), (), None, ())
    Image._op_getSeries = IcePy.Operation('getSeries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RInt, False, 0), ())
    Image._op_setSeries = IcePy.Operation('setSeries', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RInt, False, 0),), (), None, ())
    Image._op_getAcquisitionDate = IcePy.Operation('getAcquisitionDate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RTime, False, 0), ())
    Image._op_setAcquisitionDate = IcePy.Operation('setAcquisitionDate', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RTime, False, 0),), (), None, ())
    Image._op_getArchived = IcePy.Operation('getArchived', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RBool, False, 0), ())
    Image._op_setArchived = IcePy.Operation('setArchived', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RBool, False, 0),), (), None, ())
    Image._op_getPartial = IcePy.Operation('getPartial', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RBool, False, 0), ())
    Image._op_setPartial = IcePy.Operation('setPartial', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RBool, False, 0),), (), None, ())
    Image._op_getFormat = IcePy.Operation('getFormat', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Format, False, 0), ())
    Image._op_setFormat = IcePy.Operation('setFormat', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Format, False, 0),), (), None, ())
    Image._op_getImagingEnvironment = IcePy.Operation('getImagingEnvironment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImagingEnvironment, False, 0), ())
    Image._op_setImagingEnvironment = IcePy.Operation('setImagingEnvironment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImagingEnvironment, False, 0),), (), None, ())
    Image._op_getObjectiveSettings = IcePy.Operation('getObjectiveSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ObjectiveSettings, False, 0), ())
    Image._op_setObjectiveSettings = IcePy.Operation('setObjectiveSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ObjectiveSettings, False, 0),), (), None, ())
    Image._op_getInstrument = IcePy.Operation('getInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Instrument, False, 0), ())
    Image._op_setInstrument = IcePy.Operation('setInstrument', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Instrument, False, 0),), (), None, ())
    Image._op_getStageLabel = IcePy.Operation('getStageLabel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_StageLabel, False, 0), ())
    Image._op_setStageLabel = IcePy.Operation('setStageLabel', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_StageLabel, False, 0),), (), None, ())
    Image._op_getExperiment = IcePy.Operation('getExperiment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Experiment, False, 0), ())
    Image._op_setExperiment = IcePy.Operation('setExperiment', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Experiment, False, 0),), (), None, ())
    Image._op_unloadPixels = IcePy.Operation('unloadPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_sizeOfPixels = IcePy.Operation('sizeOfPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Image._op_copyPixels = IcePy.Operation('copyPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImagePixelsSeq, False, 0), ())
    Image._op_addPixels = IcePy.Operation('addPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Pixels, False, 0),), (), None, ())
    Image._op_addAllPixelsSet = IcePy.Operation('addAllPixelsSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImagePixelsSeq, False, 0),), (), None, ())
    Image._op_removePixels = IcePy.Operation('removePixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Pixels, False, 0),), (), None, ())
    Image._op_removeAllPixelsSet = IcePy.Operation('removeAllPixelsSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImagePixelsSeq, False, 0),), (), None, ())
    Image._op_clearPixels = IcePy.Operation('clearPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_reloadPixels = IcePy.Operation('reloadPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), None, ())
    Image._op_getPixels = IcePy.Operation('getPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_omero.model._t_Pixels, False, 0), ())
    Image._op_setPixels = IcePy.Operation('setPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0), ((), _M_omero.model._t_Pixels, False, 0)), (), ((), _M_omero.model._t_Pixels, False, 0), ())
    Image._op_getPrimaryPixels = IcePy.Operation('getPrimaryPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Pixels, False, 0), ())
    Image._op_setPrimaryPixels = IcePy.Operation('setPrimaryPixels', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Pixels, False, 0),), (), ((), _M_omero.model._t_Pixels, False, 0), ())
    Image._op_unloadWellSamples = IcePy.Operation('unloadWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_sizeOfWellSamples = IcePy.Operation('sizeOfWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Image._op_copyWellSamples = IcePy.Operation('copyWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageWellSamplesSeq, False, 0), ())
    Image._op_addWellSample = IcePy.Operation('addWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_WellSample, False, 0),), (), None, ())
    Image._op_addAllWellSampleSet = IcePy.Operation('addAllWellSampleSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageWellSamplesSeq, False, 0),), (), None, ())
    Image._op_removeWellSample = IcePy.Operation('removeWellSample', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_WellSample, False, 0),), (), None, ())
    Image._op_removeAllWellSampleSet = IcePy.Operation('removeAllWellSampleSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageWellSamplesSeq, False, 0),), (), None, ())
    Image._op_clearWellSamples = IcePy.Operation('clearWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_reloadWellSamples = IcePy.Operation('reloadWellSamples', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), None, ())
    Image._op_unloadRois = IcePy.Operation('unloadRois', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_sizeOfRois = IcePy.Operation('sizeOfRois', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Image._op_copyRois = IcePy.Operation('copyRois', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageRoisSeq, False, 0), ())
    Image._op_addRoi = IcePy.Operation('addRoi', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Roi, False, 0),), (), None, ())
    Image._op_addAllRoiSet = IcePy.Operation('addAllRoiSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageRoisSeq, False, 0),), (), None, ())
    Image._op_removeRoi = IcePy.Operation('removeRoi', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Roi, False, 0),), (), None, ())
    Image._op_removeAllRoiSet = IcePy.Operation('removeAllRoiSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageRoisSeq, False, 0),), (), None, ())
    Image._op_clearRois = IcePy.Operation('clearRois', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_reloadRois = IcePy.Operation('reloadRois', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), None, ())
    Image._op_unloadDatasetLinks = IcePy.Operation('unloadDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_sizeOfDatasetLinks = IcePy.Operation('sizeOfDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Image._op_copyDatasetLinks = IcePy.Operation('copyDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageDatasetLinksSeq, False, 0), ())
    Image._op_addDatasetImageLink = IcePy.Operation('addDatasetImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_DatasetImageLink, False, 0),), (), None, ())
    Image._op_addAllDatasetImageLinkSet = IcePy.Operation('addAllDatasetImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageDatasetLinksSeq, False, 0),), (), None, ())
    Image._op_removeDatasetImageLink = IcePy.Operation('removeDatasetImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_DatasetImageLink, False, 0),), (), None, ())
    Image._op_removeAllDatasetImageLinkSet = IcePy.Operation('removeAllDatasetImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageDatasetLinksSeq, False, 0),), (), None, ())
    Image._op_clearDatasetLinks = IcePy.Operation('clearDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_reloadDatasetLinks = IcePy.Operation('reloadDatasetLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), None, ())
    Image._op_getDatasetLinksCountPerOwner = IcePy.Operation('getDatasetLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Image._op_linkDataset = IcePy.Operation('linkDataset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Dataset, False, 0),), (), ((), _M_omero.model._t_DatasetImageLink, False, 0), ())
    Image._op_addDatasetImageLinkToBoth = IcePy.Operation('addDatasetImageLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_DatasetImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Image._op_findDatasetImageLink = IcePy.Operation('findDatasetImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Dataset, False, 0),), (), ((), _M_omero.model._t_ImageDatasetLinksSeq, False, 0), ())
    Image._op_unlinkDataset = IcePy.Operation('unlinkDataset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Dataset, False, 0),), (), None, ())
    Image._op_removeDatasetImageLinkFromBoth = IcePy.Operation('removeDatasetImageLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_DatasetImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Image._op_linkedDatasetList = IcePy.Operation('linkedDatasetList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageLinkedDatasetSeq, False, 0), ())
    Image._op_unloadFolderLinks = IcePy.Operation('unloadFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_sizeOfFolderLinks = IcePy.Operation('sizeOfFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Image._op_copyFolderLinks = IcePy.Operation('copyFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageFolderLinksSeq, False, 0), ())
    Image._op_addFolderImageLink = IcePy.Operation('addFolderImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0),), (), None, ())
    Image._op_addAllFolderImageLinkSet = IcePy.Operation('addAllFolderImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageFolderLinksSeq, False, 0),), (), None, ())
    Image._op_removeFolderImageLink = IcePy.Operation('removeFolderImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0),), (), None, ())
    Image._op_removeAllFolderImageLinkSet = IcePy.Operation('removeAllFolderImageLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageFolderLinksSeq, False, 0),), (), None, ())
    Image._op_clearFolderLinks = IcePy.Operation('clearFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_reloadFolderLinks = IcePy.Operation('reloadFolderLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), None, ())
    Image._op_getFolderLinksCountPerOwner = IcePy.Operation('getFolderLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Image._op_linkFolder = IcePy.Operation('linkFolder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), ((), _M_omero.model._t_FolderImageLink, False, 0), ())
    Image._op_addFolderImageLinkToBoth = IcePy.Operation('addFolderImageLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Image._op_findFolderImageLink = IcePy.Operation('findFolderImageLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), ((), _M_omero.model._t_ImageFolderLinksSeq, False, 0), ())
    Image._op_unlinkFolder = IcePy.Operation('unlinkFolder', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Folder, False, 0),), (), None, ())
    Image._op_removeFolderImageLinkFromBoth = IcePy.Operation('removeFolderImageLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_FolderImageLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Image._op_linkedFolderList = IcePy.Operation('linkedFolderList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageLinkedFolderSeq, False, 0), ())
    Image._op_getFileset = IcePy.Operation('getFileset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_Fileset, False, 0), ())
    Image._op_setFileset = IcePy.Operation('setFileset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Fileset, False, 0),), (), None, ())
    Image._op_unloadAnnotationLinks = IcePy.Operation('unloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_sizeOfAnnotationLinks = IcePy.Operation('sizeOfAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), IcePy._t_int, False, 0), ())
    Image._op_copyAnnotationLinks = IcePy.Operation('copyAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageAnnotationLinksSeq, False, 0), ())
    Image._op_addImageAnnotationLink = IcePy.Operation('addImageAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageAnnotationLink, False, 0),), (), None, ())
    Image._op_addAllImageAnnotationLinkSet = IcePy.Operation('addAllImageAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageAnnotationLinksSeq, False, 0),), (), None, ())
    Image._op_removeImageAnnotationLink = IcePy.Operation('removeImageAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageAnnotationLink, False, 0),), (), None, ())
    Image._op_removeAllImageAnnotationLinkSet = IcePy.Operation('removeAllImageAnnotationLinkSet', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageAnnotationLinksSeq, False, 0),), (), None, ())
    Image._op_clearAnnotationLinks = IcePy.Operation('clearAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), None, ())
    Image._op_reloadAnnotationLinks = IcePy.Operation('reloadAnnotationLinks', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Image, False, 0),), (), None, ())
    Image._op_getAnnotationLinksCountPerOwner = IcePy.Operation('getAnnotationLinksCountPerOwner', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.sys._t_CountMap, False, 0), ())
    Image._op_linkAnnotation = IcePy.Operation('linkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_ImageAnnotationLink, False, 0), ())
    Image._op_addImageAnnotationLinkToBoth = IcePy.Operation('addImageAnnotationLinkToBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Image._op_findImageAnnotationLink = IcePy.Operation('findImageAnnotationLink', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), ((), _M_omero.model._t_ImageAnnotationLinksSeq, False, 0), ())
    Image._op_unlinkAnnotation = IcePy.Operation('unlinkAnnotation', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_Annotation, False, 0),), (), None, ())
    Image._op_removeImageAnnotationLinkFromBoth = IcePy.Operation('removeImageAnnotationLinkFromBoth', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero.model._t_ImageAnnotationLink, False, 0), ((), IcePy._t_bool, False, 0)), (), None, ())
    Image._op_linkedAnnotationList = IcePy.Operation('linkedAnnotationList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero.model._t_ImageLinkedAnnotationSeq, False, 0), ())
    Image._op_getName = IcePy.Operation('getName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Image._op_setName = IcePy.Operation('setName', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())
    Image._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_omero._t_RString, False, 0), ())
    Image._op_setDescription = IcePy.Operation('setDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_omero._t_RString, False, 0),), (), None, ())

    _M_omero.model.Image = Image
    del Image

    _M_omero.model.ImagePrx = ImagePrx
    del ImagePrx

# End of module omero.model

__name__ = 'omero'

# End of module omero
