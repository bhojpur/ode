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
Integration test of metadata_mapannotation
"""

from ode.testlib import ITest
from ode.model import MapAnnotationI, NamedValue
from ode.rtypes import unwrap, wrap
from ode.util.metadata_mapannotations import (
    CanonicalMapAnnotation, MapAnnotationManager)
import pytest
import sys

pythonminver = pytest.mark.skipif(sys.version_info < (2, 7),
                                  reason="requires python2.7")


def assert_equal_map_value(mva, mvb):
    assert len(mva) == len(mvb)
    for a, b in zip(mva, mvb):
        assert isinstance(a, NamedValue)
        assert isinstance(b, NamedValue)
        assert a.name == b.name
        assert a.value == b.value


@pythonminver
class TestMapAnnotationManager(ITest):

    def create_mas(self):
        ns1 = self.uuid()
        ns3 = self.uuid()
        ma1 = MapAnnotationI()
        ma1.setNs(wrap(ns1))
        ma1.setMapValue([NamedValue('a', '1')])
        ma2 = MapAnnotationI()
        ma2.setNs(wrap(ns1))
        ma2.setMapValue([NamedValue('a', '2')])
        ma3 = MapAnnotationI()
        ma3.setNs(wrap(ns3))
        ma3.setMapValue([NamedValue('a', '1')])

        mids = self.update.saveAndReturnIds([ma1, ma2, ma3])
        print(ns1, ns3, mids)
        return ns1, ns3, mids

    def test_add_from_namespace_query(self):
        ns1, ns3, mids = self.create_mas()
        pks = ['a']
        mgr = MapAnnotationManager()
        mgr.add_from_namespace_query(self.sf, ns1, pks)

        assert len(mgr.mapanns) == 2
        pk1 = (ns1, frozenset([('a', '1')]))
        pk2 = (ns1, frozenset([('a', '2')]))
        assert len(mgr.mapanns) == 2
        assert pk1 in mgr.mapanns
        assert pk2 in mgr.mapanns

        cma1 = mgr.mapanns[pk1]
        assert cma1.kvpairs == [('a', '1')]
        assert cma1.parents == set()
        mv1 = cma1.get_mapann().getMapValue()
        assert_equal_map_value(mv1, [NamedValue('a', '1')])

        cma2 = mgr.mapanns[pk2]
        assert cma2.kvpairs == [('a', '2')]
        assert cma2.parents == set()
        mv2 = cma2.get_mapann().getMapValue()
        assert_equal_map_value(mv2, [NamedValue('a', '2')])

    def test_add_from_namespace_query_duplicate(self):
        ns1, ns3, mids = self.create_mas()
        pks = ['a']
        mgr = MapAnnotationManager()
        mgr.add_from_namespace_query(self.sf, ns1, pks)
        with pytest.raises(Exception) as exc_info:
            mgr.add_from_namespace_query(self.sf, ns1, pks)
        assert exc_info.value.message.startswith(
            'Duplicate MapAnnotation primary key')

    def test_update_existing_mapann(self):
        ns1, ns3, mids = self.create_mas()
        pks = ['a']
        mgr = MapAnnotationManager()
        mgr.add_from_namespace_query(self.sf, ns1, pks)

        ma4 = MapAnnotationI()
        ma4 = MapAnnotationI()
        ma4.setNs(wrap(ns1))
        ma4.setMapValue([NamedValue('a', '2'), NamedValue('b', '3'), ])

        cma = CanonicalMapAnnotation(ma4, pks)
        # This should modify ma2
        r = mgr.add(cma)
        assert r is cma

        cmas = mgr.get_map_annotations()
        assert len(cmas) == 2
        rs = self.update.saveAndReturnArray([c.get_mapann() for c in cmas])
        rs = sorted(rs, key=lambda x: unwrap(x.getId()))

        assert_equal_map_value(rs[0].getMapValue(), [NamedValue('a', '1')])
        assert unwrap(rs[0].getNs()) == ns1

        assert_equal_map_value(rs[1].getMapValue(), [
            NamedValue('a', '2'), NamedValue('b', '3')])
        assert unwrap(rs[1].getNs()) == ns1
