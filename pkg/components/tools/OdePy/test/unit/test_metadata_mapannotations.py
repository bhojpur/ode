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
Test of metadata_mapannotation classes
"""

import pytest

from ode.model import MapAnnotationI, NamedValue
from ode.rtypes import rstring
from ode.util.metadata_mapannotations import (
    CanonicalMapAnnotation, MapAnnotationPrimaryKeyException,
    MapAnnotationManager)

def assert_equal_name_value(a, b):
    assert isinstance(a, NamedValue)
    assert isinstance(b, NamedValue)
    assert a.name == b.name
    assert a.value == b.value

class TestCanonicalMapAnnotation(object):

    # test_init_* methods test process_keypairs

    @pytest.mark.parametrize('ns', [None, '', 'NS'])
    def test_init_defaults_empty(self, ns):
        ma = MapAnnotationI(1)
        if ns is not None:
            ma.setNs(rstring(ns))

        cma = CanonicalMapAnnotation(ma)
        assert cma.ma is ma
        expectedns = ns if ns else ''
        assert cma.ns == expectedns
        assert cma.kvpairs == []
        assert cma.primary is None
        assert cma.parents == set()

    @pytest.mark.parametrize('ns', [None, '', 'NS'])
    def test_init_defaults_kvpairs(self, ns):
        ma = MapAnnotationI(1)
        ma.setMapValue([NamedValue('b', '2'), NamedValue('a', '1')])
        if ns is not None:
            ma.setNs(rstring(ns))

        cma = CanonicalMapAnnotation(ma)
        assert cma.ma is ma
        expectedns = ns if ns else ''
        assert cma.ns == expectedns
        assert cma.kvpairs == [('b', '2'), ('a', '1')]
        assert cma.primary is None
        assert cma.parents == set()

    def test_init_defaults_duplicates(self):
        ma = MapAnnotationI(1)
        ma.setMapValue([NamedValue('a', '1'), NamedValue('a', '1')])

        with pytest.raises(ValueError) as excinfo:
            CanonicalMapAnnotation(ma)
        assert str(excinfo.value).startswith('Duplicate ')

    @pytest.mark.parametrize('ns', [None, '', 'NS'])
    @pytest.mark.parametrize('primary_keys', [[], ['a'], ['a', 'b']])
    def test_init_primary_keys(self, ns, primary_keys):
        ma = MapAnnotationI(1)
        ma.setNs(rstring(ns))
        ma.setMapValue([NamedValue('b', '2'), NamedValue('a', '1')])

        cma = CanonicalMapAnnotation(ma, primary_keys=primary_keys)
        assert cma.ma is ma
        expectedns = ns if ns else ''
        assert cma.ns == expectedns
        assert cma.kvpairs == [('b', '2'), ('a', '1')]
        expectedpks = [('a', '1'), ('b', '2')][:len(primary_keys)]
        if primary_keys:
            expectedpri = (expectedns, frozenset(expectedpks))
        else:
            expectedpri = None
        assert cma.primary == expectedpri
        assert cma.parents == set()

    def test_init_missing_primary_key(self):
        ma = MapAnnotationI(1)
        ma.setMapValue([NamedValue('b', '2')])

        with pytest.raises(MapAnnotationPrimaryKeyException) as excinfo:
            CanonicalMapAnnotation(ma, primary_keys=['a', 'b'])
        assert str(excinfo.value).startswith('Missing ')

    @pytest.mark.parametrize('reverse', [True, False])
    def test_merge(self, reverse):
        ma1 = MapAnnotationI(1)
        ma1.setMapValue([NamedValue('a', '1')])
        cma1 = CanonicalMapAnnotation(ma1, primary_keys=['a'])
        cma1.add_parent('Parent', 1)
        ma2 = MapAnnotationI(2)
        ma2.setMapValue([NamedValue('b', '2'), NamedValue('a', '1')])
        cma2 = CanonicalMapAnnotation(ma2, primary_keys=['b'])
        cma2.add_parent('Parent', 1)
        cma2.add_parent('Parent', 2)

        if reverse:
            cma2.merge(cma1)
            assert cma2.kvpairs == [('b', '2'), ('a', '1')]
            assert cma2.parents == set([('Parent', 1), ('Parent', 2)])
            assert cma2.primary == ('', frozenset([('b', '2')]))
        else:
            cma1.merge(cma2)
            assert cma1.kvpairs == [('a', '1'), ('b', '2')]
            assert cma1.parents == set([('Parent', 1), ('Parent', 2)])
            assert cma1.primary == ('', frozenset([('a', '1')]))

    def test_add_parent(self):
        ma = MapAnnotationI(1)
        cma = CanonicalMapAnnotation(ma)
        assert cma.parents == set()
        cma.add_parent('A', 1)
        assert cma.parents == set([('A', 1)])
        cma.add_parent('B', 2)
        assert cma.parents == set([('A', 1), ('B', 2)])

        with pytest.raises(ValueError) as excinfo:
            cma.add_parent('C', '3')
        assert str(excinfo.value).startswith('Expected parent')

    def test_get_mapann(self):
        ma = MapAnnotationI(1)
        ma.setMapValue([NamedValue('a', '1')])
        cma = CanonicalMapAnnotation(ma, primary_keys=['a'])
        cma.add_parent('Parent', 1)
        cma.kvpairs.append(('b', '2'))
        newma = cma.get_mapann()

        assert newma is ma
        mv = newma.getMapValue()
        assert len(mv) == 2
        assert_equal_name_value(mv[0], NamedValue('a', '1'))
        assert_equal_name_value(mv[1], NamedValue('b', '2'))


class TestMapAnnotationManager(object):

    def create_cmas(self, pk2):
        ma1 = MapAnnotationI(1)
        ma1.setMapValue([NamedValue('a', '1')])
        cma1 = CanonicalMapAnnotation(ma1, primary_keys=['a'])
        cma1.add_parent('Parent', 1)

        ma2 = MapAnnotationI(2)
        ma2.setMapValue([NamedValue('b', '2'), NamedValue('a', '1')])
        cma2 = CanonicalMapAnnotation(ma2, primary_keys=[pk2])
        cma2.add_parent('Parent', 1)
        cma2.add_parent('Parent', 2)

        return cma1, cma2

    @pytest.mark.parametrize('combine', [
        None, MapAnnotationManager.MA_APPEND, MapAnnotationManager.MA_OLD,
        MapAnnotationManager.MA_NEW])
    def test_add_samepk(self, combine):
        cma1, cma2 = self.create_cmas('a')
        pk1 = ('', frozenset([('a', '1')]))
        parents12 = set([('Parent', 1), ('Parent', 2)])

        if combine:
            mgr = MapAnnotationManager(combine)
        else:
            mgr = MapAnnotationManager()
        assert mgr.mapanns == {}

        # These tests all make use of object identity
        # Sanity check:
        assert cma1 != cma2

        r = mgr.add(cma1)
        assert mgr.mapanns == {pk1: cma1}
        assert r is None

        # Duplicate add should have no effect
        r = mgr.add(cma1)
        assert mgr.mapanns == {pk1: cma1}
        assert r is None

        r = mgr.add(cma2)
        if combine == MapAnnotationManager.MA_OLD:
            assert mgr.mapanns == {pk1: cma1}
            assert cma1.kvpairs == [('a', '1')]
            assert cma1.parents == parents12
            assert r is cma2
        elif combine == MapAnnotationManager.MA_NEW:
            assert mgr.mapanns == {pk1: cma2}
            assert cma2.kvpairs == [('b', '2'), ('a', '1')]
            assert cma2.parents == parents12
            assert r is cma1
        else:  # None or MA_APPEND
            assert mgr.mapanns == {pk1: cma1}
            assert cma1.kvpairs == [('a', '1'), ('b', '2')]
            assert cma1.parents == parents12
            assert r is cma2

    @pytest.mark.parametrize('combine', [
        MapAnnotationManager.MA_APPEND, MapAnnotationManager.MA_OLD,
        MapAnnotationManager.MA_NEW])
    def test_add_diffpk(self, combine):
        cma1, cma2 = self.create_cmas('b')
        pk1 = ('', frozenset([('a', '1')]))
        pk2 = ('', frozenset([('b', '2')]))

        mgr = MapAnnotationManager(combine)
        r = mgr.add(cma1)
        assert r is None

        r = mgr.add(cma2)
        assert mgr.mapanns == {pk1: cma1, pk2: cma2}
        assert cma1.kvpairs == [('a', '1')]
        assert cma1.parents == set([('Parent', 1)])
        assert cma2.kvpairs == [('b', '2'), ('a', '1')]
        assert cma2.parents == set([('Parent', 1), ('Parent', 2)])
        assert r is None
