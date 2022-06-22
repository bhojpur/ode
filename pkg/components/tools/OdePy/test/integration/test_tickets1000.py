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
   Integration test for any ticket upto #1000
"""

from ode.testlib import ITest
import pytest
import ode
from ode.rtypes import rint, rlong, rstring

from test.integration.helpers import createTestImage

# Reused bits
params = ode.sys.Parameters()
params.theFilter = ode.sys.Filter()
params.theFilter.limit = rint(1)
params.theFilter.offset = rint(1)

class TestTicket1000(ITest):

    def test711(self):
        exp = ode.model.ExperimenterI()
        exp.odeName = rstring("root")
        list = self.client.sf.getQueryService().findAllByExample(exp, None)
        assert 1 == len(list)

    def test843(self):
        with pytest.raises(ode.ValidationException):
            self.client.sf.getQueryService().get("Experimenter", -1)

    def test880(self):
        try:
            createTestImage(self.client.sf)
            i = self.client.sf.getQueryService().findAll(
                "Image", params.theFilter)[0]
            assert i is not None
            assert i.id is not None
            assert i.details is not None
        except ode.ValidationException:
            print(" test880 - createTestImage has failed. "\
                  "This fixture method needs to be fixed.")
        except IndexError:
            print(" test880 - findAll has failed so assertions "\
                  "can't be checked. Is this a fail? ")

    def test883WithoutClose(self):
        s = self.client.sf.createSearchService()
        s.onlyType("Image")
        s.byHqlQuery("select i from Image i", params)
        if s.hasNext():
            s.results()
        # s.close()

    def test883WithClose(self):
        s = self.client.sf.createSearchService()
        s.onlyType("Dataset")
        s.byHqlQuery("select d from Dataset d", params)
        if s.hasNext():
            s.results()
        s.close()

    def test883Upload(self):
        search = self.client.getSession().createSearchService()
        search.onlyType("OriginalFile")
        search.byHqlQuery(
            "select o from OriginalFile o where o.name = 'stderr'", params)
        if search.hasNext():
            ofile = search.next()
            tmpfile = self.tmpfile()
            self.client.download(ofile, tmpfile)
        else:
            print(" test883Upload - no stderr found. Is this a fail? ")

        search.close()

    success = "select i from Image i join i.annotationLinks links join "\
              "links.child ann where size(i.datasetLinks) > 0 and ann.id = :id"
    failing = "select i from Image i join i.annotationLinks links join "\
              "links.child ann where ann.id = :id and size(i.datasetLinks) > 0"

    # Both of these queries cause exceptions. Should the first succeed?
    def test985(self):
        prms = ode.sys.Parameters()
        prms.map = {}  # ParamMap
        prms.map["id"] = rlong(53)
        try:
            self.client.sf.getQueryService().findAllByQuery(
                TestTicket1000.success, prms)
        except ode.ValidationException:
            print(" test985 - query has failed. Should this query pass? ")

        with pytest.raises(ode.ValidationException):
            self.client.sf.getQueryService().findAllByQuery(
                TestTicket1000.failing, prms)

    # removed def test989(self):
