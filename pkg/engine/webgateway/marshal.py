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

import ode
import time
import re
import logging
import traceback
from future.utils import isbytes, bytes_to_native_str

from engine.rtypes import unwrap
from ode_marshal import get_encoder

logger = logging.getLogger(__name__)

# ODE.insight point list regular expression
INSIGHT_POINT_LIST_RE = re.compile(r"points\[([^\]]+)\]")

# ODE model point list regular expression
ODE_MODEL_POINT_LIST_RE = re.compile(r"([\d.]+),([\d.]+)")


def eventContextMarshal(event_context):
    """
    Marshals the ode::sys::EventContext as a dict.

    @param event_context:   ode::sys::EventContext
    @return:                Dict
    """

    ctx = {}
    for a in [
        "shareId",
        "sessionId",
        "sessionUuid",
        "userId",
        "userName",
        "sudoerId",
        "sudoerName",
        "groupId",
        "groupName",
        "isAdmin",
        "eventId",
        "eventType",
        "memberOfGroups",
        "leaderOfGroups",
        "adminPrivileges",
    ]:
        if hasattr(event_context, a):
            ctx[a] = unwrap(getattr(event_context, a))

    perms = event_context.groupPermissions
    encoder = get_encoder(perms.__class__)
    ctx["groupPermissions"] = encoder.encode(perms)

    return ctx


def channelMarshal(channel):
    """
    return a dict with all there is to know about a channel

    @param channel:     L{engine.gateway.ChannelWrapper}
    @return:            Dict
    """

    chan = {
        "emissionWave": channel.getEmissionWave(),
        "label": channel.getLabel(),
        "color": channel.getColor().getHtml(),
        # 'reverseIntensity' is deprecated. Use 'inverted'
        "inverted": channel.isInverted(),
        "reverseIntensity": channel.isInverted(),
        "family": unwrap(channel.getFamily()),
        "coefficient": unwrap(channel.getCoefficient()),
        "window": {
            "min": channel.getWindowMin(),
            "max": channel.getWindowMax(),
            "start": channel.getWindowStart(),
            "end": channel.getWindowEnd(),
        },
        "active": channel.isActive(),
    }
    lut = channel.getLut()
    if lut and len(lut) > 0:
        chan["lut"] = lut
    return chan


