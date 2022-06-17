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

"""Helper functions for views that handle object trees."""

from engine.rtypes import unwrap, wrap, rlong
from engine.sys import ParametersI
from . import api_settings

from .api_marshal import marshal_objects
from copy import deepcopy

MAX_LIMIT = max(1, api_settings.API_MAX_LIMIT)
DEFAULT_LIMIT = max(1, api_settings.API_LIMIT)

def get_wellsample_indices(conn, plate_id=None, plateacquisition_id=None):
    """
    Return min and max WellSample index for a Plate OR PlateAcquisition

    @param conn:        OdeGateway
    @param plate_id:    Plate ID
    @param plateacquisition_id:    PlateAcquisition ID
    @return             A dict of parent_id: child_count
    """
    ctx = deepcopy(conn.SERVICE_OPTS)
    ctx.setOdeGroup(-1)
    params = ParametersI()
    query = (
        "select minIndex(ws), maxIndex(ws) from Well well " "join well.wellSamples ws"
    )
    if plate_id is not None:
        query += " where well.plate.id=:plate_id "
        params.add("plate_id", rlong(plate_id))
    elif plateacquisition_id is not None:
        query += " where ws.plateAcquisition.id=:plateacquisition_id"
        params.add("plateacquisition_id", rlong(plateacquisition_id))
    result = conn.getQueryService().projection(query, params, ctx)
    result = [r for r in unwrap(result)[0] if r is not None]
    return result

def get_child_counts(conn, link_class, parent_ids):
    """
    Count child links for the specified parent_ids.

    @param conn:        OdeGateway
    @param link_class:  Type of link, e.g. 'ProjectDatasetLink'
    @param parent_ids:  List of Parent IDs
    @return             A dict of parent_id: child_count
    """
    counts = {}
    if len(parent_ids) == 0:
        return counts
    ctx = deepcopy(conn.SERVICE_OPTS)
    ctx.setOdeGroup(-1)
    params = ParametersI()
    params.add("ids", wrap([rlong(id) for id in parent_ids]))
    query = (
        "select chl.parent.id, count(chl.id) from %s chl"
        " where chl.parent.id in (:ids) group by chl.parent.id" % link_class
    )
    result = conn.getQueryService().projection(query, params, ctx)
    for d in result:
        counts[d[0].val] = unwrap(d[1])
    return counts

def validate_opts(opts):
    """Check that opts dict has valid 'limit' and 'offset'."""
    if opts is None:
        opts = {}
    if opts.get("limit") is None or opts.get("limit") < 0:
        opts["limit"] = DEFAULT_LIMIT
    opts["limit"] = min(opts["limit"], MAX_LIMIT)
    if opts.get("offset") is None or opts.get("offset") < 0:
        opts["offset"] = 0
    return opts

def query_objects(conn, object_type, group=None, opts=None, normalize=False):
    """
    Base query method, handles different object_types.

    Builds a query and adds common
    parameters and filters such as by owner or group.

    @param conn:        OdeGateway
    @param object_type: Type to query, e.g. Project
    @param group:       Filter query by ExperimenterGroup ID
    @param opts:        Options dict for conn.buildQuery()
    @param normalize:   If true, marshal groups and experimenters separately
    """
    opts = validate_opts(opts)
    # buildQuery is used by conn.getObjects()
    query, params, wrapper = conn.buildQuery(object_type, opts=opts)
    # Set the desired group context
    ctx = deepcopy(conn.SERVICE_OPTS)
    if group is None:
        group = -1
    ctx.setOdeGroup(group)

    qs = conn.getQueryService()

    objects = []
    extras = {}

    if opts["limit"] == 0:
        result = []
    else:
        result = qs.findAllByQuery(query, params, ctx)
    for obj in result:
        objects.append(obj)

    # Optionally get child counts...
    if opts and opts.get("child_count") and wrapper.LINK_CLASS:
        obj_ids = [r.id.val for r in result]
        counts = get_child_counts(conn, wrapper.LINK_CLASS, obj_ids)
        for obj_id in obj_ids:
            count = counts[obj_id] if obj_id in counts else 0
            extras[obj_id] = {"ode:childCount": count}

    # Query the count() of objects & add to 'meta' dict
    count_query, params = conn.buildCountQuery(object_type, opts=opts)
    result = qs.projection(count_query, params, ctx)

    meta = {}
    meta["offset"] = opts["offset"]
    meta["limit"] = opts["limit"]
    meta["maxLimit"] = MAX_LIMIT
    meta["totalCount"] = result[0][0].val

    marshalled = marshal_objects(objects, extras=extras, normalize=normalize)
    marshalled["meta"] = meta
    return marshalled