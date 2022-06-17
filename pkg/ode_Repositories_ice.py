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
import ode_ModelF_ice
import ode_ServicesF_ice
import ode_System_ice
import ode_Collections_ice
import ode_ServerErrors_ice
import ode_cmd_API_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module ode.model
_M_ode.model = Ice.openModule('ode.model')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Included module Glacier2
_M_Glacier2 = Ice.openModule('Glacier2')

# Included module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')

# Included module ode.api
_M_ode.api = Ice.openModule('ode.api')

# Included module ode.grid
_M_ode.grid = Ice.openModule('ode.grid')

# Included module ode.cmd
_M_ode.cmd = Ice.openModule('ode.cmd')

# Start of module ode
__name__ = 'ode'

# Start of module ode.grid
__name__ = 'ode.grid'

if 'RepositoryException' not in _M_ode.grid.__dict__:
    _M_ode.grid.RepositoryException = Ice.createTempClass()
    class RepositoryException(_M_ode.ServerError):
        """
        Base repository exception.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::grid::RepositoryException'

    _M_ode.grid._t_RepositoryException = IcePy.defineException('::ode::grid::RepositoryException', RepositoryException, (), False, _M_ode._t_ServerError, ())
    RepositoryException._ice_type = _M_ode.grid._t_RepositoryException

    _M_ode.grid.RepositoryException = RepositoryException
    del RepositoryException

if 'FileDeleteException' not in _M_ode.grid.__dict__:
    _M_ode.grid.FileDeleteException = Ice.createTempClass()
    class FileDeleteException(_M_ode.grid.RepositoryException):
        """
        Specifies that a file with the given path has failed to
        be deleted from the file system.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.grid.RepositoryException.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::grid::FileDeleteException'

    _M_ode.grid._t_FileDeleteException = IcePy.defineException('::ode::grid::FileDeleteException', FileDeleteException, (), False, _M_ode.grid._t_RepositoryException, ())
    FileDeleteException._ice_type = _M_ode.grid._t_FileDeleteException

    _M_ode.grid.FileDeleteException = FileDeleteException
    del FileDeleteException

