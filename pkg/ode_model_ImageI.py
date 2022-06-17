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

import Ice
import IceImport
import omero
IceImport.load("omero_model_DetailsI")
IceImport.load("omero_model_Image_ice")
from omero.rtypes import rlong
from collections import namedtuple
_omero = Ice.openModule("omero")
_omero_model = Ice.openModule("omero.model")
__name__ = "omero.model"
class ImageI(_omero_model.Image):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "series",
          "acquisitionDate",
          "archived",
          "partial",
          "format",
          "imagingEnvironment",
          "objectiveSettings",
          "instrument",
          "stageLabel",
          "experiment",
          "pixels",
          "wellSamples",
          "rois",
          "datasetLinks",
          "folderLinks",
          "fileset",
          "annotationLinks",
          "name",
          "description",
          "details",
      ])
      _field_info = _field_info_type(
          series=_field_info_data(wrapper=omero.rtypes.rint, nullable=True),
          acquisitionDate=_field_info_data(wrapper=omero.rtypes.rtime, nullable=True),
          archived=_field_info_data(wrapper=omero.rtypes.rbool, nullable=True),
          partial=_field_info_data(wrapper=omero.rtypes.rbool, nullable=True),
          format=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          imagingEnvironment=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          objectiveSettings=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          instrument=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          stageLabel=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          experiment=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          pixels=_field_info_data(wrapper=omero.proxy_to_instance, nullable=False),
          wellSamples=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          rois=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          datasetLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          folderLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          fileset=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          annotationLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          name=_field_info_data(wrapper=omero.rtypes.rstring, nullable=False),
          description=_field_info_data(wrapper=omero.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
      )  # end _field_info
      SERIES =  "ode.model.core.Image_series"
      ACQUISITIONDATE =  "ode.model.core.Image_acquisitionDate"
      ARCHIVED =  "ode.model.core.Image_archived"
      PARTIAL =  "ode.model.core.Image_partial"
      FORMAT =  "ode.model.core.Image_format"
      IMAGINGENVIRONMENT =  "ode.model.core.Image_imagingEnvironment"
      OBJECTIVESETTINGS =  "ode.model.core.Image_objectiveSettings"
      INSTRUMENT =  "ode.model.core.Image_instrument"
      STAGELABEL =  "ode.model.core.Image_stageLabel"
      EXPERIMENT =  "ode.model.core.Image_experiment"
      PIXELS =  "ode.model.core.Image_pixels"
      WELLSAMPLES =  "ode.model.core.Image_wellSamples"
      ROIS =  "ode.model.core.Image_rois"
      DATASETLINKS =  "ode.model.core.Image_datasetLinks"
      FOLDERLINKS =  "ode.model.core.Image_folderLinks"
      FILESET =  "ode.model.core.Image_fileset"
      ANNOTATIONLINKS =  "ode.model.core.Image_annotationLinks"
      NAME =  "ode.model.core.Image_name"
      DESCRIPTION =  "ode.model.core.Image_description"
      DETAILS =  "ode.model.core.Image_details"
      def errorIfUnloaded(self):
          if not self._loaded:
              raise _omero.UnloadedEntityException("Object unloaded:"+str(self))

      def throwNullCollectionException(self,propertyName):
          raise _omero.UnloadedEntityException(""+
          "Error updating collection:" + propertyName +"\n"+
          "Collection is currently null. This can be seen\n" +
          "by testing \""+ propertyName +"Loaded\". This implies\n"+
          "that this collection was unloaded. Please refresh this object\n"+
          "in order to update this collection.\n")

      def _toggleCollectionsLoaded(self, load):
          if load:
              self._pixelsSeq = []
              self._pixelsLoaded = True;
          else:
              self._pixelsSeq = []
              self._pixelsLoaded = False;

          if load:
              self._wellSamplesSeq = []
              self._wellSamplesLoaded = True;
          else:
              self._wellSamplesSeq = []
              self._wellSamplesLoaded = False;

          if load:
              self._roisSeq = []
              self._roisLoaded = True;
          else:
              self._roisSeq = []
              self._roisLoaded = False;

          if load:
              self._datasetLinksSeq = []
              self._datasetLinksLoaded = True;
          else:
              self._datasetLinksSeq = []
              self._datasetLinksLoaded = False;

          if load:
              self._folderLinksSeq = []
              self._folderLinksLoaded = True;
          else:
              self._folderLinksSeq = []
              self._folderLinksLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(ImageI, self).__init__()
          if id is not None and isinstance(id, (str, unicode)) and ":" in id:
              parts = id.split(":")
              if len(parts) != 2:
                  raise Exception("Invalid proxy string: %s", id)
              if parts[0] != self.__class__.__name__ and \
                 parts[0]+"I" != self.__class__.__name__:
                  raise Exception("Proxy class mismatch: %s<>%s" %
                  (self.__class__.__name__, parts[0]))
              self._id = rlong(parts[1])
              if loaded is None:
                  # If no loadedness was requested with
                  # a proxy string, then assume False.
                  loaded = False
          else:
              # Relying on omero.rtypes.rlong's error-handling
              self._id = rlong(id)
              if loaded is None:
                  loaded = True  # Assume true as previously
          self._loaded = loaded
          if self._loaded:
             self._details = _omero_model.DetailsI()
             self._toggleCollectionsLoaded(True)

      def unload(self, current = None):
          self._loaded = False
          self.unloadSeries( )
          self.unloadAcquisitionDate( )
          self.unloadArchived( )
          self.unloadPartial( )
          self.unloadFormat( )
          self.unloadImagingEnvironment( )
          self.unloadObjectiveSettings( )
          self.unloadInstrument( )
          self.unloadStageLabel( )
          self.unloadExperiment( )
          self.unloadPixels( )
          self.unloadWellSamples( )
          self.unloadRois( )
          self.unloadDatasetLinks( )
          self.unloadFolderLinks( )
          self.unloadFileset( )
          self.unloadAnnotationLinks( )
          self.unloadName( )
          self.unloadDescription( )
          self.unloadDetails( )

      def isLoaded(self, current = None):
          return self._loaded
      def unloadCollections(self, current = None):
          self._toggleCollectionsLoaded( False )
      def isGlobal(self, current = None):
          return  False ;
      def isMutable(self, current = None):
          return  True ;
      def isAnnotated(self, current = None):
          return  True ;
      def isLink(self, current = None):
          return  False ;
      def shallowCopy(self, current = None):
            if not self._loaded: return self.proxy()
            copy = ImageI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise omero.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise omero.ClientError("Proxies require an id")
          return ImageI( self._id.getValue(), False )

      def getDetails(self, current = None):
          self.errorIfUnloaded()
          return self._details

      def unloadDetails(self, current = None):
          self._details = None

      def getId(self, current = None):
          return self._id

      def setId(self, _id, current = None):
          self._id = _id

      def checkUnloadedProperty(self, value, loadedField):
          if value == None:
              self.__dict__[loadedField] = False
          else:
              self.__dict__[loadedField] = True

      def getVersion(self, current = None):
          self.errorIfUnloaded()
          return self._version

      def setVersion(self, version, current = None):
          self.errorIfUnloaded()
          self._version = version

      def unloadSeries(self, ):
          self._seriesLoaded = False
          self._series = None;

      def getSeries(self, current = None):
          self.errorIfUnloaded()
          return self._series

      def setSeries(self, _series, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.series.wrapper is not None:
              if _series is not None:
                  _series = self._field_info.series.wrapper(_series)
          self._series = _series
          pass

      def unloadAcquisitionDate(self, ):
          self._acquisitionDateLoaded = False
          self._acquisitionDate = None;

      def getAcquisitionDate(self, current = None):
          self.errorIfUnloaded()
          return self._acquisitionDate

      def setAcquisitionDate(self, _acquisitionDate, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.acquisitionDate.wrapper is not None:
              if _acquisitionDate is not None:
                  _acquisitionDate = self._field_info.acquisitionDate.wrapper(_acquisitionDate)
          self._acquisitionDate = _acquisitionDate
          pass

      def unloadArchived(self, ):
          self._archivedLoaded = False
          self._archived = None;

      def getArchived(self, current = None):
          self.errorIfUnloaded()
          return self._archived

      def setArchived(self, _archived, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.archived.wrapper is not None:
              if _archived is not None:
                  _archived = self._field_info.archived.wrapper(_archived)
          self._archived = _archived
          pass

      def unloadPartial(self, ):
          self._partialLoaded = False
          self._partial = None;

      def getPartial(self, current = None):
          self.errorIfUnloaded()
          return self._partial

      def setPartial(self, _partial, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.partial.wrapper is not None:
              if _partial is not None:
                  _partial = self._field_info.partial.wrapper(_partial)
          self._partial = _partial
          pass

      def unloadFormat(self, ):
          self._formatLoaded = False
          self._format = None;

      def getFormat(self, current = None):
          self.errorIfUnloaded()
          return self._format

      def setFormat(self, _format, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.format.wrapper is not None:
              if _format is not None:
                  _format = self._field_info.format.wrapper(_format)
          self._format = _format
          pass

      def unloadImagingEnvironment(self, ):
          self._imagingEnvironmentLoaded = False
          self._imagingEnvironment = None;

      def getImagingEnvironment(self, current = None):
          self.errorIfUnloaded()
          return self._imagingEnvironment

      def setImagingEnvironment(self, _imagingEnvironment, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.imagingEnvironment.wrapper is not None:
              if _imagingEnvironment is not None:
                  _imagingEnvironment = self._field_info.imagingEnvironment.wrapper(_imagingEnvironment)
          self._imagingEnvironment = _imagingEnvironment
          pass

      def unloadObjectiveSettings(self, ):
          self._objectiveSettingsLoaded = False
          self._objectiveSettings = None;

      def getObjectiveSettings(self, current = None):
          self.errorIfUnloaded()
          return self._objectiveSettings

      def setObjectiveSettings(self, _objectiveSettings, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.objectiveSettings.wrapper is not None:
              if _objectiveSettings is not None:
                  _objectiveSettings = self._field_info.objectiveSettings.wrapper(_objectiveSettings)
          self._objectiveSettings = _objectiveSettings
          pass

      def unloadInstrument(self, ):
          self._instrumentLoaded = False
          self._instrument = None;

      def getInstrument(self, current = None):
          self.errorIfUnloaded()
          return self._instrument

      def setInstrument(self, _instrument, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.instrument.wrapper is not None:
              if _instrument is not None:
                  _instrument = self._field_info.instrument.wrapper(_instrument)
          self._instrument = _instrument
          pass

      def unloadStageLabel(self, ):
          self._stageLabelLoaded = False
          self._stageLabel = None;

      def getStageLabel(self, current = None):
          self.errorIfUnloaded()
          return self._stageLabel

      def setStageLabel(self, _stageLabel, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.stageLabel.wrapper is not None:
              if _stageLabel is not None:
                  _stageLabel = self._field_info.stageLabel.wrapper(_stageLabel)
          self._stageLabel = _stageLabel
          pass

      def unloadExperiment(self, ):
          self._experimentLoaded = False
          self._experiment = None;

      def getExperiment(self, current = None):
          self.errorIfUnloaded()
          return self._experiment

      def setExperiment(self, _experiment, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.experiment.wrapper is not None:
              if _experiment is not None:
                  _experiment = self._field_info.experiment.wrapper(_experiment)
          self._experiment = _experiment
          pass

      def unloadPixels(self, current = None):
          self._pixelsLoaded = False
          self._pixelsSeq = None;

      def _getPixels(self, current = None):
          self.errorIfUnloaded()
          return self._pixelsSeq

      def _setPixels(self, _pixels, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.pixelsSeq.wrapper is not None:
              if _pixels is not None:
                  _pixels = self._field_info.pixelsSeq.wrapper(_pixels)
          self._pixelsSeq = _pixels
          self.checkUnloadedProperty(_pixels,'pixelsLoaded')

      def isPixelsLoaded(self):
          return self._pixelsLoaded

      def sizeOfPixels(self, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: return -1
          return len(self._pixelsSeq)

      def copyPixels(self, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          return list(self._pixelsSeq)

      def iteratePixels(self):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          return iter(self._pixelsSeq)

      def addPixels(self, target, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          self._pixelsSeq.append( target );
          target.setImage( self )

      def addAllPixelsSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          self._pixelsSeq.extend( targets )
          for target in targets:
              target.setImage( self )

      def removePixels(self, target, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          self._pixelsSeq.remove( target )
          target.setImage( None )

      def removeAllPixelsSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          for elt in targets:
              elt.setImage( None )
              self._pixelsSeq.remove( elt )

      def clearPixels(self, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          for elt in self._pixelsSeq:
              elt.setImage( None )
          self._pixelsSeq = list()

      def reloadPixels(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._pixelsLoaded:
              raise omero.ClientError("Cannot reload active collection: pixelsSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyPixels() # May also throw
          for elt in copy:
              elt.setImage( self )
          self._pixelsSeq = copy
          toCopy.unloadPixels()
          self._pixelsLoaded = True

      def getPixels(self, index, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          return self._pixelsSeq[index]

      def setPixels(self, index, element, current = None, wrap=False):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          old = self._pixelsSeq[index]
          if wrap and self._field_info.pixelsSeq.wrapper is not None:
              if element is not None:
                  element = self._field_info.pixelsSeq.wrapper(_pixels)
          self._pixelsSeq[index] =  element
          if element is not None and element.isLoaded():
              element.setImage( self )
          return old

      def getPrimaryPixels(self, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          return self._pixelsSeq[0]

      def setPrimaryPixels(self, element, current = None):
          self.errorIfUnloaded()
          if not self._pixelsLoaded: self.throwNullCollectionException("pixelsSeq")
          index = self._pixelsSeq.index(element)
          old = self._pixelsSeq[0]
          self._pixelsSeq[index] = old
          self._pixelsSeq[0] = element
          return old

      def unloadWellSamples(self, current = None):
          self._wellSamplesLoaded = False
          self._wellSamplesSeq = None;

      def _getWellSamples(self, current = None):
          self.errorIfUnloaded()
          return self._wellSamplesSeq

      def _setWellSamples(self, _wellSamples, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.wellSamplesSeq.wrapper is not None:
              if _wellSamples is not None:
                  _wellSamples = self._field_info.wellSamplesSeq.wrapper(_wellSamples)
          self._wellSamplesSeq = _wellSamples
          self.checkUnloadedProperty(_wellSamples,'wellSamplesLoaded')

      def isWellSamplesLoaded(self):
          return self._wellSamplesLoaded

      def sizeOfWellSamples(self, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: return -1
          return len(self._wellSamplesSeq)

      def copyWellSamples(self, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          return list(self._wellSamplesSeq)

      def iterateWellSamples(self):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          return iter(self._wellSamplesSeq)

      def addWellSample(self, target, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          self._wellSamplesSeq.append( target );
          target.setImage( self )

      def addAllWellSampleSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          self._wellSamplesSeq.extend( targets )
          for target in targets:
              target.setImage( self )

      def removeWellSample(self, target, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          self._wellSamplesSeq.remove( target )
          target.setImage( None )

      def removeAllWellSampleSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          for elt in targets:
              elt.setImage( None )
              self._wellSamplesSeq.remove( elt )

      def clearWellSamples(self, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          for elt in self._wellSamplesSeq:
              elt.setImage( None )
          self._wellSamplesSeq = list()

      def reloadWellSamples(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._wellSamplesLoaded:
              raise omero.ClientError("Cannot reload active collection: wellSamplesSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyWellSamples() # May also throw
          for elt in copy:
              elt.setImage( self )
          self._wellSamplesSeq = copy
          toCopy.unloadWellSamples()
          self._wellSamplesLoaded = True

      def unloadRois(self, current = None):
          self._roisLoaded = False
          self._roisSeq = None;

      def _getRois(self, current = None):
          self.errorIfUnloaded()
          return self._roisSeq

      def _setRois(self, _rois, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.roisSeq.wrapper is not None:
              if _rois is not None:
                  _rois = self._field_info.roisSeq.wrapper(_rois)
          self._roisSeq = _rois
          self.checkUnloadedProperty(_rois,'roisLoaded')

      def isRoisLoaded(self):
          return self._roisLoaded

      def sizeOfRois(self, current = None):
          self.errorIfUnloaded()
          if not self._roisLoaded: return -1
          return len(self._roisSeq)

      def copyRois(self, current = None):
          self.errorIfUnloaded()
          if not self._roisLoaded: self.throwNullCollectionException("roisSeq")
          return list(self._roisSeq)

      def iterateRois(self):
          self.errorIfUnloaded()
          if not self._roisLoaded: self.throwNullCollectionException("roisSeq")
          return iter(self._roisSeq)

      def addRoi(self, target, current = None):
          self.errorIfUnloaded()
          if not self._roisLoaded: self.throwNullCollectionException("roisSeq")
          self._roisSeq.append( target );
          target.setImage( self )

      def addAllRoiSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._roisLoaded: self.throwNullCollectionException("roisSeq")
          self._roisSeq.extend( targets )
          for target in targets:
              target.setImage( self )

      def removeRoi(self, target, current = None):
          self.errorIfUnloaded()
          if not self._roisLoaded: self.throwNullCollectionException("roisSeq")
          self._roisSeq.remove( target )
          target.setImage( None )

      def removeAllRoiSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._roisLoaded: self.throwNullCollectionException("roisSeq")
          for elt in targets:
              elt.setImage( None )
              self._roisSeq.remove( elt )

      def clearRois(self, current = None):
          self.errorIfUnloaded()
          if not self._roisLoaded: self.throwNullCollectionException("roisSeq")
          for elt in self._roisSeq:
              elt.setImage( None )
          self._roisSeq = list()

      def reloadRois(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._roisLoaded:
              raise omero.ClientError("Cannot reload active collection: roisSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyRois() # May also throw
          for elt in copy:
              elt.setImage( self )
          self._roisSeq = copy
          toCopy.unloadRois()
          self._roisLoaded = True

      def unloadDatasetLinks(self, current = None):
          self._datasetLinksLoaded = False
          self._datasetLinksSeq = None;

      def _getDatasetLinks(self, current = None):
          self.errorIfUnloaded()
          return self._datasetLinksSeq

      def _setDatasetLinks(self, _datasetLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.datasetLinksSeq.wrapper is not None:
              if _datasetLinks is not None:
                  _datasetLinks = self._field_info.datasetLinksSeq.wrapper(_datasetLinks)
          self._datasetLinksSeq = _datasetLinks
          self.checkUnloadedProperty(_datasetLinks,'datasetLinksLoaded')

      def isDatasetLinksLoaded(self):
          return self._datasetLinksLoaded

      def sizeOfDatasetLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: return -1
          return len(self._datasetLinksSeq)

      def copyDatasetLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          return list(self._datasetLinksSeq)

      def iterateDatasetLinks(self):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          return iter(self._datasetLinksSeq)

      def addDatasetImageLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          self._datasetLinksSeq.append( target );
          target.setChild( self )

      def addAllDatasetImageLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          self._datasetLinksSeq.extend( targets )
          for target in targets:
              target.setChild( self )

      def removeDatasetImageLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          self._datasetLinksSeq.remove( target )
          target.setChild( None )

      def removeAllDatasetImageLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          for elt in targets:
              elt.setChild( None )
              self._datasetLinksSeq.remove( elt )

      def clearDatasetLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          for elt in self._datasetLinksSeq:
              elt.setChild( None )
          self._datasetLinksSeq = list()

      def reloadDatasetLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._datasetLinksLoaded:
              raise omero.ClientError("Cannot reload active collection: datasetLinksSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyDatasetLinks() # May also throw
          for elt in copy:
              elt.setChild( self )
          self._datasetLinksSeq = copy
          toCopy.unloadDatasetLinks()
          self._datasetLinksLoaded = True

      def getDatasetLinksCountPerOwner(self, current = None):
          return self._datasetLinksCountPerOwner

      def linkDataset(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          link = _omero_model.DatasetImageLinkI()
          link.link( addition, self );
          self.addDatasetImageLinkToBoth( link, True )
          return link

      def addDatasetImageLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          self._datasetLinksSeq.append( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().addDatasetImageLinkToBoth( link, False )

      def findDatasetImageLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          result = list()
          for link in self._datasetLinksSeq:
              if link.getParent() == removal: result.append(link)
          return result

      def unlinkDataset(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          toRemove = self.findDatasetImageLink(removal)
          for next in toRemove:
              self.removeDatasetImageLinkFromBoth( next, True )

      def removeDatasetImageLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._datasetLinksLoaded: self.throwNullCollectionException("datasetLinksSeq")
          self._datasetLinksSeq.remove( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().removeDatasetImageLinkFromBoth(link, False)

      def linkedDatasetList(self, current = None):
          self.errorIfUnloaded()
          if not self.datasetLinksLoaded: self.throwNullCollectionException("DatasetLinks")
          linked = []
          for link in self._datasetLinksSeq:
              linked.append( link.getParent() )
          return linked

      def unloadFolderLinks(self, current = None):
          self._folderLinksLoaded = False
          self._folderLinksSeq = None;

      def _getFolderLinks(self, current = None):
          self.errorIfUnloaded()
          return self._folderLinksSeq

      def _setFolderLinks(self, _folderLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.folderLinksSeq.wrapper is not None:
              if _folderLinks is not None:
                  _folderLinks = self._field_info.folderLinksSeq.wrapper(_folderLinks)
          self._folderLinksSeq = _folderLinks
          self.checkUnloadedProperty(_folderLinks,'folderLinksLoaded')

      def isFolderLinksLoaded(self):
          return self._folderLinksLoaded

      def sizeOfFolderLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: return -1
          return len(self._folderLinksSeq)

      def copyFolderLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          return list(self._folderLinksSeq)

      def iterateFolderLinks(self):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          return iter(self._folderLinksSeq)

      def addFolderImageLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.append( target );
          target.setChild( self )

      def addAllFolderImageLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.extend( targets )
          for target in targets:
              target.setChild( self )

      def removeFolderImageLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.remove( target )
          target.setChild( None )

      def removeAllFolderImageLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          for elt in targets:
              elt.setChild( None )
              self._folderLinksSeq.remove( elt )

      def clearFolderLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          for elt in self._folderLinksSeq:
              elt.setChild( None )
          self._folderLinksSeq = list()

      def reloadFolderLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._folderLinksLoaded:
              raise omero.ClientError("Cannot reload active collection: folderLinksSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyFolderLinks() # May also throw
          for elt in copy:
              elt.setChild( self )
          self._folderLinksSeq = copy
          toCopy.unloadFolderLinks()
          self._folderLinksLoaded = True

      def getFolderLinksCountPerOwner(self, current = None):
          return self._folderLinksCountPerOwner

      def linkFolder(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          link = _omero_model.FolderImageLinkI()
          link.link( addition, self );
          self.addFolderImageLinkToBoth( link, True )
          return link

      def addFolderImageLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.append( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().addFolderImageLinkToBoth( link, False )

      def findFolderImageLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          result = list()
          for link in self._folderLinksSeq:
              if link.getParent() == removal: result.append(link)
          return result

      def unlinkFolder(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          toRemove = self.findFolderImageLink(removal)
          for next in toRemove:
              self.removeFolderImageLinkFromBoth( next, True )

      def removeFolderImageLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.remove( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().removeFolderImageLinkFromBoth(link, False)

      def linkedFolderList(self, current = None):
          self.errorIfUnloaded()
          if not self.folderLinksLoaded: self.throwNullCollectionException("FolderLinks")
          linked = []
          for link in self._folderLinksSeq:
              linked.append( link.getParent() )
          return linked

      def unloadFileset(self, ):
          self._filesetLoaded = False
          self._fileset = None;

      def getFileset(self, current = None):
          self.errorIfUnloaded()
          return self._fileset

      def setFileset(self, _fileset, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.fileset.wrapper is not None:
              if _fileset is not None:
                  _fileset = self._field_info.fileset.wrapper(_fileset)
          self._fileset = _fileset
          pass

      def unloadAnnotationLinks(self, current = None):
          self._annotationLinksLoaded = False
          self._annotationLinksSeq = None;

      def _getAnnotationLinks(self, current = None):
          self.errorIfUnloaded()
          return self._annotationLinksSeq

      def _setAnnotationLinks(self, _annotationLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.annotationLinksSeq.wrapper is not None:
              if _annotationLinks is not None:
                  _annotationLinks = self._field_info.annotationLinksSeq.wrapper(_annotationLinks)
          self._annotationLinksSeq = _annotationLinks
          self.checkUnloadedProperty(_annotationLinks,'annotationLinksLoaded')

      def isAnnotationLinksLoaded(self):
          return self._annotationLinksLoaded

      def sizeOfAnnotationLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: return -1
          return len(self._annotationLinksSeq)

      def copyAnnotationLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          return list(self._annotationLinksSeq)

      def iterateAnnotationLinks(self):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          return iter(self._annotationLinksSeq)

      def addImageAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllImageAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeImageAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllImageAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          for elt in targets:
              elt.setParent( None )
              self._annotationLinksSeq.remove( elt )

      def clearAnnotationLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          for elt in self._annotationLinksSeq:
              elt.setParent( None )
          self._annotationLinksSeq = list()

      def reloadAnnotationLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._annotationLinksLoaded:
              raise omero.ClientError("Cannot reload active collection: annotationLinksSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyAnnotationLinks() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._annotationLinksSeq = copy
          toCopy.unloadAnnotationLinks()
          self._annotationLinksLoaded = True

      def getAnnotationLinksCountPerOwner(self, current = None):
          return self._annotationLinksCountPerOwner

      def linkAnnotation(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          link = _omero_model.ImageAnnotationLinkI()
          link.link( self, addition );
          self.addImageAnnotationLinkToBoth( link, True )
          return link

      def addImageAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findImageAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findImageAnnotationLink(removal)
          for next in toRemove:
              self.removeImageAnnotationLinkFromBoth( next, True )

      def removeImageAnnotationLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( link )

      def linkedAnnotationList(self, current = None):
          self.errorIfUnloaded()
          if not self.annotationLinksLoaded: self.throwNullCollectionException("AnnotationLinks")
          linked = []
          for link in self._annotationLinksSeq:
              linked.append( link.getChild() )
          return linked

      def unloadName(self, ):
          self._nameLoaded = False
          self._name = None;

      def getName(self, current = None):
          self.errorIfUnloaded()
          return self._name

      def setName(self, _name, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.name.wrapper is not None:
              if _name is not None:
                  _name = self._field_info.name.wrapper(_name)
          self._name = _name
          pass

      def unloadDescription(self, ):
          self._descriptionLoaded = False
          self._description = None;

      def getDescription(self, current = None):
          self.errorIfUnloaded()
          return self._description

      def setDescription(self, _description, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.description.wrapper is not None:
              if _description is not None:
                  _description = self._field_info.description.wrapper(_description)
          self._description = _description
          pass


      def ice_postUnmarshal(self):
          """
          Provides additional initialization once all data loaded
          """
          pass # Currently unused


      def ice_preMarshal(self):
          """
          Provides additional validation before data is sent
          """
          pass # Currently unused

      def __getattr__(self, name):
          import __builtin__
          """
          Reroutes all access to object.field through object.getField() or object.isField()
          """
          if "_" in name:  # Ice disallows underscores, so these should be treated normally.
              return object.__getattribute__(self, name)
          field  = "_" + name
          capitalized = name[0].capitalize() + name[1:]
          getter = "get" + capitalized
          questn = "is" + capitalized
          try:
              self.__dict__[field]
              if hasattr(self, getter):
                  method = getattr(self, getter)
                  return method()
              elif hasattr(self, questn):
                  method = getattr(self, questn)
                  return method()
          except:
              pass
          raise AttributeError("'%s' object has no attribute '%s' or '%s'" % (self.__class__.__name__, getter, questn))

      def __setattr__(self, name, value):
          """
          Reroutes all access to object.field through object.getField(), with the caveat
          that all sets on variables starting with "_" are permitted directly.
          """
          if name.startswith("_"):
              self.__dict__[name] = value
              return
          else:
              field  = "_" + name
              setter = "set" + name[0].capitalize() + name[1:]
              if hasattr(self, field) and hasattr(self, setter):
                  method = getattr(self, setter)
                  return method(value)
          raise AttributeError("'%s' object has no attribute '%s'" % (self.__class__.__name__, setter))

_omero_model.ImageI = ImageI
