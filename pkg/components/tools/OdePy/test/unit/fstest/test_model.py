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
Basic usage of the "fileset" model objects.
These tests don't do anything functional, but
just show creation / linkage scenarios.
"""

import time
import ode
import ode.all

from ode.rtypes import rstring
from ode.rtypes import rtime

class TestModel(object):

    def mkentry(self, clientPath):
        originalFile = ode.model.OriginalFileI()
        parts = clientPath.split("/")
        path = "/".join(parts[:-1])
        name = parts[-1]
        originalFile.path = rstring(path)
        originalFile.name = rstring(name)
        # etc.
        entry = ode.model.FilesetEntryI()
        entry.clientPath = rstring(clientPath)
        entry.originalFile = originalFile
        return entry

    def testBasicImport(self):
        """
        basic server-side import steps

        Once a list of file paths have been passed to the server,
        an ode.model.Fileset object will be created which captures
        the state of the import.
        """

        # This will be created server-side
        serverInfo = {}
        serverInfo['bioformatsReader'] = rstring("ExampleReader")
        serverInfo['bioformatsVersion'] = rstring("v4.4.5 git: abc123"),
        serverInfo['odeVersion'] = rstring("v.4.4.4 git: def456"),
        serverInfo['osName'] = rstring("Linux"),
        serverInfo['osArchitecture'] = rstring("amd64"),
        serverInfo['osVersion'] = rstring("2.6.38-8-generic"),
        serverInfo['locale'] = rstring("en_US")

        # Now that the basics are setup, we
        # need to link to all of the original files.
        fs = ode.model.FilesetI()
        fs.addFilesetEntry(self.mkentry("main_file.txt"))  # First!
        fs.addFilesetEntry(self.mkentry("uf1.data"))
        fs.addFilesetEntry(self.mkentry("uf2.data"))

        # Now that the files are all added, we
        # add the "activities" that will be
        # performed on them.

        # Uploading is almost always the first
        # step, and must be completed by the clients
        # before any other activity.
        job1 = ode.model.UploadJobI()
        job1.scheduledFor = rtime(time.time() * 1000)  # Now
        # Set this "started" since we're expecting
        # upload to be in process.

        # Import is a server-side activity which
        # causes the files to be parsed and their
        # metadata to be stored.
        job2 = ode.model.MetadataImportJobI()

        # Other possible activities include "pyramids"
        # and "re-import"

        fs.linkJob(job1)
        fs.linkJob(job2)
