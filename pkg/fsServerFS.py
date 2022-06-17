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
    ODE.fs ServerFS module.

    The Server class is a wrapper to the FileServer. It handles the ICE
    formalities. It controls the shutdown.
"""

import logging
log = logging.getLogger("fsserver.FileServer")

import sys
import Ice

from ode.util import configure_server_logging

import fsFileServer


class Server(Ice.Application):

    """
        A fairly vanilla ICE server application.

    """

    def run(self, args):
        """
            Main method called via app.main() below.

            The Ice.Application is set to callbackOnInterrupt so that it can be
            shutdown cleanly by the callback above.

            :param args: Arguments required by the ICE system.
            :return: Exit state.
            :rtype: int
        """

        props = self.communicator().getProperties()
        configure_server_logging(props)

        # Create a FileServer, its adapter and activate it.
        try:
            serverIdString = self.getServerIdString(props)
            serverAdapterName = self.getServerAdapterName(props)
            mServer = fsFileServer.FileServerI()
            adapter = self.communicator().createObjectAdapter(
                serverAdapterName)
            adapter.add(
                mServer, self.communicator().stringToIdentity(serverIdString))
            adapter.activate()
        except:
            log.exception("Failed create ODE.fs FileServer: \n")
            return -1

        log.info('Started ODE.fs FileServer')

        # Wait for an interrupt.
        self.communicator().waitForShutdown()

        log.info('Stopping ODE.fs FileServer')
        return 0

    def getServerIdString(self, props):
        """
            Get fileServerIdString from the communicator properties.

        """
        return props.getPropertyWithDefault("ode.fs.fileServerIdString", "")

    def getServerAdapterName(self, props):
        """
            Get fileServerIdString from the communicator properties.

        """
        return props.getPropertyWithDefault(
            "ode.fs.fileServerAdapterName", "")


if __name__ == '__main__':
    try:
        log.info('Trying to start ODE.fs FileServer')
        app = Server()
    except:
        log.exception("Failed to start the server:\n")
        log.info("Exiting with exit code: -1")
        sys.exit(-1)

    exitCode = app.main(sys.argv)
    log.info("Exiting with exit code: %d", exitCode)
    sys.exit(exitCode)
