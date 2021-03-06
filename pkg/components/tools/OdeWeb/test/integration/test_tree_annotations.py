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
Integration tests for annotations methods in the "tree" module.
"""

import pytest
from ode.testlib import ITest
from datetime import datetime

import ode
from ode.gateway import ServerGateway
from ode.model import ProjectI, CommentAnnotationI, \
    TagAnnotationI, ProjectAnnotationLinkI, LongAnnotationI
from ode.rtypes import rstring, rlong
from engine.webclient.tree import marshal_annotations

def cmp_id(x, y):
    """Identifier comparator."""
    return cmp(unwrap(x.id), unwrap(y.id))


def unwrap(x):
    """Handle case where there is no value because attribute is None"""
    if x is not None:
        return x.val
    return None


def get_connection(user, group_id=None):
    """
    Get a ServerGateway connection for the given user's client
    """
    connection = ServerGateway(client_obj=user[0])
    # Refresh the session context
    connection.getEventContext()
    if group_id is not None:
        connection.SERVICE_OPTS.setOdeGroup(group_id)
    return connection


def get_update_service(user):
    """
    Get the update_service for the given user's client
    """
    return user[0].getSession().getUpdateService()


def get_query_service(user):
    """
    Get the query_service for the given user's client
    """
    return user[0].getSession().getQueryService()


# Projects
@pytest.fixture(scope='function')
def project_userA(request, userA, groupA):
    """
    Returns new Bhojpur ODE Project
    """
    ctx = {'ode.group': str(groupA.id.val)}
    project = ProjectI()
    project.name = rstring("test_tree_annnotations")
    project = get_update_service(userA).saveAndReturnObject(project, ctx)
    return project


@pytest.fixture(scope='function')
def projects_userA(request, userA, groupA):
    """
    Returns new Bhojpur ODE Project
    """
    to_save = []
    ctx = {'ode.group': str(groupA.id.val)}
    for name in "test_ann1", "test_ann2":
        project = ProjectI()
        project.name = rstring(name)
        to_save.append(project)
    projects = get_update_service(userA).saveAndReturnArray(
        to_save, ctx)
    return projects


@pytest.fixture(scope='function')
def tags_userA_userB(request, userA, userB, groupA):
    """
    Returns new Bhojpur ODE Tags with descriptions
    """
    tags = []
    ctx = {'ode.group': str(groupA.id.val)}
    for name, user in zip(["userAtag", "userBtag"], [userA, userB]):
        tag = TagAnnotationI()
        tag.textValue = rstring(name)
        # Only add description to first tag
        if name == "userAtag":
            tag.description = rstring('tag description')
        tag = get_update_service(user).saveAndReturnObject(tag, ctx)
        tags.append(tag)
    tags.sort(cmp_id)
    return tags


@pytest.fixture(scope='function')
def comments_userA(request, userA, groupA):
    """
    Returns new Bhojpur ODE Comments
    """
    comments = []
    ctx = {'ode.group': str(groupA.id.val)}
    for text in ["Test Comment", "Another comment userA"]:
        comment = CommentAnnotationI()
        comment.textValue = rstring(text)
        comments.append(comment)
    comments = get_update_service(userA).saveAndReturnArray(comments, ctx)
    comments.sort(cmp_id)
    return comments


@pytest.fixture(scope='function')
def rating_userA(request, userA, groupA):
    """
    Returns new Bhojpur ODE Rating
    """
    rating = LongAnnotationI()
    ctx = {'ode.group': str(groupA.id.val)}
    rating.longValue = rlong(4)
    rating.ns = rstring(ode.constants.metadata.NSINSIGHTRATING)
    rating = get_update_service(userA).saveAndReturnObject(rating, ctx)
    return rating


@pytest.fixture(scope='function')
def annotate_project(ann, project, user):
    """
    Returns userA's Tag linked to userB's Project
    by userA and userB
    """
    ctx = {'ode.group': str(project.details.group.id.val)}
    print('annotate_project', ctx)
    link = ProjectAnnotationLinkI()
    link.parent = ProjectI(project.id.val, False)
    link.child = ann
    update = get_connection(user).getUpdateService()
    link = update.saveAndReturnObject(link, ctx)
    return link


def expected_date(time):
    d = datetime.fromtimestamp(time/1000)
    return d.isoformat() + 'Z'


def expected_experimenter(experimenter):
    return {
        'id': experimenter.id.val,
        'odeName': experimenter.odeName.val,
        'firstName': unwrap(experimenter.firstName),
        'lastName': unwrap(experimenter.lastName)
    }


def lookup_expected_permissions(user, obj):
    query = get_query_service(user)
    params = ode.sys.ParametersI()
    params.addId(obj.id.val)
    objClass = obj.__class__.__name__[:-1]
    loadChild = ""
    if "Link" in objClass:
        loadChild = "join fetch obj.child as ann"
    sql = """select obj from %s as obj join fetch obj.details.owner as o
             %s where obj.id=:id""" % (objClass, loadChild)
    obj = query.findByQuery(sql, params, {'ode.group': '-1'})
    perms = obj.details.permissions
    return {
        'canAnnotate': perms.canAnnotate(),
        'canEdit': perms.canEdit(),
        'canDelete': perms.canDelete(),
        'canLink': perms.canLink()
    }


def expected_annotations(user, links):

    annotations = []
    exps = {}
    for link in links:

        ann = link.child
        parent = link.parent

        creation = ann.details.creationEvent._time.val
        linkDate = link.details.creationEvent._time.val
        # Need to lookup permissions for user
        annPerms = lookup_expected_permissions(user, ann)
        linkPerms = lookup_expected_permissions(user, link)

        exps[link.details.owner.id.val] = link.details.owner
        exps[ann.details.owner.id.val] = ann.details.owner

        marshalled = {
            'class': ann.__class__.__name__,
            'date': expected_date(creation),
            'link': {
                'owner': {
                    'id': link.details.owner.id.val
                },
                'date': expected_date(linkDate),
                'id': link.id.val,
                'parent': {
                    'class': parent.__class__.__name__,
                    'id': parent.id.val,
                    'name': parent.name.val
                },
                'permissions': linkPerms
            },
            'owner': {
                'id': ann.details.owner.id.val
            },
            'ns': unwrap(ann.ns),
            'description': unwrap(ann.description),
            'id': ann.id.val,
            'permissions': annPerms
        }
        for a in ['timeValue', 'termValue', 'longValue',
                  'doubleValue', 'boolValue', 'textValue']:
            if hasattr(ann, a):
                marshalled[a] = unwrap(getattr(ann, a))

        annotations.append(marshalled)

    # remove duplicates
    experimenters = [expected_experimenter(e) for e in exps.values()]
    experimenters.sort(key=lambda x: x['id'])

    return annotations, experimenters


class TestTreeAnnotations(ITest):
    """
    Tests to ensure that ODE.web "tree" infrastructure is working
    correctly.

    These tests make __extensive__ use of pytest fixtures.  In particular
    the scoping semantics allowing re-use of instances populated by the
    *request fixtures.  It is recommended that the pytest fixture
    documentation be studied in detail before modifications or attempts to
    fix failing tests are made:

     * https://pytest.org/latest/fixture.html
    """

    # Create a read-only group
    @pytest.fixture(scope='function')
    def groupA(self):
        """Returns a new read-annotate group."""
        a = self.new_group(perms='rwra--')
        print('GroupA', a.id.val)
        return a

    # Create a read-only group
    @pytest.fixture(scope='function')
    def groupB(self):
        """Returns a new read-only group."""
        b = self.new_group(perms='rwr---')
        print('Group B', b.id.val)
        return b

    # Create users in groups
    @pytest.fixture()
    def userA(self, groupA, groupB):
        """Returns a new user in the groupB (default) also add to groupA"""
        c, user = self.new_client_and_user(group=groupB)
        self.add_groups(user, [groupA])
        print('USER A', user.id.val, 'groupA', groupA.id.val)
        c.getSession().getAdminService().getEventContext()
        return c, user

    @pytest.fixture()
    def userB(self, groupA):
        """Returns another new user in the read-only group."""
        c, userb = self.new_client_and_user(group=groupA)
        print('USER B', userb.id.val, 'groupA', groupA.id.val)
        return c, userb

    def test_single_tag(self, userA, project_userA, tags_userA_userB):
        """
        Test a single annotation added by userA
        """
        conn = get_connection(userA)
        tag = tags_userA_userB[0]
        project = project_userA
        link = annotate_project(tag, project, userA)
        expected = expected_annotations(userA, [link])
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val])
        annotations, experimenters = marshaled
        anns, exps = expected

        assert annotations == anns
        assert experimenters == exps

    def test_single_tag_userB(self, userB, project_userA,
                              tags_userA_userB):
        """
        Test a single annotation added by userB
        """
        conn = get_connection(userB)
        tag = tags_userA_userB[0]
        project = project_userA
        link = annotate_project(tag, project, userB)
        expected = expected_annotations(userB, [link])
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val])
        annotations, experimenters = marshaled
        anns, exps = expected

        assert annotations == anns
        assert experimenters == exps

    def test_twin_tags_userA_userB(self, userA, userB, project_userA,
                                   tags_userA_userB):
        """
        Test two users annotate the same Project with the same tag
        """
        conn = get_connection(userA)
        tag1 = tags_userA_userB[0]
        tag2 = tags_userA_userB[1]
        project = project_userA
        link1 = annotate_project(tag1, project, userA)
        link2 = annotate_project(tag1, project, userB)
        link3 = annotate_project(tag2, project, userB)
        expected = expected_annotations(userA, [link1, link2, link3])
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val])
        annotations, experimenters = marshaled
        experimenters.sort(key=lambda x: x['id'])
        anns, exps = expected
        # need to sort since marshal_annotations doesn't sort yet
        annotations.sort(key=lambda x: x['link']['id'])
        anns.sort(key=lambda x: x['link']['id'])

        assert len(annotations) == 3
        assert len(experimenters) == 2
        assert annotations[0] == anns[0]
        assert annotations[1] == anns[1]
        assert annotations[2] == anns[2]
        assert experimenters == exps

    def test_twin_tags_projects(self, userA, userB, projects_userA,
                                tags_userA_userB):
        """
        Test two users annotate the two Projects with the same tag(s)
        """
        conn = get_connection(userA)
        tag1 = tags_userA_userB[0]
        tag2 = tags_userA_userB[1]
        project1 = projects_userA[0]
        project2 = projects_userA[1]
        link1 = annotate_project(tag1, project1, userA)
        link2 = annotate_project(tag1, project2, userB)
        link3 = annotate_project(tag2, project2, userB)
        link4 = annotate_project(tag2, project2, userA)
        expected = expected_annotations(userA, [link1, link2, link3, link4])
        pids = [p.id.val for p in projects_userA]
        marshaled = marshal_annotations(conn=conn, project_ids=pids)
        annotations, experimenters = marshaled
        experimenters.sort(key=lambda x: x['id'])
        anns, exps = expected
        # need to sort since marshal_annotations doesn't sort yet
        annotations.sort(key=lambda x: x['link']['id'])
        anns.sort(key=lambda x: x['link']['id'])

        assert len(annotations) == 4
        assert len(experimenters) == 2
        assert annotations[0] == anns[0]
        assert annotations[1] == anns[1]
        assert annotations[2] == anns[2]
        assert annotations[3] == anns[3]
        assert experimenters == exps

    def test_tags_comments_project(self, userA, project_userA, rating_userA,
                                   tags_userA_userB, comments_userA):
        """
        Test annotate Project with the Tags and Comments
        """
        conn = get_connection(userA)
        tag1, tag2 = tags_userA_userB
        comment1, comment2 = comments_userA
        rating = rating_userA

        project = project_userA
        link1 = annotate_project(tag1, project, userA)
        link2 = annotate_project(comment1, project, userA)
        link3 = annotate_project(tag2, project, userA)
        link4 = annotate_project(comment2, project, userA)
        link5 = annotate_project(rating, project, userA)

        # Get just the tags...
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val],
                                        ann_type='tag')
        expected = expected_annotations(userA, [link1, link3])
        annotations, experimenters = marshaled
        experimenters.sort(key=lambda x: x['id'])
        anns, exps = expected
        # need to sort since marshal_annotations doesn't sort yet
        annotations.sort(key=lambda x: x['link']['id'])
        anns.sort(key=lambda x: x['link']['id'])

        assert len(annotations) == 2
        assert len(experimenters) == 2
        assert annotations[0] == anns[0]
        assert annotations[1] == anns[1]
        assert experimenters == exps

        # Get just the comments...
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val],
                                        ann_type='comment')
        expected = expected_annotations(userA, [link2, link4])
        annotations, experimenters = marshaled
        experimenters.sort(key=lambda x: x['id'])
        anns, exps = expected
        # need to sort since marshal_annotations doesn't sort yet
        annotations.sort(key=lambda x: x['link']['id'])
        anns.sort(key=lambda x: x['link']['id'])

        assert len(annotations) == 2
        assert annotations[0] == anns[0]
        assert annotations[1] == anns[1]
        assert len(experimenters) == 1
        assert annotations == anns
        assert experimenters == exps

        # Get all annotations
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val])
        expected = expected_annotations(userA, [link1, link2, link3,
                                                link4, link5])
        annotations, experimenters = marshaled
        experimenters.sort(key=lambda x: x['id'])
        anns, exps = expected

        assert len(annotations) == 5
        assert len(experimenters) == 2
        # need to sort since marshal_annotations doesn't sort yet
        annotations.sort(key=lambda x: x['link']['id'])
        anns.sort(key=lambda x: x['link']['id'])
        assert annotations[0] == anns[0]
        assert annotations[1] == anns[1]
        assert annotations[2] == anns[2]
        assert annotations[3] == anns[3]
        assert annotations[4] == anns[4]
        assert experimenters == exps

    def test_filter_by_namespace(self, userA, project_userA, rating_userA,
                                 comments_userA):
        """Test filtering of Annotations by Namespace."""
        conn = get_connection(userA)
        comment1, comment2 = comments_userA
        rating = rating_userA

        project = project_userA
        rating_link = annotate_project(rating, project, userA)
        annotate_project(comment1, project, userA)
        annotate_project(comment2, project, userA)

        # Filter by rating namespace should only return a single link
        rating_ns = ode.constants.metadata.NSINSIGHTRATING
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val],
                                        ns=rating_ns)
        expected = expected_annotations(userA, [rating_link])
        assert marshaled[0] == expected[0]

        # Confirm we get all 3 links when we don't filter
        marshaled = marshal_annotations(conn=conn,
                                        project_ids=[project.id.val])
        assert len(marshaled[0]) == 3