if 'UnregisteredFileException' not in _M_ode.grid.__dict__:
    _M_ode.grid.UnregisteredFileException = Ice.createTempClass()
    class UnregisteredFileException(_M_ode.grid.RepositoryException):
        """
        Specifies that a file is located at the given location
        that is not otherwise known by the repository. A
        subsequent call to {@code Repository.register} will create
        the given file. The mimetype field of the file may or
        may not be set. If it is set, clients are suggested to
        either omit the mimetype argument to the register method
        or to pass the same value.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', file=None):
            _M_ode.grid.RepositoryException.__init__(self, serverStackTrace, serverExceptionClass, message)
            self.file = file

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::grid::UnregisteredFileException'

    _M_ode.grid._t_UnregisteredFileException = IcePy.defineException('::ode::grid::UnregisteredFileException', UnregisteredFileException, (), False, _M_ode.grid._t_RepositoryException, (('file', (), _M_ode.model._t_OriginalFile, False, 0),))
    UnregisteredFileException._ice_type = _M_ode.grid._t_UnregisteredFileException

    _M_ode.grid.UnregisteredFileException = UnregisteredFileException
    del UnregisteredFileException

if 'Repository' not in _M_ode.grid.__dict__:
    _M_ode.grid.Repository = Ice.createTempClass()
    class Repository(Ice.Object):
        """
        Client-accessible interface representing a single mount point on the server-side.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.Repository:
                raise RuntimeError('ode.grid.Repository is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::Repository')

        def ice_id(self, current=None):
            return '::ode::grid::Repository'

        def ice_staticId():
            return '::ode::grid::Repository'
        ice_staticId = staticmethod(ice_staticId)

        def root(self, current=None):
            """
            Return the OriginalFile descriptor for this Repository. It will have
            the path of the repository's root on the underlying filesystem.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def mimetype(self, path, current=None):
            """
            Returns the best-guess mimetype for the given path.
            Arguments:
            path -- 
            current -- The Current object for the invocation.
            """
            pass

        def list(self, path, current=None):
            """
            Returns a set of strings naming the files and directories in
            the directory denoted by an abstract pathname.
            Arguments:
            path -- 
            current -- The Current object for the invocation.
            """
            pass

        def listFiles(self, path, current=None):
            """
            Returns an array of abstract pathname objects denoting the
            files in the directory denoted by an abstract pathname.  It
            is expected that at a minimum the ""name"", ""path"", ""size"" and
            ""mtime"" attributes will be present for each
            ode.model.OriginalFile instance.
            Arguments:
            path -- 
            current -- The Current object for the invocation.
            """
            pass

        def register(self, path, mimetype, current=None):
            """
            Create an OriginalFile in the database for the given path.
            Arguments:
            path -- 
            mimetype -- 
            current -- The Current object for the invocation.
            """
            pass

        def file(self, path, mode, current=None):
            """
            Returns a special RawFileStore which permits only the operations
            set out in the options string ""wb"", ""a+"", etc.
            FIXME: Initially only ""r"" and ""rw"" are supported as these are
            handled directly by RandomAccessFile and so don't break the current
            implementation.
            Any call to that tries to break the options will throw an
            ApiUsageException. If a file exists at the given path, a
            ValidationException will be thrown.
            Arguments:
            path -- 
            mode -- 
            current -- The Current object for the invocation.
            """
            pass

        def fileById(self, id, current=None):
            pass

        def fileExists(self, path, current=None):
            """
            Returns true if the file or path exists within the repository.
            In other words, if a call on `dirname path` to
            {@code listFiles} would return an object for this path.
            Arguments:
            path -- 
            current -- The Current object for the invocation.
            """
            pass

        def makeDir(self, path, parents, current=None):
            """
            Create a directory at the given path. If parents is true,
            then all preceding paths will be generated and no exception
            will be thrown if the directory already exists. Otherwise,
            all parent directories must exist in both the DB and on the
            filesystem and be readable.
            Arguments:
            path -- 
            parents -- 
            current -- The Current object for the invocation.
            """
            pass

        def treeList(self, path, current=None):
            """
            Similar to {@code list} but recursive and returns only
            primitive values for the file at each location. Guaranteed for
            each path is only the values id and mimetype.
            After a call to unwrap, the returned ode.RMap for a
            call to treeList("/user_1/dir0") might look something like:
            {@code
            {
            "/user_1/dir0/file1.txt" :
            {
            "id":10,
            "mimetype":
            "binary",
            "size": 10000L
            },
            "/user_1/dir0/dir1" :
            {
            "id": 100,
            "mimetype": "Directory",
            "size": 0L,
            "files":
            {
            "/user_1/dir0/dir1/file1indir.txt" :
            {
            "id": 1,
            "mimetype": "png",
            "size": 500
            }
            }
            }
            }
            }
            Arguments:
            path -- 
            current -- The Current object for the invocation.
            """
            pass

        def deletePaths(self, paths, recursively, force, current=None):
            """
            Delete several individual paths. Internally, this converts
            all of the paths into a single ode.cmd.Delete2 command
            and submits it.
            If a ""recursively"" is true, then directories will be searched
            and all of their contained files will be placed before them in
            the delete order. When the directory is removed from the database,
            it will removed from the filesystem if and only if it is empty.
            If ""recursively"" is false, then the delete will produce an error
            according to the ""force"" flag.
            If ""force"" is false, this method attempts the delete of all given
            paths in a single transaction, and any failure will cause the
            entire transaction to fail.
            If ""force"" is true, however, then all the other deletes will succeed.
            which could possibly leave dangling files within no longer extant
            directories.
            Arguments:
            paths -- 
            recursively -- 
            force -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_Repository)

        __repr__ = __str__

    _M_ode.grid.RepositoryPrx = Ice.createTempClass()
    class RepositoryPrx(Ice.ObjectPrx):

        """
        Return the OriginalFile descriptor for this Repository. It will have
        the path of the repository's root on the underlying filesystem.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def root(self, _ctx=None):
            return _M_ode.grid.Repository._op_root.invoke(self, ((), _ctx))

        """
        Return the OriginalFile descriptor for this Repository. It will have
        the path of the repository's root on the underlying filesystem.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_root(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_root.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Return the OriginalFile descriptor for this Repository. It will have
        the path of the repository's root on the underlying filesystem.
        Arguments:
        """
        def end_root(self, _r):
            return _M_ode.grid.Repository._op_root.end(self, _r)

        """
        Returns the best-guess mimetype for the given path.
        Arguments:
        path -- 
        _ctx -- The request context for the invocation.
        """
        def mimetype(self, path, _ctx=None):
            return _M_ode.grid.Repository._op_mimetype.invoke(self, ((path, ), _ctx))

        """
        Returns the best-guess mimetype for the given path.
        Arguments:
        path -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_mimetype(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_mimetype.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        Returns the best-guess mimetype for the given path.
        Arguments:
        path -- 
        """
        def end_mimetype(self, _r):
            return _M_ode.grid.Repository._op_mimetype.end(self, _r)

        """
        Returns a set of strings naming the files and directories in
        the directory denoted by an abstract pathname.
        Arguments:
        path -- 
        _ctx -- The request context for the invocation.
        """
        def list(self, path, _ctx=None):
            return _M_ode.grid.Repository._op_list.invoke(self, ((path, ), _ctx))

        """
        Returns a set of strings naming the files and directories in
        the directory denoted by an abstract pathname.
        Arguments:
        path -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_list(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_list.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        Returns a set of strings naming the files and directories in
        the directory denoted by an abstract pathname.
        Arguments:
        path -- 
        """
        def end_list(self, _r):
            return _M_ode.grid.Repository._op_list.end(self, _r)

        """
        Returns an array of abstract pathname objects denoting the
        files in the directory denoted by an abstract pathname.  It
        is expected that at a minimum the ""name"", ""path"", ""size"" and
        ""mtime"" attributes will be present for each
        ode.model.OriginalFile instance.
        Arguments:
        path -- 
        _ctx -- The request context for the invocation.
        """
        def listFiles(self, path, _ctx=None):
            return _M_ode.grid.Repository._op_listFiles.invoke(self, ((path, ), _ctx))

        """
        Returns an array of abstract pathname objects denoting the
        files in the directory denoted by an abstract pathname.  It
        is expected that at a minimum the ""name"", ""path"", ""size"" and
        ""mtime"" attributes will be present for each
        ode.model.OriginalFile instance.
        Arguments:
        path -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_listFiles(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_listFiles.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        Returns an array of abstract pathname objects denoting the
        files in the directory denoted by an abstract pathname.  It
        is expected that at a minimum the ""name"", ""path"", ""size"" and
        ""mtime"" attributes will be present for each
        ode.model.OriginalFile instance.
        Arguments:
        path -- 
        """
        def end_listFiles(self, _r):
            return _M_ode.grid.Repository._op_listFiles.end(self, _r)

        """
        Create an OriginalFile in the database for the given path.
        Arguments:
        path -- 
        mimetype -- 
        _ctx -- The request context for the invocation.
        """
        def register(self, path, mimetype, _ctx=None):
            return _M_ode.grid.Repository._op_register.invoke(self, ((path, mimetype), _ctx))

        """
        Create an OriginalFile in the database for the given path.
        Arguments:
        path -- 
        mimetype -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_register(self, path, mimetype, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_register.begin(self, ((path, mimetype), _response, _ex, _sent, _ctx))

        """
        Create an OriginalFile in the database for the given path.
        Arguments:
        path -- 
        mimetype -- 
        """
        def end_register(self, _r):
            return _M_ode.grid.Repository._op_register.end(self, _r)

        """
        Returns a special RawFileStore which permits only the operations
        set out in the options string ""wb"", ""a+"", etc.
        FIXME: Initially only ""r"" and ""rw"" are supported as these are
        handled directly by RandomAccessFile and so don't break the current
        implementation.
        Any call to that tries to break the options will throw an
        ApiUsageException. If a file exists at the given path, a
        ValidationException will be thrown.
        Arguments:
        path -- 
        mode -- 
        _ctx -- The request context for the invocation.
        """
        def file(self, path, mode, _ctx=None):
            return _M_ode.grid.Repository._op_file.invoke(self, ((path, mode), _ctx))

        """
        Returns a special RawFileStore which permits only the operations
        set out in the options string ""wb"", ""a+"", etc.
        FIXME: Initially only ""r"" and ""rw"" are supported as these are
        handled directly by RandomAccessFile and so don't break the current
        implementation.
        Any call to that tries to break the options will throw an
        ApiUsageException. If a file exists at the given path, a
        ValidationException will be thrown.
        Arguments:
        path -- 
        mode -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_file(self, path, mode, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_file.begin(self, ((path, mode), _response, _ex, _sent, _ctx))

        """
        Returns a special RawFileStore which permits only the operations
        set out in the options string ""wb"", ""a+"", etc.
        FIXME: Initially only ""r"" and ""rw"" are supported as these are
        handled directly by RandomAccessFile and so don't break the current
        implementation.
        Any call to that tries to break the options will throw an
        ApiUsageException. If a file exists at the given path, a
        ValidationException will be thrown.
        Arguments:
        path -- 
        mode -- 
        """
        def end_file(self, _r):
            return _M_ode.grid.Repository._op_file.end(self, _r)

        def fileById(self, id, _ctx=None):
            return _M_ode.grid.Repository._op_fileById.invoke(self, ((id, ), _ctx))

        def begin_fileById(self, id, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_fileById.begin(self, ((id, ), _response, _ex, _sent, _ctx))

        def end_fileById(self, _r):
            return _M_ode.grid.Repository._op_fileById.end(self, _r)

        """
        Returns true if the file or path exists within the repository.
        In other words, if a call on `dirname path` to
        {@code listFiles} would return an object for this path.
        Arguments:
        path -- 
        _ctx -- The request context for the invocation.
        """
        def fileExists(self, path, _ctx=None):
            return _M_ode.grid.Repository._op_fileExists.invoke(self, ((path, ), _ctx))

        """
        Returns true if the file or path exists within the repository.
        In other words, if a call on `dirname path` to
        {@code listFiles} would return an object for this path.
        Arguments:
        path -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_fileExists(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_fileExists.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        Returns true if the file or path exists within the repository.
        In other words, if a call on `dirname path` to
        {@code listFiles} would return an object for this path.
        Arguments:
        path -- 
        """
        def end_fileExists(self, _r):
            return _M_ode.grid.Repository._op_fileExists.end(self, _r)

        """
        Create a directory at the given path. If parents is true,
        then all preceding paths will be generated and no exception
        will be thrown if the directory already exists. Otherwise,
        all parent directories must exist in both the DB and on the
        filesystem and be readable.
        Arguments:
        path -- 
        parents -- 
        _ctx -- The request context for the invocation.
        """
        def makeDir(self, path, parents, _ctx=None):
            return _M_ode.grid.Repository._op_makeDir.invoke(self, ((path, parents), _ctx))

        """
        Create a directory at the given path. If parents is true,
        then all preceding paths will be generated and no exception
        will be thrown if the directory already exists. Otherwise,
        all parent directories must exist in both the DB and on the
        filesystem and be readable.
        Arguments:
        path -- 
        parents -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_makeDir(self, path, parents, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_makeDir.begin(self, ((path, parents), _response, _ex, _sent, _ctx))

        """
        Create a directory at the given path. If parents is true,
        then all preceding paths will be generated and no exception
        will be thrown if the directory already exists. Otherwise,
        all parent directories must exist in both the DB and on the
        filesystem and be readable.
        Arguments:
        path -- 
        parents -- 
        """
        def end_makeDir(self, _r):
            return _M_ode.grid.Repository._op_makeDir.end(self, _r)

        """
        Similar to {@code list} but recursive and returns only
        primitive values for the file at each location. Guaranteed for
        each path is only the values id and mimetype.
        After a call to unwrap, the returned ode.RMap for a
        call to treeList("/user_1/dir0") might look something like:
        {@code
        {
        "/user_1/dir0/file1.txt" :
        {
        "id":10,
        "mimetype":
        "binary",
        "size": 10000L
        },
        "/user_1/dir0/dir1" :
        {
        "id": 100,
        "mimetype": "Directory",
        "size": 0L,
        "files":
        {
        "/user_1/dir0/dir1/file1indir.txt" :
        {
        "id": 1,
        "mimetype": "png",
        "size": 500
        }
        }
        }
        }
        }
        Arguments:
        path -- 
        _ctx -- The request context for the invocation.
        """
        def treeList(self, path, _ctx=None):
            return _M_ode.grid.Repository._op_treeList.invoke(self, ((path, ), _ctx))

        """
        Similar to {@code list} but recursive and returns only
        primitive values for the file at each location. Guaranteed for
        each path is only the values id and mimetype.
        After a call to unwrap, the returned ode.RMap for a
        call to treeList("/user_1/dir0") might look something like:
        {@code
        {
        "/user_1/dir0/file1.txt" :
        {
        "id":10,
        "mimetype":
        "binary",
        "size": 10000L
        },
        "/user_1/dir0/dir1" :
        {
        "id": 100,
        "mimetype": "Directory",
        "size": 0L,
        "files":
        {
        "/user_1/dir0/dir1/file1indir.txt" :
        {
        "id": 1,
        "mimetype": "png",
        "size": 500
        }
        }
        }
        }
        }
        Arguments:
        path -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_treeList(self, path, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_treeList.begin(self, ((path, ), _response, _ex, _sent, _ctx))

        """
        Similar to {@code list} but recursive and returns only
        primitive values for the file at each location. Guaranteed for
        each path is only the values id and mimetype.
        After a call to unwrap, the returned ode.RMap for a
        call to treeList("/user_1/dir0") might look something like:
        {@code
        {
        "/user_1/dir0/file1.txt" :
        {
        "id":10,
        "mimetype":
        "binary",
        "size": 10000L
        },
        "/user_1/dir0/dir1" :
        {
        "id": 100,
        "mimetype": "Directory",
        "size": 0L,
        "files":
        {
        "/user_1/dir0/dir1/file1indir.txt" :
        {
        "id": 1,
        "mimetype": "png",
        "size": 500
        }
        }
        }
        }
        }
        Arguments:
        path -- 
        """
        def end_treeList(self, _r):
            return _M_ode.grid.Repository._op_treeList.end(self, _r)

        """
        Delete several individual paths. Internally, this converts
        all of the paths into a single ode.cmd.Delete2 command
        and submits it.
        If a ""recursively"" is true, then directories will be searched
        and all of their contained files will be placed before them in
        the delete order. When the directory is removed from the database,
        it will removed from the filesystem if and only if it is empty.
        If ""recursively"" is false, then the delete will produce an error
        according to the ""force"" flag.
        If ""force"" is false, this method attempts the delete of all given
        paths in a single transaction, and any failure will cause the
        entire transaction to fail.
        If ""force"" is true, however, then all the other deletes will succeed.
        which could possibly leave dangling files within no longer extant
        directories.
        Arguments:
        paths -- 
        recursively -- 
        force -- 
        _ctx -- The request context for the invocation.
        """
        def deletePaths(self, paths, recursively, force, _ctx=None):
            return _M_ode.grid.Repository._op_deletePaths.invoke(self, ((paths, recursively, force), _ctx))

        """
        Delete several individual paths. Internally, this converts
        all of the paths into a single ode.cmd.Delete2 command
        and submits it.
        If a ""recursively"" is true, then directories will be searched
        and all of their contained files will be placed before them in
        the delete order. When the directory is removed from the database,
        it will removed from the filesystem if and only if it is empty.
        If ""recursively"" is false, then the delete will produce an error
        according to the ""force"" flag.
        If ""force"" is false, this method attempts the delete of all given
        paths in a single transaction, and any failure will cause the
        entire transaction to fail.
        If ""force"" is true, however, then all the other deletes will succeed.
        which could possibly leave dangling files within no longer extant
        directories.
        Arguments:
        paths -- 
        recursively -- 
        force -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_deletePaths(self, paths, recursively, force, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.Repository._op_deletePaths.begin(self, ((paths, recursively, force), _response, _ex, _sent, _ctx))

        """
        Delete several individual paths. Internally, this converts
        all of the paths into a single ode.cmd.Delete2 command
        and submits it.
        If a ""recursively"" is true, then directories will be searched
        and all of their contained files will be placed before them in
        the delete order. When the directory is removed from the database,
        it will removed from the filesystem if and only if it is empty.
        If ""recursively"" is false, then the delete will produce an error
        according to the ""force"" flag.
        If ""force"" is false, this method attempts the delete of all given
        paths in a single transaction, and any failure will cause the
        entire transaction to fail.
        If ""force"" is true, however, then all the other deletes will succeed.
        which could possibly leave dangling files within no longer extant
        directories.
        Arguments:
        paths -- 
        recursively -- 
        force -- 
        """
        def end_deletePaths(self, _r):
            return _M_ode.grid.Repository._op_deletePaths.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.RepositoryPrx.ice_checkedCast(proxy, '::ode::grid::Repository', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.RepositoryPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::Repository'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_RepositoryPrx = IcePy.defineProxy('::ode::grid::Repository', RepositoryPrx)

    _M_ode.grid._t_Repository = IcePy.defineClass('::ode::grid::Repository', Repository, -1, (), True, False, None, (), ())
    Repository._ice_type = _M_ode.grid._t_Repository

    Repository._op_root = IcePy.Operation('root', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFile, False, 0), (_M_ode._t_ServerError,))
    Repository._op_mimetype = IcePy.Operation('mimetype', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))
    Repository._op_list = IcePy.Operation('list', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_StringSet, False, 0), (_M_ode._t_ServerError,))
    Repository._op_listFiles = IcePy.Operation('listFiles', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode.api._t_OriginalFileList, False, 0), (_M_ode._t_ServerError,))
    Repository._op_register = IcePy.Operation('register', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0), ((), _M_ode._t_RString, False, 0)), (), ((), _M_ode.model._t_OriginalFile, False, 0), (_M_ode._t_ServerError,))
    Repository._op_file = IcePy.Operation('file', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_string, False, 0)), (), ((), _M_ode.api._t_RawFileStorePrx, False, 0), (_M_ode._t_ServerError,))
    Repository._op_fileById = IcePy.Operation('fileById', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_long, False, 0),), (), ((), _M_ode.api._t_RawFileStorePrx, False, 0), (_M_ode._t_ServerError,))
    Repository._op_fileExists = IcePy.Operation('fileExists', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), IcePy._t_bool, False, 0), (_M_ode._t_ServerError,))
    Repository._op_makeDir = IcePy.Operation('makeDir', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0), ((), IcePy._t_bool, False, 0)), (), None, (_M_ode._t_ServerError,))
    Repository._op_treeList = IcePy.Operation('treeList', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_string, False, 0),), (), ((), _M_ode._t_RMap, False, 0), (_M_ode._t_ServerError,))
    Repository._op_deletePaths = IcePy.Operation('deletePaths', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_StringArray, False, 0), ((), IcePy._t_bool, False, 0), ((), IcePy._t_bool, False, 0)), (), ((), _M_ode.cmd._t_HandlePrx, False, 0), (_M_ode._t_ServerError,))

    _M_ode.grid.Repository = Repository
    del Repository

    _M_ode.grid.RepositoryPrx = RepositoryPrx
    del RepositoryPrx

