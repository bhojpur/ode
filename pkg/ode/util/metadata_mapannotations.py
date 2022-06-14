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
Utilities for manipulating map-annotations used as metadata
"""

from builtins import str
from builtins import range
from builtins import object
import logging
from ode.model import NamedValue
from ode.rtypes import rstring, unwrap
# For complicated reasons `from ode.sys import ParametersI` doesn't work
from ode_sys_ParametersI import ParametersI

log = logging.getLogger("ode.util.metadata_mapannotations")

class MapAnnotationPrimaryKeyException(Exception):

    def __init__(self, message):
        super(MapAnnotationPrimaryKeyException, self).__init__(message)

class CanonicalMapAnnotation(object):
    """
    A canonical representation of a map-annotation for metadata use
    This is based around the idea of a primary key derived from the
    combination of the namespace with 1+ keys-value pairs. A null
    namespace is treated as an empty string (''), but still forms part
    of the primary key.

    ma: The ode.model.MapAnnotation object
    primary_keys: Keys from key-value pairs that will be used to form the
        primary key.
    """

    def __init__(self, ma, primary_keys=None):
        # TODO: should we consider data and description
        self.ma = ma
        ns = unwrap(ma.getNs())
        self.ns = ns if ns else ''
        try:
            mapvalue = [(kv.name, kv.value) for kv in ma.getMapValue()]
        except TypeError:
            mapvalue = []
        self.kvpairs, self.primary = self.process_keypairs(
            mapvalue, primary_keys)
        self.parents = set()

    def process_keypairs(self, kvpairs, primary_keys):
        if len(set(kvpairs)) != len(kvpairs):
            raise ValueError('Duplicate key-value pairs found: %s' % kvpairs)

        if primary_keys:
            primary_keys = set(primary_keys)
            missing = primary_keys.difference(kv[0] for kv in kvpairs)
            if missing:
                raise MapAnnotationPrimaryKeyException(
                    'Missing primary key fields: %s' % missing)
            # ns is always part of the primary key
            primary = (
                self.ns,
                frozenset((k, v) for (k, v) in kvpairs if k in primary_keys))
        else:
            primary = None

        return kvpairs, primary

    def merge(self, other):
        """
        Adds any key/value pairs from other that aren't in self
        Adds parents from other
        Does not update primary key
        """
        if self.kvpairs != other.kvpairs:
            kvpairsset = set(self.kvpairs)
            for okv in other.kvpairs:
                if okv not in kvpairsset:
                    self.kvpairs.append(okv)
        self.merge_parents(other)

    def merge_parents(self, other):
        self.parents.update(other.parents)

    def add_parent(self, parenttype, parentid):
        """
        Add a parent descriptor
        Parameter types are important because they are used in a set

        parenttype: An ODE type string
        parentid: An ODE object ID (integer)
        """
        if not isinstance(parenttype, str) or not isinstance(
                parentid, int):
            raise ValueError('Expected parenttype:str parentid:integer')
        self.parents.add((parenttype, parentid))

    def get_mapann(self):
        """
        Update and return an ode.model.MapAnnotation with merged/combined
        fields
        """
        mv = [NamedValue(*kv) for kv in self.kvpairs]
        self.ma.setMapValue(mv)
        self.ma.setNs(rstring(self.ns))
        return self.ma

    def get_parents(self):
        return self.parents

    def __str__(self):
        return 'ns:%s primary:%s keyvalues:%s parents:%s id:%s' % (
            self.ns, self.primary, self.kvpairs, self.parents,
            unwrap(self.ma.getId()))

class MapAnnotationManager(object):
    """
    Handles creation and de-duplication of MapAnnotations
    """
    # Policies for combining/replacing MapAnnotations
    MA_APPEND, MA_OLD, MA_NEW = list(range(3))

    def __init__(self, combine=MA_APPEND):
        """
        Ensure you understand the doc string for init_from_namespace_query
        if not using MA_APPEND
        """
        self.mapanns = {}
        self.nokey = []
        self.combine = combine

    def add(self, cma):
        """
        Adds a CanonicalMapAnnotation to the managed list.

        Returns any CanonicalMapAnnotation that are no longer required,
        this may be cma or it may be a previously added annotation.
        The idea is that this can be used to de-duplicate existing ODE
        MapAnnotations by calling add() on all MapAnnotations and deleting
        those which are returned

        If MapAnnotations are combined the parents of the unwanted
        MapAnnotations are appended to the one that is kept by the manager.

        :param cma: A CanonicalMapAnnotation
        """

        if cma.primary is None:
            self.nokey.append(cma)
            return

        try:
            current = self.mapanns[cma.primary]
            if current.ma is cma.ma:
                # Don't re-add an identical object
                return
            if self.combine == self.MA_APPEND:
                current.merge(cma)
                return cma
            if self.combine == self.MA_NEW:
                self.mapanns[cma.primary] = cma
                cma.merge_parents(current)
                return current
            if self.combine == self.MA_OLD:
                current.merge_parents(cma)
                return cma
            raise ValueError('Invalid combine policy')
        except KeyError:
            self.mapanns[cma.primary] = cma

    def get_map_annotations(self):
        return list(self.mapanns.values()) + self.nokey

    def add_from_namespace_query(self, session, ns, primary_keys):
        """
        Fetches all map-annotations with the given namespace
        This will only work if there are no duplicates, otherwise an
        exception will be thrown

        WARNING: You should probably only use this in MA_APPEND mode since
        the parents of existing annotations aren't fetched (requires a query
        for each parent type)
        WARNING: This may be resource intensive
        TODO: Use ode.utils.populate_metadata._QueryContext for batch queries

        :param session: An ODE session
        :param ns: The namespace
        :param primary_keys: Primary keys
        """
        qs = session.getQueryService()
        q = 'FROM MapAnnotation WHERE ns=:ns ORDER BY id DESC'
        p = ParametersI()
        p.addString('ns', ns)
        results = qs.findAllByQuery(q, p)
        log.debug('Found %d MapAnnotations in ns:%s', len(results), ns)
        for ma in results:
            cma = CanonicalMapAnnotation(ma, primary_keys)
            r = self.add(cma)
            if r:
                raise Exception(
                    'Duplicate MapAnnotation primary key: id:%s %s' % (
                        unwrap(ma.getId()), str(r)))