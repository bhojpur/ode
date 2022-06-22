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
   Integration test for analysis scripts.
"""

import ode
import ode.scripts
from script import ScriptTest
from script import run_script, points_to_string
from script import check_file_annotation

kymograph = "/ode/analysis_scripts/Kymograph.py"
plot_profile = "/ode/analysis_scripts/Plot_Profile.py"
kymograph_analysis = "/ode/analysis_scripts/Kymograph_Analysis.py"


class TestAnalysisScripts(ScriptTest):

    def test_kymograph(self):
        script_id = super(TestAnalysisScripts, self).get_script(kymograph)
        assert script_id > 0

        client, user = self.new_client_and_user()

        # create a test image
        size_t = 3
        size_x = 100
        size_y = 100
        # x,y,z,c,t
        session = client.getSession()
        image = self.create_test_image(size_x, size_y, 1, 2, size_t, session)
        image_id = image.id.val
        roi = create_roi(image_id, -10, 80.5, 60, 11.1, size_t, True)
        session.getUpdateService().saveAndReturnObject(roi)
        image_ids = []
        image_ids.append(ode.rtypes.rlong(image_id))
        args = {
            "Data_Type": ode.rtypes.rstring("Image"),
            "IDs": ode.rtypes.rlist(image_ids),
            "Line_Width": ode.rtypes.rint(5)
        }

        kymograph_img = run_script(client, script_id, args, "New_Image")

        # check the result
        assert kymograph_img is not None
        image_id = kymograph_img.getValue().id.val
        assert image_id > 0
        new_image = client.sf.getQueryService().get('Image', image_id)
        assert new_image.name.val == "%s_kymograph" % image.name.val

    def test_plot_profile(self):
        script_id = super(TestAnalysisScripts, self).get_script(plot_profile)
        assert script_id > 0

        client, user = self.new_client_and_user()

        # create a test image
        size_t = 3
        size_x = 100
        size_y = 100
        session = client.getSession()
        image = self.create_test_image(size_x, size_y, 1, 2, size_t, session)
        image_id = image.id.val
        roi = create_roi(image_id, 0, size_x / 2, 0, size_y / 2, size_t, False)
        session.getUpdateService().saveAndReturnObject(roi)
        image_ids = []
        image_ids.append(ode.rtypes.rlong(image_id))
        args = {
            "Data_Type": ode.rtypes.rstring("Image"),
            "IDs": ode.rtypes.rlist(image_ids),
            "Line_Width": ode.rtypes.rint(2),
            "Sum_or_Average": ode.rtypes.rstring("Average")
        }
        ann = run_script(client, script_id, args, "Line_Data")
        c = self.new_client(user=user)
        check_file_annotation(c, ann)

    def test_kymograph_analysis(self):
        script_id = super(TestAnalysisScripts, self).get_script(kymograph)

        client, user = self.new_client_and_user()

        # create a test image
        size_t = 3
        size_x = 100
        size_y = 100
        # x,y,z,c,t
        session = client.getSession()
        image = self.create_test_image(size_x, size_y, 1, 2, size_t, session)
        image_id = image.id.val
        roi = create_roi(image_id, 0, size_x / 2, 0, size_y / 2, size_t, True)
        session.getUpdateService().saveAndReturnObject(roi)
        image_ids = []
        image_ids.append(ode.rtypes.rlong(image_id))
        args = {
            "Data_Type": ode.rtypes.rstring("Image"),
            "IDs": ode.rtypes.rlist(image_ids),
            "Line_Width": ode.rtypes.rint(5)
        }

        kymograph_img = run_script(client, script_id, args, "New_Image")
        # now analyse the Kymograph image
        sid = super(TestAnalysisScripts, self).get_script(kymograph_analysis)
        assert sid > 0

        image_id = kymograph_img.getValue().id.val
        roi = create_roi(image_id, 0, 2, 0, 2, 1, False)
        session.getUpdateService().saveAndReturnObject(roi)
        image_ids = []
        image_ids.append(ode.rtypes.rlong(image_id))
        args = {
            "Data_Type": ode.rtypes.rstring("Image"),
            "IDs": ode.rtypes.rlist(image_ids)
        }

        ann = run_script(client, sid, args, "Line_Data")
        c = self.new_client(user=user)
        check_file_annotation(c, ann)


def create_roi(image_id, x1, x2, y1, y2, size_t, with_polylines):
    """
    Create an ROI with lines and polylines.
    """
    roi = ode.model.RoiI()
    roi.setImage(ode.model.ImageI(image_id, False))
    # create lines and polylines
    for t in range(size_t):
        # lines no t and z set
        line = ode.model.LineI()
        line.x1 = ode.rtypes.rdouble(x1)
        line.x2 = ode.rtypes.rdouble(x2)
        line.y1 = ode.rtypes.rdouble(y1)
        line.y2 = ode.rtypes.rdouble(y2)
        roi.addShape(line)
        # polylines on each timepoint
        if with_polylines:
            polyline = ode.model.PolylineI()
            polyline.theZ = ode.rtypes.rint(0)
            polyline.theT = ode.rtypes.rint(t)
            points = [[10.5, 20], [50.55, 50], [75, 60]]
            polyline.points = ode.rtypes.rstring(points_to_string(points))
            roi.addShape(polyline)
    return roi
