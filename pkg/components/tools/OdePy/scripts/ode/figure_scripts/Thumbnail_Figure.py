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
This script displays a bunch of thumbnails from Bhojpur ODE as a jpg or png, saved
back to the server as a FileAnnotation attached to the parent dataset or project.
"""

import ode.scripts as scripts
from ode.gateway import ServerGateway
import ode.util.script_utils as script_utils
from ode.rtypes import rlong, rstring, robject
import ode.util.image_utils as image_utils
from ode.constants.namespaces import NSCREATED
import os

try:
    from PIL import Image, ImageDraw  # see ticket:2597
except ImportError:
    import Image
    import ImageDraw  # see ticket:2597

WHITE = (255, 255, 255)

log_lines = []    # make a log / legend of the figure


def log(text):
    """
    Adds lines of text to the log_lines list, so they can be collected into a
    figure legend.
    """
    try:
        text = text.encode('utf8')
    except UnicodeEncodeError:
        pass
    log_lines.append(text)


def sort_images_by_tag(tag_ids, img_tags):

    # prepare list of {'iid': imgId, 'tagKey' : stringToSort }
    # E.g. if tag_ids = [5, 3, 9], we map to 'a', 'b', 'c',
    # so an Image with tags 3 & 9 will have 'tagKey': "bc"
    letters = 'abcdefghijklmnopqrstuvwxyz'
    # assume we have less than 26 tags!
    sorted_images = []
    for iid, tag_id_list in img_tags.items():
        ordered_indexes = []
        ordered_tags = []
        for i, tid in enumerate(tag_ids):
            if tid in tag_id_list:
                ordered_indexes.append(letters[i])
                ordered_tags.append(tid)
        if len(ordered_indexes) > 0:
            tag_key = "".join(ordered_indexes)
        else:
            tag_key = "z"
        sorted_images.append({
            'iid': iid,
            'tagKey': tag_key,
            'tagIds': ordered_tags})

    sorted_images.sort(key=lambda x: x['tagKey'])

    # clean up our 'z' sorting hack above.
    for i in sorted_images:
        if i['tagKey'] == "z":
            i['tagKey'] = ""

    return sorted_images


def paint_dataset_canvas(conn, images, title, tag_ids=None,
                         show_untagged=False, col_count=10, length=100):
    """
        Paints and returns a canvas of thumbnails from images, laid out in a
        set number of columns.
        Title and date-range of the images is printed above the thumbnails,
        to the left and right, respectively.

        @param conn:        Server connection
        @param image:       Image IDs
        @param title:       title to display at top of figure. String
        @param tag_ids:     Optional to sort thumbnails by tag. [long]
        @param col_count:    Max number of columns to lay out thumbnails
        @param length:      Length of longest side of thumbnails
    """

    mode = "RGB"
    fig_canvas = None
    spacing = length/40 + 2

    thumbnail_store = conn.createThumbnailStore()
    metadata_service = conn.getMetadataService()

    if len(images) == 0:
        return None
    timestamp_min = images[0].getDate()   # datetime
    timestamp_max = timestamp_min

    ds_image_ids = []
    image_pixel_map = {}
    image_names = {}

    # sort the images by name
    images.sort(key=lambda x: (x.getName().lower()))

    for image in images:
        image_id = image.getId()
        pixel_id = image.getPrimaryPixels().getId()
        name = image.getName()
        ds_image_ids.append(image_id)        # make a list of image-IDs
        # and a map of image-ID: pixel-ID
        image_pixel_map[image_id] = pixel_id
        image_names[image_id] = name
        timestamp_min = min(timestamp_min, image.getDate())
        timestamp_max = max(timestamp_max, image.getDate())

    # set-up fonts
    fontsize = length/7 + 5
    font = image_utils.get_font(fontsize)
    text_height = font.getsize("Textq")[1]
    top_spacer = spacing + text_height
    left_spacer = spacing + text_height

    tag_panes = []
    max_width = 0
    total_height = top_spacer

    # if we have a list of tags, then sort images by tag
    if tag_ids:
        # Cast to int since List can be any type
        tag_ids = [int(tag_id) for tag_id in tag_ids]
        log(" Sorting images by tags: %s" % tag_ids)
        tag_names = {}
        tagged_images = {}    # a map of tagId: list-of-image-Ids
        img_tags = {}        # a map of imgId: list-of-tagIds
        for tag_id in tag_ids:
            tagged_images[tag_id] = []

        # look for images that have a tag
        types = ["ode.model.annotations.TagAnnotation"]
        annotations = metadata_service.loadAnnotations(
            "Image", ds_image_ids, types, None, None)
        # filter images by annotation...
        for image_id, tags in annotations.items():
            img_tag_ids = []
            for tag in tags:
                tag_id = tag.getId().getValue()
                # make a dict of tag-names
                val = tag.getTextValue().getValue()
                tag_names[tag_id] = val.decode('utf8')
                img_tag_ids.append(tag_id)
            img_tags[image_id] = img_tag_ids

        # get a sorted list of {'iid': iid, 'tagKey': tagKey,
        # 'tagIds':orderedTags}
        sorted_thumbs = sort_images_by_tag(tag_ids, img_tags)

        if not show_untagged:
            sorted_thumbs = [t for t in sorted_thumbs if len(t['tagIds']) > 0]

        # Need to group sets of thumbnails by FIRST tag.
        toptag_sets = []
        grouped_pixel_ids = []
        show_subset_labels = False
        current_tag_str = None
        for i, img in enumerate(sorted_thumbs):
            tag_ids = img['tagIds']
            if len(tag_ids) == 0:
                tag_string = "Not Tagged"
            else:
                tag_string = tag_names[tag_ids[0]]
            if tag_string == current_tag_str or current_tag_str is None:
                # only show subset labels (later) if there are more than 1
                # subset
                if (len(tag_ids) > 1):
                    show_subset_labels = True
                grouped_pixel_ids.append({
                    'pid': image_pixel_map[img['iid']],
                    'tagIds': tag_ids})
            else:
                toptag_sets.append({
                    'tagText': current_tag_str,
                    'pixelIds': grouped_pixel_ids,
                    'showSubsetLabels': show_subset_labels})
                show_subset_labels = len(tag_ids) > 1
                grouped_pixel_ids = [{
                    'pid': image_pixel_map[img['iid']],
                    'tagIds': tag_ids}]
            current_tag_str = tag_string
        toptag_sets.append({
            'tagText': current_tag_str,
            'pixelIds': grouped_pixel_ids,
            'showSubsetLabels': show_subset_labels})

        # Find the indent we need
        max_tag_name_width = max([font.getsize(ts['tagText'])[0]
                                 for ts in toptag_sets])
        if show_untagged:
            max_tag_name_width = max(max_tag_name_width,
                                     font.getsize("Not Tagged")[0])

        tag_sub_panes = []

        # make a canvas for each tag combination
        def make_tagset_canvas(tag_string, tagset_pix_ids, show_subset_labels):
            log(" Tagset: %s  (contains %d images)"
                % (tag_string, len(tagset_pix_ids)))
            if not show_subset_labels:
                tag_string = None
            sub_canvas = image_utils.paint_thumbnail_grid(
                thumbnail_store, length,
                spacing, tagset_pix_ids, col_count, top_label=tag_string)
            tag_sub_panes.append(sub_canvas)

        for toptag_set in toptag_sets:
            tag_text = toptag_set['tagText']
            show_subset_labels = toptag_set['showSubsetLabels']
            image_data = toptag_set['pixelIds']
            # loop through all thumbs under TAG, grouping into subsets.
            tagset_pix_ids = []
            current_tag_str = None
            for i, img in enumerate(image_data):
                tag_ids = img['tagIds']
                pid = img['pid']
                tag_string = ", ".join([tag_names[tid] for tid in tag_ids])
                if tag_string == "":
                    tag_string = "Not Tagged"
                # Keep grouping thumbs under similar tag set (if not on the
                # last loop)
                if tag_string == current_tag_str or current_tag_str is None:
                    tagset_pix_ids.append(pid)
                else:
                    # Process thumbs added so far
                    make_tagset_canvas(current_tag_str, tagset_pix_ids,
                                       show_subset_labels)
                    # reset for next tagset
                    tagset_pix_ids = [pid]
                current_tag_str = tag_string

            make_tagset_canvas(current_tag_str, tagset_pix_ids,
                               show_subset_labels)

            max_width = max([c.size[0] for c in tag_sub_panes])
            total_height = sum([c.size[1] for c in tag_sub_panes])

            # paste them into a single canvas for each Tag

            left_spacer = 3*spacing + max_tag_name_width
            # Draw vertical line to right
            size = (left_spacer + max_width, total_height)
            tag_canvas = Image.new(mode, size, WHITE)
            p_x = left_spacer
            p_y = 0
            for pane in tag_sub_panes:
                image_utils.paste_image(pane, tag_canvas, p_x, p_y)
                p_y += pane.size[1]
            if tag_text is not None:
                draw = ImageDraw.Draw(tag_canvas)
                tt_w, tt_h = font.getsize(tag_text)
                h_offset = (total_height - tt_h)/2
                draw.text((spacing, h_offset), tag_text, font=font,
                          fill=(50, 50, 50))
            # draw vertical line
            draw.line((left_spacer-spacing, 0, left_spacer - spacing,
                       total_height), fill=(0, 0, 0))
            tag_panes.append(tag_canvas)
            tag_sub_panes = []
    else:
        left_spacer = spacing
        pixel_ids = []
        for image_id in ds_image_ids:
            log("  Name: %s  ID: %d" % (image_names[image_id], image_id))
            pixel_ids.append(image_pixel_map[image_id])
        fig_canvas = image_utils.paint_thumbnail_grid(
            thumbnail_store, length, spacing, pixel_ids, col_count)
        tag_panes.append(fig_canvas)

    # paste them into a single canvas
    tagset_spacer = length / 3
    max_width = max([c.size[0] for c in tag_panes])
    total_height = total_height + sum([c.size[1]+tagset_spacer
                                      for c in tag_panes]) - tagset_spacer
    size = (max_width, total_height)
    full_canvas = Image.new(mode, size, WHITE)
    p_x = 0
    p_y = top_spacer
    for pane in tag_panes:
        image_utils.paste_image(pane, full_canvas, p_x, p_y)
        p_y += pane.size[1] + tagset_spacer

    # create dates for the image timestamps. If dates are not the same, show
    # first - last.
    # firstdate = timestampMin
    # lastdate = timestampMax
    # figureDate = str(firstdate)
    # if firstdate != lastdate:
    #     figureDate = "%s - %s" % (firstdate, lastdate)

    draw = ImageDraw.Draw(full_canvas)
    # dateWidth = draw.textsize(figureDate, font=font)[0]
    # titleWidth = draw.textsize(title, font=font)[0]
    # dateX = fullCanvas.size[0] - spacing - dateWidth
    # title
    draw.text((left_spacer, spacing), title, font=font, fill=(0, 0, 0))
    # Don't show dates: see
    # if (leftSpacer+titleWidth) < dateX:
    # if there's enough space...
    #     draw.text((dateX, dateY), figureDate, font=font, fill=(0,0,0))
    # add date

    return full_canvas


def make_thumbnail_figure(conn, script_params):
    """
    Makes the figure using the parameters in @script_params, attaches the
    figure to the parent Project/Dataset, and returns the file-annotation ID

    @ returns       Returns the id of the originalFileLink child. (ID object,
                    not value)
    """

    log("Thumbnail figure created by Bhojpur ODE")
    log("")

    message = ""

    # Get the objects (images or datasets)
    objects, log_message = script_utils.get_objects(conn, script_params)
    message += log_message
    if not objects:
        return None, message

    # Get parent
    parent = None
    if "Parent_ID" in script_params and len(script_params["IDs"]) > 1:
        if script_params["Data_Type"] == "Image":
            parent = conn.getObject("Dataset", script_params["Parent_ID"])
        else:
            parent = conn.getObject("Project", script_params["Parent_ID"])

    if parent is None:
        parent = objects[0]  # Attach figure to the first object

    parent_class = parent.ODE_CLASS
    log("Figure will be linked to %s%s: %s"
        % (parent_class[0].lower(), parent_class[1:], parent.getName()))

    tag_ids = []
    if "Tag_IDs" in script_params:
        tag_ids = script_params['Tag_IDs']
    if len(tag_ids) == 0:
        tag_ids = None

    show_untagged = False
    if (tag_ids):
        show_untagged = script_params["Show_Untagged_Images"]

    thumb_size = script_params["Thumbnail_Size"]
    max_columns = script_params["Max_Columns"]

    fig_height = 0
    fig_width = 0
    ds_canvases = []

    if script_params["Data_Type"] == "Dataset":
        for dataset in objects:
            log("Dataset: %s     ID: %d"
                % (dataset.getName(), dataset.getId()))
            images = list(dataset.listChildren())
            title = dataset.getName().decode('utf8')
            ds_canvas = paint_dataset_canvas(
                conn, images, title, tag_ids, show_untagged,
                length=thumb_size, col_count=max_columns)
            if ds_canvas is None:
                continue
            ds_canvases.append(ds_canvas)
            fig_height += ds_canvas.size[1]
            fig_width = max(fig_width, ds_canvas.size[0])
    else:
        image_canvas = paint_dataset_canvas(
            conn, objects, "", tag_ids,
            show_untagged, length=thumb_size, col_count=max_columns)
        ds_canvases.append(image_canvas)
        fig_height += image_canvas.size[1]
        fig_width = max(fig_width, image_canvas.size[0])

    if len(ds_canvases) == 0:
        message += "No figure created"
        return None, message

    figure = Image.new("RGB", (fig_width, fig_height), WHITE)
    y = 0
    for ds in ds_canvases:
        image_utils.paste_image(ds, figure, 0, y)
        y += ds.size[1]

    log("")
    fig_legend = "\n".join(log_lines)

    format = script_params["Format"]
    figure_name = script_params["Figure_Name"]
    figure_name = os.path.basename(figure_name)
    output = "localfile"

    if format == 'PNG':
        output = output + ".png"
        figure_name = figure_name + ".png"
        figure.save(output, "PNG")
        mimetype = "image/png"
    elif format == 'TIFF':
        output = output + ".tiff"
        figure_name = figure_name + ".tiff"
        figure.save(output, "TIFF")
        mimetype = "image/tiff"
    else:
        output = output + ".jpg"
        figure_name = figure_name + ".jpg"
        figure.save(output)
        mimetype = "image/jpeg"

    namespace = NSCREATED + "/ode/figure_scripts/Thumbnail_Figure"
    file_annotation, fa_message = script_utils.create_link_file_annotation(
        conn, output, parent, output="Thumbnail figure", mimetype=mimetype,
        description=fig_legend, namespace=namespace,
        orig_file_path_and_name=figure_name)
    message += fa_message

    return file_annotation, message


def run_script():
    """
    The main entry point of the script. Gets the parameters from the scripting
    service, makes the figure and returns the output to the client.
    def __init__(self, name, optional = False, out = False, description =
    None, type = None, min = None, max = None, values = None)
    """

    formats = [rstring('JPEG'), rstring('PNG'), rstring('TIFF')]
    data_types = [rstring('Dataset'), rstring('Image')]

    client = scripts.client(
        'Thumbnail_Figure.py',
        """Export a figure of thumbnails, optionally sorted by tag.
