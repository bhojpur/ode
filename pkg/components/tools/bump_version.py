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

# Script to bump major.minor version across the code

import os
import re
import argparse

def check_version_format(version):
    """Check format of major minor number"""
    pattern = '^[0-9]+[\.][0-9]+$'
    return re.match(pattern, version) is not None


def replace_file(input_path, pattern, version):
    """Substitute a pattern with version in a file"""
    with open(input_path, "r") as infile:
        regexp = re.compile(pattern)
        new_content = regexp.sub(r"\g<baseurl>%s" % version, infile.read())
        with open(input_path, "w") as output:
            output.write(new_content)
            output.close()
        infile.close()

docs_pattern = r"(?P<baseurl>site/support/ode)(\d+(.\d+)?)"
latest_pattern = r"(?P<baseurl>latest/ode)(\d+(.\d+)?)"
extensions = ('.txt', '.md', '.java', '.ice', '.html', '.xml', '.py', '.rst')


def bump_version(version):
    """Replace versions in documentation links"""

    for base, dirs, files in os.walk('.'):
        for file in files:
            if file.endswith(extensions):
                replace_file(os.path.join(base, file), docs_pattern, version)
                replace_file(os.path.join(base, file), latest_pattern, version)


if __name__ == "__main__":
    # Input check
    parser = argparse.ArgumentParser()
    parser.add_argument("version", type=str)
    ns = parser.parse_args()

    if not check_version_format(ns.version):
        print("Invalid version format")
    else:
        bump_version(ns.version)
