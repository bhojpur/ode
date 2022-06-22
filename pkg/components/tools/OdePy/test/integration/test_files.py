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
   Test of client upload/download functionality
"""

import pytest
from ode.testlib import ITest

from ode.util.temp_files import create_path

def tmpfile():
    file = create_path()
    file.write_lines(["abc", "def", "123"])
    return file

class TestFiles(ITest):

    def testUploadDownload(self):
        uploaded = tmpfile()
        downloaded = create_path()
        ofile = self.client.upload(str(uploaded), type="text/plain")
        self.client.download(ofile, str(downloaded))
        lines = downloaded.lines()
        assert "abc\n" == lines[0], lines[0]
        assert "def\n" == lines[1], lines[1]
        assert "123\n" == lines[2], lines[2]
        sha1_upload = self.client.sha1(str(uploaded))
        sha1_download = self.client.sha1(str(downloaded))
        assert sha1_upload == sha1_download, "%s!=%s" % (
            sha1_upload, sha1_download)

    @pytest.mark.broken(ticket="11610")
    def testUploadDifferentSizeTicket2337(self):
        uploaded = tmpfile()
        ofile = self.client.upload(str(uploaded), type="text/plain")
        uploaded.write_lines(["abc", "def"])  # Shorten
        ofile = self.client.upload(
            str(uploaded), type="text/plain", ofile=ofile)

        downloaded = create_path()
        self.client.download(ofile, str(downloaded))
        lines = downloaded.lines()
        assert 2 == len(lines)
        assert "abc\n" == lines[0], lines[0]
        assert "def\n" == lines[1], lines[1]

        sha1_upload = self.client.sha1(str(uploaded))
        sha1_download = self.client.sha1(str(downloaded))
        assert sha1_upload == sha1_download, "%s!=%s" % (
            sha1_upload, sha1_download)
