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

import pytest
import ctypes
import ode
import ode.clients
from ode.rtypes import rint, rlong, rstring, rdouble, unwrap
from engine.webgateway.marshal import shapeMarshal
from engine.webgateway.marshal import rgb_int2css
from engine.webgateway.marshal import rgb_int2rgba

@pytest.fixture(scope='module')
def default_id():
    return TestShapeMarshal.DEFAULT_ID

@pytest.fixture(scope='function')
def basic_line(default_id):
    shape = ode.model.LineI()
    shape.id = rlong(default_id)
    shape.x1 = rdouble(0.0)
    shape.y1 = rdouble(1.0)
    shape.x2 = rdouble(2.0)
    shape.y2 = rdouble(3.0)
    # r=17,g=34,b=51,a=255
    shape.strokeColor = rint(ctypes.c_int(0x112233FF).value)
    return shape


@pytest.fixture(scope='function')
def basic_arrow(basic_line):
    basic_line.markerStart = rstring('Arrow')
    basic_line.markerEnd = rstring('Arrow')
    return basic_line


@pytest.fixture(scope='function', params=[
    # ODE-XML version of the points
    '1,2 2,3 4,5',
    # ODE.insight version of the points
    'points[1,2 2,3 4,5] points1[1,2 2,3 4,5] '
    'points2[1,2 2,3 4,5] mask[0,0,0]'
])
def basic_polyline(request, default_id):
    points = request.param
    shape = ode.model.PolylineI()
    shape.id = rlong(default_id)
    shape.points = rstring(points)
    # r=17,g=34,b=51,a=0
    shape.strokeColor = rint(ctypes.c_int(0x11223300).value)
    return shape


@pytest.fixture(scope='function', params=[
    # ODE-XML version of the points
    '1.5,2.5 2,3 4.1,5.1',
    # ODE.insight version of the points
    'points[1.5,2.5 2,3 4.1,5.1] points1[1.5,2.5 2,3 4.1,5.1] '
    'points2[1.5,2.5 2,3 4.1,5.1] mask[0,0,0]'
])
@pytest.fixture(scope='function')
def float_polyline(request, default_id):
    points = request.param
    shape = ode.model.PolylineI()
    shape.id = rlong(default_id)
    shape.points = rstring(points)
    return shape


@pytest.fixture(scope='function', params=[
    # ODE-XML version of the points
    '1,2 2,3 4,5',
    # ODE.insight version of the points
    'points[1,2 2,3 4,5] points1[1,2 2,3 4,5] '
    'points2[1,2 2,3 4,5] mask[0,0,0]'
])
@pytest.fixture(scope='function')
def basic_polygon(request, default_id):
    points = request.param
    shape = ode.model.PolygonI()
    shape.id = rlong(default_id)
    shape.points = rstring(points)
    return shape


@pytest.fixture(scope='function')
def empty_polygon(default_id):
    shape = ode.model.PolygonI()
    shape.id = rlong(default_id)
    shape.points = rstring('')
    return shape


@pytest.fixture(scope='function')
def basic_point(default_id):
    shape = ode.model.PointI()
    shape.id = rlong(default_id)
    shape.x = rdouble(0.0)
    shape.y = rdouble(.1)
    return shape


@pytest.fixture(scope='function')
def basic_ellipse(default_id):
    shape = ode.model.EllipseI()
    shape.id = rlong(default_id)
    shape.x = rdouble(0.0)
    shape.y = rdouble(.1)
    shape.radiusX = rdouble(1.0)
    shape.radiusY = rdouble(.5)
    # r=17,g=34,b=51,a=68
    shape.fillColor = rint(ctypes.c_int(0x11223344).value)
    # r=255,g=254,b=253,a=252 (i.e. negative int value = -66052)
    shape.strokeColor = rint(ctypes.c_int(0xfffefdfc).value)
    return shape


class TestShapeMarshal(object):
    """
    Tests to ensure that ODE-XML model and ODE.insight shape point
    parsing are supported correctly.
    """

    DEFAULT_ID = 1

    def assert_marshal(self, marshaled, type):
        assert marshaled['type'] == type
        assert marshaled['id'] == self.DEFAULT_ID

    def test_line_marshal(self, basic_line):
        marshaled = shapeMarshal(basic_line)
        self.assert_marshal(marshaled, 'Line')
        assert marshaled['x1'] == 0
        assert marshaled['y1'] == 1
        assert marshaled['x2'] == 2
        assert marshaled['y2'] == 3

    def test_arrow_marshal(self, basic_arrow):
        marshaled = shapeMarshal(basic_arrow)
        self.assert_marshal(marshaled, 'Line')
        assert marshaled['markerStart'] == 'Arrow'
        assert marshaled['markerEnd'] == 'Arrow'

    def test_polyline_marshal(self, basic_polyline):
        marshaled = shapeMarshal(basic_polyline)
        self.assert_marshal(marshaled, 'PolyLine')
        assert 'M 1 2 L 2 3 L 4 5' == marshaled['points']

    def test_polyline_float_marshal(self, float_polyline):
        marshaled = shapeMarshal(float_polyline)
        self.assert_marshal(marshaled, 'PolyLine')
        assert 'M 1.5 2.5 L 2 3 L 4.1 5.1' == marshaled['points']

    def test_polygon_marshal(self, basic_polygon):
        marshaled = shapeMarshal(basic_polygon)
        self.assert_marshal(marshaled, 'Polygon')
        assert 'M 1 2 L 2 3 L 4 5 z' == marshaled['points']

    def test_unrecognised_roi_shape_points_string(self, empty_polygon):
        marshaled = shapeMarshal(empty_polygon)
        assert ' z' == marshaled['points']

    def test_point_marshal(self, basic_point):
        marshaled = shapeMarshal(basic_point)
        self.assert_marshal(marshaled, 'Point')
        assert 0.0 == marshaled['x']
        assert 0.1 == marshaled['y']

    def test_ellipse_marshal(self, basic_ellipse):
        marshaled = shapeMarshal(basic_ellipse)
        self.assert_marshal(marshaled, 'Ellipse')
        assert 0.0 == marshaled['x']
        assert 0.1 == marshaled['y']
        assert 1.0 == marshaled['radiusX']
        assert 0.5 == marshaled['radiusY']

    def test_rgba(self, basic_ellipse, basic_line, basic_polyline):
        color = unwrap(basic_ellipse.getFillColor())  # 0x11223344
        result = rgb_int2rgba(color)
        assert result[0] == 17               # r
        assert result[1] == 34               # g
        assert result[2] == 51               # b
        assert result[3] == float(68) / 255  # a (as fraction)

        color = unwrap(basic_ellipse.getStrokeColor())  # 0xfffefdfc
        result = rgb_int2rgba(color)           # int rgb
        assert result[0] == 255                # r
        assert result[1] == 254                # g
        assert result[2] == 253                # b
        assert result[3] == float(252) / 255   # a (as fraction)
        result = rgb_int2css(color)            # hex rgb
        assert result[0] == "#fffefd"          # rgb
        assert result[1] == float(252) / 255   # a (as fraction)

        color = unwrap(basic_line.getStrokeColor())  # 0x112233FF
        result = rgb_int2css(color)
        assert result[0] == "#112233"          # rgb
        assert result[1] == 1                  # a (as fraction)

        color = unwrap(basic_polyline.getStrokeColor())  # 0x11223300
        result = rgb_int2css(color)
        assert result[0] == "#112233"          # rgb
        assert result[1] == 0                  # a (as fraction)
