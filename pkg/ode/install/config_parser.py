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
Parser for the ode.properties file to generate RST
mark up.
"""

from __future__ import print_function

from future.utils import bytes_to_native_str
from future.utils import isbytes
from past.builtins import cmp
from builtins import str
from builtins import object

class Header(object):
    def __init__(self, name, reference=None, description=""):
        """Initialize new configuration property"""
        self.name = name
        self.reference = reference
        self.description = description

    def get_reference(self):

        if not self.reference:
            return self.name.lower()
        else:
            return self.reference

DB_HEADER = Header("Database", reference="db")
FS_HEADER = Header("Binary repository", reference="fs")
GRID_HEADER = Header("Grid", reference="grid")
ICE_HEADER = Header("Ice", reference="ice")
LDAP_HEADER = Header("LDAP", reference="ldap")
JVM_HEADER = Header("JVM", reference="jvm")
MISC_HEADER = Header("Misc", reference="misc")
PERFORMANCE_HEADER = Header("Performance", reference="performance")
SCRIPTS_HEADER = Header("Scripts", reference="scripts")
SECURITY_HEADER = Header("Security", reference="security")

HEADER_MAPPING = {
    "ode.data": FS_HEADER,
    "ode.db": DB_HEADER,
    "ode.cluster": GRID_HEADER,
    "ode.grid": GRID_HEADER,
    "ode.checksum": FS_HEADER,
    "ode.fs": FS_HEADER,
    "ode.managed": FS_HEADER,
    "ode.ldap": LDAP_HEADER,
    "ode.jvmcfg": JVM_HEADER,
    "ode.sessions": PERFORMANCE_HEADER,
    "ode.threads": PERFORMANCE_HEADER,
    "ode.throttling": PERFORMANCE_HEADER,
    "ode.launcher": SCRIPTS_HEADER,
    "ode.process": SCRIPTS_HEADER,
    "ode.scripts": SCRIPTS_HEADER,
    "ode.security": SECURITY_HEADER,
    "ode.resetpassword": SECURITY_HEADER,
    "ode.upgrades": MISC_HEADER,
    "Ice": ICE_HEADER,
}

TOP = \
    """.. This file is auto-generated from ode.properties. DO NOT EDIT IT

Configuration properties glossary
=================================

.. contents::
  :depth: 1
  :local:

.. _introduction_configuration:

Introduction
------------

The primary form of configuration is via the use of key/value properties,
stored in :file:`etc/grid/config.xml` and read on server startup. Backing up
and copying these properties is as easy as copying this file to a new server
version.

The :source:`etc/ode.properties` file of your distribution defines all the
default configuration properties used by the server. Changes made to the file
are *not* recognized by the server. Instead, configuration options can be set
using the :program:`ode config set` command:

::

    $ ode config set <parameter> <value>

When supplying a value with spaces or multiple elements, use **single
quotes**. The quotes will not be saved as part of the value (see below).

To remove a configuration option (to return to default values where
mentioned), simply omit the value:

::

    $ ode config set <parameter>

These options will be stored in a file: ``etc/grid/config.xml`` which
you can read for reference. **DO NOT** edit this file directly.

You can also review all your settings by using:

::

    $ ode config get

which should return values without quotation marks.

A final useful option of :program:`ode config edit` is:

::

    $ ode config edit

which will allow for editing the configuration in a system-default text
editor.

.. note::
    Please use the **escape sequence** ``\\"`` for nesting double quotes (e.g.
    ``"[\\"foo\\", \\"bar\\"]"``) or wrap with ``'`` (e.g. ``'["foo",
    "bar"]'``).

Examples of doing this are on the
:doc:`server installation page <unix/server-installation>`, as well as the
:doc:`LDAP installation <server-ldap>` page.

.. _core_configuration:

Mandatory properties
--------------------

The following properties need to be correctly set for all installations of the
ODE.server. Depending on your set-up, default values may be sufficient.

