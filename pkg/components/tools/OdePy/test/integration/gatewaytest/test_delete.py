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
   gateway tests - Delete methods

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import ode
from ode.rtypes import wrap, unwrap
import time

class TestDelete (object):

    def _prepareObjectsToDelete(self, gateway, ns):
        """ creates a couple of annotations used in testDeleteObjects* """
        q = gateway.getQueryService()
        ids = [x.id.val for x in q.findAllByQuery(
               "from CommentAnnotation where ns='%s'" % ns, None)]
        assert len(ids) == 0
        u = gateway.getUpdateService()
        ann = ode.gateway.CommentAnnotationWrapper()
        ann.setNs(ns)
        ann.setValue('foo')
        u.saveObject(ann._obj)
        ann = ode.gateway.CommentAnnotationWrapper()
        ann.setNs(ns)
        ann.setValue('')
        u.saveObject(ann._obj)
        ids = [x.id.val for x in q.findAllByQuery(
               "from CommentAnnotation where ns='%s'" % ns, None)]
        assert len(ids) == 2
        return ids

    def testDeleteObjectsUnwrapped(self, gatewaywrapper):
        """ tests async delete objects """
        ns = 'testDeleteObjects-'+str(time.time())
        gatewaywrapper.loginAsAuthor()
        ids = self._prepareObjectsToDelete(gatewaywrapper.gateway, ns)
        # This is the same as ServerGateway.deleteObjects(), just unrolled here
        # for verbosity and to make sure the more generalistic code there
        # isn't to blame for any issue
        command = ode.cmd.Delete2(targetObjects={'Annotation': ids})
        doall = ode.cmd.DoAll()
        doall.requests = [command]
        handle = gatewaywrapper.gateway.c.sf.submit(
            doall, gatewaywrapper.gateway.SERVICE_OPTS)
        gatewaywrapper.gateway._waitOnCmd(handle)
        handle.close()
        q = gatewaywrapper.gateway.getQueryService()
        ids = [x.id.val for x in q.findAllByQuery(
               "from CommentAnnotation where ns='%s'" % ns, None)]
        assert len(ids) == 0

    def testDeleteObjects(self, gatewaywrapper):
        """ tests the call to deleteObjects """
        ns = 'testDeleteObjects-'+str(time.time())
        gatewaywrapper.loginAsAuthor()
        ids = self._prepareObjectsToDelete(gatewaywrapper.gateway, ns)
        handle = gatewaywrapper.gateway.deleteObjects('Annotation', ids)
        gatewaywrapper.gateway._waitOnCmd(handle)
        handle.close()
        q = gatewaywrapper.gateway.getQueryService()
        ids = [x.id.val for x in q.findAllByQuery(
               "from CommentAnnotation where ns='%s'" % ns, None)]
        assert len(ids) == 0

    def testDeleteObjectsWait(self, gatewaywrapper):
        """ tests the call to deleteObjects using wait """
        ns = 'testDeleteObjects-'+str(time.time())
        gatewaywrapper.loginAsAuthor()
        ids = self._prepareObjectsToDelete(gatewaywrapper.gateway, ns)
        gatewaywrapper.gateway.deleteObjects('Annotation', ids, wait=True)
        q = gatewaywrapper.gateway.getQueryService()
        ids = [x.id.val for x in q.findAllByQuery(
               "from CommentAnnotation where ns='%s'" % ns, None)]
        assert len(ids) == 0

    def testDeleteObjectsDryRun(self, gatewaywrapper):
        """ tests the call to deleteObjects with dryRun using wait"""
        ns = 'testDeleteObjects-'+str(time.time())
        gatewaywrapper.loginAsAuthor()
        ids = self._prepareObjectsToDelete(gatewaywrapper.gateway, ns)
        gatewaywrapper.gateway.deleteObjects('Annotation', ids, dryRun=True,
                                             wait=True)
        q = gatewaywrapper.gateway.getQueryService()
        foundIds = [x.id.val for x in q.findAllByQuery(
            "from CommentAnnotation where ns='%s'" % ns, None)]
        assert set(ids) == set(foundIds)

    def testDeleteAnnotatedFileAnnotation(self, gatewaywrapper):
        """ See trac:11939 """
        ns = 'testDeleteObjects-' + str(time.time())
        gatewaywrapper.loginAsAuthor()
        us = gatewaywrapper.gateway.getUpdateService()
        qs = gatewaywrapper.gateway.getQueryService()

        tag = ode.model.TagAnnotationI()
        tag.setNs(wrap(ns))
        tag.setTextValue(wrap('tag'))
        tag = us.saveAndReturnObject(tag)

        project = ode.model.ProjectI()
        project.setName(wrap('project'))
        project = us.saveAndReturnObject(project)
        pid = unwrap(project.getId())

        ofile = ode.model.OriginalFileI()
        ofile.setName(wrap('filename'))
        ofile.setPath(wrap('filepath'))
        ofile = us.saveAndReturnObject(ofile)
        oid = unwrap(ofile.getId())

        tagAnnLink = ode.model.OriginalFileAnnotationLinkI()
        tagAnnLink.link(ode.model.OriginalFileI(oid, False), tag)
        tagAnnLink = us.saveAndReturnObject(tagAnnLink)

        fileAnn = ode.model.FileAnnotationI()
        fileAnn.setFile(ode.model.OriginalFileI(oid, False))
        fileAnn.setNs(wrap(ns))
        fileAnn.setDescription(wrap('file attachment'))

        fileAnnLink = ode.model.ProjectAnnotationLinkI()
        fileAnnLink.link(ode.model.ProjectI(pid, False), fileAnn)
        fileAnnLink = us.saveAndReturnObject(fileAnnLink)

        faid = unwrap(fileAnnLink.getChild().getId())

        # Delete the file
        handle = gatewaywrapper.gateway.deleteObjects(
            'OriginalFile', [oid], True, True)
        gatewaywrapper.gateway._waitOnCmd(handle)
        handle.close()

        assert qs.find('OriginalFile', oid) is None
        assert qs.find('FileAnnotation', faid) is None
