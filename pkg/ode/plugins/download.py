#!/usr/bin/env python
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
   download plugin

   Plugin read by ode.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.
"""

from builtins import str
import sys
import ode
import os
import re
from ode.cli import BaseControl, CLI, ProxyStringType
from ode.rtypes import unwrap
from ode.gateway import OdeGateway

HELP = """Download a File, Image or Fileset with a specified ID to a target file
or directory.

Examples:

    # Download OriginalFile 2 to local_file
    ode download 2 local_file
    # Download Original File 2 to the stdout
    ode download 2 -

    # Download the OriginalFile linked to FileAnnotation 20 to local_file
    ode download FileAnnotation:20 local_file

    # Download the OriginalFiles linked to Image 5 into a directory
    # The Files will be named as they are in Bhojpur ODE's Managed repository
    ode download Image:5 output_dir
    # Download the OriginalFiles linked to Fileset 6 into a directory
    ode download Fileset:6 output_dir
"""

class StdOutHandle():
    """
    File handle for writing bytes to std.out in python 2 and python 3
    """
    @staticmethod
    def write(b):
        # Handle stdout.write for bytes
        try:
            # Try writing bytes... python 2
            return sys.stdout.write(b)
        except TypeError:
            # python 3: If String was expected, convert to String
            return sys.stdout.write(b.decode('ascii', 'replace'))

class DownloadControl(BaseControl):

    def _configure(self, parser):
        parser.add_argument(
            "object", type=ProxyStringType("OriginalFile"),
            help="Object to download of form <object>:<id>. "
            "OriginalFile is assumed if <object>: is omitted.")
        parser.add_argument(
            "filename", help="Local filename (or path for Fileset) to be saved to. '-' for stdout")
        parser.set_defaults(func=self.__call__)
        parser.add_login_arguments()

    def __call__(self, args):
        client = self.ctx.conn(args)
        obj = args.object
        # e.g. "ImageI" -> "Image"
        dtype = obj.__class__.__name__[:-1]
        conn = OdeGateway(client_obj=client)
        conn.SERVICE_OPTS.setOdeGroup(-1)

        if dtype == "Fileset":
            fileset = self.get_object(conn, dtype, obj.id.val)
            self.download_fileset(conn, fileset, args.filename)
        elif dtype == "Image":
            image = self.get_object(conn, dtype, obj.id.val)
            fileset = image.getFileset()
            if fileset is None:
                self.ctx.die(602, 'Input image has no associated Fileset')
            self.download_fileset(conn, fileset, args.filename)
        else:
            orig_file = self.get_file(client.sf, dtype, obj.id.val)
            target_file = str(args.filename)
            # only expect single file
            self.download_file(client, orig_file, target_file)

    def download_fileset(self, conn, fileset, dir_path):
        self.ctx.out(f"Fileset: {fileset.id}")
        template_prefix = fileset.getTemplatePrefix()
        for orig_file in fileset.listFiles():
            file_path = orig_file.path.replace(template_prefix, "")
            target_dir = os.path.join(dir_path, file_path)
            os.makedirs(target_dir, exist_ok=True)
            target_path = os.path.join(target_dir, orig_file.name)
            self.download_file(conn.c, orig_file._obj, target_path)

    def download_file(self, client, orig_file, target_file):
        perms = orig_file.details.permissions
        name = ode.constants.permissions.BINARYACCESS

        if perms.isRestricted(name):
            self.ctx.die(66, ("Download of OriginalFile:"
                              "%s is restricted") % orig_file.id.val)

        try:
            if target_file == "-":
                client.download(orig_file, filehandle=StdOutHandle())
                sys.stdout.flush()
            else:
                self.ctx.out(
                    f"Downloading file ID: {orig_file.id.val} to {target_file}")
                if os.path.exists(target_file):
                    self.ctx.out(f"File exists! Skipping...")
                else:
                    client.download(orig_file, target_file)
        except ode.ClientError as ce:
            self.ctx.die(67, "ClientError: %s" % ce)
        except ode.ValidationException as ve:
            # Possible, though unlikely after previous check
            self.ctx.die(67, "Unknown ValidationException: %s"
                         % ve.message)
        except ode.ResourceError as re:
            # ID exists in DB, but not on FS
            self.ctx.die(67, "ResourceError: %s" % re.message)

    def get_object(self, conn, dtype, obj_id):
        result = conn.getObject(dtype, obj_id)
        if result is None:
            self.ctx.die(601, f'No {dtype} with input ID: {obj_id}')
        return result

    def get_file(self, session, dtype, obj_id):

        query = session.getQueryService()

        # Handle OriginalFile:id
        if dtype == "OriginalFile":
            try:
                ofile = query.get("OriginalFile", obj_id,
                                  {'ode.group': '-1'})
                return ofile
            except ode.ValidationException:
                self.ctx.die(601, 'No OriginalFile with input ID')

        # Handle FileAnnotation:id
        if dtype == "FileAnnotation":
            fa = None
            try:
                fa = query.findByQuery((
                    "select fa from FileAnnotation fa "
                    "left outer join fetch fa.file "
                    "where fa.id = :id"),
                    ode.sys.ParametersI().addId(obj_id),
                    {'ode.group': '-1'})
            except ode.ValidationException:
                pass
            if fa is None:
                self.ctx.die(601, 'No FileAnnotation with input ID')
            return fa.getFile()

        self.ctx.die(601, 'Invalid object input. Use e.g. Image:ID')

try:
    register("download", DownloadControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("download", DownloadControl, HELP)
        cli.invoke(sys.argv[1:])