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

"""
Decorators for use with Bhojpur ODE web applications.
"""

import logging
import traceback
from django.http import Http404, HttpResponseRedirect, JsonResponse
from django.http.response import HttpResponseBase
from django.shortcuts import render
from django.http import HttpResponseForbidden, StreamingHttpResponse

from django.conf import settings
from django.utils.http import urlencode
from functools import update_wrapper
from django.urls import reverse, resolve, NoReverseMatch
from django.core.cache import cache

from engine.utils import reverse_with_params
from engine.connector import Connector
from engine.gateway.utils import propertiesToDict
from ode import ApiUsageException

logger = logging.getLogger(__name__)

def parse_url(lookup_view):
    if not lookup_view:
        raise ValueError("No lookup_view")
    url = None
    try:
        url = reverse_with_params(
            viewname=lookup_view["viewname"],
            args=lookup_view.get("args", []),
            query_string=lookup_view.get("query_string", None),
        )
    except KeyError:
        # assume we've been passed a url
        try:
            resolve(lookup_view)
            url = lookup_view
        except Exception:
            pass
    if url is None:
        logger.error("Reverse for '%s' not found." % lookup_view)
        raise NoReverseMatch("Reverse for '%s' not found." % lookup_view)
    return url


def get_client_ip(request):
    x_forwarded_for = request.META.get("HTTP_X_FORWARDED_FOR")
    if x_forwarded_for:
        ip = x_forwarded_for.split(",")[-1].strip()
    else:
        ip = request.META.get("REMOTE_ADDR")
    return ip


def is_public_user(request):
    """
    Is the session connector created for public user?

    Returns None if no connector found
    """
    connector = request.session.get("connector")
    if connector is not None:
        return connector.is_public


class ConnCleaningHttpResponse(StreamingHttpResponse):
    """Extension of L{HttpResponse} which closes the Bhojpur ODE connection."""

    def close(self):
        super(ConnCleaningHttpResponse, self).close()
        try:
            logger.debug("Closing Bhojpur ODE connection in %r" % self)
            if self.conn is not None and self.conn.c is not None:
                self.conn.close(hard=False)
        except Exception:
            logger.error("Failed to clean up connection.", exc_info=True)


class TableClosingHttpResponse(ConnCleaningHttpResponse):
    """Extension of L{HttpResponse} which closes the Bhojpur ODE connection."""

    def close(self):
        try:
            if self.table is not None:
                self.table.close()
        except Exception:
            logger.error("Failed to close ODE.table.", exc_info=True)
        # Now call super to close conn
        super(TableClosingHttpResponse, self).close()


