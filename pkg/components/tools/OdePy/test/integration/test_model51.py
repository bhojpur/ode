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
Basic tests for additions/changes to the 1.0 model.
"""

from ode.testlib import ITest
import pytest
import ode
import ode.model

from ode.model import NamedValue as NV
from ode.rtypes import unwrap

class TestModel51(ITest):

    def testExposureTime(self):
        imgs = self.import_fake_file(name="testExposureTime", exposureTime=1.2)
        img = imgs[0]
        plane_info = self.query.findByQuery((
            "select pi from PlaneInfo pi "
            "join fetch pi.exposureTime "
            "join pi.pixels as pix join pix.image as img "
            "where img.id = :id"), ode.sys.ParametersI().addId(img.id.val))
        exposure = plane_info.getExposureTime()
        unit = exposure.getUnit()
        assert ode.model.enums.UnitsTime.SECOND == unit

        micros = ode.model.enums.UnitsTime.MICROSECOND

        exposure.setUnit(micros)
        plane_info = self.update.saveAndReturnObject(plane_info)
        exposure = plane_info.getExposureTime()
        unit = exposure.getUnit()
        assert ode.model.enums.UnitsTime.MICROSECOND == unit

    def testPhysicalSize(self):
        imgs = self.import_fake_file(name="testPhysicalSize",
                                     physicalSizeZ=2.0)
        img = imgs[0]
        pixels = self.query.findByQuery((
            "select pix from Pixels pix "
            "join fetch pix.physicalSizeZ "
            "where pix.image.id = :id"),
            ode.sys.ParametersI().addId(img.id.val))
        size_z = pixels.getPhysicalSizeZ()
        unit = size_z.getUnit()
        assert ode.model.enums.UnitsLength.MICROMETER == unit

        mm = ode.model.enums.UnitsLength.MILLIMETER

        size_z.setUnit(mm)
        pixels = self.update.saveAndReturnObject(pixels)
        size_z = pixels.getPhysicalSizeZ()
        unit = size_z.getUnit()
        assert ode.model.enums.UnitsLength.MILLIMETER == unit

    UL = ode.model.enums.UnitsLength
    UL = sorted(UL._enumerators.values())

    @pytest.mark.parametrize("ul", UL, ids=[str(x) for x in UL])
    def testAllLengths(self, ul):
        one = ode.model.LengthI()
        one.setValue(1.0)
        one.setUnit(ul)
        roi = ode.model.RoiI()
        line = ode.model.LineI()
        line.setStrokeWidth(one)
        roi.addShape(line)
        roi = self.update.saveAndReturnObject(roi)
        line = roi.copyShapes()[0]
        stroke = line.getStrokeWidth()
        assert ul == stroke.getUnit()

    def testAsMapMethod(self):
        g = ode.model.ExperimenterGroupI()
        g.setConfig(
            [NV("foo", "bar")]
        )
        m = g.getConfigAsMap()
        assert m["foo"] == "bar"

    def assertMapAnnotation(self, anns, mid):
        m = None
        for a in anns:
            if isinstance(a, ode.model.MapAnnotationI):
                if a.id.val == mid:
                    m = a
        assert m
        assert "foo" == m.getMapValue()[0].name
        assert "bar" == m.getMapValue()[0].value

    def testMapEagerFetch(self):
        m = ode.model.MapAnnotationI()
        m.setMapValue(
            [NV("foo", "bar")]
        )
        m = self.update.saveAndReturnObject(m)
        anns = self.query.findAllByQuery(
            "select m from MapAnnotation m ",
            None)
        self.assertMapAnnotation(anns, m.id.val)

        # Add a second annotation and query both
        c = ode.model.CommentAnnotationI()
        c = self.update.saveAndReturnObject(c)
        anns = self.query.findAllByQuery(
            "select m from Annotation m ",
            None)
        self.assertMapAnnotation(anns, m.id.val)

        # Now place both on an image and retry
        i = ode.model.ImageI()
        i.setName(ode.rtypes.rstring("testMapEagerFetch"))
        i.linkAnnotation(m)
        i.linkAnnotation(c)
        i = self.update.saveAndReturnObject(i)
        imgs = self.query.findByQuery(
            ("select i from Image i join fetch "
             "i.annotationLinks l join fetch l.child "
             "where i.id = :id"),
            ode.sys.ParametersI().addId(i.id.val))
        anns = imgs.linkedAnnotationList()
        self.assertMapAnnotation(anns, m.id.val)

        # And now load via IMetadata
        meta = self.client.sf.getMetadataService()
        anns = meta.loadAnnotations(
            "ode.model.Image",
            [i.id.val],
            [],  # Supported Annotation types
            [],  # Annotator IDs
            None)
        self.assertMapAnnotation(anns[i.id.val], m.id.val)

    def testMapSecurity(self):
        c1 = self.new_client(perms="rwr---")
        u1 = c1.sf.getUpdateService()
        a1 = c1.sf.getAdminService()
        g1 = a1.getEventContext().groupName
        m1 = ode.model.MapAnnotationI()
        m1.setMapValue(
            [NV("foo", "bar")]
        )
        m1 = u1.saveAndReturnObject(m1)

        # Now create another user and try to edit
        c2 = self.new_client(group=g1)
        q2 = c2.sf.getQueryService()
        u2 = c2.sf.getUpdateService()
        m2 = q2.get("MapAnnotation", m1.id.val)
        assert not m2.details.permissions.canEdit()

        # Additions fail
        m2.getMapValue().append(
            NV("edited-by", str(c2)))
        with pytest.raises(ode.SecurityViolation):
            u2.saveAndReturnObject(m2)

        # Removals fail
        m2.setMapValue([])
        with pytest.raises(ode.SecurityViolation):
            u2.saveAndReturnObject(m2)

        # Also via a None
        m2.setMapValue(None)
        with pytest.raises(ode.SecurityViolation):
            u2.saveAndReturnObject(m2)

        # Alterations fail
        m2.setMapValue(
            [NV("foo", "WRONG")])
        with pytest.raises(ode.SecurityViolation):
            u2.saveAndReturnObject(m2)

    def testUnitProjections(self):
        imgs = self.import_fake_file(name="testUnitProjections",
                                     exposureTime=1.2)
        img = imgs[0]

        as_map = self.query.projection((
            "select pi.exposureTime from PlaneInfo pi "
            "join pi.pixels as pix join pix.image as img "
            "where img.id = :id"),
            ode.sys.ParametersI().addId(img.id.val))[0][0]
        as_map = unwrap(as_map)
        pytest.assertAlmostEqual(1.2, as_map.get("value"))
        assert "SECOND" == as_map.get("unit")
        assert "s" == as_map.get("symbol")

        as_objs = self.query.projection((
            "select pi.exposureTime.value, "
            "pi.exposureTime.unit, "
            "cast(pi.exposureTime.unit as text) from PlaneInfo pi "
            "join pi.pixels as pix join pix.image as img "
            "where img.id = :id"),
            ode.sys.ParametersI().addId(img.id.val))[0]
        as_objs = unwrap(as_objs)
        pytest.assertAlmostEqual(1.2, as_objs[0])
        assert "SECOND" == as_objs[1]
        assert "s" == as_objs[2]
