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

import glob
import sys
import os

sys.path.append("../OdePy/src")
from ode_setup import PyTest

for tools in glob.glob("../../../lib/repository/setuptools*.egg"):
    if tools.find(".".join(map(str, sys.version_info[0:2]))) > 0:
        sys.path.insert(0, os.path.abspath(tools))

if "test" in sys.argv:
    os.environ.setdefault('ODE_HOME', os.path.abspath(
        os.path.join("..", "..", "..", "dist")))

    sys.path.insert(0, os.path.join("..", "target", "lib", "fallback"))
    LIB = os.path.join("..", "target", "lib", "python")
    sys.path.insert(0, LIB)
    ENGINE_LIB = os.path.join(LIB, "engine")
    sys.path.insert(1, ENGINE_LIB)

    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "engine.settings")

    import django
    if django.VERSION > (1, 7):
        django.setup()

from ez_setup import use_setuptools
use_setuptools(to_dir='../../../lib/repository')
from setuptools import setup
from ode_version import ode_version as ov

setup(name="OdeWeb",
      version=ov,
      description="OdeWeb",
      long_description="""\
OdeWeb is the container of the web clients for Bhojpur ODE."
""",
      author="Bhojpur Consulting Private Limited, India",
      author_email="product@bhojpur-consulting.com",
      url="https://github.com/bhojpur/ode/",
      download_url="https://github.com/bhojpur/ode/",
      packages=[''],
      test_suite='test.suite',
      cmdclass={'test': PyTest},
      tests_require=['pytest<3'],
      )
