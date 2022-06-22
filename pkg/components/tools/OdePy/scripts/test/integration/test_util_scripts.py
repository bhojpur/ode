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
   Integration test for util scripts.
"""

import ode
from ode.gateway import ServerGateway
import ode.scripts
import pytest
from script import ScriptTest
from script import run_script
from ode.cmd import Delete2
from ode.rtypes import wrap

channel_offsets = "/ode/util_scripts/Channel_Offsets.py"
combine_images = "/ode/util_scripts/Combine_Images.py"
images_from_rois = "/ode/util_scripts/Images_From_ROIs.py"
dataset_to_plate = "/ode/util_scripts/Dataset_To_Plate.py"
move_annotations = "/ode/util_scripts/Move_Annotations.py"

class TestUtilScripts(ScriptTest):

    def test_channel_offsets(self):
        script_id = super(TestUtilScripts, self).get_script(channel_offsets)
        assert script_id > 0

        client = self.root

        image = self.create_test_image(100, 100, 2, 3, 4)    # x,y,z,c,t
        image_id = image.id.val
        image_ids = []
        image_ids.append(ode.rtypes.rlong(image_id))
        args = {
            "Data_Type": ode.rtypes.rstring("Image"),
            "IDs": ode.rtypes.rlist(image_ids),
            "Channel1_X_shift": ode.rtypes.rint(1),
            "Channel1_Y_shift": ode.rtypes.rint(1),
            "Channel2_X_shift": ode.rtypes.rint(2),
            "Channel2_Y_shift": ode.rtypes.rint(2),
            "Channel3_X_shift": ode.rtypes.rint(3),
            "Channel3_Y_shift": ode.rtypes.rint(3),
        }
        offset_img = run_script(client, script_id, args, "Image")
        # check the result
        assert offset_img is not None
        assert offset_img.getValue().id.val > 0

    def test_combine_images(self):
        script_id = super(TestUtilScripts, self).get_script(combine_images)
        assert script_id > 0
        client = self.root

        image_ids = []
        for i in range(2):
            image = self.create_test_image(100, 100, 2, 3, 4)    # x,y,z,c,t
            image_ids.append(ode.rtypes.rlong(image.id.val))

        args = {
            "Data_Type": ode.rtypes.rstring("Image"),
            "IDs": ode.rtypes.rlist(image_ids)
        }
        combine_img = run_script(client, script_id, args, "Combined_Image")
        # check the result
        assert combine_img is not None
        assert combine_img.getValue().id.val > 0

    @pytest.mark.parametrize("image_stack", [True, False])
    def test_images_from_rois(self, image_stack):
        script_id = super(TestUtilScripts, self).get_script(images_from_rois)
        assert script_id > 0
        # root session is root.sf
        session = self.root.sf
        client = self.root

        size_x = 100
        size_y = 100
        size_z = 5
        image = self.create_test_image(size_x, size_y, size_z, 1, 1)
        image_id = image.id.val
        image_ids = []
        image_ids.append(ode.rtypes.rlong(image_id))

        # Add rectangle
        roi = ode.model.RoiI()
        roi.setImage(ode.model.ImageI(image_id, False))
        rect = ode.model.RectangleI()
        rect.x = ode.rtypes.rdouble(0)
        rect.y = ode.rtypes.rdouble(0)
        rect.width = ode.rtypes.rdouble(size_x / 2)
        rect.height = ode.rtypes.rdouble(size_y / 2)
        roi.addShape(rect)
        session.getUpdateService().saveAndReturnObject(roi)
        args = {
            "Data_Type": ode.rtypes.rstring("Image"),
            "IDs": ode.rtypes.rlist(image_ids),
            "Make_Image_Stack": ode.rtypes.rbool(image_stack)
        }
        img_from_rois = run_script(client, script_id, args, "Result")
        # check the result
        assert img_from_rois is not None
        new_img = img_from_rois.getValue()
        new_size_z = new_img.getPrimaryPixels().getSizeZ().getValue()
        # From a single ROI (without theZ) on Z-stack image...
        # Should get a single Z image from single ROI if Make_Image_Stack
        if image_stack:
            assert new_size_z == 1
        else:
            # Otherwise we use all planes of input image
            assert new_size_z == size_z

    @pytest.mark.parametrize('image_names', [
        ["A1", "A2", "A3", "B1", "B2", "B3"],
        ["1A_0", "1A_1", "2A_0", "2A_1", "1B_0", "1B_1", "2B_0", "2B_1"]])
    def test_dataset_to_plate(self, image_names):
        script_id = super(TestUtilScripts, self).get_script(dataset_to_plate)
        assert script_id > 0
        # root session is root.sf
        session = self.root.sf
        client = self.root

        # create several test images in a dataset
        dataset = self.make_dataset("dataset_to_plate-test", client=client)
        # Images will be sorted by name and assigned Column first
        image_ids = []
        for i in image_names:
            # x,y,z,c,t
            image = self.create_test_image(100, 100, 1, 1, 1, session, name=i)
            self.link(dataset, image, client=client)
            image_ids.append(image.id.val)

        # Minimum args.
        dataset_ids = [ode.rtypes.rlong(dataset.id.val)]
        args = {
            "Data_Type": wrap("Dataset"),
            "IDs": wrap(dataset_ids),
            "First_Axis_Count": wrap(3),
        }

        # With more image names, add extra args
        images_per_well = 1
        if (len(image_names)) == 8:
            images_per_well = 2
            args["Images_Per_Well"] = wrap(images_per_well)
            args["First_Axis_Count"] = wrap(2)
            args["Column_Names"] = wrap("letter")
            args["Row_Names"] = wrap("number")

        d_to_p = run_script(client, script_id, args, "New_Object")

        # check the result - load all Wells from Plate, check image IDs
        assert d_to_p is not None
        plate_id = d_to_p.getValue().id.val

        # Check names of Images matches Well position
        images_in_plate = []
        conn = ServerGateway(client_obj=client)
        plate = conn.getObject("Plate", plate_id)
        for well in plate.listChildren():
            print('well', well)
            for w in range(images_per_well):
                if images_per_well == 1:
                    name = well.getWellPos()
                else:
                    # e.g. "A1_0"
                    name = "%s_%d" % (well.getWellPos(), w)
                image = well.getImage(w)
                assert image.getName() == name
                images_in_plate.append(image.getId())
        # and all images were in the Plate
        images_in_plate.sort()
        assert images_in_plate == image_ids

    @pytest.mark.parametrize("remove", [True, False])
    @pytest.mark.parametrize("script_runner", ['user', 'admin'])
    def test_move_annotations(self, remove, script_runner):

        script_id = super(TestUtilScripts, self).get_script(move_annotations)
        assert script_id > 0

        # create new Admin and user in same group
        admin_client, admin = self.new_client_and_user(system=True)
        group = admin_client.sf.getAdminService().getEventContext().groupId
        client, user = self.new_client_and_user(group=group)
        user_id = user.id.val
        field_count = 2

        # User creates Plate and Adds annotations...
        plate = self.import_plates(client=client, fields=field_count)[0]

        well_ids = []
        for well in plate.copyWells():
            well_ids.append(well.id)
            for well_sample in well.copyWellSamples():
                image = well_sample.getImage()
                # Add annotations
                tag = ode.model.TagAnnotationI()
                tag.textValue = ode.rtypes.rstring("testTag")
                self.link(image, tag, client=client)
                comment = ode.model.CommentAnnotationI()
                comment.textValue = ode.rtypes.rstring("test Comment")
                self.link(image, comment, client=client)
                rating = ode.model.LongAnnotationI()
                rating.longValue = ode.rtypes.rlong(5)
                rating.ns = ode.rtypes.rstring(
                    ode.constants.metadata.NSINSIGHTRATING)
                self.link(image, rating, client=client)

        # Either the 'user' or 'root' will run the script
        if script_runner == 'user':
            script_runner_client = client
        else:
            script_runner_client = admin_client

        # Run script on each type, with/without removing Annotations
        for anntype in ('Tag', 'Comment', 'Rating'):
            args = {
                "Data_Type": ode.rtypes.rstring("Well"),
                "IDs": ode.rtypes.rlist(well_ids),
                "Annotation_Type": ode.rtypes.rstring(anntype),
                "Remove_Annotations_From_Images": ode.rtypes.rbool(remove)
            }
            message = run_script(script_runner_client, script_id,
                                 args, "Message")
            assert message.val == "Moved %s Annotations" % field_count

        # Check new links are owned by user
        # and remove annotations from Wells...
        query_service = client.getSession().getQueryService()
        query = ("select l from WellAnnotationLink as l"
                 " join fetch l.child as ann"
                 " join fetch l.details.owner as owner"
                 " where l.parent.id in (:ids)")
        params = ode.sys.ParametersI().addIds(well_ids)
        links = query_service.findAllByQuery(query, params)
        link_ids = [l.id.val for l in links]
        assert len(link_ids) == field_count * 3
        for l in links:
            assert l.getDetails().owner.id.val == user_id
        delete = Delete2(targetObjects={'WellAnnotationLink': link_ids})
        handle = client.sf.submit(delete)
        client.waitOnCmd(handle, loops=10, ms=500, failonerror=True,
                         failontimeout=False, closehandle=False)

        # Run again with 'All' annotations.
        args = {
            "Data_Type": ode.rtypes.rstring("Plate"),
            "IDs": ode.rtypes.rlist([plate.id]),
            "Annotation_Type": ode.rtypes.rstring("All"),
            "Remove_Annotations_From_Images": ode.rtypes.rbool(remove)
        }
        message = run_script(script_runner_client, script_id, args, "Message")
        # If we've been removing annotations above,
        # there will be None left to move
        if remove:
            expected = "No annotations moved. See info."
        else:
            expected = "Moved %s Annotations" % (field_count * 3)
        assert message.val == expected

        # Run again - None moved since Annotations are already on Well
        message = run_script(script_runner_client, script_id, args, "Message")
        assert message.val == "No annotations moved. See info."
