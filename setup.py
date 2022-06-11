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

import glob
import sys
import os

from setuptools import (
    Command,
    setup,
    find_packages,
)

try:
    from StringIO import StringIO
    from StringIO import StringIO as BytesIO
except ImportError:
    # Python 3
    from io import StringIO
    from io import BytesIO

from shutil import (
    copy,
    rmtree,
)

try:
    from urllib.request import urlopen
except ImportError:
    # Python 2
    from urllib import urlopen
from zipfile import ZipFile

try:
    import configparser
except ImportError:
    # Python 2
    import ConfigParser as configparser

def get_ode_location():

    config_ode_version = "5.5.5"

    # simplified strings
    defaultsect = configparser.DEFAULTSECT
    version_key = "versions.ode-ode"
    url_key = "versions.ode-ode-url"

    # detect if in Jenkins or not
    if "ZIP_FILE" in os.environ:
        config_ode_url = os.environ.get("ZIP_FILE")
    elif "JENKINS_URL" in os.environ:
        config_ode_url = os.environ.get("JENKINS_URL")
        config_ode_url += "job/ODE-build-build/lastSuccessfulBuild/"
        config_ode_url += "artifact/ode/build/distributions/"
        config_ode_url += "bhojpur-ode-VERSION-python.zip"
    else:
        config_ode_url = (
            "https://artifacts.bhojpur.net/artifactory/ode.releases/"
            "net/bhojpur/ode/VERSION/"
            "bhojpur-ode-VERSION-python.zip")

    # load version.properties if available
    config_path = os.environ.get("VERSION_PROPERTIES",
                                 "artifact/version.properties")
    if os.path.exists(config_path):
        config_obj = configparser.RawConfigParser({
            url_key: config_ode_url,
            version_key: config_ode_version,
        })
        with open(config_path) as f:
            config_str = StringIO('[%s]\n%s' % (defaultsect, f.read()))
        config_obj.readfp(config_str)
        config_ode_url = config_obj.get(defaultsect, url_key)
        config_ode_version = config_obj.get(defaultsect, version_key)

    # replace VERSION in the final url and return
    config_ode_url = config_ode_url.replace(
            "VERSION", config_ode_version)
    return config_ode_url

def download_ode_target():
    loc = get_ode_location()
    if "ZIP_FILE" in os.environ:
        zipfile = ZipFile(loc)
    else: 
        print("Downloading %s ..." % loc, file=sys.stderr)
        resp = urlopen(loc)
        content = resp.read()
        content = BytesIO(content)
        zipfile = ZipFile(content)
    zipfile.extractall("target")

def _relative_symlink_file(src, dst):
    relsrc = os.path.relpath(src, os.path.dirname(dst))
    try:
        os.symlink(relsrc, dst)
        print(src, dst)
    except OSError as e:
        if e.errno != 17:
            raise
        os.remove(dst)
        os.symlink(relsrc, dst)

def copy_src_to_target(symlink=False):
    for dirpath, dirs, files in os.walk("pkg"):
        for filename in files:
            topath = dirpath.replace("pkg", "target", 1)
            if not os.path.exists(topath):
                os.makedirs(topath)
            fromfile = os.path.sep.join([dirpath, filename])
            tofile = os.path.sep.join([topath, filename])
            if symlink:
                _relative_symlink_file(fromfile, tofile)
            else:
                copy(fromfile, tofile)

class DevTargetCommand(Command):
    """
    Recreate "target" with symlinks to files in "pkg" to ease development.

    For example, `pip install -e .` will work.
    Changes in files under "src" will be automatically seen in the installed
    module.

    If you add or remove files in src you must re-run both of these commands:

        python setup.py devtarget
        pip install -e .
    """

    description = (
        'Recreate target with symlinks to files in pkg to ease development')
    user_options = []

    def initialize_options(self):
        pass

    def finalize_options(self):
        pass

    def run(self):
        rmtree('target')
        download_ode_target()
        copy_src_to_target(symlink=True)
        print("If this is installed as an editable module re-run "
              "`pip install -e .`")

if not os.path.exists('target'):
    download_ode_target()
    copy_src_to_target()

packageless = glob.glob("target/*.py")
packageless = [x[7:-3] for x in packageless]
packages = find_packages(where="target")

sys.path.append("target")
from ode_version import ode_version as ov  # noqa

def read(fname):
    """
    Utility function to read the README file.
    :rtype : String
    """
    return open(os.path.join(os.path.dirname(__file__), fname)).read()

setup(
    name="bhojpur-ode",
    version=ov,
    description="An optical data engine server",
    long_description=read("README.rst"),
    classifiers=[
      'Development Status :: 5 - Production/Stable',
      'Intended Audience :: Developers',
      'Intended Audience :: Science/Research',
      'Intended Audience :: System Administrators',
      'License :: OSI Approved :: GNU General Public License v2 '
      'or later (GPLv2+)',
      'Natural Language :: English',
      'Operating System :: OS Independent',
      'Programming Language :: Python :: 3',
      'Topic :: Software Development :: Libraries :: Python Modules',
    ],
    author="Bhojpur Consulting Private Limited, India",
    author_email="product@bhojpur-consulting.com",
    url='https://github.com/bhojpur/ode',
    project_urls={
        'Documentation':
            'https://docs.bhojpur.net/ode/latest/developers/'
            'Python.html',
        'Bug tracker': 'https://github.com/bhojpur/ode/issues',
    },
    package_dir={"": "target"},
    packages=packages,
    package_data={
        'core.gateway': ['pilfonts/*'],
        'core.gateway.scripts': ['imgs/*']},
    py_modules=packageless,
    entry_points={
        'console_scripts': ['core=core.main:main'],
    },
    python_requires='>=3',
    install_requires=[
        'appdirs',
        'future',
        'numpy',
        'Pillow',
        'PyYAML',
        'zeroc-ice>=3.6.4,<3.7',
        'pywin32; platform_system=="Windows"',
        'requests'
    ],
    tests_require=[
        'pytest',
        'mox3',
    ],
    cmdclass={
        'devtarget': DevTargetCommand,
    },
)