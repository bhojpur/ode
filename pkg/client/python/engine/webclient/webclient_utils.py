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
import datetime

logger = logging.getLogger(__name__)

def getDateTime(timeString):
    return datetime.datetime.strptime(timeString, "%Y-%m-%d %H:%M:%S")

def formatPercentFraction(value):
    """Formats a fraction as a percentage for display"""
    value = value * 100
    if value < 1:
        # Handle python3 rounding 0.05 towards even numbers
        # Make sure it always rounds 0.05 up
        if (value * 10) % 1 == 0.5:
            value += 0.01
        value = "%.1f" % round(value, 1)
    else:
        # Make sure it always rounds 0.5 up
        if value % 1 == 0.5:
            value += 0.1
        value = "%s" % int(round(value))
    return value


def _formatReport(callback):
    """
    Added as workaround to the changes made in #3006.
    """
    rsp = callback.getResponse()
    if not rsp:
        return  # Unfinished

    import ode

    if isinstance(rsp, ode.cmd.ERR):
        err = rsp.parameters.get("Error", "")
        warn = rsp.parameters.get("Warning", "")
        logger.error("Format report: %r" % {"error": err, "warning": warn})
        return "Operation could not be completed successfully"
    # Delete2Response, etc include no warnings
    # Might want to take advantage of other feedback here

def _purgeCallback(request, limit=200):

    callbacks = request.session.get("callback", {}).keys()
    if len(callbacks) > limit:
        cbKeys = list(request.session.get("callback").keys())
        for (cbString, count) in zip(cbKeys, range(0, len(callbacks) - limit)):
            del request.session["callback"][cbString]