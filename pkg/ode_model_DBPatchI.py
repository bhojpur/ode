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
IceImport.load("ode_model_DBPatch_ice")
from ode.rtypes import rlong
from collections import namedtuple
_ode = Ice.openModule("ode")
_ode_model = Ice.openModule("ode.model")
__name__ = "ode.model"
class DBPatchI(_ode_model.DBPatch):

      # Property Metadata
      _field_info_data = namedtuple("FieldData", ["wrapper", "nullable"])
      _field_info_type = namedtuple("FieldInfo", [
          "currentVersion",
          "currentPatch",
          "previousVersion",
          "previousPatch",
          "finished",
          "message",
          "details",
      ])
      _field_info = _field_info_type(
          currentVersion=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          currentPatch=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          previousVersion=_field_info_data(wrapper=ode.rtypes.rstring, nullable=False),
          previousPatch=_field_info_data(wrapper=ode.rtypes.rint, nullable=False),
          finished=_field_info_data(wrapper=ode.rtypes.rtime, nullable=True),
          message=_field_info_data(wrapper=ode.rtypes.rstring, nullable=True),
          details=_field_info_data(wrapper=ode.proxy_to_instance, nullable=True),
      )  # end _field_info
      CURRENTVERSION =  "ode.model.meta.DBPatch_currentVersion"
      CURRENTPATCH =  "ode.model.meta.DBPatch_currentPatch"
      PREVIOUSVERSION =  "ode.model.meta.DBPatch_previousVersion"
      PREVIOUSPATCH =  "ode.model.meta.DBPatch_previousPatch"
      FINISHED =  "ode.model.meta.DBPatch_finished"
      MESSAGE =  "ode.model.meta.DBPatch_message"
      DETAILS =  "ode.model.meta.DBPatch_details"
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
          pass

      def __init__(self, id=None, loaded=None):
          super(DBPatchI, self).__init__()
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
          self.unloadCurrentVersion( )
          self.unloadCurrentPatch( )
          self.unloadPreviousVersion( )
          self.unloadPreviousPatch( )
          self.unloadFinished( )
          self.unloadMessage( )
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
            copy = DBPatchI()
            copy._id = self._id;
            copy._details = None  # Unloading for the moment.
            raise ode.ClientError("NYI")
      def proxy(self, current = None):
          if self._id is None: raise ode.ClientError("Proxies require an id")
          return DBPatchI( self._id.getValue(), False )

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

      def unloadCurrentVersion(self, ):
          self._currentVersionLoaded = False
          self._currentVersion = None;

      def getCurrentVersion(self, current = None):
          self.errorIfUnloaded()
          return self._currentVersion

      def setCurrentVersion(self, _currentVersion, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.currentVersion.wrapper is not None:
              if _currentVersion is not None:
                  _currentVersion = self._field_info.currentVersion.wrapper(_currentVersion)
          self._currentVersion = _currentVersion
          pass

      def unloadCurrentPatch(self, ):
          self._currentPatchLoaded = False
          self._currentPatch = None;

      def getCurrentPatch(self, current = None):
          self.errorIfUnloaded()
          return self._currentPatch

      def setCurrentPatch(self, _currentPatch, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.currentPatch.wrapper is not None:
              if _currentPatch is not None:
                  _currentPatch = self._field_info.currentPatch.wrapper(_currentPatch)
          self._currentPatch = _currentPatch
          pass

      def unloadPreviousVersion(self, ):
          self._previousVersionLoaded = False
          self._previousVersion = None;

      def getPreviousVersion(self, current = None):
          self.errorIfUnloaded()
          return self._previousVersion

      def setPreviousVersion(self, _previousVersion, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.previousVersion.wrapper is not None:
              if _previousVersion is not None:
                  _previousVersion = self._field_info.previousVersion.wrapper(_previousVersion)
          self._previousVersion = _previousVersion
          pass

      def unloadPreviousPatch(self, ):
          self._previousPatchLoaded = False
          self._previousPatch = None;

      def getPreviousPatch(self, current = None):
          self.errorIfUnloaded()
          return self._previousPatch

      def setPreviousPatch(self, _previousPatch, current = None, wrap=False):
          self.errorIfUnloaded()
          if wrap and self._field_info.previousPatch.wrapper is not None:
              if _previousPatch is not None:
                  _previousPatch = self._field_info.previousPatch.wrapper(_previousPatch)
          self._previousPatch = _previousPatch
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

_ode_model.DBPatchI = DBPatchI
