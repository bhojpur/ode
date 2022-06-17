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
IceImport.load("ode_model_Screen_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class ScreenI(_ode_model.Screen):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "type",
          "protocolIdentifier",
          "protocolDescription",
          "reagentSetIdentifier",
          "reagentSetDescription",
          "plateLinks",
          "reagents",
          "annotationLinks",
          "name",
          "description",
          "details",
      ])
      _field_info = _field_info_type(
          type=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          protocolIdentifier=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          protocolDescription=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          reagentSetIdentifier=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          reagentSetDescription=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          plateLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          reagents=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          annotationLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          name=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          description=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      TYPE =  "ode.model.screen.Screen_type"
      PROTOCOLIDENTIFIER =  "ode.model.screen.Screen_protocolIdentifier"
      PROTOCOLDESCRIPTION =  "ode.model.screen.Screen_protocolDescription"
      REAGENTSETIDENTIFIER =  "ode.model.screen.Screen_reagentSetIdentifier"
      REAGENTSETDESCRIPTION =  "ode.model.screen.Screen_reagentSetDescription"
      PLATELINKS =  "ode.model.screen.Screen_plateLinks"
      REAGENTS =  "ode.model.screen.Screen_reagents"
      ANNOTATIONLINKS =  "ode.model.screen.Screen_annotationLinks"
      NAME =  "ode.model.screen.Screen_name"
      DESCRIPTION =  "ode.model.screen.Screen_description"
      DETAILS =  "ode.model.screen.Screen_details"
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
              self._plateLinksSeq = []
              self._plateLinksLoaded = True;
          else:
              self._plateLinksSeq = []
              self._plateLinksLoaded = False;

          if load:
              self._reagentsSeq = []
              self._reagentsLoaded = True;
          else:
              self._reagentsSeq = []
              self._reagentsLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(ScreenI, self).__init__()
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
          self.unloadProtocolIdentifier( )
          self.unloadProtocolDescription( )
          self.unloadReagentSetIdentifier( )
          self.unloadReagentSetDescription( )
          self.unloadPlateLinks( )
          self.unloadReagents( )
          self.unloadAnnotationLinks( )
          self.unloadName( )
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
          return  True ;
      def isLink(self, current = None):
          return  False ;
      def shallowCopy(self, current = None):
            if not self._loaded: return self.proxy()
            copy = ScreenI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return ScreenI( self._id.getValue(), False )

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

      def unloadProtocolIdentifier(self, ):
          self._protocolIdentifierLoaded = False
          self._protocolIdentifier = None;

      def getProtocolIdentifier(self, current = None):
          self.errorIfUnloaded()
          return self._protocolIdentifier

      def setProtocolIdentifier(self, _protocolIdentifier, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.protocolIdentifier.wrapper is not None:
              if _protocolIdentifier is not None:
                  _protocolIdentifier = self._field_info.protocolIdentifier.wrapper(_protocolIdentifier)
          self._protocolIdentifier = _protocolIdentifier
          pass

      def unloadProtocolDescription(self, ):
          self._protocolDescriptionLoaded = False
          self._protocolDescription = None;

      def getProtocolDescription(self, current = None):
          self.errorIfUnloaded()
          return self._protocolDescription

      def setProtocolDescription(self, _protocolDescription, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.protocolDescription.wrapper is not None:
              if _protocolDescription is not None:
                  _protocolDescription = self._field_info.protocolDescription.wrapper(_protocolDescription)
          self._protocolDescription = _protocolDescription
          pass

      def unloadReagentSetIdentifier(self, ):
          self._reagentSetIdentifierLoaded = False
          self._reagentSetIdentifier = None;

      def getReagentSetIdentifier(self, current = None):
          self.errorIfUnloaded()
          return self._reagentSetIdentifier

      def setReagentSetIdentifier(self, _reagentSetIdentifier, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.reagentSetIdentifier.wrapper is not None:
              if _reagentSetIdentifier is not None:
                  _reagentSetIdentifier = self._field_info.reagentSetIdentifier.wrapper(_reagentSetIdentifier)
          self._reagentSetIdentifier = _reagentSetIdentifier
          pass

      def unloadReagentSetDescription(self, ):
          self._reagentSetDescriptionLoaded = False
          self._reagentSetDescription = None;

      def getReagentSetDescription(self, current = None):
          self.errorIfUnloaded()
          return self._reagentSetDescription

      def setReagentSetDescription(self, _reagentSetDescription, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.reagentSetDescription.wrapper is not None:
              if _reagentSetDescription is not None:
                  _reagentSetDescription = self._field_info.reagentSetDescription.wrapper(_reagentSetDescription)
          self._reagentSetDescription = _reagentSetDescription
          pass

      def unloadPlateLinks(self, current = None):
          self._plateLinksLoaded = False
          self._plateLinksSeq = None;

      def _getPlateLinks(self, current = None):
          self.errorIfUnloaded()
          return self._plateLinksSeq

      def _setPlateLinks(self, _plateLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.plateLinksSeq.wrapper is not None:
              if _plateLinks is not None:
                  _plateLinks = self._field_info.plateLinksSeq.wrapper(_plateLinks)
          self._plateLinksSeq = _plateLinks
          self.checkUnloadedProperty(_plateLinks,'plateLinksLoaded')

      def isPlateLinksLoaded(self):
          return self._plateLinksLoaded

      def sizeOfPlateLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: return -1
          return len(self._plateLinksSeq)

      def copyPlateLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          return list(self._plateLinksSeq)

      def iteratePlateLinks(self):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          return iter(self._plateLinksSeq)

      def addScreenPlateLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          self._plateLinksSeq.append( target );
          target.setParent( self )

      def addAllScreenPlateLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          self._plateLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeScreenPlateLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          self._plateLinksSeq.remove( target )
          target.setParent( None )

      def removeAllScreenPlateLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          for elt in targets:
              elt.setParent( None )
              self._plateLinksSeq.remove( elt )

      def clearPlateLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          for elt in self._plateLinksSeq:
              elt.setParent( None )
          self._plateLinksSeq = list()

      def reloadPlateLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._plateLinksLoaded:
              raise ode.ClientError("Cannot reload active collection: plateLinksSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyPlateLinks() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._plateLinksSeq = copy
          toCopy.unloadPlateLinks()
          self._plateLinksLoaded = True

      def getPlateLinksCountPerOwner(self, current = None):
          return self._plateLinksCountPerOwner

      def linkPlate(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          link = _ode_model.ScreenPlateLinkI()
          link.link( self, addition );
          self.addScreenPlateLinkToBoth( link, True )
          return link

      def addScreenPlateLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          self._plateLinksSeq.append( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().addScreenPlateLinkToBoth( link, False )

      def findScreenPlateLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          result = list()
          for link in self._plateLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkPlate(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          toRemove = self.findScreenPlateLink(removal)
          for next in toRemove:
              self.removeScreenPlateLinkFromBoth( next, True )

      def removeScreenPlateLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._plateLinksLoaded: self.throwNullCollectionException("plateLinksSeq")
          self._plateLinksSeq.remove( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().removeScreenPlateLinkFromBoth(link, False)

      def linkedPlateList(self, current = None):
          self.errorIfUnloaded()
          if not self.plateLinksLoaded: self.throwNullCollectionException("PlateLinks")
          linked = []
          for link in self._plateLinksSeq:
              linked.append( link.getChild() )
          return linked

      def unloadReagents(self, current = None):
          self._reagentsLoaded = False
          self._reagentsSeq = None;

      def _getReagents(self, current = None):
          self.errorIfUnloaded()
          return self._reagentsSeq

      def _setReagents(self, _reagents, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.reagentsSeq.wrapper is not None:
              if _reagents is not None:
                  _reagents = self._field_info.reagentsSeq.wrapper(_reagents)
          self._reagentsSeq = _reagents
          self.checkUnloadedProperty(_reagents,'reagentsLoaded')

      def isReagentsLoaded(self):
          return self._reagentsLoaded

      def sizeOfReagents(self, current = None):
          self.errorIfUnloaded()
          if not self._reagentsLoaded: return -1
          return len(self._reagentsSeq)

      def copyReagents(self, current = None):
          self.errorIfUnloaded()
          if not self._reagentsLoaded: self.throwNullCollectionException("reagentsSeq")
          return list(self._reagentsSeq)

      def iterateReagents(self):
          self.errorIfUnloaded()
          if not self._reagentsLoaded: self.throwNullCollectionException("reagentsSeq")
          return iter(self._reagentsSeq)

      def addReagent(self, target, current = None):
          self.errorIfUnloaded()
          if not self._reagentsLoaded: self.throwNullCollectionException("reagentsSeq")
          self._reagentsSeq.append( target );
          target.setScreen( self )

      def addAllReagentSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._reagentsLoaded: self.throwNullCollectionException("reagentsSeq")
          self._reagentsSeq.extend( targets )
          for target in targets:
              target.setScreen( self )

      def removeReagent(self, target, current = None):
          self.errorIfUnloaded()
          if not self._reagentsLoaded: self.throwNullCollectionException("reagentsSeq")
          self._reagentsSeq.remove( target )
          target.setScreen( None )

      def removeAllReagentSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._reagentsLoaded: self.throwNullCollectionException("reagentsSeq")
          for elt in targets:
              elt.setScreen( None )
              self._reagentsSeq.remove( elt )

      def clearReagents(self, current = None):
          self.errorIfUnloaded()
          if not self._reagentsLoaded: self.throwNullCollectionException("reagentsSeq")
          for elt in self._reagentsSeq:
              elt.setScreen( None )
          self._reagentsSeq = list()

      def reloadReagents(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._reagentsLoaded:
              raise ode.ClientError("Cannot reload active collection: reagentsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyReagents() # May also throw
          for elt in copy:
              elt.setScreen( self )
          self._reagentsSeq = copy
          toCopy.unloadReagents()
          self._reagentsLoaded = True

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

      def addScreenAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllScreenAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeScreenAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllScreenAnnotationLinkSet(self, targets, current = None):
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
          link = _ode_model.ScreenAnnotationLinkI()
          link.link( self, addition );
          self.addScreenAnnotationLinkToBoth( link, True )
          return link

      def addScreenAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findScreenAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findScreenAnnotationLink(removal)
          for next in toRemove:
              self.removeScreenAnnotationLinkFromBoth( next, True )

      def removeScreenAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

      def unloadName(self, ):
          self._nameLoaded = False
          self._name = None;

      def getName(self, current = None):
          self.errorIfUnloaded()
          return self._name

      def setName(self, _name, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.name.wrapper is not None:
              if _name is not None:
                  _name = self._field_info.name.wrapper(_name)
          self._name = _name
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

_ode_model.ScreenI = ScreenI
