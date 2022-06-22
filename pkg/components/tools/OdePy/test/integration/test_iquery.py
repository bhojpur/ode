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
   Integration test focused on the ode.api.IQuery interface.
"""

from ode.testlib import ITest
from ode.rtypes import rstring
from ode.rtypes import unwrap, wrap
from ode.model import CommentAnnotationI
from ode.model import TagAnnotationI, ImageI, ImageAnnotationLinkI
from ode.model import PermissionsI
from ode.sys import ParametersI
from helpers import createImageWithPixels

class TestQuery(ITest):

    # ticket:1849

    def testGetPixelsCount(self):
        q = self.root.sf.getQueryService()
        a = self.root.sf.getAdminService()
        groups = a.lookupGroups()
        for group in groups:
            rtypeseqseq = q.projection(
                """
                select p.pixelsType.value,
                sum(cast(p.sizeX as long) * p.sizeY
                    * p.sizeZ * p.sizeT * p.sizeC)
                from Pixels p group by p.pixelsType.value
                """,
                None, {"ode.group": str(group.id.val)})
            rv = unwrap(rtypeseqseq)
            as_map = dict()
            for obj_array in rv:
                as_map[obj_array[0]] = obj_array[1]
            if len(as_map) > 0:
                print("Group %s: %s" % (group.id.val, as_map))

    def testQueryTaggedUnique(self):

        # get group we're working on...
        ctx = self.client.sf.getAdminService().getEventContext()
        groupId = ctx.groupId
        print('groupId', groupId)

        # Admin sets permissions to read-ann
        admin = self.root.sf.getAdminService()
        rootUpdate = self.root.sf.getUpdateService()
        gr = admin.getGroup(groupId)
        p = PermissionsI()
        p.setUserRead(True)
        p.setUserWrite(True)
        p.setGroupRead(True)
        p.setGroupAnnotate(True)
        p.setGroupWrite(False)
        p.setWorldRead(False)
        p.setWorldAnnotate(False)
        p.setWorldWrite(False)
        gr.details.permissions = p
        admin.updateGroup(gr)

        # Update context for user
        ctx = self.client.sf.getAdminService().getEventContext()
        update = self.client.sf.getUpdateService()
        queryService = self.client.sf.getQueryService()
        tagCount = 5
        # User creates tag linked to images
        tag = TagAnnotationI()
        tag.textValue = wrap("test_iQuerySpeed")
        links = []

        for i in range(tagCount):
            iid = createImageWithPixels(self.client, self.uuid())
            link = ImageAnnotationLinkI()
            link.parent = ImageI(iid, False)
            link.child = tag
            links.append(link)
        links = update.saveAndReturnArray(links)
        tag = links[0].child
        # check permissions
        p = tag.getDetails().getPermissions()
        assert p.isGroupRead()
        assert p.isGroupAnnotate()

        # Root also links user's tag to images
        rootLinks = []
        for l in links:
            link = ImageAnnotationLinkI()
            link.parent = ImageI(l.parent.id, False)
            link.child = TagAnnotationI(l.child.id, False)
            rootLinks.append(link)
        rootUpdate.saveAndReturnArray(rootLinks, {'ode.group': str(groupId)})

        q = """select distinct new map(obj.id as id,
               obj.name as name,
               obj.details.owner.id as ownerId,
               obj as image_details_permissions,
               obj.fileset.id as filesetId,
               lower(obj.name) as n
             ,
             pix.sizeX as sizeX,
             pix.sizeY as sizeY,
             pix.sizeZ as sizeZ
             )
            from Image obj  left outer join obj.pixels pix
            join obj.annotationLinks alink
            where %s
            order by lower(obj.name), obj.id """

        params = ParametersI()
        params.add('tid', tag.id)

        # We can get all the tagged images like this.
        # We use an additional select statement to give 'unique' results
        uniqueClause = """alink.id = (select max(alink.id)
                from ImageAnnotationLink alink
                where alink.child.id=:tid and alink.parent.id=obj.id)"""
        query = q % uniqueClause
        result1 = queryService.projection(query, params,
                                          {'ode.group': str(groupId)})
        assert len(result1) == tagCount

        # Without the select statement, we get the same image returned
        # multiple times if there is no 'distinct'
        clause = "alink.child.id=:tid"
        query = q % clause
        result2 = queryService.projection(query, params,
                                          {'ode.group': str(groupId)})
        assert len(result2) == tagCount
        for idx in range(len(result1)-1):
            # Omit final since == isn't defined for Ice objects.
            assert result1[idx] == result2[idx]

    def testClassType(self):
        uuid = self.uuid()
        created = []
        for Ann in (CommentAnnotationI, TagAnnotationI):
            ann = Ann()
            ann.setNs(rstring(uuid))
            ann = self.update.saveAndReturnObject(ann)
            created.append(ann)
        query_string = """
        select type(a.class) from Annotation a
        where a.ns = :uuid
        """
        params = ParametersI()
        params.addString("uuid", uuid)
        rv = self.query.projection(query_string, params)
        rv = [x[0] for x in unwrap(rv)]
        assert len(rv) == 2
        assert "ode.model.annotations.CommentAnnotation" in rv
        assert "ode.model.annotations.TagAnnotation" in rv
