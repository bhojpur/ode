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

from sys import version_info as _version_info_
import Ice, IcePy
import Ice_BuiltinSequences_ice
import ode_ServerErrors_ice

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Start of module ode
__name__ = 'ode'

# Start of module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')
__name__ = 'ode.grid'

# Start of module ode.grid.monitors
_M_ode.grid.monitors = Ice.openModule('ode.grid.monitors')
__name__ = 'ode.grid.monitors'

if 'EventType' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.EventType = Ice.createTempClass()
    class EventType(Ice.EnumBase):
        """
        Enumeration for Monitor event types returned.
        Create, event is file or directory creation.
        Modify, event is file or directory modification.
        Delete, event is file or directory deletion.
        System, used to flag a system notification, info in fileId.
        """

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    EventType.Create = EventType("Create", 0)
    EventType.Modify = EventType("Modify", 1)
    EventType.Delete = EventType("Delete", 2)
    EventType.System = EventType("System", 3)
    EventType._enumerators = { 0:EventType.Create, 1:EventType.Modify, 2:EventType.Delete, 3:EventType.System }

    _M_ode.grid.monitors._t_EventType = IcePy.defineEnum('::ode::grid::monitors::EventType', EventType, (), EventType._enumerators)

    _M_ode.grid.monitors.EventType = EventType
    del EventType

if 'EventInfo' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.EventInfo = Ice.createTempClass()
    class EventInfo(object):
        """
        The id and type of an event. The file's basename is included for convenience,
        other stats are not included since they may be unavailable for some event types.
        """
        def __init__(self, fileId='', type=_M_ode.grid.monitors.EventType.Create):
            self.fileId = fileId
            self.type = type

        def __hash__(self):
            _h = 0
            _h = 5 * _h + Ice.getHash(self.fileId)
            _h = 5 * _h + Ice.getHash(self.type)
            return _h % 0x7fffffff

        def __compare(self, other):
            if other is None:
                return 1
            elif not isinstance(other, _M_ode.grid.monitors.EventInfo):
                return NotImplemented
            else:
                if self.fileId is None or other.fileId is None:
                    if self.fileId != other.fileId:
                        return (-1 if self.fileId is None else 1)
                else:
                    if self.fileId < other.fileId:
                        return -1
                    elif self.fileId > other.fileId:
                        return 1
                if self.type is None or other.type is None:
                    if self.type != other.type:
                        return (-1 if self.type is None else 1)
                else:
                    if self.type < other.type:
                        return -1
                    elif self.type > other.type:
                        return 1
                return 0

        def __lt__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r < 0

        def __le__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r <= 0

        def __gt__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r > 0

        def __ge__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r >= 0

        def __eq__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r == 0

        def __ne__(self, other):
            r = self.__compare(other)
            if r is NotImplemented:
                return r
            else:
                return r != 0

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid.monitors._t_EventInfo)

        __repr__ = __str__

    _M_ode.grid.monitors._t_EventInfo = IcePy.defineStruct('::ode::grid::monitors::EventInfo', EventInfo, (), (
        ('fileId', (), IcePy._t_string),
        ('type', (), _M_ode.grid.monitors._t_EventType)
    ))

    _M_ode.grid.monitors.EventInfo = EventInfo
    del EventInfo

if '_t_EventList' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors._t_EventList = IcePy.defineSequence('::ode::grid::monitors::EventList', (), _M_ode.grid.monitors._t_EventInfo)

