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

"""Decorators for use with the webgateway application."""

import ode
import engine.decorators
import logging
import traceback
from django.http import JsonResponse
from functools import update_wrapper
from . import api_settings
from .api_exceptions import (
    BadRequestError,
    CreatedObject,
    MethodNotSupportedError,
    NotFoundError,
)

logger = logging.getLogger(__name__)

class login_required(engine.decorators.login_required):
    """webgateway specific extension of the login_required() decorator."""

    def on_not_logged_in(self, request, url, error=None):
        """Used for json api methods."""
        return JsonResponse({"message": "Not logged in"}, status=403)

class json_response(object):
    """
    Class-based decorator for wrapping Django views methods.

    Returns JsonResponse based on dict returned by views methods.
    Also handles exceptions from views methods, returning
    JsonResponse with appropriate status values.
    """

    def __init__(self):
        """Initialise the decorator."""
        pass

    def create_response(self, response, status=200):
        """Create the Json response and set global headers."""
        response = JsonResponse(response, status=status)
        response["X-ODE-ApiVersion"] = api_settings.API_VERSION
        return response

    def handle_success(self, rv):
        """
        Handle successful response from wrapped function.

        By default, we simply return a JsonResponse() but this can be
        overwritten by subclasses if needed.
        """
        return self.create_response(rv)

    def handle_error(self, ex, trace):
        """
        Handle errors from wrapped function.

        By default, we format exception or message and return this
        as a JsonResponse with an appropriate status code.
        """
        # Default status is 500 'server error'
        # But we try to handle all 'expected' errors appropriately
        # TODO: handle engine.ConcurrencyException
        status = 500
        if isinstance(ex, (NotFoundError, MethodNotSupportedError)):
            status = ex.status
        if isinstance(ex, BadRequestError):
            status = ex.status
            trace = ex.stacktrace  # Might be None
        elif isinstance(ex, engine.SecurityViolation):
            status = 403
        elif isinstance(ex, engine.ApiUsageException):
            status = 400
        logger.debug(trace)
        rsp_json = {"message": str(ex)}
        if trace is not None:
            rsp_json["stacktrace"] = trace
        # In this case, there's no Error and the response
        # is valid (status code is 201)
        if isinstance(ex, CreatedObject):
            status = ex.status
            rsp_json = ex.response
        return self.create_response(rsp_json, status=status)

    def __call__(self, f):
        """
        Return the decorator.

        The decorator calls the wrapped function and
        handles success or exception, returning a
        JsonResponse
        """

        def wrapped(request, *args, **kwargs):
            logger.debug("json_response")
            try:
                rv = f(request, *args, **kwargs)
                return self.handle_success(rv)
            except Exception as ex:
                trace = traceback.format_exc()
                return self.handle_error(ex, trace)

        return update_wrapper(wrapped, f)