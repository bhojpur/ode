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
import sys
import keyword
import logging

# The generateDS package and our generateds module
# collide on case-insensitive file systems.
import generateDS.generateDS
from ode.modeltools.exceptions import ModelProcessingError
from xml import sax
from xml.etree import ElementTree
from ode.modeltools.model import ODEModel

XschemaHandler = generateDS.generateDS.XschemaHandler
set_type_constants = generateDS.generateDS.set_type_constants


def parse(opts):
    """
    Entry point for XML Schema parsing into a Bhojpur ODE Model.
    """
    # The following two statements are required to "prime" the generateDS
    # code and ensure we have reasonable namespace support.
    filenames = opts.args
    namespace = opts.namespace

    schemas = dict()

    logging.debug("Namespace: %s" % namespace)
    set_type_constants(namespace)
    generateDS.generateDS.XsdNameSpace = namespace
    logging.debug("Type map: %s" % opts.lang.type_map)

    parser = sax.make_parser()
    ch = XschemaHandler()
    parser.setContentHandler(ch)
    for filename in filenames:
        parser.parse(filename)

        schemaname = os.path.split(filename)[1]
        schemaname = os.path.splitext(schemaname)[0]
        schema = ElementTree.parse(filename)
        schemas[schemaname] = schema

    root = ch.getRoot()
    if root is None:
        raise ModelProcessingError(
            "No model objects found, have you set the correct namespace?")
    root.annotate()
    return ODEModel.process(ch, schemas, opts)


def reset():
    """
    Since the generateDS module contains many globals and package scoped
    variables we need the ability to reset its state if we are to re-use
    it multiple times in the same process.
    """
    generateDS.generateDS.GenerateProperties = 0
    generateDS.generateDS.UseOldGetterSetter = 0
    generateDS.generateDS.MemberSpecs = None
    generateDS.generateDS.DelayedElements = []
    generateDS.generateDS.DelayedElements_subclass = []
    generateDS.generateDS.AlreadyGenerated = []
    generateDS.generateDS.AlreadyGenerated_subclass = []
    generateDS.generateDS.PostponedExtensions = []
    generateDS.generateDS.ElementsForSubclasses = []
    generateDS.generateDS.ElementDict = {}
    generateDS.generateDS.Force = 0
    generateDS.generateDS.Dirpath = []
    generateDS.generateDS.ExternalEncoding = sys.getdefaultencoding()

    generateDS.generateDS.NamespacesDict = {}
    generateDS.generateDS.Targetnamespace = ""

    generateDS.generateDS.NameTable = {
        'type': 'type_',
        'float': 'float_',
    }
    for kw in keyword.kwlist:
        generateDS.generateDS.NameTable[kw] = '%sxx' % kw

    generateDS.generateDS.SubclassSuffix = 'Sub'
    generateDS.generateDS.RootElement = None
    generateDS.generateDS.AttributeGroups = {}
    generateDS.generateDS.SubstitutionGroups = {}
    #
    # SubstitutionGroups can also include simple types that are
    #   not (defined) elements.  Keep a list of these simple types.
    #   These are simple types defined at top level.
    generateDS.generateDS.SimpleElementDict = {}
    generateDS.generateDS.SimpleTypeDict = {}
    generateDS.generateDS.ValidatorBodiesBasePath = None
    generateDS.generateDS.UserMethodsPath = None
    generateDS.generateDS.UserMethodsModule = None
    generateDS.generateDS.XsdNameSpace = ''