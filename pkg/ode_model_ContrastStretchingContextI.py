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
IceImport.load("ode_model_ContrastStretchingContext_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class ContrastStretchingContextI(_ode_model.ContrastStretchingContext):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "xstart",
          "ystart",
          "xend",
          "yend",
          "channelBinding",
          "details",
      ])
      _field_info = _field_info_type(
          xstart=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          ystart=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          xend=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          yend=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          channelBinding=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      XSTART =  "ode.model.display.ContrastStretchingContext_xstart"
      YSTART =  "ode.model.display.ContrastStretchingContext_ystart"
      XEND =  "ode.model.display.ContrastStretchingContext_xend"
      YEND =  "ode.model.display.ContrastStretchingContext_yend"
      CHANNELBINDING =  "ode.model.display.ContrastStretchingContext_channelBinding"
      DETAILS =  "ode.model.display.ContrastStretchingContext_details"
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
          super(ContrastStretchingContextI, self).__init__()
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
          self.unloadXstart( )
          self.unloadYstart( )
          self.unloadXend( )
          self.unloadYend( )
          self.unloadChannelBinding( )
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
            copy = ContrastStretchingContextI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return ContrastStretchingContextI( self._id.getValue(), False )

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

      def unloadXstart(self, ):
          self._xstartLoaded = False
          self._xstart = None;

      def getXstart(self, current = None):
          self.errorIfUnloaded()
          return self._xstart

      def setXstart(self, _xstart, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.xstart.wrapper is not None:
              if _xstart is not None:
                  _xstart = self._field_info.xstart.wrapper(_xstart)
          self._xstart = _xstart
          pass

      def unloadYstart(self, ):
          self._ystartLoaded = False
          self._ystart = None;

      def getYstart(self, current = None):
          self.errorIfUnloaded()
          return self._ystart

      def setYstart(self, _ystart, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.ystart.wrapper is not None:
              if _ystart is not None:
                  _ystart = self._field_info.ystart.wrapper(_ystart)
          self._ystart = _ystart
          pass

      def unloadXend(self, ):
          self._xendLoaded = False
          self._xend = None;

      def getXend(self, current = None):
          self.errorIfUnloaded()
          return self._xend

      def setXend(self, _xend, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.xend.wrapper is not None:
              if _xend is not None:
                  _xend = self._field_info.xend.wrapper(_xend)
          self._xend = _xend
          pass

      def unloadYend(self, ):
          self._yendLoaded = False
          self._yend = None;

      def getYend(self, current = None):
          self.errorIfUnloaded()
          return self._yend

      def setYend(self, _yend, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.yend.wrapper is not None:
              if _yend is not None:
                  _yend = self._field_info.yend.wrapper(_yend)
          self._yend = _yend
          pass

      def unloadChannelBinding(self, ):
          self._channelBindingLoaded = False
          self._channelBinding = None;

      def getChannelBinding(self, current = None):
          self.errorIfUnloaded()
          return self._channelBinding

      def setChannelBinding(self, _channelBinding, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.channelBinding.wrapper is not None:
              if _channelBinding is not None:
                  _channelBinding = self._field_info.channelBinding.wrapper(_channelBinding)
          self._channelBinding = _channelBinding
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

_ode_model.ContrastStretchingContextI = ContrastStretchingContextI
