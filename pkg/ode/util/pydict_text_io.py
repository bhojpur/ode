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
Read text-based dictionary file formats such as YAML and JSON
"""

from past.builtins import basestring
import os
import json
import re
from ode.rtypes import unwrap
from future.utils import bytes_to_native_str
import yaml

def get_supported_formats():
    """
    Return the supported formats
    """
    return ('json', 'yaml')

def load(fileobj, filetype=None, single=True, session=None):
    """
    Try and load a file in a format that is convertible to a Python dictionary

    fileobj: Either a single json object string, file-path, or OriginalFile:ID
    single: If True file should only contain a single document, otherwise a
        list of documents will always be returned. Multiple documents are not
        supported for JSON strings.
    session: If fileobj is an OriginalFile:ID a valid session is required
    """

    if not isinstance(fileobj, basestring):
        raise Exception(
            'Invalid type: fileobj must be a filename or json string')

    try:
        data = json.loads(fileobj)
        if isinstance(data, dict):
            if single:
                return data
            return [data]
    except ValueError:
        pass

    m = re.match(r'originalfile:(\d+)$', fileobj, re.I)
    if m:
        rawdata, filetype = get_format_originalfileid(
            int(m.group(1)), filetype, session)
    else:
        rawdata, filetype = get_format_filename(fileobj, filetype)

    if filetype == 'yaml':
        data = list(yaml.safe_load_all(rawdata))
        if single:
            if len(data) != 1:
                raise Exception(
                    "Expected YAML file with one document, found %d" %
                    len(data))
            return data[0]
        return data

    if filetype == 'json':
        try:
            data = json.loads(rawdata)
        except TypeError:
            # for Python 3.5
            data = json.loads(bytes_to_native_str(rawdata))
        if single:
            return data
        return [data]

def dump(data, formattype):
    """
    Convert a python object to a string in the requested format

    data: A python object (most likely a dictionary)
    formattype: The output format
    """

    if formattype == 'yaml':
        return yaml.dump(data)

    if formattype == 'json':
        return json.dumps(data)

    raise ValueError('Unknown format: %s' % formattype)

def _format_from_name(filename):
    # splitext includes the dot on the extension
    ext = os.path.splitext(filename)[1].lower()[1:]
    if ext in ('yml', 'yaml'):
        return 'yaml'
    if ext in ('js', 'json'):
        return 'json'

def get_format_filename(filename, filetype):
    """Returns bytes from the named json or yaml file."""
    if not filetype:
        filetype = _format_from_name(filename)
    if filetype not in ('json', 'yaml'):
        raise ValueError('Unknown file format: %s' % filename)
    with open(filename, 'rb') as f:
        rawdata = f.read()
    return rawdata, filetype

def get_format_originalfileid(originalfileid, filetype, session):
    if not session:
        raise ValueError(
            'Bhojpur ODE session required: OriginalFile:%d' % originalfileid)
    f = session.getQueryService().get('OriginalFile', originalfileid)
    if not filetype:
        try:
            mt = unwrap(f.getMimetype()).lower()
        except AttributeError:
            mt = ''
        if mt == 'application/x-yaml':
            filetype = 'yaml'
        if mt == 'application/json':
            filetype = 'json'
    if not filetype:
        filetype = _format_from_name(unwrap(f.getName()))
    if filetype not in ('json', 'yaml'):
        raise ValueError(
            'Unknown file format: OriginalFile:%d' % originalfileid)

    rfs = session.createRawFileStore()
    try:
        rfs.setFileId(originalfileid)
        rawdata = rfs.read(0, rfs.size())
        return rawdata, filetype
    finally:
        rfs.close()