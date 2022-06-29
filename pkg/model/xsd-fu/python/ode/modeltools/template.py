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

from ode.modeltools import config

try:
    import mx.DateTime as DateTime

    def now():
        return DateTime.ISO.str(DateTime.now())
except ImportError:
    from datetime import datetime

    def now():
        return datetime.now()


class TemplateInfo(object):
    """
    Basic status information to pass to the template engine.
    """
    def __init__(self, outputDirectory, package):
        self.outputDirectory = outputDirectory
        self.package = package
        self.date = now()
        self.DO_NOT_PROCESS = config.DO_NOT_PROCESS
        self.BACK_REFERENCE_OVERRIDE = config.BACK_REFERENCE_OVERRIDE
        self.BACK_REFERENCE_LINK_OVERRIDE = \
            config.BACK_REFERENCE_LINK_OVERRIDE
        self.BACK_REFERENCE_NAME_OVERRIDE = \
            config.BACK_REFERENCE_NAME_OVERRIDE
        self.ANNOTATION_OVERRIDE = config.ANNOTATION_OVERRIDE
        self.COMPLEX_OVERRIDE = config.COMPLEX_OVERRIDE
        self.REF_REGEX = config.REF_REGEX

    def link_overridden(self, property_name, class_name):
        """Whether or not a back reference link should be overridden."""
        try:
            return class_name in \
                self.BACK_REFERENCE_LINK_OVERRIDE[property_name]
        except KeyError:
            return False

    def backReference_overridden(self, property_name, class_name):
        """Whether or not a back reference link name should be overridden."""
        try:
            name = class_name + "." + self.REF_REGEX.sub('', property_name)
            return self.BACK_REFERENCE_NAME_OVERRIDE[name]
        except KeyError:
            return False