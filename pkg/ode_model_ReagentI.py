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
IceImport.load("ode_model_Reagent_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class ReagentI(_ode_model.Reagent):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "name",
          "reagentIdentifier",
          "screen",
          "wellLinks",
          "annotationLinks",
          "description",
          "details",
      ])
      _field_info = _field_info_type(
          name=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          reagentIdentifier=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          screen=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          wellLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          annotationLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          description=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      NAME =  "ode.model.screen.Reagent_name"
      REAGENTIDENTIFIER =  "ode.model.screen.Reagent_reagentIdentifier"
      SCREEN =  "ode.model.screen.Reagent_screen"
      WELLLINKS =  "ode.model.screen.Reagent_wellLinks"
      ANNOTATIONLINKS =  "ode.model.screen.Reagent_annotationLinks"
      DESCRIPTION =  "ode.model.screen.Reagent_description"
      DETAILS =  "ode.model.screen.Reagent_details"
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
              self._wellLinksSeq = []
              self._wellLinksLoaded = True;
          else:
              self._wellLinksSeq = []
              self._wellLinksLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(ReagentI, self).__init__()
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
          self.unloadName( )
          self.unloadReagentIdentifier( )
          self.unloadScreen( )
          self.unloadWellLinks( )
          self.unloadAnnotationLinks( )
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
            copy = ReagentI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return ReagentI( self._id.getValue(), False )

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

      def unloadReagentIdentifier(self, ):
          self._reagentIdentifierLoaded = False
          self._reagentIdentifier = None;

      def getReagentIdentifier(self, current = None):
          self.errorIfUnloaded()
          return self._reagentIdentifier

      def setReagentIdentifier(self, _reagentIdentifier, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.reagentIdentifier.wrapper is not None:
              if _reagentIdentifier is not None:
                  _reagentIdentifier = self._field_info.reagentIdentifier.wrapper(_reagentIdentifier)
          self._reagentIdentifier = _reagentIdentifier
          pass

      def unloadScreen(self, ):
          self._screenLoaded = False
          self._screen = None;

      def getScreen(self, current = None):
          self.errorIfUnloaded()
          return self._screen

      def setScreen(self, _screen, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.screen.wrapper is not None:
              if _screen is not None:
                  _screen = self._field_info.screen.wrapper(_screen)
          self._screen = _screen
          pass

      def unloadWellLinks(self, current = None):
          self._wellLinksLoaded = False
          self._wellLinksSeq = None;

      def _getWellLinks(self, current = None):
          self.errorIfUnloaded()
          return self._wellLinksSeq

      def _setWellLinks(self, _wellLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.wellLinksSeq.wrapper is not None:
              if _wellLinks is not None:
                  _wellLinks = self._field_info.wellLinksSeq.wrapper(_wellLinks)
          self._wellLinksSeq = _wellLinks
          self.checkUnloadedProperty(_wellLinks,'wellLinksLoaded')

      def isWellLinksLoaded(self):
          return self._wellLinksLoaded

      def sizeOfWellLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: return -1
          return len(self._wellLinksSeq)

      def copyWellLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          return list(self._wellLinksSeq)

      def iterateWellLinks(self):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          return iter(self._wellLinksSeq)

      def addWellReagentLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          self._wellLinksSeq.append( target );
          target.setChild( self )

      def addAllWellReagentLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          self._wellLinksSeq.extend( targets )
          for target in targets:
              target.setChild( self )

      def removeWellReagentLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          self._wellLinksSeq.remove( target )
          target.setChild( None )

      def removeAllWellReagentLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          for elt in targets:
              elt.setChild( None )
              self._wellLinksSeq.remove( elt )

      def clearWellLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          for elt in self._wellLinksSeq:
              elt.setChild( None )
          self._wellLinksSeq = list()

      def reloadWellLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._wellLinksLoaded:
              raise ode.ClientError("Cannot reload active collection: wellLinksSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyWellLinks() # May also throw
          for elt in copy:
              elt.setChild( self )
          self._wellLinksSeq = copy
          toCopy.unloadWellLinks()
          self._wellLinksLoaded = True

      def getWellLinksCountPerOwner(self, current = None):
          return self._wellLinksCountPerOwner

      def linkWell(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          link = _ode_model.WellReagentLinkI()
          link.link( addition, self );
          self.addWellReagentLinkToBoth( link, True )
          return link

      def addWellReagentLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          self._wellLinksSeq.append( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().addWellReagentLinkToBoth( link, False )

      def findWellReagentLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          result = list()
          for link in self._wellLinksSeq:
              if link.getParent() == removal: result.append(link)
          return result

      def unlinkWell(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          toRemove = self.findWellReagentLink(removal)
          for next in toRemove:
              self.removeWellReagentLinkFromBoth( next, True )

      def removeWellReagentLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._wellLinksLoaded: self.throwNullCollectionException("wellLinksSeq")
          self._wellLinksSeq.remove( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().removeWellReagentLinkFromBoth(link, False)

      def linkedWellList(self, current = None):
          self.errorIfUnloaded()
          if not self.wellLinksLoaded: self.throwNullCollectionException("WellLinks")
          linked = []
          for link in self._wellLinksSeq:
              linked.append( link.getParent() )
          return linked

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

      def addReagentAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllReagentAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeReagentAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllReagentAnnotationLinkSet(self, targets, current = None):
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
          link = _ode_model.ReagentAnnotationLinkI()
          link.link( self, addition );
          self.addReagentAnnotationLinkToBoth( link, True )
          return link

      def addReagentAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findReagentAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findReagentAnnotationLink(removal)
          for next in toRemove:
              self.removeReagentAnnotationLinkFromBoth( next, True )

      def removeReagentAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

_ode_model.ReagentI = ReagentI
