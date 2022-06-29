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

import re

# Default logger configuration
# logging.basicConfig(level=logging.DEBUG,
#                    format='%(asctime)s %(levelname)s %(message)s')

# Types which should be ignored from metadata store, retrieve, etc. code
# generation due either to their incompatibility or complexity as it applies
# to these interfaces and implementations.
METADATA_OBJECT_IGNORE = ['External', 'MapPairs', 'M', 'BinData']

# Type counts which should be ignored from metadata store, retrieve, etc. code
# generation due either to their incompatibility or complexity as it applies
# to these interfaces and implementations.
METADATA_COUNT_IGNORE = {'Annotation': ['AnnotationRef']}

# Types which have not been recognized as explicit defines (XML Schema
# definitions that warrant a the creation of a first class model object) that
# we wish to be treated otherwise. As part of the code generation process they
# will also be confirmed to be top-level types.
EXPLICIT_DEFINE_OVERRIDE = ('EmissionFilterRef', 'ExcitationFilterRef')

# Back references that we do not want in the model either because they
# conflict with other properties or do not make sense.
BACK_REFERENCE_OVERRIDE = {
    'Annotation': ['Annotation'],
    'Event': ['Event'],
}

# Reference properties of a given type for which back reference link methods
# should not be code generated.
BACK_REFERENCE_LINK_OVERRIDE = {
    'AnnotationRef': ['Annotation'],
    'FolderRef': ['Folder'],
    'Pump': ['Laser'],
}

# Back reference instance variable name overrides which will be used in place
# of the standard name translation logic.
BACK_REFERENCE_NAME_OVERRIDE = {
    'FilterSet.ExcitationFilter': 'filterSetExcitationFilter',
    'FilterSet.EmissionFilter': 'filterSetEmissionFilter',
    'LightPath.ExcitationFilter': 'lightPathExcitationFilter',
    'LightPath.EmissionFilter': 'lightPathEmissionFilter',
    'Folder.Folder': 'childFolder',
}

# Back reference class name overrides which will be used when generating
# fully qualified class names.
BACK_REFERENCE_CLASS_NAME_OVERRIDE = {
    'FilterSet.ExcitationFilter': 'FilterSetExcitationFilterLink',
    'FilterSet.EmissionFilter': 'FilterSetEmissionFilterLink',
    'LightPath.ExcitationFilter': 'LightPathExcitationFilterLink',
    'LightPath.EmissionFilter': 'LightPathEmissionFilterLink',
}

# Properties within abstract proprietary types that should be code generated
# for.
COMPLEX_OVERRIDE = ('Transform')
ANNOTATION_OVERRIDE = ('AnnotationRef',)

# The list of properties not to process.
DO_NOT_PROCESS = []  # ["ID"]

# Default root XML Schema namespace
DEFAULT_NAMESPACE = "xsd:"

# The package regular expression for Bhojpur ODE namespaces.
PACKAGE_NAMESPACE_RE = re.compile(
    r'http://www.bhojpur.net/Schemas/(\w+)/\d+-\w+')

REF_REGEX = re.compile(r'Ref$|RefNode$')

BACKREF_REGEX = re.compile(r'_BackReference')

p = r'^([A-Z]{1})[a-z0-9]+|([A-Z0-9]+)[A-Z]{1}[a-z]+|([A-Z]+)[0-9]*|([a-z]+$)'
PREFIX_CASE_REGEX = re.compile(p)

ENUM_HANDLERS = {
    'AcquisitionMode': [
        ('.*Widefield.*', 'WideField'),
        ('^Laser Scan Confocal$', 'LaserScanningConfocalMicroscopy'),
        ('^Swept Field Confocal$', 'SweptFieldConfocal')
    ],
    'Correction': [
        ('.*Pl.*Apo.*', 'PlanApo'),
        ('.*Pl.*Flu.*', 'PlanFluor'),
        ('^\\s*Vio.*Corr.*', 'VioletCorrected'),
        ('.*S.*Flu.*', 'SuperFluor'),
        ('.*Neo.*flu.*', 'Neofluar'),
        ('.*Flu.*tar.*', 'Fluotar'),
        ('.*Fluo.*', 'Fluor'),
        ('.*Flua.*', 'Fluar'),
        ('^\\s*Apo.*', 'Apo')
    ],
    'DetectorType': [
        ('.*EM.*CCD.*', 'EM-CCD'),
        ('.*CCD.*', 'CCD'),
        ('.*CMOS.*', 'CMOS')
    ],
    'Immersion': [
        ('^\\s*Dry\\s*', 'Air'),
        ('^\\s*OI\\s*', 'Oil'),
        ('.*Oil.*', 'Oil'),
        ('.*Oel.*', 'Oil'),
        ('.*Wasser.*', 'Water'),
        ('.*Gly.*', 'Glycerol'),
        ('^\\s*Wl\\s*', 'Water'),
        ('^\\s*W\\s*', 'Water')
    ]
}