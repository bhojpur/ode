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

import datetime
import traceback
import logging
import json
import random

from past.builtins import basestring

from django import template

register = template.Library()

logger = logging.getLogger(__name__)

@register.filter()
def jsonify(obj):
    """Simple template filter to encode a variable to JSON format"""
    return json.dumps(obj)

@register.filter
def hash(value, key):
    return value[key]

@register.filter
def random_if_none(value):
    if value is None:
        value = str(random.random())[2:]
    return value

@register.filter
def random_if_minus_one(value):
    """Used for thumbnail versions"""
    if value == -1:
        value = str(random.random())[2:]
    return value

@register.filter
def ago(value):
    """
    Formats a datetime.datetime object as time Ago.
    E.g. '3 days 2 hours 10 minutes'
    """
    try:
        ago = datetime.datetime.now() - value
    except TypeError:
        return str(value)

    if ago.total_seconds() < 0:
        return "Future times not supported"

    def plurals(val):
        return val != 1 and "s" or ""

    hours, remainder = divmod(ago.seconds, 3600)
    mins, secs = divmod(remainder, 60)
    if ago.days >= 365:
        years = ago.days // 365
        return "%s year%s" % (years, plurals(years))
    if ago.days > 31:
        months = ago.days // 30
        return "%s month%s" % (months, plurals(months))
    if ago.days > 0:
        return "%s day%s" % (ago.days, plurals(ago.days))
    if hours > 0:
        return "%s hour%s" % (hours, plurals(hours))
    if mins > 1:
        return "%s minutes" % (mins)
    if mins == 1:
        return "a minute"
    return "less than a minute"

@register.filter
def truncateafter(value, arg):
    """
    Truncates a string after a given number of chars
    Argument: Number of chars to truncate after
    """
    try:
        length = int(arg)
    except ValueError:  # invalid literal for int()
        return value  # Fail silently.
    if not isinstance(value, basestring):
        value = str(value)
    if len(value) > length:
        return value[:length] + "..."
    else:
        return value

@register.filter
def truncatebefor(value, arg):
    """
    Truncates a string after a given number of chars
    Argument: Number of chars to truncate befor
    """
    try:
        length = int(arg)
    except ValueError:  # invalid literal for int()
        return value  # Fail silently.
    if not isinstance(value, basestring):
        value = str(value)
    if len(value) > length:
        return "..." + value[len(value) - length :]
    else:
        return value

@register.filter
def shortening(value, arg):
    try:
        length = int(arg)
    except ValueError:  # invalid literal for int()
        return value  # Fail silently.
    chunk = length // 2 - 3

    if not isinstance(value, basestring):
        value = str(value)
    try:
        if len(value) < length:
            return value
        else:
            return value[:chunk] + "..." + value[length - chunk :]
    except Exception:
        logger.error(traceback.format_exc())
        return value

@register.filter
def subtract(value, arg):
    "Subtracts the arg from the value"
    return int(value) - int(arg)

@register.filter
def get_range(value):
    """
    Filter - returns a list containing range made from given value
    Usage (in template):

    <ul>{% for i in 3|get_range %}
      <li>{{ i }}. Do something</li>
    {% endfor %}</ul>

    Results with the HTML:
    <ul>
      <li>0. Do something</li>
      <li>1. Do something</li>
      <li>2. Do something</li>
    </ul>

    Instead of 3 one may use the variable set in the views
    """
    return range(value)

@register.filter
def lengthformat(value):
    """
    Filter - returns the converted value
    all values are in micrometers
    """
    try:
        value = float(value)
    except (TypeError, ValueError, UnicodeDecodeError):
        return value

    if value < 0.001:
        return value * 1000 * 10
    elif value < 0.01:
        return value * 1000
    elif value < 1000:
        return value
    elif value < 1000 * 100:
        return value / 1000
    elif value < 1000 * 100 * 10:
        return value / 1000 / 100
    elif value < 1000 * 100 * 10 * 100:
        return value / 1000 / 100 / 10
    else:
        return value / 1000 / 100 / 10 / 1000

@register.filter
def lengthunit(value):
    """
    Filter - returns th emost suitable length units
    all values are in micrometers
    """

    if value == 0:
        return "\u00B5m"
    elif value < 0.001:
        return "\u212B"
    elif value < 0.01:
        return "nm"
    elif value < 1000:
        return "\u00B5m"
    elif value < 1000 * 100:
        return "mm"
    elif value < 1000 * 100 * 10:
        return "cm"
    elif value < 1000 * 100 * 10 * 100:
        return "m"
    else:
        return "km"

@register.filter
def timeformat(value):
    """
    Filter - returns the converted value with units
    all values are in seconds
    """
    from decimal import Decimal, InvalidOperation
    from django.utils.encoding import force_text

    if value is None:
        return ""
    try:
        value = Decimal(force_text(value))
    except UnicodeEncodeError:
        return ""
    except InvalidOperation:
        try:
            value = Decimal(force_text(float(value)))
        except (ValueError, InvalidOperation, TypeError, UnicodeEncodeError):
            return "%s s" % str(value)
    # Formatting shows integer values for all, so we round() for accuracy
    if value == 0:
        return "%d\u00A0s" % value
    if value < Decimal("0.001"):
        return "%d\u00A0\u00B5s" % (round(value * 1000 * 1000))
    elif value < 1:
        return "%d\u00A0ms" % (round(value * 1000))
    elif round(value) < 60:
        # Round and format seconds to one decimal place
        value = round(value * 10) / 10
        return "%0.1f\u00A0s" % value
    elif round(value) < 60 * 60:
        value = round(value)  # Avoids '1min 60s'
        return "%d\u00A0min\u00A0%d\u00A0s" % (value / 60, value % 60)
    else:
        value = round(value)  # Avoids '1h 60min'
        return "%d\u00A0h\u00A0%d\u00A0min" % (value / 3600, round((value % 3600) / 60))

@register.filter
def json_dumps(value):
    return json.dumps(value)

# iterating-through-two-lists-in-django-templates
@register.filter(name="zip")
def zip_lists(a, b):
    return zip(a, b)