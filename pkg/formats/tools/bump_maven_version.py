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

# Script for increasing versions numbers across the code

import sys
import glob
import re
import argparse

def check_version_format(version):
    """Check format of version number"""
    pattern = r'^[0-9]+[\.][0-9]+[\.][0-9]+(\-.+)*$'
    return re.match(pattern, version) is not None

ODE_FORMATS_ARTIFACT = (
    r"(<groupId>%s</groupId>\n"
    ".*<artifactId>pom-ode-formats</artifactId>\n"
    ".*<version>).*(</version>)")

class Replacer(object):

    def __init__(self, old_group="ode", new_group="ode"):
        self.old_group = old_group
        self.new_group = new_group
        self.group_pattern = \
            r"(<groupId>)%s(</groupId>)" % \
            old_group
        self.artifact_pattern = ODE_FORMATS_ARTIFACT % old_group
        self.release_version_pattern = \
            r"(<release.version>).*(</release.version>)"
        self.stableversion_pattern = \
            r"(STABLE_VERSION = \").*(\";)"
        self.upgradecheck = \
            "components/formats-bsd/src/loci/formats/UpgradeChecker.java"

    def replace_file(self, input_path, pattern, version):
        """Substitute a pattern with version in a file"""
        with open(input_path, "r") as infile:
            regexp = re.compile(pattern)
            new_content = regexp.sub(r"\g<1>%s\g<2>" % version, infile.read())
            with open(input_path, "w") as output:
                output.write(new_content)
                output.close()
            infile.close()

    def bump_pom_versions(self, version):
        """Replace versions in pom.xml files"""

        # Replace versions in components pom.xml
        for pomfile in (glob.glob("*/*/pom.xml") + glob.glob("*/*/*/pom.xml")):
            self.replace_file(pomfile, self.artifact_pattern, version)
            self.replace_file(pomfile, self.group_pattern, self.new_group)

        # Replace versions in top-level pom.xml
        toplevelpomfile = "pom.xml"
        self.replace_file(
            toplevelpomfile, self.artifact_pattern, version)
        self.replace_file(
            toplevelpomfile, self.release_version_pattern, version)
        self.replace_file(
            toplevelpomfile, self.group_pattern, self.new_group)

    def bump_stable_version(self, version):
        """Replace UpgradeChecker stable version"""
        self.replace_file(
            self.upgradecheck, self.stableversion_pattern, version)

if __name__ == "__main__":
    # Input check
    parser = argparse.ArgumentParser()
    parser.add_argument("--old-group", type=str, default="ode")
    parser.add_argument("--new-group", type=str, default="ode")
    parser.add_argument("version", type=str)
    ns = parser.parse_args()

    if not check_version_format(ns.version):
        print("Invalid version format")
        sys.exit(1)

    replacer = Replacer(old_group=ns.old_group, new_group=ns.new_group)
    replacer.bump_pom_versions(ns.version)
    if not ns.version.endswith('SNAPSHOT'):
        replacer.bump_stable_version(ns.version)
