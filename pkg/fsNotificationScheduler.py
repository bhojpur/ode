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
    ODE.fs Notification Scheduler.
"""
import logging
import threading
import Queue
import time

class NotificationScheduler(threading.Thread):

    def __init__(self, proxy, monitorId, timeout=0.0, blockSize=0):
        threading.Thread.__init__(self)
        self.log = logging.getLogger("fsclient." + __name__)
        # infinite queue for first test
        self.queue = Queue.Queue(0)
        self.event = threading.Event()
        self.proxy = proxy
        self.monitorId = monitorId
        self.timeout = timeout
        self.blockSize = blockSize

    def schedule(self, eventList):
        self.queue.put_nowait(eventList)

    def run(self):
        self.log.info('Notification Scheduler running')
        while not self.event.isSet():
            qnow = self.queue.qsize()
            if qnow > 0:
                self.log.info('Notification queue size = %s', qnow)
                if self.blockSize != 0 and qnow >= self.blockSize:
                    qnow = self.blockSize
                notice = []
                for i in range(qnow):
                    notice += self.queue.get_nowait()
                self.log.info('Notification queue %s items removed.', qnow)
                self.log.info(
                    'Notification queue size = %s', self.queue.qsize())
                self.proxy.callback(self.monitorId, notice)
            time.sleep(0.1)
        self.log.info('Notification Scheduler stopped')

    def stop(self):
        self.event.set()
