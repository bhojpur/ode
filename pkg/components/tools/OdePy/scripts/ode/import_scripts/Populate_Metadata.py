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

'''
Populate metadata from CSV.
'''

import ode
from ode.gateway import ServerGateway
from ode.rtypes import rstring, rlong
import ode.scripts as scripts
import ode.model

import sys

from ode.util.populate_roi import DownloadingOriginalFileProvider
try:
    from ode_metadata.populate import ParsingContext
    OBJECT_TYPES = (
        'Plate',
        'Screen',
        'Dataset',
        'Project',
    )
    DEPRECATED = ""

except ImportError:
    from ode.util.populate_metadata import ParsingContext
    OBJECT_TYPES = (
        'Plate',
        'Screen',
    )
    DEPRECATED = """

    Warning: This script is using an outdated metadata plugin.
    Ask your administrator to install the ode-metadata plugin
    for additional features: https://pypi.org/project/ode-metadata/
    """


def get_original_file(conn, object_type, object_id, file_ann_id=None):
    if object_type not in OBJECT_TYPES:
        sys.stderr.write("Error: Invalid object type: %s.\n" % object_type)
        sys.exit(1)
    ode_object = conn.getObject(object_type, int(object_id))
    if ode_object is None:
        sys.stderr.write("Error: %s does not exist.\n" % object_type)
        sys.exit(1)
    file_ann = None

    for ann in ode_object.listAnnotations():
        if isinstance(ann, ode.gateway.FileAnnotationWrapper):
            file_name = ann.getFile().getName()
            # Pick file by Ann ID (or name if ID is None)
            if (file_ann_id is None and file_name.endswith(".csv")) or (
                    ann.getId() == file_ann_id):
                file_ann = ann
    if file_ann is None:
        sys.stderr.write("Error: File does not exist.\n")
        sys.exit(1)

    return file_ann.getFile()._obj


def populate_metadata(client, conn, script_params):
    object_ids = script_params["IDs"]
    object_id = object_ids[0]
    file_ann_id = None
    if "File_Annotation" in script_params:
        file_ann_id = long(script_params["File_Annotation"])
    data_type = script_params["Data_Type"]
    original_file = get_original_file(
        conn, data_type, object_id, file_ann_id)
    provider = DownloadingOriginalFileProvider(conn)
    data_for_preprocessing = provider.get_original_file_data(original_file)
    data = provider.get_original_file_data(original_file)
    objecti = getattr(ode.model, data_type + 'I')
    ode_object = objecti(long(object_id), False)
    ctx = ParsingContext(client, ode_object, "")

    try:
        # Old
        ctx.parse_from_handle(data)
        ctx.write_to_ode()
    except AttributeError:
        # ode-metadata >= 0.3.0
        ctx.preprocess_from_handle(data_for_preprocessing)
        ctx.parse_from_handle_stream(data)
    return "Table data populated for %s: %s" % (data_type, object_id)


def run_script():

    data_types = [rstring(otype) for otype in OBJECT_TYPES]
    client = scripts.client(
        'Populate_Metadata.py',
        """
    This script processes a csv file, attached to a container,
    converting it to a Bhojpur ODE.table, with one row per Image or Well.
    The table data can then be displayed in the Bhojpur ODE clients.
    For full details, see
    http://help.bhojpur.net/scripts.html#metadata
        """ + DEPRECATED,
        scripts.String(
            "Data_Type", optional=False, grouping="1",
            description="Choose source of images",
            values=data_types, default=OBJECT_TYPES[0]),

        scripts.List(
            "IDs", optional=False, grouping="2",
            description="Container ID.").ofType(rlong(0)),

        scripts.String(
            "File_Annotation", grouping="3",
            description="File Annotation ID containing metadata to populate. "
            "Note this is not the same as the File ID."),

        authors=["Bhojpur ODE Team"],
        institutions=["Bhojpur Consulting Private Limited, India"],
        contact="product@bhojpur-consulting.com",
    )

    try:
        # process the list of args above.
        script_params = {}
        for key in client.getInputKeys():
            if client.getInput(key):
                script_params[key] = client.getInput(key, unwrap=True)

        # wrap client to use the Server Gateway
        conn = ServerGateway(client_obj=client)
        message = populate_metadata(client, conn, script_params)
        client.setOutput("Message", rstring(message))

    finally:
        client.closeSession()


if __name__ == "__main__":
    run_script()