def imageMarshal(image, key=None, request=None):
    """
    return a dict with pretty much everything we know and care about an image,
    all wrapped in a pretty structure.

    @param image:   L{engine.gateway.ImageWrapper}
    @param key:     key of specific attributes to select
    @return:        Dict
    """

    image.loadRenderOptions()
    pr = image.getProject()
    ds = None
    wellsample = None
    well = None
    try:
        # Replicating the functionality of the deprecated
        # ImageWrapper.getDataset() with shares in mind.
        parents = image.listParents()
        if parents is not None:
            datasets = [p for p in parents if p.ODE_CLASS == "Dataset"]
            well_smpls = [p for p in parents if p.ODE_CLASS == "WellSample"]
            if len(datasets) == 1:
                ds = datasets[0]
            if len(well_smpls) == 1:
                if well_smpls[0].well is not None:
                    well = well_smpls[0].well
    except engine.SecurityViolation as e:
        # We're in a share so the Image's parent Dataset cannot be loaded
        # or some other permissions related issue has tripped us up.
        logger.warn(
            "Security violation while retrieving Dataset when "
            "marshaling image metadata: %s" % e.message
        )

    rv = {
        "id": image.id,
        "meta": {
            "imageName": image.name or "",
            "imageDescription": image.description or "",
            "imageAuthor": image.getAuthor(),
            "projectName": pr and pr.name or "Multiple",
            "projectId": pr and pr.id or None,
            "projectDescription": pr and pr.description or "",
            "datasetName": ds and ds.name or "Multiple",
            "datasetId": ds and ds.id or None,
            "datasetDescription": ds and ds.description or "",
            "wellSampleId": wellsample and wellsample.id or "",
            "wellId": well and well.id.val or "",
            "imageTimestamp": time.mktime(image.getDate().timetuple()),
            "imageId": image.id,
            "pixelsType": image.getPixelsType(),
        },
        "perms": {
            "canAnnotate": image.canAnnotate(),
            "canEdit": image.canEdit(),
            "canDelete": image.canDelete(),
            "canLink": image.canLink(),
        },
    }
    try:
        reOK = image._prepareRenderingEngine()
        if not reOK:
            logger.debug("Failed to prepare Rendering Engine for imageMarshal")
            return rv
    except engine.ConcurrencyException as ce:
        backOff = ce.backOff
        rv = {"ConcurrencyException": {"backOff": backOff}}
        return rv
    except Exception as ex:  # Handle everything else.
        rv["Exception"] = ex.message
        logger.error(traceback.format_exc())
        return rv  # Return what we have already, in case it's useful

    # big images
    levels = image._re.getResolutionLevels()
    tiles = levels > 1
    rv["tiles"] = tiles
    if tiles:
        width, height = image._re.getTileSize()
        zoomLevelScaling = image.getZoomLevelScaling()

        rv.update({"tile_size": {"width": width, "height": height}, "levels": levels})
        if zoomLevelScaling is not None:
            rv["zoomLevelScaling"] = zoomLevelScaling

    nominalMagnification = (
        image.getObjectiveSettings() is not None
        and image.getObjectiveSettings().getObjective().getNominalMagnification()
        or None
    )

    try:
        server_settings = request.session.get("server_settings", {}).get("viewer", {})
    except Exception:
        server_settings = {}
    init_zoom = server_settings.get("initial_zoom_level", 0)
    if init_zoom < 0:
        init_zoom = levels + init_zoom

    interpolate = server_settings.get("interpolate_pixels", True)

    try:

        def pixel_size_in_microns(method):
            try:
                size = method("MICROMETER")
                return size.getValue() if size else None
            except Exception:
                logger.debug(
                    "Unable to convert physical pixel size to microns", exc_info=True
                )
                return None

        rv.update(
            {
                "interpolate": interpolate,
                "size": {
                    "width": image.getSizeX(),
                    "height": image.getSizeY(),
                    "z": image.getSizeZ(),
                    "t": image.getSizeT(),
                    "c": image.getSizeC(),
                },
                "pixel_size": {
                    "x": pixel_size_in_microns(image.getPixelSizeX),
                    "y": pixel_size_in_microns(image.getPixelSizeY),
                    "z": pixel_size_in_microns(image.getPixelSizeZ),
                },
            }
        )
        if init_zoom is not None:
            rv["init_zoom"] = init_zoom
        if nominalMagnification is not None:
            rv.update({"nominalMagnification": nominalMagnification})
        try:
            rv["pixel_range"] = image.getPixelRange()
            rv["channels"] = [channelMarshal(x) for x in image.getChannels()]
            rv["split_channel"] = image.splitChannelDims()
            rv["rdefs"] = {
                "model": (image.isGreyscaleRenderingModel() and "greyscale" or "color"),
                "projection": image.getProjection(),
                "defaultZ": image._re.getDefaultZ(),
                "defaultT": image._re.getDefaultT(),
                "invertAxis": image.isInvertedAxis(),
            }
        except TypeError:
            # Will happen if an image has bad or missing pixel data
            logger.error("imageMarshal", exc_info=True)
            rv["pixel_range"] = (0, 0)
            rv["channels"] = ()
            rv["split_channel"] = ()
            rv["rdefs"] = {
                "model": "color",
                "projection": image.getProjection(),
                "defaultZ": 0,
                "defaultT": 0,
                "invertAxis": image.isInvertedAxis(),
            }
    except AttributeError:
        rv = None
        raise
    if key is not None and rv is not None:
        for k in key.split("."):
            rv = rv.get(k, {})
        if rv == {}:
            rv = None
    return rv


