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

# Bhojpur ODE Decorators

from builtins import object
import time
import logging
import traceback

import ode

from functools import wraps

perf_log = logging.getLogger("ode.perf")

def perf(func):
    """ Decorator for (optionally) printing performance statistics """
    def handler(*args, **kwargs):

        # Early Exit. Can't do this in up a level
        # because logging hasn't been configured yet.
        lvl = perf_log.getEffectiveLevel()
        if lvl > logging.DEBUG:
            return func(*args, **kwargs)

        try:
            self = args[0]
            mod = self.__class__.__module__
            cls = self.__class__.__name__
            tag = "%s.%s.%s" % (mod, cls, func.__name__)
        except:
            tag = func.__name__
        start = time.time()
        try:
            rv = func(*args, **kwargs)
            return rv
        finally:
            stop = time.time()
            diff = stop - start
            startMillis = int(start * 1000)
            timeMillis = int(diff * 1000)
            perf_log.debug(
                "start[%d] time[%d] tag[%s]", startMillis, timeMillis, tag)
    handler = wraps(func)(handler)
    return handler

__FORMAT = "%-.120s"
__RESULT = " Rslt: " + __FORMAT
__EXCEPT = " Excp: " + __FORMAT

def remoted(func):
    """
    Decorator for catching any uncaught exception
    and converting it to an InternalException
    """
    log = logging.getLogger("ode.remote")

    def exc_handler(*args, **kwargs):
        try:
            self = args[0]
            log.info(" Meth: %s.%s", self.__class__.__name__, func.__name__)
            rv = func(*args, **kwargs)
            log.info(__RESULT, rv)
            return rv
        except Exception as e:
            log.info(__EXCEPT, e)
            if isinstance(e, ode.ServerError):
                raise
            else:
                log.warn(
                    "%s raised a non-ServerError (%s): %s", func, type(e), e)
                msg = traceback.format_exc()
                raise ode.InternalException(msg, None, "Internal exception")
    exc_handler = wraps(func)(exc_handler)
    return exc_handler

def locked(func):
    """ Decorator for using the self._lock argument of the calling instance """
    def with_lock(*args, **kwargs):
        self = args[0]
        self._lock.acquire()
        try:
            return func(*args, **kwargs)
        finally:
            self._lock.release()
    with_lock = wraps(func)(with_lock)
    return with_lock

class TimeIt (object):

    """
    Decorator to measure the execution time of a function. Assumes that a
    logger global var
    is available and is the logger instance from :meth:`logging.getLogger`.

    @param level: the level to use for logging
    @param name: the name to use when logging, function name is used if None
    """
    logger = logging.getLogger('ode.timeit')

    def __init__(self, level=logging.DEBUG, name=None):
        self._level = level
        self._name = name

    def __call__(self, func):
        def wrapped(*args, **kwargs):
            name = self._name or func.__name__
            self.logger.log(self._level, "timing %s" % (name))
            now = time.time()
            rv = func(*args, **kwargs)
            self.logger.log(self._level, "timed %s: %f" %
                            (name, time.time() - now))
            return rv
        return wrapped

def timeit(func):
    """
    Shortcut version of the :class:`TimeIt` decorator class.
    Logs at logging.DEBUG level.
    """
    def wrapped(*args, **kwargs):
        TimeIt.logger.log(logging.DEBUG, "timing %s" % (func.__name__))
        now = time.time()
        rv = func(*args, **kwargs)
        TimeIt.logger.log(logging.DEBUG, "timed %s: %f" %
                          (func.__name__, time.time() - now))
        return rv
    return TimeIt()(func)

def setsessiongroup(func):
    """
    For OdeObjectWrapper class derivate functions,
    sets the session group to match the object group.
    """

    def wrapped(self, *args, **kwargs):
        rev = self._conn.setGroupForSession(
            self.getDetails().getGroup().getId())
        try:
            return func(self, *args, **kwargs)
        finally:
            if rev:
                self._conn.revertGroupForSession()
    return wrapped