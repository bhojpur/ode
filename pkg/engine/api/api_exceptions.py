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

"""Exceptions used by the api/views methods."""

class ApiException(Exception):
    """A base exception class that handles message and stactrace."""

    def __init__(self, message, stacktrace=None):
        """Override init to handle message and stacktrace."""
        super(ApiException, self).__init__(message)
        self.stacktrace = stacktrace

class BadRequestError(ApiException):
    """
    An exception that will result in a response status of 400.

    Due to invalid client input
    """

    status = 400

class NotFoundError(ApiException):
    """
    An exception that will result in a response status of 404.

    Raised due to objects not being found.
    """

    status = 404

class MethodNotSupportedError(ApiException):
    """
    An exception that will result in a response status of 405.

    Raised if user tries to DELETE, POST or PUT for an Object
    where we don't support those methods.
    """

    status = 405

class CreatedObject(Exception):
    """
    An exception that is thrown when new object created.

    This is not really an error but indicates to the handler
    that a JsonResponse with status 201 should be returned.
    The dict content is passed in as 'response'.
    """

    status = 201

    def __init__(self, response):
        """Override init to include response dict."""
        super(CreatedObject, self).__init__(response)
        self.response = response