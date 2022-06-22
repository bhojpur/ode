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
import Glacier2_Session_ice
import ode_Collections_ice

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
_M_ode.__doc__ = """
Exceptions thrown by Bhojpur ODE server components. Exceptions thrown client side
are available defined in each language binding separately, but will usually
subclass from "ClientError".
Check examples of what a appropriate try/catch block would look like.
All exceptions that are thrown by a remote call (any call on a *Prx instance)
will be either a subclass of Ice.UserException or Ice.LocalException.
Inheritance Hierarchy for Exceptions
from the Ice manual shows the entire exception hierarchy. The exceptions described in
this file will subclass from Ice.UserException. Other Ice-runtime exceptions subclass
from Ice.LocalException.
Bhojpur ODE Specific:
===============
ServerError (root server exception)
|
|_ InternalException (server bug)
|
|_ ResourceError (non-recoverable)
|  \_ NoProcessorAvailable
|
|_ ConcurrencyException (recoverable)
|  |_ ConcurrentModification (data was changed)
|  |_ OptimisticLockException (changed data conflicts)
|  |_ LockTimeout (took too long to aquire lock)
|  |_ TryAgain (some processing required before server is ready)
|  \_ TooManyUsersException
|     \_ DatabaseBusyException
|
|_ ApiUsageException (misuse of services)
|   |_ OverUsageException (too much)
|   |_ QueryException (bad query string)
|   |_ ValidationException (bad data)
|      |_ ChecksumValidationException (checksum mismatch)
|      \_ FilePathNamingException (repository path badly named)
|
|_ SecurityViolation (some no-no)
|   \_ GroupSecurityViolation
|      |_ PermissionMismatchGroupSecurityViolation
|      \_ ReadOnlyGroupSecurityViolation
|
\_SessionException
|_ RemovedSessionException (accessing a non-extant session)
|_ SessionTimeoutException (session timed out; not yet removed)
\_ ShutdownInProgress      (session on this server will most likely be destroyed)
However, in addition to Ice.LocalException subclasses, the Ice runtime also
defines subclasses of Ice.UserException. In some cases, Bhojpur ODE subclasses
from these exceptions. The subclasses shown below are not exhaustive, but show those
which an application's exception handler may want to deal with.
Ice::Exception (root of all Ice exceptions)
|
|_ Ice::UserException (super class of all application exceptions)
|  |
|  |_ Glacier2::CannotCreateSessionException (1 of 2 exceptions throwable by createSession)
|  |   |_ ode::AuthenticationException (bad login)
|  |   |_ ode::ExpiredCredentialException (old password)
|  |   |_ ode::WrappedCreateSessionException (any other server error during createSession)
|  |   \_ ode::licenses::NoAvailableLicensesException (see tools/licenses/resources/ode/LicensesAPI.ice)
|  |
|  \_ Glacier2::PermissionDeniedException (other of 2 exceptions throwable by createSession)
|
\_ Ice::LocalException (should generally be considered fatal. See exceptions below)
|
|_ Ice::ProtocolException (something went wrong on the wire. Wrong version?)
|
|_ Ice::RequestFailedException
|   |_ ObjectNotExistException (Service timeout or similar?)
|   \_ OperationNotExistException (Improper use of uncheckedCast?)
|
|_ Ice::UnknownException (server threw an unexpected exception. Bug!)
|
\_ Ice::TimeoutException
\_ Ice::ConnectTimeoutException (Couldn't establish a connection. Retry?)
"""

if 'ServerError' not in _M_ode.__dict__:
    _M_ode.ServerError = Ice.createTempClass()
    class ServerError(Ice.UserException):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            self.serverStackTrace = serverStackTrace
            self.serverExceptionClass = serverExceptionClass
            self.message = message

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ServerError'

    _M_ode._t_ServerError = IcePy.defineException('::ode::ServerError', ServerError, (), False, None, (
        ('serverStackTrace', (), IcePy._t_string, False, 0),
        ('serverExceptionClass', (), IcePy._t_string, False, 0),
        ('message', (), IcePy._t_string, False, 0)
    ))
    ServerError._ice_type = _M_ode._t_ServerError

    _M_ode.ServerError = ServerError
    del ServerError

