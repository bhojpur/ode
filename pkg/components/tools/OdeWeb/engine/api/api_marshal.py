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

from ode_marshal import get_encoder

def normalize_objects(objects):
    """
    Normalize the groups and owners from ode_marshal dicts.

    Take a list of dicts generated from ode_marshal and
    normalizes the groups and owners into separate
    lists. ode:details will only retain group and owner IDs.
    """
    experimenters = {}
    groups = {}
    objs = []
    for o in objects:
        exp = o["ode:details"]["owner"]
        experimenters[exp["@id"]] = exp
        o["ode:details"]["owner"] = {"@id": exp["@id"]}
        grp = o["ode:details"]["group"]
        groups[grp["@id"]] = grp
        o["ode:details"]["group"] = {"@id": grp["@id"]}
        objs.append(o)
    experimenters = list(experimenters.values())
    groups = list(groups.values())
    return objs, {"experimenters": experimenters, "experimenterGroups": groups}


def marshal_objects(objects, extras=None, normalize=False):
    """
    Marshal a list of ODE.model objects using ode_marshal.

    @param extras:      A dict of id:dict to add extra data to each object
    @param normalize:   If true, normalize groups & owners into separate lists
    """
    marshalled = []
    for o in objects:
        encoder = get_encoder(o.__class__)
        m = encoder.encode(o)
        if extras is not None and o.id.val in extras:
            m.update(extras[o.id.val])
        marshalled.append(m)

    if not normalize:
        return {"data": marshalled}
    data, extra_objs = normalize_objects(marshalled)
    extra_objs["data"] = data
    return extra_objs