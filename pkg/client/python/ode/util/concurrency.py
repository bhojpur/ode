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

# Bhojpur ODE Concurrency Utilities

import atexit
import logging
import threading
import ode.util
import logging.handlers

try:
    from threading import _Event
    from threading import _Timer
except ImportError:
    # Python3
    from threading import Event as _Event
    from threading import Timer as _Timer

def get_event(name="Unknown"):
    """
    Returns a threading.Event instance which is registered to be
    "set" (Event.set()) on system exit.
    """
    event = AtExitEvent(name=name)
    atexit.register(event.setAtExit)
    return event

class AtExitEvent(_Event):

    """
    threading.Event extension which provides an additional method
    setAtExit() which sets "atexit" to true.

    This class was introduced in 4.2.1 to work around issue #3260
    in which logging from background threads produced error
    messages.
    """

    def __init__(self, verbose=None, name="Unknown"):
        try:
            super(AtExitEvent, self).__init__(verbose)
        except TypeError:
            # in Python 3 there is no verbose argument
            super(AtExitEvent, self).__init__()
        self.__name = name
        self.__atexit = False

    name = property(lambda self: self.__name)
    atexit = property(lambda self: self.__atexit)

    def setAtExit(self):
        self.__atexit = True
        super(AtExitEvent, self).set()

    def __repr__(self):
        return "%s (%s)" % (super(AtExitEvent, self).__repr__(), self.__name)

class Timer(_Timer):

    """Based on threading._Thread but allows for resetting the Timer.

    t = Timer(30.0, f, args=[], kwargs={})
    t.start()
    t.cancel() # stop the timer's action if it's still waiting

    # or

    t.reset()

    After excecution, the status of the run can be checked via the
    "completed" and the "exception" Event instances.
    """

    def __init__(self, interval, function, args=None, kwargs=None):
        if args is None:
            args = []
        if kwargs is None:
            kwargs = {}
        _Timer.__init__(self, interval, function, args, kwargs)
        self.log = logging.getLogger(ode.util.make_logname(self))
        self.completed = threading.Event()
        self.exception = threading.Event()
        self._reset = threading.Event()

    def reset(self):
        self.log.debug("Reset called")
        self._reset.set()  # Set first, so that the loop will continue
        self.finished.set()  # Forces waiting thread to fall through

    def run(self):
        while True:
            self.finished.wait(self.interval)
            if self._reset.isSet():
                self.finished.clear()
                self._reset.clear()
                self.log.debug("Resetting")
                continue
            if not self.finished.isSet():
                try:
                    self.log.debug("Executing")
                    self.function(*self.args, **self.kwargs)
                    self.completed.set()
                    self.finished.set()
                except:
                    self.exception.set()
                    self.finished.set()
                    raise
            break