if 'SessionException' not in _M_ode.__dict__:
    _M_ode.SessionException = Ice.createTempClass()
    class SessionException(_M_ode.ServerError):
        """
        Base session exception, though in the Bhojpur ODE server
        implementation, all exceptions thrown by the Glacier2
        must subclass CannotCreateSessionException. See below.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::SessionException'

    _M_ode._t_SessionException = IcePy.defineException('::ode::SessionException', SessionException, (), False, _M_ode._t_ServerError, ())
    SessionException._ice_type = _M_ode._t_SessionException

    _M_ode.SessionException = SessionException
    del SessionException

if 'RemovedSessionException' not in _M_ode.__dict__:
    _M_ode.RemovedSessionException = Ice.createTempClass()
    class RemovedSessionException(_M_ode.SessionException):
        """
        Session has been removed. Either it was closed, or it
        timed out and one "SessionTimeoutException" has already
        been thrown.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.SessionException.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::RemovedSessionException'

    _M_ode._t_RemovedSessionException = IcePy.defineException('::ode::RemovedSessionException', RemovedSessionException, (), False, _M_ode._t_SessionException, ())
    RemovedSessionException._ice_type = _M_ode._t_RemovedSessionException

    _M_ode.RemovedSessionException = RemovedSessionException
    del RemovedSessionException

if 'SessionTimeoutException' not in _M_ode.__dict__:
    _M_ode.SessionTimeoutException = Ice.createTempClass()
    class SessionTimeoutException(_M_ode.SessionException):
        """
        Session has timed out and will be removed.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.SessionException.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::SessionTimeoutException'

    _M_ode._t_SessionTimeoutException = IcePy.defineException('::ode::SessionTimeoutException', SessionTimeoutException, (), False, _M_ode._t_SessionException, ())
    SessionTimeoutException._ice_type = _M_ode._t_SessionTimeoutException

    _M_ode.SessionTimeoutException = SessionTimeoutException
    del SessionTimeoutException

if 'ShutdownInProgress' not in _M_ode.__dict__:
    _M_ode.ShutdownInProgress = Ice.createTempClass()
    class ShutdownInProgress(_M_ode.SessionException):
        """
        Server is in the progress of shutting down which will
        typically lead to the current session being closed.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.SessionException.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ShutdownInProgress'

    _M_ode._t_ShutdownInProgress = IcePy.defineException('::ode::ShutdownInProgress', ShutdownInProgress, (), False, _M_ode._t_SessionException, ())
    ShutdownInProgress._ice_type = _M_ode._t_ShutdownInProgress

    _M_ode.ShutdownInProgress = ShutdownInProgress
    del ShutdownInProgress

if 'AuthenticationException' not in _M_ode.__dict__:
    _M_ode.AuthenticationException = Ice.createTempClass()
    class AuthenticationException(_M_Glacier2.CannotCreateSessionException):
        """
        Thrown when the information provided ode.createSession() or more
        specifically Glacier2.RouterPrx.createSession() is incorrect. This
        does -not- subclass from the ode.ServerError class because the
        Ice Glacier2::SessionManager interface can only throw CCSEs.
        """
        def __init__(self, reason=''):
            _M_Glacier2.CannotCreateSessionException.__init__(self, reason)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::AuthenticationException'

    _M_ode._t_AuthenticationException = IcePy.defineException('::ode::AuthenticationException', AuthenticationException, (), True, _M_Glacier2._t_CannotCreateSessionException, ())
    AuthenticationException._ice_type = _M_ode._t_AuthenticationException

    _M_ode.AuthenticationException = AuthenticationException
    del AuthenticationException

if 'ExpiredCredentialException' not in _M_ode.__dict__:
    _M_ode.ExpiredCredentialException = Ice.createTempClass()
    class ExpiredCredentialException(_M_Glacier2.CannotCreateSessionException):
        """
        Thrown when the password for a user has expired. Use: ISession.changeExpiredCredentials()
        and login as guest. This does -not- subclass from the ode.ServerError class because the
        Ice Glacier2::SessionManager interface can only throw CCSEs.
        """
        def __init__(self, reason=''):
            _M_Glacier2.CannotCreateSessionException.__init__(self, reason)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ExpiredCredentialException'

    _M_ode._t_ExpiredCredentialException = IcePy.defineException('::ode::ExpiredCredentialException', ExpiredCredentialException, (), True, _M_Glacier2._t_CannotCreateSessionException, ())
    ExpiredCredentialException._ice_type = _M_ode._t_ExpiredCredentialException

    _M_ode.ExpiredCredentialException = ExpiredCredentialException
    del ExpiredCredentialException

