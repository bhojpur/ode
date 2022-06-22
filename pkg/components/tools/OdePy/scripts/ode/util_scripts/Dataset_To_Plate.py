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
This script converts a Dataset of Images to a Plate, with one image per Well.
"""

import ode.scripts as scripts
from ode.gateway import ServerGateway
import ode.util.script_utils as script_utils
import ode

from ode.rtypes import rint, rlong, rstring, robject, unwrap

def add_images_to_plate(conn, images, plate_id, column, row, remove_from=None):
    """
    Add the Images to a Plate, creating a new well at the specified column and
    row
    NB - This will fail if there is already a well at that point
    """
    update_service = conn.getUpdateService()

    well = ode.model.WellI()
    well.plate = ode.model.PlateI(plate_id, False)
    well.column = rint(column)
    well.row = rint(row)

    try:
        for image in images:
            ws = ode.model.WellSampleI()
            ws.image = ode.model.ImageI(image.id, False)
            ws.well = well
            well.addWellSample(ws)
        update_service.saveObject(well)
    except Exception:
        return False

    # remove from Datast
    for image in images:
        if remove_from is not None:
            links = list(image.getParentLinks(remove_from.id))
            for l in links:
                conn.deleteObjectDirect(l._obj)
    return True


def dataset_to_plate(conn, script_params, dataset_id, screen):

    dataset = conn.getObject("Dataset", dataset_id)
    if dataset is None:
        return

    update_service = conn.getUpdateService()

    # create Plate
    plate = ode.model.PlateI()
    plate.name = ode.rtypes.RStringI(dataset.name)
    plate.columnNamingConvention = rstring(str(script_params["Column_Names"]))
    # 'letter' or 'number'
    plate.rowNamingConvention = rstring(str(script_params["Row_Names"]))
    plate = update_service.saveAndReturnObject(plate)

    if screen is not None and screen.canLink():
        link = ode.model.ScreenPlateLinkI()
        link.parent = ode.model.ScreenI(screen.id, False)
        link.child = ode.model.PlateI(plate.id.val, False)
        update_service.saveObject(link)
    else:
        link = None

    row = 0
    col = 0

    first_axis_is_row = script_params["First_Axis"] == 'row'
    axis_count = script_params["First_Axis_Count"]

    # sort images by name
    images = list(dataset.listChildren())
    dataset_img_count = len(images)
    if "Filter_Names" in script_params:
        filter_by = script_params["Filter_Names"]
        images = [i for i in images if i.getName().find(filter_by) >= 0]
    images.sort(key=lambda x: x.name.lower())

    # Do we try to remove images from Dataset and Delte Datset when/if empty?
    remove_from = None
    remove_dataset = "Remove_From_Dataset" in script_params and \
        script_params["Remove_From_Dataset"]
    if remove_dataset:
        remove_from = dataset

    images_per_well = script_params["Images_Per_Well"]
    image_index = 0

    while image_index < len(images):

        well_images = images[image_index: image_index + images_per_well]
        added_count = add_images_to_plate(conn, well_images,
                                          plate.getId().getValue(),
                                          col, row, remove_from)
        image_index += images_per_well

        # update row and column index
        if first_axis_is_row:
            row += 1
            if row >= axis_count:
                row = 0
                col += 1
        else:
            col += 1
            if col >= axis_count:
                col = 0
                row += 1

    # if user wanted to delete dataset, AND it's empty we can delete dataset
    delete_dataset = False   # Turning this functionality off for now.
    delete_handle = None
    if delete_dataset:
        if dataset_img_count == added_count:
            dcs = list()
            options = None  # {'/Image': 'KEEP'}    # don't delete the images!
            dcs.append(ode.api.delete.DeleteCommand(
                "/Dataset", dataset.id, options))
            delete_handle = conn.getDeleteService().queueDelete(dcs)
    return plate, link, delete_handle


def datasets_to_plates(conn, script_params):

    update_service = conn.getUpdateService()

    message = ""

    # Get the datasets ID
    datasets, log_message = script_utils.get_objects(conn, script_params)
    message += log_message

    def has_images_linked_to_well(dataset):
        params = ode.sys.ParametersI()
        query = "select count(well) from Well as well "\
                "left outer join well.wellSamples as ws " \
                "left outer join ws.image as img "\
                "where img.id in (:ids)"
        params.addIds([i.getId() for i in dataset.listChildren()])
        n_wells = unwrap(conn.getQueryService().projection(
            query, params, conn.SERVICE_OPTS)[0])[0]
        if n_wells > 0:
            return True
        else:
            return False

    # Exclude datasets containing images already linked to a well
    n_datasets = len(datasets)
    datasets = [x for x in datasets if not has_images_linked_to_well(x)]
    if len(datasets) < n_datasets:
        message += "Excluded %s out of %s dataset(s). " \
            % (n_datasets - len(datasets), n_datasets)

    # Return if all input dataset are not found or excluded
    if not datasets:
        return None, message

    # Filter dataset IDs by permissions
    ids = [ds.getId() for ds in datasets if ds.canLink()]
    if len(ids) != len(datasets):
        perm_ids = [str(ds.getId()) for ds in datasets if not ds.canLink()]
        message += "You do not have the permissions to add the images from"\
            " the dataset(s): %s." % ",".join(perm_ids)
    if not ids:
        return None, message

    # find or create Screen if specified
    screen = None
    newscreen = None
    if "Screen" in script_params and len(script_params["Screen"]) > 0:
        s = script_params["Screen"]
        # see if this is ID of existing screen
        try:
            screen_id = long(s)
            screen = conn.getObject("Screen", screen_id)
        except ValueError:
            pass
        # if not, create one
        if screen is None:
            newscreen = ode.model.ScreenI()
            newscreen.name = rstring(s)
            newscreen = update_service.saveAndReturnObject(newscreen)
            screen = conn.getObject("Screen", newscreen.getId().getValue())

    plates = []
    links = []
    deletes = []
    for dataset_id in ids:
        plate, link, delete_handle = dataset_to_plate(conn, script_params,
                                                      dataset_id, screen)
        if plate is not None:
            plates.append(plate)
        if link is not None:
            links.append(link)
        if delete_handle is not None:
            deletes.append(delete_handle)

    # wait for any deletes to finish
    for handle in deletes:
        cb = ode.callbacks.DeleteCallbackI(conn.c, handle)
        while True:  # ms
            if cb.block(100) is not None:
                break

    if newscreen:
        message += "New screen created: %s." % newscreen.getName().getValue()
        robj = newscreen
    elif plates:
        robj = plates[0]
    else:
        robj = None

    if plates:
        if len(plates) == 1:
            plate = plates[0]
            message += " New plate created: %s" % plate.getName().getValue()
        else:
            message += " %s plates created" % len(plates)
        if len(plates) == len(links):
            message += "."
        else:
            message += " but could not be attached."
    else:
        message += "No plate created."
    return robj, message


def run_script():
    """
    The main entry point of the script, as called by the client via the
    scripting service, passing the required parameters.
    """

    data_types = [rstring('Dataset')]
    first_axis = [rstring('column'), rstring('row')]
    row_col_naming = [rstring('letter'), rstring('number')]

    client = scripts.client(
        'Dataset_To_Plate.py',
        """Take a Dataset of Images and put them in a new Plate, \
