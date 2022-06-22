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
Simple unit tests for the "engine.decorators".
"""

import pytest
import string

from django.test import RequestFactory
from django.test import override_settings
from django.utils.http import urlencode

from engine.webclient.decorators import render_response

QUERY_STRING = " %s" % string.printable

def call_load_settings(request, conn):
    context = {'ome': {}}
    render_response().load_settings(request, context, conn)
    return context


class TestRenderResponse(object):

    def setup_method(self, method):
        # prepare session
        self.r = RequestFactory().get('/rand')

    @override_settings()
    def test_load_settings_defaults(self):
        context = call_load_settings(self.r, None)
        defaults = [
            {
                'link': u'/webclient/',
                'attrs': {u'title': u'Browse Data via Projects, Tags etc'},
                'label': u'Data'
            }, {
                'link': u'/webclient/history/',
                'attrs': {u'title': u'History'},
                'label': u'History'
            }, {
                'link': u'https://help.bhojpur.net/',
                'attrs': {
                    u'target': u'new',
                    u'title': u'Open Bhojpur ODE user guide in a new tab'
                },
                'label': u'Help'
            }]
        assert context['ome']['top_links'] == defaults

    @pytest.mark.parametrize('top_links', [
        [['Data1', 'webindex', {"title": "Some text"}], ["/webclient/"]],
        [['Data2', {"viewname": 'webindex'}, {"title": "Some text"}],
            ["/webclient/"]],
        [['Data3', {"viewname": "load_template", "args": ["userdata"]},
            {}], ["/webclient/userdata/"]],
        [['Data4', {"viewname": "load_template", "args": ["userdata"],
                    "query_string": {"experimenter": -1}}, {}],
            ["/webclient/userdata/?experimenter=-1"]],
        [['Data5', {"viewname": "load_template", "args": ["userdata"],
                    "query_string": {"test": QUERY_STRING}}, {}],
            ["/webclient/userdata/?%s" % urlencode({'test': QUERY_STRING})]],
        [['History', 'history', {"title": "History"}],
            ["/webclient/history/"]],
        [['HELP', 'https://help.bhojpur.net', {"title": "Help"}],
            ["https://help.bhojpur.net"]],
        [["", "", {}], [""]],
        [["", None, {}], [None]],
        [["Foo", "bar", {}], ["bar"]],
        [['Foo', {"viewname": "foo"}, {}], [""]],
        [["Foo", {"viewname": "load_template", "args": ["bar"]}, {}], [""]],
        ])
    def test_load_settings(self, top_links):
        @override_settings(TOP_LINKS=[top_links[0]])
        def _test_load_settings():
            return call_load_settings(self.r, None)

        context = _test_load_settings()
        assert context['ome']['top_links'][0]['label'] == top_links[0][0]
        assert context['ome']['top_links'][0]['link'] == top_links[1][0]
        assert context['ome']['top_links'][0]['attrs'] == top_links[0][2]
