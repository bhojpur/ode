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
IceImport.load("ode_model_OTF_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class OTFI(_ode_model.OTF):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "sizeX",
          "sizeY",
          "opticalAxisAveraged",
          "pixelsType",
          "path",
          "filterSet",
          "objective",
          "instrument",
          "details",
      ])
      _field_info = _field_info_type(
          sizeX=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          sizeY=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          opticalAxisAveraged=_field_info_data(wrapper=ode.rtypes.rbool, nullable=False),
          pixelsType=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          path=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          filterSet=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          objective=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          instrument=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      SIZEX =  "ode.model.acquisition.OTF_sizeX"
      SIZEY =  "ode.model.acquisition.OTF_sizeY"
      OPTICALAXISAVERAGED =  "ode.model.acquisition.OTF_opticalAxisAveraged"
      PIXELSTYPE =  "ode.model.acquisition.OTF_pixelsType"
      PATH =  "ode.model.acquisition.OTF_path"
      FILTERSET =  "ode.model.acquisition.OTF_filterSet"
      OBJECTIVE =  "ode.model.acquisition.OTF_objective"
      INSTRUMENT =  "ode.model.acquisition.OTF_instrument"
      DETAILS =  "ode.model.acquisition.OTF_details"
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
          pass

      def __init__(self, id=None, loaded=None):
          super(OTFI, self).__init__()
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
          self.unloadSizeX( )
          self.unloadSizeY( )
          self.unloadOpticalAxisAveraged( )
          self.unloadPixelsType( )
          self.unloadPath( )
          self.unloadFilterSet( )
          self.unloadObjective( )
          self.unloadInstrument( )
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
            copy = OTFI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return OTFI( self._id.getValue(), False )

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

      def unloadSizeX(self, ):
          self._sizeXLoaded = False
          self._sizeX = None;

      def getSizeX(self, current = None):
          self.errorIfUnloaded()
          return self._sizeX

      def setSizeX(self, _sizeX, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sizeX.wrapper is not None:
              if _sizeX is not None:
                  _sizeX = self._field_info.sizeX.wrapper(_sizeX)
          self._sizeX = _sizeX
          pass

      def unloadSizeY(self, ):
          self._sizeYLoaded = False
          self._sizeY = None;

      def getSizeY(self, current = None):
          self.errorIfUnloaded()
          return self._sizeY

      def setSizeY(self, _sizeY, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sizeY.wrapper is not None:
              if _sizeY is not None:
                  _sizeY = self._field_info.sizeY.wrapper(_sizeY)
          self._sizeY = _sizeY
          pass

      def unloadOpticalAxisAveraged(self, ):
          self._opticalAxisAveragedLoaded = False
          self._opticalAxisAveraged = None;

      def getOpticalAxisAveraged(self, current = None):
          self.errorIfUnloaded()
          return self._opticalAxisAveraged

      def setOpticalAxisAveraged(self, _opticalAxisAveraged, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.opticalAxisAveraged.wrapper is not None:
              if _opticalAxisAveraged is not None:
                  _opticalAxisAveraged = self._field_info.opticalAxisAveraged.wrapper(_opticalAxisAveraged)
          self._opticalAxisAveraged = _opticalAxisAveraged
          pass

      def unloadPixelsType(self, ):
          self._pixelsTypeLoaded = False
          self._pixelsType = None;

      def getPixelsType(self, current = None):
          self.errorIfUnloaded()
          return self._pixelsType

      def setPixelsType(self, _pixelsType, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.pixelsType.wrapper is not None:
              if _pixelsType is not None:
                  _pixelsType = self._field_info.pixelsType.wrapper(_pixelsType)
          self._pixelsType = _pixelsType
          pass

      def unloadPath(self, ):
          self._pathLoaded = False
          self._path = None;

      def getPath(self, current = None):
          self.errorIfUnloaded()
          return self._path

      def setPath(self, _path, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.path.wrapper is not None:
              if _path is not None:
                  _path = self._field_info.path.wrapper(_path)
          self._path = _path
          pass

      def unloadFilterSet(self, ):
          self._filterSetLoaded = False
          self._filterSet = None;

      def getFilterSet(self, current = None):
          self.errorIfUnloaded()
          return self._filterSet

      def setFilterSet(self, _filterSet, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.filterSet.wrapper is not None:
              if _filterSet is not None:
                  _filterSet = self._field_info.filterSet.wrapper(_filterSet)
          self._filterSet = _filterSet
          pass

      def unloadObjective(self, ):
          self._objectiveLoaded = False
          self._objective = None;

      def getObjective(self, current = None):
          self.errorIfUnloaded()
          return self._objective

      def setObjective(self, _objective, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.objective.wrapper is not None:
              if _objective is not None:
                  _objective = self._field_info.objective.wrapper(_objective)
          self._objective = _objective
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

_ode_model.OTFI = OTFI
