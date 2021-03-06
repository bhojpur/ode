/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_SERVERERRORS_ICE
#define ODE_SERVERERRORS_ICE

#include <Glacier2/Session.ice>
#include <ode/Collections.ice>

/**
 * Exceptions thrown by ODE server components. Exceptions thrown client side
 * are available defined in each language binding separately, but will usually
 * subclass from "ClientError".
 *
 * including examples of what a appropriate try/catch block would look like.
 *
 * <p>
 * All exceptions that are thrown by a remote call (any call on a *Prx instance)
 * will be either a subclass of {@link Ice.UserException} or {@link Ice.LocalException}.
 * <a href="http://doc.zeroc.com/display/Ice/Run-Time+Exceptions#Run-TimeExceptions-InheritanceHierarchyforExceptions">Inheritance Hierarchy for Exceptions</a>
 * from the Ice manual shows the entire exception hierarchy. The exceptions described in
 * this file will subclass from {@link Ice.UserException}. Other Ice-runtime exceptions subclass
 * from {@link Ice.LocalException}.
 * </p>
 *
 * <pre>
 *
 * Bhojpur ODE Specific:
 * ===============
 *  ServerError (root server exception)
 *   |
 *   |_ InternalException (server bug)
 *   |
 *   |_ ResourceError (non-recoverable)
 *   |  \_ NoProcessorAvailable
 *   |
 *   |_ ConcurrencyException (recoverable)
 *   |  |_ ConcurrentModification (data was changed)
 *   |  |_ OptimisticLockException (changed data conflicts)
 *   |  |_ LockTimeout (took too long to aquire lock)
 *   |  |_ TryAgain (some processing required before server is ready)
 *   |  \_ TooManyUsersException
 *   |     \_ DatabaseBusyException
 *   |
 *   |_ ApiUsageException (misuse of services)
 *   |   |_ OverUsageException (too much)
 *   |   |_ QueryException (bad query string)
 *   |   |_ ValidationException (bad data)
 *   |      |_ ChecksumValidationException (checksum mismatch)
 *   |      \_ FilePathNamingException (repository path badly named)
 *   |
 *   |_ SecurityViolation (some no-no)
 *   |   \_ GroupSecurityViolation
 *   |      |_ PermissionMismatchGroupSecurityViolation
 *   |      \_ ReadOnlyGroupSecurityViolation
 *   |
 *   \_SessionException
 *      |_ RemovedSessionException (accessing a non-extant session)
 *      |_ SessionTimeoutException (session timed out; not yet removed)
 *      \_ ShutdownInProgress      (session on this server will most likely be destroyed)
 * </pre>
 *
 * <p>
 * However, in addition to {@link Ice.LocalException} subclasses, the Ice runtime also
 * defines subclasses of {@link Ice.UserException}. In some cases, ODE subclasses
 * from these exceptions. The subclasses shown below are not exhaustive, but show those
 * which an application's exception handler may want to deal with.
 * </p>
 *
 * <pre>
 *  Ice::Exception (root of all Ice exceptions)
 *   |
 *   |_ Ice::UserException (super class of all application exceptions)
 *   |  |
 *   |  |_ Glacier2::CannotCreateSessionException (1 of 2 exceptions throwable by createSession)
 *   |  |   |_ ode::AuthenticationException (bad login)
 *   |  |   |_ ode::ExpiredCredentialException (old password)
 *   |  |   |_ ode::WrappedCreateSessionException (any other server error during createSession)
 *   |  |   \_ ode::licenses::NoAvailableLicensesException (see tools/licenses/resources/ode/LicensesAPI.ice)
 *   |  |
 *   |  \_ Glacier2::PermissionDeniedException (other of 2 exceptions throwable by createSession)
 *   |
 *   \_ Ice::LocalException (should generally be considered fatal. See exceptions below)
 *       |
 *       |_ Ice::ProtocolException (something went wrong on the wire. Wrong version?)
 *       |
 *       |_ Ice::RequestFailedException
 *       |   |_ ObjectNotExistException (Service timeout or similar?)
 *       |   \_ OperationNotExistException (Improper use of uncheckedCast?)
 *       |
 *       |_ Ice::UnknownException (server threw an unexpected exception. Bug!)
 *       |
 *       \_ Ice::TimeoutException
 *           \_ Ice::ConnectTimeoutException (Couldn't establish a connection. Retry?)
 *
 * </pre>
 *
 **/

module ode
{
  /*
   * Base exception. Equivalent to the ode.conditions.RootException.
   * RootException must be split into a ServerError and a ClientError
   * base-class since the two systems are more strictly split by the
   * Ice-runtime than is done in RMI/Java.
   */
  exception ServerError
    {
      string serverStackTrace;
      string serverExceptionClass;
      string message;
    };

  // SESSION EXCEPTIONS --------------------------------

  /**
   * Base session exception, though in the ODE.server
   * implementation, all exceptions thrown by the Glacier2
   * must subclass CannotCreateSessionException. See below.
   */
  exception SessionException extends ServerError
    {

    };

  /**
   * Session has been removed. Either it was closed, or it
   * timed out and one "SessionTimeoutException" has already
   * been thrown.
   */
  exception RemovedSessionException extends SessionException
    {

    };

