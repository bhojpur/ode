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
   gateway tests - Annotation Wrapper

   pytest fixtures used as defined in conftest.py:
   - gatewaywrapper
   - author_testimg_generated
"""

import time
import datetime
import os
from tempfile import NamedTemporaryFile
from cStringIO import StringIO

import ode.gateway
from ode.rtypes import rstring
from ode.gateway import FileAnnotationWrapper


def _testAnnotation(obj, annclass, ns, value, sameOwner=False,
                    testOwner=None):
    gateway = obj._conn
    # Make sure it doesn't yet exist
    obj.removeAnnotations(ns)
    assert obj.getAnnotation(ns) is None
    # Create new, link and check
    ann = annclass(gateway)
    ann.setNs(ns)
    ann.setValue(value)
    if sameOwner:
        obj.linkAnnotation(ann, sameOwner=True)
    else:
        # checks that default sameOwner=False
        obj.linkAnnotation(ann)
    ann = obj.getAnnotation(ns)
    # Make sure the group for the annotation is the same as the original
    # object. (#120)
    assert ann.getDetails().getGroup() == obj.getDetails().getGroup()
    tval = hasattr(value, 'val') and value.val or value
    assert ann.getValue() == value, '%s != %s' % (str(ann.getValue()),
                                                  str(tval))
    assert ann.getNs() == ns,  '%s != %s' % (str(ann.getNs()), str(ns))
    if testOwner is not None:
        testOwner(obj, ann)
    # Remove and check
    obj.removeAnnotations(ns)
    assert obj.getAnnotation(ns) is None
    # Same dance, createAndLink shortcut
    if sameOwner:
        annclass.createAndLink(target=obj, ns=ns, val=value, sameOwner=True)
    else:
        # checks that default sameOwner=False
        annclass.createAndLink(target=obj, ns=ns, val=value)
    ann = obj.getAnnotation(ns)
    # Make sure the group for the annotation is the same as the original
    # object. (#120)
    assert ann.getDetails().getGroup() == obj.getDetails().getGroup()
    tval = hasattr(value, 'val') and value.val or value
    assert ann.getValue() == value, '%s != %s' % (str(ann.getValue()),
                                                  str(tval))
    assert ann.getNs() == ns,  '%s != %s' % (str(ann.getNs()), str(ns))
    if testOwner is not None:
        testOwner(obj, ann)
    # Remove and check
    obj.removeAnnotations(ns)
    assert obj.getAnnotation(ns) is None

TESTANN_NS = 'ode.gateway.test_annotation'

def testSameOwner(gatewaywrapper):
    """
        tests project.linkAnnotation(sameOwner=False)
        Tests sameOwner default is False (was True for 4.4.x but False in 5.0)
    """
    gatewaywrapper.loginAsAdmin()

    gatewaywrapper.gateway.SERVICE_OPTS.setOdeGroup("-1")
    p = gatewaywrapper.getTestProject2()
    p.getDetails().owner.id.val

    def sameOwner(obj, ann):
        assert obj.getDetails().owner.id.val == ann.getDetails().owner.id.val

    def differentOwner(obj, ann):
        assert obj.getDetails().owner.id.val != ann.getDetails().owner.id.val

    _testAnnotation(p,
                    ode.gateway.CommentAnnotationWrapper,
                    TESTANN_NS, 'some value',
                    sameOwner=True, testOwner=sameOwner)

    return _testAnnotation(
        p, ode.gateway.CommentAnnotationWrapper,
        TESTANN_NS, 'some value', sameOwner=False, testOwner=differentOwner)


def testCommentAnnotation(author_testimg_generated):
    return _testAnnotation(author_testimg_generated,
                           ode.gateway.CommentAnnotationWrapper,
                           TESTANN_NS, 'some value')


def testNonDefGroupAnnotation(gatewaywrapper):
    p = gatewaywrapper.getTestProject2()
    return _testAnnotation(p,
                           ode.gateway.CommentAnnotationWrapper,
                           TESTANN_NS, 'some value')


def testTimestampAnnotation(author_testimg_generated):
    now = time.time()
    t = datetime.datetime.fromtimestamp(int(now))
    _testAnnotation(author_testimg_generated,
                    ode.gateway.TimestampAnnotationWrapper,
                    TESTANN_NS, t)
    # Now use RTime, but this one doesn't fit in the general test case
    t = ode.rtypes.rtime(int(now))
    ode.gateway.TimestampAnnotationWrapper.createAndLink(
        target=author_testimg_generated, ns=TESTANN_NS, val=t)
    t = datetime.datetime.fromtimestamp(t.val / 1000.0)
    ann = author_testimg_generated.getAnnotation(TESTANN_NS)
    assert ann.getValue() == t, '%s != %s' % (str(ann.getValue()), str(t))
    assert ann.getNs() == TESTANN_NS,  '%s != %s' % (str(ann.getNs()),
                                                     str(TESTANN_NS))
    # Remove and check
    author_testimg_generated.removeAnnotations(TESTANN_NS)
    assert author_testimg_generated.getAnnotation(TESTANN_NS) is None
    # A simple int stating secs since the epoch, also not fitting in the
    # general test case
    t = int(now)
    ode.gateway.TimestampAnnotationWrapper.createAndLink(
        target=author_testimg_generated, ns=TESTANN_NS, val=t)
    t = datetime.datetime.fromtimestamp(t)
    ann = author_testimg_generated.getAnnotation(TESTANN_NS)
    assert ann.getValue() == t, '%s != %s' % (str(ann.getValue()), str(t))
    assert ann.getNs() == TESTANN_NS,  '%s != %s' % (str(ann.getNs()),
                                                     str(TESTANN_NS))
    # Remove and check
    author_testimg_generated.removeAnnotations(TESTANN_NS)
    assert author_testimg_generated.getAnnotation(TESTANN_NS) is None


def testBooleanAnnotation(author_testimg_generated):
    _testAnnotation(author_testimg_generated,
                    ode.gateway.BooleanAnnotationWrapper,
                    TESTANN_NS, True)


def testLongAnnotation(author_testimg_generated):
    _testAnnotation(author_testimg_generated,
                    ode.gateway.LongAnnotationWrapper,
                    TESTANN_NS, 1000)


def testMapAnnotation(author_testimg_generated):
    data = [("foo", "bar"),
            ("test key", "test value")]
    _testAnnotation(author_testimg_generated,
                    ode.gateway.MapAnnotationWrapper,
                    TESTANN_NS, data)
    # test getObjects()
    conn = author_testimg_generated._conn
    ann = ode.gateway.MapAnnotationWrapper(conn)
    ann.setValue(data)
    ann.save()
    aId = ann.getId()
    ann2 = conn.getObject("MapAnnotation", aId)
    assert ann2 is not None
    assert ann2.getValue() == data
    # delete to clean up
    handle = conn.deleteObjects('/Annotation', [aId])
    try:
        conn._waitOnCmd(handle)
    finally:
        handle.close()
    assert conn.getObject("MapAnnotation", aId) is None


def testDualLinkedAnnotation(author_testimg_generated):
    """ Tests linking the same annotation to 2 separate objects """
    dataset = author_testimg_generated.getParent()
    assert dataset is not None
    author_testimg_generated.removeAnnotations(TESTANN_NS)
    assert author_testimg_generated.getAnnotation(TESTANN_NS) is None
    dataset.removeAnnotations(TESTANN_NS)
    assert dataset.getAnnotation(TESTANN_NS) is None
    ann = ode.gateway.CommentAnnotationWrapper(dataset._conn)
    ann.setNs(TESTANN_NS)
    value = 'I suffer from multi link disorder'
    ann.setValue(value)
    author_testimg_generated.linkAnnotation(ann)
    dataset.linkAnnotation(ann)
    assert author_testimg_generated.getAnnotation(TESTANN_NS).getValue() == \
        value
    assert dataset.getAnnotation(TESTANN_NS).getValue() == value
    author_testimg_generated.removeAnnotations(TESTANN_NS)
    assert author_testimg_generated.getAnnotation(TESTANN_NS) is None
    assert dataset.getAnnotation(TESTANN_NS).getValue() == value
    dataset.removeAnnotations(TESTANN_NS)
    assert dataset.getAnnotation(TESTANN_NS) is None


def testListAnnotations(author_testimg_generated):
    """ Other small things that need to be tested """
    ns1 = TESTANN_NS
    ns2 = ns1 + '_2'
    obj = author_testimg_generated
    annclass = ode.gateway.CommentAnnotationWrapper
    value = 'foo'
    # Make sure it doesn't yet exist
    obj.removeAnnotations(ns1)
    obj.removeAnnotations(ns2)
    assert obj.getAnnotation(ns1) is None
    assert obj.getAnnotation(ns2) is None
    # createAndLink
    annclass.createAndLink(target=obj, ns=ns1, val=value)
    annclass.createAndLink(target=obj, ns=ns2, val=value)
    ann1 = obj.getAnnotation(ns1)
    ann2 = obj.getAnnotation(ns2)
    l = list(obj.listAnnotations())
    assert ann1 in l
    assert ann2 in l
    l = list(obj.listAnnotations(ns=ns1))
    assert ann1 in l
    assert ann2 not in l
    l = list(obj.listAnnotations(ns=ns2))
    assert ann1 not in l
    assert ann2 in l
    l = list(obj.listAnnotations(ns='bogusns...bogusns...'))
    assert ann1 not in l
    assert ann2 not in l
    # Remove and check
    obj.removeAnnotations(ns1)
    obj.removeAnnotations(ns2)
    assert obj.getAnnotation(ns1) is None
    assert obj.getAnnotation(ns2) is None


def testFileAnnotation(author_testimg_generated, gatewaywrapper):
    """ Creates a file annotation from a local file """
    tempFileName = "tempFile"
    f = open(tempFileName, 'w')
    fileText = "Test text for writing to file for upload"
    f.write(fileText)
    f.close()
    ns = TESTANN_NS
    image = author_testimg_generated

    # use the same file to create various file annotations with different
    # namespaces
    fileAnn = gatewaywrapper.gateway.createFileAnnfromLocalFile(
        tempFileName, mimetype='text/plain', ns=ns)
    image.linkAnnotation(fileAnn)
    compAnn = gatewaywrapper.gateway.createFileAnnfromLocalFile(
        tempFileName, mimetype='text/plain',
        ns=ode.constants.namespaces.NSCOMPANIONFILE)
    image.linkAnnotation(compAnn)
    os.remove(tempFileName)

    # get user-id of another user to use below.
    gatewaywrapper.loginAsAdmin()
    adminId = gatewaywrapper.gateway.getUser().getId()
    gatewaywrapper.loginAsAuthor()

    # test listing of File Annotations. Should exclude companion files by
    # default and all files should be loaded
    gateway = gatewaywrapper.gateway
    eid = gateway.getUser().getId()
    fas = list(gateway.listFileAnnotations(eid=eid, toInclude=[ns]))
    faIds = [fa.id for fa in fas]
    assert fileAnn.getId() in faIds
    assert compAnn.getId() not in faIds
    for fa in fas:
        assert fa.getNs() == ns, \
            "All files should be filtered by this namespace"
        assert fa._obj.file.loaded, \
            "All file annotations should have files loaded"

    # filtering by namespace
    fas = list(gateway.listFileAnnotations(
        toInclude=["nothing.with.this.namespace"], eid=eid))
    assert len(fas) == 0, \
        "No file annotations should exist with bogus namespace"

    # filtering files by a different user should not return the annotations
    # above.
    fas = list(gateway.listFileAnnotations(eid=adminId))
    faIds = [fa.id for fa in fas]
    assert fileAnn.getId() not in faIds
    assert compAnn.getId() not in faIds

    # needs a fresh connection, original was closed already
    image._conn = gatewaywrapper.gateway
    ann = image.getAnnotation(ns)
    annId = ann.getId()
    assert ann.ODE_TYPE == ode.model.FileAnnotationI
    for t in ann.getFileInChunks():
        assert str(t) == fileText   # we get whole text in one chunk

    # delete what we created
    assert gateway.getObject("Annotation", annId) is not None
    handle = gateway.deleteObjects("Annotation", [annId])
    gateway._waitOnCmd(handle)
    assert gateway.getObject("Annotation", annId) is None


def testFileAnnotationNoName(author_testimg_generated, gatewaywrapper):
    """Test conn.createOriginalFileFromFileObj() and getFileName()"""
    file_text = "test"
    file_size = len(file_text)
    f = StringIO()
    f.write(file_text)
    file_name = "testFileAnnotationNoName"
    conn = gatewaywrapper.gateway
    update_service = conn.getUpdateService()

    # Create Original File and File Annotation
    orig_file = conn.createOriginalFileFromFileObj(
        f, '', file_name, file_size, mimetype="application/txt")
    fa = ode.model.FileAnnotationI()
    fa.setFile(orig_file._obj)
    fa = update_service.saveAndReturnObject(fa, conn.SERVICE_OPTS)
    ann_id = fa.id.val
    file_ann = FileAnnotationWrapper(conn, fa)

    assert file_ann.getFileName() == file_name

    # Set Name to None - getFileName() should return file ID as string
    orig_file._obj.name = rstring("")
    orig_file._obj = update_service.saveAndReturnObject(orig_file._obj,
                                                        conn.SERVICE_OPTS)
    # reload file_ann to update
    file_ann = conn.getObject("FileAnnotation", ann_id)
    assert file_ann.getFileName() == ""


def testFileAnnotationSpeed(author_testimg_generated, gatewaywrapper):
    """ Tests speed of loading file annotations. See PR: 4176 """
    try:
        f = NamedTemporaryFile()
        f.write("testFileAnnotationSpeed text")
        ns = TESTANN_NS
        image = author_testimg_generated

        # use the same file to create many file annotations
        for i in range(20):
            fileAnn = gatewaywrapper.gateway.createFileAnnfromLocalFile(
                f.name, mimetype='text/plain', ns=ns)
            image.linkAnnotation(fileAnn)
    finally:
        f.close()

    now = time.time()
    for ann in image.listAnnotations():
        if ann._obj.__class__ == ode.model.FileAnnotationI:
            # mimmic behaviour of templates which call multiple times
            print(ann.getId())
            print(ann.getFileName())
            print(ann.getFileName())
            print(ann.getFileSize())
            print(ann.getFileSize())
    print(time.time() - now)


def testFileAnnNonDefaultGroup(author_testimg_generated, gatewaywrapper):
    """ Test conn.createFileAnnfromLocalFile() respects SERVICE_OPTS """
    gatewaywrapper.loginAsAuthor()
    userId = gatewaywrapper.gateway.getUser().getId()
    ctx = gatewaywrapper.gateway.getAdminService().getEventContext()
    uuid = ctx.sessionUuid

    # Admin creates a new group with user
    gatewaywrapper.loginAsAdmin()
    gid = gatewaywrapper.gateway.createGroup(
        "testFileAnnNonDefaultGroup-%s" % uuid, member_Ids=[userId])

    # login as Author again (into 'default' group)
    gatewaywrapper.loginAsAuthor()
    conn = gatewaywrapper.gateway
    # Try to create fileAnn in another group
    conn.SERVICE_OPTS.setOdeGroup(gid)
    tempFileName = "tempFile"
    f = open(tempFileName, 'w')
    fileText = "Test text for writing to file for upload"
    f.write(fileText)
    f.close()
    ns = TESTANN_NS
    fileAnn = conn.createFileAnnfromLocalFile(
        tempFileName, mimetype='text/plain', ns=ns)
    os.remove(tempFileName)
    assert fileAnn.getDetails().group.id.val == gid


def testUnlinkAnnotation(author_testimg_generated):
    """ Tests the use of unlinkAnnotations. See #7301 """

    # Setup test dataset
    dataset = author_testimg_generated.getParent()
    assert dataset is not None
    gateway = dataset._conn

    # Make really sure there are no annotations
    dataset.removeAnnotations(TESTANN_NS)
    assert dataset.getAnnotation(TESTANN_NS) is None

    # Add an annotation
    ann = ode.gateway.CommentAnnotationWrapper(gateway)
    ann.setNs(TESTANN_NS)
    dataset.linkAnnotation(ann)
    assert dataset.getAnnotation(TESTANN_NS).getNs() == TESTANN_NS

    # Unlink annotations
    dataset.unlinkAnnotations(TESTANN_NS)
    assert dataset.getAnnotation(TESTANN_NS) is None