See http://help.bhojpur.net/publish.html#figures""",

        scripts.String(
            "Data_Type", optional=False, grouping="1",
            description="The data you want to work with.",
            values=data_types, default="Dataset"),

        scripts.List(
            "IDs", optional=False, grouping="2",
            description="List of Dataset IDs or Image"
            " IDs").ofType(rlong(0)),

        scripts.List(
            "Tag_IDs", grouping="3",
            description="Group thumbnails by these tags."),

        scripts.Bool(
            "Show_Untagged_Images", grouping="3.1", default=False,
            description="If true (and you're sorting by tagIds) also"
            " show images without the specified tags"),

        scripts.Long(
            "Parent_ID", grouping="4",
            description="Attach figure to this Project (if datasetIds"
            " above) or Dataset if imageIds. If not specified, attach"
            " figure to first dataset or image."),
        # this will be ignored if only a single ID in list - attach to
        # that object instead.

        scripts.Int(
            "Thumbnail_Size", grouping="5", min=10, max=250, default=100,
            description="The dimension of each thumbnail. Default is 100"),

        scripts.Int(
            "Max_Columns", grouping="5.1", min=1, default=10,
            description="The max number of thumbnail columns. Default is 10"),

        scripts.String(
            "Format", grouping="6",
            description="Format to save image.", values=formats,
            default="JPEG"),

        scripts.String(
            "Figure_Name", grouping="6.1", default='Thumbnail_Figure',
            description="File name of figure to create"),

        version="1.0.0",
        authors=["Bhojpur ODE Team"],
        institutions=["Bhojpur Consulting Private Limited, India"],
        contact="product@bhojpur-consulting.com",
        )

    try:
        conn = ServerGateway(client_obj=client)

        command_args = client.getInputs(unwrap=True)

        # Makes the figure and attaches it to Project/Dataset. Returns
        # FileAnnotationI object
        file_annotation, message = make_thumbnail_figure(conn, command_args)

        # Return message and file annotation (if applicable) to the client
        client.setOutput("Message", rstring(message))
        if file_annotation is not None:
            client.setOutput("File_Annotation", robject(file_annotation._obj))
    finally:
        client.closeSession()


if __name__ == "__main__":
    run_script()
