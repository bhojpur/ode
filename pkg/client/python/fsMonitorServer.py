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
    ODE.fs MonitorServer module.
"""
import logging

import uuid

from fsMonitor import MonitorFactory

import ode.all
import ode.grid.monitors as monitors

class MonitorServerI(monitors.MonitorServer):

    """
        Co-ordinates a number of Monitors

        :group Constructor: __init__
        :group Methods exposed in Slice: createMonitor, startMonitor,
            stopMonitor, destroyMonitor, getMonitorState
        :group Other methods: _getNextMonitorId, callback

    """

    def __init__(self):
        """
            Intialise the instance variables.

        """
        self.log = logging.getLogger("fsserver." + __name__)
        # self.fsPrefix = 'ode-fs://' + socket.gethostbyname(
        #     socket.gethostname())
        # self.prefixLne = len(self.fsPrefix) ## Save calculating it each time?
        #: Numerical component of a Monitor Id
        self.monitorId = 0
        #: Dictionary of Monitors by Id
        self.monitors = {}
        #: Dictionary of MonitorClientI proxies by Id
        self.proxies = {}

    """
        Methods published in the slice interface odefs.ice

    """

    def createMonitor(self, mType, eTypes, pMode, pathString, whitelist,
                      blacklist, timeout, blockSize, ignoreSysFiles,
                      ignoreDirEvents, platformCheck, proxy, current=None):
        """
            Create a the Monitor for a given path.

            :Parameters:
                mType :
                    The type of monitor.

                eTypes :
                    A list of the event types to be monitored.

                pMode :
                    The mode of directory monitoring:
                    flat, recursive or following.

                pathString : string
                    A string representing a path to be monitored.

                whitelist : list<string>
                    A list of extensions of interest.

                blacklist : list<string>
                    A list of subdirectories to be excluded.

                timeout : float
                    A timeout used by some types of monitor.

                blockSize : int
                    Number of events to pack into one notification.

                ignoreSysFiles : boolean
                    Flag. If true platform-dependent system files are ignored

                ignoreDirEvents : boolean
                    Flag. If true directory events are ignored

                platformCheck : boolean
                    Flag. If true platform check is strict

                proxy :
                    A proxy to be informed of events

                current
                    An ICE context, this parameter is required to be present
                    in an ICE interface method.

            :return: Monitor Id.
            :rtype: string

        """

        monitorId = self._getNextMonitorId()

        try:
            # blockSize (0) and ignoreDirEvents (True) hardwired until slice is
            # changed.
            self.monitors[monitorId] = MonitorFactory.createMonitor(
                mType, eTypes, pMode, pathString, whitelist, blacklist,
                timeout, blockSize, ignoreSysFiles, ignoreDirEvents,
                platformCheck, self, monitorId)

        except Exception as e:
            self.log.exception('Failed to create monitor: ')
            raise ode.OdeFSError(
                reason='Failed to create monitor: ' + str(e))

        self.proxies[monitorId] = proxy

        self.log.info(
            'Monitor id = ' + monitorId + ' created. Proxy: ' + str(proxy))

        return monitorId

    def startMonitor(self, id, current=None):
        """
            Start the Monitor with the given Id.

            :Parameters:
                id : string
                    A string uniquely identifying a Monitor.

                current
                    An ICE context, this parameter is required to be present
                    in an ICE interface method.

            :return: Success status.
            :rtype: boolean

        """
        try:
            self.monitors[id].start()
            self.log.info('Monitor id = ' + id + ' started')
        except Exception as e:
            self.log.error(
                'Monitor id = ' + id + ' failed to start: ' + str(e))
            raise ode.OdeFSError(
                reason='Monitor id = ' + id + ' failed to start: ' + str(e))

    def stopMonitor(self, id, current=None):
        """
            Stop the Monitor with the given Id.

            :Parameters:
                id : string
                    A string uniquely identifying a Monitor.

                current
                    An ICE context, this parameter is required to be present
                    in an ICE interface method.

            :return: Success status.
            :rtype: boolean

        """
        try:
            self.monitors[id].stop()
            self.log.info('Monitor id = ' + id + ' stopped')
        except Exception as e:
            self.log.error('Monitor id = ' + id + ' failed to stop: ' + str(e))
            raise ode.OdeFSError(
                reason='Monitor id = ' + id + ' failed to stop: ' + str(e))

    def destroyMonitor(self, id, current=None):
        """
            Destroy the Monitor with the given Id.

            :Parameters:
                id : string
                    A string uniquely identifying a Monitor.

                current
                    An ICE context, this parameter is required to be present
                    in an ICE interface method.

            :return: Success status.
            :rtype: boolean

        """
        try:
            del self.monitors[id]
            del self.proxies[id]
            self.log.info('Monitor id = ' + id + ' destroyed')
        except Exception as e:
            self.log.error('Monitor id = ' + id + ' not destroyed: ' + str(e))
            raise ode.OdeFSError(
                reason='Monitor id = ' + id + ' not destroyed: ' + str(e))

    def getMonitorState(self, id):
        """
            Get the state of a monitor.

            Return the state of an existing monitor.
            Raise an exception if the monitor does no exist.

        """
        self.log.info('Monitor id = ' + id + ' state requested')
        # *****  TO BE IMPLEMENTED  *****
        # If monitor exists return state
        # otherwise raise an exception (no subscription).
        # (and ICE exception implies no server)

        raise ode.OdeFSError('Method not yet implemented.')

    def _getNextMonitorId(self):
        """
            Return next monitor ID and increment.

            The monitorID is a unique key to identify a monitor on the
            file system. In the present implementation this is a string
            generated by uuid.uuid1()

            :return: Next monitor Id
            :rtype: string

        """
        return str(uuid.uuid1())

    def callback(self, monitorId, fileList):
        """
            Callback required by FSEvents.FSEventStream.

            :Parameters:


            :return: No explicit return value.

        """

        eventList = []
        for fileEvent in fileList:
            info = monitors.EventInfo(fileEvent[0], fileEvent[1])
            eventList.append(info)

        proxy = self.proxies[monitorId]

        try:
            self.log.info('Event notification on monitor id= %s', monitorId)
            self.log.debug(' ...notifications are: %s', str(eventList))
            proxy.fsEventHappened(monitorId, eventList)
        except Exception as e:
            self.log.info(
                'Callback to monitor id=' + monitorId
                + ' failed. Reason: ' + str(e))
