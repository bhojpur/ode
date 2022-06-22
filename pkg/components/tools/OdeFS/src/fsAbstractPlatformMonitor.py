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
    ODE.fs Abstract Monitor module.
"""
import threading

class AbstractPlatformMonitor(threading.Thread):

    """
        A Thread to monitor a path.

        :group Constructor: __init__
        :group Other methods: run, stop
    """

    def __init__(self, eventTypes, pathMode, pathString, whitelist, blacklist,
                 ignoreSysFiles, ignoreDirEvents, proxy):
        """
            Set-up Monitor thread.
        """
        threading.Thread.__init__(self)

        self.eTypes = []
        for eT in eventTypes:
            self.eTypes.append(str(eT))

        self.pathMode = str(pathMode)
        self.pathsToMonitor = pathString
        self.whitelist = whitelist
        self.blacklist = blacklist
        self.ignoreSysFiles = ignoreSysFiles
        self.ignoreDirEvents = ignoreDirEvents
        self.proxy = proxy

    def run(self):
        """
            Start monitoring.

            :return: No explicit return value.
        """
        # pass

    def stop(self):
        """
            Stop monitoring

            :return: No explicit return value.
        """
        # pass

    def propagateEvents(self, eventList):
        """
            Propagate events to proxy.

            :Parameters:

                eventPath : List
                    events.

            :return: No explicit return value.

        """
        if len(eventList) > 0:
            try:
                self.log.info('Event notification : %s', str(eventList))
                self.proxy.callback(eventList)
            except:
                self.log.exception("Notification failed : ")
        else:
            self.log.info('No notifications propagated')
