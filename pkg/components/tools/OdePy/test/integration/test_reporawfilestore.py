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
   Integration test focused on the BfPixelsStore API

   This test compares data got through BFPixelsStore using
   different methods. No file needs to be imported for these
   tests. bfpixelsstoreexternal.py tests that the methods
   return the same data as the equivalent rps methods would
   for imported data.
"""

import pytest
import ode
import ode.gateway

from path import path
from ode.testlib import AbstractRepoTest

class TestRepoRawFileStore(AbstractRepoTest):

    def setup_method(self, method):
        super(TestRepoRawFileStore, self).setup_method(method)
        tmp_dir = path(self.unique_dir)
        self.repoPrx = self.get_managed_repo()
        self.repo_filename = tmp_dir / self.uuid() + ".txt"

    def testCreate(self):
        rfs = self.repoPrx.file(self.repo_filename, "rw")
        assert rfs.size() == 0

    def testWrite(self):
        rfs = self.repoPrx.file(self.repo_filename, "rw")
        assert rfs.size() == 0
        wbytes = "0123456789"
        rfs.write(wbytes, 0, len(wbytes))
        assert rfs.size() == len(wbytes)

    def testFailedWrite(self):
        # Perform a touch
        rfs = self.repoPrx.file(self.repo_filename, "rw")  # create empty file
        rfs.write([], 0, 0)
        rfs.close()

        rfs = self.repoPrx.file(self.repo_filename, "r")
        assert rfs.size() == 0
        wbytes = "0123456789"
        try:
            rfs.write(wbytes, 0, len(wbytes))
        except:
            pass
        assert rfs.size() == 0

    @pytest.mark.broken(ticket="11610")
    def testFailedWriteNoFile(self):
        # Without a single write, no file is produced
        rfs = self.repoPrx.file(self.repo_filename, "rw")  # create empty file
        rfs.close()

        rfs = self.repoPrx.file(self.repo_filename, "r")
        with pytest.raises(ode.ResourceError):
            rfs.size()
        wbytes = "0123456789"
        try:
            rfs.write(wbytes, 0, len(wbytes))
        except:
            pass
        with pytest.raises(ode.ResourceError):
            rfs.size()

    def testWriteRead(self):
        rfs = self.repoPrx.file(self.repo_filename, "rw")
        assert rfs.size() == 0
        wbytes = "0123456789"
        rfs.write(wbytes, 0, len(wbytes))
        assert rfs.size() == len(wbytes)
        rbytes = rfs.read(0, len(wbytes))
        assert wbytes == rbytes

    def testAppend(self):
        rfs = self.repoPrx.file(self.repo_filename, "rw")
        assert rfs.size() == 0
        wbytes = "0123456789"
        rfs.write(wbytes, 0, len(wbytes))
        assert rfs.size() == len(wbytes)
        end = rfs.size()
        rfs.write(wbytes, end, len(wbytes))
        assert rfs.size() == 2 * len(wbytes)
        rbytes = rfs.read(0, 2 * len(wbytes))
        assert wbytes + wbytes == rbytes

    def testTruncateToZero(self):
        rfs = self.repoPrx.file(self.repo_filename, "rw")
        assert rfs.size() == 0
        wbytes = "0123456789"
        rfs.write(wbytes, 0, len(wbytes))
        assert rfs.size() == len(wbytes)
        assert rfs.truncate(0)
        assert rfs.size() == 0

    def testClose(self):
        rfs = self.repoPrx.file(self.repo_filename, "rw")
        assert rfs.size() == 0
        wbytes = "0123456789"
        rfs.write(wbytes, 0, len(wbytes))
        assert rfs.size() == len(wbytes)
        rbytes = rfs.read(0, len(wbytes))
        assert wbytes == rbytes
        try:
            rfs.close()
        except:
            # FIXME: close throws an NPE but should close the filehandle...
            pass
        try:
            rbytes = rfs.read(0, len(wbytes))
        except:
            pass  # FIXME: ... so an exception should be thrown here now.
        rfs = self.repoPrx.file(self.repo_filename, "r")
        assert rfs.size() == len(wbytes)

    # ticket:11154
    def testImportLogFilenameSetting(self):
        q = self.root.sf.getQueryService()

        with pytest.raises(ode.SecurityViolation):
            q.projection("select e.id from Experimenter e where e.id = 0",
                         None, {"ode.logfilename": "/tmp/foo.log"})
