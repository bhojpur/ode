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
   gateway tests - Testing speed of using Server gateway for various queries
                   particularly checking whether lazy loading can hurt
                   performance vv loading graphs.

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import pytest
import ode
import time

from ode.rtypes import rstring, rlong


class TestPerformance (object):

    @pytest.mark.broken(ticket="11494")
    def testListFileAnnotations(self, gatewaywrapper):
        """
        testListFileAnnotations: test speed of getObjects('FileAnnotation') vv
        listFileAnnotations()
        """
        gatewaywrapper.loginAsAuthor()
        updateService = gatewaywrapper.gateway.getUpdateService()

        def createFileAnnotation(name, ns):
            originalFile = ode.model.OriginalFileI()
            originalFile.setName(rstring(name))
            originalFile.setPath(rstring(name))
            originalFile.setSize(rlong(0))
            originalFile.setHash(rstring("Foo"))
            originalFile = updateService.saveAndReturnObject(originalFile)
            fa = ode.model.FileAnnotationI()
            fa.setFile(originalFile)
            fa.setNs(rstring(ns))
            fa = updateService.saveAndReturnObject(fa)
            return fa.id.val

        ns = "ode.gatewaytest.PerformanceTest.testListFileAnnotations"
        fileCount = 250

        fileAnnIds = [createFileAnnotation("testListFileAnnotations%s" % i,
                      ns) for i in range(fileCount)]

        # test speed of listFileAnnotations
        startTime = time.time()
        fileCount = 0
        fileAnns = gatewaywrapper.gateway.listFileAnnotations(toInclude=[ns])
        for fa in fileAnns:
            fa.getFileName()
            fileCount += 1
        t1 = time.time() - startTime
        print("listFileAnnotations for %d files = %s secs" % (fileCount, t1))
        # Typically 1.4 secs

        # test speed of getOjbects("Annotation") - lazy loading file names
        startTime = time.time()
        fileCount = 0
        fileAnns = gatewaywrapper.gateway.getObjects(
            "FileAnnotation", attributes={'ns': ns})
        for fa in fileAnns:
            fa.getFileName()
            fileCount += 1
        t2 = time.time() - startTime
        print("getObjects, lazy loading file names for %d files = %s secs" \
            % (fileCount, t2))  # Typically 2.8 secs

        # test speed of getOjbects("Annotation") - NO loading file names
        startTime = time.time()
        fileCount = 0
        fileAnns = gatewaywrapper.gateway.getObjects(
            "FileAnnotation", attributes={'ns': ns})
        for fa in fileAnns:
            fa.getId()
            fileCount += 1
        t3 = time.time() - startTime
        print("getObjects, NO file names for %d files = %s secs" \
            % (fileCount, t3))  # Typically 0.4 secs

        assert t1 < t2, "Server listFileAnnotations() should be faster " \
            "than getObjects('FileAnnotation')"
        assert t3 < t2, "Server getObjects('FileAnnotation') should be " \
            "faster without fa.getFileName()"
        assert t3 < t1, "Server getting unloaded 'FileAnnotation' should be" \
            " faster than listFileAnnotations()"

        # now delete what we have created
        handle = gatewaywrapper.gateway.deleteObjects("Annotation", fileAnnIds)
        gatewaywrapper.waitOnCmd(gatewaywrapper.gateway.c, handle)
