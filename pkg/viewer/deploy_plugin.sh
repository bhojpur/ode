#!/bin/bash

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

#copy over css and js
echo "Deploying built resources to plugin directory..."
rm -rf plugin/ol3-viewer/static
mkdir -p plugin/ol3-viewer/static/ol3-viewer/js
mkdir -p plugin/ol3-viewer/static/ol3-viewer/css
mkdir -p plugin/ol3-viewer/static/ol3-viewer/fonts

# Generated from webpack.plugin.config.js
cp build/ol-viewer.js plugin/ol3-viewer/static/ol3-viewer/js

# Copy from source (not minified css)
cp -r css/* plugin/ol3-viewer/static/ol3-viewer/css

# Copy bootstrap css and fonts (for button glyphicons etc)
cp ./node_modules/bootstrap/dist/css/bootstrap.min.css plugin/ol3-viewer/static/ol3-viewer/css
cp -r ./node_modules/bootstrap/dist/fonts/* plugin/ol3-viewer/static/ol3-viewer/fonts