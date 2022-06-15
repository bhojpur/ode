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

# Main Bhojpur ODE installation script

set -e
set -u
set -x

export PATH=/usr/local/bin:/usr/local/opt/python/libexec/bin:$PATH
export ODE_DATA_DIR=${ODE_DATA_DIR:-~/ODE.data}
export ROOT_PASSWORD=${ROOT_PASSWORD:-ode_root_password}
export ICE=${ICE:-3.7}
export HTTPPORT=${HTTPPORT:-8080}
export LANG=${LANG:-en_US.UTF-8}
export LANGUAGE=${LANGUAGE:-en_US:en}

###################################################################
# Bhojpur ODE installation
###################################################################

# Install Bhojpur ODE
ODE_PYTHONPATH=$(brew --prefix ode53)/lib/python
brew install ode53 --with-nginx --with-cpp

export PYTHONPATH=$ODE_PYTHONPATH
VERBOSE=1 brew test ode53

# Install Bhojpur ODE Python dependencies
pip install -r $(brew --prefix ode53)/share/web/requirements-py27-all.txt
cd /usr/local
bash bin/ode_python_deps

# Set additional environment variables
export ICE_CONFIG=$(brew --prefix ode53)/etc/ice.config

# Reinitialize PSQL cluster to fix missing directories on OSX 10.11
rm -rf /usr/local/var/postgres
initdb -E UTF8 /usr/local/var/postgres

# Start PostgreSQL
pg_ctl -D /usr/local/var/postgres -l /usr/local/var/postgres/server.log -w start

# Create user and database
createuser -w -D -R -S db_user
createdb -E UTF8 -O db_user ode_database
psql -h localhost -U db_user -l

# Set database
ode config set ode.db.name ode_database
ode config set ode.db.user db_user
ode config set ode.db.pass db_password

# Run DB script
ode db script --password $ROOT_PASSWORD -f - | psql -h localhost -U db_user ode_database

# Stop PostgreSQL
pg_ctl -D /usr/local/var/postgres -m fast stop

# Set up the data directory
if [ -d "$ODE_DATA_DIR" ]; then
    rm -rf $ODE_DATA_DIR
fi
mkdir -p $ODE_DATA_DIR
ode config set ode.data.dir $ODE_DATA_DIR
