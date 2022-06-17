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
IceImport.load("ode_model_TransmittanceRange_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class TransmittanceRangeI(_ode_model.TransmittanceRange):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "cutIn",
          "cutOut",
          "cutInTolerance",
          "cutOutTolerance",
          "transmittance",
          "details",
      ])
      _field_info = _field_info_type(
          cutIn=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          cutOut=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          cutInTolerance=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          cutOutTolerance=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          transmittance=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      CUTIN =  "ode.model.acquisition.TransmittanceRange_cutIn"
      CUTOUT =  "ode.model.acquisition.TransmittanceRange_cutOut"
      CUTINTOLERANCE =  "ode.model.acquisition.TransmittanceRange_cutInTolerance"
      CUTOUTTOLERANCE =  "ode.model.acquisition.TransmittanceRange_cutOutTolerance"
      TRANSMITTANCE =  "ode.model.acquisition.TransmittanceRange_transmittance"
      DETAILS =  "ode.model.acquisition.TransmittanceRange_details"
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
          super(TransmittanceRangeI, self).__init__()
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
          self.unloadCutIn( )
          self.unloadCutOut( )
          self.unloadCutInTolerance( )
          self.unloadCutOutTolerance( )
          self.unloadTransmittance( )
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
            copy = TransmittanceRangeI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return TransmittanceRangeI( self._id.getValue(), False )

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

      def unloadCutIn(self, ):
          self._cutInLoaded = False
          self._cutIn = None;

      def getCutIn(self, current = None):
          self.errorIfUnloaded()
          return self._cutIn

      def setCutIn(self, _cutIn, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.cutIn.wrapper is not None:
              if _cutIn is not None:
                  _cutIn = self._field_info.cutIn.wrapper(_cutIn)
          self._cutIn = _cutIn
          pass

      def unloadCutOut(self, ):
          self._cutOutLoaded = False
          self._cutOut = None;

      def getCutOut(self, current = None):
          self.errorIfUnloaded()
          return self._cutOut

      def setCutOut(self, _cutOut, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.cutOut.wrapper is not None:
              if _cutOut is not None:
                  _cutOut = self._field_info.cutOut.wrapper(_cutOut)
          self._cutOut = _cutOut
          pass

      def unloadCutInTolerance(self, ):
          self._cutInToleranceLoaded = False
          self._cutInTolerance = None;

      def getCutInTolerance(self, current = None):
          self.errorIfUnloaded()
          return self._cutInTolerance

      def setCutInTolerance(self, _cutInTolerance, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.cutInTolerance.wrapper is not None:
              if _cutInTolerance is not None:
                  _cutInTolerance = self._field_info.cutInTolerance.wrapper(_cutInTolerance)
          self._cutInTolerance = _cutInTolerance
          pass

      def unloadCutOutTolerance(self, ):
          self._cutOutToleranceLoaded = False
          self._cutOutTolerance = None;

      def getCutOutTolerance(self, current = None):
          self.errorIfUnloaded()
          return self._cutOutTolerance

      def setCutOutTolerance(self, _cutOutTolerance, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.cutOutTolerance.wrapper is not None:
              if _cutOutTolerance is not None:
                  _cutOutTolerance = self._field_info.cutOutTolerance.wrapper(_cutOutTolerance)
          self._cutOutTolerance = _cutOutTolerance
          pass

      def unloadTransmittance(self, ):
          self._transmittanceLoaded = False
          self._transmittance = None;

      def getTransmittance(self, current = None):
          self.errorIfUnloaded()
          return self._transmittance

      def setTransmittance(self, _transmittance, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.transmittance.wrapper is not None:
              if _transmittance is not None:
                  _transmittance = self._field_info.transmittance.wrapper(_transmittance)
          self._transmittance = _transmittance
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

_ode_model.TransmittanceRangeI = TransmittanceRangeI
