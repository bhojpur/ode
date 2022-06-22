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

import logging
logging.basicConfig(level=0)

import ode.util.temp_files as t_f

class TestTemps(object):

    def testBasicUsage(self):
        p = t_f.create_path("foo", ".bar")
        assert p.exists()
        t_f.remove_path(p)
        assert not p.exists()

    def testBasicUsagePassString(self):
        p = t_f.create_path("foo", ".bar")
        assert p.exists()
        t_f.remove_path(str(p))
        assert not p.exists()

    def testNoCleanUp(self):
        p = t_f.create_path("foo", ".bar")
        assert p.exists()
        # Logger should print out one file

    def testUsingThePath(self):
        p = t_f.create_path("write", ".txt")
        p.write_text("hi")
        assert ["hi"] == p.lines()

    def testUsingThePath2(self):
        p = t_f.create_path("write2", ".txt")
        p.write_text("hi2")
        assert ["hi2"] == p.lines()

    def testUsingThePathAndAFile(self):
        p = t_f.create_path("write", ".txt")
        p.write_text("hi")
        f = open(str(p), "r")
        assert ["hi"] == f.readlines()
        f.close()

    def testFolderSimple(self):
        p = t_f.create_path("close", ".dir", folder=True)
        assert p.exists()
        assert p.isdir()
        return p

    def testFolderWrite(self):
        p = self.testFolderSimple()
        f = p / "file"
        f.write_text("hi")
        return p

    def testFolderDelete(self):
        p = self.testFolderWrite()
        p.rmtree()

    #
    # Misc
    #

    def DISABLEDtestManagerPrefix(self):
        mgr = t_f.TempFileManager(prefix="ode_temp_files_test")
        dir = mgr.gettempdir()
        mgr.clean_tempdir()  # start with a blank dir
        assert not dir.exists()
        mgr.create_path("test", ".tmp")
        assert dir.exists()
        mgr.clean_tempdir()
        # There should still be one file lock
        assert dir.exists()