arranging them into rows or columns as desired.
Optionally add the Plate to a new or existing Screen.
See http://help.bhojpur.net/scripts.html""",

        scripts.String(
            "Data_Type", optional=False, grouping="1",
            description="Choose source of images (only Dataset supported)",
            values=data_types, default="Dataset"),

        scripts.List(
            "IDs", optional=False, grouping="2",
            description="List of Dataset IDs to convert to new"
            " Plates.").ofType(rlong(0)),

        scripts.String(
            "Filter_Names", grouping="2.1",
            description="Filter the images by names that contain this value"),

        scripts.String(
            "First_Axis", grouping="3", optional=False, default='column',
            values=first_axis,
            description="""Arrange images accross 'column' first or down"
            " 'row'"""),

        scripts.Int(
            "First_Axis_Count", grouping="3.1", optional=False, default=12,
            description="Number of Rows or Columns in the 'First Axis'",
            min=1),

        scripts.Int(
            "Images_Per_Well", grouping="3.2", optional=False, default=1,
            description="Number of Images (Well Samples) per Well",
            min=1),

        scripts.String(
            "Column_Names", grouping="4", optional=False, default='number',
            values=row_col_naming,
            description="""Name plate columns with 'number' or 'letter'"""),

        scripts.String(
            "Row_Names", grouping="5", optional=False, default='letter',
            values=row_col_naming,
            description="""Name plate rows with 'number' or 'letter'"""),

        scripts.String(
            "Screen", grouping="6",
            description="Option: put Plate(s) in a Screen. Enter Name of new"
            " screen or ID of existing screen"""),

        scripts.Bool(
            "Remove_From_Dataset", grouping="7", default=True,
            description="Remove Images from Dataset as they are added to"
            " Plate"),

        version="1.0.0",
        authors=["Bhojpur ODE Team"],
        institutions=["Bhojpur Consulting Private Limited, India"],
        contact="product@bhojpur-consulting.com",
    )

    try:
        script_params = client.getInputs(unwrap=True)

        # wrap client to use the Server Gateway
        conn = ServerGateway(client_obj=client)

        # convert Dataset(s) to Plate(s). Returns new plates or screen
        new_obj, message = datasets_to_plates(conn, script_params)

        client.setOutput("Message", rstring(message))
        if new_obj:
            client.setOutput("New_Object", robject(new_obj))

    finally:
        client.closeSession()


if __name__ == "__main__":
    run_script()
