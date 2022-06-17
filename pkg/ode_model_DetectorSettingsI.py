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
IceImport.load("ode_model_DetectorSettings_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class DetectorSettingsI(_ode_model.DetectorSettings):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "voltage",
          "gain",
          "offsetValue",
          "readOutRate",
          "binning",
          "integration",
          "zoom",
          "detector",
          "details",
      ])
      _field_info = _field_info_type(
          voltage=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          gain=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=True),
          offsetValue=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=True),
          readOutRate=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          binning=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          integration=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          zoom=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=True),
          detector=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      VOLTAGE =  "ode.model.acquisition.DetectorSettings_voltage"
      GAIN =  "ode.model.acquisition.DetectorSettings_gain"
      OFFSETVALUE =  "ode.model.acquisition.DetectorSettings_offsetValue"
      READOUTRATE =  "ode.model.acquisition.DetectorSettings_readOutRate"
      BINNING =  "ode.model.acquisition.DetectorSettings_binning"
      INTEGRATION =  "ode.model.acquisition.DetectorSettings_integration"
      ZOOM =  "ode.model.acquisition.DetectorSettings_zoom"
      DETECTOR =  "ode.model.acquisition.DetectorSettings_detector"
      DETAILS =  "ode.model.acquisition.DetectorSettings_details"
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
          super(DetectorSettingsI, self).__init__()
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
          self.unloadVoltage( )
          self.unloadGain( )
          self.unloadOffsetValue( )
          self.unloadReadOutRate( )
          self.unloadBinning( )
          self.unloadIntegration( )
          self.unloadZoom( )
          self.unloadDetector( )
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
            copy = DetectorSettingsI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return DetectorSettingsI( self._id.getValue(), False )

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

      def unloadVoltage(self, ):
          self._voltageLoaded = False
          self._voltage = None;

      def getVoltage(self, current = None):
          self.errorIfUnloaded()
          return self._voltage

      def setVoltage(self, _voltage, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.voltage.wrapper is not None:
              if _voltage is not None:
                  _voltage = self._field_info.voltage.wrapper(_voltage)
          self._voltage = _voltage
          pass

      def unloadGain(self, ):
          self._gainLoaded = False
          self._gain = None;

      def getGain(self, current = None):
          self.errorIfUnloaded()
          return self._gain

      def setGain(self, _gain, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.gain.wrapper is not None:
              if _gain is not None:
                  _gain = self._field_info.gain.wrapper(_gain)
          self._gain = _gain
          pass

      def unloadOffsetValue(self, ):
          self._offsetValueLoaded = False
          self._offsetValue = None;

      def getOffsetValue(self, current = None):
          self.errorIfUnloaded()
          return self._offsetValue

      def setOffsetValue(self, _offsetValue, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.offsetValue.wrapper is not None:
              if _offsetValue is not None:
                  _offsetValue = self._field_info.offsetValue.wrapper(_offsetValue)
          self._offsetValue = _offsetValue
          pass

      def unloadReadOutRate(self, ):
          self._readOutRateLoaded = False
          self._readOutRate = None;

      def getReadOutRate(self, current = None):
          self.errorIfUnloaded()
          return self._readOutRate

      def setReadOutRate(self, _readOutRate, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.readOutRate.wrapper is not None:
              if _readOutRate is not None:
                  _readOutRate = self._field_info.readOutRate.wrapper(_readOutRate)
          self._readOutRate = _readOutRate
          pass

      def unloadBinning(self, ):
          self._binningLoaded = False
          self._binning = None;

      def getBinning(self, current = None):
          self.errorIfUnloaded()
          return self._binning

      def setBinning(self, _binning, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.binning.wrapper is not None:
              if _binning is not None:
                  _binning = self._field_info.binning.wrapper(_binning)
          self._binning = _binning
          pass

      def unloadIntegration(self, ):
          self._integrationLoaded = False
          self._integration = None;

      def getIntegration(self, current = None):
          self.errorIfUnloaded()
          return self._integration

      def setIntegration(self, _integration, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.integration.wrapper is not None:
              if _integration is not None:
                  _integration = self._field_info.integration.wrapper(_integration)
          self._integration = _integration
          pass

      def unloadZoom(self, ):
          self._zoomLoaded = False
          self._zoom = None;

      def getZoom(self, current = None):
          self.errorIfUnloaded()
          return self._zoom

      def setZoom(self, _zoom, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.zoom.wrapper is not None:
              if _zoom is not None:
                  _zoom = self._field_info.zoom.wrapper(_zoom)
          self._zoom = _zoom
          pass

      def unloadDetector(self, ):
          self._detectorLoaded = False
          self._detector = None;

      def getDetector(self, current = None):
          self.errorIfUnloaded()
          return self._detector

      def setDetector(self, _detector, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.detector.wrapper is not None:
              if _detector is not None:
                  _detector = self._field_info.detector.wrapper(_detector)
          self._detector = _detector
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

_ode_model.DetectorSettingsI = DetectorSettingsI
