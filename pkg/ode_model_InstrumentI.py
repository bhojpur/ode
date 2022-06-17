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
IceImport.load("omero_model_Instrument_ice")
from omero.rtypes import rlong
from collections import namedtuple
_omero = Ice.openModule("omero")
_omero_model = Ice.openModule("omero.model")
__name__ = "omero.model"
class InstrumentI(_omero_model.Instrument):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "microscope",
          "detector",
          "objective",
          "lightSource",
          "filter",
          "dichroic",
          "filterSet",
          "otf",
          "annotationLinks",
          "details",
      ])
      _field_info = _field_info_type(
          microscope=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          detector=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          objective=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          lightSource=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          filter=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          dichroic=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          filterSet=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          otf=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          annotationLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
      )  # end _field_info
      MICROSCOPE =  "ode.model.acquisition.Instrument_microscope"
      DETECTOR =  "ode.model.acquisition.Instrument_detector"
      OBJECTIVE =  "ode.model.acquisition.Instrument_objective"
      LIGHTSOURCE =  "ode.model.acquisition.Instrument_lightSource"
      FILTER =  "ode.model.acquisition.Instrument_filter"
      DICHROIC =  "ode.model.acquisition.Instrument_dichroic"
      FILTERSET =  "ode.model.acquisition.Instrument_filterSet"
      OTF =  "ode.model.acquisition.Instrument_otf"
      ANNOTATIONLINKS =  "ode.model.acquisition.Instrument_annotationLinks"
      DETAILS =  "ode.model.acquisition.Instrument_details"
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
              self._detectorSeq = []
              self._detectorLoaded = True;
          else:
              self._detectorSeq = []
              self._detectorLoaded = False;

          if load:
              self._objectiveSeq = []
              self._objectiveLoaded = True;
          else:
              self._objectiveSeq = []
              self._objectiveLoaded = False;

          if load:
              self._lightSourceSeq = []
              self._lightSourceLoaded = True;
          else:
              self._lightSourceSeq = []
              self._lightSourceLoaded = False;

          if load:
              self._filterSeq = []
              self._filterLoaded = True;
          else:
              self._filterSeq = []
              self._filterLoaded = False;

          if load:
              self._dichroicSeq = []
              self._dichroicLoaded = True;
          else:
              self._dichroicSeq = []
              self._dichroicLoaded = False;

          if load:
              self._filterSetSeq = []
              self._filterSetLoaded = True;
          else:
              self._filterSetSeq = []
              self._filterSetLoaded = False;

          if load:
              self._otfSeq = []
              self._otfLoaded = True;
          else:
              self._otfSeq = []
              self._otfLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(InstrumentI, self).__init__()
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
          self.unloadMicroscope( )
          self.unloadDetector( )
          self.unloadObjective( )
          self.unloadLightSource( )
          self.unloadFilter( )
          self.unloadDichroic( )
          self.unloadFilterSet( )
          self.unloadOtf( )
          self.unloadAnnotationLinks( )
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
            copy = InstrumentI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise omero.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise omero.ClientError("Proxies require an id")
          return InstrumentI( self._id.getValue(), False )

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

      def unloadMicroscope(self, ):
          self._microscopeLoaded = False
          self._microscope = None;

      def getMicroscope(self, current = None):
          self.errorIfUnloaded()
          return self._microscope

      def setMicroscope(self, _microscope, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.microscope.wrapper is not None:
              if _microscope is not None:
                  _microscope = self._field_info.microscope.wrapper(_microscope)
          self._microscope = _microscope
          pass

      def unloadDetector(self, current = None):
          self._detectorLoaded = False
          self._detectorSeq = None;

      def _getDetector(self, current = None):
          self.errorIfUnloaded()
          return self._detectorSeq

      def _setDetector(self, _detector, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.detectorSeq.wrapper is not None:
              if _detector is not None:
                  _detector = self._field_info.detectorSeq.wrapper(_detector)
          self._detectorSeq = _detector
          self.checkUnloadedProperty(_detector,'detectorLoaded')

      def isDetectorLoaded(self):
          return self._detectorLoaded

      def sizeOfDetector(self, current = None):
          self.errorIfUnloaded()
          if not self._detectorLoaded: return -1
          return len(self._detectorSeq)

      def copyDetector(self, current = None):
          self.errorIfUnloaded()
          if not self._detectorLoaded: self.throwNullCollectionException("detectorSeq")
          return list(self._detectorSeq)

      def iterateDetector(self):
          self.errorIfUnloaded()
          if not self._detectorLoaded: self.throwNullCollectionException("detectorSeq")
          return iter(self._detectorSeq)

      def addDetector(self, target, current = None):
          self.errorIfUnloaded()
          if not self._detectorLoaded: self.throwNullCollectionException("detectorSeq")
          self._detectorSeq.append( target );
          target.setInstrument( self )

      def addAllDetectorSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._detectorLoaded: self.throwNullCollectionException("detectorSeq")
          self._detectorSeq.extend( targets )
          for target in targets:
              target.setInstrument( self )

      def removeDetector(self, target, current = None):
          self.errorIfUnloaded()
          if not self._detectorLoaded: self.throwNullCollectionException("detectorSeq")
          self._detectorSeq.remove( target )
          target.setInstrument( None )

      def removeAllDetectorSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._detectorLoaded: self.throwNullCollectionException("detectorSeq")
          for elt in targets:
              elt.setInstrument( None )
              self._detectorSeq.remove( elt )

      def clearDetector(self, current = None):
          self.errorIfUnloaded()
          if not self._detectorLoaded: self.throwNullCollectionException("detectorSeq")
          for elt in self._detectorSeq:
              elt.setInstrument( None )
          self._detectorSeq = list()

      def reloadDetector(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._detectorLoaded:
              raise omero.ClientError("Cannot reload active collection: detectorSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyDetector() # May also throw
          for elt in copy:
              elt.setInstrument( self )
          self._detectorSeq = copy
          toCopy.unloadDetector()
          self._detectorLoaded = True

      def unloadObjective(self, current = None):
          self._objectiveLoaded = False
          self._objectiveSeq = None;

      def _getObjective(self, current = None):
          self.errorIfUnloaded()
          return self._objectiveSeq

      def _setObjective(self, _objective, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.objectiveSeq.wrapper is not None:
              if _objective is not None:
                  _objective = self._field_info.objectiveSeq.wrapper(_objective)
          self._objectiveSeq = _objective
          self.checkUnloadedProperty(_objective,'objectiveLoaded')

      def isObjectiveLoaded(self):
          return self._objectiveLoaded

      def sizeOfObjective(self, current = None):
          self.errorIfUnloaded()
          if not self._objectiveLoaded: return -1
          return len(self._objectiveSeq)

      def copyObjective(self, current = None):
          self.errorIfUnloaded()
          if not self._objectiveLoaded: self.throwNullCollectionException("objectiveSeq")
          return list(self._objectiveSeq)

      def iterateObjective(self):
          self.errorIfUnloaded()
          if not self._objectiveLoaded: self.throwNullCollectionException("objectiveSeq")
          return iter(self._objectiveSeq)

      def addObjective(self, target, current = None):
          self.errorIfUnloaded()
          if not self._objectiveLoaded: self.throwNullCollectionException("objectiveSeq")
          self._objectiveSeq.append( target );
          target.setInstrument( self )

      def addAllObjectiveSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._objectiveLoaded: self.throwNullCollectionException("objectiveSeq")
          self._objectiveSeq.extend( targets )
          for target in targets:
              target.setInstrument( self )

      def removeObjective(self, target, current = None):
          self.errorIfUnloaded()
          if not self._objectiveLoaded: self.throwNullCollectionException("objectiveSeq")
          self._objectiveSeq.remove( target )
          target.setInstrument( None )

      def removeAllObjectiveSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._objectiveLoaded: self.throwNullCollectionException("objectiveSeq")
          for elt in targets:
              elt.setInstrument( None )
              self._objectiveSeq.remove( elt )

      def clearObjective(self, current = None):
          self.errorIfUnloaded()
          if not self._objectiveLoaded: self.throwNullCollectionException("objectiveSeq")
          for elt in self._objectiveSeq:
              elt.setInstrument( None )
          self._objectiveSeq = list()

      def reloadObjective(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._objectiveLoaded:
              raise omero.ClientError("Cannot reload active collection: objectiveSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyObjective() # May also throw
          for elt in copy:
              elt.setInstrument( self )
          self._objectiveSeq = copy
          toCopy.unloadObjective()
          self._objectiveLoaded = True

      def unloadLightSource(self, current = None):
          self._lightSourceLoaded = False
          self._lightSourceSeq = None;

      def _getLightSource(self, current = None):
          self.errorIfUnloaded()
          return self._lightSourceSeq

      def _setLightSource(self, _lightSource, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.lightSourceSeq.wrapper is not None:
              if _lightSource is not None:
                  _lightSource = self._field_info.lightSourceSeq.wrapper(_lightSource)
          self._lightSourceSeq = _lightSource
          self.checkUnloadedProperty(_lightSource,'lightSourceLoaded')

      def isLightSourceLoaded(self):
          return self._lightSourceLoaded

      def sizeOfLightSource(self, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceLoaded: return -1
          return len(self._lightSourceSeq)

      def copyLightSource(self, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceLoaded: self.throwNullCollectionException("lightSourceSeq")
          return list(self._lightSourceSeq)

      def iterateLightSource(self):
          self.errorIfUnloaded()
          if not self._lightSourceLoaded: self.throwNullCollectionException("lightSourceSeq")
          return iter(self._lightSourceSeq)

      def addLightSource(self, target, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceLoaded: self.throwNullCollectionException("lightSourceSeq")
          self._lightSourceSeq.append( target );
          target.setInstrument( self )

      def addAllLightSourceSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._lightSourceLoaded: self.throwNullCollectionException("lightSourceSeq")
          self._lightSourceSeq.extend( targets )
          for target in targets:
              target.setInstrument( self )

      def removeLightSource(self, target, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceLoaded: self.throwNullCollectionException("lightSourceSeq")
          self._lightSourceSeq.remove( target )
          target.setInstrument( None )

      def removeAllLightSourceSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceLoaded: self.throwNullCollectionException("lightSourceSeq")
          for elt in targets:
              elt.setInstrument( None )
              self._lightSourceSeq.remove( elt )

      def clearLightSource(self, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceLoaded: self.throwNullCollectionException("lightSourceSeq")
          for elt in self._lightSourceSeq:
              elt.setInstrument( None )
          self._lightSourceSeq = list()

      def reloadLightSource(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._lightSourceLoaded:
              raise omero.ClientError("Cannot reload active collection: lightSourceSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyLightSource() # May also throw
          for elt in copy:
              elt.setInstrument( self )
          self._lightSourceSeq = copy
          toCopy.unloadLightSource()
          self._lightSourceLoaded = True

      def unloadFilter(self, current = None):
          self._filterLoaded = False
          self._filterSeq = None;

      def _getFilter(self, current = None):
          self.errorIfUnloaded()
          return self._filterSeq

      def _setFilter(self, _filter, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.filterSeq.wrapper is not None:
              if _filter is not None:
                  _filter = self._field_info.filterSeq.wrapper(_filter)
          self._filterSeq = _filter
          self.checkUnloadedProperty(_filter,'filterLoaded')

      def isFilterLoaded(self):
          return self._filterLoaded

      def sizeOfFilter(self, current = None):
          self.errorIfUnloaded()
          if not self._filterLoaded: return -1
          return len(self._filterSeq)

      def copyFilter(self, current = None):
          self.errorIfUnloaded()
          if not self._filterLoaded: self.throwNullCollectionException("filterSeq")
          return list(self._filterSeq)

      def iterateFilter(self):
          self.errorIfUnloaded()
          if not self._filterLoaded: self.throwNullCollectionException("filterSeq")
          return iter(self._filterSeq)

      def addFilter(self, target, current = None):
          self.errorIfUnloaded()
          if not self._filterLoaded: self.throwNullCollectionException("filterSeq")
          self._filterSeq.append( target );
          target.setInstrument( self )

      def addAllFilterSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._filterLoaded: self.throwNullCollectionException("filterSeq")
          self._filterSeq.extend( targets )
          for target in targets:
              target.setInstrument( self )

      def removeFilter(self, target, current = None):
          self.errorIfUnloaded()
          if not self._filterLoaded: self.throwNullCollectionException("filterSeq")
          self._filterSeq.remove( target )
          target.setInstrument( None )

      def removeAllFilterSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._filterLoaded: self.throwNullCollectionException("filterSeq")
          for elt in targets:
              elt.setInstrument( None )
              self._filterSeq.remove( elt )

      def clearFilter(self, current = None):
          self.errorIfUnloaded()
          if not self._filterLoaded: self.throwNullCollectionException("filterSeq")
          for elt in self._filterSeq:
              elt.setInstrument( None )
          self._filterSeq = list()

      def reloadFilter(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._filterLoaded:
              raise omero.ClientError("Cannot reload active collection: filterSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyFilter() # May also throw
          for elt in copy:
              elt.setInstrument( self )
          self._filterSeq = copy
          toCopy.unloadFilter()
          self._filterLoaded = True

      def unloadDichroic(self, current = None):
          self._dichroicLoaded = False
          self._dichroicSeq = None;

      def _getDichroic(self, current = None):
          self.errorIfUnloaded()
          return self._dichroicSeq

      def _setDichroic(self, _dichroic, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.dichroicSeq.wrapper is not None:
              if _dichroic is not None:
                  _dichroic = self._field_info.dichroicSeq.wrapper(_dichroic)
          self._dichroicSeq = _dichroic
          self.checkUnloadedProperty(_dichroic,'dichroicLoaded')

      def isDichroicLoaded(self):
          return self._dichroicLoaded

      def sizeOfDichroic(self, current = None):
          self.errorIfUnloaded()
          if not self._dichroicLoaded: return -1
          return len(self._dichroicSeq)

      def copyDichroic(self, current = None):
          self.errorIfUnloaded()
          if not self._dichroicLoaded: self.throwNullCollectionException("dichroicSeq")
          return list(self._dichroicSeq)

      def iterateDichroic(self):
          self.errorIfUnloaded()
          if not self._dichroicLoaded: self.throwNullCollectionException("dichroicSeq")
          return iter(self._dichroicSeq)

      def addDichroic(self, target, current = None):
          self.errorIfUnloaded()
          if not self._dichroicLoaded: self.throwNullCollectionException("dichroicSeq")
          self._dichroicSeq.append( target );
          target.setInstrument( self )

      def addAllDichroicSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._dichroicLoaded: self.throwNullCollectionException("dichroicSeq")
          self._dichroicSeq.extend( targets )
          for target in targets:
              target.setInstrument( self )

      def removeDichroic(self, target, current = None):
          self.errorIfUnloaded()
          if not self._dichroicLoaded: self.throwNullCollectionException("dichroicSeq")
          self._dichroicSeq.remove( target )
          target.setInstrument( None )

      def removeAllDichroicSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._dichroicLoaded: self.throwNullCollectionException("dichroicSeq")
          for elt in targets:
              elt.setInstrument( None )
              self._dichroicSeq.remove( elt )

      def clearDichroic(self, current = None):
          self.errorIfUnloaded()
          if not self._dichroicLoaded: self.throwNullCollectionException("dichroicSeq")
          for elt in self._dichroicSeq:
              elt.setInstrument( None )
          self._dichroicSeq = list()

      def reloadDichroic(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._dichroicLoaded:
              raise omero.ClientError("Cannot reload active collection: dichroicSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyDichroic() # May also throw
          for elt in copy:
              elt.setInstrument( self )
          self._dichroicSeq = copy
          toCopy.unloadDichroic()
          self._dichroicLoaded = True

      def unloadFilterSet(self, current = None):
          self._filterSetLoaded = False
          self._filterSetSeq = None;

      def _getFilterSet(self, current = None):
          self.errorIfUnloaded()
          return self._filterSetSeq

      def _setFilterSet(self, _filterSet, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.filterSetSeq.wrapper is not None:
              if _filterSet is not None:
                  _filterSet = self._field_info.filterSetSeq.wrapper(_filterSet)
          self._filterSetSeq = _filterSet
          self.checkUnloadedProperty(_filterSet,'filterSetLoaded')

      def isFilterSetLoaded(self):
          return self._filterSetLoaded

      def sizeOfFilterSet(self, current = None):
          self.errorIfUnloaded()
          if not self._filterSetLoaded: return -1
          return len(self._filterSetSeq)

      def copyFilterSet(self, current = None):
          self.errorIfUnloaded()
          if not self._filterSetLoaded: self.throwNullCollectionException("filterSetSeq")
          return list(self._filterSetSeq)

      def iterateFilterSet(self):
          self.errorIfUnloaded()
          if not self._filterSetLoaded: self.throwNullCollectionException("filterSetSeq")
          return iter(self._filterSetSeq)

      def addFilterSet(self, target, current = None):
          self.errorIfUnloaded()
          if not self._filterSetLoaded: self.throwNullCollectionException("filterSetSeq")
          self._filterSetSeq.append( target );
          target.setInstrument( self )

      def addAllFilterSetSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._filterSetLoaded: self.throwNullCollectionException("filterSetSeq")
          self._filterSetSeq.extend( targets )
          for target in targets:
              target.setInstrument( self )

      def removeFilterSet(self, target, current = None):
          self.errorIfUnloaded()
          if not self._filterSetLoaded: self.throwNullCollectionException("filterSetSeq")
          self._filterSetSeq.remove( target )
          target.setInstrument( None )

      def removeAllFilterSetSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._filterSetLoaded: self.throwNullCollectionException("filterSetSeq")
          for elt in targets:
              elt.setInstrument( None )
              self._filterSetSeq.remove( elt )

      def clearFilterSet(self, current = None):
          self.errorIfUnloaded()
          if not self._filterSetLoaded: self.throwNullCollectionException("filterSetSeq")
          for elt in self._filterSetSeq:
              elt.setInstrument( None )
          self._filterSetSeq = list()

      def reloadFilterSet(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._filterSetLoaded:
              raise omero.ClientError("Cannot reload active collection: filterSetSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyFilterSet() # May also throw
          for elt in copy:
              elt.setInstrument( self )
          self._filterSetSeq = copy
          toCopy.unloadFilterSet()
          self._filterSetLoaded = True

      def unloadOtf(self, current = None):
          self._otfLoaded = False
          self._otfSeq = None;

      def _getOtf(self, current = None):
          self.errorIfUnloaded()
          return self._otfSeq

      def _setOtf(self, _otf, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.otfSeq.wrapper is not None:
              if _otf is not None:
                  _otf = self._field_info.otfSeq.wrapper(_otf)
          self._otfSeq = _otf
          self.checkUnloadedProperty(_otf,'otfLoaded')

      def isOtfLoaded(self):
          return self._otfLoaded

      def sizeOfOtf(self, current = None):
          self.errorIfUnloaded()
          if not self._otfLoaded: return -1
          return len(self._otfSeq)

      def copyOtf(self, current = None):
          self.errorIfUnloaded()
          if not self._otfLoaded: self.throwNullCollectionException("otfSeq")
          return list(self._otfSeq)

      def iterateOtf(self):
          self.errorIfUnloaded()
          if not self._otfLoaded: self.throwNullCollectionException("otfSeq")
          return iter(self._otfSeq)

      def addOTF(self, target, current = None):
          self.errorIfUnloaded()
          if not self._otfLoaded: self.throwNullCollectionException("otfSeq")
          self._otfSeq.append( target );
          target.setInstrument( self )

      def addAllOTFSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._otfLoaded: self.throwNullCollectionException("otfSeq")
          self._otfSeq.extend( targets )
          for target in targets:
              target.setInstrument( self )

      def removeOTF(self, target, current = None):
          self.errorIfUnloaded()
          if not self._otfLoaded: self.throwNullCollectionException("otfSeq")
          self._otfSeq.remove( target )
          target.setInstrument( None )

      def removeAllOTFSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._otfLoaded: self.throwNullCollectionException("otfSeq")
          for elt in targets:
              elt.setInstrument( None )
              self._otfSeq.remove( elt )

      def clearOtf(self, current = None):
          self.errorIfUnloaded()
          if not self._otfLoaded: self.throwNullCollectionException("otfSeq")
          for elt in self._otfSeq:
              elt.setInstrument( None )
          self._otfSeq = list()

      def reloadOtf(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._otfLoaded:
              raise omero.ClientError("Cannot reload active collection: otfSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyOtf() # May also throw
          for elt in copy:
              elt.setInstrument( self )
          self._otfSeq = copy
          toCopy.unloadOtf()
          self._otfLoaded = True

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

      def addInstrumentAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllInstrumentAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeInstrumentAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllInstrumentAnnotationLinkSet(self, targets, current = None):
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
          link = _omero_model.InstrumentAnnotationLinkI()
          link.link( self, addition );
          self.addInstrumentAnnotationLinkToBoth( link, True )
          return link

      def addInstrumentAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findInstrumentAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findInstrumentAnnotationLink(removal)
          for next in toRemove:
              self.removeInstrumentAnnotationLinkFromBoth( next, True )

      def removeInstrumentAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

_omero_model.InstrumentI = InstrumentI
