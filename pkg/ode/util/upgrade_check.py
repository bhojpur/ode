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

"""

from future import standard_library
from builtins import str
from builtins import object
from ode_version import ode_version

import platform
import logging
import requests

standard_library.install_aliases()

# Ensure we are using pyopenssl (ode/ode-py#240)
try:
    import urllib3.contrib.pyopenssl
    urllib3.contrib.pyopenssl.inject_into_urllib3()
except ImportError:
    pass

class UpgradeCheck(object):

    """
    Port of Java UpgradeCheck:

    >>> from ode.util.upgrade_check import UpgradeCheck
    >>> uc = UpgradeCheck("doctest")
    >>> uc.run()
    >>> uc.isUpgradeNeeded()
    False
    >>> uc.isExceptionThrown()
    False
    >>> uc = UpgradeCheck("doctest", version = "0.0.0")
    >>> uc.run()
    >>> uc.isUpgradeNeeded()
    True
    >>> uc.isExceptionThrown()
    False
    >>>
    >>> uc = UpgradeCheck("doctest",
    ...     url = "http://some-completely-unknown-host.abcd/")
    >>> uc.run()
    >>> uc.isUpgradeNeeded()
    False
    >>> uc.isExceptionThrown()
    True
    """

    #
    # our default timeout to use for requests; package default is no timeout
    #
    DEFAULT_TIMEOUT = 6.0

    def __init__(self, agent, url="http://upgrade.bhojpur.net/",
                 version=ode_version, timeout=DEFAULT_TIMEOUT):
        """
        ::
            agent   := Name of the agent which is accessing the registry.
                       This will be appended to "ODE." in order to adhere
                       to the registry API.
            url     := Connection information for the upgrade check.
                       None or empty string disables check.
                       Defaults to upgrade.bhojpur.net
            version := Version to check against the returned value.
                       Defaults to current version as specified
                       in ode_version.py.
            timeout := How long to wait for the HTTP GET in seconds (float).
                       The default timeout is 3 seconds.
        """

        self.log = logging.getLogger("ode.util.UpgradeCheck")

        self.url = str(url)
        self.version = str(version)
        self.timeout = float(timeout)
        self.agent = "ODE." + str(agent)

        self.upgradeUrl = None
        self.exc = None

    def isUpgradeNeeded(self):
        return self.upgradeUrl is not None

    def getUpgradeUrl(self):
        return self.upgradeUrl

    def isExceptionThrown(self):
        return self.exc is not None

    def getExceptionThrown(self):
        return self.exc

    def _set(self, results, e):
        self.upgradeUrl = results
        self.exc = e

    def getOSVersion(self):
        try:
            if len(platform.mac_ver()[0]) > 0:
                version = "%s;%s" % (platform.platform(),
                                     platform.mac_ver()[0])
            else:
                version = platform.platform()
        except Exception:
            version = platform.platform()
        return version

    def run(self):
        """
        If the {@link #url} has been set to null or the empty string, then no
        upgrade check will be performed (silently). If however the string is an
        invalid URL, a warning will be printed.

        This method should <em>never</em> throw an exception.
        """

        # If None or empty, the upgrade check is disabled.
        if self.url is None or len(self.url) == 0:
            return
            # EARLY EXIT!

        try:
            params = {}
            params["version"] = self.version
            params["os.name"] = platform.system()
            params["os.arch"] = platform.machine()
            params["os.version"] = self.getOSVersion()
            params["python.version"] = platform.python_version()
            params["python.compiler"] = platform.python_compiler()
            params["python.build"] = platform.python_build()

            headers = {'User-Agent': self.agent}
            request = requests.get(
                self.url, headers=headers, params=params,
                timeout=self.DEFAULT_TIMEOUT
            )
            result = request.text
        except Exception as e:
            self.log.error(str(e), exc_info=0)
            self._set(None, e)
            return

        if len(result) == 0:
            self.log.info("no update needed")
            self._set(None, None)
        else:
            self.log.warn("UPGRADE AVAILABLE:" + result)
            self._set(result, None)