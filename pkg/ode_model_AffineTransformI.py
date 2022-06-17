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
IceImport.load("ode_model_AffineTransform_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class AffineTransformI(_ode_model.AffineTransform):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "a00",
          "a10",
          "a01",
          "a11",
          "a02",
          "a12",
          "details",
      ])
      _field_info = _field_info_type(
          a00=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          a10=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          a01=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          a11=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          a02=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          a12=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      A00 =  "ode.model.roi.AffineTransform_a00"
      A10 =  "ode.model.roi.AffineTransform_a10"
      A01 =  "ode.model.roi.AffineTransform_a01"
      A11 =  "ode.model.roi.AffineTransform_a11"
      A02 =  "ode.model.roi.AffineTransform_a02"
      A12 =  "ode.model.roi.AffineTransform_a12"
      DETAILS =  "ode.model.roi.AffineTransform_details"
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
          super(AffineTransformI, self).__init__()
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
          self.unloadA00( )
          self.unloadA10( )
          self.unloadA01( )
          self.unloadA11( )
          self.unloadA02( )
          self.unloadA12( )
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
            copy = AffineTransformI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return AffineTransformI( self._id.getValue(), False )

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

      def unloadA00(self, ):
          self._a00Loaded = False
          self._a00 = None;

      def getA00(self, current = None):
          self.errorIfUnloaded()
          return self._a00

      def setA00(self, _a00, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.a00.wrapper is not None:
              if _a00 is not None:
                  _a00 = self._field_info.a00.wrapper(_a00)
          self._a00 = _a00
          pass

      def unloadA10(self, ):
          self._a10Loaded = False
          self._a10 = None;

      def getA10(self, current = None):
          self.errorIfUnloaded()
          return self._a10

      def setA10(self, _a10, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.a10.wrapper is not None:
              if _a10 is not None:
                  _a10 = self._field_info.a10.wrapper(_a10)
          self._a10 = _a10
          pass

      def unloadA01(self, ):
          self._a01Loaded = False
          self._a01 = None;

      def getA01(self, current = None):
          self.errorIfUnloaded()
          return self._a01

      def setA01(self, _a01, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.a01.wrapper is not None:
              if _a01 is not None:
                  _a01 = self._field_info.a01.wrapper(_a01)
          self._a01 = _a01
          pass

      def unloadA11(self, ):
          self._a11Loaded = False
          self._a11 = None;

      def getA11(self, current = None):
          self.errorIfUnloaded()
          return self._a11

      def setA11(self, _a11, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.a11.wrapper is not None:
              if _a11 is not None:
                  _a11 = self._field_info.a11.wrapper(_a11)
          self._a11 = _a11
          pass

      def unloadA02(self, ):
          self._a02Loaded = False
          self._a02 = None;

      def getA02(self, current = None):
          self.errorIfUnloaded()
          return self._a02

      def setA02(self, _a02, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.a02.wrapper is not None:
              if _a02 is not None:
                  _a02 = self._field_info.a02.wrapper(_a02)
          self._a02 = _a02
          pass

      def unloadA12(self, ):
          self._a12Loaded = False
          self._a12 = None;

      def getA12(self, current = None):
          self.errorIfUnloaded()
          return self._a12

      def setA12(self, _a12, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.a12.wrapper is not None:
              if _a12 is not None:
                  _a12 = self._field_info.a12.wrapper(_a12)
          self._a12 = _a12
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

_ode_model.AffineTransformI = AffineTransformI
