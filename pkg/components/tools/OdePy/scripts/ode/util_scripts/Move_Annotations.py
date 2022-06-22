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

"""Moves Annotations from Images to their parent Wells."""

import ode.scripts as scripts
from ode.gateway import ServerGateway
from ode.model import ExperimenterI, \
    WellAnnotationLinkI, \
    WellI
from ode.cmd import Delete2
from ode.sys import ParametersI, Filter
from ode.rtypes import rstring, rlong, robject
from ode.constants.metadata import NSINSIGHTRATING

ANN_TYPES = {
    'Tag': 'TagAnnotationI',
    'File': 'FileAnnotationI',
    'Comment': 'CommentAnnotationI',
    'Rating': 'LongAnnotationI',
    'Key-Value': 'MapAnnotationI'
}

def log(text):
    """Handle logging statements in a single place."""
    print(text)

def move_well_annotations(conn, well, ann_type, remove_anns, ns):
    """Move annotations from Images in this Well onto the Well itself."""
    log("Processing Well: %s %s" % (well.id, well.getWellPos()))
    iids = [well_sample.getImage().id for well_sample in well.listChildren()]
    log("  Image IDs: %s" % iids)
    if len(iids) == 0:
        return 0

    # Params to query links. If not Admin, only work with our own links
    params = ParametersI()
    if not conn.isAdmin():
        params.theFilter = Filter()
        params.theFilter.ownerId = rlong(conn.getUserId())

    old_links = list(conn.getAnnotationLinks('Image', iids,
                                             ns=ns, params=params))

    # Filter by type
    old_links = [l for l in old_links
                 if (ann_type is None
                     or (l.child.__class__.__name__ == ann_type))]

    link_ids = [l.id for l in old_links]

    def get_key(ann_link, with_owner=False):
        # We use ann's 'key' to avoid adding duplicate annotations
        # Key includes link owner (allows multiple links with different owners)
        ann = ann_link.child
        return "%s_%s" % (ann_link.details.owner.id.val, ann.id.val)

    links_dict = {}
    # Remove duplicate annotations according to get_key(l)
    for l in old_links:
        links_dict[get_key(l, conn.isAdmin())] = l

    old_links = links_dict.values()

    # Find existing links on Well so we don't try to duplicate them
    existing_well_links = list(conn.getAnnotationLinks('Well', [well.id],
                                                       ns=ns, params=params))
    existing_well_keys = [get_key(l) for l in existing_well_links]

    new_links = []
    for l in old_links:
        if get_key(l) in existing_well_keys:
            continue
        log("    Annotation: %s %s" % (l.child.id.val,
                                       l.child.__class__.__name__))
        link = WellAnnotationLinkI()
        link.parent = WellI(well.id, False)
        link.child = l.child
        # If Admin, the new link Owner is same as old link Owner
        if conn.isAdmin():
            owner_id = l.details.owner.id.val
            link.details.owner = ExperimenterI(owner_id, False)
        new_links.append(link)
    try:
        conn.getUpdateService().saveArray(new_links)
    except Exception as ex:
        log("Failed to create links: %s" % ex.message)
        return 0

    if remove_anns:
        log("Deleting ImageAnnotation links... %s" % link_ids)
        try:
            delete = Delete2(targetObjects={'ImageAnnotationLink': link_ids})
            handle = conn.c.sf.submit(delete)
            conn.c.waitOnCmd(handle, loops=10, ms=500, failonerror=True,
                             failontimeout=False, closehandle=False)
        except Exception as ex:
            log("Failed to delete links: %s" % ex.message)
    return len(new_links)


def move_annotations(conn, script_params):
    """Process script parameters and move annotations as specified."""
    plates = []
    filter_type = None

    dtype = script_params['Data_Type']
    ids = script_params['IDs']
    ann_type = script_params['Annotation_Type']
    if ann_type in ANN_TYPES:
        filter_type = ANN_TYPES[ann_type]
    if ann_type == 'Rating':
        ns = NSINSIGHTRATING
    else:
        ns = script_params.get('Namespace')
    remove_anns = script_params['Remove_Annotations_From_Images']

    # Get the Plates or Wells
    objects = list(conn.getObjects(dtype, ids))

    ann_total = 0

    if dtype == 'Well':
        for well in objects:
            ann_count = move_well_annotations(conn, well, filter_type,
                                              remove_anns, ns)
            ann_total += ann_count
    else:
        if dtype == 'Plate':
            plates = objects
        elif dtype == 'Screen':
            for screen in objects:
                plates.extend(list(screen.listChildren()))
        log("Found Plates: %s" % [p.id for p in plates])
        for plate in plates:
            for well in plate.listChildren():
                ann_count = move_well_annotations(conn, well, filter_type,
                                                  remove_anns, ns)
                ann_total += ann_count

    return objects, ann_total


def run_script():
    """The main entry point of the script."""
    data_types = [rstring('Screen'), rstring('Plate'), rstring('Well')]

    ann_types = [rstring('All')]
    ann_types.extend([rstring(k) for k in ANN_TYPES.keys()])

    client = scripts.client(
        'Move_Annotations.py',
        """
For Screen/Plate/Well data, this script moves your Annotations from Images to
their parent Wells. If you are an Admin, this will also move annotations that
other users have added, creating links that belong to the same users.
    """,

        scripts.String(
            "Data_Type", optional=False, grouping="1",
            description="The data you want to work with.", values=data_types,
            default="Plate"),

        scripts.List(
            "IDs", optional=False, grouping="2",
            description="List of Screen, Plate or Well IDs").ofType(rlong(0)),

        scripts.String(
            "Annotation_Type", grouping="3",
            description="Move all annotations OR just one type of annotation",
            values=ann_types, default='All'),

        scripts.String(
            "Namespace", grouping="4",
            description="Filter annotations by namespace"),

        scripts.Bool(
            "Remove_Annotations_From_Images", grouping="5",
            description="If false, annotations will remain linked to Images",
            default=False),

        version="1.0.0",
        authors=["Bhojpur ODE Team"],
        institutions=["Bhojpur Consulting Private Limited, India"],
        contact="product@bhojpur-consulting.com",
    )

    try:
        conn = ServerGateway(client_obj=client)

        script_params = client.getInputs(unwrap=True)
        log(script_params)

        # call the main script
        objects, anns_moved = move_annotations(conn, script_params)

        # return 'Message' to client
        message = ""
        if len(objects) == 0:
            message = ("Found no %ss with IDs: %s" %
                       (script_params['Data_Type'], script_params['IDs']))
        else:
            client.setOutput("Target", robject(objects[0]._obj))
        if anns_moved > 0:
            message = "Moved %s Annotations" % anns_moved
        else:
            message = "No annotations moved. See info."

        client.setOutput("Message", rstring(message))

    finally:
        client.closeSession()


if __name__ == "__main__":
    run_script()
