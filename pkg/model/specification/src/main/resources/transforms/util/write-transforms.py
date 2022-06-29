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

# Bhojpur ODE Data Model transforms

from collections import defaultdict
from os import listdir
from re import compile

# note the schemas (in reverse order) and the transforms among them

schemas = []
transforms = defaultdict(list)

# report the quality of a single transform

def quality(from_schema, to_schema):
    if from_schema < to_schema:
        return 4
    elif to_schema < '2008':
        return 1
    elif to_schema < '2010-06':
        return 2
    else:
        return 3

# find the shortest sequence of transforms of sufficient quality

def shortest_path(from_schema, to_schema, min_quality):
    paths = [[from_schema]]
    while True:
        # any paths lead to the goal?
        for path in paths:
            if path[-1] == to_schema:  # yes, goal path found
                return path

        # extend all the current paths
        next_paths = []
        for path in paths:
            curr_step = path[-1]
            for next_step in transforms[curr_step]:
                if quality(curr_step, next_step) >= min_quality and \
                        next_step not in path:
                    next_path = list(path)
                    next_path.append(next_step)
                    next_paths.append(next_path)

        if not next_paths:  # no more transforms to try
            return None
        paths = next_paths

# the style of transform file names

name_pattern = compile('^(.+)\-to\-(.+)\.xsl$')

# scan the current directory to determine the schemas and transforms

def load_transforms():
    global transforms
    global schemas
    seen = set()

    for name in listdir('.'):
        match = name_pattern.match(name)
        if match:
            from_schema = match.group(1)
            to_schema = match.group(2)

            transforms[from_schema].append(to_schema)
            seen.add(from_schema)
            seen.add(to_schema)

    schemas = list(seen)
    schemas.sort(reverse=True)

# note of the best sequence of transforms among the schemas

best_paths = {}

# determine the best sequences of transforms among the schemas

load_transforms()

for from_schema in schemas:
    for to_schema in schemas:
        if from_schema != to_schema:
            for min_quality in range(4, 0, -1):
                path = shortest_path(from_schema, to_schema, min_quality)
                if path:
                    best_paths[(from_schema, to_schema)] = (path, min_quality)
                    break
            if not best_paths[(from_schema, to_schema)]:
                raise Exception(
                    'no path from ' + from_schema + ' to ' + to_schema)


# name the quality levels of transforms

qualities = ['poor', 'fair', 'good', 'excellent']

# print in XML a sequence of transforms to the given schema

def print_path(to_schema):
    (path, min_quality) = best_paths[(from_schema, to_schema)]
    print ('\t\t\t<target schema="' + to_schema + \
        '" quality="' + qualities[min_quality - 1] + '">')
    while len(path) > 1:
        print ('\t\t\t\t<transform file="' + \
            path[0] + '-to-' + path[1] + '.xsl"/>')
        path = path[1:]
    print('\t\t\t</target>')


# print in XML all the transforms among the schemas

print("""<?xml version = "1.0" encoding = "UTF-8"?>
<!--
  #%L
  Bhojpur ODE Data Model transforms
  %%
 Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
  %%
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
  -->
""")

print('<ode-transforms current="' + schemas[0] + '">')

for from_schema in schemas:
    print('\t<source schema="' + from_schema + '">')
    print('\t\t<upgrades>')
    upgrades = True
    for to_schema in schemas:
        if from_schema < to_schema:
            print_path(to_schema)
        elif to_schema < from_schema:
            if upgrades:
                upgrades = False
                print('\t\t</upgrades>')
                print('\t\t<downgrades>')
            print_path(to_schema)
    if upgrades:
        print('\t\t</upgrades>')
        print('\t\t<downgrades>')
    print('\t\t</downgrades>')
    print('\t</source>')

print('</ode-transforms>')