  /**
   * Session has timed out and will be removed.
   */
  exception SessionTimeoutException extends SessionException
    {

    };

  /**
   * Server is in the progress of shutting down which will
   * typically lead to the current session being closed.
   */
  exception ShutdownInProgress extends SessionException
    {

    };

  // SESSION EXCEPTIONS (Glacier2) ---------------------

  /**
   * createSession() is a two-phase process. First, a PermissionsVerifier is
   * called which must return true; then a SessionManager is called to create
   * the session (ServiceFactory). If the PermissionsVerifier returns false,
   * then PermissionDeniedException will be thrown. This, however, cannot be
   * subclassed and so string parsing must be used.
   */

  /**
   * Thrown when the information provided ode.createSession() or more
   * specifically Glacier2.RouterPrx.createSession() is incorrect. This
   * does -not- subclass from the ode.ServerError class because the
   * Ice Glacier2::SessionManager interface can only throw CCSEs.
   */
  exception AuthenticationException extends Glacier2::CannotCreateSessionException
    {

    };

  /**
   * Thrown when the password for a user has expired. Use: ISession.changeExpiredCredentials()
   * and login as guest. This does -not- subclass from the ode.ServerError class because the
   * Ice Glacier2::SessionManager interface can only throw CCSEs.
   */
  exception ExpiredCredentialException extends Glacier2::CannotCreateSessionException
    {

    };

  /**
   * Thrown when any other server exception causes the session creation to fail.
   * Since working with the static information of Ice exceptions is not as easy
   * as with classes, here we use booleans to represent what has gone wrong.
   */
  exception WrappedCreateSessionException extends Glacier2::CannotCreateSessionException
    {
      bool    concurrency;
      long    backOff;    /* Only used if ConcurrencyException */
      string  type;       /* Ice static type information */
    };

  // OTHER SERVER EXCEPTIONS ------------------------------

  /**
   * Programmer error. Ideally should not be thrown.
   */
  exception InternalException extends ServerError
    {
    };

  // RESOURCE

  /**
   * Unrecoverable error. The resource being accessed is not available.
   */
  exception ResourceError extends ServerError
    {
    };

  /**
   * A script cannot be executed because no matching processor
   * was found.
   */
  exception NoProcessorAvailable extends ResourceError
    {
        /**
         * Number of processors that responded to the inquiry.
         * If 1 or more, then the given script was not acceptable
         * (e.g. non-official) and a specialized processor may need
         * to be started.
         **/
        int processorCount;
    };

  // CONCURRENCY

  /**
   * Recoverable error caused by simultaneous access of some form.
   */
  exception ConcurrencyException extends ServerError
    {
       long backOff; /* Backoff in milliseconds */
    };

  /**
   * Currently unused.
   */
  exception ConcurrentModification extends ConcurrencyException
    {
    };

  /**
   * Too many simultaneous database users. This implies that a
   * connection to the database could not be acquired, no data
   * was saved or modified. Clients may want to wait the given
   * backOff period, and retry.
   */
  exception DatabaseBusyException extends ConcurrencyException
    {
    };

  /**
   * Conflicting changes to the same piece of data.
   */
  exception OptimisticLockException extends ConcurrencyException
    {
    };

  /**
   * Lock cannot be acquired and has timed out.
   */
  exception LockTimeout extends ConcurrencyException
    {
        int seconds; /* Informational field on how long timeout was */
    };

  /**
   * Background processing needed before server is ready
   */
  exception TryAgain extends ConcurrencyException
    {
    };

  exception MissingPyramidException extends ConcurrencyException
   {
        long pixelsID;
   };

  // API USAGE

  exception ApiUsageException extends ServerError
    {
    };

  exception OverUsageException extends ApiUsageException
    {
    };

  exception QueryException extends ApiUsageException
    {
    };

  exception ValidationException extends ApiUsageException
    {
    };

  exception ChecksumValidationException extends ValidationException
    {
        ode::api::IntStringMap failingChecksums;
    };

  exception FilePathNamingException extends ValidationException
    {
        /* the file path that breaks the server's rules */
        string illegalFilePath;
        /* the rules actually violated by the file path */
        ode::api::IntegerList illegalCodePoints;  /* proscribed Unicode code points */
        ode::api::StringSet illegalPrefixes;      /* proscribed name prefixes */
        ode::api::StringSet illegalSuffixes;      /* proscribed name suffixes */
        ode::api::StringSet illegalNames;         /* proscribed names */
    };

  // SECURITY

  exception SecurityViolation extends ServerError
    {
    };

  exception GroupSecurityViolation extends SecurityViolation
    {
    };

  exception PermissionMismatchGroupSecurityViolation extends SecurityViolation
    {
    };
  exception ReadOnlyGroupSecurityViolation extends SecurityViolation
    {
    };

  // ODEFS

    /**
     * OdeFSError
     *
     * Just one catch-all UserException for the present. It could be
     * subclassed to provide a finer grained level if necessary.
     *
     * It should be fitted into or subsumed within the above hierarchy
     **/
    exception OdeFSError extends ServerError
      {
        string reason;
      };

};

#endif // ODE_SERVERERRORS_ICE