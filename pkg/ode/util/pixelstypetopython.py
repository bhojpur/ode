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

# The Pixels object in Bhojpur ODE, has a member pixelsType, this can be
#    INT_8 = "int8";
#    UINT_8 = "uint8";
#    INT_16 = "int16";
#    UINT_16 = "uint16";
#    INT_32 = "int32";
#    UINT_32 = "uint32";
#    FLOAT = "float";
#    DOUBLE = "double";
# we can convert these to the appropriate types in python.

from ode.model.enums import PixelsTypeint8, PixelsTypeuint8, PixelsTypeint16
from ode.model.enums import PixelsTypeuint16, PixelsTypeint32
from ode.model.enums import PixelsTypeuint32, PixelsTypefloat
from ode.model.enums import PixelsTypedouble

INT_8 = PixelsTypeint8
UINT_8 = PixelsTypeuint8
INT_16 = PixelsTypeint16
UINT_16 = PixelsTypeuint16
INT_32 = PixelsTypeint32
UINT_32 = PixelsTypeuint32
FLOAT = PixelsTypefloat
DOUBLE = PixelsTypedouble

def toPython(pixelType):
    if(pixelType == INT_8):
        return 'b'
    if(pixelType == UINT_8):
        return 'B'
    if(pixelType == INT_16):
        return 'h'
    if(pixelType == UINT_16):
        return 'H'
    if(pixelType == INT_32):
        return 'i'
    if(pixelType == UINT_32):
        return 'I'
    if(pixelType == FLOAT):
        return 'f'
    if(pixelType == DOUBLE):
        return 'd'

def toNumpy(pixelType):
    import numpy
    if(pixelType == INT_8):
        return numpy.int8
    if(pixelType == UINT_8):
        return numpy.uint8
    if(pixelType == INT_16):
        return numpy.int16
    if(pixelType == UINT_16):
        return numpy.uint16
    if(pixelType == INT_32):
        return numpy.int32
    if(pixelType == UINT_32):
        return numpy.uint32
    if(pixelType == FLOAT):
        return numpy.float
    if(pixelType == DOUBLE):
        return numpy.double

def toArray(pixelType):
    if(pixelType == INT_8):
        return 'b'
    if(pixelType == UINT_8):
        return 'B'
    if(pixelType == INT_16):
        return 'i2'
    if(pixelType == UINT_16):
        return 'H2'
    if(pixelType == INT_32):
        return 'i4'
    if(pixelType == UINT_32):
        return 'I4'
    if(pixelType == FLOAT):
        return 'f'
    if(pixelType == DOUBLE):
        return 'd'

def toPIL(pixelType):
    if(pixelType == INT_8):
        return 'L'
    if(pixelType == UINT_8):
        return 'L'
    if(pixelType == INT_16):
        return 'I;16'
    if(pixelType == UINT_16):
        return 'I;16'
    if(pixelType == INT_32):
        return 'I'
    if(pixelType == UINT_32):
        return 'I'
    if(pixelType == FLOAT):
        return 'F'
    if(pixelType == DOUBLE):
        return 'F'