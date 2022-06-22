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
Tests for the MapAnnotation and related base types introduced in 1.0.
"""

from ode.testlib import ITest
import pytest
import ode

from ode_model_ExperimenterGroupI import ExperimenterGroupI
from ode_model_MapAnnotationI import MapAnnotationI
from ode.rtypes import rbool, rstring
from ode.rtypes import unwrap
from ode.model import NamedValue as NV

class TestMapAnnotation(ITest):

    def assertNV(self, nv, name, value):
        assert name == nv.name
        assert value == nv.value

    def assertNVs(self, nvl1, nvl2):
        for i in range(max(len(nvl1), len(nvl2))):
            assert nvl1[i].name == nvl2[i].name
            assert nvl1[i].value == nvl2[i].value

    def testMapStringField(self):
        uuid = self.uuid()
        queryService = self.root.getSession().getQueryService()
        updateService = self.root.getSession().getUpdateService()
        group = ExperimenterGroupI()
        group.setName(rstring(uuid))
        group.setLdap(rbool(False))
        group.setConfig([NV("language", "python")])
        group = updateService.saveAndReturnObject(group)
        group = queryService.findByQuery(
            ("select g from ExperimenterGroup g join fetch g.config "
             "where g.id = %s" % group.getId().getValue()), None)
        self.assertNV(group.getConfig()[0], "language", "python")

    @pytest.mark.parametrize("data", (
        ([NV("a", "")], [NV("a", "")]),
        ([NV("a", "b")], [NV("a", "b")]),
    ))
    def testGroupConfigA(self, data):

        save_value, expect_value = data

        LOAD = ("select g from ExperimenterGroup g "
                "left outer join fetch g.config where g.id = :id")

        queryService = self.root.sf.getQueryService()
        updateService = self.root.sf.getUpdateService()
        params = ode.sys.ParametersI()

        def load_group(id):
            params.addId(id)
            return queryService.findByQuery(LOAD, params)

        group = self.new_group()
        gid = group.id.val
        group.config = save_value

        updateService.saveObject(group)
        group = load_group(gid)
        config = unwrap(group.config)

        self.assertNVs(expect_value, config)

        name, value = unwrap(queryService.projection(
            """
            select m.name, m.value from ExperimenterGroup g
            left outer join g.config m where m.name = 'a'
            and g.id = :id
            """, ode.sys.ParametersI().addId(gid))[0])

        self.assertNV(expect_value[0], name, value)

    def testGroupConfigEdit(self):

        before = [
            NV("a", "b"),
            NV("c", "d"),
            NV("e", "f")
        ]

        remove_one = [
            NV("a", "b"),
            NV("e", "f")
        ]

        swapped = [
            NV("e", "f"),
            NV("a", "b")
        ]

        edited = [
            NV("e", "f"),
            NV("a", "x")
        ]

        root_update = self.root.sf.getUpdateService()

        group = self.new_group()
        group.setConfig(before)
        group = root_update.saveAndReturnObject(group)
        self.assertNVs(before, group.getConfig())

        del group.getConfig()[1]
        group = root_update.saveAndReturnObject(group)
        self.assertNVs(remove_one, group.getConfig())

        old = list(group.getConfig())
        self.assertNVs(old, remove_one)
        group.setConfig([old[1], old[0]])
        group = root_update.saveAndReturnObject(group)
        self.assertNVs(swapped, group.getConfig())

        group.getConfig()[1].value = "x"
        group = root_update.saveAndReturnObject(group)
        self.assertNVs(edited, group.getConfig())

    def testEmptyItem(self):
        a = MapAnnotationI()
        a.setMapValue([NV('Name1', 'Value1'), NV('Name2', 'Value2')])
        a = self.update.saveAndReturnObject(a)
        m = self.query.findAllByQuery((
            "from MapAnnotation m "
            "join fetch m.mapValue a "
            "where a.name='Name2'"), None)[0]
        l1 = m.getMapValue()
        assert l1[0] is None
        self.assertNV(l1[1], "Name2", "Value2")
        assert {"Name2": "Value2"} == m.getMapValueAsMap()

    def testBigKeys(self):
        uuid = self.uuid()
        big = uuid + "X" * 500
        a = MapAnnotationI()
        a.setMapValue([NV(big, big)])
        a = self.update.saveAndReturnObject(a)
        m = self.query.findAllByQuery((
            "from MapAnnotation m "
            "join fetch m.mapValue a "
            "where a.name like :name"),
            ode.sys.ParametersI().addString("name",
                                              uuid + "%")
        )[0]
        l1 = m.getMapValue()
        self.assertNV(l1[0], big, big)