def shapeMarshal(shape):
    """
    return a dict with all there is to know about a shape

    @param channel:     L{engine.model.ShapeI}
    @return:            Dict
    """
    rv = {}

    def set_if(k, v, func=lambda a: a is not None):
        """
        Sets the key L{k} with the value of L{v} if the unwrapped value L{v}
        passed to L{func} evaluates to True.  In the default case this is
        True if the unwrapped value L{v} is not None.
        """
        v = unwrap(v)
        if func(v):
            rv[k] = v

    def decode(shape_field):
        value = shape_field.getValue()
        if value and isbytes(value):
            value = bytes_to_native_str(value)
        return value

    rv["id"] = shape.getId().getValue()
    set_if("theT", shape.getTheT())
    set_if("theZ", shape.getTheZ())
    shape_type = type(shape)
    if shape_type == engine.model.RectangleI:
        rv["type"] = "Rectangle"
        rv["x"] = shape.getX().getValue()
        rv["y"] = shape.getY().getValue()
        rv["width"] = shape.getWidth().getValue()
        rv["height"] = shape.getHeight().getValue()
    elif shape_type == engine.model.MaskI:
        rv["type"] = "Mask"
        rv["x"] = shape.getX().getValue()
        rv["y"] = shape.getY().getValue()
        rv["width"] = shape.getWidth().getValue()
        rv["height"] = shape.getHeight().getValue()
        # TODO: support for mask
    elif shape_type == engine.model.EllipseI:
        rv["type"] = "Ellipse"
        rv["x"] = shape.getX().getValue()
        rv["y"] = shape.getY().getValue()
        rv["radiusX"] = shape.getRadiusX().getValue()
        rv["radiusY"] = shape.getRadiusY().getValue()
    elif shape_type == engine.model.PolylineI:
        rv["type"] = "PolyLine"
        points = decode(shape.getPoints())
        rv["points"] = stringToSvg(points)
    elif shape_type == engine.model.LineI:
        rv["type"] = "Line"
        rv["x1"] = shape.getX1().getValue()
        rv["x2"] = shape.getX2().getValue()
        rv["y1"] = shape.getY1().getValue()
        rv["y2"] = shape.getY2().getValue()
    elif shape_type == engine.model.PointI:
        rv["type"] = "Point"
        rv["x"] = shape.getX().getValue()
        rv["y"] = shape.getY().getValue()
    elif shape_type == engine.model.PolygonI:
        rv["type"] = "Polygon"
        # z = closed line
        points = decode(shape.getPoints())
        rv["points"] = stringToSvg(points) + " z"
    elif shape_type == engine.model.LabelI:
        rv["type"] = "Label"
        rv["x"] = shape.getX().getValue()
        rv["y"] = shape.getY().getValue()
    else:
        logger.debug("Shape type not supported: %s" % str(shape_type))

    text_value = unwrap(shape.getTextValue())
    if text_value is not None:
        # only populate json with font styles if we have some text
        rv["textValue"] = text_value
        # FIXME: units ignored for font size
        if shape.getFontSize() is not None:
            set_if("fontSize", shape.getFontSize().getValue())
        set_if("fontStyle", shape.getFontStyle())
        set_if("fontFamily", shape.getFontFamily())

    if shape.getTransform() is not None:
        transform = shape.getTransform()
        tm = [
            unwrap(transform.a00),
            unwrap(transform.a10),
            unwrap(transform.a01),
            unwrap(transform.a11),
            unwrap(transform.a02),
            unwrap(transform.a12),
        ]
        rv["transform"] = "matrix(%s)" % (" ".join([str(t) for t in tm]))
    fill_color = unwrap(shape.getFillColor())
    if fill_color is not None:
        rv["fillColor"], rv["fillAlpha"] = rgb_int2css(fill_color)
    stroke_color = unwrap(shape.getStrokeColor())
    if stroke_color is not None:
        rv["strokeColor"], rv["strokeAlpha"] = rgb_int2css(stroke_color)
    if shape.getStrokeWidth() is not None:
        # FIXME: units ignored for stroke width
        set_if("strokeWidth", shape.getStrokeWidth().getValue())
    if hasattr(shape, "getMarkerStart") and shape.getMarkerStart() is not None:
        # Handle string for python2 and bytes python3. TODO: lower level fix
        rv["markerStart"] = decode(shape.getMarkerStart())
    if hasattr(shape, "getMarkerEnd") and shape.getMarkerEnd() is not None:
        rv["markerEnd"] = decode(shape.getMarkerEnd())
    return rv


