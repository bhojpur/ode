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

ICEVER=${ICEVER:-ice37}

# Ice installation
if [ "$ICEVER" = "ice37" ]; then
   #start-recommended
    curl -sL https://zeroc.com/download/Ice/3.7/el7/zeroc-ice3.7.repo > \
    /etc/yum.repos.d/zeroc-ice3.7.repo

    yum -y install glacier2 \
                   icebox \
                   icegrid \
                   icepatch2 \
                   libfreeze3.7-c++ \
                   libice3.7-c++ \
                   libicestorm3.7

    #end-recommended
    # reset the locale
    localedef -i en_US -f UTF-8 en_US.UTF-8
elif [ "$ICEVER" = "ice37-devel" ]; then
    curl -sL https://zeroc.com/download/Ice/3.7/el7/zeroc-ice3.7.repo > \
    /etc/yum.repos.d/zeroc-ice3.7.repo

    yum -y install glacier2 \
                   icebox \
                   icegrid \
                   icepatch2 \
                   libfreeze3.7-c++ \
                   libice3.7-c++ \
                   libicestorm3.7 \
                   ice-all-devel

    localedef -i en_US -f UTF-8 en_US.UTF-8
fi