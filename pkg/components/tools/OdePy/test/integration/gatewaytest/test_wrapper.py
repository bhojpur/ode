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
   gateway tests - Object Wrappers

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
   - author_testimg
"""

import Ice
import ode
import os
import pytest

class TestWrapper(object):

    def setUp(self, gatewaywrapper):
        super(TestWrapper, self).setUp()
        gatewaywrapper.loginAsAuthor()
        self.TESTIMG = gatewaywrapper.getTestImage()

    def testAllObjectsWrapped(self, author_testimg):
        """
        Server object wrapper should ensure that all values returned are also
        wrapped (or are primative values)
        """
        image = author_testimg
        pixels = image.getPrimaryPixels()
        instrument = image.getInstrument()
        assert isinstance(instrument, ode.gateway.ServerObjectWrapper)
        assert not hasattr(image.getArchived(), 'val'), \
            "Shouldn't return rtype"
        assert not hasattr(image.getAcquisitionDate(), 'val'), \
            "Shouldn't return rtype"
        assert not hasattr(image.sizeOfPixels(), 'val'), \
            "Non 'get' methods shouldn't return rtype either"
        assert isinstance(
            image.getInstrument(), ode.gateway.InstrumentWrapper), \
            "Should return InstrumentWrapper"
        assert isinstance(pixels, ode.gateway.ServerObjectWrapper), \
            "Should return a ServerObjectWrapper"

        # 'get' methods should wrap model objects in ServerObjectWrapper -
        # allowing lazy loading
        assert isinstance(
            image.getFormat(), ode.gateway.ServerObjectWrapper), \
            "Should return a ServerObjectWrapper"
        format = image.getFormat()
        assert format.value == "Deltavision", \
            "ServerObjectWrapper should lazy-load the value"
        assert not hasattr(format.value, 'val'), "Shouldn't return rtype"
        assert not hasattr(format.getValue(), 'val'), "Shouldn't return rtype"
        # direct access of the same model object shouldn't wrap
        assert isinstance(image.format, ode.model.FormatI), \
            "Shouldn't wrap directly-accessed objects"
        assert hasattr(image.format.id, 'val'), "Model object access"
        # 'get' methods where there isn't a similarly-named attribute also
        # shouldn't wrap
        assert isinstance(image.getPixels(0), ode.model.PixelsI), \
            "Shouldn't wrap: No 'pixels' attribute"
        # Don't accidentally wrap data structures
        assert isinstance(image.copyPixels(), list), "Shouldn't wrap lists"

    def testProjectWrapper(self, gatewaywrapper):
        gatewaywrapper.loginAsAuthor()
        p = gatewaywrapper.getTestProject()
        pid = p.getId()
        gid = p.getDetails().getGroup().getId()
        m = p.simpleMarshal()
        assert m['name'] == p.getName()
        assert m['description'] == p.getDescription()
        assert m['id'] == p.getId()
        assert m['type'] == p.ODE_CLASS
        assert 'parents' not in m
        m = p.simpleMarshal(parents=True)
        assert m['name'] == p.getName()
        assert m['description'] == p.getDescription()
        assert m['id'] == p.getId()
        assert m['type'] == p.ODE_CLASS
        assert m['parents'] == []
        m = p.simpleMarshal(xtra={'childCount': None})
        assert m['name'] == p.getName()
        assert m['description'] == p.getDescription()
        assert m['id'] == p.getId()
        assert m['type'] == p.ODE_CLASS
        assert 'parents' not in m
        assert m['child_count'] == p.countChildren_cached()
        # Verify canOwnerWrite
        gatewaywrapper.loginAsAdmin()
        gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
        chmod = ode.cmd.Chmod2(targetObjects={'ExperimenterGroup': [gid]})
        p = gatewaywrapper.gateway.getObject('project', pid)
        perms = str(p.getDetails().getGroup().getDetails().permissions)

        # try making the group writable by owner
        chmod.permissions = 'rw' + perms[2:]
        gatewaywrapper.gateway.c.submit(chmod)
        p = gatewaywrapper.gateway.getObject('project', pid)
        assert p.canOwnerWrite() is True

        # try making the group not writable by owner
        chmod.permissions = 'r-' + perms[2:]
        gatewaywrapper.gateway.c.submit(chmod)
        p = gatewaywrapper.gateway.getObject('project', pid)
        assert p.canOwnerWrite() is False

        # put the group back as it was
        chmod.permissions = perms
        gatewaywrapper.gateway.c.submit(chmod)

    def testDatasetWrapper(self, gatewaywrapper, author_testimg):
        # author_testimg above is only included to populate the dataset
        gatewaywrapper.loginAsAuthor()
        d = gatewaywrapper.getTestDataset()
        # first call to count_cached should calculate and store
        assert d.countChildren_cached() == 1
        pm = d.getParent().simpleMarshal()
        m = d.simpleMarshal()
        assert m['name'] == d.getName()
        assert m['description'] == d.getDescription()
        assert m['id'] == d.getId()
        assert m['type'] == d.ODE_CLASS
        assert 'parents' not in m
        m = d.simpleMarshal(parents=True)
        assert m['name'] == d.getName()
        assert m['description'] == d.getDescription()
        assert m['id'] == d.getId()
        assert m['type'] == d.ODE_CLASS
        assert m['parents'] == [pm]
        m = d.simpleMarshal(xtra={'childCount': None})
        assert m['name'] == d.getName()
        assert m['description'] == d.getDescription()
        assert m['id'] == d.getId()
        assert m['type'] == d.ODE_CLASS
        assert 'parents' not in m
        assert m['child_count'] == d.countChildren_cached()
        # Do an extra check on listParents
        pm_multi = d.getParent()
        assert d.listParents()[0] == pm_multi

    def testExperimenterWrapper(self, gatewaywrapper):
        gatewaywrapper.loginAsAdmin()
        e = gatewaywrapper.gateway.getObject(
            "Experimenter", attributes={'odeName': gatewaywrapper.USER.name})
        assert e.getName() == gatewaywrapper.USER.name

    def testDetailsWrapper(self, gatewaywrapper, author_testimg):
        img = author_testimg
        d = img.getDetails()
        assert d.getOwner().odeName == gatewaywrapper.AUTHOR.name
        assert d.getGroup().name == \
            img.getProject().getDetails().getGroup().name

    def createTestFile(self, gatewaywrapper):
        gatewaywrapper.loginAsAuthor()
        content = 'abcdefghijklmnopqrstuvwxyz'
        f = gatewaywrapper.createTestFile(
            'testOriginalFileWrapper', 'test_wrapper', content)
        return f

    def testOriginalFileWrapperGetFileInChunks(self, gatewaywrapper):
        f = self.createTestFile(gatewaywrapper)
        assert ''.join(f.getFileInChunks()) == 'abcdefghijklmnopqrstuvwxyz'
        assert list(f.getFileInChunks(buf=10)) == [
            'abcdefghij',
            'klmnopqrst',
            'uvwxyz',
        ]

    def testOriginalFileWrapperAsFileObj(self, gatewaywrapper):
        f = self.createTestFile(gatewaywrapper)
        fobj = f.asFileObj(buf=7)
        assert fobj.pos == 0
        assert fobj.read(5) == 'abcde'
        assert fobj.pos == 5
        fobj.seek(10)
        assert fobj.pos == 10
        assert fobj.read(5) == 'klmno'
        assert fobj.pos == 15
        fobj.seek(5, os.SEEK_CUR)
        assert fobj.pos == 20
        assert fobj.read(4) == 'uvwx'
        fobj.seek(-5, os.SEEK_END)
        assert fobj.pos == 21
        assert fobj.read(2) == 'vw'
        assert fobj.read() == 'xyz'
        assert fobj.read() == ''
        fobj.close()

    def testOriginalFileWrapperAsFileObjContextManager(self, gatewaywrapper):
        f = self.createTestFile(gatewaywrapper)
        with f.asFileObj() as fobj:
            assert fobj.read() == 'abcdefghijklmnopqrstuvwxyz'
        # Verify close was automatically called
        with pytest.raises(Ice.ObjectNotExistException):
            fobj.read()

    def testOriginalFileWrapperAsFileObjMultiple(self, gatewaywrapper):
        # Test that multiple file objects are allowed
        # https://trello.com/c/lC8hFFix/522
        f1 = self.createTestFile(gatewaywrapper)
        f2 = self.createTestFile(gatewaywrapper)
        with f1.asFileObj() as fobj1:
            with f2.asFileObj() as fobj2:
                assert fobj1.rfs.getFileId().val != fobj2.rfs.getFileId().val

    def testSetters(self, gatewaywrapper):
        """
        Verify the setters that coerce values into server friendly rtypes.
        """
        gatewaywrapper.loginAsAuthor()
        p = gatewaywrapper.getTestProject()
        n = p.getName()
        p.setName('some name')
        assert p.getName() == 'some name'
        # we have not saved, but just in case revert it
        p.setName(n)
        assert p.getName() == n
        # Trying for something that does not exist must raise
        pytest.raises(AttributeError, getattr, self,
                      'something_wild_that_does_not_exist')

    def testOther(self):
        p = ode.gateway.ProjectWrapper()
        assert repr(p) is not None