- :property:`ode.data.dir`
- :property:`ode.db.host`
- :property:`ode.db.name`
- :property:`ode.db.pass`
"""

HEADER = \
    """.. _%(reference)s_configuration:

%(header)s
%(hline)s

%(properties)s"""

EXCLUDE_LIST = ("##", "versions", "ode.upgrades", "ode.version")

STOP = "### END"

import os
import argparse
import fileinput
import logging
import warnings

from collections import defaultdict

def fail(msg="", debug=0):
    if debug:
        import pdb
        pdb.set_trace()
    else:
        raise Exception(msg)

def dbg(msg):
    logging.debug(msg)

def underline(size):
    """Create underline for reStructuredText headings"""
    return '^' * size

class Property(object):

    def __init__(self, key=None, val=None, txt=""):
        """Initialize new configuration property"""
        self.key = key
        self.val = val
        self.txt = txt

    def append(self, line):
        """Append line to property description"""
        dbg("append:" + line)
        self.txt += line
        self.txt += "\n"

    def detect(self, line):
        dbg("detect:" + line)
        idx = line.index("=")
        self.key = line[0:idx]
        self.val = line[idx + 1:]

    def cont(self, line):
        dbg("cont:  " + line)
        if self.key is None:
            fail("key is none on line: " + line)
        self.val += line

    def __str__(self):
        return ("Property(key='%s', val='%s', txt='%s')"
                % (self.key, self.val, self.txt))

IN_PROGRESS = "in_progress_action"
ESCAPED = "escaped_action"

class PropertyParser(object):

    def __init__(self):
        """Initialize a property set"""
        self.properties = []
        self.curr_p = None
        self.curr_a = None

    def parse_file(self, argv=None):
        """Parse the properties from the input configuration file"""
        try:
            self.parse_lines(fileinput.input(argv))
        finally:
            fileinput.close()
        return self.properties

    def parse_lines(self, lines):
        """Parse the properties from the given configuration file lines"""
        for line in lines:
            if isbytes(line):
                line = bytes_to_native_str(line)
            if line.endswith("\n"):
                line = line[:-1]

            if line.startswith(STOP):
                self.cleanup()
                break
            if self.is_excluded(line):
                self.cleanup()
                continue
            elif not line.strip():
                self.cleanup()
                continue
            elif line.startswith("#"):
                self.append(line)
            elif "=" in line and self.curr_a != ESCAPED:
                self.detect(line)
            elif line.endswith("\\"):
                self.cont(line[:-1])
            else:
                self.cont(line)
        self.cleanup()  # Handle no newline at end of file
        return self.properties

    def black_list(self, line):
        warnings.warn(
            "This method is deprecated. Use is_excluded instead",
            DeprecationWarning)
        return self.is_excluded(line)

    def is_excluded(self, line):
        for x in EXCLUDE_LIST:
            if line.startswith(x):
                return True

    def cleanup(self):
        if self.curr_p is not None:
            if self.curr_p.key is not None:  # Handle ending '####'
                self.properties.append(self.curr_p)
                self.curr_p = None
                self.curr_a = None

    def init(self):
        if self.curr_p is None:
            self.curr_p = Property()

    def ignore(self):
        self.cleanup()

    def append(self, line):
        self.init()
        # Assume line starts with "# " and strip
        self.curr_p.append(line[2:])

    def detect(self, line):
        if line.endswith("\\"):
            line = line[:-1]
            self.curr_a = ESCAPED
        else:
            self.cleanup()
            self.curr_a = IN_PROGRESS

        self.init()
        self.curr_p.detect(line)
        if self.curr_a != ESCAPED:
            self.cleanup()

    def cont(self, line):
        self.curr_p.cont(line)

    def __iter__(self):
        return iter(self.properties)

    def data(self):
        data = defaultdict(list)
        for x in self:
            if x.key is None:
                raise Exception("Bad key: %s" % x)
            parts = x.key.split(".")
            if parts[0] == "Ice":
                continue
            if parts[0] != "ode":
                raise Exception("Bad key: %s" % x)

            parts = parts[1:]
            data[parts[0]].append(".".join(parts[1:]))
        return data

    def parse_module(self, module='engine.settings'):
        """Parse the properties from the setting module"""

        os.environ['DJANGO_SETTINGS_MODULE'] = module

        from django.conf import settings

        for key, values in sorted(
                iter(list(settings.CUSTOM_SETTINGS_MAPPINGS.items())),
                key=lambda k: k):

            p = Property()
            global_name, default_value, mapping, description, config = \
                tuple(values)
            p.val = str(default_value)
            p.key = key
            p.txt = (description or "") + "\n"
            self.properties.append(p)

    def headers(self):
        headers = defaultdict(list)
        additional_headers = {}
        for x in self:
            found = False
            for header in HEADER_MAPPING:
                if x.key.startswith(header):
                    headers.setdefault(HEADER_MAPPING[header], []).append(x)
                    found = True
                    break

            if not found and x.key.startswith('ode.'):
                parts = x.key.split(".")
                section = parts[1].title()
                if section not in additional_headers:
                    additional_headers[section] = Header(section)
                headers.setdefault(additional_headers[section], []).append(x)

        for key in list(headers.keys()):
            headers[key].sort(key=lambda x: x.key)
        return headers

    def print_defaults(self):
        """Print all keys and their default values"""
        values = ["%s=%s" % (p.key, p.val) for p in self]
        for x in sorted(values):
            print(x)

    def print_keys(self):
        """Print all keys"""
        data = self.data()
        for k, v in sorted(data.items()):
            print("%s (%s)" % (k, len(v)))
            for i in v:
                print("\t", i)

    def print_headers(self):
        """Print headers and number of keys"""
        headers = self.headers()
        for k, v in sorted(list(headers.items()), key=lambda x: x[0].name):
            print("%s (%s)" % (k.name, len(v)))

    def print_rst(self):
        """Print configuration in reStructuredText format"""
        print(TOP)
        headers = self.headers()
        for header in sorted(headers, key=lambda x: x.name):
            properties = ""
            # Filter properties marked as DEVELOPMENT
            props = [p for p in headers[header] if
                     not p.txt.startswith('DEVELOPMENT')]
            for p in props:
                properties += ".. property:: %s\n" % (p.key)
                properties += "\n"
                properties += "%s\n" % p.key
                properties += "%s\n" % underline(len(p.key))
                for line in p.txt.split("\n"):
                    if line:
                        if isbytes(line):
                            line = bytes_to_native_str(line)
                        properties += "%s\n" % (line)
                    else:
                        properties += "\n"
                v = p.val
                if not p.val:
                    v = "[empty]"

                v = v.replace('"', '\\\\"')
                if ',' in v and ', ' not in v:
                    properties += "Default: `%s`\n\n" % (
                        ",\n".join(v.split(',')))
                else:
                    properties += "Default: `%s`\n\n" % v

            hline = "-" * len(header.name)
            m = {"header": header.name,
                 "hline": hline,
                 "properties": properties,
                 "reference": header.get_reference()}
            print(HEADER % m)

if __name__ == "__main__":
    ap = argparse.ArgumentParser()
    g = ap.add_mutually_exclusive_group()
    g.add_argument("--rst", action="store_true")
    g.add_argument("--dbg", action="store_true")
    g.add_argument("--keys", action="store_true")
    g.add_argument("--headers", action="store_true")
    ap.add_argument("files", nargs="+")
    ns = ap.parse_args()

    if ns.dbg:
        logging.basicConfig(level=10)
    else:
        logging.basicConfig(level=20)

    pp = PropertyParser()
    pp.parse_file(ns.files)
    pp.parse_module('engine.settings')

    if ns.dbg:
        print("Found:", len(list(pp)))

    elif ns.keys:
        pp.print_keys()
    elif ns.headers:
        pp.print_headers()
    elif ns.rst:
        pp.print_rst()
    else:
        raise Exception(ns)