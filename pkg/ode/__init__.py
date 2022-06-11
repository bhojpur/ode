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

"""
   Primary Bhojpur ODE types

   Classes:
      core.client    -- Main connector object
"""

from ode_version import ode_version
from ode_version import ice_compatibility as compat
import Ice
import os

_sys = __import__("sys")

try:
    vers = Ice.stringVersion()
    vers = vers.split(".")
    compat = compat.split(".")
    if compat[0:2] != vers[0:2]:
        msg = """

        ERROR: Zero-C Ice version mismatch!

        Your Bhojpur ODE code has been compiled using Ice version %s
        but you seem to have Ice version %s installed. If you need
        help understanding this issue, please send this error message
        to the Bhojpur ODE community:

        https://desk.bhojpur-consulting.com/

        Debugging Info:
        --------------
        VERSION=%s
        PYTHONPATH=%s
        """ % (".".join(compat), ".".join(vers), ode_version,
               os.path.pathsep.join(_sys.path))
        raise Exception(msg)
finally:
    del ode_version
    del compat
    del vers
    del Ice
    del os

__import_style__ = None

def client_wrapper(*args, **kwargs):
    """
    Returns an instance of :class:`ode.gateway.OdeGateway` created with all
    arguments passed to this method

    @return:    See above
    """
    import core.gateway
    return core.gateway.OdeGateway(*args, **kwargs)

def client(*args, **kwargs):
    import ode.clients
    return ode.clients.BaseClient(*args, **kwargs)

class ClientError(Exception):
    """
    Top of client exception hierarchy.
    """
    pass

class CmdError(ClientError):
    """
    Thrown by ode.client.waitOnCmd() when
    failonerror is True and an ode.cmd.ERR
    is returned. The only argument
    """

    def __init__(self, err, *args, **kwargs):
        ClientError.__init__(self, *args, **kwargs)
        self.err = err

    def __str__(self):
        sb = ClientError.__str__(self)
        sb += "\n"
        sb += str(self.err)
        return sb

class UnloadedEntityException(ClientError):
    pass

class UnloadedCollectionException(ClientError):
    pass

def proxy_to_instance(proxy_string, default=None):
    """
    Convert a proxy string to an instance. If no
    default is provided, the string must be of the
    form: 'Image:1' or 'ImageI:1'. With a default,
    a string consisting of just the ID is permissible
    but not required.
    """
    import ode
    parts = proxy_string.split(":")
    if len(parts) == 1 and default is not None:
        proxy_string = "%s:%s" % (default, proxy_string)
        parts.insert(0, default)

    kls = parts[0]
    if not kls.endswith("I"):
        kls += "I"
    kls = getattr(ode.model, kls, None)
    if kls is None:
        raise ClientError(("Invalid proxy string: %s. "
                          "Correct format is Class:ID") % proxy_string)
    return kls(proxy_string)

# Workaround for warning messages produced in
# code-generated Ice files.
#
if _sys.version_info[:2] == (2, 6):
    import warnings
    warnings.filterwarnings(
        action='ignore',
        message='BaseException.message has been deprecated as of Python 2.6',
        category=DeprecationWarning)