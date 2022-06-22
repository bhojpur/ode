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
   Integration test focused on the ode.api.IShare interface
   a running server.
"""

import time
import pytest
from ode.testlib import ITest
import ode

from ode.rtypes import rint, rlong, rstring, rtime

class TestITimeline(ITest):

    DEFAULT_PERMS = 'rwr---'

    def testGeneral(self):
        uuid = self.ctx.sessionUuid
        timeline = self.sf.getTimelineService()

        im_ids = dict()
        for i in range(0, 10):
            # create image
            acquired = long(time.time() * 1000)
            img = self.make_image(name='test-img-%s' % uuid, date=acquired)
            im_ids[i] = [img.id.val, acquired]

        # Here we assume that this test is not run within the last 1 second
        start = acquired - 86400
        end = acquired + 1

        p = ode.sys.Parameters()
        p.map = {}
        f = ode.sys.Filter()
        f.ownerId = rlong(self.ctx.userId)
        f.groupId = rlong(self.ctx.groupId)
        p.theFilter = f

        M = timeline.countByPeriod
        A = rtime(long(start))
        B = rtime(long(end))
        counter = M(['Image'], A, B, p)
        assert counter['Image'] == 10
        # And with #9609
        counter = M(['Image'], A, B, p, {"ode.group": "-1"})
        assert counter['Image'] == 10

        p2 = ode.sys.Parameters()
        p2.map = {}
        f2 = ode.sys.Filter()
        f2.ownerId = rlong(self.ctx.userId)
        f2.groupId = rlong(self.ctx.groupId)
        f2.limit = rint(5)
        p2.theFilter = f2

        # p.map["start"] = rtime(long(start))
        # p.map["end"] = rtime(long(end))

        M = timeline.getMostRecentObjects
        res = M(['Image'], p2, False)["Image"]
        assert 5 == len(res)
        # And with #9609
        res = M(['Image'], p2, False, {"ode.group": "-1"})["Image"]
        assert 5 == len(res)

        # 1st element should be the 9th from the im_ids
        assert im_ids[9][0] == res[0].id.val
        # 2nd element should be the 8th from the im_ids
        assert im_ids[8][0] == res[1].id.val
        # 3rd element should be the 7th from the im_ids
        assert im_ids[7][0] == res[2].id.val
        # 4th element should be the 6th from the im_ids
        assert im_ids[6][0] == res[3].id.val
        # 5th element should be the 5th from the im_ids
        assert im_ids[5][0] == res[4].id.val

    def testCollaborativeTimeline(self):
        """
        Create some images as one user - test if another user
        can see these events in timeline.
        """

        client2, user2 = self.new_client_and_user(group=self.group)

        # log in as first user & create images
        timeline2 = client2.sf.getTimelineService()

        im_ids = dict()
        for i in range(0, 10):
            # create image
            acquired = long(time.time() * 1000)
            img = self.make_image(name='test-img-%s' % client2.sf,
                                  date=acquired, client=client2)
            im_ids[i] = [img.id.val, acquired]

        # Here we assume that this test is not run within the last 1 second
        start = acquired - 86400
        end = acquired + 1

        ownerId = rlong(user2.id.val)
        groupId = rlong(self.group.id.val)

        def assert_timeline(timeline, start, end, ownerId=None, groupId=None):
            p = ode.sys.Parameters()
            p.map = {}
            f = ode.sys.Filter()
            if ownerId is not None:
                f.ownerId = ownerId
            if groupId is not None:
                f.groupId = groupId
            p.theFilter = f

            counter = timeline.countByPeriod(
                ['Image'], rtime(long(start)), rtime(long(end)), p)
            assert 10 == counter['Image']
            data = timeline.getByPeriod(
                ['Image'], rtime(long(start)), rtime(long(end)), p, False)
            assert 10 == len(data['Image'])

        assert_timeline(timeline2, start, end, ownerId, groupId)

        # now log in as another user (default group is same as user-created
        # images above)
        assert_timeline(
            self.sf.getTimelineService(), start, end, ownerId, groupId)

    def test1173(self):
        uuid = self.root.sf.getAdminService().getEventContext().sessionUuid
        timeline = self.root.sf.getTimelineService()

        # create image
        ds = self.make_dataset(name='test1173-ds-%s' % uuid, client=self.root)
        ds.unload()

        # Here we assume that this test is not run within the last 1 second
        start = long(time.time() * 1000 - 86400)
        end = long(time.time() * 1000 + 86400)

        p = ode.sys.Parameters()
        p.map = {}
        f = ode.sys.Filter()
        f.ownerId = rlong(self.new_user().id.val)
        p.theFilter = f

        M = timeline.getEventLogsByPeriod
        A = rtime(long(start))
        B = rtime(long(end))

        rv = M(A, B, p)
        assert rv > 0

        # And now for #9609
        rv = M(A, B, p, {"ode.group": "-1"})
        assert rv > 0

    def test1175(self):
        uuid = self.root.sf.getAdminService().getEventContext().sessionUuid
        update = self.root.sf.getUpdateService()
        timeline = self.root.sf.getTimelineService()

        # create dataset
        ds = self.make_dataset(name='test1154-ds-%s' % (uuid),
                               client=self.root)
        ds.unload()

        # create tag
        ann = ode.model.TagAnnotationI()
        ann.textValue = rstring('tag-%s' % (uuid))
        ann.setDescription(rstring('tag-%s' % (uuid)))
        t_ann = ode.model.DatasetAnnotationLinkI()
        t_ann.setParent(ds)
        t_ann.setChild(ann)
        update.saveObject(t_ann)

        p = ode.sys.Parameters()
        p.map = {}
        f = ode.sys.Filter()
        f.ownerId = rlong(0)
        f.limit = rint(10)
        p.theFilter = f

        M = timeline.getMostRecentAnnotationLinks
        res = M(None, ['TagAnnotation'], None, p)
        assert len(res) > 0

        # And now for #9609
        res = M(None, ['TagAnnotation'], None, p, {"ode.group": "-1"})
        assert len(res) > 0

    # This test relates to a ticket that has not yet been resolved
    # https://trac.bhojpur.net/ome/ticket/1225
    # If the ticket is still valid then this test should presumably pass
    # after the ticket is closed but not before then. If the issue is not
    # to be addressed then this test should be removed.
    @pytest.mark.broken(ticket="1225")
    def test1225(self):
        uuid = self.root.sf.getAdminService().getEventContext().sessionUuid
        update = self.root.sf.getUpdateService()
        timeline = self.root.sf.getTimelineService()
        query = self.root.sf.getQueryService()

        # create dataset
        to_save = list()
        for i in range(0, 10):
            to_save.append(self.new_dataset(name="ds-%i-%s" % (i, uuid)))

        dss = update.saveAndReturnArray(to_save)

        # create tag
        for i in range(0, 10):
            ds1 = query.get("Dataset", dss[i].id.val)
            ann = ode.model.TagAnnotationI()
            ann.textValue = rstring('tag-%i-%s' % (i, uuid))
            ann.setDescription(rstring('desc-%i-%s' % (i, uuid)))
            t_ann = ode.model.DatasetAnnotationLinkI()
            t_ann.setParent(ds1)
            t_ann.setChild(ann)
            update.saveObject(t_ann)

        p = ode.sys.Parameters()
        p.map = {}
        f = ode.sys.Filter()
        f.ownerId = rlong(0)
        f.limit = rint(10)
        p.theFilter = f

        M = timeline.getMostRecentAnnotationLinks
        tagids = set([e.child.id.val for e in
                      M(None, ['TagAnnotation'], None, p)])
        assert len(tagids) == 10

        # And under #9609
        tagids = set([e.child.id.val for e in
                     M(None, ['TagAnnotation'], None, p,
                       {"ode.group": "-1"})])
        assert len(tagids) == 10

        ann = ode.model.TagAnnotationI()
        ann.textValue = rstring('tag-%s' % (uuid))
        ann.setDescription(rstring('desc-%s' % (uuid)))
        ann = update.saveAndReturnObject(ann)
        for i in range(0, 10):
            ds1 = query.get("Dataset", dss[i].id.val)
            ann1 = query.get("TagAnnotation", ann.id.val)
            t_ann = ode.model.DatasetAnnotationLinkI()
            t_ann.setParent(ds1)
            t_ann.setChild(ann1)
            update.saveObject(t_ann)

        tids = set([e.child.id.val for e in
                    M(None, ['TagAnnotation'], None, p)])
        assert len(tids) == 10

        # And again #9609
        tids = set([e.child.id.val for e in
                    M(None, ['TagAnnotation'], None, p,
                      {"ode.group": "-1"})])
        assert len(tids) == 10

    def test3234(self):

        user_object = ode.model.ExperimenterI(self.ctx.userId, False)

        share = self.root.sf.getShareService()
        share.createShare(
            "description", None, None, [user_object], None, True)

        timeline = self.sf.getTimelineService()
        timeline.getMostRecentShareCommentLinks(None)
        timeline.getMostRecentShareCommentLinks(None, {"ode.group": "-1"})
