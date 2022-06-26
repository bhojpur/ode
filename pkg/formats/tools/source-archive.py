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

from __future__ import print_function

from optparse import OptionParser
import os
from subprocess import call
import sys
import zipfile
import tarfile
import platform

# This script archives the base tree and repacks it into a single zip which is
# the source release, taking care to preserve timestamps and exectute
# permissions, etc.  This is done via ZipInfo objects, and the
# repacking is done entirely in memory so that this should work on any
# platform irrespective of filesystem support for the archive
# attributes.  It excludes .gitignore files at this point to avoid
# polluting the release with version control files.


if __name__ == "__main__":

    parser = OptionParser()
    parser.add_option(
        "--release", action="store", type="string", dest="release")
    parser.add_option(
        "--odeformats-version", action="store", type="string",
        dest="odeformats_version")
    parser.add_option(
        "--odeformats-shortversion", action="store", type="string",
        dest="odeformats_shortversion")
    parser.add_option(
        "--odeformats-vcsshortrevision", action="store", type="string",
        dest="odeformats_vcsshortrevision")
    parser.add_option(
        "--odeformats-vcsrevision", action="store", type="string",
        dest="odeformats_vcsrevision")
    parser.add_option(
        "--odeformats-vcsdate", action="store", type="string",
        dest="odeformats_vcsdate")
    parser.add_option(
        "--odeformats-vcsdate-unix", action="store", type="string",
        dest="odeformats_vcsdate_unix")
    parser.add_option(
        "--target", action="store", type="string", dest="target")

    (options, args) = parser.parse_args(sys.argv)

    options.target = os.path.abspath(options.target)

    version = options.odeformats_version
    shortversion = options.odeformats_shortversion
    vcsdate = options.odeformats_vcsdate
    vcsshortrevision = options.odeformats_vcsshortrevision
    vcsrevision = options.odeformats_vcsrevision

    prefix = "%s-%s" % (options.release, version)

    if not os.path.exists('.git'):
        raise Exception('Releasing is only possible from a git repository')

    print("Releasing %s" % (prefix))
    sys.stdout.flush()

    # Create base archive
    base_archive_status = call([
        'git', 'archive', '--format', 'zip',
        '--prefix', "%s/" % (prefix),
        '--output', "%s/%s-base.zip" % (options.target, prefix),
        'HEAD'])
    if base_archive_status != 0:
        raise Exception('Failed to create git zip base archive')

    zips = list(["%s/%s-base.zip" % (options.target, prefix)])

    # Create destination zip file
    print("  - creating %s/%s.zip" % (options.target, prefix))
    sys.stdout.flush()
    basezip = zipfile.ZipFile("%s/%s.zip" % (options.target, prefix), 'w')

    # Repack each of the separate zips into the destination zip
    for name in zips:
        subzip = zipfile.ZipFile(name, 'r')
        print("  - repacking %s" % (name))
        sys.stdout.flush()
        # Iterate over the ZipInfo objects from the archive
        for info in subzip.infolist():
            # Skip unwanted git and travis files
            if (os.path.basename(info.filename) == '.gitignore' or
                    os.path.basename(info.filename) == '.travis.yml'):
                continue
            # Skip files for which we don't have source in this repository,
            # for GPL compliance
            if (options.release.endswith("-dfsg") and
                (os.path.splitext(info.filename)[1] == ".jar" or
                 os.path.splitext(info.filename)[1] == ".dll" or
                 os.path.splitext(info.filename)[1] == ".dylib" or
                 os.path.splitext(info.filename)[1] == ".so")):
                continue
            print("File: %s" % (info.filename))
            # Repack a single zip object; preserve the metadata
            # directly via the ZipInfo object and rewrite the content
            # (which unfortunately requires decompression and
            # recompression rather than a direct copy)
            basezip.writestr(info, subzip.open(info.filename).read())

        # Close zip or else the remove will fail on Windows
        subzip.close()

        # Remove repacked zip
        os.remove(name)

    # Repeat for tar archive
    base_archive_status = call([
        'git', 'archive', '--format', 'tar',
        '--prefix', "%s/" % (prefix),
        '--output', "%s/%s-base.tar" % (options.target, prefix),
        'HEAD'])
    if base_archive_status != 0:
        raise Exception('Failed to create git tar base archive')

    tars = list(["%s/%s-base.tar" % (options.target, prefix)])

    # Create destination tar file
    print("  - creating %s/%s.tar" % (options.target, prefix))
    sys.stdout.flush()
    basetar = tarfile.open("%s/%s.tar" % (options.target, prefix), 'w',
                           format=tarfile.PAX_FORMAT)

    # Repack each of the separate tars into the destination tar
    for name in tars:
        subtar = tarfile.open(name, 'r')
        print("  - repacking %s" % (name))
        sys.stdout.flush()
        # Iterate over the TarInfo objects from the archive
        for info in subtar.getmembers():
            # Skip unwanted git and travis files
            if (os.path.basename(info.name) == '.gitignore' or
                    os.path.basename(info.name) == '.travis.yml'):
                continue
            # Skip files for which we don't have source in this repository,
            # for GPL compliance
            if (options.release.endswith("-dfsg") and
                (os.path.splitext(info.name)[1] == ".jar" or
                 os.path.splitext(info.name)[1] == ".dll" or
                 os.path.splitext(info.name)[1] == ".dylib" or
                 os.path.splitext(info.name)[1] == ".so")):
                continue
            print("File: %s" % (info.name))
            # Repack a single tar object; preserve the metadata
            # directly via the TarInfo object and rewrite the content
            basetar.addfile(info, subtar.extractfile(info.name))

        # Close tar or else the remove will fail on Windows
        subtar.close()

        # Remove repacked tar
        os.remove(name)

    basetar.close()
    try:
        call(['xz', "%s/%s.tar" % (options.target, prefix)])
    except Exception:
        # This is expected to fail on Windows when xz is unavailable,
        # but is always an error on all other platforms.
        if platform.system() != 'Windows':
            sys.exit(1)
