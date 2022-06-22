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
Script used by robot_setup.py to set posX and posY on Well Samples.

Usage:
$ python well_sample_posXY.py host port sessionKey plateId

Used after import of SPW data to provide spatial settings on well
samples. Tested by spw_test.
"""

import argparse
import ode
from ode.gateway import ServerGateway
from ode.model.enums import UnitsLength
parser = argparse.ArgumentParser()
parser.add_argument('host', help='Bhojpur ODE host')
parser.add_argument('port', help='Bhojpur ODE port')
parser.add_argument('key', help='Bhojpur ODE session key')
parser.add_argument('plateId', help='Plate ID to process', type=int)
args = parser.parse_args()
conn = ServerGateway(host=args.host, port=args.port)
conn.connect(args.key)
update = conn.getUpdateService()
plate = conn.getObject('Plate', args.plateId)
r = UnitsLength.REFERENCEFRAME
cols = 3
for well in plate.listChildren():
    for i, ws in enumerate(well.listChildren()):
        x = i % cols
        y = i / cols
        ws = conn.getQueryService().get('WellSample', ws.id)
        ws.posY = ode.model.LengthI(y, r)
        ws.posX = ode.model.LengthI(x, r)
        update.saveObject(ws)
