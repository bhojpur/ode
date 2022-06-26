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

echo "Preparing build. You are going to need npm at a minimum"

#fetch dependencies via npm
echo "Fetching dependencies needed for build..."
npm install

#wipe build directory
echo "Erasing build/deploy directories..."
rm -rf build

#delete plugin directories
rm -rf plugin/dist plugin/ode_viewer.egg-info
rm -rf plugin/ode_viewer/static plugin/ode_viewer/templates
rm -rf plugin/ol3-viewer/static/

#recreate static and templates directories
echo "Recreating build/deploy directories..."
mkdir -p plugin/ode_viewer/static/ode_viewer/css/images
mkdir -p plugin/ode_viewer/templates/ode_viewer
mkdir -p plugin/ol3-viewer/static/ol3-viewer/js
mkdir -p plugin/ol3-viewer/static/ol3-viewer/css

#prepare css (combine and minify)
if [ "$#" -gt 0 ] && [ "$1" = "DEV" ]; then
    ant prepare-css-debug
else
    ant prepare-css-prod
fi