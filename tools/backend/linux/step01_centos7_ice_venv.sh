#!/usr/bin/env bash

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

set -e -u -x

VENV_SERVER=${VENV_SERVER:-/opt/ode/server/venv3}
#start-ice-py
# Create a virtual env
python3 -mvenv $VENV_SERVER

# Upgrade pip
$VENV_SERVER/bin/pip install --upgrade pip

# Install the Ice Python binding
$VENV_SERVER/bin/pip install https://github.com/bhojpur/ode/releases/download/0.2.1/zeroc_ice-3.7.7-cp37-cp37m-linux_x86_64.whl

# Install server dependencies
$VENV_SERVER/bin/pip install ode-server[default]
#end-ice-py