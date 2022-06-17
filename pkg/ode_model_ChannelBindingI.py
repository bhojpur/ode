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
IceImport.load("ode_model_ChannelBinding_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class ChannelBindingI(_ode_model.ChannelBinding):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "renderingDef",
          "family",
          "coefficient",
          "inputStart",
          "inputEnd",
          "active",
          "noiseReduction",
          "red",
          "green",
          "blue",
          "alpha",
          "lookupTable",
          "spatialDomainEnhancement",
          "details",
      ])
      _field_info = _field_info_type(
          renderingDef=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          family=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          coefficient=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          inputStart=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          inputEnd=_field_info_data(wrapper=ode.rtypes.rdouble, nullable=False),
          active=_field_info_data(wrapper=ode.rtypes.rbool, nullable=False),
          noiseReduction=_field_info_data(wrapper=ode.rtypes.rbool, nullable=False),
          red=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          green=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          blue=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          alpha=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          lookupTable=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          spatialDomainEnhancement=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      RENDERINGDEF =  "ode.model.display.ChannelBinding_renderingDef"
      FAMILY =  "ode.model.display.ChannelBinding_family"
      COEFFICIENT =  "ode.model.display.ChannelBinding_coefficient"
      INPUTSTART =  "ode.model.display.ChannelBinding_inputStart"
      INPUTEND =  "ode.model.display.ChannelBinding_inputEnd"
      ACTIVE =  "ode.model.display.ChannelBinding_active"
      NOISEREDUCTION =  "ode.model.display.ChannelBinding_noiseReduction"
      RED =  "ode.model.display.ChannelBinding_red"
      GREEN =  "ode.model.display.ChannelBinding_green"
      BLUE =  "ode.model.display.ChannelBinding_blue"
      ALPHA =  "ode.model.display.ChannelBinding_alpha"
      LOOKUPTABLE =  "ode.model.display.ChannelBinding_lookupTable"
      SPATIALDOMAINENHANCEMENT =  "ode.model.display.ChannelBinding_spatialDomainEnhancement"
      DETAILS =  "ode.model.display.ChannelBinding_details"
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
              self._spatialDomainEnhancementSeq = []
              self._spatialDomainEnhancementLoaded = True;
          else:
              self._spatialDomainEnhancementSeq = []
              self._spatialDomainEnhancementLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(ChannelBindingI, self).__init__()
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
          self.unloadRenderingDef( )
          self.unloadFamily( )
          self.unloadCoefficient( )
          self.unloadInputStart( )
          self.unloadInputEnd( )
          self.unloadActive( )
          self.unloadNoiseReduction( )
          self.unloadRed( )
          self.unloadGreen( )
          self.unloadBlue( )
          self.unloadAlpha( )
          self.unloadLookupTable( )
          self.unloadSpatialDomainEnhancement( )
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
            copy = ChannelBindingI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return ChannelBindingI( self._id.getValue(), False )

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

      def unloadRenderingDef(self, ):
          self._renderingDefLoaded = False
          self._renderingDef = None;

      def getRenderingDef(self, current = None):
          self.errorIfUnloaded()
          return self._renderingDef

      def setRenderingDef(self, _renderingDef, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.renderingDef.wrapper is not None:
              if _renderingDef is not None:
                  _renderingDef = self._field_info.renderingDef.wrapper(_renderingDef)
          self._renderingDef = _renderingDef
          pass

      def unloadFamily(self, ):
          self._familyLoaded = False
          self._family = None;

      def getFamily(self, current = None):
          self.errorIfUnloaded()
          return self._family

      def setFamily(self, _family, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.family.wrapper is not None:
              if _family is not None:
                  _family = self._field_info.family.wrapper(_family)
          self._family = _family
          pass

      def unloadCoefficient(self, ):
          self._coefficientLoaded = False
          self._coefficient = None;

      def getCoefficient(self, current = None):
          self.errorIfUnloaded()
          return self._coefficient

      def setCoefficient(self, _coefficient, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.coefficient.wrapper is not None:
              if _coefficient is not None:
                  _coefficient = self._field_info.coefficient.wrapper(_coefficient)
          self._coefficient = _coefficient
          pass

      def unloadInputStart(self, ):
          self._inputStartLoaded = False
          self._inputStart = None;

      def getInputStart(self, current = None):
          self.errorIfUnloaded()
          return self._inputStart

      def setInputStart(self, _inputStart, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.inputStart.wrapper is not None:
              if _inputStart is not None:
                  _inputStart = self._field_info.inputStart.wrapper(_inputStart)
          self._inputStart = _inputStart
          pass

      def unloadInputEnd(self, ):
          self._inputEndLoaded = False
          self._inputEnd = None;

      def getInputEnd(self, current = None):
          self.errorIfUnloaded()
          return self._inputEnd

      def setInputEnd(self, _inputEnd, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.inputEnd.wrapper is not None:
              if _inputEnd is not None:
                  _inputEnd = self._field_info.inputEnd.wrapper(_inputEnd)
          self._inputEnd = _inputEnd
          pass

      def unloadActive(self, ):
          self._activeLoaded = False
          self._active = None;

      def getActive(self, current = None):
          self.errorIfUnloaded()
          return self._active

      def setActive(self, _active, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.active.wrapper is not None:
              if _active is not None:
                  _active = self._field_info.active.wrapper(_active)
          self._active = _active
          pass

      def unloadNoiseReduction(self, ):
          self._noiseReductionLoaded = False
          self._noiseReduction = None;

      def getNoiseReduction(self, current = None):
          self.errorIfUnloaded()
          return self._noiseReduction

      def setNoiseReduction(self, _noiseReduction, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.noiseReduction.wrapper is not None:
              if _noiseReduction is not None:
                  _noiseReduction = self._field_info.noiseReduction.wrapper(_noiseReduction)
          self._noiseReduction = _noiseReduction
          pass

      def unloadRed(self, ):
          self._redLoaded = False
          self._red = None;

      def getRed(self, current = None):
          self.errorIfUnloaded()
          return self._red

      def setRed(self, _red, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.red.wrapper is not None:
              if _red is not None:
                  _red = self._field_info.red.wrapper(_red)
          self._red = _red
          pass

      def unloadGreen(self, ):
          self._greenLoaded = False
          self._green = None;

      def getGreen(self, current = None):
          self.errorIfUnloaded()
          return self._green

      def setGreen(self, _green, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.green.wrapper is not None:
              if _green is not None:
                  _green = self._field_info.green.wrapper(_green)
          self._green = _green
          pass

      def unloadBlue(self, ):
          self._blueLoaded = False
          self._blue = None;

      def getBlue(self, current = None):
          self.errorIfUnloaded()
          return self._blue

      def setBlue(self, _blue, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.blue.wrapper is not None:
              if _blue is not None:
                  _blue = self._field_info.blue.wrapper(_blue)
          self._blue = _blue
          pass

      def unloadAlpha(self, ):
          self._alphaLoaded = False
          self._alpha = None;

      def getAlpha(self, current = None):
          self.errorIfUnloaded()
          return self._alpha

      def setAlpha(self, _alpha, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.alpha.wrapper is not None:
              if _alpha is not None:
                  _alpha = self._field_info.alpha.wrapper(_alpha)
          self._alpha = _alpha
          pass

      def unloadLookupTable(self, ):
          self._lookupTableLoaded = False
          self._lookupTable = None;

      def getLookupTable(self, current = None):
          self.errorIfUnloaded()
          return self._lookupTable

      def setLookupTable(self, _lookupTable, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.lookupTable.wrapper is not None:
              if _lookupTable is not None:
                  _lookupTable = self._field_info.lookupTable.wrapper(_lookupTable)
          self._lookupTable = _lookupTable
          pass

      def unloadSpatialDomainEnhancement(self, current = None):
          self._spatialDomainEnhancementLoaded = False
          self._spatialDomainEnhancementSeq = None;

      def _getSpatialDomainEnhancement(self, current = None):
          self.errorIfUnloaded()
          return self._spatialDomainEnhancementSeq

      def _setSpatialDomainEnhancement(self, _spatialDomainEnhancement, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.spatialDomainEnhancementSeq.wrapper is not None:
              if _spatialDomainEnhancement is not None:
                  _spatialDomainEnhancement = self._field_info.spatialDomainEnhancementSeq.wrapper(_spatialDomainEnhancement)
          self._spatialDomainEnhancementSeq = _spatialDomainEnhancement
          self.checkUnloadedProperty(_spatialDomainEnhancement,'spatialDomainEnhancementLoaded')

      def isSpatialDomainEnhancementLoaded(self):
          return self._spatialDomainEnhancementLoaded

      def sizeOfSpatialDomainEnhancement(self, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: return -1
          return len(self._spatialDomainEnhancementSeq)

      def copySpatialDomainEnhancement(self, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          return list(self._spatialDomainEnhancementSeq)

      def iterateSpatialDomainEnhancement(self):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          return iter(self._spatialDomainEnhancementSeq)

      def addCodomainMapContext(self, target, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          self._spatialDomainEnhancementSeq.append( target );
          target.setChannelBinding( self )

      def addAllCodomainMapContextSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          self._spatialDomainEnhancementSeq.extend( targets )
          for target in targets:
              target.setChannelBinding( self )

      def removeCodomainMapContext(self, target, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          self._spatialDomainEnhancementSeq.remove( target )
          target.setChannelBinding( None )

      def removeAllCodomainMapContextSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          for elt in targets:
              elt.setChannelBinding( None )
              self._spatialDomainEnhancementSeq.remove( elt )

      def clearSpatialDomainEnhancement(self, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          for elt in self._spatialDomainEnhancementSeq:
              elt.setChannelBinding( None )
          self._spatialDomainEnhancementSeq = list()

      def reloadSpatialDomainEnhancement(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._spatialDomainEnhancementLoaded:
              raise ode.ClientError("Cannot reload active collection: spatialDomainEnhancementSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copySpatialDomainEnhancement() # May also throw
          for elt in copy:
              elt.setChannelBinding( self )
          self._spatialDomainEnhancementSeq = copy
          toCopy.unloadSpatialDomainEnhancement()
          self._spatialDomainEnhancementLoaded = True

      def getCodomainMapContext(self, index, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          return self._spatialDomainEnhancementSeq[index]

      def setCodomainMapContext(self, index, element, current = None, wrap=False):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          old = self._spatialDomainEnhancementSeq[index]
          if wrap and self._field_info.spatialDomainEnhancementSeq.wrapper is not None:
              if element is not None:
                  element = self._field_info.spatialDomainEnhancementSeq.wrapper(_spatialDomainEnhancement)
          self._spatialDomainEnhancementSeq[index] =  element
          if element is not None and element.isLoaded():
              element.setChannelBinding( self )
          return old

      def getPrimaryCodomainMapContext(self, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          return self._spatialDomainEnhancementSeq[0]

      def setPrimaryCodomainMapContext(self, element, current = None):
          self.errorIfUnloaded()
          if not self._spatialDomainEnhancementLoaded: self.throwNullCollectionException("spatialDomainEnhancementSeq")
          index = self._spatialDomainEnhancementSeq.index(element)
          old = self._spatialDomainEnhancementSeq[0]
          self._spatialDomainEnhancementSeq[index] = old
          self._spatialDomainEnhancementSeq[0] = element
          return old


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

_ode_model.ChannelBindingI = ChannelBindingI