def testAnnotationCount(author_testimg_generated):
    """ Test get annotations counts """

    img = author_testimg_generated
    gateway = img._conn
    ann = ode.gateway.CommentAnnotationWrapper(gateway)
    img.linkAnnotation(ann)
    # LongAnnotation without NS, == OtherAnnotation
    ann = ode.gateway.LongAnnotationWrapper(gateway)
    img.linkAnnotation(ann)
    # LongAnnotation with rating NS
    ann = ode.gateway.LongAnnotationWrapper(gateway)
    ann.setNs(ode.constants.metadata.NSINSIGHTRATING)
    img.linkAnnotation(ann)
    ann = ode.gateway.DoubleAnnotationWrapper(gateway)
    img.linkAnnotation(ann)
    ann = ode.gateway.BooleanAnnotationWrapper(gateway)
    img.linkAnnotation(ann)
    ann = ode.gateway.MapAnnotationWrapper(gateway)
    img.linkAnnotation(ann)
    ann = ode.gateway.TagAnnotationWrapper(gateway)
    img.linkAnnotation(ann)
    ann = ode.gateway.FileAnnotationWrapper(gateway)
    img.linkAnnotation(ann)

    counts = img.getAnnotationCounts()
    assert counts['CommentAnnotation'] == 1
    assert counts['TagAnnotation'] == 1
    assert counts['LongAnnotation'] == 1
    assert counts['MapAnnotation'] == 1
    assert counts['FileAnnotation'] == 1
    assert counts['OtherAnnotation'] == 3
