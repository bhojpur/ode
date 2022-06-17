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
import ode
IceImport.load("ode_model_DetailsI")
IceImport.load("ode_model_MicrobeamManipulation_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class MicrobeamManipulationI(_ode_model.MicrobeamManipulation):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "type",
          "lightSourceSettings",
          "experiment",
          "description",
          "details",
      ])
      _field_info = _field_info_type(
          type=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          lightSourceSettings=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          experiment=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          description=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      TYPE =  "ode.model.experiment.MicrobeamManipulation_type"
      LIGHTSOURCESETTINGS =  "ode.model.experiment.MicrobeamManipulation_lightSourceSettings"
      EXPERIMENT =  "ode.model.experiment.MicrobeamManipulation_experiment"
      DESCRIPTION =  "ode.model.experiment.MicrobeamManipulation_description"
      DETAILS =  "ode.model.experiment.MicrobeamManipulation_details"
      def errorIfUnloaded(self):
          if not self._loaded:
              raise _ode.UnloadedEntityException("Object unloaded:"+str(self))

      def throwNullCollectionException(self,propertyName):
          raise _ode.UnloadedEntityException(""+
          "Error updating collection:" + propertyName +"\n"+
          "Collection is currently null. This can be seen\n" +
          "by testing \""+ propertyName +"Loaded\". This implies\n"+
          "that this collection was unloaded. Please refresh this object\n"+
          "in order to update this collection.\n")

      def _toggleCollectionsLoaded(self, load):
          if load:
              self._lightSourceSettingsSeq = []
              self._lightSourceSettingsLoaded = True;
          else:
              self._lightSourceSettingsSeq = []
              self._lightSourceSettingsLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(MicrobeamManipulationI, self).__init__()
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
              # Relying on ode.rtypes.rlong's error-handling
              self._id = rlong(id)
              if loaded is None:
                  loaded = True  # Assume true as previously
          self._loaded = loaded
          if self._loaded:
             self._details = _ode_model.DetailsI()
             self._toggleCollectionsLoaded(True)

      def unload(self, current = None):
          self._loaded = False
          self.unloadType( )
          self.unloadLightSourceSettings( )
          self.unloadExperiment( )
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
          return  False ;
      def isLink(self, current = None):
          return  False ;
      def shallowCopy(self, current = None):
            if not self._loaded: return self.proxy()
            copy = MicrobeamManipulationI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return MicrobeamManipulationI( self._id.getValue(), False )

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

      def unloadType(self, ):
          self._typeLoaded = False
          self._type = None;

      def getType(self, current = None):
          self.errorIfUnloaded()
          return self._type

      def setType(self, _type, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.type.wrapper is not None:
              if _type is not None:
                  _type = self._field_info.type.wrapper(_type)
          self._type = _type
          pass

      def unloadLightSourceSettings(self, current = None):
          self._lightSourceSettingsLoaded = False
          self._lightSourceSettingsSeq = None;

      def _getLightSourceSettings(self, current = None):
          self.errorIfUnloaded()
          return self._lightSourceSettingsSeq

      def _setLightSourceSettings(self, _lightSourceSettings, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.lightSourceSettingsSeq.wrapper is not None:
              if _lightSourceSettings is not None:
                  _lightSourceSettings = self._field_info.lightSourceSettingsSeq.wrapper(_lightSourceSettings)
          self._lightSourceSettingsSeq = _lightSourceSettings
          self.checkUnloadedProperty(_lightSourceSettings,'lightSourceSettingsLoaded')

      def isLightSourceSettingsLoaded(self):
          return self._lightSourceSettingsLoaded

      def sizeOfLightSourceSettings(self, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceSettingsLoaded: return -1
          return len(self._lightSourceSettingsSeq)

      def copyLightSourceSettings(self, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceSettingsLoaded: self.throwNullCollectionException("lightSourceSettingsSeq")
          return list(self._lightSourceSettingsSeq)

      def iterateLightSourceSettings(self):
          self.errorIfUnloaded()
          if not self._lightSourceSettingsLoaded: self.throwNullCollectionException("lightSourceSettingsSeq")
          return iter(self._lightSourceSettingsSeq)

      def addLightSettings(self, target, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceSettingsLoaded: self.throwNullCollectionException("lightSourceSettingsSeq")
          self._lightSourceSettingsSeq.append( target );
          target.setMicrobeamManipulation( self )

      def addAllLightSettingsSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._lightSourceSettingsLoaded: self.throwNullCollectionException("lightSourceSettingsSeq")
          self._lightSourceSettingsSeq.extend( targets )
          for target in targets:
              target.setMicrobeamManipulation( self )

      def removeLightSettings(self, target, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceSettingsLoaded: self.throwNullCollectionException("lightSourceSettingsSeq")
          self._lightSourceSettingsSeq.remove( target )
          target.setMicrobeamManipulation( None )

      def removeAllLightSettingsSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceSettingsLoaded: self.throwNullCollectionException("lightSourceSettingsSeq")
          for elt in targets:
              elt.setMicrobeamManipulation( None )
              self._lightSourceSettingsSeq.remove( elt )

      def clearLightSourceSettings(self, current = None):
          self.errorIfUnloaded()
          if not self._lightSourceSettingsLoaded: self.throwNullCollectionException("lightSourceSettingsSeq")
          for elt in self._lightSourceSettingsSeq:
              elt.setMicrobeamManipulation( None )
          self._lightSourceSettingsSeq = list()

      def reloadLightSourceSettings(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._lightSourceSettingsLoaded:
              raise ode.ClientError("Cannot reload active collection: lightSourceSettingsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyLightSourceSettings() # May also throw
          for elt in copy:
              elt.setMicrobeamManipulation( self )
          self._lightSourceSettingsSeq = copy
          toCopy.unloadLightSourceSettings()
          self._lightSourceSettingsLoaded = True

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

_ode_model.MicrobeamManipulationI = MicrobeamManipulationI
