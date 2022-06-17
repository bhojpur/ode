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
IceImport.load("ode_model_ParseJob_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class ParseJobI(_ode_model.ParseJob):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "params",
          "username",
          "groupname",
          "type",
          "message",
          "status",
          "submitted",
          "scheduledFor",
          "started",
          "finished",
          "originalFileLinks",
          "details",
      ])
      _field_info = _field_info_type(
          params=_field_info_data(wrapper=None, nullable=True),

          username=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          groupname=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          type=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          message=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          status=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          submitted=_field_info_data(wrapper=ode.rtypes.rtime, nullable=False),
          scheduledFor=_field_info_data(wrapper=ode.rtypes.rtime, nullable=False),
          started=_field_info_data(wrapper=ode.rtypes.rtime, nullable=True),
          finished=_field_info_data(wrapper=ode.rtypes.rtime, nullable=True),
          originalFileLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      PARAMS =  "ode.model.jobs.ParseJob_params"
      USERNAME =  "ode.model.jobs.ParseJob_username"
      GROUPNAME =  "ode.model.jobs.ParseJob_groupname"
      TYPE =  "ode.model.jobs.ParseJob_type"
      MESSAGE =  "ode.model.jobs.ParseJob_message"
      STATUS =  "ode.model.jobs.ParseJob_status"
      SUBMITTED =  "ode.model.jobs.ParseJob_submitted"
      SCHEDULEDFOR =  "ode.model.jobs.ParseJob_scheduledFor"
      STARTED =  "ode.model.jobs.ParseJob_started"
      FINISHED =  "ode.model.jobs.ParseJob_finished"
      ORIGINALFILELINKS =  "ode.model.jobs.ParseJob_originalFileLinks"
      DETAILS =  "ode.model.jobs.ParseJob_details"
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
              self._originalFileLinksSeq = []
              self._originalFileLinksLoaded = True;
          else:
              self._originalFileLinksSeq = []
              self._originalFileLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(ParseJobI, self).__init__()
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
          self.unloadParams( )
          self.unloadUsername( )
          self.unloadGroupname( )
          self.unloadType( )
          self.unloadMessage( )
          self.unloadStatus( )
          self.unloadSubmitted( )
          self.unloadScheduledFor( )
          self.unloadStarted( )
          self.unloadFinished( )
          self.unloadOriginalFileLinks( )
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
            copy = ParseJobI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return ParseJobI( self._id.getValue(), False )

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

      def unloadParams(self, ):
          self._paramsLoaded = False
          self._params = None;

      def getParams(self, current = None):
          self.errorIfUnloaded()
          return self._params

      def setParams(self, _params, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.params.wrapper is not None:
              if _params is not None:
                  _params = self._field_info.params.wrapper(_params)
          self._params = _params
          pass

      def unloadUsername(self, ):
          self._usernameLoaded = False
          self._username = None;

      def getUsername(self, current = None):
          self.errorIfUnloaded()
          return self._username

      def setUsername(self, _username, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.username.wrapper is not None:
              if _username is not None:
                  _username = self._field_info.username.wrapper(_username)
          self._username = _username
          pass

      def unloadGroupname(self, ):
          self._groupnameLoaded = False
          self._groupname = None;

      def getGroupname(self, current = None):
          self.errorIfUnloaded()
          return self._groupname

      def setGroupname(self, _groupname, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.groupname.wrapper is not None:
              if _groupname is not None:
                  _groupname = self._field_info.groupname.wrapper(_groupname)
          self._groupname = _groupname
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

      def unloadMessage(self, ):
          self._messageLoaded = False
          self._message = None;

      def getMessage(self, current = None):
          self.errorIfUnloaded()
          return self._message

      def setMessage(self, _message, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.message.wrapper is not None:
              if _message is not None:
                  _message = self._field_info.message.wrapper(_message)
          self._message = _message
          pass

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

      def unloadSubmitted(self, ):
          self._submittedLoaded = False
          self._submitted = None;

      def getSubmitted(self, current = None):
          self.errorIfUnloaded()
          return self._submitted

      def setSubmitted(self, _submitted, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.submitted.wrapper is not None:
              if _submitted is not None:
                  _submitted = self._field_info.submitted.wrapper(_submitted)
          self._submitted = _submitted
          pass

      def unloadScheduledFor(self, ):
          self._scheduledForLoaded = False
          self._scheduledFor = None;

      def getScheduledFor(self, current = None):
          self.errorIfUnloaded()
          return self._scheduledFor

      def setScheduledFor(self, _scheduledFor, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.scheduledFor.wrapper is not None:
              if _scheduledFor is not None:
                  _scheduledFor = self._field_info.scheduledFor.wrapper(_scheduledFor)
          self._scheduledFor = _scheduledFor
          pass

      def unloadStarted(self, ):
          self._startedLoaded = False
          self._started = None;

      def getStarted(self, current = None):
          self.errorIfUnloaded()
          return self._started

      def setStarted(self, _started, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.started.wrapper is not None:
              if _started is not None:
                  _started = self._field_info.started.wrapper(_started)
          self._started = _started
          pass

      def unloadFinished(self, ):
          self._finishedLoaded = False
          self._finished = None;

      def getFinished(self, current = None):
          self.errorIfUnloaded()
          return self._finished

      def setFinished(self, _finished, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.finished.wrapper is not None:
              if _finished is not None:
                  _finished = self._field_info.finished.wrapper(_finished)
          self._finished = _finished
          pass

      def unloadOriginalFileLinks(self, current = None):
          self._originalFileLinksLoaded = False
          self._originalFileLinksSeq = None;

      def _getOriginalFileLinks(self, current = None):
          self.errorIfUnloaded()
          return self._originalFileLinksSeq

      def _setOriginalFileLinks(self, _originalFileLinks, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.originalFileLinksSeq.wrapper is not None:
              if _originalFileLinks is not None:
                  _originalFileLinks = self._field_info.originalFileLinksSeq.wrapper(_originalFileLinks)
          self._originalFileLinksSeq = _originalFileLinks
          self.checkUnloadedProperty(_originalFileLinks,'originalFileLinksLoaded')

      def isOriginalFileLinksLoaded(self):
          return self._originalFileLinksLoaded

      def sizeOfOriginalFileLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: return -1
          return len(self._originalFileLinksSeq)

      def copyOriginalFileLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          return list(self._originalFileLinksSeq)

      def iterateOriginalFileLinks(self):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          return iter(self._originalFileLinksSeq)

      def addJobOriginalFileLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          self._originalFileLinksSeq.append( target );
          target.setParent( self )

      def addAllJobOriginalFileLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          self._originalFileLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeJobOriginalFileLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          self._originalFileLinksSeq.remove( target )
          target.setParent( None )

      def removeAllJobOriginalFileLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          for elt in targets:
              elt.setParent( None )
              self._originalFileLinksSeq.remove( elt )

      def clearOriginalFileLinks(self, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          for elt in self._originalFileLinksSeq:
              elt.setParent( None )
          self._originalFileLinksSeq = list()

      def reloadOriginalFileLinks(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._originalFileLinksLoaded:
              raise ode.ClientError("Cannot reload active collection: originalFileLinksSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyOriginalFileLinks() # May also throw
          for elt in copy:
              elt.setParent( self )
          self._originalFileLinksSeq = copy
          toCopy.unloadOriginalFileLinks()
          self._originalFileLinksLoaded = True

      def getOriginalFileLinksCountPerOwner(self, current = None):
          return self._originalFileLinksCountPerOwner

      def linkOriginalFile(self, addition, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          link = _ode_model.JobOriginalFileLinkI()
          link.link( self, addition );
          self.addJobOriginalFileLinkToBoth( link, True )
          return link

      def addJobOriginalFileLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          self._originalFileLinksSeq.append( link )

      def findJobOriginalFileLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          result = list()
          for link in self._originalFileLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkOriginalFile(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          toRemove = self.findJobOriginalFileLink(removal)
          for next in toRemove:
              self.removeJobOriginalFileLinkFromBoth( next, True )

      def removeJobOriginalFileLinkFromBoth(self, link, bothSides, current = None):
          self.errorIfUnloaded()
          if not self._originalFileLinksLoaded: self.throwNullCollectionException("originalFileLinksSeq")
          self._originalFileLinksSeq.remove( link )

      def linkedOriginalFileList(self, current = None):
          self.errorIfUnloaded()
          if not self.originalFileLinksLoaded: self.throwNullCollectionException("OriginalFileLinks")
          linked = []
          for link in self._originalFileLinksSeq:
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

_ode_model.ParseJobI = ParseJobI
