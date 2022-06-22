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
   Integration test for adding annotations to Project.
"""

from ode.testlib import ITest
import ode
import ode.scripts
from ode.rtypes import rstring, rbool, rtime, rlong, rdouble
import random
from datetime import datetime


class TestFigureExportScripts(ITest):

    def testAddAnnotations(self):

        # root session is root.sf
        session = self.root.sf
        updateService = session.getUpdateService()
        queryService = session.getQueryService()

        # create project
        parent = self.make_project(name="Annotations Test", client=self.root)

        xml = "<testXml><testElement><annotation>\
            Text</annotation></testElement></testXml>"
        doubleVal = random.random()
        timeVal = datetime.now().microsecond
        addTag(
            updateService, parent, "Test-Tag",
            ns="test/ode/tag/ns",
            description=None)
        addComment(
            updateService, parent, "Test-Comment",
            ns="test/ode/comment/ns",
            description=None)
        addXmlAnnotation(
            updateService, parent, xml,
            ns="test/ode/xml/ns",
            description="Test xml annotation description")
        addBooleanAnnotation(
            updateService, parent, True,
            ns="test/ode/boolean/ns",
            description="True if True, otherwise False")
        addDoubleAnnotation(
            updateService, parent, doubleVal,
            ns="test/ode/double/ns",
            description="Random number!")
        addLongAnnotation(
            updateService, parent, 123456,
            ns="test/ode/long/ns",
            description=None)
        addTermAnnotation(
            updateService, parent, "Metaphase",
            ns="test/ode/term/ns",
            description="Metaphase is part of mitosis")
        addTimestampAnnotation(
            updateService, parent, timeVal,
            ns="test/ode/timestamp/ns",
            description=None)

        annValues = {"test/ode/tag/ns": ["Test-Tag", "getTextValue"],
                     "test/ode/comment/ns": ["Test-Comment", "getTextValue"],
                     "test/ode/xml/ns": [xml, "getTextValue"],
                     "test/ode/boolean/ns": [True, "getBoolValue"],
                     "test/ode/double/ns": [doubleVal, "getDoubleValue"],
                     "test/ode/long/ns": [123456, "getLongValue"],
                     "test/ode/term/ns": ["Metaphase", "getTermValue"],
                     "test/ode/timestamp/ns": [timeVal, "getTimeValue"]}

        # retrieve the annotations and check values.
        p = ode.sys.Parameters()
        p.map = {}
        p.map["pid"] = parent.getId()
        query = "select l from ProjectAnnotationLink as l join\
            fetch l.child as a where l.parent.id=:pid and a.ns=:ns"
        for ns, values in annValues.items():
            p.map["ns"] = rstring(ns)
            # only 1 of each namespace
            link = queryService.findByQuery(query, p)
            valueMethod = getattr(link.child, values[1])
            assert values[0] == valueMethod().getValue(),\
                "Annotation %s value %s not equal to set value %s"\
                % (link.child.__class__, valueMethod().getValue(), values[0])


def saveAndLinkAnnotation(
        updateService, parent, annotation, ns=None, description=None):
    """
    Saves the Annotation and Links it to a Project, Dataset or Image

    """

    if ns:
        annotation.setNs(rstring(ns))
    if description:
        annotation.setDescription(rstring(description))
    annotation = updateService.saveAndReturnObject(annotation)
    if type(parent) == ode.model.DatasetI:
        l = ode.model.DatasetAnnotationLinkI()
    elif type(parent) == ode.model.ProjectI:
        l = ode.model.ProjectAnnotationLinkI()
    elif type(parent) == ode.model.ImageI:
        l = ode.model.ImageAnnotationLinkI()
    else:
        return
    parent = parent.__class__(parent.id.val, False)
    l.setParent(parent)
    l.setChild(annotation)
    return updateService.saveAndReturnObject(l)


# Text Annotations
def addTag(
        updateService, parent, text, ns=None, description=None):
    """ Adds a Tag. """
    child = ode.model.TagAnnotationI()
    child.setTextValue(rstring(text))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)


def addComment(
        updateService, parent, text, ns=None, description=None):
    """ Adds a Comment. """
    child = ode.model.CommentAnnotationI()
    child.setTextValue(rstring(text))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)


def addXmlAnnotation(
        updateService, parent, xmlText, ns=None, description=None):
    """ Adds XML Annotation. """
    child = ode.model.XmlAnnotationI()
    child.setTextValue(rstring(xmlText))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)


# Basic Annotations
def addBooleanAnnotation(
        updateService, parent, boolean, ns=None, description=None):
    """ Adds a Boolean Annotation. """
    child = ode.model.BooleanAnnotationI()
    child.setBoolValue(rbool(boolean))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)


def addDoubleAnnotation(
        updateService, parent, double, ns=None, description=None):
    """ Adds a Double Annotation. """
    child = ode.model.DoubleAnnotationI()
    child.setDoubleValue(rdouble(double))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)


def addLongAnnotation(
        updateService, parent, longValue, ns=None, description=None):
    """ Adds a Long Annotation. """
    child = ode.model.LongAnnotationI()
    child.setLongValue(rlong(longValue))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)


def addTermAnnotation(
        updateService, parent, term, ns=None, description=None):
    """ Adds a Term Annotation. """
    child = ode.model.TermAnnotationI()
    child.setTermValue(rstring(term))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)


def addTimestampAnnotation(
        updateService, parent, timeValue, ns=None, description=None):
    """ Adds a Timestamp Annotation. """
    child = ode.model.TimestampAnnotationI()
    child.setTimeValue(rtime(timeValue))
    saveAndLinkAnnotation(updateService, parent, child, ns, description)
