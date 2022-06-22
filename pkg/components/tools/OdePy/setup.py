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
   setuptools entry point

   Tests run by default using the OdePy/dist egg as Bhojpur ODE python lib but
   you can override that by using the --test-pythonpath flag to ./setup.py
   test.

   For testing that require a running Bhojpur ODE server, the ice.config file must
   exist and hold the proper configuration either at the same directory as
   this file or in some place pointed to by the --test-ice-config flag to
   ./setup.py test.

   For example:

      # this will run all tests under OdePy/test/
      ./setup.py test
       # run all tests under OdePy/test/gatewaytest
      ./setup.py test -s test/gatewaytest
      # run all tests that include TopLevelObjects in the name
      ./setup.py test -k TopLevelObjects
      # exit on first failure
      ./setup.py test -x
      # drop to the pdb debugger on failure
      ./setup.py test --pdb
"""

import glob
import sys
import os

sys.path.append("src")
from ode_setup import PyTest

for tools in glob.glob("../../../lib/repository/setuptools*.egg"):
    if tools.find(".".join(map(str, sys.version_info[0:2]))) > 0:
        sys.path.insert(0, os.path.abspath(tools))

from ez_setup import use_setuptools
use_setuptools(to_dir='../../../lib/repository')
from setuptools import setup, find_packages
from ode_version import ode_version as ov

if os.path.exists("target"):
    packages = find_packages("target")+[""]
else:
    packages = [""]

url = 'https://docs.bhojpur.net/latest/ode/developers'

setup(
    name="ode_client",
    version=ov,
    description="Python bindings to the Bhojpur ODE server",
    long_description="Python bindings to the Bhojpur ODE server.",
    author="Bhojpur Consulting Private Limited, India",
    author_email="product@bhojpur-consulting.com",
    url=url,
    download_url=url,
    package_dir={"": "target"},
    packages=packages,
    package_data={
        'ode.gateway': ['pilfonts/*'],
        'ode.gateway.scripts': ['imgs/*']},
    cmdclass={'test': PyTest},
    tests_require=['pytest<3'])
