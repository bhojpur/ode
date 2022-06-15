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

JAVAVER=${JAVAVER:-openjdk11}

# install java
if [ "$JAVAVER" = "openjdk1.8" ]; then
    apt-get -y install software-properties-common
    add-apt-repository -y ppa:openjdk-r/ppa
    apt-get update
    apt-get -y install openjdk-8-jre
elif [ "$JAVAVER" = "openjdk1.8-devel" ]; then
    apt-get -y install software-properties-common
    add-apt-repository -y ppa:openjdk-r/ppa
    apt-get update
    apt-get -y install openjdk-8-jdk
elif [ "$JAVAVER" = "openjdk11-devel" ]; then
    apt-get -y install software-properties-common
    add-apt-repository ppa:openjdk-r/ppa
    apt-get update -q
    apt-get install -y openjdk-11-jdk
elif [ "$JAVAVER" = "openjdk11" ]; then
    #start-recommended
    apt-get -y install software-properties-common
    add-apt-repository ppa:openjdk-r/ppa
    apt-get update -q
    apt-get install -y openjdk-11-jre
    #end-recommended
fi