class login_required(object):
    """
    ODE.web specific extension of the Django login_required() decorator,
    https://docs.djangoproject.com/en/dev/topics/auth/, which is responsible
    for ensuring a valid L{engine.gateway.OdeGateway} connection. Is
    configurable by various options.

    doConnectionCleanup:
        Used to indicate methods that may return ConnCleaningHttpResponse.
        If True (default), then returning a ConnCleaningHttpResponse will
        raise an Exception since cleanup is intended to be immediate; if
        False, connection cleanup will be skipped ONLY when a
        ConnCleaningHttpResponse is returned.
    """

    def __init__(
        self,
        useragent="ODE.web",
        isAdmin=False,
        isGroupOwner=False,
        doConnectionCleanup=True,
        ode_group="-1",
        allowPublic=None,
    ):
        """
        Initialises the decorator.
        """
        self.useragent = useragent
        self.isAdmin = isAdmin
        self.isGroupOwner = isGroupOwner
        self.doConnectionCleanup = doConnectionCleanup
        self.ode_group = ode_group
        self.allowPublic = allowPublic

    # To make django's method_decorator work, this is required until
    # python/django sort out how argumented decorator wrapping should work
    def __getattr__(self, name):
        if name == "__name__":
            return self.__class__.__name__
        else:
            return super(login_required, self).getattr(name)

    def get_login_url(self):
        """The URL that should be redirected to if not logged in."""
        return reverse(settings.LOGIN_VIEW)

    login_url = property(get_login_url)

    def get_share_connection(self, request, conn, share_id):
        try:
            conn.SERVICE_OPTS.setOdeShare(share_id)
            conn.getShare(share_id)
            return conn
        except Exception:
            logger.error("Error activating share.", exc_info=True)
            return None

    def prepare_share_connection(self, request, conn, share_id):
        """Prepares the share connection if we have a valid share ID."""
        # we always need to clear any dirty 'engine.share' values from previous
        # calls
        conn.SERVICE_OPTS.setOdeShare()
        if share_id is None:
            return None
        share = conn.getShare(share_id)
        try:
            if share.getOwner().id != conn.getUserId():
                if share.active and not share.isExpired():
                    return self.get_share_connection(request, conn, share_id)
                logger.debug("Share is unavailable.")
                return None
        except Exception:
            logger.error("Error retrieving share connection.", exc_info=True)
            return None

    def on_not_logged_in(self, request, url, error=None):
        """Called whenever the user is not logged in."""
        if request.is_ajax():
            logger.debug("Request is Ajax, returning HTTP 403.")
            return HttpResponseForbidden()

        try:
            for lookup_view in settings.LOGIN_REDIRECT["redirect"]:
                try:
                    if url == reverse(lookup_view):
                        url = parse_url(settings.LOGIN_REDIRECT)
                except NoReverseMatch:
                    try:
                        resolve(lookup_view)
                        if url == lookup_view:
                            url = parse_url(settings.LOGIN_REDIRECT)
                    except Http404:
                        logger.error("Cannot resolve url %s" % lookup_view)
        except KeyError:
            pass
        except Exception:
            logger.error("Error while redirection on not logged in.", exc_info=True)

        args = {"url": url}

        logger.debug(
            "Request is not Ajax, redirecting to %s?%s"
            % (self.login_url, urlencode(args))
        )
        return HttpResponseRedirect("%s?%s" % (self.login_url, urlencode(args)))

    def on_logged_in(self, request, conn):
        """
        Called whenever the users is successfully logged in.
        Sets the 'engine.group' option if specified in the constructor
        """
        if self.ode_group is not None:
            conn.SERVICE_OPTS.setOdeGroup(self.ode_group)

    def on_share_connection_prepared(self, request, conn_share):
        """Called whenever a share connection is successfully prepared."""
        pass

    def verify_is_admin(self, conn):
        """
        If we have been requested to by the isAdmin flag, verify the user
        is an admin and raise an exception if they are not.
        """
        if self.isAdmin and not conn.isAdmin():
            raise Http404

    def verify_is_group_owner(self, conn, gid):
        """
        If we have been requested to by the isGroupOwner flag, verify the user
        is the owner of the provided group. If no group is provided the user's
        active session group ownership will be verified.
        """
        if not self.isGroupOwner:
            return
        if gid is not None:
            if not conn.isLeader(gid):
                raise Http404
        else:
            if not conn.isLeader():
                raise Http404

    def is_valid_public_url(self, server_id, request):
        """
        Verifies that the URL for the resource being requested falls within
        the scope of the ODE.webpublic URL filter.
        """
        if settings.PUBLIC_ENABLED:
            if not hasattr(settings, "PUBLIC_USER"):
                logger.warn(
                    "ODE.webpublic enabled but public user "
                    "(engine.web.public.user) not set, disabling "
                    "ODE.webpublic."
                )
                settings.PUBLIC_ENABLED = False
                return False
            if not hasattr(settings, "PUBLIC_PASSWORD"):
                logger.warn(
                    "ODE.webpublic enabled but public user "
                    "password (engine.web.public.password) not set, "
                    "disabling ODE.webpublic."
                )
                settings.PUBLIC_ENABLED = False
                return False
            if settings.PUBLIC_GET_ONLY and (request.method != "GET"):
                return False
            if self.allowPublic is None:
                return settings.PUBLIC_URL_FILTER.search(request.path) is not None
            return self.allowPublic
        return False

    def load_server_settings(self, conn, request):
        """Loads Client preferences and Read-Only status from the server."""
        try:
            request.session["can_create"]
        except KeyError:
            request.session.modified = True
            request.session["can_create"] = conn.canCreate()
        try:
            request.session["server_settings"]
        except Exception:
            request.session.modified = True
            request.session["server_settings"] = {}
            try:
                request.session["server_settings"] = propertiesToDict(
                    conn.getClientSettings(), prefix="engine.client."
                )
            except Exception:
                logger.error(traceback.format_exc())
            # make extra call for engine.mail, not a part of engine.client
            request.session["server_settings"]["email"] = conn.getEmailSettings()

    def get_public_user_connector(self):
        """
        Returns the current cached ODE.webpublic connector or None if
        nothing has been cached.
        """
        if not settings.PUBLIC_CACHE_ENABLED:
            return
        return cache.get(settings.PUBLIC_CACHE_KEY)

    def set_public_user_connector(self, connector):
        """Sets the current cached ODE.webpublic connector."""
        if not settings.PUBLIC_CACHE_ENABLED or connector.ode_session_key is None:
            return
        logger.debug("Setting ODE.webpublic connector: %r" % connector)
        cache.set(settings.PUBLIC_CACHE_KEY, connector, settings.PUBLIC_CACHE_TIMEOUT)

    def get_connection(self, server_id, request):
        """
        Prepares a Bhojpur ODE connection wrapper (from L{engine.gateway}) for
        use with a view function.
        """
        connection = self.get_authenticated_connection(server_id, request)
        is_valid_public_url = self.is_valid_public_url(server_id, request)
        logger.debug("Is valid public URL? %s" % is_valid_public_url)
        if connection is None and is_valid_public_url:
            # If Bhojpur ODE web public is enabled, pick up a username and
            # password from configuration and use those credentials to
            # create a connection.
            logger.debug(
                "ODE.webpublic enabled, attempting to login "
                "with configuration supplied credentials."
            )
            if server_id is None:
                server_id = settings.PUBLIC_SERVER_ID
            username = settings.PUBLIC_USER
            password = settings.PUBLIC_PASSWORD
            is_secure = settings.SECURE
            logger.debug("Is SSL? %s" % is_secure)
            # Try and use a cached ODE.webpublic user session key.
            public_user_connector = self.get_public_user_connector()
            if public_user_connector is not None:
                logger.debug(
                    "Attempting to use cached ODE.webpublic "
                    "connector: %r" % public_user_connector
                )
                connection = public_user_connector.join_connection(self.useragent)
                if connection is not None:
                    request.session["connector"] = public_user_connector
                    logger.debug(
                        "Attempt to use cached ODE.web public "
                        "session key successful!"
                    )
                    return connection
                logger.debug(
                    "Attempt to use cached ODE.web public " "session key failed."
                )
            # We don't have a cached ODE.webpublic user session key,
            # create a new connection based on the credentials we've been
            # given.
            connector = Connector(server_id, is_secure)
            connection = connector.create_connection(
                self.useragent,
                username,
                password,
                is_public=True,
                userip=get_client_ip(request),
            )
            request.session["connector"] = connector
            # Clear any previous context so we don't try to access this
            # NB: we also do this in WebclientLoginView.handle_logged_in()
            if "active_group" in request.session:
                del request.session["active_group"]
            if "user_id" in request.session:
                del request.session["user_id"]
            request.session.modified = True
            self.set_public_user_connector(connector)
        elif connection is not None:
            is_anonymous = connection.isAnonymous()
            logger.debug("Is anonymous? %s" % is_anonymous)
            if is_anonymous and not is_valid_public_url:
                if connection.c is not None:
                    logger.debug("Closing anonymous connection")
                    connection.close(hard=False)
                return None
        return connection

    def get_authenticated_connection(self, server_id, request):
        """
        Prepares an authenticated Bhojpur ODe connection wrapper (from
        L{engine.gateway}) for use with a view function.
        """
        # TODO: Handle previous try_super logic; is it still needed?

        userip = get_client_ip(request)
        session = request.session
        request = request.GET
        is_secure = settings.SECURE
        logger.debug("Is SSL? %s" % is_secure)
        connector = session.get("connector", None)
        logger.debug("Connector: %s" % connector)

        if server_id is None:
            # If no server id is passed, the db entry will not be used and
            # instead we'll depend on the request.session and request.GET
            # values
            if connector is not None:
                server_id = connector.server_id
            else:
                try:
                    server_id = request["server"]
                except Exception:
                    logger.debug("No Server ID available.")
                    return None

        # If we have a Bhojpur ODE session key in our request variables attempt
        # to make a connection based on those credentials.
        try:
            ode_session_key = request["bsession"]
            connector = Connector(server_id, is_secure)
        except KeyError:
            # We do not have a Bhojpur ODE session key in the current request.
            pass
        else:
            # We have a Bhojpur ODE session key in the current request use it
            # to try join an existing connection / Bhojpur ODE session.
            logger.debug(
                "Have Bhojpur ODE session key %s, attempting to join..." % ode_session_key
            )
            connector.user_id = None
            connector.ode_session_key = ode_session_key
            connection = connector.join_connection(self.useragent, userip)
            session["connector"] = connector
            return connection

        # A Bhojpur ODE session is not available, we're either trying to service
        # a request to a login page or an anonymous request.
        username = None
        password = None
        try:
            username = request["username"]
            password = request["password"]
        except KeyError:
            if connector is None:
                logger.debug("No username or password in request, exiting.")
                # We do not have a Bhojpur ODE session or a username and password
                # in the current request and we do not have a valid connector.
                # Raise an error (return None).
                return None

        if username is not None and password is not None:
            # We have a username and password in the current request, or
            # ODE.webpublic is enabled and has provided us with a username
            # and password via configureation. Use them to try and create a
            # new connection / Bhojpur ODE session.
            logger.debug("Creating connection with username and password...")
            connector = Connector(server_id, is_secure)
            connection = connector.create_connection(
                self.useragent, username, password, userip=userip
            )
            session["connector"] = connector
            return connection

        logger.debug("Django session connector: %r" % connector)
        if connector is not None:
            # We have a connector, attempt to use it to join an existing
            # connection / Bhojpur ODE session.
            connection = connector.join_connection(self.useragent, userip)
            if connection is not None:
                logger.debug("Connector valid, session successfully joined.")
                return connection
            # Fall through, we the session we've been asked to join may
            # be invalid and we may have other credentials as request
            # variables.
            logger.debug("Connector is no longer valid, destroying...")
            del session["connector"]
            return None

        session["connector"] = connector
        return None

    def __call__(ctx, f):
        """
        Tries to prepare a logged in connection, then calls function and
        returns the result.
        """

        def wrapped(request, *args, **kwargs):
            url = request.GET.get("url")
            if url is None or len(url) == 0:
                url = request.get_full_path()

            doConnectionCleanup = False

            conn = kwargs.get("conn", None)
            error = None
            server_id = kwargs.get("server_id", None)
            # Short circuit connection retrieval when a connection was
            # provided to us via 'conn'. This is useful when in testing
            # mode or when stacking view functions/methods.
            if conn is None:
                doConnectionCleanup = ctx.doConnectionCleanup
                logger.debug("Connection not provided, attempting to get one.")
                try:
                    conn = ctx.get_connection(server_id, request)
                except Exception as x:
                    logger.error("Error retrieving connection.", exc_info=True)
                    error = str(x)
                else:
                    # various configuration & checks only performed on new
                    # 'conn'
                    if conn is None:
                        return ctx.on_not_logged_in(request, url, error)
                    else:
                        ctx.on_logged_in(request, conn)
                    ctx.verify_is_admin(conn)
                    ctx.verify_is_group_owner(conn, kwargs.get("gid"))
                    ctx.load_server_settings(conn, request)

                    share_id = kwargs.get("share_id")
                    conn_share = ctx.prepare_share_connection(request, conn, share_id)
                    if conn_share is not None:
                        ctx.on_share_connection_prepared(request, conn_share)
                        kwargs["conn"] = conn_share
                    else:
                        kwargs["conn"] = conn

                    # kwargs['error'] = request.GET.get('error')
                    kwargs["url"] = url
            retval = None
            try:
                retval = f(request, *args, **kwargs)
            finally:
                # If f() raised Exception, e.g. Http404() we must still cleanup
                delayConnectionCleanup = isinstance(retval, ConnCleaningHttpResponse)
                if doConnectionCleanup and delayConnectionCleanup:
                    raise ApiUsageException(
                        "Methods that return a"
                        " ConnCleaningHttpResponse must be marked with"
                        " @login_required(doConnectionCleanup=False)"
                    )
                doConnectionCleanup = not delayConnectionCleanup
                logger.debug("Doing connection cleanup? %s" % doConnectionCleanup)
                try:
                    if doConnectionCleanup:
                        if conn is not None and conn.c is not None:
                            conn.close(hard=False)
                except Exception:
                    logger.warn("Failed to clean up connection", exc_info=True)
            return retval

        return update_wrapper(wrapped, f)


