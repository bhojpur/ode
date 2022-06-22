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
   upload plugin

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.
"""

import sys
import re
import os
import warnings
import mimetypes

from ode.cli import BaseControl, CLI
import extras.path as path

try:
    import hashlib
    hash_sha1 = hashlib.sha1
except:
    import sha
    hash_sha1 = sha.new

HELP = """Upload local files to the Bhojpur ODE server"""
RE = re.compile(r"\s*upload\s*")
UNKNOWN = 'type/unknown'

class UploadControl(BaseControl):

    def _complete(self, text, line, begidx, endidx):
        """
        Returns a file after "upload" and otherwise delegates to the
        BaseControl
        """
        m = RE.match(line)
        if m:
            return self._complete_file(RE.sub('', line))
        else:
            return BaseControl._complete(self, text, line, begidx, endidx)

    def _configure(self, parser):
        parser.add_argument("file", nargs="+")
        parser.set_defaults(func=self.upload)
        parser.add_login_arguments()

    def upload(self, args):
        self.ctx.err(
            "This module is deprecated as of Bhojpur ODE. Use the module"
            " available from https://pypi.org/project/ode-upload/"
            " instead.", DeprecationWarning)
        client = self.ctx.conn(args)
        objIds = []
        for file in args.file:
            if not path.path(file).exists():
                self.ctx.die(500, "File: %s does not exist" % file)
        for file in args.file:
            ode_format = UNKNOWN
            if(mimetypes.guess_type(file) != (None, None)):
                ode_format = mimetypes.guess_type(file)[0]
            obj = client.upload(file, type=ode_format)
            objIds.append(obj.id.val)
            self.ctx.set("last.upload.id", obj.id.val)

        objIds = self._order_and_range_ids(objIds)
        self.ctx.out("OriginalFile:%s" % objIds)

try:
    if "ODE_NO_DEPRECATED_PLUGINS" not in os.environ:
        warnings.warn(
            "This plugin is deprecated as of Bhojpur ODE. Use the upload"
            " CLI plugin available from"
            " https://pypi.org/project/ode-upload/ instead.",
            DeprecationWarning)
        register("upload", UploadControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("upload", UploadControl, HELP)
        cli.invoke(sys.argv[1:])