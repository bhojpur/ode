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
This script is the second Kymograph script, for analyzing lines drawn on
kymograph images that have been created by the 'Kymograph.py' Script.
"""

from ode.gateway import ServerGateway
import ode
from ode.rtypes import rlong, rstring, robject
from ode.model import ImageAnnotationLinkI, ImageI
import ode.scripts as scripts
import ode.util.script_utils as script_utils
import ode.util.roi_handling_utils as roi_utils
import logging

logger = logging.getLogger('kymograph_analysis')

def process_images(conn, script_params):

    file_anns = []
    message = ""
    # Get the images
    images, log_message = script_utils.get_objects(conn, script_params)
    message += log_message
    if not images:
        return None, message
    # Check for line and polyline ROIs and filter images list
    images = [image for image in images if
              image.getROICount(["Polyline", "Line"]) > 0]
    if not images:
        message += "No ROI containing line or polyline was found."
        return None, message

    csv_data = []

    for image in images:

        if image.getSizeT() > 1:
            message += "%s ID: %s appears to be a time-lapse Image," \
                " not a kymograph." % (image.getName(), image.getId())
            continue

        roi_service = conn.getRoiService()
        result = roi_service.findByImage(image.getId(), None)

        secs_per_pixel_y = image.getPixelSizeY()
        microns_per_pixel_x = image.getPixelSizeX()
        if secs_per_pixel_y and microns_per_pixel_x:
            microns_per_sec = microns_per_pixel_x / secs_per_pixel_y
        else:
            microns_per_sec = None

        # for each line or polyline, create a row in csv table: y(t), x,
        # dy(dt), dx, x/t (line), x/t (average)
        col_names = "\nt_start (pixels), x_start (pixels), t_end (pixels)," \
            " x_end (pixels), dt (pixels), dx (pixels), x/t, speed(um/sec)," \
            "avg x/t, avg speed(um/sec)"
        table_data = ""
        for roi in result.rois:
            for s in roi.copyShapes():
                if s is None:
                    continue    # seems possible in some situations
                if type(s) == ode.model.LineI:
                    table_data += "\nLine ID: %s" % s.getId().getValue()
                    x1 = s.getX1().getValue()
                    x2 = s.getX2().getValue()
                    y1 = s.getY1().getValue()
                    y2 = s.getY2().getValue()
                    dx = abs(x1-x2)
                    dy = abs(y1-y2)
                    dx_per_y = float(dx)/dy
                    speed = ""
                    if microns_per_sec:
                        speed = dx_per_y * microns_per_sec
                    table_data += "\n"
                    table_data += ",".join(
                        [str(x) for x in (y1, x1, y2, x2, dy, dx, dx_per_y,
                                          speed)])

                elif type(s) == ode.model.PolylineI:
                    table_data += "\nPolyline ID: %s" % s.getId().getValue()
                    v = s.getPoints().getValue()
                    points = roi_utils.points_string_to_xy_list(v)
                    x_start, y_start = points[0]
                    for i in range(1, len(points)):
                        x1, y1 = points[i-1]
                        x2, y2 = points[i]
                        dx = abs(x1-x2)
                        dy = abs(y1-y2)
                        dx_per_y = float(dx)/dy
                        av_x_per_y = abs(float(x2-x_start)/(y2-y_start))
                        speed = ""
                        avg_speed = ""
                        if microns_per_sec:
                            speed = dx_per_y * microns_per_sec
                            avg_speed = av_x_per_y * microns_per_sec
                        table_data += "\n"
                        table_data += ",".join(
                            [str(x) for x in (y1, x1, y2, x2, dy, dx, dx_per_y,
                                              speed, av_x_per_y, avg_speed)])

        # write table data to csv...
        if len(table_data) > 0:
            table_string = "Image ID:, %s," % image.getId()
            table_string += "Name:, %s" % image.getName()
            table_string += "\nsecsPerPixelY: %s" % secs_per_pixel_y
            table_string += '\nmicronsPerPixelX: %s' % microns_per_pixel_x
            table_string += "\n"
            table_string += col_names
            table_string += table_data
            csv_data.append(table_string)

    iids = [str(i.getId()) for i in images]
    to_link_csv = [i.getId() for i in images if i.canAnnotate()]
    csv_file_name = 'kymograph_velocities_%s.csv' % "-".join(iids)
    with open(csv_file_name, 'w') as csv_file:
        csv_file.write("\n \n".join(csv_data))

    file_ann = conn.createFileAnnfromLocalFile(csv_file_name,
                                               mimetype="text/csv")
    fa_message = "Created Line Plot csv (Excel) file"

    links = []
    if len(to_link_csv) == 0:
        fa_message += " but could not attach to images."
    for iid in to_link_csv:
        link = ImageAnnotationLinkI()
        link.parent = ImageI(iid, False)
        link.child = file_ann._obj
        links.append(link)
    if len(links) > 0:
        links = conn.getUpdateService().saveAndReturnArray(links)

    if file_ann:
        file_anns.append(file_ann)

    if not file_anns:
        fa_message = "No Analysis files created. See 'Info' or 'Error'" \
            " for more details"
    elif len(file_anns) > 1:
        fa_message = "Created %s csv (Excel) files" % len(file_anns)
    message += fa_message
    return file_anns, message


def run_script():
    """
    The main entry point of the script, as called by the client via the
    scripting service, passing the required parameters.
    """
    data_types = [rstring('Image')]

    client = scripts.client(
        'Kymograph_Analysis.py',
        """This script analyzes Kymograph images, which have Line or \
PolyLine ROIs that track moving objects. It generates a table of the speed \
of movement, saved as an Excel / CSV file.""",

        scripts.String(
            "Data_Type", optional=False, grouping="1",
            description="Choose source of images (only Image supported)",
            values=data_types, default="Image"),

        scripts.List(
            "IDs", optional=False, grouping="2",
            description="List of Image IDs to process.").ofType(rlong(0)),

        version="1.0.0",
        authors=["Bhojpur ODE Team"],
        institutions=["Bhojpur Consulting Private Limited, India"],
        contact="product@bhojpur-consulting.com",
    )

    try:
        script_params = client.getInputs(unwrap=True)

        # wrap client to use the Server Gateway
        conn = ServerGateway(client_obj=client)

        file_anns, message = process_images(conn, script_params)

        if file_anns:
            if len(file_anns) == 1:
                client.setOutput("Line_Data", robject(file_anns[0]._obj))
        client.setOutput("Message", rstring(message))

    finally:
        client.closeSession()


if __name__ == "__main__":
    run_script()
