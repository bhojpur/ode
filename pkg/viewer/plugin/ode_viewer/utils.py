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

def get_version(version=None):

    """
    Returns a PEP 386-compliant version number.
    See https://www.python.org/dev/peps/pep-0440/
    """

    version = get_full_version(version)
    parts = 3
    res = '.'.join(str(x) for x in version[:parts])
    if len(version) > 3:
        res = "%s%s" % (res, version[3])
    return str(res)+get_rc_version()


def get_full_version(value=None):

    """
    Returns a tuple of the Bhojpur ODE viewer version.
    """

    if value is None:
        from .version import VERSION as value  # noqa
    return value


def get_rc_version(value=None):

    """
    Returns a tuple of the Bhojpur ODE viewer version.
    """

    if value is None:
        from .version import RC as value  # noqa
    return value