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

from django import template
from django.templatetags.static import PrefixNode

register = template.Library()

logger = logging.getLogger(__name__)

@register.tag()
def get_static_webclient_prefix(parser, token):
    """
    Populates a template variable with the static prefix,
    ``settings.WEBCLIENT_STATIC_URL``.

    Usage::

        {% get_static_webclient_prefix [as varname] %}

    Examples::

        {% get_static_webclient_prefix %}
        {% get_static_webclient_prefix as STATIC_WEBCLIENT_PREFIX %}

    """
    return PrefixNode.handle_token(parser, token, "STATIC_WEBCLIENT_URL")

@register.filter
def get_item(dictionary, key):
    return dictionary.get(key)