if 'WrappedCreateSessionException' not in _M_ode.__dict__:
    _M_ode.WrappedCreateSessionException = Ice.createTempClass()
    class WrappedCreateSessionException(_M_Glacier2.CannotCreateSessionException):
        """
        Thrown when any other server exception causes the session creation to fail.
        Since working with the static information of Ice exceptions is not as easy
        as with classes, here we use booleans to represent what has gone wrong.
        """
        def __init__(self, reason='', concurrency=False, backOff=0, type=''):
            _M_Glacier2.CannotCreateSessionException.__init__(self, reason)
            self.concurrency = concurrency
            self.backOff = backOff
            self.type = type

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::WrappedCreateSessionException'

    _M_ode._t_WrappedCreateSessionException = IcePy.defineException('::ode::WrappedCreateSessionException', WrappedCreateSessionException, (), True, _M_Glacier2._t_CannotCreateSessionException, (
        ('concurrency', (), IcePy._t_bool, False, 0),
        ('backOff', (), IcePy._t_long, False, 0),
        ('type', (), IcePy._t_string, False, 0)
    ))
    WrappedCreateSessionException._ice_type = _M_ode._t_WrappedCreateSessionException

    _M_ode.WrappedCreateSessionException = WrappedCreateSessionException
    del WrappedCreateSessionException

if 'InternalException' not in _M_ode.__dict__:
    _M_ode.InternalException = Ice.createTempClass()
    class InternalException(_M_ode.ServerError):
        """
        Programmer error. Ideally should not be thrown.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::InternalException'

    _M_ode._t_InternalException = IcePy.defineException('::ode::InternalException', InternalException, (), False, _M_ode._t_ServerError, ())
    InternalException._ice_type = _M_ode._t_InternalException

    _M_ode.InternalException = InternalException
    del InternalException

if 'ResourceError' not in _M_ode.__dict__:
    _M_ode.ResourceError = Ice.createTempClass()
    class ResourceError(_M_ode.ServerError):
        """
        Unrecoverable error. The resource being accessed is not available.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ResourceError'

    _M_ode._t_ResourceError = IcePy.defineException('::ode::ResourceError', ResourceError, (), False, _M_ode._t_ServerError, ())
    ResourceError._ice_type = _M_ode._t_ResourceError

    _M_ode.ResourceError = ResourceError
    del ResourceError

if 'NoProcessorAvailable' not in _M_ode.__dict__:
    _M_ode.NoProcessorAvailable = Ice.createTempClass()
    class NoProcessorAvailable(_M_ode.ResourceError):
        """
        A script cannot be executed because no matching processor
        was found.
        Members:
        processorCount -- Number of processors that responded to the inquiry.
        If 1 or more, then the given script was not acceptable
        (e.g. non-official) and a specialized processor may need
        to be started.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', processorCount=0):
            _M_ode.ResourceError.__init__(self, serverStackTrace, serverExceptionClass, message)
            self.processorCount = processorCount

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::NoProcessorAvailable'

    _M_ode._t_NoProcessorAvailable = IcePy.defineException('::ode::NoProcessorAvailable', NoProcessorAvailable, (), False, _M_ode._t_ResourceError, (('processorCount', (), IcePy._t_int, False, 0),))
    NoProcessorAvailable._ice_type = _M_ode._t_NoProcessorAvailable

    _M_ode.NoProcessorAvailable = NoProcessorAvailable
    del NoProcessorAvailable

if 'ConcurrencyException' not in _M_ode.__dict__:
    _M_ode.ConcurrencyException = Ice.createTempClass()
    class ConcurrencyException(_M_ode.ServerError):
        """
        Recoverable error caused by simultaneous access of some form.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', backOff=0):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)
            self.backOff = backOff

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ConcurrencyException'

    _M_ode._t_ConcurrencyException = IcePy.defineException('::ode::ConcurrencyException', ConcurrencyException, (), False, _M_ode._t_ServerError, (('backOff', (), IcePy._t_long, False, 0),))
    ConcurrencyException._ice_type = _M_ode._t_ConcurrencyException

    _M_ode.ConcurrencyException = ConcurrencyException
    del ConcurrencyException

