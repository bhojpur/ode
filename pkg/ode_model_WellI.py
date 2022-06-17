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
IceImport.load("ode_model_Well_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class WellI(_ode_model.Well):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "status",
          "column",
          "row",
          "red",
          "green",
          "blue",
          "alpha",
          "reagentLinks",
          "externalDescription",
          "externalIdentifier",
          "type",
          "wellSamples",
          "plate",
          "annotationLinks",
          "details",
      ])
      _field_info = _field_info_type(
          status=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          column=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          row=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          red=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          green=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          blue=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          alpha=_field_info_data(wrapper=ode.rtypes.rint, nullable=True),
          reagentLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          externalDescription=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          externalIdentifier=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          type=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          wellSamples=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          plate=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          annotationLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      STATUS =  "ode.model.screen.Well_status"
      COLUMN =  "ode.model.screen.Well_column"
      ROW =  "ode.model.screen.Well_row"
      RED =  "ode.model.screen.Well_red"
      GREEN =  "ode.model.screen.Well_green"
      BLUE =  "ode.model.screen.Well_blue"
      ALPHA =  "ode.model.screen.Well_alpha"
      REAGENTLINKS =  "ode.model.screen.Well_reagentLinks"
      EXTERNALDESCRIPTION =  "ode.model.screen.Well_externalDescription"
      EXTERNALIDENTIFIER =  "ode.model.screen.Well_externalIdentifier"
      TYPE =  "ode.model.screen.Well_type"
      WELLSAMPLES =  "ode.model.screen.Well_wellSamples"
      PLATE =  "ode.model.screen.Well_plate"
      ANNOTATIONLINKS =  "ode.model.screen.Well_annotationLinks"
      DETAILS =  "ode.model.screen.Well_details"
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
              self._reagentLinksSeq = []
              self._reagentLinksLoaded = True;
          else:
              self._reagentLinksSeq = []
              self._reagentLinksLoaded = False;

          if load:
              self._wellSamplesSeq = []
              self._wellSamplesLoaded = True;
          else:
              self._wellSamplesSeq = []
              self._wellSamplesLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(WellI, self).__init__()
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
          self.unloadStatus( )
          self.unloadColumn( )
          self.unloadRow( )
          self.unloadRed( )
          self.unloadGreen( )
          self.unloadBlue( )
          self.unloadAlpha( )
          self.unloadReagentLinks( )
          self.unloadExternalDescription( )
          self.unloadExternalIdentifier( )
          self.unloadType( )
          self.unloadWellSamples( )
          self.unloadPlate( )
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
            copy = WellI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return WellI( self._id.getValue(), False )

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

      def unloadStatus(self, ):
          self._statusLoaded = False
          self._status = None;

      def getStatus(self, current = None):
          self.errorIfUnloaded()
          return self._status

      def setStatus(self, _status, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.status.wrapper is not None:
              if _status is not None:
                  _status = self._field_info.status.wrapper(_status)
          self._status = _status
          pass

      def unloadColumn(self, ):
          self._columnLoaded = False
          self._column = None;

      def getColumn(self, current = None):
          self.errorIfUnloaded()
          return self._column

      def setColumn(self, _column, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.column.wrapper is not None:
              if _column is not None:
                  _column = self._field_info.column.wrapper(_column)
          self._column = _column
          pass

      def unloadRow(self, ):
          self._rowLoaded = False
          self._row = None;

      def getRow(self, current = None):
          self.errorIfUnloaded()
          return self._row

      def setRow(self, _row, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.row.wrapper is not None:
              if _row is not None:
                  _row = self._field_info.row.wrapper(_row)
          self._row = _row
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

      def unloadReagentLinks(self, current = None):
          self._reagentLinksLoaded = False
          self._reagentLinksSeq = None;

      def _getReagentLinks(self, current = None):
          self.errorIfUnloaded()
          return self._reagentLinksSeq

      def _setReagentLinks(self, _reagentLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.reagentLinksSeq.wrapper is not None:
              if _reagentLinks is not None:
                  _reagentLinks = self._field_info.reagentLinksSeq.wrapper(_reagentLinks)
          self._reagentLinksSeq = _reagentLinks
          self.checkUnloadedProperty(_reagentLinks,'reagentLinksLoaded')

      def isReagentLinksLoaded(self):
          return self._reagentLinksLoaded

      def sizeOfReagentLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: return -1
          return len(self._reagentLinksSeq)

      def copyReagentLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          return list(self._reagentLinksSeq)

      def iterateReagentLinks(self):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          return iter(self._reagentLinksSeq)

      def addWellReagentLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          self._reagentLinksSeq.append( target );
          target.setParent( self )

      def addAllWellReagentLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          self._reagentLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeWellReagentLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          self._reagentLinksSeq.remove( target )
          target.setParent( None )

      def removeAllWellReagentLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          for elt in targets:
              elt.setParent( None )
              self._reagentLinksSeq.remove( elt )

      def clearReagentLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          for elt in self._reagentLinksSeq:
              elt.setParent( None )
          self._reagentLinksSeq = list()

      def reloadReagentLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._reagentLinksLoaded:
              raise ode.ClientError("Cannot reload active collection: reagentLinksSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyReagentLinks() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._reagentLinksSeq = copy
          toCopy.unloadReagentLinks()
          self._reagentLinksLoaded = True

      def getReagentLinksCountPerOwner(self, current = None):
          return self._reagentLinksCountPerOwner

      def linkReagent(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          link = _ode_model.WellReagentLinkI()
          link.link( self, addition );
          self.addWellReagentLinkToBoth( link, True )
          return link

      def addWellReagentLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          self._reagentLinksSeq.append( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().addWellReagentLinkToBoth( link, False )

      def findWellReagentLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          result = list()
          for link in self._reagentLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkReagent(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          toRemove = self.findWellReagentLink(removal)
          for next in toRemove:
              self.removeWellReagentLinkFromBoth( next, True )

      def removeWellReagentLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._reagentLinksLoaded: self.throwNullCollectionException("reagentLinksSeq")
          self._reagentLinksSeq.remove( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().removeWellReagentLinkFromBoth(link, False)

      def linkedReagentList(self, current = None):
          self.errorIfUnloaded()
          if not self.reagentLinksLoaded: self.throwNullCollectionException("ReagentLinks")
          linked = []
          for link in self._reagentLinksSeq:
              linked.append( link.getChild() )
          return linked

      def unloadExternalDescription(self, ):
          self._externalDescriptionLoaded = False
          self._externalDescription = None;

      def getExternalDescription(self, current = None):
          self.errorIfUnloaded()
          return self._externalDescription

      def setExternalDescription(self, _externalDescription, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.externalDescription.wrapper is not None:
              if _externalDescription is not None:
                  _externalDescription = self._field_info.externalDescription.wrapper(_externalDescription)
          self._externalDescription = _externalDescription
          pass

      def unloadExternalIdentifier(self, ):
          self._externalIdentifierLoaded = False
          self._externalIdentifier = None;

      def getExternalIdentifier(self, current = None):
          self.errorIfUnloaded()
          return self._externalIdentifier

      def setExternalIdentifier(self, _externalIdentifier, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.externalIdentifier.wrapper is not None:
              if _externalIdentifier is not None:
                  _externalIdentifier = self._field_info.externalIdentifier.wrapper(_externalIdentifier)
          self._externalIdentifier = _externalIdentifier
          pass

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

      def unloadWellSamples(self, current = None):
          self._wellSamplesLoaded = False
          self._wellSamplesSeq = None;

      def _getWellSamples(self, current = None):
          self.errorIfUnloaded()
          return self._wellSamplesSeq

      def _setWellSamples(self, _wellSamples, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.wellSamplesSeq.wrapper is not None:
              if _wellSamples is not None:
                  _wellSamples = self._field_info.wellSamplesSeq.wrapper(_wellSamples)
          self._wellSamplesSeq = _wellSamples
          self.checkUnloadedProperty(_wellSamples,'wellSamplesLoaded')

      def isWellSamplesLoaded(self):
          return self._wellSamplesLoaded

      def sizeOfWellSamples(self, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: return -1
          return len(self._wellSamplesSeq)

      def copyWellSamples(self, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          return list(self._wellSamplesSeq)

      def iterateWellSamples(self):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          return iter(self._wellSamplesSeq)

      def addWellSample(self, target, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          self._wellSamplesSeq.append( target );
          target.setWell( self )

      def addAllWellSampleSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          self._wellSamplesSeq.extend( targets )
          for target in targets:
              target.setWell( self )

      def removeWellSample(self, target, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          self._wellSamplesSeq.remove( target )
          target.setWell( None )

      def removeAllWellSampleSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          for elt in targets:
              elt.setWell( None )
              self._wellSamplesSeq.remove( elt )

      def clearWellSamples(self, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          for elt in self._wellSamplesSeq:
              elt.setWell( None )
          self._wellSamplesSeq = list()

      def reloadWellSamples(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._wellSamplesLoaded:
              raise ode.ClientError("Cannot reload active collection: wellSamplesSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyWellSamples() # May also throw
          for elt in copy:
              elt.setWell( self )
          self._wellSamplesSeq = copy
          toCopy.unloadWellSamples()
          self._wellSamplesLoaded = True

      def getWellSample(self, index, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          return self._wellSamplesSeq[index]

      def setWellSample(self, index, element, current = None, wrap=False):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          old = self._wellSamplesSeq[index]
          if wrap and self._field_info.wellSamplesSeq.wrapper is not None:
              if element is not None:
                  element = self._field_info.wellSamplesSeq.wrapper(_wellSamples)
          self._wellSamplesSeq[index] =  element
          if element is not None and element.isLoaded():
              element.setWell( self )
          return old

      def getPrimaryWellSample(self, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          return self._wellSamplesSeq[0]

      def setPrimaryWellSample(self, element, current = None):
          self.errorIfUnloaded()
          if not self._wellSamplesLoaded: self.throwNullCollectionException("wellSamplesSeq")
          index = self._wellSamplesSeq.index(element)
          old = self._wellSamplesSeq[0]
          self._wellSamplesSeq[index] = old
          self._wellSamplesSeq[0] = element
          return old

      def unloadPlate(self, ):
          self._plateLoaded = False
          self._plate = None;

      def getPlate(self, current = None):
          self.errorIfUnloaded()
          return self._plate

      def setPlate(self, _plate, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.plate.wrapper is not None:
              if _plate is not None:
                  _plate = self._field_info.plate.wrapper(_plate)
          self._plate = _plate
          pass

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

      def addWellAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllWellAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeWellAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllWellAnnotationLinkSet(self, targets, current = None):
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
              raise ode.ClientError("Cannot reload active collection: annotationLinksSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
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
          link = _ode_model.WellAnnotationLinkI()
          link.link( self, addition );
          self.addWellAnnotationLinkToBoth( link, True )
          return link

      def addWellAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findWellAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findWellAnnotationLink(removal)
          for next in toRemove:
              self.removeWellAnnotationLinkFromBoth( next, True )

      def removeWellAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

_ode_model.WellI = WellI