def stringToSvg(string):
    """
    Method for converting the string returned from
    engine.model.ShapeI.getPoints() into an SVG for display on web.
    E.g: "points[309,427, 366,503, 190,491] points1[309,427, 366,503, 190,491]
          points2[309,427, 366,503, 190,491]"
    To: M 309 427 L 366 503 L 190 491 z
    """
    point_list = string.strip()
    match = INSIGHT_POINT_LIST_RE.search(point_list)
    if match is not None:
        point_list = match.group(1)
    point_list = ODE_MODEL_POINT_LIST_RE.findall(point_list)
    if len(point_list) == 0:
        logger.error("Unrecognised ROI shape 'points' string: %r" % string)
        return ""
    point_list = " L ".join([" ".join(point) for point in point_list])
    return "M %s" % point_list


def rgb_int2css(rgbint):
    """
    converts a bin int number into css colour and alpha fraction.
    E.g. -1006567680 to '#00ff00', 0.5
    """
    alpha = rgbint % 256
    alpha = float(alpha) / 255
    b = rgbint // 256 % 256
    g = rgbint // 256 // 256 % 256
    r = rgbint // 256 // 256 // 256 % 256
    return "#%02x%02x%02x" % (r, g, b), alpha


def rgb_int2rgba(rgbint):
    """
    converts a bin int number into (r, g, b, alpha) tuple.
    E.g. 1694433280 to (255, 0, 0, 0.390625)
    """
    alpha = rgbint % 256
    alpha = float(alpha) / 255
    b = rgbint // 256 % 256
    g = rgbint // 256 // 256 % 256
    r = rgbint // 256 // 256 // 256 % 256
    return (r, g, b, alpha)


