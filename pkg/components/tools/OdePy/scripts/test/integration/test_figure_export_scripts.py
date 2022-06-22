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
   Integration test for figure export scripts.
"""

import pytest
import ode
from script import ScriptTest
from script import run_script
from script import check_file_annotation


thumbnail_figure = "/ode/figure_scripts/Thumbnail_Figure.py"
split_view_figure = "/ode/figure_scripts/Split_View_Figure.py"
roi_figure = "/ode/figure_scripts/ROI_Split_Figure.py"
movie_figure = "/ode/figure_scripts/Movie_Figure.py"
movie_roi_figure = "/ode/figure_scripts/Movie_ROI_Figure.py"


class TestFigureExportScripts(ScriptTest):

    @pytest.mark.parametrize('data_type', ["Dataset", "Image"])
    @pytest.mark.parametrize('all_parameters', [True, False])
    def test_thumbnail_figure(self, data_type, all_parameters):

        sid = super(TestFigureExportScripts, self).get_script(thumbnail_figure)
        assert sid > 0

        client, user = self.new_client_and_user()

        # create several test images in a dataset
        dataset = self.make_dataset("thumbnailFigure-test", client=client)

        # make some tags
        tag_ids = []
        session = client.getSession()
        for t in range(5):
            tag = ode.model.TagAnnotationI()
            tag.setTextValue(ode.rtypes.rstring("TestTag_%s" % t))
            tag = session.getUpdateService().saveAndReturnObject(tag)
            tag_ids.append(tag.id)

        # put some images in dataset
        image_ids = []
        for i in range(2):
            # x,y,z,c,t
            image = self.create_test_image(100, 100, 1, 1, 1, session)
            image_ids.append(ode.rtypes.rlong(image.id.val))
            self.link(dataset, image, client=client)

            # add tag
            t = i % 5
            tag = ode.model.TagAnnotationI(tag_ids[t].val, False)
            self.link(image, tag, client=client)

        dataset_ids = [ode.rtypes.rlong(dataset.id.val)]
        ids = image_ids
        if data_type == "Dataset":
            ids = dataset_ids
        if all_parameters:
            args = {
                "IDs": ode.rtypes.rlist(ids),
                "Data_Type": ode.rtypes.rstring(data_type),
                "Thumbnail_Size": ode.rtypes.rint(16),
                "Max_Columns": ode.rtypes.rint(6),
                "Format": ode.rtypes.rstring("PNG"),
                "Figure_Name": ode.rtypes.rstring("thumbnail-test"),
                "Tag_IDs": ode.rtypes.rlist(tag_ids)
            }
        else:
            args = {
                "Data_Type": ode.rtypes.rstring(data_type),
                "IDs": ode.rtypes.rlist(ids)
            }
        ann = run_script(client, sid, args, "File_Annotation")

        # should have figures attached to dataset and first image.
        c = self.new_client(user=user)
        check_file_annotation(c, ann, parent_type=data_type)

    @pytest.mark.parametrize('all_parameters', [True, False])
    def test_split_view_figure(self, all_parameters):

        id = super(TestFigureExportScripts, self).get_script(split_view_figure)
        assert id > 0

        client, user = self.new_client_and_user()

        # create several test images in a dataset
        dataset = self.make_dataset("thumbnailFigure-test", client=client)
        project = self.make_project("thumbnailFigure-test", client=client)
        self.link(project, dataset, client=client)

        # put some images in dataset
        session = client.getSession()
        image_ids = []
        for i in range(2):
            image = self.create_test_image(256, 200, 5, 4, 1, session)
            image_ids.append(ode.rtypes.rlong(image.id.val))
            self.link(dataset, image, client=client)

        c_names_map = ode.rtypes.rmap({'0': ode.rtypes.rstring("DAPI"),
                                         '1': ode.rtypes.rstring("GFP"),
                                         '2': ode.rtypes.rstring("Red"),
                                         '3': ode.rtypes.rstring("ACA")})
        blue = ode.rtypes.rlong(255)
        red = ode.rtypes.rlong(16711680)
        mrgd_colours_map = ode.rtypes.rmap({'0': blue, '1': blue, '3': red})
        if all_parameters:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids),
                "Z_Start": ode.rtypes.rint(0),
                "Z_End": ode.rtypes.rint(3),
                "Channel_Names": c_names_map,
                "Split_Indexes": ode.rtypes.rlist(
                    [ode.rtypes.rint(1), ode.rtypes.rint(2)]),
                "Split_Panels_Grey": ode.rtypes.rbool(True),
                "Merged_Colours": mrgd_colours_map,
                "Merged_Names": ode.rtypes.rbool(True),
                "Width": ode.rtypes.rint(200),
                "Height": ode.rtypes.rint(200),
                "Image_Labels": ode.rtypes.rstring("Datasets"),
                "Algorithm": ode.rtypes.rstring("Mean Intensity"),
                "Stepping": ode.rtypes.rint(1),
                # will be ignored since no pixelsize set
                "Scalebar": ode.rtypes.rint(10),
                "Format": ode.rtypes.rstring("PNG"),
                "Figure_Name": ode.rtypes.rstring("splitViewTest"),
            }
        else:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids),
                "Merged_Colours": mrgd_colours_map,
                "Format": ode.rtypes.rstring("PNG"),
                "Figure_Name": ode.rtypes.rstring("splitViewTest")
            }
        ann = run_script(client, id, args, "File_Annotation")

        c = self.new_client(user=user)
        check_file_annotation(c, ann)

    @pytest.mark.parametrize('all_parameters', [True, False])
    def test_roi_figure(self, all_parameters):

        sid = super(TestFigureExportScripts, self).get_script(roi_figure)
        assert sid > 0

        client, user = self.new_client_and_user()

        # create several test images in a dataset
        dataset = self.make_dataset("roiFig-test", client=client)
        project = self.make_project("roiFig-test", client=client)
        self.link(project, dataset, client=client)

        # put some images in dataset
        image_ids = []
        session = client.getSession()
        for i in range(2):
            image = self.create_test_image(256, 200, 5, 4, 1, session)
            image_ids.append(ode.rtypes.rlong(image.id.val))
            self.link(dataset, image, client=client)
            add_rectangle_roi(session.getUpdateService(),
                              50 + (i * 10), 100 - (i * 10),
                              50 + (i * 5), 100 - (i * 5),
                              image.id.val)

        c_names_map = ode.rtypes.rmap({'0': ode.rtypes.rstring("DAPI"),
                                         '1': ode.rtypes.rstring("GFP"),
                                         '2': ode.rtypes.rstring("Red"),
                                         '3': ode.rtypes.rstring("ACA")})
        blue = ode.rtypes.rint(255)
        red = ode.rtypes.rint(16711680)
        mrgd_colours_map = ode.rtypes.rmap({'0': blue, '1': blue, '3': red})
        if all_parameters:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids),
                "Channel_Names": c_names_map,
                "Split_Indexes": ode.rtypes.rlist(
                    [ode.rtypes.rlong(1), ode.rtypes.rlong(2)]),
                "Split_Panels_Grey": ode.rtypes.rbool(True),
                "Merged_Colours": mrgd_colours_map,
                "Merged_Names": ode.rtypes.rbool(True),
                "Width": ode.rtypes.rint(200),
                "Height": ode.rtypes.rint(200),
                "Image_Labels": ode.rtypes.rstring("Datasets"),
                "Algorithm": ode.rtypes.rstring("Mean Intensity"),
                "Stepping": ode.rtypes.rint(1),
                # will be ignored since no pixelsize set
                "Scalebar": ode.rtypes.rint(10),
                "Format": ode.rtypes.rstring("PNG"),
                "Figure_Name": ode.rtypes.rstring("splitViewTest"),
                "Overlay_Colour": ode.rtypes.rstring("Red"),
                "ROI_Zoom": ode.rtypes.rfloat(3),
                # won't be found - but should still work
                "ROI_Label": ode.rtypes.rstring("fakeTest"),
            }
        else:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids)
            }

        ann = run_script(client, sid, args, "File_Annotation")

        c = self.new_client(user=user)
        check_file_annotation(c, ann)

    @pytest.mark.parametrize('all_parameters', [True, False])
    def test_movie_roi_figure(self, all_parameters):

        sid = super(TestFigureExportScripts, self).get_script(movie_roi_figure)
        assert sid > 0

        client, user = self.new_client_and_user()

        # create several test images in a dataset
        dataset = self.make_dataset("movieRoiFig-test", client=client)
        project = self.make_project("movieRoiFig-test", client=client)
        self.link(project, dataset, client=client)

        # put some images in dataset
        image_ids = []
        session = client.getSession()
        for i in range(2):
            image = self.create_test_image(256, 256, 10, 3, 1, session)
            image_ids.append(ode.rtypes.rlong(image.id.val))
            self.link(dataset, image, client=client)

            # add roi -   x, y, width, height
            add_rectangle_roi(session.getUpdateService(),
                              50 + (i * 10), 100 - (i * 10),
                              50 + (i * 5), 100 - (i * 5),
                              image.id.val)

        if all_parameters:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids),
                "ROI_Zoom": ode.rtypes.rfloat(3),
                "Max_Columns": ode.rtypes.rint(10),
                "Resize_Images": ode.rtypes.rbool(True),
                "Width": ode.rtypes.rint(200),
                "Height": ode.rtypes.rint(200),
                "Image_Labels": ode.rtypes.rstring("Datasets"),
                "Show_ROI_Duration": ode.rtypes.rbool(True),
                # will be ignored since no pixelsize set
                "Scalebar": ode.rtypes.rint(10),
                # will be ignored since no pixelsize set
                "Scalebar_Colour": ode.rtypes.rstring("White"),
                # won't be found - but should still work
                "Roi_Selection_Label": ode.rtypes.rstring("fakeTest"),
                "Algorithm": ode.rtypes.rstring("Mean Intensity"),
                "Figure_Name": ode.rtypes.rstring("movieROITest")
            }
        else:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids)
            }
        ann = run_script(client, sid, args, "File_Annotation")

        c = self.new_client(user=user)
        check_file_annotation(c, ann)

    @pytest.mark.parametrize('all_parameters', [True, False])
    def test_movie_figure(self, all_parameters):

        sid = super(TestFigureExportScripts, self).get_script(movie_figure)
        assert sid > 0

        client, user = self.new_client_and_user()

        # create several test images in a dataset
        dataset = self.make_dataset("movieFig-test", client=client)
        project = self.make_project("movieFig-test", client=client)
        self.link(project, dataset, client=client)

        # put some images in dataset
        session = client.getSession()
        image_ids = []
        for i in range(2):
            image = self.create_test_image(256, 256, 5, 3, 20, session)
            image_ids.append(ode.rtypes.rlong(image.id.val))
            self.link(dataset, image, client=client)

        red = ode.rtypes.rint(16711680)
        t_indexes = [ode.rtypes.rint(0), ode.rtypes.rint(1),
                     ode.rtypes.rint(5), ode.rtypes.rint(10),
                     ode.rtypes.rint(15)]

        if all_parameters:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids),
                "T_Indexes": ode.rtypes.rlist(t_indexes),
                "Z_Start": ode.rtypes.rint(1),
                "Z_End": ode.rtypes.rint(3),
                "Width": ode.rtypes.rint(150),
                "Height": ode.rtypes.rint(150),
                "Image_Labels": ode.rtypes.rstring("Datasets"),
                "Algorithm": ode.rtypes.rstring("Mean Intensity"),
                "Stepping": ode.rtypes.rint(1),
                "Scalebar": ode.rtypes.rint(10),
                "Format": ode.rtypes.rstring("PNG"),
                "Figure_Name": ode.rtypes.rstring("movieFigureTest"),
                "TimeUnits": ode.rtypes.rstring("MINS"),
                "Overlay_Colour": red,
            }
        else:
            args = {
                "Data_Type": ode.rtypes.rstring("Image"),
                "IDs": ode.rtypes.rlist(image_ids),
                "T_Indexes": ode.rtypes.rlist(t_indexes)
            }
        ann = run_script(client, sid, args, "File_Annotation")

        c = self.new_client(user=user)
        check_file_annotation(c, ann)


def add_rectangle_roi(update_service, x, y, width, height, image_id):
    """
    Adds a Rectangle (particle) to the current Bhojpur ODE image, at point x, y.
    """
    # create an ROI, add the rectangle and save
    roi = ode.model.RoiI()
    roi.setImage(ode.model.ImageI(image_id, False))
    r = update_service.saveAndReturnObject(roi)

    # create and save a rectangle shape
    rect = ode.model.RectangleI()
    rect.x = ode.rtypes.rdouble(x)
    rect.y = ode.rtypes.rdouble(y)
    rect.width = ode.rtypes.rdouble(width)
    rect.height = ode.rtypes.rdouble(height)
    rect.locked = ode.rtypes.rbool(True)        # don't allow editing
    rect.strokeWidth = ode.model.LengthI()
    rect.strokeWidth.setValue(1.0)
    rect.strokeWidth.setUnit(ode.model.enums.UnitsLength.POINT)

    # link the rectangle to the ROI and save it
    rect.setRoi(r)
    r.addShape(rect)
    update_service.saveAndReturnObject(rect)
