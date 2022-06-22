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
   Integration test focused on the ode.api.ITypes interface
   a running server.
"""

from ode.testlib import ITest

from ode.rtypes import rstring

class TestTypes(ITest):

    # ticket:1436

    def testGetEnumerationTypes(self):
        self.client.sf.getTypesService().getEnumerationTypes()

    def testAllEnumerations(self):
        types = self.root.sf.getTypesService()
        rv = dict()
        for e in types.getOriginalEnumerations():
            if rv.get(e.__class__.__name__) is None:
                rv[e.__class__.__name__] = list()
            rv[e.__class__.__name__].append(e)

        for r in rv:
            types.allEnumerations(str(r))

    def testGetEnumerationWithEntries(self):
        self.root.sf.getTypesService().getEnumerationsWithEntries().items()

    def testManageEnumeration(self):
        from ode_model_ExperimentTypeI import ExperimentTypeI
        uuid = self.root.sf.getAdminService().getEventContext().sessionUuid
        types = self.root.sf.getTypesService()

        # create enums
        obj = ExperimentTypeI()
        obj.setValue(rstring("test_value_%s" % uuid))
        enum = types.createEnumeration(obj)
        types.deleteEnumeration(enum)

        obj = ExperimentTypeI()
        obj.setValue(rstring("test_value2_%s" % (uuid)))
        new_entries = [obj]
        types.updateEnumerations(new_entries)

        types.resetEnumerations("ExperimentTypeI")