if 'ImportLocation' not in _M_ode.grid.__dict__:
    _M_ode.grid.ImportLocation = Ice.createTempClass()
    class ImportLocation(Ice.Object):
        """
        Returned by {@code ManagedRepository.importFileset} with
        the information needed to proceed with an FS import.
        For the examples that follow, assume that the used
        files passed to importFileset were:
        /Users/jack/Documents/Data/Experiment-1/1.dv
        /Users/jack/Documents/Data/Experiment-1/1.dv.log
        /Users/jack/Documents/Data/Experiment-2/2.dv
        /Users/jack/Documents/Data/Experiment-2/2.dv.log
        Members:
        sharedPath -- The shared base of all the paths passed to
        the server.
        omittedLevels -- Number of directories which have been omitted
        from the original paths passed to the server.
        usedFiles -- Parsed string names which should be used by the
        clients during upload. This array will be of the
        same length as the argument passed to
        {@code ManagedRepository.importFileset} but will have
        shortened paths.
        Experiment/1.dv
        Experiment/1.dv.log
        directory -- Represents the directory to which all files
        will be uploaded.
        """
        def __init__(self, sharedPath='', omittedLevels=0, usedFiles=None, directory=None):
            self.sharedPath = sharedPath
            self.omittedLevels = omittedLevels
            self.usedFiles = usedFiles
            self.directory = directory

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::ImportLocation')

        def ice_id(self, current=None):
            return '::ode::grid::ImportLocation'

        def ice_staticId():
            return '::ode::grid::ImportLocation'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ImportLocation)

        __repr__ = __str__

    _M_ode.grid.ImportLocationPrx = Ice.createTempClass()
    class ImportLocationPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ImportLocationPrx.ice_checkedCast(proxy, '::ode::grid::ImportLocation', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ImportLocationPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ImportLocation'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ImportLocationPrx = IcePy.defineProxy('::ode::grid::ImportLocation', ImportLocationPrx)

    _M_ode.grid._t_ImportLocation = IcePy.declareClass('::ode::grid::ImportLocation')

    _M_ode.grid._t_ImportLocation = IcePy.defineClass('::ode::grid::ImportLocation', ImportLocation, -1, (), False, False, None, (), (
        ('sharedPath', (), IcePy._t_string, False, 0),
        ('omittedLevels', (), IcePy._t_int, False, 0),
        ('usedFiles', (), _M_ode.api._t_StringSet, False, 0),
        ('directory', (), _M_ode.model._t_OriginalFile, False, 0)
    ))
    ImportLocation._ice_type = _M_ode.grid._t_ImportLocation

    _M_ode.grid.ImportLocation = ImportLocation
    del ImportLocation

    _M_ode.grid.ImportLocationPrx = ImportLocationPrx
    del ImportLocationPrx

if 'ImportSettings' not in _M_ode.grid.__dict__:
    _M_ode.grid.ImportSettings = Ice.createTempClass()
    class ImportSettings(Ice.Object):
        """
        User configuration options. These are likely set in the UI
        before the import is initiated.
        Members:
        userSpecifiedTarget -- The container which this object should be added to.
        userSpecifiedName -- Custom name suggested by the user.
        userSpecifiedDescription -- Custom description suggested by the user.
        userSpecifiedPixels -- User choice of pixels sizes.
        userSpecifiedAnnotationList -- Annotations that the user
        doThumbnails -- Whether or not the thumbnailing action should be performed.
        noStatsInfo -- Whether we are to disable StatsInfo population.
        checksumAlgorithm -- User choice of checksum algorithm for verifying upload.
        """
        def __init__(self, userSpecifiedTarget=None, userSpecifiedName=None, userSpecifiedDescription=None, userSpecifiedPixels=None, userSpecifiedAnnotationList=None, doThumbnails=None, noStatsInfo=None, checksumAlgorithm=None):
            self.userSpecifiedTarget = userSpecifiedTarget
            self.userSpecifiedName = userSpecifiedName
            self.userSpecifiedDescription = userSpecifiedDescription
            self.userSpecifiedPixels = userSpecifiedPixels
            self.userSpecifiedAnnotationList = userSpecifiedAnnotationList
            self.doThumbnails = doThumbnails
            self.noStatsInfo = noStatsInfo
            self.checksumAlgorithm = checksumAlgorithm

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::ImportSettings')

        def ice_id(self, current=None):
            return '::ode::grid::ImportSettings'

        def ice_staticId():
            return '::ode::grid::ImportSettings'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ImportSettings)

        __repr__ = __str__

    _M_ode.grid.ImportSettingsPrx = Ice.createTempClass()
    class ImportSettingsPrx(Ice.ObjectPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ImportSettingsPrx.ice_checkedCast(proxy, '::ode::grid::ImportSettings', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ImportSettingsPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ImportSettings'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ImportSettingsPrx = IcePy.defineProxy('::ode::grid::ImportSettings', ImportSettingsPrx)

    _M_ode.grid._t_ImportSettings = IcePy.declareClass('::ode::grid::ImportSettings')

    _M_ode.grid._t_ImportSettings = IcePy.defineClass('::ode::grid::ImportSettings', ImportSettings, -1, (), False, False, None, (), (
        ('userSpecifiedTarget', (), _M_ode.model._t_IObject, False, 0),
        ('userSpecifiedName', (), _M_ode._t_RString, False, 0),
        ('userSpecifiedDescription', (), _M_ode._t_RString, False, 0),
        ('userSpecifiedPixels', (), _M_ode.api._t_DoubleArray, False, 0),
        ('userSpecifiedAnnotationList', (), _M_ode.api._t_AnnotationList, False, 0),
        ('doThumbnails', (), _M_ode._t_RBool, False, 0),
        ('noStatsInfo', (), _M_ode._t_RBool, False, 0),
        ('checksumAlgorithm', (), _M_ode.model._t_ChecksumAlgorithm, False, 0)
    ))
    ImportSettings._ice_type = _M_ode.grid._t_ImportSettings

    _M_ode.grid.ImportSettings = ImportSettings
    del ImportSettings

    _M_ode.grid.ImportSettingsPrx = ImportSettingsPrx
    del ImportSettingsPrx

if 'ImportProcess' not in _M_ode.grid.__dict__:
    _M_ode.grid.ImportProcess = Ice.createTempClass()
    class ImportProcess(_M_ode.api.StatefulServiceInterface):
        """
        User configuration options. These are likely set in the UI
        before the import is initiated.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.ImportProcess:
                raise RuntimeError('ode.grid.ImportProcess is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::api::ServiceInterface', '::ode::api::StatefulServiceInterface', '::ode::grid::ImportProcess')

        def ice_id(self, current=None):
            return '::ode::grid::ImportProcess'

        def ice_staticId():
            return '::ode::grid::ImportProcess'
        ice_staticId = staticmethod(ice_staticId)

        def getUploader(self, i, current=None):
            """
            Step 1: Returns a RawFileStore that can be used to upload one of
            the used files. The index is the same as the used file listed in
            {@code ImportLocation}. {@code ode.api.RawFileStore.close}
            should be called once all data has been transferred. If the
            file must be re-written, call {@code getUploader} with the
            same index again. Once all uploads have been completed,
            {@code verifyUpload} should be called to initiate background
            processing
            Arguments:
            i -- 
            current -- The Current object for the invocation.
            """
            pass

        def verifyUpload(self, hash, current=None):
            """
            Step 2: Passes a set of client-side calculated hashes to the
            server for verifying that all of the files were correctly
            uploaded. If this passes then a ode.cmd.Handle
            proxy is returned, which completes all the necessary import
            steps. A successful import will return an
            {@code ImportResponse}. Otherwise, some ode.cmd.ERR
            will be returned.
            Arguments:
            hash -- 
            current -- The Current object for the invocation.
            """
            pass

        def getUploadOffset(self, i, current=None):
            """
            In case an upload must be resumed, this provides the
            location of the last successful upload.
            Arguments:
            i -- 
            current -- The Current object for the invocation.
            """
            pass

        def getHandle(self, current=None):
            """
            Reacquire the handle which was returned by
            {@code verifyUpload}. This is useful in case a new
            client is re-attaching to a running import.
            From the ode.cmd.Handle instance, the
            original {@code ImportRequest} can also be found.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def getImportSettings(self, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ImportProcess)

        __repr__ = __str__

    _M_ode.grid.ImportProcessPrx = Ice.createTempClass()
    class ImportProcessPrx(_M_ode.api.StatefulServiceInterfacePrx):

        """
        Step 1: Returns a RawFileStore that can be used to upload one of
        the used files. The index is the same as the used file listed in
        {@code ImportLocation}. {@code ode.api.RawFileStore.close}
        should be called once all data has been transferred. If the
        file must be re-written, call {@code getUploader} with the
        same index again. Once all uploads have been completed,
        {@code verifyUpload} should be called to initiate background
        processing
        Arguments:
        i -- 
        _ctx -- The request context for the invocation.
        """
        def getUploader(self, i, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getUploader.invoke(self, ((i, ), _ctx))

        """
        Step 1: Returns a RawFileStore that can be used to upload one of
        the used files. The index is the same as the used file listed in
        {@code ImportLocation}. {@code ode.api.RawFileStore.close}
        should be called once all data has been transferred. If the
        file must be re-written, call {@code getUploader} with the
        same index again. Once all uploads have been completed,
        {@code verifyUpload} should be called to initiate background
        processing
        Arguments:
        i -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getUploader(self, i, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getUploader.begin(self, ((i, ), _response, _ex, _sent, _ctx))

        """
        Step 1: Returns a RawFileStore that can be used to upload one of
        the used files. The index is the same as the used file listed in
        {@code ImportLocation}. {@code ode.api.RawFileStore.close}
        should be called once all data has been transferred. If the
        file must be re-written, call {@code getUploader} with the
        same index again. Once all uploads have been completed,
        {@code verifyUpload} should be called to initiate background
        processing
        Arguments:
        i -- 
        """
        def end_getUploader(self, _r):
            return _M_ode.grid.ImportProcess._op_getUploader.end(self, _r)

        """
        Step 2: Passes a set of client-side calculated hashes to the
        server for verifying that all of the files were correctly
        uploaded. If this passes then a ode.cmd.Handle
        proxy is returned, which completes all the necessary import
        steps. A successful import will return an
        {@code ImportResponse}. Otherwise, some ode.cmd.ERR
        will be returned.
        Arguments:
        hash -- 
        _ctx -- The request context for the invocation.
        """
        def verifyUpload(self, hash, _ctx=None):
            return _M_ode.grid.ImportProcess._op_verifyUpload.invoke(self, ((hash, ), _ctx))

        """
        Step 2: Passes a set of client-side calculated hashes to the
        server for verifying that all of the files were correctly
        uploaded. If this passes then a ode.cmd.Handle
        proxy is returned, which completes all the necessary import
        steps. A successful import will return an
        {@code ImportResponse}. Otherwise, some ode.cmd.ERR
        will be returned.
        Arguments:
        hash -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_verifyUpload(self, hash, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ImportProcess._op_verifyUpload.begin(self, ((hash, ), _response, _ex, _sent, _ctx))

        """
        Step 2: Passes a set of client-side calculated hashes to the
        server for verifying that all of the files were correctly
        uploaded. If this passes then a ode.cmd.Handle
        proxy is returned, which completes all the necessary import
        steps. A successful import will return an
        {@code ImportResponse}. Otherwise, some ode.cmd.ERR
        will be returned.
        Arguments:
        hash -- 
        """
        def end_verifyUpload(self, _r):
            return _M_ode.grid.ImportProcess._op_verifyUpload.end(self, _r)

        """
        In case an upload must be resumed, this provides the
        location of the last successful upload.
        Arguments:
        i -- 
        _ctx -- The request context for the invocation.
        """
        def getUploadOffset(self, i, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getUploadOffset.invoke(self, ((i, ), _ctx))

        """
        In case an upload must be resumed, this provides the
        location of the last successful upload.
        Arguments:
        i -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getUploadOffset(self, i, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getUploadOffset.begin(self, ((i, ), _response, _ex, _sent, _ctx))

        """
        In case an upload must be resumed, this provides the
        location of the last successful upload.
        Arguments:
        i -- 
        """
        def end_getUploadOffset(self, _r):
            return _M_ode.grid.ImportProcess._op_getUploadOffset.end(self, _r)

        """
        Reacquire the handle which was returned by
        {@code verifyUpload}. This is useful in case a new
        client is re-attaching to a running import.
        From the ode.cmd.Handle instance, the
        original {@code ImportRequest} can also be found.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def getHandle(self, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getHandle.invoke(self, ((), _ctx))

        """
        Reacquire the handle which was returned by
        {@code verifyUpload}. This is useful in case a new
        client is re-attaching to a running import.
        From the ode.cmd.Handle instance, the
        original {@code ImportRequest} can also be found.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_getHandle(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getHandle.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Reacquire the handle which was returned by
        {@code verifyUpload}. This is useful in case a new
        client is re-attaching to a running import.
        From the ode.cmd.Handle instance, the
        original {@code ImportRequest} can also be found.
        Arguments:
        """
        def end_getHandle(self, _r):
            return _M_ode.grid.ImportProcess._op_getHandle.end(self, _r)

        def getImportSettings(self, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getImportSettings.invoke(self, ((), _ctx))

        def begin_getImportSettings(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ImportProcess._op_getImportSettings.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getImportSettings(self, _r):
            return _M_ode.grid.ImportProcess._op_getImportSettings.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ImportProcessPrx.ice_checkedCast(proxy, '::ode::grid::ImportProcess', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ImportProcessPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ImportProcess'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ImportProcessPrx = IcePy.defineProxy('::ode::grid::ImportProcess', ImportProcessPrx)

    _M_ode.grid._t_ImportProcess = IcePy.defineClass('::ode::grid::ImportProcess', ImportProcess, -1, (), True, False, None, (_M_ode.api._t_StatefulServiceInterface,), ())
    ImportProcess._ice_type = _M_ode.grid._t_ImportProcess

    ImportProcess._op_getUploader = IcePy.Operation('getUploader', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), _M_ode.api._t_RawFileStorePrx, False, 0), (_M_ode._t_ServerError,))
    ImportProcess._op_verifyUpload = IcePy.Operation('verifyUpload', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_StringSet, False, 0),), (), ((), _M_ode.cmd._t_HandlePrx, False, 0), (_M_ode._t_ServerError,))
    ImportProcess._op_getUploadOffset = IcePy.Operation('getUploadOffset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), IcePy._t_int, False, 0),), (), ((), IcePy._t_long, False, 0), (_M_ode._t_ServerError,))
    ImportProcess._op_getHandle = IcePy.Operation('getHandle', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.cmd._t_HandlePrx, False, 0), (_M_ode._t_ServerError,))
    ImportProcess._op_getImportSettings = IcePy.Operation('getImportSettings', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.grid._t_ImportSettings, False, 0), ())

    _M_ode.grid.ImportProcess = ImportProcess
    del ImportProcess

    _M_ode.grid.ImportProcessPrx = ImportProcessPrx
    del ImportProcessPrx

if '_t_ImportProcessList' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_ImportProcessList = IcePy.defineSequence('::ode::grid::ImportProcessList', (), _M_ode.grid._t_ImportProcessPrx)

if 'ImportRequest' not in _M_ode.grid.__dict__:
    _M_ode.grid.ImportRequest = Ice.createTempClass()
    class ImportRequest(_M_ode.cmd.Request):
        """
        Command object which will be used to create
        the ode.cmd.Handle instances passed
        back by the {@code ImportProcess}.
        Members:
        clientUuid -- Lookup value for the session that import is taking
        part in.
        repoUuid -- Repository which is responsible for this import.
        All files which are uploaded will be available
        from it.
        process -- Proxy of the process which this request
        will be running in. This value will be
        filled in for possible later re-use, but
        is not read by the server.
        activity -- Activity that this will be filling
        out in the database. This always points to a
        ode.model.MetadataImportJob which is the
        first server-side phase after the
        ode.model.UploadJob.
        settings -- ImportSettings which are provided by the
        client on the call to {@code ManagedRepository.importFileset}.
        location -- ImportLocation which is calculated during
        the call to {@code ManagedRepository.importFileset}.
        logFile -- ode.model.OriginalFile object representing the import log file.
        """
        def __init__(self, clientUuid='', repoUuid='', process=None, activity=None, settings=None, location=None, logFile=None):
            _M_ode.cmd.Request.__init__(self)
            self.clientUuid = clientUuid
            self.repoUuid = repoUuid
            self.process = process
            self.activity = activity
            self.settings = settings
            self.location = location
            self.logFile = logFile

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Request', '::ode::grid::ImportRequest')

        def ice_id(self, current=None):
            return '::ode::grid::ImportRequest'

        def ice_staticId():
            return '::ode::grid::ImportRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ImportRequest)

        __repr__ = __str__

    _M_ode.grid.ImportRequestPrx = Ice.createTempClass()
    class ImportRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ImportRequestPrx.ice_checkedCast(proxy, '::ode::grid::ImportRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ImportRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ImportRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ImportRequestPrx = IcePy.defineProxy('::ode::grid::ImportRequest', ImportRequestPrx)

    _M_ode.grid._t_ImportRequest = IcePy.declareClass('::ode::grid::ImportRequest')

    _M_ode.grid._t_ImportRequest = IcePy.defineClass('::ode::grid::ImportRequest', ImportRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('clientUuid', (), IcePy._t_string, False, 0),
        ('repoUuid', (), IcePy._t_string, False, 0),
        ('process', (), _M_ode.grid._t_ImportProcessPrx, False, 0),
        ('activity', (), _M_ode.model._t_FilesetJobLink, False, 0),
        ('settings', (), _M_ode.grid._t_ImportSettings, False, 0),
        ('location', (), _M_ode.grid._t_ImportLocation, False, 0),
        ('logFile', (), _M_ode.model._t_OriginalFile, False, 0)
    ))
    ImportRequest._ice_type = _M_ode.grid._t_ImportRequest

    _M_ode.grid.ImportRequest = ImportRequest
    del ImportRequest

    _M_ode.grid.ImportRequestPrx = ImportRequestPrx
    del ImportRequestPrx

if 'ImportResponse' not in _M_ode.grid.__dict__:
    _M_ode.grid.ImportResponse = Ice.createTempClass()
    class ImportResponse(_M_ode.cmd.OK):
        """
        Successful response returned from execution
        of ImportRequest. This is the simplest way
        to return the results, but is likely not the
        overall best strategy.
        Members:
        pixels -- 
        objects -- Top-level ODE-XML objects which are created
        during the import. This will not contain any
        pixels which were imported, but images, plates,
        etc. which may be useful for user feedback.
        """
        def __init__(self, pixels=None, objects=None):
            _M_ode.cmd.OK.__init__(self)
            self.pixels = pixels
            self.objects = objects

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::OK', '::ode::cmd::Response', '::ode::grid::ImportResponse')

        def ice_id(self, current=None):
            return '::ode::grid::ImportResponse'

        def ice_staticId():
            return '::ode::grid::ImportResponse'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ImportResponse)

        __repr__ = __str__

    _M_ode.grid.ImportResponsePrx = Ice.createTempClass()
    class ImportResponsePrx(_M_ode.cmd.OKPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ImportResponsePrx.ice_checkedCast(proxy, '::ode::grid::ImportResponse', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ImportResponsePrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ImportResponse'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ImportResponsePrx = IcePy.defineProxy('::ode::grid::ImportResponse', ImportResponsePrx)

    _M_ode.grid._t_ImportResponse = IcePy.declareClass('::ode::grid::ImportResponse')

    _M_ode.grid._t_ImportResponse = IcePy.defineClass('::ode::grid::ImportResponse', ImportResponse, -1, (), False, False, _M_ode.cmd._t_OK, (), (
        ('pixels', (), _M_ode.api._t_PixelsList, False, 0),
        ('objects', (), _M_ode.api._t_IObjectList, False, 0)
    ))
    ImportResponse._ice_type = _M_ode.grid._t_ImportResponse

    _M_ode.grid.ImportResponse = ImportResponse
    del ImportResponse

    _M_ode.grid.ImportResponsePrx = ImportResponsePrx
    del ImportResponsePrx

if 'ManagedRepository' not in _M_ode.grid.__dict__:
    _M_ode.grid.ManagedRepository = Ice.createTempClass()
    class ManagedRepository(_M_ode.grid.Repository):
        """
        FS-enabled repository which can convert uploaded files
        into Images by using Bio-Formats to import them.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.ManagedRepository:
                raise RuntimeError('ode.grid.ManagedRepository is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::ManagedRepository', '::ode::grid::Repository')

        def ice_id(self, current=None):
            return '::ode::grid::ManagedRepository'

        def ice_staticId():
            return '::ode::grid::ManagedRepository'
        ice_staticId = staticmethod(ice_staticId)

        def importFileset(self, fs, settings, current=None):
            """
            Returns an ImportProcess which can be used to upload files.
            On ImportProcess#verifyUpload, an ode.cmd.Handle will be
            returned which can be watched for knowing when the server-side import
            is complete.
            Client paths set in the fileset entries must /-separate their components.
            Once the upload is complete, the ImportProcess must be closed.
            Once ode.cmd.Handle#getResponse returns a non-null value, the
            handle instance can and must be closed.
            Arguments:
            fs -- 
            settings -- 
            current -- The Current object for the invocation.
            """
            pass

        def importPaths(self, filePaths, current=None):
            """
            For clients without access to Bio-Formats, the simplified
            {@code importPaths} method allows passing solely the absolute
            path of the files to be uploaded (no directories) and all
            configuration happens server-side. Much of the functionality
            provided via ode.model.Fileset and
            ode.grid.ImportSettings is of course lost.
            Arguments:
            filePaths -- 
            current -- The Current object for the invocation.
            """
            pass

        def listImports(self, current=None):
            """
            List imports that are currently running in this importer.
            These will be limited based on user/group membership for
            the ode.model.Fileset object which is being created
            by the import. If the user has write permissions for the
            fileset, then the import will be included.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def listChecksumAlgorithms(self, current=None):
            """
            Return the list of checksum algorithms supported by this repository
            for verifying the integrity of uploaded files.
            They are named as ""algorithm-integer"",
            integer being the bit width of the resulting hash code.
            It is possible for the same algorithm to be offered with
            different bit widths.
            They are listed in descending order of preference,
            as set by the server administrator, and any of them may
            be specified for ImportSettings#checksumAlgorithm.
            Arguments:
            current -- The Current object for the invocation.
            """
            pass

        def suggestChecksumAlgorithm(self, supported, current=None):
            """
            Suggest a checksum algorithm to use for
            ImportSettings#checksumAlgorithm according to the
            preferences set by the server administrator. Provide a
            list of the algorithms supported by the client, and the
            server will report which of them is most preferred by
            the server, or return null if none of them are supported.
            Arguments:
            supported -- 
            current -- The Current object for the invocation.
            """
            pass

        def verifyChecksums(self, ids, current=None):
            """
            Verify the checksum for the original files identified by
            the given IDs.
            The files must be in this repository.
            Returns the IDs of the original files whose checksums
            do not match the file on disk.
            Arguments:
            ids -- 
            current -- The Current object for the invocation.
            """
            pass

        def setChecksumAlgorithm(self, hasher, ids, current=None):
            """
            Set the checksum algorithm for the original files identified
            by the given IDs and calculate their checksum accordingly.
            The files must be in this repository.
            Existing checksums are checked before being changed.
            If a checksum does not match, ServerError will be thrown;
            in this case some other files may already have had their
            checksum algorithm set.
            Returns the IDs of the original files that did not already
            have a checksum set for the given algorithm.
            Arguments:
            hasher -- 
            ids -- 
            current -- The Current object for the invocation.
            """
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_ManagedRepository)

        __repr__ = __str__

    _M_ode.grid.ManagedRepositoryPrx = Ice.createTempClass()
    class ManagedRepositoryPrx(_M_ode.grid.RepositoryPrx):

        """
        Returns an ImportProcess which can be used to upload files.
        On ImportProcess#verifyUpload, an ode.cmd.Handle will be
        returned which can be watched for knowing when the server-side import
        is complete.
        Client paths set in the fileset entries must /-separate their components.
        Once the upload is complete, the ImportProcess must be closed.
        Once ode.cmd.Handle#getResponse returns a non-null value, the
        handle instance can and must be closed.
        Arguments:
        fs -- 
        settings -- 
        _ctx -- The request context for the invocation.
        """
        def importFileset(self, fs, settings, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_importFileset.invoke(self, ((fs, settings), _ctx))

        """
        Returns an ImportProcess which can be used to upload files.
        On ImportProcess#verifyUpload, an ode.cmd.Handle will be
        returned which can be watched for knowing when the server-side import
        is complete.
        Client paths set in the fileset entries must /-separate their components.
        Once the upload is complete, the ImportProcess must be closed.
        Once ode.cmd.Handle#getResponse returns a non-null value, the
        handle instance can and must be closed.
        Arguments:
        fs -- 
        settings -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_importFileset(self, fs, settings, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_importFileset.begin(self, ((fs, settings), _response, _ex, _sent, _ctx))

        """
        Returns an ImportProcess which can be used to upload files.
        On ImportProcess#verifyUpload, an ode.cmd.Handle will be
        returned which can be watched for knowing when the server-side import
        is complete.
        Client paths set in the fileset entries must /-separate their components.
        Once the upload is complete, the ImportProcess must be closed.
        Once ode.cmd.Handle#getResponse returns a non-null value, the
        handle instance can and must be closed.
        Arguments:
        fs -- 
        settings -- 
        """
        def end_importFileset(self, _r):
            return _M_ode.grid.ManagedRepository._op_importFileset.end(self, _r)

        """
        For clients without access to Bio-Formats, the simplified
        {@code importPaths} method allows passing solely the absolute
        path of the files to be uploaded (no directories) and all
        configuration happens server-side. Much of the functionality
        provided via ode.model.Fileset and
        ode.grid.ImportSettings is of course lost.
        Arguments:
        filePaths -- 
        _ctx -- The request context for the invocation.
        """
        def importPaths(self, filePaths, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_importPaths.invoke(self, ((filePaths, ), _ctx))

        """
        For clients without access to Bio-Formats, the simplified
        {@code importPaths} method allows passing solely the absolute
        path of the files to be uploaded (no directories) and all
        configuration happens server-side. Much of the functionality
        provided via ode.model.Fileset and
        ode.grid.ImportSettings is of course lost.
        Arguments:
        filePaths -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_importPaths(self, filePaths, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_importPaths.begin(self, ((filePaths, ), _response, _ex, _sent, _ctx))

        """
        For clients without access to Bio-Formats, the simplified
        {@code importPaths} method allows passing solely the absolute
        path of the files to be uploaded (no directories) and all
        configuration happens server-side. Much of the functionality
        provided via ode.model.Fileset and
        ode.grid.ImportSettings is of course lost.
        Arguments:
        filePaths -- 
        """
        def end_importPaths(self, _r):
            return _M_ode.grid.ManagedRepository._op_importPaths.end(self, _r)

        """
        List imports that are currently running in this importer.
        These will be limited based on user/group membership for
        the ode.model.Fileset object which is being created
        by the import. If the user has write permissions for the
        fileset, then the import will be included.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def listImports(self, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_listImports.invoke(self, ((), _ctx))

        """
        List imports that are currently running in this importer.
        These will be limited based on user/group membership for
        the ode.model.Fileset object which is being created
        by the import. If the user has write permissions for the
        fileset, then the import will be included.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_listImports(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_listImports.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        List imports that are currently running in this importer.
        These will be limited based on user/group membership for
        the ode.model.Fileset object which is being created
        by the import. If the user has write permissions for the
        fileset, then the import will be included.
        Arguments:
        """
        def end_listImports(self, _r):
            return _M_ode.grid.ManagedRepository._op_listImports.end(self, _r)

        """
        Return the list of checksum algorithms supported by this repository
        for verifying the integrity of uploaded files.
        They are named as ""algorithm-integer"",
        integer being the bit width of the resulting hash code.
        It is possible for the same algorithm to be offered with
        different bit widths.
        They are listed in descending order of preference,
        as set by the server administrator, and any of them may
        be specified for ImportSettings#checksumAlgorithm.
        Arguments:
        _ctx -- The request context for the invocation.
        """
        def listChecksumAlgorithms(self, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_listChecksumAlgorithms.invoke(self, ((), _ctx))

        """
        Return the list of checksum algorithms supported by this repository
        for verifying the integrity of uploaded files.
        They are named as ""algorithm-integer"",
        integer being the bit width of the resulting hash code.
        It is possible for the same algorithm to be offered with
        different bit widths.
        They are listed in descending order of preference,
        as set by the server administrator, and any of them may
        be specified for ImportSettings#checksumAlgorithm.
        Arguments:
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_listChecksumAlgorithms(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_listChecksumAlgorithms.begin(self, ((), _response, _ex, _sent, _ctx))

        """
        Return the list of checksum algorithms supported by this repository
        for verifying the integrity of uploaded files.
        They are named as ""algorithm-integer"",
        integer being the bit width of the resulting hash code.
        It is possible for the same algorithm to be offered with
        different bit widths.
        They are listed in descending order of preference,
        as set by the server administrator, and any of them may
        be specified for ImportSettings#checksumAlgorithm.
        Arguments:
        """
        def end_listChecksumAlgorithms(self, _r):
            return _M_ode.grid.ManagedRepository._op_listChecksumAlgorithms.end(self, _r)

        """
        Suggest a checksum algorithm to use for
        ImportSettings#checksumAlgorithm according to the
        preferences set by the server administrator. Provide a
        list of the algorithms supported by the client, and the
        server will report which of them is most preferred by
        the server, or return null if none of them are supported.
        Arguments:
        supported -- 
        _ctx -- The request context for the invocation.
        """
        def suggestChecksumAlgorithm(self, supported, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_suggestChecksumAlgorithm.invoke(self, ((supported, ), _ctx))

        """
        Suggest a checksum algorithm to use for
        ImportSettings#checksumAlgorithm according to the
        preferences set by the server administrator. Provide a
        list of the algorithms supported by the client, and the
        server will report which of them is most preferred by
        the server, or return null if none of them are supported.
        Arguments:
        supported -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_suggestChecksumAlgorithm(self, supported, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_suggestChecksumAlgorithm.begin(self, ((supported, ), _response, _ex, _sent, _ctx))

        """
        Suggest a checksum algorithm to use for
        ImportSettings#checksumAlgorithm according to the
        preferences set by the server administrator. Provide a
        list of the algorithms supported by the client, and the
        server will report which of them is most preferred by
        the server, or return null if none of them are supported.
        Arguments:
        supported -- 
        """
        def end_suggestChecksumAlgorithm(self, _r):
            return _M_ode.grid.ManagedRepository._op_suggestChecksumAlgorithm.end(self, _r)

        """
        Verify the checksum for the original files identified by
        the given IDs.
        The files must be in this repository.
        Returns the IDs of the original files whose checksums
        do not match the file on disk.
        Arguments:
        ids -- 
        _ctx -- The request context for the invocation.
        """
        def verifyChecksums(self, ids, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_verifyChecksums.invoke(self, ((ids, ), _ctx))

        """
        Verify the checksum for the original files identified by
        the given IDs.
        The files must be in this repository.
        Returns the IDs of the original files whose checksums
        do not match the file on disk.
        Arguments:
        ids -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_verifyChecksums(self, ids, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_verifyChecksums.begin(self, ((ids, ), _response, _ex, _sent, _ctx))

        """
        Verify the checksum for the original files identified by
        the given IDs.
        The files must be in this repository.
        Returns the IDs of the original files whose checksums
        do not match the file on disk.
        Arguments:
        ids -- 
        """
        def end_verifyChecksums(self, _r):
            return _M_ode.grid.ManagedRepository._op_verifyChecksums.end(self, _r)

        """
        Set the checksum algorithm for the original files identified
        by the given IDs and calculate their checksum accordingly.
        The files must be in this repository.
        Existing checksums are checked before being changed.
        If a checksum does not match, ServerError will be thrown;
        in this case some other files may already have had their
        checksum algorithm set.
        Returns the IDs of the original files that did not already
        have a checksum set for the given algorithm.
        Arguments:
        hasher -- 
        ids -- 
        _ctx -- The request context for the invocation.
        """
        def setChecksumAlgorithm(self, hasher, ids, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_setChecksumAlgorithm.invoke(self, ((hasher, ids), _ctx))

        """
        Set the checksum algorithm for the original files identified
        by the given IDs and calculate their checksum accordingly.
        The files must be in this repository.
        Existing checksums are checked before being changed.
        If a checksum does not match, ServerError will be thrown;
        in this case some other files may already have had their
        checksum algorithm set.
        Returns the IDs of the original files that did not already
        have a checksum set for the given algorithm.
        Arguments:
        hasher -- 
        ids -- 
        _response -- The asynchronous response callback.
        _ex -- The asynchronous exception callback.
        _sent -- The asynchronous sent callback.
        _ctx -- The request context for the invocation.
        Returns: An asynchronous result object for the invocation.
        """
        def begin_setChecksumAlgorithm(self, hasher, ids, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.ManagedRepository._op_setChecksumAlgorithm.begin(self, ((hasher, ids), _response, _ex, _sent, _ctx))

        """
        Set the checksum algorithm for the original files identified
        by the given IDs and calculate their checksum accordingly.
        The files must be in this repository.
        Existing checksums are checked before being changed.
        If a checksum does not match, ServerError will be thrown;
        in this case some other files may already have had their
        checksum algorithm set.
        Returns the IDs of the original files that did not already
        have a checksum set for the given algorithm.
        Arguments:
        hasher -- 
        ids -- 
        """
        def end_setChecksumAlgorithm(self, _r):
            return _M_ode.grid.ManagedRepository._op_setChecksumAlgorithm.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.ManagedRepositoryPrx.ice_checkedCast(proxy, '::ode::grid::ManagedRepository', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.ManagedRepositoryPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::ManagedRepository'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_ManagedRepositoryPrx = IcePy.defineProxy('::ode::grid::ManagedRepository', ManagedRepositoryPrx)

    _M_ode.grid._t_ManagedRepository = IcePy.defineClass('::ode::grid::ManagedRepository', ManagedRepository, -1, (), True, False, None, (_M_ode.grid._t_Repository,), ())
    ManagedRepository._ice_type = _M_ode.grid._t_ManagedRepository

    ManagedRepository._op_importFileset = IcePy.Operation('importFileset', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_Fileset, False, 0), ((), _M_ode.grid._t_ImportSettings, False, 0)), (), ((), _M_ode.grid._t_ImportProcessPrx, False, 0), (_M_ode._t_ServerError,))
    ManagedRepository._op_importPaths = IcePy.Operation('importPaths', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_StringSet, False, 0),), (), ((), _M_ode.grid._t_ImportProcessPrx, False, 0), (_M_ode._t_ServerError,))
    ManagedRepository._op_listImports = IcePy.Operation('listImports', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.grid._t_ImportProcessList, False, 0), (_M_ode._t_ServerError,))
    ManagedRepository._op_listChecksumAlgorithms = IcePy.Operation('listChecksumAlgorithms', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.api._t_ChecksumAlgorithmList, False, 0), ())
    ManagedRepository._op_suggestChecksumAlgorithm = IcePy.Operation('suggestChecksumAlgorithm', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_ChecksumAlgorithmList, False, 0),), (), ((), _M_ode.model._t_ChecksumAlgorithm, False, 0), ())
    ManagedRepository._op_verifyChecksums = IcePy.Operation('verifyChecksums', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.api._t_LongList, False, 0),), (), ((), _M_ode.api._t_LongList, False, 0), (_M_ode._t_ServerError,))
    ManagedRepository._op_setChecksumAlgorithm = IcePy.Operation('setChecksumAlgorithm', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_ChecksumAlgorithm, False, 0), ((), _M_ode.api._t_LongList, False, 0)), (), ((), _M_ode.api._t_LongList, False, 0), (_M_ode._t_ServerError,))

    _M_ode.grid.ManagedRepository = ManagedRepository
    del ManagedRepository

    _M_ode.grid.ManagedRepositoryPrx = ManagedRepositoryPrx
    del ManagedRepositoryPrx

if 'RawAccessRequest' not in _M_ode.grid.__dict__:
    _M_ode.grid.RawAccessRequest = Ice.createTempClass()
    class RawAccessRequest(_M_ode.cmd.Request):
        """
        Command object which will be parsed by the internal
        repository given by ""repo"". This command will *only*
        be processed if the user has sufficient rights (e.g.
        is a member of "system") and is largely intended for
        testing and diagnosis rather than actual client
        functionality.
        """
        def __init__(self, repoUuid='', command='', args=None, path=''):
            _M_ode.cmd.Request.__init__(self)
            self.repoUuid = repoUuid
            self.command = command
            self.args = args
            self.path = path

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::cmd::Request', '::ode::grid::RawAccessRequest')

        def ice_id(self, current=None):
            return '::ode::grid::RawAccessRequest'

        def ice_staticId():
            return '::ode::grid::RawAccessRequest'
        ice_staticId = staticmethod(ice_staticId)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_RawAccessRequest)

        __repr__ = __str__

    _M_ode.grid.RawAccessRequestPrx = Ice.createTempClass()
    class RawAccessRequestPrx(_M_ode.cmd.RequestPrx):

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.RawAccessRequestPrx.ice_checkedCast(proxy, '::ode::grid::RawAccessRequest', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.RawAccessRequestPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::RawAccessRequest'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_RawAccessRequestPrx = IcePy.defineProxy('::ode::grid::RawAccessRequest', RawAccessRequestPrx)

    _M_ode.grid._t_RawAccessRequest = IcePy.defineClass('::ode::grid::RawAccessRequest', RawAccessRequest, -1, (), False, False, _M_ode.cmd._t_Request, (), (
        ('repoUuid', (), IcePy._t_string, False, 0),
        ('command', (), IcePy._t_string, False, 0),
        ('args', (), _M_ode.api._t_StringSet, False, 0),
        ('path', (), IcePy._t_string, False, 0)
    ))
    RawAccessRequest._ice_type = _M_ode.grid._t_RawAccessRequest

    _M_ode.grid.RawAccessRequest = RawAccessRequest
    del RawAccessRequest

    _M_ode.grid.RawAccessRequestPrx = RawAccessRequestPrx
    del RawAccessRequestPrx

if 'InternalRepository' not in _M_ode.grid.__dict__:
    _M_ode.grid.InternalRepository = Ice.createTempClass()
    class InternalRepository(Ice.Object):
        """
        Internal portion of the API used for management. Not available to clients.
        """
        def __init__(self):
            if Ice.getType(self) == _M_ode.grid.InternalRepository:
                raise RuntimeError('ode.grid.InternalRepository is an abstract class')

        def ice_ids(self, current=None):
            return ('::Ice::Object', '::ode::grid::InternalRepository')

        def ice_id(self, current=None):
            return '::ode::grid::InternalRepository'

        def ice_staticId():
            return '::ode::grid::InternalRepository'
        ice_staticId = staticmethod(ice_staticId)

        def createRawFileStore(self, file, current=None):
            pass

        def createRawPixelsStore(self, file, current=None):
            pass

        def createRenderingEngine(self, file, current=None):
            pass

        def createThumbnailStore(self, file, current=None):
            pass

        def getDescription(self, current=None):
            pass

        def getProxy(self, current=None):
            pass

        def rawAccess(self, raw, current=None):
            pass

        def getFilePath(self, file, current=None):
            pass

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_InternalRepository)

        __repr__ = __str__

    _M_ode.grid.InternalRepositoryPrx = Ice.createTempClass()
    class InternalRepositoryPrx(Ice.ObjectPrx):

        def createRawFileStore(self, file, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createRawFileStore.invoke(self, ((file, ), _ctx))

        def begin_createRawFileStore(self, file, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createRawFileStore.begin(self, ((file, ), _response, _ex, _sent, _ctx))

        def end_createRawFileStore(self, _r):
            return _M_ode.grid.InternalRepository._op_createRawFileStore.end(self, _r)

        def createRawPixelsStore(self, file, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createRawPixelsStore.invoke(self, ((file, ), _ctx))

        def begin_createRawPixelsStore(self, file, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createRawPixelsStore.begin(self, ((file, ), _response, _ex, _sent, _ctx))

        def end_createRawPixelsStore(self, _r):
            return _M_ode.grid.InternalRepository._op_createRawPixelsStore.end(self, _r)

        def createRenderingEngine(self, file, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createRenderingEngine.invoke(self, ((file, ), _ctx))

        def begin_createRenderingEngine(self, file, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createRenderingEngine.begin(self, ((file, ), _response, _ex, _sent, _ctx))

        def end_createRenderingEngine(self, _r):
            return _M_ode.grid.InternalRepository._op_createRenderingEngine.end(self, _r)

        def createThumbnailStore(self, file, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createThumbnailStore.invoke(self, ((file, ), _ctx))

        def begin_createThumbnailStore(self, file, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_createThumbnailStore.begin(self, ((file, ), _response, _ex, _sent, _ctx))

        def end_createThumbnailStore(self, _r):
            return _M_ode.grid.InternalRepository._op_createThumbnailStore.end(self, _r)

        def getDescription(self, _ctx=None):
            return _M_ode.grid.InternalRepository._op_getDescription.invoke(self, ((), _ctx))

        def begin_getDescription(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_getDescription.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getDescription(self, _r):
            return _M_ode.grid.InternalRepository._op_getDescription.end(self, _r)

        def getProxy(self, _ctx=None):
            return _M_ode.grid.InternalRepository._op_getProxy.invoke(self, ((), _ctx))

        def begin_getProxy(self, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_getProxy.begin(self, ((), _response, _ex, _sent, _ctx))

        def end_getProxy(self, _r):
            return _M_ode.grid.InternalRepository._op_getProxy.end(self, _r)

        def rawAccess(self, raw, _ctx=None):
            return _M_ode.grid.InternalRepository._op_rawAccess.invoke(self, ((raw, ), _ctx))

        def begin_rawAccess(self, raw, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_rawAccess.begin(self, ((raw, ), _response, _ex, _sent, _ctx))

        def end_rawAccess(self, _r):
            return _M_ode.grid.InternalRepository._op_rawAccess.end(self, _r)

        def getFilePath(self, file, _ctx=None):
            return _M_ode.grid.InternalRepository._op_getFilePath.invoke(self, ((file, ), _ctx))

        def begin_getFilePath(self, file, _response=None, _ex=None, _sent=None, _ctx=None):
            return _M_ode.grid.InternalRepository._op_getFilePath.begin(self, ((file, ), _response, _ex, _sent, _ctx))

        def end_getFilePath(self, _r):
            return _M_ode.grid.InternalRepository._op_getFilePath.end(self, _r)

        def checkedCast(proxy, facetOrCtx=None, _ctx=None):
            return _M_ode.grid.InternalRepositoryPrx.ice_checkedCast(proxy, '::ode::grid::InternalRepository', facetOrCtx, _ctx)
        checkedCast = staticmethod(checkedCast)

        def uncheckedCast(proxy, facet=None):
            return _M_ode.grid.InternalRepositoryPrx.ice_uncheckedCast(proxy, facet)
        uncheckedCast = staticmethod(uncheckedCast)

        def ice_staticId():
            return '::ode::grid::InternalRepository'
        ice_staticId = staticmethod(ice_staticId)

    _M_ode.grid._t_InternalRepositoryPrx = IcePy.defineProxy('::ode::grid::InternalRepository', InternalRepositoryPrx)

    _M_ode.grid._t_InternalRepository = IcePy.defineClass('::ode::grid::InternalRepository', InternalRepository, -1, (), True, False, None, (), ())
    InternalRepository._ice_type = _M_ode.grid._t_InternalRepository

    InternalRepository._op_createRawFileStore = IcePy.Operation('createRawFileStore', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.api._t_RawFileStorePrx, False, 0), (_M_ode._t_ServerError,))
    InternalRepository._op_createRawPixelsStore = IcePy.Operation('createRawPixelsStore', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.api._t_RawPixelsStorePrx, False, 0), (_M_ode._t_ServerError,))
    InternalRepository._op_createRenderingEngine = IcePy.Operation('createRenderingEngine', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.api._t_RenderingEnginePrx, False, 0), (_M_ode._t_ServerError,))
    InternalRepository._op_createThumbnailStore = IcePy.Operation('createThumbnailStore', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), _M_ode.api._t_ThumbnailStorePrx, False, 0), (_M_ode._t_ServerError,))
    InternalRepository._op_getDescription = IcePy.Operation('getDescription', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.model._t_OriginalFile, False, 0), (_M_ode._t_ServerError,))
    InternalRepository._op_getProxy = IcePy.Operation('getProxy', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (), (), ((), _M_ode.grid._t_RepositoryPrx, False, 0), (_M_ode._t_ServerError,))
    InternalRepository._op_rawAccess = IcePy.Operation('rawAccess', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.grid._t_RawAccessRequest, False, 0),), (), ((), _M_ode.cmd._t_Response, False, 0), (_M_ode._t_ServerError,))
    InternalRepository._op_getFilePath = IcePy.Operation('getFilePath', Ice.OperationMode.Normal, Ice.OperationMode.Normal, False, None, (), (((), _M_ode.model._t_OriginalFile, False, 0),), (), ((), IcePy._t_string, False, 0), (_M_ode._t_ServerError,))

    _M_ode.grid.InternalRepository = InternalRepository
    del InternalRepository

    _M_ode.grid.InternalRepositoryPrx = InternalRepositoryPrx
    del InternalRepositoryPrx

if '_t_RepositoryProxyList' not in _M_ode.grid.__dict__:
    _M_ode.grid._t_RepositoryProxyList = IcePy.defineSequence('::ode::grid::RepositoryProxyList', (), _M_ode.grid._t_RepositoryPrx)

if 'RepositoryMap' not in _M_ode.grid.__dict__:
    _M_ode.grid.RepositoryMap = Ice.createTempClass()
    class RepositoryMap(object):
        """
        Return value for ode.grid.SharedResources#repositories
        The descriptions and proxies arrays will have the same size and each
        index in descriptions (non-null) will match a possibly null proxy, if
        the given repository is not currently accessible.
        """
        def __init__(self, descriptions=None, proxies=None):
            self.descriptions = descriptions
            self.proxies = proxies

        def __eq__(self, other):
            if other is None:
                return False
            elif not isinstance(other, _M_ode.grid.RepositoryMap):
                return NotImplemented
            else:
                if self.descriptions != other.descriptions:
                    return False
                if self.proxies != other.proxies:
                    return False
                return True

        def __ne__(self, other):
            return not self.__eq__(other)

        def __str__(self):
            return IcePy.stringify(self, _M_ode.grid._t_RepositoryMap)

        __repr__ = __str__

    _M_ode.grid._t_RepositoryMap = IcePy.defineStruct('::ode::grid::RepositoryMap', RepositoryMap, (), (
        ('descriptions', (), _M_ode.api._t_OriginalFileList),
        ('proxies', (), _M_ode.grid._t_RepositoryProxyList)
    ))

    _M_ode.grid.RepositoryMap = RepositoryMap
    del RepositoryMap

# End of module ode.grid

__name__ = 'ode'

# End of module ode
