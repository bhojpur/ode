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
IceImport.load("ode_model_Event_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class EventI(_ode_model.Event):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "status",
          "time",
          "experimenter",
          "experimenterGroup",
          "type",
          "containingEvent",
          "logs",
          "session",
          "details",
      ])
      _field_info = _field_info_type(
          status=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          time=_field_info_data(wrapper=ode.rtypes.rtime, nullable=False),
          experimenter=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          experimenterGroup=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          type=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          containingEvent=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
          logs=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          session=_field_info_data(wrapper=ode.proxy_to_instance, nullable=False),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      STATUS =  "ode.model.meta.Event_status"
      TIME =  "ode.model.meta.Event_time"
      EXPERIMENTER =  "ode.model.meta.Event_experimenter"
      EXPERIMENTERGROUP =  "ode.model.meta.Event_experimenterGroup"
      TYPE =  "ode.model.meta.Event_type"
      CONTAININGEVENT =  "ode.model.meta.Event_containingEvent"
      LOGS =  "ode.model.meta.Event_logs"
      SESSION =  "ode.model.meta.Event_session"
      DETAILS =  "ode.model.meta.Event_details"
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
              self._logsSeq = []
              self._logsLoaded = True;
          else:
              self._logsSeq = []
              self._logsLoaded = False;

          pass

      def __init__(self, id=None, loaded=None):
          super(EventI, self).__init__()
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
          self.unloadTime( )
          self.unloadExperimenter( )
          self.unloadExperimenterGroup( )
          self.unloadType( )
          self.unloadContainingEvent( )
          self.unloadLogs( )
          self.unloadSession( )
          self.unloadDetails( )

      def isLoaded(self, current = None):
          return self._loaded
      def unloadCollections(self, current = None):
          self._toggleCollectionsLoaded( False )
      def isGlobal(self, current = None):
          return  True ;
      def isMutable(self, current = None):
          return  False ;
      def isAnnotated(self, current = None):
          return  False ;
      def isLink(self, current = None):
          return  False ;
      def shallowCopy(self, current = None):
            if not self._loaded: return self.proxy()
            copy = EventI()
            copy._id = self._id;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return EventI( self._id.getValue(), False )

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

      def unloadTime(self, ):
          self._timeLoaded = False
          self._time = None;

      def getTime(self, current = None):
          self.errorIfUnloaded()
          return self._time

      def setTime(self, _time, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.time.wrapper is not None:
              if _time is not None:
                  _time = self._field_info.time.wrapper(_time)
          self._time = _time
          pass

      def unloadExperimenter(self, ):
          self._experimenterLoaded = False
          self._experimenter = None;

      def getExperimenter(self, current = None):
          self.errorIfUnloaded()
          return self._experimenter

      def setExperimenter(self, _experimenter, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.experimenter.wrapper is not None:
              if _experimenter is not None:
                  _experimenter = self._field_info.experimenter.wrapper(_experimenter)
          self._experimenter = _experimenter
          pass

      def unloadExperimenterGroup(self, ):
          self._experimenterGroupLoaded = False
          self._experimenterGroup = None;

      def getExperimenterGroup(self, current = None):
          self.errorIfUnloaded()
          return self._experimenterGroup

      def setExperimenterGroup(self, _experimenterGroup, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.experimenterGroup.wrapper is not None:
              if _experimenterGroup is not None:
                  _experimenterGroup = self._field_info.experimenterGroup.wrapper(_experimenterGroup)
          self._experimenterGroup = _experimenterGroup
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

      def unloadContainingEvent(self, ):
          self._containingEventLoaded = False
          self._containingEvent = None;

      def getContainingEvent(self, current = None):
          self.errorIfUnloaded()
          return self._containingEvent

      def setContainingEvent(self, _containingEvent, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.containingEvent.wrapper is not None:
              if _containingEvent is not None:
                  _containingEvent = self._field_info.containingEvent.wrapper(_containingEvent)
          self._containingEvent = _containingEvent
          pass

      def unloadLogs(self, current = None):
          self._logsLoaded = False
          self._logsSeq = None;

      def _getLogs(self, current = None):
          self.errorIfUnloaded()
          return self._logsSeq

      def _setLogs(self, _logs, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.logsSeq.wrapper is not None:
              if _logs is not None:
                  _logs = self._field_info.logsSeq.wrapper(_logs)
          self._logsSeq = _logs
          self.checkUnloadedProperty(_logs,'logsLoaded')

      def isLogsLoaded(self):
          return self._logsLoaded

      def sizeOfLogs(self, current = None):
          self.errorIfUnloaded()
          if not self._logsLoaded: return -1
          return len(self._logsSeq)

      def copyLogs(self, current = None):
          self.errorIfUnloaded()
          if not self._logsLoaded: self.throwNullCollectionException("logsSeq")
          return list(self._logsSeq)

      def iterateLogs(self):
          self.errorIfUnloaded()
          if not self._logsLoaded: self.throwNullCollectionException("logsSeq")
          return iter(self._logsSeq)

      def addEventLog(self, target, current = None):
          self.errorIfUnloaded()
          if not self._logsLoaded: self.throwNullCollectionException("logsSeq")
          self._logsSeq.append( target );
          target.setEvent( self )

      def addAllEventLogSet(self, targets, current = None):
          self.errorIfUnloaded()
          if  not self._logsLoaded: self.throwNullCollectionException("logsSeq")
          self._logsSeq.extend( targets )
          for target in targets:
              target.setEvent( self )

      def removeEventLog(self, target, current = None):
          self.errorIfUnloaded()
          if not self._logsLoaded: self.throwNullCollectionException("logsSeq")
          self._logsSeq.remove( target )
          target.setEvent( None )

      def removeAllEventLogSet(self, targets, current = None):
          self.errorIfUnloaded()
          if not self._logsLoaded: self.throwNullCollectionException("logsSeq")
          for elt in targets:
              elt.setEvent( None )
              self._logsSeq.remove( elt )

      def clearLogs(self, current = None):
          self.errorIfUnloaded()
          if not self._logsLoaded: self.throwNullCollectionException("logsSeq")
          for elt in self._logsSeq:
              elt.setEvent( None )
          self._logsSeq = list()

      def reloadLogs(self, toCopy, current = None):
          self.errorIfUnloaded()
          if self._logsLoaded:
              raise ode.ClientError("Cannot reload active collection: logsSeq")
          if not toCopy:
              raise ode.ClientError("Argument cannot be null")
          if toCopy.getId().getValue() != self.getId().getValue():
             raise ode.ClientError("Argument must have the same id as this instance")
          copy = toCopy.copyLogs() # May also throw
          for elt in copy:
              elt.setEvent( self )
          self._logsSeq = copy
          toCopy.unloadLogs()
          self._logsLoaded = True

      def unloadSession(self, ):
          self._sessionLoaded = False
          self._session = None;

      def getSession(self, current = None):
          self.errorIfUnloaded()
          return self._session

      def setSession(self, _session, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.session.wrapper is not None:
              if _session is not None:
                  _session = self._field_info.session.wrapper(_session)
          self._session = _session
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

_ode_model.EventI = EventI
