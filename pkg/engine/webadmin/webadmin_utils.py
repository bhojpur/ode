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

import logging

logger = logging.getLogger(__name__)

def upgradeCheck(url):
    # upgrade check:
    # -------------
    # On each startup ODE.web checks for possible server upgrades
    # and logs the upgrade url at the WARNING level. If you would
    # like to disable the checks, please set 'ode.web.upgrades_url`
    # to an empty string.
    #
    try:
        from ode.util.upgrade_check import UpgradeCheck

        if url:
            check = UpgradeCheck("web", url=url)
            check.run()
            if check.isUpgradeNeeded():
                logger.warn(
                    "Upgrade is available. Please visit"
                    " https://downloads.bhojpur.net/latest/ode/.\n"
                )
            else:
                logger.debug("Up to date.\n")
    except Exception as x:
        logger.error("Upgrade check error: %s" % x)