if 'ConcurrentModification' not in _M_ode.__dict__:
    _M_ode.ConcurrentModification = Ice.createTempClass()
    class ConcurrentModification(_M_ode.ConcurrencyException):
        """
        Currently unused.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', backOff=0):
            _M_ode.ConcurrencyException.__init__(self, serverStackTrace, serverExceptionClass, message, backOff)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ConcurrentModification'

    _M_ode._t_ConcurrentModification = IcePy.defineException('::ode::ConcurrentModification', ConcurrentModification, (), False, _M_ode._t_ConcurrencyException, ())
    ConcurrentModification._ice_type = _M_ode._t_ConcurrentModification

    _M_ode.ConcurrentModification = ConcurrentModification
    del ConcurrentModification

if 'DatabaseBusyException' not in _M_ode.__dict__:
    _M_ode.DatabaseBusyException = Ice.createTempClass()
    class DatabaseBusyException(_M_ode.ConcurrencyException):
        """
        Too many simultaneous database users. This implies that a
        connection to the database could not be acquired, no data
        was saved or modified. Clients may want to wait the given
        backOff period, and retry.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', backOff=0):
            _M_ode.ConcurrencyException.__init__(self, serverStackTrace, serverExceptionClass, message, backOff)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::DatabaseBusyException'

    _M_ode._t_DatabaseBusyException = IcePy.defineException('::ode::DatabaseBusyException', DatabaseBusyException, (), False, _M_ode._t_ConcurrencyException, ())
    DatabaseBusyException._ice_type = _M_ode._t_DatabaseBusyException

    _M_ode.DatabaseBusyException = DatabaseBusyException
    del DatabaseBusyException

if 'OptimisticLockException' not in _M_ode.__dict__:
    _M_ode.OptimisticLockException = Ice.createTempClass()
    class OptimisticLockException(_M_ode.ConcurrencyException):
        """
        Conflicting changes to the same piece of data.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', backOff=0):
            _M_ode.ConcurrencyException.__init__(self, serverStackTrace, serverExceptionClass, message, backOff)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::OptimisticLockException'

    _M_ode._t_OptimisticLockException = IcePy.defineException('::ode::OptimisticLockException', OptimisticLockException, (), False, _M_ode._t_ConcurrencyException, ())
    OptimisticLockException._ice_type = _M_ode._t_OptimisticLockException

    _M_ode.OptimisticLockException = OptimisticLockException
    del OptimisticLockException

if 'LockTimeout' not in _M_ode.__dict__:
    _M_ode.LockTimeout = Ice.createTempClass()
    class LockTimeout(_M_ode.ConcurrencyException):
        """
        Lock cannot be acquired and has timed out.
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', backOff=0, seconds=0):
            _M_ode.ConcurrencyException.__init__(self, serverStackTrace, serverExceptionClass, message, backOff)
            self.seconds = seconds

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::LockTimeout'

    _M_ode._t_LockTimeout = IcePy.defineException('::ode::LockTimeout', LockTimeout, (), False, _M_ode._t_ConcurrencyException, (('seconds', (), IcePy._t_int, False, 0),))
    LockTimeout._ice_type = _M_ode._t_LockTimeout

    _M_ode.LockTimeout = LockTimeout
    del LockTimeout

