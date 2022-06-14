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

import os
import sys
import logging
from wsgiref.simple_server import WSGIServer

logger = logging.getLogger(__name__)

if __name__ == "__main__":
    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "engine.settings")

    try:
        from django.core.management import execute_from_command_line
    except ImportError:
        # The above import may fail for some other reason. Ensure that the
        # issue is really that Django is missing to avoid masking other
        # exceptions on Python 2.
        try:
            import django
        except ImportError:
            raise ImportError(
                "Couldn't import Django. Are you sure it's installed and "
                "available on your PYTHONPATH environment variable? Did you "
                "forget to activate a virtual environment?"
            )
        raise

    import settings
    from engine.util import configure_logging

    if settings.DEBUG:
        configure_logging(settings.LOGDIR, "ODEweb.log", loglevel=logging.DEBUG)

    logger.info("Application Starting...")

    # Monkeypatch Django development web server to always run in single thread
    # even if --nothreading is not specified on command line
    def force_nothreading(
        addr, port, wsgi_handler, ipv6=False, threading=False, server_cls=WSGIServer
    ):
        django_core_servers_basehttp_run(
            addr, port, wsgi_handler, ipv6, False, server_cls
        )

    import django.core.servers.basehttp

    if django.core.servers.basehttp.run.__module__ != "settings":
        django_core_servers_basehttp_run = django.core.servers.basehttp.run
        django.core.servers.basehttp.run = force_nothreading

    execute_from_command_line(sys.argv)