if 'MonitorClient' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.MonitorClient = Ice.createTempClass()
    class MonitorClient(Ice.Object):
        """
        This interface must be implemented by a client that
        wishes to subscribe to an ode.fs server.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.monitors.MonitorClient:
                raise RuntimeError('ode.grid.monitors.MonitorClient is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::monitors::MonitorClient')

        def ice_id(self, current=None):
            return '::ode::grid::monitors::MonitorClient'

        def ice_staticId():
            return '::ode::grid::monitors::MonitorClient'
        ice_staticId = staticmethod(ice_staticId)

        def fsEventHappened(self, id, el, current=None):
            """
            Callback, called by the monitor upon the proxy of the ODE.fs client.
            Arguments:
            id -- monitor Id from which the event was reported (string).
            el -- list of events (EventList).
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid.monitors._t_MonitorClient)

        __repr__ = __str__

    _M_ode.grid.monitors.MonitorClientPrx = Ice.createTempClass()
    class MonitorClientPrx(Ice.ObjectPrx):

        """
        Callback, called by the monitor upon the proxy of the ODE.fs client.
        Arguments:
        id -- monitor Id from which the event was reported (string).
        el -- list of events (EventList).
        _ctx -- The request context for the invocation.
        """
        def fsEventHappened(self, id, el, _ctx=None):
            return _M_ode.grid.monitors.MonitorClient._op_fsEventHappened.invoke(self, ((id, el), _ctx))

        """
        Callback, called by the monitor upon the proxy of the ODE.fs client.
        Arguments:
        id -- monitor Id from which the event was reported (string).
        el -- list of events (EventList).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_fsEventHappened(self, id, el, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorClient._op_fsEventHappened.begin(self, ((id, el), _response, _ex, _sent, _ctx))

        """
        Callback, called by the monitor upon the proxy of the ODE.fs client.
        Arguments:
        id -- monitor Id from which the event was reported (string).
        el -- list of events (EventList).
        """
        def end_fsEventHappened(self, _r):
            return _M_ode.grid.monitors.MonitorClient._op_fsEventHappened.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorClientPrx.ice_checkedCast(proxy, '::ode::grid::monitors::MonitorClient', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.monitors.MonitorClientPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::monitors::MonitorClient'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid.monitors._t_MonitorClientPrx = IcePy.defineProxy('::ode::grid::monitors::MonitorClient', MonitorClientPrx)

    _M_ode.grid.monitors._t_MonitorClient = IcePy.defineClass('::ode::grid::monitors::MonitorClient', MonitorClient, -1, (), True, False, None, (), ())
    MonitorClient._ice_type = _M_ode.grid.monitors._t_MonitorClient

    MonitorClient._op_fsEventHappened = IcePy.Operation('fsEventHappened', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode.grid.monitors._t_EventList, False, 0)), (), None, (_M_ode._t_ServerError,))

    _M_ode.grid.monitors.MonitorClient = MonitorClient
    del MonitorClient

    _M_ode.grid.monitors.MonitorClientPrx = MonitorClientPrx
    del MonitorClientPrx

if 'MonitorType' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.MonitorType = Ice.createTempClass()
    class MonitorType(Ice.EnumBase):

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    MonitorType.Persistent = MonitorType("Persistent", 0)
    MonitorType.OneShot = MonitorType("OneShot", 1)
    MonitorType.Inactivity = MonitorType("Inactivity", 2)
    MonitorType._enumerators = { 0:MonitorType.Persistent, 1:MonitorType.OneShot, 2:MonitorType.Inactivity }

    _M_ode.grid.monitors._t_MonitorType = IcePy.defineEnum('::ode::grid::monitors::MonitorType', MonitorType, (), MonitorType._enumerators)

    _M_ode.grid.monitors.MonitorType = MonitorType
    del MonitorType

if 'FileType' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.FileType = Ice.createTempClass()
    class FileType(Ice.EnumBase):

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    FileType.File = FileType("File", 0)
    FileType.Dir = FileType("Dir", 1)
    FileType.Link = FileType("Link", 2)
    FileType.Mount = FileType("Mount", 3)
    FileType.Unknown = FileType("Unknown", 4)
    FileType._enumerators = { 0:FileType.File, 1:FileType.Dir, 2:FileType.Link, 3:FileType.Mount, 4:FileType.Unknown }

    _M_ode.grid.monitors._t_FileType = IcePy.defineEnum('::ode::grid::monitors::FileType', FileType, (), FileType._enumerators)

    _M_ode.grid.monitors.FileType = FileType
    del FileType

if 'PathMode' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.PathMode = Ice.createTempClass()
    class PathMode(Ice.EnumBase):
        """
        Enumeration for Monitor path modes.
        Flat, monitor the specified directory but not its subdirectories.
        Recursive, monitor the specified directory and its subdirectories.
        Follow,  monitor as Recursive but with new directories being added
        to the monitor if they are created.
        Not all path modes may be implemented for a given operating system.
        """

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    PathMode.Flat = PathMode("Flat", 0)
    PathMode.Recurse = PathMode("Recurse", 1)
    PathMode.Follow = PathMode("Follow", 2)
    PathMode._enumerators = { 0:PathMode.Flat, 1:PathMode.Recurse, 2:PathMode.Follow }

    _M_ode.grid.monitors._t_PathMode = IcePy.defineEnum('::ode::grid::monitors::PathMode', PathMode, (), PathMode._enumerators)

    _M_ode.grid.monitors.PathMode = PathMode
    del PathMode

if 'WatchEventType' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.WatchEventType = Ice.createTempClass()
    class WatchEventType(Ice.EnumBase):
        """
        Enumeration for event types to watch.
        Create, notify on file creation only.
        Modify, notify on file modification only.
        Delete, notify on file deletion only.
        All, notify on all vents in the enumeration that apply to a given OS.
        Not all event types may be implemented for a given operating system.
        """

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    WatchEventType.Creation = WatchEventType("Creation", 0)
    WatchEventType.Modification = WatchEventType("Modification", 1)
    WatchEventType.Deletion = WatchEventType("Deletion", 2)
    WatchEventType.All = WatchEventType("All", 3)
    WatchEventType._enumerators = { 0:WatchEventType.Creation, 1:WatchEventType.Modification, 2:WatchEventType.Deletion, 3:WatchEventType.All }

    _M_ode.grid.monitors._t_WatchEventType = IcePy.defineEnum('::ode::grid::monitors::WatchEventType', WatchEventType, (), WatchEventType._enumerators)

    _M_ode.grid.monitors.WatchEventType = WatchEventType
    del WatchEventType

if 'MonitorState' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.MonitorState = Ice.createTempClass()
    class MonitorState(Ice.EnumBase):
        """
        Enumeration for Monitor state.
        Stopped, a monitor exists but is not actively monitoring.
        Started, a monitor exists and is actively monitoring.
        """

        def __init__(self, _n, _v):
            Ice.EnumBase.__init__(self, _n, _v)

        def valueOf(self, _n):
            if _n in self._enumerators:
                return self._enumerators[_n]
            return None
        valueOf = classmethod(valueOf)

    MonitorState.Stopped = MonitorState("Stopped", 0)
    MonitorState.Started = MonitorState("Started", 1)
    MonitorState._enumerators = { 0:MonitorState.Stopped, 1:MonitorState.Started }

    _M_ode.grid.monitors._t_MonitorState = IcePy.defineEnum('::ode::grid::monitors::MonitorState', MonitorState, (), MonitorState._enumerators)

    _M_ode.grid.monitors.MonitorState = MonitorState
    del MonitorState

if 'FileStats' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.FileStats = Ice.createTempClass()
    class FileStats(object):
        """
        File stats.
        What stats are likely to be needed? Could this struct be trimmed down
        or does it need any further attributes?
        """
        def __init__(self, baseName='', owner='', size=0, mTime=0.0, cTime=0.0, aTime=0.0, type=_M_ode.grid.monitors.FileType.File):
            self.baseName = baseName
            self.owner = owner
            self.size = size
            self.mTime = mTime
            self.cTime = cTime
            self.aTime = aTime
            self.type = type

        def __eq__(self, other):
            if other is None:
                return False
            elif not isinstance(other, _M_ode.grid.monitors.FileStats):
                return NotImplemented
            else:
                if self.baseName != other.baseName:
                    return False
                if self.owner != other.owner:
                    return False
                if self.size != other.size:
                    return False
                if self.mTime != other.mTime:
                    return False
                if self.cTime != other.cTime:
                    return False
                if self.aTime != other.aTime:
                    return False
                if self.type != other.type:
                    return False
                return True

        def __ne__(self, other):
            return not self.__eq__(other)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid.monitors._t_FileStats)

        __repr__ = __str__

    _M_ode.grid.monitors._t_FileStats = IcePy.defineStruct('::ode::grid::monitors::FileStats', FileStats, (), (
        ('baseName', (), IcePy._t_string),
        ('owner', (), IcePy._t_string),
        ('size', (), IcePy._t_long),
        ('mTime', (), IcePy._t_float),
        ('cTime', (), IcePy._t_float),
        ('aTime', (), IcePy._t_float),
        ('type', (), _M_ode.grid.monitors._t_FileType)
    ))

    _M_ode.grid.monitors.FileStats = FileStats
    del FileStats

if '_t_WatchEventList' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors._t_WatchEventList = IcePy.defineSequence('::ode::grid::monitors::WatchEventList', (), _M_ode.grid.monitors._t_WatchEventType)

if '_t_FileStatsList' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors._t_FileStatsList = IcePy.defineSequence('::ode::grid::monitors::FileStatsList', (), _M_ode.grid.monitors._t_FileStats)

if 'FileServer' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.FileServer = Ice.createTempClass()
    class FileServer(Ice.Object):
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.monitors.FileServer:
                raise RuntimeError('ode.grid.monitors.FileServer is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::monitors::FileServer')

        def ice_id(self, current=None):
            return '::ode::grid::monitors::FileServer'

        def ice_staticId():
            return '::ode::grid::monitors::FileServer'
        ice_staticId = staticmethod(ice_staticId)

        def getDirectory(self, absPath, filter, current=None):
            """
            Get an absolute directory from an ODE.fs server.
            The returned list will contain just the file names for each directory entry.
            An exception will be raised if the path does not exist or is inaccessible to the
            ODE.fs server. An exception will be raised if directory list cannot be
            returned for any other reason.
            Arguments:
            absPath -- an absolute path on the monitor's watch path (string).
            filter -- a filter to apply to the listing, cf. ls (string).
            current -- The Current object for the invocation.
            Returns: a directory listing (Ice::StringSeq).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getBulkDirectory(self, absPath, filter, current=None):
            """
            Get an absolute directory from an ODE.fs server.
            The returned list will contain the file stats for each directory entry.
            An exception will be raised if the path does not exist or is inaccessible to the
            ODE.fs server. An exception will be raised if directory list cannot be
            returned for any other reason.
            Arguments:
            absPath -- an absolute path on the monitor's watch path (string).
            filter -- a filter to apply to the listing, cf. ls (string).
            current -- The Current object for the invocation.
            Returns: a directory listing (FileStatsList).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def fileExists(self, fileId, current=None):
            """
            Query the existence of a file
            An exception will be raised if the method fails to determine the existence.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: existence of file.
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getBaseName(self, fileId, current=None):
            """
            Get base name of a file, this is the name
            stripped of any path, e.g. file.ext
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: base name.
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getStats(self, fileId, current=None):
            """
            Get all FileStats of a file
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: file stats (FileStats).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getSize(self, fileId, current=None):
            """
            Get size of a file in bytes
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: byte size of file (long).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getOwner(self, fileId, current=None):
            """
            Get owner of a file
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: owner of file (string).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getCTime(self, fileId, current=None):
            """
            Get ctime of a file
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: ctime of file (float).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getMTime(self, fileId, current=None):
            """
            Get mtime of a file
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: mtime of file (float).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getATime(self, fileId, current=None):
            """
            Get atime of a file
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: atime of file (float).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def isDir(self, fileId, current=None):
            """
            Query whether file is a directory
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: true is directory (bool).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def isFile(self, fileId, current=None):
            """
            Query whether file is a file
            An exception will be raised if the file no longer exists or is inaccessible.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: true if file (bool).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getSHA1(self, fileId, current=None):
            """
            Get SHA1 of a file
            An exception will be raised if the file no longer exists or is inaccessible.
            An exception will be raised if the SHA1 cannot be generated for any reason.
            Arguments:
            fileId -- see above.
            current -- The Current object for the invocation.
            Returns: SHA1 hex hash digest of file (string).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def readBlock(self, fileId, offset, size, current=None):
            """
            readBlock should open, read size bytes from offset
            and then close the file.
            An exception will be raised if the file no longer exists or is inaccessible.
            An exception will be raised if the file read fails for any other reason.
            Arguments:
            fileId -- see above.
            offset -- byte offset into file from where read should begin (long).
            size -- number of bytes that should be read (int).
            current -- The Current object for the invocation.
            Returns: byte sequence of upto size bytes.
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid.monitors._t_FileServer)

        __repr__ = __str__

    _M_ode.grid.monitors.FileServerPrx = Ice.createTempClass()
    class FileServerPrx(Ice.ObjectPrx):

        """
        Get an absolute directory from an ODE.fs server.
        The returned list will contain just the file names for each directory entry.
        An exception will be raised if the path does not exist or is inaccessible to the
        ODE.fs server. An exception will be raised if directory list cannot be
        returned for any other reason.
        Arguments:
        absPath -- an absolute path on the monitor's watch path (string).
        filter -- a filter to apply to the listing, cf. ls (string).
        _ctx -- The request context for the invocation.
        Returns: a directory listing (Ice::StringSeq).
        Throws:
        ode::OdeFSError -- 
        """
        def getDirectory(self, absPath, filter, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getDirectory.invoke(self, ((absPath, filter), _ctx))

        """
        Get an absolute directory from an ODE.fs server.
        The returned list will contain just the file names for each directory entry.
        An exception will be raised if the path does not exist or is inaccessible to the
        ODE.fs server. An exception will be raised if directory list cannot be
        returned for any other reason.
        Arguments:
        absPath -- an absolute path on the monitor's watch path (string).
        filter -- a filter to apply to the listing, cf. ls (string).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getDirectory(self, absPath, filter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getDirectory.begin(self, ((absPath, filter), _response, _ex, _sent, _ctx))

        """
        Get an absolute directory from an ODE.fs server.
        The returned list will contain just the file names for each directory entry.
        An exception will be raised if the path does not exist or is inaccessible to the
        ODE.fs server. An exception will be raised if directory list cannot be
        returned for any other reason.
        Arguments:
        absPath -- an absolute path on the monitor's watch path (string).
        filter -- a filter to apply to the listing, cf. ls (string).
        Returns: a directory listing (Ice::StringSeq).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getDirectory(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getDirectory.end(self, _r)

        """
        Get an absolute directory from an ODE.fs server.
        The returned list will contain the file stats for each directory entry.
        An exception will be raised if the path does not exist or is inaccessible to the
        ODE.fs server. An exception will be raised if directory list cannot be
        returned for any other reason.
        Arguments:
        absPath -- an absolute path on the monitor's watch path (string).
        filter -- a filter to apply to the listing, cf. ls (string).
        _ctx -- The request context for the invocation.
        Returns: a directory listing (FileStatsList).
        Throws:
        ode::OdeFSError -- 
        """
        def getBulkDirectory(self, absPath, filter, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getBulkDirectory.invoke(self, ((absPath, filter), _ctx))

        """
        Get an absolute directory from an ODE.fs server.
        The returned list will contain the file stats for each directory entry.
        An exception will be raised if the path does not exist or is inaccessible to the
        ODE.fs server. An exception will be raised if directory list cannot be
        returned for any other reason.
        Arguments:
        absPath -- an absolute path on the monitor's watch path (string).
        filter -- a filter to apply to the listing, cf. ls (string).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getBulkDirectory(self, absPath, filter, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getBulkDirectory.begin(self, ((absPath, filter), _response, _ex, _sent, _ctx))

        """
        Get an absolute directory from an ODE.fs server.
        The returned list will contain the file stats for each directory entry.
        An exception will be raised if the path does not exist or is inaccessible to the
        ODE.fs server. An exception will be raised if directory list cannot be
        returned for any other reason.
        Arguments:
        absPath -- an absolute path on the monitor's watch path (string).
        filter -- a filter to apply to the listing, cf. ls (string).
        Returns: a directory listing (FileStatsList).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getBulkDirectory(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getBulkDirectory.end(self, _r)

        """
        Query the existence of a file
        An exception will be raised if the method fails to determine the existence.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: existence of file.
        Throws:
        ode::OdeFSError -- 
        """
        def fileExists(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_fileExists.invoke(self, ((fileId, ), _ctx))

        """
        Query the existence of a file
        An exception will be raised if the method fails to determine the existence.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_fileExists(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_fileExists.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Query the existence of a file
        An exception will be raised if the method fails to determine the existence.
        Arguments:
        fileId -- see above.
        Returns: existence of file.
        Throws:
        ode::OdeFSError -- 
        """
        def end_fileExists(self, _r):
            return _M_ode.grid.monitors.FileServer._op_fileExists.end(self, _r)

        """
        Get base name of a file, this is the name
        stripped of any path, e.g. file.ext
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: base name.
        Throws:
        ode::OdeFSError -- 
        """
        def getBaseName(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getBaseName.invoke(self, ((fileId, ), _ctx))

        """
        Get base name of a file, this is the name
        stripped of any path, e.g. file.ext
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getBaseName(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getBaseName.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get base name of a file, this is the name
        stripped of any path, e.g. file.ext
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: base name.
        Throws:
        ode::OdeFSError -- 
        """
        def end_getBaseName(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getBaseName.end(self, _r)

        """
        Get all FileStats of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: file stats (FileStats).
        Throws:
        ode::OdeFSError -- 
        """
        def getStats(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getStats.invoke(self, ((fileId, ), _ctx))

        """
        Get all FileStats of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getStats(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getStats.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get all FileStats of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: file stats (FileStats).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getStats(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getStats.end(self, _r)

        """
        Get size of a file in bytes
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: byte size of file (long).
        Throws:
        ode::OdeFSError -- 
        """
        def getSize(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getSize.invoke(self, ((fileId, ), _ctx))

        """
        Get size of a file in bytes
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSize(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getSize.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get size of a file in bytes
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: byte size of file (long).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getSize(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getSize.end(self, _r)

        """
        Get owner of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: owner of file (string).
        Throws:
        ode::OdeFSError -- 
        """
        def getOwner(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getOwner.invoke(self, ((fileId, ), _ctx))

        """
        Get owner of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getOwner(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getOwner.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get owner of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: owner of file (string).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getOwner(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getOwner.end(self, _r)

        """
        Get ctime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: ctime of file (float).
        Throws:
        ode::OdeFSError -- 
        """
        def getCTime(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getCTime.invoke(self, ((fileId, ), _ctx))

        """
        Get ctime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getCTime(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getCTime.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get ctime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: ctime of file (float).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getCTime(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getCTime.end(self, _r)

        """
        Get mtime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: mtime of file (float).
        Throws:
        ode::OdeFSError -- 
        """
        def getMTime(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getMTime.invoke(self, ((fileId, ), _ctx))

        """
        Get mtime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMTime(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getMTime.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get mtime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: mtime of file (float).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getMTime(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getMTime.end(self, _r)

        """
        Get atime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: atime of file (float).
        Throws:
        ode::OdeFSError -- 
        """
        def getATime(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getATime.invoke(self, ((fileId, ), _ctx))

        """
        Get atime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getATime(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getATime.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get atime of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: atime of file (float).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getATime(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getATime.end(self, _r)

        """
        Query whether file is a directory
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: true is directory (bool).
        Throws:
        ode::OdeFSError -- 
        """
        def isDir(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_isDir.invoke(self, ((fileId, ), _ctx))

        """
        Query whether file is a directory
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isDir(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_isDir.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Query whether file is a directory
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: true is directory (bool).
        Throws:
        ode::OdeFSError -- 
        """
        def end_isDir(self, _r):
            return _M_ode.grid.monitors.FileServer._op_isDir.end(self, _r)

        """
        Query whether file is a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: true if file (bool).
        Throws:
        ode::OdeFSError -- 
        """
        def isFile(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_isFile.invoke(self, ((fileId, ), _ctx))

        """
        Query whether file is a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_isFile(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_isFile.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Query whether file is a file
        An exception will be raised if the file no longer exists or is inaccessible.
        Arguments:
        fileId -- see above.
        Returns: true if file (bool).
        Throws:
        ode::OdeFSError -- 
        """
        def end_isFile(self, _r):
            return _M_ode.grid.monitors.FileServer._op_isFile.end(self, _r)

        """
        Get SHA1 of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        An exception will be raised if the SHA1 cannot be generated for any reason.
        Arguments:
        fileId -- see above.
        _ctx -- The request context for the invocation.
        Returns: SHA1 hex hash digest of file (string).
        Throws:
        ode::OdeFSError -- 
        """
        def getSHA1(self, fileId, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getSHA1.invoke(self, ((fileId, ), _ctx))

        """
        Get SHA1 of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        An exception will be raised if the SHA1 cannot be generated for any reason.
        Arguments:
        fileId -- see above.
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getSHA1(self, fileId, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_getSHA1.begin(self, ((fileId, ), _response, _ex, _sent, _ctx))

        """
        Get SHA1 of a file
        An exception will be raised if the file no longer exists or is inaccessible.
        An exception will be raised if the SHA1 cannot be generated for any reason.
        Arguments:
        fileId -- see above.
        Returns: SHA1 hex hash digest of file (string).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getSHA1(self, _r):
            return _M_ode.grid.monitors.FileServer._op_getSHA1.end(self, _r)

        """
        readBlock should open, read size bytes from offset
        and then close the file.
        An exception will be raised if the file no longer exists or is inaccessible.
        An exception will be raised if the file read fails for any other reason.
        Arguments:
        fileId -- see above.
        offset -- byte offset into file from where read should begin (long).
        size -- number of bytes that should be read (int).
        _ctx -- The request context for the invocation.
        Returns: byte sequence of upto size bytes.
        Throws:
        ode::OdeFSError -- 
        """
        def readBlock(self, fileId, offset, size, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_readBlock.invoke(self, ((fileId, offset, size), _ctx))

        """
        readBlock should open, read size bytes from offset
        and then close the file.
        An exception will be raised if the file no longer exists or is inaccessible.
        An exception will be raised if the file read fails for any other reason.
        Arguments:
        fileId -- see above.
        offset -- byte offset into file from where read should begin (long).
        size -- number of bytes that should be read (int).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_readBlock(self, fileId, offset, size, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.FileServer._op_readBlock.begin(self, ((fileId, offset, size), _response, _ex, _sent, _ctx))

        """
        readBlock should open, read size bytes from offset
        and then close the file.
        An exception will be raised if the file no longer exists or is inaccessible.
        An exception will be raised if the file read fails for any other reason.
        Arguments:
        fileId -- see above.
        offset -- byte offset into file from where read should begin (long).
        size -- number of bytes that should be read (int).
        Returns: byte sequence of upto size bytes.
        Throws:
        ode::OdeFSError -- 
        """
        def end_readBlock(self, _r):
            return _M_ode.grid.monitors.FileServer._op_readBlock.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.monitors.FileServerPrx.ice_checkedCast(proxy, '::ode::grid::monitors::FileServer', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.monitors.FileServerPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::monitors::FileServer'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid.monitors._t_FileServerPrx = IcePy.defineProxy('::ode::grid::monitors::FileServer', FileServerPrx)

    _M_ode.grid.monitors._t_FileServer = IcePy.defineClass('::ode::grid::monitors::FileServer', FileServer, -1, (), True, False, None, (), ())
    FileServer._ice_type = _M_ode.grid.monitors._t_FileServer

    FileServer._op_getDirectory = IcePy.Operation('getDirectory', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_Ice._t_StringSeq, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getBulkDirectory = IcePy.Operation('getBulkDirectory', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.grid.monitors._t_FileStatsList, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_fileExists = IcePy.Operation('fileExists', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getBaseName = IcePy.Operation('getBaseName', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getStats = IcePy.Operation('getStats', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.grid.monitors._t_FileStats, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getSize = IcePy.Operation('getSize', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getOwner = IcePy.Operation('getOwner', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getCTime = IcePy.Operation('getCTime', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_float, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getMTime = IcePy.Operation('getMTime', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_float, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getATime = IcePy.Operation('getATime', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_float, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_isDir = IcePy.Operation('isDir', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_isFile = IcePy.Operation('isFile', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_getSHA1 = IcePy.Operation('getSHA1', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_OdeFSError,))
    FileServer._op_readBlock = IcePy.Operation('readBlock', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_long, False, 0), ((), IcePy._t_int, False, 0)), (), ((), _M_Ice._t_ByteSeq, False, 0), (_M_ode._t_OdeFSError,))

    _M_ode.grid.monitors.FileServer = FileServer
    del FileServer

    _M_ode.grid.monitors.FileServerPrx = FileServerPrx
    del FileServerPrx

if 'MonitorServer' not in _M_ode.grid.monitors.__dict__:
    _M_ode.grid.monitors.MonitorServer = Ice.createTempClass()
    class MonitorServer(Ice.Object):
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.monitors.MonitorServer:
                raise RuntimeError('ode.grid.monitors.MonitorServer is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::monitors::MonitorServer')

        def ice_id(self, current=None):
            return '::ode::grid::monitors::MonitorServer'

        def ice_staticId():
            return '::ode::grid::monitors::MonitorServer'
        ice_staticId = staticmethod(ice_staticId)

        def createMonitor(self, mType, eTypes, pMode, pathString, whitelist, blacklist, timeout, blockSize, ignoreSysFiles, ignoreDirEvents, platformCheck, proxy, current=None):
            """
            Create a monitor of events.
            A exception will be raised if the event type or path mode is not supported by
            the Monitor implementation for a given OS. An exception will be raised if the
            path does not exist or is inaccessible to the monitor. An exception will be raised
            if a monitor cannot be created for any other reason.
            Arguments:
            mType -- type of monitor to create (MonitorType).
            eTypes -- a sequence of watch event type to monitor (WatchEventTypeList).
            pMode -- path mode of monitor (PathMode).
            pathString -- full path of directory of interest (string).
            whitelist -- list of files or extensions of interest (Ice::StringSeq).
            blacklist -- list of directories, files or extensions that are not of interest (Ice::StringSeq).
            timeout -- time in seconds fo monitor to time out (float).
            blockSize -- the number of events to pack into each notification (int).
            ignoreSysFiles -- ignore system files or not (bool).
            ignoreDirEvents -- ignore directory events (bool).
            platformCheck -- if true strictly check platform (bool).
            proxy -- a proxy of the client to which notifications will be sent (MonitorClient*).
            current -- The Current object for the invocation.
            Returns: monitorId, a uuid1 (string).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def startMonitor(self, id, current=None):
            """
            Start an existing monitor.
            An exception will be raised if the id does not correspond to an existing monitor.
            An exception will be raised if a monitor cannot be started for any other reason,
            in this case the monitor's state cannot be assumed.
            Arguments:
            id -- monitor id (string).
            current -- The Current object for the invocation.
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def stopMonitor(self, id, current=None):
            """
            Stop an existing monitor.
            Attempting to stop a monitor that is not running raises no exception.
            An exception will be raised if the id does not correspond to an existing monitor.
            An exception will be raised if a monitor cannot be stopped for any other reason,
            in this case the monitor's state cannot be assumed.
            Arguments:
            id -- monitor id (string).
            current -- The Current object for the invocation.
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def destroyMonitor(self, id, current=None):
            """
            Destroy an existing monitor.
            Attempting to destroy a monitor that is running will try to first stop
            the monitor and then destroy it.
            An exception will be raised if the id does not correspond to an existing monitor.
            An exception will be raised if a monitor cannot be destroyed (or stopped and destroyed)
            for any other reason, in this case the monitor's state cannot be assumed.
            Arguments:
            id -- monitor id (string).
            current -- The Current object for the invocation.
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def getMonitorState(self, id, current=None):
            """
            Get the state of an existing monitor.
            An exception will be raised if the id does not correspond to an existing monitor.
            Arguments:
            id -- monitor id (string).
            current -- The Current object for the invocation.
            Returns: the monitor state (MonitorState).
            Throws:
            ode::OdeFSError -- 
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid.monitors._t_MonitorServer)

        __repr__ = __str__

    _M_ode.grid.monitors.MonitorServerPrx = Ice.createTempClass()
    class MonitorServerPrx(Ice.ObjectPrx):

        """
        Create a monitor of events.
        A exception will be raised if the event type or path mode is not supported by
        the Monitor implementation for a given OS. An exception will be raised if the
        path does not exist or is inaccessible to the monitor. An exception will be raised
        if a monitor cannot be created for any other reason.
        Arguments:
        mType -- type of monitor to create (MonitorType).
        eTypes -- a sequence of watch event type to monitor (WatchEventTypeList).
        pMode -- path mode of monitor (PathMode).
        pathString -- full path of directory of interest (string).
        whitelist -- list of files or extensions of interest (Ice::StringSeq).
        blacklist -- list of directories, files or extensions that are not of interest (Ice::StringSeq).
        timeout -- time in seconds fo monitor to time out (float).
        blockSize -- the number of events to pack into each notification (int).
        ignoreSysFiles -- ignore system files or not (bool).
        ignoreDirEvents -- ignore directory events (bool).
        platformCheck -- if true strictly check platform (bool).
        proxy -- a proxy of the client to which notifications will be sent (MonitorClient*).
        _ctx -- The request context for the invocation.
        Returns: monitorId, a uuid1 (string).
        Throws:
        ode::OdeFSError -- 
        """
        def createMonitor(self, mType, eTypes, pMode, pathString, whitelist, blacklist, timeout, blockSize, ignoreSysFiles, ignoreDirEvents, platformCheck, proxy, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_createMonitor.invoke(self, ((mType, eTypes, pMode, pathString, whitelist, blacklist, timeout, blockSize, ignoreSysFiles, ignoreDirEvents, platformCheck, proxy), _ctx))

        """
        Create a monitor of events.
        A exception will be raised if the event type or path mode is not supported by
        the Monitor implementation for a given OS. An exception will be raised if the
        path does not exist or is inaccessible to the monitor. An exception will be raised
        if a monitor cannot be created for any other reason.
        Arguments:
        mType -- type of monitor to create (MonitorType).
        eTypes -- a sequence of watch event type to monitor (WatchEventTypeList).
        pMode -- path mode of monitor (PathMode).
        pathString -- full path of directory of interest (string).
        whitelist -- list of files or extensions of interest (Ice::StringSeq).
        blacklist -- list of directories, files or extensions that are not of interest (Ice::StringSeq).
        timeout -- time in seconds fo monitor to time out (float).
        blockSize -- the number of events to pack into each notification (int).
        ignoreSysFiles -- ignore system files or not (bool).
        ignoreDirEvents -- ignore directory events (bool).
        platformCheck -- if true strictly check platform (bool).
        proxy -- a proxy of the client to which notifications will be sent (MonitorClient*).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_createMonitor(self, mType, eTypes, pMode, pathString, whitelist, blacklist, timeout, blockSize, ignoreSysFiles, ignoreDirEvents, platformCheck, proxy, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_createMonitor.begin(self, ((mType, eTypes, pMode, pathString, whitelist, blacklist, timeout, blockSize, ignoreSysFiles, ignoreDirEvents, platformCheck, proxy), _response, _ex, _sent, _ctx))

        """
        Create a monitor of events.
        A exception will be raised if the event type or path mode is not supported by
        the Monitor implementation for a given OS. An exception will be raised if the
        path does not exist or is inaccessible to the monitor. An exception will be raised
        if a monitor cannot be created for any other reason.
        Arguments:
        mType -- type of monitor to create (MonitorType).
        eTypes -- a sequence of watch event type to monitor (WatchEventTypeList).
        pMode -- path mode of monitor (PathMode).
        pathString -- full path of directory of interest (string).
        whitelist -- list of files or extensions of interest (Ice::StringSeq).
        blacklist -- list of directories, files or extensions that are not of interest (Ice::StringSeq).
        timeout -- time in seconds fo monitor to time out (float).
        blockSize -- the number of events to pack into each notification (int).
        ignoreSysFiles -- ignore system files or not (bool).
        ignoreDirEvents -- ignore directory events (bool).
        platformCheck -- if true strictly check platform (bool).
        proxy -- a proxy of the client to which notifications will be sent (MonitorClient*).
        Returns: monitorId, a uuid1 (string).
        Throws:
        ode::OdeFSError -- 
        """
        def end_createMonitor(self, _r):
            return _M_ode.grid.monitors.MonitorServer._op_createMonitor.end(self, _r)

        """
        Start an existing monitor.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be started for any other reason,
        in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        _ctx -- The request context for the invocation.
        Throws:
        ode::OdeFSError -- 
        """
        def startMonitor(self, id, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_startMonitor.invoke(self, ((id, ), _ctx))

        """
        Start an existing monitor.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be started for any other reason,
        in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_startMonitor(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_startMonitor.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Start an existing monitor.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be started for any other reason,
        in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        Throws:
        ode::OdeFSError -- 
        """
        def end_startMonitor(self, _r):
            return _M_ode.grid.monitors.MonitorServer._op_startMonitor.end(self, _r)

        """
        Stop an existing monitor.
        Attempting to stop a monitor that is not running raises no exception.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be stopped for any other reason,
        in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        _ctx -- The request context for the invocation.
        Throws:
        ode::OdeFSError -- 
        """
        def stopMonitor(self, id, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_stopMonitor.invoke(self, ((id, ), _ctx))

        """
        Stop an existing monitor.
        Attempting to stop a monitor that is not running raises no exception.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be stopped for any other reason,
        in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_stopMonitor(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_stopMonitor.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Stop an existing monitor.
        Attempting to stop a monitor that is not running raises no exception.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be stopped for any other reason,
        in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        Throws:
        ode::OdeFSError -- 
        """
        def end_stopMonitor(self, _r):
            return _M_ode.grid.monitors.MonitorServer._op_stopMonitor.end(self, _r)

        """
        Destroy an existing monitor.
        Attempting to destroy a monitor that is running will try to first stop
        the monitor and then destroy it.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be destroyed (or stopped and destroyed)
        for any other reason, in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        _ctx -- The request context for the invocation.
        Throws:
        ode::OdeFSError -- 
        """
        def destroyMonitor(self, id, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_destroyMonitor.invoke(self, ((id, ), _ctx))

        """
        Destroy an existing monitor.
        Attempting to destroy a monitor that is running will try to first stop
        the monitor and then destroy it.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be destroyed (or stopped and destroyed)
        for any other reason, in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_destroyMonitor(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_destroyMonitor.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Destroy an existing monitor.
        Attempting to destroy a monitor that is running will try to first stop
        the monitor and then destroy it.
        An exception will be raised if the id does not correspond to an existing monitor.
        An exception will be raised if a monitor cannot be destroyed (or stopped and destroyed)
        for any other reason, in this case the monitor's state cannot be assumed.
        Arguments:
        id -- monitor id (string).
        Throws:
        ode::OdeFSError -- 
        """
        def end_destroyMonitor(self, _r):
            return _M_ode.grid.monitors.MonitorServer._op_destroyMonitor.end(self, _r)

        """
        Get the state of an existing monitor.
        An exception will be raised if the id does not correspond to an existing monitor.
        Arguments:
        id -- monitor id (string).
        _ctx -- The request context for the invocation.
        Returns: the monitor state (MonitorState).
        Throws:
        ode::OdeFSError -- 
        """
        def getMonitorState(self, id, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_getMonitorState.invoke(self, ((id, ), _ctx))

        """
        Get the state of an existing monitor.
        An exception will be raised if the id does not correspond to an existing monitor.
        Arguments:
        id -- monitor id (string).
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getMonitorState(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorServer._op_getMonitorState.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        """
        Get the state of an existing monitor.
        An exception will be raised if the id does not correspond to an existing monitor.
        Arguments:
        id -- monitor id (string).
        Returns: the monitor state (MonitorState).
        Throws:
        ode::OdeFSError -- 
        """
        def end_getMonitorState(self, _r):
            return _M_ode.grid.monitors.MonitorServer._op_getMonitorState.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.monitors.MonitorServerPrx.ice_checkedCast(proxy, '::ode::grid::monitors::MonitorServer', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.monitors.MonitorServerPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::monitors::MonitorServer'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid.monitors._t_MonitorServerPrx = IcePy.defineProxy('::ode::grid::monitors::MonitorServer', MonitorServerPrx)

    _M_ode.grid.monitors._t_MonitorServer = IcePy.defineClass('::ode::grid::monitors::MonitorServer', MonitorServer, -1, (), True, False, None, (), ())
    MonitorServer._ice_type = _M_ode.grid.monitors._t_MonitorServer

    MonitorServer._op_createMonitor = IcePy.Operation('createMonitor', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid.monitors._t_MonitorType, False, 0), ((), _M_ode.grid.monitors._t_WatchEventList, False, 0), ((), _M_ode.grid.monitors._t_PathMode, False, 0), ((), IcePy._t_string, False, 0), ((), _M_Ice._t_StringSeq, False, 0), ((), _M_Ice._t_StringSeq, False, 0), ((), IcePy._t_float, False, 0), ((), IcePy._t_int, False, 0), ((), IcePy._t_bool, False, 0), ((), IcePy._t_bool, False, 0), ((), IcePy._t_bool, False, 0), ((), _M_ode.grid.monitors._t_MonitorClientPrx, False, 0)), (), ((), IcePy._t_string, False, 0), (_M_ode._t_OdeFSError,))
    MonitorServer._op_startMonitor = IcePy.Operation('startMonitor', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_OdeFSError,))
    MonitorServer._op_stopMonitor = IcePy.Operation('stopMonitor', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_OdeFSError,))
    MonitorServer._op_destroyMonitor = IcePy.Operation('destroyMonitor', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), None, (_M_ode._t_OdeFSError,))
    MonitorServer._op_getMonitorState = IcePy.Operation('getMonitorState', Ice.OperationMode.Idempotent, Ice.OperationMode.Idempotent, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.grid.monitors._t_MonitorState, False, 0), (_M_ode._t_OdeFSError,))

    _M_ode.grid.monitors.MonitorServer = MonitorServer
    del MonitorServer

    _M_ode.grid.monitors.MonitorServerPrx = MonitorServerPrx
    del MonitorServerPrx

# End of module ode.grid.monitors

__name__ = 'ode.grid'

# End of module ode.grid

__name__ = 'ode'

# End of module ode
