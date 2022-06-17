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
IceImport.load("ode_model_Session_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class SessionI(_ode_model.Session):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "node",
          "uuid",
          "owner",
          "sudoer",
          "timeToIdle",
          "timeToLive",
          "started",
          "closed",
          "message",
          "defaultEventType",
          "userAgent",
          "userIP",
          "events",
          "annotationLinks",
          "details",
      ])
      _field_info = _field_info_type(
          node=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          uuid=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          owner=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          sudoer=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          timeToIdle=_field_info_data(wrapper=ode.rtypes.rlong, nullable=False),
          timeToLive=_field_info_data(wrapper=ode.rtypes.rlong, nullable=False),
          started=_field_info_data(wrapper=ode.rtypes.rtime, nullable=False),
          closed=_field_info_data(wrapper=ode.rtypes.rtime, nullable=True),
          message=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          defaultEventType=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          userAgent=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          userIP=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          events=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          annotationLinks=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      NODE =  "ode.model.meta.Session_node"
      UUID =  "ode.model.meta.Session_uuid"
      OWNER =  "ode.model.meta.Session_owner"
      SUDOER =  "ode.model.meta.Session_sudoer"
      TIMETOIDLE =  "ode.model.meta.Session_timeToIdle"
      TIMETOLIVE =  "ode.model.meta.Session_timeToLive"
      STARTED =  "ode.model.meta.Session_started"
      CLOSED =  "ode.model.meta.Session_closed"
      MESSAGE =  "ode.model.meta.Session_message"
      DEFAULTEVENTTYPE =  "ode.model.meta.Session_defaultEventType"
      USERAGENT =  "ode.model.meta.Session_userAgent"
      USERIP =  "ode.model.meta.Session_userIP"
      EVENTS =  "ode.model.meta.Session_events"
      ANNOTATIONLINKS =  "ode.model.meta.Session_annotationLinks"
      DETAILS =  "ode.model.meta.Session_details"
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
              self._eventsSeq = []
              self._eventsLoaded = True;
          else:
              self._eventsSeq = []
              self._eventsLoaded = False;

          if load:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = True;
          else:
              self._annotationLinksSeq = []
              self._annotationLinksLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(SessionI, self).__init__()
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
          self.unloadNode( )
          self.unloadUuid( )
          self.unloadOwner( )
          self.unloadSudoer( )
          self.unloadTimeToIdle( )
          self.unloadTimeToLive( )
          self.unloadStarted( )
          self.unloadClosed( )
          self.unloadMessage( )
          self.unloadDefaultEventType( )
          self.unloadUserAgent( )
          self.unloadUserIP( )
          self.unloadEvents( )
          self.unloadAnnotationLinks( )
          self.unloadDetails( )

      def isLoaded(self, current = None):
          return self._loaded
      def unloadCollections(self, current = None):
          self._toggleCollectionsLoaded( False )
      def isGlobal(self, current = None):
          return  True ;
      def isMutable(self, current = None):
          return  True ;
      def isAnnotated(self, current = None):
          return  True ;
      def isLink(self, current = None):
          return  False ;
      def shallowCopy(self, current = None):
            if not self._loaded: return self.proxy()
            copy = SessionI()
            copy._id = self._id;
            copy._version = self._version;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return SessionI( self._id.getValue(), False )

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

      def unloadNode(self, ):
          self._nodeLoaded = False
          self._node = None;

      def getNode(self, current = None):
          self.errorIfUnloaded()
          return self._node

      def setNode(self, _node, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.node.wrapper is not None:
              if _node is not None:
                  _node = self._field_info.node.wrapper(_node)
          self._node = _node
          pass

      def unloadUuid(self, ):
          self._uuidLoaded = False
          self._uuid = None;

      def getUuid(self, current = None):
          self.errorIfUnloaded()
          return self._uuid

      def setUuid(self, _uuid, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.uuid.wrapper is not None:
              if _uuid is not None:
                  _uuid = self._field_info.uuid.wrapper(_uuid)
          self._uuid = _uuid
          pass

      def unloadOwner(self, ):
          self._ownerLoaded = False
          self._owner = None;

      def getOwner(self, current = None):
          self.errorIfUnloaded()
          return self._owner

      def setOwner(self, _owner, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.owner.wrapper is not None:
              if _owner is not None:
                  _owner = self._field_info.owner.wrapper(_owner)
          self._owner = _owner
          pass

      def unloadSudoer(self, ):
          self._sudoerLoaded = False
          self._sudoer = None;

      def getSudoer(self, current = None):
          self.errorIfUnloaded()
          return self._sudoer

      def setSudoer(self, _sudoer, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.sudoer.wrapper is not None:
              if _sudoer is not None:
                  _sudoer = self._field_info.sudoer.wrapper(_sudoer)
          self._sudoer = _sudoer
          pass

      def unloadTimeToIdle(self, ):
          self._timeToIdleLoaded = False
          self._timeToIdle = None;

      def getTimeToIdle(self, current = None):
          self.errorIfUnloaded()
          return self._timeToIdle

      def setTimeToIdle(self, _timeToIdle, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.timeToIdle.wrapper is not None:
              if _timeToIdle is not None:
                  _timeToIdle = self._field_info.timeToIdle.wrapper(_timeToIdle)
          self._timeToIdle = _timeToIdle
          pass

      def unloadTimeToLive(self, ):
          self._timeToLiveLoaded = False
          self._timeToLive = None;

      def getTimeToLive(self, current = None):
          self.errorIfUnloaded()
          return self._timeToLive

      def setTimeToLive(self, _timeToLive, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.timeToLive.wrapper is not None:
              if _timeToLive is not None:
                  _timeToLive = self._field_info.timeToLive.wrapper(_timeToLive)
          self._timeToLive = _timeToLive
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

      def unloadClosed(self, ):
          self._closedLoaded = False
          self._closed = None;

      def getClosed(self, current = None):
          self.errorIfUnloaded()
          return self._closed

      def setClosed(self, _closed, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.closed.wrapper is not None:
              if _closed is not None:
                  _closed = self._field_info.closed.wrapper(_closed)
          self._closed = _closed
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

      def unloadDefaultEventType(self, ):
          self._defaultEventTypeLoaded = False
          self._defaultEventType = None;

      def getDefaultEventType(self, current = None):
          self.errorIfUnloaded()
          return self._defaultEventType

      def setDefaultEventType(self, _defaultEventType, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.defaultEventType.wrapper is not None:
              if _defaultEventType is not None:
                  _defaultEventType = self._field_info.defaultEventType.wrapper(_defaultEventType)
          self._defaultEventType = _defaultEventType
          pass

      def unloadUserAgent(self, ):
          self._userAgentLoaded = False
          self._userAgent = None;

      def getUserAgent(self, current = None):
          self.errorIfUnloaded()
          return self._userAgent

      def setUserAgent(self, _userAgent, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.userAgent.wrapper is not None:
              if _userAgent is not None:
                  _userAgent = self._field_info.userAgent.wrapper(_userAgent)
          self._userAgent = _userAgent
          pass

      def unloadUserIP(self, ):
          self._userIPLoaded = False
          self._userIP = None;

      def getUserIP(self, current = None):
          self.errorIfUnloaded()
          return self._userIP

      def setUserIP(self, _userIP, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.userIP.wrapper is not None:
              if _userIP is not None:
                  _userIP = self._field_info.userIP.wrapper(_userIP)
          self._userIP = _userIP
          pass

      def unloadEvents(self, current = None):
          self._eventsLoaded = False
          self._eventsSeq = None;

      def _getEvents(self, current = None):
          self.errorIfUnloaded()
          return self._eventsSeq

      def _setEvents(self, _events, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.eventsSeq.wrapper is not None:
              if _events is not None:
                  _events = self._field_info.eventsSeq.wrapper(_events)
          self._eventsSeq = _events
          self.checkUnloadedProperty(_events,'eventsLoaded')

      def isEventsLoaded(self):
          return self._eventsLoaded

      def sizeOfEvents(self, current = None):
          self.errorIfUnloaded()
          if not self._eventsLoaded: return -1
          return len(self._eventsSeq)

      def copyEvents(self, current = None):
          self.errorIfUnloaded()
          if not self._eventsLoaded: self.throwNullCollectionException("eventsSeq")
          return list(self._eventsSeq)

      def iterateEvents(self):
          self.errorIfUnloaded()
          if not self._eventsLoaded: self.throwNullCollectionException("eventsSeq")
          return iter(self._eventsSeq)

      def addEvent(self, target, current = None):
          self.errorIfUnloaded()
          if not self._eventsLoaded: self.throwNullCollectionException("eventsSeq")
          self._eventsSeq.append( target );
          target.setSession( self )

      def addAllEventSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._eventsLoaded: self.throwNullCollectionException("eventsSeq")
          self._eventsSeq.extend( targets )
          for target in targets:
              target.setSession( self )

      def removeEvent(self, target, current = None):
          self.errorIfUnloaded()
          if not self._eventsLoaded: self.throwNullCollectionException("eventsSeq")
          self._eventsSeq.remove( target )
          target.setSession( None )

      def removeAllEventSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._eventsLoaded: self.throwNullCollectionException("eventsSeq")
          for elt in targets:
              elt.setSession( None )
              self._eventsSeq.remove( elt )

      def clearEvents(self, current = None):
          self.errorIfUnloaded()
          if not self._eventsLoaded: self.throwNullCollectionException("eventsSeq")
          for elt in self._eventsSeq:
              elt.setSession( None )
          self._eventsSeq = list()

      def reloadEvents(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._eventsLoaded:
              raise ode.ClientError("Cannot reload active collection: eventsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          if toCopy.getDetails().getUpdateEvent().getId().getValue() < self.getDetails().getUpdateEvent().getId().getValue():
             raise ode.ClientError("Argument may not be older than this instance")
          copy = toCopy.copyEvents() # May also throw
          for elt in copy:
              elt.setSession( self )
          self._eventsSeq = copy
          toCopy.unloadEvents()
          self._eventsLoaded = True

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

      def addSessionAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( target );
          target.setParent( self )

      def addAllSessionAnnotationLinkSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.extend( targets )
          for target in targets:
              target.setParent( self )

      def removeSessionAnnotationLink(self, target, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.remove( target )
          target.setParent( None )

      def removeAllSessionAnnotationLinkSet(self, targets, current = None):
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
          link = _ode_model.SessionAnnotationLinkI()
          link.link( self, addition );
          self.addSessionAnnotationLinkToBoth( link, True )
          return link

      def addSessionAnnotationLinkToBoth(self, link, bothSides):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          self._annotationLinksSeq.append( link )

      def findSessionAnnotationLink(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          result = list()
          for link in self._annotationLinksSeq:
              if link.getChild() == removal: result.append(link)
          return result

      def unlinkAnnotation(self, removal, current = None):
          self.errorIfUnloaded()
          if not self._annotationLinksLoaded: self.throwNullCollectionException("annotationLinksSeq")
          toRemove = self.findSessionAnnotationLink(removal)
          for next in toRemove:
              self.removeSessionAnnotationLinkFromBoth( next, True )

      def removeSessionAnnotationLinkFromBoth(self, link, bothSides, current = None):
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

_ode_model.SessionI = SessionI
