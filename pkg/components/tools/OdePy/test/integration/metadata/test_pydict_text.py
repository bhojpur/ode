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
Test of the yaml/json parameters file handling
"""

from ode.testlib import ITest
from ode.rtypes import unwrap
from ode.util import pydict_text_io

import pytest
import sys

pythonminver = pytest.mark.skipif(sys.version_info < (2, 7),
                                  reason="requires python2.7")

@pythonminver
class TestPydictTextIo(ITest):

    def getTestJson(self):
        # space after : is optional for json but required for yaml
        return '{"a":2}'

    def getTestYaml(self):
        # quoted strings are optional for yaml but required for json
        return 'a: 2'

    @pytest.mark.parametrize('format', ['yaml', 'Yml'])
    def test_get_format_filename_yaml(self, tmpdir, format):
        f = tmpdir.join('test.%s' % format)
        content = self.getTestYaml()
        f.write(content)
        rawdata, filetype = pydict_text_io.get_format_filename(
            str(f), None)
        assert filetype == 'yaml'
        assert rawdata == content

    @pytest.mark.parametrize('format', ['json', 'JS'])
    def test_get_format_filename_json(self, tmpdir, format):
        f = tmpdir.join('test.%s' % format)
        content = self.getTestJson()
        f.write(content)
        rawdata, filetype = pydict_text_io.get_format_filename(
            str(f), None)
        assert filetype == 'json'
        assert rawdata == content

    # Mime-type overrides extension

    @pytest.mark.parametrize('format', [
        ('json', 'application/x-yaml'), ('yaml', 'application/x-yaml'),
        ('yaml', '')])
    def test_get_format_originalfileid_yaml(self, format):
        content = self.getTestYaml()
        fa = self.make_file_annotation(
            name='test.%s' % format[0], binary=content, mimetype=format[1])
        fid = unwrap(fa.file.id)
        print fid, unwrap(fa.file.mimetype)
        retdata, rettype = pydict_text_io.get_format_originalfileid(
            fid, None, self.client.getSession())
        assert rettype == 'yaml'
        assert retdata == content

    @pytest.mark.parametrize('format', [
        ('json', 'application/json'), ('yaml', 'application/json'),
        ('json', '')])
    def test_get_format_originalfileid_json(self, format):
        content = self.getTestJson()
        fa = self.make_file_annotation(
            name='test.%s' % format[0], binary=content, mimetype=format[1])
        fid = unwrap(fa.file.id)
        retdata, rettype = pydict_text_io.get_format_originalfileid(
            fid, None, self.client.getSession())
        assert rettype == 'json'
        assert retdata == content

    @pytest.mark.parametrize('remote', [True, False])
    @pytest.mark.parametrize('format', ['json', 'yaml'])
    def test_load(self, tmpdir, remote, format):
        if format == 'json':
            content = self.getTestJson()
        else:
            content = self.getTestYaml()

        if remote:
            fa = self.make_file_annotation(
                name='test.%s' % format, binary=content, mimetype=format)
            fid = unwrap(fa.file.id)
            fileobj = 'OriginalFile:%d' % fid
        else:
            f = tmpdir.join('test.%s' % format)
            f.write(content)
            fileobj = str(f)

        data = pydict_text_io.load(
            fileobj, session=self.client.getSession())
        assert data == {'a': 2}

    def test_load_fromstring(self):
        content = self.getTestJson()
        data = pydict_text_io.load(content)
        assert data == {'a': 2}

    def test_load_invalidtype(self):
        with pytest.raises(Exception) as excinfo:
            pydict_text_io.load(123)
        assert str(excinfo.value).startswith('Invalid type: ')

    @pytest.mark.parametrize('format', ['json', 'yaml'])
    def test_dump(data, tmpdir, format):
        d = {'a': 2}
        dumpstring = pydict_text_io.dump(d, format)
        f = tmpdir.join('test-dump.%s' % format)
        f.write(dumpstring)
        fileobj = str(f)

        assert pydict_text_io.load(fileobj, format) == d
