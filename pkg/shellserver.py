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
#
# Bhojpur ODE Grid Shell Server
#
# A python wrapper which can call arbitrary shell commands
# and wait on a signal from the Bhojpur ODE.grid to shut them
# down again.

import Ice
import os
import signal
import subprocess
import sys
import time


def call(arr):
    # The following work around is due to the relative paths
    # passed by icegridnode
    env = dict(os.environ)
    pth = list(sys.path)
    for i in range(0, len(pth)):
        pth[i] = os.path.abspath(pth[i])
    env["PYTHONPATH"] = os.path.pathsep.join(pth)
    return subprocess.Popen(arr, env=env)

class Ex(Exception):
    pass

class ShellServer(Ice.Application):

    def __init__(self, name):
        self.name = name
        self.restarts = 0

        def handler(signal, frame):
            if not hasattr(self, "proc"):
                self.log.error("No process to send %s" % signal)
            else:
                self.log.error("Passing sig %s to child process %s" % signal)
                os.kill(self.proc.pid, signal)
        for sig in [signal.SIGQUIT, signal.SIGTERM]:
            signal.signal(sig, handler)

    def init(self):
        self.log = self.communicator().getLogger()
        return "%sAdapter" % self.name

    def start(self):
        # Lowercasing should be done in cli.py
        self.proc = call(
            [sys.executable, "bin/ode", "server", self.name.lower()])

    def stop(self):
        if not hasattr(self, "proc"):
            self.log.error("No process found. Exiting")
        else:
            proc = self.proc
            del self.proc
            if not proc.poll():
                os.kill(proc.pid, signal.SIGTERM)
                self.log.warning(
                    "Sent %s SIGTERM. Sleeping..." % str(proc.pid))
                for i in range(1, 30):
                    time.sleep(1)
                    self.log.warning("tick")
                    if proc.poll():
                        self.log.warning("Stopped.")
                        break
            if not proc.poll():
                os.kill(proc.pid, signal.SIGKILL)
                self.log.error("\nKilling %s..." % str(proc.pid))
        sys.exit(0)

    def run(self, args):
        """
        Starts the defined process and watches for it to exit.
        If it exits before stop() is called, it will be restarted.
        """
        self.shutdownOnInterrupt()
        adapterName = self.init()
        try:
            self.start()
            adapter = self.communicator().createObjectAdapter(adapterName)
            adapter.activate()
            while not self.communicator()._impl.waitForShutdown(1000):
                if self.proc and self.proc.poll():
                    self.restarts += 1
                    if self.restarts > 5:
                        raise Ex("Too many restarts")
                    else:
                        self.log.warning("Restart " + str(self.restarts))
                        self.start()
        finally:
            self.stop()

if __name__ == "__main__":
    if len(sys.argv) == 0:
        raise Ex("Requires argument to pass to bin/ode server")
    name = sys.argv[1]
    app = ShellServer(name)
    sys.exit(app.main(sys.argv))
