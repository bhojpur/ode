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

# Start up server, perform some simple test, shutdown again.

set -e
set -u
set -x

export PATH=/usr/local/bin:/usr/local/opt/python/libexec/bin:$PATH
export HTTPPORT=${HTTPPORT:-8080}
export ROOT_PASSWORD=${ROOT_PASSWORD:-ode_root_password}

# Start PostgreSQL
pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log -w start

# Start the server
ode admin start


if [ -f $(brew --prefix ode53)/var/log/ODE-0.log ]; then
    d=10;
    while ! grep "ODE.server now accepting connections" $(brew --prefix ode53)/var/log/ODE-0.log ;
        do
            sleep 10;
            d=$[$d -1];
            if [ $d -lt 0 ]; then
                exit 1;
            fi;
        done
else
    echo "File not found!"
    exit 1
fi

# Start Bhojpur ODE web engine
ode web start
nginx -c $(brew --prefix ode53)/etc/nginx.conf

# Check Bhojpur ODE version
ode version | grep -v UNKNOWN

# Test simple fake import
ode login -s localhost -u root -w $ROOT_PASSWORD
touch test.fake
ode import test.fake
ode logout

# Test simple Web connection
WEB_HOST="localhost:${HTTPPORT}" ODE_ROOT_PASS=$ROOT_PASSWORD bash ../linux/test/test_login_to_web.sh

# Stop Bhojpur ODE web engine
nginx -c $(brew --prefix ode53)/etc/nginx.conf -s stop
ode web stop

# Stop the server
ode admin stop

# Stop PostgreSQL
pg_ctl -D /usr/local/var/postgres -m fast stop