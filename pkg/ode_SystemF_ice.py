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

from sys import version_info as _version_info_
import Ice, IcePy
import ode_RTypes_ice
import Ice_BuiltinSequences_ice

# Included module ode
_M_ode = Ice.openModule('ode')

# Included module Ice
_M_Ice = Ice.openModule('Ice')

# Start of module ode
__name__ = 'ode'

# Start of module ode.sys
_M_ode.sys = Ice.openModule('ode.sys')
__name__ = 'ode.sys'

if '_t_LongList' not in _M_ode.sys.__dict__:
    _M_ode.sys._t_LongList = IcePy.defineSequence('::ode::sys::LongList', (), IcePy._t_long)

if '_t_IntList' not in _M_ode.sys.__dict__:
    _M_ode.sys._t_IntList = IcePy.defineSequence('::ode::sys::IntList', (), IcePy._t_int)

if '_t_CountMap' not in _M_ode.sys.__dict__:
    _M_ode.sys._t_CountMap = IcePy.defineDictionary('::ode::sys::CountMap', (), IcePy._t_long, IcePy._t_long)

if '_t_ParamMap' not in _M_ode.sys.__dict__:
    _M_ode.sys._t_ParamMap = IcePy.defineDictionary('::ode::sys::ParamMap', (), IcePy._t_string, _M_ode._t_RType)

if '_t_IdByteMap' not in _M_ode.sys.__dict__:
    _M_ode.sys._t_IdByteMap = IcePy.defineDictionary('::ode::sys::IdByteMap', (), IcePy._t_long, _M_Ice._t_ByteSeq)

if 'EventContext' not in _M_ode.sys.__dict__:
    _M_ode.sys._t_EventContext = IcePy.declareClass('::ode::sys::EventContext')
    _M_ode.sys._t_EventContextPrx = IcePy.declareProxy('::ode::sys::EventContext')

# End of module ode.sys

__name__ = 'ode'

# End of module ode
