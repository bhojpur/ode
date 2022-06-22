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
Test server config in decorator.
"""

import json

from ode.testlib import ITest

from django.test import RequestFactory
from django.contrib.sessions.middleware import SessionMiddleware

from engine.webclient import webclient_gateway  # NOQA
from ode.gateway import ServerGateway
from engine.decorators import login_required

from ode.gateway.utils import propertiesToDict

import pytest

def default_view(request):
    pass

def flattenProperties(d):
    """
    Convert nested dictionary to flat map,
    """

    def items():
        for key, value in d.items():
            if isinstance(value, dict):
                for subkey, subvalue in flattenProperties(value).items():
                    yield key + "." + subkey, subvalue
            else:
                yield key, value
    return dict(items())


def test_flattenProperties():
    d = {
        'ode.prefix.str.1': 'mystring',
        'ode.prefix.str.2': '1',
        'ode.prefix.int.1': 1
    }
    dictprop = propertiesToDict(d, prefix='ode.prefix.')
    flatprop = flattenProperties({'ode': {'prefix': dictprop}})
    assert set(d.keys()) - set(flatprop.keys()) == set()


class TestConfig(ITest):

    def setup_method(self, method):
        # prepare session
        self.r = RequestFactory().get('/rand')
        middleware = SessionMiddleware()
        middleware.process_request(self.r)
        self.r.session.save()
        self.rs = self.root.sf.getConfigService()
        self.conn = ServerGateway(client_obj=self.new_client())

    def teardown_method(self, method):
        self.conn.close()
        self.r.session.flush()

    def testDefaultConfig(self):
        """ Test loading default config """
        default = self.rs.getClientConfigDefaults()
        login_required(default_view).load_server_settings(self.conn, self.r)
        s = {"ode": {"client": self.r.session.get('server_settings', {})}}
        # compare keys in default and config loaded by decorator
        a = filter(lambda x: x not in (
            set(default.keys())),
            set(flattenProperties(s).keys())
            )
        assert a == ['ode.client.email']

    def testDefaultConfigConversion(self):
        default = self.rs.getClientConfigDefaults()

        # bool
        key1 = 'ode.client.ui.tree.orphans.enabled'
        self.rs.setConfigValue(key1, default[key1])

        key11 = 'ode.client.ui.tree.orphans.name'
        self.rs.setConfigValue(key11, default[key11])

        # digit
        key2 = 'ode.client.viewer.roi_limit'
        self.rs.setConfigValue(key2, default[key2])

        login_required(default_view).load_server_settings(self.conn, self.r)
        ss = self.r.session['server_settings']

        assert isinstance(ss['ui']['tree']['orphans']['enabled'], bool)
        assert ss['ui']['tree']['orphans']['enabled'] == bool(default[key1])

        assert isinstance(ss['ui']['tree']['orphans']['name'], str)
        assert ss['ui']['tree']['orphans']['name'] == default[key11]

        assert isinstance(ss['viewer']['roi_limit'], int)
        assert ss['viewer']['roi_limit'] == json.loads(default[key2])

    @pytest.mark.parametrize("prop", ["colleagues.label", "leaders.label",
                                      "everyone.label"])
    @pytest.mark.parametrize("label", ["foo"])
    def testUpgradeDropdownMenuConfig(self, prop, label):
        """ Test to set and get DropdownMenuConfig """
        d = self.rs.getClientConfigDefaults()
        key = "ode.client.ui.menu.dropdown.%s" % prop
        try:
            self.rs.setConfigValue(key, label)
            # test load_server_settings directly
            login_required(default_view).load_server_settings(
                self.conn, self.r)
            s = self.r.session.get('server_settings', {})
            prop = prop.replace(".label", "")
            assert s['ui']['menu']['dropdown'][prop]['label'] == label
        finally:
            self.rs.setConfigValue(key, d[key])
