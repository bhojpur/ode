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

import sys
import platform
import traceback
import logging
from urllib.parse import urlencode

try:
    # python2
    from urllib2 import urlopen, Request, HTTPError, URLError
except ImportError:
    # python3
    from urllib.request import urlopen, Request
    from urllib.error import HTTPError, URLError
try:
    # python2
    from urlparse import urljoin
except ImportError:
    # python3
    from urllib.parse import urljoin

from engine.version import engine_version as ode_version

from django.conf import settings

logger = logging.getLogger(__name__)

class SendFeedback(object):

    conn = None

    def __init__(self, feedback_url):
        self.url = urljoin(feedback_url, "/qa/initial/")

    def send_feedback(self, error=None, comment=None, email=None, user_agent=""):
        try:
            p = {
                "app_name": settings.FEEDBACK_APP,
                "app_version": ode_version,
                "extra": "",
                "error": (error or ""),
                "email": (email or ""),
                "comment": comment,
            }
            try:
                p["python_classpath"] = sys.path
            except Exception:
                pass
            try:
                p["python_version"] = platform.python_version()
            except Exception:
                pass
            try:
                p["os_name"] = platform.platform()
            except Exception:
                pass
            try:
                p["os_arch"] = platform.machine()
            except Exception:
                pass
            try:
                p["os_version"] = platform.release()
            except Exception:
                pass
            data = urlencode(p)
            data = data.encode()
            headers = {
                "Content-type": "application/x-www-form-urlencoded",
                "Accept": "text/plain",
                "User-Agent": user_agent,
            }
            request = Request(self.url, data, headers)
            response = None
            try:
                response = urlopen(request)
                if response.code == 200:
                    logger.info(response.read())
                else:
                    logger.error("Feedback server error: %s" % response.reason)
                    raise Exception("Feedback server error: %s" % response.reason)
            except HTTPError as e:
                logger.error(traceback.format_exc())
                raise Exception("Feedback server error: %s" % e.code)
            except URLError as e:
                logger.error(traceback.format_exc())
                raise Exception("Feedback server error: %s" % e.reason)
            finally:
                if response:
                    response.close()
        except Exception as x:
            logger.error(traceback.format_exc())
            raise Exception("Feedback server error: %s" % x)