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
IceImport.load("omero_model_Folder_ice")
from omero.rtypes import rlong
from collections import namedtuple
_omero = Ice.openModule("omero")
_omero_model = Ice.openModule("omero.model")
__name__ = "omero.model"
class FolderI(_omero_model.Folder):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "childFolders",
          "parentFolder",
          "imageLinks",
          "roiLinks",
          "annotationLinks",
          "name",
          "description",
          "details",
      ])
      _field_info = _field_info_type(
          childFolders=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          parentFolder=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          imageLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          roiLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          annotationLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          name=_field_info_data(wrapper=omero.rtypes.rstring, nullable=False),
          description=_field_info_data(wrapper=omero.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
      )  # end _field_info
      CHILDFOLDERS =  "ode.model.containers.Folder_childFolders"
      PARENTFOLDER =  "ode.model.containers.Folder_parentFolder"
      IMAGELINKS =  "ode.model.containers.Folder_imageLinks"
      ROILINKS =  "ode.model.containers.Folder_roiLinks"
      ANNOTATIONLINKS =  "ode.model.containers.Folder_annotationLinks"
      NAME =  "ode.model.containers.Folder_name"
      DESCRIPTION =  "ode.model.containers.Folder_description"
      DETAILS =  "ode.model.containers.Folder_details"
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
              self._childFoldersSeq = []
              self._childFoldersLoaded = True;
          else:
              self._childFoldersSeq = []
              self._childFoldersLoaded = False;

          if load:
              self._imageLinksSeq = []
              self._imageLinksLoaded = True;
          else:
              self._imageLinksSeq = []
              self._imageLinksLoaded = False;

          if load:
              self._roiLinksSeq = []
              self._roiLinksLoaded = True;
          else:
              self._roiLinksSeq = []
              self._roiLinksLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(FolderI, self).__init__()
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
          self.unloadChildFolders( )
          self.unloadParentFolder( )
          self.unloadImageLinks( )
          self.unloadRoiLinks( )
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
            copy = FolderI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise omero.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise omero.ClientError("Proxies require an id")
          return FolderI( self._id.getValue(), False )

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

      def unloadChildFolders(self, current = None):
          self._childFoldersLoaded = False
          self._childFoldersSeq = None;

      def _getChildFolders(self, current = None):
          self.errorIfUnloaded()
          return self._childFoldersSeq

      def _setChildFolders(self, _childFolders, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.childFoldersSeq.wrapper is not None:
              if _childFolders is not None:
                  _childFolders = self._field_info.childFoldersSeq.wrapper(_childFolders)
          self._childFoldersSeq = _childFolders
          self.checkUnloadedProperty(_childFolders,'childFoldersLoaded')

      def isChildFoldersLoaded(self):
          return self._childFoldersLoaded

      def sizeOfChildFolders(self, current = None):
          self.errorIfUnloaded()
          if not self._childFoldersLoaded: return -1
          return len(self._childFoldersSeq)

      def copyChildFolders(self, current = None):
          self.errorIfUnloaded()
          if not self._childFoldersLoaded: self.throwNullCollectionException("childFoldersSeq")
          return list(self._childFoldersSeq)

      def iterateChildFolders(self):
          self.errorIfUnloaded()
          if not self._childFoldersLoaded: self.throwNullCollectionException("childFoldersSeq")
          return iter(self._childFoldersSeq)

      def addChildFolders(self, target, current = None):
          self.errorIfUnloaded()
          if not self._childFoldersLoaded: self.throwNullCollectionException("childFoldersSeq")
          self._childFoldersSeq.append( target );
          target.setParentFolder( self )

      def addAllChildFoldersSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._childFoldersLoaded: self.throwNullCollectionException("childFoldersSeq")
          self._childFoldersSeq.extend( targets )
          for target in targets:
              target.setParentFolder( self )

      def removeChildFolders(self, target, current = None):
          self.errorIfUnloaded()
          if not self._childFoldersLoaded: self.throwNullCollectionException("childFoldersSeq")
          self._childFoldersSeq.remove( target )
          target.setParentFolder( None )

      def removeAllChildFoldersSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._childFoldersLoaded: self.throwNullCollectionException("childFoldersSeq")
          for elt in targets:
              elt.setParentFolder( None )
              self._childFoldersSeq.remove( elt )

      def clearChildFolders(self, current = None):
          self.errorIfUnloaded()
          if not self._childFoldersLoaded: self.throwNullCollectionException("childFoldersSeq")
          for elt in self._childFoldersSeq:
              elt.setParentFolder( None )
          self._childFoldersSeq = list()

      def reloadChildFolders(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._childFoldersLoaded:
              raise omero.ClientError("Cannot reload active collection: childFoldersSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyChildFolders() # May also throw
          for elt in copy:
              elt.setParentFolder( self )
          self._childFoldersSeq = copy
          toCopy.unloadChildFolders()
          self._childFoldersLoaded = True

      def unloadParentFolder(self, ):
          self._parentFolderLoaded = False
          self._parentFolder = None;

      def getParentFolder(self, current = None):
          self.errorIfUnloaded()
          return self._parentFolder

      def setParentFolder(self, _parentFolder, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.parentFolder.wrapper is not None:
              if _parentFolder is not None:
                  _parentFolder = self._field_info.parentFolder.wrapper(_parentFolder)
          self._parentFolder = _parentFolder
          pass

      def unloadImageLinks(self, current = None):
          self._imageLinksLoaded = False
          self._imageLinksSeq = None;

      def _getImageLinks(self, current = None):
          self.errorIfUnloaded()
          return self._imageLinksSeq

      def _setImageLinks(self, _imageLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.imageLinksSeq.wrapper is not None:
              if _imageLinks is not None:
                  _imageLinks = self._field_info.imageLinksSeq.wrapper(_imageLinks)
          self._imageLinksSeq = _imageLinks
          self.checkUnloadedProperty(_imageLinks,'imageLinksLoaded')

      def isImageLinksLoaded(self):
          return self._imageLinksLoaded

      def sizeOfImageLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: return -1
          return len(self._imageLinksSeq)

      def copyImageLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          return list(self._imageLinksSeq)

      def iterateImageLinks(self):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          return iter(self._imageLinksSeq)

      def addFolderImageLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          self._imageLinksSeq.append( target );
          target.setParent( self )

      def addAllFolderImageLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          self._imageLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeFolderImageLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          self._imageLinksSeq.remove( target )
          target.setParent( None )

      def removeAllFolderImageLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          for elt in targets:
              elt.setParent( None )
              self._imageLinksSeq.remove( elt )

      def clearImageLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          for elt in self._imageLinksSeq:
              elt.setParent( None )
          self._imageLinksSeq = list()

      def reloadImageLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._imageLinksLoaded:
              raise omero.ClientError("Cannot reload active collection: imageLinksSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyImageLinks() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._imageLinksSeq = copy
          toCopy.unloadImageLinks()
          self._imageLinksLoaded = True

      def getImageLinksCountPerOwner(self, current = None):
          return self._imageLinksCountPerOwner

      def linkImage(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          link = _omero_model.FolderImageLinkI()
          link.link( self, addition );
          self.addFolderImageLinkToBoth( link, True )
          return link

      def addFolderImageLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          self._imageLinksSeq.append( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().addFolderImageLinkToBoth( link, False )

      def findFolderImageLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          result = list()
          for link in self._imageLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkImage(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          toRemove = self.findFolderImageLink(removal)
          for next in toRemove:
              self.removeFolderImageLinkFromBoth( next, True )

      def removeFolderImageLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._imageLinksLoaded: self.throwNullCollectionException("imageLinksSeq")
          self._imageLinksSeq.remove( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().removeFolderImageLinkFromBoth(link, False)

      def linkedImageList(self, current = None):
          self.errorIfUnloaded()
          if not self.imageLinksLoaded: self.throwNullCollectionException("ImageLinks")
          linked = []
          for link in self._imageLinksSeq:
              linked.append( link.getChild() )
          return linked

      def unloadRoiLinks(self, current = None):
          self._roiLinksLoaded = False
          self._roiLinksSeq = None;

      def _getRoiLinks(self, current = None):
          self.errorIfUnloaded()
          return self._roiLinksSeq

      def _setRoiLinks(self, _roiLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.roiLinksSeq.wrapper is not None:
              if _roiLinks is not None:
                  _roiLinks = self._field_info.roiLinksSeq.wrapper(_roiLinks)
          self._roiLinksSeq = _roiLinks
          self.checkUnloadedProperty(_roiLinks,'roiLinksLoaded')

      def isRoiLinksLoaded(self):
          return self._roiLinksLoaded

      def sizeOfRoiLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: return -1
          return len(self._roiLinksSeq)

      def copyRoiLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          return list(self._roiLinksSeq)

      def iterateRoiLinks(self):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          return iter(self._roiLinksSeq)

      def addFolderRoiLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          self._roiLinksSeq.append( target );
          target.setParent( self )

      def addAllFolderRoiLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          self._roiLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeFolderRoiLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          self._roiLinksSeq.remove( target )
          target.setParent( None )

      def removeAllFolderRoiLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          for elt in targets:
              elt.setParent( None )
              self._roiLinksSeq.remove( elt )

      def clearRoiLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          for elt in self._roiLinksSeq:
              elt.setParent( None )
          self._roiLinksSeq = list()

      def reloadRoiLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._roiLinksLoaded:
              raise omero.ClientError("Cannot reload active collection: roiLinksSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyRoiLinks() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._roiLinksSeq = copy
          toCopy.unloadRoiLinks()
          self._roiLinksLoaded = True

      def getRoiLinksCountPerOwner(self, current = None):
          return self._roiLinksCountPerOwner

      def linkRoi(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          link = _omero_model.FolderRoiLinkI()
          link.link( self, addition );
          self.addFolderRoiLinkToBoth( link, True )
          return link

      def addFolderRoiLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          self._roiLinksSeq.append( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().addFolderRoiLinkToBoth( link, False )

      def findFolderRoiLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          result = list()
          for link in self._roiLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkRoi(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          toRemove = self.findFolderRoiLink(removal)
          for next in toRemove:
              self.removeFolderRoiLinkFromBoth( next, True )

      def removeFolderRoiLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._roiLinksLoaded: self.throwNullCollectionException("roiLinksSeq")
          self._roiLinksSeq.remove( link )
          if bothSides and link.getChild().isLoaded():
              link.getChild().removeFolderRoiLinkFromBoth(link, False)

      def linkedRoiList(self, current = None):
          self.errorIfUnloaded()
          if not self.roiLinksLoaded: self.throwNullCollectionException("RoiLinks")
          linked = []
          for link in self._roiLinksSeq:
              linked.append( link.getChild() )
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

      def addFolderAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllFolderAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeFolderAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllFolderAnnotationLinkSet(self, targets, current = None):
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
              raise omero.ClientError("Cannot reload active collection: annotationLinksSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
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
          link = _omero_model.FolderAnnotationLinkI()
          link.link( self, addition );
          self.addFolderAnnotationLinkToBoth( link, True )
          return link

      def addFolderAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findFolderAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findFolderAnnotationLink(removal)
          for next in toRemove:
              self.removeFolderAnnotationLinkFromBoth( next, True )

      def removeFolderAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

_omero_model.FolderI = FolderI
