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
IceImport.load("omero_model_Fileset_ice")
from omero.rtypes import rlong
from collections import namedtuple
_omero = Ice.openModule("omero")
_omero_model = Ice.openModule("omero.model")
__name__ = "omero.model"
class FilesetI(_omero_model.Fileset):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "usedFiles",
          "images",
          "jobLinks",
          "templatePrefix",
          "annotationLinks",
          "details",
      ])
      _field_info = _field_info_type(
          usedFiles=_field_info_data(wrapper=omero.proxy_to_instance, nullable=False),
          images=_field_info_data(wrapper=omero.proxy_to_instance, nullable=False),
          jobLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=False),
          templatePrefix=_field_info_data(wrapper=omero.rtypes.rstring, nullable=False),
          annotationLinks=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=omero.proxy_to_instance, nullable=True),
      )  # end _field_info
      USEDFILES =  "ode.model.fs.Fileset_usedFiles"
      IMAGES =  "ode.model.fs.Fileset_images"
      JOBLINKS =  "ode.model.fs.Fileset_jobLinks"
      TEMPLATEPREFIX =  "ode.model.fs.Fileset_templatePrefix"
      ANNOTATIONLINKS =  "ode.model.fs.Fileset_annotationLinks"
      DETAILS =  "ode.model.fs.Fileset_details"
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
              self._usedFilesSeq = []
              self._usedFilesLoaded = True;
          else:
              self._usedFilesSeq = []
              self._usedFilesLoaded = False;

          if load:
              self._imagesSeq = []
              self._imagesLoaded = True;
          else:
              self._imagesSeq = []
              self._imagesLoaded = False;

          if load:
              self._jobLinksSeq = []
              self._jobLinksLoaded = True;
          else:
              self._jobLinksSeq = []
              self._jobLinksLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(FilesetI, self).__init__()
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
          self.unloadUsedFiles( )
          self.unloadImages( )
          self.unloadJobLinks( )
          self.unloadTemplatePrefix( )
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
            copy = FilesetI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise omero.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise omero.ClientError("Proxies require an id")
          return FilesetI( self._id.getValue(), False )

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

      def unloadUsedFiles(self, current = None):
          self._usedFilesLoaded = False
          self._usedFilesSeq = None;

      def _getUsedFiles(self, current = None):
          self.errorIfUnloaded()
          return self._usedFilesSeq

      def _setUsedFiles(self, _usedFiles, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.usedFilesSeq.wrapper is not None:
              if _usedFiles is not None:
                  _usedFiles = self._field_info.usedFilesSeq.wrapper(_usedFiles)
          self._usedFilesSeq = _usedFiles
          self.checkUnloadedProperty(_usedFiles,'usedFilesLoaded')

      def isUsedFilesLoaded(self):
          return self._usedFilesLoaded

      def sizeOfUsedFiles(self, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: return -1
          return len(self._usedFilesSeq)

      def copyUsedFiles(self, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          return list(self._usedFilesSeq)

      def iterateUsedFiles(self):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          return iter(self._usedFilesSeq)

      def addFilesetEntry(self, target, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          self._usedFilesSeq.append( target );
          target.setFileset( self )

      def addAllFilesetEntrySet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          self._usedFilesSeq.extend( targets )
          for target in targets:
              target.setFileset( self )

      def removeFilesetEntry(self, target, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          self._usedFilesSeq.remove( target )
          target.setFileset( None )

      def removeAllFilesetEntrySet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          for elt in targets:
              elt.setFileset( None )
              self._usedFilesSeq.remove( elt )

      def clearUsedFiles(self, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          for elt in self._usedFilesSeq:
              elt.setFileset( None )
          self._usedFilesSeq = list()

      def reloadUsedFiles(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._usedFilesLoaded:
              raise omero.ClientError("Cannot reload active collection: usedFilesSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyUsedFiles() # May also throw
          for elt in copy:
              elt.setFileset( self )
          self._usedFilesSeq = copy
          toCopy.unloadUsedFiles()
          self._usedFilesLoaded = True

      def getFilesetEntry(self, index, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          return self._usedFilesSeq[index]

      def setFilesetEntry(self, index, element, current = None, wrap=False):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          old = self._usedFilesSeq[index]
          if wrap and self._field_info.usedFilesSeq.wrapper is not None:
              if element is not None:
                  element = self._field_info.usedFilesSeq.wrapper(_usedFiles)
          self._usedFilesSeq[index] =  element
          if element is not None and element.isLoaded():
              element.setFileset( self )
          return old

      def getPrimaryFilesetEntry(self, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          return self._usedFilesSeq[0]

      def setPrimaryFilesetEntry(self, element, current = None):
          self.errorIfUnloaded()
          if not self._usedFilesLoaded: self.throwNullCollectionException("usedFilesSeq")
          index = self._usedFilesSeq.index(element)
          old = self._usedFilesSeq[0]
          self._usedFilesSeq[index] = old
          self._usedFilesSeq[0] = element
          return old

      def unloadImages(self, current = None):
          self._imagesLoaded = False
          self._imagesSeq = None;

      def _getImages(self, current = None):
          self.errorIfUnloaded()
          return self._imagesSeq

      def _setImages(self, _images, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.imagesSeq.wrapper is not None:
              if _images is not None:
                  _images = self._field_info.imagesSeq.wrapper(_images)
          self._imagesSeq = _images
          self.checkUnloadedProperty(_images,'imagesLoaded')

      def isImagesLoaded(self):
          return self._imagesLoaded

      def sizeOfImages(self, current = None):
          self.errorIfUnloaded()
          if not self._imagesLoaded: return -1
          return len(self._imagesSeq)

      def copyImages(self, current = None):
          self.errorIfUnloaded()
          if not self._imagesLoaded: self.throwNullCollectionException("imagesSeq")
          return list(self._imagesSeq)

      def iterateImages(self):
          self.errorIfUnloaded()
          if not self._imagesLoaded: self.throwNullCollectionException("imagesSeq")
          return iter(self._imagesSeq)

      def addImage(self, target, current = None):
          self.errorIfUnloaded()
          if not self._imagesLoaded: self.throwNullCollectionException("imagesSeq")
          self._imagesSeq.append( target );
          target.setFileset( self )

      def addAllImageSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._imagesLoaded: self.throwNullCollectionException("imagesSeq")
          self._imagesSeq.extend( targets )
          for target in targets:
              target.setFileset( self )

      def removeImage(self, target, current = None):
          self.errorIfUnloaded()
          if not self._imagesLoaded: self.throwNullCollectionException("imagesSeq")
          self._imagesSeq.remove( target )
          target.setFileset( None )

      def removeAllImageSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._imagesLoaded: self.throwNullCollectionException("imagesSeq")
          for elt in targets:
              elt.setFileset( None )
              self._imagesSeq.remove( elt )

      def clearImages(self, current = None):
          self.errorIfUnloaded()
          if not self._imagesLoaded: self.throwNullCollectionException("imagesSeq")
          for elt in self._imagesSeq:
              elt.setFileset( None )
          self._imagesSeq = list()

      def reloadImages(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._imagesLoaded:
              raise omero.ClientError("Cannot reload active collection: imagesSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyImages() # May also throw
          for elt in copy:
              elt.setFileset( self )
          self._imagesSeq = copy
          toCopy.unloadImages()
          self._imagesLoaded = True

      def unloadJobLinks(self, current = None):
          self._jobLinksLoaded = False
          self._jobLinksSeq = None;

      def _getJobLinks(self, current = None):
          self.errorIfUnloaded()
          return self._jobLinksSeq

      def _setJobLinks(self, _jobLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.jobLinksSeq.wrapper is not None:
              if _jobLinks is not None:
                  _jobLinks = self._field_info.jobLinksSeq.wrapper(_jobLinks)
          self._jobLinksSeq = _jobLinks
          self.checkUnloadedProperty(_jobLinks,'jobLinksLoaded')

      def isJobLinksLoaded(self):
          return self._jobLinksLoaded

      def sizeOfJobLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: return -1
          return len(self._jobLinksSeq)

      def copyJobLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          return list(self._jobLinksSeq)

      def iterateJobLinks(self):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          return iter(self._jobLinksSeq)

      def addFilesetJobLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          self._jobLinksSeq.append( target );
          target.setParent( self )

      def addAllFilesetJobLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          self._jobLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeFilesetJobLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          self._jobLinksSeq.remove( target )
          target.setParent( None )

      def removeAllFilesetJobLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          for elt in targets:
              elt.setParent( None )
              self._jobLinksSeq.remove( elt )

      def clearJobLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          for elt in self._jobLinksSeq:
              elt.setParent( None )
          self._jobLinksSeq = list()

      def reloadJobLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._jobLinksLoaded:
              raise omero.ClientError("Cannot reload active collection: jobLinksSeq")
          if not toCopy:
              raise omero.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise omero.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise omero.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyJobLinks() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._jobLinksSeq = copy
          toCopy.unloadJobLinks()
          self._jobLinksLoaded = True

      def getFilesetJobLink(self, index, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          return self._jobLinksSeq[index]

      def setFilesetJobLink(self, index, element, current = None, wrap=False):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          old = self._jobLinksSeq[index]
          if wrap and self._field_info.jobLinksSeq.wrapper is not None:
              if element is not None:
                  element = self._field_info.jobLinksSeq.wrapper(_jobLinks)
          self._jobLinksSeq[index] =  element
          if element is not None and element.isLoaded():
              element.setParent( self )
          return old

      def getPrimaryFilesetJobLink(self, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          return self._jobLinksSeq[0]

      def setPrimaryFilesetJobLink(self, element, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          index = self._jobLinksSeq.index(element)
          old = self._jobLinksSeq[0]
          self._jobLinksSeq[index] = old
          self._jobLinksSeq[0] = element
          return old

      def getJobLinksCountPerOwner(self, current = None):
          return self._jobLinksCountPerOwner

      def linkJob(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          link = _omero_model.FilesetJobLinkI()
          link.link( self, addition );
          self.addFilesetJobLinkToBoth( link, True )
          return link

      def addFilesetJobLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          self._jobLinksSeq.append( link )

      def findFilesetJobLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          result = list()
          for link in self._jobLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkJob(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          toRemove = self.findFilesetJobLink(removal)
          for next in toRemove:
              self.removeFilesetJobLinkFromBoth( next, True )

      def removeFilesetJobLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._jobLinksLoaded: self.throwNullCollectionException("jobLinksSeq")
          self._jobLinksSeq.remove( link )

      def linkedJobList(self, current = None):
          self.errorIfUnloaded()
          if not self.jobLinksLoaded: self.throwNullCollectionException("JobLinks")
          linked = []
          for link in self._jobLinksSeq:
              linked.append( link.getChild() )
          return linked

      def unloadTemplatePrefix(self, ):
          self._templatePrefixLoaded = False
          self._templatePrefix = None;

      def getTemplatePrefix(self, current = None):
          self.errorIfUnloaded()
          return self._templatePrefix

      def setTemplatePrefix(self, _templatePrefix, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.templatePrefix.wrapper is not None:
              if _templatePrefix is not None:
                  _templatePrefix = self._field_info.templatePrefix.wrapper(_templatePrefix)
          self._templatePrefix = _templatePrefix
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

      def addFilesetAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllFilesetAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeFilesetAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllFilesetAnnotationLinkSet(self, targets, current = None):
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
          link = _omero_model.FilesetAnnotationLinkI()
          link.link( self, addition );
          self.addFilesetAnnotationLinkToBoth( link, True )
          return link

      def addFilesetAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findFilesetAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findFilesetAnnotationLink(removal)
          for next in toRemove:
              self.removeFilesetAnnotationLinkFromBoth( next, True )

      def removeFilesetAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

_omero_model.FilesetI = FilesetI