if 'TryAgain' not in _M_ode.__dict__:
    _M_ode.TryAgain = Ice.createTempClass()
    class TryAgain(_M_ode.ConcurrencyException):
        """
        Background processing needed before server is ready
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', backOff=0):
            _M_ode.ConcurrencyException.__init__(self, serverStackTrace, serverExceptionClass, message, backOff)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::TryAgain'

    _M_ode._t_TryAgain = IcePy.defineException('::ode::TryAgain', TryAgain, (), False, _M_ode._t_ConcurrencyException, ())
    TryAgain._ice_type = _M_ode._t_TryAgain

    _M_ode.TryAgain = TryAgain
    del TryAgain

if 'MissingPyramidException' not in _M_ode.__dict__:
    _M_ode.MissingPyramidException = Ice.createTempClass()
    class MissingPyramidException(_M_ode.ConcurrencyException):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', backOff=0, pixelsID=0):
            _M_ode.ConcurrencyException.__init__(self, serverStackTrace, serverExceptionClass, message, backOff)
            self.pixelsID = pixelsID

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::MissingPyramidException'

    _M_ode._t_MissingPyramidException = IcePy.defineException('::ode::MissingPyramidException', MissingPyramidException, (), False, _M_ode._t_ConcurrencyException, (('pixelsID', (), IcePy._t_long, False, 0),))
    MissingPyramidException._ice_type = _M_ode._t_MissingPyramidException

    _M_ode.MissingPyramidException = MissingPyramidException
    del MissingPyramidException

if 'ApiUsageException' not in _M_ode.__dict__:
    _M_ode.ApiUsageException = Ice.createTempClass()
    class ApiUsageException(_M_ode.ServerError):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ApiUsageException'

    _M_ode._t_ApiUsageException = IcePy.defineException('::ode::ApiUsageException', ApiUsageException, (), False, _M_ode._t_ServerError, ())
    ApiUsageException._ice_type = _M_ode._t_ApiUsageException

    _M_ode.ApiUsageException = ApiUsageException
    del ApiUsageException

if 'OverUsageException' not in _M_ode.__dict__:
    _M_ode.OverUsageException = Ice.createTempClass()
    class OverUsageException(_M_ode.ApiUsageException):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ApiUsageException.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::OverUsageException'

    _M_ode._t_OverUsageException = IcePy.defineException('::ode::OverUsageException', OverUsageException, (), False, _M_ode._t_ApiUsageException, ())
    OverUsageException._ice_type = _M_ode._t_OverUsageException

    _M_ode.OverUsageException = OverUsageException
    del OverUsageException

if 'QueryException' not in _M_ode.__dict__:
    _M_ode.QueryException = Ice.createTempClass()
    class QueryException(_M_ode.ApiUsageException):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ApiUsageException.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::QueryException'

    _M_ode._t_QueryException = IcePy.defineException('::ode::QueryException', QueryException, (), False, _M_ode._t_ApiUsageException, ())
    QueryException._ice_type = _M_ode._t_QueryException

    _M_ode.QueryException = QueryException
    del QueryException

if 'ValidationException' not in _M_ode.__dict__:
    _M_ode.ValidationException = Ice.createTempClass()
    class ValidationException(_M_ode.ApiUsageException):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ApiUsageException.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ValidationException'

    _M_ode._t_ValidationException = IcePy.defineException('::ode::ValidationException', ValidationException, (), False, _M_ode._t_ApiUsageException, ())
    ValidationException._ice_type = _M_ode._t_ValidationException

    _M_ode.ValidationException = ValidationException
    del ValidationException

if 'ChecksumValidationException' not in _M_ode.__dict__:
    _M_ode.ChecksumValidationException = Ice.createTempClass()
    class ChecksumValidationException(_M_ode.ValidationException):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', failingChecksums=None):
            _M_ode.ValidationException.__init__(self, serverStackTrace, serverExceptionClass, message)
            self.failingChecksums = failingChecksums

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ChecksumValidationException'

    _M_ode._t_ChecksumValidationException = IcePy.defineException('::ode::ChecksumValidationException', ChecksumValidationException, (), False, _M_ode._t_ValidationException, (('failingChecksums', (), _M_ode.api._t_IntStringMap, False, 0),))
    ChecksumValidationException._ice_type = _M_ode._t_ChecksumValidationException

    _M_ode.ChecksumValidationException = ChecksumValidationException
    del ChecksumValidationException

if 'FilePathNamingException' not in _M_ode.__dict__:
    _M_ode.FilePathNamingException = Ice.createTempClass()
    class FilePathNamingException(_M_ode.ValidationException):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', illegalFilePath='', illegalCodePoints=None, illegalPrefixes=None, illegalSuffixes=None, illegalNames=None):
            _M_ode.ValidationException.__init__(self, serverStackTrace, serverExceptionClass, message)
            self.illegalFilePath = illegalFilePath
            self.illegalCodePoints = illegalCodePoints
            self.illegalPrefixes = illegalPrefixes
            self.illegalSuffixes = illegalSuffixes
            self.illegalNames = illegalNames

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::FilePathNamingException'

    _M_ode._t_FilePathNamingException = IcePy.defineException('::ode::FilePathNamingException', FilePathNamingException, (), False, _M_ode._t_ValidationException, (
        ('illegalFilePath', (), IcePy._t_string, False, 0),
        ('illegalCodePoints', (), _M_ode.api._t_IntegerList, False, 0),
        ('illegalPrefixes', (), _M_ode.api._t_StringSet, False, 0),
        ('illegalSuffixes', (), _M_ode.api._t_StringSet, False, 0),
        ('illegalNames', (), _M_ode.api._t_StringSet, False, 0)
    ))
    FilePathNamingException._ice_type = _M_ode._t_FilePathNamingException

    _M_ode.FilePathNamingException = FilePathNamingException
    del FilePathNamingException

if 'SecurityViolation' not in _M_ode.__dict__:
    _M_ode.SecurityViolation = Ice.createTempClass()
    class SecurityViolation(_M_ode.ServerError):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::SecurityViolation'

    _M_ode._t_SecurityViolation = IcePy.defineException('::ode::SecurityViolation', SecurityViolation, (), False, _M_ode._t_ServerError, ())
    SecurityViolation._ice_type = _M_ode._t_SecurityViolation

    _M_ode.SecurityViolation = SecurityViolation
    del SecurityViolation

if 'GroupSecurityViolation' not in _M_ode.__dict__:
    _M_ode.GroupSecurityViolation = Ice.createTempClass()
    class GroupSecurityViolation(_M_ode.SecurityViolation):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.SecurityViolation.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::GroupSecurityViolation'

    _M_ode._t_GroupSecurityViolation = IcePy.defineException('::ode::GroupSecurityViolation', GroupSecurityViolation, (), False, _M_ode._t_SecurityViolation, ())
    GroupSecurityViolation._ice_type = _M_ode._t_GroupSecurityViolation

    _M_ode.GroupSecurityViolation = GroupSecurityViolation
    del GroupSecurityViolation

if 'PermissionMismatchGroupSecurityViolation' not in _M_ode.__dict__:
    _M_ode.PermissionMismatchGroupSecurityViolation = Ice.createTempClass()
    class PermissionMismatchGroupSecurityViolation(_M_ode.SecurityViolation):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.SecurityViolation.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::PermissionMismatchGroupSecurityViolation'

    _M_ode._t_PermissionMismatchGroupSecurityViolation = IcePy.defineException('::ode::PermissionMismatchGroupSecurityViolation', PermissionMismatchGroupSecurityViolation, (), False, _M_ode._t_SecurityViolation, ())
    PermissionMismatchGroupSecurityViolation._ice_type = _M_ode._t_PermissionMismatchGroupSecurityViolation

    _M_ode.PermissionMismatchGroupSecurityViolation = PermissionMismatchGroupSecurityViolation
    del PermissionMismatchGroupSecurityViolation

if 'ReadOnlyGroupSecurityViolation' not in _M_ode.__dict__:
    _M_ode.ReadOnlyGroupSecurityViolation = Ice.createTempClass()
    class ReadOnlyGroupSecurityViolation(_M_ode.SecurityViolation):
        def __init__(self, serverStackTrace='', serverExceptionClass='', message=''):
            _M_ode.SecurityViolation.__init__(self, serverStackTrace, serverExceptionClass, message)

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::ReadOnlyGroupSecurityViolation'

    _M_ode._t_ReadOnlyGroupSecurityViolation = IcePy.defineException('::ode::ReadOnlyGroupSecurityViolation', ReadOnlyGroupSecurityViolation, (), False, _M_ode._t_SecurityViolation, ())
    ReadOnlyGroupSecurityViolation._ice_type = _M_ode._t_ReadOnlyGroupSecurityViolation

    _M_ode.ReadOnlyGroupSecurityViolation = ReadOnlyGroupSecurityViolation
    del ReadOnlyGroupSecurityViolation

if 'OdeFSError' not in _M_ode.__dict__:
    _M_ode.OdeFSError = Ice.createTempClass()
    class OdeFSError(_M_ode.ServerError):
        """
        OdeFSError
        Just one catch-all UserException for the present. It could be
        subclassed to provide a finer grained level if necessary.
        It should be fitted into or subsumed within the above hierarchy
        """
        def __init__(self, serverStackTrace='', serverExceptionClass='', message='', reason=''):
            _M_ode.ServerError.__init__(self, serverStackTrace, serverExceptionClass, message)
            self.reason = reason

        def __str__(self):
            return IcePy.stringifyException(self)

        __repr__ = __str__

        _ice_name = 'ode::OdeFSError'

    _M_ode._t_OdeFSError = IcePy.defineException('::ode::OdeFSError', OdeFSError, (), False, _M_ode._t_ServerError, (('reason', (), IcePy._t_string, False, 0),))
    OdeFSError._ice_type = _M_ode._t_OdeFSError

    _M_ode.OdeFSError = OdeFSError
    del OdeFSError

# End of module ode
