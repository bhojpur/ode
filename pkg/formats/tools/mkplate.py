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

from optparse import OptionParser
import os
import sys
import shutil

if __name__ == "__main__":

    parser = OptionParser()
    parser.add_option("--basefile", action="store", type="string", dest="basefile")
    parser.add_option("--prefix", action="store", type="string", dest="platePrefix")
    parser.add_option("--rows", action="store", type="int", dest="plateRows", default=1)
    parser.add_option("--columns", action="store", type="int", dest="plateColumns", default=1)
    parser.add_option("--fields", action="store", type="int", dest="fields", default=1)
    parser.add_option("--channels", action="store", type="int", dest="channels", default=1)

    (options, args) = parser.parse_args(sys.argv)

    suffix = options.basefile[options.basefile.index('.'):]

    for row in (1, options.plateRows):
        for col in (1, options.plateColumns):
            for field in (1, options.fields):
                for channel in (0, options.channels - 1):
                  destFile = '{:s}_{:c}{:02d}f{:02d}d{:1d}{:s}'.format(options.platePrefix, row + 64, col, field, channel, suffix)
                  shutil.copyfile(options.basefile, destFile)
