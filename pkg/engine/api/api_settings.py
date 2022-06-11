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

"""Settings for the Bhojpur ODE JSON api app."""

import sys
from engine.settings import process_custom_settings, report_settings, str_slash

# load settings
API_SETTINGS_MAPPING = {
    "engine.web.api.limit": [
        "API_LIMIT",
        200,
        int,
        "Default number of items returned from json api.",
    ],
    "engine.web.api.max_limit": [
        "API_MAX_LIMIT",
        500,
        int,
        "Maximum number of items returned from json api.",
    ],
    "engine.web.api.absolute_url": [
        "API_ABSOLUTE_URL",
        None,
        str_slash,
        (
            "URL to use for generating urls within API json responses. "
            "By default this is None, and we use Django's "
            "request.build_absolute_uri() to generate absolute urls "
            "based on each request. If set to a string or empty string, "
            "this will be used as prefix to relative urls."
        ),
    ],
}

process_custom_settings(sys.modules[__name__], "API_SETTINGS_MAPPING")
report_settings(sys.modules[__name__])

# For any given release of api, we may support
# one or more versions of the api.
# E.g. /api/v0/
# TODO - need to decide how this is configured, strategy for extending etc.
API_VERSIONS = ("0",)

# Current major.minor version number
API_VERSION = "0.2"