class render_response(object):
    """
    This decorator handles the rendering of view methods to HttpResponse. It
    expects that wrapped view methods return a dict. This allows:
    - The template to be specified in the method arguments OR within the view
      method itself
    - The dict to be returned as json if required
    - The request is passed to the template context, as required by some tags
      etc
    - A hook is provided for adding additional data to the context, from the
      L{engine.gateway.OdeGateway} or from the request.
    """

    # To make django's method_decorator work, this is required until
    # python/django sort out how argumented decorator wrapping should work
    def __getattr__(self, name):
        if name == "__name__":
            return self.__class__.__name__
        else:
            return super(render_response, self).getattr(name)

    def prepare_context(self, request, context, *args, **kwargs):
        """Hook for adding additional data to the context dict"""
        context["html"] = context.get("html", {})
        context["html"]["meta_referrer"] = settings.HTML_META_REFERRER

    def __call__(ctx, f):
        """Here we wrap the view method f and return the wrapped method"""

        def wrapper(request, *args, **kwargs):
            """
            Wrapper calls the view function, processes the result and returns
            HttpResponse"""

            # call the view function itself...
            context = f(request, *args, **kwargs)

            # if we happen to have a Response, return it
            if isinstance(context, HttpResponseBase):
                return context

            # get template from view dict. Can be overridden from the **kwargs
            template = "template" in context and context["template"] or None
            template = kwargs.get("template", template)
            logger.debug("Rendering template: %s" % template)

            # allows us to return the dict as json  (NB: OdeGateway objects
            # don't serialize)
            if template is None or template == "json":
                # We still need to support non-dict data:
                safe = type(context) is dict
                return JsonResponse(context, safe=safe)
            else:
                # allow additional processing of context dict
                ctx.prepare_context(request, context, *args, **kwargs)
                return render(request, template, context)

        return update_wrapper(wrapper, f)