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
IceImport.load("omero_model_FilterSet_ice")
from omero.rtypes import rlong
from collections import namedtuple
_omero = Ice.openModule("omero")
_omero_model = Ice.openModule("omero.model")
__name__ = "omero.model"
class FilterSetI(_omero_model.FilterSet):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "manufacturer",
          "model",
          "lotNumber",
          "serialNumber",
          "instrument",
          "excitationFilterLink",
          "dichroic",
          "emissionFilterLink",
          "details",
      ])
      _field_info = _field_info_type(
          manufacturer=_field_info_data(wrapper=omero.rtypes.rstring, nullable=True),
          model=_field_info_data(wrapper=omero.rtypes.rstring, nullable=True),
          lotNumber=_field_info_data(wrapper=omero.rtypes.rstring, nullable=True),
          serialNumber=_field_info_data(wrapper=omero.rtypes.rstring, nullable=True),
          instrument=_field_info_data(wrapper=omero.proxy_to_instance, nullable=False),
          excitationFilterLink=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          dichroic=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          emissionFilterLink=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
      )  # end _field_info
      MANUFACTURER =  "ode.model.acquisition.FilterSet_manufacturer"
      MODEL =  "ode.model.acquisition.FilterSet_model"
      LOTNUMBER =  "ode.model.acquisition.FilterSet_lotNumber"
      SERIALNUMBER =  "ode.model.acquisition.FilterSet_serialNumber"
      INSTRUMENT =  "ode.model.acquisition.FilterSet_instrument"
      EXCITATIONFILTERLINK =  "ode.model.acquisition.FilterSet_excitationFilterLink"
      DICHROIC =  "ode.model.acquisition.FilterSet_dichroic"
      EMISSIONFILTERLINK =  "ode.model.acquisition.FilterSet_emissionFilterLink"
      DETAILS =  "ode.model.acquisition.FilterSet_details"
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
              self._excitationFilterLinkSeq = []
              self._excitationFilterLinkLoaded = True;
          else:
              self._excitationFilterLinkSeq = []
              self._excitationFilterLinkLoaded = False;

          if load:
              self._emissionFilterLinkSeq = []
              self._emissionFilterLinkLoaded = True;
          else:
              self._emissionFilterLinkSeq = []
              self._emissionFilterLinkLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(FilterSetI, self).__init__()
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
          self.unloadManufacturer( )
          self.unloadModel( )
          self.unloadLotNumber( )
          self.unloadSerialNumber( )
          self.unloadInstrument( )
          self.unloadExcitationFilterLink( )
          self.unloadDichroic( )
          self.unloadEmissionFilterLink( )
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
            copy = FilterSetI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise omero.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise omero.ClientError("Proxies require an id")
          return FilterSetI( self._id.getValue(), False )

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

      def unloadManufacturer(self, ):
          self._manufacturerLoaded = False
          self._manufacturer = None;

      def getManufacturer(self, current = None):
          self.errorIfUnloaded()
          return self._manufacturer

      def setManufacturer(self, _manufacturer, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.manufacturer.wrapper is not None:
              if _manufacturer is not None:
                  _manufacturer = self._field_info.manufacturer.wrapper(_manufacturer)
          self._manufacturer = _manufacturer
          pass

      def unloadModel(self, ):
          self._modelLoaded = False
          self._model = None;

      def getModel(self, current = None):
          self.errorIfUnloaded()
          return self._model

      def setModel(self, _model, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.model.wrapper is not None:
              if _model is not None:
                  _model = self._field_info.model.wrapper(_model)
          self._model = _model
          pass

      def unloadLotNumber(self, ):
          self._lotNumberLoaded = False
          self._lotNumber = None;

      def getLotNumber(self, current = None):
          self.errorIfUnloaded()
          return self._lotNumber

      def setLotNumber(self, _lotNumber, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.lotNumber.wrapper is not None:
              if _lotNumber is not None:
                  _lotNumber = self._field_info.lotNumber.wrapper(_lotNumber)
          self._lotNumber = _lotNumber
          pass

      def unloadSerialNumber(self, ):
          self._serialNumberLoaded = False
          self._serialNumber = None;

      def getSerialNumber(self, current = None):
          self.errorIfUnloaded()
          return self._serialNumber

      def setSerialNumber(self, _serialNumber, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.serialNumber.wrapper is not None:
              if _serialNumber is not None:
                  _serialNumber = self._field_info.serialNumber.wrapper(_serialNumber)
          self._serialNumber = _serialNumber
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

      def unloadExcitationFilterLink(self, current = None):
          self._excitationFilterLinkLoaded = False
          self._excitationFilterLinkSeq = None;

      def _getExcitationFilterLink(self, current = None):
          self.errorIfUnloaded()
          return self._excitationFilterLinkSeq

      def _setExcitationFilterLink(self, _excitationFilterLink, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.excitationFilterLinkSeq.wrapper is not None:
              if _excitationFilterLink is not None:
                  _excitationFilterLink = self._field_info.excitationFilterLinkSeq.wrapper(_excitationFilterLink)
          self._excitationFilterLinkSeq = _excitationFilterLink
          self.checkUnloadedProperty(_excitationFilterLink,'excitationFilterLinkLoaded')

      def isExcitationFilterLinkLoaded(self):
          return self._excitationFilterLinkLoaded

      def sizeOfExcitationFilterLink(self, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: return -1
          return len(self._excitationFilterLinkSeq)

      def copyExcitationFilterLink(self, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          return list(self._excitationFilterLinkSeq)

      def iterateExcitationFilterLink(self):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          return iter(self._excitationFilterLinkSeq)

      def addFilterSetExcitationFilterLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          self._excitationFilterLinkSeq.append( target );
          target.setParent( self )

      def addAllFilterSetExcitationFilterLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          self._excitationFilterLinkSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeFilterSetExcitationFilterLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          self._excitationFilterLinkSeq.remove( target )
          target.setParent( None )

      def removeAllFilterSetExcitationFilterLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          for elt in targets:
              elt.setParent( None )
              self._excitationFilterLinkSeq.remove( elt )

      def clearExcitationFilterLink(self, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          for elt in self._excitationFilterLinkSeq:
              elt.setParent( None )
          self._excitationFilterLinkSeq = list()

      def reloadExcitationFilterLink(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._excitationFilterLinkLoaded:
              raise omero.ClientError("Cannot reload active collection: excitationFilterLinkSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyExcitationFilterLink() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._excitationFilterLinkSeq = copy
          toCopy.unloadExcitationFilterLink()
          self._excitationFilterLinkLoaded = True

      def getExcitationFilterLinkCountPerOwner(self, current = None):
          return self._excitationFilterLinkCountPerOwner

      def linkExcitationFilter(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          link = _omero_model.FilterSetExcitationFilterLinkI()
          link.link( self, addition );
          self.addFilterSetExcitationFilterLinkToBoth( link, True )
          return link

      def addFilterSetExcitationFilterLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          self._excitationFilterLinkSeq.append( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().addFilterSetExcitationFilterLinkToBoth( link, False )

      def findFilterSetExcitationFilterLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          result = list()
          for link in self._excitationFilterLinkSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkExcitationFilter(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          toRemove = self.findFilterSetExcitationFilterLink(removal)
          for next in toRemove:
              self.removeFilterSetExcitationFilterLinkFromBoth( next, True )

      def removeFilterSetExcitationFilterLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._excitationFilterLinkLoaded: self.throwNullCollectionException("excitationFilterLinkSeq")
          self._excitationFilterLinkSeq.remove( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().removeFilterSetExcitationFilterLinkFromBoth(link, False)

      def linkedExcitationFilterList(self, current = None):
          self.errorIfUnloaded()
          if not self.excitationFilterLinkLoaded: self.throwNullCollectionException("ExcitationFilterLink")
          linked = []
          for link in self._excitationFilterLinkSeq:
              linked.append( link.getChild() )
          return linked

      def unloadDichroic(self, ):
          self._dichroicLoaded = False
          self._dichroic = None;

      def getDichroic(self, current = None):
          self.errorIfUnloaded()
          return self._dichroic

      def setDichroic(self, _dichroic, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.dichroic.wrapper is not None:
              if _dichroic is not None:
                  _dichroic = self._field_info.dichroic.wrapper(_dichroic)
          self._dichroic = _dichroic
          pass

      def unloadEmissionFilterLink(self, current = None):
          self._emissionFilterLinkLoaded = False
          self._emissionFilterLinkSeq = None;

      def _getEmissionFilterLink(self, current = None):
          self.errorIfUnloaded()
          return self._emissionFilterLinkSeq

      def _setEmissionFilterLink(self, _emissionFilterLink, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.emissionFilterLinkSeq.wrapper is not None:
              if _emissionFilterLink is not None:
                  _emissionFilterLink = self._field_info.emissionFilterLinkSeq.wrapper(_emissionFilterLink)
          self._emissionFilterLinkSeq = _emissionFilterLink
          self.checkUnloadedProperty(_emissionFilterLink,'emissionFilterLinkLoaded')

      def isEmissionFilterLinkLoaded(self):
          return self._emissionFilterLinkLoaded

      def sizeOfEmissionFilterLink(self, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: return -1
          return len(self._emissionFilterLinkSeq)

      def copyEmissionFilterLink(self, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          return list(self._emissionFilterLinkSeq)

      def iterateEmissionFilterLink(self):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          return iter(self._emissionFilterLinkSeq)

      def addFilterSetEmissionFilterLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          self._emissionFilterLinkSeq.append( target );
          target.setParent( self )

      def addAllFilterSetEmissionFilterLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          self._emissionFilterLinkSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeFilterSetEmissionFilterLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          self._emissionFilterLinkSeq.remove( target )
          target.setParent( None )

      def removeAllFilterSetEmissionFilterLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          for elt in targets:
              elt.setParent( None )
              self._emissionFilterLinkSeq.remove( elt )

      def clearEmissionFilterLink(self, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          for elt in self._emissionFilterLinkSeq:
              elt.setParent( None )
          self._emissionFilterLinkSeq = list()

      def reloadEmissionFilterLink(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._emissionFilterLinkLoaded:
              raise omero.ClientError("Cannot reload active collection: emissionFilterLinkSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyEmissionFilterLink() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._emissionFilterLinkSeq = copy
          toCopy.unloadEmissionFilterLink()
          self._emissionFilterLinkLoaded = True

      def getEmissionFilterLinkCountPerOwner(self, current = None):
          return self._emissionFilterLinkCountPerOwner

      def linkEmissionFilter(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          link = _omero_model.FilterSetEmissionFilterLinkI()
          link.link( self, addition );
          self.addFilterSetEmissionFilterLinkToBoth( link, True )
          return link

      def addFilterSetEmissionFilterLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          self._emissionFilterLinkSeq.append( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().addFilterSetEmissionFilterLinkToBoth( link, False )

      def findFilterSetEmissionFilterLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          result = list()
          for link in self._emissionFilterLinkSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkEmissionFilter(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          toRemove = self.findFilterSetEmissionFilterLink(removal)
          for next in toRemove:
              self.removeFilterSetEmissionFilterLinkFromBoth( next, True )

      def removeFilterSetEmissionFilterLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._emissionFilterLinkLoaded: self.throwNullCollectionException("emissionFilterLinkSeq")
          self._emissionFilterLinkSeq.remove( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().removeFilterSetEmissionFilterLinkFromBoth(link, False)

      def linkedEmissionFilterList(self, current = None):
          self.errorIfUnloaded()
          if not self.emissionFilterLinkLoaded: self.throwNullCollectionException("EmissionFilterLink")
          linked = []
          for link in self._emissionFilterLinkSeq:
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

_omero_model.FilterSetI = FilterSetI
