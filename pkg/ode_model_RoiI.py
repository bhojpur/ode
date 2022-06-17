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
IceImport.load("ode_model_Roi_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class RoiI(_ode_model.Roi):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "name",
          "shapes",
          "image",
          "source",
          "folderLinks",
          "annotationLinks",
          "description",
          "details",
      ])
      _field_info = _field_info_type(
          name=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          shapes=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          image=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          source=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          folderLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          annotationLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          description=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      NAME =  "ode.model.roi.Roi_name"
      SHAPES =  "ode.model.roi.Roi_shapes"
      IMAGE =  "ode.model.roi.Roi_image"
      SOURCE =  "ode.model.roi.Roi_source"
      FOLDERLINKS =  "ode.model.roi.Roi_folderLinks"
      ANNOTATIONLINKS =  "ode.model.roi.Roi_annotationLinks"
      DESCRIPTION =  "ode.model.roi.Roi_description"
      DETAILS =  "ode.model.roi.Roi_details"
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
              self._shapesSeq = []
              self._shapesLoaded = True;
          else:
              self._shapesSeq = []
              self._shapesLoaded = False;

          if load:
              self._folderLinksSeq = []
              self._folderLinksLoaded = True;
          else:
              self._folderLinksSeq = []
              self._folderLinksLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(RoiI, self).__init__()
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
          self.unloadShapes( )
          self.unloadImage( )
          self.unloadSource( )
          self.unloadFolderLinks( )
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
            copy = RoiI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return RoiI( self._id.getValue(), False )

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

      def unloadShapes(self, current = None):
          self._shapesLoaded = False
          self._shapesSeq = None;

      def _getShapes(self, current = None):
          self.errorIfUnloaded()
          return self._shapesSeq

      def _setShapes(self, _shapes, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.shapesSeq.wrapper is not None:
              if _shapes is not None:
                  _shapes = self._field_info.shapesSeq.wrapper(_shapes)
          self._shapesSeq = _shapes
          self.checkUnloadedProperty(_shapes,'shapesLoaded')

      def isShapesLoaded(self):
          return self._shapesLoaded

      def sizeOfShapes(self, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: return -1
          return len(self._shapesSeq)

      def copyShapes(self, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          return list(self._shapesSeq)

      def iterateShapes(self):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          return iter(self._shapesSeq)

      def addShape(self, target, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          self._shapesSeq.append( target );
          target.setRoi( self )

      def addAllShapeSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          self._shapesSeq.extend( targets )
          for target in targets:
              target.setRoi( self )

      def removeShape(self, target, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          self._shapesSeq.remove( target )
          target.setRoi( None )

      def removeAllShapeSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          for elt in targets:
              elt.setRoi( None )
              self._shapesSeq.remove( elt )

      def clearShapes(self, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          for elt in self._shapesSeq:
              elt.setRoi( None )
          self._shapesSeq = list()

      def reloadShapes(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._shapesLoaded:
              raise ode.ClientError("Cannot reload active collection: shapesSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyShapes() # May also throw
          for elt in copy:
              elt.setRoi( self )
          self._shapesSeq = copy
          toCopy.unloadShapes()
          self._shapesLoaded = True

      def getShape(self, index, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          return self._shapesSeq[index]

      def setShape(self, index, element, current = None, wrap=False):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          old = self._shapesSeq[index]
          if wrap and self._field_info.shapesSeq.wrapper is not None:
              if element is not None:
                  element = self._field_info.shapesSeq.wrapper(_shapes)
          self._shapesSeq[index] =  element
          if element is not None and element.isLoaded():
              element.setRoi( self )
          return old

      def getPrimaryShape(self, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          return self._shapesSeq[0]

      def setPrimaryShape(self, element, current = None):
          self.errorIfUnloaded()
          if not self._shapesLoaded: self.throwNullCollectionException("shapesSeq")
          index = self._shapesSeq.index(element)
          old = self._shapesSeq[0]
          self._shapesSeq[index] = old
          self._shapesSeq[0] = element
          return old

      def unloadImage(self, ):
          self._imageLoaded = False
          self._image = None;

      def getImage(self, current = None):
          self.errorIfUnloaded()
          return self._image

      def setImage(self, _image, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.image.wrapper is not None:
              if _image is not None:
                  _image = self._field_info.image.wrapper(_image)
          self._image = _image
          pass

      def unloadSource(self, ):
          self._sourceLoaded = False
          self._source = None;

      def getSource(self, current = None):
          self.errorIfUnloaded()
          return self._source

      def setSource(self, _source, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.source.wrapper is not None:
              if _source is not None:
                  _source = self._field_info.source.wrapper(_source)
          self._source = _source
          pass

      def unloadFolderLinks(self, current = None):
          self._folderLinksLoaded = False
          self._folderLinksSeq = None;

      def _getFolderLinks(self, current = None):
          self.errorIfUnloaded()
          return self._folderLinksSeq

      def _setFolderLinks(self, _folderLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.folderLinksSeq.wrapper is not None:
              if _folderLinks is not None:
                  _folderLinks = self._field_info.folderLinksSeq.wrapper(_folderLinks)
          self._folderLinksSeq = _folderLinks
          self.checkUnloadedProperty(_folderLinks,'folderLinksLoaded')

      def isFolderLinksLoaded(self):
          return self._folderLinksLoaded

      def sizeOfFolderLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: return -1
          return len(self._folderLinksSeq)

      def copyFolderLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          return list(self._folderLinksSeq)

      def iterateFolderLinks(self):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          return iter(self._folderLinksSeq)

      def addFolderRoiLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.append( target );
          target.setChild( self )

      def addAllFolderRoiLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.extend( targets )
          for target in targets:
              target.setChild( self )

      def removeFolderRoiLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.remove( target )
          target.setChild( None )

      def removeAllFolderRoiLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          for elt in targets:
              elt.setChild( None )
              self._folderLinksSeq.remove( elt )

      def clearFolderLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          for elt in self._folderLinksSeq:
              elt.setChild( None )
          self._folderLinksSeq = list()

      def reloadFolderLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._folderLinksLoaded:
              raise ode.ClientError("Cannot reload active collection: folderLinksSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyFolderLinks() # May also throw
          for elt in copy:
              elt.setChild( self )
          self._folderLinksSeq = copy
          toCopy.unloadFolderLinks()
          self._folderLinksLoaded = True

      def getFolderLinksCountPerOwner(self, current = None):
          return self._folderLinksCountPerOwner

      def linkFolder(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          link = _ode_model.FolderRoiLinkI()
          link.link( addition, self );
          self.addFolderRoiLinkToBoth( link, True )
          return link

      def addFolderRoiLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.append( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().addFolderRoiLinkToBoth( link, False )

      def findFolderRoiLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          result = list()
          for link in self._folderLinksSeq:
              if link.getParent() == removal: result.append(link)
          return result

      def unlinkFolder(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          toRemove = self.findFolderRoiLink(removal)
          for next in toRemove:
              self.removeFolderRoiLinkFromBoth( next, True )

      def removeFolderRoiLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._folderLinksLoaded: self.throwNullCollectionException("folderLinksSeq")
          self._folderLinksSeq.remove( link )
          if bothSides and link.getParent().isLoaded():
              link.getParent().removeFolderRoiLinkFromBoth(link, False)

      def linkedFolderList(self, current = None):
          self.errorIfUnloaded()
          if not self.folderLinksLoaded: self.throwNullCollectionException("FolderLinks")
          linked = []
          for link in self._folderLinksSeq:
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

      def addRoiAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllRoiAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeRoiAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllRoiAnnotationLinkSet(self, targets, current = None):
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
          link = _ode_model.RoiAnnotationLinkI()
          link.link( self, addition );
          self.addRoiAnnotationLinkToBoth( link, True )
          return link

      def addRoiAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findRoiAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findRoiAnnotationLink(removal)
          for next in toRemove:
              self.removeRoiAnnotationLinkFromBoth( next, True )

      def removeRoiAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

_ode_model.RoiI = RoiI
