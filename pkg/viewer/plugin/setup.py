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

import os
import setuptools.command.install
import setuptools.command.sdist
from distutils.core import Command
from setuptools import setup, find_packages
from ode_viewer.version import get_version

# Utility function to read the README file.
# Used for the long_description.  It's nice, because now 1) we have a top level
# README file and 2) it's easier to type in the README file than to put a raw
# string in below ...
def read(fname):
    return open(os.path.join(os.path.dirname(__file__), fname)).read()

cmdclass = {}

class RunProd(Command):

    def initialize_options(self):
        pass

    def finalize_options(self):
        pass

    def run(self):
        if not os.path.isdir('ode_viewer/static'):
            self.spawn(['npm', 'run', 'prod'])
            self.spawn(['rm', '-rf', '../node_modules'])


cmdclass['run_prod'] = RunProd

class Sdist(setuptools.command.sdist.sdist):

    def run(self):
        if not os.path.isdir('ode_viewer/static'):
            self.run_command('run_prod')
        setuptools.command.sdist.sdist.run(self)

cmdclass['sdist'] = Sdist

class Install(setuptools.command.install.install):

    def run(self):
        if not os.path.isdir('ode_viewer/static'):
            self.run_command('run_prod')
        setuptools.command.install.install.run(self)

class Test(setuptools.command.install.install):

    def run(self):
        self.spawn(['ant', 'unit-tests'])
        pass

cmdclass['install'] = Install

cmdclass['test'] = Test

version = get_version()

setup(name="ode-viewer",
      packages=find_packages(exclude=['ez_setup', 'ol3-viewer']),
      version=version,
      description="A Python plugin for the Bhojpur ODE web application",
      long_description=read('ode_viewer/README.rst'),
      classifiers=[
          'Development Status :: 5 - Production/Stable',
          'Environment :: Web Environment',
          'Framework :: Django',
          'Intended Audience :: End Users/Desktop',
          'Intended Audience :: Science/Research',
          'Natural Language :: English',
          'Operating System :: OS Independent',
          'Programming Language :: JavaScript',
          'Programming Language :: Python :: 3',
          'Topic :: Internet :: WWW/HTTP',
          'Topic :: Internet :: WWW/HTTP :: Dynamic Content',
          'Topic :: Internet :: WWW/HTTP :: WSGI',
          'Topic :: Scientific/Engineering :: Visualization',
          'Topic :: Software Development :: Libraries :: '
          'Application Frameworks',
          'Topic :: Text Processing :: Markup :: HTML'
      ],  # Get strings from
          # http://pypi.python.org/pypi?%3Aaction=list_classifiers
      author='Bhojpur Consulting Private Limited, India',
      author_email='product@bhojpur-consulting.com',
      license='AGPL-3.0',
      url="https://github.com/bhojpur/ode/",
      download_url='https://github.com/bhojpur/ode/archive/v%s.tar.gz' % version,  # NOQA
      keywords=['ODE.web', 'plugin'],
      install_requires=['ode-web>=1.0.0'],
      python_requires='>=3',
      include_package_data=True,
      zip_safe=False,
      cmdclass=cmdclass,
      )