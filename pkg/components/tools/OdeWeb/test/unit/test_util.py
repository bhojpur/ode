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

"""
Simple unit tests for the "webclient_utils" module.
"""

import pytest
import json

from django.core.urlresolvers import reverse

from engine.utils import reverse_with_params, sort_properties_to_tuple
from engine.webclient.webclient_utils import formatPercentFraction
from engine.webclient.webclient_utils import getDateTime

class TestUtil(object):
    """
    Tests various util methods
    """

    def test_format_percent_fraction(self):

        assert formatPercentFraction(1) == "100"
        assert formatPercentFraction(0.805) == "81"
        assert formatPercentFraction(0.2) == "20"
        assert formatPercentFraction(0.01) == "1"
        assert formatPercentFraction(0.005) == "0.5"
        assert formatPercentFraction(0.005) == "0.5"
        assert formatPercentFraction(0.0025) == "0.3"
        assert formatPercentFraction(0.00) == "0.0"

    def test_get_date_time(self):
        """ Tests that only a full date-time string is valid """
        assert getDateTime("2015-12-01 00:00:00") is not None
        assert getDateTime("2015-12-01 23:59:59") is not None
        with pytest.raises(ValueError):
            getDateTime("12345")
        with pytest.raises(ValueError):
            getDateTime("invalid")
        with pytest.raises(ValueError):
            getDateTime("2015-12-01")

    @pytest.mark.parametrize('top_links', [(
        ('{"viewname": "load_template", "args": ["userdata"],'
         '"query_string": {"experimenter": -1}}'),
        "/webclient/userdata/?experimenter=-1"),
        ('{"viewname": "webindex", "query_string": {"foo": "bar"}}',
         "/webclient/?foo=bar"),
        ('{"viewname": "foo", "args": ["bar"]}', ""),
        ('{"viewname": "foo", "query_string": {"foo": "bar"}}', ""),
        ])
    def test_reverse_with_params_dict(self, top_links):
        top_link = json.loads(top_links[0])
        assert reverse_with_params(**top_link) == top_links[1]

    @pytest.mark.parametrize('top_links', [
        ("history", "/webclient/history/"),
        ("webindex", "/webclient/"),
        ])
    def test_reverse_with_params_string(self, top_links):
        top_link = top_links[0]
        assert reverse_with_params(top_link) == reverse(top_link) \
            == top_links[1]

    @pytest.mark.parametrize('top_links', [
        ("foo", "str"),
        ('', "str"),
        (None, "NoneType"),
    ])
    def test_bad_reverse_with_params_string(self, top_links):
        kwargs = top_links[0]
        with pytest.raises(TypeError) as excinfo:
            reverse_with_params(**kwargs)
        assert ('reverse_with_params() argument after ** must'
                ' be a mapping, not %s') % top_links[1] \
            in str(excinfo.value)

    @pytest.mark.parametrize('params', [
        ([], ()),
        ([{"index": 1, "class": "abc"}], ('abc',)),
        ([{"index": 1, "class": "abc"}, {"index": 1, "class": "cde"}],
         ('abc', 'cde')),
        ([{"index": 2, "class": "abc"}, {"index": 1, "class": "cde"}],
         ('cde', 'abc')),
        (({"index": 1, "class": "abc"},), ('abc',)),
    ])
    def test_sort_properties_to_tuple(self, params):
        assert sort_properties_to_tuple(params[0]) == params[1]

    @pytest.mark.parametrize('params', [
        ([{"foo": 1, "bar": "abc"}], ('abc',)),
    ])
    def test_sort_properties_to_tuple_custom(self, params):
        assert sort_properties_to_tuple(
            params[0], params[0][0].keys()[0],
            params[0][0].keys()[1]) == params[1]

    @pytest.mark.parametrize('bad_params', [
        ([{}], KeyError, "'index'"),
        ([{"foo": 1}], KeyError, "'index'"),
        ([{"index": 1}], KeyError, "'class'"),
    ])
    def test_sort_properties_to_tuple_keyerror(self, bad_params):
        with pytest.raises(bad_params[1]) as excinfo:
            sort_properties_to_tuple(bad_params[0])
        assert bad_params[2] in str(excinfo.value)
