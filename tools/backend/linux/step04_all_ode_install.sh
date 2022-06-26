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

ODEVER=${ODEVER:-latest}
ICEVER=${ICEVER:-ice37}

set -eux

. `dirname $0`/settings.env


#start-install
if [ "$ICEVER" = "ice37" ] || [ "$ICEVER" = "ice37-devel" ]; then
	if [ $ODEVER == "latest" ]; then
		#start-release-ice37
		cd /opt/ode/server
		SERVER=https://downloads.bhojpur.net/ode/5.6/server-ice37.zip
		wget -q $SERVER -O ODE.server-ice37.zip
		unzip -q ODE.server*
		#end-release-ice37
		rm ODE.server-ice37.zip
	fi
fi

#start-link
# change ownership of the folder
chown -R ode-server ODE.server-*
ln -s ODE.server-*/ ODE.server
#end-link