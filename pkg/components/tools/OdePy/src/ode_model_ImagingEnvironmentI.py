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
IceImport.load("ode_model_ImagingEnvironment_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class ImagingEnvironmentI(_ode_model.ImagingEnvironment):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "temperature",
          "airPressure",
          "humidity",
          "co2percent",
          "map",
          "details",
      ])
      _field_info = _field_info_type(
          temperature=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          airPressure=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          humidity=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=True),
          co2percent=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=True),
          map=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      TEMPERATURE =  "ode.model.acquisition.ImagingEnvironment_temperature"
      AIRPRESSURE =  "ode.model.acquisition.ImagingEnvironment_airPressure"
      HUMIDITY =  "ode.model.acquisition.ImagingEnvironment_humidity"
      CO2PERCENT =  "ode.model.acquisition.ImagingEnvironment_co2percent"
      MAP =  "ode.model.acquisition.ImagingEnvironment_map"
      DETAILS =  "ode.model.acquisition.ImagingEnvironment_details"
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
          super(ImagingEnvironmentI, self).__init__()
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
          self.unloadTemperature( )
          self.unloadAirPressure( )
          self.unloadHumidity( )
          self.unloadCo2percent( )
          self.unloadMap( )
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
            copy = ImagingEnvironmentI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return ImagingEnvironmentI( self._id.getValue(), False )

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

      def unloadTemperature(self, ):
          self._temperatureLoaded = False
          self._temperature = None;

      def getTemperature(self, current = None):
          self.errorIfUnloaded()
          return self._temperature

      def setTemperature(self, _temperature, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.temperature.wrapper is not None:
              if _temperature is not None:
                  _temperature = self._field_info.temperature.wrapper(_temperature)
          self._temperature = _temperature
          pass

      def unloadAirPressure(self, ):
          self._airPressureLoaded = False
          self._airPressure = None;

      def getAirPressure(self, current = None):
          self.errorIfUnloaded()
          return self._airPressure

      def setAirPressure(self, _airPressure, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.airPressure.wrapper is not None:
              if _airPressure is not None:
                  _airPressure = self._field_info.airPressure.wrapper(_airPressure)
          self._airPressure = _airPressure
          pass

      def unloadHumidity(self, ):
          self._humidityLoaded = False
          self._humidity = None;

      def getHumidity(self, current = None):
          self.errorIfUnloaded()
          return self._humidity

      def setHumidity(self, _humidity, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.humidity.wrapper is not None:
              if _humidity is not None:
                  _humidity = self._field_info.humidity.wrapper(_humidity)
          self._humidity = _humidity
          pass

      def unloadCo2percent(self, ):
          self._co2percentLoaded = False
          self._co2percent = None;

      def getCo2percent(self, current = None):
          self.errorIfUnloaded()
          return self._co2percent

      def setCo2percent(self, _co2percent, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.co2percent.wrapper is not None:
              if _co2percent is not None:
                  _co2percent = self._field_info.co2percent.wrapper(_co2percent)
          self._co2percent = _co2percent
          pass

      def unloadMap(self, ):
          self._mapLoaded = False
          self._map = None;

      def getMap(self, current = None):
          self.errorIfUnloaded()
          return self._map

      def setMap(self, _map, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.map.wrapper is not None:
              if _map is not None:
                  _map = self._field_info.map.wrapper(_map)
          self._map = _map
          pass

      def getMapAsMap(self, current = None):
          self.errorIfUnloaded()
          rv = dict()
          for nv in self._map:
              if nv is not None:
                  rv[nv.name] = nv.value
          return rv


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

_ode_model.ImagingEnvironmentI = ImagingEnvironmentI
