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
Generator for Genshi templates of the Units
"""

import os
import fileinput

from collections import namedtuple
from genshi.template import MarkupTemplate
from genshi.template import NewTextTemplate
from argparse import ArgumentParser

equations_file = "odesym/equations.py"
if os.path.exists(equations_file):
    exec(open(equations_file).read()) # Sets EQUATIONS
try:
    EQUATIONS.get("test")
except:
    EQUATIONS = {}
Unit = namedtuple('Unit', ['CODE', 'SYMBOL', 'SYSTEM'])
Field = namedtuple('Field', ['CLASS', 'NAME', 'TYPE', 'DEFAULT'])

class Engine(object):

    def __init__(self, template_name, markup):
        self.template_name = template_name
        self.markup = markup
        with open(self.template_name) as f:
            self.template_text = f.read()
        self.fields = self.parse("units/Fields.txt", Type=Field)

    def basename(self, data_filename):
        basename = os.path.basename(data_filename)
        basename = basename[:-4]  # Remove ".txt"
        return basename

    def parse(self, data_filename, Type=Unit):
        data = []
        for line in fileinput.input([data_filename]):
            line = line.strip()
            if line:
                line = line.encode().decode('UTF-8')
                items = tuple(line.strip().split("\t"))
                try:
                    i = Type(*items)
                except:
                    raise Exception(items)
                data.append(i)
        return data

    def render(self, **kwargs):
        markup = kwargs.pop("markup")
        if markup:
            tmpl = MarkupTemplate(
                self.template_text, encoding='UTF-8')
        else:
            tmpl = NewTextTemplate(
                self.template_text, encoding='UTF-8')
        content = tmpl.generate(**kwargs)
        print(content.render())

    def combined_template(self, data_filenames):
        items = dict()
        for data_filename in data_filenames:
            basename = self.basename(data_filename)
            data = self.parse(data_filename)
            items[basename] = data
        self.render(markup=self.markup,
                    items=items, fields=self.fields,
                    equations=EQUATIONS)

    def individual_templates(self, data_filename):
        basename = self.basename(data_filename)
        data = self.parse(data_filename)
        key = basename.upper()
        val = EQUATIONS[key]
        self.render(markup=self.markup,
                    name=basename, items=data, fields=self.fields,
                    equations=val)

if __name__ == "__main__":
    parser = ArgumentParser()
    parser.add_argument(
        "--combine",
        action="store_true",
        help="Combine all files into a single call")
    parser.add_argument(
        "--markup",
        action="store_true",
        help="Use XML markup rather than the text template")
    parser.add_argument(
        "template_name")
    parser.add_argument(
        "data_filenames",
        nargs="+")
    ns = parser.parse_args()
    if ns.combine:
        engine = Engine(ns.template_name, markup=ns.markup)
        engine.combined_template(ns.data_filenames)
    else:
        for data_filename in ns.data_filenames:
            engine = Engine(ns.template_name, markup=ns.markup)
            engine.individual_templates(data_filename)