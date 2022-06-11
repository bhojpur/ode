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

import logging

from django.utils.http import urlencode
from django.urls import reverse
from django.urls import NoReverseMatch
from past.builtins import basestring

logger = logging.getLogger(__name__)

def reverse_with_params(*args, **kwargs):
    """
    Adds query string to django.urls.reverse
    """

    url = ""
    qs = kwargs.pop("query_string", {})
    try:
        url = reverse(*args, **kwargs)
    except NoReverseMatch:
        return url
    if qs:
        if not isinstance(qs, basestring):
            qs = urlencode(qs)
        url += "?" + qs
    return url

def sort_properties_to_tuple(input_list, index="index", element="class"):
    return tuple(e[element] for e in sorted(input_list, key=lambda k: k[index]))