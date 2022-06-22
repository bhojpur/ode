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
   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
"""

import ode
from ode.rtypes import rstring
from ode.cmd import State, ERR, OK
from ode.callbacks import CmdCallbackI

PRIVATE = 'rw----'
READONLY = 'rwr---'
COLLAB = 'rwrw--'


def doChange(gateway, obj_type, obj_ids, group_id, container_id=None,
             test_should_pass=True, return_complete=True):
    """
    Performs the change-group action, waits on completion and checks that the
    result is not an error.
    """
    prx = gateway.chgrpObjects(obj_type, obj_ids, group_id, container_id)

    if not return_complete:
        return prx

    cb = CmdCallbackI(gateway.c, prx)
    try:
        for i in range(10):
            cb.loop(20, 500)
            if prx.getResponse() is not None:
                break

        assert prx.getResponse() is not None

        prx.getStatus()
        rsp = prx.getResponse()

        if test_should_pass:
            assert not isinstance(rsp, ERR), \
                "Found ERR when test_should_pass==true: %s (%s) params=%s" \
                % (rsp.category, rsp.name, rsp.parameters)
            assert State.FAILURE not in prx.getStatus().flags
        else:
            assert not isinstance(rsp, OK), \
                "Found OK when test_should_pass==false: %s" % rsp
            assert State.FAILURE in prx.getStatus().flags
        return rsp
    finally:
        cb.close(True)


def testImageChgrp(gatewaywrapper):
    """
    Create a new group with the User as member. Test move the Image to new
    group.
    """
    gatewaywrapper.loginAsAuthor()
    image = gatewaywrapper.createTestImage()
    ctx = gatewaywrapper.gateway.getAdminService().getEventContext()
    uuid = ctx.sessionUuid

    gatewaywrapper.loginAsAdmin()
    gid = gatewaywrapper.gateway.createGroup(
        "chgrp-test-%s" % uuid, member_Ids=[ctx.userId], perms=COLLAB)
    gatewaywrapper.loginAsAuthor()
    assert gatewaywrapper.gateway.getObject("Image", image.id) is not None

    # Do the Chgrp
    doChange(gatewaywrapper.gateway, "Image", [image.getId()], gid)

    # Image should no-longer be available in current group
    assert gatewaywrapper.gateway.getObject("Image", image.id) is None, \
        "Image should not be available in original group"

    # Switch to new group - confirm that image is there.
    gatewaywrapper.gateway.setGroupForSession(gid)
    img = gatewaywrapper.gateway.getObject("Image", image.id)
    assert img is not None, "Image should be available in new group"
    assert img.getDetails().getGroup().id == gid, \
        "Image group.id should match new group"


def testDatasetChgrp(gatewaywrapper):
    """
    Create a new group with the User as member. Test move the Dataset/Image to
    new group.
    """
    gatewaywrapper.loginAsAuthor()
    dataset = gatewaywrapper.createPDTree(dataset="testDatasetChgrp")
    image = gatewaywrapper.createTestImage(dataset=dataset)
    ctx = gatewaywrapper.gateway.getAdminService().getEventContext()
    uuid = ctx.sessionUuid

    gatewaywrapper.loginAsAdmin()
    gid = gatewaywrapper.gateway.createGroup(
        "chgrp-test-%s" % uuid, member_Ids=[ctx.userId], perms=PRIVATE)
    gatewaywrapper.loginAsAuthor()
    assert gatewaywrapper.gateway.getObject("Image", image.id) is not None

    # Do the Chgrp
    doChange(gatewaywrapper.gateway, "Dataset", [dataset.id], gid)

    # Dataset should no-longer be available in current group
    assert gatewaywrapper.gateway.getObject("Dataset", dataset.id) is None, \
        "Dataset should not be available in original group"

    # Switch to new group - confirm that Dataset, Image is there.
    gatewaywrapper.gateway.setGroupForSession(gid)
    ds = gatewaywrapper.gateway.getObject("Dataset", dataset.id)
    assert ds is not None, "Dataset should be available in new group"

    img = gatewaywrapper.gateway.getObject("Image", image.id)
    assert img is not None, "Image should be available in new group"
    assert img.getDetails().getGroup().id == gid, \
        "Image group.id should match new group"


def testPDIChgrp(gatewaywrapper):
    """
    Create a new group with the User as member. Test move the
    Project/Dataset/Image to new group.
    """
    gatewaywrapper.loginAsAuthor()
    link = gatewaywrapper.createPDTree(project="testPDIChgrp",
                                       dataset="testPDIChgrp")
    dataset = link.getChild()   # DatasetWrapper
    # ode.model.ProjectI - link.getParent() overwritten - returns None
    project = link.parent
    image = gatewaywrapper.createTestImage(dataset=dataset)
    grp = project.details.group

    ctx = gatewaywrapper.gateway.getAdminService().getEventContext()
    uuid = ctx.sessionUuid

    gatewaywrapper.loginAsAdmin()
    gid = gatewaywrapper.gateway.createGroup(
        "chgrp-test-%s" % uuid, member_Ids=[ctx.userId], perms=COLLAB)
    gatewaywrapper.loginAsAuthor()
    assert gatewaywrapper.gateway.getObject("Image", image.id) is not None

    try:
        # Do the Chgrp
        doChange(gatewaywrapper.gateway, "Project", [project.id.val], gid)

        # Image should no-longer be available in current group
        assert gatewaywrapper.gateway.getObject("Image", image.id) is None, \
            "Image should not be available in original group"

        # Switch to new group - confirm that Project, Dataset, Image is there.
        gatewaywrapper.gateway.setGroupForSession(gid)
        prj = gatewaywrapper.gateway.getObject("Project", project.id.val)
        assert prj is not None, "Project should be available in new group"

        ds = gatewaywrapper.gateway.getObject("Dataset", dataset.id)
        assert ds is not None, "Dataset should be available in new group"

        img = gatewaywrapper.gateway.getObject("Image", image.id)
        assert img is not None, "Image should be available in new group"
        assert img.getDetails().getGroup().id == gid, \
            "Image group.id should match new group"
    finally:
        # Change it all back
        gatewaywrapper.loginAsAuthor()

        # Do the Chgrp
        doChange(gatewaywrapper.gateway, "Project", [project.id.val],
                 grp.id.val)

        # Image should again be available in current group
        assert gatewaywrapper.gateway.getObject("Image", image.id) \
            is not None, "Image should be available in original group"


def testTwoDatasetsChgrpToProject(gatewaywrapper):
    """
    Create a new group with the User as member. Image has 2 Dataset Parents.
    Test move one Dataset to new group. Image does not move. Move 2nd Dataset
    - Image moves.
    """
    gatewaywrapper.loginAsAuthor()
    dataset = gatewaywrapper.createPDTree(
        dataset="testTwoDatasetsChgrpToProject")
    image = gatewaywrapper.createTestImage(dataset=dataset)
    orig_gid = dataset.details.group.id.val

    new_ds = gatewaywrapper.createPDTree(
        dataset="testTwoDatasetsChgrp-parent2")
    update = gatewaywrapper.gateway.getUpdateService()
    link = ode.model.DatasetImageLinkI()
    link.setParent(ode.model.DatasetI(new_ds.id, False))
    link.setChild(ode.model.ImageI(image.id, False))
    update.saveObject(link)

    ctx = gatewaywrapper.gateway.getAdminService().getEventContext()
    uuid = ctx.sessionUuid

    gatewaywrapper.loginAsAdmin()
    gid = gatewaywrapper.gateway.createGroup("chgrp-test-%s" % uuid,
                                             member_Ids=[ctx.userId])
    gatewaywrapper.loginAsAuthor()
    assert gatewaywrapper.gateway.getObject("Dataset", dataset.id) is not None

    # create Project in destination group
    gatewaywrapper.gateway.setGroupForSession(gid)
    p = ode.model.ProjectI()
    p.name = rstring("testTwoDatasetsChgrpToProject")
    p = gatewaywrapper.gateway.getUpdateService().saveAndReturnObject(p)
    assert p.details.group.id.val == gid, \
        "Project should be created in target group"
    gatewaywrapper.gateway.setGroupForSession(orig_gid)   # switch back

    # Do the Chgrp with one of the parents
    doChange(gatewaywrapper.gateway, "Dataset", [new_ds.id], gid)

    # Dataset should no-longer be available in current group
    assert gatewaywrapper.gateway.getObject("Dataset", new_ds.id) is None, \
        "Dataset should not be available in original group"
    assert gatewaywrapper.gateway.getObject("Dataset", dataset.getId()) \
        is not None, "Other Dataset should still be in original group"
    # But Image should
    img = gatewaywrapper.gateway.getObject("Image", image.id)
    assert img is not None, \
        "Image should still be available in original group"

    # Do the Chgrp with the OTHER parent
    # switch BEFORE doChange to allow Project link Save
    gatewaywrapper.gateway.setGroupForSession(gid)
    doChange(gatewaywrapper.gateway, "Dataset", [dataset.id], gid,
             container_id=p.id.val)

    # Confirm that Dataset AND Image is now in new group
    ctx = gatewaywrapper.gateway.getAdminService().getEventContext()
    ds = gatewaywrapper.gateway.getObject("Dataset", dataset.id)
    projects = list(ds.listParents())
    assert len(projects) == 1, \
        "Dataset should have one parent Project in new group"
    assert projects[0].getId() == p.id.val, \
        "Check Dataset parent is Project created above"
    assert ds is not None, "Dataset should now be available in new group"
    assert ds.getDetails().getGroup().id == gid, \
        "Dataset group.id should match new group"

    img = gatewaywrapper.gateway.getObject("Image", image.id)
    assert img is not None, "Image should now be available in new group"
    assert img.getDetails().getGroup().id == gid, \
        "Image group.id should match new group"


def testMultiDatasetDoAll(gatewaywrapper):
    """
    Need to enable chgrp independently of EventContext group being the
    destination group.
    Other tests that do not set ode.group require this for DoAll Save to
    work.
    """
    gatewaywrapper.loginAsAuthor()
    ctx = gatewaywrapper.gateway.getAdminService().getEventContext()
    uuid = ctx.sessionUuid
    update = gatewaywrapper.gateway.getUpdateService()

    new_ds = ode.model.DatasetI()
    new_ds.name = rstring("testMultiDatasetDoAll")
    new_ds = update.saveAndReturnObject(new_ds)

    new_ds2 = ode.model.DatasetI()
    new_ds2.name = rstring("testMultiDatasetDoAll2")
    new_ds2 = update.saveAndReturnObject(new_ds2)

    # new group
    gatewaywrapper.loginAsAdmin()
    gid = gatewaywrapper.gateway.createGroup(
        "testMultiDatasetDoAll-%s" % uuid, member_Ids=[ctx.userId])
    gatewaywrapper.loginAsAuthor()

    # create Project in new group
    gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup(gid)
    p = ode.model.ProjectI()
    p.name = rstring("testMultiChgrp")
    p = gatewaywrapper.gateway.getUpdateService().saveAndReturnObject(
        p, gatewaywrapper.gateway.SERVICE_OPTS)
    assert p.details.group.id.val == gid, \
        "Project should be created in target group"

    # Test that this works whichever group you're in
    gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup(ctx.groupId)
    dsIds = [new_ds.id.val, new_ds2.id.val]

    # Chgrp
    doChange(gatewaywrapper.gateway, "Dataset", dsIds, gid,
             container_id=p.id.val)

    # Check all objects in destination group
    # we can get objects from either group...
    gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup(-1)
    p = gatewaywrapper.gateway.getObject("Project", p.id.val)
    datasets = list(p.listChildren())
    assert len(datasets) == 2, "Project should have 2 new Datasets"
    for d in datasets:
        assert d.details.group.id.val == gid, "Dataset should be in new group"
        assert d.getId() in dsIds, "Checking Datasets by ID"
