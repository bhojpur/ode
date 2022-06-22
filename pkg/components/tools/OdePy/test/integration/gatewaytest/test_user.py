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
   gateway tests - Users

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import ode
import pytest

from ode.gateway.scripts import dbhelpers

class TestUser (object):
    def testUsers(self, gatewaywrapper):
        gatewaywrapper.loginAsUser()
        # Try reconnecting without disconnect
        gatewaywrapper._has_connected = False
        gatewaywrapper.doConnect()
        gatewaywrapper.loginAsAuthor()
        gatewaywrapper.loginAsAdmin()

    def testSaveAs(self, gatewaywrapper):
        for u in (gatewaywrapper.AUTHOR, gatewaywrapper.ADMIN):
            # Test image should be owned by author
            gatewaywrapper.loginAsAuthor()
            image = gatewaywrapper.getTestImage(autocreate=True)
            ownername = image.getOwnerOdeName()
            # Now login as author or admin
            gatewaywrapper.doLogin(u)
            gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
            image = gatewaywrapper.getTestImage()
            assert ownername == gatewaywrapper.AUTHOR.name
            # Create some object
            param = ode.sys.Parameters()
            param.map = {
                'ns': ode.rtypes.rstring('webode.UserTest.testSaveAs')}
            queryService = gatewaywrapper.gateway.getQueryService()
            anns = queryService.findAllByQuery(
                'from CommentAnnotation as a where a.ns=:ns', param)
            assert len(anns) == 0
            gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup()
            ann = ode.gateway.CommentAnnotationWrapper(
                conn=gatewaywrapper.gateway)
            ann.setNs(param.map['ns'].val)
            ann.setValue('foo')
            ann.saveAs(image.getDetails())

            # Annotations are owned by author
            gatewaywrapper.loginAsAuthor()
            try:
                queryService = gatewaywrapper.gateway.getQueryService()
                anns = queryService.findAllByQuery(
                    'from CommentAnnotation as a where a.ns=:ns', param)
                assert len(anns) == 1
                assert ode.gateway.CommentAnnotationWrapper(
                    gatewaywrapper.gateway, anns[0]).getOwnerOdeName(), \
                    gatewaywrapper.AUTHOR.name
            finally:
                gatewaywrapper.gateway.getUpdateService().deleteObject(
                    ann._obj)
                queryService = gatewaywrapper.gateway.getQueryService()
                anns = queryService.findAllByQuery(
                    'from CommentAnnotation as a where a.ns=:ns', param)
                assert len(anns) == 0

    def testCrossGroupSave(self, gatewaywrapper):
        gatewaywrapper.loginAsUser()
        uid = gatewaywrapper.gateway.getUserId()
        gatewaywrapper.loginAsAdmin()
        gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
        d = gatewaywrapper.getTestDataset()
        did = d.getId()
        g = d.getDetails().getGroup()
        gid = g.getId()
        chmod = ode.cmd.Chmod2(targetObjects={'ExperimenterGroup': [gid]})
        admin = gatewaywrapper.gateway.getAdminService()
        admin.addGroups(ode.model.ExperimenterI(uid, False), [g._obj])
        gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
        # make sure the group is groupwrite enabled
        perms = str(d.getDetails().getGroup().getDetails().permissions)
        chmod.permissions = 'rwrw--'
        gatewaywrapper.gateway.c.submit(chmod)
        d = gatewaywrapper.getTestDataset()
        g = d.getDetails().getGroup()
        assert g.getDetails().permissions.isGroupWrite()

        gatewaywrapper.loginAsUser()
        # User is now a member of the group to which testDataset belongs,
        # which has groupWrite==True
        # But the default group for User is diferent
        try:
            gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
            d = gatewaywrapper.getTestDataset()
            did = d.getId()
            n = d.getName()
            d.setName(n+'_1')
            d.save()
            d = gatewaywrapper.gateway.getObject('dataset', did)
            assert d.getName() == n+'_1'
            d.setName(n)
            d.save()
            d = gatewaywrapper.gateway.getObject('dataset', did)
            assert d.getName() == n
        finally:
            # Revert group permissions
            gatewaywrapper.loginAsAdmin()
            chmod.permissions = perms
            gatewaywrapper.gateway.c.submit(chmod)

    @pytest.mark.broken(ticket="11545")
    def testCrossGroupRead(self, gatewaywrapper):
        gatewaywrapper.loginAsAuthor()
        p = gatewaywrapper.getTestProject()
        assert str(p.getDetails().permissions)[4] == '-'
        d = p.getDetails()
        g = d.getGroup()
        gatewaywrapper.loginAsUser()
        gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
        assert not g.getId() in \
            gatewaywrapper.gateway.getEventContext().memberOfGroups
        assert gatewaywrapper.gateway.getObject('project', p.getId()) is None

    def testGroupOverObjPermissions(self, gatewaywrapper):
        """ Object accesss must be dependent only of group permissions """
        # Author
        gatewaywrapper.loginAsAuthor()
        # create group with rw----
        # create project and annotation in that group
        p = dbhelpers.ProjectEntry(
            'testAnnotationPermissions', None,
            create_group='testAnnotationPermissions', group_perms='rw----')
        try:
            p = p.create(gatewaywrapper.gateway)
        except dbhelpers.BadGroupPermissionsException:
            gatewaywrapper.loginAsAdmin()
            admin = gatewaywrapper.gateway.getAdminService()
            group = admin.lookupGroup('testAnnotationPermissions')
            group_as_target = {'ExperimenterGroup': [group.id.val]}
            chmod = ode.cmd.Chmod2(targetObjects=group_as_target,
                                     permissions='rw----')
            gatewaywrapper.gateway.c.submit(chmod)
            gatewaywrapper.loginAsAuthor()
            p = p.create(gatewaywrapper.gateway)
        pid = p.getId()
        g = p.getDetails().getGroup()._obj
        try:
            # Admin
            # add User to group
            gatewaywrapper.loginAsUser()
            uid = gatewaywrapper.gateway.getUserId()
            gatewaywrapper.loginAsAdmin()
            admin = gatewaywrapper.gateway.getAdminService()
            admin.addGroups(ode.model.ExperimenterI(uid, False), [g])
            # User
            # try to read project and annotation, which fails
            gatewaywrapper.loginAsUser()
            gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
            assert gatewaywrapper.gateway.getObject('project', pid) is None
            # Admin
            # Chmod project to rwrw--
            gatewaywrapper.loginAsAdmin()
            group_as_target = {'ExperimenterGroup': [g.id.val]}
            chmod = ode.cmd.Chmod2(targetObjects=group_as_target,
                                     permissions='rwrw--')
            gatewaywrapper.gateway.c.submit(chmod)
            # Author
            # check project has proper permissions
            gatewaywrapper.loginAsAuthor()
            gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
            pa = gatewaywrapper.gateway.getObject('project', pid)
            assert pa is not None
            # User
            # read project and annotation
            gatewaywrapper.loginAsUser()
            gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup('-1')
            assert gatewaywrapper.gateway.getObject(
                'project', pid) is not None
        finally:
            gatewaywrapper.loginAsAuthor()
            handle = gatewaywrapper.gateway.deleteObjects(
                'Project', [p.getId()], deleteAnns=True, deleteChildren=True)
            gatewaywrapper.waitOnCmd(gatewaywrapper.gateway.c, handle)
