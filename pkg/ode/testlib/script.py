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
import logging
from ode.testlib import ITest
from ode.gateway import OdeGateway

class ScriptTest(ITest):

    def get_script(self, path):
        script_service = self.root.sf.getScriptService()
        script = _get_script(script_service, path)
        if script is None:
            return -1
        return script.getId().getValue()

def run_script(client, script_id, args, key=None):
    script_service = client.sf.getScriptService()
    proc = script_service.runScript(script_id, args, None)
    try:
        cb = ode.scripts.ProcessCallbackI(client, proc)
        while not cb.block(1000):  # ms.
            pass
        cb.close()
        results = proc.getResults(0)    # ms
    finally:
        proc.close(False)

    if 'stdout' in results:
        orig_file = results['stdout'].getValue()
        v = "Script generated StdOut in file:", orig_file.getId().getValue()
        logging.debug(v)
        assert orig_file.id.val > 0
    if 'stderr' in results:
        orig_file = results['stderr'].getValue()
        v = "Script generated StdErr in file:", orig_file.getId().getValue()
        logging.debug(v)
        assert orig_file.getId().getValue() > 0
    if key and key in results:
        return results[key]

def _get_script(script_service, script_path):
    """ Utility method, return the script or None """
    scripts = script_service.getScripts()     # returns list of OriginalFiles

    # make sure path starts with a slash.
    # ** If you are a Windows client - will need to convert all path separators
    #    to "/" since server stores /path/to/script.py **
    if not script_path.startswith("/"):
        script_path = "/" + script_path

    named_scripts = [
        s for s in scripts if
        s.getPath().getValue() + s.getName().getValue() == script_path]

    if len(named_scripts) == 0:
        return None

    return named_scripts[0]

def points_to_string(points):
    """ Returns legacy format supported by Insight """
    points = ["%s,%s" % (p[0], p[1]) for p in points]
    csv = ", ".join(points)
    return "points[%s] points1[%s] points2[%s]" % (csv, csv, csv)


def check_file_annotation(client, file_annotation,
                          parent_type="Image", is_linked=True,
                          file_name=None):
    """
    Check validity of file annotation. If hasFileAnnotation, check the size,
    name and number of objects linked to the original file.
    """
    assert file_annotation is not None
    orig_file = file_annotation.getValue().getFile()
    assert orig_file.getSize().getValue() > 0
    assert orig_file.getName().getValue() is not None
    id = file_annotation.getValue().getId().getValue()
    assert id > 0

    conn = OdeGateway(client_obj=client)
    wrapper = conn.getObject("FileAnnotation", id)
    name = None
    if file_name is not None:
        name = wrapper.getFile().getName()

    links = sum(1 for i in wrapper.getParentLinks(parent_type))
    conn.close()

    if is_linked:
        assert links == 1
    else:
        assert links == 0

    if file_name is not None:
        assert name == file_name