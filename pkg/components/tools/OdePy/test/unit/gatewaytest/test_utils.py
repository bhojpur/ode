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
   gateway tests - Object Wrappers
"""

from ode.gateway.utils import ServiceOptsDict
from ode.gateway.utils import toBoolean
from ode.gateway.utils import propertiesToDict
import pytest

class TestServiceOptsDict (object):

    def test_constructor(self):
        assert ServiceOptsDict() == {}
        assert ServiceOptsDict() is not {}
        assert ServiceOptsDict() is not dict()

        d = {"ode.group": -1}
        d = ServiceOptsDict(d)

        resd = d.get("ode.group")
        assert isinstance(resd, str)
        assert d.get("ode.group") == str(d["ode.group"])

        d = ServiceOptsDict(x=1, y=2)
        assert d.get("x") == "1"
        assert d.get("y") == "2"

        # ServiceOptsDict can be passed initializing data, but it needs to be
        # a dict
        pytest.raises(AttributeError, ServiceOptsDict,
                      kwargs={"data": "data"})
        ServiceOptsDict(data={'option': 'a'})

    def test_keys(self):
        d = ServiceOptsDict()
        assert d.keys() == []

        d = ServiceOptsDict({"ode.group": -1})
        assert 'ode.group' in d

        pytest.raises(TypeError, d.keys, None)

    def test_values(self):
        d = ServiceOptsDict()
        assert d.values() == []

        d = ServiceOptsDict({"ode.group": -1})
        assert d.values() == ["-1"]

        pytest.raises(TypeError, d.values, None)

        d = ServiceOptsDict({
            "a": None, "b": True, "c": "foo", "d": 1, "e": 1.45, "f": [],
            "g": {}})
        assert d.values() == ['foo', '1.45', '1']

    def test_items(self):
        d = ServiceOptsDict()
        assert d.items() == []

        d = ServiceOptsDict({"ode.group": -1})
        assert d.items() == [("ode.group", "-1")]

        pytest.raises(TypeError, d.items, None)

    def test_has_key(self):
        d = ServiceOptsDict()
        assert 'ode' not in d

        d = ServiceOptsDict({"ode.group": -1, "ode.user": 1})
        k = d.keys()
        k.sort()
        assert k == ['ode.group', 'ode.user']

        pytest.raises(TypeError, d.has_key)

    def test_contains(self):
        d = ServiceOptsDict()
        assert not ('ode.group' in d)
        assert 'ode.group' not in d

        d = ServiceOptsDict({"ode.group": -1, "ode.user": 1})
        assert 'ode.group' in d
        assert 'ode.user' in d
        assert 'ode.share' not in d

    def test_len(self):
        d = ServiceOptsDict()
        assert len(d) == 0

        d = ServiceOptsDict({"ode.group": -1, "ode.user": 1})
        assert len(d) == 2

    def test_getitem(self):
        d = ServiceOptsDict({"ode.group": -1, "ode.user": 1})
        assert d["ode.group"] == "-1"
        assert d["ode.user"] == "1"

        d["ode.share"] = 2
        d["foo"] = "bar"
        assert d["ode.share"] == "2"
        assert d["foo"] == "bar"

        del d["ode.user"]

        assert d == {"ode.group": "-1", 'foo': 'bar', "ode.share": "2"}

        pytest.raises(TypeError, d.__getitem__)

        assert d.get("ode.user") is None
        assert d.get("ode.user", "5"), "5"

    def test_setitem(self):

        # string
        d = ServiceOptsDict({"ode.share": "2", "ode.user": "1"})
        d["ode.group"] = "-1"

        # unicode
        d = ServiceOptsDict({"ode.share": u'2', "ode.user": u'1'})
        d["ode.group"] = u'-1'

        # int
        d = ServiceOptsDict({"ode.share": 1, "ode.user": 2})
        d["ode.group"] = -1

        # long
        import sys
        maxint = sys.maxint
        d = ServiceOptsDict({"ode.group": (maxint + 1)})
        d["ode.user"] = (maxint + 1)

        # Setter passed None as value remove from internal dict
        d = ServiceOptsDict({"ode.share": "2", "ode.user": "1"})
        assert d.get("ode.share") is not None
        d.setOdeShare()
        assert d.get("ode.share") is None
        assert d.get("ode.user") is not None
        d.setOdeUser()
        assert d.get("ode.user") is None

        try:
            d = ServiceOptsDict({"ode.group": True})
            d["ode.user"] = True
        except:
            pass
        else:
            self.fail("AttributeError: ServiceOptsDict argument must be a"
                      " string, unicode or numeric type")

        try:
            d = ServiceOptsDict({"ode.group": []})
            d["ode.user"] = []
        except:
            pass
        else:
            self.fail("AttributeError: ServiceOptsDict argument must be a"
                      " string, unicode or numeric type")

        try:
            d = ServiceOptsDict({"ode.group": {}})
            d["ode.user"] = {}
        except:
            pass
        else:
            self.fail("AttributeError: ServiceOptsDict argument must be a"
                      " string, unicode or numeric type")

    def test_clear(self):
        d = ServiceOptsDict(
            {"ode.group": -1, "ode.user": 1, "ode.share": 2})
        d.clear()
        assert d == {}

        pytest.raises(TypeError, d.clear, None)

    def test_repr(self):
        d = ServiceOptsDict()
        assert repr(d) == '<ServiceOptsDict: {}>'
        d["ode.group"] = -1
        assert repr(d) == "<ServiceOptsDict: {'ode.group': '-1'}>"

    def test_copy(self):

        def getHash(obj):
            return hex(id(obj))

        d = ServiceOptsDict(
            {"ode.group": -1, "ode.user": 1, "ode.share": 2})
        assert d.copy() == d
        assert getHash(d.copy()) != getHash(d)
        assert ServiceOptsDict().copy() == ServiceOptsDict()
        assert getHash(ServiceOptsDict().copy()) != \
            getHash(ServiceOptsDict())
        pytest.raises(TypeError, d.copy, None)

    def test_setter_an_getter(self):
        group = -1
        user = 1
        share = 2

        d = ServiceOptsDict()
        d.set("ode.group", group)
        assert d.get("ode.group") == d.getOdeGroup()

        d.setOdeGroup(group)
        assert d.get("ode.group") == d.getOdeGroup()

        d.set("ode.user", user)
        assert d.get("ode.user") == d.getOdeUser()

        d.setOdeUser(user)
        assert d.get("ode.user") == d.getOdeUser()

        d.set("ode.share", share)
        assert d.get("ode.share") == d.getOdeShare()

        d.setOdeShare(share)
        assert d.get("ode.share") == d.getOdeShare()


class TestHelpers (object):

    @pytest.mark.parametrize('true_val',
                             [True, "true", "yes", "y", "t", "1", "on"])
    def test_toBoolean_true(self, true_val):
        assert toBoolean(true_val)

    @pytest.mark.parametrize(
        'false_val',
        [False, "false", "f", "no", "n", "none", "0", "[]", "{}", "", "off"])
    def test_toBoolean_false(self, false_val):
        assert not toBoolean(false_val)

    def test_propertiesToDict(self):
        d = {
            '1.1': '11',
            '1.2': '12',
            '2.1': '21',
            '3': '3'
        }
        dictprop = propertiesToDict(d)
        assert len(dictprop) == 3
        assert set(['1', '2', '3']) - set(dictprop.keys()) == set()
        assert len(dictprop['1']) == 2
        assert len(dictprop['2']) == 1
        assert dictprop['1']['1'] == 11
        assert dictprop['1']['2'] == 12
        assert dictprop['2']['1'] == 21
        assert dictprop['3'] == 3

    def test_propertiesToDictWithPrefix(self):
        d = {
            'ode.prefix.str.1': 'mystring',
            'ode.prefix.str.2': '1',
            'ode.prefix.int.1': 1
        }
        dictprop = propertiesToDict(d, prefix='ode.prefix.')
        assert len(dictprop) == 2
        assert set(['str', 'int']) - set(dictprop.keys()) == set()
        assert len(dictprop['str']) == 2
        assert len(dictprop['int']) == 1
        assert dictprop['int']['1'] == 1
        assert dictprop['str']['1'] == 'mystring'
        assert dictprop['str']['2'] == 1

    def test_propertiesToDictBool(self):
        d = {
            'ode.prefix.strbool.false1.enabled': 'False',
            'ode.prefix.strbool.false2.enabled': 'false',
            'ode.prefix.strbool.true1.enabled': 'True',
            'ode.prefix.strbool.true2.enabled': 'true',
            'ode.prefix.bool.1.enabled': False,
            'ode.prefix.bool.2.enabled': True,
            'ode.prefix.str.1.enabled': 't',
            'ode.prefix.str.2.enabled': 'f'
        }
        dictprop = propertiesToDict(d, prefix='ode.prefix.')

        assert len(dictprop) == 3
        assert set(['strbool', 'bool', 'str']) - set(dictprop.keys()) == \
            set()

        assert not dictprop['strbool']['false1']['enabled']
        assert isinstance(dictprop['strbool']['false1']['enabled'], bool)
        assert not dictprop['strbool']['false2']['enabled']
        assert isinstance(dictprop['strbool']['false2']['enabled'], bool)
        assert dictprop['strbool']['true1']['enabled']
        assert isinstance(dictprop['strbool']['true1']['enabled'], bool)
        assert dictprop['strbool']['true2']['enabled']
        assert isinstance(dictprop['strbool']['true2']['enabled'], bool)
        assert not dictprop['bool']['1']['enabled']
        assert isinstance(dictprop['bool']['1']['enabled'], bool)
        assert dictprop['bool']['2']['enabled']
        assert isinstance(dictprop['bool']['2']['enabled'], bool)

        assert dictprop['str']['1']['enabled'] == 't'
        assert dictprop['str']['2']['enabled'] == 'f'