def graphResponseMarshal(conn, rsp):
    """
    Helper for marshalling a Chgrp or Chown response.
    Uses conn to lookup unlinked objects.
    Returns dict of e.g.
    {'includedObjects': {'Datasets':[1,2,3]},
     'unlinkedDetails': {'Tags':[{'id':1, 'name':'t'}]}
     }
    """
    rv = {}
    if isinstance(rsp, engine.cmd.ERR):
        rsp_params = ", ".join(["%s: %s" % (k, v) for k, v in rsp.parameters.items()])
        rv["error"] = rsp.message
        rv["report"] = "%s %s" % (rsp.name, rsp_params)
    else:
        included = rsp.responses[0].includedObjects
        deleted = rsp.responses[0].deletedObjects

        # Included: just simplify the key, e.g. -> Projects, Datasets etc
        includedObjects = {}
        objKeys = [
            "ode.model.containers.Project",
            "ode.model.containers.Dataset",
            "ode.model.core.Image",
            "ode.model.screen.Screen",
            "ode.model.screen.Plate",
            "ode.model.screen.Well",
        ]
        for k in objKeys:
            if k in included:
                otype = k.split(".")[-1]
                oids = included[k]
                oids.sort()  # makes testing easier
                includedObjects[otype + "s"] = oids
        rv["includedObjects"] = includedObjects

        # Annotation links - need to get info on linked objects
        tags = {}
        files = {}
        comments = {}
        others = 0
        annotationLinks = [
            "ode.model.annotations.ProjectAnnotationLink",
            "ode.model.annotations.DatasetAnnotationLink",
            "ode.model.annotations.ImageAnnotationLink",
            "ode.model.annotations.ScreenAnnotationLink",
            "ode.model.annotations.PlateAnnotationLink",
            "ode.model.annotations.WellAnnotationLink",
        ]
        for link in annotationLinks:
            if link in deleted:
                linkType = link.split(".")[-1]
                params = engine.sys.ParametersI()
                params.addIds(deleted[link])
                query = (
                    "select annLink from %s as annLink "
                    "join fetch annLink.child as ann "
                    "left outer join fetch ann.file "
                    "where annLink.id in (:ids)" % linkType
                )
                links = conn.getQueryService().findAllByQuery(
                    query, params, conn.SERVICE_OPTS
                )
                for lnk in links:
                    ann = lnk.child
                    if isinstance(ann, engine.model.FileAnnotationI):
                        name = unwrap(ann.getFile().getName())
                        files[ann.id.val] = {"id": ann.id.val, "name": name}
                    elif isinstance(ann, engine.model.TagAnnotationI):
                        name = unwrap(ann.getTextValue())
                        tags[ann.id.val] = {"id": ann.id.val, "name": name}
                    elif isinstance(ann, engine.model.CommentAnnotationI):
                        text = unwrap(ann.getTextValue())
                        comments[ann.id.val] = {"id": ann.id.val, "text": text}
                    else:
                        others += 1
        # sort tags & comments
        tags = list(tags.values())
        tags.sort(key=lambda x: x["name"])
        files = list(files.values())
        files.sort(key=lambda x: x["name"])
        comments = list(comments.values())
        comments.sort(key=lambda x: x["text"])
        rv["unlinkedParents"] = {}
        rv["unlinkedChildren"] = {}
        rv["unlinkedAnnotations"] = {
            "Tags": tags,
            "Files": files,
            "Comments": comments,
            "Others": others,
        }

        # Container links - only report these if we are moving the *parent*,
        # E.g. DatasetImageLinks are only reported if we are moving the Dataset
        # (and the image is left behind). If we were moving the Image then we'd
        # expect the link to be broken (can ignore)
        children = {}
        parents = {}
        containerLinks = {
            "ode.model.containers.ProjectDatasetLink": ["Project", "Dataset"],
            "ode.model.containers.DatasetImageLink": ["Dataset", "Image"],
            "ode.model.screen.ScreenPlateLink": ["Screen", "Plate"],
        }
        for link, types in containerLinks.items():
            pa_type = types[0]
            ch_type = types[1]
            if link in deleted:
                linkType = link.split(".")[-1]
                params = engine.sys.ParametersI()
                params.addIds(deleted[link])
                query = (
                    "select conLink from %s as conLink "
                    "join fetch conLink.child "
                    "join fetch conLink.parent "
                    "where conLink.id in (:ids)" % linkType
                )
                links = conn.getQueryService().findAllByQuery(
                    query, params, conn.SERVICE_OPTS
                )
                for lnk in links:
                    child = lnk.child.id.val
                    parent = lnk.parent.id.val
                    # If child IS included, then parent is the unlinked object
                    if child in includedObjects.get(ch_type + "s", []):
                        name = unwrap(lnk.parent.getName())
                        # Put objects in a dictionary to avoid duplicates
                        if pa_type not in parents:
                            parents[pa_type] = {}
                        parents[pa_type][parent] = {"id": parent, "name": name}
                    else:
                        name = unwrap(lnk.child.getName())
                        if ch_type not in children:
                            children[ch_type] = {}
                        children[ch_type][child] = {"id": child, "name": name}
        # sort objects
        for otype, objs in parents.items():
            objs = list(objs.values())
            objs.sort(key=lambda x: x["name"])
            # E.g. 'Dataset' objects in 'Datasets'
            rv["unlinkedParents"][otype + "s"] = objs

        for otype, objs in children.items():
            objs = list(objs.values())
            objs.sort(key=lambda x: x["name"])
            rv["unlinkedChildren"][otype + "s"] = objs

    return rv