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

# webclient_gateway

from builtins import str
from future.utils import native_str
from past.builtins import basestring
from builtins import object
import logging
import json

try:
    long
except:
    # Python 3
    long = int

logger = logging.getLogger(__name__)

class GatewayConfig(object):

    """
    Global Gateway configuration:

    - :attr:`IMG_RDEFNS`:  a namespace for annotations linked on images holding
                           the default rendering settings object id.
    - :attr:`IMG_ROPTSNS`: a namespace for annotations linked on images holding
                           default rendering options that don't get saved in
                           the rendering settings.
    """

    def __init__(self):
        self.IMG_RDEFNS = None
        self.IMG_ROPTSNS = None

class ServiceOptsDict(dict):

    def __new__(cls, *args, **kwargs):
        return super(ServiceOptsDict, cls).__new__(cls, *args, **kwargs)

    def __init__(self, data=None, *args, **kwargs):
        if data is None:
            data = dict()
        if len(kwargs) > 0:
            for key, val in dict(*args, **kwargs).items():
                self[key] = val
        if isinstance(data, dict):
            for key in data:
                item = data[key]
                if self._testItem(item):
                    self[key] = str(item)
                else:
                    logger.debug(
                        "None or non- string, unicode or numeric type"
                        "values are ignored, (%r, %r)" % (key, item))
        else:
            raise AttributeError(
                "%s argument (%r:%s) must be a dictionary"
                % (self.__class__.__name__, data, type(data)))

    def __repr__(self):
        return "<%s: %s>" % (self.__class__.__name__,
                             super(ServiceOptsDict, self).__repr__())

    def __setitem__(self, key, item):
        """Set key to value as string."""
        if self._testItem(item):
            super(ServiceOptsDict, self).__setitem__(key, native_str(item))
            logger.debug("Setting %r to %r" % (key, item))
        else:
            raise AttributeError(
                "%s argument (%r:%s) must be a string, unicode or numeric type"
                % (self.__class__.__name__, item, type(item)))

    def __getitem__(self, key):
        """
        Return the value for key if key is in the dictionary.
        Raises a KeyError if key is not in the map.
        """
        try:
            return super(ServiceOptsDict, self).__getitem__(key)
        except KeyError:
            raise KeyError("Key %r not found in %r" % (key, self))

    def __delitem__(self, key):
        """
        Remove dict[key] from dict.
        Raises a KeyError if key is not in the map.
        """
        super(ServiceOptsDict, self).__delitem__(key)
        logger.debug("Deleting %r" % (key))

    def copy(self):
        """Returns a copy of this object."""
        return self.__class__(self)

    def clear(self):
        """Remove all items from the dictionary."""
        super(ServiceOptsDict, self).clear()

    def get(self, key, default=None):
        """
        Return the value for key if key is in the dictionary, else default.
        If default is not given, it defaults to None, so that this method
        never raises a KeyError.
        """
        try:
            return self.__getitem__(key)
        except KeyError:
            return default

    def set(self, key, value):
        """Set key to value as string."""
        return self.__setitem__(key, value)

    def getOdeGroup(self):
        return self.get('ode.group')

    def setOdeGroup(self, value=None):
        if value is not None:
            self.set('ode.group', value)
        else:
            try:
                del self['ode.group']
            except KeyError:
                logger.debug("Key 'ode.group' not found in %r" % self)

    def getOdeUser(self):
        return self.get('ode.user')

    def setOdeUser(self, value=None):
        if value is not None:
            self.set('ode.user', value)
        else:
            try:
                del self['ode.user']
            except KeyError:
                logger.debug("Key 'ode.user' not found in %r" % self)

    def getOdeShare(self):
        return self.get('ode.share')

    def setOdeShare(self, value=None):
        if value is not None:
            self.set('ode.share', value)
        else:
            try:
                del self['ode.share']
            except KeyError:  # pragma: no cover
                logger.debug("Key 'ode.share' not found in %r" % self)

    def _testItem(self, item):
        if item is not None and not isinstance(item, bool) and \
            (isinstance(item, basestring) or
             isinstance(item, int) or
             isinstance(item, long) or
             isinstance(item, float)):
            return True
        return False

def toBoolean(val):
    """
    Get the boolean value of the provided input.

        If the value is a boolean return the value.
        Otherwise check to see if the value is in
        ["true", "yes", "y", "t", "1"]
        and returns True if value is in the list
    """

    if val is True or val is False:
        return val

    trueItems = ["true", "yes", "y", "t", "1", "on"]

    return str(val).strip().lower() in trueItems

def propertiesToDict(m, prefix=None):
    """
    Convert Bhojpur ODE properties to nested dictionary, skipping common prefix
    """

    nested_dict = {}
    for item, value in m.items():
        d = nested_dict
        if prefix is not None:
            item = item.replace(prefix, "")
        items = item.split('.')
        for key in items[:-1]:
            d = d.setdefault(key, {})
        try:
            if value.strip().lower() in ('true', 'false'):
                d[items[-1]] = toBoolean(value)
            else:
                d[items[-1]] = json.loads(value)
        except:
            d[items[-1]] = value
    return nested_dict