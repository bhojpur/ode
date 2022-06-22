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

import ode
import ode.gateway

class TestHelperObjects(object):
    def testColorHolder(self):
        ColorHolder = ode.gateway.ColorHolder
        c1 = ColorHolder()
        assert c1._color == {'red': 0, 'green': 0, 'blue': 0, 'alpha': 255}
        c1 = ColorHolder('blue')
        assert c1.getHtml() == '0000FF'
        assert c1.getCss() == 'rgba(0,0,255,1.000)'
        assert c1.getRGB() == (0, 0, 255)
        c1.setRed(0xF0)
        assert c1.getCss() == 'rgba(240,0,255,1.000)'
        c1.setGreen(0x0F)
        assert c1.getCss() == 'rgba(240,15,255,1.000)'
        c1.setBlue(0)
        assert c1.getCss() == 'rgba(240,15,0,1.000)'
        c1.setAlpha(0x7F)
        assert c1.getCss() == 'rgba(240,15,0,0.498)'
        c1 = ColorHolder.fromRGBA(50, 100, 200, 300)
        assert c1.getCss() == 'rgba(50,100,200,1.000)'

    def testOdeType(self):
        ode_type = ode.gateway.ode_type
        assert isinstance(ode_type('rstring'), ode.RString)
        assert isinstance(ode_type(u'rstring'), ode.RString)
        assert isinstance(ode_type(1), ode.RInt)
        assert isinstance(ode_type(1), ode.RLong)
        assert isinstance(ode_type(False), ode.RBool)
        assert isinstance(ode_type(True), ode.RBool)
        assert not isinstance(ode_type((1, 2, 'a')), ode.RType)

    def testSplitHTMLColor(self):
        splitHTMLColor = ode.gateway.splitHTMLColor
        assert splitHTMLColor('abc') == [0xAA, 0xBB, 0xCC, 0xFF]
        assert splitHTMLColor('abcd') == [0xAA, 0xBB, 0xCC, 0xDD]
        assert splitHTMLColor('abbccd') == [0xAB, 0xBC, 0xCD, 0xFF]
        assert splitHTMLColor('abbccdde') == [0xAB, 0xBC, 0xCD, 0xDE]
        assert splitHTMLColor('#$%&%') is None
