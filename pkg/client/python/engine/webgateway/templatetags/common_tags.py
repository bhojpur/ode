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
import time

from django.conf import settings
from django import template

register = template.Library()

logger = logging.getLogger(__name__)

# makes settings available in template
@register.tag
def setting(parser, token):
    try:
        tag_name, option = token.split_contents()
    except ValueError:
        raise template.TemplateSyntaxError(
            "%r tag requires a single argument" % token.contents[0]
        )
    return SettingNode(option)

class SettingNode(template.Node):
    def __init__(self, option):
        self.option = option

    def render(self, context):
        try:
            setting = settings
            for name in self.option.split("."):
                if name.isdigit():
                    setting = setting[int(name)]
                else:
                    if type(setting) == dict:
                        setting = setting.get(name)
                    else:
                        setting = setting.__getattr__(name)
            if setting is None:
                return ""
            return str(setting)
        except Exception:
            # if FAILURE then FAIL silently
            return ""

class PluralNode(template.Node):
    def __init__(self, quantity, single, plural):
        self.quantity = template.Variable(quantity)
        self.single = template.Variable(single)
        self.plural = template.Variable(plural)

    def render(self, context):
        if self.quantity.resolve(context) == 1:
            return "%s" % self.single.resolve(context)
        else:
            return "%s" % self.plural.resolve(context)

@register.tag(name="plural")
def do_plural(parser, token):
    """
    Usage: {% plural quantity name_singular name_plural %}

    This simple version only works with template variable since we will use
    blocktrans for strings.
    """

    try:
        # split_contents() knows not to split quoted strings.
        tag_name, quantity, single, plural = token.split_contents()
    except ValueError:
        raise template.TemplateSyntaxError(
            "%r tag requires exactly three arguments" % token.contents.split()[0]
        )

    return PluralNode(quantity, single, plural)

class TemplateTokenNode(template.Node):
    def __init__(self, component, cid):
        self.component = template.Variable(component)
        self.cid = template.Variable(cid)

    def render(self, context):
        timestamp = time.time()
        return "%s-%s-%s" % (
            self.component.resolve(context),
            self.cid.resolve(context),
            timestamp,
        )

@register.tag(name="content_identifier")
def content_identifier(parser, token):
    """
    Usage: {% plural quantity name_singular name_plural %}

    This simple version only works with template variable since we will use
    blocktrans for strings.
    """

    try:
        # split_contents() knows not to split quoted strings.
        tag_name, component, cid = token.split_contents()
    except ValueError:
        raise template.TemplateSyntaxError(
            "%r tag requires exactly three arguments" % token.contents.split()[0]
        )

    return TemplateTokenNode(component, cid)