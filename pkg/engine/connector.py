#!/usr/bin/env python
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

import re
import logging

from django.utils.encoding import force_text
from future.utils import with_metaclass

from ode import client_wrapper
from engine.version import engine_version as ode_version

logger = logging.getLogger(__name__)

class IterRegistry(type):
    def __new__(cls, name, bases, attr):
        attr["_registry"] = {}
        attr["_frozen"] = False
        return type.__new__(cls, name, bases, attr)

    def __iter__(cls):
        return iter(cls._registry.values())


# with_metaclass to support python2 and python3
class ServerBase(with_metaclass(IterRegistry)):
    _next_id = 1

    def __init__(self, host, port, server=None):
        if hasattr(self, "host") or hasattr(self, "port"):
            return
        self.id = type(self)._next_id
        self.host = host
        self.port = port
        self.server = (server is not None and server != "") and server or None
        type(self)._registry[self.id] = self
        type(self)._next_id += 1

    def __new__(cls, host, port, server=None):
        for key in cls._registry:
            val = cls._registry[key]
            if val.host == host and val.port == port:
                return cls._registry[key]

        if cls._frozen:
            raise TypeError("No more instances allowed")
        else:
            return object.__new__(cls)

    @classmethod
    def instance(cls, pk):
        if pk in cls._registry:
            return cls._registry[pk]
        return None

    @classmethod
    def freeze(cls):
        cls._frozen = True

    @classmethod
    def reset(cls):
        cls._registry = {}
        cls._frozen = False
        cls._next_id = 1


class Server(ServerBase):
    def __repr__(self):
        """
        Json for printin settings.py: [["localhost", 4064, "ode"]]'
        """
        return """["%s", %s, "%s"]""" % (self.host, self.port, self.server)

    def __str__(self):
        return force_text(self).encode("utf-8")

    def __unicode__(self):
        return str(self.id)

    @classmethod
    def get(cls, pk):
        r = None
        try:
            pk = int(pk)
        except Exception:
            pass
        else:
            if pk in cls._registry:
                r = cls._registry[pk]
        return r

    @classmethod
    def find(cls, host=None, port=None, server=None):
        rv = []
        for s in cls._registry.values():
            if (
                (host is not None and host != s.host)
                or (port is not None and port != s.port)
                or (server is not None and server != s.server)
            ):
                continue
            rv.append(s)
        return rv


class Connector(object):
    """
    Object which encompasses all of the logic related to a Bhojpur ODE connection
    and its status with respect to ODE.web.
    """

    SERVER_VERSION_RE = re.compile("^.*?[-]?(\\d+[.]\\d+([.]\\d+)?)[-]?.*?$")

    def __init__(self, server_id, is_secure):
        self.server_id = server_id
        self.is_secure = is_secure
        self.is_public = False
        self.ode_session_key = None
        self.user_id = None

    def lookup_host_and_port(self):
        server = Server.get(self.server_id)
        if server is None:
            server = Server.find(server=self.server_id)[0]
        return (server.host, server.port)

    def create_gateway(self, useragent, username=None, password=None, userip=None):
        host, port = self.lookup_host_and_port()
        return client_wrapper(
            username,
            password,
            host=host,
            port=port,
            secure=self.is_secure,
            useragent=useragent,
            anonymous=self.is_public,
            userip=userip,
        )

    def prepare_gateway(self, connection):
        connection.server_id = self.server_id
        # Lazy import due to the potential usage of the decorator in
        # the engine.webgateway.views package.
        # TODO: UserProxy needs to be moved to this package or similar
        from engine.webgateway.views import UserProxy

        connection.user = UserProxy(connection)
        connection.user.logIn()
        self.ode_session_key = connection._sessionUuid
        self.user_id = connection.getUserId()
        logger.debug("Successfully prepared gateway: %s" % self.ode_session_key)
        # TODO: Properly handle activating the webode_cache

    def create_connection(
        self, useragent, username, password, is_public=False, userip=None
    ):
        self.is_public = is_public
        connection = self.create_gateway(useragent, username, password, userip)
        try:
            if connection.connect():
                logger.debug("Successfully created connection for: %s" % username)
                self.prepare_gateway(connection)
                return connection
        except Exception:
            logger.debug("Cannot create a new connection.", exc_info=True)
        connection.close()
        return None

    def create_guest_connection(self, useragent, is_public=False):
        guest = "guest"
        connection = self.create_gateway(useragent, guest, guest, None)
        try:
            if connection.connect():
                logger.debug("Successfully created a guest connection.")
                return connection
            else:
                logger.warn("Cannot create a guest connection.")
        except Exception:
            logger.error("Cannot create a guest connection.", exc_info=True)
        connection.close()
        return None

    def join_connection(self, useragent, userip=None):
        connection = self.create_gateway(useragent, userip=userip)
        try:
            if connection.connect(sUuid=self.ode_session_key):
                logger.debug(
                    "Successfully joined connection: %s" % self.ode_session_key
                )
                connection.setUserId(self.user_id)
                self.prepare_gateway(connection)
                return connection
        except Exception:
            logger.debug("Cannot create a new connection.", exc_info=True)
        connection.close()
        return None

    def is_server_up(self, useragent):
        connection = self.create_guest_connection(useragent)
        if connection is None:
            return False
        try:
            try:
                connection.getServerVersion()
                return True
            except Exception:
                logger.error("Cannot request server version.", exc_info=True)
            return False
        finally:
            connection.close()

    def check_version(self, useragent):
        """
        Check whether the server version is compatible with this client
        """
        connection = self.create_guest_connection(useragent)
        if connection is None:
            return False
        try:
            try:
                server_version = connection.getServerVersion()
                server_version = self.SERVER_VERSION_RE.match(server_version)
                server_version = server_version.group(1).split(".")

                client_version = self.SERVER_VERSION_RE.match(ode_version)
                client_version = client_version.group(1).split(".")
                logger.info(
                    "Client version: '%s'; Server version: '%s'"
                    % (client_version, server_version)
                )
                return self.is_compatible(server_version, client_version)
            except Exception:
                logger.error("Cannot compare server to client version.", exc_info=True)
            return False
        finally:
            connection.close()

    @staticmethod
    def is_compatible(server_version, client_version):
        # Compatibility is determined by matching the major version
        # If ODE moves to semver only the first element will need
        # to be checked
        if server_version[0] != client_version[0]:
            return False
        # Currently, web 5.6+ is compatible with server 5.5+
        if client_version[0] == "5" and int(client_version[1]) >= 6:
            return int(server_version[1]) >= 5
        return server_version[:2] == client_version[:2]