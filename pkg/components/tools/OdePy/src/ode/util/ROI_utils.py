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

# ROIUtils allows the mapping of ode.model.ROIDataTypesI to python types
# and to create ROIDataTypesI from ROIUtil types.
# These methods also implement the acceptVisitor method linking to
# the ROIDrawingCanvas.

from __future__ import division
from builtins import str
from builtins import map
from builtins import range
from past.utils import old_div
from builtins import object
from ode.model.enums import UnitsLength
from ode.model import LengthI
from ode.model import EllipseI
from ode.model import LineI
from ode.model import RectangleI
from ode.model import PointI
from ode.model import PolylineI
from ode.model import PolygonI
from ode.model import MaskI
from ode.rtypes import rdouble, rint, rstring

import warnings
#
# HELPERS
#

def pointsStringToXYlist(string):
    """
    Method for converting the string returned from
    ode.model.ShapeI.getPoints() into list of (x,y) points.
    E.g: "points[309,427, 366,503, 190,491] points1[309,427, 366,503,
    190,491] points2[309,427, 366,503, 190,491]"
    or the new format: "309,427 366,503 190,491"
    """
    warnings.warn(
        "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
    pointLists = string.strip().split("points")
    if len(pointLists) < 2:
        if len(pointLists) == 1 and pointLists[0]:
            xys = pointLists[0].split()
            xyList = [tuple(map(int, xy.split(','))) for xy in xys]
            return xyList

        msg = "Unrecognised ROI shape 'points' string: %s" % string
        raise ValueError(msg)

    firstList = pointLists[1]
    xyList = []
    for xy in firstList.strip(" []").split(", "):
        x, y = xy.split(",")
        xyList.append((int(x.strip()), int(y.strip())))
    return xyList

def xyListToBbox(xyList):
    """
    Returns a bounding box (x,y,w,h) that will contain the shape
    represented by the XY points list
    """
    warnings.warn(
        "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
    xList, yList = [], []
    for xy in xyList:
        x, y = xy
        xList.append(x)
        yList.append(y)
    return (min(xList), min(yList), max(xList)-min(xList),
            max(yList)-min(yList))

#
# Data implementation
#

##
# abstract, defines the method that call it as abstract.
#
#
def abstract():
    import inspect
    caller = inspect.getouterframes(inspect.currentframe())[1][3]
    raise NotImplementedError(caller + ' must be implemented in subclass')

##
# ShapeSettingsData contains all the display information about
# the ROI that aggregates it.
#

class ShapeSettingsData(object):

    ##
    # Initialises the default values of the ShapeSettings.
    # Stroke has default colour of darkGrey
    # StrokeWidth defaults to 1
    #

    def __init__(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.WHITE = 16777215
        self.BLACK = 0
        self.GREY = 11184810
        self.strokeColour = rint(self.GREY)
        self.strokeWidth = LengthI()
        self.strokeWidth.setValue(1)
        self.strokeWidth.setUnit(UnitsLength.POINT)
        self.strokeDashArray = rstring('')
        self.fillColour = rint(self.GREY)
        self.fillRule = rstring('')

    ##
    # Applies the settings in the ShapeSettingsData to the ROITypeI
    # @param shape the ode.model.ROITypeI that these settings will
    #              be applied to
    #
    def setROIShapeSettings(self, shape):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        shape.setStrokeColor(self.strokeColour)
        shape.setStrokeWidth(self.strokeWidth)
        shape.setStrokeDashArray(self.strokeDashArray)
        shape.setFillColor(self.fillColour)
        shape.setFillRule(self.fillRule)

    ##
    # Set the Stroke settings of the ShapeSettings.
    # @param colour The colour of the stroke.
    # @param width The stroke width.
    #
    def setStrokeSettings(self, colour, width=1):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.strokeColour = rint(colour)
        self.strokeWidth = LengthI()
        self.strokeWidth.setValue(width)
        self.strokeWidth.setUnit(UnitsLength.POINT)

    ###
    # Set the Fill Settings for the ShapeSettings.
    # @param colour The fill colour of the shape.
    def setFillSettings(self, colour):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.fillColour = rstring(colour)

    ##
    # Get the stroke settings as the tuple (strokeColour, strokeWidth).
    # @return See above.
    #
    def getStrokeSettings(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return (self.strokeColour.getValue(), self.strokeWidth.getValue())

    ##
    # Get the fill setting as a tuple of (fillColour)
    # @return See above.
    #
    def getFillSettings(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return (self.fillColour.getValue())

    ##
    # Get the tuple ((stokeColor, strokeWidth), (fillColour)).
    # @return see above.
    #
    def getSettings(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return (self.getStrokeSettings(), self.getFillSettings())

    ##
    # Set the current shapeSettings from the ROI roi.
    # @param roi see above.
    #
    def getShapeSettingsFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.strokeColour = roi.getStrokeColor()
        self.strokeWidth = roi.getStrokeWidth()
        self.strokeDashArray = roi.getStrokeDashArray()
        self.fillColour = roi.getFillColor()
        self.fillRule = roi.getFillRule()

##
# This class stores the ROI Coordinate (Z,T).
#

class ROICoordinate(object):

    ##
    # Initialise the ROICoordinate.
    # @param z The z-section.
    # @param t The timepoint.

    def __init__(self, z=0, t=0):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.theZ = rint(z)
        self.theT = rint(t)

    ##
    # Set the (z, t) for the roi using the (z, t) of the ROICoordinate.
    # @param roi The ROI to set the (z, t) on.
    #
    def setROICoord(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        roi.setTheZ(self.theZ)
        roi.setTheT(self.theT)

    ##
    # Get the (z, t) from the ROI.
    # @param See above.
    #
    def setCoordFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.theZ = roi.getTheZ()
        self.theT = roi.getTheT()

##
# Interface to inherit for accepting ROIDrawing as a visitor.
# @param visitor The ROIDrawingCompoent.
#

class ROIDrawingI(object):

    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        abstract()

##
# The base class for all ROIShapeData objects.
#

class ShapeData(object):

    ##
    # Constructor sets up the coord, shapeSettings and ROI objects.
    #

    def __init__(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.coord = ROICoordinate()
        self.shapeSettings = ShapeSettingsData()
        self.ROI = None

    ##
    # Set the coord of the class to coord.
    # @param See above.
    #
    def setCoord(self, coord):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.coord = coord

    ##
    # Set the ROICoordinate of the roi.
    # @param roi See above.
    #
    def setROICoord(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.coord.setROICoord(roi)

    ##
    # Set the Geometry of the roi from the geometry in ShapeData.
    # @param roi See above.
    #
    def setROIGeometry(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        abstract()

    ##
    # Set the Settings of the ShapeDate form the settings object.
    # @param settings See above.
    #
    def setShapeSettings(self, settings):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.shapeSettings = settings

    ##
    # Set the Settings of the roi from the setting in ShapeData.
    # @param roi See above.
    #
    def setROIShapeSettings(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.shapeSettings.setROIShapeSettings(roi)

    ##
    # Accept visitor.
    # @param visitor See above.
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        abstract()

    ##
    # Create the base type of ROI for this shape.
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        abstract()

    ##
    # Get the roi from the ShapeData. If the roi already exists return it.
    # Otherwise create it from the ShapeData and return it.
    # @return See above.
    #
    def getROI(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        if(self.roi is not None):
            return self.roi
        self.roi = self.createBaseType()
        self.setROICoord(self.roi)
        self.setROIGeometry(self.roi)
        self.setROIShapeSettings(self.roi)
        return self.roi

    ##
    # Set the shape settings object from the roi.
    # @param roi see above.
    #
    def getShapeSettingsFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.shapeSettings.getShapeSettingsFromROI(roi)

    ##
    # Set the ROICoordinate from the roi.
    # @param roi See above.
    #
    def getCoordFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.coord.setCoordFromROI(roi)

    ##
    # Set the Geometr from the roi.
    # @param roi See above.
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        abstract()

    ##
    # Get all settings from the roi, Geomerty, Shapesettins, ROICoordinate.
    # @param roi See above.
    #
    def fromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.roi = roi
        self.getShapeSettingsFromROI(roi)
        self.getCoordFromROI(roi)
        self.getGeometryFromROI(roi)

##
# The EllispeData class contains all the manipulation and create of EllipseI
# types.
# It also accepts the ROIDrawingUtils visitor for drawing ellipses.
#

class EllipseData(ShapeData, ROIDrawingI):

    ##
    # Constructor for EllipseData object.
    # @param roicoord The ROICoordinate of the object (default: 0,0)
    # @param x The centre x coordinate of the ellipse.
    # @param y The centre y coordinate of the ellipse.
    # @param radiusX The major axis of the ellipse.
    # @param radiusY The minor axis of the ellipse.

    def __init__(self, roicoord=ROICoordinate(), x=0, y=0, radiusX=0,
                 radiusY=0):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ShapeData.__init__(self)
        self.x = rdouble(x)
        self.y = rdouble(y)
        self.radiusX = rdouble(radiusX)
        self.radiusY = rdouble(radiusY)
        self.setCoord(roicoord)

    ##
    # overridden, @See ShapeData#setROIGeometry
    #
    def setROIGeometry(self, ellipse):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ellipse.setTheZ(self.coord.theZ)
        ellipse.setTheT(self.coord.theT)
        ellipse.setX(self.x)
        ellipse.setY(self.y)
        ellipse.setRadiusX(self.radiusX)
        ellipse.setRadiusY(self.radiusY)

    ##
    # overridden, @See ShapeData#getGeometryFromROI
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.x = roi.getX()
        self.y = roi.getY()
        self.radiusX = roi.getRadiusX()
        self.radiusY = roi.getRadiusY()

    ##
    # overridden, @See ShapeData#createBaseType
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return EllipseI()

    ##
    # overridden, @See ShapeData#acceptVisitor
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        visitor.drawEllipse(
            self.x.getValue(), self.y.getValue(), self.radiusX.getValue(),
            self.radiusY.getValue(), self.shapeSettings.getSettings())

##
# The RectangleData class contains all the manipulation and creation of
# RectangleI types.
# It also accepts the ROIDrawingUtils visitor for drawing rectangles.
#

class RectangleData(ShapeData, ROIDrawingI):

    ##
    # Constructor for RectangleData object.
    # @param roicoord The ROICoordinate of the object (default: 0,0)
    # @param x The top left x - coordinate of the shape.
    # @param y The top left y - coordinate of the shape.
    # @param width The width of the shape.
    # @param height The height of the shape.

    def __init__(self, roicoord=ROICoordinate(), x=0, y=0, width=0, height=0):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ShapeData.__init__(self)
        self.x = rdouble(x)
        self.y = rdouble(y)
        self.width = rdouble(width)
        self.height = rdouble(height)
        self.setCoord(roicoord)

    ##
    # overridden, @See ShapeData#setGeometry
    #
    def setGeometry(self, rectangle):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        rectangle.setTheZ(self.coord.theZ)
        rectangle.setTheT(self.coord.theT)
        rectangle.setX(self.x)
        rectangle.setY(self.y)
        rectangle.setWidth(self.width)
        rectangle.setHeight(self.height)

    ##
    # overridden, @See ShapeData#getGeometryFromROI
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.x = roi.getX()
        self.y = roi.getY()
        self.width = roi.getWidth()
        self.height = roi.getHeight()

    ##
    # overridden, @See ShapeData#createBaseType
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return RectangleI()

    ##
    # overridden, @See ShapeData#acceptVisitor
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        visitor.drawRectangle(
            self.x, self.y, self.width, self.height,
            self.shapeSettings.getSettings())
##
# The LineData class contains all the manipulation and create of LineI
# types.
# It also accepts the ROIDrawingUtils visitor for drawing lines.
#

class LineData(ShapeData, ROIDrawingI):

    ##
    # Constructor for LineData object.
    # @param roicoord The ROICoordinate of the object (default: 0,0)
    # @param x1 The first x coordinate of the shape.
    # @param y1 The first y coordinate of the shape.
    # @param x2 The second x  coordinate of the shape.
    # @param y2 The second y coordinate of the shape.

    def __init__(self, roicoord=ROICoordinate(), x1=0, y1=0, x2=0, y2=0):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ShapeData.__init__(self)
        self.x1 = rdouble(x1)
        self.y1 = rdouble(y1)
        self.x2 = rdouble(x2)
        self.y2 = rdouble(y2)
        self.setCoord(roicoord)

    ##
    # overridden, @See ShapeData#setGeometry
    #
    def setGeometry(self, line):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        line.setTheZ(self.coord.theZ)
        line.setTheT(self.coord.theT)
        line.setX1(self.x1)
        line.setY1(self.y1)
        line.setX2(self.x2)
        line.setY2(self.y2)

    ##
    # overridden, @See ShapeData#getGeometryFromROI
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.x1 = roi.getX1()
        self.y1 = roi.getY1()
        self.x2 = roi.getX2()
        self.y2 = roi.getY2()

    ##
    # overridden, @See ShapeData#createBaseType
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return LineI()

    ##
    # overridden, @See ShapeData#acceptVisitor
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        visitor.drawLine(
            self.x1.getValue(), self.y1.getValue(), self.x2.getValue(),
            self.y2.getValue(), self.shapeSettings.getSettings())

##
# The MaskData class contains all the manipulation and create of MaskI
# types.
# It also accepts the ROIDrawingUtils visitor for drawing masks.
#

class MaskData(ShapeData, ROIDrawingI):

    ##
    # Constructor for MaskData object.
    # @param roicoord The ROICoordinate of the object (default: 0,0)
    # @param bytes The mask data.
    # @param x The top left x - coordinate of the shape.
    # @param y The top left y - coordinate of the shape.
    # @param width The width of the shape.
    # @param height The height of the shape.

    def __init__(self, roicoord=ROICoordinate(), bytes=None,
                 x=0, y=0, width=0, height=0):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ShapeData.__init__(self)
        self.x = rdouble(x)
        self.y = rdouble(y)
        self.width = rdouble(width)
        self.height = rdouble(height)
        self.bytesdata = bytes
        self.setCoord(roicoord)

    ##
    # overridden, @See ShapeData#setGeometry
    #
    def setGeometry(self, mask):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        mask.setTheZ(self.coord.theZ)
        mask.setTheT(self.coord.theT)
        mask.setX(self.x)
        mask.setY(self.y)
        mask.setWidth(self.width)
        mask.setHeight(self.height)
        mask.setBytes(self.bytedata)

    ##
    # overridden, @See ShapeData#getGeometryFromROI
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.x = roi.getX()
        self.y = roi.getY()
        self.width = roi.getWidth()
        self.height = roi.getHeight()
        self.bytesdata = roi.getBytes()

    ##
    # overridden, @See ShapeData#createBaseType
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return MaskI()

    ##
    # overridden, @See ShapeData#acceptVisitor
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        visitor.drawMask(
            self.x.getValue(), self.y.getValue(),
            self.width.getValue(), self.height.getValue(),
            self.bytesdata, self.shapeSettings.getSettings())

##
# The PointData class contains all the manipulation and create of PointI
# types.
# It also accepts the ROIDrawingUtils visitor for drawing points.
#

class PointData(ShapeData, ROIDrawingI):

    ##
    # Constructor for PointData object.
    # @param roicoord The ROICoordinate of the object (default: 0,0)
    # @param x The x coordinate of the shape.
    # @param y The y coordinate of the shape.

    def __init__(self, roicoord=ROICoordinate(), x=0, y=0):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ShapeData.__init__(self)
        self.x = rdouble(x)
        self.y = rdouble(y)
        self.setCoord(roicoord)

    ##
    # overridden, @See ShapeData#setGeometry
    #
    def setGeometry(self, point):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        point.setTheZ(self.coord.theZ)
        point.setTheT(self.coord.theT)
        point.setX(self.x)
        point.setY(self.y)

    ##
    # overridden, @See ShapeData#getGeometryFromROI
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.x = roi.getX()
        self.y = roi.getY()

    ##
    # overridden, @See ShapeData#createBaseType
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return PointI()

    ##
    # overridden, @See ShapeData#acceptVisitor
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        visitor.drawEllipse(
            self.x.getValue(), self.y.getValue(), 3, 3,
            self.shapeSettings.getSettings())

##
# The PolygonData class contains all the manipulation and create of PolygonI
# types.
# It also accepts the ROIDrawingUtils visitor for drawing polygons.
#

class PolygonData(ShapeData, ROIDrawingI):

    ##
    # Constructor for PolygonData object.
    # @param roicoord The ROICoordinate of the object (default: 0,0)
    # @param pointList The list of points that make up the polygon,
    #                  as pairs [x1, y1, x2, y2 ..].

    def __init__(self, roicoord=ROICoordinate(), pointsList=(0, 0)):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ShapeData.__init__(self)
        self.points = rstring(self.listToString(pointsList))
        self.setCoord(roicoord)

    ##
    # overridden, @See ShapeData#setGeometry
    #
    def setGeometry(self, polygon):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        polygon.setTheZ(self.coord.theZ)
        polygon.setTheT(self.coord.theT)
        polygon.setPoints(self.points)

    ##
    # overridden, @See ShapeData#getGeometryFromROI
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.points = roi.getPoints()

    ##
    # Convert a pointsList[x1,y1,x2,y2..] to a string.
    # @param pointsList The list of points to convert.
    # @return The pointsList converted to a string.
    def listToString(self, pointsList):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        string = ''
        cnt = 0
        for element in pointsList:
            if(cnt != 0):
                string = string + ','
            cnt += 1
            string = string + str(element)
        return string

    ##
    # Convert a string of points to a tuple list [(x1,y1),(x2,y2)..].
    # @param pointString The string to convert.
    # @return The tuple list converted from a string.
    def stringToTupleList(self, pointString):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        elements = []
        list = pointString.split(',')
        numTokens = len(list)
        for tokenPair in range(0, old_div(numTokens, 2)):
            elements.append(
                (int(list[tokenPair * 2]), int(list[tokenPair * 2 + 1])))
        return elements

    ##
    # overridden, @See ShapeData#createBaseType
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return PolygonI()

    ##
    # overridden, @See ShapeData#acceptVisitor
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        visitor.drawPolygon(self.stringToTupleList(
            self.points.getValue()), self.shapeSettings.getSettings())

##
# The PolylineData class contains all the manipulation and create of PolylineI
# types.
# It also accepts the ROIDrawingUtils visitor for drawing polylines.
#

class PolylineData(ShapeData, ROIDrawingI):

    ##
    # Constructor for PolylineData object.
    # @param roicoord The ROICoordinate of the object (default: 0,0)
    # @param pointList The list of points that make up the polygon,
    #                  as pairs [x1, y1, x2, y2 ..].

    def __init__(self, roicoord=ROICoordinate(), pointsList=(0, 0)):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        ShapeData.__init__(self)
        self.points = rstring(self.listToString(pointsList))
        self.setCoord(roicoord)

    ##
    # overridden, @See ShapeData#setGeometry
    #
    def setGeometry(self, point):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        point.setTheZ(self.coord.theZ)
        point.setTheT(self.coord.theT)
        point.setPoints(self.points)

    ##
    # overridden, @See ShapeData#getGeometryFromROI
    #
    def getGeometryFromROI(self, roi):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        self.points = roi.getPoints()

    ##
    # Convert a pointsList[x1,y1,x2,y2..] to a string.
    # @param pointsList The list of points to convert.
    # @return The pointsList converted to a string.
    def listToString(self, pointsList):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        string = ''
        cnt = 0
        for element in pointsList:
            if(cnt > 0):
                string = string + ','
            string = string + str(element)
            cnt += 1
        return string

    ##
    # Convert a string of points to a tuple list [(x1,y1),(x2,y2)..].
    # @param pointString The string to convert.
    # @return The tuple list converted from a string.
    def stringToTupleList(self, pointString):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        elements = []
        list = pointString.split(',')
        numTokens = len(list)
        for tokenPair in range(0, old_div(numTokens, 2)):
            elements.append(
                (int(list[tokenPair * 2]), int(list[tokenPair * 2 + 1])))
        return elements

    ##
    # overridden, @See ShapeData#createBaseType
    #
    def createBaseType(self):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        return PolylineI()

    ##
    # overridden, @See ShapeData#acceptVisitor
    #
    def acceptVisitor(self, visitor):
        warnings.warn(
            "This module is deprecated as of Bhojpur ODE", DeprecationWarning)
        visitor.drawPolyline(self.stringToTupleList(
            self.points.getValue()), self.shapeSettings.getSettings())